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

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

if (Validator.isNull(redirect)) {
	redirect = ParamUtil.getString(PortalUtil.getOriginalServletRequest(request), "redirect");
}

redirect = PortalUtil.escapeRedirect(redirect);

boolean showBackURL = GetterUtil.getBoolean(request.getAttribute("view.jsp-showBackURL"));

if (Validator.isNull(redirect)) {
	PortletURL portletURL = renderResponse.createRenderURL();

	portletURL.setParameter("mvcPath", "/view.jsp");

	redirect = portletURL.toString();
}

AssetEntry assetEntry = (AssetEntry)request.getAttribute("view.jsp-assetEntry");
AssetRendererFactory<?> assetRendererFactory = (AssetRendererFactory<?>)request.getAttribute("view.jsp-assetRendererFactory");
AssetRenderer<?> assetRenderer = (AssetRenderer<?>)request.getAttribute("view.jsp-assetRenderer");

long previewClassNameId = ParamUtil.getLong(request, "previewClassNameId");
long previewClassPK = ParamUtil.getLong(request, "previewClassPK");

boolean print = GetterUtil.getBoolean(request.getAttribute("view.jsp-print"));

assetPublisherDisplayContext.setLayoutAssetEntry(assetEntry);

assetEntry = assetPublisherDisplayContext.incrementViewCounter(assetEntry);

String title = assetRenderer.getTitle(LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request)));

PortletURL viewFullContentURL = assetPublisherHelper.getBaseAssetViewURL(liferayPortletRequest, liferayPortletResponse, assetRenderer, assetEntry);

if (print) {
	viewFullContentURL.setParameter("viewMode", Constants.PRINT);
}

String viewInContextURL = assetRenderer.getURLViewInContext(liferayPortletRequest, liferayPortletResponse, HttpUtil.setParameter(viewFullContentURL.toString(), "redirect", currentURL));

Map<String, Object> fragmentsEditorData = HashMapBuilder.<String, Object>put(
	"fragments-editor-item-id", PortalUtil.getClassNameId(assetRenderer.getClassName()) + "-" + assetRenderer.getClassPK()
).put(
	"fragments-editor-item-type", "fragments-editor-mapped-item"
).build();
%>

<div class="asset-full-content clearfix mb-5 <%= assetPublisherDisplayContext.isDefaultAssetPublisher() ? "default-asset-publisher" : StringPool.BLANK %> <%= assetPublisherDisplayContext.isShowAssetTitle() ? "show-asset-title" : "no-title" %> <%= ((previewClassNameId == assetEntry.getClassNameId()) && (previewClassPK == assetEntry.getClassPK())) ? "p-1 preview-asset-entry" : StringPool.BLANK %>" <%= AUIUtil.buildData(fragmentsEditorData) %>>

	<%
	String fullContentRedirect = themeDisplay.getURLCurrent();

	if (WorkflowDefinitionLinkLocalServiceUtil.hasWorkflowDefinitionLink(assetEntry.getCompanyId(), assetEntry.getGroupId(), assetEntry.getClassName())) {
		fullContentRedirect = redirect;
	}

	request.setAttribute("view.jsp-fullContentRedirect", fullContentRedirect);
	%>

	<liferay-util:buffer
		var="assetActions"
	>
		<liferay-util:include page="/asset_actions.jsp" servletContext="<%= application %>" />
	</liferay-util:buffer>

	<c:if test="<%= (showBackURL && Validator.isNotNull(redirect)) || assetPublisherDisplayContext.isShowAssetTitle() || (!print && Validator.isNotNull(assetActions)) %>">
		<div class="align-items-center d-flex">
			<!-- Begin custom -->
			<!-- Customizing by removing source on 7.3 version -->
			<!-- End custom -->
			<c:if test="<%= !print %>">
				<c:if test="<%= Validator.isNotNull(assetActions) %>">
					<div class="d-inline-flex">
						<%= assetActions %>
					</div>
				</c:if>
			</c:if>
		</div>
	</c:if>

	<span class="asset-anchor lfr-asset-anchor" id="<%= assetEntry.getEntryId() %>"></span>

	<!-- Begin custom -->
	<!-- Customizing by removing source on 7.3 version -->
	<!-- End custom -->

	<div class="asset-content mb-3">
		<liferay-asset:asset-display
			assetEntry="<%= assetEntry %>"
			assetRenderer="<%= assetRenderer %>"
			assetRendererFactory="<%= assetRendererFactory %>"
			showExtraInfo="<%= assetPublisherDisplayContext.isShowExtraInfo() %>"
		/>
	</div>

	<c:if test="<%= assetPublisherDisplayContext.isShowCategories() %>">
		<div class="asset-categories mb-3">
			<liferay-asset:asset-categories-summary
				className="<%= assetEntry.getClassName() %>"
				classPK="<%= assetEntry.getClassPK() %>"
				displayStyle="simple-category"
				portletURL="<%= renderResponse.createRenderURL() %>"
			/>
		</div>
	</c:if>

	<c:if test="<%= assetPublisherDisplayContext.isShowTags() %>">
		<div class="asset-tags mb-3">
			<liferay-asset:asset-tags-summary
				className="<%= assetEntry.getClassName() %>"
				classPK="<%= assetEntry.getClassPK() %>"
				portletURL="<%= renderResponse.createRenderURL() %>"
			/>
		</div>
	</c:if>

	<c:if test="<%= assetPublisherDisplayContext.isShowPriority() %>">
		<div class="asset-priority mb-4 text-secondary">
			<liferay-ui:message key="priority" />: <%= assetEntry.getPriority() %>
		</div>
	</c:if>

	<c:if test="<%= assetPublisherDisplayContext.isEnableRelatedAssets() %>">

		<%
		PortletURL assetLingsURL = renderResponse.createRenderURL();

		assetLingsURL.setParameter("mvcPath", "/view_content.jsp");

		if (print) {
			assetLingsURL.setParameter("viewMode", Constants.PRINT);
		}
		%>

		<div class="asset-links mb-4">
			<liferay-asset:asset-links
				assetEntryId="<%= assetEntry.getEntryId() %>"
				portletURL="<%= assetLingsURL %>"
				viewInContext="<%= assetPublisherDisplayContext.isAssetLinkBehaviorViewInPortlet() %>"
			/>
		</div>
	</c:if>

	<%
	boolean showContextLink = assetPublisherDisplayContext.isShowContextLink() && !print && assetEntry.isVisible();
	boolean showRatings = assetPublisherDisplayContext.isEnableRatings() && assetRenderer.isRatable();
	%>

	<c:if test="<%= showContextLink || showRatings || assetPublisherDisplayContext.isEnableFlags() || assetPublisherDisplayContext.isEnablePrint() || Validator.isNotNull(assetPublisherDisplayContext.getSocialBookmarksTypes()) %>">
		<clay:content-row
			cssClass="asset-details"
			floatElements=""
			verticalAlign="center"
		>
			<c:if test="<%= showContextLink %>">
				<!-- Begin custom -->
				<!-- Customizing by removing source on 7.3 version -->
				<!-- End custom -->
			</c:if>

			<c:if test="<%= showRatings %>">
				<clay:content-col
					cssClass="asset-ratings mr-3"
				>
					<liferay-ratings:ratings
						className="<%= assetEntry.getClassName() %>"
						classPK="<%= assetEntry.getClassPK() %>"
					/>
				</clay:content-col>
			</c:if>

			<c:if test="<%= assetPublisherDisplayContext.isEnableFlags() %>">

				<%
				TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(assetRenderer.getClassName());

				boolean inTrash = trashHandler.isInTrash(assetEntry.getClassPK());
				%>

				<clay:content-col
					cssClass="asset-flag mr-3"
				>
					<liferay-flags:flags
						className="<%= assetEntry.getClassName() %>"
						classPK="<%= assetEntry.getClassPK() %>"
						contentTitle="<%= title %>"
						enabled="<%= !inTrash %>"
						label="<%= false %>"
						message='<%= inTrash ? "flags-are-disabled-because-this-entry-is-in-the-recycle-bin" : null %>'
						reportedUserId="<%= assetRenderer.getUserId() %>"
					/>
				</clay:content-col>
			</c:if>

			<c:if test="<%= assetPublisherDisplayContext.isEnablePrint() %>">
				<clay:content-col
					cssClass="component-subtitle mr-3 print-action"
				>
					<c:choose>
						<c:when test="<%= print %>">
							<liferay-ui:icon
								icon="print"
								linkCssClass="btn btn-monospaced btn-outline-borderless btn-outline-secondary btn-sm"
								markupView="lexicon"
								message='<%= LanguageUtil.format(request, "print-x-x", new Object[] {"hide-accessible", HtmlUtil.escape(title)}, false) %>'
								url="javascript:print();"
							/>

							<aui:script>
								print();
							</aui:script>
						</c:when>
						<c:otherwise>

							<%
							String id = assetEntry.getEntryId() + StringUtil.randomId();
							%>

							<liferay-ui:icon
								icon="print"
								linkCssClass="btn btn-monospaced btn-outline-borderless btn-outline-secondary btn-sm"
								markupView="lexicon"
								message='<%= LanguageUtil.format(request, "print-x-x", new Object[] {"hide-accessible", HtmlUtil.escape(title)}, false) %>'
								url='<%= "javascript:" + liferayPortletResponse.getNamespace() + "printPage_" + id + "();" %>'
							/>

							<%
							PortletURL printAssetURL = renderResponse.createRenderURL();

							printAssetURL.setParameter("mvcPath", "/view_content.jsp");
							printAssetURL.setParameter("assetEntryId", String.valueOf(assetEntry.getEntryId()));
							printAssetURL.setParameter("viewMode", Constants.PRINT);
							printAssetURL.setParameter("type", assetRendererFactory.getType());
							printAssetURL.setParameter("languageId", LanguageUtil.getLanguageId(request));
							printAssetURL.setWindowState(LiferayWindowState.POP_UP);
							%>

							<aui:script>
								function <portlet:namespace />printPage_<%= id %>() {
									window.open(
										'<%= printAssetURL %>',
										'',
										'directories=0,height=480,left=80,location=1,menubar=1,resizable=1,scrollbars=yes,status=0,toolbar=0,top=180,width=640'
									);
								}
							</aui:script>
						</c:otherwise>
					</c:choose>
				</clay:content-col>
			</c:if>

			<c:if test="<%= Validator.isNotNull(assetPublisherDisplayContext.getSocialBookmarksTypes()) %>">
				<clay:content-col>
					<liferay-social-bookmarks:bookmarks
						className="<%= assetEntry.getClassName() %>"
						classPK="<%= assetEntry.getClassPK() %>"
						displayStyle="<%= assetPublisherDisplayContext.getSocialBookmarksDisplayStyle() %>"
						target="_blank"
						title="<%= title %>"
						types="<%= assetPublisherDisplayContext.getSocialBookmarksTypes() %>"
						urlImpl="<%= viewFullContentURL %>"
					/>
				</clay:content-col>
			</c:if>
		</clay:content-row>
	</c:if>

	<%
	boolean showConversions = assetPublisherDisplayContext.isEnableConversions() && assetRenderer.isConvertible() && !print;
	boolean showLocalization = assetPublisherDisplayContext.isShowAvailableLocales() && assetRenderer.isLocalizable() && !print;
	%>

	<c:if test="<%= showConversions || showLocalization %>">
		<div class="separator"><!-- --></div>

		<clay:content-row
			cssClass="asset-details"
			floatElements=""
			verticalAlign="center"
		>
			<c:if test="<%= showLocalization %>">

				<%
				String languageId = LanguageUtil.getLanguageId(request);

				String[] availableLanguageIds = assetRenderer.getAvailableLanguageIds();

				if (ArrayUtil.isNotEmpty(availableLanguageIds) && !ArrayUtil.contains(availableLanguageIds, languageId)) {
					languageId = assetRenderer.getDefaultLanguageId();
				}
				%>

				<c:if test="<%= availableLanguageIds.length > 1 %>">
					<div class="autofit-col locale-actions mr-3">
						<liferay-ui:language
							formAction="<%= currentURL %>"
							languageId="<%= languageId %>"
							languageIds="<%= availableLanguageIds %>"
						/>
					</div>
				</c:if>
			</c:if>

			<c:if test="<%= showConversions %>">

				<%
				PortletURL exportAssetURL = assetRenderer.getURLExport(liferayPortletRequest, liferayPortletResponse);

				exportAssetURL.setParameter("plid", String.valueOf(themeDisplay.getPlid()));
				exportAssetURL.setParameter("portletResource", portletDisplay.getId());
				exportAssetURL.setWindowState(LiferayWindowState.EXCLUSIVE);

				for (String extension : assetPublisherDisplayContext.getExtensions(assetRenderer)) {
					exportAssetURL.setParameter("targetExtension", extension);

					Map<String, Object> data = HashMapBuilder.<String, Object>put(
						"resource-href", exportAssetURL.toString()
					).build();
				%>

					<clay:content-col
						cssClass="component-subtitle export-action"
					>
						<aui:a cssClass="btn btn-outline-borderless btn-outline-secondary btn-sm" data="<%= data %>" href="<%= exportAssetURL.toString() %>" label='<%= LanguageUtil.format(request, "x-convert-x-to-x", new Object[] {"hide-accessible", title, StringUtil.toUpperCase(HtmlUtil.escape(extension))}, false) %>' />
					</clay:content-col>

				<%
				}
				%>

			</c:if>
		</clay:content-row>
	</c:if>

	<c:if test="<%= assetPublisherDisplayContext.isEnableComments() && assetRenderer.isCommentable() %>">
		<clay:col
			cssClass="mt-4"
			md="12"
		>
			<liferay-comment:discussion
				className="<%= assetEntry.getClassName() %>"
				classPK="<%= assetEntry.getClassPK() %>"
				formName='<%= "fm" + assetEntry.getClassPK() %>'
				ratingsEnabled="<%= assetPublisherDisplayContext.isEnableCommentRatings() %>"
				redirect="<%= currentURL %>"
				userId="<%= assetRenderer.getUserId() %>"
			/>
		</clay:col>
	</c:if>
</div>