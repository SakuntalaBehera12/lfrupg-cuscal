<%@ include file="init.jsp" %>
<div id="service-request-wrapper">
	<portlet:actionURL escapeXml="false" var="ofiDeTraceFormActionUrl">
		<portlet:param name="action" value="updateOfiDeTraceFormAction" />
	</portlet:actionURL>

	<h1>OFI Initiated Trace Request No. <c:out value="${ticketNumber}" /></h1>

	<form:form action="${ofiDeTraceFormActionUrl}" enctype="multipart/form-data" id="deForm" method="post" modelAttribute="serviceRequestForm">
		<input id="ticketNumber" name="ticketNumber" type="hidden" value="${ticketNumber}" />
		<form:hidden path="ticketId" />

		<%@ include file="ofiDeContactDetails.jsp" %>

		<fieldset>
			<legend>Request Details</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.requestFor">Request Type</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.requestFor}" /></span>

				<form:hidden path="attributesInformation.requestFor" />
			</div>
		</fieldset>

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
		</fieldset>

		<fieldset>
			<legend>Trace Account Details</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.finInstTraceBSBNumber">Trace BSB Number</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.finInstTraceBSBNumber}" /></span>

				<form:hidden path="attributesInformation.finInstTraceBSBNumber" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.accountNumberRemitter">Trace Account Number</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.accountNumberRemitter}" /></span>

				<form:hidden path="attributesInformation.accountNumberRemitter" />
			</div>

			<c:choose>
				<c:when test="${not empty serviceRequestForm.attributesInformation.remitterName}">
					<div class="de-input-wrapper">
						<form:label class="large" path="attributesInformation.remitterName">Name of Remitter</form:label>
						<span><c:out value="${serviceRequestForm.attributesInformation.remitterName}" /></span>

						<form:hidden path="attributesInformation.remitterName" />
					</div>
				</c:when>
			</c:choose>
		</fieldset>

		<fieldset>
			<legend>Response</legend>

			<c:choose>
				<c:when test="${serviceRequestForm.attributesInformation.requestFor == serviceRequestForm.finalDestination}">
					<div class="de-input-wrapper">
						<form:label class="large" path="attributesInformation.ofiDatePosted">Date posted<em class="mandatory">*</em></form:label>
						<form:input class="dpInput" id="ofiDatePosted" path="attributesInformation.ofiDatePosted" />

						<a href="javascript:;" title="The date in which the funds were credited to your customers account"><strong>?</strong></a>

						<form:errors class="error" path="attributesInformation.ofiDatePosted" />
					</div>

					<div class="de-input-wrapper">
						<form:label class="large" path="attributesInformation.ofiBsbNumber">BSB number<em class="mandatory">*</em></form:label>
						<form:input id="ofiBsbNumber" path="attributesInformation.ofiBsbNumber" />

						<form:errors class="error" path="attributesInformation.ofiBsbNumber" />
					</div>

					<div class="de-input-wrapper">
						<form:label class="large" path="attributesInformation.ofiBranch">Branch<em class="mandatory">*</em></form:label>
						<form:input id="ofiBranch" path="attributesInformation.ofiBranch" />

						<form:errors class="error" path="attributesInformation.ofiBranch" />
					</div>

					<div class="de-input-wrapper">
						<form:label class="large" path="attributesInformation.ofiAccountNumber">Account Number<em class="mandatory">*</em></form:label>
						<form:input id="ofiAccountNumber" path="attributesInformation.ofiAccountNumber" />

						<a href="javascript:;" title="The account number in which the funds were credited"><strong>?</strong></a>

						<form:errors class="error" path="attributesInformation.ofiAccountNumber" />
					</div>

					<div class="de-input-wrapper">
						<form:label class="large" path="attributesInformation.ofiAccountName">Account Name<em class="mandatory">*</em></form:label>
						<form:input id="ofiAccountName" path="attributesInformation.ofiAccountName" />

						<a href="javascript:;" title="The name of the account holder whose account was credited"><strong>?</strong></a>

						<form:errors class="error" path="attributesInformation.ofiAccountName" />
					</div>
				</c:when>
				<c:otherwise>
					<div class="de-input-wrapper">
						<form:label class="large" path="attributesInformation.ofiRemitterName">Name of Remitter<em class="mandatory">*</em></form:label>
						<form:input id="ofiRemitterName" path="attributesInformation.ofiRemitterName" />

						<form:errors class="error" path="attributesInformation.ofiRemitterName" />
					</div>
				</c:otherwise>
			</c:choose>

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