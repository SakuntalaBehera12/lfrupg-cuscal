package au.com.cuscal.transactions.forms;

public class ServiceRequestForm {

	public void clearForm() {
		setTransactionInformation(new RequestTransactionInformation());
		setAtmPosClaimInformation(new RequestAtmPosClaimInformation());
		setVisaTransactionInformation(
			new RequestAdditionalTransactionInformation());
		setContactInformation(new RequestContactInformation());
		setMemberInformation(new RequestMemberInformation());
	}

	/* Contact Information */
	public RequestAtmPosClaimInformation getAtmPosClaimInformation() {
		return atmPosClaimInformation;
	}

	/* Member Details */
	public RequestContactInformation getContactInformation() {
		return contactInformation;
	}

	/* Service Request Details */
	public RequestMemberInformation getMemberInformation() {
		return memberInformation;
	}

	/* ATM/POS Disputes */
	public RequestTransactionInformation getTransactionInformation() {
		return transactionInformation;
	}

	public RequestAdditionalTransactionInformation
		getVisaTransactionInformation() {

		return visaTransactionInformation;
	}

	public boolean isDisclaimer() {
		return disclaimer;
	}

	public void setAtmPosClaimInformation(
		RequestAtmPosClaimInformation atmPosClaimInformation) {

		this.atmPosClaimInformation = atmPosClaimInformation;
	}

	public void setContactInformation(
		RequestContactInformation contactInformation) {

		this.contactInformation = contactInformation;
	}

	public void setDisclaimer(boolean disclaimer) {
		this.disclaimer = disclaimer;
	}

	public void setMemberInformation(
		RequestMemberInformation memberInformation) {

		this.memberInformation = memberInformation;
	}

	public void setTransactionInformation(
		RequestTransactionInformation transactionInformation) {

		this.transactionInformation = transactionInformation;
	}

	public void setVisaTransactionInformation(
		RequestAdditionalTransactionInformation visaTransactionInformation) {

		this.visaTransactionInformation = visaTransactionInformation;
	}

	private RequestAtmPosClaimInformation atmPosClaimInformation;
	private RequestContactInformation contactInformation;
	private boolean disclaimer;
	private RequestMemberInformation memberInformation;
	private RequestTransactionInformation transactionInformation;
	private RequestAdditionalTransactionInformation visaTransactionInformation;

}