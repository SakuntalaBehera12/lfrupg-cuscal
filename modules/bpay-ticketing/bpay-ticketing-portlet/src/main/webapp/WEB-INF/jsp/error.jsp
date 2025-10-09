<%@ include file="init.jsp" %>

<c:choose>
	<c:when test="${sessionTimeout}">
		<div class="portlet-msg-error">
			<span><spring:message code="bpay.error.user.timedout" /></span>
		</div>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-error">
			<span><spring:message code="bpay.error.generic.error" /></span>
		</div>
	</c:otherwise>
</c:choose>