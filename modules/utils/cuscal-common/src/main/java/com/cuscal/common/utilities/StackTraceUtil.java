//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtil {

	public static String getStackTrace(final Throwable exception) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);

		return sw.toString();
	}

}