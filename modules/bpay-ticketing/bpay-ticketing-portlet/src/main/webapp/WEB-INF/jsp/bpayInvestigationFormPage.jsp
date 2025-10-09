<%@ include file="init.jsp" %>
<%@ page import="au.com.cuscal.bpay.ticketing.domain.BPayListBoxData" %>

<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>

<%@ page import="java.io.IOException" %>

<%
ObjectMapper objectMapper = new ObjectMapper();
String fraudTypeDescriptions = null;
String scamTypeDescriptions = null;
Object bPayListBoxData = request.getAttribute("bPayListBoxData");

// Check if bPayListBoxData is not null and has a fraudTypeMap property

if ((bPayListBoxData != null) && (bPayListBoxData instanceof BPayListBoxData)) {
	BPayListBoxData fraudTypeTitleMap = (BPayListBoxData)bPayListBoxData;

	try {
		fraudTypeDescriptions = objectMapper.writeValueAsString(fraudTypeTitleMap.getFraudTypeToolTipMap());
	}
	catch (IOException e) {
		e.printStackTrace();
	}
}
%>

<div id="service-request-wrapper">
<c:choose>
	<c:when test="${updateTicket}">
		<portlet:actionURL escapeXml="false" var="submitBpayUrl">
			<portlet:param name="action" value="updateBpayInvestigationAction" />
		</portlet:actionURL>
	</c:when>
	<c:otherwise>
		<portlet:actionURL escapeXml="false" var="submitBpayUrl">
			<portlet:param name="action" value="submitBpayInvestigationAction" />
		</portlet:actionURL>
	</c:otherwise>
</c:choose>

<h1>BPAY Investigation</h1>

<form:form action="${submitBpayUrl}" enctype="multipart/form-data" id="bpayForm" method="post" modelAttribute="serviceRequestForm">
	<form:hidden path="ticketId" />
	<form:hidden path="attributesInformation.bpayId" />

	<fieldset>
		<legend>Your Contact Details</legend>

		<div class="contact-info">
			<label class="">Organisation</label>

			<span><c:out value="${serviceRequestForm.contactInformation.organisation}" /></span>

			<form:hidden id="organisation" path="contactInformation.organisation" />
		</div>

		<div class="contact-info">
			<label class="">Organisation BSB</label>

			<span><c:out value="${serviceRequestForm.contactInformation.bsb}" /></span>

			<form:hidden id="bsb" path="contactInformation.bsb" />
			<form:hidden id="bsb" path="attributesInformation.bsb" value="${serviceRequestForm.contactInformation.bsb}" />
		</div>

		<br />

		<div class="contact-info">
			<label class="">First Name</label>

			<span><c:out value="${serviceRequestForm.contactInformation.givenName}" /></span>

			<form:hidden id="givenName" path="contactInformation.givenName" />
		</div>

		<div class="contact-info">
			<label class="">Last Name</label>

			<span><c:out value="${serviceRequestForm.contactInformation.surname}" /></span>

			<form:hidden id="surname" path="contactInformation.surname" />
		</div>

		<br />

		<div class="contact-info">
			<label class="">Email</label>

			<span><c:out value="${serviceRequestForm.contactInformation.email}" /></span>

			<form:hidden id="email" path="contactInformation.email" />
		</div>
	</fieldset>

	<fieldset>
		<legend>BPAY Investigation</legend>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.investigationType">Investigation Type<em class="mandatory">*</em></form:label>
			<form:select id="investigationType" path="attributesInformation.investigationType">
				<option value="">Please select</option>

				<form:options items="${bPayListBoxData.investigationTypeMap}" />
			</form:select>

			<form:errors class="error" path="attributesInformation.investigationType" />
		</div>

		<div class="bpay-input-wrapper" id="paymentInvestigationReasonDiv">
			<form:label class="medium" path="attributesInformation.paymentInvestigationReason">Reason for Request<em class="mandatory">*</em></form:label>
			<form:select id="reasonForRequestPaymentInvestigation" path="attributesInformation.paymentInvestigationReason">
				<option value="">Please select</option>

				<form:options items="${bPayListBoxData.reasonPaymentInvestigationMap}" />
			</form:select>
			<!-- <a href="javascript:;" title="This should be the actual reason for the service request"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.paymentInvestigationReason" />
		</div>

		<div class="bpay-input-wrapper" id="errorCorrectionReasonDiv">
			<form:label class="medium" path="attributesInformation.errorCorrectionReason">Reason for Request<em class="mandatory">*</em></form:label>
			<form:select id="reasonForRequestErrorCorrection" path="attributesInformation.errorCorrectionReason">
				<option value="">Please select</option>

				<form:options items="${bPayListBoxData.reasonErrorCorrectionMap}" />
			</form:select>
			<!-- <a href="javascript:;" title="This should be the actual reason for the service request"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.errorCorrectionReason" />
		</div>
	</fieldset>

	<fieldset id="bpayInvestigationPayemntDetails">
		<legend>Payment Details</legend>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.clientRefNumber">Your Reference Number</form:label>
			<form:input id="clientRefNumber" path="attributesInformation.clientRefNumber" />

			<a href="javascript:;" title="Optional. Here you can enter either your customers membership / account number or an internal reference number that you have assigned to the dispute"><strong>?</strong></a>

			<form:errors class="error" path="attributesInformation.clientRefNumber" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.transactionRefNumber">Transaction Reference Number<em class="mandatory">*</em></form:label>
			<form:input id="transactionRefNumber" path="attributesInformation.transactionRefNumber" />

			<a href="javascript:;" title="This is the receipt number provided to the member / customer as confirmation of payment"><strong>?</strong></a>

			<form:errors class="error" path="attributesInformation.transactionRefNumber" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.transactionDate">Transaction Date<em class="mandatory">*</em></form:label>
			<form:input id="transactionDate" path="attributesInformation.transactionDate" />
			<!-- <a href="javascript:;" title="This is the processing date of the transaction"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.transactionDate" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.billerName">Biller Name</form:label>
			<form:input id="billerName" path="attributesInformation.billerName" />

			<form:errors class="error" path="attributesInformation.billerName" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.billerCode">Biller Code<em class="mandatory">*</em></form:label>
			<form:input id="billerCode" path="attributesInformation.billerCode" />

			<form:errors class="error" path="attributesInformation.billerCode" />
		</div>

		<div class="bpay-input-wrapper" id="bpayCorrectBillerCode">
			<form:label class="medium" path="attributesInformation.correctBillerCode">Correct Biller Code<em class="mandatory">*</em></form:label>
			<form:input id="correctBillerCode" path="attributesInformation.correctBillerCode" />
			<!-- <a href="javascript:;" title="This should be the correct Biller Code of the person disputing the transaction"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.correctBillerCode" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.customerRefNumber">Customer Reference Number<em class="mandatory">*</em></form:label>
			<form:input id="customerRefNumber" path="attributesInformation.customerRefNumber" />
			<!-- <a href="javascript:;" title="This should be the CRN of the person disputing the transaction"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.customerRefNumber" />
		</div>

		<div class="bpay-input-wrapper" id="bpayCorrectCRN">
			<form:label class="medium" path="attributesInformation.correctCRN">Correct CRN<em class="mandatory">*</em></form:label>
			<form:input id="correctCRN" path="attributesInformation.correctCRN" />
			<!-- <a href="javascript:;" title="This is the correct CRN that the transaction should have been made too"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.correctCRN" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.paymentAmount">Amount<em class="mandatory">*</em></form:label>
			<form:input id="paymentAmount" path="attributesInformation.paymentAmount" />

			<form:errors class="error" path="attributesInformation.paymentAmount" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.paymentMethod">Payment Method<em class="mandatory">*</em></form:label>
			<form:select id="paymentMethod" path="attributesInformation.paymentMethod">
				<option value="">Please select</option>

				<form:options items="${bPayListBoxData.paymentMethodMap}" />
			</form:select>

			<a href="javascript:;" title="This is the payment method used when making the transaction 001-Debit. 101-Visa. 201-Mastercard. 301-Other credit card"><strong>?</strong></a>

			<form:errors class="error" path="attributesInformation.paymentMethod" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.entryMethod">Entry Method<em class="mandatory">*</em></form:label>
			<form:select id="entryMethod" path="attributesInformation.entryMethod">
				<option value="">Please select</option>

				<form:options items="${bPayListBoxData.entryMethodMap}" />
			</form:select>
			<!-- <a href="javascript:;" title="This should be the method used to conduct the transaction"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.entryMethod" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.payerName">Payer Name<em class="mandatory">*</em></form:label>
			<form:input id="payerName" path="attributesInformation.payerName" />
			<!-- <a href="javascript:;" title="This should be the actual payer's name"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.payerName" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.memberName">Account Name</form:label>
			<form:input id="memberName" path="attributesInformation.memberName" />
			<!-- <a href="javascript:;" title="This is the name of the person who operates the membership"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.memberName" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.memberNumber">Account Number<em class="mandatory">*</em></form:label>
			<form:input id="memberNumber" path="attributesInformation.memberNumber" />
			<!-- <a href="javascript:;" title="This is the member number of the primary person who operates the membership"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.memberNumber" />
		</div>

		<div class="bpay-input-wrapper">
			<form:label class="medium" path="attributesInformation.payerReportedDate">Payer Reported Date<em class="mandatory">*</em></form:label>
			<form:input id="payerReportedDate" path="attributesInformation.payerReportedDate" />
			<!-- <a href="javascript:;" title="This is the payer reported date"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.payerReportedDate" />
		</div>

		<div class="bpay-input-wrapper" id="bpayFraudType">
			<form:label class="medium" path="attributesInformation.fraudType">Fraud Type<em class="mandatory">*</em></form:label>
			<form:select id="fraudType" path="attributesInformation.fraudType">
				<option value="">Please select</option>

				<form:options items="${bPayListBoxData.fraudTypeMap}" />
			</form:select>
			<!-- Display hidden div for showing title -->
			<div class="hidden-message-fraud" id="fraudTypeDescription"></div>
			<!-- <a href="javascript:;" title="This is fraudType"><strong>?</strong></a> -->
			<form:errors class="error" path="attributesInformation.fraudType" />
		</div>

		<!-- Text box for additional information on Fraud Type -->
		<div class="bpay-input-wrapper" id="fraudInfoBlock">
			<form:label class="medium" path="attributesInformation.fraudInfo">Other(please specify)<em class="mandatory">*</em></form:label>
			<form:textarea id="fraudInfo" maxlength="256" path="attributesInformation.fraudInfo" />

			<form:errors class="error" path="attributesInformation.fraudInfo" />
		</div>

		<div class="bpay-input-wrapper" id="bpayScamType">
			<form:label class="medium" path="attributesInformation.scamType">Scam Type<em class="mandatory">*</em></form:label>
			<form:select id="scamType" path="attributesInformation.scamType">
				<option value="">Please select</option>

				<form:options items="${bPayListBoxData.scamTypeMap}" />
			</form:select>
			<!-- Display hidden div for showing title -->
			<!--div class="hidden-message-scam" id="scamTypeDescription"></div-->
			<!-- Div for displaying descriptions based on the selected option -->
			<div class="scam-type-description" id="scamTypeDescription">
			<div class="description" data-option="1">Victim is tricked by scammers into believing they are being given trusted advice to make investments in certain commodities such as cryptocurrency, gold bullion, etc. The funds they send to the merchant are either received directly by the scammer who has set up a fake account, or sent on to another account by instruction of the scammer. They may also be instructed to actually make a purchase pf the commodity and send that on to another recipient.</div>

			<div class="description" data-option="2">Victim believes they have met a romantic partner, often online via website, apps or social media. Using emotional triggers, the fraudsters convince the victim to send money, gifts or personal details</div>

			<div class="description" data-option="3">Victim is convinced by scammers that they have a problem with their computer or internet which the scammer can fix, if remote access to the computer is provided. The scammers may pretend to be from a software provider, telecommunications company or even law enforcement. Once remote access is provided, the scammer can download malware or access personal details</div>

			<div class="description" data-option="4">Scammers set up an email which looks very similar to that of a third party a company does business with.They start impersonating the third party, eventually providing new BSB and Account Number details for invoices to be paid to. The new details are for an account held by the scammers.An alternative scenario is where the scammers set up an email pretending to be that of a senior executive of a company.- Using social media, the scammers identify when the executive is travelling, for example,then email staff at the company pretending to be the executive, giving instructions to make large payments overseas</div>

			<div class="description" data-option="5">Victim is contacted by the scammers by email, SMS, social media or telephone. They may pretend to be from Law enforcement, the ATO. Centrelink or similar demanding that a bill or fine must be paid. They will use threats to frighten the victim into making the payment, often targeting more vulnerable people such as the elderly or newly arrived immigrants</div>

			<div class="description" data-option="6">Victim is contacted (email, letter, SMS, social media, etc) by a scammer advising they are entitled to a large sum of money from overseas. After establishing trust and convincing the victim it is true, the scammer will start to request funds for fees, charges or various reasons to help release the funds. The value of the payments may increase until the victim realises what's happened.</div>

			<div class="description" data-option="7">Victim applies for a job promising easy money for a job working from home, the victim first having to send money to pay for a 'starter kit' or similar. Nothing is ever received and no contact can be made with the scammers.</div>

			<div class="description" data-option="8">Please use this Scam Type for cases which are not covered by any of the other options. include details about the other Scam Type in the below Other Text box.</div>
		<!-- Add divs for other options as needed -->
	</div>
			<form:errors class="error" path="attributesInformation.scamType" />
		</div>

		<!-- Text box for additional information on Scam Type -->
		<div class="bpay-input-wrapper" id="scamInfoBlock">
			<form:label class="medium" path="attributesInformation.scamInfo">Other(please specify)<em class="mandatory">*</em></form:label>
			<form:textarea id="scamInfo" maxlength="256" path="attributesInformation.scamInfo" />

			<form:errors class="error" path="attributesInformation.scamInfo" />
		</div>

		<script>
		// Assign the JSON string to a JavaScript variable
		var fraudTypeDescriptionMapJson = <%= fraudTypeDescriptions %>;

		function populateDynamicDescription() {
			var selectedOptionValue = document.getElementById("fraudType").value;
			var fraudTypeDescriptionDiv = document.getElementById("fraudTypeDescription");
			var selectedScamOptionValue = document.getElementById("scamType").value;

			if (selectedScamOptionValue) {
			// Set the marginBottom dynamically based on the selected value
				var bpayScamTypeDiv = document.getElementById("bpayScamType");
				var dynamicMarginValue;

				switch (selectedScamOptionValue) {
					case "1":
						dynamicMarginValue = "148px";
						break;
					case "2":
						dynamicMarginValue = "94px";
						break;
					case "3":
						dynamicMarginValue = "130px";
						break;
					case "4":
						dynamicMarginValue = "202px";
						break;
					case "5":
						dynamicMarginValue = "130px";
						break;
					case "6":
						dynamicMarginValue = "130px";
						break;
					case "7":
						dynamicMarginValue = "94px";
						break;
					case "8":
						dynamicMarginValue = "77px";
						break;

					default:
						dynamicMarginValue = "6px";
				}

				// Apply the dynamic margin to the element
				bpayScamTypeDiv.style.marginBottom = dynamicMarginValue;

			}

			if (selectedOptionValue === '3') {
				var selectedOptionTitle = fraudTypeDescriptionMapJson[selectedOptionValue];
				fraudTypeDescriptionDiv.textContent = selectedOptionTitle;
				fraudTypeDescriptionDiv.style.display = 'block';
			}
			else {
				fraudTypeDescriptionDiv.style.display = 'none';
			}
		}

		//Dynamically populates description for selected option at edit ticket scenario
		populateDynamicDescription();

	</script>

	</fieldset>

	<fieldset>
		<legend>Additional Information<a href="javascript:;" title="If fraud, please explain further and proceed to phone Call Direct to advise of fraud"><strong>?</strong></a></legend>

		<div>
			<em style="color: red;">If fraud, please explain further and proceed to phone Call Direct to advise of fraud.</em>
			<em>Please do not enter PAN details into the comments section.</em>

			<form:textarea htmlEscape="false" path="attributesInformation.comment" />

			<div>
				<form:errors cssClass="error" path="attributesInformation.comment" />
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
		<input class="request-buttons" id="submitButton" type="submit" value="Submit" />
		</c:when>
		<c:otherwise>
		<input class="request-buttons" id="submitButton" type="submit" value="Resubmit" />
		</c:otherwise>
	</c:choose>

	<div id="loading" style="display: none; z-index: 10000;">
		<div
			style="position: fixed; top: 0px; left: 0px; width: 100%; height: 100%; background-color: rgb(0, 0, 0); opacity: 0.3; z-index: 10000;">&nbsp;</div
		>
		<div
			style="position: fixed; height: 400px; width: 500px; top: 276.5px; left: 381.5px; background-color: rgb(255, 255, 255); text-align: center; border: 1px solid rgb(0, 0, 0); border-radius: 4px; z-index: 10001;"
		>
			<img
				src="<%= request.getContextPath() %>/images/ajax-loader.gif"
				style="position: relative; height: 32px; width: 32px; top: 184px; z-index: 10001;"
			>
			<div
				style="position: relative; top: 204px; font-weight: bold; text-align: center;">Please wait...</div
			>
		</div>
	</div>
</form:form>
</div>