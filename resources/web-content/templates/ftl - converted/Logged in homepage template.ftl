<#assign layoutService = serviceLocator.findService("com.liferay.portal.service.LayoutLocalService")><#assign nav_item = layoutService.getFriendlyURLLayout(getterUtil.getLong(request.theme-display.scope-group-id), false, "/online-applications")>
<#assign userLocalService = serviceLocator.findService("com.liferay.portal.service.UserLocalService")><#assign roleLocalService = serviceLocator.findService("com.liferay.portal.service.RoleLocalService")>
<#assign user_id = getterUtil.getLong(request.get("attributes").get("USER_ID"))><#assign user = userLocalService.getUserById(user_id)><#assign userHasLimitedAccess = true><#assign orgRoles = [>
<#foreach place in user.getMyPlaces()><#assign boolean = orgRoles.add(roleLocalService.getGroupRoles(place.getGroupId()))></#foreach>
<#foreach roles in orgRoles>	<#foreach role in roles>		<#if role.getName() == "Customer General Content"><#assign userHasLimitedAccess = false>		</#if>	</#foreach></#foreach>
<div id="world">
	<div class="left-bar">

		<div class="generalcontent-wrapper">
			<h1 class="generalcontent-heading">${generalcontent-heading.getData()}</h1>
			<div class="generalcontent">
			<#if userHasLimitedAccess == true>				${limtedaccessuserscontent.getData()}
			</div>
		</div>
			<#else>				<#foreach generalcontent in generalcontent-heading.getChildren()>					${generalcontent.getData()}
				</#foreach>			</div>
		</div>


		<div class="news-section">
			<h2>Cuscal News</h2>
			<div class="latest-news">
				<runtime-portlet name="${latestnewsid.getData()}" />
				<#foreach latest-news-children in latestnewsid.getChildren()>					<#if velocityCount == 1><#assign latest-news-text = latest-news-children.getData()>					<#else><#assign latest-news-link = latest-news-children.getData()>					</#if>				</#foreach>				<a href="${latest-news-link}" class="read-more">${latest-news-text}</a>
			</div>

			<h2>Bulletins</h2>
			<div class="latest-operational-bulletins">
				<runtime-portlet name="${operationalbulletinsid.getData()}" />
				<#foreach operation-bulletins-children in operationalbulletinsid.getChildren()>					<#if velocityCount == 1><#assign operation-bulletins-text = operation-bulletins-children.getData()>					<#else><#assign operation-bulletins-link = operation-bulletins-children.getData()>					</#if>				</#foreach>				<a href="${operation-bulletins-link}" class="read-more">${operation-bulletins-text}</a>
			</div>


			<div class="special-ad-images-wrapper">
				<#foreach special-image in bannerimages.getSiblings()>					<#foreach special-link in special-image.getChildren()><#assign special-image-link = special-link.getData()>					</#foreach>					<#if mathTool.mod(velocityCount, 3) == 0>						<a href="${special-image-link}" class="special-image-links">
							<img src="${special-image.getData()}" alt="" class="special-ad-images last" />
						</a>
						<br />
					<#else>						<a href="${special-image-link}" class="special-image-links">
							<img src="${special-image.getData()}" alt="" class="special-ad-images" />
						</a>		
					</#if>				</#foreach>			</div>
			<br style="clear: both;" />
		</div>
	<#-- End the if statement for the limnited access view.
-->	</#if>	</div>
	
	<div class="right-bar">
		<#foreach nav_app in nav_item.getChildren(permissionChecker)><#assign size = velocityCount>		</#foreach>		<#if (size>0)>		<ul class="online-apps"> 
		<#foreach nav_app in nav_item.getChildren(permissionChecker)>			<#if velocityCount == 1>				<li class="first">
			<#else>			   <li>  
			</#if>				<a href="javascript: void(0)" onClick="javascript: openPopup('${nav_app.getFriendlyURL()}', '_blank'); return false;"><span>${nav_app.getName()}</span></a>
			</li>
		</#foreach>		</ul>
		</#if>	</div>
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