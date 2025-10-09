package au.com.cuscal.transactions.validator;

import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.commons.Utility;
import au.com.cuscal.transactions.forms.PinChangeSearchForm;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("pinChangeHistorySearchValidator")
public class PinChangeHistorySearchValidator implements Validator {

	/**
	 * Validation method for check EndTime should more than StartTime When Dates Are Equal
	 * @param pinChangeForm
	 * @return boolean
	 */
	public boolean checkEndTimeStartTimeWhenDatesAreEqual(
		PinChangeSearchForm pinChangeForm) {

		boolean isValid = true;

		int endHr = 0;
		int endMin = 0;

		if ("24".equals(pinChangeForm.getEndTimeHr())) {
			endHr = 0;
			endMin = 0;
		}
		else {
			endHr = Integer.valueOf(
				pinChangeForm.getEndTimeHr()
			).intValue();
			endMin = Integer.valueOf(
				pinChangeForm.getEndTimeMin()
			).intValue();
		}

		int startHr = Integer.valueOf(
			pinChangeForm.getStartTimeHr()
		).intValue();

		if (startHr > endHr) {
			isValid = false;
		}
		else {
			if (startHr == endHr) {
				int startMin = Integer.valueOf(
					pinChangeForm.getStartTimeMin()
				).intValue();

				if ((startMin > endMin) || (startMin == endMin)) {
					isValid = false;
				}
			}
		}

		return isValid;
	}

	public boolean supports(Class<?> arg0) {
		return false;
	}

	public void validate(Object form, Errors errors) {
		logger.debug("validate - start");

		PinChangeSearchForm pinChangeForm = (PinChangeSearchForm)form;

		//Both are blank.

		if (StringUtils.isBlank(pinChangeForm.getPanBin()) &&
			StringUtils.isBlank(pinChangeForm.getTerminalId())) {

			errors.rejectValue("panBin", "pin.change.pan.terminal.id.empty");
		}
		else if (StringUtils.isNotBlank(pinChangeForm.getPanBin()) &&
				 StringUtils.isNotBlank(pinChangeForm.getTerminalId())) {

			errors.rejectValue(
				"panBin", "pin.change.pan.terminal.id.not.empty");
		}
		else {
			if (StringUtils.isNotBlank(pinChangeForm.getPanBin())) {
				pinChangeCheckForValidPan(pinChangeForm, errors);
			}

			if (StringUtils.isNotBlank(pinChangeForm.getTerminalId())) {
				pinChangeCheckForValidTerminalId(pinChangeForm, errors);
			}

			if (StringUtils.isBlank(pinChangeForm.getStartDate())) {
				errors.rejectValue("startDate", "pin.change.start.date.empty");
			}
			else {
				pinChangeCheckForValidStartDate(pinChangeForm, errors);
			}

			if (StringUtils.isBlank(pinChangeForm.getEndDate())) {
				errors.rejectValue("endDate", "pin.change.end.date.empty");
			}
			else {
				pinChangeCheckForValidEndDate(pinChangeForm, errors);
			}

			if (StringUtils.isNotBlank(pinChangeForm.getStartDate()) &&
				StringUtils.isNotBlank(pinChangeForm.getEndDate())) {

				pinChangeCheckForStartDateBeforeEndDate(pinChangeForm, errors);
			}
		}

		logger.debug("validate - end");
	}

	private boolean pinChangeCheckForDateDifference(
		PinChangeSearchForm pinChangeForm, Errors errors) {

		boolean validDates = false;

		logger.debug(
			"pinChangeCheckForDateDifference - start date: " +
				pinChangeForm.getStartDate());
		logger.debug(
			"pinChangeCheckForDateDifference - end date: " +
				pinChangeForm.getEndDate());

		SimpleDateFormat dateFormat = new SimpleDateFormat(
			Constants.DATE_FORMAT);

		try {
			Date startDate = dateFormat.parse(pinChangeForm.getStartDate());
			Date endDate = dateFormat.parse(pinChangeForm.getEndDate());

			long differenceInMilliseconds =
				endDate.getTime() - startDate.getTime();
			long differenceInDays =
				differenceInMilliseconds / (1000L * 60L * 60L * 24L);

			//Only true if the difference is less than or equal to 7.

			if (differenceInDays <= 7) {
				validDates = true;
			}
		}
		catch (ParseException e) {
			validDates = false;
		}

		return validDates;
	}

	/**
	 * Validation method for fromDate NoGreater Than ToDate
	 * @param pinChangeForm
	 * @param errors
	 * @return void
	 */
	private void pinChangeCheckForStartDateBeforeEndDate(
		PinChangeSearchForm pinChangeForm, Errors errors) {

		if (!StringUtils.isBlank(pinChangeForm.getEndDate()) &&
			!StringUtils.isBlank(pinChangeForm.getStartDate())) {

			String newEndDate = "";
			Date toDate = null;

			if ("24".equals(pinChangeForm.getEndTimeHr())) {
				if (!StringUtils.isBlank(pinChangeForm.getEndDate())) {
					newEndDate = Utility.getNextDayForThisDate(
						pinChangeForm.getEndDate());
				}
			}

			if (!StringUtils.isBlank(newEndDate)) {
				toDate = Utility.formatDate(newEndDate, Constants.DATE_FORMAT);
			}
			else {
				toDate = Utility.formatDate(
					pinChangeForm.getEndDate(), Constants.DATE_FORMAT);
			}

			Date fromDate = Utility.formatDate(
				pinChangeForm.getStartDate(), Constants.DATE_FORMAT);

			if (fromDate.compareTo(toDate) == 0) {
				if (!checkEndTimeStartTimeWhenDatesAreEqual(pinChangeForm)) {
					errors.rejectValue(
						"startDate", "pin.change.start.date.after.end.date");
				}
			}
			else {
				if (fromDate.after(toDate)) {
					errors.rejectValue(
						"startDate", "pin.change.start.date.after.end.date");
				}
			}
		}
	}

	/**
	 * Validation method for End date
	 * @param pinChangeForm
	 * @param errors
	 * @return Errors
	 */
	private void pinChangeCheckForValidEndDate(
		PinChangeSearchForm pinChangeForm, Errors errors) {

		if (StringUtils.isNotBlank(pinChangeForm.getEndDate())) {
			if (!Utility.validateDate(
					pinChangeForm.getEndDate(), Constants.DATE_FORMAT)) {

				errors.rejectValue("endDate", "pin.change.date.format.invalid");
			}
		}
	}

	/**
	 * Check for a valid PAN.
	 *
	 * @param pinChangeForm
	 * @param errors
	 */
	private void pinChangeCheckForValidPan(
		PinChangeSearchForm pinChangeForm, Errors errors) {

		String pan = pinChangeForm.getPanBin(
		).trim();

		if ((pan.length() < 13) || (pan.length() > 19) ||
			!pinChangeCheckPanForInvalidCharacters(pinChangeForm, errors)) {

			errors.rejectValue("panBin", "pin.change.pan.invalid");
		}
	}

	/**
	 * Validation method for Start date
	 * @param pinChangeForm
	 * @param errors
	 * @return Errors
	 */
	private void pinChangeCheckForValidStartDate(
		PinChangeSearchForm pinChangeForm, Errors errors) {

		if (StringUtils.isNotBlank(pinChangeForm.getStartDate())) {
			if (!Utility.validateDate(
					pinChangeForm.getStartDate(), Constants.DATE_FORMAT)) {

				errors.rejectValue(
					"startDate", "pin.change.date.format.invalid");
			}
		}
	}

	private void pinChangeCheckForValidTerminalId(
		PinChangeSearchForm pinChangeForm, Errors errors) {

		int terminalIdLength = pinChangeForm.getTerminalId(
		).trim(
		).length();

		if (terminalIdLength > 15) {
			errors.rejectValue("terminalId", "pin.change.terminal.id.invalid");
		}

		if (!pinChangeCheckForDateDifference(pinChangeForm, errors)) {
			errors.rejectValue(
				"startDate", "pin.change.date.older.than.1.week");
		}
	}

	/**
	 * Validation method for check PanBin Value For SpecialChar
	 * @param pinChangeForm
	 * @param errors
	 * @return boolean
	 */
	private boolean pinChangeCheckPanForInvalidCharacters(
		PinChangeSearchForm pinChangeForm, Errors errors) {

		boolean isValid = false;

		if (StringUtils.isNotBlank(pinChangeForm.getPanBin())) {
			String panOrBin = Utility.removeAllSpacesFromPanorBin(
				pinChangeForm.getPanBin(
				).trim());

			if (!Utility.validatePatternMatcher(
					Constants.REG_EXP_SPECIAL_CHAR, panOrBin)) {

				isValid = false;
			}
			else {
				isValid = true;
			}
		}

		return isValid;
	}

	private static Logger logger = Logger.getLogger("pinChangeLogging");

}