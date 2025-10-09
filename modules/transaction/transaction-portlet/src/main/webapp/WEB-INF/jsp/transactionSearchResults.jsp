<%@ include file="init.jsp" %>

<%@ include file="transactionSearch.jsp" %>

<portlet:renderURL escapeXml="false" var="renderURL" />

<c:if test="${dateWarningMsg}">
	<p>
		<spring:message code="transaction.search.fromDate.18month" />
	</p>
</c:if>

<c:if test="${moreDataMsg}">
	<p>
		<spring:message code="transaction.search.fromToDate.moreThan.hardLimit" />
	</p>
</c:if>

<c:if test="${moreOrg}">
	<p class="error no-results">
		<spring:message code="cuscal.transaction.error.more.org.msg" />
	</p>
</c:if>

<c:if test="${dataBaseError}">
	<p class="error no-results">
		<spring:message code="cuscal.transaction.search.results.incomplete" />
	</p>
</c:if>

<c:if test="${otherError}">
	<div class="portlet-msg-error">
		<span class="error no-results">
			<spring:message code="cuscal.transaction.error.msg" />
		</span>
	</div>
</c:if>

<c:if test="${sqlTimeout}">
	<div class="portlet-msg-error">
		<span>
			<spring:message code="cuscal.transaction.timeout.error.msg" />
		</span>
	</div>
</c:if>

<display:table class="cuscalTable" defaultorder="descending" defaultsort="8" id="txFrm" name="sessionScope.txList" pagesize="25" requestURI="${renderURL}" sort="external" uid="txFrm">
	<c:choose>
		<c:when test="${not empty noDataFoundMsg}">
			<display:setProperty name="basic.msg.empty_list">
				<div class="portlet-msg-info">
					<span class="no-results">${noDataFoundMsg}</span>
				</div>
			</display:setProperty>
		</c:when>
		<c:otherwise>
			<display:setProperty name="basic.msg.empty_list" value="" />
		</c:otherwise>
	</c:choose>

	<display:setProperty name="factory.requestHelper" value="org.displaytag.portlet.PortletRequestHelperFactory" />

	<display:setProperty name="paging.banner.page.separator">
	</display:setProperty>

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
		<span class="pagination-links"> <a href="{2}">&lt;</a> {0} <a
			href="{3}">&gt;</a> </span>
	</display:setProperty>

	<display:setProperty name="css.th.ascending" value="ascending" />
	<display:setProperty name="css.th.descending" value="descending" />

	<display:column class="first" defaultorder="ascending" sortable="true" sortName="pan" title="PAN">
		<a href="javascript:openTransactionDetailsJsonObj('${showTxDetails}','${txFrm.transactionId}','${txFrm.busniessDate}','${txFrm.dataSource}','${txFrm_rowNum}','searchResult');" id="panTx">${txFrm.pan}</a>
	</display:column>

	<display:column defaultorder="ascending" property="terminalId" sortable="true" sortName="terminalId" title="Terminal" />

	<display:column defaultorder="ascending" property="cardAcceptorId" sortable="true" sortName="cardAcceptorId" title="Card Acceptor" />

	<display:column defaultorder="ascending" property="externalTransactionId" sortable="true" sortName="externalTransactionId" title="Scheme ID" />

	<display:column defaultorder="ascending" sortable="true" sortName="responseCode" title="Response Code">
		<div title="${txFrm.responseDescription}">${txFrm.responseCode}</div>
	</display:column>

	<display:column defaultorder="ascending" sortable="true" sortName="messageType" title="Message Type">
		<div title="${txFrm.messageDescription}">${txFrm.messageType}</div>
	</display:column>

	<c:choose>
		<c:when test="${displayFunctionCode}">
			<display:column defaultorder="ascending" sortable="true" sortName="functionCode" title="Function Code">
				<div title="${txFrm.functionCodeDescription}">${txFrm.functionCode}</div>
			</display:column>
		</c:when>
		<c:otherwise>
			<display:column title="">
			</display:column>
		</c:otherwise>
	</c:choose>

	<display:column defaultorder="ascending" property="systemTrace" sortable="true" sortName="systemTrace" title="<abbr title='System Trace Audit Number'>STAN</abbr>" />

	<%
	if ((session.getAttribute("switchDate") != null) &&
		"true".equals(
			session.getAttribute(
				"switchDate"
			).toString())) {
	%>

			<display:column defaultorder="descending" format="{0,date,dd/MM/yyyy HH:mm}" property="dateTime" sortable="true" sortName="dateTime" title="Switch Date" />

	<%
	}
	else {
	%>

			<display:column defaultorder="descending" format="{0,date,dd/MM/yyyy HH:mm}" property="dateTime" sortable="true" sortName="dateTime" title="Location Date" />

	<%
	}
	%>

	<display:column defaultorder="ascending" property="description" sortable="true" sortName="description" title="Description" />

	<display:column class="alignRight last" defaultorder="ascending" sortable="true" sortName="amount" title="Amount">
		<span class="nowrap">
			<fmt:formatNumber groupingUsed="true" minFractionDigits="2" minIntegerDigits="1" value="${txFrm.amount}" />&nbsp;${txFrm.currencyCodeAcq}
			<c:if test="${txFrm.currencyCodeAcq ne 'AUD'}">
			<br /><fmt:formatNumber groupingUsed="true" minFractionDigits="2" minIntegerDigits="1" value="${txFrm.cardHolderBillAmount}" />&nbsp;AUD
			</c:if>
		</span>
	</display:column>
</display:table>

<div class="modal-wrapper" id="transDetailErrorDiv">
	<div class="modal-header">
		<h5>Transaction Details</h5>

		<a class="modal-close" href="#">X</a>
	</div>

	<table border="0" cellpadding="0" cellspacing="0" class="response-message-codes tx-details" id="transDetailError">
	</table>
</div>

<%@ include file="transaction-details-wrapper.jsp" %>