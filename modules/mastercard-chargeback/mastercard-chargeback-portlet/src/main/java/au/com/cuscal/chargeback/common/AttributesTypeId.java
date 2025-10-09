//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.common;

public enum AttributesTypeId {

	MASTERCARD_DISPUTE_TYPES(402),
	MASTERCARD_RETRIEVAL_REQUEST_REASON_CODES(403),
	MASTERCARD_FIRST_CHARGEBACK_REASON_CODES(404),
	MASTERCARD_ARBITRATION_CHARGEBACK_REASON_CODES(405);

	public int getValue() {
		return this.value;
	}

	private AttributesTypeId(final int value) {
		this.value = value;
	}

	private int value;

}