<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<c:choose>
	<c:when test="${errorMsg}">
		<p class="error">
			<spring:message code="cards.error.msg" />
		</p>
	</c:when>
	<c:otherwise>
		<p class="error">
			<c:out value="${errorMsg}" />
		</p>
	</c:otherwise>
</c:choose>