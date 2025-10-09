<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %><%@
taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<%@ page contentType="text/html" isELIgnored="false" %>
<%@ page import="com.cuscal.security.*" %><%@
page import="com.cuscal.security.twoFA" %>

<%@ page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.model.Organization" %><%@
page import="com.liferay.portal.kernel.service.OrganizationLocalServiceUtil" %>

<%@ page import="javax.portlet.*,java.util.*" %><%@
page import="javax.portlet.PortletPreferences" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
PortletPreferences prefs = renderRequest.getPreferences();
Object stepupObj = portletSession.getAttribute("_USER_STEPPEDUP", PortletSession.APPLICATION_SCOPE);
Object returnURLObj = portletSession.getAttribute("_RETURN_URL", PortletSession.APPLICATION_SCOPE);
String steppedUp = "false", returnURL = "/web/guest", challengeKey = "";
String tokenType = "";

boolean userLoggedIn = false;

twoFA twoFa = new twoFA();

if (!((stepupObj == null) || (returnURLObj == null))) {
	try {
		steppedUp = stepupObj.toString();
		returnURL = returnURLObj.toString();
	}
	catch (Exception e) {
		System.err.println(e.getMessage());
	}
}

String submitCaption = "Step up";

if (themeDisplay.getTheme(
	).getName(
	).equalsIgnoreCase(
		"vasco-theme"
	))
	submitCaption = "";

if ((user == null) ||
	user.getEmailAddress(
	).equalsIgnoreCase(
		"default@liferay.com"
	)) {

	userLoggedIn = false;
}
else {
	userLoggedIn = true;

	tokenType = twoFa.getTokenType(renderRequest);

	if (request.getAttribute("challengeKey") != null) {
		Object objChallengeKey = request.getAttribute("challengeKey");

		challengeKey = objChallengeKey.toString();
	}
}

String userOrg = "";
List<Organization> organizations = OrganizationLocalServiceUtil.getUserOrganizations(user.getUserId());

if ((organizations != null) && (organizations.size() > 0)) {
	userOrg = organizations.get(
		0
	).getName();
}
%>

<script type="text/javascript">
	/* use in no conflict mode */
	jQuery.noConflict();

	function doSubmit(url, actionType) {

		var isValid = false;

		if (actionType == "challenge") {
			jQuery("#action-name").val("stepUp");
			jQuery("#token-type").val("<%= twoFA.TOKEN_TYPE_CHALLENGE_RESPONSE %>");
			if (validateFieldForNotEmpty("token-challenge")) {
				isValid = true;
			} else {
				jQuery(".error").html("Please enter a token response and click Continue.");
				jQuery(".error").show();
			}
		} else if (actionType == "respOnly") {
			jQuery("#action-name").val("stepUp");
			jQuery("#token-type").val("<%= twoFA.TOKEN_TYPE_RESPONSE_ONLY %>");
			if (validateFieldForNotEmpty("token-response")) {
				isValid = true;
			} else {
				jQuery(".error").html("Please enter a token response and click Continue.");
				jQuery(".error").show();
			}
		} else if (actionType == "geneToken") {
			jQuery("#action-name").val("generate");
			jQuery("#token-type").val('<c:out value="${tokenType}" />');
			isValid = true;
		} else if (actionType == "cancel") {
			jQuery("#action-name").val("stepUpCancel");
			jQuery("#token-type").val('<c:out value="${tokenType}" />');
			isValid = true;
		}

		if (isValid) {
			jQuery("#theform").attr("action", url);
			jQuery("#theform").submit();
		}

		return isValid;
	}

	/**
	* Validate the fields and check if they are empty or not.
	*/
	function validateFieldForNotEmpty(fieldId) {
		var fieldVal = jQuery("#" + fieldId).val();
		if(fieldVal == "") {
			return false;
		} else {
			return true;
		}
	}

	var stepUpType = '<c:out value="${tokenType}" />';
	var error = <%= (Objects.equals(stepupObj, "error") && request.getAttribute("stepUpMsg") != null)? true : false %>

	/**
	* Check if what the user has selected.
	*/
	function challOrResp(itemClicked) {

		if (itemClicked == "login") {
			jQuery('.home').removeClass("selected");
			jQuery('.login').addClass("selected");
			jQuery("#response-only").hide();
			jQuery("#challenge-response").show();
			jQuery("#token-type").val("<%= twoFA.TOKEN_TYPE_CHALLENGE_RESPONSE %>");
			jQuery("#token-challenge").focus();
			jQuery("#login-response").hide();
			jQuery("#login-challenge").show();
			<%-- TODO if (stepUpType == "<%= twoFA.TOKEN_TYPE_CHALLENGE_RESPONSE %>" && error) {
				jQuery(".error").show();
			} else {
				jQuery(".error").hide();
			} --%>
		} else if (itemClicked == "home") {
			jQuery('.login').removeClass("selected");
			jQuery('.home').addClass("selected");
			jQuery("#challenge-response").hide();
			jQuery("#response-only").show();
			jQuery("#challenge-key").val("");
			jQuery("#token-type").val("<%= twoFA.TOKEN_TYPE_RESPONSE_ONLY %>");
			jQuery("#token-response").focus();
			jQuery("#login-challenge").hide();
			jQuery("#login-response").show();
			<%-- TODO if (stepUpType == "<%= twoFA.TOKEN_TYPE_RESPONSE_ONLY %>" && error) {
				jQuery(".error").show();
			} else {
				jQuery(".error").hide();
			} --%>
		}
	}
</script>

<c:if test='<%= tokenType.equalsIgnoreCase("Challenge/Response") %>'>
	<c:set value="true" var="isChallenge" />
</c:if>

<portlet:actionURL var="formSubmit" />

<c:choose>
	<c:when test="<%= !Objects.equals(stepupObj, StringPool.BLANK) && !Objects.equals(returnURL, StringPool.BLANK) && userLoggedIn %>">
		<ul class="menu">
			<li class="login" onclick="javascript:challOrResp(this.className);"></li>
			<li class="home selected" onclick="javascript:challOrResp(this.className);"></li>
		</ul>

		<div class="forms">
			<%-- <h5>Please select your token type and enter your token details to continue</h5> --%>

		<h5>Please select the type of token you have, enter the token response and then click on the 'Continue' button.</h5>

<h5 style="color: red;">Note: You can't enter the token response and hit the enter/return key on your keyboard. You must click on the Continue button on the screen.</h5>

			<form autocomplete="off" id="theform" method="POST" name="theform">
				<div class="fields">
					<label>User ID:</label>

					<span><c:out value="${user.screenName}" /></span>
				</div>

				<div class="fields">
					<label>Name:</label>

					<span><c:out value="${user.fullName}" /></span>
				</div>

				<div class="fields">
					<label>Organisation:</label>

					<span><%= userOrg %></span>
				</div>

				<div class="fields">
					<label>Email:</label>

					<span><c:out value="${user.emailAddress}" /></span>
				</div>

				<div id="response-only">
					<div class="fields">
						<label for="token-response">Token Response:</label>

						<input autocomplete="off" id="token-response" name="respTokenID" type="password" />
					</div>
				</div>

				<%-- This is only for the Challenge Response --%>
				<div id="challenge-response">
					<div class="fields">
						<label>Token Challenge:</label>

						<span class="challenge-key"><%= challengeKey %></span>

						<input onclick="javascript:return doSubmit('<%= formSubmit %>', 'geneToken');" type="submit" value="Regenerate" />
					</div>

					<div class="fields">
						<label for="token-challange">Token Response:</label>

						<input autocomplete="off" id="token-challenge" name="tokenID" type="password" />
					</div>
				</div>

				<div class="fields">
					<input id="challenge-key" name="challengeKey" type="hidden" value="<%= challengeKey %>" />
					<input id="action-name" name="actionName" type="hidden" value="" />
					<input id="token-type" name="tokenType" type="hidden" value="" />
					<label>&nbsp;</label>

					<input id="login-response" name="submit" onclick="javascript:return doSubmit('<%= formSubmit %>', 'respOnly');" type="submit" value="Continue" />
					<input id="login-challenge" name="submit" onclick="javascript:return doSubmit('<%= formSubmit %>', 'challenge');" type="submit" value="Continue" />

					<input name="cancel" onclick="javascript:return doSubmit('<%= formSubmit %>', 'cancel');" type="submit" value="Cancel" />
				</div>

				<c:choose>
					<c:when test="${ isChallenge == true }">
						<script type="text/javascript">
							challOrResp("login");
						</script>
					</c:when>
					<c:otherwise>
						<script type="text/javascript">
							challOrResp("home");
						</script>
					</c:otherwise>
				</c:choose>

				<div class="error">
					<c:if test='<%= Objects.equals(stepupObj, "error") && (request.getAttribute("stepUpMsg") != null) %>'>
						<%= request.getAttribute("stepUpMsg") %>

						<%
						request.setAttribute("stepUpMsg", "");
						%>

					</c:if>
				</div>
			</form>

			<div class="important-info">
				<p align="left">
					If you require access or have lost your token please call Cuscal CallDirect on 1300 650 501 or email <a href="mailto:info@cuscal.com.au" class="links">info@cuscal.com.au</a> providing your name, company and the information you require.
				</p>

				<p>
					The material contained in this website is copyright of Cuscal Limited and may only be used for the purpose intended and must not be made available to third parties. Access to this site is discretionary and is subject to Cuscal's <a href="/cuscal-connect-terms-and-conditions" class="links">terms and conditions</a>. Cuscal reserves the right to withdraw access at any time.
				</p>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<p>You need to be logged in to access this page. <a href="/c/portal/login">Please login first</a>.</p>
	</c:otherwise>
</c:choose>