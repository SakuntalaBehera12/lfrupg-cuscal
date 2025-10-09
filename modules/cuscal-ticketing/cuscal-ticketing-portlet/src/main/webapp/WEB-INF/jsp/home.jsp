<%@ include file="init.jsp" %>
<div id="service-request-wrapper">
	<h1><c:out value="${heading}" /></h1>

	<p class="intro"><c:out value="${introText}" /></p>

	<%@ include file="newServiceRequest.jsp" %>

	<br />

	<%@ include file="actionPending.jsp" %>

	<br />

	<%@ include file="filter.jsp" %>
	<%-- <portlet:renderURL escapeXml="false" var="addTicketUrl">
		<portlet:param name="render" value="addTicketPage" />
	</portlet:renderURL>

	<a href="${addTicketUrl}">New Call</a> --%>

	<fieldset class="filter-fieldset">
	<legend>Requests</legend>

	<portlet:renderURL escapeXml="false" var="requestUrl" />

	<br />

	<display:table defaultorder="descending" defaultsort="1" id="ticket" name="tickets" requestURI="${requestUrl}" uid="ticket">
		<display:setProperty name="basic.msg.empty_list">
		<c:if test="${empty tickets}">
			<span class="portlet-msg-info">No calls found. Please update the Call filter and try again.</span>
		</c:if>
		</display:setProperty>

		<display:setProperty name="factory.requestHelper" value="org.displaytag.portlet.PortletRequestHelperFactory" />

		<display:setProperty name="paging.banner.page.separator">
		</display:setProperty>

		<display:setProperty name="basic.show.header">true</display:setProperty>

		<display:setProperty name="paging.banner.placement">both</display:setProperty>

		<display:setProperty name="paging.banner.item_name">result</display:setProperty>

		<display:setProperty name="paging.banner.items_name">results</display:setProperty>

		<display:setProperty name="paging.banner.group_size" value="100" />

		<display:setProperty name="paging.banner.all_items_found">
			<div class="pagination"><span class="pagination-text">Displaying results 1-{0} of {0}</span></div>
		</display:setProperty>

		<display:setProperty name="paging.banner.some_items_found">
			<div class="pagination"><span class="pagination-text">Displaying results {2}-{3} of {0}</span></div>
		</display:setProperty>

		<display:setProperty name="paging.banner.first">
			<!-- <span class="pagination-links"> {0} <a href="{3}">&gt;</a> </span> -->
		</display:setProperty>

		<display:setProperty name="paging.banner.last">
			<!-- <span class="pagination-links"> <a href="{2}">&lt;</a> {0} </span> -->
		</display:setProperty>

		<display:setProperty name="paging.banner.onepage">
			<!-- <span class="pagination-links"> {0} </span> -->
		</display:setProperty>

		<display:setProperty name="paging.banner.full">
			<!-- <span class="pagination-links"> <a href="{2}">&lt;</a> {0} <a href="{3}">&gt;</a> </span> -->
		</display:setProperty>

		<display:setProperty name="css.th.ascending" value="ascending" />
		<display:setProperty name="css.th.descending" value="descending" />

		<display:column class="first">
			<c:choose>
				<c:when test="${ticket.isUnread}"><div class="unread">&nbsp;</div></c:when>
				<c:otherwise>&nbsp;</c:otherwise>
			</c:choose>
		</display:column>

		<display:column sortable="true" title="Request No.">
			<span class="display-hidden"><c:out value="${ticket.ticketNumber}" /></span>

			<portlet:renderURL escapeXml="false" var="ticketDetailsUrl">
				<portlet:param name="render" value="ticketDetails" />
				<portlet:param name="i" value="${ticket.cuscalTicketNumber}" />
			</portlet:renderURL>

			<div class="new-ticket">
				<c:choose>
				<c:when test="${ticket.isNewTicket}">*</c:when>
				<c:otherwise>&nbsp;</c:otherwise>
				</c:choose>
			</div>

			<a href="${ticketDetailsUrl}"><c:out value="${ticket.ticketNumber}" /></a>
		</display:column>

		<display:column sortable="true" title="Request Type">
			<c:out value="${ticket.ticketType}" />
		</display:column>

		<display:column class="product" sortable="true" title="Service Type">
			<c:out value="${ticket.requestType}" />
		</display:column>

		<display:column sortable="true" title="Submitted By">
			<%-- <c:out value="${ticket.submittedByFirstName} ${ticket.submittedByLastName}" /><br />

			<c:out value="${ticket.organisation}" /> --%>
			<c:out value="${ticket.ticketSubmittedBy}" />
		</display:column>

		<display:column format="{0,date,dd/MM/yy HH:mm}" property="submittedDate" sortable="true" title="Submitted">
			<%-- <fmt:formatDate pattern="dd/MM/yy HH:mm" value="${ticket.submittedDate}" /> --%>
		</display:column>

		<display:column format="{0,date,dd/MM/yy HH:mm}" property="updatedDate" sortable="true" title="Updated">
			<%-- <c:if test="${not empty ticket.updatedDate}">
				<fmt:formatDate pattern="dd/MM/yy HH:mm" value="${ticket.updatedDate}" />
			</c:if> --%>
		</display:column>

		<display:column class="last" sortable="true" title="Status">
			<c:out value="${ticket.ticketStatus}" />
		</display:column>
	</display:table>

	<br />

	<p><span class="unread">&nbsp;</span> = Updated but not viewed by your organisation</p>
	</fieldset>
</div> <%-- Start of this tag is in init.jsp --%>