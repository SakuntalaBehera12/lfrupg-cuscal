<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="fieldClass" required="false" rtexprvalue="false" %>
<%@ attribute name="labelClass" required="false" rtexprvalue="true" %>
<div class="normal">
	<label class="${labelClass}">${label}</label>

	<span class="${fieldClass}" id="${fieldClass}"></span>

	<form:hidden cssClass="${fieldClass}" path="${name}" />
</div>