package au.com.cuscal.transactions.forms;

public class RequestAdditionalTransactionInformation {

	public String getComments() {
		return comments;
	}

	public String getDetection() {
		return detection;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getFraudNotificationCode() {
		return fraudNotificationCode;
	}

	public String getFraudType() {
		return fraudType;
	}

	public String getRequestType() {
		return requestType;
	}

	public String getVoucherReason() {
		return voucherReason;
	}

	public boolean isDeclined() {
		return isDeclined;
	}

	public boolean isOutstandingMasterCardDisputeArbitrationChargeback() {
		return outstandingMasterCardDisputeArbitrationChargeback;
	}

	public boolean isOutstandingMasterCardDisputeFirstChargeback() {
		return outstandingMasterCardDisputeFirstChargeback;
	}

	public boolean isOutstandingMasterCardDisputeReportFraud() {
		return outstandingMasterCardDisputeReportFraud;
	}

	public boolean isOutstandingMasterCardDisputeRetrievalRequest() {
		return outstandingMasterCardDisputeRetrievalRequest;
	}

	public boolean isOutstandingTC40() {
		return outstandingTC40;
	}

	public boolean isOutstandingTC52() {
		return outstandingTC52;
	}

	public boolean isReversal() {
		return isReversal;
	}

	public boolean isVisaAtm() {
		return isVisaAtm;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setDeclined(boolean isDeclined) {
		this.isDeclined = isDeclined;
	}

	public void setDetection(String detection) {
		this.detection = detection;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setFraudNotificationCode(String fraudNotificationCode) {
		this.fraudNotificationCode = fraudNotificationCode;
	}

	public void setFraudType(String fraudType) {
		this.fraudType = fraudType;
	}

	public void setOutstandingMasterCardDisputeArbitrationChargeback(
		boolean outstandingMasterCardDisputeArbitrationChargeback) {

		this.outstandingMasterCardDisputeArbitrationChargeback =
			outstandingMasterCardDisputeArbitrationChargeback;
	}

	public void setOutstandingMasterCardDisputeFirstChargeback(
		boolean outstandingMasterCardDisputeFirstChargeback) {

		this.outstandingMasterCardDisputeFirstChargeback =
			outstandingMasterCardDisputeFirstChargeback;
	}

	public void setOutstandingMasterCardDisputeReportFraud(
		boolean outstandingMasterCardDisputeReportFraud) {

		this.outstandingMasterCardDisputeReportFraud =
			outstandingMasterCardDisputeReportFraud;
	}

	public void setOutstandingMasterCardDisputeRetrievalRequest(
		boolean outstandingMasterCardDisputeRetrievalRequest) {

		this.outstandingMasterCardDisputeRetrievalRequest =
			outstandingMasterCardDisputeRetrievalRequest;
	}

	public void setOutstandingTC40(boolean outstandingTC40) {
		this.outstandingTC40 = outstandingTC40;
	}

	public void setOutstandingTC52(boolean outstandingTC52) {
		this.outstandingTC52 = outstandingTC52;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public void setReversal(boolean isReversal) {
		this.isReversal = isReversal;
	}

	public void setVisaAtm(boolean isVisaAtm) {
		this.isVisaAtm = isVisaAtm;
	}

	public void setVoucherReason(String voucherReason) {
		this.voucherReason = voucherReason;
	}

	private String comments;
	private String detection;
	private String errorMsg;
	private String fraudNotificationCode;
	private String fraudType;
	private boolean isDeclined;
	private boolean isReversal;
	private boolean isVisaAtm;
	private boolean outstandingMasterCardDisputeArbitrationChargeback;
	private boolean outstandingMasterCardDisputeFirstChargeback;
	private boolean outstandingMasterCardDisputeReportFraud;
	private boolean outstandingMasterCardDisputeRetrievalRequest;
	private boolean outstandingTC40;
	private boolean outstandingTC52;
	private String requestType;
	private String voucherReason;

}