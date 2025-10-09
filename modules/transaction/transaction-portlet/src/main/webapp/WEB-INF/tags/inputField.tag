<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="requiredField" required="true" rtexprvalue="false" %>
<%@ attribute name="labelClass" required="false" rtexprvalue="true" %>
<%@ attribute name="errorId" required="false" rtexprvalue="false" %>
<%@ attribute name="errorClass" required="false" rtexprvalue="false" %>
<%@ attribute name="fieldClass" required="false" rtexprvalue="false" %>
<div class="normal">
	<label class="${labelClass}">${label}<c:if test="${requiredField eq 'yes'}"><em>*</em></c:if></label>

	<form:input class="${fieldClass}" path="${name}" />

	<br />

	<span class="${errorClass}" id="${errorId}"></span>

	<form:errors class="${errorClass}" path="${name}" />
</div>