

<%@ include file="init.jsp" %>

<portlet:actionURL var="showCardPage">
	<portlet:param name="action" value="cardList" />
</portlet:actionURL>

<portlet:actionURL var="updateCardStatus">
	<portlet:param name="action" value="statusUpdate" />
	<portlet:param name="cardTab" value="CardInfo" />
</portlet:actionURL>

<portlet:resourceURL escapeXml="false" id="tokenLastUpdatedByBlurb" var="tokenLastUpdatedByBlurbUrl" />

<c:if test="${empty isSuccessOrFail}">
	<script type="text/javascript">
		/* use in no conflict mode */
		jQuery.noConflict();

		Liferay.on('allPortletsReady',function() {
			var _href = removeURLParameter(location.href, "tflag");
			history.replaceState({}, "", _href + "&tflag=" + new Date().getTime());
		});
	</script>
</c:if>

<form:form
	action=""
	id="cardDetailsForm"
	method="post"
	name="cardDetailsForm"
>
	<input id="cardId" name="cardId" type="hidden" value="" />
	<input id="cuscalToken" name="cuscalToken" type="hidden" value="" />
	<input id="deviceId" name="deviceId" type="hidden" value="" />
	<input id="tokenId" name="tokenId" type="hidden" value="" />
	<input id="tokenAction" name="tokenAction" type="hidden" value="" />
	<input id="tokenLastUpdatedByBlurbUrl" name="tokenLastUpdatedByBlurbUrl" type="hidden" value="${tokenLastUpdatedByBlurbUrl}" />

	<div>
		<p>
			Card Details <br />
			<a href="javascript:backToSearchPage('${showCardPage}');" id="backToList">
				Back to Search Results
			</a>
		</p>

		<br />

		<ul class="card-details-menu">
			<%-- <li
			<c:choose>
				<c:when test="${show1}">
				class="first-tab selected"
				</c:when>
				<c:otherwise>
				class="first-tab"
				</c:otherwise>
			</c:choose>> --%>

			<c:choose>
				<c:when test="${show1}">
				<li class="first-tab selected">
					<span class="active">Card Information</span>
				</li>
				</c:when>
				<c:otherwise>
				<li class="first-tab">
					<portlet:renderURL var="showCardInfo">
						<portlet:param name="render" value="cardD" />
						<portlet:param name="cardTab" value="CardInfo" />
					</portlet:renderURL>

					<a href="javascript:openCardsDetails('${cardId}','${cuscalToken}','${showCardInfo}');">
						Card Information
					</a>
				</li>
				</c:otherwise>
			</c:choose>

			<c:if test="${showCardLimits}">
			<li <c:if test="${show2}">class="selected"</c:if>>
				<c:choose>
					<c:when test="${show2}">
					<span class="active">Card Limits</span>
					</c:when>
					<c:otherwise>
					<portlet:renderURL var="showCardLmt">
						<portlet:param name="render" value="cardD" />
						<portlet:param name="cardTab" value="CardLmt" />
					</portlet:renderURL>

					<a href="javascript:openCardsDetails('${cardId}','${cuscalToken}','${showCardLmt}');">
						Card Limits
					</a>
					</c:otherwise>
				</c:choose>
			</li>
			</c:if>

			<li <c:if test="${show3}">class="selected"</c:if>>
				<c:choose>
					<c:when test="${show3}">
					<span class="active">Account Information</span>
					</c:when>
					<c:otherwise>
					<portlet:renderURL var="showAccInfo">
						<portlet:param name="render" value="cardD" />
						<portlet:param name="cardTab" value="AccInfo" />
					</portlet:renderURL>

					<a href="javascript:openCardsDetails('${cardId}','${cuscalToken}','${showAccInfo}');">
						Account Information
					</a>
					</c:otherwise>
				</c:choose>
			</li>

			<c:if test="${showMobileDevices}">
			<li <c:if test="${show4}">class="selected"</c:if>>
				<c:choose>
					<c:when test="${show4}">
					<span class="active">Mobile Devices</span>
					</c:when>
					<c:otherwise>
					<portlet:renderURL var="showCardDevices">
						<portlet:param name="render" value="devices" />
					</portlet:renderURL>

					<a href="javascript:openCardDevices('${cardId}','${cuscalToken}','${showCardDevices}');">
						Mobile Devices
					</a>
					</c:otherwise>
				</c:choose>
			</li>
			</c:if>

			<%-- <c:if test="${showWalletDevices}"> --%>
			<li <c:if test="${show5 || show6}">class="selected"</c:if>>
				<c:choose>
					<c:when test="${show5 || show6}">
					<span class="active">Card Tokens</span>
					</c:when>
					<c:otherwise>
					<portlet:renderURL var="openTokenWallets">
						<portlet:param name="render" value="tokenWallets" />
					</portlet:renderURL>

					<a href="javascript:openTokenWallets('${cardId}','${cuscalToken}','${openTokenWallets}');">
						Card Tokens
					</a>
					</c:otherwise>
				</c:choose>
			</li>
			<%-- </c:if> --%>

			<li <c:if test="${show7}">class="selected"</c:if>>
				<portlet:renderURL var="cardControlsScreen">
					<portlet:param name="render" value="cardControls" />
				</portlet:renderURL>
			<c:choose>
				<c:when test="${show7}">
				<span class="active">Card Controls</span>
				</c:when>
				<c:otherwise>
				<a href="javascript:showCardsTab('${cardId}', '${cuscalToken}', '${cardControlsScreen}');">Card Controls</a>
				</c:otherwise>
			</c:choose>
			</li>

			<c:if test="${showMccControls}">
				<li <c:if test="${show8}">class="selected"</c:if>>
					<c:choose>
						<c:when test="${show8}">
						<span class="active">MCC Controls</span>
						</c:when>
						<c:otherwise>
						<portlet:renderURL var="showMccControls">
							<portlet:param name="render" value="mccControls" />
						</portlet:renderURL>

						<a href="javascript:showMccControls('${cardId}','${cuscalToken}','${showMccControls}');">
							MCC Controls
						</a>
						</c:otherwise>
					</c:choose>
				</li>
			</c:if>

			<c:if test="${showLimits}">
				<li <c:if test="${show9}">class="selected"</c:if>>
					<c:choose>
						<c:when test="${show9}">
						<span class="active">Limits</span>
						</c:when>
						<c:otherwise>
						<portlet:renderURL var="showCardLimits">
							<portlet:param name="render" value="cardLimits" />
						</portlet:renderURL>

						<a href="javascript:showCardLimits('${cardId}','${cuscalToken}','${showCardLimits}');">
							Limits
						</a>
						</c:otherwise>
					</c:choose>
				</li>
			</c:if>
		</ul>

		<div id="cardInfoDiv"
			<c:choose>
				<c:when test="${show1}">
				style="display: block;"
				</c:when>
				<c:otherwise>
				style="display: none;"
				</c:otherwise>
			</c:choose>>
			<c:choose>
				<c:when test="${not empty cardInfoData.pan}">
					<div class="pan">
						<p>
							PAN: <span class="bold print">${cardInfoData.maskedPan}</span>
							<span class="bold no-print">${cardInfoData.pan}</span>
						</p>
					</div>

					<div class="institution">
						<p>
							Issuer: <span class="bold">${cardInfoData.institution}</span>
						</p>
					</div>

					<table border="0" cellpadding="0" cellspacing="0" width="99%">
						<tr>
							<td class="card-titles" colspan="6">${cardInfoData.cardholderDetailsTxt}</td>
						</tr>
						<tr>
							<td colspan="6">Name: <span class="bold">${cardInfoData.cardHolder.cardHolderName}</span>
							</td>
						</tr>
						<tr>
							<td colspan="6">Address: <span class="bold">${cardInfoData.cardHolder.cardHolderAddress}</span>
							</td>
						</tr>
						<tr>
							<td colspan="6">&nbsp;</td>
						</tr>
						<tr>
							<td class="card-titles" colspan="6">
								${cardInfoData.cardDetailsTxt}
							</td>
						</tr>
						<tr>
							<td colspan="6">Card Status:
							<c:set value="${cardInfoData.cardStatusMap}" var="sizeMap" />

							<c:choose>
								<c:when test="${cardInfoData.hasCardPortedToPrimarySwitch}">
									<c:choose>
										<c:when test="${fn:length(sizeMap) == 1}">
											<span class="bold">${cardInfoData.cardStatus}</span>
											<%-- Vigil blocked icon --%>
											<c:if test="${cardInfoData.vigilBlocked}">
												<span class="vigil-logo">&nbsp;</span>
											</c:if>
										</c:when>
										<c:otherwise>
											<select id="cardAvalStatus">
												<c:forEach items="${cardInfoData.cardStatusMap}" var="cardAvalStatusOpts">
													<c:choose>
														<c:when test="${cardInfoData.cardStatus == cardAvalStatusOpts.value}">
															<option selected="selected" value="${cardAvalStatusOpts.key}">
																${cardAvalStatusOpts.value}
															</option>
														</c:when>
														<c:otherwise>
															<option value="${cardAvalStatusOpts.key}">
																${cardAvalStatusOpts.value}
															</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>

											<input id="cardNewStatus" name="cardNewStatus" type="hidden" value="" />

											<a class="update-button" href="javascript:updateCardStatus('${cardInfoData.cardId}','${cuscalToken}','${updateCardStatus}','${cardInfoData.cardStatusCode}');">
												Update
											</a>

											<%-- Vigil blocked icon --%>
											<c:if test="${cardInfoData.vigilBlocked}">
												<span class="vigil-logo">&nbsp;</span>
											</c:if>
										</c:otherwise>
									</c:choose>

									<c:if test="${noMsg}">
										<c:choose>
											<c:when test="${not empty isSuccessOrFail}">
												<c:choose>
													<c:when test="${isSuccessOrFail eq 'failed' }">
													<span class="error">
														<spring:message code="cards.status.update.error" />
													</span>
													</c:when>
													<c:otherwise>
													<span>
														<spring:message code="cards.status.update.success" /> ${isSuccessOrFail}.
													</span>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<span class="error">
													<spring:message code="cards.status.update.error" />
												</span>
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:when>
								<c:otherwise>
									<span class="bold">${cardInfoData.cardStatus}</span>

									<%-- Vigil blocked icon --%>
									<c:if test="${cardInfoData.vigilBlocked}">
										<span class="vigil-logo">&nbsp;</span>
									</c:if>

									<br />

									<span class="secondary-switch"><spring:message code="cards.secondary.switch.card.message" /></span>
								</c:otherwise>
							</c:choose>

							<c:if test="${not empty cardInfoData.cardStatusMessage}">
								<br />

								<span class="secondary-switch"><spring:message code="${cardInfoData.cardStatusMessage}" /></span>
							</c:if>
							</td>
						</tr>
						<tr>
							<td colspan="3">Expiry Date:
								<span class="bold">
									${cardInfoData.expiryDate}
								</span>
							</td>
							<td colspan="3">Last Used:
								<span class="bold">
									<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${cardInfoData.lastUsed}" />
								</span>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								PIN Count Failures:
								<span class="bold">
									${cardInfoData.pinCountFailures}
								</span>
							</td>
							<td colspan="3">
								Last Updated:
								<span class="bold">
									<fmt:formatDate pattern="dd/MM/yyyy" value="${cardInfoData.lastUpdated}" />
								</span>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								&nbsp;
							</td>
							<td colspan="3">
								Last Modified:
								<span class="bold">
									<fmt:formatDate pattern="dd/MM/yyyy" value="${cardInfoData.lastMaintained}" />
								</span>
							</td>
						</tr>

						<%-- <tr>
				<td colspan="6">&nbsp;</td>
			</tr>
			<tr>
				<td class="card-titles" colspan="6">
					${cardInfoData.visaExceptionTxt}
				</td>
			</tr>
			<tr>
				<td colspan="6">Switch Response Code: <span class="bold">${cardInfoData.switchResponseCode}</span></td>
			</tr>
			<tr>
				<td colspan="6">Response Description: <span class="bold">${cardInfoData.responseDescription}</span></td>
			</tr>
			<tr>
				<td colspan="6">Visa Reference: <span class="bold">${cardInfoData.visaReference}</span></td>
			</tr> --%>

						<tr>
							<td colspan="6">&nbsp;</td>
						</tr>

						<c:choose>
							<c:when test="${empty cardInfoData.cardChannelPermissionsMessage}">
						<!-- This is the new card access section -->
						<c:forEach items="${cardInfoData.cardPermissions}" var="cardPermission">
							<tr>
								<td class="card-titles" colspan="6">
									${cardPermission.channelPermissionName}
								</td>
							</tr>
							<tr>
								<c:forEach items="${cardPermission.accessTypes}" var="cardAccessObject" varStatus="rowCounter">
								<c:choose>
									<c:when test="${cardAccessObject.accessAvailable eq 'Y'}">
										<c:set value="Yes" var="avialableAccess" />
									</c:when>
									<c:otherwise>
										<c:set value="No" var="avialableAccess" />
									</c:otherwise>
								</c:choose>

								<c:choose>
									<c:when test="${rowCounter.count % 4 == 1}">
							</tr>
							<tr>
								<td>${cardAccessObject.accessType}:</td>
								<td class="card-access-response">${avialableAccess}</td>
								</c:when>
								<c:otherwise>
								<td>${cardAccessObject.accessType}:</td>
								<td class="card-access-response">${avialableAccess}</td>
								</c:otherwise>
								</c:choose>
								</c:forEach>
							</tr>
							<tr>
								<td colspan="6">&nbsp;</td>
							</tr>
						</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="6">
									<span class="portlet-msg-alert"><spring:message code="${cardInfoData.cardChannelPermissionsMessage}" /></span>
								</td>
							</tr>
						</c:otherwise>
						</c:choose>
					</table>

					<div class="print-wrapper">
						<a class="print-button" href="javascript:void(0)" onclick="javascript:window.print();">Print</a>
					</div>
				</c:when>
				<c:otherwise>
					<p class="error">
						<spring:message code="cards.error.msg" />
					</p>
				</c:otherwise>
			</c:choose>
		</div>

		<div id="cardLimitDiv"
			<c:choose>
				<c:when test="${show2}">
				style="display: block;"
				</c:when>
				<c:otherwise>
				style="display: none;"
				</c:otherwise>
			</c:choose>>

			<c:choose>
				<c:when test="${not empty CardLmtData.pan}">
					<div class="pan">
						<p>
							PAN: <span class="bold no-print">${CardLmtData.pan}</span><span class="bold print">${CardLmtData.maskedPan}</span>
						</p>
					</div>

					<div class="institution">
						<p>
							Issuer: <span class="bold">${CardLmtData.institution}</span>
						</p>
					</div>

					<table border="0" cellpadding="0" cellspacing="0" width="99%">
						<tr>
							<td class="card-titles">Limit Type</td>
							<td class="card-titles-right" colspan="2">
								${CardLmtData.standardOverLimistTxt}
							</td>
							<td class="card-titles-right" colspan="2">
								${CardLmtData.usageInfoTxt}
							</td>
						</tr>
						<tr>
							<td class="card-titles" style="width: 40%;"></td>
							<td class="amount bold">Amount</td>
							<td class="bold usage">Use</td>
							<td class="amount bold">Amount</td>
							<td class="bold usage">Use</td>
						</tr>

						<c:forEach
							items="${CardLmtData.standardOverrideLimits}"
							var="standardLimitObject"
							varStatus="rowCounter"
						>
							<c:choose>
								<c:when test="${rowCounter.count % 2 == 1}">
									<tr class="odd">
								</c:when>
								<c:otherwise>
									<tr class="even">
								</c:otherwise>
							</c:choose>

							<c:choose>
								<c:when test="${standardLimitObject.limitAmount ==  '-'}">
									<c:set scope="page" value="${standardLimitObject.limitAmount}" var="limitAmount" />
								</c:when>
								<c:otherwise>
									<fmt:formatNumber pattern="#,##0.00;-#,##0.00" type="currency" value="${standardLimitObject.limitAmount/100}" var="limitAmount" />
								</c:otherwise>
							</c:choose>

							<c:choose>
								<c:when test="${standardLimitObject.usageAmount ==  '-'}">
									<c:set scope="page" value="${standardLimitObject.usageAmount}" var="usageAmount" />
								</c:when>
								<c:otherwise>
									<fmt:formatNumber pattern="#,##0.00;-#,##0.00" type="currency" value="${standardLimitObject.usageAmount/100}" var="usageAmount" />
								</c:otherwise>
							</c:choose>

							<td class="bold">${standardLimitObject.limitType}</td>
							<td align="right">${limitAmount}</td>
							<td align="right">${standardLimitObject.limitCount}</td>
							<td align="right">${usageAmount}</td>
							<td align="right">${standardLimitObject.usageCount}</td>

							</tr>
						</c:forEach>
					</table>

					<div class="print-wrapper">
						<a class="print-button" href="javascript:void(0)" onclick="javascript:window.print();">Print</a>
					</div>
				</c:when>
				<c:otherwise>
					<c:choose>
					<c:when test="${noCardLimit}">
						<p class="portlet-msg-info">
						<spring:message code="card.limits.unavailable" />
						</p>
					</c:when>
					<c:otherwise>
					<p class="error">
						<spring:message code="cards.error.msg" />
					</p>
					</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>

		<div id="accountInfoDiv"
			<c:choose>
				<c:when test="${show3}">
				style="display: block;"
				</c:when>
				<c:otherwise>
				style="display: none;"
				</c:otherwise>
			</c:choose>>

			<c:choose>
				<c:when test="${not empty accInfoData}">
					<c:forEach items="${accInfoData}" var="accountInformationObject" varStatus="rowCounter">
						<c:if test="${rowCounter.count == 1}">
							<div class="pan">
								<p>
									PAN: <span class="bold no-print">${accountInformationObject.pan}</span><span class="print bold">${accountInformationObject.maskedPan}</span>
								</p>
							</div>

							<div class="institution">
								<p>
									Issuer: <span class="bold">${accountInformationObject.institution}</span>
								</p>
							</div>

							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td class="card-titles" colspan="9">${accountInformationObject.accountMapDetailsTxt}</td>
								</tr>
								<tr>
									<th>Account Type</th>
									<th>Account Number</th>
									<th class="account-qualifier">Account Qualifier</th>
									<th class="primary-qualifier">Default Account</th>
									<th class="amount">Ledger Balance</th>
									<th class="amount">Available Balance</th>
									<th class="amount">Credit Line</th>
									<%-- <th>Last Updated</th> --%>
								</tr>
								</c:if>

								<c:choose>
									<c:when test="${rowCounter.count % 2 == 0}">
										<c:set scope="page" value="even" var="rowStyle" />
									</c:when>
									<c:otherwise>
										<c:set scope="page" value="odd" var="rowStyle" />
									</c:otherwise>
								</c:choose>

								<tr class="${rowStyle}">
									<td class="bold">${accountInformationObject.accountType}</td>
									<td class="bold">${accountInformationObject.accountNumber}</td>
									<td class="account-qualifier bold">${accountInformationObject.accountQualifier}</td>
									<td class="primary-account">${accountInformationObject.primaryAccount}</td>

									<c:choose>
										<c:when test="${not empty accountInformationObject.ledgerBalance && not empty accountInformationObject.availableBalance && not empty accountInformationObject.creditLine}">
										<td class="amount">
											<c:choose>
												<c:when test="${accountInformationObject.ledgerBalance eq 'Not available'}">
													<acronym title="${accountInformationObject.ledgerBalance}">N/A</acronym>
												</c:when>
												<c:otherwise>
													<fmt:formatNumber pattern="#,##0.00;-#,##0.00" type="currency" value="${accountInformationObject.ledgerBalance/100}" />
												</c:otherwise>
											</c:choose>
										</td>
										<td class="amount">
											<c:choose>
												<c:when test="${accountInformationObject.availableBalance eq 'Not available'}">
													<acronym title="${accountInformationObject.availableBalance}">N/A</acronym>
												</c:when>
												<c:otherwise>
													<fmt:formatNumber pattern="#,##0.00;-#,##0.00" type="currency" value="${accountInformationObject.availableBalance/100}" />
												</c:otherwise>
											</c:choose>
										</td>
										<td class="amount">
											<c:choose>
												<c:when test="${accountInformationObject.creditLine eq 'Not available'}">
													<acronym title="${accountInformationObject.creditLine}">N/A</acronym>
												</c:when>
												<c:otherwise>
													<fmt:formatNumber pattern="#,##0.00;-#,##0.00" type="currency" value="${accountInformationObject.creditLine}" />
												</c:otherwise>
											</c:choose>
										</td>
										<%-- <td class="last-updated">
										<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${accountInformationObject.lastUpdated}" />
										</td> --%>
										</c:when>
										<c:otherwise>
										<td colspan="3">${accountInformationObject.msgAccountInfoTxt}</td>
										<%-- <td class="last-updated">
										<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${accountInformationObject.lastUpdated}" />
										</td> --%>
										</c:otherwise>
									</c:choose>
								</tr>
								</c:forEach>
							</table>

					<div class="print-wrapper">
						<a class="print-button" href="javascript:void(0)" onclick="javascript:window.print();">Print</a>
					</div>
				</c:when>
				<c:otherwise>
					<c:choose>
					<c:when test="${noCardInfo}">
						<p class="portlet-msg-info">
						<spring:message code="card.accounts.unavailable" />
						</p>
					</c:when>
					<c:otherwise>
					<p class="error">
						<spring:message code="cards.error.msg" />
					</p>
					</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>

		<c:if test="${showMobileDevices}">
		<!-- devices div -->
		<div id="cardDevicesDiv"
			<c:choose>
				<c:when test="${show4}">
				style="display: block;"
				</c:when>
				<c:otherwise>
				style="display: none;"
				</c:otherwise>
			</c:choose>>

			<div class="pan">
				<p>
					PAN <span class="bold print">${cardInfoData.maskedPan}</span>
					<span class="bold no-print">${cardInfoData.pan}</span>
				</p>
			</div>

			<div class="institution">
				<p>
					Issuer <span class="bold">${cardInfoData.institution}</span>
				</p>
			</div>

			<div style="display: block; clear: both; margin: 0 5px;">
				<c:out escapeXml="false" value="${requestScope['mobile.blurb']}" />
			</div>

			<div style="display: block; clear: both; margin: 0 10px 0 0;">
				<display:table class="cuscalTable" id="devices" name="devices" uid="device">
					<display:setProperty name="basic.msg.empty_list" value="&nbsp;There are no mobile devices registered to this card" />

					<display:column property="deviceType" title="Device" />
					<display:column property="lastUsedDate" title="Last Used" />

					<display:column property="statusDescription" title="Status" />

					<display:column title="">
						<c:if test="${device.status eq 'I'}">
							By <c:out value="${device.deregistrationUser}" />, <c:out value="${device.deregistrationDate}" />
						</c:if>

						<c:if test="${device.status eq 'A'}">
							<portlet:renderURL var="deRegDeviceUrl">
								<portlet:param name="render" value="dereg" />
							</portlet:renderURL>

							<a href="javascript:deregisterDevice('${cardId}', '${cuscalToken}', '${device.deviceId}','${deRegDeviceUrl}', '<c:out value="${device.deviceType}" />');">
								Deregister
							</a>
						</c:if>

						<c:if test="${device.status eq 'B'}">
							<portlet:renderURL var="deRegDeviceUrl">
								<portlet:param name="render" value="dereg" />
							</portlet:renderURL>

							<a href="javascript:deregisterDevice('${cardId}', '${cuscalToken}', '${device.deviceId}','${deRegDeviceUrl}', '<c:out value="${device.deviceType}" />');">
								Unblock
							</a>
						</c:if>
					</display:column>
				</display:table>
			</div>
		</div>
		</c:if>

		<!-- wallet div -->
		<div id="tokenWalletsDiv"
			<c:choose>
				<c:when test="${show5}">
				style="display: block;"
				</c:when>
				<c:otherwise>
				style="display: none;"
				</c:otherwise>
			</c:choose>>

			<div class="pan">
				<p>
					PAN <span class="bold print">${cardInfoData.maskedPan}</span>
					<span class="bold no-print">${cardInfoData.pan}</span>
				</p>
			</div>

			<div class="institution">
				<p>
					Issuer <span class="bold">${cardInfoData.institution}</span>
				</p>
			</div>

			<div style="display: block; clear: both; margin: 0 5px;">
				<c:out escapeXml="false" value="${requestScope['mobile.blurb']}" />
			</div>

			<c:if test="${tokenSearchPartialSuccess}">
				<p class="error">NOTE! Some schemes systems are down. We are unable to provide all details.</p>
			</c:if>

			<div style="display: block; clear: both; margin: 0 10px 0 0;">
				<display:table class="cuscalTable" id="tokens" name="tokens" uid="token">
					<display:setProperty name="basic.msg.empty_list" value="&nbsp;There are no tokens registered to this card" />

					<display:column property="maskedToken" title="Token" />
					<display:column property="deviceNumber" title="Device Number" />
					<display:column property="deviceName" title="Device Name" />

					<display:column title="Status">
						<c:if
							test="${token.status eq 'I' || token.status eq 'A' || token.status eq 'S'}"
						>
							<portlet:renderURL var="tokenStatusUrl">
								<portlet:param name="render" value="tokenStatus" />
							</portlet:renderURL>

							<c:choose>
								<c:when test="${token.auxilaryToken}">
									<a
										href="javascript:warnNoTokenStatusEdit();">${token.statusDescription}</a
									>
								</c:when>
								<c:otherwise>
									<a
										href="javascript:showTokenStatus('${cardId}', '${cuscalToken}', '${token.tokenId}','${tokenStatusUrl}');">${token.statusDescription}</a
									>
								</c:otherwise>
							</c:choose>
						</c:if>

						<c:if test="${token.status eq 'D' || token.status eq 'L'}">
									${token.statusDescription}
						</c:if>
					</display:column>

					<display:column property="flowType" title="Flow Type" />
					<display:column property="tokenRequster" title="Token Requester" />
					<display:column property="scheme" title="Scheme" />

					<display:column title="Last Updated">
						<fmt:formatDate pattern="dd/MM/yyyy HH:mm" timeZone="Australia/Sydney" value="${token.lastUpdatedTime}" />
					</display:column>

					<display:column property="lastUpdatedBy" title="Last Updated By <a href='javascript:openTokenLastUpdatedByBlurbPage();'><b>?</b></a>" />
				</display:table>
			</div>
		</div>

		<div id="tokenStatusDiv"
			<c:choose>
				<c:when test="${show6}">
				style="display: block;"
				</c:when>
				<c:otherwise>
				style="display: none;"
				</c:otherwise>
			</c:choose>>

			<div class="pan">
				<p>
					PAN <span class="bold print">${cardInfoData.maskedPan}</span>
					<span class="bold no-print">${cardInfoData.pan}</span>
				</p>
			</div>

			<div class="institution">
				<p>
					Issuer <span class="bold">${cardInfoData.institution}</span>
				</p>
			</div>

			<div style="display: block; clear: both; margin: 0 10px 0 0;">
				<display:table class="cuscalTable" id="tokens" name="tokens" uid="token">
					<display:column property="maskedToken" title="Token" />
					<display:column property="deviceNumber" title="Device Number" />
					<display:column property="deviceName" title="Device Name" />
					<display:column property="flowType" title="Flow Type" />
					<display:column property="tokenRequster" title="Token Requster" />
					<display:column property="scheme" title="Scheme" />
					<display:column property="statusDescription" title="Status" />

					<display:column title="Last Updated">
						<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${token.lastUpdatedTime}" />
					</display:column>

					<display:column property="lastUpdatedBy" title="Last Updated By" />
				</display:table>
			</div>

			<br />

			<portlet:actionURL var="activateTokenUrl">
				<portlet:param name="action" value="activateToken" />
			</portlet:actionURL>

			<portlet:actionURL var="updateTokenUrl">
				<portlet:param name="action" value="updateToken" />
			</portlet:actionURL>

			<table border="0" cellpadding="0" cellspacing="0" width="99%">
				<tr>
					<c:if test="${token.status eq 'I'}">
						<td>
							<div>
								<p class="action-title">
									Activate Token
								</p>

								<a class="clear-button" href="javascript:activateToken('${cardId}', '${cuscalToken}', '${token.tokenId}', '${token.maskedToken}','${activateTokenUrl}');">Activate</a>
							</div>
						</td>
					</c:if>

					<c:if test="${token.status eq 'A'}">
						<td>
							<div>
								<p class="action-title">
									Suspend Token
								</p>

								<p>Reason<em class="mandatory-field">*</em>
									<select id="suspendReason" name="suspendReason">
										<option value="DEVICE_LOST">Device Lost</option>
										<option value="DEVICE_STOLEN">Device Stolen</option>
										<option value="FRAUD">Fraudulent Transaction</option>
										<option value="OTHER">Other</option>
									</select>
								</p>

								<p>Comment<em class="mandatory-field">*</em></p>
								<p>
									<textarea class="comment-textarea" cols="30" maxlength="200" name="suspendComment" rows="6"></textarea>
								</p>

								<c:if test="${suspendCommentError}">
									<p>
										<span class="error" id="suspendCommentError">Please provide comment</span>
									</p>
								</c:if>

								<a class="clear-button" href="javascript:updateToken('${cardId}', '${cuscalToken}', '${token.tokenId}', 'suspend', '${token.maskedToken}','${updateTokenUrl}');">Suspend</a>
							</div>
						</td>
					</c:if>

					<c:if test="${(token.status eq 'S') && not (token.scheme eq 'VISA' && not (token.lastUpdatedBy eq 'ISSUER'))}">
						<td>
							<div>
								<p class="action-title">
									Unsuspend Token
								</p>

								<p>Reason<em class="mandatory-field">*</em>
									<select id="unsuspendReason" name="unsuspendReason">
										<option value="DEVICE_FOUND">Device Recovered/Found</option>
										<option value="NOT_FRAUD">Confirmed No Fraudulent Transaction</option>
										<option value="OTHER">Other</option>
									</select>
								</p>

								<p>Comment<em class="mandatory-field">*</em></p>
								<p>
									<textarea class="comment-textarea" cols="30" maxlength="200" name="unsuspendComment" rows="6"></textarea>
								</p>

								<c:if test="${unsuspendCommentError}">
									<p>
										<span class="error" id="unsuspendCommentError">Please provide comment</span>
									</p>
								</c:if>

								<a class="clear-button" href="javascript:updateToken('${cardId}', '${cuscalToken}', '${token.tokenId}', 'unsuspend', '${token.maskedToken}','${updateTokenUrl}');">Unsuspend</a>
							</div>
						</td>
					</c:if>

					<td>
						<div>
							<p class="action-title">
								Deactivate Token
							</p>

							<p>Reason<em class="mandatory-field">*</em>
								<select id="deleteReason" name="deleteReason">
									<option value="DEVICE_LOST">Device Lost</option>
									<option value="DEVICE_STOLEN">Device Stolen</option>
									<option value="FRAUD">Fraudulent Transaction</option>
									<option value="ACCOUNT_CLOSED">Account Closed</option>
									<option value="OTHER">Other</option>
								</select>
							</p>

							<p>Comment<em class="mandatory-field">*</em></p>
							<p>
								<textarea class="comment-textarea" cols="30" maxlength="200" name="deleteComment" rows="6"></textarea>
							</p>

							<c:if test="${deleteCommentError}">
								<p>
									<span class="error" id="deleteCommentError">Please provide comment</span>
								</p>
							</c:if>

							<a class="clear-button" href="javascript:updateToken('${cardId}', '${cuscalToken}', '${token.tokenId}', 'delete', '${token.maskedToken}','${updateTokenUrl}');">Delete</a>
						</div>
					</td>
				</tr>
			</table>
		</div>

		<%@ include file="cardControls.jsp" %>

		<%@ include file="mccControls.jsp" %>

		<%@ include file="cardLimits.jsp" %>
	</div> <%-- Close the wrapper div for the entire content. --%>
</form:form>

<div id=tokenLastUpdatedByBlurbDiv class="modal-wrapper">
	<div class="modal-header">
		<h5>Last Updated By</h5>

		<a class="modal-close" href="#">X</a>
	</div>

	<table border="0" cellpadding="0" cellspacing="0" id="tokenLastUpdatedByBlurb">
	</table>
</div>