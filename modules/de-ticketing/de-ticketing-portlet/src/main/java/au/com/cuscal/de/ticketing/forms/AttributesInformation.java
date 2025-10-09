//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.forms;

import java.io.Serializable;

public class AttributesInformation implements Serializable {

	public String getAccountNumberRemitter() {
		return this.accountNumberRemitter;
	}

	public String getAccountTitle() {
		return this.accountTitle;
	}

	public String getBatchNumber() {
		return this.batchNumber;
	}

	public String getBsb() {
		return this.bsb;
	}

	public String getClaimAdditionalInfo() {
		return this.claimAdditionalInfo;
	}

	public String getClaimReason() {
		return this.claimReason;
	}

	public String getClaimReply() {
		return this.claimReply;
	}

	public String getClientRefNumber() {
		return this.clientRefNumber;
	}

	public String getCurrentDDRStatus() {
		return this.currentDDRStatus;
	}

	public String getCustomerDeclarationAttached() {
		return this.customerDeclarationAttached;
	}

	public String getDatePosted() {
		return this.datePosted;
	}

	public String getDestinationAccountNumber() {
		return this.destinationAccountNumber;
	}

	public String getDestinationAdditionalInfo() {
		return this.destinationAdditionalInfo;
	}

	public String getDestinationBSB() {
		return this.destinationBSB;
	}

	public String[] getDestinationLodgementRefs() {
		return this.destinationLodgementRefs;
	}

	public String getDrawingInTheNameOf() {
		return this.drawingInTheNameOf;
	}

	public String getFinInstName() {
		return this.finInstName;
	}

	public String getFinInstTraceBSBNumber() {
		return this.finInstTraceBSBNumber;
	}

	public String getFinInstUserId() {
		return this.finInstUserId;
	}

	public String getFundsAvailable() {
		return this.fundsAvailable;
	}

	public int getId() {
		return this.id;
	}

	public String getIntendedAccountBSBNumber() {
		return this.intendedAccountBSBNumber;
	}

	public String getIntendedAccountNumber() {
		return this.intendedAccountNumber;
	}

	public String getIntendedAccountOrWrongPayee() {
		return this.intendedAccountOrWrongPayee;
	}

	public String getIntendedAccountTitle() {
		return this.intendedAccountTitle;
	}

	public String getLedgerAdditionalInfo() {
		return this.ledgerAdditionalInfo;
	}

	public String getOfiAccountName() {
		return this.ofiAccountName;
	}

	public String getOfiAccountNumber() {
		return this.ofiAccountNumber;
	}

	public String getOfiAdditionalInfo() {
		return this.ofiAdditionalInfo;
	}

	public String getOfiBranch() {
		return this.ofiBranch;
	}

	public String getOfiBsbNumber() {
		return this.ofiBsbNumber;
	}

	public String getOfiClaimResult() {
		return this.ofiClaimResult;
	}

	public String getOfiComment() {
		return this.ofiComment;
	}

	public String getOfiContactNumber() {
		return this.ofiContactNumber;
	}

	public String getOfiContactOfficer() {
		return this.ofiContactOfficer;
	}

	public String getOfiDatePosted() {
		return this.ofiDatePosted;
	}

	public String getOfiDateResolved() {
		return this.ofiDateResolved;
	}

	public String getOfiDrawingAmount() {
		return this.ofiDrawingAmount;
	}

	public String getOfiFundAvailability() {
		return this.ofiFundAvailability;
	}

	public String[] getOfiMistakenResults() {
		return this.ofiMistakenResults;
	}

	public String getOfiRecallResult() {
		return this.ofiRecallResult;
	}

	public String getOfiRemitterName() {
		return this.ofiRemitterName;
	}

	public String getOfiTracePersonName() {
		return this.ofiTracePersonName;
	}

	public String getOfiTracePhoneNumber() {
		return this.ofiTracePhoneNumber;
	}

	public String getOutcome() {
		return this.outcome;
	}

	public String[] getProcessedDates() {
		return this.processedDates;
	}

	public String getReceiverAccountName() {
		return this.receiverAccountName;
	}

	public String getReceiverAccountNumber() {
		return this.receiverAccountNumber;
	}

	public String getReceiverAdditionalInfo() {
		return this.receiverAdditionalInfo;
	}

	public String getReceiverBranch() {
		return this.receiverBranch;
	}

	public String getReceiverBSBNumber() {
		return this.receiverBSBNumber;
	}

	public String getReceiverLodgementRef() {
		return this.receiverLodgementRef;
	}

	public String getReference() {
		return this.reference;
	}

	public String getRemitterName() {
		return this.remitterName;
	}

	public String getRequestFor() {
		return this.requestFor;
	}

	public String getRequestReceived() {
		return this.requestReceived;
	}

	public String getRequestType() {
		return this.requestType;
	}

	public String getTimeframe() {
		return this.timeframe;
	}

	public String[] getTransactionAmounts() {
		return this.transactionAmounts;
	}

	public String getTransactionCode() {
		return this.transactionCode;
	}

	public String getTransactionCodeName() {
		return this.transactionCodeName;
	}

	public boolean isCustomerSelectedWrongPayee() {
		return this.customerSelectedWrongPayee;
	}

	public boolean isIntendedAccountDetails() {
		return this.intendedAccountDetails;
	}

	public void setAccountNumberRemitter(final String accountNumberRemitter) {
		this.accountNumberRemitter = accountNumberRemitter;
	}

	public void setAccountTitle(final String accountTitle) {
		this.accountTitle = accountTitle;
	}

	public void setBatchNumber(final String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public void setBsb(final String bsb) {
		this.bsb = bsb;
	}

	public void setClaimAdditionalInfo(final String claimAdditionalInfo) {
		this.claimAdditionalInfo = claimAdditionalInfo;
	}

	public void setClaimReason(final String claimReason) {
		this.claimReason = claimReason;
	}

	public void setClaimReply(final String claimReply) {
		this.claimReply = claimReply;
	}

	public void setClientRefNumber(final String clientRefNumber) {
		this.clientRefNumber = clientRefNumber;
	}

	public void setCurrentDDRStatus(final String currentDDRStatus) {
		this.currentDDRStatus = currentDDRStatus;
	}

	public void setCustomerDeclarationAttached(
		final String customerDeclarationAttached) {

		this.customerDeclarationAttached = customerDeclarationAttached;
	}

	public void setCustomerSelectedWrongPayee(
		final boolean customerSelectedWrongPayee) {

		this.customerSelectedWrongPayee = customerSelectedWrongPayee;
	}

	public void setDatePosted(final String datePosted) {
		this.datePosted = datePosted;
	}

	public void setDestinationAccountNumber(
		final String destinationAccountNumber) {

		this.destinationAccountNumber = destinationAccountNumber;
	}

	public void setDestinationAdditionalInfo(
		final String destinationAdditionalInfo) {

		this.destinationAdditionalInfo = destinationAdditionalInfo;
	}

	public void setDestinationBSB(final String destinationBSB) {
		this.destinationBSB = destinationBSB;
	}

	public void setDestinationLodgementRefs(
		final String[] destinationLodgementRefs) {

		this.destinationLodgementRefs = destinationLodgementRefs;
	}

	public void setDrawingInTheNameOf(final String drawingInTheNameOf) {
		this.drawingInTheNameOf = drawingInTheNameOf;
	}

	public void setFinInstName(final String finInstName) {
		this.finInstName = finInstName;
	}

	public void setFinInstTraceBSBNumber(final String finInstTraceBSBNumber) {
		this.finInstTraceBSBNumber = finInstTraceBSBNumber;
	}

	public void setFinInstUserId(final String finInstUserId) {
		this.finInstUserId = finInstUserId;
	}

	public void setFundsAvailable(final String fundsAvailable) {
		this.fundsAvailable = fundsAvailable;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setIntendedAccountBSBNumber(
		final String intendedAccountBSBNumber) {

		this.intendedAccountBSBNumber = intendedAccountBSBNumber;
	}

	public void setIntendedAccountDetails(
		final boolean intendedAccountDetails) {

		this.intendedAccountDetails = intendedAccountDetails;
	}

	public void setIntendedAccountNumber(final String intendedAccountNumber) {
		this.intendedAccountNumber = intendedAccountNumber;
	}

	public void setIntendedAccountOrWrongPayee(
		final String intendedAccountOrWrongPayee) {

		this.intendedAccountOrWrongPayee = intendedAccountOrWrongPayee;
	}

	public void setIntendedAccountTitle(final String intendedAccountTitle) {
		this.intendedAccountTitle = intendedAccountTitle;
	}

	public void setLedgerAdditionalInfo(final String ledgerAdditionalInfo) {
		this.ledgerAdditionalInfo = ledgerAdditionalInfo;
	}

	public void setOfiAccountName(final String ofiAccountName) {
		this.ofiAccountName = ofiAccountName;
	}

	public void setOfiAccountNumber(final String ofiAccountNumber) {
		this.ofiAccountNumber = ofiAccountNumber;
	}

	public void setOfiAdditionalInfo(final String ofiAdditionalInfo) {
		this.ofiAdditionalInfo = ofiAdditionalInfo;
	}

	public void setOfiBranch(final String ofiBranch) {
		this.ofiBranch = ofiBranch;
	}

	public void setOfiBsbNumber(final String ofiBsbNumber) {
		this.ofiBsbNumber = ofiBsbNumber;
	}

	public void setOfiClaimResult(final String ofiClaimResult) {
		this.ofiClaimResult = ofiClaimResult;
	}

	public void setOfiComment(final String ofiComment) {
		this.ofiComment = ofiComment;
	}

	public void setOfiContactNumber(final String ofiContactNumber) {
		this.ofiContactNumber = ofiContactNumber;
	}

	public void setOfiContactOfficer(final String ofiContactOfficer) {
		this.ofiContactOfficer = ofiContactOfficer;
	}

	public void setOfiDatePosted(final String ofiDatePosted) {
		this.ofiDatePosted = ofiDatePosted;
	}

	public void setOfiDateResolved(final String ofiDateResolved) {
		this.ofiDateResolved = ofiDateResolved;
	}

	public void setOfiDrawingAmount(final String ofiDrawingAmount) {
		this.ofiDrawingAmount = ofiDrawingAmount;
	}

	public void setOfiFundAvailability(final String ofiFundAvailability) {
		this.ofiFundAvailability = ofiFundAvailability;
	}

	public void setOfiMistakenResults(final String[] ofiMistakenResults) {
		this.ofiMistakenResults = ofiMistakenResults;
	}

	public void setOfiRecallResult(final String ofiRecallResult) {
		this.ofiRecallResult = ofiRecallResult;
	}

	public void setOfiRemitterName(final String ofiRemitterName) {
		this.ofiRemitterName = ofiRemitterName;
	}

	public void setOfiTracePersonName(final String ofiTracePersonName) {
		this.ofiTracePersonName = ofiTracePersonName;
	}

	public void setOfiTracePhoneNumber(final String ofiTracePhoneNumber) {
		this.ofiTracePhoneNumber = ofiTracePhoneNumber;
	}

	public void setOutcome(final String outcome) {
		this.outcome = outcome;
	}

	public void setProcessedDates(final String[] processedDates) {
		this.processedDates = processedDates;
	}

	public void setReceiverAccountName(final String receiverAccountName) {
		this.receiverAccountName = receiverAccountName;
	}

	public void setReceiverAccountNumber(final String receiverAccountNumber) {
		this.receiverAccountNumber = receiverAccountNumber;
	}

	public void setReceiverAdditionalInfo(final String receiverAdditionalInfo) {
		this.receiverAdditionalInfo = receiverAdditionalInfo;
	}

	public void setReceiverBranch(final String receiverBranch) {
		this.receiverBranch = receiverBranch;
	}

	public void setReceiverBSBNumber(final String receiverBSBNumber) {
		this.receiverBSBNumber = receiverBSBNumber;
	}

	public void setReceiverLodgementRef(final String receiverLodgementRef) {
		this.receiverLodgementRef = receiverLodgementRef;
	}

	public void setReference(final String reference) {
		this.reference = reference;
	}

	public void setRemitterName(final String remitterName) {
		this.remitterName = remitterName;
	}

	public void setRequestFor(final String requestFor) {
		this.requestFor = requestFor;
	}

	public void setRequestReceived(final String requestReceived) {
		this.requestReceived = requestReceived;
	}

	public void setRequestType(final String requestType) {
		this.requestType = requestType;
	}

	public void setTimeframe(final String timeframe) {
		this.timeframe = timeframe;
	}

	public void setTransactionAmounts(final String[] transactionAmounts) {
		this.transactionAmounts = transactionAmounts;
	}

	public void setTransactionCode(final String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public void setTransactionCodeName(final String transactionCodeName) {
		this.transactionCodeName = transactionCodeName;
	}

	private static final long serialVersionUID = -3364537385264426460L;

	private String accountNumberRemitter;
	private String accountTitle;
	private String batchNumber;
	private String bsb;
	private String claimAdditionalInfo;
	private String claimReason;
	private String claimReply;
	private String clientRefNumber;
	private String currentDDRStatus;
	private String customerDeclarationAttached;
	private boolean customerSelectedWrongPayee;
	private String datePosted;
	private String destinationAccountNumber;
	private String destinationAdditionalInfo;
	private String destinationBSB;
	private String[] destinationLodgementRefs;
	private String drawingInTheNameOf;
	private String finInstName;
	private String finInstTraceBSBNumber;
	private String finInstUserId;
	private String fundsAvailable;
	private int id;
	private String intendedAccountBSBNumber;
	private boolean intendedAccountDetails;
	private String intendedAccountNumber;
	private String intendedAccountOrWrongPayee;
	private String intendedAccountTitle;
	private String ledgerAdditionalInfo;
	private String ofiAccountName;
	private String ofiAccountNumber;
	private String ofiAdditionalInfo;
	private String ofiBranch;
	private String ofiBsbNumber;
	private String ofiClaimResult;
	private String ofiComment;
	private String ofiContactNumber;
	private String ofiContactOfficer;
	private String ofiDatePosted;
	private String ofiDateResolved;
	private String ofiDrawingAmount;
	private String ofiFundAvailability;
	private String[] ofiMistakenResults;
	private String ofiRecallResult;
	private String ofiRemitterName;
	private String ofiTracePersonName;
	private String ofiTracePhoneNumber;
	private String outcome;
	private String[] processedDates;
	private String receiverAccountName;
	private String receiverAccountNumber;
	private String receiverAdditionalInfo;
	private String receiverBranch;
	private String receiverBSBNumber;
	private String receiverLodgementRef;
	private String reference;
	private String remitterName;
	private String requestFor;
	private String requestReceived;
	private String requestType;
	private String timeframe;
	private String[] transactionAmounts;
	private String transactionCode;
	private String transactionCodeName;

}