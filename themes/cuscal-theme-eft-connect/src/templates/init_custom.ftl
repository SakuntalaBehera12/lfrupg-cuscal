<#assign
	custom_viewport = getterUtil.getString(themeDisplay.getThemeSetting("custom-viewport"))
	show_footer = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-footer"))
	show_header = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-header"))
	show_header_search = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-header-search"))
	wrap_widget_page_content = getterUtil.getBoolean(themeDisplay.getThemeSetting("wrap-widget-page-content"))

	permission_checker = themeDisplay.getPermissionChecker()
	group = themeDisplay.getLayout().getGroup()
	is_group_admin = permission_checker.isGroupAdmin(group_id)
	is_omniadmin = permission_checker.isOmniadmin()
	has_permission_view_site_menu = groupPermissionUtil.contains(permission_checker, group, "VIEW_SITE_ADMINISTRATION")
	has_permission_view_control_menu = portalPermissionUtil.contains(permission_checker, "VIEW_CONTROL_PANEL")

	show_dockbar = is_omniadmin || is_group_admin || has_permission_view_site_menu || has_permission_view_control_menu

/>
<#if wrap_widget_page_content && ((layout.isTypeContent() && themeDisplay.isStateMaximized()) || (layout.getType() == "portlet"))>
	<#assign portal_content_css_class = "container" />
<#else>
	<#assign portal_content_css_class = "" />
</#if> 