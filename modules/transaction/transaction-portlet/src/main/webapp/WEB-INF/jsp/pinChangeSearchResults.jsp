<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@
taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@
taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %><%@
taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<%@ page contentType="text/html" isELIgnored="false" %>
<portlet:defineObjects />

<c:if test="${fromFA == true}">
	<portlet:resourceURL escapeXml="false" id="pinChangeDetailResource" var="faPinChangeDetails" />
</c:if>

<portlet:resourceURL escapeXml="false" id="pinChangeDetailResource" var="pinChangeDetails" />

<portlet:resourceURL escapeXml="false" id="nextPinChangeDetails" var="nextPinChangeDetails" />

<portlet:resourceURL escapeXml="false" id="prevPinChangeDetails" var="prevPinChangeDetails" />

<portlet:resourceURL escapeXml="false" id="printPinChangeDetails" var="printPinChangeDetails" />

<portlet:resourceURL escapeXml="false" id="pinChangeExportList" var="exportPinChangeResults" />

<portlet:renderURL escapeXml="false" var="renderUrl" />

<portlet:actionURL var="show2FaTxDet">
	<portlet:param name="action" value="pinChange2FAPage" />
</portlet:actionURL>

<%
String steppedUp = "";

if (session.getAttribute("_USER_STEPPEDUP") != null) {
	steppedUp = (String)session.getAttribute("_USER_STEPPEDUP");
}
%>

<script charset="utf-8" type="text/javascript">

	var ifFaPage = "<c:out value="${fromFA}" />";
	if (ifFaPage == "true") {
		Liferay.on('allPortletsReady', function() {
			var url = "<c:out value="${pinChangeDetails}" />";
			var txId="<c:out value="${pinChangeTransactionId}" />";
			var busDate="<c:out value="${pinChangeBusinessDate}" />";
			var dataSrc="<c:out value="${pinChangeSource}" />";
			var currRow="<c:out value="${currRowNum}" />";

			var urlFinal = replaceAll(url, "&amp;", "&");

			openPinChangeDetailsJsonObj(urlFinal,
										txId,
										busDate,
										dataSrc,
										currRow);
		});
	}

	function createPanHtml(data) {

		var panUrl = "<c:out value="${show2FaTxDet}" />";

		var panUrlFinal = replaceAll(panUrl, "&amp;", "&");

		var isStepUp = <%= "true".equals(steppedUp) %>;

		//Remove the spacer divs in the modal window and then add when needed them.
		jQuery("#pin-change-details-wrapper div.spacer").each(function() {
			jQuery(this).remove();
		});

		if (isStepUp) {
			jQuery("#pin-change-details-pan").html(data.requestDetails.pan);
			jQuery("#pin-change-details-pan").after("<div class='spacer'>&nbsp;</div>");
		} else {
			var isFaLink = "<c:out value="${isFa}" />";

			if(isFaLink == "true") {
				var theLink = "&nbsp;";
				theLink += "<a href=\"javascript:void(0)\" onclick=\"javascript:open2FAPagePinChangeDetails('" + panUrlFinal + "');\"";
				theLink += " class=\"normal-link\">View Full PAN</a>";

				jQuery("#pin-change-show-pan").html(theLink);

				jQuery("#pin-change-details-pan").html(data.requestDetails.pan);
				jQuery("#pin-change-show-pan").after("<div class='spacer'>&nbsp;</div>");
			} else {
				jQuery("#pin-change-details-pan").html(data.requestDetails.pan);
				jQuery("#pin-change-details-pan").after("<div class='spacer'>&nbsp;</div>");
			}
		}
	}
</script>

<form action="" id="pinChangeSearchResultsForm" method="post">
	<%@ include file="pinChangeSearch.jsp" %>

	<c:if test="${not empty sessionScope.pinSearchResults}">
	<div class="show-hide-export-button-wrapper">
		<a href="javascript:void(0)" id="show-hide-search-form"></a>
		<a class="export-button" href="${exportPinChangeResults}">Export Results</a>
	</div>
	</c:if>

	<c:if test="${moreDataMsg}">
		<div class="portlet-msg-info">
			<span><spring:message code="pin.change.results.more.than.250" /></span>
		</div>
	</c:if>

	<c:if test="${dateWarningMsg}">
		<div class="portlet-msg-info">
			<span><spring:message code="pin.change.search.older.18month" /></span>
		</div>
	</c:if>

	<display:table class="cuscalTable" defaultorder="descending" defaultsort="6" id="pinChangeResults" name="sessionScope.pinSearchResults" pagesize="25" requestURI="${renderUrl}" sort="external" uid="pinChangeResults">
		<display:setProperty name="factory.requestHelper" value="org.displaytag.portlet.PortletRequestHelperFactory" />

		<display:setProperty name="basic.msg.empty_list">
			<div class="portlet-msg-info">
				<span><spring:message code="pin.change.no.records.found" /></span>
			</div>
		</display:setProperty>

		<display:setProperty name="paging.banner.page.separator">
		</display:setProperty>

		<display:setProperty name="paging.banner.placement">both</display:setProperty>

		<display:setProperty name="paging.banner.item_name">result</display:setProperty>

		<display:setProperty name="paging.banner.items_name">results</display:setProperty>

		<display:setProperty name="paging.banner.group_size" value="10" />

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
			<span class="pagination-links"> <a href="{2}">&lt;</a> {0} <a
				href="{3}">&gt;</a> </span>
		</display:setProperty>

		<display:setProperty name="css.th.ascending" value="ascending" />
		<display:setProperty name="css.th.descending" value="descending" />

		<display:column class="first" defaultorder="ascending" sortable="true" sortName="pan" title="PAN">
			<a href="javascript:void(0)" onclick="javascript:openPinChangeDetailsJsonObj('${pinChangeDetails}', '${pinChangeResults.transactionId}','${pinChangeResults.busniessDate}','${pinChangeResults.dataSource}','${pinChangeResults_rowNum}');">
				<c:out value="${pinChangeResults.pan}" />
			</a>
		</display:column>

		<display:column defaultorder="ascending" sortable="true" sortName="terminalId" title="Terminal ID">
			<c:out value="${pinChangeResults.terminalId}" />
		</display:column>

		<display:column defaultorder="ascending" sortable="true" sortName="cardAcceptorId" title="Card Acceptor Name">
			<c:out value="${pinChangeResults.cardAcceptorId}" />
		</display:column>

		<display:column defaultorder="ascending" sortable="true" sortName="responseCode" title="Response Code">
			<div title="<c:out value="${pinChangeResults.responseDescription}" />">
				<c:out value="${pinChangeResults.responseCode}" />
			</div>
		</display:column>

		<display:column class="stan" defaultorder="ascending" sortable="true" sortName="systemTrace" title="System Trace">
			<c:out value="${pinChangeResults.systemTrace}" />
		</display:column>

		<display:column defaultorder="descending" format="{0,date,dd/MM/yyyy HH:mm}" property="dateTime" sortable="true" sortName="dateTime" title="Date/Time" />

		<display:column class="last" defaultorder="ascending" sortable="true" sortName="description" title="Description">
			<c:out value="${pinChangeResults.description}" />
		</display:column>
	</display:table>

	<div class="modal-wrapper" id="pin-change-details-wrapper">
		<div class="modal-header">
			<h5>Transaction Details</h5>

			<a class="modal-close" href="#">X</a>
		</div>

		<div class="pct-print">
			<table cellpadding="3" cellspacing="0" class="transaction-code" width="100%">
				<tr>
					<td class="tx-titles">Code:</td>
					<td>
						<span id="pin-change-details-code"></span>
					</td>
					<td class="tx-titles">Description</td>
					<td>
						<span id="pin-change-details-description"></span>
					</td>
				</tr>
			</table>

			<div>
				<label class="message-type">Message Type:</label>

				<span id="pin-change-details-message-type"></span>
			</div>

			<div class="modal-header">
				<h5>Request Details</h5>
			</div>

			<div>
				<label class="pan">PAN:</label>

				<span id="pin-change-details-pan"></span>
				<span id="pin-change-details-masked-pan"></span>
				<span id="pin-change-show-pan"></span>

				<label class="expiry-date">Expiry Date:</label>

				<span id="pin-change-details-expiry-date"></span>

				<div id="pin-change-hsm-msg"></div>
			</div>

			<input id="pin-change-transaction-id" name="pinChangeTransactionId" type="hidden" value="" />
			<input id="pin-change-business-date" name="pinChangeBusinessDate" type="hidden" value="" />
			<input id="pin-change-source" name="pinChangeSource" type="hidden" value="" />
			<input id="pin-change-size" name="pinChangeSize" type="hidden" value="<c:out value="${fn:length(sessionScope.pinSearchResults)}" />" />

			<fieldset class="date-time">
				<legend>Date/Time</legend>

				<table cellpadding="3" cellspacing="0" class="tx-details" width="100%">
					<tr>
						<td class="tx-titles">Transaction:</td>
						<td>
							<span id="pin-change-details-transaction-date"></span>
						</td>
					</tr>
					<tr class="alt">
						<td class="tx-titles">Location:</td>
						<td>
							<span id="pin-change-details-local-date"></span>
						</td>
					</tr>
					<tr>
						<td class="tx-titles">Switch:</td>
						<td>
							<span id="pin-change-details-switch-date"></span>
						</td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="acquirer">
				<legend>Acquirer</legend>

				<table cellpadding="3" cellspacing="0" class="tx-details" width="100%">
					<tr>
						<td class="id tx-titles">ID:</td>
						<td>
							<span id="pin-change-details-acquiring-id"></span>
						</td>
					</tr>
					<tr class="alt">
						<td class="tx-titles">Name:</td>
						<td>
							<span id="pin-change-details-acquiring-name"></span>
						</td>
					</tr>
					<tr>
						<td class="tx-titles">System Trace:</td>
						<td>
							<span id="pin-change-details-stan"></span>
						</td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="issuer">
				<legend>Issuer</legend>

				<table cellpadding="3" cellspacing="0" class="tx-details" width="100%">
					<tr>
						<td class="id tx-titles">ID:</td>
						<td>
							<span id="pin-change-details-issuer-id"></span>
						</td>
						<td class="tx-titles">Name:</td>
						<td>
							<span id="pin-change-details-issuer-name"></span>
						</td>
						<td class="tx-titles"><abbr title="Retreival Reference Number">RRN</abbr>:</td>
						<td>
							<span id="pin-change-details-ret-reference"></span>
						</td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="response-details">
				<legend>Response Details</legend>

				<table cellpadding="3" cellspacing="0" class="pin-response-details tx-details" width="100%">
					<tr>
						<td class="tx-titles">Code:</td>
						<td>
							<span id="pin-change-details-response-code"></span>
						</td>
					</tr>
					<tr class="alt">
						<td class="tx-titles">Description:</td>
						<td>
							<span id="pin-change-details-response-desc"></span>
						</td>
					</tr>
					<tr>
						<td class="tx-titles">Authorisation ID:</td>
						<td>
							<span id="pin-change-details-auth-id"></span>
						</td>
					</tr>
					<tr class="alt">
						<td class="tx-titles">PCT Operator ID:</td>
						<td>
							<span id="pan-change-details-operator"></span>
						</td>
					</tr>
					<tr>
						<td class="tx-titles">PCT Supervisor ID:</td>
						<td>
							<span id="pan-change-details-supervisor"></span>
						</td>
					</tr>
				</table>
			</fieldset>
		</div>

		<div class="tx-buttons-left" id="prevNextPrt">
			<a class="prev-button" href="javascript:void(0)" id="prev" onclick="javascript:openPinChangeDetailsForPrevOrNext('${prevPinChangeDetails}');">Previous</a>
			<a class="next-button" href="javascript:void(0)" id="next" onclick="javascript:openPinChangeDetailsForPrevOrNext('${nextPinChangeDetails}');">Next</a>
			<a class="print-button" href="javascript:void(0)" id="print" onclick="javascript:printPinChangeDetails('${printPinChangeDetails}');">Print</a>
		</div>

		<div class="tx-buttons-right">
			<a class="close-button modal-close" href="#" id="closeToken">Close</a>
		</div>

		<br />
	</div>
</form>