<%-- Attempt to Resolve section --%>
<fieldset id="attemptToResolve">
	<legend>Attempt to Resolve</legend>

	<div id="attemptResolveWithMerchant">
		<label><spring:message code="message.did.attempt.to.resolve" /></label>

		<form:select cssClass="reset" id="resolveWithMerchant" path="attemptToResolve.resolveWithMerchant">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="yes"><spring:message code="label.yes" /></form:option>
			<form:option value="no"><spring:message code="label.no" /></form:option>
		</form:select>
	</div>

	<div class="padded" id="attemptDateContact">
		<label class="medium" for="errorDateContact"><spring:message code="label.date.contact" /></label>

		<form:input cssClass="date-pick" id="errorDateContact" path="attemptToResolve.errorDateContact" />
	</div>

	<div class="padded" id="attemptNameContact">
		<label class="medium" for="nameContact"><spring:message code="label.name.contact" /></label>

		<form:input id="nameContact" maxlength="50" path="attemptToResolve.nameContact" />
	</div>

	<div class="padded" id="attemptMethodContact">
		<label class="medium" for="methodContact"><spring:message code="label.method.contact" /></label>

		<form:input id="methodContact" maxlength="26" path="attemptToResolve.methodContact" />
	</div>

	<div class="padded" id="attemptMerchantResponse">
		<label class="medium" for="merchantResponse"><spring:message code="label.merchant.response" /></label>

		<form:textarea cssClass="limit" id="merchantResponse" length="150" path="attemptToResolve.merchantResponse" />
	</div>

	<div class="padded" id="attemptNotApplicableLocalLaw">
		<form:checkbox id="notApplicableLocalLaw" path="attemptToResolve.notApplicableLocalLaw" />

		<label for="notApplicableLocalLaw"><spring:message code="label.not.applicable.local.law" /></label>
	</div>

	<div class="padded" id="attemptExplainWhyNotResolve">
		<label for="explainWhyNotResolve"><spring:message code="label.why.not.resolve" /></label>

		<form:textarea cssClass="limit" id="explainWhyNotResolve" limit="500" path="attemptToResolve.explainWhyNotResolve" />
	</div>
</fieldset>