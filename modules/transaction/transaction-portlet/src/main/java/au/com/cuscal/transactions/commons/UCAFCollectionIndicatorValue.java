package au.com.cuscal.transactions.commons;

public enum UCAFCollectionIndicatorValue {

	NOT_SUPPORTED("0"), ATTEMPTED("1"), AUTHENTICATED("2"),
	ASSIGNED_STATIC_AAV("3"), REMOTE_PAYMENTS_ASSURANCE_SERVICE_PROGRAM("4");

	public String getValue() {
		return value;
	}

	private UCAFCollectionIndicatorValue(String value) {
		this.value = value;
	}

	private String value;

}