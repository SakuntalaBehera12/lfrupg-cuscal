<!DOCTYPE html>

<#include init />

<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
	<title>${html_title}</title>

	<#if custom_viewport?has_content>
		<meta content="${custom_viewport}" name="viewport" />
	<#else>
		<meta content="initial-scale=1.0, width=device-width" name="viewport" />
	</#if>

	<@liferay_util["include"] page=top_head_include />

	<link rel="shortcut icon" href="${images_folder}/favicon.ico">
</head>

<body class="${css_class}">

<@liferay_ui["quick-access"] contentId="#main-content" />

<@liferay_util["include"] page=body_top_include />

<div class="d-flex flex-column min-vh-100">

	<#if show_dockbar>
	    <@liferay.control_menu />
	</#if>

	<div class="d-flex flex-column flex-fill" id="wrapper">
	
		<#if show_header>
			<#include "${full_templates_path}/header.ftl" />
		</#if>

		<section class="flex-fill" id="content">
			<h2 class="sr-only" role="heading" aria-level="1">${the_title}</h2>

			<#if is_portlet_page>
                <#include "${full_templates_path}/layout/portlet_type.ftl" />
            <#else>
                <#include "${full_templates_path}/layout/content_type.ftl" />
            </#if>
		</section>

		<#if show_footer>
			<#include "${full_templates_path}/footer.ftl" />
		</#if>
	</div>
</div>

<@liferay_util["include"] page=body_bottom_include />

<@liferay_util["include"] page=bottom_include />

</body>

</html>