package au.com.cuscal.cards.validator;

import au.com.cuscal.cards.commons.Constants;
import au.com.cuscal.cards.commons.Utility;
import au.com.cuscal.cards.forms.CardSearchForm;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Switch Card Search form validator
 *
 * @author Rajni
 *
 */
@Component("cardsSearchValidator")
public class CardsSearchValidator implements Validator {

	/**
	 * Validation method for check PanBin Value For SpecialChar
	 *
	 * @param panOrBin <em>String</em>
	 * @return boolean
	 */
	public boolean checkPanBinValueForSpecialChar(String panOrBin) {
		boolean isSpChar = false;

		if (!Utility.validatePatternMatcher(
				Constants.REG_EXP_ONLY_DIGITS, panOrBin)) {

			logger.debug("Here is the  pan is not valid and have special char");
			isSpChar = true;
		}

		return isSpChar;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see Validator#supports(Class)
	 */
	public boolean supports(Class<?> arg0) {
		return false;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see Validator#validate(Object,
	 * Errors)
	 */
	public void validate(Object target, Errors errors) {
		CardSearchForm cardSearchForm = (CardSearchForm)target;
		logger.debug("validate - start");

		logger.debug("checking manditory fields");
		checkMandatoryCriteria(cardSearchForm, errors);

		// valid card expiry date

		if (StringUtils.isNotBlank(cardSearchForm.getExpiryMonth()) ||
			StringUtils.isNotBlank(cardSearchForm.getExpiryYear())) {

			boolean validDate = true;

			String mm = cardSearchForm.getExpiryMonth();
			String yyyy = cardSearchForm.getExpiryYear();

			logger.debug("Validating input date: *" + mm + "/" + yyyy + "*");
			mm = "".equals(mm) ? CardSearchForm.EXPIRY_DATE_MM_DEFAULT : mm;
			yyyy =
				"".equals(yyyy) ? CardSearchForm.EXPIRY_DATE_YYYY_DEFAULT :
					yyyy;
			logger.debug(
				"Validating input date (normalised): *" + mm + "/" + yyyy +
					"*");

			if ((CardSearchForm.EXPIRY_DATE_MM_DEFAULT.equalsIgnoreCase(mm) &&
				 !CardSearchForm.EXPIRY_DATE_YYYY_DEFAULT.equalsIgnoreCase(
					 yyyy)) ||
				(!CardSearchForm.EXPIRY_DATE_MM_DEFAULT.equalsIgnoreCase(mm) &&
				 CardSearchForm.EXPIRY_DATE_YYYY_DEFAULT.equalsIgnoreCase(
					 yyyy))) {

				logger.debug("MM/YYYY - either the mm or yyyy part is missing");
				validDate = false;
			}

			if (!StringUtils.equalsIgnoreCase(
					CardSearchForm.EXPIRY_DATE_MM_DEFAULT, mm)) {

				validDate = validDate && validationForExpiryMonth(mm);
				logger.debug("Valid date in month check: " + validDate);
			}

			// Check for a valid Year.

			if (!StringUtils.equalsIgnoreCase(
					CardSearchForm.EXPIRY_DATE_YYYY_DEFAULT, yyyy)) {

				// only validate the year portion if the month portion is valid.

				validDate = validDate && validationForExpiryYear(yyyy);
				logger.debug("Valid date in year check: " + validDate);
			}

			if (!validDate) {
				errors.rejectValue(
					"expiryMonth", "cards.search.expirydate.validation");
			}
		}

		validationForIssuerNameAgainstWSOrgName(cardSearchForm, errors);
		logger.debug("validate - end");
	}

	/**
	 * Validate the mandatory fields of the Card Search form.
	 *
	 * @param cardSearchForm <em>CardSearchForm</em>
	 * @param errors <em>Errors</em>
	 */
	private void checkMandatoryCriteria(
		CardSearchForm cardSearchForm, Errors errors) {

		if (StringUtils.isNotBlank(cardSearchForm.getPanOrBin())) {

			// Check for a valid PAN or BIN.

			validationForPan(cardSearchForm, errors);
		}
		else {
			if (cardSearchForm.getOrganisationsMap(
				).size() == 1) {

				logger.debug(
					"[checkMandatoryCriteria] There is only one organisation");
				validateFormWithOneOrganisation(cardSearchForm, errors);
			}
			else {
				if (StringUtils.isNotBlank(cardSearchForm.getIssuer()) &&
					StringUtils.isBlank(cardSearchForm.getCardholderName())) {

					logger.debug(
						"[checkMandatoryCriteria] Empty cardholder name");
					errors.rejectValue(
						"panOrBin", "cards.search.mandatory.empty");
				}
				else if (StringUtils.isBlank(cardSearchForm.getIssuer()) &&
						 StringUtils.isNotBlank(
							 cardSearchForm.getCardholderName())) {

					logger.debug("[checkMandatoryCriteria] Empty issuer name");
					errors.rejectValue(
						"panOrBin", "cards.search.mandatory.empty");
				}
				else if (StringUtils.isBlank(cardSearchForm.getIssuer()) &&
						 StringUtils.isBlank(
							 cardSearchForm.getCardholderName())) {

					logger.debug(
						"[checkMandatoryCriteria] Empty issuer and cardholder and PAN");
					errors.rejectValue(
						"panOrBin", "cards.search.mandatory.empty");
				}
			}
		}
	}

	/**
	 * Validate the form when the user has access to cards from only one Organisation.
	 *
	 * @param cardSearchForm <em>CardSearchForm</em>
	 * @param errors
	 */
	private void validateFormWithOneOrganisation(
		CardSearchForm cardSearchForm, Errors errors) {

		if (StringUtils.isNotBlank(cardSearchForm.getPanOrBin())) {
			logger.debug(
				"[validateFormWithOneOrganisation] Pan is not empty and validating that");
			validationForPan(cardSearchForm, errors);
		}
		else if (StringUtils.isNotBlank(cardSearchForm.getCardholderName()) ||
				 StringUtils.isNotBlank(cardSearchForm.getPostCode())) {

			logger.debug(
				"[validateFormWithOneOrganisation] Cardholder name is not empty and validating that");

			if (StringUtils.isBlank(cardSearchForm.getCardholderName())) {
				errors.rejectValue(
					"panOrBin", "cards.search.mandatory.one.org.empty");
			}
		}
		else {
			logger.debug(
				"[validateFormWithOneOrganisation] Both are empty and displaying appropriate error message");
			errors.rejectValue(
				"panOrBin", "cards.search.mandatory.one.org.empty");
		}
	}

	/**
	 * Validation method to check for a proper Expiry date.
	 *
	 * @param mm <em>String</em>
	 * @return boolean
	 */
	private boolean validationForExpiryMonth(String mm) {
		logger.debug("validationForExpiryMonth - start");

		boolean validMonth = false;

		try {
			logger.debug("about to parse month: " + mm);
			int month = Integer.parseInt(mm);
			logger.debug("month parsed as: " + month);

			if ((month <= 12) && (month > 0)) {
				validMonth = true;
			}
		}
		catch (NumberFormatException ne) {
			validMonth = false;
		}

		logger.debug("validationForExpiryMonth - end");

		return validMonth;
	}

	/**
	 * Validate the Year portion of the expiry date to make sure that it is in 4
	 * digits.
	 *
	 * @param yyyy <em>String</em>
	 * @return boolean
	 */
	private boolean validationForExpiryYear(String yyyy) {
		boolean validYear = false;

		if (yyyy.length() == 4) {
			try {
				int year = Integer.parseInt(yyyy);

				if (year >= 0) {
					validYear = true;
				}
			}
			catch (NumberFormatException ne) {
				validYear = false;
			}
		}

		return validYear;
	}

	/**
	 * Check for a valid Issuer name from the list of org name we got from Web
	 * service .
	 *
	 * @param cardSearchForm <em>CardSearchForm</em>
	 * @param errors <em>Errors</em>
	 */
	private void validationForIssuerNameAgainstWSOrgName(
		CardSearchForm cardSearchForm, Errors errors) {

		if (StringUtils.isNotBlank(cardSearchForm.getIssuer())) {
			String issuerName = cardSearchForm.getIssuer();

			if (null != cardSearchForm.getOrganisationsMap()) {
				Map<String, String> organisationsMap =
					(TreeMap<String, String>)
						cardSearchForm.getOrganisationsMap();

				if (organisationsMap.containsKey(issuerName)) {
					cardSearchForm.setIssuerShortName(
						organisationsMap.get(issuerName));
					logger.debug(
						"The organisation short name, when hit by back button on details page is  " +
							cardSearchForm.getIssuerShortName());
					logger.debug(
						"[validationForIssuerNameAgainstWSOrgName] The Organisation Map size is: " +
							cardSearchForm.getOrganisationsMap(
							).size());
				}
				else {
					errors.rejectValue(
						"issuer", "cards.search.issuer.validation");
				}
			}
		}
	}

	/**
	 * Validation method for checking a valid PAN.
	 *
	 * @param cardSearchForm <em>CardSearchForm</em>
	 * @param errors <em>Errors</em>
	 */
	private void validationForPan(
		CardSearchForm cardSearchForm, Errors errors) {

		if (StringUtils.isNotBlank(cardSearchForm.getPanOrBin())) {
			boolean isInvalid = false;
			String normalizedPan = Utility.removeAllSpacesFromPan(
				cardSearchForm.getPanOrBin());

			Integer panLength = (Integer)normalizedPan.length();

			if ((panLength.intValue() < Constants.MINIMUM_PAN_LENGTH) ||
				(panLength.intValue() > Constants.MAXIMUM_PAN_LENGTH)) {

				isInvalid = true;
			}

			if (checkPanBinValueForSpecialChar(normalizedPan)) {
				isInvalid = true;
			}

			if (isInvalid) {
				errors.rejectValue("panOrBin", "cards.search.pan.validation");
			}
		}
	}

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(
		CardsSearchValidator.class);

}