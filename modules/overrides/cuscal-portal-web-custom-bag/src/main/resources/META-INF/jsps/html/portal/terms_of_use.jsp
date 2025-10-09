<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/html/portal/init.jsp" %>

<%-- CUSCAL: START CUSTOMIZATION --%>
<script src="/o/cuscal-theme/js/jquery.min.js" type="text/javascript"></script>
<script src="/o/cuscal-theme/js/jquery.simplemodal-1.4.1.js" type="text/javascript"></script>
<%-- CUSCAL: END CUSTOMIZATION --%>

<%
String referer = ParamUtil.getString(request, WebKeys.REFERER, PortalUtil.getCurrentURL(request));

if (referer.equals(themeDisplay.getPathMain() + "/portal/update_terms_of_use")) {
	referer = themeDisplay.getPathMain() + "?doAsUserId=" + themeDisplay.getDoAsUserId();
}

TermsOfUseContentProvider termsOfUseContentProvider = TermsOfUseContentProviderUtil.getTermsOfUseContentProvider();
%>

<div class="mt-4 sheet sheet-lg">
	<div class="sheet-header">
		<div class="autofit-padded-no-gutters-x autofit-row">
			<div class="autofit-col autofit-col-expand">
				<h2 class="sheet-title">
					<liferay-ui:message key="terms-of-use" />
				</h2>
			</div>

			<div class="autofit-col">
				<%@ include file="/html/portal/select_language.jspf" %>
			</div>
		</div>
	</div>

	<aui:form action='<%= themeDisplay.getPathMain() + "/portal/update_terms_of_use" %>' name="fm">
		<aui:input name="p_auth" type="hidden" value="<%= AuthTokenUtil.getToken(request) %>" />
		<aui:input name="doAsUserId" type="hidden" value="<%= themeDisplay.getDoAsUserId() %>" />
		<aui:input name="<%= WebKeys.REFERER %>" type="hidden" value="<%= referer %>" />

		<div class="sheet-text">
			<c:choose>
				<c:when test="<%= termsOfUseContentProvider != null %>">

					<%
					termsOfUseContentProvider.includeView(request, PipingServletResponse.createPipingServletResponse(pageContext));
					%>

				</c:when>
				<c:otherwise>
					<liferay-util:include page="/html/portal/terms_of_use_default.jsp" />
				</c:otherwise>
			</c:choose>
		</div>
		<%-- CUSCAL: START CUSTOMIZATION --%>
		<br />
		<%-- CUSCAL: END CUSTOMIZATION --%>
		<div class="sheet-footer">
			<c:if test="<%= !user.isAgreedToTermsOfUse() %>">
				<%-- CUSCAL: START CUSTOMIZATION --%>
				<div style="padding-left: 30px;">
					<button class="btn btn-primary" type="submit"><span><liferay-ui:message key="i-agree" /></span></button>
					<button class="btn btn-secondary" onclick="javascript:ShowPopUp(); return false;" type="button"><span><liferay-ui:message key="i-disagree" /></span></button>
				</div>
				<%-- CUSCAL: END CUSTOMIZATION --%>
			</c:if>
		</div>
	</aui:form>
</div>

<%-- CUSCAL: START CUSTOMIZATION --%>
<div class="modal-wrapper terms-of-use">
	<div class="modal-header">
		<h6><liferay-ui:message key="terms-of-use" /></h6>

		<a class="links modal-close" href="#">X</a>
	</div>

	<div class="modal-content">
		<p>
			<liferay-ui:message key="you-must-agree-with-the-terms-of-use-to-continue" />
		</p>
	</div>

	<div class="modal-buttons">
		<a class="links modal-close" href="#"><liferay-ui:message key="ok" /></a>
	</div>
</div>
<%-- CUSCAL: END CUSTOMIZATION --%>

<%-- CUSCAL: START CUSTOMIZATION --%>
<aui:script>
function ShowPopUp() {
	jQuery(".modal-wrapper").modal({
		opacity: 80,
		overlayCss: {backgroundColor:"#000"},
		closeClass: "modal-close"
	});
}
</aui:script>
<%-- CUSCAL: END CUSTOMIZATION --%>