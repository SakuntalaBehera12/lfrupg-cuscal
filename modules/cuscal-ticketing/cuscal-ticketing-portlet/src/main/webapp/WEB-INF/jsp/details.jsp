<%@ page import="java.util.Objects" %>

<%@ page import="javax.portlet.PortletSession" %>

<%@ include file="init.jsp" %>

<div id="service-request-wrapper">
<portlet:defineObjects />

<%
String steppedUp = (String)portletSession.getAttribute("_USER_STEPPEDUP", PortletSession.APPLICATION_SCOPE);
%>

<portlet:actionURL escapeXml="false" var="stepUpUrl">
	<portlet:param name="action" value="stepUp" />
</portlet:actionURL>
	<c:choose>
	<c:when test="${not ticketDetailsNull}">
		<c:choose>
			<%-- If there is no ticket found with the provided number --%>
			<c:when test="${ticketNotFound}">
			<%@ include file="filter.jsp" %>
			<br />

			<div class="portlet-msg-info">
				<span>Unable to find the specified service request.</span>
			</div>
			</c:when>
			<c:otherwise>
			<c:if test="${addNoteFailed}">
			<script type="text/javascript">
			Liferay.on('allPortletsReady', function() {
					setTimeout("scrollToAnchor('notes')", 500);
				});
			</script>
			</c:if>

			<c:set value="${ticketDetails}" var="ticket" />

			<c:choose>
			<c:when test="${ticket.ticketNumber eq 'Submitted'}">
				<c:set value="" var="requestNumber" />
			</c:when>
			<c:otherwise>
				<c:set value="${ticket.ticketNumber}" var="requestNumber" />
			</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${IsAMasterCardDispute != null && IsAMasterCardDispute eq 'true'}">
					<c:set value="true" var="isMasterCardDispute" />
				</c:when>
				<c:otherwise>
					<c:set value="false" var="isMasterCardDispute" />
				</c:otherwise>
			</c:choose>

			<h1>Request No. <c:out value="${requestNumber}" /> (<c:out value="${ticket.ticketStatus}" />)</h1>

			<div class="call-details">
				<fieldset class="request-fieldset">
					<legend>Request Details</legend>

					<div class="request-details">
						<div class="title">Request Type</div>
						<div><c:out value="${ticket.ticketType}" /></div>
					</div>

					<div class="request-details">
						<div class="title">Service Type</div>
						<div><c:out value="${ticket.requestType}" /></div>
					</div>

					<c:if test="${not empty ticket.referenceNumber}">
					<div class="request-details">
						<div class="title">Reference</div>
						<div><c:out value="${ticket.referenceNumber}" /></div>
					</div>
					</c:if>

					<br />

					<div class="request-details">
						<div class="title">Date Submitted</div>
						<div><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${ticket.submittedDate}" /></div>
					</div>

					<div class="request-details">
						<div class="title">Date Updated</div>
						<div><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${ticket.updatedDate}" /></div>
					</div>

					<br />

					<div class="request-details submitted-by">
						<div class="title">Submitted By</div>
						<div><c:out value="${ticket.ticketSubmittedBy}" /></div>
					</div>

					<div class="section">
						<div class="title">Description</div>
						<div class="description value"><c:out escapeXml="false" value="${ticket.description}" /></div>
					</div>

					<br />

					<c:if test="${not empty ticketDetails.attributes && (ticketDetails.ticket.type.typeId eq '26126' || ticketDetails.ticket.type.typeId eq '26364')}">
						<c:choose>
							<c:when test='<%= Objects.equals(steppedUp, "true") %>'>
								<portlet:resourceURL escapeXml="false" id="retrieveMaskedFeilds" var="retrieveMaskedFields">
									<portlet:param name="ticketId" value="${ticketDetails.cuscalTicketNumber}" />
								</portlet:resourceURL>

								<a class="buttons masked-details" href="javascript:;" url="${retrieveMaskedFields}">View Masked Detail/s</a>
							</c:when>
							<c:otherwise>
								<form action="${stepUpUrl}" method="post">
									<input name="ticketIdStepUp" type="hidden" value="${ticketDetails.cuscalTicketNumber}" />
									<input class="request-buttons" type="submit" value="Activate Masked Detail/s" />
								</form>
							</c:otherwise>
						</c:choose>
					</c:if>
				</fieldset>

				<c:if test="${not empty ticketDetails.transactions}">
				<fieldset class="transactions">
					<legend>Transaction</legend>

					<c:forEach items="${ticketDetails.transactions}" var="transaction">
						<table cellpadding="3" cellspacing="0" class="transaction-details" width="100%">
							<tr>
								<td class="first-column title">PAN</td>
								<td><c:out value="${transaction.maskedPan}" /></td>
								<td class="second-column title">Acquirer</td>
								<td>
									<c:out value="${transaction.acquirerId}" />

									<c:if test="${not empty transaction.acquirerName}">
										- <c:out value="${transaction.acquirerName}" />
									</c:if>
								</td>
							</tr>
							<tr>
								<td class="first-column title">Issuer</td>
								<td>
									<c:out value="${transaction.issuerId}" />

									<c:if test="${not empty transaction.issuerName}">
										- <c:out value="${transaction.issuerName}" />
									</c:if>
								</td>
								<td class="second-column title">Terminal ID</td>
								<td><c:out value="${transaction.terminalId}" /></td>
							</tr>
							<tr>
								<td class="first-column title">Location Date</td>
								<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${transaction.transactionLocalDate}" /></td>
								<td class="second-column title">ATM/POS</td>
								<td><c:out value="${transaction.atmPos}" /></td>
							</tr>
							<tr>
								<td class="first-column title">Transaction Amount</td>
								<td><c:out value="${transaction.transactionAmount}" />&nbsp;<c:out value="${transaction.transactionAmountCurrency}" /></td>
								<td class="second-column title">Cardholder Amount</td>
								<td><c:out value="${transaction.cardholderAmount}" />&nbsp;<c:out value="${transaction.cardholderAmountCurrency}" /></td>
							</tr>
							<tr>
								<td class="first-column title">Cuscal ID</td>
								<td><c:out value="${transaction.transactionId}" /></td>
								<td class="second-column title">STAN</td>
								<td><c:out value="${transaction.stan}" />
							</tr>

							<c:if test="${not empty transaction.visaTransactionId}">
							<tr>
								<td class="first-column title">Scheme ID</td>
								<td><c:out value="${transaction.visaTransactionId}" /></td>

								<c:if test="${not empty transaction.arnId}">
								<td class="second-column title">ARN</td>
								<td><c:out value="${transaction.arnId}" /></td>
								</c:if>
							</tr>
							</c:if>
						</table>
					</c:forEach>
				</fieldset>
				</c:if>

				<c:if test="${not empty ticketDetails.attachments}">
				<fieldset class="attachment-fieldset">
					<legend>Attachments</legend>

					<div class="section">
						<c:if test="${not empty ticketDetails.attachments}">
							<c:forEach items="${ticketDetails.attachments}" var="attachment" varStatus="counter">
								<div class="attachments">
									<fmt:formatDate pattern="dd/MM/yyyy" value="${attachment.attachmentDate}" />:
									<c:choose>
										<c:when test='<%= Objects.equals(steppedUp, "true") %>'>
											<c:if test="${attachmentRole}">
											<portlet:resourceURL escapeXml="false" id="downloadFile" var="ticketAttachment">
												<portlet:param name="attachmentId" value="${attachment.attachmentId}" />
											</portlet:resourceURL>

											<a href="${ticketAttachment}"><c:out value="${attachment.filename}" /></a>
											(<c:out value="${attachment.uploadedBy}" />)
											</c:if>
										</c:when>
										<c:otherwise>
											<c:out value="${attachment.filename}" />
											(<c:out value="${attachment.uploadedBy}" />)
										</c:otherwise>
									</c:choose>
								</div>

								<br />
							</c:forEach>

							<c:if test="${attachmentRole}">
								<c:if test='<%= !Objects.equals(steppedUp, "true") %>'>
									<form action="${stepUpUrl}" method="post" name="stepUpForm">
										<input name="ticketIdStepUp" type="hidden" value="${ticketDetails.cuscalTicketNumber}" />
										<input class="request-buttons" type="submit" value="Activate File/s" />
									</form>
								</c:if>
							</c:if>
						</c:if>

						<c:if test="${fileName != null}">
							<c:out value="${fileName}" />
						</c:if>
					</div>
				</fieldset>
				</c:if>

				<c:choose>
					<c:when test="${isMasterCardDispute}">
						<c:if test="${EditRequestFlag}">
							<c:if test="${chargebackLink != null}">
								<a class="buttons" href=<c:out value="${chargebackLink}" />>Edit Request</a>
							</c:if>
						</c:if>

						<c:if test="${CancelRequestFlag}">
							<a class="cancel-this-request" href="javascript:;">Cancel Request</a>

							<br />
						</c:if>

						<c:if test="${ReverseRequestFlag}">
							<a class="reverse-this-request" href="javascript:;">Reverse Request</a>

							<br />
						</c:if>
					</c:when>
					<c:otherwise>
						<c:if test="${chargebackLink != null}">
							<a class="buttons" href=<c:out value="${chargebackLink}" />>Edit Request</a>
						</c:if>
					</c:otherwise>
			</c:choose>

			<portlet:actionURL escapeXml="false" var="updateScActionUrl">
					<portlet:param name="action" value="updateScAction" />
					<portlet:param name="ticketId" value="${ticketDetails.cuscalTicketNumber}" />
					<portlet:param name="scItemId" value="${scItemId}" />
				</portlet:actionURL>

				<c:if test="${scItemId != null}">
					<a class="buttons" href=<c:out value="${updateScActionUrl}" />>Edit Ticket</a>
				</c:if>

				<div class="section updates">
					<h5>Comments</h5>

					<div class="value">
						<c:set value="${ticketDetails.notes}" var="notes" />

						<c:choose>
						<c:when test="${not empty notes}">
						<c:forEach items="${notes}" var="note" varStatus="counter">
						<c:if test="${not note.isDeleted}">

						<c:choose>
						<c:when test="${note.isCuscalUser}">
							<c:set value="cuscal-user" var="commentUser" />
						</c:when>
						<c:otherwise>
							<c:set value="original-user" var="commentUser" />
						</c:otherwise>
						</c:choose>

						<div class="note ${commentUser}">
							<%-- <c:if test="${note.updateTime gt note.userReadTime}">
								<c:set value="highlight" var="highlight" />
							</c:if> --%>
							<div class="note-user">
								<c:out value="${note.noteUser}" />
								&nbsp;<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${note.noteDate}" />
							</div>

							<pre class="note-comment"><c:out escapeXml="true" value="${note.note}" /></pre>
						</div>
						</c:if>
						</c:forEach>
						</c:when>
						<%-- <c:otherwise>
						No updates
						</c:otherwise> --%>
						</c:choose>
					</div>
				</div>

				<portlet:actionURL escapeXml="false" var="addNotesUrl">
					<portlet:param name="action" value="addNote" />
				</portlet:actionURL>

				<c:if test="${notesRole and ticket.ticketStatus ne 'Closed'}">
				<div class="section">
					<a name="notes"></a>

					<form:form action="${addNotesUrl}" method="post" modelAttribute="ticketNote">
						<input name="i" type="hidden" value="${ticket.cuscalTicketNumber}" />

						<div class="disclaimer">
							<spring:message code="ticket.clear.pan.disclaimer" />
						</div>

						<div class="value">
							<form:textarea htmlEscape="false" maxlength="4000" path="note" />

							<div>
								<form:errors cssClass="error" path="note" />
							</div>
						</div>

						<input class="request-buttons" type="submit" value="Add Comment" />
					</form:form>
				</div>
				</c:if>

				<c:choose>
					<c:when test="${attachmentRole}">
						<c:choose>
							<c:when test="${ticket.ticketStatus ne 'Closed'}">
								<c:set value="true" var="attachFiles" />
							</c:when>
							<c:when test="${ticket.ticketStatus eq 'Closed'}">
								<c:if test="${attachAfterClose}">
									<c:set value="true" var="attachFiles" />
								</c:if>
							</c:when>
							<c:otherwise>
								<c:set value="false" var="attachFiles" />
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:set value="false" var="attachFiles" />
					</c:otherwise>
				</c:choose>

				<c:if test="${attachFiles eq true}">

				<portlet:actionURL escapeXml="false" var="attachFileUrl">
					<portlet:param name="action" value="attachFile" />
				</portlet:actionURL>

				<div class="section">
					<form action="${attachFileUrl}" enctype="multipart/form-data" method="post" name="uploadFilesForm">
						<input name="ticketId" type="hidden" value="${ticketDetails.cuscalTicketNumber}" />
						<input name="file" type="file" />
						<input class="request-buttons" type="submit" value="Attach File" />
					</form>

					<c:if test="${fileTooBig != null}">
						<div class="error"><span><spring:message code="ticket.file.size" /></span></div>
					</c:if>

					<c:if test="${fileInvalid != null}">
						<div class="error"><span><spring:message code="ticket.file.invalid" /></span></div>
					</c:if>
				</div>
				</c:if>
			</div>

			<br />
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
	<div class="portlet-msg-error">
		<span><spring:message code="ticket.generic.error" /></span>
	</div>
	</c:otherwise>
	</c:choose>

	<div class="modal-wrapper" id="maskedFieldDiv">
		<ul class="masked-fields-output no-print"></ul>

		<a class="buttons modal-close no-print" href="javascript:void(0)">Ok</a>
	</div>

	<!-- mastercard starts -->
	<div class="modal-wrapper" id="cancelRequestDiv">
		<portlet:resourceURL escapeXml="false" id="cancelRequest" var="cancelRequest">
			<portlet:param name="ticketId" value="${ticketDetails.cuscalTicketNumber}" />
			<portlet:param name="cancelThisRequest" value="true" />
		</portlet:resourceURL>

		<table>
			<tr>
				<td class="text"><b>Cancel Request</b></td>
				<td class="right">
					<a class="cancel-request-close modal-close no-print" href="javascript:void(0)">X</a>
				</td>
			</tr>
			<tr><td colspan="2" class="text">Please provide the reason for cancelling this Service Request and click submit.</td></tr>

			<tr>
				<td class="text" colspan="2">
					<textarea class="cancel-note-ta" cols="20" id="cancelNote" name="cancelNote" rows="2" wrap="hard"></textarea>

					<ul class="cancel-request-output error no-print"></ul>
				</td>
			</tr>
			<tr>
				<td class="right" colspan="2">
					<a class="cancel-request" href="javascript:;" url="${cancelRequest}">Submit</a>

					<form:form action="${addNotesUrl}" id="cancelRequestSubmitForm" method="post" modelAttribute="ticketNote">
						<input name="i" type="hidden" value="${ticketDetails.cuscalTicketNumber}" />
						<input id="cancelThisRequest" name="cancelThisRequest" type="hidden" value="true" />
						<input name="exportIPM" type="hidden" value="${ticketDetails.exportIPM}" />
						<form:hidden class="cancelRequestNoteCSSClass" path="note" />
					</form:form>
				</td>
			</tr>
		</table>
	</div>

	<div class="modal-wrapper" id="reverseRequestDiv">
		<portlet:resourceURL escapeXml="false" id="reverseRequest" var="reverseRequest">
			<portlet:param name="ticketId" value="${ticketDetails.cuscalTicketNumber}" />
			<portlet:param name="reverseThisRequest" value="true" />
		</portlet:resourceURL>

		<table>
			<tr>
				<td class="text"><b>Reverse Request</b></td>
				<td class="right">
					<a class="cancel-request-close modal-close no-print" href="javascript:void(0)">X</a>
				</td>
			</tr>
			<tr><td colspan="2" class="text">
						If you are reversing a first chargeback, please ensure the merchant has
						not processed a second presentment before proceeding with this reversal.<br /><br />
						Please provide the reason for reversing this Service Request and click submit.
				</td></tr>

			<tr>
				<td class="text" colspan="2">
					<textarea class="cancel-note-ta" cols="20" id="reverseNote" name="reverseNote" rows="2" wrap="hard"></textarea>

					<ul class="error no-print reverse-request-output"></ul>
				</td>
			</tr>
			<tr>
				<td class="right" colspan="2">
					<a class="reverse-request" href="javascript:;" url="${cancelRequest}">Submit</a>

					<form:form action="${addNotesUrl}" id="reverseRequestSubmitForm" method="post" modelAttribute="ticketNote">
						<input name="i" type="hidden" value="${ticketDetails.cuscalTicketNumber}" />
						<input id="reverseThisRequest" name="reverseThisRequest" type="hidden" value="true" />
						<input name="exportIPM" type="hidden" value="${ticketDetails.exportIPM}" />
						<form:hidden class="reverseRequestNoteCSSClass" path="note" />
					</form:form>
				</td>
			</tr>
		</table>
	</div>
	<!-- mastercard ends -->
</div> <%-- Start of this tag is in init.jsp --%>