<fieldset id="questionnaireInformation">
	<legend>Questionnaire Information</legend>

	<div id="questionATMCashLoadInformation">
		<label class="medium"><spring:message code="label.cash.load.information" /></label>

		<form:select cssClass="reset" path="questionInfo.cashLoadInformation">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="284"><spring:message code="label.no.cash.dispensed" /></form:option>
			<form:option value="285"><spring:message code="label.incorrect.cash.dispensed" /></form:option>
		</form:select>
	</div>

	<div id="questionAmountRequested">
		<label class="medium" for="amountRequested">Amount Requested</label>

		<form:input id="amountRequested" path="questionInfo.amountRequested" />
	</div>

	<div id="questionAmountReceived">
		<label class="medium" for="amountReceived">Amount Received</label>

		<form:input id="amountReceived" path="questionInfo.amountReceived" />
	</div>
</fieldset>