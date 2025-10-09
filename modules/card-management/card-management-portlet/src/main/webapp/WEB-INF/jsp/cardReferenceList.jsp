<%@ include file="init.jsp" %>

<portlet:resourceURL escapeXml="false" id="clearCardReferenceList" var="clearCardReferenceListUrl" />
<portlet:resourceURL escapeXml="false" id="removeCardFromCardReferenceList" var="removeCardFromCardReferenceListUrl" />

<portlet:renderURL var="showCardsDetails">
	<portlet:param name="render" value="cardD" />
	<portlet:param name="cardTab" value="CardInfo" />
</portlet:renderURL>

<div id="reference-list-parent">
	<a href="javascript:void(0)" id="show-hide-reference-list">Show Card Reference List</a>

	<div id="reference-list-wrapper">
		<span>Card Reference List</span>

		<div id="reference-list-cards">
			<c:forEach items="${sessionScope.cardReferenceList}" var="card">
				<p>
					<a class="cards" href="javascript:void(0)" id="card-${card.cardId}" onclick="javascript:openCardsDetailsPage('${card.cardId}','${card.cuscalToken}','${showCardsDetails}');">${card.pan}</a>
					<a class="remove-card" href="javascript:void(0)" onclick="javascript:removeCardFromCardReferenceList('${card.cardId}', '${removeCardFromCardReferenceListUrl}')">X</a>
				</p>
			</c:forEach>
		</div>

		<p class="clear-button-wrapper">
			<a class="clear-reference-list" href="javascript:void(0)" onclick="javascript:clearCardReferenceList('${clearCardReferenceListUrl}')">Clear List</a>
		</p>
	</div>
</div>

<c:if test="${not empty sessionScope.cardReferenceList}">
	<script type="text/javascript">
		Liferay.on('allPortletsReady',function() {

			jQuery("#reference-list-parent").show(300);
		});
	</script>
</c:if>