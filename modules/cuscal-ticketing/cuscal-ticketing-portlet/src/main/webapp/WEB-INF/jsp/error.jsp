<%@ include file="init.jsp" %>

<div id="service-request-wrapper">
	<div class="portlet-msg-error">
		<c:choose>
			<c:when test="${userTimedOut}">
				<span><spring:message code="ticket.user.logout" /></span>
			</c:when>
			<c:when test="${invalidUserSetup}">
				<span><spring:message code="ticket.invalid.account" /></span>
			</c:when>
			<c:when test="${invalidResponse}">
				<span><spring:message code="ticket.generic.error" /></span>
			</c:when>
			<c:otherwise>
				<span><spring:message code="ticket.generic.error" /></span>
			</c:otherwise>
		</c:choose>
	</div>
</div>