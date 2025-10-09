<#assign layoutService = serviceLocator.findService("com.liferay.portal.service.LayoutLocalService")><#assign nav_item = layoutService.getFriendlyURLLayout(getterUtil.getLong(request.theme-display.scope-group-id), false, "/online-applications")><div id="cuscalContent">
<#if headline.getData() != ""><h1 id="contentHeading">${headline.getData()}</h1></#if><div id="rightBar">
<#if !nav_item.getChildren(permissionChecker).isEmpty()><div id="rightNav">
<div class="round"></div>
<ul> 
	<#foreach nav_app in nav_item.getChildren(permissionChecker)>	<#if velocityCount == 1>	<li class="first">
	<#else>	 <li> 
	</#if>	<a href="#" onClick="javascript:window.open('${nav_app.getFriendlyURL()}','newWindow','HEIGHT=600, WIDTH=800, TOP=0,scrollbars=1')"><span>${nav_app.getName()}</span></a>
 </li>
	</#foreach>	
</ul>
<div class="round"></div>
</div>
<#elseif image.getData() != ""><img alt="" src="${image.getData()}" /></#if></div>
<div id="contentBody">
<div id="information">
<#if content.getData() != "">${content.getData()}</#if></div>
</div>
</div>
<script type="text/javascript" defer="defer"> if(document.getElementById('contentWrapper2')!=null) document.getElementById('contentWrapper2').style.width = 790 + 'px'; 
if(document.getElementById('contentBody')!=null && document.getElementById('rightBar')!=null && document.getElementById("rightBar").innerHTML.length > 5) { 
document.getElementById('contentBody').style.width = 530 + 'px'; 
} else if(document.getElementById("rightBar").innerHTML.length < 5) { 
document.getElementById('contentBody').style.marginRight = 15 + 'px';
}
</script><br />

