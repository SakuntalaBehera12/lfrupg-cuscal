//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.command;

public class BusinessCommandException extends CommandException {

	public BusinessCommandException(final String message) {
		super(message);
	}

	public BusinessCommandException(
		final String message, final Throwable cause) {

		super(message, cause);
	}

}