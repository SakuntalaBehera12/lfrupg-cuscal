<%@ include file="init.jsp" %>

<div id="service-request-wrapper">
	<portlet:actionURL escapeXml="false" var="submitBpayUrl">
		<portlet:param name="action" value="updateOfiErrorCorrectionAction" />
	</portlet:actionURL>

	<h1>OFI Initiated BPAY Error Correction Request No. <c:out value="${ticketNumber}" /></h1>

	<form:form action="${submitBpayUrl}" enctype="multipart/form-data" id="bpayForm" method="post" modelAttribute="serviceRequestForm">
		<form:hidden path="ticketId" />
		<form:hidden path="attributesInformation.investigationType" />
		<input id="ticketNumber" name="ticketNumber" type="hidden" value="${ticketNumber}" />
		<form:hidden path="attributesInformation.bpayId" />

		<%@ include file="ofiBpayForm.jsp" %>

		<fieldset>
			<legend>Response</legend>

			<div>
				<form:label class="medium" path="attributesInformation.ofiPayerName">Payer Name<em class="mandatory">*</em></form:label>
				<form:input id="ofiPayerName" path="attributesInformation.ofiPayerName" />

				<a href="javascript:;" title="The name of your customer who processed the payment"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.ofiPayerName" />
			</div>

			<div>
				<form:label class="medium" path="attributesInformation.ofiMemberNumber">Member Number<em class="mandatory">*</em></form:label>
				<form:input id="ofiMemberNumber" path="attributesInformation.ofiMemberNumber" />

				<a href="javascript:;" title="The account number of your customer who processed the payment"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.ofiMemberNumber" />
			</div>

			<div>
				<form:label class="medium" path="attributesInformation.ofiPaymentMethod">Payment Method<em class="mandatory">*</em></form:label>
				<form:select id="ofiPaymentMethod" path="attributesInformation.ofiPaymentMethod">
					<option value="">Please select</option>

					<form:options items="${bPayListBoxData.paymentMethodMap}" />
				</form:select>

				<form:errors class="error" path="attributesInformation.ofiPaymentMethod" />
			</div>

			<div>
				<form:label class="medium" path="attributesInformation.ofiEntryMethod">Entry Method<em class="mandatory">*</em></form:label>
				<form:select id="ofiEntryMethod" path="attributesInformation.ofiEntryMethod">
					<option value="">Please select</option>

					<form:options items="${bPayListBoxData.entryMethodMap}" />
				</form:select>

				<form:errors class="error" path="attributesInformation.ofiEntryMethod" />
			</div>

			<div>
				<form:label class="large" path="attributesInformation.ofiComment">Additional Information</form:label>
				<form:textarea htmlEscape="false" path="attributesInformation.ofiComment" />

				<form:errors cssClass="error" path="attributesInformation.ofiComment" />
				<!-- <div>

				</div> -->
			</div>
		</fieldset>

		<fieldset>
			<legend>Attachment</legend>

			<table id="fileTable">
				<tr>
					<td><input name="requestAttachments[0]" type="file" /></td>
					<td>&nbsp;<form:errors cssClass="error" path="requestAttachments" /></td>
				</tr>
			</table>

			<input class="request-buttons" id="addFile" onclick="javascript:addMoreFiles();" type="button" value="Add More Files" />
		</fieldset>

		<br />

		<div id="disclaimer-wrap">
			<form:checkbox path="disclaimer" />I agree Cuscal is not liable for any errors and/or delays due to incorrect/incomplete information being supplied.
			<br />

			<form:errors class="error" path="disclaimer" />
		</div>

		<br />

		<input class="request-buttons" id="submitButton" type="submit" value="Submit" />
	</form:form>
</div>