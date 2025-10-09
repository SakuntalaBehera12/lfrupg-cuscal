<#assign layoutService = serviceLocator.findService("com.liferay.portal.kernel.service.LayoutLocalService")>
<#assign nav_item = layoutService.getFriendlyURLLayout(getterUtil.getLong(groupId), false, "/online-applications")>
<#assign userLocalService = serviceLocator.findService("com.liferay.portal.kernel.service.UserLocalService")>
<#assign roleLocalService = serviceLocator.findService("com.liferay.portal.kernel.service.RoleLocalService")>
<#assign user_id = getterUtil.getLong(request.getAttribute("USER_ID"))>
<#assign user = userLocalService.getUserById(user_id)>
<#assign userHasLimitedAccess = true>
<#assign orgRoles = []>
<#list user.getMySiteGroups() as place>
	<#assign groupRoles = roleLocalService.getGroupRoles(place.getGroupId()) >
	<#assign orgRoles+=groupRoles >
</#list>
<#foreach role in orgRoles>
    <#if role.getName() == "Customer General Content">
        <#assign userHasLimitedAccess = false>
    </#if>
</#foreach>
<div id="world">

	<div class="left-bar">
		<div class="general-content-wrapper">
			<h1 class="general-content-heading">
			${generalcontentheading.getData()}
			</h1>
			<div class="general-content">
				<#if userHasLimitedAccess == true>
					${limtedaccessuserscontent.getData()}
			</div>
			</div>
				<#else>
					<#foreach generalcontent in generalcontentheading.getChildren()>
						${generalcontent.getData()}
					</#foreach>
		</div>
	</div>
	<div class="news-section">
		<h2>Cuscal News</h2>
		<div class="latest-news">
					<runtime-portlet name="${latestnewsid.getData()}" />
					<#list latestnewsid.getChildren() as latestnewschildren>
						<#if latestnewschildren?index == 0>
							<#assign latestnewstext = latestnewschildren.getData()>
						<#else>
							<#assign latestnewslink = latestnewschildren.getData()>
						</#if>
					</#list>
					<a href="${latestnewslink}" class="read-more">${latestnewstext}</a>
		</div>
		<h2>Bulletins</h2>
		<div class="latest-operational-bulletins">
					<runtime-portlet name="${operationalbulletinsid.getData()}" />
					<#foreach operationbulletinschildren in operationalbulletinsid.getChildren()>
						<#if operationbulletinschildren?index == 0>
							<#assign operationbulletinstext = operationbulletinschildren.getData()>
						<#else>
							<#assign operationbulletinslink = operationbulletinschildren.getData()>
						</#if>
					</#foreach>
					<a href="${operationbulletinslink}" class="read-more">${operationbulletinstext}</a>
		</div>
		<div class="special-ad-images-wrapper">
					<#foreach specialimage in bannerimages.getSiblings()>
						<#foreach speciallink in specialimage.getChildren()>
							<#assign specialimagelink = speciallink.getData()>
						</#foreach>
						<#if specialimage?index == 2>
							<a href="${specialimagelink}" class="special-image-links">
								<img src="${specialimage.getData()}" alt="" class="special-ad-images last" />
							</a>
							
						<#else>
							<a href="${specialimagelink}" class="special-image-links">
								<img src="${specialimage.getData()}" alt="" class="special-ad-images" />
							</a>
						</#if>
					</#foreach>
		</div>
		<br style="clear: both;" />
	</div>
				<#-- End the if statement for the limnited access view.-->
				</#if>

</div>

<div class="right-bar">
<#if nav_item.getChildren(permissionChecker)?has_content>
	<ul class="online-apps">
		<#list nav_item.getChildren(permissionChecker) as nav_app >
		<#if nav_app?index == 1>
			<li class="first">
			<#else>
				<li>
				</#if>
				<a href="javascript: void(0)" onClick="javascript: openPopup('${nav_app.getFriendlyURL()}', '_blank'); return false;"><span>${nav_app.getName()}</span></a>
			</li>
			</#list>
		</ul>
	</#if>
</div>
</div>

<script type="text/javascript">
	function openPopup(url, name) {
		var screenHeight = screen.height;
		var screenWidth = screen.width;
		
		if (screenHeight > 768) {
			var windowHeight = 768;
		} else {
			var windowHeight = 600;
		}
		
		
		if (screenWidth > 1024) {
			var windowWidth = 1024;
		} else {
			var windowWidth = 800
		}
		
		var popUpWindow = window.open(url, name, 'resizable=1, location=1, menubar=1, toolbar=1, status=1, scrollbars=1');
		popUpWindow.resizeTo(windowWidth, windowHeight);
	}
</script>