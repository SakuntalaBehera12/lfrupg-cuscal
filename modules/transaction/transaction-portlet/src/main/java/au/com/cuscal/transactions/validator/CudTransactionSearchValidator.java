package au.com.cuscal.transactions.validator;

import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.commons.Utility;
import au.com.cuscal.transactions.forms.TransactionForm;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * CUD Transaction Search form validator
 *
 * @author Rajni
 *
 */
@Component(Constants.CUD_TRANSACTION_FORM_VALIDATOR)
public class CudTransactionSearchValidator implements Validator {

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

		checkPanBinNotEmpty(transactionForm, errors);
		boolean isSpCharPan = checkPanBinValueForSpecialChar(
			transactionForm, errors);

		if (!isSpCharPan) {
			checkValidationForPanBin(transactionForm, errors);
		}

		if (transactionForm.isBinTerminalOrCardAcceptor()) {
			checkValidationForBinSearch(transactionForm, errors);
		}
		else {
			ValidationUtils.rejectIfEmptyOrWhitespace(
				errors, "startDate", "transaction.search.startDate.empty");
			ValidationUtils.rejectIfEmptyOrWhitespace(
				errors, "endDate", "transaction.search.endDate.empty");
		}

		errors = checkValidationForStartDate(transactionForm, errors);
		errors = checkValidationForEndDate(transactionForm, errors);

		if (!errors.hasFieldErrors("startDate") &&
			!errors.hasFieldErrors("endDate"))
			fromDateNoGreaterThanToDate(transactionForm, errors);
	}

	/**
	 * Validation method for check EndTime should more than StartTime When Dates
	 * Are Equal
	 *
	 * @param TransactionForm
	 * @return boolean
	 */
	private boolean checkEndTimeStartTimeWhenDatesAreEqual(
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
	 * Validation method for Pan/bin
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	private void checkPanBinNotEmpty(
		TransactionForm transactionForm, Errors errors) {

		if (StringUtils.isBlank(transactionForm.getPanBin())) {
			errors.rejectValue(
				"panBin", "cud.transaction.search.panorbin.validation");
		}
	}

	/**
	 * Validation method for check PanBin Value For SpecialChar
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return boolean
	 */
	private boolean checkPanBinValueForSpecialChar(
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
					"panBin", "cud.transaction.search.panorbin.validation");
			}
		}

		return isSpChar;
	}

	/**
	 * Validation method for bin/terminal/card acceptor id search
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	private void checkValidationForBinSearch(
		TransactionForm transactionForm, Errors errors) {

		if (StringUtils.isNotBlank(transactionForm.getEndDate()) &&
			StringUtils.isNotBlank(transactionForm.getStartDate())) {

			Date toDate = Utility.formatDate(
				transactionForm.getEndDate(), Constants.DATE_FORMAT);
			Date fromDate = Utility.formatDate(
				transactionForm.getStartDate(), Constants.DATE_FORMAT);

			if (toDate.compareTo(fromDate) != 0) {
				errors.rejectValue(
					"endDate", "transaction.bin.search.enddate.validation");
			}
		}
		else {
			errors.rejectValue(
				"endDate", "transaction.bin.search.enddate.validation");
		}
	}

	/**
	 * Validation method for End date
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return Errors
	 */
	private Errors checkValidationForEndDate(
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
	 * Validation method for Pan/bin length
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	private void checkValidationForPanBin(
		TransactionForm transactionForm, Errors errors) {

		if (!StringUtils.isBlank(transactionForm.getPanBin())) {
			Integer panBinCount = (Integer)transactionForm.getPanBin(
			).trim(
			).length();

			// TODO uncomment following to re-enable BIN search validation.

			/*			if (panBinCount.intValue() >= 6 && panBinCount.intValue() <= 9) {
							if (panBinCount.intValue() == 6 || panBinCount.intValue() == 9) {
								transactionForm.setBinTerminalOrCardAcceptor(true);
							} else {
								errors.rejectValue("panBin",
										"transaction.search.panorbin.validation");
							}
						} else {*/
			if ((panBinCount.intValue() >= 13) &&
				(panBinCount.intValue() <= 19)) {

				transactionForm.setBinTerminalOrCardAcceptor(false);
			}
			else {
				errors.rejectValue(
					"panBin", "cud.transaction.search.panorbin.validation");
			}
			//			}
		}
	}

	/**
	 * Validation method for Start date
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return Errors
	 */
	private Errors checkValidationForStartDate(
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
	 * Validation method for fromDate NoGreater Than ToDate
	 *
	 * @param TransactionForm
	 * @param Errors
	 * @return void
	 */
	private void fromDateNoGreaterThanToDate(
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

	private static Logger logger = LoggerFactory.getLogger("cudLogging");

}