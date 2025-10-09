package au.com.cuscal.transactions.commons;

import junit.framework.Assert;

import org.junit.Test;

public class TestUtility {

	@Test
	public void testFormatCSBDDateEmpty() {
		Assert.assertEquals(
			"testFormatCSBDDate", "", Utility.formatCSBDDate(""));
	}

	@Test
	public void testFormatCSBDDateNull() {
		Assert.assertEquals(
			"testFormatCSBDDate", null, Utility.formatCSBDDate(null));
	}

	@Test
	public void testFormatCSBDDateValid() {
		Assert.assertEquals(
			"testFormatCSBDDate", "March 02", Utility.formatCSBDDate("0302"));
	}

	@Test
	public void testFormatCSBDDateValidDec() {
		Assert.assertEquals(
			"testFormatCSBDDate", "December 12",
			Utility.formatCSBDDate("1212"));
	}

	@Test
	public void testFormatCSBDDateZero() {
		Assert.assertEquals(
			"testFormatCSBDDate", "0", Utility.formatCSBDDate("0"));
	}

	@Test
	public void testIsAlphaNumericAlpha() {
		Assert.assertFalse(Utility.isNotAlphaNumeric("ABCDefTYY"));
	}

	@Test
	public void testIsAlphaNumericAlphaNumeric() {
		Assert.assertFalse(Utility.isNotAlphaNumeric("123ertyu890"));
	}

	@Test
	public void testIsAlphaNumericEmpty() {
		Assert.assertTrue(Utility.isNotAlphaNumeric(""));
	}

	@Test
	public void testIsAlphaNumericEmptySpaces() {
		Assert.assertTrue(Utility.isNotAlphaNumeric("    "));
	}

	@Test
	public void testIsAlphaNumericNull() {
		Assert.assertTrue(Utility.isNotAlphaNumeric(null));
	}

	@Test
	public void testIsAlphaNumericNumeric() {
		Assert.assertFalse(Utility.isNotAlphaNumeric("123123423423"));
	}

	@Test
	public void testIsAlphaNumericSpace() {
		Assert.assertFalse(Utility.isNotAlphaNumeric(" 123ertyu8 "));
	}

	@Test
	public void testIsAlphaNumericSpecialCharacters() {
		Assert.assertTrue(Utility.isNotAlphaNumeric("123ertyu8_90"));
	}

}