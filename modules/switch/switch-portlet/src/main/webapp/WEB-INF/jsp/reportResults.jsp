<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@
taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@
taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://java.sun.com/portlet" prefix="liferay-portlet" %>

<%@ taglib uri="http://liferay.com/tld/journal" prefix="liferay-journal" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %><%@
taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<%@ page import="au.com.cuscal.connect.forms.ReportForm" %>

<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.URLCodec" %>

<%@ page import="javax.portlet.PortletSession" %>

<%@ page contentType="text/html" isELIgnored="false" %>

<portlet:defineObjects />

<portlet:renderURL escapeXml="false" var="renderURL" />

<%
int _page = ParamUtil.getInteger(request, "page", 1);
String _backURL = renderURL.toString() + "&rr=showReportSuccess&implicitMode=true&page=" + _page;
%>

<portlet:actionURL var="showReportOnClear">
	<portlet:param name="reportAction" value="showDefaultReport" />
</portlet:actionURL>

<portlet:actionURL escapeXml="false" var="showReportList">
	<portlet:param name="reportAction" value="sendReportRequest" />
</portlet:actionURL>

<c:if test="${fromFA==true}">
	<portlet:resourceURL escapeXml="false" var="steppedUpDownload">
		<portlet:param name="id" value="${id}" />
		<portlet:param name="rd" value="${rd}" />
		<portlet:param name="type" value="ws" />
	</portlet:resourceURL>
</c:if>

<script charset="utf-8" type="text/javascript">

	Liferay.on('allPortletsReady',function() {

		if (typeof setupLoader !== "undefined") setupLoader();
		jQuery('.date-pick').datePicker({
			clickInput : true,
			createButton : false,
			startDate : "01/01/2000",
			showYearNavigation : false
		});

		jQuery("a.download-Pdf-Text").live("click", function() {
			jQuery("#loading").show();
			jQuery.fileDownload(jQuery(this).attr('href'), {
				successCallback : function(url) {
					jQuery("#loading").hide();
				},
				failCallback : function(responseHtml, url) {
					jQuery("#loading").hide();
				}
			});
			return false;
		});

		jQuery(document).ajaxStart(function() {
			jQuery("#loading").show();
		}).ajaxStop(function() {
			jQuery("#loading").hide();
		});
	});

	var ifFaPage = '<c:out value="${fromFA}" />';

	if (ifFaPage == 'true') {

		Liferay.on('allPortletsReady', function() {

			var link = '<c:out value="${steppedUpDownload}" />';
			link = replaceAll(link, "&amp;", "&");

			jQuery("#loading").show();
			jQuery.fileDownload(link, {
				successCallback : function(url) {
					jQuery("#loading").hide();
				},
				failCallback : function(responseHtml, url) {
					jQuery("#loading").hide();
				}
			});
			return false;
		});
	}
</script>

<%
String articleId = request.getAttribute(
	"articleId"
).toString();
String groupId = request.getAttribute(
	"groupId"
).toString();

if (session.getAttribute("_USER_STEPPEDUP") != null) {
	request.setAttribute("steppedUp", (String)session.getAttribute("_USER_STEPPEDUP"));
}
else if (portletSession.getAttribute("_USER_STEPPEDUP", PortletSession.APPLICATION_SCOPE) != null) {
	request.setAttribute("steppedUp", (String)portletSession.getAttribute("_USER_STEPPEDUP", PortletSession.APPLICATION_SCOPE));
}
%>

<div id="report-disclaimer-wrapper">
	<h1><spring:message code="report.heading.text" /></h1>

	<br />
<liferay-journal:journal-article articleId="${articleId}" groupId="${groupId}" />

</div>

<form:form action="${showReportList}" id="reportForm" method="post" modelAttribute="reportForm" name="reportForm" onSubmit="javascript:filterByDates('${showReportList}');">
	<c:if test="${not empty _reportForm}">
		<c:set value="${_reportForm}" var="reportForm" />
	</c:if>

	<%
	ReportForm _rf = (ReportForm)request.getAttribute("reportForm");

	if (_rf == null) {
		_rf = (ReportForm)renderRequest.getAttribute("reportForm");
	}

	if (_rf == null) {
		_rf = (ReportForm)renderRequest.getAttribute("_reportForm");
	}

	if (_rf != null) {
		_backURL += "&customerName=" + (_rf.getCustomerName() != null ? _rf.getCustomerName() : "");
		_backURL += "&fromDate=" + (_rf.getFromDate() != null ? _rf.getFromDate() : "");
		_backURL += "&toDate=" + (_rf.getToDate() != null ? _rf.getToDate() : "");
		_backURL += "&reportName=" + (_rf.getReportName() != null ? _rf.getReportName() : "");
		_backURL += "&customerBin=" + (_rf.getCustomerBin() != null ? _rf.getCustomerBin() : "");
		_backURL += "&pageSize=" + (_rf.getPageSize() > 0 ? _rf.getPageSize() : 1);
	}
	%>

	<div class="filter-wrapper">
		<div class="filter-elements">
			<div class="customer-name">
				<label for="customerName">Report Owner</label>

				<form:input autocomplete="off" class="customerName" id="customerName" path="customerName" readonly="false" value="${reportForm.customerName}" />

				<br />

				<form:errors cssClass="error" path="customerName" />
			</div>

			<div class="from-date">
				<label for="fromDate">From Date<em>*</em> </label>
				<form:input autocomplete="off" class="date-pick" id="fromDate" path="fromDate" readonly="true" value="${reportForm.fromDate}" />

				<br />

				<form:errors cssClass="error" path="fromDate" />
			</div>

			<div class="to-date">
				<label for="toDate">To Date<em>*</em> </label>
				<form:input autocomplete="off" class="date-pick" id="toDate" path="toDate" readonly="true" value="${reportForm.toDate}" />

				<br />

				<form:errors cssClass="error" path="toDate" />
			</div>

			<br />

			<div class="report-name">
				<label for="reportName">Report Name</label>

				<form:input autocomplete="off" class="reportName" id="reportName" path="reportName" readonly="false" value="${reportForm.reportName}" />

				<br />

				<form:errors cssClass="error" path="reportName" />
			</div>

			<div class="customer-bin">
				<label for="customerBin">Report Bin</label>

				<form:input autocomplete="off" class="customerBin" id="customerBin" path="customerBin" readonly="false" value="${reportForm.customerBin}" />

				<br />

				<form:errors cssClass="error" path="customerBin" />
			</div>

			<div class="page-size">
				<label for="pageSize">Reports per page</label>

				<form:select id="pageSize" path="pageSize">
					<form:option label="10" value="10" />
					<form:option label="25" value="25" />
					<form:option label="50" value="50" />
					<form:option label="100" value="100" />
				</form:select>

				<br />

				<form:errors cssClass="error" path="pageSize" />
			</div>

			<br />

			<input class="button" type="submit" value="Search" />
			<input onclick="javascript:clearAll('${showReportOnClear}');" type="button" value="Reset" />
		</div>
	</div>
</form:form>

<br />

<c:if test="${showDatePast18MonthMsg}">
	<div class="portlet-msg-info">
		<span><spring:message code="switch.reports.fromDate.18month" /></span>
	</div>
</c:if>

<display:table class="cuscalTable" name="reportList" requestURI="${renderURL}" uid="reportFrm">
	<display:setProperty name="basic.msg.empty_list">
		<div class="portlet-msg-info">
			<span>${noDataFoundMsg}</span>
		</div>
	</display:setProperty>

	<display:setProperty
		name="factory.requestHelper"
		value="org.displaytag.portlet.PortletRequestHelperFactory"
	/>

	<display:setProperty name="paging.banner.page.separator">
	</display:setProperty>

	<display:setProperty name="paging.banner.placement">both</display:setProperty>

	<display:setProperty name="paging.banner.item_name">result</display:setProperty>
	<display:setProperty name="paging.banner.items_name">results</display:setProperty>

	<display:setProperty name="paging.banner.all_items_found">
		<span class="pagination-text">Displaying results 1-{0} of {0}</span>
	</display:setProperty>

	<display:setProperty name="paging.banner.some_items_found">
		<span class="pagination-text">Displaying results {2}-{3} of {0}</span>
	</display:setProperty>

	<display:setProperty name="paging.banner.first">
		<span class="pagination-links"> {0} <a href="{3}">&gt;</a> </span>
	</display:setProperty>

	<display:setProperty name="paging.banner.last">
		<span class="pagination-links"> <a href="{2}">&lt;</a> {0} </span>
	</display:setProperty>

	<display:setProperty name="paging.banner.onepage">
		<span class="pagination-links"> {0} </span>
	</display:setProperty>

	<display:setProperty name="paging.banner.full">
		<span class="pagination-links"><a href="{2}">&lt;</a> {0} <a href="{3}">&gt;</a> </span>
	</display:setProperty>

	<display:column property="reportOwnerName" title="Owner" />

	<display:column>
		<fmt:formatDate pattern="dd/MM/yyyy" value="${reportFrm.runDate}" var="rDate" />

		<portlet:renderURL var="viewReportBlobData">
			<portlet:param name="id" value="${reportFrm.blobFileId}" />
			<portlet:param name="rd" value="${rDate}" />
			<portlet:param name="rr" value="rv" />
			<portlet:param name="_backURL" value="<%= URLCodec.encodeURL(_backURL) %>" />
		</portlet:renderURL>

		<c:if test="${fn:toLowerCase(reportFrm.fileType) != fn:toLowerCase('pdf') and not reportFrm.isEncrypted}">
			<c:choose>
				<c:when test="${reportFrm.fileSize ge reportFrm.maxFileSize}">
					<a href="${viewReportBlobData}" id="viewReports">View*</a>
				</c:when>
				<c:otherwise>
					<a href="${viewReportBlobData}" id="viewReports">View</a>
				</c:otherwise>
			</c:choose>
		</c:if>
	</display:column>

	<display:column title="Download">
		<fmt:formatDate pattern="dd/MM/yyyy" value="${reportFrm.runDate}" var="rDate" />

		<c:choose>
			<c:when test="${reportFrm.isEncrypted}">
				<c:choose>
					<c:when test="${steppedUp eq 'true'}">
						<portlet:resourceURL var="downloadReportBlobDataEncrypted">
							<portlet:param name="id" value="${reportFrm.blobFileId}" />
							<portlet:param name="rd" value="${rDate}" />
							<portlet:param name="type" value="ws" />
						</portlet:resourceURL>

						<a class="download-Pdf-Text csv-file" href="${downloadReportBlobDataEncrypted}" title="Download Encrypted file">
							<c:choose>
								<c:when test="${not empty reportFrm.fileType}">
									<c:out value="${fn:toUpperCase(reportFrm.fileType)}" />
								</c:when>
								<c:otherwise>
									<span>Unknown</span>
								</c:otherwise>
							</c:choose>
						</a>
					</c:when>
					<c:otherwise>
						<portlet:actionURL var="stepUpAndDownload">
							<portlet:param name="id" value="${reportFrm.blobFileId}" />
							<portlet:param name="rd" value="${rDate}" />
							<portlet:param name="type" value="ws" />
							<portlet:param name="reportAction" value="2FAPage" />
						</portlet:actionURL>

						<a class="csv-file" href="${stepUpAndDownload}" title="Download Encrypted file">
							<c:choose>
								<c:when test="${not empty reportFrm.fileType}">
									<c:out value="${fn:toUpperCase(reportFrm.fileType)}" />
								</c:when>
								<c:otherwise>
									<span>Unknown</span>
								</c:otherwise>
							</c:choose>
						</a>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<portlet:resourceURL var="downloadReportBlobData">
					<portlet:param name="id" value="${reportFrm.blobFileId}" />
					<portlet:param name="rd" value="${rDate}" />
					<portlet:param name="type" value="dw" />
				</portlet:resourceURL>

				<portlet:resourceURL var="downloadReportBlobDataPDF">
					<portlet:param name="id" value="${reportFrm.blobFileId}" />
					<portlet:param name="rd" value="${rDate}" />
					<portlet:param name="type" value="pdf" />
					<portlet:param name="convert" value="yes" />
				</portlet:resourceURL>

				<c:choose>
					<c:when test="${fn:toLowerCase(reportFrm.fileType) eq fn:toLowerCase('pdf')}">
						<portlet:resourceURL var="downloadPdfReportBlob">
							<portlet:param name="id" value="${reportFrm.blobFileId}" />
							<portlet:param name="rd" value="${rDate}" />
							<portlet:param name="type" value="pdf" />
							<portlet:param name="convert" value="no" />
						</portlet:resourceURL>

						<a class="download-Pdf-Text" href="${downloadPdfReportBlob}" title="Download PDF file">PDF</a>
					</c:when>
					<c:otherwise>
						<a class="download-Pdf-Text" href="${downloadReportBlobData}" title="Download as a text file">Text</a>
						/
						<a class="download-Pdf-Text" href="${downloadReportBlobDataPDF}" title="Download as a PDF file">PDF</a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</display:column>

	<display:column title="Name">
		<fmt:formatDate pattern="dd/MM/yyyy" value="${reportFrm.runDate}" var="rDate" />

		<portlet:renderURL var="reportDetails">
			<portlet:param name="rr" value="rd" />
			<portlet:param name="id" value="${reportFrm.blobFileId}" />
			<portlet:param name="rd" value="${rDate}" />
		</portlet:renderURL>

		<a href="javascript:openReportDetails('div${reportFrm.blobFileId}${reportFrm.runDate}');">
			${reportFrm.reportName}
		</a>

		<div class="modal-wrapper" id="div${reportFrm.blobFileId}${reportFrm.runDate}">
			<div class="modal-header">
				<h5>Report Details</h5>

				<a class="modal-close" href="#">X</a>
			</div>

			<div class="modal-content">
				<table
					border="0"
					cellpadding="0"
					cellspacing="0"
					class="report-details"
					width="100%"
				>
					<tr>
						<td class="report-titles">Report Owner</td>
						<td>${reportFrm.reportOwnerName}</td>
					</tr>
					<tr>
						<td class="report-titles">Report Name</td>
						<td>${reportFrm.reportName}</td>
					</tr>
					<tr>
						<td class="report-titles">Report Description</td>
						<td>${reportFrm.reportTitle}</td>
					</tr>
					<tr>
						<td class="report-titles">Reporting Period</td>
						<td>
							<fmt:formatDate pattern="dd/MM/yyyy" value="${reportFrm.reportStartDate}" />

							<span> - </span>
							<fmt:formatDate pattern="dd/MM/yyyy" value="${reportFrm.reportEndDate}" />
						</td>
					</tr>
					<tr>
						<td class="report-titles">Report Date</td>
						<td>
							<fmt:formatDate pattern="dd/MM/yyyy" value="${reportFrm.reportDate}" />
						</td>
					</tr>
					<tr>
						<td class="report-titles"><c:choose>
								<c:when test="${reportFrm.fileSize ge reportFrm.maxFileSize}">
										File Size*
									</c:when>
								<c:otherwise>
										File Size
									</c:otherwise>
							</c:choose>
						</td>
						<td>${reportFrm.fileSizeType}</td>
					</tr>
					<tr>
						<td class="report-titles">Download Time</td>
						<td>${reportFrm.fileDownloadTime} (approx.)</td>
					</tr>
					<tr>
						<td class="report-titles">File Name</td>
						<td>${reportFrm.fileName}</td>
					</tr>
				</table>

				<c:if test="${reportFrm.fileSize ge reportFrm.maxFileSize}">
					<div class="warning-msg">
						*
						<spring:message code="cuscal.switch.view.over.size.file.msg" />
					</div>
				</c:if>

				<div class="report-buttons-left">
					<fmt:formatDate
						pattern="dd/MM/yyyy"
						value="${reportFrm.runDate}"
						var="rDate"
					/>

					<c:choose>
						<c:when test="${reportFrm.isEncrypted}">
							<c:choose>
								<c:when test="${steppedUp eq 'true'}">
									<portlet:resourceURL var="downloadReportBlobDataEncrypted">
										<portlet:param name="id" value="${reportFrm.blobFileId}" />
										<portlet:param name="rd" value="${rDate}" />
										<portlet:param name="type" value="ws" />
									</portlet:resourceURL>

									<a class="download-button padlock" href="${downloadReportBlobDataEncrypted}">
										<c:choose>
											<c:when test="${not empty reportFrm.fileType}">
												Download <c:out value="${fn:toUpperCase(reportFrm.fileType)}" />
											</c:when>
											<c:otherwise>
												<span>Download Unknown</span>
											</c:otherwise>
										</c:choose>
									</a>
								</c:when>
								<c:otherwise>
									<portlet:actionURL var="stepUpAndDownload">
										<portlet:param name="id" value="${reportFrm.blobFileId}" />
										<portlet:param name="rd" value="${rDate}" />
										<portlet:param name="type" value="ws" />
										<portlet:param name="reportAction" value="2FAPage" />
									</portlet:actionURL>

									<a class="download-button padlock" href="${stepUpAndDownload}">
										<c:choose>
											<c:when test="${not empty reportFrm.fileType}">
												Download <c:out value="${fn:toUpperCase(reportFrm.fileType)}" />
											</c:when>
											<c:otherwise>
												<span>Download Unknown</span>
											</c:otherwise>
										</c:choose>
									</a>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:if test="${fn:toLowerCase(reportFrm.fileType) != fn:toLowerCase('pdf')}">
							<portlet:renderURL var="viewReportBlobData">
								<portlet:param name="id" value="${reportFrm.blobFileId}" />
								<portlet:param name="rd" value="${rDate}" />
								<portlet:param name="rr" value="rv" />
							</portlet:renderURL>

							<a class="view-button" href="${viewReportBlobData}" id="viewReports">
								View
							</a>&nbsp;
							<portlet:resourceURL var="downloadReportBlobData">
								<portlet:param name="id" value="${reportFrm.blobFileId}" />
								<portlet:param name="rd" value="${rDate}" />
								<portlet:param name="type" value="dw" />
							</portlet:resourceURL>

							<a class="download-button" href="${downloadReportBlobData}" id="downloadReports">
								Download Text
							</a>&nbsp;
							</c:if>

							<c:choose>
								<c:when test="${fn:toLowerCase(reportFrm.fileType) eq fn:toLowerCase('PDF')}">
									<portlet:resourceURL var="downloadReportBlobDataPDF">
										<portlet:param name="id" value="${reportFrm.blobFileId}" />
										<portlet:param name="rd" value="${rDate}" />
										<portlet:param name="type" value="pdf" />
										<portlet:param name="convert" value="no" />
									</portlet:resourceURL>
								</c:when>
								<c:otherwise>
									<portlet:resourceURL var="downloadReportBlobDataPDF">
										<portlet:param name="id" value="${reportFrm.blobFileId}" />
										<portlet:param name="rd" value="${rDate}" />
										<portlet:param name="type" value="pdf" />
										<portlet:param name="convert" value="yes" />
									</portlet:resourceURL>
								</c:otherwise>
							</c:choose>

							<a class="download-button" href="${downloadReportBlobDataPDF}" id="downloadReports">
								Download PDF
							</a>
						</c:otherwise>
					</c:choose>
				</div>

				<div class="report-buttons-right">
					<a class="close-button modal-close" href="#" id="closeView">Close</a>
				</div>

				<br />
			</div>
		</div>
	</display:column>

	<display:column title=" Date">
		<fmt:formatDate
			pattern="dd/MM/yyyy"
			value="${reportFrm.reportEndDate}"
		/>
	</display:column>

	<display:column property="reportBin" title=" Prefix(BIN)" />

	<display:column property="reportTitle" title=" Title" />
</display:table>

<c:if test="${not empty reportList}">
	<div class="warning-msg">
		*
		<spring:message code="cuscal.switch.view.over.size.file.msg" />
	</div>
</c:if>