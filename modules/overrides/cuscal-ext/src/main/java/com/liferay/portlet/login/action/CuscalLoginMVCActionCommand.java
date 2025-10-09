package com.liferay.portlet.login.action;

import com.liferay.login.web.constants.LoginPortletKeys;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.CompanyMaxUsersException;
import com.liferay.portal.kernel.exception.CookieNotSupportedException;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.exception.PasswordExpiredException;
import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.exception.UserIdException;
import com.liferay.portal.kernel.exception.UserLockoutException;
import com.liferay.portal.kernel.exception.UserPasswordException;
import com.liferay.portal.kernel.exception.UserScreenNameException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.AuthException;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.URLCodec;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.auth.session.AuthenticatedSessionManagerUtil;
import com.liferay.portal.util.PropsValues;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Peter Fellwock
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + LoginPortletKeys.FAST_LOGIN,
		"javax.portlet.name=" + LoginPortletKeys.LOGIN,
		"mvc.command.name=/login/login", "service.ranking:Integer=100"
	},
	service = MVCActionCommand.class
)
public class CuscalLoginMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (PropsValues.AUTH_LOGIN_DISABLED) {
			actionResponse.sendRedirect(
				themeDisplay.getPathMain() +
					PropsValues.AUTH_LOGIN_DISABLED_PATH);

			return;
		}

		/*if (actionRequest.getRemoteUser() != null) {
			actionResponse.sendRedirect(themeDisplay.getPathMain());

			return;
		}*/

		try {
			login(themeDisplay, actionRequest, actionResponse);

			boolean doActionAfterLogin = ParamUtil.getBoolean(
				actionRequest, "doActionAfterLogin");

			if (doActionAfterLogin) {
				LiferayPortletResponse liferayPortletResponse =
					_portal.getLiferayPortletResponse(actionResponse);

				PortletURL renderURL = liferayPortletResponse.createRenderURL();

				renderURL.setParameter(
					"mvcRenderCommandName", "/login/login_redirect");

				actionRequest.setAttribute(
					WebKeys.REDIRECT, renderURL.toString());
			}
		}
		catch (Exception exception) {
			if (exception instanceof AuthException) {
				Throwable throwable = exception.getCause();

				if (throwable instanceof PasswordExpiredException ||
					throwable instanceof UserLockoutException) {

					SessionErrors.add(
						actionRequest, throwable.getClass(), throwable);
				}
				else {
					if (_log.isInfoEnabled()) {
						_log.info("Authentication failed");
					}

					SessionErrors.add(actionRequest, exception.getClass());
				}
			}
			else if (exception instanceof CompanyMaxUsersException ||
					 exception instanceof CookieNotSupportedException ||
					 exception instanceof NoSuchUserException ||
					 exception instanceof PasswordExpiredException ||
					 exception instanceof UserEmailAddressException ||
					 exception instanceof UserIdException ||
					 exception instanceof UserLockoutException ||
					 exception instanceof UserPasswordException ||
					 exception instanceof UserScreenNameException) {

				SessionErrors.add(
					actionRequest, exception.getClass(), exception);
			}
			else {
				_log.error(exception, exception);

				_portal.sendError(exception, actionRequest, actionResponse);

				return;
			}

			postProcessAuthFailure(actionRequest, actionResponse);

			hideDefaultErrorMessage(actionRequest);
		}
	}

	protected String getCompleteRedirectURL(
		HttpServletRequest httpServletRequest, String redirect) {

		String portalURL = _portal.getPortalURL(httpServletRequest);

		return portalURL.concat(redirect);
	}

	/* CUSCALSOW5-72 */
	protected void isUserActive(
			HttpServletRequest httpServletRequest, String login,
			String authType)
		throws Exception {

		User user = null;

		Company company = PortalUtil.getCompany(httpServletRequest);

		if (Validator.isNull(authType)) {
			authType = company.getAuthType();
		}

		if (authType.equals(CompanyConstants.AUTH_TYPE_EA)) {
			user = UserLocalServiceUtil.fetchUserByEmailAddress(
				company.getCompanyId(), login);
		}
		else if (authType.equals(CompanyConstants.AUTH_TYPE_SN)) {
			user = UserLocalServiceUtil.fetchUserByScreenName(
				company.getCompanyId(), login);
		}
		else if (authType.equals(CompanyConstants.AUTH_TYPE_ID)) {
			user = UserLocalServiceUtil.fetchUser(Long.valueOf(login));
		}

		if ((user != null) && !user.isActive()) {
			throw new UserLockoutException.LDAPLockout("", "");
		}
	}

	protected void login(
			ThemeDisplay themeDisplay, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		HttpServletRequest httpServletRequest =
			_portal.getOriginalServletRequest(
				_portal.getHttpServletRequest(actionRequest));

		if (!themeDisplay.isSignedIn()) {
			HttpServletResponse httpServletResponse =
				_portal.getHttpServletResponse(actionResponse);

			String login = ParamUtil.getString(actionRequest, "login");
			String password = actionRequest.getParameter("password");
			boolean rememberMe = ParamUtil.getBoolean(
				actionRequest, "rememberMe");

			PortletPreferences portletPreferences =
				PortletPreferencesFactoryUtil.getStrictPortletSetup(
					themeDisplay.getLayout(),
					_portal.getPortletId(actionRequest));

			String authType = portletPreferences.getValue("authType", null);

			/* CUSCALSOW5-72 */
			isUserActive(httpServletRequest, login, authType);

			AuthenticatedSessionManagerUtil.login(
				httpServletRequest, httpServletResponse, login, password,
				rememberMe, authType);
		}

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		String mainPath = themeDisplay.getPathMain();

		if (PropsValues.PORTAL_JAAS_ENABLE) {
			if (Validator.isNotNull(redirect)) {
				redirect = StringBundler.concat(
					mainPath, "/portal/protected?redirect=",
					URLCodec.encodeURL(redirect));
			}
			else {
				redirect = mainPath.concat("/portal/protected");
			}

			HttpServletResponse httpServletResponse =
				_portal.getHttpServletResponse(actionResponse);

			httpServletResponse.sendRedirect(redirect);

			return;
		}

		if (Validator.isNotNull(redirect)) {
			if (!themeDisplay.isSignedIn()) {
				actionRequest.setAttribute(
					WebKeys.REDIRECT,
					HttpComponentsUtil.addParameter(
						_portal.getPathMain() + "/portal/login", "redirect",
						redirect));

				return;
			}

			redirect = _portal.escapeRedirect(redirect);

			if (Validator.isNotNull(redirect) &&
				!redirect.startsWith(Http.HTTP)) {

				redirect = getCompleteRedirectURL(httpServletRequest, redirect);
			}
		}

		if (Validator.isNotNull(redirect)) {
			actionResponse.sendRedirect(redirect);
		}
		else {
			boolean doActionAfterLogin = ParamUtil.getBoolean(
				actionRequest, "doActionAfterLogin");

			if (doActionAfterLogin) {
				return;
			}

			actionResponse.sendRedirect(mainPath);
		}
	}

	protected void postProcessAuthFailure(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		LiferayPortletRequest liferayPortletRequest =
			_portal.getLiferayPortletRequest(actionRequest);

		String portletName = liferayPortletRequest.getPortletName();

		Layout layout = (Layout)actionRequest.getAttribute(WebKeys.LAYOUT);

		PortletURL portletURL = PortletURLFactoryUtil.create(
			actionRequest, liferayPortletRequest.getPortlet(), layout,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("saveLastPath", Boolean.FALSE.toString());

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		if (Validator.isNotNull(redirect)) {
			portletURL.setParameter("redirect", redirect);
		}

		String login = ParamUtil.getString(actionRequest, "login");

		if (Validator.isNotNull(login)) {
			SessionErrors.add(actionRequest, "login", login);
		}

		if (portletName.equals(LoginPortletKeys.LOGIN)) {
			portletURL.setWindowState(WindowState.MAXIMIZED);
		}
		else {
			portletURL.setWindowState(actionRequest.getWindowState());
		}

		actionResponse.sendRedirect(portletURL.toString());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CuscalLoginMVCActionCommand.class);

	@Reference
	private Http _http;

	@Reference
	private Portal _portal;

}