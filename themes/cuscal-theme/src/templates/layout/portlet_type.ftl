<div class="layout-content portlet-layout" id="main-content" role="main">
	<div class="container widget-container-wrapper-lv1">
		<div class="widget-container-wrapper-lv2">
			<div class="row align-items-lg-start align-items-sm-start align-items-start align-items-md-start flex-lg-row flex-sm-row flex-row flex-md-row">
			<#if show_left_navigation>
			    <div class="col col-lg-2 col-sm-12 col-12 col-md-2 left-navigation-menu-parent-container">
                    <div class="">
                        <div class="left-navigation-menu-container">
                            <#include "${full_templates_path}/left_navigation.ftl" />
                        </div>
                    </div>
                </div>
                <div class="col col-lg-10 col-sm-12 col-12 col-md-10 pl-0">
                    <div class="">
                        <div class="portlet-content-include-container">
                            <#include "${full_templates_path}/portal_content_include.ftl" />
                        </div>
                    </div>
                </div>
			<#else>
			    <div class="col col-lg-12 col-sm-12 col-12 col-md-12">
                    <div class="">
                        <div class="portlet-content-include-container">
                            <#include "${full_templates_path}/portal_content_include.ftl" />
                        </div>
                    </div>
                </div>
			</#if>
			</div>
		</div>
	</div>
</div>