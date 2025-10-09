package au.com.cuscal.transactions.validator;

import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.commons.TransactionAppProperties;
import au.com.cuscal.transactions.commons.Utility;
import au.com.cuscal.transactions.forms.TransactionForm;

import java.io.IOException;

import java.math.BigInteger;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Switch Transaction Search form validator
 *
 * @author Rajni
 *
 */
@Component(Constants.TRANSACTION_FORM_VALIDATOR)
public class TransactionSearchValidator implements Validator {

	/**
	 * Validation method for check EndTime should more than StartTime When Dates
	 * Are Equal
	 *
	 * @param TransactionForm
	 * @return boolean
	 */
	public boolean checkEndTimeStartTimeWhenDatesAreEqual(
		TransactionForm transactionForm) {

		boolean isValid = true;
		int endHr = 0;
		int endMin = 0;

		if ("24".equals(transactionForm.getEndTimeHr())) {
			endHr = 0;
			endMin = 0;
		}
		else {
			endHr = Integer.valueOf(
				transactionForm.getEndTimeHr()
			).intValue();
			endMin = Integer.valueOf(
				transactionForm.getEndTimeMin()
			).intValue();
		}

		int startHr = Integer.valueOf(
			transactionForm.getStartTimeHr()
		).intValue();

		if (startHr > endHr) {
			isValid = false;
		}
		else {
			if (startHr == endHr) {
				int startMin = Integer.valueOf(
					transactionForm.getStartTimeMin()
				).intValue();

				if ((startMin > endMin) || (startMin == endMin)) {
					isValid = false;
				}
			}
		}

		logger.debug(
			"[checkEndTimeStartTimeWhenDatesAreEqual] Transaction Search start time hour is: " +
				startHr);
		logger.debug(
			"[checkEndTimeStartTimeWhenDatesAreEqual] Transaction Search start time min is: " +
				startHr);
		logger.debug(
			"[checkEndTimeStartTimeWhenDatesAreEqual] Transaction Search end time hour is: " +
				endHr);
		logger.debug(
			"[checkEndTimeStartTimeWhenDatesAreEqual] Transaction Search end time min is: " +
				endMin);

		return isValid;
	}

	/**
	 * Validation method for Amount length
	 *
	 * @param String
	 * @return boolean
	 */
	public boolean checkForAmountLength(String amount) {
		boolean isValid = true;
		String amountWODot = "";

		if (amount.indexOf(".") != -1) {
			amountWODot = amount.substring(0, amount.indexOf("."));
		}
		else {
			amountWODot = amount;
		}

		if (!StringUtils.isBlank(amountWODot)) {
			int lengthCount = amountWODot.length();

			if (lengthCount > 12) {
				isValid = false;
			}
		}

		return isValid;
	}

	/**
	 * Validation method for check PanBin Value For SpecialChar
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return boolean
	 */
	public boolean checkPanBinValueForSpecialChar(
		TransactionForm transactionForm, Errors errors) {

		boolean isSpChar = false;

		if (!StringUtils.isBlank(transactionForm.getPanBin())) {
			String panOrBin = Utility.removeAllSpacesFromPanorBin(
				transactionForm.getPanBin(
				).trim());

			if (!Utility.validatePatternMatcher(
					Constants.REG_EXP_SPECIAL_CHAR, panOrBin)) {

				isSpChar = true;
				errors.rejectValue(
					"panBin", "transaction.search.panorbin.validation");
			}
		}

		return isSpChar;
	}

	/**
	 * Validation method for Pan/bin or Terminal id or Merchant id or External
	 * Transaction Id
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkTransactionDetailsIsNotEmpty(
		TransactionForm transactionForm, Errors errors) {

		if (StringUtils.isBlank(transactionForm.getPanBin()) &&
			StringUtils.isBlank(transactionForm.getTerminalId()) &&
			StringUtils.isBlank(transactionForm.getMerchantId()) &&
			StringUtils.isBlank(transactionForm.getExternalTransactionId()) &&
			StringUtils.isBlank(transactionForm.getStan()) &&
			StringUtils.isBlank(transactionForm.getCuscalId())) {

			//use the PropertiesLoader to read the properties in realtime
			Resource resource = new ClassPathResource(
				"transaction-search/transaction.properties");

			try {
				Properties properties = PropertiesLoaderUtils.loadProperties(
					resource);

				String transIdVisible = properties.getProperty(
					"transaction.search.ui.visibile.cuscalId");
				String stanVisible = properties.getProperty(
					"transaction.search.ui.visibile.stan");

				String[] fields = {"", ""};

				if (StringUtils.isNotBlank(transIdVisible) &&
					Boolean.parseBoolean(transIdVisible)) {

					fields[0] = ", Cuscal Id";
				}

				if (StringUtils.isNotBlank(stanVisible) &&
					Boolean.parseBoolean(stanVisible)) {

					fields[1] = ", Stan";
				}

				errors.rejectValue(
					"panBin", "transaction.search.transactionDetails.empty",
					fields, null);
			}
			catch (IOException e) {
				logger.error("Error reading transaction.properties");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Validation method for Amount
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkValidationForAmount(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getAmount())) {
			String amountVal = Utility.removeAndCovertInFloat(
				transactionForm.getAmount(
				).trim());

			try {
				Float.parseFloat(amountVal);
			}
			catch (NumberFormatException nfe) {
				logger.debug(
					"checkValidationForAmount - The amount has special char: " +
						amountVal);
				errors.rejectValue(
					"amount", "transaction.search.amount.validation");

				return;
			}

			if (!checkForAmountLength(amountVal)) {
				logger.debug(
					"checkValidationForAmount - Amount length is greater than 12 digits.");
				errors.rejectValue(
					"amount", "transaction.search.amount.validation");
			}
		}
	}

	/**
	 * Validation method for Cuscal id
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkValidationForCuscalId(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getCuscalId())) {
			int transactionIdLength = transactionForm.getCuscalId(
			).trim(
			).length();

			if (transactionIdLength > 38) {
				errors.rejectValue(
					"cuscalId", "transaction.search.cuscalId.validation");
			}

			try {
				new BigInteger(
					transactionForm.getCuscalId(
					).trim());
			}
			catch (NumberFormatException ne) {
				errors.rejectValue(
					"cuscalId", "transaction.search.cuscalId.validation");
			}

			transactionForm.setVisaIdOrCuscal(true);
		}
	}

	/**
	 * Validation method for End date
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return Errors
	 */
	public Errors checkValidationForEndDate(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getEndDate())) {
			if (!Utility.validateDate(
					transactionForm.getEndDate(), Constants.DATE_FORMAT)) {

				errors.rejectValue(
					"endDate", "transaction.search.date.validation");
			}
		}

		return errors;
	}

	/**
	 * Validation method for bin/terminal/card acceptor id search
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkValidationForMandatoryFieldSearch(
		TransactionForm transactionForm, Errors errors) {

		boolean isLoginTrue = false;
		int allowedNumberOfDays = 1;

		if (StringUtils.isNotBlank(transactionForm.getEndDate()) &&
			StringUtils.isNotBlank(transactionForm.getStartDate())) {

			Date toDate = Utility.formatDate(
				transactionForm.getEndDate() + " " +
					transactionForm.getEndTimeHr() + ":" +
						transactionForm.getEndTimeMin(),
				Constants.DATE_TIME_FORMAT_24HR);

			Date fromDate = Utility.formatDate(
				transactionForm.getStartDate() + " " +
					transactionForm.getStartTimeHr() + ":" +
						transactionForm.getStartTimeMin(),
				Constants.DATE_TIME_FORMAT_24HR);

			int minutes = Minutes.minutesBetween(
				new DateTime(fromDate), new DateTime(toDate)
			).getMinutes();

			try {
				if (null != transactionAppProperties.getTransProps(
					).getProperty(
						Constants.BIN_SEARCH_DAYS_ALLOWED
					)) {

					allowedNumberOfDays = Integer.parseInt(
						transactionAppProperties.getTransProps(
						).getProperty(
							Constants.BIN_SEARCH_DAYS_ALLOWED
						));
				}
				else {
					logger.error(
						"Error reading " + Constants.BIN_SEARCH_DAYS_ALLOWED +
							"from properties");
				}
			}
			catch (NumberFormatException e) {
				logger.error(
					"Error reading " + Constants.BIN_SEARCH_DAYS_ALLOWED +
						"from properties");
			}

			if ((minutes <= (allowedNumberOfDays * 60 * 24)) &&
				(toDate.compareTo(fromDate) >= 0)) {

				/**
				 * if
				 * (StringUtils.isNotBlank(transactionForm.getResponseCode())) {
				 * if (transactionForm.getResponseCode()
				 * .equalsIgnoreCase("00")) { errors.rejectValue("responseCode",
				 * "transaction.bin.search.responseCode.validation"); } } else {
				 * errors.rejectValue("responseCode",
				 * "transaction.bin.search.responseCode.validation"); }
				 */
			}
			else {
				isLoginTrue = true;
			}
		}
		else {
			isLoginTrue = true;
		}

		if (isLoginTrue) {
			errors.rejectValue(
				"endDate", "transaction.bin.search.enddate.validation",
				new String[] {
					transactionAppProperties.getTransProps(
					).getProperty(
						Constants.BIN_SEARCH_DAYS_ALLOWED
					)
				},
				null);

			/**
			 * if (StringUtils.isBlank(transactionForm.getResponseCode())) {
			 * errors.rejectValue("responseCode",
			 * "transaction.bin.search.responseCode.validation"); } else { if
			 * (transactionForm.getResponseCode().equalsIgnoreCase("00")) {
			 * errors.rejectValue("responseCode",
			 * "transaction.bin.search.responseCode.validation"); } }
			 */
		}
	}

	/**
	 * Validation method for Merchant id
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkValidationForMerchantAndTerminalId(
		TransactionForm transactionForm, Errors errors) {

		boolean isMearchant = false;
		boolean isTerminal = false;

		if (!StringUtils.isBlank(transactionForm.getTerminalId())) {
			int terminalIdLength = transactionForm.getTerminalId(
			).trim(
			).length();

			if (terminalIdLength > 15) {
				errors.rejectValue(
					"terminalId", "transaction.search.terminalId.validation");
			}
			else {
				isTerminal = true;
			}
		}

		if (!StringUtils.isBlank(transactionForm.getMerchantId())) {
			int merchantIdLength = transactionForm.getMerchantId(
			).trim(
			).length();

			if (merchantIdLength > 15) {
				errors.rejectValue(
					"merchantId",
					"transaction.search.cardAcceptorId.validation");
			}
			else {
				isMearchant = true;
			}
		}

		if (transactionForm.isBinTerminalOrCardAcceptor() ||
			StringUtils.isBlank(transactionForm.getPanBin())) {

			if (isTerminal || isMearchant)
				transactionForm.setBinTerminalOrCardAcceptor(true);
		}
	}

	/**
	 * Validation method for Message code
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkValidationForMessageCode(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getMessageCode())) {
			Integer messageCount = (Integer)transactionForm.getMessageCode(
			).trim(
			).length();

			if ((messageCount != 3) && (messageCount != 4)) {
				errors.rejectValue(
					"messageCode", "transaction.search.messageCode.validation");
			}
			else if (!Utility.validatePatternMatcher(
						Constants.REG_EXP_SPECIAL_CHAR,
						transactionForm.getMessageCode(
						).trim())) {

				errors.rejectValue(
					"messageCode", "transaction.search.messageCode.validation");
			}
		}
	}

	/**
	 * Validation method for Pan/bin length
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkValidationForPanBin(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getPanBin())) {
			String panBin = transactionForm.getPanBin(
			).trim();

			Integer panBinCount = (Integer)panBin.length();

			if (!transactionForm.isMaskedPan() &&
				(panBinCount.intValue() >= 6) &&
				(panBinCount.intValue() <= 9)) {

				if ((panBinCount.intValue() == 6) ||
					(panBinCount.intValue() == 8) ||
					(panBinCount.intValue() == 9)) {

					logger.debug(
						"checkValidationForPanBin  - valid BIN is " + panBin);
					transactionForm.setPan(false);
					transactionForm.setBinTerminalOrCardAcceptor(true);
				}
				else {
					errors.rejectValue(
						"panBin", "transaction.search.panorbin.validation");
				}
			}
			else {
				if ((panBinCount.intValue() >= 13) &&
					(panBinCount.intValue() <= 19)) {

					// check if its a masked pan , and valid as a masked pan if it is.

					if (transactionForm.isMaskedPan()) {
						logger.debug(
							"checkValidationForPanBin  - MASKED PAN is " +
								panBin);
						panBin = normalisePan(panBin);

						if (!isValidMaskedPan(panBin)) {
							logger.debug(
								"checkValidationForPanBin  - not a Valid MASKED PAN is " +
									panBin);
							transactionForm.setMaskedPan(false);
							errors.rejectValue(
								"panBin",
								"transaction.search.maskedpan.validation");
						}
						else {
							logger.debug(
								"checkValidationForPanBin  - Normalised MASKED PAN set in form is " +
									panBin);
							transactionForm.setNormalisedPan(panBin);
						}
					}
					else {
						logger.debug(
							"checkValidationForPanBin  - valid PAN is " +
								panBin);
						transactionForm.setPan(true);
					}
				}
				else {
					errors.rejectValue(
						"panBin", "transaction.search.panorbin.validation");
				}
			}
		}
	}

	/**
	 * Validation method for Response code
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkValidationForResponseCode(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getResponseCode())) {
			Integer responseCount = (Integer)transactionForm.getResponseCode(
			).trim(
			).length();

			if (responseCount != 2) {
				errors.rejectValue(
					"responseCode",
					"transaction.search.responseCode.validation");
			}
			else if (!Utility.validatePatternMatcher(
						Constants.REG_EXP_SPECIAL_CHAR,
						transactionForm.getResponseCode(
						).trim())) {

				errors.rejectValue(
					"responseCode",
					"transaction.search.responseCode.validation");
			}
		}
	}

	/**
	 * Validation method for RRN
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkValidationForRRN(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getRrn())) {
			int externalIdLength = transactionForm.getRrn(
			).trim(
			).length();

			if (externalIdLength > 12) {
				errors.rejectValue("rrn", "transaction.search.rrn.validation");
			}
			else {
				transactionForm.setStanOrRrn(true);
			}
		}
	}

	/**
	 * Validation method for STAN
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkValidationForStan(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getStan())) {
			int externalIdLength = transactionForm.getStan(
			).trim(
			).length();

			if (externalIdLength > 6) {
				errors.rejectValue(
					"stan", "transaction.search.stan.validation");
			}
			else {
				try {
					Integer.parseInt(
						transactionForm.getStan(
						).trim());
					transactionForm.setStanOrRrn(true);
				}
				catch (NumberFormatException ne) {
					errors.rejectValue(
						"stan", "transaction.search.stan.validation");
				}
			}
		}
	}

	/**
	 * Validation method for Start date
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return Errors
	 */
	public Errors checkValidationForStartDate(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getStartDate())) {
			if (!Utility.validateDate(
					transactionForm.getStartDate(), Constants.DATE_FORMAT)) {

				errors.rejectValue(
					"startDate", "transaction.search.date.validation");
			}
		}

		return errors;
	}

	/**
	 * Validation method for External Transaction id
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void checkValidationOnExternalId(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getExternalTransactionId())) {
			int externalIdLength = transactionForm.getExternalTransactionId(
			).trim(
			).length();

			if (externalIdLength > 15) {
				errors.rejectValue(
					"externalTransactionId",
					"transaction.search.externalTransactionId.validation");
			}

			if (Utility.isNotAlphaNumeric(
					transactionForm.getExternalTransactionId(
					).trim())) {

				errors.rejectValue(
					"externalTransactionId",
					"transaction.search.externalTransactionId.validation");
			}

			transactionForm.setVisaIdOrCuscal(true);
		}
	}

	/**
	 * Validation method for fromDate NoGreater Than ToDate
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	public void fromDateNoGreaterThanToDate(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getEndDate()) &&
			!StringUtils.isBlank(transactionForm.getStartDate())) {

			String newEndDate = "";
			Date toDate = null;

			if ("24".equals(transactionForm.getEndTimeHr())) {
				if (!StringUtils.isBlank(transactionForm.getEndDate())) {
					newEndDate = Utility.getNextDayForThisDate(
						transactionForm.getEndDate());
				}
			}

			if (!StringUtils.isBlank(newEndDate)) {
				toDate = Utility.formatDate(newEndDate, Constants.DATE_FORMAT);
			}
			else {
				toDate = Utility.formatDate(
					transactionForm.getEndDate(), Constants.DATE_FORMAT);
			}

			Date fromDate = Utility.formatDate(
				transactionForm.getStartDate(), Constants.DATE_FORMAT);

			if (fromDate.compareTo(toDate) == 0) {
				if (!checkEndTimeStartTimeWhenDatesAreEqual(transactionForm)) {
					errors.rejectValue(
						"startDate", "transaction.search.fromDate.toDate");
				}
			}
			else {
				if (fromDate.after(toDate)) {
					errors.rejectValue(
						"startDate", "transaction.search.fromDate.toDate");
				}
			}
		}
	}

	/**
	 * Validation masked pan format
	 *
	 * @param normalisedMaskedPan
	 * @return boolean
	 */
	public boolean isValidMaskedPan(String normalisedMaskedPan) {
		boolean isMaskedPanValid = false;

		isMaskedPanValid = Utility.validatePatternMatcher(
			Constants.REG_EXP_MASKED_PAN_SPECIAL_CHAR, normalisedMaskedPan);

		return isMaskedPanValid;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see Validator#supports(Class)
	 */
	public boolean supports(Class<?> arg0) {

		// TODO Auto-generated method stub

		return false;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see Validator#validate(Object,
	 * Errors)
	 */
	public void validate(Object target, Errors errors) {
		TransactionForm transactionForm = (TransactionForm)target;

		checkTransactionDetailsIsNotEmpty(transactionForm, errors);

		errors = checkValidationForStartDate(transactionForm, errors);
		errors = checkValidationForEndDate(transactionForm, errors);

		checkValidationOnExternalId(transactionForm, errors);

		if (StringUtils.isNotBlank(transactionForm.getPanBin())) {
			String pan = transactionForm.getPanBin();

			pan = normalisePan(pan);

			if (Character.isDigit(pan.charAt(0))) {
				boolean hasSpCharInPan = false;
				isMaskedPan(pan, transactionForm);

				if (transactionForm.isMaskedPan()) {
					//do nothing because checkValidationForPanBin will validate the mask pan
				}
				else {
					hasSpCharInPan = checkPanBinValueForSpecialChar(
						transactionForm, errors);
				}

				if (!hasSpCharInPan)
					checkValidationForPanBin(transactionForm, errors);
			}
			else {
				errors.rejectValue(
					"panBin", "transaction.search.panorbin.validation");
			}
		}

		checkValidationForMerchantAndTerminalId(transactionForm, errors);

		checkValidationForRRN(transactionForm, errors);
		checkValidationForStan(transactionForm, errors);
		checkValidationForCuscalId(transactionForm, errors);

		if (StringUtils.isBlank(transactionForm.getStartDate()) ||
			StringUtils.isBlank(transactionForm.getEndDate())) {

			ValidationUtils.rejectIfEmptyOrWhitespace(
				errors, "startDate", "transaction.search.startDate.empty");
			ValidationUtils.rejectIfEmptyOrWhitespace(
				errors, "endDate", "transaction.search.endDate.empty");
		}
		else if ((transactionForm.isBinTerminalOrCardAcceptor() ||
				  transactionForm.isStanOrRrn()) &&
				 !(transactionForm.isVisaIdOrCuscal() ||
				   transactionForm.isPan() || transactionForm.isMaskedPan())) {

			checkValidationForMandatoryFieldSearch(transactionForm, errors);
		}

		if (!errors.hasFieldErrors("startDate") &&
			!errors.hasFieldErrors("endDate"))
			fromDateNoGreaterThanToDate(transactionForm, errors);

		checkValidationForAmount(transactionForm, errors);
		checkValidationForResponseCode(transactionForm, errors);
	}

	/**
	 * Method to check PanBin Value For Masked Pan
	 *
	 * @param TransactionForm
	 * @param String
	 * @return void
	 */
	protected void isMaskedPan(
		String normalisedPan, TransactionForm transactionForm) {

		Integer panBinCount = (Integer)normalisedPan.length();

		if ((panBinCount.intValue() >= 13) && (panBinCount.intValue() <= 19)) {
			int i = normalisedPan.indexOf(MASK_PAN_CHAR);

			if (i >= 0) {
				logger.debug("isMaskedPan  - pan is Masked" + normalisedPan);
				transactionForm.setMaskedPan(true);
			}
		}
	}

	/**
	 * Normalise method for Pan
	 *
	 * @param String
	 * @return String
	 */
	protected String normalisePan(String pan) {
		char[] maskAlternatives = {'#', '*'};
		String normalisedPan = Utility.removeAllSpacesFromPanorBin(
			pan.toUpperCase(
			).trim());

		for (int i = 0; i < maskAlternatives.length; i++) {
			normalisedPan = normalisedPan.replace(
				maskAlternatives[i], MASK_PAN_CHAR);
		}

		logger.debug("normalisePan  - pan is " + normalisedPan);

		return normalisedPan;
	}

	private static Logger logger = LoggerFactory.getLogger(
		TransactionSearchValidator.class);

	private char MASK_PAN_CHAR = 'X';

	/**
	 * TransactionAppProperties object
	 */
	@Autowired
	@Qualifier("transactionAppProperties")
	private TransactionAppProperties transactionAppProperties;

}