package au.com.cuscal.chargeback.forms;

public class ReturnInformation {

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public String getMerchantReceivedOn() {
		return merchantReceivedOn;
	}

	public String getOtherMethod() {
		return otherMethod;
	}

	public String getReturnAuthorisationNumber() {
		return returnAuthorisationNumber;
	}

	//For FedEx shipping return method.
	public String getReturnDate() {
		return returnDate;
	}

	public String getReturnInstruction() {
		return returnInstruction;
	}

	public String getReturnMethod() {
		return returnMethod;
	}

	//For Other return method.
	public String getShippingNumber() {
		return shippingNumber;
	}

	//Merchant return instructions.
	public String getWhoSignedPackage() {
		return whoSignedPackage;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public void setMerchantReceivedOn(String merchantReceivedOn) {
		this.merchantReceivedOn = merchantReceivedOn;
	}

	public void setOtherMethod(String otherMethod) {
		this.otherMethod = otherMethod;
	}

	public void setReturnAuthorisationNumber(String returnAuthorisationNumber) {
		this.returnAuthorisationNumber = returnAuthorisationNumber;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public void setReturnInstruction(String returnInstruction) {
		this.returnInstruction = returnInstruction;
	}

	public void setReturnMethod(String returnMethod) {
		this.returnMethod = returnMethod;
	}

	public void setShippingNumber(String shippingNumber) {
		this.shippingNumber = shippingNumber;
	}

	public void setWhoSignedPackage(String whoSignedPackage) {
		this.whoSignedPackage = whoSignedPackage;
	}

	private String deliveryAddress;
	private String merchantReceivedOn;
	private String otherMethod;
	private String returnAuthorisationNumber;
	private String returnDate;
	private String returnInstruction;
	private String returnMethod;
	private String shippingNumber;
	private String whoSignedPackage;

}