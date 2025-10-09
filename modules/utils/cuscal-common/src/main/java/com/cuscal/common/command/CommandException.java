//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.command;

import com.cuscal.common.exceptions.BaseException;

public class CommandException extends BaseException {

	public CommandException(final String message) {
		super(message);

		this.cause = null;
	}

	public CommandException(final String message, final Throwable cause) {
		super(message, cause);

		this.cause = null;
	}

	public CommandException(final Throwable t) {
		super(t);

		this.cause = null;
	}

	private Throwable cause;

}