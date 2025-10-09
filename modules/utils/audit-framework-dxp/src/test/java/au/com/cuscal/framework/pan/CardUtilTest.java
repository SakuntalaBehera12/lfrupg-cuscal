package au.com.cuscal.framework.pan;

import static org.junit.Assert.fail;

import au.com.cuscal.framework.cards.CardUtil;
import au.com.cuscal.framework.cards.IssuerCategory;
import au.com.cuscal.framework.cards.Scheme;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.junit.BeforeClass;
import org.junit.Test;

public class CardUtilTest {

	// random numbers that happen to conform to format of certain scheme types

	String[] mastercard = {
		"5115958381240696", "5531805436515828", "5312404754348288",
		"5377232698746016", "5112362737272094", "5168346678266010",
		"5224158786168885", "5530397224825230", "5452114702591554",
		"5482317232560213"
	};
	String[] visa16 = {
		"4485918933413030", "4929962533856697", "4024007124369595",
		"4916253250513892", "4024007171683120", "4556789692235847",
		"4539979964452533", "4716153855191932", "4916087523786103",
		"4716026635728928"
	};
	String[] visa13 = {
		"4539008621313", "4082359606997", "4929724865619", "4726787487095",
		"4916495350970"
	};
	String[] amex = {
		"372095596213900", "343884751890339", "372424634183810",
		"342153532917097", "373077310773194"
	};
	String[] discover = {
		"6011473994439852", "6011929800208047", "6011027447694081"
	};
	String[] diners = {"36122052952165", "30011023877534", "30135488120450"};
	String[] enRoute = {
		"201407069220444", "201467935572618", "214996226677640"
	};
	String[] jcb15 = {"180040544051855", "180012674473001", "180063537461428"};
	String[] jcb16 = {
		"3158018559816257", "3337415053425946", "3528254236962338"
	};
	String[] voyager = {
		"869986392445961", "869981379869119", "869935022266149"
	};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetIssuerCategories() {
		IssuerCategory[] issuerCategories = cardUtil.getIssuerCategories();

		for (IssuerCategory issuerCategory : issuerCategories) {
			System.out.println(
				"testGetIssuerCategories: found issuerCategory [" +
					issuerCategory.getMII() + ": " +
						issuerCategory.getCategory() + "]");
		}
	}

	@Test
	public void testGetIssuerCategory() {
		if (!testIssuerCategory(mastercard))
			fail("Mastercard failed testGetIssuerCategory");

		if (!testIssuerCategory(visa13))
			fail("Visa13 failed testGetIssuerCategory");

		if (!testIssuerCategory(visa16))
			fail("Visa16 failed testGetIssuerCategory");
	}

	@Test
	public void testGetScheme() {
		if (!areValidSchemeCards(cardUtil.MASTERCARD.getName(), mastercard))
			fail("Mastercard getScheme failed");

		if (!areValidSchemeCards(cardUtil.VISA.getName(), visa13))
			fail("Visa13 getScheme failed");

		if (!areValidSchemeCards(cardUtil.VISA.getName(), visa16))
			fail("Visa16 getScheme failed");

		if (!areValidSchemeCards(cardUtil.AMEX.getName(), amex))
			fail("Amex getScheme failed");

		if (!areValidSchemeCards(cardUtil.DINERS.getName(), diners))
			fail("Diners getScheme failed");

		if (!areValidSchemeCards(cardUtil.DISCOVER.getName(), discover))
			fail("Discover getScheme failed");

		if (!areValidSchemeCards(cardUtil.ENROUTE.getName(), enRoute))
			fail("Enroute getScheme failed");

		if (!areValidSchemeCards(cardUtil.JCB16.getName(), jcb16))
			fail("Jcb16 getScheme failed");

		if (!areValidSchemeCards(cardUtil.JCB15.getName(), jcb15))
			fail("Jcb15 getScheme failed");
	}

	@Test
	public void testGetSchemes() {
		Scheme[] schemes = cardUtil.getSchemes();

		for (Scheme scheme : schemes) {
			System.out.println(
				"testGetSchemes: found scheme [" + scheme.getName() + "]");
		}
	}

	@Test
	public void testMaskString() {
		if (!testMaskPan(mastercard))
			fail("Mastercard testMaskString failed");
	}

	@Test
	public void testMaskStringChar() {
		if (!testMaskPan(mastercard, '#'))
			fail("Mastercard testMaskStringChar failed");
	}

	@Test
	public void testPanValidate() {
		if (!validatePan(mastercard))
			fail("Mastercard pan validation failed");

		if (!validatePan(visa13))
			fail("Visa13 pan validation failed");

		if (!validatePan(visa16))
			fail("Visa16 pan validation failed");

		if (!validatePan(amex))
			fail("Amex pan validation failed");

		if (!validatePan(diners))
			fail("Diners pan validation failed");

		if (!validatePan(discover))
			fail("Discover pan validation failed");

		if (!validatePan(enRoute))
			fail("enRoute pan validation failed");

		if (!validatePan(jcb16))
			fail("Jcb16 pan validation failed");

		if (!validatePan(jcb15))
			fail("Jcb15 pan validation failed");
	}

	@Test
	public void testSecureNumberGeneration() throws NoSuchAlgorithmException {
		SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");

		// generate a random number

		String randomNum = Integer.valueOf(
			prng.nextInt()
		).toString();

		System.out.printf("SecureRandom Number: %s\n", randomNum);
	}

	@Test
	public void testSecureNumberGeneration2() throws NoSuchAlgorithmException {
		SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");

		// generate a random number

		String randomNum = Integer.valueOf(
			prng.nextInt(9999)
		).toString();

		System.out.printf("SecureRandom Number2: %s\n", randomNum);
	}

	protected boolean areValidSchemeCards(String schemeName, String[] pans) {
		for (String pan : pans) {
			Scheme scheme = cardUtil.getScheme(pan);

			System.out.println("Testing " + pan + " with scheme " + schemeName);

			if (scheme == null) {
				return false;
			}

			if (!scheme.getName(
				).equals(
					schemeName
				)) {

				return false;
			}
		}

		return true;
	}

	protected boolean testIssuerCategory(String[] pans) {
		for (String pan : pans) {
			IssuerCategory issuerCategory = cardUtil.getIssuerCategory(pan);

			if (!pan.startsWith(issuerCategory.getMII())) {
				return false;
			}
		}

		return true;
	}

	protected boolean testMaskPan(String[] pans) {
		for (String pan : pans) {
			String maskedPan = cardUtil.mask(pan);
			System.out.println(pan);
			System.out.println(maskedPan);

			if (maskedPan.equals(pan))

				return false;
		}

		return true;
	}

	protected boolean testMaskPan(String[] pans, char maskChar) {
		for (String pan : pans) {
			String maskedPan = cardUtil.mask(pan, maskChar);
			System.out.println(pan);
			System.out.println(maskedPan);

			if (maskedPan.equals(pan))

				return false;
		}

		return true;
	}

	protected boolean validatePan(String[] pans) {
		for (String pan : pans) {
			System.out.println("validating pan " + pan);

			if (!cardUtil.panValidate(pan)) {
				return false;
			}
		}

		return true;
	}

	private CardUtil cardUtil = CardUtil.getInstance();

}

/*

*/