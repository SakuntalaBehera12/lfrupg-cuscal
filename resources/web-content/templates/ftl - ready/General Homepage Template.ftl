<script type="text/javascript" src="/o/cuscal-theme/js/jquery.min.js">
</script>
<script type="text/javascript" src="/o/cuscal-theme/js/jquery.cycle.all.latest.js">
</script>
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
	
	jQuery("div#fader a").click(function() {
		if (jQuery(this).attr("rel") != "play") {
			jQuery(".fader").cycle("pause");
			jQuery("a#faderPlayPause").attr("rel", "play");
			jQuery("a#faderPlayPause").removeClass("pause");
			jQuery("a#faderPlayPause").addClass("play");
		} else if(jQuery(this).attr("rel") == "play") {
			jQuery(".fader").cycle("resume");
			jQuery("a#faderPlayPause").attr("rel", "pause");
			jQuery("a#faderPlayPause").removeClass("play");
			jQuery("a#faderPlayPause").addClass("pause");
		}
		return false;
	});
});
</script>

<style>
.special-ad-images.last {
    margin-right: 0;
}
</style>


<div class="fader-container">
	<div id="fader" class="link_number">
		<div class="fader-nav"></div>
		<div class="fader-nav-background">
			<a href="#" id="faderPlayPause" class="pause" rel="pause"></a>
		</div>					
	</div>
<div class="fader">
<#list casestudiescontent.getSiblings() as casestudy >
<#list casestudy.getChildren() as casestudycontent>
<#if casestudycontent?index== 0>
<#assign casestudyimage = casestudycontent.getData()>
<#else>
<#assign casestudylink = casestudycontent.getData()>
</#if>
</#list><div style="width: 100%;">
<a href="${casestudylink}">
<img rel="${casestudyimage}" src="${casestudyimage}" />
</a>
<div class="case-study-content">
${casestudy.getData()}
</div>
</div>
</#list></div>

<div id="features-div">
      <div id="features">
            <ul>
                   <li class="content"><h2>${featuredcontentheading1.getData()}</h2>
<#foreach featuredcontent in featuredcontentheading1.getChildren()>    <#if featuredcontent? index == 0>        <p>${featuredcontent.getData()}</p>
    <#else>        <a href="${featuredcontent.getData()}">&nbsp;</a>      
    </#if></#foreach></li>
                  <li class="content"><h2>${featuredcontentheading2.getData()}</h2>
<#foreach featuredcontent in featuredcontentheading2.getChildren()>    <#if featuredcontent? index == 0>        <p>${featuredcontent.getData()}</p>
    <#else>        <a href="${featuredcontent.getData()}">&nbsp;</a>      
    </#if></#foreach></li>
                  <li class="content"><h2>${featuredcontentheading3.getData()}</h2>
<#foreach featuredcontent in featuredcontentheading3.getChildren()>    <#if featuredcontent? index == 0>        <p>${featuredcontent.getData()}</p>
    <#else>        <a href="${featuredcontent.getData()}">&nbsp;</a>      
    </#if></#foreach></li>
                  <li class="content last"><h2>${featuredcontentheading4.getData()}</h2>
<#foreach featuredcontent in featuredcontentheading4.getChildren()>    <#if featuredcontent? index == 0>        <p>${featuredcontent.getData()}</p>
    <#else>        <a href="${featuredcontent.getData()}">&nbsp;</a>      
    </#if></#foreach></li>
            </ul>
<br />
      </div>
</div>
</div>

<div class="latest-news">
    <runtime-portlet name="${latestnewsitemid.getData()}" />
	<#foreach readmorecontent in latestnewsitemid.getChildren()>	
	<#if readmorecontent? index == 0>
	<#assign readmoretext = readmorecontent.getData()>		
	<#else>
	<#assign readmorelink = readmorecontent.getData()>		
	</#if>	
	</#foreach>	
	<a href="${readmorelink}" class="read-more">${readmoretext}</a>
</div>

<div class="special-ad-images-wrapper">
<#list bannerimages.getSiblings() as specialimage >
<#list specialimage.getChildren() as specialimagelink>
<#assign imagelink = specialimagelink.getData()>
</#list>

<#if specialimage?index == 2><a href="${imagelink}" class="special-image-links">

<img src="${specialimage.getData()}" alt="" class="special-ad-images last"/>
</a>

<#else><a href="${imagelink}" class="special-image-links">
<img src="${specialimage.getData()}" alt="" class="special-ad-images"/>
</a>
</#if>
</#list></div>