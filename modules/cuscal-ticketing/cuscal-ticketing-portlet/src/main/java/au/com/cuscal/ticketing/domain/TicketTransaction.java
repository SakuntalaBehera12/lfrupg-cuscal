//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.domain;

import java.math.BigDecimal;

import java.util.Date;

public class TicketTransaction {

	public String getAcquirerId() {
		return this.acquirerId;
	}

	public String getAcquirerName() {
		return this.acquirerName;
	}

	public BigDecimal getAmountReceived() {
		return this.amountReceived;
	}

	public String getArnId() {
		return this.arnId;
	}

	public BigDecimal getAtmFeeAmount() {
		return this.atmFeeAmount;
	}

	public String getAtmFeeAmountCurrency() {
		return this.atmFeeAmountCurrency;
	}

	public String getAtmPos() {
		return this.atmPos;
	}

	public String getCardholderAmount() {
		return this.cardholderAmount;
	}

	public String getCardholderAmountCurrency() {
		return this.cardholderAmountCurrency;
	}

	public BigDecimal getClaimAmount() {
		return this.claimAmount;
	}

	public String getClaimAmountCurrency() {
		return this.claimAmountCurrency;
	}

	public Long getIssuerId() {
		return this.issuerId;
	}

	public String getIssuerName() {
		return this.issuerName;
	}

	public String getMaskedPan() {
		return this.maskedPan;
	}

	public String getMemberNumber() {
		return this.memberNumber;
	}

	public String getMemeberName() {
		return this.memeberName;
	}

	public String getStan() {
		return this.stan;
	}

	public String getTerminalId() {
		return this.terminalId;
	}

	public String getTicketId() {
		return this.ticketId;
	}

	public String getTransactionAmount() {
		return this.transactionAmount;
	}

	public String getTransactionAmountCurrency() {
		return this.transactionAmountCurrency;
	}

	public Date getTransactionBusinessDate() {
		return this.transactionBusinessDate;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public Date getTransactionLocalDate() {
		return this.transactionLocalDate;
	}

	public String getVisaTransactionId() {
		return this.visaTransactionId;
	}

	public void setAcquirerId(final String acquirerId) {
		this.acquirerId = acquirerId;
	}

	public void setAcquirerName(final String acquirerName) {
		this.acquirerName = acquirerName;
	}

	public void setAmountReceived(final BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}

	public void setArnId(final String arnId) {
		this.arnId = arnId;
	}

	public void setAtmFeeAmount(final BigDecimal atmFeeAmount) {
		this.atmFeeAmount = atmFeeAmount;
	}

	public void setAtmFeeAmountCurrency(final String atmFeeAmountCurrency) {
		this.atmFeeAmountCurrency = atmFeeAmountCurrency;
	}

	public void setAtmPos(final String atmPos) {
		this.atmPos = atmPos;
	}

	public void setCardholderAmount(final String cardholderAmount) {
		this.cardholderAmount = cardholderAmount;
	}

	public void setCardholderAmountCurrency(
		final String cardholderAmountCurrency) {

		this.cardholderAmountCurrency = cardholderAmountCurrency;
	}

	public void setClaimAmount(final BigDecimal claimAmount) {
		this.claimAmount = claimAmount;
	}

	public void setClaimAmountCurrency(final String claimAmountCurrency) {
		this.claimAmountCurrency = claimAmountCurrency;
	}

	public void setIssuerId(final Long issuerId) {
		this.issuerId = issuerId;
	}

	public void setIssuerName(final String issuerName) {
		this.issuerName = issuerName;
	}

	public void setMaskedPan(final String maskedPan) {
		this.maskedPan = maskedPan;
	}

	public void setMemberNumber(final String memberNumber) {
		this.memberNumber = memberNumber;
	}

	public void setMemeberName(final String memeberName) {
		this.memeberName = memeberName;
	}

	public void setStan(final String stan) {
		this.stan = stan;
	}

	public void setTerminalId(final String terminalId) {
		this.terminalId = terminalId;
	}

	public void setTicketId(final String ticketId) {
		this.ticketId = ticketId;
	}

	public void setTransactionAmount(final String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public void setTransactionAmountCurrency(
		final String transactionAmountCurrency) {

		this.transactionAmountCurrency = transactionAmountCurrency;
	}

	public void setTransactionBusinessDate(final Date transactionBusinessDate) {
		this.transactionBusinessDate = transactionBusinessDate;
	}

	public void setTransactionId(final String transactionId) {
		this.transactionId = transactionId;
	}

	public void setTransactionLocalDate(final Date transactionLocalDate) {
		this.transactionLocalDate = transactionLocalDate;
	}

	public void setVisaTransactionId(final String visaTransactionId) {
		this.visaTransactionId = visaTransactionId;
	}

	private String acquirerId;
	private String acquirerName;
	private BigDecimal amountReceived;
	private String arnId;
	private BigDecimal atmFeeAmount;
	private String atmFeeAmountCurrency;
	private String atmPos;
	private String cardholderAmount;
	private String cardholderAmountCurrency;
	private BigDecimal claimAmount;
	private String claimAmountCurrency;
	private Long issuerId;
	private String issuerName;
	private String maskedPan;
	private String memberNumber;
	private String memeberName;
	private String stan;
	private String terminalId;
	private String ticketId;
	private String transactionAmount;
	private String transactionAmountCurrency;
	private Date transactionBusinessDate;
	private String transactionId;
	private Date transactionLocalDate;
	private String visaTransactionId;

}