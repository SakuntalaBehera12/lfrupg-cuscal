package au.com.cuscal.transactions.domain;

import java.io.Serializable;

public class ContactInformation implements Serializable {

	public String getEmail() {
		return email;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getOrganisation() {
		return organisation;
	}

	public String getPhoneNo() {
		return phoneNo;
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

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 3042257080758415190L;

	private String email;
	private String givenName;
	private String organisation;
	private String phoneNo;
	private String surname;

}