<%@ page import="au.com.cuscal.bpay.ticketing.common.Constants" %>

<%@ page import="javax.portlet.PortletSession" %>

<%
String steppedUp = "";

if (portletSession.getAttribute(Constants._USER_STEPPEDUP, PortletSession.APPLICATION_SCOPE) != null) {
	steppedUp = (String)portletSession.getAttribute(Constants._USER_STEPPEDUP, PortletSession.APPLICATION_SCOPE);
}
%>

<fieldset>
	<legend>Contact Details</legend>

	<div class="contact-info">
		<label class="">Organisation</label>

		<span><c:out value="${serviceRequestForm.contactInformation.organisation}" /></span>

		<form:hidden id="organisation" path="contactInformation.organisation" />
	</div>

	<div class="contact-info">
		<label class="">Organisation BSB</label>

		<span><c:out value="${serviceRequestForm.contactInformation.bsb}" /></span>

		<form:hidden id="bsb" path="contactInformation.bsb" />
		<%-- <form:hidden id="bsb" path="attributesInformation.bsb" value="${serviceRequestForm.contactInformation.bsb}" /> --%>
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
	<%-- <div class="contact-info">
		<label class="">Phone Number</label>

		<span><c:out value="${serviceRequestForm.contactInformation.phoneNumber}" /></span>

		<form:hidden id="phoneNumber" path="contactInformation.phoneNumber" />
	</div> --%>
</fieldset>

<fieldset>
	<legend>Ticket Details</legend>

	<div class="bpay-input-wrapper">
		<form:label class="medium" path="attributesInformation.reason">Reason for Request</form:label>
		<span><c:out value="${serviceRequestForm.attributesInformation.reason}" /></span>

		<form:hidden path="attributesInformation.reason" />
	</div>

	<div class="bpay-input-wrapper">
		<form:label class="medium" path="attributesInformation.transactionRefNumber">Transaction Reference Number</form:label>
		<span><c:out value="${serviceRequestForm.attributesInformation.transactionRefNumber}" /></span>

		<form:hidden path="attributesInformation.transactionRefNumber" />
	</div>

	<div class="bpay-input-wrapper">
		<form:label class="medium" path="attributesInformation.transactionDate">Transaction Date</form:label>
		<span><c:out value="${serviceRequestForm.attributesInformation.transactionDate}" /></span>

		<form:hidden path="attributesInformation.transactionDate" />
	</div>

	<div class="bpay-input-wrapper">
		<form:label class="medium" path="attributesInformation.billerName">Biller Name</form:label>
		<span><c:out value="${serviceRequestForm.attributesInformation.billerName}" /></span>

		<form:hidden path="attributesInformation.billerName" />
	</div>

	<div class="bpay-input-wrapper">
		<form:label class="medium" path="attributesInformation.billerCode">Biller Code</form:label>
		<span><c:out value="${serviceRequestForm.attributesInformation.billerCode}" /></span>

		<form:hidden path="attributesInformation.billerCode" />
	</div>

	<div class="bpay-input-wrapper">
		<form:label class="medium" path="attributesInformation.paymentAmount">Payment Amount</form:label>
		<span><c:out value="${serviceRequestForm.attributesInformation.paymentAmount}" /></span>

		<form:hidden path="attributesInformation.paymentAmount" />
	</div>

	<c:choose>
		<c:when test="${not empty serviceRequestForm.attributesInformation.customerRefNumber}">
			<div class="bpay-input-wrapper">
				<form:label class="medium" path="attributesInformation.customerRefNumber">Customer Reference Number</form:label>
				<span><c:out value="XXXX" /></span>

				<form:hidden path="attributesInformation.customerRefNumber" />
			</div>
		</c:when>
	</c:choose>

	<c:choose>
		<c:when test="${not empty serviceRequestForm.attributesInformation.correctCRN}">
			<div class="bpay-input-wrapper">
				<form:label class="medium" path="attributesInformation.correctCRN">Correct CRN</form:label>
				<span><c:out value="XXXX" /></span>

				<form:hidden path="attributesInformation.correctCRN" />
			</div>
		</c:when>
	</c:choose>

	<c:choose>
		<c:when test='<%= "true".equals(steppedUp) %>'>
			<portlet:resourceURL escapeXml="false" id="retrieveMaskedFeilds" var="retrieveMaskedFields">
				<portlet:param name="ticketId" value="${serviceRequestForm.ticketId}" />
			</portlet:resourceURL>

			<a class="buttons masked-details" href="javascript:;" url="${retrieveMaskedFields}">View Masked Detail/s</a>
		</c:when>
		<c:otherwise>
			<portlet:actionURL escapeXml="false" var="stepUpUrl">
				<portlet:param name="action" value="stepUp" />
				<portlet:param name="ticketIdStepUp" value="${serviceRequestForm.ticketId}" />
			</portlet:actionURL>

			<a class="buttons" href="${stepUpUrl}">Activate Masked Detail/s</a>
		</c:otherwise>
	</c:choose>
</fieldset>

<div class="modal-wrapper">
	<ul class="masked-fields-output no-print"></ul>

	<a class="buttons modal-close no-print" href="javascript:void(0)">Ok</a>
</div>

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