<fieldset>
	<legend>Dispute Information</legend>

	<div>
		<label class="bold small">Chargeback Type</label>

		<form:select id="chargebackType" path="type">
			<form:option value="0">Please select</form:option>
			<form:options items="${cbType}"></form:options>
		</form:select>

		<form:errors cssClass="error" path="type" />
	</div>

	<div id="reasons">
		<label class="bold small">Reason Code</label>

		<form:hidden id="reasonCode" path="reasonCode" value="" />

		<form:select id="cbReasonRFI" path="">
			<option value="0">Please select</option>

			<form:options items="${cbRFIMap}"></form:options>
		</form:select>

		<form:select id="cbReasonFraud" path="">
			<option value="0">Please select</option>

			<form:options items="${cbFraudMap}"></form:options>
		</form:select>

		<form:select id="cbReasonAuth" path="">
			<option value="0">Please select</option>

			<form:options items="${cbAuthMap}"></form:options>
		</form:select>

		<form:select id="cbReasonProcessingError" path="">
			<option value="0">Please select</option>

			<form:options items="${cbProcessingErrorMap}"></form:options>
		</form:select>

		<form:select id="cbReasonNRFS" path="">
			<option value="0">Please select</option>

			<form:options items="${cbNRFSMap}"></form:options>
		</form:select>

		<form:select id="cbReasonCancelled" path="">
			<option value="0">Please select</option>

			<form:options items="${cbCancelledMap}"></form:options>
		</form:select>

		<form:errors cssClass="error" path="reasonCode" />
	</div>

	<div>
		<label class="bold small">Dispute Amount</label>

		<span>${transaction.cardholderAmount}</span>
	</div>
</fieldset>