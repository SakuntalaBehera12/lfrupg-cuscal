<%@ include file="init.jsp" %>

<%
session.removeAttribute("outstandingMasterCardDisputeRetrievalRequest");
session.removeAttribute("outstandingMasterCardDisputeFirstChargeback");
session.removeAttribute("outstandingMasterCardDisputeArbitrationChargeback");
session.removeAttribute("outstandingMasterCardDisputeReportFraud");
%>

<portlet:resourceURL escapeXml="false" id="serviceRequest" var="submitServiceRequestUrl" />

<form:form action="${submitServiceRequestUrl}" id="service-request-form-mastercard" method="post" modelAttribute="serviceRequestForm">
	<form:hidden class="outstandingMasterCardDisputeRetrievalRequest" path="visaTransactionInformation.outstandingMasterCardDisputeRetrievalRequest" />
	<form:hidden class="outstandingMasterCardDisputeFirstChargeback" path="visaTransactionInformation.outstandingMasterCardDisputeFirstChargeback" />
	<form:hidden class="outstandingMasterCardDisputeArbitrationChargeback" path="visaTransactionInformation.outstandingMasterCardDisputeArbitrationChargeback" />
	<form:hidden class="outstandingMasterCardDisputeReportFraud" path="visaTransactionInformation.outstandingMasterCardDisputeReportFraud" />

	<div id="masterccard-member-and-contact-info">
		<%@ include file="member-and-contact-info.jsp" %>
	</div>

	<fieldset>
		<legend>Service Request</legend>
		<!-- <span id="service-request-form-mastercard-message">Please click on 'MasterCard Dispute' button to raise a service request for first chargeback or arbitration chargeback or retrieval request</span>
		-->
		<label class="large">Service Type<em>*</em></label>

		<form:select id="mastercard-request-type" name="mastercard-requestType" path="visaTransactionInformation.requestType">
			<option value="0">Please select</option>
		</form:select>

		<span class="error" id="mastercard-requesttype-error"></span>

		<br />

		<div id="mastercard-dispute-form"></div>
	</fieldset>
</form:form>