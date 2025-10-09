<fieldset id="creditInformationFieldset">
	<legend>Credit Information</legend>

	<div id="creditInfoCreditVoucherGiven">
		<label class="large" for="creditVoucherGiven">
			Was a Credit Voucher, Voided Transaction Receipt or Refund Acknowledgement given
		</label>

		<form:select cssClass="reset" id="creditVoucherGiven" path="creditInfo.creditVoucherGiven">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="354"><spring:message code="label.yes" /></form:option>
			<form:option value="355"><spring:message code="label.no" /></form:option>
			<form:option value="356">Not Applicable</form:option>
		</form:select>

		<form:errors class="errors" path="creditInfo.creditVoucherGiven" />
	</div>

	<div id="creditInfoCreditVoucherDated">
		<label class="large" for="creditVoucherDated">Was the Credit Voucher, Voided Transaction Receipt or Refund Acknowledgement Dated</label>

		<form:select cssClass="reset" id="creditVoucherDated" path="creditInfo.creditVoucherDated">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="357"><spring:message code="label.yes" /></form:option>
			<form:option value="358"><spring:message code="label.no" /></form:option>
		</form:select>
	</div>

	<div id="creditInfoDateCreditVoucher">
		<label class="large" for="dateCreditVoucher">Date of Credit Voucher, Voided Transaction Receipt or Refund Acknowledgement Date</label>

		<form:input class="date-pick" id="dateCreditVoucher" path="creditInfo.dateCreditVoucher" />
	</div>

	<div id="creditInfoCreditAmount">
		<label class="large" for="creditAmount">Credit Amount</label>

		<form:input id="creditAmount" path="creditInfo.creditAmount" />
	</div>

	<div id="creditInfoInvoiceNumber">
		<label class="large" for="invoiceNumber">Invoice/Receipt Number</label>

		<form:input id="invoiceNumber" path="creditInfo.invoiceNumber" />
	</div>
</fieldset>