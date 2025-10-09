//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common;

public interface User {

	void setGivenName(final String p0);

	String getGivenName();

	void setSurname(final String p0);

	String getSurname();

	void setEmail(final String p0);

	String getEmail();

	void setUid(final String p0);

	String getUid();

	void setRole(final String p0, final boolean p1);

	boolean hasRole(final String p0);

}