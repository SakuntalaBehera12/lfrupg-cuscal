<%@ include file="init.jsp" %>

<c:choose>
	<c:when test="${updateTicket}">
		<portlet:actionURL escapeXml="false" var="submitChargebackUrl">
			<portlet:param name="action" value="UPDATE_CHARGEBACK" />
		</portlet:actionURL>
	</c:when>
	<c:otherwise>
		<portlet:actionURL escapeXml="false" var="submitChargebackUrl">
			<portlet:param name="action" value="SUBMIT_CHARGEBACK" />
		</portlet:actionURL>
	</c:otherwise>
</c:choose>

<form:form action="${submitChargebackUrl}" enctype="multipart/form-data" id="chargeback-form" method="post" modelAttribute="chargebackForm">
	<form:hidden path="ticketId" value="${ticketId}" />

	<form:errors cssClass="portlet-msg-error" id="errorMsg" path="errorMsg" />

	<div id="vc-form">
		<%@ include file="transaction-information-section.jsp" %>

		<%@ include file="dispute-information-section.jsp" %>

		<%-- <%@ include file="member-message-section.jsp" %> --%>

		<%@ include file="fraud-reporting-date.jsp" %>

		<%@ include file="retrieval-request-section.jsp" %>

		<%@ include file="credit-info-section.jsp" %>

		<jsp:include page="elaboration-info-section.jsp" />

		<%@ include file="certifications-section.jsp" %>

		<%@ include file="return-information-section.jsp" %>

		<portlet:renderURL escapeXml="false" var="renderCancelInfoHtmlUrl">
			<portlet:param name="render" value="cancellationInfo" />
		</portlet:renderURL>

		<span class="cancellation-info-section" data-id="${chargebackForm.ticketId}" data-url="${renderCancelInfoHtmlUrl}"></span>

		<div id="cancellation-section"></div>
		<%-- <%@ include file="cancellation-info-section.jsp" %> --%>

		<%@ include file="attempt-to-resolve.jsp" %>

		<%@ include file="questionnaire-information-section.jsp" %>

		<%@ include file="comments-attachments-section.jsp" %>

		<%-- Disclaimer area --%>
		<div id="disclaimer-wrap">
			<form:errors cssClass="error" path="disclaimer" /><br />

			<form:checkbox path="disclaimer" /><label for="disclaimer1">I agree Cuscal is not liable for any errors and/or delays due to incorrect/incomplete information being supplied.</label>

			<br />
		</div>

		<c:choose>
			<c:when test="${not updateTicket}">
				<input class="request-buttons" type="submit" value="Submit" />
			</c:when>
			<c:otherwise>
				<input class="request-buttons" type="submit" value="Resubmit" />
			</c:otherwise>
		</c:choose>
	</div>
</form:form>