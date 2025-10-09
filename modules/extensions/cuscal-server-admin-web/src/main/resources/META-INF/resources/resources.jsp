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

<%-- CUSCAL: START CUSTOMIZATION --%>

<style>
		table.memory {
			text-align: left;
			font-size: 12px;
			font-family: verdana;
			background: #c0c0c0;
		}

		table.memory thead tr,
		table.memory tfoot tr {
			background: #c0c0c0;
		}

		table.memory tbody tr {
			background: #f0f0f0;
		}

		td.memory, th.memory {
			border: 1px solid white;
		}
</style>

<%-- CUSCAL: END CUSTOMIZATION --%>

<liferay-ui:error exception="<%= CaptchaConfigurationException.class %>" message="a-captcha-error-occurred-please-contact-an-administrator" />
<liferay-ui:error exception="<%= CaptchaException.class %>" message="captcha-verification-failed" />
<liferay-ui:error exception="<%= CaptchaTextException.class %>" message="text-verification-failed" />

<%
	String[] installedPatches = PatcherValues.INSTALLED_PATCH_NAMES;

	Date modifiedDate = PortalUtil.getUptime();

	long uptimeDiff = System.currentTimeMillis() - modifiedDate.getTime();

	long days = uptimeDiff / Time.DAY;
	long hours = (uptimeDiff / Time.HOUR) % 24;
	long minutes = (uptimeDiff / Time.MINUTE) % 60;
	long seconds = (uptimeDiff / Time.SECOND) % 60;

	Runtime runtime = Runtime.getRuntime();

	long totalMemory = runtime.totalMemory();

	long usedMemory = totalMemory - runtime.freeMemory();
%>

<div class="sheet">
	<div class="panel-group panel-group-flush">
		<aui:fieldset>
			<div class="alert alert-info">
				<strong><liferay-ui:message key="info" /></strong>: <%= ReleaseInfo.getReleaseInfo() %>
				<c:if test="<%= (installedPatches != null) && (installedPatches.length > 0) %>">
					<strong><liferay-ui:message key="patch" /></strong>: <%= StringUtil.merge(installedPatches, StringPool.COMMA_AND_SPACE) %>
				</c:if>

				<strong><liferay-ui:message key="uptime" /></strong>:

				<c:if test="<%= days > 0 %>">
					<%= days %> <liferay-ui:message key='<%= (days > 1) ? "days" : "day" %>' />,
				</c:if>

				<%
					NumberFormat timeNumberFormat = NumberFormat.getInstance();

					timeNumberFormat.setMaximumIntegerDigits(2);
					timeNumberFormat.setMinimumIntegerDigits(2);
				%>

				<%= timeNumberFormat.format(hours) %>:<%= timeNumberFormat.format(minutes) %>:<%= timeNumberFormat.format(seconds) %>
			</div>

			<div class="meter-wrapper text-center">
				<portlet:resourceURL id="/server_admin/view_chart" var="totalMemoryChartURL">
					<portlet:param name="type" value="total" />
					<portlet:param name="totalMemory" value="<%= String.valueOf(totalMemory) %>" />
					<portlet:param name="usedMemory" value="<%= String.valueOf(usedMemory) %>" />
				</portlet:resourceURL>

				<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="memory-used-vs-total-memory" />" src="<%= totalMemoryChartURL %>" />

				<portlet:resourceURL id="/server_admin/view_chart" var="maxMemoryChartURL">
					<portlet:param name="type" value="max" />
					<portlet:param name="maxMemory" value="<%= String.valueOf(runtime.maxMemory()) %>" />
					<portlet:param name="usedMemory" value="<%= String.valueOf(usedMemory) %>" />
				</portlet:resourceURL>

				<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="memory-used-vs-max-memory" />" src="<%= maxMemoryChartURL %>" />
			</div>

			<br />

			<%
				NumberFormat basicNumberFormat = NumberFormat.getInstance(locale);
			%>

			<table class="lfr-table memory-status-table">
				<tr>
					<td>
						<span class="font-weight-semi-bold"><liferay-ui:message key="used-memory" /></span>
					</td>
					<td>
						<span class="text-muted"><%= basicNumberFormat.format(usedMemory) %> <liferay-ui:message key="bytes" /></span>
					</td>
				</tr>
				<tr>
					<td>
						<span class="font-weight-semi-bold"><liferay-ui:message key="total-memory" /></span>
					</td>
					<td>
						<span class="text-muted"><%= basicNumberFormat.format(runtime.totalMemory()) %> <liferay-ui:message key="bytes" /></span>
					</td>
				</tr>
				<tr>
					<td>
						<span class="font-weight-semi-bold"><liferay-ui:message key="maximum-memory" /></span>
					</td>
					<td>
						<span class="text-muted"><%= basicNumberFormat.format(runtime.maxMemory()) %> <liferay-ui:message key="bytes" /></span>
					</td>
				</tr>
			</table>
		</aui:fieldset>

		<liferay-captcha:captcha />

		<aui:fieldset collapsed="<%= false %>" collapsible="<%= true %>" label="system-actions">
			<ul class="list-group system-action-group">
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="run-the-garbage-collector-to-free-up-memory" />
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="gc" value="execute" />
					</div>
				</li>
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="generate-thread-dump" />
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="threadDump" value="execute" />
					</div>
				</li>
			</ul>
		</aui:fieldset>

		<%-- CUSCAL: START CUSTOMIZATION --%>

		<liferay-ui:panel
				collapsible="<%= true %>"
				cssClass="server-admin-actions-panel"
				extended="<%= true %>"
				id="adminServerAdministrationMemoryPanel2"
				markupView="lexicon"
				persistState="<%= true %>"
				title="JVM Memory Monitor"
		>

			<%
				Iterator iter = ManagementFactory.getMemoryPoolMXBeans(
				).iterator();
			%>

			<ul class="list-group system-action-group">
				<li class="clearfix list-group-item">
					<div class="float-left">

						<%
							if (iter.hasNext()) {
								MemoryPoolMXBean item = (MemoryPoolMXBean)iter.next();
						%>

						<h5><liferay-ui:message key="Memory MXBean" /></h5>

						<h5><liferay-ui:message key="Heap Memory Usage" /></h5>
						<%= ManagementFactory.getMemoryMXBean().getHeapMemoryUsage() %><br /><br />

						<h5><liferay-ui:message key="Non-Heap Memory Usage" /></h5>
						<%= ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage() %>

						<%
							}
						%>

						<br /><br />

						<h5><liferay-ui:message key="Memory Pool MXBeans" /></h5>

						<%
							iter = ManagementFactory.getMemoryPoolMXBeans(
							).iterator();

							while (iter.hasNext()) {
								MemoryPoolMXBean item = (MemoryPoolMXBean)iter.next();
						%>

						<h5><liferay-ui:message key="<%= item.getName() %>" /></h5>
						Type <%= item.getType() %><br />
						Usage <%= item.getUsage() %><br />
						Peak Usage <%= item.getPeakUsage() %><br />
						Collection Usage <%= item.getCollectionUsage() %>
						<br /><br />
						<%if ("PS Perm Gen".equalsIgnoreCase(item.getName().toString())){ %>
						<b>PermGen Summary</b><br />
						Initial JVM PermGen allocation <%= item.getUsage().getInit()/1024L/1024L %>MB<br />
						Committed PermGen for the JVM to use <%= item.getUsage().getCommitted()/1024L/1024L %>MB<br />
						<b><font color="red">Current PermGen <%= item.getUsage().getUsed()/1024L/1024L %>MB</font></b><br />

						<b><font color="red">Maximum PermGen available <%= item.getUsage().getMax()/1024L/1024L %>MB</font></b><br />

						<%
								}
							}
						%>

					</div>
				</li>
			</ul>
		</liferay-ui:panel>

		<%-- CUSCAL: END CUSTOMIZATION --%>

		<aui:fieldset collapsed="<%= false %>" collapsible="<%= true %>" label="cache-actions">
			<ul class="list-group system-action-group">
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="clear-content-cached-by-this-vm" />
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="cacheSingle" value="execute" />
					</div>
				</li>
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="clear-content-cached-across-the-cluster" />
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="cacheMulti" value="execute" />
					</div>
				</li>
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="clear-the-database-cache" />
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="cacheDb" value="execute" />
					</div>
				</li>
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="clear-the-direct-servlet-cache" />
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="cacheServlet" value="execute" />
					</div>
				</li>
			</ul>
		</aui:fieldset>

		<aui:fieldset collapsed="<%= false %>" collapsible="<%= true %>" label="verification-actions">
			<ul class="list-group system-action-group">
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="verify-membership-policies" />
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="verifyMembershipPolicies" value="execute" />
					</div>
				</li>
			</ul>
		</aui:fieldset>

		<aui:fieldset collapsed="<%= false %>" collapsible="<%= true %>" label="clean-up-actions">
			<ul class="list-group system-action-group">
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="reset-preview-and-thumbnail-files-for-documents-and-media" />

							<span aria-label="<%= LanguageUtil.get(request, "reset-preview-and-thumbnail-files-for-documents-and-media-help") %>" class="lfr-portal-tooltip" tabindex="0" title="<%= LanguageUtil.get(request, "reset-preview-and-thumbnail-files-for-documents-and-media-help") %>">
								<clay:icon
										symbol="question-circle-full"
								/>
							</span>
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="dlDeletePreviews" value="execute" />
					</div>
				</li>
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="clean-up-permissions" />

							<span aria-label="<%= LanguageUtil.get(request, "clean-up-permissions-help") %>" class="lfr-portal-tooltip" tabindex="0" title="<%= LanguageUtil.get(request, "clean-up-permissions-help") %>">
								<clay:icon
										symbol="question-circle-full"
								/>
							</span>
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="cleanUpAddToPagePermissions" value="execute" />
					</div>
				</li>
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="clean-up-orphaned-page-revision-portlet-preferences" />

							<span aria-label="<%= LanguageUtil.get(request, "clean-up-orphaned-page-revision-portlet-preferences-help") %>" class="lfr-portal-tooltip" tabindex="0" title="<%= LanguageUtil.get(request, "clean-up-orphaned-page-revision-portlet-preferences-help") %>">
								<clay:icon
										symbol="question-circle-full"
								/>
							</span>
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="cleanUpLayoutRevisionPortletPreferences" value="execute" />
					</div>
				</li>
				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="clean-up-orphaned-theme-portlet-preferences" />

							<span aria-label="<%= LanguageUtil.get(request, "clean-up-orphaned-theme-portlet-preferences-help") %>" class="lfr-portal-tooltip" tabindex="0" title="<%= LanguageUtil.get(request, "clean-up-orphaned-theme-portlet-preferences-help") %>">
								<clay:icon
										symbol="question-circle-full"
								/>
							</span>
						</p>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="cleanUpOrphanedPortletPreferences" value="execute" />
					</div>
				</li>
			</ul>
		</aui:fieldset>

		<aui:fieldset collapsed="<%= false %>" collapsible="<%= true %>" label="regeneration-actions">
			<ul class="list-group system-action-group">
				<c:if test="<%= (audioConverter != null) && audioConverter.isEnabled() %>">
					<li class="list-group-item list-group-item-flex">
						<div class="autofit-col autofit-col-expand">
							<p class="list-group-title text-truncate">
								<liferay-ui:message key="regenerate-preview-of-audio-files-in-documents-and-media" />

								<span aria-label="<%= LanguageUtil.get(request, "regenerate-preview-of-audio-files-in-documents-and-media-help") %>" class="lfr-portal-tooltip" tabindex="0" title="<%= LanguageUtil.get(request, "regenerate-preview-of-audio-files-in-documents-and-media-help") %>">
									<clay:icon
											symbol="question-circle-full"
									/>
								</span>
							</p>
						</div>

						<%
							List<BackgroundTask> audioPreviewBackgroundTasks = BackgroundTaskManagerUtil.getBackgroundTasks(CompanyConstants.SYSTEM, "com.liferay.document.library.preview.audio.internal.background.task.AudioPreviewBackgroundTaskExecutor", BackgroundTaskConstants.STATUS_IN_PROGRESS);
						%>

						<div class="autofit-col">
							<span class="<%= (audioPreviewBackgroundTasks.size() > 0) ? StringPool.BLANK : "hide" %> loading-animation loading-animation-sm"></span>
						</div>

						<div class="autofit-col">
							<aui:button cssClass="save-server-button" data-cmd="dlGenerateAudioPreviews" disabled="<%= (audioPreviewBackgroundTasks.size() > 0) ? true : false %>" value="execute" />
						</div>
					</li>
				</c:if>

				<c:if test="<%= DocumentConversionUtil.isEnabled() %>">
					<li class="list-group-item list-group-item-flex">
						<div class="autofit-col autofit-col-expand">
							<p class="list-group-title text-truncate">
								<liferay-ui:message key="regenerate-preview-and-thumbnail-of-openoffice-files-in-documents-and-media" />

								<span aria-label="<%= LanguageUtil.get(request, "regenerate-preview-and-thumbnail-of-openoffice-files-in-documents-and-media-help") %>" class="lfr-portal-tooltip" tabindex="0" title="<%= LanguageUtil.get(request, "regenerate-preview-and-thumbnail-of-pdf-files-in-documents-and-media-help") %>">
									<clay:icon
											symbol="question-circle-full"
									/>
								</span>
							</p>
						</div>

						<%
							List<BackgroundTask> openOfficeConversionPreviewBackgroundTasks = BackgroundTaskManagerUtil.getBackgroundTasks(CompanyConstants.SYSTEM, "com.liferay.document.library.document.conversion.internal.background.task.OpenOfficeConversionPreviewBackgroundTaskExecutor", BackgroundTaskConstants.STATUS_IN_PROGRESS);
						%>

						<div class="autofit-col">
							<span class="<%= (openOfficeConversionPreviewBackgroundTasks.size() > 0) ? StringPool.BLANK : "hide" %> loading-animation loading-animation-sm"></span>
						</div>

						<div class="autofit-col">
							<aui:button cssClass="save-server-button" data-cmd="dlGenerateOpenOfficePreviews" disabled="<%= (openOfficeConversionPreviewBackgroundTasks.size() > 0) ? true : false %>" value="execute" />
						</div>
					</li>
				</c:if>

				<li class="list-group-item list-group-item-flex">
					<div class="autofit-col autofit-col-expand">
						<p class="list-group-title text-truncate">
							<liferay-ui:message key="regenerate-preview-and-thumbnail-of-pdf-files-in-documents-and-media" />

							<span aria-label="<%= LanguageUtil.get(request, "regenerate-preview-and-thumbnail-of-pdf-files-in-documents-and-media-help") %>" class="lfr-portal-tooltip" tabindex="0" title="<%= LanguageUtil.get(request, "regenerate-preview-and-thumbnail-of-pdf-files-in-documents-and-media-help") %>">
								<clay:icon
										symbol="question-circle-full"
								/>
							</span>
						</p>
					</div>

					<%
						List<BackgroundTask> pdfPreviewBackgroundTasks = BackgroundTaskManagerUtil.getBackgroundTasks(CompanyConstants.SYSTEM, "com.liferay.document.library.preview.pdf.internal.background.task.PDFPreviewBackgroundTaskExecutor", BackgroundTaskConstants.STATUS_IN_PROGRESS);
					%>

					<div class="autofit-col">
						<span class="<%= (pdfPreviewBackgroundTasks.size() > 0) ? StringPool.BLANK : "hide" %> loading-animation loading-animation-sm"></span>
					</div>

					<div class="autofit-col">
						<aui:button cssClass="save-server-button" data-cmd="dlGeneratePDFPreviews" disabled="<%= (pdfPreviewBackgroundTasks.size() > 0) ? true : false %>" value="execute" />
					</div>
				</li>

				<c:if test="<%= (videoConverter != null) && videoConverter.isEnabled() %>">
					<li class="list-group-item list-group-item-flex">
						<div class="autofit-col autofit-col-expand">
							<p class="list-group-title text-truncate">
								<liferay-ui:message key="regenerate-preview-and-thumbnail-of-video-files-in-documents-and-media" />

								<span aria-label="<%= LanguageUtil.get(request, "regenerate-preview-and-thumbnail-of-video-files-in-documents-and-media-help") %>" class="lfr-portal-tooltip" tabindex="0" title="<%= LanguageUtil.get(request, "regenerate-preview-and-thumbnail-of-video-files-in-documents-and-media-help") %>">
									<clay:icon
											symbol="question-circle-full"
									/>
								</span>
							</p>
						</div>

						<%
							List<BackgroundTask> videoPreviewBackgroundTasks = BackgroundTaskManagerUtil.getBackgroundTasks(CompanyConstants.SYSTEM, "com.liferay.document.library.preview.video.internal.background.task.VideoPreviewBackgroundTaskExecutor", BackgroundTaskConstants.STATUS_IN_PROGRESS);
						%>

						<div class="autofit-col">
							<span class="<%= (videoPreviewBackgroundTasks.size() > 0) ? StringPool.BLANK : "hide" %> loading-animation loading-animation-sm"></span>
						</div>

						<div class="autofit-col">
							<aui:button cssClass="save-server-button" data-cmd="dlGenerateVideoPreviews" disabled="<%= (videoPreviewBackgroundTasks.size() > 0) ? true : false %>" value="execute" />
						</div>
					</li>
				</c:if>
			</ul>
		</aui:fieldset>
	</div>
</div>