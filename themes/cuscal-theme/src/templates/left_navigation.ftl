<#macro buildNavigation
    branchNavItems
    cssClass
    displayDepth
    includeAllChildNavItems
    navItemLevel
    navItems
>
    <#if navItems?has_content && ((displayDepth == 0) || (navItemLevel <= displayDepth))>
        <div id="secondaryNav">
            <ul class="${cssClass} level-${navItemLevel}">
                <#list navItems as navItem>
                    <#assign
                        nav_item_css_class = "lfr-nav-item"
                    />
                    <#if navItem.isInNavigation(branchNavItems)>
                        <#assign nav_item_css_class = "${nav_item_css_class} open" />
                    </#if>
                    <#if navItem.isSelected()>
                        <#assign
                            nav_item_css_class = "${nav_item_css_class} selected active"
                        />
                    </#if>
                    <li class="${nav_item_css_class}">
                        <#if navItem.isBrowsable()>
                            <a class="${nav_item_css_class}" href="${navItem.getRegularURL()!""}" ${navItem.getTarget()}>${navItem.getName()}</a>
                        <#else>
                            ${navItem.getName()}
                        </#if>
                        <#if navItem.isInNavigation(branchNavItems)>
                            <@buildNavigation
                                branchNavItems=branchNavItems
                                cssClass=cssClass
                                displayDepth=displayDepth
                                includeAllChildNavItems=includeAllChildNavItems
                                navItemLevel=(navItemLevel + 1)
                                navItems=navItem.getChildren()
                            />
                        </#if>
                    </li>
                </#list>
            </ul>
        </div>
    </#if>
</#macro>
<#if !navItems?has_content>
    <#if themeDisplay.isSignedIn()>
        <div class="alert alert-info">
            <@liferay.language key="there-are-no-menu-items-to-display" />
        </div>
    </#if>
<#else>
    <#foreach navItem in navItems>
        <#if navItem.isSelected() && navItem.hasChildren()>
            <div aria-label="<@liferay.language key="site-pages" />" class="list-menu">
        <@buildNavigation
            branchNavItems=branch_nav_items
            cssClass="layouts"
            displayDepth=0
            includeAllChildNavItems=true
            navItemLevel=1
            navItems=navItem.getChildren()
        />
    </div>
        </#if>
    </#foreach>
</#if>