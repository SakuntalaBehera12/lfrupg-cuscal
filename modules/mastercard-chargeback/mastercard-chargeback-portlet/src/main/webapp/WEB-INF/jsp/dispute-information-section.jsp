<portlet:resourceURL escapeXml="false" id="respList" var="respUrl" />

<fieldset>
	<legend>Dispute Information</legend>

	<div>
		<label class="bold small">Dispute Type</label>

		<form:select id="chargebackType" path="type">
			<form:option value="0">Please select</form:option>
			<form:options items="${cbType}"></form:options>
		</form:select>

		<form:errors cssClass="error" path="type" />
	</div>

	<div id="reasons">
		<label class="bold small">Reason Code</label>

		<form:hidden id="reasonCode" path="reasonCode" value="" />

		<form:select id="firstCBReasonCode" path="">
			<option value="0">Please select</option>

			<form:options items="${firstChargebackReasonCodesMap}"></form:options>
		</form:select>

		<form:select id="arbitrationCBReasonCode" path="">
			<option value="0">Please select</option>

			<form:options items="${arbitrationChargebackReasonCodesMap}"></form:options>
		</form:select>

		<form:select id="retrievalRequestReasonCode" path="">
			<option value="0">Please select</option>

			<form:options items="${retrievalRequestReasonCodesMap}"></form:options>
		</form:select>

		<form:errors cssClass="error" path="reasonCode" />
	</div>

	<div id="disputeAmountDiv">
		<label class="bold small" for="disputeAmount">Dispute Amount (AUD)</label>

		<c:choose>
			<c:when test="${not updateTicket}">
				<form:input id="disputeAmount" maxlength="12" path="disputeAmount" size="13" value="${transaction.cardholderAmount}" />
			</c:when>
			<c:otherwise>
				<input id="updateTicket" type="hidden" value="true" />
				<form:input id="disputeAmount" maxlength="12" path="disputeAmount" size="13" value="${chargebackForm.disputeAmount}" />
			</c:otherwise>
		</c:choose>

		<a class="dispute-amount-help" href="javascript:;"><b>?</b></a>

		<br />

		<form:errors cssClass="error" path="disputeAmount" />
	</div>

	<div id="documentIndicator">
		<label class="bold small" for="documentIndicator">Documentation Indicator</label>

		<form:checkbox id="document-Indicator-checkbox" path="documentIndicator" />

		<a class="document-indicator-help" href="javascript:;"><b>?</b></a>

		<br />

		<form:errors cssClass="error" path="documentIndicator" />
	</div>
</fieldset>

<div class="modal-wrapper" id="disputeAmountHelpDiv">
	<div class="modal-header">
		<a class="modal-close" href="#">X</a>
	</div>

	<table border="0" cellpadding="1" cellspacing="1" class="response-message-codes tx-details" id="disputeAmountHelpTable">
	</table>
</div>

<div class="modal-wrapper" id="documentIndicatorHelpDiv">
	<div class="modal-header">
		<a class="modal-close" href="#">X</a>
	</div>

	<table border="0" cellpadding="1" cellspacing="1" class="response-message-codes tx-details" id="documentIndicatorHelpTable">
	</table>
</div>