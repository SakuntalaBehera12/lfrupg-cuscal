<script type="text/javascript" src="/o/cuscal-theme/js/jquery.min.js">
</script>
<script type="text/javascript" src="/o/cuscal-theme/js/jquery.cycle.all.latest.js">
</script>
<script type="text/javascript">
$(document).ready(function() {
	document.getElementById('backgrounddiv').display = 'absolute';
			
	//For the fader
	$(".fader").cycle({
		fx: 'fade',  //Try fade, scrollLeft, scrollRight, scrollUp, scrollDown, cover
		speed: 'slow',
		timeout: 5000,
		pause: 1
	});
});
</script>
<#assign
	assetTagLocalService = serviceLocator.findService("com.liferay.asset.kernel.service.AssetTagLocalService")
	searchTag = "breaking news"
	journalArticleLocalService = serviceLocator.findService("com.liferay.journal.service.JournalArticleLocalService")
	articles = journalArticleLocalService.getArticles(getterUtil.getLong(groupId))
	assetTag = assetTagLocalService.getTag(getterUtil.getLong(groupId), searchTag)
/>

<#if (assetTag.getAssetCount()>0)>
	<#assign breakingNews = "available">
</#if>
<div id="contentWrapperHome">
	<div id="home">
		<div id="faderDiv">
			<div id="fader" class="fader">
				<#foreach banner in bannerImage.getSiblings()>
					<#if velocityCount == 1>
						<div id="backgrounddiv">
							<img src="${banner.getData()}">
					<#else>
						<div id="backgrounddiv" style="display:none">
							<img src="${banner.getData()}">
					</#if>
							<p>
								<h1>
								<div id="homeBgHeader">
									${heading.getData()}
								</div>
								<div id="homeBgHeaderContent">
									${mainContent.getData()}
								</div>
								</h1>
							</p>
						</div>
				</#foreach>
			</div>
			<div id="featuresDiv">
				<div id="features">
					<ul>
						<#if featuredContent1.getData() != "">
							<li>
								<div class="content">
									${featuredContent1.getData()}
								</div>
							</li>
						</#if>
						<#if featuredContent2.getData() != "">
							<li>
								<div class="content">
									${featuredContent2.getData()}
								</div>
							</li>
						</#if>
						<#if featuredContent3.getData() != "">
							<li>
								<div class="content">
									${featuredContent3.getData()}
								</div>
							</li>
						</#if>
						<#if featuredContent4.getData() != "">
							<li>
								<div class="content">
									${featuredContent4.getData()}
								</div>
							</li>
						</#if>
					</ul>
				</div>
			</div>
		</div>
		<div id="breakingNewsDiv">
			<#if breakingNews == "available">
				<div id="breakingNews">
					<runtime-portlet name="${brNewsid.getData()}" />
				</div>
				<div id="NewsSection">
					<h2>
					${newsHeading.getData()}
					</h2>
					<div id="newsItems4" >
						<runtime-portlet name="${newsid1.getData()}" />
					</div>
				</div>
			<#else>
				<h2>
				${newsHeading.getData()}
				</h2>
				<div id="newsItems6">
					<runtime-portlet name="${newsid2.getData()}" />
				</div>
			</#if>
		</div>
	</div>
</div>
<script>
	window.onload = function () {
		if(document.getElementsByTagName("html")[0].className.indexOf("firefox") == -1) {
			if(document.getElementById('primaryNav')!=null) {
				document.getElementById('primaryNav').style.marginBottom = 0 +'px'; 
			}
		} else {
			if(document.getElementById('contentWrapper2') != null)
				document.getElementById('contentWrapper2').style.marginTop = -30 +'px';
		}
	};
</script>