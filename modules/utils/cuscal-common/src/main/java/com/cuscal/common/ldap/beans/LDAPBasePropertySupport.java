//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.ldap.beans;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class LDAPBasePropertySupport implements Serializable {

	public static final int AMBIGUOUS_RESULTS = 0;

	public static final int AUTHENTICATION_ERROR = 1;

	public static final int CONNECT_ERROR = 2;

	public static final int INVALID_PARAMETER = 3;

	public static final int NO_SUCH_OBJECT = 4;

	public static final int OK = 5;

	public static final int PROPERTY__NOT_FOUND = 6;

	public LDAPBasePropertySupport() {
		this.filters = new ArrayList();
	}

	public String getAuthDN() {
		return this.authDN;
	}

	public String getAuthPassword() {
		return this.authPassword;
	}

	public String getBase() {
		return this.base;
	}

	public int getErrorCode() {
		return this.errorCode;
	}

	public List getFilters() {
		return this.filters;
	}

	public String getScope() {
		return this.scope;
	}

	public String getUserID() {
		return this.userID;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setAuthDN(final String authDN) {
		this.authDN = authDN;
	}

	public void setAuthPassword(final String authPassword) {
		this.authPassword = authPassword;
	}

	public void setBase(final String base) {
		this.base = base;
	}

	public void setErrorCode(final int errorCode) {
		this.errorCode = errorCode;
	}

	public void setFilter(final String[] filter) {
		this.filters.add(filter);
	}

	public void setScope(final String scope) {
		this.scope = scope;
	}

	public void setUserID(final String userID) {
		this.userID = userID;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	private String authDN;
	private String authPassword;
	private String base;
	private int errorCode;
	private List filters;
	private String scope;
	private String userID;
	private String userName;

}