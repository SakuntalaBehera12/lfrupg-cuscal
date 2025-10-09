//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.ldap.beans;

import com.cuscal.common.User;
import com.cuscal.common.constants.AttributeField;
import com.cuscal.common.constants.CuscalCommonConstant;

import java.util.HashMap;

import javax.naming.NamingException;

public class LdapUser implements User {

	public LdapUser() {
		this.roles = new HashMap();
	}

	public LdapUser(final LDAPQuery query, final String uid)
		throws NamingException {

		this.roles = new HashMap();
		final String[] filter = {AttributeField.UID.getKey(), uid};
		query.setFilter(filter);
		query.query(
			CuscalCommonConstant.QUERY_BASE, query.getFilters(),
			AttributeField.getLdapUserAttributeNames());
		query.setResultIndex(0);
		this.populate(query);
	}

	public String getCn() {
		return this.cn;
	}

	public String getCuscalBSB() {
		return this.cuscalBSB;
	}

	public String getDn() {
		return this.dn;
	}

	public String getEmail() {
		return this.mail;
	}

	public String getGivenName() {
		return this.givenName;
	}

	public String getMail() {
		return this.mail;
	}

	public String getO() {
		return this.o;
	}

	public String getOrgId() {
		return this.OrgId;
	}

	public String getOu() {
		return this.ou;
	}

	public String getSurname() {
		return this.sn;
	}

	public String getUid() {
		return this.uid;
	}

	public boolean hasRole(final String role) {
		if ((this.roles.get(role) != null) &&
			this.roles.get(
				role
			).equals(
				Boolean.TRUE
			)) {

			return true;
		}

		return false;
	}

	public void populate(final LDAPQuery query) throws NamingException {
		this.setDn(query.getName());
		this.setCuscalBSB(query.getBSB());
		this.setGivenName(query.getGivenName());
		this.setSurname(query.getSurname());
		this.setCn(query.getCommonName());
		this.setMail(query.getMail());
		this.setOu(query.getOrganisationalUnit());

		for (final Object o : query.getRoles()) {
			if (o instanceof String) {
				this.setRole((String)o, true);
			}
		}
	}

	public void setCn(final String cn) {
		this.cn = cn;
	}

	public void setCuscalBSB(final String cuscalBSB) {
		this.cuscalBSB = cuscalBSB;
	}

	public void setDn(final String dn) {
		this.dn = dn;
	}

	public void setEmail(final String email) {
		this.mail = email;
	}

	public void setGivenName(final String givenName) {
		this.givenName = givenName;
	}

	public void setMail(final String mail) {
		this.mail = mail;
	}

	public void setO(final String o) {
		this.o = o;
	}

	public void setOrgId(final String orgId) {
		this.OrgId = orgId;
	}

	public void setOu(final String ou) {
		this.ou = ou;
	}

	public void setRole(final String role, final boolean has) {
		this.roles.put(role, Boolean.valueOf(has));
	}

	public void setSurname(final String surname) {
		this.sn = surname;
	}

	public void setUid(final String uid) {
		this.uid = uid;
	}

	private String cn;
	private String cuscalBSB;
	private String dn;
	private String givenName;
	private String mail;
	private String o;
	private String OrgId;
	private String ou;
	private HashMap roles;
	private String sn;
	private String uid;

}