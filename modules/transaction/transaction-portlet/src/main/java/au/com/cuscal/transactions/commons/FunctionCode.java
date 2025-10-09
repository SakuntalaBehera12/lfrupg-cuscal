package au.com.cuscal.transactions.commons;

public enum FunctionCode {

	MASTERCARD_FIRST_PRESENTMENT("200"),
	MASTERCARD_SECOND_PRESENTMENT_FULL("205"),
	MASTERCARD_SECOND_PRESENTMENT_PARTIAL("282");

	public String getValue() {
		return value;
	}

	private FunctionCode(String value) {
		this.value = value;
	}

	private String value;

}