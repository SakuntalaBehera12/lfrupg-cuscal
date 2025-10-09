package au.com.cuscal.transactions.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;

@JsonAutoDetect
public class RequestDetails implements Serializable {

	public String getAcquirerId() {
		return acquirerId;
	}

	public String getAcquirerName() {
		return acquirerName;
	}

	public String getAcquirerSystemTrace() {
		return acquirerSystemTrace;
	}

	public String getCardAcceptorId() {
		return cardAcceptorId;
	}

	public String getCardAcceptorName() {
		return cardAcceptorName;
	}

	public String getCardAcceptorRetrievalReferenceNumber() {
		return cardAcceptorRetrievalReferenceNumber;
	}

	// Amounts

	public String getCardAcceptorTerminalId() {
		return cardAcceptorTerminalId;
	}

	public String getCardholderAmt() {
		return cardholderAmt;
	}

	public String getCashAmt() {
		return cashAmt;
	}

	public String getCurrencyCodeAcq() {
		return this.currencyCodeAcq;
	}

	public String getCurrencyCodeIss() {
		return this.currencyCodeIss;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	// Acquirer

	public String getFaultyCardReader() {
		return faultyCardReader;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public String getIsMobile() {
		return isMobile;
	}

	// Orignal Acquirer

	public String getIssuerId() {
		return issuerId;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public String getIssuerOrgName() {
		return issuerOrgName;
	}

	public String getIssuerRetrievalReferenceNumber() {
		return issuerRetrievalReferenceNumber;
	}

	// Date/Time

	public String getMaskedPan() {
		return maskedPan;
	}

	public String getOrignalAcquirerAtmOrPos() {
		return orignalAcquirerAtmOrPos;
	}

	public String getOrignalAcquirerId() {
		return orignalAcquirerId;
	}

	public String getOrignalAcquirerName() {
		return orignalAcquirerName;
	}

	public String getOrignalAcquirerTerminalId() {
		return orignalAcquirerTerminalId;
	}

	// Issuer

	public String getPan() {
		return pan;
	}

	public String getPinPresent() {
		return pinPresent;
	}

	public String getPosConditionCode() {
		return posConditionCode;
	}

	public String getPosConditionDescription() {
		return posConditionDescription;
	}

	// Card Acceptor

	public String getPosEntryDescription() {
		return posEntryDescription;
	}

	public String getPosEntryMode() {
		return posEntryMode;
	}

	public String getPosMerchantCode() {
		return posMerchantCode;
	}

	public String getPosMerchantDescription() {
		return posMerchantDescription;
	}

	// POS

	public String getSettlementDateTime() {
		return settlementDateTime;
	}

	public String getSwitchDateTime() {
		return switchDateTime;
	}

	/**
	 * @return the terminalCapabilty
	 */
	public String getTerminalCapability() {
		return terminalCapability;
	}

	public String getTransactionAmt() {
		return transactionAmt;
	}

	public String getTransactionDateTime() {
		return transactionDateTime;
	}

	public String getTransactionLocalDateTimeZone() {
		return transactionLocalDateTimeZone;
	}

	/* Project 8847 */
	public String getTransactionLocationDateTime() {
		return transactionLocationDateTime;
	}

	/* Project P00337*/
	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}

	public void setAcquirerName(String acquirerName) {
		this.acquirerName = acquirerName;
	}

	public void setAcquirerSystemTrace(String acquirerSystemTrace) {
		this.acquirerSystemTrace = acquirerSystemTrace;
	}

	public void setCardAcceptorId(String cardAcceptorId) {
		this.cardAcceptorId = cardAcceptorId;
	}

	public void setCardAcceptorName(String cardAcceptorName) {
		this.cardAcceptorName = cardAcceptorName;
	}

	public void setCardAcceptorRetrievalReferenceNumber(
		String cardAcceptorRetrievalReferenceNumber) {

		this.cardAcceptorRetrievalReferenceNumber =
			cardAcceptorRetrievalReferenceNumber;
	}

	public void setCardAcceptorTerminalId(String cardAcceptorTerminalId) {
		this.cardAcceptorTerminalId = cardAcceptorTerminalId;
	}

	public void setCardholderAmt(String cardholderAmt) {
		this.cardholderAmt = cardholderAmt;
	}

	public void setCashAmt(String cashAmt) {
		this.cashAmt = cashAmt;
	}

	public void setCurrencyCodeAcq(String currencyCode) {
		this.currencyCodeAcq = currencyCode;
	}

	public void setCurrencyCodeIss(String currencyCode) {
		this.currencyCodeIss = currencyCode;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setFaultyCardReader(String faultyCardReader) {
		this.faultyCardReader = faultyCardReader;
	}

	public String getPos3dsAuthentication() {
		return pos3dsAuthentication;
	}

	public void setPos3dsAuthentication(String pos3dsAuthentication) {
		this.pos3dsAuthentication = pos3dsAuthentication;
	}

	public String getPos3dsAuthDescription() {
		return pos3dsAuthDescription;
	}

	public void setPos3dsAuthDescription(String pos3dsAuthDescription) {
		this.pos3dsAuthDescription = pos3dsAuthDescription;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	public void setIsMobile(String isMobile) {
		this.isMobile = isMobile;
	}

	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public void setIssuerOrgName(String issuerOrgName) {
		this.issuerOrgName = issuerOrgName;
	}

	public void setIssuerRetrievalReferenceNumber(
		String issuerRetrievalReferenceNumber) {

		this.issuerRetrievalReferenceNumber = issuerRetrievalReferenceNumber;
	}

	public void setMaskedPan(String maskedPan) {
		this.maskedPan = maskedPan;
	}

	public void setOrignalAcquirerAtmOrPos(String orignalAcquirerAtmOrPos) {
		this.orignalAcquirerAtmOrPos = orignalAcquirerAtmOrPos;
	}

	public void setOrignalAcquirerId(String orignalAcquirerId) {
		this.orignalAcquirerId = orignalAcquirerId;
	}

	public void setOrignalAcquirerName(String orignalAcquirerName) {
		this.orignalAcquirerName = orignalAcquirerName;
	}

	public void setOrignalAcquirerTerminalId(String orignalAcquirerTerminalId) {
		this.orignalAcquirerTerminalId = orignalAcquirerTerminalId;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public void setPinPresent(String pinPresent) {
		this.pinPresent = pinPresent;
	}

	public void setPosConditionCode(String posConditionCode) {
		this.posConditionCode = posConditionCode;
	}

	public void setPosConditionDescription(String posConditionDescription) {
		this.posConditionDescription = posConditionDescription;
	}

	public void setPosEntryDescription(String posEntryDescription) {
		this.posEntryDescription = posEntryDescription;
	}

	public void setPosEntryMode(String posEntryMode) {
		this.posEntryMode = posEntryMode;
	}

	public void setPosMerchantCode(String posMerchantCode) {
		this.posMerchantCode = posMerchantCode;
	}

	public void setPosMerchantDescription(String posMerchantDescription) {
		this.posMerchantDescription = posMerchantDescription;
	}

	public void setSettlementDateTime(String settlementDateTime) {
		this.settlementDateTime = settlementDateTime;
	}

	public void setSwitchDateTime(String switchDateTime) {
		this.switchDateTime = switchDateTime;
	}

	/**
	 * @param terminalCapabilty the terminalCapabilty to set
	 */
	public void setTerminalCapability(String terminalCapability) {
		this.terminalCapability = terminalCapability;
	}

	public void setTransactionAmt(String transactionAmt) {
		this.transactionAmt = transactionAmt;
	}

	public void setTransactionDateTime(String transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public void setTransactionLocalDateTimeZone(
		String transactionLocalDateTimeZone) {

		this.transactionLocalDateTimeZone = transactionLocalDateTimeZone;
	}

	public void setTransactionLocationDateTime(
		String transactionLocationDateTime) {

		this.transactionLocationDateTime = transactionLocationDateTime;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -5470958226656726022L;

	private String acquirerId;
	private String acquirerName;
	private String acquirerSystemTrace;
	private String cardAcceptorId;
	private String cardAcceptorName;
	private String cardAcceptorRetrievalReferenceNumber;
	private String cardAcceptorTerminalId;
	private String cardholderAmt;
	private String cashAmt;
	private String currencyCodeAcq;
	private String currencyCodeIss;
	private String expiryDate;
	private String faultyCardReader;
	private String feeAmt;
	private String isMobile;
	private String issuerId;
	private String issuerName;
	private String issuerOrgName;
	private String issuerRetrievalReferenceNumber;
	private String maskedPan;
	private String orignalAcquirerAtmOrPos;
	private String orignalAcquirerId;
	private String orignalAcquirerName;
	private String orignalAcquirerTerminalId;
	private String pan;
	private String pinPresent;
	//POS
	private String posConditionCode;
	private String posConditionDescription;
	private String posEntryDescription;
	private String posEntryMode;
	private String posMerchantCode;
	private String posMerchantDescription;
	private String pos3dsAuthentication;
	private String pos3dsAuthDescription;

	private String settlementDateTime;
	private String switchDateTime;
	private String terminalCapability;

	/* Project 8847 */
	private String transactionAmt;
	private String transactionDateTime;
	private String transactionLocalDateTimeZone;
	private String transactionLocationDateTime;

}