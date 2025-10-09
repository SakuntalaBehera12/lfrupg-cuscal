<%@ include file="init.jsp" %>
<portlet:renderURL escapeXml="false" var="searchFilterUrl">
	<portlet:param name="render" value="ticketFilterRender" />
</portlet:renderURL>

<%-- <portlet:actionURL escapeXml="false" var="newTicketUrl">
	<portlet:param name="action" value="addTicket" />
</portlet:actionURL> --%>

<portlet:renderURL var="resetFilterUrl" />

<portlet:renderURL escapeXml="false" var="gotoTicketUrl">
	<portlet:param name="render" value="loadTicket" />
</portlet:renderURL>

<%-- <%@ include file="goto-ticket.jsp" %> --%>
<fieldset class="filter-fieldset">
	<legend>Request Filter</legend>

	<form:form action="${gotoTicketUrl}" class="calls-form" id="ticketDetailsForm" method="post" modelAttribute="ticketFilter">
			<label for="call-number">Request No.</label>

			<form:input id="call-number" name="ticketNumber" path="ticketNumber" />

			<input class="request-buttons" type="submit" value="View Request" />

			<br />

			<form:errors class="error" path="ticketNumber" />
	</form:form>

	<form:form action="${searchFilterUrl}" class="search-filter" method="post" modelAttribute="ticketFilter">
		<%-- <div>
			<label>Request Type</label>

			<form:select id="ticket-type" path="ticketCategory">
				<form:option value="all">All</form:option>
				<form:options items="${ticketCategoryMap}" />
			</form:select>
		</div> --%>
		<div id="serviceType">
			<label>Service Type</label>

			<form:select id="product-filter" path="product">
				<form:option value="all">All</form:option>
				<form:options items="${productMap}" />
			</form:select>
		</div>

		<div>
			<label>Status</label>

			<form:select path="ticketStatus">
				<form:option value="all">All</form:option>
				<form:options items="${ticketStatusMap}" />
			</form:select>
		</div>

		<div>
			<label>Submitted By</label>

			<form:select path="submittedBy">
				<form:option value="0"><c:out value="${submittedByUser}" /></form:option>
				<form:options items="${submittedByMap}"></form:options>
			</form:select>

			<br />

			<form:errors class="error" path="submittedBy" />
		</div>

		<div>
			<label>Submitted Within</label>

			<form:select path="submittedWithin">
				<form:options items="${lastModifiedMap}" />
			</form:select>
		</div>

		<div>
			<label>Last Modified</label>

			<form:select path="updatedWithin">
				<form:options items="${lastModifiedMap}" />
			</form:select>
		</div>

		<br />

		<input class="request-buttons" type="submit" value="Filter" />
		<input class="request-buttons" onclick="javascript:gotoUrl('${resetFilterUrl}');" type="button" value="Reset" />

		<%-- <a href="${newTicketUrl}">New Call</a> --%>
	</form:form>
</fieldset>