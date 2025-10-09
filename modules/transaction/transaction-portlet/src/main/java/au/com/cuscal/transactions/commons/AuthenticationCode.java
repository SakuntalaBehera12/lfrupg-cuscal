package au.com.cuscal.transactions.commons;

/**
 * 3DS Authentication transaction indicator for EFT connect portal
 *
 * @author v-dreddy
 *
 */
public enum AuthenticationCode {

	AUTHENTICATION_00("00", "Authentication is not attempted/required"),
	AUTHENTICATION_01("01", "Authentication was unsuccessful either from issuer or merchant end"),
	AUTHENTICATION_02("02", "Successfully Authenticated"),
	AUTHENTICATION_05("05", "Successfully Authenticated"),
	AUTHENTICATION_06("06", "Authentication was unsuccessful either from issuer or merchant end"),
	AUTHENTICATION_07("07", "Authentication is not attempted/required");

	private final String value;
	private final String description;

	private AuthenticationCode(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * Get description by value
	 *
	 * @param value
	 * @return
	 */
	public static String getDescriptionByValue(String value) {
		for (AuthenticationCode authenticationValues : AuthenticationCode.values()) {
			if (authenticationValues.getValue().equalsIgnoreCase(value)) {
				return authenticationValues.getDescription();
			}
		}
		return null;
	}
}