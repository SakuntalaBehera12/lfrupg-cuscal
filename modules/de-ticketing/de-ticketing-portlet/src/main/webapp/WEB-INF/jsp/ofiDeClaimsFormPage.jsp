<%@ include file="init.jsp" %>
<div id="service-request-wrapper">
	<portlet:actionURL escapeXml="false" var="ofiDeClaimsFormActionUrl">
		<portlet:param name="action" value="updateOfiDeClaimsFormAction" />
	</portlet:actionURL>

	<h1>OFI Direct Entry Claims Request No. <c:out value="${ticketNumber}" /></h1>
	<!-- <div><a href="javascript:goBack();">Return to search results</a></div> -->
	<form:form action="${ofiDeClaimsFormActionUrl}" enctype="multipart/form-data" id="deForm" method="post" modelAttribute="serviceRequestForm">
		<form:hidden path="ticketId" />
		<%-- <input name="prepopulated" id="prepopulated" type="hidden" value="${prepopulated}" /> --%>
		<input id="ticketNumber" name="ticketNumber" type="hidden" value="${ticketNumber}" />

		<%@ include file="ofiDeContactDetails.jsp" %>
		<fieldset>
			<legend>Claim details</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.batchNumber">Batch Number</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.batchNumber}" /></span>

				<form:hidden path="attributesInformation.batchNumber" />
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
				<form:label class="large" path="attributesInformation.accountTitle">Account Name</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.accountTitle}" /></span>

				<form:hidden path="attributesInformation.accountTitle" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.destinationBSB">BSB</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.destinationBSB}" /></span>

				<form:hidden path="attributesInformation.destinationBSB" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.destinationAccountNumber">Account Number</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.destinationAccountNumber}" /></span>

				<form:hidden path="attributesInformation.destinationAccountNumber" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.claimReason">Reason for Claim</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.claimReason}" /></span>

				<form:hidden path="attributesInformation.claimReason" />
			</div>

			<c:choose>
				<c:when test="${not empty serviceRequestForm.attributesInformation.drawingInTheNameOf}">
					<div class="de-input-wrapper" id="drawingInTheNameOfDiv">
						<form:label class="large" path="attributesInformation.drawingInTheNameOf">Drawing in the Name of</form:label>
						<span><c:out value="${serviceRequestForm.attributesInformation.drawingInTheNameOf}" /></span>

						<form:hidden path="attributesInformation.drawingInTheNameOf" />
					</div>
				</c:when>
			</c:choose>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.currentDDRStatus">A current DDR was held at time drawing was received</form:label>
				<span><c:out value="${serviceRequestForm.attributesInformation.currentDDRStatus}" /></span>

				<form:hidden path="attributesInformation.currentDDRStatus" />
			</div>

			<br />

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
		</fieldset>

		<fieldset>
			<legend>Response</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.ofiClaimResult">Result<em class="mandatory">*</em></form:label>
				<form:select cssClass="reset" id="ofiClaimResult" path="attributesInformation.ofiClaimResult">
					<option value="">Please select</option>

					<form:options items="${deListBoxData.ofiClaimResultMap}" />
				</form:select>

				<form:errors class="error" path="attributesInformation.ofiClaimResult" />
			</div>

			<div class="de-input-wrapper" id="claimResultAcceptedDiv">
				<form:label
					class="large"
					path="attributesInformation.ofiDrawingAmount">Draw for the amount of<em class="mandatory">*</em
				>
				</form:label
			>
				<form:input id="ofiDrawingAmount" path="attributesInformation.ofiDrawingAmount" />

				<form:errors class="error" path="attributesInformation.ofiDrawingAmount" />
			</div>

			<div class="de-input-wrapper">
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