//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.utilities;

import java.util.Arrays;

public class TextUtility {

	public static final String createStringOfSize(
		final int size, final char fill) {

		final char[] chars = new char[size];
		Arrays.fill(chars, fill);

		return new String(chars);
	}

	public static final String leftJustify(
		String field, final int length, final char pad) {

		if (field == null) {
			field = "";
		}

		final StringBuffer sb = new StringBuffer(field);

		while (sb.length() < length) {
			sb.append(pad);
		}

		return sb.toString();
	}

	public static final String rightJustify(
		String field, final int length, final char pad) {

		if (field == null) {
			field = "";
		}

		final StringBuffer sb = new StringBuffer(field);

		while (sb.length() < length) {
			sb.insert(0, pad);
		}

		return sb.toString();
	}

	private TextUtility() {
	}

}