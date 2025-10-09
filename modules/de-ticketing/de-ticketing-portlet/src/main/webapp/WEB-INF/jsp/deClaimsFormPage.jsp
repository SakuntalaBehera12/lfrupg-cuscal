<%@ include file="init.jsp" %>
<div id="service-request-wrapper">
	<c:choose>
		<c:when test="${updateTicket}">
			<portlet:actionURL escapeXml="false" var="submitDeClaimsFormActionUrl">
				<portlet:param name="action" value="updateDeClaimsFormAction" />
			</portlet:actionURL>
		</c:when>
		<c:otherwise>
			<portlet:actionURL escapeXml="false" var="submitDeClaimsFormActionUrl">
				<portlet:param name="action" value="deClaimsFormAction" />
			</portlet:actionURL>
		</c:otherwise>
	</c:choose>

	<h1>Direct Entry Claims</h1>
	<!-- <div><a href="javascript:goBack();">Return to search results</a></div> -->
	<form:form action="${submitDeClaimsFormActionUrl}" enctype="multipart/form-data" id="deForm" method="post" modelAttribute="serviceRequestForm">
		<c:choose>
			<c:when test="${amountCount == null}">
				<input id="more-amounts" name="more-amounts" type="hidden" value="0" />
			</c:when>
			<c:otherwise>
				<input id="more-amounts" name="more-amounts" type="hidden" value="${amountCount}" />
			</c:otherwise>
		</c:choose>

		<form:hidden path="ticketId" />
		<input id="prepopulated" name="prepopulated" type="hidden" value="${prepopulated}" />

		<%@ include file="deContactDetails.jsp" %>
		<fieldset>
			<legend>Claim details</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.clientRefNumber">Your Reference Number</form:label>
				<form:input id="clientRefNumber" path="attributesInformation.clientRefNumber" />

				<a href="javascript:;" title="Optional. Here you can enter either your customers membership / account number or an internal reference number that you have assigned to the dispute"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.clientRefNumber" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.batchNumber">Batch Number<em class="mandatory">*</em></form:label>
				<c:choose>
					<c:when test="${prepopulated}">
						<c:out value="${serviceRequestForm.attributesInformation.batchNumber}" />

						<form:hidden path="attributesInformation.batchNumber" />
					</c:when>
					<c:otherwise>
						<form:input id="batchNumber" path="attributesInformation.batchNumber" />

						<a href="javascript:;" title="Batch Number assigned in Direct Entry to the Claimants Value"><strong>?</strong></a>

						<form:errors class="error" path="attributesInformation.batchNumber" />
					</c:otherwise>
				</c:choose>
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

						<a href="javascript:;" title="Name assigned to the USER ID"><strong>?</strong></a>

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

						<a href="javascript:;" title="Is the 6 digit ID number assigned to the Name of the Debit User"><strong>?</strong></a>

						<form:errors class="error" path="attributesInformation.finInstUserId" />
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
						<!-- <a href="javascript:;" title="Name of Account Debited"><strong>?</strong></a> -->
						<form:errors class="error" path="attributesInformation.accountTitle" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.destinationBSB">BSB<em class="mandatory">*</em></form:label>
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
				<form:label class="large" path="attributesInformation.destinationAccountNumber">Account Number<em class="mandatory">*</em></form:label>
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
				<form:label class="large" path="attributesInformation.claimReason">Reason for claim<em class="mandatory">*</em></form:label>
				<form:select id="claimReason" path="attributesInformation.claimReason">
					<option value="">Please select</option>

					<form:options items="${deListBoxData.claimReasonMap}" />
				</form:select>

				<a href="javascript:;" title="Claimant's reason why they have raised a claim"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.claimReason" />
			</div>

			<div class="de-input-wrapper" id="drawingInTheNameOfDiv">
				<form:label class="large" path="attributesInformation.drawingInTheNameOf">Drawing in the name of<em class="mandatory">*</em></form:label>
				<form:input id="drawingInTheNameOf" path="attributesInformation.drawingInTheNameOf" />
				<!-- <a href="javascript:;" title="Name of Account Debited"><strong>?</strong></a> -->
				<form:errors class="error" path="attributesInformation.drawingInTheNameOf" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.currentDDRStatus">A current DDR was held at time drawing was received<em class="mandatory">*</em></form:label>
				<form:select id="currentDDRStatus" path="attributesInformation.currentDDRStatus">
					<option value="">Please select</option>

					<form:options items="${deListBoxData.generalYesNoMap}" />
				</form:select>

				<a href="javascript:;" title="Does the Claimant hold a current DDR Authority with the Company or FI they are claiming against"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.currentDDRStatus" />
			</div>

			<br />

			<c:forEach begin="0" end="${amountCount}" var="i">
				<div class="divTable" id="disputed-amounts${i}">
					<div class="divRow">
						<div align="center" class="divCell"><form:label class="more-amounts-label-width" path="attributesInformation.processedDates[${i}]">Date Processed<em class="mandatory">*</em></form:label></div>
						<div class="divCell"><form:label class="more-amounts-label-width" path="attributesInformation.destinationLodgementRefs[${i}]">Lodgement Reference<em class="mandatory">*</em></form:label></div>
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

									<span class="error"></span>
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

									<a href="javascript:;" title="Reference Number provided by Debit User to identify transaction"><strong>?</strong></a>

									<br />

									<form:errors class="error" path="attributesInformation.destinationLodgementRefs[${i}]" />

									<span class="error"></span>
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

									<span class="error"></span>
								</c:otherwise>
							</c:choose>
						</div>

						<div class="divCell">
						</div>
					</div>
				</div>
			</c:forEach>

			<input class="add-amounts request-buttons" onclick="javascript:addMoreAmounts();" type="button" value="Add more amounts" />

			<span class="error" id="maxClaimsError"></span>
		</fieldset>

		<fieldset>
			<legend>Remit Payment To</legend>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverAccountName">Account Name<em class="mandatory">*</em></form:label>
				<form:input id="receiverAccountName" path="attributesInformation.receiverAccountName" />

				<a href="javascript:;" title="Account Name of the account to which will be refunded"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.receiverAccountName" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverBSBNumber">BSB Number<em class="mandatory">*</em></form:label>
				<form:input id="receiverBSBNumber" path="attributesInformation.receiverBSBNumber" />

				<a href="javascript:;" title="The BSB number that you would like the funds returned to"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.receiverBSBNumber" />
			</div>

			<div class="de-input-wrapper">
				<form:label class="large" path="attributesInformation.receiverAccountNumber">Account Number<em class="mandatory">*</em></form:label>
				<form:input id="receiverAccountNumber" path="attributesInformation.receiverAccountNumber" />

				<a href="javascript:;" title="Account number that you would like the funds returned to"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.receiverAccountNumber" />
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