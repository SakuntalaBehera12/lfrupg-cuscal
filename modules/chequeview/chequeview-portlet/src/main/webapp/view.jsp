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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<portlet:defineObjects />

<div id="wait">
	<p>Please wait while we redirect you...</p>
</div>

<div id="<portlet:namespace />Content" style="display: none;">
	<form action="<portlet:actionURL />" id="afc" method="post">
		<p>This is the <b>ChequeViewPortlet</b> portlet</p>
		<p>If you can read this then <strong>Toggle Edit Controls</strong> is checked. </p>
		<p>To test please uncheck <strong>Toggle Edit Controls</strong> and refresh the page.</p>
		<p>
			<strong>ActionURL: </strong><portlet:actionURL />
		</p>
	</form>
</div>

<script type="text/javascript">
	var toggle = $(".toggle-controls span svg");

	if (toggle.length == 0 || toggle.hasClass("lexicon-icon-hidden")) {
		$('#afc').submit();
	} else {
		$('#<portlet:namespace />Content').show();
	}
</script>