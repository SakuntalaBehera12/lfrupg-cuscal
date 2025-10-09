//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.ldap.beans;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

public class LDAPQueryResult implements Serializable {

	Attributes attributes;
	String dn;

	public LDAPQueryResult(final Attributes attributes) {
		this.attributes = attributes;
	}

	public LDAPQueryResult(final Attributes attributes, final String dn) {
		this.attributes = attributes;
		this.dn = dn;
	}

	public String[] getFieldValue(final String fieldName)
		throws NamingException {

		final Attribute attribute = this.attributes.get(fieldName);

		if (attribute == null) {
			return new String[1];
		}

		final List<String> l = new ArrayList();
		final NamingEnumeration e = attribute.getAll();

		while (e.hasMore()) {
			l.add(String.valueOf(e.next()));
		}

		return l.toArray(new String[0]);
	}

	public String getName() {
		return this.dn;
	}

	public int getResultSize() {
		return this.attributes.size();
	}

}