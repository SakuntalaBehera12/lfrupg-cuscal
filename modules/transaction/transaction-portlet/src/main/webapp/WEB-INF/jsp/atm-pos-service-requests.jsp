<%@ include file="init.jsp" %>

<portlet:resourceURL escapeXml="false" id="serviceRequest" var="submitServiceRequestUrl" />

<portlet:actionURL escapeXml="false" var="attachFile1Url">
	<portlet:param name="action" value="SERVICE_REQUEST_WITH_ATTACHMENTS" />
</portlet:actionURL>

<form:form action="${attachFile1Url}" data-senna-off="true" enctype="multipart/form-data" id="service-request-form-atmpos" method="post" modelAttribute="serviceRequestForm">
	<div id="member-and-contact-info">
		<%@ include file="member-and-contact-info.jsp" %>
	</div>

	<%-- Hidden fields to hold the validation information to prevent submit --%>
	<form:hidden class="outstandingDispute" path="atmPosClaimInformation.outstandingDispute" />
	<form:hidden class="outstandingClosedDispute" path="atmPosClaimInformation.outstandingClosedDispute" />
	<form:hidden class="transactionCode" path="atmPosClaimInformation.transactionCode" />

	<fieldset class="claim-details">
		<legend>Service Request</legend>

		<label class="large">Dispute Type<em>*</em></label>

		<form:select id="atmpos-dispute-type" name="disputeType" path="atmPosClaimInformation.requestDisputeType">
			<form:option value="0">Please select</form:option>
			<form:option value="29">Dispute</form:option>
			<form:option value="30">Reinvestigation</form:option>
		</form:select>

		<form:errors cssClass="error" path="atmPosClaimInformation.requestDisputeType" />

		<br />

		<html:ajaxHidden fieldClass="transaction-amount" label="Transaction Amount" labelClass="large" name="atmPosClaimInformation.requestTransactionAmount" />

		<div class="normal">
			<label class="amount-received-label large">
				<span>Amount Expected<em>*</em></span>
			</label>

			<form:input class="amount-received" path="atmPosClaimInformation.requestAmountReceived" />

			<form:errors cssClass="error" path="atmPosClaimInformation.requestAmountReceived" />
		</div>

		<div id="atm-fee-div">
			<html:ajaxHidden fieldClass="atm-fee" label="ATM Fee" labelClass="atm-fee-label" name="atmPosClaimInformation.requestAtmFee" />
		</div>

		<br />

		<html:ajaxHidden fieldClass="claim-amount" label="Claim Amount" labelClass="large" name="atmPosClaimInformation.requestClaimAmount" />

		<label class="large">Reason<em>*</em></label>

		<form:select class="reason-list" id="atmForm" name="atmReason" path="atmPosClaimInformation.requestAtmReason">
			<form:option value="0">Please select</form:option>
		</form:select>

		<form:errors cssClass="error" path="atmPosClaimInformation.requestAtmReason" />

		<form:select class="reason-list" id="posForm" name="posReason" path="atmPosClaimInformation.requestPosReason">
			<form:option value="0">Please select</form:option>
		</form:select>

		<br />

		<div class="comments">
			<label>Comments</label>

			<span>Please do not enter PAN details into the comments section.</span>

			<br />

			<form:textarea htmlEscape="false" path="atmPosClaimInformation.requestComments" />

			<div>
				<form:errors cssClass="error" path="atmPosClaimInformation.requestComments" />
			</div>
		</div>
	</fieldset>

	<fieldset>
		<legend>Supporting Documentation</legend>

		<label class="large">Attachment/s<span id="file-mandatory"><em>*</em></span></label>

		<table id="fileTable">
			<tr>
				<td><input name="atmPosClaimInformation.requestAttachments[0]" type="file" /></td>
				<td>&nbsp;<form:errors cssClass="error" path="atmPosClaimInformation.requestAttachments" /></td>
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

		<form:errors cssClass="error" path="disclaimer" />
	</div>

	<br />

	<input id="createTicket" name="create" type="submit" value="Submit Service Request" />
</form:form>