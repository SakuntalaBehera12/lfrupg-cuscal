//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.exceptions;

public class ApplicationException extends BaseException {

	public ApplicationException(final String message) {
		super(message);
	}

	public ApplicationException(
		final String message, final Throwable throwable) {

		super(message, throwable);
	}

	public ApplicationException(final Throwable e) {
		super(e);
	}

}