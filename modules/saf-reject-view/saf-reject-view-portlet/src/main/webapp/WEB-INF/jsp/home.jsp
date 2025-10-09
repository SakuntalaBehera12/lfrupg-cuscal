<%@ include file="init.jsp" %>

<c:choose>
	<c:when test="${empty decoratedList1 and empty decoratedList2 and empty decoratedList3}">
		<div class="portlet-msg-info">
			<span>No records found.</span>
		</div>
	</c:when>
	<c:otherwise>
		<display:table class="saf-list" id="safList" name="decoratedList1" uid="safList">
			<display:setProperty name="factory.requestHelper" value="org.displaytag.portlet.PortletRequestHelperFactory" />

			<display:setProperty name="paging.banner.placement">top</display:setProperty>
			<display:setProperty name="paging.banner.one_item_found">
				<span></span>
			</display:setProperty>

			<display:setProperty name="paging.banner.all_items_found">
				<span></span>
			</display:setProperty>

			<display:setProperty name="paging.banner.no_items_found">
				<span></span>
			</display:setProperty>

			<display:setProperty name="paging.banner.onepage">
				<span></span>
			</display:setProperty>

			<%-- Column 1 --%>
			<display:column class="first" title="Date">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${safList.date}" />
			</display:column>

			<display:column class="count" title="SAF Rejects">
				<c:out value="${safList.count}" />
			</display:column>

			<display:column class="last" title="">
				<portlet:resourceURL escapeXml="false" id="downloadReport" var="exportUrl">
					<portlet:param name="sd" value="${safList.settlementDate}" />
				</portlet:resourceURL>

				<c:if test="${safList.count > 0}">
					<a class="file-download" href="${exportUrl}">Download</a>
				</c:if>
			</display:column>
		</display:table>

		<display:table class="saf-list" id="safList" name="decoratedList2" uid="safList">
			<display:setProperty name="factory.requestHelper" value="org.displaytag.portlet.PortletRequestHelperFactory" />

			<display:setProperty name="paging.banner.placement">top</display:setProperty>
			<display:setProperty name="paging.banner.one_item_found">
				<span></span>
			</display:setProperty>

			<display:setProperty name="paging.banner.all_items_found">
				<span></span>
			</display:setProperty>

			<display:setProperty name="paging.banner.no_items_found">
				<span></span>
			</display:setProperty>

			<display:setProperty name="paging.banner.onepage">
				<span></span>
			</display:setProperty>

			<%-- Column 1 --%>
			<display:column class="first" title="Date">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${safList.date}" />
			</display:column>

			<display:column class="count" title="SAF Rejects">
				<c:out value="${safList.count}" />
			</display:column>

			<display:column class="last" title="">
				<portlet:resourceURL escapeXml="false" id="downloadReport" var="exportUrl">
					<portlet:param name="sd" value="${safList.settlementDate}" />
				</portlet:resourceURL>

				<c:if test="${safList.count > 0}">
					<a class="file-download" href="${exportUrl}">Download</a>
				</c:if>
			</display:column>
		</display:table>

		<display:table class="saf-list" id="safList" name="decoratedList3" uid="safList">
			<display:setProperty name="factory.requestHelper" value="org.displaytag.portlet.PortletRequestHelperFactory" />

			<display:setProperty name="paging.banner.placement">top</display:setProperty>
			<display:setProperty name="paging.banner.one_item_found">
				<span></span>
			</display:setProperty>

			<display:setProperty name="paging.banner.all_items_found">
				<span></span>
			</display:setProperty>

			<display:setProperty name="paging.banner.no_items_found">
				<span></span>
			</display:setProperty>

			<display:setProperty name="paging.banner.onepage">
				<span></span>
			</display:setProperty>

			<%-- Column 1 --%>
			<display:column class="first" title="Date">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${safList.date}" />
			</display:column>

			<display:column class="count" title="SAF Rejects">
				<c:out value="${safList.count}" />
			</display:column>

			<display:column class="last" title="">
				<portlet:resourceURL escapeXml="false" id="downloadReport" var="exportUrl">
					<portlet:param name="sd" value="${safList.settlementDate}" />
				</portlet:resourceURL>

				<c:if test="${safList.count > 0}">
					<a class="file-download" href="${exportUrl}">Download</a>
				</c:if>
			</display:column>
		</display:table>
	</c:otherwise>
</c:choose>