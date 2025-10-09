package com.cuscal.chequeview;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TokenString {

	public static void main(final String[] args) {
		final Calendar cal = Calendar.getInstance();
		String day = new StringBuilder(
			String.valueOf(cal.get(5))
		).toString();

		if (day.length() == 1) {
			day = "0" + day;
		}

		String month = new StringBuilder(
			String.valueOf(cal.get(2) + 1)
		).toString();

		if (month.length() == 1) {
			month = "0" + month;
		}

		final String dateString = String.valueOf(cal.get(1)) + month + day;
		System.out.println("System date : - " + dateString);
		final TokenString bob = new TokenString(
			"org=802001&user=cvtest&date=" + dateString);
		System.out.println("String:\t" + bob);
		System.out.println("Encode:\t" + bob.encode());
		final TokenString joe = new TokenString(bob.encode());
		System.out.println("Decode:\t" + joe.decode());
	}

	public TokenString(final String token) {
		this.token = token;
	}

	public String decode() {
		String result = "";
		final Calendar cal = new GregorianCalendar();
		final int dayOfMonth = cal.get(5);
		final char[] asciiArray = this.unescape(
			this.token
		).toCharArray();

		for (int n = 0; n < asciiArray.length; ++n) {
			int ascii = asciiArray[n];
			ascii = (ascii == (38 + dayOfMonth + 10)) ? 38 :
				(((ascii - dayOfMonth - 10) > 47) ? (ascii - dayOfMonth - 10) :
					(123 - (48 - (ascii - dayOfMonth - 10))));
			result = String.valueOf(result) + this.ascii2text(ascii);
		}

		return result;
	}

	public String encode() {
		String result = "";
		final Calendar cal = new GregorianCalendar();
		final int dayOfMonth = cal.get(5);
		final char[] asciiArray = this.token.toCharArray();

		for (int n = 0; n < asciiArray.length; ++n) {
			int ascii = asciiArray[n];
			ascii =
				((dayOfMonth + ascii + 10) < 123) ? (dayOfMonth + ascii + 10) :
					(dayOfMonth + ascii + 10 - 123 + 48);
			result =
				String.valueOf(result) +
					((((ascii > 47) && (ascii < 58)) ||
					  ((ascii > 64) && (ascii < 91)) ||
					  ((ascii > 96) && (ascii < 123))) ?
						this.ascii2text(ascii) : this.unicode(ascii));
		}

		return result;
	}

	@Override
	public String toString() {
		return this.token;
	}

	private String ascii2text(final int arg) {
		return Character.valueOf(
			(char)arg
		).toString();
	}

	private String unescape(final String arg) {
		String result = "";
		final char[] characterArray = arg.toCharArray();

		for (int n = 0; n < characterArray.length; ++n) {
			if (characterArray[n] == '%') {
				final String hexxy =
					String.valueOf(characterArray[n + 1]) +
						characterArray[n + 2];
				n += 2;
				final int i = Integer.valueOf(hexxy, 16);
				result =
					String.valueOf(result) +
						Character.valueOf(
							(char)i
						).toString();
			}
			else {
				result = String.valueOf(result) + characterArray[n];
			}
		}

		return result;
	}

	private String unicode(final int arg) {
		return "%" +
			Integer.toString(
				arg, 16
			).toUpperCase();
	}

	private final int LOWERCHAR = 48;

	private final int STDSHIFT = 10;

	private final int UPPERCHAR = 123;

	private String token;

}