<fieldset>
	<legend>Transaction Information</legend>

	<table border="0" cellpadding="3" cellspacing="0" class="transaction-details" width="100%">
		<tr>
			<td class="first-column title">PAN</td>
			<td class="first-column-value"><c:out value="${transaction.maskedPan}" /></td>
			<td class="second-column title">Acquirer</td>
			<td class="second-column-value">
				<c:out value="${transaction.acquirerId}" />

				<c:if test="${not empty transaction.acquirerName}">
				- <c:out value="${transaction.acquirerName}" />
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="first-column title">Issuer</td>
			<td class="first-column-value">
				<c:out value="${transaction.issuerId}" />

				<c:if test="${not empty transaction.issuerName}">
				- <c:out value="${transaction.issuerName}" />
				</c:if>
			</td>
			<td class="second-column title">Terminal ID</td>
			<td class="second-column-value"><c:out value="${transaction.terminalId}" /></td>
		</tr>
		<tr>
			<td class="first-column title">Location Date</td>
			<td class="first-column-value"><c:out value="${transaction.transactionLocalDate}" /></td>
			<td class="second-column title">ATM/POS</td>
			<td class="second-column-value"><c:out value="${transaction.atmPos}" /></td>
		</tr>
		<tr>
			<td class="first-column title">Transaction Amount</td>
			<td class="first-column-value"><c:out value="${transaction.transactionAmount}" />&nbsp;<c:out value="${transaction.transactionAmountCurrency}" /></td>
			<td class="second-column title">Cardholder Amount</td>
			<td class="second-column-value"><c:out value="${transaction.cardholderAmount}" />&nbsp;<%--<c:out value="${transaction.cardholderAmountCurrency}" /> --%></td>
		</tr>
		<tr>
			<td class="first-column title">Cuscal ID</td>
			<td class="first-column-value"><c:out value="${transaction.transactionId}" /></td>
			<td class="second-column title"><abbr title="System Trace Number">STAN</abbr></td>
			<td class="second-column-value"><c:out value="${transaction.stan}" />
		</tr>

		<c:if test="${not empty transaction.visaTransactionId}">
		<tr>
			<td class="first-column title">Scheme ID</td>
			<td class="first-column-value"><c:out value="${transaction.visaTransactionId}" /></td>
			<td class="second-column title">&nbsp;</td>
			<td class="second-column-value">&nbsp;</td>
		</tr>
		</c:if>
	</table>
</fieldset>

<fieldset>
	<legend>Additional Transaction Information</legend>

	<table border="0" cellpadding="3" cellspacing="0" class="transaction-details" width="100%">
		<tr>
			<td class="first-column title">POS Entry Mode</td>

			<c:choose>
				<c:when test="${not empty transaction.posEntryModeCode}">
					<td class="first-column-value">
						<c:out value="${transaction.posEntryModeCode}" />

						<c:if test="${not empty transaction.posEntryModeDescription}">
							&nbsp;<c:out value="${transaction.posEntryModeDescription}" />
						</c:if>
					</td>
				</c:when>
				<c:otherwise>
					<td class="second-column-value">&nbsp;</td>
				</c:otherwise>
			</c:choose>

			<td class="second-column title">POS Condition</td>

			<c:choose>
				<c:when test="${not empty transaction.posConditionCode}">
					<td class="second-column-value">
						<c:out value="${transaction.posConditionCode}" />

						<c:if test="${not empty transaction.posConditionDescription}">&nbsp;<c:out value="${transaction.posConditionDescription}" /></c:if>
					</td>
				</c:when>
				<c:otherwise>
					<td class="second-column-value">&nbsp;</td>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td class="second-column title">Function Code</td>
			<td class="first-column-value"><c:out value="${transaction.functionCode}" />
				&nbsp;<c:out value="${transaction.functionCodeDescription}" /></td>

			<td class="second-column title"><abbr title="Central Site Business Date">CSBD</abbr></td>
			<td class="second-column-value"><c:out value="${transaction.centralSiteBusinessDate}"></c:out></td>
		</tr>
		<tr>
			<td class="first-column title"><abbr title="The first digit of the service code indicates the card usage and chip presence">Track 2 Data Present</abbr></td>

			<c:choose>
				<c:when test="${not empty transaction.track2DataServiceCode}">
				<td class="first-column-value"><c:out value="${transaction.track2DataServiceCode}" /></td>
			</c:when>
				<c:otherwise>
					<td class="second-column title">&nbsp;</td>
				</c:otherwise>
			</c:choose>

			<td class="second-column title">Chip Card Read</td>

			<c:choose>
				<c:when test="${not empty transaction.chipCardPresent}">
				<td class="second-column-value"><c:out value="${transaction.chipCardPresent}" /></td>
			</c:when>
				<c:otherwise>
					<td class="second-column title">&nbsp;</td>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td class="first-column title"><abbr title="Indicates electronic commerce or MOTO transaction">eCommerce Transaction</abbr></td>

			<c:choose>
				<c:when test="${not empty transaction.electronicCommerceIndciator}">
					<td class="first-column-value"><c:out value="${transaction.electronicCommerceIndciator}"></c:out></td>
				</c:when>
				<c:otherwise>
					<td class="second-column title">&nbsp;</td>
				</c:otherwise>
			</c:choose>

			<td class="second-column title"><abbr title="Universal Cardholder Authentication Field">UCAF Indicator</abbr></td>

			<c:choose>
				<c:when test="${not empty transaction.UCAFCollectionIndicator}">
					<td class="second-column-value">
						<c:out value="${transaction.UCAFCollectionIndicator}" />

						<c:if test="${not empty transaction.UCAFCollectionIndicatorDescription}">&nbsp;<c:out value="${transaction.UCAFCollectionIndicatorDescription}" /></c:if>
					</td>
				</c:when>
				<c:otherwise>
					<td class="second-column title">&nbsp;</td>
				</c:otherwise>
			</c:choose>
		</tr>
	</table>
</fieldset>