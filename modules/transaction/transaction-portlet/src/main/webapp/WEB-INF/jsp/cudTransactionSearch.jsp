<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@
taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@
taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %><%@
taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<%----%>
<%@ page contentType="text/html" isELIgnored="false" %>
<portlet:defineObjects />

<c:if test="${fromFA==true}">
	<portlet:resourceURL escapeXml="false" id="cudDetail" var="faTxDetail" />
</c:if>

<c:set var="ns"><portlet:namespace /></c:set>

<portlet:actionURL var="show2FaTxDet">
	<portlet:param name="action" value="cud2FA" />
</portlet:actionURL>

<portlet:resourceURL escapeXml="false" id="cudNext" var="nextCudDetails" />

<portlet:resourceURL escapeXml="false" id="cudPrev" var="prevCudDetails" />

<portlet:resourceURL escapeXml="false" id="cudExportxls" var="exportCudLst" />

<portlet:resourceURL escapeXml="false" id="cudPrint" var="printCudDetails" />
<portlet:resourceURL escapeXml="false" id="cudDetail" var="showCudDetails" />

<portlet:renderURL escapeXml="false" var="clearVal">
	<portlet:param name="render" value="cudClear" />
</portlet:renderURL>

<portlet:renderURL escapeXml="false" var="purge">
	<portlet:param name="render" value="cudPurge" />
</portlet:renderURL>

<portlet:renderURL escapeXml="false" var="logPool">
	<portlet:param name="render" value="cudLogPool" />
</portlet:renderURL>

<%
String steppedUp = "";

if (session.getAttribute("_USER_STEPPEDUP") != null) {
	steppedUp = (String)session.getAttribute("_USER_STEPPEDUP");
}
%>

<script charset="utf-8" type="text/javascript">

	var ifFaPage = "<c:out value="${fromFA}" />";
	if (ifFaPage == "true") {
		Liferay.on('allPortletsReady', function() {
			var url = "<c:out value="${faTxDetail}" />";
			var cudTxId="<c:out value="${cudTxId}" />";
			var cudEtlDate="<c:out value="${cudEtlDate}" />";
			var cudAdPank="<c:out value="${cudAdPank}" />";
			var adTransactionDetailK="<c:out value="${adTransactionDetailK}" />";
			var cudCurrRowNum="<c:out value="${cudCurrRowNum}" />";

			var urlFinal = replaceAll(url, "&amp;", "&");
			openCudTransactionDetailsJsonObj(urlFinal,cudTxId,cudEtlDate,cudAdPank,cudCurrRowNum,adTransactionDetailK);
		});
	}

	function createPanHtml(data) {

		var panUrl = "<c:out value="${show2FaTxDet}" />";

		var panUrlFinal = replaceAll(panUrl, "&amp;", "&");

		var isStepUp = <%= "true".equals(steppedUp) %>;

		if (isStepUp) {
			jQuery('#pan').html(data.requestDetails.pan);
		} else {
			var isFaLink = "<c:out value="${cudIsFa}" />";
			if(isFaLink == "true") {
				jQuery("#showPan").html("&nbsp;<a href=\"javascript:open2FAPageCudDet('" + panUrlFinal
					+ "');\" class='normal-link'>View Full PAN</a>");
				jQuery("#pan").html(data.requestDetails.maskedPan);
			} else {
				jQuery('#pan').html(data.requestDetails.maskedPan);
			}
		}
		jQuery(".expiry-date").before("&nbsp;&nbsp;");
	}

</script>

<portlet:actionURL var="showCudList">
	<portlet:param name="action" value="cudList" />
</portlet:actionURL>

<portlet:renderURL escapeXml="false" var="renderURL" />

<form:form action="${showCudList}" autocomplete="off" id="transactionCudForm" method="post" modelAttribute="transactionForm" name="transactionCudForm">
		<div>
			<c:choose>
			<c:when test="${not empty cudList}">
			<div id="transactionCudSearch" style="display: none;">
				</c:when>
				<c:otherwise>
				<div id="transactionCudSearch" style="display: block;">
					</c:otherwise>
					</c:choose>

					<div>
						<fieldset>
							<legend>Transaction Details</legend>

							<div>
								<!-- Change this label backto 'PAN/BIN' if we ever make BIN search available again. -->
								<label for="panBin">PAN<em>*</em></label>

								<form:input autocomplete="off" id="panBin" path="panBin" value="${transactionForm.panBin}" />

								<br />

								<form:errors cssClass="error" path="panBin" />
							</div>

							<br />

							<div class="date-time-div">
								<label class="start-date" for="startDate">Start Date & Time<em>*</em></label>

								<form:input class="date-pick" id="startDate" path="startDate" value="${transactionForm.startDate}" />

								<form:select id="startTimeHr" path="startTimeHr">
									<c:forEach items="${transactionForm.startHourDisplay}" var="hrC">
										<form:option value="${hrC}">
											<fmt:formatNumber maxIntegerDigits="2" minIntegerDigits="2" value="${hrC}" />
										</form:option>
									</c:forEach>
								</form:select>

								<span>:</span>

								<form:select id="startTimeMin" path="startTimeMin">
									<c:forEach items="${transactionForm.startMinuteDisplay}" var="minsC">
										<form:option value="${minsC}">
											<fmt:formatNumber maxIntegerDigits="2" minIntegerDigits="2" value="${minsC}" />
										</form:option>
									</c:forEach>
								</form:select>

								<br />

								<form:errors cssClass="error" path="startDate" />
							</div>

							<div class="date-time-div">
								<label class="end-date" for="endDate">End Date & Time<em>*</em></label>

								<form:input class="date-pick" id="endDate" path="endDate" value="${transactionForm.endDate}" />

								<form:select id="endTimeHr" path="endTimeHr">
									<c:forEach items="${transactionForm.endHourDisplay}" var="hrC">
										<form:option value="${hrC}">
											<fmt:formatNumber maxIntegerDigits="2" minIntegerDigits="2" value="${hrC}" />
										</form:option>
									</c:forEach>
								</form:select>

								<span>:</span>

								<form:select id="endTimeMin" path="endTimeMin">
									<c:forEach items="${transactionForm.endMinuteDisplay}" var="minsC">
										<form:option value="${minsC}">
											<fmt:formatNumber maxIntegerDigits="2" minIntegerDigits="2" value="${minsC}" />
										</form:option>
									</c:forEach>
								</form:select><br />

								<form:errors cssClass="error" path="endDate" />
							</div>

							<br />

							<div class="date-type-div">
								<label>Search By</label>

								<form:select id="dateType" path="dateType">
									<form:option value="system">Cuscal Switch Date/Time</form:option>
									<form:option value="local">Location Date/Time</form:option>
								</form:select>
							</div>
						</fieldset>
					</div>

					<br />

					<a class="search-button" href="javascript:filterCudByDates('${showCudList}');">Search</a>
					<a class="clear-button" href="javascript:clearCudAll('${clearVal}');">Clear</a>
				</div>

				<br />

				<c:if test="${not empty cudList}">
					<div id="transaction-cud-form">
						<a href="javascript:void(0);" id="show-hide-cudSearch" onclick="javascript:showHideCudSearchForm();">
							<c:choose>
								<c:when test="${not empty cudList}">
									Show search form
								</c:when>
								<c:otherwise>
									Hide search form
								</c:otherwise>
							</c:choose>
						</a>
						<!-- <a class="export-button" href="${exportCudLst}">Export Results</a> -->
					</div>

					<br />
				</c:if>

				<br />

				<c:if test="${dateWarningMsg}">
					<div class="portlet-msg-info">
						<spring:message code="date.range.limit.transactionArchive" />
					</div>
				</c:if>

				<c:if test="${moreDataMsg}">
					<div class="portlet-msg-info">
						<spring:message code="transaction.search.fromToDate.moreThan.hardLimit" />
					</div>
				</c:if>

				<c:if test="${moreOrg}">
					<div class="portlet-msg-info">
						<spring:message code="cuscal.transaction.error.more.org.msg" />
					</div>
				</c:if>

				<c:if test="${dataBaseError}">
					<div class="portlet-msg-info">
						<spring:message code="cuscal.transaction.search.results.incomplete" />
					</div>
				</c:if>

				<c:if test="${otherError}">
					<div class="portlet-msg-info">
						<spring:message code="cuscal.transaction.error.msg" />
					</div>
				</c:if>

				<display:table
					class="cuscalTable"
					defaultorder="descending"
					defaultsort="6"
					id="cudFrm"
					name="sessionScope.cudList"
					pagesize="25"
					requestURI="${renderURL}"
					sort="external"
					uid="cudFrm"
				>
					<display:setProperty name="basic.msg.empty_list">
						<p class="no-results">${noDataFoundMsg}</p>
					</display:setProperty>

					<display:setProperty name="factory.requestHelper" value="org.displaytag.portlet.PortletRequestHelperFactory" />

					<display:setProperty name="paging.banner.page.separator">
					</display:setProperty>

					<display:setProperty name="paging.banner.placement">both</display:setProperty>

					<display:setProperty name="paging.banner.item_name">result</display:setProperty>

					<display:setProperty name="paging.banner.items_name">results</display:setProperty>

					<display:setProperty name="paging.banner.group_size" value="10" />

					<display:setProperty name="paging.banner.all_items_found">
						<span class="pagination-text">Displaying results 1-{0} of {0}</span>
					</display:setProperty>

					<display:setProperty name="paging.banner.some_items_found">
						<span class="pagination-text">Displaying results {2}-{3} of {0}</span>
					</display:setProperty>

					<display:setProperty name="paging.banner.first">
						<span class="pagination-links"> {0} <a href="{3}">&gt;</a> </span>
					</display:setProperty>

					<display:setProperty name="paging.banner.last">
						<span class="pagination-links"> <a href="{2}">&lt;</a> {0} </span>
					</display:setProperty>

					<display:setProperty name="paging.banner.onepage">
						<span class="pagination-links"> {0} </span>
					</display:setProperty>

					<display:setProperty name="paging.banner.full">
			<span class="pagination-links"> <a href="{2}">&lt;</a> {0} <a
					href="{3}">&gt;</a> </span>
					</display:setProperty>

					<display:setProperty name="css.th.ascending" value="ascending" />
					<display:setProperty name="css.th.descending" value="descending" />

					<display:column class="first" defaultorder="ascending" sortable="true" sortName="pan" title="PAN">
						<a
								href="javascript:openCudTransactionDetailsJsonObj('${showCudDetails}','${cudFrm.transactionId}','${cudFrm.etlProcessEndDate}','${cudFrm.adPank}','${cudFrm_rowNum}','${cudFrm.adTransactionDetailK}');"
								id="panTx">${cudFrm.pan}</a
						>
					</display:column>

					<display:column defaultorder="ascending" property="terminalId" sortable="true" sortName="terminalId" title="Terminal ID" />

					<display:column defaultorder="ascending" property="terminalLocation" sortable="true" sortName="terminalLocation" title="Terminal Location" />

					<display:column defaultorder="ascending" sortable="true" sortName="responseCode" title="Response Code">
						<div title="${cudFrm.responseDescription}">${cudFrm.responseCode}</div>
					</display:column>

					<display:column defaultorder="ascending" property="systemTrace" sortable="true" sortName="systemTrace" title="System Trace" />

					<display:column defaultorder="descending" sortable="true" sortName="cudTransactionDate" title="Date/Time">
			<span class="nowrap">
				<fmt:formatDate pattern="dd/MM/yyyy" type="date" value="${cudFrm.cudTransactionDate}" />&nbsp;${cudFrm.cudTranscationTime}
			</span>
					</display:column>

					<display:column defaultorder="ascending" property="description" sortable="true" sortName="description" title="Description" />

					<display:column class="alignRight last" defaultorder="ascending" sortable="true" sortName="amount" title="Amount">
			<span class="nowrap">
				<fmt:formatNumber groupingUsed="true" maxFractionDigits="${cudFrm.currencyFormatVal}" minFractionDigits="${cudFrm.currencyFormatVal}" value="${cudFrm.amount}" />&nbsp;${cudFrm.currencyCodeAcq}
			</span>
					</display:column>
				</display:table>
			</div>

			<div class="modal-wrapper" id="transCudDetailErrorDiv">
				<div class="modal-header">
					<h5>Transaction Details</h5>

					<a class="modal-close" href="#">X</a>
				</div>

				<table border="0" cellpadding="0" cellspacing="0" class="response-message-codes tx-details" id="transDetailError">
				</table>
			</div>

			<div class="modal-wrapper" id="transactionCudDetailsDiv">
				<div class="modal-header">
					<h5>Transaction Details</h5>

					<a class="modal-close" href="#">X</a>
				</div>

				<div class="printable">
					<table border="0" cellpadding="0" cellspacing="0" class="transaction-code" width="100%">
						<tr>
							<td class="tx-titles">Description:</td>
							<td id="cudDesc"></td>
						</tr>
					</table>

					<div class="modal-header">
						<h5>Request Details</h5>
					</div>

					<input id="cudTxId" name="cudTxId" type="hidden" value="" />
					<input id="cudEtlDate" name="cudEtlDate" type="hidden" value="" />
					<input id="cudAdPank" name="cudAdPank" type="hidden" value="" />
					<input id="adTransactionDetailK" name="adTransactionDetailK" type="hidden" value="" />
					<input id="size" name="size" type="hidden" value="<c:out value="${fn:length(sessionScope.cudList)}" />" />

					<div>
						<label class="pan">PAN:</label>

						<span id="pan"></span>
						<span id="masked-pan"></span>
						<span id="showPan"></span>
					</div>

					<div style="display: block;">
						<fieldset class="amounts">
							<legend>Amounts</legend>

							<table cellpadding="0" cellspacing="0" class="tx-details" width="100%">
								<tr>
									<td class="tx-titles">
										Transaction
										<br />

										<span class="normal nowrap" id="txAmt"></span>
									</td>
									<td class="tx-titles">
										Cash
										<br />

										<span class="normal nowrap" id="cash"></span>
									</td>
									<td class="tx-titles">
										Fee
										<br />

										<span class="normal nowrap" id="fee"></span>
									</td>
								</tr>
							</table>
						</fieldset>

						<fieldset class="date-time">
							<legend>Date/Time</legend>

							<table cellpadding="0" cellspacing="0" class="tx-details" width="100%">
								<tr>
									<td class="tx-titles">Switch:</td>
									<td id="switch"></td>
									<td class="tx-titles">Location:</td>
									<td id="lctDtTme"></td>
								</tr>
							</table>
						</fieldset>

						<fieldset class="acquirer">
							<legend>Acquirer</legend>

							<table cellpadding="0" cellspacing="0" class="tx-details" width="100%">
								<tr>
									<td class="id tx-titles">ID:</td>
									<td id="acqId"></td>
									<td class="tx-titles">Name:</td>
									<td id="acqName"></td>
									<td class="tx-titles">System Trace:</td>
									<td id="sysTrace"></td>
								</tr>
							</table>
						</fieldset>

						<fieldset class="issuer">
							<legend>Issuer</legend>

							<table cellpadding="0" cellspacing="0" class="tx-details" width="100%">
								<tr>
									<td class="id tx-titles">ID:</td>
									<td id="issueId"></td>
									<td class="tx-titles">Name:</td>
									<td id="issuerName"></td>
								</tr>
							</table>
						</fieldset>

						<fieldset class="card-acceptor">
							<legend>Card Acceptor</legend>

							<table cellpadding="0" cellspacing="0" class="tx-details" width="100%">
								<tr>
									<td class="id tx-titles">ID:</td>
									<td id="cardAcceptId"></td>
									<td class="tx-titles">Name:</td>
									<td id="cardAcptName"></td>
									<td class="tx-titles">Terminal ID:</td>
									<td id="cardAcptTerminalId"></td>
								</tr>
							</table>
						</fieldset>

						<fieldset class="pos">
							<legend>POS</legend>

							<table border="0" cellpadding="0" cellspacing="0" class="tx-details" width="100%">
								<tr>
									<td class="tx-titles">Entry Mode:</td>
									<td id="entryMode"></td>
								</tr>
								<tr class="alt">
									<td class="tx-titles">Entry Description:</td>
									<td id="entryDscrip"></td>
								</tr>
								<tr>
									<td class="tx-titles">Condition Code:</td>
									<td id="condCode"></td>
								</tr>
								<tr class="alt">
									<td class="tx-titles">Condition Description:</td>
									<td id="condDescp"></td>
								</tr>
								<tr>
									<td class="tx-titles">Merchant Code:</td>
									<td id="merchCode"></td>
								</tr>
								<tr class="alt">
									<td class="tx-titles">Merchant Description:</td>
									<td id="merchDscrp"></td>
								</tr>
							</table>
						</fieldset>

						<fieldset class="response-details">
							<legend>Response Details</legend>

							<table border="0" cellpadding="0" cellspacing="0" class="response-details tx-details" width="100%">
								<tr>
									<td class="tx-titles">Code:</td>
									<td id="respCode"></td>
								</tr>
								<tr class="alt">
									<td class="tx-titles">Description:</td>
									<td id="respDescp"></td>
								</tr>
								<tr>
									<td class="tx-titles">Switched By:</td>
									<td id="switchBy"></td>
								</tr>
								<tr class="alt">
									<td class="tx-titles">Authorised By:</td>
									<td id="authBy"></td>
								</tr>
							</table>
						</fieldset>
					</div>
				</div>

				<div class="tx-buttons-left" id="prevNextPrt">
					<a class="prev-button" href="javascript:openCudTransactionDetailsJsonObjForNextPrev('${prevCudDetails}');" id="prev">Previous</a>
					<a class="next-button" href="javascript:openCudTransactionDetailsJsonObjForNextPrev('${nextCudDetails}');" id="next">Next</a>
					<a class="print-button" href="javascript:printCudReport('${printCudDetails}');" id="print">Print</a>
				</div>

				<div class="tx-buttons-right">
					<a class="close-button modal-close" href="#" id="closeToken">Close</a>
				</div>

				<br />
			</div>
	</form:form>