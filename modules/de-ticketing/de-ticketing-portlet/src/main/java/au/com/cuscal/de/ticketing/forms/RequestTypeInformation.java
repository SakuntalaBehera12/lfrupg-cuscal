//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.forms;

public class RequestTypeInformation {

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public String getRequestTypeId() {
		return this.requestTypeId;
	}

	public void setErrorMsg(final String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setProductCode(final String productCode) {
		this.productCode = productCode;
	}

	public void setRequestTypeId(final String requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	private String errorMsg;
	private String productCode;
	private String requestTypeId;

}