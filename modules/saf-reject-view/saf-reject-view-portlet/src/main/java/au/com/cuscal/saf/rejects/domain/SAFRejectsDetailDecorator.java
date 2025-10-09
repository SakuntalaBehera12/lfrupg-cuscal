//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.saf.rejects.domain;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

public class SAFRejectsDetailDecorator implements Serializable {

	public String getAccount() {
		return this.account;
	}

	public String getAcquirerId() {
		return this.acquirerId;
	}

	public String getApprovalCode() {
		return this.approvalCode;
	}

	public String getAuthorisedBy() {
		return this.authorisedBy;
	}

	public String getBin() {
		return this.bin;
	}

	public String getBusinessDate() {
		return this.businessDate;
	}

	public BigDecimal getCardHolderBillingAmount() {
		return this.cardHolderBillingAmount;
	}

	public BigDecimal getCompletedAmount() {
		return this.completedAmount;
	}

	public String getDestinationInterchangeName() {
		return this.destinationInterchangeName;
	}

	public BigDecimal getFeeAmount() {
		return this.feeAmount;
	}

	public String getFinancialInstitution() {
		return this.financialInstitution;
	}

	public String getMessageType() {
		return this.messageType;
	}

	public String getPan() {
		return this.pan;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public String getRrn() {
		return this.rrn;
	}

	public String getSettlementDate() {
		return this.settlementDate;
	}

	public String getStan() {
		return this.stan;
	}

	public String getTerminalId() {
		return this.terminalId;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public BigDecimal getTransactionAmount() {
		return this.transactionAmount;
	}

	public Date getTransactionDateTime() {
		return this.transactionDateTime;
	}

	public String getTransactionReversed() {
		return this.transactionReversed;
	}

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setAccount(final String account) {
		this.account = account;
	}

	public void setAcquirerId(final String acquirerId) {
		this.acquirerId = acquirerId;
	}

	public void setApprovalCode(final String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public void setAuthorisedBy(final String authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	public void setBin(final String bin) {
		this.bin = bin;
	}

	public void setBusinessDate(final String businessDate) {
		this.businessDate = businessDate;
	}

	public void setCardHolderBillingAmount(
		final BigDecimal cardHolderBillingAmount) {

		this.cardHolderBillingAmount = cardHolderBillingAmount;
	}

	public void setCompletedAmount(final BigDecimal completedAmount) {
		this.completedAmount = completedAmount;
	}

	public void setDestinationInterchangeName(
		final String destinationInterchangeName) {

		this.destinationInterchangeName = destinationInterchangeName;
	}

	public void setFeeAmount(final BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public void setFinancialInstitution(final String financialInstitution) {
		this.financialInstitution = financialInstitution;
	}

	public void setMessageType(final String messageType) {
		this.messageType = messageType;
	}

	public void setPan(final String pan) {
		this.pan = pan;
	}

	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	public void setResponseCode(final String responseCode) {
		this.responseCode = responseCode;
	}

	public void setRrn(final String rrn) {
		this.rrn = rrn;
	}

	public void setSettlementDate(final String settlementDate) {
		this.settlementDate = settlementDate;
	}

	public void setStan(final String stan) {
		this.stan = stan;
	}

	public void setTerminalId(final String terminalId) {
		this.terminalId = terminalId;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setTransactionAmount(final BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public void setTransactionDateTime(final Date transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public void setTransactionReversed(final String transactionReversed) {
		this.transactionReversed = transactionReversed;
	}

	public void setTransactionType(final String transactionType) {
		this.transactionType = transactionType;
	}

	private static final long serialVersionUID = -6720280398392112710L;

	private String account;
	private String acquirerId;
	private String approvalCode;
	private String authorisedBy;
	private String bin;
	private String businessDate;
	private BigDecimal cardHolderBillingAmount;
	private BigDecimal completedAmount;
	private String destinationInterchangeName;
	private BigDecimal feeAmount;
	private String financialInstitution;
	private String messageType;
	private String pan;
	private String prefix;
	private String responseCode;
	private String rrn;
	private String settlementDate;
	private String stan;
	private String terminalId;
	private Date timestamp;
	private BigDecimal transactionAmount;
	private Date transactionDateTime;
	private String transactionReversed;
	private String transactionType;

}