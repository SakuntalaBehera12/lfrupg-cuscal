<%@ include file="init.jsp" %>
<div class="printable" id="transaction-details-wrapper">
	<table border="0" cellpadding="0" cellspacing="0" class="transaction-code" width="100%">
		<tr>
			<td class="code tx-details">Code</td>
			<td id="code"></td>
			<td class="tx-titles">Description</td>
			<td id="txDesc"></td>
		</tr>
	</table>

	<div>
		<label class="message-type">Message Type</label>

		<span id="msgType"></span>
	</div>

	<div class="modal-header">
		<h5>Request Details</h5>
	</div>

	<input class="cuscal-transaction-id" name="txId" type="hidden" value="" />
	<input class="business-date" id="busDate" name="BusDate" type="hidden" value="" />
	<input id="txSrc" name="src" type="hidden" value="" />
	<input id="size" name="size" type="hidden" value="<c:out value="${fn:length(sessionScope.txList)}" />" />

	<div>
		<label class="pan">PAN</label>

		<span id="pan"></span>
		<span class="masked-pan"></span>
		<span id="showPan"></span>
		<!--<span id="followPan"></span>-->
		<label class="expiry-date">Expiry Date</label>

		<span id="expDate"></span>

		<label class="expiry-date">Cuscal ID</label>

		<span class="cuscal-transaction-id"></span>
	</div>

	<div>
		<label class="expiry-date">Mobile Device</label>

		<span id="isMobile"></span>
	</div>

	<div>
		<span class="error" id="hsmMsg"></span>
	</div>

	<div style="display: block;">
		<fieldset class="amounts">
			<legend>Amounts</legend>

			<table cellpadding="0" cellspacing="0" class="tx-details" width="100%">
				<tr>
					<td class="tx-titles">
						Transaction
						<br />

						<span class="normal nowrap transaction-amount"></span>
					</td>
					<td class="tx-titles">
						Cardholder
						<br />

						<span class="cardholder-amount normal nowrap"></span>
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
					<td class="tx-titles">Transmission</td>
					<td id="txDtTme"></td>
					<td class="tx-titles">Location</td>
					<td class="location-date"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">Switch</td>
					<td id="switch"></td>
					<td class="tx-titles">Settlement</td>
					<td id="settlement"></td>
				</tr>
			</table>
		</fieldset>

		<fieldset class="acquirer">
			<legend>Acquirer</legend>

			<table cellpadding="0" cellspacing="0" class="tx-details" width="100%">
				<tr>
					<td class="tx-titles">ID</td>
					<td class="acquirer-id"></td>
					<td class="tx-titles">Name</td>
					<td class="acquirer-name"></td>
					<td class="tx-titles">
						<abbr title="System Trace Audit Number">STAN</abbr></td>
					<td class="stan"></td>
					<td class="tx-titles">Scheme ID</td>
					<td class="visa-transaction-id"></td>
				</tr>
			</table>
		</fieldset>

		<fieldset class="issuer">
			<legend>Issuer</legend>

			<table cellpadding="0" cellspacing="0" class="tx-details" width="100%">
				<tr>
					<td class="id tx-titles">ID</td>
					<td class="issuer-id"></td>
					<td class="tx-titles">Name</td>
					<td class="issuer-name"></td>
					<td class="tx-titles">
						<abbr title="Retrieval Reference Number">RRN</abbr>
					</td>
					<td id="retRefNo"></td>
				</tr>
			</table>
		</fieldset>

		<fieldset class="original-acquirer">
			<legend>Original Acquirer</legend>

			<table cellpadding="0" cellspacing="0" class="tx-details" width="100%">
				<tr>
					<td class="tx-titles">ID</td>
					<td id="orgAcqId"></td>
					<td class="tx-titles">Name</td>
					<td id="orgAcqName"></td>
					<td class="tx-titles">Terminal ID</td>
					<td class="terminal-id"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">ATM or POS</td>
					<td class="atm-or-pos" colspan="3"></td>
					<td class="tx-titles">PIN Present</td>
					<td id="pinPrst"></td>
				</tr>
			</table>
		</fieldset>

		<fieldset class="card-acceptor">
			<legend>Card Acceptor</legend>

			<table cellpadding="0" cellspacing="0" class="tx-details" width="100%">
				<tr>
					<td class="id tx-titles">ID</td>
					<td id="cardAcceptId"></td>
					<td class="tx-titles">Name</td>
					<td id="cardAcptName"></td>
					<td class="tx-titles">
						<abbr title="Retrieval Reference Number">RRN</abbr>
					</td>
					<td id="cardRetRefNo"></td>
				</tr>
			</table>
		</fieldset>

		<fieldset class="pos">
			<legend>Point of Service</legend>

			<table border="0" cellpadding="0" cellspacing="0" class="tx-details" width="100%">
				<tr>
					<td class="tx-titles">Entry Mode</td>
					<td id="entryMode"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">Entry Description</td>
					<td id="entryDscrip"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">3DS Authentication</td>
					<td id="auth3ds"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">3DS Value Description</td>
					<td id="auth3ds_desc"></td>
				</tr>
				<tr>
					<td class="tx-titles">Condition Code</td>
					<td id="condCode"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">Condition Description</td>
					<td id="condDescp"></td>
				</tr>
				<tr>
					<td class="tx-titles">Merchant Code</td>
					<td id="merchCode"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">Merchant Description</td>
					<td id="merchDscrp"></td>
				</tr>
				<%-- Project 8847 --%>
				<tr class="alt">
					<td class="tx-titles">Terminal Capability</td>
					<td id="termCapability"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">Faulty Card Reader</td>
					<td id="faultyCardReader"></td>
				</tr>
			</table>
		</fieldset>

		<fieldset class="mastercard-additional-transaction-info-fieldset">
			<legend>Additional Transaction Information</legend>

			<table border="0" cellpadding="0" cellspacing="0" class="tx-details" width="100%">
				<tr>
					<td class="tx-titles">Function Code</td>
					<td>
						<span class="functionCode"></span>
						<span class="functionCodeDescription"></span>
					</td>
				</tr>
				<tr>
					<td class="tx-titles"><abbr title="Central Site Business Date">CSBD</abbr></td>
					<td>
						<span class="csbd-value"></span>
					</td>
				</tr>
				<tr>
					<td class="tx-titles"><abbr title="The first digit of the service code indicates the card usage and chip presence">Track 2 Data Present</abbr></td>
					<td>
						<span class="track2-data-present-value"></span>
					</td>
				</tr>
				<tr>
					<td class="tx-titles">Chip Card Read</td>
					<td>
						<span class="chip-card-present-value"></span>
					</td>
				</tr>
				<tr>
					<td class="tx-titles"><abbr title="Indicates electronic commerce or MOTO transaction">eCommerce Transaction</abbr></td>
					<td>
						<span class="electronic-commerce-indciator"></span>
					</td>
				</tr>
				<tr>
					<td class="tx-titles"><abbr title="Universal Cardholder Authentication Field">UCAF Indicator</abbr></td>
					<td>
						<span class="ucaf-collection-indicator"></span>
						<span class="ucaf-collection-indicator-description"></span>
					</td>
				</tr>
				<tr>
					<td class="tx-titles"><abbr title="Card Control Reject Code">Card Control Reject Code</abbr></td>
					<td>
						<span class="card-control-reject-code"></span>
					</td>
				</tr>
			</table>
		</fieldset>

		<fieldset class="response-details">
			<legend>Response Details</legend>

			<table border="0" cellpadding="0" cellspacing="0" class="tx-details" width="100%">
				<tr>
					<td class="tx-titles">Code</td>
					<td id="respCode"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">Description</td>
					<td id="respDescp"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">Card Control Reject Code</td>
					<td id="cardControlRejectCode"></td>
				</tr>
				<tr>
					<td class="tx-titles">Authorisation ID</td>
					<td id="authId"></td>
				</tr>
				<tr class="alt">
					<td class="tx-titles">Authorised By</td>
					<td id="authBy"></td>
				</tr>
			</table>
		</fieldset>
	</div>

	<div class="tx-buttons-left">
		<a class="no-print prev-button" href="javascript:void(0)" id="prev" onclick="javascript:openTransactionDetailsJsonObjForNextPrev('${prevTxDetails}');">Previous</a>
		<a class="next-button no-print" href="javascript:void(0)" id="next" onclick="javascript:openTransactionDetailsJsonObjForNextPrev('${nextTxDetails}');">Next</a>
		<%-- <a class="print-button no-print" href="javascript:void(0)" onclick="javascript:printRecord('${printTxDetails}');" id="print">Print</a> --%>
	</div>

	<div class="tx-buttons-right">
		<a class="close-button modal-close no-print" href="#" id="closeToken">Close</a>
	</div>
</div>