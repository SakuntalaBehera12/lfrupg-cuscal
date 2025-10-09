<input id="cuscalToken" name="cuscalToken" type="hidden" value="" />
<input id="mccControlVals" name="mccControlVals" type="hidden" value="" />
<input id="mccCheckedTotal" name="mccCheckedTotal" type="hidden" value="${mccCheckedTotal}" />
<input id="mccTotal" name="mccTotal" type="hidden" value="${mccTotal}" />
<input id="mccHistoryEvents" name="mccHistoryEvents" type="hidden" value="${mccHistoryEvents}" />
<input id="mccHistoryPageIndex" name="mccHistoryPageIndex" type="hidden" value="${mccHistoryPageIndex}" />
<input id="mccHistoryMoreResults" name="mccHistoryMoreResults" type="hidden" value="${mccHistoryMoreResults}" />
<input id="toggleMccHistory" name="toggleMccHistory" type="hidden" value="false" />

<div id="mccControlsDiv"
	<c:choose>
		<c:when test="${show8}">
		style="display: block;"
		</c:when>
		<c:otherwise>
		style="display: none;"
		</c:otherwise>
	</c:choose>>

	<c:choose>
		<c:when test="${mccControlsError}">
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

			<c:if test="${showMccControlHistoryUI}">
				<br />

				<div id="mccHistory" class="cuscalTable"
					<c:choose>
						<c:when test="${toggleMccHistory}">
						style="display: block;"
						</c:when>
						<c:otherwise>
						style="display: none;"
						</c:otherwise>
					</c:choose>>
					<display:table class="cuscalTable" id="mccHistoryForm" name="mccHistoryEvents" sort="external" uid="mccHistoryForm">
						<display:setProperty name="css.th.ascending" value="ascending" />
						<display:setProperty name="css.th.descending" value="descending" />

						<display:column defaultorder="ascending" headerClass="last-modified" sortable="true" sortProperty="datetime" title="Date/Time">
							<fmt:formatDate pattern="dd-MM-yyyy" value="${mccHistoryForm.datetime}" /> <br />
							<fmt:formatDate pattern="hh:mm (a) (z)" timeZone="Australia/Sydney" value="${mccHistoryForm.datetime}" />
						</display:column>

						<display:column defaultorder="ascending" property="eventDetails" sortable="true" sortProperty="eventDetails" title="MCC Blocking Type" />
						<display:column defaultorder="ascending" property="action" sortable="true" sortProperty="action" title="Action" />
						<display:column defaultorder="ascending" property="channel" sortable="true" sortProperty="channel" title="Channel" />
						<display:column defaultorder="ascending" property="user" sortable="true" sortProperty="user" title="User" />
					</display:table>

					<br />

					<portlet:actionURL var="showMccHistoryUrl">
						<portlet:param name="action" value="showMccHistory" />
					</portlet:actionURL>

					<div class=history-links>
						<a href="javascript:showMccHistory('${cardId}', '${cuscalToken}', ${mccHistoryPageIndex - 1}, '${showMccHistoryUrl}');"
							<c:choose>
								<c:when test="${mccHistoryPageIndex > 1}">
								style="display: inline;"
								</c:when>
								<c:otherwise>
								style="display: none;"
								</c:otherwise>
							</c:choose>>
							< Prev</a>
						<a href="javascript:showMccHistory('${cardId}', '${cuscalToken}', ${mccHistoryPageIndex + 1}, '${showMccHistoryUrl}');"
							<c:choose>
								<c:when test="${mccHistoryMoreResults && not empty mccHistoryEvents}">
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

				<div class="pan" id="mcc-history-form">
					<a href="javascript:showHideMccHistory('${cardId}', '${cuscalToken}', '${mccHistoryPageIndex}', '${showMccHistoryUrl}')" id="show-hide-mcc-history">
					<c:choose>
						<c:when test="${toggleMccHistory}">
						Hide MCC blocking history
						</c:when>
						<c:otherwise>
						Review MCC blocking history
						</c:otherwise>
					</c:choose>
					</a>
				</div>

				<br />
			</c:if>

			<br />

			<c:if test="${fn:length(mccControls) > 0}">
				<p>
					<input type="checkbox" value="-1" name="mccBlockAll" <c:if test="${mccCheckedTotal == mccTotal}">checked</c:if> /><label>Block All Merchant Categories</label>

					<br />
				</p>

				<c:forEach items="${mccControls}" var="mccControl">
					<p>
					<input type="checkbox" value="${mccControl.id}" name="mcc" <c:if test="${mccControl.value eq '1'}">checked</c:if> /><label><c:out value="${mccControl.name}" /><br /><span><c:out value="${mccControl.description}" /></span></label>

					</p>
				</c:forEach>

				<portlet:actionURL var="updateMccControlsUrl">
					<portlet:param name="action" value="updateMccControls" />
				</portlet:actionURL>

				<br />

				<div class="print-wrapper">
					<a class="print-button" href="javascript:updateMccControls('${cardId}', '${cuscalToken}', '${updateMccControlsUrl}');">Save</a>
				</div>
			</c:if>

			<c:if test="${fn:length(mccControls) == 0}">
				<p>&nbsp;There are no mcc controls associated with this card</p>
			</c:if>
		</c:otherwise>
	</c:choose>
</div>