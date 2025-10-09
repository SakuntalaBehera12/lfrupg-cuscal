
<%

/**
 * Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
%>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<portlet:defineObjects />

<div class="resetPassword">
	<h3>
		<liferay-ui:message key="resetPassword.passwordreset.label" />
	</h3>

	<span><liferay-ui:message key="manage_account_cpw_msg" /> </span>
	<br />

	<%
	if ((request.getAttribute("invalidPassword") != null) && (Boolean)request.getAttribute("invalidPassword")) {
	%>

		<span class="portlet-msg-error"><liferay-ui:message
			key="resetPassword.invalidPassword.label" /> </span> <br />
	<%} %>

	<%
	if ((request.getAttribute("noMatchPassword") != null) && (Boolean)request.getAttribute("noMatchPassword")) {
	%>

		<span class="portlet-msg-error"><liferay-ui:message
			key="resetPassword.noMatchPassword.label" /> </span> <br />
	<%} %>

	<%
	if ((request.getAttribute("success") != null) && (Boolean)request.getAttribute("success")) {
	%>

	<span class="portlet-msg-info"><liferay-ui:message
			key="resetPassword.updatePassword.msg" /> </span> <br />
	<%} %>

	<form
		action="<portlet:actionURL />"
		class="accountManagementForm"
		method="post"
		name="<portlet:namespace />resetPassword"
	>
		<dl>
			<dt>
				<label for="newPassword"><liferay-ui:message key="resetPassword.newPassword.label" /></label>
			</dt>
			<dd>
				<input id="newPassword" name="newPassword" type="password" value="" />
			</dd>
			<dt>
				<label for="retypePassword"><liferay-ui:message key="resetPassword.retypePassword.label" /></label>
			</dt>
			<dd>
				<input id="retypePassword" name="retypePassword" type="password" value="" />
			</dd>
			<dt></dt>
			<dd>
				<button
					onclick="setUserId('hiddenuserIdForm2','email')"
					type="submit"
				>
					<span>Reset Password</span>
				</button>

				<input
					id="fromPage"
					name="fromPage"
					type="hidden"
					value="passRecovery_retypePage"
				/>

				<input
					id="ticket"
					name="ticket"
					type="hidden"
					value="<%= request.getAttribute("ticket") %>"
				/>
			</dd>
		</dl>
	</form>
</div>