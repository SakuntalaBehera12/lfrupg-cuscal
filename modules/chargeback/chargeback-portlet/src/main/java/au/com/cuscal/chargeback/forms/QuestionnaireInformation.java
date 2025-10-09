package au.com.cuscal.chargeback.forms;

public class QuestionnaireInformation {

	public String getAmountReceived() {
		return amountReceived;
	}

	public String getAmountRequested() {
		return amountRequested;
	}

	public String getCashLoadInformation() {
		return cashLoadInformation;
	}

	public boolean isCashOrLoadValueNotReceived() {
		return cashOrLoadValueNotReceived;
	}

	public boolean isPartialCashOrLoadValueReceived() {
		return partialCashOrLoadValueReceived;
	}

	public void setAmountReceived(String amountReceived) {
		this.amountReceived = amountReceived;
	}

	public void setAmountRequested(String amountRequested) {
		this.amountRequested = amountRequested;
	}

	public void setCashLoadInformation(String cashLoadInformation) {
		this.cashLoadInformation = cashLoadInformation;
	}

	public void setCashOrLoadValueNotReceived(
		boolean cashOrLoadValueNotReceived) {

		this.cashOrLoadValueNotReceived = cashOrLoadValueNotReceived;
	}

	public void setPartialCashOrLoadValueReceived(
		boolean partialCashOrLoadValueReceived) {

		this.partialCashOrLoadValueReceived = partialCashOrLoadValueReceived;
	}

	private String amountReceived;
	private String amountRequested;
	private String cashLoadInformation;
	private boolean cashOrLoadValueNotReceived;
	private boolean partialCashOrLoadValueReceived;

}