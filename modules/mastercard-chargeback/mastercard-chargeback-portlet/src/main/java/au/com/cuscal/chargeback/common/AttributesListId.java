//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.common;

public enum AttributesListId {

	MASTERCARD_RETRIEVAL_REQUEST(889L), MASTERCARD_FIRST_CHARGEBACK(890L),
	MASTERCARD_ARBITRATION_CHARGEBACK(891L), MASTERCARD_REPORT_FRAUD(946L);

	public long getValue() {
		return this.value;
	}

	private AttributesListId(final long value) {
		this.value = value;
	}

	private long value;

}