package au.com.cuscal.chargeback.forms;

/**
 * @author jluu
 *
 */
public class ElaborationInfo {

	public String getAmountIncorrect() {
		return amountIncorrect;
	}

	public String getAmountWorkRedone() {
		return amountWorkRedone;
	}

	public String getAuthCrbDate() {
		return authCrbDate;
	}

	public String[] getAuthCrbRegions() {
		return authCrbRegions;
	}

	public String getAuthDate() {
		return authDate;
	}

	public String getAuthDeclinedDate() {
		return authDeclinedDate;
	}

	public String getAuthMCCInClearing() {
		return authMCCInClearing;
	}

	public String getAuthMCCInSystemAuthorisation() {
		return authMCCInSystemAuthorisation;
	}

	public String getAuthObtainedIncorrectDataExplain() {
		return authObtainedIncorrectDataExplain;
	}

	public String getAuthTransExceedsAuthAmountExplain() {
		return authTransExceedsAuthAmountExplain;
	}

	public String getCardDate() {
		return cardDate;
	}

	public String getCardExpiredDate() {
		return cardExpiredDate;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public String getDateInformed() {
		return dateInformed;
	}

	public String getDetailsDefective() {
		return detailsDefective;
	}

	public String getDidCancel() {
		return didCancel;
	}

	public String getExpectedServicesDate() {
		return expectedServicesDate;
	}

	public String getExpertIdentified() {
		return expertIdentified;
	}

	public String getFraudAdviceNo() {
		return fraudAdviceNo;
	}

	public String getFraudDate() {
		return fraudDate;
	}

	public String getIncorrectAmountShouldBe() {
		return incorrectAmountShouldBe;
	}

	public String getIncorrectAmountWas() {
		return incorrectAmountWas;
	}

	/*private boolean merchantRefusedAuthorisation;
	private boolean merchantRefusedReturn;
	private boolean merchantAdvisedNoReturn;*/
	public String getInfoIncorrect() {
		return infoIncorrect;
	}

	public String getMerchandiseDetailsNotAsDescribed() {
		return merchandiseDetailsNotAsDescribed;
	}

	public String getMerchandiseExplainTermsOfSale() {
		return merchandiseExplainTermsOfSale;
	}

	public String getMerchandiseOrdered() {
		return merchandiseOrdered;
	}

	public String getMerchandiseQualityExplain() {
		return merchandiseQualityExplain;
	}

	public String getMerchandiseReturn() {
		return merchandiseReturn;
	}

	public String getMerchandiseWhatWasWrong() {
		return merchandiseWhatWasWrong;
	}

	public String getMerchantRefuse() {
		return merchantRefuse;
	}

	public String getMerchantRefusedReason() {
		return merchantRefusedReason;
	}

	public String getNameOnVoucher() {
		return nameOnVoucher;
	}

	public String getOriginalCreditNotAccepted() {
		return originalCreditNotAccepted;
	}

	public String getProcessingErrorARN() {
		return processingErrorARN;
	}

	public String getProcessingErrorCurrencyTransaction() {
		return processingErrorCurrencyTransaction;
	}

	public String getProcessingErrorCurrencyTransmitted() {
		return processingErrorCurrencyTransmitted;
	}

	public String getProcessingErrorDueTo() {
		return processingErrorDueTo;
	}

	public String getProcessingErrorInformationIncorrect() {
		return processingErrorInformationIncorrect;
	}

	public String getProcessingErrorPaymentMethod() {
		return processingErrorPaymentMethod;
	}

	public String getProcessingErrorPortalCountry() {
		return processingErrorPortalCountry;
	}

	public String getProcessingErrorSettleDate() {
		return processingErrorSettleDate;
	}

	public String getProcessingErrorTransactionCountry() {
		return processingErrorTransactionCountry;
	}

	public String getProcessingErrorTransactionDate() {
		return processingErrorTransactionDate;
	}

	public String getProcessingErrorTransactionIncorrect() {
		return processingErrorTransactionIncorrect;
	}

	public String getProcessingErrorTransDate() {
		return processingErrorTransDate;
	}

	public String getQuestionnaireMerchandiseAgreedLocation() {
		return questionnaireMerchandiseAgreedLocation;
	}

	public String getQuestionnaireMerchandiseDateExpected() {
		return questionnaireMerchandiseDateExpected;
	}

	public String getQuestionnaireMerchandiseOrdered() {
		return questionnaireMerchandiseOrdered;
	}

	public String getQuestionnaireMerchandiseReceivedOn() {
		return questionnaireMerchandiseReceivedOn;
	}

	public String getQuestionnaireMerchandiseReturn() {
		return questionnaireMerchandiseReturn;
	}

	public String getQuestionnaireServicesDateExpected() {
		return questionnaireServicesDateExpected;
	}

	public String getQuestionnaireServicesOrdered() {
		return questionnaireServicesOrdered;
	}

	public String getQuestionnaireServicesReceivedOn() {
		return questionnaireServicesReceivedOn;
	}

	public String getQuestionnaireWasMerchandiseCancelledNonReceipt() {
		return questionnaireWasMerchandiseCancelledNonReceipt;
	}

	public String getQuestionnaireWasServicesCancelledNonReceipt() {
		return questionnaireWasServicesCancelledNonReceipt;
	}

	public String getQuestionnaireWhatWasPurchased() {
		return questionnaireWhatWasPurchased;
	}

	public String getQuestionnaireWhereIsMerchandise() {
		return questionnaireWhereIsMerchandise;
	}

	public String getServicesDetailsNotAsDescribed() {
		return servicesDetailsNotAsDescribed;
	}

	public String getServicesExplainTermsOfSale() {
		return servicesExplainTermsOfSale;
	}

	public String getServicesOrdered() {
		return servicesOrdered;
	}

	public String getServicesWhatWasWrong() {
		return servicesWhatWasWrong;
	}

	public String getVisaEuropeOnlyDate() {
		return visaEuropeOnlyDate;
	}

	public String getWhatWasPurchased() {
		return whatWasPurchased;
	}

	public String getWhenWorkRedone() {
		return whenWorkRedone;
	}

	public String getWhereWorkRedone() {
		return whereWorkRedone;
	}

	public String getWhyMerchandiseNotReturned() {
		return whyMerchandiseNotReturned;
	}

	public String getWorkRedone() {
		return workRedone;
	}

	public boolean isAcctNoFictitiousOrInvalid() {
		return acctNoFictitiousOrInvalid;
	}

	public boolean isAuthAccountNumberOnExceptionFile() {
		return authAccountNumberOnExceptionFile;
	}

	public boolean isAuthNonMatchingMCC() {
		return authNonMatchingMCC;
	}

	public boolean isAuthObtainedIncorrectData() {
		return authObtainedIncorrectData;
	}

	public boolean isAuthRequiredNoTransDate() {
		return authRequiredNoTransDate;
	}

	public boolean isAuthTransExceedsAuthAmount() {
		return authTransExceedsAuthAmount;
	}

	public boolean isCardStatusChanged() {
		return cardStatusChanged;
	}

	public boolean isCounterfeitCertification() {
		return counterfeitCertification;
	}

	public boolean isCounterfeitDocumentation() {
		return counterfeitDocumentation;
	}

	public boolean isMerchandiseAttachingIncidentReporting() {
		return merchandiseAttachingIncidentReporting;
	}

	public boolean isMerchantBilledMore() {
		return merchantBilledMore;
	}

	public boolean isMsrtt() {
		return msrtt;
	}

	public boolean isProcessingErrorAccountClosed() {
		return processingErrorAccountClosed;
	}

	public boolean isProcessingErrorAccountNumberExceptionFile() {
		return processingErrorAccountNumberExceptionFile;
	}

	public boolean isProcessingErrorAccountNumberNegativeResponse() {
		return processingErrorAccountNumberNegativeResponse;
	}

	public boolean isProcessingErrorCardStatusChanged() {
		return processingErrorCardStatusChanged;
	}

	public boolean isProcessingErrorCounterfeit() {
		return processingErrorCounterfeit;
	}

	public boolean isProcessingErrorNonExistingAccount() {
		return processingErrorNonExistingAccount;
	}

	public boolean isProcessingErrorNonMatchingAccountNumber() {
		return processingErrorNonMatchingAccountNumber;
	}

	public boolean isProcessingErrorOtherFraud() {
		return processingErrorOtherFraud;
	}

	public boolean isProcessingErrorPresentmentOlder180Days() {
		return processingErrorPresentmentOlder180Days;
	}

	public boolean isProcessingErrorProofOtherMeans() {
		return processingErrorProofOtherMeans;
	}

	public boolean isProcessingErrorTransactionCountryDifferent() {
		return processingErrorTransactionCountryDifferent;
	}

	public boolean isProcessingErrorTransactionCurrencyDifferentTransmitted() {
		return processingErrorTransactionCurrencyDifferentTransmitted;
	}

	public boolean isProcessingErrorTransProcessedAfterTransactionDate() {
		return processingErrorTransProcessedAfterTransactionDate;
	}

	public boolean isQuestionnaireMerchandiseNotReceived() {
		return questionnaireMerchandiseNotReceived;
	}

	public boolean isQuestionnaireMerchantUnwilling() {
		return questionnaireMerchantUnwilling;
	}

	public boolean isQuestionnaireServicesNotReceived() {
		return questionnaireServicesNotReceived;
	}

	public boolean isServicesAttachingIncidentReporting() {
		return servicesAttachingIncidentReporting;
	}

	public boolean isVisaEuropeOnlyData() {
		return visaEuropeOnlyData;
	}

	public void setAcctNoFictitiousOrInvalid(
		boolean acctNoFictitiousOrInvalid) {

		this.acctNoFictitiousOrInvalid = acctNoFictitiousOrInvalid;
	}

	public void setAmountIncorrect(String amountIncorrect) {
		this.amountIncorrect = amountIncorrect;
	}

	public void setAmountWorkRedone(String amountWorkRedone) {
		this.amountWorkRedone = amountWorkRedone;
	}

	public void setAuthAccountNumberOnExceptionFile(
		boolean authAccountNumberOnExceptionFile) {

		this.authAccountNumberOnExceptionFile =
			authAccountNumberOnExceptionFile;
	}

	public void setAuthCrbDate(String authCrbDate) {
		this.authCrbDate = authCrbDate;
	}

	public void setAuthCrbRegions(String[] authCrbRegions) {
		this.authCrbRegions = authCrbRegions;
	}

	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}

	public void setAuthDeclinedDate(String authDeclinedDate) {
		this.authDeclinedDate = authDeclinedDate;
	}

	public void setAuthMCCInClearing(String authMCCInClearing) {
		this.authMCCInClearing = authMCCInClearing;
	}

	public void setAuthMCCInSystemAuthorisation(
		String authMCCInSystemAuthorisation) {

		this.authMCCInSystemAuthorisation = authMCCInSystemAuthorisation;
	}

	public void setAuthNonMatchingMCC(boolean authNonMatchingMCC) {
		this.authNonMatchingMCC = authNonMatchingMCC;
	}

	public void setAuthObtainedIncorrectData(
		boolean authObtainedIncorrectData) {

		this.authObtainedIncorrectData = authObtainedIncorrectData;
	}

	public void setAuthObtainedIncorrectDataExplain(
		String authObtainedIncorrectDataExplain) {

		this.authObtainedIncorrectDataExplain =
			authObtainedIncorrectDataExplain;
	}

	public void setAuthRequiredNoTransDate(boolean authRequiredNoTransDate) {
		this.authRequiredNoTransDate = authRequiredNoTransDate;
	}

	public void setAuthTransExceedsAuthAmount(
		boolean authTransExceedsAuthAmount) {

		this.authTransExceedsAuthAmount = authTransExceedsAuthAmount;
	}

	public void setAuthTransExceedsAuthAmountExplain(
		String authTransExceedsAuthAmountExplain) {

		this.authTransExceedsAuthAmountExplain =
			authTransExceedsAuthAmountExplain;
	}

	public void setCardDate(String cardDate) {
		this.cardDate = cardDate;
	}

	public void setCardExpiredDate(String cardExpiredDate) {
		this.cardExpiredDate = cardExpiredDate;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public void setCardStatusChanged(boolean cardStatusChanged) {
		this.cardStatusChanged = cardStatusChanged;
	}

	public void setCounterfeitCertification(boolean counterfeitCertification) {
		this.counterfeitCertification = counterfeitCertification;
	}

	public void setCounterfeitDocumentation(boolean counterfeitDocumentation) {
		this.counterfeitDocumentation = counterfeitDocumentation;
	}

	public void setDateInformed(String dateInformed) {
		this.dateInformed = dateInformed;
	}

	public void setDetailsDefective(String detailsDefective) {
		this.detailsDefective = detailsDefective;
	}

	public void setDidCancel(String didCancel) {
		this.didCancel = didCancel;
	}

	public void setExpectedServicesDate(String expectedServicesDate) {
		this.expectedServicesDate = expectedServicesDate;
	}

	public void setExpertIdentified(String expertIdentified) {
		this.expertIdentified = expertIdentified;
	}

	public void setFraudAdviceNo(String fraudAdviceNo) {
		this.fraudAdviceNo = fraudAdviceNo;
	}

	public void setFraudDate(String fraudDate) {
		this.fraudDate = fraudDate;
	}

	public void setIncorrectAmountShouldBe(String incorrectAmountShouldBe) {
		this.incorrectAmountShouldBe = incorrectAmountShouldBe;
	}

	public void setIncorrectAmountWas(String incorrectAmountWas) {
		this.incorrectAmountWas = incorrectAmountWas;
	}

	public void setInfoIncorrect(String infoIncorrect) {
		this.infoIncorrect = infoIncorrect;
	}

	public void setMerchandiseAttachingIncidentReporting(
		boolean merchandiseAttachingIncidentReporting) {

		this.merchandiseAttachingIncidentReporting =
			merchandiseAttachingIncidentReporting;
	}

	public void setMerchandiseDetailsNotAsDescribed(
		String merchandiseDetailsNotAsDescribed) {

		this.merchandiseDetailsNotAsDescribed =
			merchandiseDetailsNotAsDescribed;
	}

	public void setMerchandiseExplainTermsOfSale(
		String merchandiseExplainTermsOfSale) {

		this.merchandiseExplainTermsOfSale = merchandiseExplainTermsOfSale;
	}

	public void setMerchandiseOrdered(String merchandiseOrdered) {
		this.merchandiseOrdered = merchandiseOrdered;
	}

	public void setMerchandiseQualityExplain(String merchandiseQualityExplain) {
		this.merchandiseQualityExplain = merchandiseQualityExplain;
	}

	public void setMerchandiseReturn(String merchandiseReturn) {
		this.merchandiseReturn = merchandiseReturn;
	}

	public void setMerchandiseWhatWasWrong(String merchandiseWhatWasWrong) {
		this.merchandiseWhatWasWrong = merchandiseWhatWasWrong;
	}

	public void setMerchantBilledMore(boolean merchantBilledMore) {
		this.merchantBilledMore = merchantBilledMore;
	}

	public void setMerchantRefuse(String merchantRefuse) {
		this.merchantRefuse = merchantRefuse;
	}

	public void setMerchantRefusedReason(String merchantRefusedReason) {
		this.merchantRefusedReason = merchantRefusedReason;
	}

	public void setMsrtt(boolean msrtt) {
		this.msrtt = msrtt;
	}

	public void setNameOnVoucher(String nameOnVoucher) {
		this.nameOnVoucher = nameOnVoucher;
	}

	public void setOriginalCreditNotAccepted(String originalCreditNotAccepted) {
		this.originalCreditNotAccepted = originalCreditNotAccepted;
	}

	public void setProcessingErrorAccountClosed(
		boolean processingErrorAccountClosed) {

		this.processingErrorAccountClosed = processingErrorAccountClosed;
	}

	public void setProcessingErrorAccountNumberExceptionFile(
		boolean processingErrorAccountNumberExceptionFile) {

		this.processingErrorAccountNumberExceptionFile =
			processingErrorAccountNumberExceptionFile;
	}

	public void setProcessingErrorAccountNumberNegativeResponse(
		boolean processingErrorAccountNumberNegativeResponse) {

		this.processingErrorAccountNumberNegativeResponse =
			processingErrorAccountNumberNegativeResponse;
	}

	public void setProcessingErrorARN(String processingErrorARN) {
		this.processingErrorARN = processingErrorARN;
	}

	public void setProcessingErrorCardStatusChanged(
		boolean processingErrorCardStatusChanged) {

		this.processingErrorCardStatusChanged =
			processingErrorCardStatusChanged;
	}

	public void setProcessingErrorCounterfeit(
		boolean processingErrorCounterfeit) {

		this.processingErrorCounterfeit = processingErrorCounterfeit;
	}

	public void setProcessingErrorCurrencyTransaction(
		String processingErrorCurrencyTransaction) {

		this.processingErrorCurrencyTransaction =
			processingErrorCurrencyTransaction;
	}

	public void setProcessingErrorCurrencyTransmitted(
		String processingErrorCurrencyTransmitted) {

		this.processingErrorCurrencyTransmitted =
			processingErrorCurrencyTransmitted;
	}

	public void setProcessingErrorDueTo(String processingErrorDueTo) {
		this.processingErrorDueTo = processingErrorDueTo;
	}

	public void setProcessingErrorInformationIncorrect(
		String processingErrorInformationIncorrect) {

		this.processingErrorInformationIncorrect =
			processingErrorInformationIncorrect;
	}

	public void setProcessingErrorNonExistingAccount(
		boolean processingErrorNonExistingAccount) {

		this.processingErrorNonExistingAccount =
			processingErrorNonExistingAccount;
	}

	public void setProcessingErrorNonMatchingAccountNumber(
		boolean processingErrorNonMatchingAccountNumber) {

		this.processingErrorNonMatchingAccountNumber =
			processingErrorNonMatchingAccountNumber;
	}

	public void setProcessingErrorOtherFraud(
		boolean processingErrorOtherFraud) {

		this.processingErrorOtherFraud = processingErrorOtherFraud;
	}

	public void setProcessingErrorPaymentMethod(
		String processingErrorPaymentMethod) {

		this.processingErrorPaymentMethod = processingErrorPaymentMethod;
	}

	public void setProcessingErrorPortalCountry(
		String processingErrorPortalCountry) {

		this.processingErrorPortalCountry = processingErrorPortalCountry;
	}

	public void setProcessingErrorPresentmentOlder180Days(
		boolean processingErrorPresentmentOlder180Days) {

		this.processingErrorPresentmentOlder180Days =
			processingErrorPresentmentOlder180Days;
	}

	public void setProcessingErrorProofOtherMeans(
		boolean processingErrorProofOtherMeans) {

		this.processingErrorProofOtherMeans = processingErrorProofOtherMeans;
	}

	public void setProcessingErrorSettleDate(String processingErrorSettleDate) {
		this.processingErrorSettleDate = processingErrorSettleDate;
	}

	public void setProcessingErrorTransactionCountry(
		String processingErrorTransactionCountry) {

		this.processingErrorTransactionCountry =
			processingErrorTransactionCountry;
	}

	public void setProcessingErrorTransactionCountryDifferent(
		boolean processingErrorTransactionCountryDifferent) {

		this.processingErrorTransactionCountryDifferent =
			processingErrorTransactionCountryDifferent;
	}

	public void setProcessingErrorTransactionCurrencyDifferentTransmitted(
		boolean processingErrorTransactionCurrencyDifferentTransmitted) {

		this.processingErrorTransactionCurrencyDifferentTransmitted =
			processingErrorTransactionCurrencyDifferentTransmitted;
	}

	public void setProcessingErrorTransactionDate(
		String processingErrorTransactionDate) {

		this.processingErrorTransactionDate = processingErrorTransactionDate;
	}

	public void setProcessingErrorTransactionIncorrect(
		String processingErrorTransactionIncorrect) {

		this.processingErrorTransactionIncorrect =
			processingErrorTransactionIncorrect;
	}

	public void setProcessingErrorTransDate(String processingErrorTransDate) {
		this.processingErrorTransDate = processingErrorTransDate;
	}

	public void setProcessingErrorTransProcessedAfterTransactionDate(
		boolean processingErrorTransProcessedAfterTransactionDate) {

		this.processingErrorTransProcessedAfterTransactionDate =
			processingErrorTransProcessedAfterTransactionDate;
	}

	public void setQuestionnaireMerchandiseAgreedLocation(
		String questionnaireMerchandiseAgreedLocation) {

		this.questionnaireMerchandiseAgreedLocation =
			questionnaireMerchandiseAgreedLocation;
	}

	public void setQuestionnaireMerchandiseDateExpected(
		String questionnaireMerchandiseDateExpected) {

		this.questionnaireMerchandiseDateExpected =
			questionnaireMerchandiseDateExpected;
	}

	public void setQuestionnaireMerchandiseNotReceived(
		boolean questionnaireMerchandiseNotReceived) {

		this.questionnaireMerchandiseNotReceived =
			questionnaireMerchandiseNotReceived;
	}

	public void setQuestionnaireMerchandiseOrdered(
		String questionnaireMerchandiseOrdered) {

		this.questionnaireMerchandiseOrdered = questionnaireMerchandiseOrdered;
	}

	public void setQuestionnaireMerchandiseReceivedOn(
		String questionnaireMerchandiseReceivedOn) {

		this.questionnaireMerchandiseReceivedOn =
			questionnaireMerchandiseReceivedOn;
	}

	public void setQuestionnaireMerchandiseReturn(
		String questionnaireMerchandiseReturn) {

		this.questionnaireMerchandiseReturn = questionnaireMerchandiseReturn;
	}

	public void setQuestionnaireMerchantUnwilling(
		boolean questionnaireMerchantUnwilling) {

		this.questionnaireMerchantUnwilling = questionnaireMerchantUnwilling;
	}

	public void setQuestionnaireServicesDateExpected(
		String questionnaireServicesDateExpected) {

		this.questionnaireServicesDateExpected =
			questionnaireServicesDateExpected;
	}

	public void setQuestionnaireServicesNotReceived(
		boolean questionnaireServicesNotReceived) {

		this.questionnaireServicesNotReceived =
			questionnaireServicesNotReceived;
	}

	public void setQuestionnaireServicesOrdered(
		String questionnaireServicesOrdered) {

		this.questionnaireServicesOrdered = questionnaireServicesOrdered;
	}

	public void setQuestionnaireServicesReceivedOn(
		String questionnaireServicesReceivedOn) {

		this.questionnaireServicesReceivedOn = questionnaireServicesReceivedOn;
	}

	public void setQuestionnaireWasMerchandiseCancelledNonReceipt(
		String questionnaireWasMerchandiseCancelledNonReceipt) {

		this.questionnaireWasMerchandiseCancelledNonReceipt =
			questionnaireWasMerchandiseCancelledNonReceipt;
	}

	public void setQuestionnaireWasServicesCancelledNonReceipt(
		String questionnaireWasServicesCancelledNonReceipt) {

		this.questionnaireWasServicesCancelledNonReceipt =
			questionnaireWasServicesCancelledNonReceipt;
	}

	public void setQuestionnaireWhatWasPurchased(
		String questionnaireWhatWasPurchased) {

		this.questionnaireWhatWasPurchased = questionnaireWhatWasPurchased;
	}

	public void setQuestionnaireWhereIsMerchandise(
		String questionnaireWhereIsMerchandise) {

		this.questionnaireWhereIsMerchandise = questionnaireWhereIsMerchandise;
	}

	public void setServicesAttachingIncidentReporting(
		boolean servicesAttachingIncidentReporting) {

		this.servicesAttachingIncidentReporting =
			servicesAttachingIncidentReporting;
	}

	public void setServicesDetailsNotAsDescribed(
		String servicesDetailsNotAsDescribed) {

		this.servicesDetailsNotAsDescribed = servicesDetailsNotAsDescribed;
	}

	public void setServicesExplainTermsOfSale(
		String servicesExplainTermsOfSale) {

		this.servicesExplainTermsOfSale = servicesExplainTermsOfSale;
	}

	public void setServicesOrdered(String servicesOrdered) {
		this.servicesOrdered = servicesOrdered;
	}

	public void setServicesWhatWasWrong(String servicesWhatWasWrong) {
		this.servicesWhatWasWrong = servicesWhatWasWrong;
	}

	public void setVisaEuropeOnlyData(boolean visaEuropeOnlyData) {
		this.visaEuropeOnlyData = visaEuropeOnlyData;
	}

	public void setVisaEuropeOnlyDate(String visaEuropeOnlyDate) {
		this.visaEuropeOnlyDate = visaEuropeOnlyDate;
	}

	public void setWhatWasPurchased(String whatWasPurchased) {
		this.whatWasPurchased = whatWasPurchased;
	}

	public void setWhenWorkRedone(String whenWorkRedone) {
		this.whenWorkRedone = whenWorkRedone;
	}

	public void setWhereWorkRedone(String whereWorkRedone) {
		this.whereWorkRedone = whereWorkRedone;
	}

	public void setWhyMerchandiseNotReturned(String whyMerchandiseNotReturned) {
		this.whyMerchandiseNotReturned = whyMerchandiseNotReturned;
	}

	public void setWorkRedone(String workRedone) {
		this.workRedone = workRedone;
	}

	public String[] authCrbRegions;

	private boolean acctNoFictitiousOrInvalid;
	private String amountIncorrect;
	private String amountWorkRedone;
	private boolean authAccountNumberOnExceptionFile;
	private String authCrbDate;
	private String authDate;
	private String authDeclinedDate;
	private String authMCCInClearing;
	private String authMCCInSystemAuthorisation;
	private boolean authNonMatchingMCC;
	private boolean authObtainedIncorrectData;
	private String authObtainedIncorrectDataExplain;
	private boolean authRequiredNoTransDate;
	private boolean authTransExceedsAuthAmount;
	private String authTransExceedsAuthAmountExplain;
	private String cardDate;
	private String cardExpiredDate;
	private String cardNumber;
	private String cardStatus;
	private boolean cardStatusChanged;
	private boolean counterfeitCertification;
	private boolean counterfeitDocumentation;
	private String dateInformed;

	/*public boolean isMerchantRefusedAuthorisation() {
		return merchantRefusedAuthorisation;
	}

	public void setMerchantRefusedAuthorisation(boolean merchantRefusedAuthorisation) {
		this.merchantRefusedAuthorisation = merchantRefusedAuthorisation;
	}

	public boolean isMerchantRefusedReturn() {
		return merchantRefusedReturn;
	}

	public void setMerchantRefusedReturn(boolean merchantRefusedReturn) {
		this.merchantRefusedReturn = merchantRefusedReturn;
	}

	public boolean isMerchantAdvisedNoReturn() {
		return merchantAdvisedNoReturn;
	}

	public void setMerchantAdvisedNoReturn(boolean merchantAdvisedNoReturn) {
		this.merchantAdvisedNoReturn = merchantAdvisedNoReturn;
	}*/

	private String detailsDefective;
	private String didCancel;
	private String expectedServicesDate;
	private String expertIdentified;
	private String fraudAdviceNo;
	private String fraudDate;
	private String incorrectAmountShouldBe;
	private String incorrectAmountWas;
	private String infoIncorrect;
	private boolean merchandiseAttachingIncidentReporting;
	private String merchandiseDetailsNotAsDescribed;
	private String merchandiseExplainTermsOfSale;
	private String merchandiseOrdered;
	private String merchandiseQualityExplain;
	private String merchandiseReturn;
	private String merchandiseWhatWasWrong;
	private boolean merchantBilledMore;
	private String merchantRefuse;
	private String merchantRefusedReason;
	private boolean msrtt;
	private String nameOnVoucher;
	private String originalCreditNotAccepted;
	private boolean processingErrorAccountClosed;
	private boolean processingErrorAccountNumberExceptionFile;
	private boolean processingErrorAccountNumberNegativeResponse;
	private String processingErrorARN;
	private boolean processingErrorCardStatusChanged;
	private boolean processingErrorCounterfeit;
	private String processingErrorCurrencyTransaction;
	private String processingErrorCurrencyTransmitted;
	private String processingErrorDueTo;
	private String processingErrorInformationIncorrect;
	private boolean processingErrorNonExistingAccount;
	private boolean processingErrorNonMatchingAccountNumber;
	private boolean processingErrorOtherFraud;
	private String processingErrorPaymentMethod;
	private String processingErrorPortalCountry;
	private boolean processingErrorPresentmentOlder180Days;
	private boolean processingErrorProofOtherMeans;
	private String processingErrorSettleDate;
	private String processingErrorTransactionCountry;
	private boolean processingErrorTransactionCountryDifferent;
	private boolean processingErrorTransactionCurrencyDifferentTransmitted;
	private String processingErrorTransactionDate;
	private String processingErrorTransactionIncorrect;
	private String processingErrorTransDate;
	private boolean processingErrorTransProcessedAfterTransactionDate;
	private String questionnaireMerchandiseAgreedLocation;
	private String questionnaireMerchandiseDateExpected;
	private boolean questionnaireMerchandiseNotReceived;
	private String questionnaireMerchandiseOrdered;
	private String questionnaireMerchandiseReceivedOn;
	private String questionnaireMerchandiseReturn;
	private boolean questionnaireMerchantUnwilling;
	private String questionnaireServicesDateExpected;
	private boolean questionnaireServicesNotReceived;
	private String questionnaireServicesOrdered;
	private String questionnaireServicesReceivedOn;
	private String questionnaireWasMerchandiseCancelledNonReceipt;
	private String questionnaireWasServicesCancelledNonReceipt;
	private String questionnaireWhatWasPurchased;
	private String questionnaireWhereIsMerchandise;
	private boolean servicesAttachingIncidentReporting;
	private String servicesDetailsNotAsDescribed;
	private String servicesExplainTermsOfSale;
	private String servicesOrdered;
	private String servicesWhatWasWrong;
	private boolean visaEuropeOnlyData;
	private String visaEuropeOnlyDate;
	private String whatWasPurchased;
	private String whenWorkRedone;
	private String whereWorkRedone;
	private String whyMerchandiseNotReturned;
	private String workRedone;

}