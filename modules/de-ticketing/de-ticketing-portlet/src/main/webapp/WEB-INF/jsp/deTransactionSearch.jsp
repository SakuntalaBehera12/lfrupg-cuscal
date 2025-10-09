<%@ include file="init.jsp" %>

<div id="service-request-wrapper">
<portlet:renderURL escapeXml="false" var="searchFilterUrl">
	<portlet:param name="render" value="deTransactionSearchAction" />
	<portlet:param name="formId" value="${formId}" />
</portlet:renderURL>

<portlet:renderURL var="resetUrl">
	<portlet:param name="render" value="resetAction" />
	<portlet:param name="formId" value="${formId}" />
</portlet:renderURL>

<portlet:renderURL escapeXml="false" var="deRequestManualUrl">
	<portlet:param name="render" value="deRequest" />
	<portlet:param name="formId" value="${formId}" />
</portlet:renderURL>

<c:choose>
	<c:when test="${formId == 1}">
		<h1>Direct Entry Trace Transaction Search</h1>

		<p class="instruction">To initiate a Trace request, please complete the below fields and search. Once complete, select the applicable transaction from the available results. The selected transaction will automatically populate to the online form</p>
	</c:when>
	<c:when test="${formId == 2}">
		<h1>Direct Entry Recall Transaction Search</h1>

		<p class="instruction">To initiate a Recall request, please complete the below fields and search. Once complete, select the applicable transaction from the available results. The selected transaction will automatically populate to the online form</p>
	</c:when>
	<c:when test="${formId == 3}">
		<h1>Direct Entry Mistaken Internet Payment transaction search</h1>

		<p class="instruction">To initiate a Mistaken Payment request, please complete the below fields and search. Once complete, select the applicable transaction from the available results. The selected transaction will automatically populate to the online form</p>
	</c:when>
	<c:when test="${formId == 4}">
		<h1>Direct Entry Claims Transaction Search</h1>

		<p class="instruction">To initiate a Claims request, please complete the below fields and search. Once complete, select the applicable transaction from the available results. The selected transaction will automatically populate to the online form</p>
	</c:when>
</c:choose>

<form:form action="${searchFilterUrl}" class="search-filter" id="deForm" method="post" modelAttribute="serviceRequestForm">
	<c:choose>
		<c:when test="${formId == 4}">
			<fieldset>
				<legend>Transaction Filter</legend>
				<!-- <span class="message">Please fill in all following fields</span> -->
				<div class="searchField">
					<label class="small" for="attributesInformation.destinationBSB">Destination BSB<em class="mandatory">*</em></label>

					<form:input autocomplete="off" id="destinationBSB" path="attributesInformation.destinationBSB" tabindex="1" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.destinationBSB" />
				</div>

				<div class="searchField">
					<label class="small" for="attributesInformation.destinationAccountNumber">Destination Account Number<em class="mandatory">*</em></label>

					<form:input autocomplete="off" id="destinationAccountNumber" maxlength="15" path="attributesInformation.destinationAccountNumber" tabindex="2" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.destinationAccountNumber" />
				</div>

				<div class="searchField">
					<label class="small" for="attributesInformation.transactionAmounts[0]">Amount<em class="mandatory">*</em></label>

					<form:input autocomplete="off" id="transactionAmount" path="attributesInformation.transactionAmounts[0]" tabindex="3" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.transactionAmounts[0]" />
				</div>

				<div class="searchField">
					<label class="small" for="attributesInformation.processedDates[0]">Date<em class="mandatory">*</em></label>

					<form:input autocomplete="off" cssClass="dpInput" id="processedDate" maxlength="38" path="attributesInformation.processedDates[0]" tabindex="4" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.processedDates[0]" />
				</div>

				<br />

				<div class="searchField">
					<label class="small" for="attributesInformation.destinationLodgementRefs[0]">Lodgement Reference<!-- <em class="mandatory">*</em> --></label>

					<form:input autocomplete="off" id="destinationLodgementRef" maxlength="30" path="attributesInformation.destinationLodgementRefs[0]" tabindex="5" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.destinationLodgementRefs[0]" />
				</div>

				<div class="searchField">
					<label class="small" for="attributesInformation.batchNumber">Batch Number</label>

					<form:input autocomplete="off" id="batchNumber" maxlength="15" path="attributesInformation.batchNumber" tabindex="6" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.batchNumber" />
				</div>
			</fieldset>
		</c:when>
		<c:otherwise>
			<fieldset>
				<legend>Transaction Filter</legend>
				<!-- <span class="message">Please fill in all following fields</span> -->
				<div class="searchField">
					<label class="small" for="attributesInformation.destinationBSB">Destination BSB<em class="mandatory">*</em></label>

					<form:input autocomplete="off" id="destinationBSB" path="attributesInformation.destinationBSB" tabindex="1" />

					<c:choose>
						<c:when test="${formId == 1}">
							<a href="javascript:;" title="In order to perform a transaction search to lodge a Final Destination Trace Request, please enter the BSB of the Financial Institution in which the payment was sent to. In order to perform a transaction search to lodge a Remitter Details Trace Request, please enter the BSB of your Institution"><strong>?</strong></a>
						</c:when>
					</c:choose>

					<br />

					<form:errors cssClass="error" path="attributesInformation.destinationBSB" />
				</div>

				<div class="searchField">
					<label class="small" for="attributesInformation.transactionAmounts[0]">Amount<em class="mandatory">*</em></label>

					<form:input autocomplete="off" id="transactionAmount" maxlength="15" path="attributesInformation.transactionAmounts[0]" tabindex="2" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.transactionAmounts[0]" />
				</div>

				<div class="searchField">
					<label class="small" for="attributesInformation.processedDates[0]">Date<em class="mandatory">*</em></label>

					<form:input autocomplete="off" cssClass="dpInput" id="processedDate" maxlength="38" path="attributesInformation.processedDates[0]" tabindex="3" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.processedDates[0]" />
				</div>

				<div class="searchField">
					<label class="small" for="attributesInformation.finInstUserId">Supplier ID
					<c:choose>
						<c:when test="${formId != 1}">
							<em class="mandatory">*</em>
						</c:when>
					</c:choose>
					</label>

					<form:input autocomplete="off" id="finInstUserId" maxlength="8" path="attributesInformation.finInstUserId" tabindex="4" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.finInstUserId" />
				</div>

				<br />

				<div class="searchField">
					<label class="small" for="attributesInformation.destinationLodgementRefs[0]">Lodgement Reference</label>

					<form:input autocomplete="off" id="destinationLodgementRef" path="attributesInformation.destinationLodgementRefs[0]" tabindex="5" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.destinationLodgementRefs[0]" />
				</div>

				<div class="searchField">
					<label class="small" for="attributesInformation.destinationAccountNumber">Destination Account Number</label>

					<form:input autocomplete="off" id="destinationAccountNumber" maxlength="15" path="attributesInformation.destinationAccountNumber" tabindex="6" />

					<br />

					<form:errors cssClass="error" path="attributesInformation.destinationAccountNumber" />
				</div>
			</fieldset>
		</c:otherwise>
	</c:choose>

	<br />

	<input class="request-buttons" type="submit" value="Search" />
	<input class="request-buttons" onclick="javascript:clearAll('${resetUrl}')" type="button" value="Clear" />

	<div id="loading" style="display: none; z-index: 10000;">
		<div
			style="position: fixed; top: 0px; left: 0px; width: 100%; height: 100%; background-color: rgb(0, 0, 0); opacity: 0.3; z-index: 10000;">&nbsp;</div
		>
		<div
			style="position: fixed; height: 400px; width: 500px; top: 276.5px; left: 381.5px; background-color: rgb(255, 255, 255); text-align: center; border: 1px solid rgb(0, 0, 0); border-radius: 4px; z-index: 10001;"
		>
			<img
				src="<%= request.getContextPath() %>/images/ajax-loader.gif"
				style="position: relative; height: 32px; width: 32px; top: 184px; z-index: 10001;"
			>
			<div
				style="position: relative; top: 204px; font-weight: bold; text-align: center;">Please wait...</div
			>
		</div>
	</div>
</form:form>

<br />

<portlet:renderURL escapeXml="false" var="requestUrl" />

<c:if test="${not empty trans}">
	<div class="portlet-msg-info">
		<span class="no-results">Please select a transaction. If you are unable to locate your transaction <input type="button" class="request-buttons" value="Enter Transaction Details" onclick="javascript:gotoUrl('${deRequestManualUrl}');" /></span>
	</div>
</c:if>

<display:table defaultorder="descending" defaultsort="1" id="tran" name="trans" pagesize="10" requestURI="${requestUrl}" uid="tran">
	<display:setProperty name="basic.msg.empty_list">
	<c:if test="${trans != null && empty trans}">
		<div class="portlet-msg-info">
			<span class="no-results">No transaction found. Please update the search filter and try again or <input type="button" class="request-buttons" value="Enter Transaction Details" onclick="javascript:gotoUrl('${deRequestManualUrl}');" /></span>
		</div>
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

	<display:column class="first" title="Date">
		<portlet:renderURL escapeXml="false" var="deRequestUrl">
			<portlet:param name="render" value="deRequest" />
			<portlet:param name="formId" value="${formId}" />
			<portlet:param name="id" value="${tran.id}" />
		</portlet:renderURL>

		<a href="${deRequestUrl}"><c:out value="${tran.processedDates[0]}" /></a>
	</display:column>

	<c:choose>
		<c:when test="${formId == 4}">
			<display:column property="finInstName" sortable="false" title="User Name" />
			<display:column property="finInstUserId" sortable="false" title="User ID" />
			<display:column property="destinationBSB" sortable="false" title="Destination BSB" />
			<display:column property="destinationAccountNumber" sortable="false" title="Destination Account Number" />
			<display:column property="transactionAmounts[0]" sortable="false" title="Amount" />
			<display:column property="accountTitle" sortable="false" title="Account Name" />
			<display:column property="destinationLodgementRefs[0]" sortable="false" title="Lodgement REF" />
			<display:column property="batchNumber" sortable="false" title="Batch Number" />
			<%-- <display:column property="processedDates[0]" sortable="false" title="Date" /> --%>
		</c:when>
		<c:when test="${formId != 4}">
			<display:column property="destinationBSB" sortable="false" title="Destination BSB" />
			<display:column property="destinationAccountNumber" sortable="false" title="Destination Account Number" />
			<display:column property="transactionAmounts[0]" sortable="false" title="Amount" />
			<display:column property="accountTitle" sortable="false" title="Account Name" />
			<display:column property="destinationLodgementRefs[0]" sortable="false" title="Lodgement REF" />
			<display:column property="transactionCodeName" sortable="false" title="Transaction Type" />
			<display:column property="finInstName" sortable="false" title="FI Name" />
			<display:column property="finInstUserId" sortable="false" title="User ID" />
			<display:column property="finInstTraceBSBNumber" sortable="false" title="Trace BSB" />
			<display:column property="accountNumberRemitter" sortable="false" title="Trace Account Number" />
			<display:column class="last" property="remitterName" sortable="false" title="Remitter Name" />
			<%-- <display:column property="processedDates[0]" sortable="false" title="Date" /> --%>
		</c:when>
	</c:choose>
</display:table>
</div>