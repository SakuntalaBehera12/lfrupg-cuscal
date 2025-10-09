<%-- Attachment/s section. --%>
<fieldset>
	<legend>Comments &amp; Attachments</legend>

	<div class="value">
		<em><spring:message code="chargeback.comments.pan.message" /></em>

		<form:textarea htmlEscape="false" maxlength="4000" path="note" />

		<div>
			<form:errors cssClass="error" path="note" />
		</div>
	</div>
	<%-- <c:if test="${not updateTicket}"> --%>
	<br />

	<table id="fileTable">
		<tr>
			<td><input name="requestAttachments[0]" type="file" /></td>
			<td>&nbsp;<form:errors cssClass="error" path="requestAttachments" /></td>
		</tr>
	</table>

	<input class="request-buttons" id="addFile" onclick="javascript:addMoreFiles();" type="button" value="Add More Files" />
</fieldset>