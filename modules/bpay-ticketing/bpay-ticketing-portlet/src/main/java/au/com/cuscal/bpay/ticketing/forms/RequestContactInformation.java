package au.com.cuscal.bpay.ticketing.forms;

/**
 * Form class to handle the request contact related information.
 *
 *
 */
public class RequestContactInformation {

	public String getBsb() {
		return bsb;
	}

	public String getEmail() {
		return email;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getOrganisation() {
		return organisation;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getSurname() {
		return surname;
	}

	public void setBsb(String bsb) {
		this.bsb = bsb;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	private String bsb;
	private String email;
	private String givenName;
	private String organisation;
	private String phoneNumber;
	private String surname;

}