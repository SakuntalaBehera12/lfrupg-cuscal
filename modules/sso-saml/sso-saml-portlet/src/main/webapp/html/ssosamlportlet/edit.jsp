<%--
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
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<portlet:defineObjects />

<h2>SSO SAML Configuration</h2>
<br />
<form action="<portlet:actionURL />" method="post" name="<portlet:actionURL />configFrm">
	<input name="<portlet:namespace />action" size="80" type="hidden" value="save" />
	<label>Property file:</label> <input name="<portlet:namespace />propertyFile" size="80" value="<c:out value="${propertyFile}" />" />

	<br />
	<br />
	<input type="submit" />
</form>