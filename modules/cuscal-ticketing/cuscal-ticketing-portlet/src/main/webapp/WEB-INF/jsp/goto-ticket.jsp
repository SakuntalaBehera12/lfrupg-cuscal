<%@ include file="init.jsp" %>

<portlet:renderURL escapeXml="false" var="gotoTicketUrl">
	<portlet:param name="render" value="loadTicket" />
</portlet:renderURL>

<form:form action="${gotoTicketUrl}" class="calls-form" id="ticketDetailsForm" method="post" modelAttribute="ticketFilter">
	<div>
		<label for="call-number">Request No.</label>

		<form:input id="call-number" name="ticketNumber" path="ticketNumber" />

		<input class="request-buttons" type="submit" value="View Request" />

		<br />

		<form:errors class="error" path="ticketNumber" />
	</div>
</form:form>