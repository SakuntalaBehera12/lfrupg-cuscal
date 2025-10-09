<%@ include file="init.jsp" %>

<div class="portlet-msg-info" id="success-message">
	<span><spring:message code="chargeback.request.success.submission" /></span>
</div>

<%-- <div id="ticket-table">
	<display:table id="ticket" name="tickets" uid="ticket">
		<display:column title="Request No.">
			<a href=<c:out value="${ticket.ticketLink}" />><c:out value="${ticket.ticketNumber}" /></a>
		</display:column>

		<display:column title="Service Type">
			<c:out value="${ticket.ticketCategory}" />
		</display:column>

		<display:column title="Submitted By">
			<c:out value="${ticket.ticketFirstName}" /> <c:out value="${ticket.ticketLastName}" />
		</display:column>

		<display:column title="Submitted">
			<c:out value="${ticket.ticketUpdateDate}" />
		</display:column>

		<display:column title="Updated">
			<c:out value="${ticket.ticketSubmittedDate}" />
		</display:column>

		<display:column title="Status">
			<c:out value="${ticket.ticketStatus}" />
		</display:column>
	</display:table>
</div> --%>