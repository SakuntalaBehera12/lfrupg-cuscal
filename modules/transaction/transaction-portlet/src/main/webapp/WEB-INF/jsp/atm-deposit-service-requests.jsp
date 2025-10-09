<%@ include file="init.jsp" %>

<portlet:resourceURL escapeXml="false" id="msgList" var="msgUrl" />
<portlet:resourceURL escapeXml="false" id="serviceRequest" var="submitServiceRequestUrl" />

<portlet:actionURL escapeXml="false" var="attachFile1Url">
	<portlet:param name="action" value="SERVICE_REQUEST_WITH_ATTACHMENTS" />
</portlet:actionURL>

<form:form action="${attachFile1Url}" enctype="multipart/form-data" id="service-request-form-atmpos" method="post" modelAttribute="serviceRequestForm">
	<div id="member-and-contact-info">
		<%@ include file="member-and-contact-info.jsp" %>
	</div>

	<%-- Hidden fields to hold the validation information to prevent submit --%>
	<form:hidden class="outstandingDispute" path="atmPosClaimInformation.outstandingDispute" />
	<form:hidden class="outstandingClosedDispute" path="atmPosClaimInformation.outstandingClosedDispute" />
	<form:hidden class="transactionCode" path="atmPosClaimInformation.transactionCode" />
	<form:hidden class="outstandingReinvestigation" path="atmPosClaimInformation.outstandingReinvestigation" />

	<fieldset class="claim-details">
		<legend>Service Request</legend>

		<label class="large">Dispute Type<em>*</em></label>

		<form:select id="atmpos-dispute-type" name="disputeType" path="atmPosClaimInformation.requestDisputeType">
			<form:option value="0">Please select</form:option>
			<form:option value="29">Dispute</form:option>
			<form:option value="30">Reinvestigation</form:option>
		</form:select>

		<form:errors cssClass="atm-request-dispute-type-error error" path="atmPosClaimInformation.requestDisputeType" />

		<br />

		<html:ajaxHidden fieldClass="cardholder-amount" label="Cardholder Amount" labelClass="large" name="atmPosClaimInformation.requestCardholderAmount" />

		<div class="normal">
			<label class="large">Amount Deposited<em>*</em></label>

			<form:input class="amount-deposited" id="amountDeposited" maxlength="13" path="atmPosClaimInformation.requestAmountReceived" />

			<a href="javascript:;" title="Please enter a value less than or equal to 0."><b>?</b></a>

			<br />

			<form:errors cssClass="atm-deposit-error atm-request-amount-received-error error" path="atmPosClaimInformation.requestAmountReceived" />
		</div>

		<div id="atm-fee-div">
			<html:ajaxHidden fieldClass="atm-fee" label="ATM Fee" labelClass="atm-fee-label" name="atmPosClaimInformation.requestAtmFee" />
		</div>

		<br />

		<html:ajaxHidden fieldClass="claim-amount" label="Claim Amount" labelClass="large" name="atmPosClaimInformation.requestClaimAmount" />

		<label class="large">Reason<em>*</em></label>

		<form:select class="reason-list" id="atmDepositDisputeReasonsForm" name="atmReason" path="atmPosClaimInformation.requestAtmReason">
			<form:option value="0">Please select</form:option>
		</form:select>

		<form:errors cssClass="atm-request-atm-reason-error error" path="atmPosClaimInformation.requestAtmReason" />

		<br />

		<div class="comments">
			<label>Comments</label>

			<span>Please do not enter PAN details into the comments section.</span>

			<br />

			<form:textarea htmlEscape="false" maxlength="4000" path="atmPosClaimInformation.requestComments" />

			<div>
				<form:errors cssClass="atm-deposit-error atm-request-comments-error error" path="atmPosClaimInformation.requestComments" />
			</div>
		</div>
	</fieldset>

	<fieldset>
		<legend>Supporting Documentation</legend>

		<label class="large">Attachment/s</label>

		<table id="fileTable">
			<tr>
				<td><input name="atmPosClaimInformation.requestAttachments[0]" type="file" /></td>
				<td>&nbsp;<form:errors cssClass="atm-request-attachments-error error" path="atmPosClaimInformation.requestAttachments" /></td>
			</tr>
		</table>

		<br />

		<label class="large">&nbsp;</label><input id="addFile" type="button" value="Add More Files" onclick="javascript:addMoreFiles();" />
	</fieldset>

	<form:errors cssClass="portlet-msg-error" id="errorMsg" path="atmPosClaimInformation.errorMsg" />

	<br />

	<div id="atm-disclaimer-wrap">
		<form:checkbox path="disclaimer" /> I agree Cuscal is not liable for any errors and/or delays due to incorrect/incomplete information being supplied.
		<br />

		<form:errors cssClass="atm-disclaimer-error error" path="disclaimer" />
	</div>

	<br />

	<input id="createTicket" name="create" type="submit" value="Submit Service Request" />
</form:form>