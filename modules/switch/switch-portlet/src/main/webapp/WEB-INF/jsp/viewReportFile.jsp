<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@
taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %><%@
taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.URLCodec" %>

<%@ page contentType="text/html" isELIgnored="false" %>

<portlet:defineObjects />

<portlet:resourceURL escapeXml="false" var="viewReportBlobData">
			<portlet:param name="id" value="${blobFileId}" />
			<portlet:param name="rd" value="${runDate}" />
			<portlet:param name="type" value="view" />
</portlet:resourceURL>

<script charset="utf-8" type="text/javascript">

jQuery(document).ajaxStart(function() {
	jQuery("#loading").show();
}).ajaxStop(function() {
	jQuery("#loading").hide();
});

var windowWidth;
//offsetSize is the main wrapper margin + header margin + scrollbar width.
var offsetSize = 64;
if (Liferay.Browser.isIe()) {
	windowWidth = document.body.offsetWidth;
	offsetSize = 52;
} else {
	windowWidth = window.innerWidth;
}
windowWidth = windowWidth - offsetSize;
windowWidth = windowWidth + 'px';

function replaceAll(strMain, strFind, strRep) {
	  return strMain.replace(new RegExp(strFind, 'g'),strRep);
}

var urlTemp = "<c:out value="${viewReportBlobData}" />" ;
var urlFinal=replaceAll(urlTemp,"&amp;","&");

Liferay.on('allPortletsReady',function() {
	if (typeof setupLoader !== "undefined") setupLoader();
	jQuery('#filedata').css('width', windowWidth);
	jQuery('#filedata').load(urlFinal);
});
</script>

<form:form action="" id="reportView" method="post" name="reportView">

<div id="report-disclaimer-wrapper">
	<h1><spring:message code="report.heading.text" /></h1>
<p><fmt:formatDate pattern="dd-MM-yyyy" value="${reportDate}" />&nbsp;&nbsp;<c:out value="${fileName}" /></p>
<a href="<%= URLCodec.decodeURL(ParamUtil.getString(renderRequest, "_backURL")) %>" id="backList">Back to report list</a>
<br /><br />

<pre class="preTag" id="filedata">

</pre>

</div>

</form:form>