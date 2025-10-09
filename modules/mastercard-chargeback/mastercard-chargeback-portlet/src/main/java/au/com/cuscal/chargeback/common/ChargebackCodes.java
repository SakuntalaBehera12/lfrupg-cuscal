//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.common;

import java.util.HashMap;

public class ChargebackCodes {

	public HashMap<Long, String> getArbitrationChargebackReasonCodesMap() {
		return this.arbitrationChargebackReasonCodesMap;
	}

	public HashMap<Long, String> getCbTypeMap() {
		return this.cbTypeMap;
	}

	public HashMap<Long, String> getFirstChargebackReasonCodesMap() {
		return this.firstChargebackReasonCodesMap;
	}

	public HashMap<Long, String> getRetrievalRequestReasonCodesMap() {
		return this.retrievalRequestReasonCodesMap;
	}

	public void setArbitrationChargebackReasonCodesMap(
		final HashMap<Long, String> arbitrationChargebackReasonCodesMap) {

		this.arbitrationChargebackReasonCodesMap =
			arbitrationChargebackReasonCodesMap;
	}

	public void setCbTypeMap(final HashMap<Long, String> cbTypeMap) {
		this.cbTypeMap = cbTypeMap;
	}

	public void setFirstChargebackReasonCodesMap(
		final HashMap<Long, String> firstChargebackReasonCodesMap) {

		this.firstChargebackReasonCodesMap = firstChargebackReasonCodesMap;
	}

	public void setRetrievalRequestReasonCodesMap(
		final HashMap<Long, String> retrievalRequestReasonCodesMap) {

		this.retrievalRequestReasonCodesMap = retrievalRequestReasonCodesMap;
	}

	private HashMap<Long, String> arbitrationChargebackReasonCodesMap;
	private HashMap<Long, String> cbTypeMap;
	private HashMap<Long, String> firstChargebackReasonCodesMap;
	private HashMap<Long, String> retrievalRequestReasonCodesMap;

}