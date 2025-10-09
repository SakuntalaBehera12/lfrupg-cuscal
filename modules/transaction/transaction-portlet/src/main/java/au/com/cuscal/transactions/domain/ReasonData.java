package au.com.cuscal.transactions.domain;

import java.util.HashMap;

public class ReasonData {

	public HashMap<Long, String> getAtmDepositDisputeReasonsMap() {
		return atmDepositDisputeReasonsMap;
	}

	public HashMap<Long, String> getAtmMap() {
		return atmMap;
	}

	public HashMap<Long, String> getPosMap() {
		return posMap;
	}

	public HashMap<Long, String> getTc40DetectionMap() {
		return tc40DetectionMap;
	}

	public HashMap<Long, String> getTc40FraudTypeMap() {
		return tc40FraudTypeMap;
	}

	public HashMap<Long, String> getTc40NotificationTypeMap() {
		return tc40NotificationTypeMap;
	}

	public HashMap<Long, String> getTc52Map() {
		return tc52Map;
	}

	public HashMap<Long, String> getVisaDestination() {
		return visaDestination;
	}

	public HashMap<Long, String> getVisaType() {
		return visaType;
	}

	public void setAtmDepositDisputeReasonsMap(
		HashMap<Long, String> atmDepositDisputeReasonsMap) {

		this.atmDepositDisputeReasonsMap = atmDepositDisputeReasonsMap;
	}

	public void setAtmMap(HashMap<Long, String> atmMap) {
		this.atmMap = atmMap;
	}

	public void setPosMap(HashMap<Long, String> posMap) {
		this.posMap = posMap;
	}

	public void setTc40DetectionMap(HashMap<Long, String> tc40DetectionMap) {
		this.tc40DetectionMap = tc40DetectionMap;
	}

	public void setTc40FraudTypeMap(HashMap<Long, String> tc40FraudTypeMap) {
		this.tc40FraudTypeMap = tc40FraudTypeMap;
	}

	public void setTc40NotificationTypeMap(
		HashMap<Long, String> tc40NotificationTypeMap) {

		this.tc40NotificationTypeMap = tc40NotificationTypeMap;
	}

	public void setTc52Map(HashMap<Long, String> tc52Map) {
		this.tc52Map = tc52Map;
	}

	public void setVisaDestination(HashMap<Long, String> visaDestination) {
		this.visaDestination = visaDestination;
	}

	public void setVisaType(HashMap<Long, String> visaType) {
		this.visaType = visaType;
	}

	private HashMap<Long, String> atmDepositDisputeReasonsMap;
	private HashMap<Long, String> atmMap;
	private HashMap<Long, String> posMap;
	private HashMap<Long, String> tc40DetectionMap;
	private HashMap<Long, String> tc40FraudTypeMap;
	private HashMap<Long, String> tc40NotificationTypeMap;
	private HashMap<Long, String> tc52Map;
	private HashMap<Long, String> visaDestination;
	private HashMap<Long, String> visaType;

}