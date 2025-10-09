<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<portlet:defineObjects />

<div class="resetPassword">
	<h3>
		<liferay-ui:message key="resetPassword.passwordRecovery.label" />
	</h3>
	<%if(request.getAttribute("successMsg")!= null && (Boolean)request.getAttribute("successMsg")) {
	%>

		<span><liferay-ui:message
				key="resetPassword.resetLink.msg"
			/>
		</span>
		<%} %>
</div>