
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

<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<portlet:defineObjects />
	<div class="resetPassword">
		<h3>
			<liferay-ui:message key="resetPassword.passwordRecovery.label" />
		</h3>

		<%
		if ((request.getAttribute("errorMsg") != null) && (Boolean)request.getAttribute("errorMsg")) {
		%>

		<span class="portlet-msg-error"><liferay-ui:message
				key="resetPassword.userIdInvalid.msg"
			/>
		</span> <br />

		<%
		}
		%>

		<label><liferay-ui:message key="resetPassword.infoMsg" /></labe>

		<form action="<portlet:actionURL />" class="accountManagementForm" method="post" name="<portlet:namespace />resetPassword">
			<dl>
				<dt>
					<label for="userId"><liferay-ui:message key="user.account.user.id" /></label>
				</dt>
				<dd>
					<input autocomplete="off" id="userId" name="userId" type="text" value="" />
				</dd>
				<dt>
					<label><liferay-ui:message key="user.account.secret.question.1.label" /></label>
				</dt>
				<dd>
					<liferay-ui:message key="user.account.secret.question.1" />
				</dd>
				<dt>
					<label for="answer1"><liferay-ui:message key="user.account.secret.question.answer.label" /></label>
				</dt>
				<dd>
					<input autocomplete="off" id="answer1" name="answer1" type="text" value="" />
				</dd>
				<dt>
					<label><liferay-ui:message key="user.account.secret.question.2.label" /></label>
				</dt>
				<dd>
					<liferay-ui:message key="user.account.secret.question.2" />
				</dd>
				<dt>
					<label for="answer2"><liferay-ui:message key="user.account.secret.question.answer.label" /></label>
				</dt>
				<dd>
					<input autocomplete="off" id="answer2" name="answer2" type="text" value="" />
				</dd>
				<dt></dt>
				<dd>
					<button type="submit"><span>Submit Request</span></button>
					<input id="fromPage" name="fromPage" type="hidden" value="reset" />
				</dd>
			</dl>
		</form>
</div>