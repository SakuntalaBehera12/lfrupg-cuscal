//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.domain;

import java.io.Serializable;

public class TicketUser implements Serializable {

	public String getEmail() {
		return this.email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getOrganisation() {
		return this.organisation;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public void setOrganisation(final String organisation) {
		this.organisation = organisation;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	private static final long serialVersionUID = 2701638250154307117L;

	private String email;
	private String firstName;
	private String fullName;
	private String lastName;
	private String organisation;
	private String phoneNumber;

}