//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.common;

public enum FunctionCode {

	FIRST_PRESENTMENT("200"), SECOND_PRESENTMENT_FULL("205"),
	SECOND_PRESENTMENT_PARTIAL("282");

	public String getValue() {
		return this.value;
	}

	private FunctionCode(final String value) {
		this.value = value;
	}

	private String value;

}