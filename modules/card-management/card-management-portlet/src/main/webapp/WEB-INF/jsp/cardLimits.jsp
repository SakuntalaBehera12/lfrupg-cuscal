<portlet:resourceURL escapeXml="false" id="getCardSpend" var="getCardSpendUrl" />

<input id="cuscalToken" name="cuscalToken" type="hidden" value="" />
<input id="cardLimitId" name="cardLimitId" type="hidden" value="" />
<input id="cardLimitsVals" name="cardLimitsVals" type="hidden" value="" />

<input id="cardLimitId" name="cardLimitId" type="hidden" value="" />
<input id="getCardSpendUrl" name="getCardSpendUrl" type="hidden" value="${getCardSpendUrl}" />
<input id="limitsHistoryEvents" name="limitsHistoryEvents" type="hidden" value="${limitsHistoryEvents}" />
<input id="limitsHistoryPageIndex" name="limitsHistoryPageIndex" type="hidden" value="${limitsHistoryPageIndex}" />
<input id="limitsHistoryMoreResults" name="limitsHistoryMoreResults" type="hidden" value="${limitsHistoryMoreResults}" />
<input id="toggleLimitsHistory" name="toggleLimitsHistory" type="hidden" value="false" />

<div id="cardLimitsDiv"
	<c:choose>
		<c:when test="${show9}">
		style="display: block;"
		</c:when>
		<c:otherwise>
		style="display: none;"
		</c:otherwise>
	</c:choose>>

	<c:choose>
		<c:when test="${cardLimitsError}">
			<p class="error"><spring:message code="cards.error.msg" /></p>
		</c:when>
		<c:otherwise>
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

			<c:if test="${showCardLimitsHistoryUI}">
				<br />

				<div id="limitsHistory" class="cuscalTable"
					<c:choose>
						<c:when test="${toggleLimitsHistory}">
						style="display: block;"
						</c:when>
						<c:otherwise>
						style="display: none;"
						</c:otherwise>
					</c:choose>>
					<display:table class="cuscalTable" id="limitsHistoryForm" name="limitsHistoryEvents" sort="external" uid="limitsHistoryForm">
						<display:setProperty name="css.th.ascending" value="ascending" />
						<display:setProperty name="css.th.descending" value="descending" />

						<display:column defaultorder="ascending" headerClass="last-modified" sortable="true" sortProperty="datetime" title="Date/Time">
							<fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss z" value="${limitsHistoryForm.datetime}" />
						</display:column>

						<display:column defaultorder="ascending" property="eventDetails" sortable="true" sortProperty="eventDetails" title="Limit Type" />
						<display:column defaultorder="ascending" property="action" sortable="true" sortProperty="action" title="Action" />
						<display:column defaultorder="ascending" property="oldLimit" sortable="true" sortProperty="oldLimit" title="Old Limit" />
						<display:column defaultorder="ascending" property="newLimit" sortable="true" sortProperty="newLimit" title="New Limit" />
						<display:column defaultorder="ascending" property="channel" sortable="true" sortProperty="channel" title="Channel" />
						<display:column defaultorder="ascending" property="user" sortable="true" sortProperty="user" title="User" />
					</display:table>

					<br />

					<portlet:actionURL var="showLimitsHistoryUrl">
						<portlet:param name="action" value="showLimitsHistory" />
					</portlet:actionURL>

					<div class=history-links>
						<a href="javascript:showLimitsHistory('${cardId}', '${cuscalToken}', ${limitsHistoryPageIndex - 1}, '${showLimitsHistoryUrl}');"
							<c:choose>
								<c:when test="${limitsHistoryPageIndex > 1}">
								style="display: inline;"
								</c:when>
								<c:otherwise>
								style="display: none;"
								</c:otherwise>
							</c:choose>>
							< Prev</a>
						<a href="javascript:showLimitsHistory('${cardId}', '${cuscalToken}', ${limitsHistoryPageIndex + 1}, '${showLimitsHistoryUrl}');"
							<c:choose>
								<c:when test="${limitsHistoryMoreResults && not empty limitsHistoryEvents}">
								style="display: inline;"
								</c:when>
								<c:otherwise>
								style="display: none;"
								</c:otherwise>
							</c:choose>>
							Next ></a>
					</div>
				</div>

				<br />

				<div class="pan" id="limits-history-form">
					<a href="javascript:showHideLimitsHistory('${cardId}', '${cuscalToken}', '${limitsHistoryPageIndex}', '${showLimitsHistoryUrl}')" id="show-hide-limits-history">
					<c:choose>
						<c:when test="${toggleLimitsHistory}">
						Hide limits history
						</c:when>
						<c:otherwise>
						Review limits history
						</c:otherwise>
					</c:choose>
					</a>
				</div>

				<br />
			</c:if>

			<br />

			<c:if test="${fn:length(cardLimits) > 0}">
				<table style="width: 100%;">
				<tr>
					<th>Limit Type</th>
					<th>Limit Status</th>
					<th>Current Limit</th>
					<th>Amount Spent</th>
					<th>Action</th>
					<th>New Limit</th>
					<th></th>
				</tr>

				<c:forEach items="${cardLimits}" var="cardLimit">
					<tr>
						<td>
							<div class="entry"><c:out value="${cardLimit.name}" /></div>
							<div class="subEntry">Min amount is $<c:out value="${cardLimit.minLimit}" /></div>
							<input id="minLimit${cardLimit.id}" type="hidden" value="${cardLimit.minLimit}" />
							<div class="subEntry">Max amount is $<c:out value="${cardLimit.maxLimit}" /></div>
							<input id="maxLimit${cardLimit.id}" type="hidden" value="${cardLimit.maxLimit}" />
						</td>
						<td>
							<c:out value="${cardLimit.status}" />
						</td>
						<td>
							<c:out value="${cardLimit.limit}" />
							<input id="currentLimit${cardLimit.id}" type="hidden" value="${cardLimit.limit}" />
						</td>
						<td>
							<c:out value="${cardLimit.spend}" />
						</td>
						<td>
							<select id="limitAction${cardLimit.id}" name="limitAction" onchange="changeLimitAction(${cardLimit.id});">
								<option value="-1">Select</option>

								<c:forEach items="${cardLimit.limitActionMap}" var="cardLimitOpts">
								<option value="${cardLimitOpts.value}"><c:out value="${cardLimitOpts.key}" /></option>
								</c:forEach>
							</select>
						</td>
						<td>
							<input type="text" id="newLimit${cardLimit.id}" placeholder="$ Enter Amount" disabled />

							<br />

							<form:errors cssClass="error" path="newLimit" />
						</td>
						<td>
							<portlet:actionURL var="updateCardLimitsUrl">
								<portlet:param name="action" value="updateCardLimits" />
							</portlet:actionURL>

							<div class="print-wrapper">
								<a class="print-button" href="javascript:updateCardLimits('${cardId}', '${cuscalToken}', '${updateCardLimitsUrl}', '${cardLimit.id}');">Save</a>
							</div>
						</td>
					</tr>
				</c:forEach>
				</table>
			</c:if>

			<c:if test="${fn:length(cardLimits) == 0}">
				<p>&nbsp;There are no card limits associated with this card</p>
			</c:if>
		</c:otherwise>
	</c:choose>
</div>