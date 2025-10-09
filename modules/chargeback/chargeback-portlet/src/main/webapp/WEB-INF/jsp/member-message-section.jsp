<%-- Member Message --%>
<fieldset id="member-messages">
	<legend>Visa Chargeback Description</legend>

	<div>
		<label class="bold small">Chargeback Case</label>

		<form:select id="memberMessage" path="memberMessage">
			<option value="0">Please select</option>

			<form:options items="${memberMessage}"></form:options>
		</form:select>

		<form:errors cssClass="error" path="memberMessage" />
	</div>

	<div>
		<label class="bold small">Update Chargeback</label>

		<form:input class="update-message" id="updateMessage" path="updateMessage" />

		<a class="bold" href="javascript:;" title="<spring:message code="hover.member.message" />">?</a>

		<form:errors cssClass="error" path="updateMessage" />
	</div>

	<div class="message-text-note">Only update fields marked with X's and/or MMDDYY</div>
</fieldset>