<%@ include file="init.jsp" %>
<div id="service-request-wrapper">
<portlet:actionURL escapeXml="false" var="ofiDeMistakenPaymentFormActionUrl">
	<portlet:param name="action" value="updateOfiDeMistakenPaymentFormAction" />
</portlet:actionURL>

<h1>OFI Initiated Mistaken Internet Payment Request No. <c:out value="${ticketNumber}" /></h1>

<p class="instruction">This is to advise that a Record Type 1 Item issued by us as the Sending Member has been sent to an incorrect account as a Mistaken Payment pursuant to a payment instruction given by a customer who benefits from the provisions of the ePayments Code.</p>

<form:form action="${ofiDeMistakenPaymentFormActionUrl}" enctype="multipart/form-data" id="deForm" method="post" modelAttribute="serviceRequestForm">
	<form:hidden path="ticketId" />
	<input id="ticketNumber" name="ticketNumber" type="hidden" value="${ticketNumber}" />

	<%@ include file="ofiDeContactDetails.jsp" %>
	<fieldset>
		<legend>Details of Item Sent</legend>

		<div class="de-input-wrapper">
			<form:label class="large" path="attributesInformation.timeframe">Time frame</form:label>
			<span><c:out value="${serviceRequestForm.attributesInformation.timeframe}" /></span>

			<form:hidden path="attributesInformation.timeframe" />
		</div>

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
			<form:label class="large" path="attributesInformation.accountTitle">Account Name</form:label>
			<span><c:out value="${serviceRequestForm.attributesInformation.accountTitle}" /></span>

			<form:hidden path="attributesInformation.accountTitle" />
		</div>

		<c:forEach begin="0" end="${amountCount}" var="i">
			<div class="divTable" id="disputed-amounts${i}">
				<div class="divRow">
					<div align="center" class="divCell"><form:label class="more-amounts-label-width" path="attributesInformation.processedDates[${i}]">Date Processed</form:label></div>
					<div class="divCell"><form:label class="more-amounts-label-width" path="attributesInformation.destinationLodgementRefs[${i}]">Lodgement Reference</form:label></div>
					<div class="divCell"><form:label class="more-amounts-label-width" path="attributesInformation.transactionAmounts[${i}]">Amount</form:label></div>
				</div>

				<div class="divRow">
					<div class="divCell">
						<c:out value="${serviceRequestForm.attributesInformation.processedDates[i]}" />

						<form:hidden path="attributesInformation.processedDates[${i}]" />
					</div>

					<div class="divCell">
						<c:out value="${serviceRequestForm.attributesInformation.destinationLodgementRefs[i]}" />

						<form:hidden path="attributesInformation.destinationLodgementRefs[${i}]" />
					</div>

					<div class="divCell">
						<c:out value="${serviceRequestForm.attributesInformation.transactionAmounts[i]}" />

						<form:hidden path="attributesInformation.transactionAmounts[${i}]" />
					</div>

					<div class="divCell">
					</div>
				</div>
			</div>
		</c:forEach>
	</fieldset>

	<fieldset>
		<legend>Trace Record Details</legend>

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

		<div class="de-input-wrapper">
			<form:label class="large" path="attributesInformation.remitterName">Name of Remitter</form:label>
			<span><c:out value="${serviceRequestForm.attributesInformation.remitterName}" /></span>

			<form:hidden path="attributesInformation.remitterName" />
		</div>
	</fieldset>

	<fieldset>
		<legend>Reason for Dispute</legend>

		<c:choose>
			<c:when test="${not empty serviceRequestForm.attributesInformation.intendedAccountOrWrongPayee}">
				<div class="de-input-wrapper" id="intendedAccountOrWrongPayeeDiv">
					<form:label class="large" path="attributesInformation.intendedAccountOrWrongPayee">Reason</form:label>
					<span><c:out value="${serviceRequestForm.attributesInformation.intendedAccountOrWrongPayee}" /></span>

					<form:hidden path="attributesInformation.intendedAccountOrWrongPayee" />
				</div>

				<c:choose>
					<c:when test="${not empty serviceRequestForm.attributesInformation.intendedAccountBSBNumber}">
						<form:label path="" style="float: none;">Intended Account Details:</form:label>
						<div class="de-input-wrapper" id="intendedAccountBSBNumberDiv">
							<form:label class="large" path="attributesInformation.intendedAccountBSBNumber">BSB Number</form:label>
							<span><c:out value="${serviceRequestForm.attributesInformation.intendedAccountBSBNumber}" /></span>

							<form:hidden path="attributesInformation.intendedAccountBSBNumber" />
						</div>
					</c:when>
				</c:choose>

				<c:choose>
					<c:when test="${not empty serviceRequestForm.attributesInformation.intendedAccountNumber}">
						<div class="de-input-wrapper" id="intendedAccountNumberDiv">
							<form:label class="large" path="attributesInformation.intendedAccountNumber">Account Number</form:label>
							<span><c:out value="${serviceRequestForm.attributesInformation.intendedAccountNumber}" /></span>

							<form:hidden path="attributesInformation.intendedAccountNumber" />
						</div>
					</c:when>
				</c:choose>

				<c:choose>
					<c:when test="${not empty serviceRequestForm.attributesInformation.intendedAccountTitle}">
						<div class="de-input-wrapper" id="intendedAccountTitleDiv">
							<form:label class="large" path="attributesInformation.intendedAccountTitle">Title of Account</form:label>
							<span><c:out value="${serviceRequestForm.attributesInformation.intendedAccountTitle}" /></span>

							<form:hidden path="attributesInformation.intendedAccountTitle" />
						</div>
					</c:when>
				</c:choose>

				<c:choose>
					<c:when test="${not empty serviceRequestForm.attributesInformation.customerDeclarationAttached}">
						<div class="de-input-wrapper" id="customerDeclarationAttachedDiv">
							<form:label class="large" path="attributesInformation.customerDeclarationAttached">Copy of customer's declaration attached (if required)</form:label>
							<span><c:out value="${serviceRequestForm.attributesInformation.customerDeclarationAttached}" /></span>

							<form:hidden path="attributesInformation.customerDeclarationAttached" />
						</div>
					</c:when>
				</c:choose>
			</c:when>
		</c:choose>
	</fieldset>

	<fieldset>
		<legend>Remit Payment To</legend>

		<div class="de-input-wrapper">
			<form:label class="large" path="attributesInformation.receiverAccountName">Account Name</form:label>
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

			<form:hidden path="attributesInformation.receiverLodgementRef" />
		</div>
	</fieldset>

	<fieldset>
		<legend>Response</legend>

		<div class="de-input-wrapper">
			<form:label class="tiny" path="attributesInformation.ofiFundAvailability">Result:<em class="mandatory">*</em></form:label>
			<form:select cssClass="reset" id="ofiFundAvailability" path="attributesInformation.ofiFundAvailability">
				<option value="">Please select</option>

				<form:options items="${deListBoxData.ofiMistakenResultMap}" />
			</form:select>

			<form:errors class="error" path="attributesInformation.ofiFundAvailability" />
		</div>

		<div>
			<form:checkboxes items="${deListBoxData.ofiMistakenCheckboxResultMap}" path="attributesInformation.ofiMistakenResults" />
		</div>

		<br />

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