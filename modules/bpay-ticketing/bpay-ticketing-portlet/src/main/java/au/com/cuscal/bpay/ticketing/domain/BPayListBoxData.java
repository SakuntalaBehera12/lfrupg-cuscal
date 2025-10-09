package au.com.cuscal.bpay.ticketing.domain;

import java.io.Serializable;

import java.util.HashMap;

/**
 * Domain class to handle the BPay list box related data.
 *
 *
 */
public class BPayListBoxData implements Serializable {

	/**
	 * @return the bpayTypeOfi
	 */
	public HashMap<Long, String> getBpayTypeOfi() {
		return bpayTypeOfi;
	}

	/**
	 * @return the entryMethodMap
	 */
	public HashMap<Long, String> getEntryMethodMap() {
		return entryMethodMap;
	}

	/**
	 * @return the fraudTypeMap
	 */
	public HashMap<Long, String> getFraudTypeMap() {
		return fraudTypeMap;
	}

	/**
	 * @return the fraudTypeToolTipMap
	 */
	public HashMap<Long, String> getFraudTypeToolTipMap() {
		return fraudTypeToolTipMap;
	}

	/**
	 * @return the investigationTypeMap
	 */
	public HashMap<Long, String> getInvestigationTypeMap() {
		return investigationTypeMap;
	}

	/**
	 * @return the paymentMethodMap
	 */
	public HashMap<Long, String> getPaymentMethodMap() {
		return paymentMethodMap;
	}

	/**
	 * @return the reasonErrorCorrectionMap
	 */
	public HashMap<Long, String> getReasonErrorCorrectionMap() {
		return reasonErrorCorrectionMap;
	}

	/**
	 * @return the reasonPaymentInvestigationMap
	 */
	public HashMap<Long, String> getReasonPaymentInvestigationMap() {
		return reasonPaymentInvestigationMap;
	}

	/**
	 * @return the scamTypeMap
	 */
	public HashMap<Long, String> getScamTypeMap() {
		return scamTypeMap;
	}

	/**
	 * @return the scamTypeToolTipMap
	 */
	public HashMap<Long, String> getScamTypeToolTipMap() {
		return scamTypeToolTipMap;
	}

	/**
	 * @param bpayTypeOfi the bpayTypeOfi to set
	 */
	public void setBpayTypeOfi(HashMap<Long, String> bpayTypeOfi) {
		this.bpayTypeOfi = bpayTypeOfi;
	}

	/**
	 * @param entryMethodMap the entryMethodMap to set
	 */
	public void setEntryMethodMap(HashMap<Long, String> entryMethodMap) {
		this.entryMethodMap = entryMethodMap;
	}

	/**
	 * @param fraudTypeMap the fraudTypeMap to set
	 */
	public void setFraudTypeMap(HashMap<Long, String> fraudTypeMap) {
		this.fraudTypeMap = fraudTypeMap;
	}

	/**
	 * @param fraudTypeToolTipMap the fraudTypeToolTipMap to set
	 */
	public void setFraudTypeToolTipMap(
		HashMap<Long, String> fraudTypeToolTipMap) {

		this.fraudTypeToolTipMap = fraudTypeToolTipMap;
	}

	/**
	 * @param investigationTypeMap the investigationTypeMap to set
	 */
	public void setInvestigationTypeMap(
		HashMap<Long, String> investigationTypeMap) {

		this.investigationTypeMap = investigationTypeMap;
	}

	/**
	 * @param paymentMethodMap the paymentMethodMap to set
	 */
	public void setPaymentMethodMap(HashMap<Long, String> paymentMethodMap) {
		this.paymentMethodMap = paymentMethodMap;
	}

	/**
	 * @param reasonErrorCorrectionMap the reasonErrorCorrectionMap to set
	 */
	public void setReasonErrorCorrectionMap(
		HashMap<Long, String> reasonErrorCorrectionMap) {

		this.reasonErrorCorrectionMap = reasonErrorCorrectionMap;
	}

	/**
	 * @param reasonPaymentInvestigationMap the reasonPaymentInvestigationMap to set
	 */
	public void setReasonPaymentInvestigationMap(
		HashMap<Long, String> reasonPaymentInvestigationMap) {

		this.reasonPaymentInvestigationMap = reasonPaymentInvestigationMap;
	}

	/**
	 * @param scamTypeMap the scamTypeMap to set
	 */
	public void setScamTypeMap(HashMap<Long, String> scamTypeMap) {
		this.scamTypeMap = scamTypeMap;
	}

	/**
	 * @param scamTypeToolTipMap the scamTypeToolTipMap to set
	 */
	public void setScamTypeToolTipMap(
		HashMap<Long, String> scamTypeToolTipMap) {

		this.scamTypeToolTipMap = scamTypeToolTipMap;
	}

	private static final long serialVersionUID = 1L;

	private HashMap<Long, String> bpayTypeOfi;
	private HashMap<Long, String> entryMethodMap;
	private HashMap<Long, String> fraudTypeMap;
	private HashMap<Long, String> fraudTypeToolTipMap;
	private HashMap<Long, String> investigationTypeMap;
	private HashMap<Long, String> paymentMethodMap;
	private HashMap<Long, String> reasonErrorCorrectionMap;
	private HashMap<Long, String> reasonPaymentInvestigationMap;
	private HashMap<Long, String> scamTypeMap;
	private HashMap<Long, String> scamTypeToolTipMap;

}