<%@ include file="init.jsp" %>

<portlet:actionURL escapeXml="false" var="submitBpaySCActionUrl">
	<portlet:param name="action" value="submitScAction" />
</portlet:actionURL>

<fieldset>
	<legend>New Service Request</legend>

	<form:form action="${submitBpaySCActionUrl}" class="calls-form" data-senna-off="true" id="scForm" method="post" modelAttribute="serviceCatalogueForm">
		<div>
			<label class="sc-list-label" for="scItems">Service Type<em>*</em></label>

			<form:select id="ticket-type" path="scItem" size="1">
				<option value="">Please select</option>

				<form:options items="${serviceCatalogueData.descriptionMap}" />
			</form:select>

			<form:errors class="error" path="scItem" />
		</div>

		<input class="request-buttons" type="submit" value="Create" />
	</form:form>
</fieldset>