package au.com.cuscal.bpay.ticketing.forms;

/**
 * Form class to handle the request type related information.
 *
 *
 */
public class RequestTypeInformation {

	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	public String getRequestTypeId() {
		return requestTypeId;
	}

	/**
	 * @return the typeDescription
	 */
	public String getTypeDescription() {
		return typeDescription;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setRequestTypeId(String requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	/**
	 * @param typeDescription the typeDescription to set
	 */
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}

	private String errorMsg;
	private String productCode;
	private String requestTypeId;
	private String typeDescription;

}