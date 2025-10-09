package au.com.cuscal.chargeback.common;

import java.util.HashMap;

public class ChargebackCodes {

	public HashMap<Long, String> getCbAuthMap() {
		return cbAuthMap;
	}

	public HashMap<Long, String> getCbCancelledMap() {
		return cbCancelledMap;
	}

	public HashMap<Long, String> getCbFraudMap() {
		return cbFraudMap;
	}

	public HashMap<Long, String> getCbNRFSMap() {
		return cbNRFSMap;
	}

	public HashMap<Long, String> getCbProcessingErrorMap() {
		return cbProcessingErrorMap;
	}

	public HashMap<Long, String> getCbRFIMap() {
		return cbRFIMap;
	}

	public HashMap<Long, String> getCbTypeMap() {
		return cbTypeMap;
	}

	public void setCbAuthMap(HashMap<Long, String> cbAuthMap) {
		this.cbAuthMap = cbAuthMap;
	}

	public void setCbCancelledMap(HashMap<Long, String> cbCancelledMap) {
		this.cbCancelledMap = cbCancelledMap;
	}

	public void setCbFraudMap(HashMap<Long, String> cbFraudMap) {
		this.cbFraudMap = cbFraudMap;
	}

	public void setCbNRFSMap(HashMap<Long, String> cbNRFSMap) {
		this.cbNRFSMap = cbNRFSMap;
	}

	public void setCbProcessingErrorMap(
		HashMap<Long, String> cbProcessingErrorMap) {

		this.cbProcessingErrorMap = cbProcessingErrorMap;
	}

	public void setCbRFIMap(HashMap<Long, String> cbRFIMap) {
		this.cbRFIMap = cbRFIMap;
	}

	public void setCbTypeMap(HashMap<Long, String> cbTypeMap) {
		this.cbTypeMap = cbTypeMap;
	}

	private HashMap<Long, String> cbAuthMap;
	private HashMap<Long, String> cbCancelledMap;
	private HashMap<Long, String> cbFraudMap;
	private HashMap<Long, String> cbNRFSMap;
	private HashMap<Long, String> cbProcessingErrorMap;
	private HashMap<Long, String> cbRFIMap;
	private HashMap<Long, String> cbTypeMap;

}