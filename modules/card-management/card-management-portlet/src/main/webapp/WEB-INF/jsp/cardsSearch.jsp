<%@ include file="init.jsp" %>

<portlet:defineObjects />

<portlet:actionURL escapeXml="false" var="showCardList">
	<portlet:param name="action" value="cardList" />
	<portlet:param name="from" value="searchFrm" />
</portlet:actionURL>

<portlet:renderURL escapeXml="false" var="resetForm">
	<portlet:param name="render" value="resetCardsForm" />
</portlet:renderURL>

<portlet:renderURL escapeXml="false" var="renderURL" />

<portlet:resourceURL escapeXml="true" id="updateCardReferenceList" var="updateCardReferenceListUrl" />

<script type="text/javascript">
	/* use in no conflict mode */
	jQuery.noConflict();

	/* turn off ajax caching since some versions of IE have issues */
	jQuery.ajaxSetup({
		cache: false
	});

	var orgNameArray = new Array();

	<c:forEach items="${cardSearchForm.organisationsMap}" var="orgNameMap" varStatus="rowCounter">
	   <c:set value="${rowCounter.count}" var="count" />
	   orgNameArray["${count - 1}"] = "<c:out escapeXml="false" value="${orgNameMap.key}" />";
	</c:forEach>

	Liferay.on('allPortletsReady',function() {

		jQuery("input#autocomplete").autocomplete({
				source: orgNameArray,
				minLength : 3
			});
	});
</script>

<c:set value="${sessionScope.cardReferenceList}" var="cards" />

<%
ArrayList<SimpleCard> cards = (ArrayList<SimpleCard>)pageContext.getAttribute("cards");
ArrayList<String> referenceListCardIds = new ArrayList<String>();

if ((cards != null) && (cards.size() > 0)) {
	for (SimpleCard card : cards) {
		referenceListCardIds.add(card.getCardId());
	}
}
%>

<form:form action="${showCardList}" autocomplete="off" data-senna-off="true" id="cardSearchForm" method="post" modelAttribute="cardSearchForm" name="cardSearchForm" onsubmit="javascript:filterCardByFields('${showCardList}');">
	<div id="cardSearch"
		<c:choose>
		<c:when test="${not empty cardList}">
			style="display: none;"
		</c:when>
		<c:otherwise>
			style="display: block;"
		</c:otherwise>
	</c:choose>>

		<%-- Here starts the card search form --%>
		<fieldset class="card-details">
			<legend>Card Details</legend>

			<span class="message">Please specify a PAN or Issuer name and Surname</span>

			<div>
				<label for="panOrBin">
					PAN<em>*</em>
				</label>

				<form:input autocomplete="off" id="panOrBin" path="panOrBin" value="${cardSearchForm.panOrBin}" />

				<br />

				<form:errors cssClass="error" path="panOrBin" />
			</div>
		</fieldset>

		<div class="or">Or</div>

		<fieldset class="user-details">
			<legend>User Details</legend>

			<div>
				<label class="issuer" for="issuer">
					Issuer Name<em>*</em>
				</label>

				<c:set value="${cardSearchForm.organisationsMap}" var="sizeMap" />
						<c:choose>
				<c:when test="${fn:length(sizeMap) == 1}">
				<c:forEach items="${cardSearchForm.organisationsMap}" var="orgOneName">
						<form:input class="readonly" id="oneOrg" path="issuer" readonly="true" value="${orgOneName.key}" />
				</c:forEach>
				</c:when>
				<c:otherwise>
					<form:input id="autocomplete" maxlength="300" path="issuer" value="${cardSearchForm.issuer}" />
				</c:otherwise>
				</c:choose>

				<br />

				<form:errors cssClass="error" path="issuer" />
			</div>

			<div>
				<label class="cardholder-name" for="surname">
					Surname<em>*</em>
				</label>

				<form:input id="cardholderName" maxlength="50" path="cardholderName" value="${cardSearchForm.cardholderName}" />

				<br />

				<form:errors cssClass="error" path="cardholderName" />
			</div>

			<div>
				<label for="">Postcode</label>

				<form:input class="postcode" id="postCode" maxlength="15" path="postCode" value="${cardSearchForm.postCode}" />
			</div>
		</fieldset>

		<fieldset class="card-status">
			<legend>Card Status</legend>

			<span class="message">
				All card status' and scheme types will automatically appear in your search. You may customise your search
				by selecting the required options in 'Card Status' and 'Scheme Type'. Use Ctrl + click to select multiple options.
			</span>

			<div>
				<label for="cardStatus">Card Status</label>

				<form:select id="cardStatus" multiple="true" path="cardStatus" size="10">
					<c:forEach items="${cardSearchForm.cardStatusMap}" var="cardStatusOpts">
						<form:option value="${cardStatusOpts.key}">${cardStatusOpts.value}</form:option>
					</c:forEach>
				</form:select>
			</div>

			<%-- <div>
				<label for="schemeType">Scheme Type</label>

				<form:select id="schemeType" multiple="true" path="schemeType" size="10">
					<c:forEach items="${cardSearchForm.schemeTypeMap}" var="schemeOpts">
						<form:option value="${schemeOpts.key}">${schemeOpts.value}</form:option>
					</c:forEach>
				</form:select>
			</div> --%>

			<div class="non-expired">
				<label for="nonExpired">Exclude Expired Cards</label>

				<form:checkbox id="nonExpired" path="nonExpired" />
			</div>

			<div class="expiry-date">
				<label for="expiryDate">Expiry Date</label>

				<form:input class="expiry-month" id="expiryMonth" maxlength="2" path="expiryMonth" value="${cardSearchForm.expiryMonth}" />
				<form:input class="expiry-year" id="expiryYear" maxlength="4" path="expiryYear" value="${cardSearchForm.expiryYear}" />

				<br />

				<form:errors cssClass="error" path="expiryMonth" />
			</div>
		</fieldset>

		<input class="search-button" type="submit" value="Search" />

		<input class="clear-button" onclick="javascript:clearAll('${resetForm}');" type="button" value="Clear" />
		<%-- <a href="javascript:clearAll('${resetForm}');" class="clear-button">
			Clear
		</a> --%>

		<input id="cardId" name="cardId" type="hidden" value="" />
		<input id="cuscalToken" name="cuscalToken" type="hidden" value="" />
	</div>

	<br />

	<c:if test="${not empty cardList}">
		<div id="card-search-form">
			<a href="javascript:showHideSearchForm()" id="show-hide-search">
				<c:choose>
					<c:when test="${not empty cardList}">
					Show search form
					</c:when>
					<c:otherwise>
					Hide search form
					</c:otherwise>
				</c:choose>
			</a>
		</div>

		<br />
	</c:if>

	<%-- Card Reference List section starts here --%>
<!-- <%@ include file="cardReferenceList.jsp" %>-->
	<%-- Card Reference List section ends here --%>

	<c:if test="${moreOrg}">
		<p class="error">
			<spring:message code="cards.error.more.org.msg" />
		</p>
	</c:if>

	<c:if test="${failureError}">
		<p class="error">
			<spring:message code="cards.error.msg" />
		</p>
	</c:if>

	<display:table class="cuscalTable" id="cardFrm" name="cardList" pagesize="25" requestURI="${renderURL}" sort="external" uid="cardFrm">
		<display:setProperty name="basic.msg.empty_list">
			<c:if test="${not empty noRecordsFound}">
				<p class="portlet-msg-info">${noRecordsFound}</p>
			</c:if>
		</display:setProperty>

		<display:setProperty
			name="factory.requestHelper"
			value="org.displaytag.portlet.PortletRequestHelperFactory"
		/>

		<display:setProperty name="paging.banner.page.separator">
		</display:setProperty>

		<display:setProperty name="paging.banner.placement">both</display:setProperty>

		<display:setProperty name="paging.banner.item_name">result</display:setProperty>
		<display:setProperty name="paging.banner.items_name">results</display:setProperty>

		<display:setProperty name="paging.banner.all_items_found">
			<span class="pagination-text">
				Displaying results 1-{0} of {0}
			</span>
		</display:setProperty>

		<display:setProperty name="paging.banner.some_items_found">
			<span class="pagination-text">
				Displaying results {2}-{3} of {0}
			</span>
		</display:setProperty>

		<display:setProperty name="paging.banner.first">
			<span class="pagination-links">
				{0} <a data-senna-off="true" href="{3}">&gt;</a>
			</span>
		</display:setProperty>

		<display:setProperty name="paging.banner.last">
			<span class="pagination-links">
				<a data-senna-off="true" href="{2}">&lt;</a> {0}
			</span>
		</display:setProperty>

		<display:setProperty name="paging.banner.onepage">
			<span class="pagination-links">{0}</span>
		</display:setProperty>

		<display:setProperty name="paging.banner.full">
			<span class="pagination-links">
				<a data-senna-off="true" href="{2}">&lt;</a> {0} <a data-senna-off="true" href="{3}">&gt;</a>
			</span>
		</display:setProperty>

		<display:setProperty name="css.th.ascending" value="ascending" />
		<display:setProperty name="css.th.descending" value="descending" />

		<portlet:renderURL var="showCardsDetails">
			<portlet:param name="render" value="cardD" />
			<portlet:param name="cardTab" value="CardInfo" />
		</portlet:renderURL>

		<%-- Card Reference List add/remove button column (need remove 'class="first"' from the next column) --%>

<%--
		<display:column class="first">

		<c:set value="${cardFrm.cardId}" var="searchResultCardId" />

		<a class="update-shortlist" href="javascript:void(0)" id="reference-${cardFrm.cardId}" onclick="javascript:addToShortlist('${cardFrm.pan}', '${cardFrm.cardId}','${cardFrm.cuscalToken}', '${showCardsDetails}', '${updateCardReferenceListUrl}')">
		<% String searchResultCardId = (String) pageContext.getAttribute("searchResultCardId");

			if (referenceListCardIds.contains(searchResultCardId)) {
				out.print("-");
			}
else {
				out.print("+");
			}
		%>

		</a>
		</display:column>
--%>

		<display:column class="first" headerClass="results-pan" title="PAN">
			<a class="no-print" href="javascript:openCardsDetailsPage('${cardFrm.cardId}','${cardFrm.cuscalToken}','${showCardsDetails}');" id="panCard">${cardFrm.pan}</a>

			<span class="print">${cardFrm.maskedPan}</span>
		</display:column>

		<display:column defaultorder="ascending" property="cardHolder" sortable="true" sortProperty="lastName" title="Cardholder" />
		<display:column headerClass="address" property="address" title="Address" />
		<display:column defaultorder="ascending" property="institution" sortable="true" sortProperty="issuerId" title="Issuer" />
		<display:column defaultorder="ascending" property="status" sortable="true" sortProperty="statusCode" title="Status" />
		<display:column defaultorder="ascending" property="expiryDate" sortable="true" sortProperty="expiryDate" title="Expiry Date" />

		<display:column class="last" defaultorder="ascending" headerClass="last-modified" sortable="true" sortProperty="lastUpdated" title="Last Modified">
			<fmt:formatDate pattern="dd/MM/yyyy" value="${cardFrm.lastUpdated}" />
		</display:column>
	</display:table>
</form:form>