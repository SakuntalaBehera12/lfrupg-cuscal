package au.com.cuscal.chargeback.forms;

public class AttemptToResolve {

	public String getErrorDateContact() {
		return errorDateContact;
	}

	public String getExplainWhyNotResolve() {
		return explainWhyNotResolve;
	}

	public String getMerchantResponse() {
		return merchantResponse;
	}

	public String getMethodContact() {
		return methodContact;
	}

	public String getNameContact() {
		return nameContact;
	}

	public String getResolveWithMerchant() {
		return resolveWithMerchant;
	}

	public boolean isNotApplicableLocalLaw() {
		return notApplicableLocalLaw;
	}

	public void setErrorDateContact(String errorDateContact) {
		this.errorDateContact = errorDateContact;
	}

	public void setExplainWhyNotResolve(String explainWhyNotResolve) {
		this.explainWhyNotResolve = explainWhyNotResolve;
	}

	public void setMerchantResponse(String merchantResponse) {
		this.merchantResponse = merchantResponse;
	}

	public void setMethodContact(String methodContact) {
		this.methodContact = methodContact;
	}

	public void setNameContact(String nameContact) {
		this.nameContact = nameContact;
	}

	public void setNotApplicableLocalLaw(boolean notApplicableLocalLaw) {
		this.notApplicableLocalLaw = notApplicableLocalLaw;
	}

	public void setResolveWithMerchant(String resolveWithMerchant) {
		this.resolveWithMerchant = resolveWithMerchant;
	}

	private String errorDateContact;
	private String explainWhyNotResolve;
	private String merchantResponse;
	private String methodContact;
	private String nameContact;
	private boolean notApplicableLocalLaw;
	private String resolveWithMerchant;

}