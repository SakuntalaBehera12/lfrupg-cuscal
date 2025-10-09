<script src="/html/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/CuscalTheme-theme/js/jquery.cycle.all.latest.js"></script>
<script type="text/javascript">
jQuery.noConflict();
function removeNotification(divId, url, contentId) {
	jQuery.ajax({
		type: "post",
		url: url,
		data: {
			webContentId: contentId
		},
		success: function() {
			jQuery("#" + divId).hide();
		}
	});
}

jQuery(document).ready(function() {
	jQuery(".fader").cycle({
		fx: 'fade',
		speed: 'slow',
		timeout: 5000,
		pager: '.fader-nav',
		pause: 1
	});
	
	jQuery("div<#call fader> a").click(function() {
		if (jQuery(this).attr("rel") != "play") {
			jQuery(".fader").cycle("pause");
			jQuery("a<#call faderPlayPause>").attr("rel", "play");
			jQuery("a<#call faderPlayPause>").removeClass("pause");
			jQuery("a<#call faderPlayPause>").addClass("play");
		} else if(jQuery(this).attr("rel") == "play") {
			jQuery(".fader").cycle("resume");
			jQuery("a<#call faderPlayPause>").attr("rel", "pause");
			jQuery("a<#call faderPlayPause>").removeClass("play");
			jQuery("a<#call faderPlayPause>").addClass("pause");
		}
		return false;
	});
});
</script>
<div class="fader-container">
	<div id="fader" class="link_number">
		<div class="fader-nav"></div>
		<div class="fader-nav-background">
			<a href="#" id="faderPlayPause" class="pause" rel="pause"></a>
		</div>					
	</div>
<div class="fader">
<#foreach case-study in casestudiescontent.getSiblings()><#foreach case-study-content in case-study.getChildren()><#if velocityCount == 1><#assign case-study-image = case-study-content.getData()><#else><#assign case-study-link = case-study-content.getData()></#if></#foreach><div>
<a href="${case-study-link}">
<img rel="${case-study_index}" src="${case-study-image}" />
</a>
<div class="case-study-content">
${case-study.getData()}
</div>
</div>
</#foreach></div>

<div id="features-div">
      <div id="features">
            <ul>
                   <li class="content"><h2>${featuredcontentheading1.getData()}</h2>
<#foreach featured-content in featuredcontentheading1.getChildren()>    <#if velocityCount == 1>        <p>${featured-content.getData()}</p>
    <#else>        <a href="${featured-content.getData()}">&nbsp;</a>      
    </#if></#foreach></li>
                  <li class="content"><h2>${featuredcontentheading2.getData()}</h2>
<#foreach featured-content in featuredcontentheading2.getChildren()>    <#if velocityCount == 1>        <p>${featured-content.getData()}</p>
    <#else>        <a href="${featured-content.getData()}">&nbsp;</a>      
    </#if></#foreach></li>
                  <li class="content"><h2>${featuredcontentheading3.getData()}</h2>
<#foreach featured-content in featuredcontentheading3.getChildren()>    <#if velocityCount == 1>        <p>${featured-content.getData()}</p>
    <#else>        <a href="${featured-content.getData()}">&nbsp;</a>      
    </#if></#foreach></li>
                  <li class="content last"><h2>${featuredcontentheading4.getData()}</h2>
<#foreach featured-content in featuredcontentheading4.getChildren()>    <#if velocityCount == 1>        <p>${featured-content.getData()}</p>
    <#else>        <a href="${featured-content.getData()}">&nbsp;</a>      
    </#if></#foreach></li>
            </ul>
<br />
      </div>
</div>
</div>

<div class="latest-news">
    <runtime-portlet name="${latestnewsitemid.getData()}" />
	<#foreach read-more-content in latestnewsitemid.getChildren()>		<#if velocityCount == 1><#assign readmoretext = read-more-content.getData()>		<#else><#assign readmorelink = read-more-content.getData()>		</#if>	</#foreach>	<a href="${readmorelink}" class="read-more">${readmoretext}</a>
</div>

<div class="special-ad-images-wrapper">
<#foreach special-image in bannerimages.getSiblings()><#foreach special-image-link in special-image.getChildren()><#assign image-link = special-image-link.getData()></#foreach><#if mathTool.mod(velocityCount, 3) == 0><a href="${image-link}" class="special-image-links">
<img src="${special-image.getData()}" alt="" class="special-ad-images last"/>
</a>
<br />
<#else><a href="${image-link}" class="special-image-links">
<img src="${special-image.getData()}" alt="" class="special-ad-images"/>
</a>
</#if></#foreach></div>