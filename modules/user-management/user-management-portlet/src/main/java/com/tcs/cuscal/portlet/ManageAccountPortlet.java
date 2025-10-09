package com.tcs.cuscal.portlet;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;
import au.com.cuscal.connect.util.resource.dxp.UserUtilImpl;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.webservices.client.EntityService;
import au.com.cuscal.framework.webservices.client.impl.EntityServiceImpl;
import au.com.cuscal.framework.webservices.liferay.dxp.LiferayClientUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.portlet.LiferayPortlet;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import com.tcs.cuscal.dao.LDAPUtil;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageAccountPortlet extends LiferayPortlet {

	public ManageAccountPortlet() {
	}

	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		try {
			logger.debug(
				ManageAccountPortlet.class.toString() + " - doView() - Start.");
			String strUserId = renderRequest.getUserPrincipal(
			).getName();

			logger.debug(
				ManageAccountPortlet.class.toString() +
					" - doView() - User = " + strUserId);
			User user = UserLocalServiceUtil.getUser(
				Long.parseLong(
					renderRequest.getUserPrincipal(
					).getName()));

			if (user != null) {
				String screenName = user.getScreenName();
				String firstName = user.getFirstName();
				String lastName = user.getLastName();
				String email = user.getEmailAddress();
				UserUtilImpl userUtil = new UserUtilImpl();

				String phoneNo = userUtil.retrieveBusinessPhoneNumberForUser(
					user);
				String clientName = "";
				List<Organization> organisations = user.getOrganizations();

				if ((organisations != null) && (organisations.size() > 0)) {
					Iterator var13 = organisations.iterator();

					label47:
					while (true) {
						while (true) {
							if (!var13.hasNext()) {
								break label47;
							}

							Organization org = (Organization)var13.next();

							if ((clientName != null) &&
								(clientName.length() > 0)) {

								clientName = clientName + ", " + org.getName();
							}
							else {
								clientName = org.getName();
							}
						}
					}
				}

				List<Role> allUserRoles = this.getUserRoles(user);

				logger.debug(
					"[doView] All User roles size: " + allUserRoles.size());
				Iterator var20 = allUserRoles.iterator();

				while (var20.hasNext()) {
					Role userRole = (Role)var20.next();

					logger.debug(
						"[doView] user Role is: " + userRole.getName());
				}

				renderRequest.setAttribute("clientName", clientName);
				renderRequest.setAttribute("email", email);
				renderRequest.setAttribute("firstName", firstName);
				renderRequest.setAttribute("lastName", lastName);
				renderRequest.setAttribute("phoneNo", phoneNo);
				renderRequest.setAttribute("screenName", screenName);
			}
			else {
				logger.debug(
					ManageAccountPortlet.class.toString() +
						" - doView() - Could not retrieve user from database. (User ID = " +
							strUserId + ")");
			}
		}
		catch (NumberFormatException var16) {
			logger.error(
				ManageAccountPortlet.class.toString() +
					" - doView() - Exception:",
				var16);
		}
		catch (PortalException var17) {
			logger.error(
				ManageAccountPortlet.class.toString() +
					" - doView() - Exception:",
				var17);
		}
		catch (SystemException var18) {
			logger.error(
				ManageAccountPortlet.class.toString() +
					" - doView() - Exception:",
				var18);
		}

		this.include(manageYourAccountJSP, renderRequest, renderResponse);
		logger.debug(
			ManageAccountPortlet.class.toString() + " - doView() - End.");
	}

	public void init() {
		manageYourAccountJSP = this.getInitParameter("manageYourAccount-jsp");
		logger.info(
			ManageAccountPortlet.class.toString() +
				" - init() - LDAP Connection setting: " +
					LDAPUtil.getInstance(
					).toString());

		try {
			this.entityService = new EntityServiceImpl();
			this.entityService.loadProperties();
		}
		catch (IOException var2) {
			logger.error(
				"Unable to load webservice.properties file: " +
					var2.getMessage(),
				var2);
		}
	}

	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - processAction() - Start.");
		String hiddenformName = actionRequest.getParameter("formName");
		actionRequest.setAttribute("errorMsg", Boolean.FALSE);
		actionRequest.setAttribute("noMatchPassword", Boolean.FALSE);
		actionRequest.setAttribute("invalidPassword", Boolean.FALSE);
		actionRequest.setAttribute("verifyPassFailed", Boolean.FALSE);
		actionRequest.setAttribute("passUpdateSuccessMsg", Boolean.FALSE);
		actionRequest.setAttribute("answUpdateSuccessMsg", Boolean.FALSE);
		actionRequest.setAttribute("answUpdateErrorMsg", Boolean.FALSE);
		actionRequest.setAttribute("firstName", Boolean.FALSE);
		actionRequest.setAttribute("secondName", Boolean.FALSE);
		actionRequest.setAttribute("email", Boolean.FALSE);
		actionRequest.setAttribute("phoneNo", Boolean.FALSE);
		actionRequest.setAttribute("userDetailsUpdateSuccess", Boolean.FALSE);
		actionRequest.setAttribute("userDetailsMissingError", Boolean.FALSE);
		actionRequest.setAttribute("invalidPCTPassword", Boolean.FALSE);
		actionRequest.setAttribute("passwordsNotEqual", Boolean.FALSE);
		actionRequest.setAttribute("invalidPortalPassword", Boolean.FALSE);
		logger.debug("[processAction] form name is: " + hiddenformName);

		if ((hiddenformName != null) &&
			hiddenformName.equalsIgnoreCase("updateDetails")) {

			this.updateUserDetails(actionRequest);
		}
		else if ((hiddenformName != null) &&
				 hiddenformName.equalsIgnoreCase("resetPassword")) {

			this.updateUserPassword(actionRequest);
		}
		else if ((hiddenformName != null) &&
				 hiddenformName.equalsIgnoreCase("updateAnswers")) {

			this.updateSecretAnswers(actionRequest);
		}

		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - processAction() - Finished custom processing.");
		super.processAction(actionRequest, actionResponse);

		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - processAction() - End.");
	}

	protected void include(
			String path, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws IOException, PortletException {

		logger.debug(
			ManageAccountPortlet.class.toString() + " - include() - Start.");
		PortletRequestDispatcher portletRequestDispatcher =
			this.getPortletContext(
			).getRequestDispatcher(
				path
			);

		if (portletRequestDispatcher == null) {
			logger.error(
				ManageAccountPortlet.class.toString() +
					" - include() - Invalid include:");
		}
		else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}

		logger.debug(
			ManageAccountPortlet.class.toString() + " - include() - End.");
	}

	private User getUser(long companyId, String screenName)
		throws PortalException {

		logger.debug(
			ManageAccountPortlet.class.toString() + " - getUser() - Start.)");
		User user = null;

		if ((screenName != null) && (screenName.length() > 0)) {
			user = UserLocalServiceUtil.getUserByScreenName(
				companyId, screenName);
		}

		logger.debug(
			ManageAccountPortlet.class.toString() + " - getUser() - Start.)");

		return user;
	}

	private User getUser(PortletRequest request) {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		User user = themeDisplay.getUser();

		return user;
	}

	private List<Role> getUserInheritedRoles(User user) {
		List<Role> inheritedRoles = new ArrayList();
		List groupRoles = null;

		try {
			List<UserGroup> userGroups = user.getUserGroups();

			Iterator var5 = userGroups.iterator();

			while (var5.hasNext()) {
				UserGroup userGroup = (UserGroup)var5.next();
				new ArrayList();
				long groupId = userGroup.getGroup(
				).getGroupId();
				groupRoles = RoleLocalServiceUtil.getGroupRoles(groupId);
				Iterator var9 = groupRoles.iterator();

				while (var9.hasNext()) {
					Role groupRole = (Role)var9.next();

					inheritedRoles.add(groupRole);
				}
			}
		}
		catch (Exception var11) {
			logger.error(
				"Could not get the inherited roles for the user " +
					user.getUserId() + ". ",
				var11.getMessage(), var11);
		}

		return inheritedRoles;
	}

	private String getUserOrgShortName(PortletRequest request)
		throws Exception {

		if (this.orgIdOverride == null) {
			return LiferayClientUtil.getOrgShortName(
				this.entityService, request);
		}

		return LiferayClientUtil.getOrgShortName(
			this.entityService, this.orgIdOverride);
	}

	private List<Role> getUserRoles(User user) {
		List<Role> assignedRoles = RoleLocalServiceUtil.getUserRoles(
			user.getUserId());
		List<Role> inheritedRoles = this.getUserInheritedRoles(user);
		List<Role> allRoles = new ArrayList();

		allRoles.addAll(assignedRoles);
		allRoles.addAll(inheritedRoles);

		return allRoles;
	}

	private String getUserScreenName(PortletRequest request) {
		User user = this.getUser(request);

		if (user == null) {
			return null;
		}

		return user.getScreenName();
	}

	private void loadProperties(String propertyFilename) throws IOException {
		logger.debug("loadProperties - start");
		InputStream iStream = null;

		try {
			logger.info(
				"Initialising webservice urls from " + propertyFilename);
			Class<? extends ManageAccountPortlet> clazz = getClass();

			iStream = CuscalSharedPropsUtil.getResourceStream(
				clazz, propertyFilename);
			Properties p = new Properties();

			p.load(iStream);
			this.pctChangePasswordRole = p.getProperty(
				"pct.manage.account.display.role");

			if (StringUtils.isNotBlank(p.getProperty("override.user.orgId"))) {
				this.orgIdOverride = Long.parseLong(
					p.getProperty("override.user.orgId"));
				logger.debug(
					"[loadProperties] Loaded organisation ID from the property file: " +
						this.orgIdOverride);
			}

			logger.info(
				"Loaded pct.manage.account.display.role=" +
					this.pctChangePasswordRole);
		}
		finally {
			if (iStream != null) {
				iStream.close();
			}
		}

		logger.debug("loadProperties - end");
	}

	private void updateSecretAnswers(ActionRequest actionRequest) {
		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - updateSecretAnswers() - Start.");
		String hiddenUserId = this.getUserScreenName(actionRequest);
		String firstAnsw = ParamUtil.getString(actionRequest, "secQ_ans1");
		String secondAnsw = ParamUtil.getString(actionRequest, "secQ_ans2");

		try {
			if (!firstAnsw.equalsIgnoreCase("") &&
				!secondAnsw.equalsIgnoreCase("")) {

				LDAPUtil.getInstance(
				).modifyAttribute(
					hiddenUserId, firstAnsw, secondAnsw
				);
				logger.debug(
					ManageAccountPortlet.class.toString() +
						" - updateSecretAnswers() - Updated secret anwers successfully.");
				actionRequest.setAttribute(
					"answUpdateSuccessMsg", Boolean.TRUE);
				AUDITOR.success(
					(PortletResponse)null, actionRequest, "PORTAL",
					"USER/SECRET_QUESTIONS",
					hiddenUserId + " has updated their secret answers");
			}
			else {
				logger.debug(
					ManageAccountPortlet.class.toString() +
						" - updateSecretAnswers() - Missing required values (Secret Questions).");
				actionRequest.setAttribute("answUpdateErrorMsg", Boolean.TRUE);
			}
		}
		catch (NamingException var6) {
			logger.error(
				ManageAccountPortlet.class.toString() +
					" - updateSecretAnswers() - Exception: Secret Question updated failed.",
				var6);
		}

		actionRequest.setAttribute("scrollTo", "update-password-questions");
		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - updateSecretAnswers() - End.");
	}

	private boolean updateUser(
		long companyId, String screenName, String strFirstName,
		String strLastName, String strEmailAddress, String strPhoneNumber) {

		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - updateUser() - Start.)");
		boolean result = false;

		try {
			User user = this.getUser(companyId, screenName);

			if (user == null) {
				logger.debug(
					ManageAccountPortlet.class.toString() +
						" - updateUser() - User not found (" + screenName +
							").");
			}
			else {
				user.setFirstName(strFirstName);
				user.setLastName(strLastName);
				user.setEmailAddress(strEmailAddress);
				UserLocalServiceUtil.updateUser(user);
				UserUtilImpl userUtil = new UserUtilImpl();

				userUtil.addOrUpdatePhoneNumberForUser(user, strPhoneNumber);
				result = true;
			}
		}
		catch (PortalException var11) {
			logger.debug(
				ManageAccountPortlet.class.toString() +
					" - updateUser() - Exception updating user using  UserLocalServiceUtil.");
			var11.printStackTrace();
		}
		catch (SystemException var12) {
			logger.debug(
				ManageAccountPortlet.class.toString() +
					" - updateUser() - Exception updating user using  UserLocalServiceUtil.");
			var12.printStackTrace();
		}

		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - updateUser() - User updated ok? " + result + "+ End.)");

		return result;
	}

	private void updateUserDetails(ActionRequest actionRequest) {
		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - updateUserDetails() - Start.");
		boolean updateUserDetailsSuccess = false;
		String hiddenUserId = this.getUserScreenName(actionRequest);

		String fName = ParamUtil.getString(actionRequest, "fname");
		String lName = ParamUtil.getString(actionRequest, "lname");
		String emailId = ParamUtil.getString(actionRequest, "email");
		String phoneNo = ParamUtil.getString(actionRequest, "phoneNo");

		if (!fName.equalsIgnoreCase("") && !lName.equalsIgnoreCase("") &&
			!emailId.equalsIgnoreCase("") && !phoneNo.equalsIgnoreCase("")) {

			updateUserDetailsSuccess = this.updateUser(
				PortalUtil.getCompanyId(actionRequest), hiddenUserId, fName,
				lName, emailId, phoneNo);

			if (updateUserDetailsSuccess) {
				try {
					LDAPUtil.getInstance(
					).modifyAttributesForUserDetails(
						hiddenUserId, fName, lName, emailId, phoneNo
					);
				}
				catch (NamingException var12) {
					logger.error(
						ManageAccountPortlet.class.toString() +
							" - updateUserDetails() - Exception: Failed to update User details directly in LDAP",
						var12);
				}

				try {
					UserUtilImpl userUtil = new UserUtilImpl();
					User user = this.getUser(actionRequest);

					userUtil.addOrUpdateUserInGetTicketUserService(user);
				}
				catch (SystemException var10) {
					logger.error(
						ManageAccountPortlet.class.toString() +
							" - updateUserDetails() - Exception: Failed to update User details in Business objects with GetTicketUser service",
						var10);
					var10.printStackTrace();
				}
				catch (PortalException var11) {
					logger.error(
						ManageAccountPortlet.class.toString() +
							" - updateUserDetails() - Exception: Failed to update User details in Business objects with GetTicketUser service",
						var11);
				}

				actionRequest.setAttribute("email", Boolean.TRUE);
				actionRequest.setAttribute("emailId", emailId);
				actionRequest.setAttribute("firstName", Boolean.TRUE);
				actionRequest.setAttribute("fName", fName);
				actionRequest.setAttribute("phoneNo", Boolean.TRUE);
				actionRequest.setAttribute("phoneNo", phoneNo);
				actionRequest.setAttribute("secondName", Boolean.TRUE);
				actionRequest.setAttribute("sName", lName);
				logger.debug(
					ManageAccountPortlet.class.toString() +
						" - updateUserDetails() - User Details updated successfully.");
				actionRequest.setAttribute(
					"userDetailsUpdateSuccess", Boolean.TRUE);
				AUDITOR.success(
					(PortletResponse)null, actionRequest, "PORTAL",
					"USER/DETAILS",
					hiddenUserId + " has updated firstname, surname and email");
			}
			else {
				logger.debug(
					ManageAccountPortlet.class.toString() +
						" - updateUserDetails() - Failed to update user in LifeRay Database." +
							hiddenUserId);
				AUDITOR.fail(
					(PortletResponse)null, actionRequest, "PORTAL",
					"USER/DETAILS",
					"Failed updated of firstname, surname and email");
			}
		}
		else {
			actionRequest.setAttribute("userDetailsMissingError", Boolean.TRUE);
		}

		actionRequest.setAttribute("scrollTo", "update-details");
		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - updateUserDetails() - End.");
	}

	private void updateUserPassword(ActionRequest actionRequest) {
		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - updateUserPassword() - Start.");
		boolean verifySuccess = false;
		boolean updatePassSuccess = false;
		boolean passwordNotEntered = false;
		String oldPwd = actionRequest.getParameter("oldPassword");
		String newPwd = actionRequest.getParameter("newPassword");
		String retypePwd = actionRequest.getParameter("retypePassword");

		if ((oldPwd == null) || (oldPwd.length() == 0) || (newPwd == null) ||
			(newPwd.length() == 0) || (retypePwd == null) ||
			(retypePwd.length() == 0)) {

			passwordNotEntered = true;
		}

		String hiddenUserId = this.getUserScreenName(actionRequest);

		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - updateUserPassword() - Verifying Password for " +
					hiddenUserId);

		if (!passwordNotEntered) {
			verifySuccess = LDAPUtil.getInstance(
			).verifyPassword(
				hiddenUserId, oldPwd
			);
			logger.debug(
				ManageAccountPortlet.class.toString() +
					" - updateUserPassword() - Verifying:" + verifySuccess);

			if (verifySuccess) {
				if (newPwd.equals(retypePwd)) {
					logger.debug(
						ManageAccountPortlet.class.toString() +
							" - updateUserPassword() - Same passwords old and new.");

					try {
						updatePassSuccess = LDAPUtil.getInstance(
						).modifyPasswordAttributeForForgotPassword(
							hiddenUserId, newPwd
						);

						if (updatePassSuccess) {
							logger.debug(
								ManageAccountPortlet.class.toString() +
									"- updateUserPassword() - Updated password successfully.");
							actionRequest.setAttribute(
								"passUpdateSuccessMsg", Boolean.TRUE);
							AUDITOR.success(
								(PortletResponse)null, actionRequest, "PORTAL",
								"USER/PASSWORD",
								hiddenUserId + " has updated their password");
						}
						else {
							actionRequest.setAttribute(
								"invalidPassword", Boolean.TRUE);
						}
					}
					catch (NamingException var10) {
						logger.error(
							ManageAccountPortlet.class.toString() +
								" - updateUserPassword() - Exception:" +
									var10.getMessage(),
							var10);
						actionRequest.setAttribute(
							"invalidPassword", Boolean.TRUE);
					}
				}
				else {
					logger.debug(
						ManageAccountPortlet.class.toString() +
							" - updateUserPassword() - New and Retyped passwords don't match.");
					actionRequest.setAttribute("noMatchPassword", Boolean.TRUE);
				}
			}
			else {
				logger.debug(
					ManageAccountPortlet.class.toString() +
						" - updateUserPassword() - Old password verification failed.");
				actionRequest.setAttribute("verifyPassFailed", Boolean.TRUE);
				AUDITOR.fail(
					(PortletResponse)null, actionRequest, "PORTAL",
					"USER/PASSWORD",
					"Old password not correct for " + hiddenUserId);
			}
		}
		else {
			logger.debug(
				ManageAccountPortlet.class.toString() +
					" - updateUserPassword() - At lease one of the passwords not provided.");
			actionRequest.setAttribute("invalidPassword", Boolean.TRUE);
		}

		actionRequest.setAttribute("scrollTo", "update-password");
		logger.debug(
			ManageAccountPortlet.class.toString() +
				" - updateUserPassword() - End.");
	}

	private static final Auditor AUDITOR = Auditor.getInstance();

	private static Logger logger = LoggerFactory.getLogger(
		ManageAccountPortlet.class);
	private static String manageYourAccountJSP;

	private EntityService entityService = null;
	private Long orgIdOverride = null;
	private String pctChangePasswordRole = null;

}