<%-- Retrieval Request fieldset --%>
<fieldset id="retrievalRequest">
	<legend>Voucher Request</legend>

	<div id="tc52Date">
		<label class="small" for="tc52DateInput"><spring:message code="label.date.tc52.voucher" /></label>

		<form:input class="date-pick" id="tc52DateInput" path="retrievalRequest.tc52Date" value="${retrievalRequest.tc52Date}" />

		<form:errors cssClass="error" path="retrievalRequest.tc52Date" />
	</div>
</fieldset>