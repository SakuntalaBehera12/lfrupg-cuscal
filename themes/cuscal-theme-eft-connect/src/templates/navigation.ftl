
<nav class="navbar navbar-classic navbar-expand-md navbar-light container pt-0 pb-3">
	<button
		aria-controls="navigationCollapse"
		aria-expanded="false"
		aria-label="Toggle navigation"
		class="btn btn-outline-secondary navbar-toggler navbar-toggler-right py-2"
		data-target="#navigationCollapse"
		data-toggle="liferay-collapse"
		type="button"
		>
		<@clay["icon"] symbol="bars" />
	</button>

	<div class="navigation-fragment-root nav-container container-fluid container-fluid-max-xl">

		<#if has_navigation && is_setup_complete>
			<#assign preferences = freeMarkerPortletPreferences.getPreferences({"portletSetupPortletDecoratorId": "barebone", "destination": "/search"}) />
			<div class="nav-items">	
				<div aria-expanded="false" class="collapse flex-grow-0 navbar-collapse" id="navigationCollapse">
					<@liferay.navigation_menu default_preferences="${preferences}" />
				</div>
				<div class="nav-spacer d-sm-none d-none d-md-block"></div>
			</div>	
		</#if>
	</div>

</nav>


