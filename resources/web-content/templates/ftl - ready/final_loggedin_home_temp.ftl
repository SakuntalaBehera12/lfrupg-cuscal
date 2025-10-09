<#assign
	layoutService = serviceLocator.findService("com.liferay.portal.kernel.service.LayoutLocalService")
	navItem = layoutService.getFriendlyURLLayout(getterUtil.getLong(groupId),false,"/online-applications")
/>
<div id="homeLoggedinContent">
	<div style="width: 750px; float: left;">
		<div id="detailedNews">
			<table id="detailedNewsTable">
				<tr>
					<td>
						<h1 id="contentHeading">${header.getData()}</h1>
					</td>
				</tr>
				<tr>
					<td>
						<table id="lgdInHomePaddedContent">
							<tr>
								<td>
									<table>
										<tr>
											<td id="loggedinHomeNewsHeading" colspan="3">
												<strong>${newsHeading.getData()}</strong>
											</td>
										</tr>
										<tr>
											<td colspan="3">
												${newsContent.getData()}
											</td>
										</tr>
										<tr>
											<td colspan="3">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td id="loggedInTD">
												<div class="c1"> </div><div class="c2"> </div><div class="c3"> </div><div class="c4"> </div>
												<div id="loggedinHomeFeature">
													<table id="loggedinHomeFeaturePadded">
														<tr id="loggedinHomeFeatureHeading">
															<td colspan="2">
																<strong>${stopPressheader1.getData()}</strong>
															</td>
														</tr>
														<tr id="loggedinHomeFeatureContent">
															<td>
																${stopPressFeature1.getData()}
															</td>
															<td rowspan="3" id="loggedinHomeFeatureImage">
																<#if stopPressImage1.getData() != "">
																	<img alt="" src="${stopPressImage1.getData()}" />
																</#if>
															</td>
														</tr>
														<tr>
															<td>
																<span style="font-size: x-small">&nbsp;</span>
															</td>
														</tr>
														<tr>
															<td>
																<a href="${stopPressLink1.getData()}"><IMG alt=more src="${moreImage.getData()}"></a>
															</td>
														</tr>
													</table>
												</div>
												<div class="c8"> </div><div class="c7"> </div><div class="c6"> </div><div class="c5"> </div>
											</td>
											<td id="loggedinHomeFeatureSpacer">&nbsp;</td>
											<td id="loggedInTD">
												<div class="c1"> </div><div class="c2"> </div><div class="c3"> </div><div class="c4"> </div>
												<div id="loggedinHomeFeature">
													<table id="loggedinHomeFeaturePadded">
														<tr id="loggedinHomeFeatureHeading">
															<td colspan="2">
																<strong>${stopPressheader2.getData()}</strong>
															</td>
														</tr>
														<tr id="loggedinHomeFeatureContent">
															<td>
																${stopPressFeature2.getData()}
															</td>
															<td rowspan="3" id="loggedinHomeFeatureImage">
																<#if stopPressImage2.getData() != "">
																	<img alt="" src="${stopPressImage2.getData()}" />
																</#if>
															</td>
														</tr>
														<tr>
															<td>
																<span style="font-size: x-small">&nbsp;</span>
															</td>
														</tr>
														<tr>
															<td>
																<a href="${stopPressLink2.getData()}"><IMG alt=more src="${moreImage.getData()}"></a>
															</td>
														</tr>
													</table>
												</div>
												<div class="c8"> </div><div class="c7"> </div><div class="c6"> </div><div class="c5"> </div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<div class="b4"> </div><div class="b3"> </div><div class="b2"> </div><div class="b1"> </div>
		</div>
		<div id="newsWrapper" >
			<div class="b1"> </div><div class="b2"> </div><div class="b3"> </div><div class="b4"> </div>
			<div id="newsContent">
				<h2>${newsHeader.getData()}</h2>
				<div >
					<runtime-portlet name="${featureNewsid.getData()}" />
				</div>
				<div style="width:717px; margin-left: 15px; overflow: hidden; height:210px;">
					
					<runtime-portlet name="${blurbNewsid.getData()}" />
					
				</div>
			</div>
		</div>
	</div>
	<div id="rightBar">
		<#if navItem.getChildren(permissionChecker)?has_content>
            <div id="rightNav">
                <div class="round"></div>
                <ul>
                    <#list navItem.getChildren(permissionChecker) as navApp>
                        <#if navApp?index==1>
                            <li class="first">
                            <#else>
                            <li>
                        </#if>
                                <a href="#" onClick="javascript:window.open('${navApp.getFriendlyURL()}','newWindow','HEIGHT=600, WIDTH=800, TOP=0,scrollbars=1')">
                                    <span>${navApp.getName()}</span>
                                </a>
                            </li>
                    </#list>
                </ul>
                <div class="round"></div>
            </div>
		</#if>
	</div>
</div>
<script>
	if(document.getElementById('contentWrapper2')!=null) {
		document.getElementById('contentWrapper2').style.width = 966 + 'px';
	}
</script>