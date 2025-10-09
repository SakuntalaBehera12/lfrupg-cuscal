<fieldset id="fraudReporting">
	<legend>Fraud Reporting</legend>

	<label for="fraudReportingDate"><spring:message code="label.fraud.reporting" /></label>

	<form:input cssClass="date-pick" id="fraudReportingDate" path="fraudReportingDate" />

	<a class="bold" href="javascript:;" title="<spring:message code="hover.fraud.reporting" />">?</a>

	<form:errors cssClass="error" path="fraudReportingDate" />
</fieldset>