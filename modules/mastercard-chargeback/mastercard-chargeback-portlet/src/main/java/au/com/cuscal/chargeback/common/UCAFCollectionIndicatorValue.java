//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.common;

public enum UCAFCollectionIndicatorValue {

	NOT_SUPPORTED("0"), ATTEMPTED("1"), AUTHENTICATED("2"),
	ASSIGNED_STATIC_AAV("3"), REMOTE_PAYMENTS_ASSURANCE_SERVICE_PROGRAM("4");

	public String getValue() {
		return this.value;
	}

	private UCAFCollectionIndicatorValue(final String value) {
		this.value = value;
	}

	private String value;

}