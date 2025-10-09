<div id="cuscalContent">
<#if headline.getData() != ""><h1 id="contentHeading">${headline.getData()}</h1></#if><div id="contentBody">
<div id="information">
<#if content.getData() != "">${content.getData()}</#if></div>
</div>
</div>
<script> if(document.getElementById('contentWrapper2')!=null) document.getElementById('contentWrapper2').style.width = 790 + 'px'; 
if(document.getElementById('contentBody')!=null) { document.getElementById('contentBody').style.width = 530 + 'px'; 
document.getElementById('contentBody').style.textAlign = "justify";
}
</script><br />