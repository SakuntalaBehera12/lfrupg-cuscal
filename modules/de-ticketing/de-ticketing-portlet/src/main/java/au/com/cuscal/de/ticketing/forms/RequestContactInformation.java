//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.forms;

public class RequestContactInformation {

	public String getBsb() {
		return this.bsb;
	}

	public String getContactNumberTo() {
		return this.contactNumberTo;
	}

	public String getContactOfficerFirstName() {
		return this.contactOfficerFirstName;
	}

	public String getContactOfficerLastName() {
		return this.contactOfficerLastName;
	}

	public String getEmailFrom() {
		return this.emailFrom;
	}

	public String getEmailTo() {
		return this.emailTo;
	}

	public String getFaxNumberFrom() {
		return this.faxNumberFrom;
	}

	public String getFaxNumberTo() {
		return this.faxNumberTo;
	}

	public String getPhoneNumberFrom() {
		return this.phoneNumberFrom;
	}

	public String getReqInstACNorARBNfrom() {
		return this.reqInstACNorARBNfrom;
	}

	public String getReqInstFullNamefrom() {
		return this.reqInstFullNamefrom;
	}

	public String getRespInstNameTo() {
		return this.respInstNameTo;
	}

	public void setBsb(final String bsb) {
		this.bsb = bsb;
	}

	public void setContactNumberTo(final String contactNumberTo) {
		this.contactNumberTo = contactNumberTo;
	}

	public void setContactOfficerFirstName(
		final String contactOfficerFirstName) {

		this.contactOfficerFirstName = contactOfficerFirstName;
	}

	public void setContactOfficerLastName(final String contactOfficerLastName) {
		this.contactOfficerLastName = contactOfficerLastName;
	}

	public void setEmailFrom(final String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public void setEmailTo(final String emailTo) {
		this.emailTo = emailTo;
	}

	public void setFaxNumberFrom(final String faxNumberFrom) {
		this.faxNumberFrom = faxNumberFrom;
	}

	public void setFaxNumberTo(final String faxNumberTo) {
		this.faxNumberTo = faxNumberTo;
	}

	public void setPhoneNumberFrom(final String phoneNumberFrom) {
		this.phoneNumberFrom = phoneNumberFrom;
	}

	public void setReqInstACNorARBNfrom(final String reqInstACNorARBNfrom) {
		this.reqInstACNorARBNfrom = reqInstACNorARBNfrom;
	}

	public void setReqInstFullNamefrom(final String reqInstFullNamefrom) {
		this.reqInstFullNamefrom = reqInstFullNamefrom;
	}

	public void setRespInstNameTo(final String respInstNameTo) {
		this.respInstNameTo = respInstNameTo;
	}

	private String bsb;
	private String contactNumberTo;
	private String contactOfficerFirstName;
	private String contactOfficerLastName;
	private String emailFrom;
	private String emailTo;
	private String faxNumberFrom;
	private String faxNumberTo;
	private String phoneNumberFrom;
	private String reqInstACNorARBNfrom;
	private String reqInstFullNamefrom;
	private String respInstNameTo;

}