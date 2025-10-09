package au.com.cuscal.transactions.forms;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class RequestAtmPosClaimInformation {

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getRequestAmountReceived() {
		return requestAmountReceived;
	}

	public String getRequestAtmFee() {
		return requestAtmFee;
	}

	public String getRequestAtmReason() {
		return requestAtmReason;
	}

	public List<MultipartFile> getRequestAttachments() {
		return requestAttachments;
	}

	public String getRequestCardholderAmount() {
		return requestCardholderAmount;
	}

	public String getRequestClaimAmount() {
		return requestClaimAmount;
	}

	public String getRequestComments() {
		return requestComments;
	}

	public String getRequestDisputeType() {
		return requestDisputeType;
	}

	public String getRequestPosReason() {
		return requestPosReason;
	}

	public String getRequestTransactionAmount() {
		return requestTransactionAmount;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public boolean isOutstandingClosedDispute() {
		return isOutstandingClosedDispute;
	}

	public boolean isOutstandingDispute() {
		return isOutstandingDispute;
	}

	public boolean isOutstandingReinvestigation() {
		return isOutstandingReinvestigation;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setOutstandingClosedDispute(
		boolean isOutstandingClosedDispute) {

		this.isOutstandingClosedDispute = isOutstandingClosedDispute;
	}

	public void setOutstandingDispute(boolean isOutstandingDispute) {
		this.isOutstandingDispute = isOutstandingDispute;
	}

	public void setOutstandingReinvestigation(
		boolean isOutstandingReinvestigation) {

		this.isOutstandingReinvestigation = isOutstandingReinvestigation;
	}

	public void setRequestAmountReceived(String requestAmountReceived) {
		this.requestAmountReceived = requestAmountReceived;
	}

	public void setRequestAtmFee(String requestAtmFee) {
		this.requestAtmFee = requestAtmFee;
	}

	public void setRequestAtmReason(String requestAtmReason) {
		this.requestAtmReason = requestAtmReason;
	}

	public void setRequestAttachments(List<MultipartFile> requestAttachments) {
		this.requestAttachments = requestAttachments;
	}

	public void setRequestCardholderAmount(String requestCardholderAmount) {
		this.requestCardholderAmount = requestCardholderAmount;
	}

	public void setRequestClaimAmount(String requestClaimAmount) {
		this.requestClaimAmount = requestClaimAmount;
	}

	public void setRequestComments(String requestComments) {
		this.requestComments = requestComments;
	}

	public void setRequestDisputeType(String requestDisputeType) {
		this.requestDisputeType = requestDisputeType;
	}

	public void setRequestPosReason(String requestPosReason) {
		this.requestPosReason = requestPosReason;
	}

	public void setRequestTransactionAmount(String requestTransactionAmount) {
		this.requestTransactionAmount = requestTransactionAmount;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	private String errorMsg;
	private boolean isOutstandingClosedDispute;
	private boolean isOutstandingDispute;
	private boolean isOutstandingReinvestigation;
	private String requestAmountReceived;
	private String requestAtmFee;
	private String requestAtmReason;
	private List<MultipartFile> requestAttachments;
	private String requestCardholderAmount;
	private String requestClaimAmount;
	private String requestComments;
	private String requestDisputeType;
	private String requestPosReason;
	private String requestTransactionAmount;
	private String transactionCode;

}