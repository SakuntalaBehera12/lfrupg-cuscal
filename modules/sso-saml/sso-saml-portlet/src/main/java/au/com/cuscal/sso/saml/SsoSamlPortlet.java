package au.com.cuscal.sso.saml;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;
import au.com.cuscal.sso.saml.token.provider.ISamlProvider;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.portlet.LiferayPortlet;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

/**
 * Portlet implementation class SsoSamlPortlet
 */
public class SsoSamlPortlet extends LiferayPortlet {

	public void doConfig(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		include(configJSP, renderRequest, renderResponse);
	}

	public void doEdit(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		logger.info("doEdit - start");

		try {
			Properties properties = loadProperties(
				renderRequest, renderResponse);

			updatePortletTitle(renderResponse, properties);
			String portletPropertyFile = getConfigurationFile(renderRequest);

			logger.info("doEdit - propertyFile=" + portletPropertyFile);

			renderRequest.setAttribute("propertyFile", portletPropertyFile);
		}
		catch (SystemException e) {
			e.printStackTrace();
		}

		include(editJSP, renderRequest, renderResponse);
		logger.info("doEdit - end");
	}

	public void doEditDefaults(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		logger.info("doEditDefaults - start");

		if (renderRequest.getPreferences() == null) {
			super.doEdit(renderRequest, renderResponse);
		}
		else {
			include(editDefaultsJSP, renderRequest, renderResponse);
		}

		logger.info("doEditDefaults - end");
	}

	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		logger.info("doView - start");

		Boolean hasRole = Boolean.FALSE;

		try {
			Properties properties = loadProperties(
				renderRequest, renderResponse);

			updatePortletTitle(renderResponse, properties);

			String userName = ParamUtil.getString(
				renderRequest, "username", StringPool.BLANK);
			User user = null;

			if (!userName.isEmpty()) {
				ThemeDisplay themeDisplay =
					(ThemeDisplay)renderRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				try {
					user = UserLocalServiceUtil.getUserByScreenName(
						themeDisplay.getCompanyId(), userName.trim());
				}
				catch (Exception e) {
					String userNotFoundMessage = properties.getProperty(
						"call.direct.user.not.found");

					userNotFoundMessage = userNotFoundMessage.replace(
						"[username]", userName);
					renderRequest.setAttribute(
						"errorMessage", userNotFoundMessage);
					hasRole = Boolean.TRUE;
				}
			}
			else {
				String checkingForRole = properties.getProperty(
					"call.direct.role");
				user = getUserFromRequest(renderRequest);

				if (null != user) {
					List<Role> userRoleList = getAllUserRoles(user);

					for (Role role : userRoleList) {
						if ((role.getName() != null) &&
							role.getName(
							).equalsIgnoreCase(
								checkingForRole
							)) {

							hasRole = Boolean.TRUE;

							break;
						}
					}
				}
			}

			if (hasRole) {
				String calldirectPreamble = properties.getProperty(
					"call.direct.preamble");

				renderRequest.setAttribute("preamble", calldirectPreamble);
				renderRequest.setAttribute("status", "success");
				renderRequest.setAttribute(
					"username",
					userName.isEmpty() ? user.getScreenName() : userName);
				include(callDirectViewJsp, renderRequest, renderResponse);

				return;
			}

			String samlTokenFieldname = properties.getProperty(
				"form.token.fieldname");
			String samlUrl = properties.getProperty("form.url");
			String preamble = properties.getProperty("form.preamble");
			Boolean autoSubmit = "false".equalsIgnoreCase(
				properties.getProperty("form.autosubmit")) ? Boolean.FALSE :
					Boolean.TRUE;	// default is to autosubmit
			String errorMessage = properties.getProperty(
				"form.token.failure.message");

			renderRequest.setAttribute("autoSubmit", autoSubmit);
			renderRequest.setAttribute("errorMessage", errorMessage);
			renderRequest.setAttribute("preamble", preamble);
			renderRequest.setAttribute("status", "success");
			renderRequest.setAttribute("tokenfieldname", samlTokenFieldname);
			renderRequest.setAttribute("url", samlUrl);

			ISamlProvider samlProvider = newSamlProviderInstance(properties);

			String samlToken = samlProvider.buildSamlToken(
				properties, user, renderRequest, renderResponse);

			renderRequest.setAttribute("token", samlToken);
		}
		catch (Exception e) {
			renderRequest.setAttribute("status", "failed");
			logger.error(
				"doView - unable to generate SAML token: " + e.getMessage(), e);
		}

		include(viewJSP, renderRequest, renderResponse);
		logger.info("doView - end");
	}

	/**
	 * Find Regular Roles assigned to an organisation
	 * (Not to be confused with organisation roles)
	 *
	 * @param user
	 * @return List<Role>
	 */
	public List<Role> findRegularRolesAssignedToUsersOrganisationList(
		User user) {

		List<Role> finalRolesLst = new ArrayList<>();
		List<Role> rolesLst = null;

		try {
			List<Organization> orgs = user.getOrganizations();

			for (Organization org : orgs) {
				rolesLst = new ArrayList<>();
				long groupId = org.getGroup(
				).getGroupId();

				rolesLst = RoleLocalServiceUtil.getGroupRoles(groupId);

				for (Role role : rolesLst) {
					logger.debug(
						"findRegularRolesAssignedToUsersOrganisationList - Org roles for user:" +
							role.getName());
					finalRolesLst.add(role);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return finalRolesLst;
	}

	/**
	 * Find user group role list
	 *
	 * @param user
	 * @return List<Role>
	 */
	public List<Role> findUserGroupRoleList(User user) {
		List<Role> finalRolesLst = new ArrayList<>();
		List<Role> rolesLst = null;

		try {
			List<UserGroup> userGrps = user.getUserGroups();

			for (UserGroup userGroup : userGrps) {
				rolesLst = new ArrayList<>();
				long groupId = userGroup.getGroup(
				).getGroupId();

				rolesLst = RoleLocalServiceUtil.getGroupRoles(groupId);

				for (Role role : rolesLst) {
					finalRolesLst.add(role);
					logger.debug(
						"findUserGroupRoleList - User Group roles for user:" +
							role.getName());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return finalRolesLst;
	}

	public List<Role> getAllUserRoles(User user) {
		List<Role> allUserRoles = new ArrayList<>();

		if (null != user) {
			Long userId = user.getUserId();
			List<Role> userGroupRoles = findUserGroupRoleList(user);
			List<Role> organisationRoles =
				findRegularRolesAssignedToUsersOrganisationList(user);
			List<Role> roles = new ArrayList<>();

			try {
				roles = RoleLocalServiceUtil.getUserRoles(userId);
			}
			catch (SystemException e) {
				e.printStackTrace();
			}

			allUserRoles.addAll(userGroupRoles);
			allUserRoles.addAll(organisationRoles);
			allUserRoles.addAll(roles);
		}

		return allUserRoles;
	}

	public String getConfigurationFile(RenderRequest renderRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String portletId = themeDisplay.getPortletDisplay(
		).getId();

		PortletPreferences config =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				themeDisplay.getLayout(), portletId);

		String portletPropertyFile = config.getValue("propertyFile", "");

		return portletPropertyFile;
	}

	public User getUserFromRequest(PortletRequest request) {
		User user = null;

		try {
			user = PortalUtil.getUser(request);
		}
		catch (PortalException e) {
			e.printStackTrace();
		}
		catch (SystemException e) {
			e.printStackTrace();
		}

		return user;
	}

	public void init() {
		configJSP = getInitParameter("config-jsp");
		editDefaultsJSP = getInitParameter("edit-defaults-jsp");
		editJSP = getInitParameter("edit-jsp");
		viewJSP = getInitParameter("view-jsp");
		callDirectViewJsp = getInitParameter("calldirect-view-jsp");
		computershareJSP = getInitParameter("computershare-jsp");
	}

	public Properties loadProperties(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		Properties properties = new Properties();

		try {
			String portletPropertyFile = getConfigurationFile(renderRequest);

			if (Validator.isNull(portletPropertyFile)) {
				portletPropertyFile = _SSO_SAML_FINVIEW_SAML_PROPERTIES;
			}

			logger.info(
				"loadProperties - attempting load " + portletPropertyFile +
					" via classloader.getResourceAsStream(...)");
			InputStream inStream = CuscalSharedPropsUtil.getResourceStream(
				getClass().getClassLoader(), portletPropertyFile);

			if (inStream == null) {
				logger.info(
					"loadProperties - attempting load " + portletPropertyFile +
						" direct load via new File(..)");
				File file = new File(portletPropertyFile);

				inStream = new FileInputStream(file);
			}

			properties.load(inStream);
			logger.info("loadProperties - properties: " + properties);
		}
		catch (Exception e) {
			logger.error(
				"loadProperties - error loading properties: " + e.getMessage(),
				e);
		}

		return properties;
	}

	public ISamlProvider newSamlProviderInstance(Properties properties)
		throws Exception {

		logger.info("newSamlProviderInstance - start");

		String propertyName = "providerClass";

		String samlProviderClassname = properties.getProperty(propertyName);

		if (samlProviderClassname == null)

			return null;

		@SuppressWarnings("unchecked")
		Class<ISamlProvider> samlProviderClass =
			(Class<ISamlProvider>)Class.forName(samlProviderClassname);

		ISamlProvider samlProvider = samlProviderClass.newInstance();

		logger.info("newSamlProviderInstance - end");

		return samlProvider;
	}

	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

		logger.info("processAction - start");

		String action = actionRequest.getParameter("action");

		try {
			if (action != null)

				if ("save".equalsIgnoreCase(action)) {
					String propertyFile = actionRequest.getParameter(
						"propertyFile");

					PortletPreferences prefs = actionRequest.getPreferences();

					prefs.setValue("propertyFile", propertyFile);
					prefs.store();
				}
		}
		finally {
			super.processAction(actionRequest, actionResponse);

			logger.info("processAction - end");
		}
	}

	public void updatePortletTitle(
		RenderResponse renderResponse, Properties properties) {

		String title = properties.getProperty("portlet.title", "");

		renderResponse.setTitle(title);
		logger.debug("updatePortletTitle - title=" + title);
	}

	protected void include(
			String path, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws IOException, PortletException {

		PortletRequestDispatcher portletRequestDispatcher =
			getPortletContext().getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			logger.error(path + " is not a valid include");
		}
		else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	protected String callDirectViewJsp;
	protected String computershareJSP;
	protected String configJSP;
	protected String editDefaultsJSP;
	protected String editJSP;
	protected String viewJSP;

	private static final String _SSO_SAML_FINVIEW_SAML_PROPERTIES =
		"/sso-saml/finview/saml.properties";

	private static final Logger logger = Logger.getLogger(SsoSamlPortlet.class);

}