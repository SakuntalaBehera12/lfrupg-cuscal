<%-- Elaboration Fieldset. --%>
<%@ include file="init.jsp" %>

<fieldset id="elabInfoFraud">
	<legend>Elaboration Information</legend>

	<div id="elabFictitious">
		<form:checkbox path="elaborationInfo.acctNoFictitiousOrInvalid" value="${elaborationInfo.acctNoFictitiousOrInvalid}" />

		<label for="elaborationInfo.acctNoFictitiousOrInvalid1"><spring:message code="label.card.fictitious.invalid" /></label>
	</div>

	<div id="fraudDate">
		<label class="large" for="fraudDateInput"><spring:message code="label.date.tc40.reported" /></label>

		<form:input class="date-pick" id="fraudDateInput" path="elaborationInfo.fraudDate" value="${elaborationInfo.fraudDate}" />

		<form:errors cssClass="error" path="elaborationInfo.fraudDate" />

		<br />

		<em><spring:message code="message.tc40.reported" /></em>
	</div>

	<div id="fraudAdviceNo">
		<label class="large" for="elaborationInfo.fraudAdviceNo"><spring:message code="label.tc40.ref.number" /></label>

		<form:input path="elaborationInfo.fraudAdviceNo" value="${elaborationInfo.fraudAdviceNo}" />

		<a class="bold" href="javascript:;" title="<spring:message code="hover.elabinfo.refNumber" />">?</a>

		<form:errors cssClass="error" path="elaborationInfo.fraudAdviceNo" />
	</div>

	<div id="cardDate">
		<label class="large" for="cardDateInput"><spring:message code="label.date.listing.exception.file" /></label>

		<form:input class="date-pick" id="cardDateInput" path="elaborationInfo.cardDate" value="${elaborationInfo.cardDate}" />

		<form:errors cssClass="error" path="elaborationInfo.cardDate" />
	</div>

	<div id="cardStatus">
		<label class="large"><spring:message code="label.card.status" /></label>

		<form:select cssClass="reset" id="cardFraudStatus" path="elaborationInfo.cardStatus">
			<option value="0"><spring:message code="label.please.select" /></option>

			<form:options items="${cardStatus}"></form:options>
		</form:select>

		<form:errors cssClass="error" path="elaborationInfo.cardStatus" />

		<br />

		<em>(Do not select an option for 'Was the card' if you already stated above that card was fictitious/not valid)</em>
	</div>

	<div id="visaEuropeOnly">
		<form:checkbox id="visaEuropeOnlyData" path="elaborationInfo.visaEuropeOnlyData" />

		<label for="visaEuropeOnlyData">Issuer certifies cardholder denial of participation was obtained in a secure telephone banking session (not applicable for disputes in excess of 1,000 or local equivalent) - Visa Europe Only</label>
	</div>

	<!-- <div id="visaEuropeOnlyDate" class="padded">
		<label for="visaEuropeOnlyDate">Date and time of secure telephone banking session</label>

		<form:input id="visaEuropeOnlyDate" path="elaborationInfo.visaEuropeOnlyDate" />
	</div> -->

	<div id="visaEuropeOnlyDate">
		<label class="padded" for="visaEuropeOnlyDateInput">Date and time of secure telephone banking session</label>
		<%-- <form:input class="date-pick" id="visaEuropeOnlyDateInput" path="elaborationInfo.visaEuropeOnlyDate" value="${elaborationInfo.visaEuropeOnlyDate}" /> --%>
		<form:input id="visaEuropeOnlyDateInput" path="elaborationInfo.visaEuropeOnlyDate" value="${elaborationInfo.visaEuropeOnlyDate}" />

		<form:errors cssClass="error" path="elaborationInfo.visaEuropeOnlyDate" />
	</div>
</fieldset>

<fieldset id="elabInfoAuth">
	<legend>Elaboration Information</legend>

	<div id="elabException">
		<label class="large" for="authDate"><spring:message code="label.date.listing.exception.file" /></label>

		<form:input class="date-pick" id="authDate" path="elaborationInfo.authDate" value="${elaborationInfo.authDate}" />

		<a class="bold" href="javascript:;" title="<spring:message code="hover.elabinfo.authDate" />">?</a>
	</div>

	<%-- <div id="elabCrbDate">
		<label class="large" for="authCrbDate"><spring:message code="label.date.listing.crb" /></label>

		<form:input class="date-pick" id="authCrbDate" path="elaborationInfo.authCrbDate" value="${elaborationInfo.authCrbDate}" />

		<a class="bold" href="javascript:;" title="<spring:message code="hover.elabinfo.authCrbDate" />">?</a>
	</div> --%>

	<div id="elabAuthCrbRegion">
		<label class="large">If listed on the CRB, which region(s)<a href="javascript:;" title="<spring:message code="hover.elabinfo.authCrbRegions" />" class="bold">?</a></label>

		<br />

		<form:checkbox id="usa" path="elaborationInfo.authCrbRegions" value="USA" />

		<label class="large" for="usa">USA</label><br />

		<form:checkbox id="ap" path="elaborationInfo.authCrbRegions" value="AP" />

		<label class="large" for="ap">AP</label><br />

		<form:checkbox id="cemea" path="elaborationInfo.authCrbRegions" value="CEMEA" />

		<label class="large" for="cemea">CEMEA</label><br />

		<form:checkbox id="canada" path="elaborationInfo.authCrbRegions" value="Canada" />

		<label class="large" for="canada">Canada</label><br />

		<form:checkbox id="eu" path="elaborationInfo.authCrbRegions" value="EU" />

		<label class="large" for="eu">EU</label><br />

		<form:checkbox id="lac" path="elaborationInfo.authCrbRegions" value="LAC" />

		<label class="large" for="lac">LAC</label><br />

		<form:checkbox id="all" path="elaborationInfo.authCrbRegions" value="all" />

		<label class="large" for="all">Select All</label>
	</div>

	<div id="elabAuthDeclinedDate">
		<label class="small" for=authDeclinedDate>Date Authorisation Declined</label>

		<form:input class="date-pick" id="authDeclinedDate" path="elaborationInfo.authDeclinedDate" value="${elaborationInfo.authDeclinedDate}" />

		<form:errors cssClass="error" path="elaborationInfo.authDeclinedDate" />
	</div>

	<div id="elabCardExpiredDate">
		<label class="small" for=cardExpiredDate>Date Card Expired</label>

		<form:input class="date-pick" id="cardExpiredDate" path="elaborationInfo.cardExpiredDate" value="${elaborationInfo.authDeclinedDate}" />

		<form:errors cssClass="error" path="elaborationInfo.cardExpiredDate" />
	</div>

	<div id="elabAuthNoTransDate">
		<form:checkbox id="authNoTransDate" path="elaborationInfo.authRequiredNoTransDate" />

		<label for="authNoTransDate">Authorisation was required but not obtained on the transaction date</label>
	</div>

	<div id="elabAuthIncorrectData">
		<form:checkbox id="authIncorrectData" path="elaborationInfo.authObtainedIncorrectData" />

		<label for="authIncorrectData">Transaction was processed with an authorisation from invalid or incorrect data elements</label>
	</div>

	<div class="padded" id="elabAuthIncorrectDataExplain">
		<label for="authIncorrectDataExplain">Provide additional information</label>

		<form:input id="authIncorrectDataExplain" path="elaborationInfo.authObtainedIncorrectDataExplain" />
	</div>

	<div id="elabAuthTransExceedsAuthAmount">
		<form:checkbox id="transExceedsAuthAmount" path="elaborationInfo.authTransExceedsAuthAmount" />

		<label for="transExceedsAuthAmount">Transaction exceeds Authorised amount or allowed percentage</label>
	</div>

	<div class="padded" id="elabAuthTransExceedsAuthAmountExplain">
		<label for="authTransExceedsAuthAmountExplain">Provide additional information</label>

		<form:input id="authTransExceedsAuthAmountExplain" path="elaborationInfo.authTransExceedsAuthAmountExplain" />
	</div>

	<div id="elabAuthNonMatchingMCC">
		<form:checkbox id="authNonMatchingMCC" path="elaborationInfo.authNonMatchingMCC" />

		<label for="authNonMatchingMCC">Non matching Merchant Category Code (MCC) in authorisation and clearing record</label>
	</div>

	<div class="padded" id="elabAuthMCCInClearing">
		<label class="large" for="authMCCInClearing">Merchant Category Code in Clearing Record</label>

		<form:input id="authMCCInClearing" maxlength="4" path="elaborationInfo.authMCCInClearing" size="4" />
	</div>

	<div class="padded" id="elabAuthMCCInSystemAuthorisation">
		<label class="large" for="authMCCInSystemAuthorisation">Merchant Category Code (MCC) in authorisation message</label>

		<form:input id="authMCCInSystemAuthorisation" maxlength="4" path="elaborationInfo.authMCCInSystemAuthorisation" size="4" />
	</div>

	<div id="elabCardStatusChanged">
		<form:checkbox id="cardStatusChanged" path="elaborationInfo.cardStatusChanged" />

		<label for="cardStatusChanged">The card status was changed to lost, stolen or pick-up</label>
	</div>

	<%--<div id="elabAuthAccountNumberOnExceptionFileNegativeResponse">
		<form:checkbox id="authAccountNumberOnExceptionFileNegativeResponse" path="elaborationInfo.authAccountNumberOnExceptionFileNegativeResponse" />

		<label for="authAccountNumberOnExceptionFileNegativeResponse">Account Number was listed on the Exception File with a Negative Response on the Chargeback Central Processing Date</label>
	</div> --%>
</fieldset>

<fieldset id="elabInfoProcessingError">
	<legend>Elaboration Information</legend>

	<div id="elabProcessingErrorTransDate">
		<label class="large" for="processingErrorTransDate"><spring:message code="label.transaction.date" /></label>

		<form:input class="date-pick" id="processingErrorTransDate" path="elaborationInfo.processingErrorTransDate" value="${elaborationInfo.processingErrorTransDate}" />
	</div>

	<div id="elabProcessingErrorSettleDate">
		<label class="large" for="processingErrorSettleDate"><spring:message code="label.settlement.date" /></label>

		<form:input class="date-pick" id="processingErrorSettleDate" path="elaborationInfo.processingErrorSettleDate" value="${elaborationInfo.processingErrorSettleDate}" />
	</div>

	<div id="elabProcessingErrorCounterfeit">
		<form:checkbox id="processingErrorCounterfeit" path="elaborationInfo.processingErrorCounterfeit" />

		<label for="processingErrorCounterfeit"><spring:message code="label.counterfeit" /></label>
	</div>

	<div id="elabProcessingErrorAccountClosed">
		<form:checkbox id="processingErrorAccountClosed" path="elaborationInfo.processingErrorAccountClosed" />

		<label for="processingErrorAccountClosed"><spring:message code="label.account.closed" /></label>
	</div>

	<div id="elabProcessingErrorOtherFraud">
		<form:checkbox id="processingErrorOtherFraud" path="elaborationInfo.processingErrorOtherFraud" />

		<label for="processingErrorOtherFraud"><spring:message code="label.other.fraud" /></label>

		<a class="bold" href="javascript:;" title="<spring:message code="hover.elabinfo.other.fraud" />">?</a>
	</div>

	<div id="elabProcessingErrorPresentmentOlder180Days">
		<form:checkbox id="processingErrorPresentmentOlder180Days" path="elaborationInfo.processingErrorPresentmentOlder180Days" />

		<label for="processingErrorPresentmentOlder180Days"><spring:message code="label.presentment.older.180.days" /></label>
	</div>

	<div id="elabProcessingErrorTransProcessedAfterTransactionDate">
		<form:checkbox id="processingErrorTransProcessedAfterTransactionDate" path="elaborationInfo.processingErrorTransProcessedAfterTransactionDate" />

		<label for="processingErrorTransProcessedAfterTransactionDate"><spring:message code="label.transaction.processed.within.10.days" /></label>
	</div>

	<div id="elabProcessingErrorCardStatusChanged">
		<form:checkbox id="processingCardStatusChanged" path="elaborationInfo.processingErrorCardStatusChanged" />

		<label for="processingCardStatusChanged">The card status was changed to lost, stolen or pick-up.</label>
	</div>

	<div id="elabProcessingErrorTransactionIncorrect">
		<label><spring:message code="message.transaction.incorrect" /></label>

		<form:select cssClass="reset" path="elaborationInfo.processingErrorTransactionIncorrect">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="242"><spring:message code="label.credit.as.debit" /></form:option>
			<form:option value="243"><spring:message code="label.debit.as.credit" /></form:option>
			<form:option value="244"><spring:message code="label.purchase.as.cash" /></form:option>
			<form:option value="245"><spring:message code="label.cash.as.purchase" /></form:option>
			<form:option value="246"><spring:message code="label.credit.instead.of.reversal" /></form:option>
		</form:select>
	</div>

	<div id="elabProcessingErrorTransactionCurrencyDifferentTransmitted">
		<form:checkbox id="processingErrorTransactionCurrencyDifferentTransmitted" path="elaborationInfo.processingErrorTransactionCurrencyDifferentTransmitted" />

		<label for="processingErrorTransactionCurrencyDifferentTransmitted"><spring:message code="label.transaction.currency.different.transmitted" /></label>
	</div>

	<div class="padded" id="elabProcessingErrorCurrencyTransaction">
		<label class="large" for="processingErrorCurrencyTransaction"><spring:message code="label.transaction.currency" /></label>

		<form:input id="processingErrorCurrencyTransaction" path="elaborationInfo.processingErrorCurrencyTransaction" />
	</div>

	<div class="padded" id="elabProcessingErrorCurrencyTransmitted">
		<label class="large" for="processingErrorCurrencyTransmitted"><spring:message code="label.transmission.currency" /></label>

		<form:input id="processingErrorCurrencyTransmitted" path="elaborationInfo.processingErrorCurrencyTransmitted" />
	</div>

	<div id="elabProcessingErrorTransactionCountryDifferent">
		<form:checkbox id="processingErrorTransactionCountryDifferent" path="elaborationInfo.processingErrorTransactionCountryDifferent" />

		<label for="processingErrorTransactionCountryDifferent"><spring:message code="label.incorrect.country.code" /></label>

		<a class="bold" href="javascript:;" title="<spring:message code="hover.elabinfo.trans.different.country" />">?</a>
	</div>

	<div class="padded" id="elabProcessingErrorTransactionCountry">
		<label class="large" for="processingErrorTransactionCountry"><spring:message code="label.transaction.country" /></label>

		<form:input id="processingErrorTransactionCountry" path="elaborationInfo.processingErrorTransactionCountry" />
	</div>

	<div class="padded" id="elabProcessingErrorPortalCountry">
		<label class="large" for="processingErrorPortalCountry"><spring:message code="label.portal.country" /></label>

		<form:input id="processingErrorPortalCountry" path="elaborationInfo.processingErrorPortalCountry" />
	</div>

	<div id="elabProcessingErrorNonMatchingAccountNumber">
		<form:checkbox id="processingErrorNonMatchingAccountNumber" path="elaborationInfo.processingErrorNonMatchingAccountNumber" />

		<label for="processingErrorNonMatchingAccountNumber"><spring:message code="label.non.matching.account" /></label>
	</div>

	<div id="elabProcessingErrorNonExistingAccount">
		<form:checkbox id="processingErrorNonExistingAccount" path="elaborationInfo.processingErrorNonExistingAccount" />

		<label for="processingErrorNonExistingAccount"><spring:message code="label.non.existing.account" /></label>
	</div>

	<div id="elabProcessingErrorAccountNumberExceptionFile">
		<label>Processing Error Due To</label>

		<form:select cssClass="reset" id="processingErrorDueTo" path="elaborationInfo.processingErrorDueTo">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="281">Account is closed</form:option>
			<form:option value="282">Account is not on file</form:option>
			<form:option value="283">No such account number</form:option>
		</form:select>
	</div>

	<div id="elabProcessingErrorInfoIncorrect">
		<label>How is the information incorrect</label>

		<form:select cssClass="reset" id="infoIncorrect" path="elaborationInfo.infoIncorrect">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="286">Incorrect transaction amount</form:option>
			<form:option value="287">Incorrect card number</form:option>
		</form:select>

		<form:errors cssClass="error" path="elaborationInfo.infoIncorrect" />
	</div>

	<div class="padded" id="elabProcessingErrorAmountIncorrect">
		<label class="small">How is the amount incorrect</label>

		<form:select cssClass="reset" id="amountIncorrect" path="elaborationInfo.amountIncorrect">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="288">Total added incorrectly</form:option>
			<form:option value="289">Total altered by merchant</form:option>
		</form:select>

		<form:errors cssClass="error" path="elaborationInfo.amountIncorrect" />
	</div>

	<div class="padded" id="elabProcessingErrorIncorrectAmountWas">
		<label class="small" for="incorrectAmountWas">The incorrect amount was</label>

		<form:input id="incorrectAmountWas" path="elaborationInfo.incorrectAmountWas" />
	</div>

	<div class="padded" id="elabProcessingErrorIncorrectAmountShouldBe">
		<label class="small" for="incorrectAmountShouldBe">The correct amount should be</label>

		<form:input id="incorrectAmountShouldBe" path="elaborationInfo.incorrectAmountShouldBe" />
	</div>

	<%-- <div id="elabProcessingErrorCardNumber" class="padded">
		<label class="small-medium" for="cardNumber">Card Number on the Transaction</label>

		<form:input id="cardNumber" path="elaborationInfo.cardNumber" />
	</div> --%>

	<div class="padded" id="elabProcessingErrorNameOnVoucher">
		<label class="small-medium" for="nameOnVoucher">Name on Voucher</label>

		<form:input id="nameOnVoucher" path="elaborationInfo.nameOnVoucher" />
	</div>

	<%-- <div id="elabProcessingErrorIncorrectInformation">
		<p class="bold"><spring:message code="message.information.incorrect" /></p>

		<form:radiobutton id="incorrectTransactionAmount" path="elaborationInfo.processingErrorInformationIncorrect" value="" />

		<label for="incorrectTransactionAmount"><spring:message code="label.incorrect.transaction.amount" /></label>

		<form:radiobutton id="incorrectAccountNumber" path="elaborationInfo.processingErrorInformationIncorrect" value="" />

		<label for="incorrectAccountNumber"><spring:message code="label.incorrect.account.number" /></label>
	</div> --%>

	<div id="elabProcessingErrorARN">
		<label class="large" for="processingErrorARN"><spring:message code="label.arn.original.transaction" /></label>

		<form:input id="processingErrorARN" path="elaborationInfo.processingErrorARN" />
	</div>

	<div id="elabProcessingErrorTransactionDate">
		<label class="large" for="processingErrorTransactionDate"><spring:message code="label.transaction.date" /></label>

		<form:input cssClass="date-pick" id="processingErrorTransactionDate" path="elaborationInfo.processingErrorTransactionDate" />
	</div>

	<div id="elabProcessingErrorPaymentMethod">
		<label class="small" for="processingErrorPaymentMethod"><spring:message code="label.payment.method" /></label>

		<form:select cssClass="reset" id="processingErrorPaymentMethod" path="elaborationInfo.processingErrorPaymentMethod">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="257"><spring:message code="label.payment.cash" /></form:option>
			<form:option value="258"><spring:message code="label.payment.atm.debit" /></form:option>
			<form:option value="259"><spring:message code="label.payment.cheque" /></form:option>
			<form:option value="260"><spring:message code="label.payment.other.card" /></form:option>
		</form:select>

		<form:errors cssClass="error" path="elaborationInfo.processingErrorPaymentMethod" />
	</div>

	<div id="elabProcessingErrorProofOtherMeans">
		<form:checkbox id="processingErrorProofOtherMeans" path="elaborationInfo.processingErrorProofOtherMeans" />

		<label for="processingErrorProofOtherMeans"><spring:message code="label.proof.other.means" /></label>

		<a class="bold" href="javascript:;" title="<spring:message code="hover.elabinfo.proof.other.means" />">?</a>

		<form:errors cssClass="error" path="elaborationInfo.processingErrorProofOtherMeans" />
	</div>
</fieldset>

<fieldset id="elaborationInfoCancellation">
	<legend>Elaboration Information</legend>

	<div id="elabCancellationWhatWasPurchased">
		<label class="medium" for="whatWasPurchased">What was purchased</label>

		<form:select cssClass="reset" id="whatWasPurchased" path="elaborationInfo.whatWasPurchased">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="334">Other Services</form:option>
			<form:option value="294"><spring:message code="label.merchandise" /></form:option>
			<form:option value="295"><spring:message code="label.services" /></form:option>
			<form:option value="335">Advanced Deposit</form:option>
			<form:option value="336">TimeShare</form:option>
			<form:option value="337">Guaranteed Reservation</form:option>
		</form:select>
	</div>

	<div id="merchandiseSection">
		<div class="padded" id="elabCancellationMerchandiseWrong">
			<label class="medium" for="merchandiseWhatWasWrong">What was wrong</label>

			<form:select cssClass="reset" id="merchandiseWhatWasWrong" path="elaborationInfo.merchandiseWhatWasWrong">
				<form:option value="0"><spring:message code="label.please.select" /></form:option>
				<form:option value="296">Not as described</form:option>
				<form:option value="297">Defective</form:option>
				<form:option value="378">Quality</form:option>
				<form:option value="298">Counterfeit</form:option>
				<form:option value="299">Terms of sale misrepresented</form:option>
			</form:select>
		</div>

		<div id="merchandiseWhatWasWrongSection">
			<div class="padded" id="elabCancellationMerchandiseDetailsNotAsDescribed">
				<label class="large" for="merchandiseDetailsNotAsDescribed">Provide details why the Merchandise was not as described</label>

				<form:textarea id="merchandiseDetailsNotAsDescribed" length="500" path="elaborationInfo.merchandiseDetailsNotAsDescribed" />
			</div>

			<div class="padded" id="elabCancellationDetailsDefective">
				<label class="large" for="detailsDefective">Provide details why the Merchandise was defective</label>

				<form:textarea id="detailsDefective" length="500" path="elaborationInfo.detailsDefective" />
			</div>

			<div class="padded" id="elabCancellationExpertIdentified">
				<label for="expertIdentified">Explain which Agency or Expert identified the Merchandise as Counterfeit</label>

				<form:textarea id="expertIdentified" length="100" path="elaborationInfo.expertIdentified" />
			</div>

			<div class="padded" id="elabCancellationDateInformed">
				<label for="dateInformed">Date the cardholder was informed that the Merchandise was Counterfeit</label>

				<form:input cssClass="date-pick" id="dateInformed" path="elaborationInfo.dateInformed" />
			</div>

			<div class="padded" id="elabCancellationCounterfeitCertification">
				<form:checkbox id="counterfeitCertification" path="elaborationInfo.counterfeitCertification" />

				<label for="counterfeitCertification">Certification cardholder received notification that the Merchandise is Counterfeit</label>
			</div>

			<div class="padded" id="elabCancellationCounterfeitDocumentation">
				<form:checkbox id="counterfeitDocumentation" path="elaborationInfo.counterfeitDocumentation" />

				<label for="counterfeitDocumentation">Providing information or documentation to prove the Merchandise is Counterfeit</label>
			</div>

			<div class="padded" id="elabCancellationMerchandiseExplainTermsOfSale">
				<label class="large" for="merchandiseExplainTermsOfSale">Explain how the terms of sale were misrepresented</label>

				<form:textarea id="merchandiseExplainTermsOfSale" length="100" path="elaborationInfo.merchandiseExplainTermsOfSale" />
			</div>

			<div class="padded" id="elabCancellationMerchandiseAttachingIncidentReporting">
				<form:checkbox id="merchandiseAttachingIncidentReporting" path="elaborationInfo.merchandiseAttachingIncidentReporting" />

				<label for="merchandiseAttachingIncidentReporting">Attaching information about the reporting of the incident by the cardholder or issuer to a consumer protection, law enforcement, or comparable agency</label>
			</div>

			<div class="padded" id="elabCancellationMerchandiseQualityExplain">
				<label class="large" for="merchandiseQualityExplain">Provide details of the quality-related issue</label>

				<form:textarea id="merchandiseQualityExplain" length="500" path="elaborationInfo.merchandiseQualityExplain" />
			</div>
		</div>

		<div class="padded" id="elabCancellationMerchandiseOrdered">
			<label for="merchandiseOrdered">Description of merchandise ordered</label>

			<form:textarea id="merchandiseOrdered" length="200" path="elaborationInfo.merchandiseOrdered" />
		</div>

		<div class="padded" id="elabCancellationMerchandiseReturn">
			<label class="medium" for="merchandiseReturn">Was the Merchandise returned</label>

			<form:select cssClass="reset" id="merchandiseReturn" path="elaborationInfo.merchandiseReturn">
				<form:option value="0"><spring:message code="label.please.select" /></form:option>
				<form:option value="yes"><spring:message code="label.yes" /></form:option>
				<form:option value="no"><spring:message code="label.no" /></form:option>
			</form:select>
		</div>

		<div class="padded" id="elabCancellationWhyMerchandiseNotReturned">
			<label for="whyMerchandiseNotReturned">Why was it not returned and where is it currently</label>

			<form:textarea id="whyMerchandiseNotReturned" length="200" path="elaborationInfo.whyMerchandiseNotReturned" />
		</div>

		<div class="padded" id="elabCancellationMerchantRefuseReturn">
			<label for="merchantRefuse">Did the merchant refuse to accept returned merchandise or provide a return authorsation</label>

			<form:select cssClass="reset" id="merchantRefuse" path="elaborationInfo.merchantRefuse">
				<form:option value="0"><spring:message code="label.please.select" /></form:option>
				<form:option value="315"><spring:message code="label.yes" /></form:option>
				<form:option value="316"><spring:message code="label.no" /></form:option>
				<form:option value="317">Not applicable</form:option>
			</form:select>
		</div>

		<div class="padded" id="merchantRefusedReason">
			<label>The Merchant</label>

			<form:select cssClass="reset" id="merchantRefusedReason" path="elaborationInfo.merchantRefusedReason">
				<form:option value="0"><spring:message code="label.please.select" /></form:option>
				<form:option value="318">Refused to provide return authorisation</form:option>
				<form:option value="319">Refused to accept returned merchandise</form:option>
				<form:option value="320">Informed cardholder not to return the merchandise</form:option>
			</form:select>
		</div>

		<%-- <div id="elabCancellationMerchantRefusedAuthorisation" class="padded">
			<form:checkbox id="merchantRefusedAuthorisation" path="elaborationInfo.merchantRefusedAuthorisation" />

			<label for="merchantRefusedAuthorisation">Merchant refused to provide return authorisation</label>
		</div>

		<div class="padded" id="elabCancellationMerchantRefusedReturn">
			<form:checkbox id="merchantRefusedReturn" path="elaborationInfo.merchantRefusedReturn" />

			<label for="merchantRefusedReturn">Merchant refused to accept returned merchandise</label>
		</div>

		<div class="padded" id="elabCancellationMerchantAdvisedNoReturn">
			<form:checkbox id="merchantAdvisedNoReturn" path="elaborationInfo.merchantAdvisedNoReturn" />

			<label for="merchantAdvisedNoReturn">Merchant informed the cardholder not to return the Merchandise</label>
		</div> --%>
	</div>

	<div class="padded" id="elabCancellationMerchantBilledMore">
		<form:checkbox id="merchantBilledMore" path="elaborationInfo.merchantBilledMore" />

		<label for="merchantBilledMore">Merchant billed for more than 1 night for no show</label>
	</div>

	<div id="servicesSection">
		<div class="padded" id="elabCancellationServicesWrong">
			<label class="medium" for="servicesWhatWasWrong">What was wrong</label>

			<form:select cssClass="reset" id="servicesWhatWasWrong" path="elaborationInfo.servicesWhatWasWrong">
				<form:option value="0"><spring:message code="label.please.select" /></form:option>
				<form:option value="296">Not as described</form:option>
				<form:option value="299">Terms of sale misrepresented</form:option>
			</form:select>
		</div>

		<div id="servicesWhatWasWrongSection">
			<div class="padded" id="elabCancellationServicesDetailsNotAsDescribed">
				<label class="large" for="servicesDetailsNotAsDescribed">Provide details why the Service was not as described</label>

				<form:textarea id="servicesDetailsNotAsDescribed" length="500" path="elaborationInfo.servicesDetailsNotAsDescribed" />
			</div>

			<div class="padded" id="elabCancellationServicesExplainTermsOfSale">
				<label class="large" for="servicesExplainTermsOfSale">Explain how the terms of sale were misrepresented</label>

				<form:textarea id="servicesExplainTermsOfSale" length="200" path="elaborationInfo.servicesExplainTermsOfSale" />
			</div>

			<div class="padded" id="elabCancellationServicesAttachingIncidentReporting">
				<form:checkbox id="servicesAttachingIncidentReporting" path="elaborationInfo.servicesAttachingIncidentReporting" />

				<label for="servicesAttachingIncidentReporting">Attaching information about the reporting of the incident by the cardholder or issuer to a consumer protection, law enforcement, or comparable agency</label>
			</div>
		</div>

		<div class="padded" id="elabCancellationServicesOrdered">
			<label for="servicesOrdered">Description of services ordered</label>

			<form:textarea id="servicesOrdered" length="200" path="elaborationInfo.servicesOrdered" />
		</div>

		<div class="padded" id="elabCancellationExpectedServicesDate">
			<label for="expectedServicesDate">Cardholder expected to receive services on</label>

			<form:input cssClass="date-pick" id="expectedServicesDate" path="elaborationInfo.expectedServicesDate" />
		</div>
	</div>

	<div id="elabCancellationDidYouCancel">
		<label class="medium" for="didCancel">Did you cancel</label>

		<form:select cssClass="reset" id="didCancel" path="elaborationInfo.didCancel">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="Yes"><spring:message code="label.yes" /></form:option>
			<form:option value="No"><spring:message code="label.no" /></form:option>
			<form:option value="Not Applicable"><spring:message code="label.not.applicable" /></form:option>
		</form:select>
	</div>

	<div id="elabCancellationWorkRedone">
		<label for="workRedone">Did the cardholder pay to have the work re-done</label>

		<form:select cssClass="reset" id="workRedone" path="elaborationInfo.workRedone">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="Yes"><spring:message code="label.yes" /></form:option>
			<form:option value="No"><spring:message code="label.no" /></form:option>
			<form:option value="Not Applicable"><spring:message code="label.not.applicable" /></form:option>
		</form:select>
	</div>

	<div class="padded" id="showWorkRedoneDetails">
		<div id="questionOne">
			<label class="large" for="whereWorkRedone">Where did the cardholder have the work re-done</label>

			<form:input id="whereWorkRedone" path="elaborationInfo.whereWorkRedone" />
		</div>

		<div id="questionTwo">
			<label class="large" for="whenWorkRedone">When was the work re-done</label>

			<form:input cssClass="date-pick" id="whenWorkRedone" path="elaborationInfo.whenWorkRedone" />
		</div>

		<div id="questionThree">
			<label class="large" for="amountWorkRedone">What amount did the cardholder pay</label>

			<form:input id="amountWorkRedone" path="elaborationInfo.amountWorkRedone" />
		</div>
	</div>

	<div id="elabCancellationOriginalCreditNotAccepted">
		<label class="medium" for="originalCreditNotAccepted">An original credit was not accepted because either</label>

		<form:select cssClass="reset" id="originalCreditNotAccepted" path="elaborationInfo.originalCreditNotAccepted">
			<form:option value="0">&nbsp;</form:option>
			<form:option value="338">Recipient refused the original credit</form:option>
			<form:option value="339">Original credit is prohibited by local law</form:option>
		</form:select>
	</div>
</fieldset>

<fieldset id="elaborationInfoQuestionnaire">
	<legend>Elaboration Information</legend>

	<div id="elabQuestionnaireWhatWasPurchased">
		<label class="medium" for="questionnaireWhatWasPurchased">What was purchased</label>

		<form:select cssClass="reset" id="questionnaireWhatWasPurchased" path="elaborationInfo.questionnaireWhatWasPurchased">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="362">Merchandise or Ticket</form:option>
			<form:option value="295"><spring:message code="label.services" /></form:option>
		</form:select>
	</div>

	<div id="questionnaireMerchandiseSection">
		<div class="padded" id="elabQuestionnaireMerchandiseOrdered">
			<label for="questionnaireMerchandiseOrdered">Description of merchandise ordered</label>

			<form:textarea id="questionnaireMerchandiseOrdered" length="200" path="elaborationInfo.questionnaireMerchandiseOrdered" />
		</div>

		<div class="padded" id="elabQuestionnaireMerchandiseDateExpected">
			<label class="medium" for="questionnaireMerchandiseDateExpected">Date of expected receipt</label>

			<form:input class="date-pick" id="questionnaireMerchandiseDateExpected" path="elaborationInfo.questionnaireMerchandiseDateExpected" />
		</div>

		<div class="padded" id="elabQuestionnaireMerchandiseNotReceived">
			<form:checkbox id="questionnaireMerchandiseNotReceived" path="elaborationInfo.questionnaireMerchandiseNotReceived" />

			<label for="questionnaireMerchandiseNotReceived">Merchandise not received by agreed date</label>
		</div>

		<div class="padded" id="elabQuestionnaireMerchandiseReceivedOn">
			<label class="medium" for="questionnaireMerchandiseReceivedOn">Cardholder received merchandise on</label>

			<form:input class="date-pick" id="questionnaireMerchandiseReceivedOn" path="elaborationInfo.questionnaireMerchandiseReceivedOn" />
		</div>

		<div class="padded" id="elabQuestionnaireMerchandiseReturn">
			<label class="medium" for="questionnaireMerchandiseReturn">Was the Merchandise returned</label>

			<form:select cssClass="reset" id="questionnaireMerchandiseReturn" path="elaborationInfo.questionnaireMerchandiseReturn">
				<form:option value="0"><spring:message code="label.please.select" /></form:option>
				<form:option value="yes"><spring:message code="label.yes" /></form:option>
				<form:option value="no"><spring:message code="label.no" /></form:option>
			</form:select>
		</div>

		<div class="padded" id="elabQuestionnaireWhereIsMerchandise">
			<label for="questionnaireWhereIsMerchandise">Where is the merchandise currently</label>

			<form:textarea id="questionnaireWhereIsMerchandise" length="200" path="elaborationInfo.questionnaireWhereIsMerchandise" />
		</div>

		<div class="padded" id="elabQuestionnaireMerchandiseAgreedLocation">
			<label class="medium" for="questionnaireMerchandiseAgreedLocation">Agreed upon location for delivery of merchandise</label>

			<form:input id="questionnaireMerchandiseAgreedLocation" path="elaborationInfo.questionnaireMerchandiseAgreedLocation" />
		</div>

		<div class="padded" id="elabQuestionnaireWasMerchandiseCancelledNonReceipt">
			<label class="medium" for="questionnaireWasMerchandiseCancelledNonReceipt">Was the merchandise cancelled due to non-receipt</label>

			<form:select cssClass="reset" id="questionnaireWasMerchandiseCancelledNonReceipt" path="elaborationInfo.questionnaireWasMerchandiseCancelledNonReceipt">
				<form:option value="0"><spring:message code="label.please.select" /></form:option>
				<form:option value="yes"><spring:message code="label.yes" /></form:option>
				<form:option value="no"><spring:message code="label.no" /></form:option>
				<form:option value="applicable not">Not Applicable</form:option>
			</form:select>
		</div>
	</div>

	<div id="questionnaireServicesSection">
		<div class="padded" id="elabQuestionnaireServicesOrdered">
			<label for="questionnaireServicesOrdered">Describe services ordered</label>

			<form:textarea id="questionnaireServicesOrdered" length="200" path="elaborationInfo.questionnaireServicesOrdered" />
		</div>

		<div class="padded" id="elabQuestionnaireMerchantUnwilling">
			<form:checkbox id="questionnaireMerchantUnwilling" path="elaborationInfo.questionnaireMerchantUnwilling" />

			<label for="questionnaireMerchantUnwilling">Merchant unwilling or unable to provide service</label>
		</div>

		<div class="padded" id="elabQuestionnaireServicesDateExpected">
			<label class="medium" for="questionnaireServicesDateExpected">Date of expected receipt</label>

			<form:input class="date-pick" id="questionnaireServicesDateExpected" path="elaborationInfo.questionnaireServicesDateExpected" />
		</div>

		<div class="padded" id="elabQuestionnaireServicesNotReceived">
			<form:checkbox id="questionnaireServicesNotReceived" path="elaborationInfo.questionnaireServicesNotReceived" />

			<label for="questionnaireServicesNotReceived">Services not received by agreed date</label>
		</div>

		<div class="padded" id="elabQuestionnaireServicesReceivedOn">
			<label class="medium" for="questionnaireServicesReceivedOn">Cardholder received services on</label>

			<form:input class="date-pick" id="questionnaireServicesReceivedOn" path="elaborationInfo.questionnaireServicesReceivedOn" />
		</div>

		<div class="padded" id="elabQuestionnaireWasServiceCancelledNonReceipt">
			<label class="medium" for="questionnaireWasServicesCancelledNonReceipt">Was the service cancelled due to non-receipt</label>

			<form:select cssClass="reset" id="questionnaireWasServicesCancelledNonReceipt" path="elaborationInfo.questionnaireWasServicesCancelledNonReceipt">
				<form:option value="0"><spring:message code="label.please.select" /></form:option>
				<form:option value="yes"><spring:message code="label.yes" /></form:option>
				<form:option value="no"><spring:message code="label.no" /></form:option>
				<form:option value="applicable not">Not Applicable</form:option>
			</form:select>
		</div>
	</div>
</fieldset>