package au.com.cuscal.transactions.validator;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestServiceRequestValidator {

	public static final String REG_EXP_NEGATIVE_AMOUNT =
		"^-?(?:\\$\\s*)?(?:(?:\\d{0,3}(?:[, ]\\d{0,3})*[, ])+\\d{3}|\\d+)(?:\\.\\d{2})?(?:\\s*\\$)?$";

	@Test
	public void testValidateAmount_Negativeamount() {
		boolean isAmountValid = validateAmount("-$5,000,000.00");
		assertTrue(isAmountValid);
	}

	@Test
	public void testValidateAmount_PostiveAmount() {
		boolean isAmountValid = validateAmount("50");
		assertTrue(isAmountValid);
	}

	@Test
	public void testValidateAmount_Zero() {
		boolean isAmountValid = validateAmount("0");
		assertTrue(isAmountValid);
	}

	@Test
	public void testValidateAmount_Zero_Two() {
		boolean isAmountValid = validateAmount("-0");
		assertTrue(isAmountValid);
	}

	public boolean validateAmount(String amount) {
		Pattern pattern = Pattern.compile(REG_EXP_NEGATIVE_AMOUNT);

		Matcher matcher = pattern.matcher(amount);

		if (!matcher.matches()) {
			return false;
		}

		return true;
	}

}