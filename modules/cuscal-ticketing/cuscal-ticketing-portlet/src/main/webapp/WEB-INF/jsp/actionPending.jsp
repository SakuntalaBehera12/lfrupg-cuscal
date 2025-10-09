<%@ include file="init.jsp" %>

<fieldset class="filter-fieldset">
	<legend>Client Action</legend>

	<p>Please action the following</p>

	<portlet:renderURL escapeXml="false" var="actionPendingUrl" />

	<br />

	<display:table defaultorder="descending" defaultsort="1" id="actionTicket" name="actionTickets" pagesize="6" requestURI="${actionPendingUrl}" sort="list" uid="actionTicket">
		<display:setProperty name="basic.msg.empty_list">
		<c:if test="${empty actionTickets}">
			<span class="portlet-msg-info">There are no requests to be actioned.</span>
		</c:if>
		</display:setProperty>

		<display:setProperty name="factory.requestHelper" value="org.displaytag.portlet.PortletRequestHelperFactory" />

		<display:setProperty name="paging.banner.page.separator">
		</display:setProperty>

		<display:setProperty name="basic.show.header">true</display:setProperty>

		<display:setProperty name="paging.banner.placement">both</display:setProperty>

		<display:setProperty name="paging.banner.item_name">result</display:setProperty>

		<display:setProperty name="paging.banner.items_name">results</display:setProperty>

		<display:setProperty name="paging.banner.group_size" value="10" />

		<display:setProperty name="paging.banner.all_items_found">
			<span class="pagination-text">Displaying results 1-{0} of {0}</span>
		</display:setProperty>

		<display:setProperty name="paging.banner.some_items_found">
			<span class="pagination-text">Displaying results {2}-{3} of {0}</span>
		</display:setProperty>

		<display:setProperty name="paging.banner.first">
			<span class="pagination-links"> {0} <a href="{3}">&gt;</a> </span>
		</display:setProperty>

		<display:setProperty name="paging.banner.last">
			<span class="pagination-links"> <a href="{2}">&lt;</a> {0} </span>
		</display:setProperty>

		<display:setProperty name="paging.banner.onepage">
			<span class="pagination-links"> {0} </span>
		</display:setProperty>

		<display:setProperty name="paging.banner.full">
			<span class="pagination-links"> <a href="{2}">&lt;</a> {0} <a href="{3}">&gt;</a> </span>
		</display:setProperty>

		<display:setProperty name="css.th.ascending" value="ascending" />
		<display:setProperty name="css.th.descending" value="descending" />

		<%-- <display:column class="first">
			<div>
				<c:choose>
				<c:when test="${actionTicket.isUnread}"><div class="unread">&nbsp;</div></c:when>
				<c:otherwise>&nbsp;</c:otherwise>
				</c:choose>
			</div>
		</display:column> --%>

		<display:column class="first" sortable="true" title="Request No.">
			<span class="display-hidden"><c:out value="${actionTicket.ticketNumber}" /></span>

			<portlet:actionURL escapeXml="false" var="updateScActionUrl">
					<portlet:param name="action" value="updateOfiScAction" />
					<portlet:param name="ticketId" value="${actionTicket.cuscalTicketNumber}" />
					<%-- <portlet:param name="scItemId" value="${actionTicket.scItemId}" /> --%>
					<%-- <portlet:param name="scItemId" value="1" /> --%>
					<portlet:param name="typeId" value="${actionTicket.ticket.type.typeId}" />
			</portlet:actionURL>
			<%-- <div class="new-ticket">
				<c:choose>
				<c:when test="${actionTicket.isNewTicket}">*</c:when>
				<c:otherwise>&nbsp;</c:otherwise>
				</c:choose>
			</div> --%>
			<a data-senna-off="true" href="${updateScActionUrl}"><c:out value="${actionTicket.ticketNumber}" /></a>
		</display:column>

		<display:column sortable="true" title="Request Type">
			<c:out value="${actionTicket.ticketType}" />
		</display:column>

		<display:column class="product" sortable="true" title="Service Type">
			<c:out value="${actionTicket.requestType}" />
		</display:column>

		<display:column sortable="true" title="Submitted By">
			<%-- <c:out value="${actionTicket.submittedByFirstName} ${actionTicket.submittedByLastName}" /><br />

			<c:out value="${actionTicket.organisation}" /> --%>
			<%-- <c:out value="${actionTicket.ticketSubmittedBy}" /> --%>
			<c:out value="${actionTicket.organisation}" />
		</display:column>

		<display:column format="{0,date,dd/MM/yy HH:mm}" property="submittedDate" sortable="true" title="Submitted">
			<%-- <fmt:formatDate pattern="dd/MM/yy HH:mm" value="${actionTicket.submittedDate}" /> --%>
		</display:column>

		<display:column format="{0,date,dd/MM/yy HH:mm}" property="updatedDate" sortable="true" title="Updated">
			<%-- <c:if test="${not empty actionTicket.updatedDate}">
				<fmt:formatDate pattern="dd/MM/yy HH:mm" value="${actionTicket.updatedDate}" />
			</c:if> --%>
		</display:column>

		<display:column class="last" sortable="true" title="Status">
			<c:out value="${actionTicket.ticketStatus}" />
		</display:column>
	</display:table>
</fieldset>