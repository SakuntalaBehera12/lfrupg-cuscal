<%@ include file="init.jsp" %>
<div id="service-request-wrapper">
	<c:choose>
		<c:when test="${updateTicket}">
			<portlet:actionURL escapeXml="false" var="submitDeRecallFormActionUrl">
				<portlet:param name="action" value="updateDeRecallFormAction" />
			</portlet:actionURL>
		</c:when>
		<c:otherwise>
			<portlet:actionURL escapeXml="false" var="submitDeRecallFormActionUrl">
				<portlet:param name="action" value="deRecallFormAction" />
			</portlet:actionURL>
		</c:otherwise>
	</c:choose>

	<h1>Direct Entry Recall</h1>

	<form:form action="${submitDeRecallFormActionUrl}" enctype="multipart/form-data" id="deForm" method="post" modelAttribute="serviceRequestForm">
		<form:hidden path="ticketId" />
		<input id="prepopulated" name="prepopulated" type="hidden" value="${prepopulated}" />

		<%@ include file="deContactDetails.jsp" %>

		<fieldset>
			<legend>Payment Details</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.clientRefNumber">Your Reference Number</form:label>
				<form:input id="clientRefNumber" path="attributesInformation.clientRefNumber" />

				<a href="javascript:;" title="Optional. Here you can enter either your customers membership / account number or an internal reference number that you have assigned to the dispute"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.clientRefNumber" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.finInstName">Sending Financial Institution Name<em class="mandatory">*</em></form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.finInstName}" />

						<form:hidden path="attributesInformation.finInstName" />
					</c:when>
					<c:otherwise>
						<form:input id="finInstName" path="attributesInformation.finInstName" />

						<a href="javascript:;" title="This is the sending financial institution name"><strong>?</strong></a>

						<form:errors class="error" path="attributesInformation.finInstName" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.finInstUserId">Sending Financial Institution User ID<em class="mandatory">*</em></form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.finInstUserId}" />

						<form:hidden path="attributesInformation.finInstUserId" />
					</c:when>
					<c:otherwise>
						<form:input id="finInstUserId" path="attributesInformation.finInstUserId" />

						<a href="javascript:;" title="This the senders user ID number"><strong>?</strong></a>

						<form:errors class="error" path="attributesInformation.finInstUserId" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.processedDates[0]">Date Processed<em class="mandatory">*</em></form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.processedDates[0]}" />

						<form:hidden path="attributesInformation.processedDates[0]" />
					</c:when>
					<c:otherwise>
						<form:input cssClass="dpInput" id="processedDate" path="attributesInformation.processedDates[0]" />
						<!-- <a href="javascript:;" title="Date Processed"><strong>?</strong></a> -->
						<form:errors class="error" path="attributesInformation.processedDates[0]" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.destinationBSB">Destination BSB<em class="mandatory">*</em></form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.destinationBSB}" />

						<form:hidden path="attributesInformation.destinationBSB" />
					</c:when>
					<c:otherwise>
						<form:input id="destinationBSB" path="attributesInformation.destinationBSB" />
						<!-- <a href="javascript:;" title="Destination BSB"><strong>?</strong></a> -->
						<form:errors class="error" path="attributesInformation.destinationBSB" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.destinationAccountNumber">Destination Account Number<em class="mandatory">*</em></form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.destinationAccountNumber}" />

						<form:hidden path="attributesInformation.destinationAccountNumber" />
					</c:when>
					<c:otherwise>
						<form:input id="destinationAccountNumber" path="attributesInformation.destinationAccountNumber" />
						<!-- <a href="javascript:;" title="Account Number to be Credited"><strong>?</strong></a> -->
						<form:errors class="error" path="attributesInformation.destinationAccountNumber" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.transactionAmounts[0]">Amount<em class="mandatory">*</em></form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.transactionAmounts[0]}" />

						<form:hidden path="attributesInformation.transactionAmounts[0]" />
					</c:when>
					<c:otherwise>
						<form:input id="transactionAmount" path="attributesInformation.transactionAmounts[0]" />
						<!-- <a href="javascript:;" title="Amount"><strong>?</strong></a> -->
						<form:errors class="error" path="attributesInformation.transactionAmounts[0]" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.transactionCode">Transaction Type<em class="mandatory">*</em></form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.transactionCodeName}" />

						<form:hidden path="attributesInformation.transactionCode" />
						<form:hidden path="attributesInformation.transactionCodeName" />
					</c:when>
					<c:otherwise>
						<form:select id="transactionCode" path="attributesInformation.transactionCode">
							<option value="">Please select</option>

							<form:options items="${deListBoxData.transactionCodeMap}" />
						</form:select>
						<!-- <a href="javascript:;" title="Transaction Type"><strong>?</strong></a> -->
						<form:errors class="error" path="attributesInformation.transactionCode" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.accountTitle">Account Name<em class="mandatory">*</em></form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.accountTitle}" />

						<form:hidden path="attributesInformation.accountTitle" />
					</c:when>
					<c:otherwise>
						<form:input id="accountTitle" path="attributesInformation.accountTitle" />
						<!-- <a href="javascript:;" title="Title of Account"><strong>?</strong></a> -->
						<form:errors class="error" path="attributesInformation.accountTitle" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.destinationLodgementRefs[0]">Lodgement Reference<!-- <em class="mandatory">*</em> --></form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.destinationLodgementRefs[0]}" />

						<form:hidden path="attributesInformation.destinationLodgementRefs[0]" />
					</c:when>
					<c:otherwise>
						<form:input id="destinationLodgementRef" path="attributesInformation.destinationLodgementRefs[0]" />
						<!-- <a href="javascript:;" title="Lodgement Reference"><strong>?</strong></a> -->
						<form:errors class="error" path="attributesInformation.destinationLodgementRefs[0]" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.remitterName">Name of Remitter</form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.remitterName}" />

						<form:hidden path="attributesInformation.remitterName" />
					</c:when>
					<c:otherwise>
						<form:input id="remitterName" path="attributesInformation.remitterName" />

						<a href="javascript:;" title="This is the name of your member / customer who processed the transaction"><strong>?</strong></a>

						<form:errors class="error" path="attributesInformation.remitterName" />
					</c:otherwise>
				</c:choose>
			</div>
		</fieldset>

		<fieldset>
			<legend>Remit Payment To</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverAccountName">Account Name<em class="mandatory">*</em></form:label>
				<form:input id="receiverAccountName" path="attributesInformation.receiverAccountName" />
				<!-- <a href="javascript:;" title="Full Account Name"><strong>?</strong></a> -->
				<form:errors class="error" path="attributesInformation.receiverAccountName" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverBSBNumber">BSB Number<em class="mandatory">*</em></form:label>
				<form:input id="receiverBSBNumber" path="attributesInformation.receiverBSBNumber" />
				<!-- <a href="javascript:;" title="BSB Number"><strong>?</strong></a> -->
				<form:errors class="error" path="attributesInformation.receiverBSBNumber" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverAccountNumber">Account Number<em class="mandatory">*</em></form:label>
				<form:input id="receiverAccountNumber" path="attributesInformation.receiverAccountNumber" />
				<!-- <a href="javascript:;" title="Account Number"><strong>?</strong></a> -->
				<form:errors class="error" path="attributesInformation.receiverAccountNumber" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverLodgementRef">Reference</form:label>
				<form:input id="reference" path="attributesInformation.receiverLodgementRef" />

				<form:errors class="error" path="attributesInformation.receiverLodgementRef" />
			</div>
		</fieldset>

		<fieldset>
			<legend>Additional Information</legend>

			<div>
				<em>Please do not enter PAN details into the comments section.</em>

				<form:textarea htmlEscape="false" path="attributesInformation.destinationAdditionalInfo" />

				<div>
					<form:errors cssClass="error" path="attributesInformation.destinationAdditionalInfo" />
				</div>
			</div>

			<br />

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

		<c:choose>
			<c:when test="${not updateTicket}">
				<input class="request-buttons" type="submit" value="Submit" />
			</c:when>
			<c:otherwise>
				<input class="request-buttons" type="submit" value="Resubmit" />
			</c:otherwise>
		</c:choose>
	</form:form>
</div>