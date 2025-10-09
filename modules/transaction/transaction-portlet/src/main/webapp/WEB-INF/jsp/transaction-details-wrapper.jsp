<%@ include file="init.jsp" %>

<portlet:resourceURL escapeXml="false" id="transactionTickets" var="transactionTicketsUrl" />
<portlet:resourceURL escapeXml="false" id="requestPrint" var="requestPrintUrl" />

<div class="modal-wrapper" id="transactionDetailsDiv">
	<div class="modal-header">
		<ul class="tabs">
			<li class="selected tab trans-det" sec="detail">
				Transaction Details
			</li>

			<c:if test="${serviceRequestRole}">
				<li class="serv-req tab" id="servReqTab" sec="request" url="${transactionTicketsUrl}">
					Service Request
				</li>
			</c:if>
		</ul>

		<a class="no-print print-button trans-print" href="javascript:void(0)" onclick="javascript:printDetails('${printTxDetails}');">Print</a>
		<%-- <a class="print-button no-print req-print" href="javascript:void(0)" onclick="javascript:printRequest('${requestPrintUrl}');">Print</a> --%>
		<!-- <a class="print-button no-print" href="javascript:void(0)" sec="details" onclick="javascript:printRecord();">Print</a> -->
		<a class="modal-close no-print" href="javascript:void(0)">X</a>
	</div>

	<%@ include file="transaction-details.jsp" %>

	<%@ include file="service-requests.jsp" %>
	<br />
</div>