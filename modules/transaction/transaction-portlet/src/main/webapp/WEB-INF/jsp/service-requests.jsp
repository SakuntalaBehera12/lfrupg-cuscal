<%@ include file="init.jsp" %>
<c:if test="${serviceRequestRole}">
<%-- Service Requests Section. --%>
<div id="service-requests">
	<div id="ticket-table">
		<table cellpadding="3px" cellspacing="0" class="transaction-service-requests" width="100%">
			<thead>
				<tr>
					<th>Request No.</th>
					<th class="header-product">Service Type</th>
					<th class="header-submitted-by">Submitted By</th>
					<th>Submitted</th>
					<th>Updated</th>
					<th class="header-status">Status</th>
				</tr>
			</thead>

			<tbody>
			</tbody>
		</table>
	</div>

	<br />

	<div id="request-form">
		<fieldset class="request-transaction">
			<legend>Transaction</legend>

			<table cellpadding="3" cellspacing="0" width="100%">
				<tr>
					<th>PAN</th>
					<td class="value"><span class="masked-pan"></span></td>
					<th>Acquirer</th>
					<td colspan="3"><span class="ticket-acquirer"></span></td>
				</tr>
				<tr>
					<th>Issuer</th>
					<td class="value"><span class="ticket-issuer"></span></td>
					<th>Terminal ID</th>
					<td colspan="3"><span class="terminal-id"></span></td>
				</tr>
				<tr>
					<th>Location Date</th>
					<td class="value"><span class="location-date"></span></td>
					<th>ATM/POS</th>
					<td colspan="3"><span class="atm-or-pos"></span></td>
				</tr>
				<tr>
					<th>Transaction Amount</th>
					<td class="value"><span class="transaction-amount"></span></td>
					<th>Cardholder Amount</th>
					<td><span class="cardholder-amount"></span></td>
				</tr>
				<tr>
					<th class="small">Cuscal ID</th>
					<td><span class="cuscal-transaction-id"></span></td>
					<th class="small"><abbr title="System Trace Audit Number">STAN</abbr></th>
					<td><span class="stan"></span></td>
				</tr>
				<tr class="visa-transaction-details-class">
					<th class="small">Scheme ID</th>
					<td><span class="visa-transaction-id"></span></td>

					<c:if test="<%= showArn == true %>">
						<th class="small">ARN</th>
						<td class="value"><span class="arn"></span></td>
					</c:if>
				</tr>
			</table>
		</fieldset>

		<fieldset class="mastercard-additional-transaction-info-fieldset">
			<legend>Additional Transaction Information</legend>

			<table cellpadding="3" cellspacing="0" width="100%">
				<tr>
					<th>POS Entry Mode</th>
					<td class="value">
						<span class="entryMode"></span>
						<span class="entryDscrip"></span>
					</td>
					<th>POS Condition Code</th>
					<td colspan="3">
						<span class="condCode"></span>
						<span class="condDescp"></span>
					</td>
				</tr>
				<tr>
					<th>Function Code</th>
					<td class="value">
						<span class="functionCode"></span>
						<span class="functionCodeDescription"></span>
					</td>
					<th><abbr title="Central Site Business Date">CSBD</abbr></th>
					<td colspan="3">
						<span class="csbd-value"></span>
					</td>
				</tr>
				<tr>
					<th><abbr title="The first digit of the service code indicates the card usage and chip presence">Track 2 Data Present</abbr></th>
					<td class="value">
						<span class="track2-data-present-value"></span>
					</td>
					<th><abbr title="Central Site Business Date">Chip Card Read</abbr></th>
					<td colspan="3">
						<span class="chip-card-present-value"></span>
					</td>
				</tr>
				<tr>
					<th><abbr title="Indicates electronic commerce or MOTO transaction">eCommerce Transaction</abbr></th>
					<td class="value">
						<span class="electronic-commerce-indciator"></span>
					</td>
					<th><abbr title="Universal Cardholder Authentication Field">UCAF Indicator</abbr></abbr></th>
					<td colspan="3">
						<span class="ucaf-collection-indicator"></span>
						<span class="ucaf-collection-indicator-description"></span>
					</td>
				</tr>
			</table>
		</fieldset>

		<div class="bold" id="serviceReqText">Submit a Service Request</div>

		<span id="serviceReqTextSpan">Please complete the details in the form below before submitting your Service Request.</span>

		<div id="visa-service-request">
			<%@ include file="visa-service-requests.jsp" %>
		</div>

		<div id="atm-pos-service-request">
			<%@ include file="atm-pos-service-requests.jsp" %>
		</div>

		<div id="atm-deposit-service-request">
			<%@ include file="atm-deposit-service-requests.jsp" %>
		</div>

		<div id="mastercard-service-request">
			<%@ include file="mastercard-service-requests.jsp" %>
		</div>
	</div>

	<div class="portlet-msg-info" id="success-message">Your service request has been submitted.</div>

	<div class="portlet-msg-info" id="info-message-txdetails"></div>
	<div class="portlet-msg-error" id="error-message-txdetails"></div>

	<div class="tx-buttons-left">
		<!-- <a class="print-button no-print" href="javascript:void(0)" onclick="javascript:printRequest();">Print</a> -->
		&nbsp;
	</div>

	<div class="tx-buttons-right">
		<a class="close-button modal-close no-print" href="#" id="closeToken">Close</a>
	</div>
</div>
</c:if>