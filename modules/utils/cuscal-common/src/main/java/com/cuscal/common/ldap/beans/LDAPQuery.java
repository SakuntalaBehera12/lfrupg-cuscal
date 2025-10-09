//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.ldap.beans;

import com.cuscal.common.constants.AttributeField;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LDAPQuery extends LDAPBasePropertySupport implements Serializable {

	public static Hashtable getUserRoles(final Hashtable ht, final String base)
		throws NamingException {

		final Hashtable userRoles = new Hashtable();
		final LDAPQuery ldapQuery = new LDAPQuery(ht);
		final String[] filter = {AttributeField.COMMON_NAME.getKey(), "*"};
		ldapQuery.setFilter(filter);
		final String[] attributes = {AttributeField.UNIQUE_MEMBER.getKey()};
		ldapQuery.query(base, ldapQuery.getFilters(), attributes);

		for (int i = 0; i < 2; ++i) {
			ldapQuery.setResultIndex(i);
			final String name = ldapQuery.getName();
			final String pair = name.substring(
				0,
				ldapQuery.getName(
				).indexOf(
					","
				));
			final String grp = pair.substring(
				ldapQuery.getName(
				).indexOf(
					"="
				) + 1,
				pair.length());
			final String[] list = ldapQuery.getQueryResultValues(
				AttributeField.UNIQUE_MEMBER.getKey());

			for (int n = 0; n < list.length; ++n) {
				if (list[n] != null) {
					userRoles.put(list[n], grp);
				}
			}
		}

		return userRoles;
	}

	public LDAPQuery() throws NamingException {
		this.attrIDs = new String[0];
		this.ctx = null;
		this.ctx = new InitialDirContext();
	}

	public LDAPQuery(final Hashtable properties) throws NamingException {
		this.attrIDs = new String[0];
		this.ctx = null;
		final Hashtable JNDIProperties = new Hashtable();
		JNDIProperties.putAll(properties);
		this.ctx = new InitialDirContext(JNDIProperties);
	}

	public void close() throws NamingException {
		this.ctx.close();
	}

	public String findUserDN(final String userid) throws NamingException {
		final Hashtable env = new Hashtable();
		String tmpUserDN = null;
		final SearchControls constraints = new SearchControls();
		constraints.setSearchScope(2);
		final NamingEnumeration results = this.ctx.search(
			this.getBase(), "uid=" + userid, constraints);

		while ((results != null) && results.hasMore()) {
			final SearchResult sr = (SearchResult)results.next();
			tmpUserDN = sr.getName();
		}

		return tmpUserDN;
	}

	public String[] getAttrIDs() {
		return this.attrIDs;
	}

	public String getBSB() throws NamingException {
		return this.getQueryResultValue(AttributeField.BSB.getKey());
	}

	public String getCommonName() throws NamingException {
		return this.getQueryResultValue(AttributeField.COMMON_NAME.getKey());
	}

	public String getFaxNumber() throws NamingException {
		return this.getQueryResultValue(AttributeField.FAX_NUMBER.getKey());
	}

	public String getGivenName() throws NamingException {
		return this.getQueryResultValue(AttributeField.GIVEN_NAME.getKey());
	}

	public String getMail() throws NamingException {
		return this.getQueryResultValue(AttributeField.MAIL.getKey());
	}

	public String getName() throws NamingException {
		if ((this.index <= this.results.size()) && (this.results.size() > 0)) {
			final LDAPQueryResult r = (LDAPQueryResult)this.results.get(
				this.index);
			final String fieldValue = r.getName();

			if (fieldValue != null) {
				return fieldValue;
			}
		}

		return "";
	}

	public String getOrganisationalUnit() throws NamingException {
		return this.getQueryResultValue(
			AttributeField.ORGANISATIONAL_UNIT.getKey());
	}

	public String getQueryResultValue(final String attrID)
		throws NamingException {

		if ((this.index <= this.results.size()) & this.isAttrIDSet(attrID)) {
			final LDAPQueryResult r = (LDAPQueryResult)this.results.get(
				this.index);
			final String[] fieldValues = r.getFieldValue(attrID);

			if (fieldValues[0] != null) {
				return fieldValues[0];
			}
		}

		return "";
	}

	public String[] getQueryResultValues(final String attrID)
		throws NamingException {

		if ((this.index <= this.results.size()) & this.isAttrIDSet(attrID)) {
			final LDAPQueryResult r = (LDAPQueryResult)this.results.get(
				this.index);

			return r.getFieldValue(attrID);
		}

		return new String[0];
	}

	public int getResultIndex() {
		return this.index;
	}

	public int getResultSize() {
		return this.results.size();
	}

	public ArrayList getRoles() throws NamingException {
		final String userDN = this.getName();
		final String filter = "(uniquemember=" + userDN + ")";
		NamingEnumeration answer = null;
		final String[] attrIDs = {AttributeField.COMMON_NAME.getKey()};
		final SearchControls ctls = new SearchControls();
		ctls.setReturningAttributes(attrIDs);
		ctls.setSearchScope(2);
		answer = this.ctx.search(this.getBase(), filter, ctls);
		final ArrayList roles = new ArrayList();

		while (answer.hasMore()) {
			final SearchResult sr = (SearchResult)answer.next();
			roles.add(sr.getName());
		}

		return roles;
	}

	public String getSurname() throws NamingException {
		return this.getQueryResultValue(AttributeField.SUR_NAME.getKey());
	}

	public String getTelephoneNumber() throws NamingException {
		return this.getQueryResultValue(
			AttributeField.TELEPHONE_NUMBER.getKey());
	}

	public String[] getTelephoneNumbers() throws NamingException {
		return this.getQueryResultValues(
			AttributeField.TELEPHONE_NUMBER.getKey());
	}

	public boolean isAttrIDSet(final String attrID) {
		final int i = Arrays.binarySearch(this.getAttrIDs(), attrID);

		if (i >= 0) {
			return true;
		}

		return false;
	}

	public void query() throws NamingException {
		NamingEnumeration answer = null;
		final Attributes matchAttrs = new BasicAttributes(true);
		final List filters = this.getFilters();

		for (int i = 0; i < filters.size(); ++i) {
			final String[] filter = (String[])filters.get(i);

			if (filter.length == 1) {
				matchAttrs.put(new BasicAttribute(filter[0]));
			}
			else if (filter.length == 2) {
				matchAttrs.put(new BasicAttribute(filter[0], filter[1]));
			}
		}

		if (this.attrIDs.length > 0) {
			answer = this.ctx.search(this.getBase(), matchAttrs, this.attrIDs);
		}
		else {
			answer = this.ctx.search(this.getBase(), matchAttrs);
		}

		this.results = new ArrayList();

		while (answer.hasMore()) {
			final SearchResult sr = (SearchResult)answer.next();
			this.results.add(
				new LDAPQueryResult(sr.getAttributes(), sr.getName()));
		}
	}

	public void query(
			final String base, final List filters, final String[] attrIDs)
		throws NamingException {

		this.setBase(base);

		for (int lastFilter = filters.size(), i = 0; i < lastFilter; ++i) {
			final String[] filter = (String[])filters.get(i);
			this.setFilter(filter);
		}

		this.setAttrIDs(attrIDs);
		this.query();
	}

	public void setAttrIDs(final String[] attrIDs) {
		Arrays.sort(this.attrIDs = attrIDs);
	}

	public void setResultIndex(final int index) {
		this.index = index;
	}

	private String[] attrIDs;
	private InitialDirContext ctx;
	private int index;
	private List results;

}