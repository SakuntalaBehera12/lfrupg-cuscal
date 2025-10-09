<#assign layoutService = serviceLocator.findService("com.liferay.portal.service.LayoutLocalService")><div id="cuscalContent">
<#if headline.getData() != ""><h1 id="contentHeading">${headline.getData()}</h1></#if><div id="rightBar">
<#if image.getData() != ""><img alt="" src="${image.getData()}" /></#if></div>
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
</script>
<br />