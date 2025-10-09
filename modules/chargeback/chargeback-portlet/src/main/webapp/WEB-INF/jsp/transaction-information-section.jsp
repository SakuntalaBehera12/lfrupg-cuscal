<fieldset>
	<legend>Transaction Information</legend>

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
			<td><c:out value="${transaction.transactionLocalDate}" /></td>
			<td class="second-column title">ATM/POS</td>
			<td><c:out value="${transaction.atmPos}" /></td>
		</tr>
		<tr>
			<td class="first-column title">Transaction Amount</td>
			<td><c:out value="${transaction.transactionAmount}" />&nbsp;<c:out value="${transaction.transactionAmountCurrency}" /></td>
			<td class="second-column title">Cardholder Amount</td>
			<td><c:out value="${transaction.cardholderAmount}" />&nbsp;<%--<c:out value="${transaction.cardholderAmountCurrency}" /> --%></td>
		</tr>
		<tr>
			<td class="first-column title">Cuscal ID</td>
			<td><c:out value="${transaction.transactionId}" /></td>
			<td class="second-column title"><acronym title="System Trace Number">STAN</acronym></td>
			<td><c:out value="${transaction.stan}" />
		</tr>

		<c:if test="${not empty transaction.visaTransactionId}">
		<tr>
			<td class="first-column title">Visa ID</td>
			<td><c:out value="${transaction.visaTransactionId}" /></td>
		</tr>
		</c:if>
	</table>
</fieldset>