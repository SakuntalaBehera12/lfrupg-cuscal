package au.com.cuscal.cards.domain.ui;

import au.com.cuscal.service.mobile.v1_0.MobileDevice;

public class DecoratedDevice {

	public static DecoratedDevice newDecoratedDevice(MobileDevice device) {
		DecoratedDevice decoratedDevice = new DecoratedDevice();

		decoratedDevice.setDeviceId(device.getDeviceHash());
		decoratedDevice.setDeviceType(device.getType());
		decoratedDevice.setStatus(device.getStatus());
		decoratedDevice.setDeregistrationDate(device.getDeregisterationDate());
		decoratedDevice.setDeregistrationUser(device.getDeregisterationUser());
		decoratedDevice.setLastUsedDate(device.getLastUsedDate());

		return decoratedDevice;
	}

	/**
	 * @return the deregistrationDate
	 */
	public String getDeregistrationDate() {
		return deregistrationDate;
	}

	/**
	 * @return the deregistrationUser
	 */
	public String getDeregistrationUser() {
		return deregistrationUser;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * @return the lastUsedDate
	 */
	public String getLastUsedDate() {
		return lastUsedDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	public String getStatusDescription() {
		if ("A".equals(status))

			return "Registered";

		if ("I".equals(status))

			return "Deregistered";

		if ("B".equals(status))

			return "Blocked";

		return status;
	}

	/**
	 * @param deregistrationDate the deregistrationDate to set
	 */
	public void setDeregistrationDate(String deregistrationDate) {
		this.deregistrationDate = deregistrationDate;
	}

	/**
	 * @param deregistrationUser the deregistrationUser to set
	 */
	public void setDeregistrationUser(String deregistrationUser) {
		this.deregistrationUser =
			deregistrationUser != null ? deregistrationUser.toLowerCase() :
				deregistrationUser;

		if ("handset".equals(this.deregistrationUser))
			this.deregistrationUser = "device";
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * @param lastUsedDate the lastUsedDate to set
	 */
	public void setLastUsedDate(String lastUsedDate) {
		this.lastUsedDate = lastUsedDate;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	private String deregistrationDate;
	private String deregistrationUser;
	private String deviceId;
	private String deviceType;
	private String lastUsedDate;
	private String status;

}