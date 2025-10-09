<%@ include file="init.jsp" %>
<div id="service-request-wrapper">
	<portlet:actionURL escapeXml="false" var="ofiDeRecallFormActionUrl">
		<portlet:param name="action" value="updateOfiDeRecallFormAction" />
	</portlet:actionURL>

	<h1>OFI Initiated Recall Request No. <c:out value="${ticketNumber}" /></h1>

	<form:form action="${ofiDeRecallFormActionUrl}" enctype="multipart/form-data" id="deForm" method="post" modelAttribute="serviceRequestForm">
		<form:hidden path="ticketId" />
		<input id="ticketNumber" name="ticketNumber" type="hidden" value="${ticketNumber}" />

		<%@ include file="ofiDeContactDetails.jsp" %>

		<fieldset>
			<legend>Payment Details</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.finInstName">Sending Financial Institution Name</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.finInstName}" /></span>

				<form:hidden path="attributesInformation.finInstName" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.finInstUserId">Sending Financial Institution User ID</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.finInstUserId}" /></span>

				<form:hidden path="attributesInformation.finInstUserId" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.processedDates[0]">Date Processed</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.processedDates[0]}" /></span>

				<form:hidden path="attributesInformation.processedDates[0]" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.destinationBSB">Destination BSB</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.destinationBSB}" /></span>

				<form:hidden path="attributesInformation.destinationBSB" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.destinationAccountNumber">Destination Account Number</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.destinationAccountNumber}" /></span>

				<form:hidden path="attributesInformation.destinationAccountNumber" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.transactionAmounts[0]">Amount</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.transactionAmounts[0]}" /></span>

				<form:hidden path="attributesInformation.transactionAmounts[0]" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.transactionCode">Transaction Type</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.transactionCode}" /></span>

				<form:hidden path="attributesInformation.transactionCode" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.accountTitle">Account Name</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.accountTitle}" /></span>

				<form:hidden path="attributesInformation.accountTitle" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.destinationLodgementRefs[0]">Lodgement Reference</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.destinationLodgementRefs[0]}" /></span>

				<form:hidden path="attributesInformation.destinationLodgementRefs[0]" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.remitterName">Name of Remitter</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.remitterName}" /></span>

				<form:hidden path="attributesInformation.remitterName" />
			</div>
		</fieldset>

		<fieldset>
			<legend>Remit Payment To</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverAccountName">Full Account Name</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.receiverAccountName}" /></span>

				<form:hidden path="attributesInformation.receiverAccountName" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverBSBNumber">BSB Number</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.receiverBSBNumber}" /></span>

				<form:hidden path="attributesInformation.receiverBSBNumber" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverAccountNumber">Account Number</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.receiverAccountNumber}" /></span>

				<form:hidden path="attributesInformation.receiverAccountNumber" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverLodgementRef">Reference</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.receiverLodgementRef}" /></span>

				<form:hidden path="attributesInformation.reference" />
			</div>
		</fieldset>

		<fieldset>
			<legend>Resolution</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.ofiRecallResult">Result<em class="mandatory">*</em></form:label>
				<form:select cssClass="reset" id="ofiRecallResult" path="attributesInformation.ofiRecallResult">
					<option value="">Please select</option>

					<form:options items="${deListBoxData.ofiRecallResultMap}" />
				</form:select>

				<form:errors class="error" path="attributesInformation.ofiRecallResult" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.ofiDateResolved">Date Resolved<em class="mandatory">*</em></form:label>
				<form:input class="dpInput" id="ofiDateResolved" path="attributesInformation.ofiDateResolved" />

				<form:errors class="error" path="attributesInformation.ofiDateResolved" />
			</div>

			<div>
				<form:label class="large" path="attributesInformation.ofiAdditionalInfo">Additional Information</form:label>
				<form:textarea htmlEscape="false" path="attributesInformation.ofiAdditionalInfo" />

				<div>
					<form:errors cssClass="error" path="attributesInformation.ofiAdditionalInfo" />
				</div>
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
			<form:checkbox path="disclaimer" /> I agree Cuscal is not liable for any errors and/or delays due to incorrect/incomplete information being supplied.
			<br />

			<form:errors class="error" path="disclaimer" />
		</div>

		<br />

		<input class="request-buttons" type="submit" value="Submit" />
	</form:form>
</div>