package au.com.cuscal.chargeback.forms;

public class CancellationInfo {

	public String getCancellationCode() {
		return cancellationCode;
	}

	public String getCancelledArn() {
		return cancelledArn;
	}

	public String getDateAcquirerNotified() {
		return dateAcquirerNotified;
	}

	public String getDateCancelled() {
		return dateCancelled;
	}

	public String getDateInfo() {
		return dateInfo;
	}

	public String getDatePreviousChargeback() {
		return datePreviousChargeback;
	}

	public String getDateRecurringTransCancelled() {
		return dateRecurringTransCancelled;
	}

	public String getExplainWhyCancellationCodeNotGiven() {
		return explainWhyCancellationCodeNotGiven;
	}

	public String getReasonCancelled() {
		return reasonCancelled;
	}

	public String getReasonCancelledRecurring() {
		return reasonCancelledRecurring;
	}

	public String getSpokeWith() {
		return spokeWith;
	}

	public String getWasCancellationCodeGiven() {
		return wasCancellationCodeGiven;
	}

	public String getWasCardholderGivenCancellationPolicy() {
		return wasCardholderGivenCancellationPolicy;
	}

	public String getWhatWasCancellationPolicy() {
		return whatWasCancellationPolicy;
	}

	public boolean isCancellationCodeGivenButNotRetained() {
		return cancellationCodeGivenButNotRetained;
	}

	public boolean isMerchantNotAcceptCancellation() {
		return merchantNotAcceptCancellation;
	}

	public boolean isReservationWithin72Hours() {
		return reservationWithin72Hours;
	}

	public void setCancellationCode(String cancellationCode) {
		this.cancellationCode = cancellationCode;
	}

	public void setCancellationCodeGivenButNotRetained(
		boolean cancellationCodeGivenButNotRetained) {

		this.cancellationCodeGivenButNotRetained =
			cancellationCodeGivenButNotRetained;
	}

	public void setCancelledArn(String cancelledArn) {
		this.cancelledArn = cancelledArn;
	}

	public void setDateAcquirerNotified(String dateAcquirerNotified) {
		this.dateAcquirerNotified = dateAcquirerNotified;
	}

	public void setDateCancelled(String dateCancelled) {
		this.dateCancelled = dateCancelled;
	}

	public void setDateInfo(String dateInfo) {
		this.dateInfo = dateInfo;
	}

	public void setDatePreviousChargeback(String datePreviousChargeback) {
		this.datePreviousChargeback = datePreviousChargeback;
	}

	public void setDateRecurringTransCancelled(
		String dateRecurringTransCancelled) {

		this.dateRecurringTransCancelled = dateRecurringTransCancelled;
	}

	public void setExplainWhyCancellationCodeNotGiven(
		String explainWhyCancellationCodeNotGiven) {

		this.explainWhyCancellationCodeNotGiven =
			explainWhyCancellationCodeNotGiven;
	}

	public void setMerchantNotAcceptCancellation(
		boolean merchantNotAcceptCancellation) {

		this.merchantNotAcceptCancellation = merchantNotAcceptCancellation;
	}

	public void setReasonCancelled(String reasonCancelled) {
		this.reasonCancelled = reasonCancelled;
	}

	public void setReasonCancelledRecurring(String reasonCancelledRecurring) {
		this.reasonCancelledRecurring = reasonCancelledRecurring;
	}

	public void setReservationWithin72Hours(boolean reservationWithin72Hours) {
		this.reservationWithin72Hours = reservationWithin72Hours;
	}

	public void setSpokeWith(String spokeWith) {
		this.spokeWith = spokeWith;
	}

	public void setWasCancellationCodeGiven(String wasCancellationCodeGiven) {
		this.wasCancellationCodeGiven = wasCancellationCodeGiven;
	}

	public void setWasCardholderGivenCancellationPolicy(
		String wasCardholderGivenCancellationPolicy) {

		this.wasCardholderGivenCancellationPolicy =
			wasCardholderGivenCancellationPolicy;
	}

	public void setWhatWasCancellationPolicy(String whatWasCancellationPolicy) {
		this.whatWasCancellationPolicy = whatWasCancellationPolicy;
	}

	private String cancellationCode;
	private boolean cancellationCodeGivenButNotRetained;
	private String cancelledArn;
	private String dateAcquirerNotified;
	private String dateCancelled;
	private String dateInfo;
	private String datePreviousChargeback;
	private String dateRecurringTransCancelled;
	private String explainWhyCancellationCodeNotGiven;
	private boolean merchantNotAcceptCancellation;
	private String reasonCancelled;
	private String reasonCancelledRecurring;
	private boolean reservationWithin72Hours;
	private String spokeWith;
	private String wasCancellationCodeGiven;
	private String wasCardholderGivenCancellationPolicy;
	private String whatWasCancellationPolicy;

}