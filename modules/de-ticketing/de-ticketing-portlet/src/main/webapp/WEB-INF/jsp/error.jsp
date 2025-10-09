<%@ include file="init.jsp" %>

<c:choose>
	<c:when test="${sessionTimeout}">
		<div class="portlet-msg-error">
			<span><spring:message code="de.error.user.timedout" /></span>
		</div>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-error">
			<span><spring:message code="de.error.generic.error" /></span>
		</div>
	</c:otherwise>
</c:choose>