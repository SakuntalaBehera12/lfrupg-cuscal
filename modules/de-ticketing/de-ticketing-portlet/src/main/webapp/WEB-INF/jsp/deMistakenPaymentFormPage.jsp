<%@ include file="init.jsp" %>
<div id="service-request-wrapper">
<c:choose>
	<c:when test="${updateTicket}">
		<portlet:actionURL escapeXml="false" var="submitDeMistakenPaymentFormActionUrl">
			<portlet:param name="action" value="updateDeMistakenPaymentFormAction" />
		</portlet:actionURL>
	</c:when>
	<c:otherwise>
		<portlet:actionURL escapeXml="false" var="submitDeMistakenPaymentFormActionUrl">
			<portlet:param name="action" value="deMistakenPaymentFormAction" />
		</portlet:actionURL>
	</c:otherwise>
</c:choose>

<h1>Direct Entry Mistaken Internet Payment</h1>
<form:form action="${submitDeMistakenPaymentFormActionUrl}" enctype="multipart/form-data" id="deForm" method="post" modelAttribute="serviceRequestForm">
	<input id="more-amounts" name="more-amounts" type="hidden" value="-1" />
	<form:hidden path="ticketId" />
	<input id="prepopulated" name="prepopulated" type="hidden" value="${prepopulated}" />

	<%@ include file="deContactDetails.jsp" %>
	<fieldset>
		<legend>Details of Item Sent</legend>

		<div class="de-input-wrapper">
			<form:label class="large" path="attributesInformation.clientRefNumber">Your Reference Number</form:label>
			<form:input id="clientRefNumber" path="attributesInformation.clientRefNumber" />

			<a href="javascript:;" title="Optional. Here you can enter either your customers membership / account number or an internal reference number that you have assigned to the dispute"><strong>?</strong></a>

			<form:errors class="error" path="attributesInformation.clientRefNumber" />
		</div>

		<div class="de-input-wrapper">
			<form:label class="large" path="attributesInformation.timeframe">Select Time frame<em class="mandatory">*</em></form:label>
			<form:select id="timeframe" path="attributesInformation.timeframe">
				<option value="">Please select</option>

				<form:options items="${deListBoxData.timeframeMap}" />
			</form:select>

			<a href="javascript:;" title="This is the time in which your member reported the transaction to your financial institution"><strong>?</strong></a>

			<br />

			<form:errors class="error" path="attributesInformation.timeframe" />
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
			<form:label class="large" path="attributesInformation.destinationBSB">Destination BSB<em class="mandatory">*</em></form:label>
			<c:choose>
				<c:when test="${prepopulated}">
					<c:out value="${serviceRequestForm.attributesInformation.destinationBSB}" />

					<form:hidden path="attributesInformation.destinationBSB" />
				</c:when>
				<c:otherwise>
					<form:input id="destinationBSB" path="attributesInformation.destinationBSB" />
					<!-- <a href="javascript:;" title="BSB Number"><strong>?</strong></a> -->
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
					<!-- <a href="javascript:;" title="Account Number"><strong>?</strong></a> -->
					<form:errors class="error" path="attributesInformation.destinationAccountNumber" />
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

		<br />

		<c:forEach begin="0" end="${amountCount}" var="i">
			<div class="divTable" id="disputed-amounts${i}">
				<div class="divRow">
					<div align="center" class="divCell"><form:label class="more-amounts-label-width" path="attributesInformation.processedDates[${i}]">Date Processed<em class="mandatory">*</em></form:label></div>
					<div class="divCell"><form:label class="more-amounts-label-width" path="attributesInformation.destinationLodgementRefs[${i}]">Lodgement Reference<!-- <em class="mandatory">*</em> --></form:label></div>
					<div class="divCell"><form:label class="more-amounts-label-width" path="attributesInformation.transactionAmounts[${i}]">Amount<em class="mandatory">*</em></form:label></div>
				</div>

				<div class="divRow">
					<div class="divCell">
						<c:choose>
							<c:when test="${prepopulated && i == 0}">
								<form:input id="processedDate" path="attributesInformation.processedDates[${i}]" readonly="true" width="100px" />
							</c:when>
							<c:otherwise>
								<form:input cssClass="dpInput" id="processedDate" path="attributesInformation.processedDates[${i}]" width="100px" />
								<!-- <a href="javascript:;" title="Date debited"><strong>?</strong></a> -->
								<br />

								<form:errors class="error" path="attributesInformation.processedDates[${i}]" />
							</c:otherwise>
						</c:choose>
					</div>

					<div class="divCell">
						<c:choose>
							<c:when test="${prepopulated && i == 0}">
								<form:input id="destinationLodgementRef" path="attributesInformation.destinationLodgementRefs[${i}]" readonly="true" />
							</c:when>
							<c:otherwise>
								<form:input id="destinationLodgementRef" path="attributesInformation.destinationLodgementRefs[${i}]" />
								<!-- <a href="javascript:;" title="Lodgement Reference"><strong>?</strong></a> -->
								<br />

								<form:errors class="error" path="attributesInformation.destinationLodgementRefs[${i}]" />
							</c:otherwise>
						</c:choose>
					</div>

					<div class="divCell">
						<c:choose>
							<c:when test="${prepopulated && i == 0}">
								<form:input id="transactionAmount" path="attributesInformation.transactionAmounts[${i}]" readonly="true" />
							</c:when>
							<c:otherwise>
								<form:input id="transactionAmount" path="attributesInformation.transactionAmounts[${i}]" />
								<!-- <a href="javascript:;" title="Amount"><strong>?</strong></a> -->
								<br />

								<form:errors class="error" path="attributesInformation.transactionAmounts[${i}]" />
							</c:otherwise>
						</c:choose>
					</div>
					<!-- <div class="divCell"></div> -->
					<!-- <div><a href="#" class="removeLink">X</a></div> -->
				</div>
			</div>
		</c:forEach>
		<!-- <input class="request-buttons add-amounts" onclick="javascript:addMoreAmounts();" type="button" value="Add more amounts" /> -->
		<span class="error" id="maxClaimsError"></span>
	</fieldset>

	<fieldset>
		<legend>Trace Record Details</legend>

		<div class="de-input-wrapper">
			<form:label class="large" path="attributesInformation.finInstTraceBSBNumber">Trace BSB Number<em class="mandatory">*</em></form:label>
			<c:choose>
				<c:when test="${prepopulated}">
					<c:out value="${serviceRequestForm.attributesInformation.finInstTraceBSBNumber}" />

					<form:hidden path="attributesInformation.finInstTraceBSBNumber" />
				</c:when>
				<c:otherwise>
					<form:input id="finInstTraceBSBNumber" path="attributesInformation.finInstTraceBSBNumber" />

					<a href="javascript:;" title="This is the BSB in which the transaction was processed from"><strong>?</strong></a>

					<form:errors class="error" path="attributesInformation.finInstTraceBSBNumber" />
				</c:otherwise>
			</c:choose>
		</div>

		<div class="de-input-wrapper">
			<form:label class="large" path="attributesInformation.accountNumberRemitter">Trace Account Number<em class="mandatory">*</em></form:label>
			<c:choose>
				<c:when test="${prepopulated}">
					<c:out value="${serviceRequestForm.attributesInformation.accountNumberRemitter}" />

					<form:hidden path="attributesInformation.accountNumberRemitter" />
				</c:when>
				<c:otherwise>
					<form:input id="accountNumberRemitter" path="attributesInformation.accountNumberRemitter" />

					<a href="javascript:;" title="This is the account number in which the transaction was processed from"><strong>?</strong></a>

					<form:errors class="error" path="attributesInformation.accountNumberRemitter" />
				</c:otherwise>
			</c:choose>
		</div>

		<div class="de-input-wrapper">
			<form:label class="large" path="attributesInformation.remitterName">Name of Remitter<em class="mandatory">*</em></form:label>
			<c:choose>
				<c:when test="${prepopulated}">
					<c:out value="${serviceRequestForm.attributesInformation.remitterName}" />

					<form:hidden path="attributesInformation.remitterName" />
				</c:when>
				<c:otherwise>
					<form:input id="remitterName" path="attributesInformation.remitterName" />

					<a href="javascript:;" title="This is the name of the person who processed the transaction"><strong>?</strong></a>

					<form:errors class="error" path="attributesInformation.remitterName" />
				</c:otherwise>
			</c:choose>
		</div>
	</fieldset>

	<fieldset>
		<legend>Reason for Dispute</legend>

		<div class="de-input-wrapper">
			<%-- <form:label class="medium" path="attributesInformation.intendedAccountOrWrongPayee">??????<em class="mandatory">*</em></form:label> --%>
			<form:select cssClass="reset" id="intendedAccountOrWrongPayee" path="attributesInformation.intendedAccountOrWrongPayee">
				<option value="">Please select</option>

				<form:options items="${deListBoxData.intendedAccountDetailsMap}" />
			</form:select>
			<!-- <a href="javascript:;" title=""><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.intendedAccountOrWrongPayee" />
		</div>

		<div id="intendedAccountDiv">
			<form:label path="" style="float: none;">Intended Account Details:</form:label>
			<div class="de-input-wrapper" id="intendedAccountBSBNumberDiv">
				<form:label class="large" path="attributesInformation.intendedAccountBSBNumber">BSB Number<em class="mandatory">*</em></form:label>
				<form:input id="intendedAccountBSBNumber" path="attributesInformation.intendedAccountBSBNumber" />

				<form:errors class="error" path="attributesInformation.intendedAccountBSBNumber" />
			</div>

			<div class="de-input-wrapper" id="intendedAccountNumberDiv">
				<form:label class="large" path="attributesInformation.intendedAccountNumber">Account Number<em class="mandatory">*</em></form:label>
				<form:input id="intendedAccountNumber" path="attributesInformation.intendedAccountNumber" />

				<form:errors class="error" path="attributesInformation.intendedAccountNumber" />
			</div>

			<div class="de-input-wrapper" id="intendedAccountTitleDiv">
				<form:label class="large" path="attributesInformation.intendedAccountTitle">Title of Account<em class="mandatory">*</em></form:label>
				<form:input id="intendedAccountTitle" path="attributesInformation.intendedAccountTitle" />

				<form:errors class="error" path="attributesInformation.intendedAccountTitle" />
			</div>
		</div>

		<div class="de-input-wrapper" id="customerDeclarationAttachedDiv">
			<form:label class="large" path="attributesInformation.customerDeclarationAttached">Copy of customer's declaration attached (if required)<em class="mandatory">*</em></form:label>
			<form:select id="customerDeclarationAttached" path="attributesInformation.customerDeclarationAttached">
				<option value="">Please select</option>

				<form:options items="${deListBoxData.generalYesNoMap}" />
			</form:select>

			<form:errors class="error" path="attributesInformation.customerDeclarationAttached" />
		</div>
	</fieldset>

	<fieldset>
		<%--<legend>Funds to be Returned To</legend> --%>
		<legend>Remit Payment To</legend>

		<div class="de-input-wrapper">
			<form:label class="large" path="attributesInformation.receiverAccountName">Account Name<em class="mandatory">*</em></form:label>
			<form:input id="receiverAccountName" path="attributesInformation.receiverAccountName" />
			<!-- <a href="javascript:;" title="Account Title"><strong>?</strong></a> -->
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
			<form:label class="large" path="attributesInformation.receiverLodgementRef">Reference<em class="mandatory">*</em></form:label>
			<form:input id="receiverLodgementRef" path="attributesInformation.receiverLodgementRef" />
		<!-- <a href="javascript:;" title="Reference"><strong>?</strong></a> -->
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