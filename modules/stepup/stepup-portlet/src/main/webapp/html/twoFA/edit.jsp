<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %><%@
taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<%@ page import="com.liferay.petra.string.StringPool" %>

<%@ page import="javax.portlet.PortletPreferences" %>

<portlet:defineObjects />

Vasco Identikey settings : <br />

<%
PortletPreferences prefs = renderRequest.getPreferences();

String RadiusServerSharedKey = prefs.getValue("Radius.Server.Shared.Key", StringPool.BLANK);
String StaticPassword = prefs.getValue("Static.Password", StringPool.BLANK);
%>

<form
	action="<portlet:actionURL secure="<%= request.isSecure() %>"><portlet:param name="actionName" value="savePrefs" /></portlet:actionURL>"
	id="theform"
	method="POST"
	name="form"
>
	<table>
		<tr>
			<td><label for="RadiusServerName"> Radius Server Host Name </label></td>
			<td><input type="text" name="RadiusServerName"
				id="RadiusServerName" value="<%= prefs.getValue("Radius.Server.Name", StringPool.BLANK) %>" size="30"/></td>
		</tr>
		<tr>
			<td><label for="RadiusServerAuthPort"> Authentication
					Port</label></td>
			<td><input type="text" name="RadiusServerAuthPort"
				id="RadiusServerAuthPort" value="<%= prefs.getValue("Radius.Server.Auth.Port", StringPool.BLANK) %>" " size="30" /></td>
		</tr>
		<tr>
			<td><label for="RadiusServerAcchPort"> Accounting
					Port</label></td>
			<td><input type="text" name="RadiusServerAccPort"
				id="RadiusServerAccPort" value="<%= prefs.getValue("Radius.Server.Acc.Port", StringPool.BLANK) %>" " size="30" /></td>
		</tr>
		<tr>
			<td><label for="RadiusServerSharedKey"> Radius Shared
					Key</label></td>
			<td><input type="password" name="RadiusServerSharedKey"
				id="RadiusServerSharedKey" value="****" size="30" /></td>
		</tr>
		<tr>
			<td><label for="StaticPassword"> Static Password
					</label></td>
			<td><input type="password" name="StaticPassword"
				id="StaticPassword" value="****" size="30" /></td>
		</tr>
		<tr>
			<td colspan=2><input class="submit" type="submit" name="submit"
				value="Save Settings" /></td>
		</tr>
	</table>
</form>