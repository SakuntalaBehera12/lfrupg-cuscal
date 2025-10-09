package au.com.cuscal.connect.ldap.domain;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Person {

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public String getCountry() {
		return this.country;
	}

	public String getCuscalBsb() {
		return this.cuscalBsb;
	}

	public String getCuscalCuOrgId() {
		return this.cuscalCuOrgId;
	}

	public String getDescription() {
		return this.description;
	}

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

	public Date getModifiedDateTime() {
		return this.modifiedDateTime;
	}

	public String getOrganisation() {
		return this.organisation;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getUid() {
		return this.userUid;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public void setCuscalBsb(final String cuscalBsb) {
		this.cuscalBsb = cuscalBsb;
	}

	public void setCuscalCuOrgId(final String cuscalCuOrgId) {
		this.cuscalCuOrgId = cuscalCuOrgId;
	}

	public void setDescription(final String description) {
		this.description = description;
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

	public void setModifiedDateTime(final Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}

	public void setOrganisation(final String organisation) {
		this.organisation = organisation;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public void setUid(final String userUid) {
		this.userUid = userUid;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(
			this, ToStringStyle.MULTI_LINE_STYLE);
	}

	private String country;
	private String cuscalBsb;
	private String cuscalCuOrgId;
	private String description;
	private String email;
	private String firstName;
	private String fullName;
	private String lastName;
	private Date modifiedDateTime;
	private String organisation;
	private String phone;
	private String userUid;

}