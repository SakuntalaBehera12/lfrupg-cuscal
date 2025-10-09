<%@ include file="init.jsp" %>

<portlet:resourceURL escapeXml="false" id="serviceRequest" var="submitServiceRequestUrl" />

<form:form action="${submitServiceRequestUrl}" id="service-request-form-visa" method="post" modelAttribute="serviceRequestForm">
	<div id="member-and-contact-info">
		<%@ include file="member-and-contact-info.jsp" %>
	</div>

	<%-- Hidden fields to hold the validation information to prevent submit --%>
	<form:hidden class="outstandingTC40" path="visaTransactionInformation.outstandingTC40" />
	<form:hidden class="outstandingTC52" path="visaTransactionInformation.outstandingTC52" />

	<form:hidden class="reversal" path="visaTransactionInformation.reversal" />
	<form:hidden class="visaAtm" path="visaTransactionInformation.visaAtm" />

	<fieldset>
		<legend>Service Request</legend>

		<label class="large">Service Type<em>*</em></label>

		<form:select id="visa-request-type" name="requestType" path="visaTransactionInformation.requestType">
			<option value="0">Please select</option>
		</form:select>

		<span class="error" id="requesttype-error"></span>

		<br />

		<div id="tc40-form">
			<label class="large">Fraud Type<em>*</em></label>

			<form:select id="fraudType" path="visaTransactionInformation.fraudType">
				<option value="0">Please select</option>
			</form:select>

			<span class="error" id="fraudtype-error"></span>

			<br />

			<label class="large">Notification Code<em>*</em></label>

			<form:select id="fraudNotifCode" path="visaTransactionInformation.fraudNotificationCode">
			</form:select>

			<span class="error" id="notificationcode-error"></span>

			<br />

			<label class="large">Detected By<em>*</em></label>

			<form:select id="detection" path="visaTransactionInformation.detection">
				<option value="0">Please select</option>
			</form:select>

			<span class="error" id="detection-error"></span>

			<br />
			<%-- <html:inputField errorClass="error" label="Comments" labelClass="large" name="visaTransactionInformation.comments" requiredField="no" /> --%>
			<div class="comments">
				<label class="large">Comments</label>

				<span>Please do not enter PAN details into the comments section.</span>

				<br />

				<form:textarea htmlEscape="false" path="visaTransactionInformation.comments" />
				<%-- <form:input path="visaTransactionInformation.comments" /> --%>
				<span class="error" id="visa-comments"></span>
			</div>
		</div>

		<div id="tc52-form">
			<label class="large">Reason<em>*</em></label>

			<form:select id="voucherReason" path="visaTransactionInformation.voucherReason">
			</form:select>

			<span class="error" id="voucherreason-error"></span>
		</div>

		<div id="vc-form">
			<!-- <div id="open-sr-portlet"></div> -->
		</div>
	</fieldset>

	<br />

	<div id="disclaimer-wrap">
		<form:checkbox id="disclaimer" path="disclaimer" /> I agree Cuscal is not liable for any errors and/or delays due to incorrect/incomplete information being supplied.
		<br />

		<span class="error" id="disclaimer-error"></span>
	</div>

	<br />

	<input id="submit-visa" type="submit" value="Submit Service Request" />
</form:form>