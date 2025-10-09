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

<div id="content">
	<div id="contentBody">
		<form action="<portlet:actionURL />" method="post" name="<portlet:namespace />resetPassword">
			<div>
				<!-- <label for="userId"><liferay-ui:message key="resetPassword.userId.label" /></label>-->
				<b>User ID:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<!-- <input id="userId" name="userId" type="text" value='' /> -->
				<aui:input autocomplete=off id="userId" name="userId" type="text" value=''></aui:input>
				<br /><br />

				<b>Password:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<!-- <input id="userId" name="userId" type="password" value='' /> -->
				<aui:input autocomplete=off id="userId" name="userId" type="password" value=''></aui:input>
				<br /><br />
			</div>

			<div>
				<input id="submit" name="submit" style="background-color: #F88017;" type="submit" value="LOGIN" />
			</div>
		</form>

		<u>
			<a href="<portlet:renderURL><portlet:param name="jspPage" value="/html/resetpassword/view.jsp" /><portlet:param name="n" value="forgot" /></portlet:renderURL> ">Forgot Password</a>
		</u>
	</div>
</div>