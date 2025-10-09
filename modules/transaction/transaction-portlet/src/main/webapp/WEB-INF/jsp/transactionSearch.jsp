<%@ page import="au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil" %>

<%@ page import="java.io.InputStream" %>

<%@ page import="javax.portlet.PortletSession" %>

<%@ include file="init.jsp" %>

<portlet:defineObjects />

<c:if test="${fromFA==true}">
	<portlet:resourceURL escapeXml="false" id="txDetail" var="faTxDetail" />
</c:if>

<c:set var="ns"><portlet:namespace /></c:set>

<portlet:actionURL var="show2FaTxDet">
	<portlet:param name="action" value="2FAPage" />
</portlet:actionURL>

<portlet:actionURL escapeXml="false" var="showTxList">
	<portlet:param name="action" value="txList" />
</portlet:actionURL>

<portlet:resourceURL escapeXml="false" id="msgList" var="msgUrl" />

<portlet:resourceURL escapeXml="false" id="respList" var="respUrl" />

<portlet:resourceURL escapeXml="false" id="transactionTickets" var="ticketsUrl" />

<portlet:resourceURL escapeXml="false" id="nextTxDet" var="nextTxDetails" />

<portlet:resourceURL escapeXml="false" id="prevTxDet" var="prevTxDetails" />

<portlet:resourceURL escapeXml="false" id="exportxls" var="exportTxLst" />

<portlet:resourceURL escapeXml="false" id="printAudit" var="printTxDetails" />
<portlet:resourceURL escapeXml="false" id="txDetail" var="showTxDetails" />

<portlet:renderURL escapeXml="false" var="clearVal">
	<portlet:param name="render" value="clearValid" />
</portlet:renderURL>

<portlet:renderURL escapeXml="false" var="purge">
	<portlet:param name="render" value="purge" />
</portlet:renderURL>

<portlet:renderURL escapeXml="false" var="logPool">
	<portlet:param name="render" value="logPool" />
</portlet:renderURL>

<%
String steppedUp = "";

if (portletSession.getAttribute("_USER_STEPPEDUP", PortletSession.APPLICATION_SCOPE) != null) {
	steppedUp = (String)portletSession.getAttribute("_USER_STEPPEDUP", PortletSession.APPLICATION_SCOPE);
}

InputStream resource = CuscalSharedPropsUtil.getResourceStream(getClass(), "transaction-search/transaction.properties");

Properties props = new Properties();
props.load(resource);

boolean showDateTypeField = false;
boolean showCuscalIdField = false;
boolean showStanField = false;
boolean showRrnField = false;
boolean showArn = false;

if (null != props.getProperty("transaction.search.ui.visibile.dateType")) {
	showDateTypeField = Boolean.parseBoolean(
		props.getProperty(
			"transaction.search.ui.visibile.dateType"
		).trim());
}

if (null != props.getProperty("transaction.search.ui.visibile.cuscalId")) {
	showCuscalIdField = Boolean.parseBoolean(
		props.getProperty(
			"transaction.search.ui.visibile.cuscalId"
		).trim());
}

if (null != props.getProperty("transaction.search.ui.visibile.stan")) {
	showStanField = Boolean.parseBoolean(
		props.getProperty(
			"transaction.search.ui.visibile.stan"
		).trim());
}

if (null != props.getProperty("transaction.search.ui.visibile.rrn")) {
	showRrnField = Boolean.parseBoolean(
		props.getProperty(
			"transaction.search.ui.visibile.rrn"
		).trim());
}

if (null != props.getProperty("transaction.search.ui.visibile.arn")) {
	showArn = Boolean.parseBoolean(
		props.getProperty(
			"transaction.search.ui.visibile.arn"
		).trim());
}
%>

<script charset="utf-8" type="text/javascript">

	var ifFaPage = "<c:out value="${fromFA}" />";
	if (ifFaPage == "true") {

		Liferay.on('allPortletsReady', function() {
			var url = "<c:out value="${faTxDetail}" />";
			var txId="<c:out value="${txId}" />";
			var busDate="<c:out value="${busDate}" />";
			var dataSrc="<c:out value="${txSrc}" />";
			var currRow="<c:out value="${currRowNum}" />";
			var urlFinal = replaceAll(url, "&amp;", "&");
			var pageName = "fromFA";
			openTransactionDetailsJsonObj(urlFinal,txId,busDate,dataSrc,currRow,pageName);
		});
	}

	var ifTxDPage = "<c:out value="${loadTXDetails}" />";
	if (ifTxDPage == "true") {
		Liferay.on('allPortletsReady', function() {
			jQuery(".trans-print").hide();
			var txDetailsUrl = "<c:out value="${showTxDetails}" />";
			var txId="<c:out value="${txId}" />";
			var busDate="<c:out value="${busDate}" />";
			var dataSrc="<c:out value="${txSrc}" />";
			var currRow="<c:out value="${currRowNum}" />";
			var pageName = "loadTXDetails";
			var txDetailsURLFinal = replaceAll(txDetailsUrl, "&amp;", "&");
			openTransactionDetailsJsonObj(txDetailsURLFinal,txId,busDate,dataSrc,currRow,pageName);

			var ticketsUrl = "<c:out value="${ticketsUrl}" />";
			var ticketsUrlFinal = replaceAll(ticketsUrl, "&amp;", "&");

			jQuery("#service-requests").attr("trans", txId);
			jQuery("#service-requests").attr("dat", busDate);
			getTransactionTickets(ticketsUrlFinal);

			jQuery("li.trans-det").removeClass("selected");
			jQuery("li.serv-req").addClass("selected");
			jQuery("#transaction-details-wrapper").hide();
			jQuery("#service-requests").show();

			var errors = "<c:out value="${errors}" />";
			if (errors === "true") {

				var reason = "<c:out value="${reasonCode}" />";

				setTimeout(function () {
					jQuery('#posForm').val(reason);
					jQuery('#atmForm').val(reason);
					jQuery("#createTicket").focus();
				}, 2000);

			} else {

				jQuery("#success-message").show();
				setTimeout(function () {
						jQuery("#request-form").fadeOut(300);
					}, 2000);
			}
		});
	}

	function createPanHtml(data) {

		var panUrl = "<c:out value="${show2FaTxDet}" />";
		var panUrlFinal = replaceAll(panUrl, "&amp;", "&");

		var resultsUrl = "<c:out value="${showTxList}" />";
		var resultsUrlFinal = replaceAll(resultsUrl, "&amp;", "&");

		var isStepUp = "<%= steppedUp %>";

		if (isStepUp == "true") {
			jQuery('#pan').html(data.requestDetails.pan);
//				jQuery("#followPan").html("&nbsp;<a href=\"javascript:followPan('" + data.requestDetails.pan
//						+ "', '" + resultsUrlFinal + "');\" class='normal-link'>Follow This PAN</a>");
		} else {
			var isFaLink = "<c:out value="${isFa}" />";
			if(isFaLink == "true") {
				jQuery("#showPan").html("&nbsp;<a href=\"javascript:open2FAPageTxDet('" + panUrlFinal
						+ "');\" class='normal-link'>View Full PAN</a>");
				jQuery("#pan").html(data.requestDetails.pan);
			} else {
				jQuery('#pan').html(data.requestDetails.pan);
			}
		}
		jQuery(".expiry-date").before("&nbsp;&nbsp;");
	}

	jQuery(document).ajaxStart(function() {
		jQuery("#loading").show();
	}).ajaxStop(function() {
		jQuery("#loading").hide();
	});

</script>

<form:form action="" id="transactionForm" method="post" modelAttribute="transactionForm" name="transactionForm" onSubmit="javascript:filterTxByDates('${showTxList}');">
	<div>
		<p>
			Card/ATM Transaction Search <br /> Use the form below to search for transactions
		</p>

		<c:choose>
			<c:when test="${not empty txList}">
				<%--<div id="searchSelect" style="display: none;">
					<form:radiobutton name="searchView" path="searchView" value="quick" />Quick Search
					<form:radiobutton id="advSearch" name="searchView" path="searchView" value="advanced" />Advanced Search
				</div>--%>
				<br />

				<div id="transactionSearch" style="display: none;">
			</c:when>
			<c:otherwise>
				<%--<div id="searchSelect" style="display: block;">
					<form:radiobutton name="searchView" path="searchView" value="quick" />Quick Search
					<form:radiobutton id="advSearch" name="searchView" path="searchView" value="advanced" />Advanced Search
				</div>--%>
				<br />

				<div id="transactionSearch" style="display: block;">
			</c:otherwise>
		</c:choose>

		<div>
			<fieldset>
			<legend>Transaction Details<em>*</em></legend>

			<span class="message">Please fill in at least one of these fields</span>

			<div>
				<label for="panBin">PAN/BIN</label>

				<c:if test="${not empty _transactionForm}">
					<c:set value="${_transactionForm}" var="transactionForm" />
				</c:if>

				<form:input autocomplete="off" id="panBin" path="panBin" tabindex="1" value="${transactionForm.panBin}" />

				<br />

				<form:errors cssClass="error" path="panBin" />
			</div>

			<div id="visaIdField">
				<label class="externalTransaction" for="externalTransactionId">Scheme ID</label>
				<%-- <form:input autocomplete="off" id="externalTransactionId" maxlength="15" path="externalTransactionId" tabindex="2" value="011113011113" /> --%>
				<form:input autocomplete="off" id="externalTransactionId" maxlength="15" path="externalTransactionId" tabindex="2" value="${transactionForm.externalTransactionId}" />

				<a class="scheme-id-help" href="javascript:;" tabindex="3"><b>?</b></a>

				<br />

				<form:errors cssClass="error" path="externalTransactionId" />
			</div>

			<c:if test="<%= showCuscalIdField == true %>">
				<div id="cuscalIdField">
					<label for="cuscalId">Cuscal ID</label>

					<form:input autocomplete="off" id="cuscalId" maxlength="38" path="cuscalId" tabindex="4" value="${transactionForm.cuscalId}" />
					<%-- <form:input autocomplete="off" id="cuscalId" maxlength="38" path="cuscalId" tabindex="4" value="100000000046592675" /> --%>
					<br />

					<form:errors cssClass="error" path="cuscalId" />
				</div>
			</c:if>

			<br />

			<div id="terminalIdField">
				<label for="terminalId">Terminal ID</label>

				<form:input autocomplete="off" id="terminalId" maxlength="8" path="terminalId" tabindex="5" value="${transactionForm.terminalId}" />

				<br />

				<form:errors cssClass="error" path="terminalId" />
			</div>

			<div id="cardAcceptorIdField">
				<label class="cardAcceptor" for="merchantId">Card Acceptor ID</label>

				<form:input autocomplete="off" id="merchantId" maxlength="15" path="merchantId" tabindex="6" value="${transactionForm.merchantId}" />

				<br />

				<form:errors cssClass="error" path="merchantId" />
			</div>

			<c:if test="<%= showStanField == true %>">
				<div id="stanField">
					<label for="stan">STAN</label>

					<form:input autocomplete="off" id="stan" maxlength="6" path="stan" tabindex="7" value="${transactionForm.stan}" />

					<br />

					<form:errors cssClass="error" path="stan" />
				</div>
			</c:if>
			</fieldset>

			<fieldset>
			<legend>Date/Time</legend>

			<div class="date-time-div">
				<label class="start-date" for="startDate">Start Date & Time<em>*</em></label>

				<form:input autocomplete="off" class="date-pick" id="startDate" path="startDate" tabindex="8" value="${transactionForm.startDate}" />
				<%-- <form:input autocomplete="off" class="date-pick" id="startDate" path="startDate" tabindex="8" value="28/11/2014" /> --%>
				<form:select id="startTimeHr" path="startTimeHr" tabindex="9">
					<c:forEach items="${transactionForm.startHourDisplay}" var="hrC">
						<form:option value="${hrC}">
							<fmt:formatNumber maxIntegerDigits="2" minIntegerDigits="2" value="${hrC}" />
						</form:option>
					</c:forEach>
				</form:select>

				<span>:</span>

				<form:select id="startTimeMin" path="startTimeMin" tabindex="10">
					<c:forEach items="${transactionForm.startMinuteDisplay}" var="minsC">
						<form:option value="${minsC}">
							<fmt:formatNumber maxIntegerDigits="2" minIntegerDigits="2" value="${minsC}" />
						</form:option>
					</c:forEach>
				</form:select>

				<br />

				<form:errors cssClass="error" path="startDate" />
			</div>

			<div class="date-time-div">
				<label class="end-date" for="endDate">End Date & Time<em>*</em></label>

				<form:input autocomplete="off" class="date-pick" id="endDate" path="endDate" tabindex="11" value="${transactionForm.endDate}" />
				<%-- <form:input autocomplete="off" class="date-pick" id="endDate" path="endDate" tabindex="11" value="28/11/2014" /> --%>
				<form:select id="endTimeHr" path="endTimeHr" tabindex="12">
					<c:forEach items="${transactionForm.endHourDisplay}" var="hrC">
						<form:option value="${hrC}">
							<fmt:formatNumber maxIntegerDigits="2" minIntegerDigits="2" value="${hrC}" />
						</form:option>
					</c:forEach>
				</form:select>

				<span>:</span>

				<form:select id="endTimeMin" path="endTimeMin" tabindex="13">
					<c:forEach items="${transactionForm.endMinuteDisplay}" var="minsC">
						<form:option value="${minsC}">
							<fmt:formatNumber maxIntegerDigits="2" minIntegerDigits="2" value="${minsC}" />
						</form:option>
					</c:forEach>
				</form:select><br />

				<form:errors cssClass="error" path="endDate" />
			</div>

			<c:choose>
				<c:when test="<%= showDateTypeField == true %>">
				<br />
					<div class="date-type-div">
						<label>Search By</label>

						<form:select id="dateType" path="dateType" tabindex="14">
							<form:option value="local">Location Date/Time</form:option>
							<form:option value="system">Cuscal Switch Date/Time</form:option>
						</form:select>
					</div>
				</c:when>
				<c:otherwise>
					<input name="dateType" type="hidden" value="local" />
				</c:otherwise>
			</c:choose>
			</fieldset>

			<fieldset id="optionalFilters">
			<legend>Optional Filters</legend>

			<div class="amount-div">
				<label class="amountset" for="amount">Amount</label>

				<form:select id="conditions" path="conditions" tabindex="15">
					<form:option value="eq">=</form:option>
					<form:option value="ge">&gt;=</form:option>
					<form:option value="gt">&gt;</form:option>
					<form:option value="lt">&lt;</form:option>
					<form:option value="le">&lt;=</form:option>
				</form:select>

				<form:input autocomplete="off" id="amount" path="amount" tabindex="16" value="${transactionForm.amount}" />

				<br />

				<form:errors cssClass="error" path="amount" />
			</div>

			<div>
				<label class="amount-type" for="amountType">Amount Type</label>

				<form:select id="amountType" path="amountType" tabindex="17">
					<form:option value="billingAmount">Issuer</form:option>
					<form:option value="transactionAmount">Acquirer</form:option>
				</form:select>
			</div>

			<br />

			<div class="response-code-wrapper">
				<label class="response-code" for="resoponseCode">Response Code</label>

				<form:input id="responseCode" maxlength="2" path="responseCode" tabindex="18" value="${transactionForm.responseCode}" />

				<a href="javascript:openMessageResponseCodePage('responseCodeDiv','respCodeDescrp','${respUrl}');" tabindex="19"><b>?</b></a>

				<br />

				<form:errors cssClass="error" path="responseCode" />
			</div>

			<div class="message-div-wrapper">
				<label class="message-code" for="messageCode">Message Type</label>

				<form:select id="messageCode" path="messageCode" tabindex="20">
					<form:option value=""></form:option>
					<c:forEach items="${transactionForm.msgTypeOptionDisplay}" var="msgCodeDisp">
						<c:set value="${fn:substring(msgCodeDisp,0,4)}" var="opVal" />

						<form:option value="${opVal}">${msgCodeDisp}</form:option>
					</c:forEach>
				</form:select>

				<a href="javascript:openMessageResponseCodePage('messageCodeDiv','msgCodeDescrp','${msgUrl}');" tabindex="21"><b>?</b></a>

				<br />

				<form:errors cssClass="error" path="messageCode" />
			</div>

			<c:if test="<%= showRrnField == true %>">
				<div>
					<label class="rrn" for="rrn">RRN</label>

					<form:input autocomplete="off" id="rrn" maxlength="12" path="rrn" tabindex="22" value="${transactionForm.rrn}" />

					<br />

					<form:errors cssClass="error" path="rrn" />
				</div>
			</c:if>
			</fieldset>
		</div>

		<br />

		<input id="search" tabindex="23" type="submit" value="Search" />
		<input onclick="javascript:clearAll('${clearVal}')" tabindex="24" type="button" value="Clear" />
	</div>

	<c:if test="${not empty txList}">
		<div id="transaction-search-form">
			<a href="javascript:showHideSearchForm()" id="show-hide-search">
				<c:choose>
					<c:when test="${not empty txList}">
					Show search form
					</c:when>
					<c:otherwise>
					Hide search form
					</c:otherwise>
				</c:choose>
			</a>

			<a class="export-button" href="${exportTxLst}">Export Results</a>
		</div>

		<br />
	</c:if>
	</div>
</form:form>

<div class="modal-wrapper" id="responseCodeDiv">
	<div class="modal-header">
		<h5>Response Codes</h5>

		<a class="modal-close" href="#">X</a>
	</div>

	<table border="0" cellpadding="0" cellspacing="0" class="response-message-codes tx-details" id="respCodeDescrp">
	</table>
</div>

<div class="modal-wrapper" id="messageCodeDiv">
	<div class="modal-header">
		<h5>Message Codes</h5>

		<a class="modal-close" href="#">X</a>
	</div>

	<div id="msgCodeDescrp">
	</div>
</div>

<div class="modal-wrapper" id="scheme-id-div">
	<div class="modal-header">
		<a class="modal-close" href="#">X</a>
	</div>

	<div id="scheme-id-desc"></div>
</div>