package com.tcs.cuscal.dao;

public class TestLDAPUtil {

	public static void main(String[] args) throws Exception {
		System.out.println("Preparing...");
		LDAPUtil ldap = LDAPUtil.getInstance();

		ldap.modifyAttribute(args[0], args[1], args[2]);
		System.out.println("Done.");
	}

	public TestLDAPUtil() {
	}

}