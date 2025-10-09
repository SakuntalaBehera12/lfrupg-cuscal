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

<div id="content">
	<div id="contentBody">
		<p>
		<!-- <strong><liferay-ui:message key="resetPassword.passwordRecovery.label" /></strong>-->
		<h2>Password Recovery</h2>
		</p>

		<p>
		<h3>The details you supplied were incorrect. Either we don't have a record of your User ID or your response to one or more of the questions was incorrect.<h3 />
		</p>

		<p><!--<liferay-ui:message key="resetPassword.infoMsg" />-->Please enter your User ID and answer the secret questions below so that we can confirm your identity.</p>

		<form action="<portlet:actionURL />" method="post" name="<portlet:namespace />resetPassword">
		<div>
			<!-- <label for="userId"><liferay-ui:message key="resetPassword.userId.label" /></label>-->
			<b>User ID</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="userId" name="userId" type="text" value="" />

			<p><br /><br />
			<!-- <liferay-ui:message key="resetPassword.secretQuestions.label" />-->
			<b>Secret Questions</b>
			</p>
			<!--<liferay-ui:message key="resetPassword.question1.label" />

			<liferay-ui:message key="resetPassword.question1.msg" />-->
			<b>Question 1:</b> What is your favourite Australian holiday destination?
			<br />

			<b><label for="answer1"><liferay-ui:message key="resetPassword.answer.label" /></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>

			<input id="answer1" name="answer1" type="text" value="" />

			<br />
			<!--<liferay-ui:message key="resetPassword.question2.label" />

			<liferay-ui:message key="resetPassword.question2.msg" />-->
			<b>Question 2</b> Who is your favourite musician?<br />

			<b><label for="answer2"><liferay-ui:message key="resetPassword.answer.label" /></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>

			<input id="answer2" name="answer2" type="text" value="" />

			<br /><br />
			<input id="fromPage" name="fromPage" type="hidden" value="passRecovery_errorPage" />
		</div>

		<div>
			<input id="submit" name="submit" style="background-color: #F88017;" type="submit" value="Recover Password" />
		</div>
		</form>
	</div>
</div>