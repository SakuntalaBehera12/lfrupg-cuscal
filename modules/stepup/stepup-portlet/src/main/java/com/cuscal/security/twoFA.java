package com.cuscal.security;

import au.com.cuscal.framework.audit.Audit;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.impl.log4j.Log4jAuditor;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.jradiusclient.RadiusAttribute;
import net.sourceforge.jradiusclient.RadiusAttributeValues;
import net.sourceforge.jradiusclient.RadiusClient;
import net.sourceforge.jradiusclient.RadiusPacket;
import net.sourceforge.jradiusclient.exception.InvalidParameterException;
import net.sourceforge.jradiusclient.exception.RadiusException;

/**
 * Portlet implementation class twoFA
 *
 * @author yhossein
 */
public final class twoFA extends MVCPortlet {

	private static final String _RETURN_URL = "_RETURN_URL";

	private static final String _USER_STEPPEDUP = "_USER_STEPPEDUP";

	private static final Log _log = LogFactoryUtil.getLog(twoFA.class);

	public static final String REQUESTED_TOKEN_TYPE = "requestedTokenType";

	public static final String TOKEN_TYPE_RESPONSE_ONLY = "Response Only";

	public static final String TOKEN_TYPE_CHALLENGE_RESPONSE =
		"Challenge/Response";

	public static final String AUDIT_CATEGORY = "Step up";

	public static final String ERROR_PROCESSING_REQUEST =
		"An error occured processing your request. Please try again later.";

	public static final String ERROR_PROCESSING_REQUEST_CONTINUES =
		"An error has occurred while trying to process your request. If this error continues, please contact <a href='mailto:calldirect@cuscal.com.au' class='links'>CallDirect</a> on <span class='no-wrap'>1300 650 501</span>.";

	public static final String ERROR_INVALID_TOKEN =
		"The token response you supplied was incorrect. Please check your token and try again. If you continue to receive this error, please contact <a href='mailto:calldirect@cuscal.com.au' class='links'>CallDirect</a> on 1300 650 501.";

	static final String TOKEN_ID = "tokenID";
	static final String TOKEN_TYPE_ATTRIBUTE_NAME = "Tokentype";

	// Radius (i.e. Vasco) server configuration

	private String RadiusServerName = "";
	private String RadiusServerAuthPort = "";
	private String RadiusServerAccPort = "";
	private String RadiusServerSharedKey = "";
	private String StaticPassword = "";

	public twoFA() {
	}

	/**
	 * Save the session variables.
	 *
	 * @param request <em>PortletRequest</em>
	 * @param key <em>String</em>
	 * @param value <em>String</em>
	 */
	protected void setSessionVariable(
		PortletRequest request, String key, String value) {

		PortletSession session = request.getPortletSession(true);

		session.setAttribute(key, value);
	}

	/**
	 * Retrieve the session variable.
	 *
	 * @param request <em>PortletRequest</em>
	 * @param key <em>String</em>
	 * @return String
	 */
	protected String getSessionVariable(PortletRequest request, String key) {
		PortletSession session = request.getPortletSession(true);

		String value = (String)session.getAttribute(key);

		return value;
	}

	/**
	 * ProcessAction method that handles the users request.
	 *
	 * @param request <em>ActionRequest</em>
	 * @param response <em>ActionResponse</em>
	 *
	 */
	public void processAction(ActionRequest request, ActionResponse response)
		throws IOException, PortletException {

		request.setAttribute("genToken", Boolean.TRUE);
		String tokenType = ParamUtil.getString(request, "tokenType");

		if (!isValidTokenType(tokenType)) {
			tokenType = TOKEN_TYPE_RESPONSE_ONLY;
		}

		setSessionVariable(request, REQUESTED_TOKEN_TYPE, tokenType);

		loadPrefs(request);

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession httpSession = httpServletRequest.getSession();
		PortletSession portletSession = request.getPortletSession(true);

		clean(request, httpSession, portletSession);

		String actionName = ParamUtil.getString(request, "actionName");

		if (actionName.equalsIgnoreCase("generate")) {
			request.setAttribute("isChallenge", "true");

			if (!getChallengeKey(request, response)) {
				setSessionKeysAndLogError(
					request, response, ERROR_PROCESSING_REQUEST_CONTINUES);
			}
		}

		if (actionName.equalsIgnoreCase("stepUp")) {
			httpSession.setAttribute("USER_CANCEL", null);
			portletSession.setAttribute(
				"USER_CANCEL", null, PortletSession.APPLICATION_SCOPE);

			stepUp(request, response);
		}

		if (actionName.equalsIgnoreCase("stepUpCancel")) {
			httpSession.setAttribute("USER_CANCEL", "cancel");
			portletSession.setAttribute(
				"USER_CANCEL", "cancel", PortletSession.APPLICATION_SCOPE);

			String returnURLSession = GetterUtil.getString(
				portletSession.getAttribute(
					_RETURN_URL, PortletSession.APPLICATION_SCOPE));

			if (returnURLSession.isEmpty()) {
				returnURLSession = GetterUtil.getString(
					httpSession.getAttribute(_RETURN_URL));
			}

			response.sendRedirect(returnURLSession);
		}

		if (request.getPortletMode(
			).equals(
				PortletMode.EDIT
			) & actionName.equalsIgnoreCase("savePrefs")) {

			try {
				savePref(request, response);
			}
			catch (PortalException e) {
				_log.error(
					"Could not save the preferences: " + e.getMessage(), e);
			}
			catch (SystemException e) {
				_log.error(
					"There was a system exception trying to save the preferences: " +
						e.getMessage(),
					e);
			}
		}
	}

	/**
	 * Find weather Request token is valid or not
	 * @param String
	 * @return boolean
	 */
	private static boolean isValidTokenType(String tokenType) {
		boolean isValid = false;

		if ((tokenType != null) &&
			(tokenType.equalsIgnoreCase(TOKEN_TYPE_RESPONSE_ONLY) ||
			 tokenType.equalsIgnoreCase(TOKEN_TYPE_CHALLENGE_RESPONSE))) {

			isValid = true;
		}

		return isValid;
	}

	/**
	 * Clean the portlet session.
	 *
	 * @param portletRequest <em>PortletRequest</em>
	 */
	private static void clean(
		PortletRequest portletRequest, HttpSession httpSession,
		PortletSession portletSession) {

		portletSession.setAttribute(
			_USER_STEPPEDUP, "false", PortletSession.APPLICATION_SCOPE);
		httpSession.setAttribute(_USER_STEPPEDUP, "false");
		portletRequest.setAttribute("stepUpMsg", "");
	}

	/**
	 * Loading Portlet Preferences from Portlet resource , this would overwrites
	 * the default settings from hook
	 *
	 * @param request <em>PortletRequest</em>
	 */
	private void loadPrefs(PortletRequest request) {
		PortletPreferences prefs = request.getPreferences();

		setRadiusServerAuthPort(
			prefs.getValue("Radius.Server.Auth.Port", StringPool.BLANK));

		setRadiusServerAccPort(
			prefs.getValue("Radius.Server.Acc.Port", StringPool.BLANK));

		setRadiusServerName(
			prefs.getValue("Radius.Server.Name", StringPool.BLANK));

		setRadiusServerSharedKey(
			prefs.getValue("Radius.Server.Shared.Key", StringPool.BLANK));

		setStaticPassword(prefs.getValue("Static.Password", StringPool.BLANK));

		_log.debug(
			"Loading of preferences complete.  Using " + RadiusServerName +
				":" + RadiusServerAuthPort + ":" + RadiusServerAccPort);
	}

	/**
	 * Save portlet preferences
	 *
	 * @param actionRequest </em>ActionRequest</em>
	 * @param response <em>ActionResponse</em>
	 * @throws PortalException
	 * @throws SystemException
	 * @throws ReadOnlyException
	 * @throws ValidatorException
	 * @throws IOException
	 * @throws PortletModeException
	 */
	public void savePref(ActionRequest actionRequest, ActionResponse response)
		throws IOException, PortalException, PortletModeException,
			   ReadOnlyException, SystemException, ValidatorException {

		setRadiusServerAuthPort(
			ParamUtil.getString(actionRequest, "RadiusServerAuthPort"));

		setRadiusServerAccPort(
			ParamUtil.getString(actionRequest, "RadiusServerAccPort"));

		setRadiusServerName(
			ParamUtil.getString(actionRequest, "RadiusServerName"));
		setRadiusServerSharedKey(
			ParamUtil.getString(actionRequest, "RadiusServerSharedKey"));

		setStaticPassword(ParamUtil.getString(actionRequest, "StaticPassword"));

		// Creating the Reference for Portletpreferences Table

		PortletPreferences prefs =
			PortletPreferencesFactoryUtil.getPortletSetup(actionRequest);

		// Setting The Value in the PortletPreferences table

		prefs.setValue("Radius.Server.Auth.Port", getRadiusServerAuthPort());
		prefs.setValue("Radius.Server.Acc.Port", getRadiusServerAccPort());
		prefs.setValue("Radius.Server.Name", getRadiusServerName());

		if (!"****".equals(getRadiusServerSharedKey())) {
			_log.info("updating Radius.Server.Shared.Key");
			prefs.setValue(
				"Radius.Server.Shared.Key", getRadiusServerSharedKey());
		}

		if (!"****".equals(getStaticPassword())) {
			_log.info("updating Static.Password");
			prefs.setValue("Static.Password", getStaticPassword());
		}

		try {
			prefs.store();
			_log.info(
				"Stepup Portlet preference settings has been updated by user:" +
					getUserScreenName(actionRequest));
		}
		catch (Exception e) {
			_log.error("Unable to store preferences: " + e.getMessage(), e);
		};

		response.setPortletMode(PortletMode.VIEW);
	}

	/**
	 * Begin the step up process and redirect the user to the appropriate page.
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws RadiusException
	 */
	private void stepUp(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		String tokenId = StringPool.BLANK;

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		HttpSession httpSession = httpServletRequest.getSession();
		PortletSession portletSession = portletRequest.getPortletSession(true);

		String challengeKey = ParamUtil.get(
			portletRequest, "challengeKey", (String)null);

		if ((challengeKey != null) && !challengeKey.isEmpty()) {
			tokenId = ParamUtil.getString(portletRequest, "tokenID");
			portletRequest.setAttribute("genToken", Boolean.TRUE);
		}
		else {
			tokenId = ParamUtil.getString(portletRequest, "respTokenID");
			portletRequest.setAttribute("genToken", Boolean.FALSE);
		}

		setSessionVariable(portletRequest, TOKEN_ID, tokenId);

		if (tokenId.isEmpty() &&
			getTokenType(
				portletRequest
			).equalsIgnoreCase(
				TOKEN_TYPE_RESPONSE_ONLY
			)) {

			_log.warn("Token cannot be null");
		}

		String steppedUpSession = GetterUtil.getString(
			portletSession.getAttribute(
				_USER_STEPPEDUP, PortletSession.APPLICATION_SCOPE));

		if (steppedUpSession.isEmpty()) {
			steppedUpSession = GetterUtil.getString(
				httpSession.getAttribute(_USER_STEPPEDUP));
		}

		if (steppedUpSession.isEmpty()) {
			_log.error("Please deploy vasco-hook if you haven't");
		}

		String returnUrl = GetterUtil.getString(
			portletSession.getAttribute(
				_RETURN_URL, PortletSession.APPLICATION_SCOPE));

		if (returnUrl.isEmpty()) {
			returnUrl = GetterUtil.getString(
				httpSession.getAttribute(_RETURN_URL));
		}

		boolean steppedUp = steppedUpSession.equals("true");

		if (_log.isDebugEnabled()) {
			_log.debug("returnURL: " + returnUrl);
		}

		if (steppedUp && !returnUrl.isEmpty()) {
			try {
				((ActionResponse)portletResponse).sendRedirect(returnUrl);
			}
			catch (IOException e) {
				_log.warn(
					"Could not redirect the user to the right page. " +
						e.getMessage(),
					e);
			}
		}

		if (!steppedUp) {
			if (!doStepUp(portletSession, portletRequest, portletResponse)) {
				setSessionKeysAndLogError(
					portletRequest, portletResponse,
					ERROR_PROCESSING_REQUEST_CONTINUES);
			}
		}
	}

	/**
	 * do Stepup which communicates back to Radius server
	 *
	 * @param portletSession <em>PortletSession</em>
	 * @param portletRequest <em>PortletRequest</em>
	 * @param response <PortletResponse>
	 * @throws IOException
	 * @throws RadiusException
	 */
	private boolean doStepUp(
		PortletSession portletSession, PortletRequest portletRequest,
		PortletResponse portletResponse) {

		if ((getRadiusServerName() == null) ||
			(getRadiusServerAuthPort() == null) ||
			(getRadiusServerSharedKey() == null) ||
			(getRadiusServerAccPort() == null)) {

			_log.error(
				"The properties for radius server have not been configured in portlet preferences ");
		}
		else {
			String requestedTokenType = getSessionVariable(
				portletRequest, REQUESTED_TOKEN_TYPE);

			if (requestedTokenType.equalsIgnoreCase(
					TOKEN_TYPE_CHALLENGE_RESPONSE)) {

				String challengeKey = ParamUtil.getString(
					portletRequest, "challengeKey");

				doChallenge2(portletRequest, portletResponse, challengeKey);
			}
			else {
				RadiusClient rClient = newRadiusClient(
					getRadiusServerName(), getRadiusServerAuthPort(),
					getRadiusServerAccPort(), getRadiusServerSharedKey());

				if (rClient != null) {
					RadiusPacket accessRequest = createRadiusPacket(
						portletRequest);

					RadiusPacket accessResponse = createResponsePacket(
						rClient, accessRequest);

					parseResponse(
						portletRequest, portletResponse, accessResponse);
				}
				else {
					if (_log.isWarnEnabled()) {
						_log.warn("rClient is null");
					}

					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Do the step up if the user is using response only.
	 *
	 * @param portletRequest
	 * @param portletResponse
	 * @param packetResponse
	 * @return String 0
	 */
	private void parseResponse(
		PortletRequest portletRequest, PortletResponse portletResponse,
		RadiusPacket packetResponse) {

		try {
			String requestedTokenType = getSessionVariable(
				portletRequest, REQUESTED_TOKEN_TYPE);

			switch (packetResponse.getPacketType()) {
				case RadiusPacket.ACCESS_ACCEPT:
					saveUserTokenType(portletRequest, requestedTokenType);
					setSessionKeysAndRedirect(
						portletRequest, portletResponse,
						"Your request has been successfully accepted");

					break;

				case RadiusPacket.ACCESS_REJECT:
					setSessionKeysAndLogError(
						portletRequest, portletResponse, ERROR_INVALID_TOKEN);
					_log.warn(
						"Access rejected from Vasco for user " +
							getUserScreenName(portletRequest) + ".");

					break;

				case RadiusPacket.ACCESS_REQUEST:
					_log.info("Acceess Requested to a restricted resource.");

					break;

				default:
					_log.error(
						"ERROR: An unknown packet was received from the Vasco server.");
			}
		}
		catch (Exception e) {
			_log.error(
				"Unexpected result was returned from the Vasco server. " +
					e.getMessage(),
				e);
		}
	}

	/**
	 * Create the RadiusPacket to retrieve the Challenge.
	 *
	 * @param request <em>PortletRequest</em>
	 * @param userId <em>String</em>
	 * @return RadiusPacket
	 */
	public RadiusPacket getRadiusChallengePacket(
		PortletRequest request, PortletResponse response, String userId) {

		loadPrefs(request);
		RadiusClient rClient = newRadiusClient();

		RadiusPacket accessRequest = null;

		try {
			accessRequest = new RadiusPacket(RadiusPacket.ACCESS_REQUEST);
		}
		catch (InvalidParameterException e) {
			_log.error(
				"There was an invalid parameter when trying to create an accessRequest. " +
					e.getMessage(),
				e);
		}

		try {
			accessRequest.setAttribute(
				new RadiusAttribute(
					RadiusAttributeValues.USER_NAME, userId.getBytes()));
		}
		catch (InvalidParameterException e2) {
			_log.error(
				"There was an invalid parameter when trying to set the username. " +
					e2.getMessage(),
				e2);
		}

		try {
			accessRequest.setAttribute(
				new RadiusAttribute(
					RadiusAttributeValues.USER_PASSWORD,
					getStaticPassword().getBytes()));
		}
		catch (InvalidParameterException e3) {
			_log.error(
				"There was an invalid parameter when trying to set the user static password. " +
					e3.getMessage(),
				e3);
		}

		RadiusPacket responsePacket = null;

		if (rClient != null) {
			try {
				try {
					responsePacket = rClient.authenticate(
						accessRequest, RadiusPacket.ACCESS_REQUEST);
				}
				catch (RadiusException e) {
					_log.error(
						"Unable to authenticate against the vasco server. " +
							e.getMessage(),
						e);
				}
			}
			catch (InvalidParameterException e1) {
				_log.error(
					"There was an invalid parameter in the response packet. " +
						e1.getMessage(),
					e1);
			}
		}

		return responsePacket;
	}

	/**
	 * Login process for the Challenge Response section.
	 *
	 * @param portletRequest <em>PortletRequest</em>
	 * @param portletResponse <em>PortletResponse</em>
	 * @param challengeToken <em>String</em>
	 */
	public boolean doChallenge2(
		PortletRequest portletRequest, PortletResponse portletResponse,
		String challengeToken) {

		RadiusPacket radiusPacket = null;

		try {
			radiusPacket = sendRadiusChallenge(portletRequest, challengeToken);
		}
		catch (RadiusException e) {
			_log.error(
				"Unable to send the radius challenge. " + e.getMessage(), e);
		}
		catch (InvalidParameterException e1) {
			_log.error(
				"There was an invalid parameter when trying to access the server. " +
					e1.getMessage(),
				e1);
		}
		catch (NullPointerException e2) {
			_log.error(
				"Null pointer exception from the server. " + e2.getMessage(),
				e2);
		}
		catch (Exception e3) {
			_log.error(
				"Unknown exception when trying to access the Vasco server. " +
					e3.getMessage(),
				e3);
		}

		if (radiusPacket != null) {
			String requestedTokenType = getSessionVariable(
				portletRequest, REQUESTED_TOKEN_TYPE);

			switch (radiusPacket.getPacketType()) {
				case RadiusPacket.ACCESS_ACCEPT:
					saveUserTokenType(portletRequest, requestedTokenType);
					setSessionKeysAndRedirect(
						portletRequest, portletResponse,
						"Your request has been successfully accepted.");

					break;

				case RadiusPacket.ACCESS_CHALLENGE:
					_log.info("User requested a new challenge.");

					break;

				case RadiusPacket.ACCESS_REJECT:
					_log.warn(
						"Access rejected from Vasco for user " +
							getUserScreenName(portletRequest) + ".");
					setSessionKeysAndLogError(
						portletRequest, portletResponse, ERROR_INVALID_TOKEN);

					break;

				case RadiusPacket.ACCESS_REQUEST:
					_log.info("Request");

					break;
				default:
					_log.error(
						"An unknown packet was received from the Vasco server.");
			}
		}
		else {
			_log.error("Response from the Vasco server is null.");

			return false;
		}

		return true;
	}

	/**
	 * Save the user token type after a successful login.
	 *
	 * @param portletRequest <em>PortletRequest</em>
	 * @param requestTokenType <em>String</em>
	 */
	private void saveUserTokenType(
		PortletRequest portletRequest, String requestTokenType) {

		ExpandoBridge exBrg = null;
		User user = null;

		try {
			user = PortalUtil.getUser(portletRequest);
		}
		catch (PortalException e1) {
			_log.error(
				"Portal exception when trying to get the user from the request. " +
					e1.getMessage(),
				e1);
		}
		catch (SystemException e2) {
			_log.error(
				"System exception when trying to get the user from the request. " +
					e2.getMessage(),
				e2);
		}

		if (user != null) {
			try {
				exBrg = user.getExpandoBridge();
			}
			catch (Exception e) {
				_log.error(
					"Could not get the ExpandoBridge for user: " +
						user.getScreenName() + " " + e.getMessage(),
					e);
			}

			if (exBrg != null) {
				exBrg.setAttribute(
					TOKEN_TYPE_ATTRIBUTE_NAME, requestTokenType, false);
			}
			else {
				_log.error("Expando Bridge returned null.");
			}
		}
		else {
			_log.error(
				"The user is null. Could not get the expando bridge to save the users profile");
		}
	}

	/**
	 * Returns a new Radius Client.
	 *
	 * @return RadiusClient
	 */
	private RadiusClient newRadiusClient() {
		RadiusClient rClient = null;

		try {
			try {
				rClient = new RadiusClient(
					getRadiusServerName(),
					Integer.parseInt(getRadiusServerAuthPort()),
					Integer.parseInt(getRadiusServerAccPort()),
					getRadiusServerSharedKey());
			}
			catch (NumberFormatException e) {
				_log.error(
					"Invalid number when trying to create a new Radius Client. " +
						e.getMessage(),
					e);
			}
			catch (RadiusException e) {
				_log.error(
					"Exception creating a new Radius Client. " + e.getMessage(),
					e);
			}
		}
		catch (InvalidParameterException e2) {
			_log.error(
				"Invalid parameter when creating a new Radius Client. " +
					e2.getMessage(),
				e2);
		}

		return rClient;
	}

	/**
	 * Sends the challenge to the Vasco Server.
	 *
	 * @param portletRequest
	 * @param challenge
	 * @return RadiusPacket
	 * @throws RadiusException
	 * @throws InvalidParameterException
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public RadiusPacket sendRadiusChallenge(
			PortletRequest portletRequest, String challenge)
		throws InvalidParameterException, RadiusException {

		RadiusPacket accessRequest = new RadiusPacket(
			RadiusPacket.ACCESS_REQUEST);

		RadiusClient radiusClient = newRadiusClient();

		RadiusPacket responsePacket = RadiusThreadLocal.get();

		if (responsePacket != null) {
			accessRequest.setAttributes(
				new ArrayList(responsePacket.getAttributes()));
		}
		else {
			_log.error("responsePacket in portletRequest is null");
		}

		String tokenId = getSessionVariable(portletRequest, TOKEN_ID);

		accessRequest.setAttribute(
			new RadiusAttribute(
				RadiusAttributeValues.USER_NAME,
				getUserScreenName(
					portletRequest
				).getBytes()));

		accessRequest.setAttribute(
			new RadiusAttribute(
				RadiusAttributeValues.USER_PASSWORD, tokenId.getBytes()));

		RadiusPacket rp = null;

		try {
			rp = radiusClient.authenticate(accessRequest);
		}
		catch (RadiusException e) {
			_log.error(
				"Could not authenticate to the radius client. " +
					e.getMessage(),
				e);
		}

		try {
			radiusClient.finalize();
		}
		catch (Throwable e) {
			_log.error(
				"Could not finalize the Radius Client. " + e.getMessage(), e);
		}

		return rp;
	}

	/**
	 * Create a response packet from the Vasco servers request packet.
	 *
	 * @param rClient <em>RadiusClient</em>
	 * @param accessRequest <em>RadiusPacket</em>
	 * @return RadiusPacket
	 */
	private RadiusPacket createResponsePacket(
		RadiusClient rClient, RadiusPacket accessRequest) {

		RadiusPacket response = null;

		try {
			try {
				response = rClient.authenticate(
					accessRequest, RadiusPacket.ACCESS_REQUEST);
			}
			catch (RadiusException e) {
				_log.error(
					"Could not authenticate the request. " + e.getMessage(), e);
			}
		}
		catch (InvalidParameterException e1) {
			_log.error(
				"Invalid parameter when trying to connect to the server. " +
					e1.getMessage(),
				e1);
		}

		try {
			rClient.finalize();
		}
		catch (Throwable e) {
			_log.error(
				"Could not finalize the Radius Client. " + e.getMessage(), e);
		}

		return response;
	}

	/**
	 * Create a new Radius client.
	 *
	 * @param host <em>String</em>
	 * @param AuthPort <em>String</em>
	 * @param AccPort <em>String</em>
	 * @param sharedPass <em>String</em>
	 * @return RadiusClient
	 */
	private RadiusClient newRadiusClient(
		String host, String AuthPort, String AccPort, String sharedPass) {

		int iAuthPort = 0;

		try {
			iAuthPort = Integer.parseInt(AuthPort);
		}
		catch (Exception e) {
			_log.error(
				"Could not parse the authentication port. " + e.getMessage(),
				e);
		}

		int iAccPort = 0;

		try {
			iAccPort = Integer.parseInt(AccPort);
		}
		catch (Exception e) {
			_log.error(
				"Could not parse the accounting port. " + e.getMessage(), e);
		}

		RadiusClient rClient = null;

		try {
			rClient = new RadiusClient(host, iAuthPort, iAccPort, sharedPass);
		}
		catch (RadiusException e) {
			_log.error(
				"Could not setup the new Radius Client. " + e.getMessage(), e);
		}
		catch (InvalidParameterException e) {
			_log.error(
				"There was an invalid parameter when trying to setup the radius client. " +
					e.getMessage(),
				e);
		}

		return rClient;
	}

	/**
	 * Set the session variables for an unsuccessful login and log the error message.
	 *
	 * @param portletRequest <em>PortletRequest</em>
	 * @param portletResponse <em>PortletResponse</em>
	 * @param msg <em>String</em>
	 */
	private void setSessionKeysAndLogError(
		PortletRequest portletRequest, PortletResponse portletResponse,
		String msg) {

		PortletSession session = portletRequest.getPortletSession(true);

		//Set the steppedup attribute to error in the session.
		session.setAttribute(
			_USER_STEPPEDUP, "error", PortletSession.APPLICATION_SCOPE);

		//Display the proper error message on the screen for the user.
		portletRequest.setAttribute("stepUpMsg", msg);

		String userName = getUserScreenName(portletRequest);
		String serverPort = portletRequest.getServerPort() + "";

		Audit audit = new Log4jAuditor();

		try {
			audit.audit(
				AuditOrigin.PORTAL_ORIGIN, portletRequest.getServerName(),
				portletRequest.getServerName(), serverPort, userName,
				AUDIT_CATEGORY, audit.FAILURE,
				"User " + userName + " failed to step up");
		}
		catch (au.com.cuscal.framework.audit.AuditException e) {
			_log.error("Could not log the audit message. " + e.getMessage(), e);
		}
	}

	/**
	 * Set the session variables for a successful login and redirect to the appropriate page.
	 *
	 * @param portletRequest <em>PortletRequest</em>
	 * @param portletResponse <em>PortletResponse</em>
	 * @param msg <em>String</em>
	 */
	private void setSessionKeysAndRedirect(
		PortletRequest portletRequest, PortletResponse portletResponse,
		String msg) {

		PortletSession portletSession = portletRequest.getPortletSession(true);

		//Set the steppedup attribute in the session to true.
		portletSession.setAttribute(
			_USER_STEPPEDUP, "true", PortletSession.APPLICATION_SCOPE);

		//Clear out the error message if there was any.
		portletRequest.setAttribute("stepUpMsg", "");

		String userName = getUserScreenName(portletRequest);
		String serverPort = portletRequest.getServerPort() + "";

		String returnURL = GetterUtil.getString(
			portletSession.getAttribute(
				_RETURN_URL, PortletSession.APPLICATION_SCOPE));

		if (returnURL.isEmpty()) {
			HttpSession httpSession = PortalUtil.getHttpServletRequest(
				portletRequest
			).getSession();

			returnURL = GetterUtil.getString(
				httpSession.getAttribute(_RETURN_URL));
		}

		if (_log.isDebugEnabled()) {
			_log.debug("currentUrl: " + returnURL);
		}

		Audit audit = new Log4jAuditor();

		try {
			audit.audit(
				AuditOrigin.PORTAL_ORIGIN, portletRequest.getServerName(),
				portletRequest.getServerName(), serverPort, userName,
				AUDIT_CATEGORY, Audit.SUCCESS,
				"User " + userName + " stepped up successfully");
		}
		catch (au.com.cuscal.framework.audit.AuditException e) {
			_log.error("Could not log the audit message. " + e.getMessage(), e);
		}

		if (!returnURL.isEmpty()) {
			try {
				((ActionResponse)portletResponse).sendRedirect(returnURL);
			}
			catch (IOException e) {
				_log.error(
					"Could not redirect the user to the right page. " +
						e.getMessage(),
					e);
			}
		}
		else {
			_log.warn("ReturnURL for stepup is Null, staying on the same page");
		}
	}

	/**
	 * Generate a new Challenge key for Challenge Response.
	 *
	 * @param portletRequest <em>PortletRequest</em>
	 * @param portletResponse <em>PortletResponse</em>
	 */
	public boolean getChallengeKey(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		String challengeKey = "";

		String userID = getUserScreenName(portletRequest);

		RadiusPacket responsePacket = getRadiusChallengePacket(
			portletRequest, portletResponse, userID);

		if (responsePacket != null) {
			switch (responsePacket.getPacketType()) {
				case RadiusPacket.ACCESS_ACCEPT:
					_log.info("Accept");

					break;

				case RadiusPacket.ACCESS_CHALLENGE:
					_log.info("User " + userID + " requested a new challenge.");

					challengeKey = getChallenge(responsePacket);

					portletRequest.setAttribute(
						"responsePacket", responsePacket);
					portletRequest.setAttribute("challengeKey", challengeKey);
					RadiusThreadLocal.set(responsePacket);

					break;

				case RadiusPacket.ACCESS_REJECT:
					setSessionKeysAndLogError(
						portletRequest, portletResponse,
						ERROR_PROCESSING_REQUEST);
					_log.info(
						"User " + userID +
							" access to generate a token was rejected.");

					break;

				case RadiusPacket.ACCESS_REQUEST:
					_log.info("Requested");

					break;

				default:
					_log.warn("unknown");
			}
		}
		else {
			return false;
		}

		return true;
	}

	/**
	 * Display the Step up portlet main page.
	 *
	 * @param request <em>RenderRequest</em>
	 * @param response <en>RenderResponse</em>
	 * @throws PortletException
	 * @throws IOException
	 */
	public void doView(RenderRequest request, RenderResponse response)
		throws IOException, PortletException {

		String tokenType = getTokenType(request);

		if (TOKEN_TYPE_CHALLENGE_RESPONSE.equalsIgnoreCase(tokenType)) {
			getChallengeKey(request, response);
		}

		PortletRequestDispatcher dispatcher =
			getPortletContext().getRequestDispatcher("/html/twoFA/view.jsp");

		dispatcher.include(request, response);
	}

	/**
	 * Display the edit preferences page.
	 *
	 * @param request <em>RenderRequest</em>
	 * @param response <en>RenderResponse</em>
	 * @throws PortletException
	 * @throws IOException
	 */
	public void doEdit(RenderRequest request, RenderResponse response)
		throws IOException, PortletException {

		PortletRequestDispatcher dispatcher =
			getPortletContext().getRequestDispatcher("/html/twoFA/edit.jsp");

		dispatcher.include(request, response);
	}

	/**
	 * Creates the Radius packet to send to the Vasco server.
	 * @param request
	 * @return accessRequest <em>RadiusPacket</em>
	 */
	public RadiusPacket createRadiusPacket(PortletRequest request) {
		RadiusPacket accessRequest = null;

		try {
			accessRequest = new RadiusPacket(RadiusPacket.ACCESS_REQUEST);
		}
		catch (InvalidParameterException e) {
			_log.error(
				"Invalid parameter in creating the Access radius packet. " +
					e.getMessage(),
				e);
		}

		try {
			accessRequest.setAttribute(
				new RadiusAttribute(
					RadiusAttributeValues.USER_NAME,
					getUserScreenName(
						request
					).getBytes()));
		}
		catch (InvalidParameterException e) {
			_log.error(
				"Invalid parameter when setting the username to the radius request. " +
					e.getMessage(),
				e);
		}

		try {
			String tokenType = getSessionVariable(
				request, REQUESTED_TOKEN_TYPE);

			if (tokenType.equalsIgnoreCase(TOKEN_TYPE_CHALLENGE_RESPONSE)) {
				accessRequest.setAttribute(
					new RadiusAttribute(
						RadiusAttributeValues.USER_PASSWORD,
						getStaticPassword().getBytes()));
			}
			else {
				String tokenId = getSessionVariable(request, TOKEN_ID);

				accessRequest.setAttribute(
					new RadiusAttribute(
						RadiusAttributeValues.USER_PASSWORD,
						tokenId.getBytes()));
			}
		}
		catch (InvalidParameterException e) {
			_log.error(
				"Invalid parameter when setting the users details in the Radius packet. " +
					e.getMessage(),
				e);
		}

		return accessRequest;
	}

	/**
	 *
	 * @param rp <em>RadiusPacket</em>
	 * @return
	 */
	public String getReplyMessage(RadiusPacket rp) {
		for (@SuppressWarnings("rawtypes")
		Iterator i = rp.getAttributes(
		).iterator(); i.hasNext();) {

			RadiusAttribute radiusAttribute = (RadiusAttribute)i.next();

			if (radiusAttribute.getType() ==
					RadiusAttributeValues.REPLY_MESSAGE)

				return new String(radiusAttribute.getValue());
		}

		return null;
	}

	/**
	 *
	 * @param rp <em>RadiusPacket</em>
	 * @return
	 */
	public String getChallenge(RadiusPacket rp) {
		String replyMessage = getReplyMessage(rp);

		String[] parts = replyMessage.split(" ");

		for (int i = parts.length - 1; i >= 0; i--) {
			try {
				String part = parts[i].trim();

				Long.parseLong(part);

				return part;
			}
			catch (NumberFormatException e) {
				_log.error(
					"Could not parse the challenge. " + e.getMessage(), e);
			}
		}

		return replyMessage;
	}

	/**
	 * Return the stored token type from the user's custom fields in liferay.
	 *
	 * @param request <em>PortletRequest</em>
	 * @return tokenType <em>String</em>
	 */
	public String getTokenType(PortletRequest request) {
		User user = null;

		String requestedTokenType = getSessionVariable(
			request, REQUESTED_TOKEN_TYPE);

		if ((requestedTokenType != null) && !requestedTokenType.isEmpty()) {
			return requestedTokenType;
		}

		try {
			user = PortalUtil.getUser(request);
		}
		catch (PortalException e) {
			_log.error(
				"Could not get the user from the request. " + e.getMessage(),
				e);
		}
		catch (SystemException e) {
			_log.error(
				"Server error when trying to retrieve the user from the request. " +
					e.getMessage(),
				e);
		}

		requestedTokenType = getTokenType(user);

		return requestedTokenType;
	}

	/**
	 * Return the stored token type from the user's custom fields in liferay. If there is none then return Response Only.
	 *
	 * @param user
	 * @return String refer to <em>TOKEN_TYPE_RESPONSE_ONLY</em> and <em>TOKEN_TYPE_CHALLENGE_RESPONSE</em>
	 */
	public String getTokenType(User user) {
		ExpandoBridge exBrg = null;

		String tokenType = TOKEN_TYPE_RESPONSE_ONLY;

		if (user != null) {
			try {
				exBrg = user.getExpandoBridge();
				Serializable attributeTokenType = exBrg.getAttribute(
					TOKEN_TYPE_ATTRIBUTE_NAME, false);

				tokenType =
					(attributeTokenType == null) ? TOKEN_TYPE_RESPONSE_ONLY :
						attributeTokenType.toString();
			}
			catch (Exception e) {
				_log.warn(
					"Could not find ExpandoBridge for user " +
						(user != null ? user.getScreenName() :
							"[user is null]") + ": " + e.getMessage() +
								", will default user to " + tokenType,
					e);
			}
		}

		return tokenType;
	}

	/**
	 * Retrieve the logged in users screen name.
	 *
	 * @param portletRequest <em>PortletRequest</em>
	 * @return userScreenName <em>String</em>
	 */
	public String getUserScreenName(PortletRequest portletRequest) {
		User user = null;
		String userID = "";

		try {
			user = PortalUtil.getUser(portletRequest);
		}
		catch (PortalException e) {
			_log.error(
				"Could not get the user from the request. " + e.getMessage(),
				e);
		}
		catch (SystemException e) {
			_log.error(
				"Server error when trying to retrieve the user from the request. " +
					e.getMessage(),
				e);
		}

		if (user != null) {
			userID = user.getScreenName();
		}

		return userID;
	}

	/**
	 * Get the static password for the portlet.
	 *
	 * @return staticPassword <em>String</em>
	 */
	public String getStaticPassword() {
		return StaticPassword;
	}

	/**
	 * Set the static password for the portlet.
	 *
	 * @param staticPassword <em>String</em>
	 */
	public void setStaticPassword(String staticPassword) {
		StaticPassword = staticPassword;
	}

	/**
	 * @return the radiusServerSharedKey
	 */
	public String getRadiusServerSharedKey() {
		return RadiusServerSharedKey;
	}

	/**
	 * @param radiusServerSharedKey
	 *            the radiusServerSharedKey to set
	 */
	public void setRadiusServerSharedKey(String radiusServerSharedKey) {
		RadiusServerSharedKey = radiusServerSharedKey;
	}

	/**
	 * @return the radiusServerAccPort
	 */
	public String getRadiusServerAccPort() {
		return RadiusServerAccPort;
	}

	/**
	 * @param radiusServerAccPort
	 *            the radiusServerAccPort to set
	 */
	public void setRadiusServerAccPort(String radiusServerAccPort) {
		RadiusServerAccPort = radiusServerAccPort;
	}

	/**
	 * @return the radiusServerAuthPort
	 */
	public String getRadiusServerAuthPort() {
		return RadiusServerAuthPort;
	}

	/**
	 * @param radiusServerAuthPort
	 *            the radiusServerAuthPort to set
	 */
	public void setRadiusServerAuthPort(String radiusServerAuthPort) {
		RadiusServerAuthPort = radiusServerAuthPort;
	}

	/**
	 * @return the radiusServerName
	 */
	public String getRadiusServerName() {
		return RadiusServerName;
	}

	/**
	 * @param radiusServerName
	 *            the radiusServerName to set
	 */
	public void setRadiusServerName(String radiusServerName) {
		RadiusServerName = radiusServerName;
	}

}