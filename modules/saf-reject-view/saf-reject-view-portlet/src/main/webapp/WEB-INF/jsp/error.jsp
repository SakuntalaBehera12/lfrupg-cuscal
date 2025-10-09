<%@ include file="init.jsp" %>

<div class="portlet-msg-error">
	<c:choose>
		<c:when test="${errorType eq 'loggedOut'}">
			<spring:message code="saf.session.timeout.msg" />
		</c:when>
		<c:when test="${errorType eq 'invalidConfig'}">
			<spring:message code="saf.user.config.error.msg" />
		</c:when>
		<c:when test="${errorType eq 'systemException'}">
			<spring:message code="saf.system.error.msg" />
		</c:when>
	</c:choose>
</div>