<%@ include file="init.jsp" %>

<portlet:actionURL escapeXml="false" var="addTicketUrl">
	<portlet:param name="action" value="addTicket" />
</portlet:actionURL>

<div id="service-request-wrapper">
	<form:form action="${addTicketUrl}" method="post" modelAttribute="ticket">
		<div class="input-wrapper">
			<form:label path="user.firstName">First Name: </form:label>
			<form:input path="user.firstName" />
		</div>

		<div class="input-wrapper">
			<form:label path="user.lastName">Last Name: </form:label>
			<form:input path="user.lastName" />
		</div>

		<div class="input-wrapper">
			<form:label path="user.email">Email: </form:label>
			<form:input path="user.email" />
		</div>

		<div class="input-wrapper">
			<form:label path="user.phoneNumber">Phone Number: </form:label>
			<form:input path="user.phoneNumber" />

			<br />

			<form:errors class="error" path="user.phoneNumber" />
		</div>

		<div class="input-wrapper">
			<form:label path="user.organisation">Organisation: </form:label>
			<form:input path="user.organisation" />
		</div>

		<div class="input-wrapper">
			<form:label path="product">Product: </form:label>
			<form:input path="product" />

			<br />

			<form:errors class="error" path="product" />
		</div>

		<div class="input-wrapper">
			<form:label path="ticketCategory">Type: </form:label>
			<form:input path="ticketCategory" />

			<br />

			<form:errors class="error" path="ticketCategory" />
		</div>

		<div class="input-wrapper">
			<form:label path="ticketType">Request: </form:label>
			<form:input path="ticketType" />

			<br />

			<form:errors class="error" path="ticketType" />
		</div>

		<div class="input-wrapper">
			<form:label path="ticketDescription">Description: </form:label>
			<form:input path="ticketDescription" />

			<br />

			<form:errors class="error" path="ticketDescription" />
		</div>

		<label>&nbsp;</label><input type="submit" value="Add Ticket" />
	</form:form>
</div>