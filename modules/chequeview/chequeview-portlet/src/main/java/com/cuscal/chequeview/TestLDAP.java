package com.cuscal.chequeview;

import com.cuscal.common.ldap.beans.LDAPQuery;

import java.io.Serializable;

import java.util.Hashtable;

import javax.naming.NamingException;

public class TestLDAP implements Serializable {

	public static void main(final String[] args) throws NamingException {
		System.out.println("initiate LDAPgetResult()");
		final Hashtable ht = new Hashtable();
		ht.put(
			"java.naming.factory.initial",
			"com.netscape.jndi.ldap.LdapContextFactory");
		ht.put("java.naming.provider.url", "ldap://suldap01.cuscal.com.au:389");
		ht.put("java.naming.ldap.version", "3");
		final LDAPQuery l = new LDAPQuery(ht);
		System.out.println("set filter method");
		final String[] filter = {"uid", "stewoo"};
		l.setFilter(filter);
		final String[] n = {
			"cuscalbsb", "givenname", "sn", "cn", "uid", "mail",
			"telephonenumber", "facsimiletelephonenumber", "o"
		};
		System.out.println("get property method");

		try {
			l.query("dc=cuscal,dc=com", l.getFilters(), n);
			System.out.println("display property");
			l.setResultIndex(0);
			System.out.println("BSB=" + l.getBSB());
			System.out.println("given Name=" + l.getGivenName());
			System.out.println("surname=" + l.getSurname());
			System.out.println("common name=" + l.getCommonName());
			System.out.println("mail=" + l.getMail());
			System.out.println("ph no=" + l.getTelephoneNumber());
			System.out.println("fax no=" + l.getFaxNumber());
			System.out.println("org unit=" + l.getOrganisationalUnit());
		}
		catch (NamingException e) {
			System.out.println(e.toString());
		}
	}

}