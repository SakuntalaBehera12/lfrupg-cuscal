//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BaseException extends Exception {

	public BaseException() {
		this.previousThrowable = null;
	}

	public BaseException(final String inMessage) {
		super(inMessage);

		this.previousThrowable = null;
	}

	public BaseException(final String inMessage, final Throwable inThrowable) {
		super(inMessage);

		this.previousThrowable = null;
		this.previousThrowable = inThrowable;
	}

	public BaseException(final Throwable inThrowable) {
		this.previousThrowable = null;
		this.previousThrowable = inThrowable;
	}

	public void printStacktarce() {
		super.printStackTrace();

		if (this.previousThrowable != null) {
			this.previousThrowable.printStackTrace();
		}
	}

	@Override
	public void printStackTrace(final PrintStream inPrintStream) {
		super.printStackTrace(inPrintStream);

		if (this.previousThrowable != null) {
			this.previousThrowable.printStackTrace(inPrintStream);
		}
	}

	@Override
	public void printStackTrace(final PrintWriter inPrintWriter) {
		super.printStackTrace(inPrintWriter);

		if (this.previousThrowable != null) {
			this.previousThrowable.printStackTrace(inPrintWriter);
		}
	}

	private Throwable previousThrowable;

}