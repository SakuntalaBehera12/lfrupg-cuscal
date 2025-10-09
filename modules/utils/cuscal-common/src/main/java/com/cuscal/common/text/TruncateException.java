//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.text;

import com.cuscal.common.exceptions.BaseException;

public class TruncateException extends BaseException {

	public TruncateException() {
	}

	public TruncateException(final String inMessage) {
		super(inMessage);
	}

	public TruncateException(
		final String inMessage, final Throwable inThrowable) {

		super(inMessage, inThrowable);
	}

	public TruncateException(final Throwable inThrowable) {
		super(inThrowable);
	}

}