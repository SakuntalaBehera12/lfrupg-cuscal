//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.text;

import java.math.BigDecimal;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;

public class FixedWidth {

	public static String leftPad(
			final String value, final int width, final char pad,
			final boolean truncateOK)
		throws TruncateException {

		return pad(value, width, pad, false, truncateOK);
	}

	public static String pad(
			String value, final int width, final char pad,
			final boolean rightPad, final boolean truncateOK)
		throws TruncateException {

		if ((value == null) || (width == 0)) {
			return "";
		}

		value = value.trim();

		if (value.length() < width) {
			final int difference = width - value.length();
			final StringBuffer buf = new StringBuffer();

			if (rightPad) {
				buf.append(value);
			}

			for (int i = 0; i < difference; ++i) {
				buf.append(pad);
			}

			if (!rightPad) {
				buf.append(value);
			}

			value = buf.toString();
		}
		else if (value.length() > width) {
			if (!truncateOK) {
				throw new TruncateException(
					"Attempt to truncate: " + value + " to width:" + width);
			}

			return value.substring(0, width);
		}

		return value;
	}

	public static String rightPad(
			final String value, final int width, final char pad,
			final boolean truncateOK)
		throws TruncateException {

		return pad(value, width, pad, true, truncateOK);
	}

	public static String zeroPad(
			final BigDecimal value, final int integerDigits,
			final int decimalDigits)
		throws TruncateException {

		final DecimalFormat formatter = new DecimalFormat();
		formatter.setMaximumFractionDigits(decimalDigits);
		formatter.setMinimumFractionDigits(decimalDigits);
		formatter.setGroupingUsed(false);
		formatter.setMinimumIntegerDigits(integerDigits);
		formatter.setMaximumIntegerDigits(integerDigits);

		return formatter.format(value.doubleValue());
	}

	private static Logger logger;

	static {
		FixedWidth.logger = Logger.getLogger((Class)FixedWidth.class);
	}

}