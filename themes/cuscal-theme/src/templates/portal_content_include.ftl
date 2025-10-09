<#if selectable>
    <@liferay_util["include"] page=content_include />
<#else>
    ${portletDisplay.recycle()}

    ${portletDisplay.setTitle(the_title)}

    <@liferay_theme["wrap-portlet"] page="portlet.ftl">
        <@liferay_util["include"] page=content_include />
    </@>
</#if>