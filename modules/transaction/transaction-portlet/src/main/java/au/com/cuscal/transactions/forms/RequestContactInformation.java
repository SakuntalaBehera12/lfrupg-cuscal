package au.com.cuscal.transactions.forms;

public class RequestContactInformation {

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

	private String email;
	private String givenName;
	private String organisation;
	private String phoneNumber;
	private String surname;

}