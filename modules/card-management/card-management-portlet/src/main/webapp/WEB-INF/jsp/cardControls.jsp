<%@ include file="init.jsp" %>

<input id="cuscalToken" name="cuscalToken" type="hidden" value="" />
<input id="controlVals" name="controlVals" type="hidden" value="" />

<input
	id="existingControlVals"
	name="existingControlVals"
	type="hidden"
	value="${fn:escapeXml(existingControlVals)}"
/>

<input
	id="hideIfEnableds"
	name="hideIfEnableds"
	type="hidden"
	value="${fn:escapeXml(hideIfEnableds)}"
/>

<input id="ccHistoryEvents" name="ccHistoryEvents" type="hidden" value="${ccHistoryEvents}" />
<input id="ccHistoryPageIndex" name="ccHistoryPageIndex" type="hidden" value="${ccHistoryPageIndex}" />
<input id="ccHistoryMoreResults" name="ccHistoryMoreResults" type="hidden" value="${ccHistoryMoreResults}" />
<input id="toggleCardControlHistory" name="toggleCardControlHistory" type="hidden" value="false" />

<div id="cardControlsDiv"
	<c:choose>
		<c:when test="${show7}">
		style="display: block;"
		</c:when>
		<c:otherwise>
		style="display: none;"
		</c:otherwise>
	</c:choose>>

	<c:choose>
		<c:when test="${cardControlsError}">
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

			<c:if test="${showCardControlHistoryUI}">
				<br />

				<div id="cardControlHistory" class="cuscalTable"
					<c:choose>
						<c:when test="${toggleCardControlHistory}">
						style="display: block;"
						</c:when>
						<c:otherwise>
						style="display: none;"
						</c:otherwise>
					</c:choose>>
					<display:table class="cuscalTable" id="ccHistoryForm" name="ccHistoryEvents" sort="external" uid="ccHistoryForm">
						<display:setProperty name="css.th.ascending" value="ascending" />
						<display:setProperty name="css.th.descending" value="descending" />

						<display:column defaultorder="ascending" headerClass="last-modified" sortable="true" sortProperty="datetime" title="Date/Time">
							<fmt:formatDate pattern="dd-MM-yyyy" value="${ccHistoryForm.datetime}" />
							<fmt:formatDate pattern="hh:mm (a) (z)" timeZone="Australia/Sydney" value="${ccHistoryForm.datetime}" />
						</display:column>

						<display:column defaultorder="ascending" property="eventDetails" sortable="true" sortProperty="eventDetails" title="Card Control Type" />
						<display:column defaultorder="ascending" property="action" sortable="true" sortProperty="action" title="Action" />
						<display:column defaultorder="ascending" property="channel" sortable="true" sortProperty="channel" title="Channel" />
						<display:column defaultorder="ascending" property="user" sortable="true" sortProperty="user" title="User" />
					</display:table>

					<br />

					<portlet:actionURL var="showCardControlHistoryUrl">
						<portlet:param name="action" value="showCardControlHistory" />
					</portlet:actionURL>

					<div class=history-links>
						<a href="javascript:showCardControlHistory('${cardId}', '${cuscalToken}', ${ccHistoryPageIndex - 1}, '${showCardControlHistoryUrl}');"
							<c:choose>
								<c:when test="${ccHistoryPageIndex > 1}">
								style="display: inline;"
								</c:when>
								<c:otherwise>
								style="display: none;"
								</c:otherwise>
							</c:choose>>
							< Prev</a>
						<a href="javascript:showCardControlHistory('${cardId}', '${cuscalToken}', ${ccHistoryPageIndex + 1}, '${showCardControlHistoryUrl}');"
							<c:choose>
								<c:when test="${ccHistoryMoreResults && not empty ccHistoryEvents}">
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

				<div class="pan" id="cc-history-form">
					<a href="javascript:showHideCardControlHistory('${cardId}', '${cuscalToken}', '${ccHistoryPageIndex}', '${showCardControlHistoryUrl}')" id="show-hide-cc-history">
					<c:choose>
						<c:when test="${toggleCardControlHistory}">
						Hide card controls history
						</c:when>
						<c:otherwise>
						Review card controls history
						</c:otherwise>
					</c:choose>
					</a>
				</div>

				<br />
			</c:if>

			<br />

			<c:if test="${fn:length(controls) > 0}">
				<c:forEach items="${controls}" var="entry">
					<c:set value="${entry.value}" var="control" />

					<c:if test="${control.displayType eq 'UI_SWITCH'}">
						<input type="checkbox" class="uiSwitch" value="${control.controlId}" name="${control.controlId}" <c:if test="${control.currentValue eq '1'}">checked</c:if> /><label><c:out value="${control.controlText}" /></label>

						<abbr title="<c:out value="${control.controlDescription}" />">?</abbr>

						<br />
					</c:if>
				</c:forEach>

				<portlet:actionURL var="updateCardControlsUrl">
					<portlet:param name="action" value="updateCardControls" />
				</portlet:actionURL>

				<br />

				<div class="print-wrapper">
					<a class="print-button" href="javascript:updateCardControls('${cardId}', '${cuscalToken}', '${updateCardControlsUrl}');">Save</a>
				</div>
			</c:if>

			<c:if test="${fn:length(controls) == 0}">
				<p>&nbsp;There are no card controls associated with this card</p>
			</c:if>
		</c:otherwise>
	</c:choose>
</div>