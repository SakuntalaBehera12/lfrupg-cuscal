package au.com.cuscal.connect.validator;

import au.com.cuscal.connect.commons.Utility;
import au.com.cuscal.connect.forms.ReportForm;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Switch Report form validator
 *
 * @author Rajni
 *
 */
@Component("reportFormValidator")
public class ReportValidator implements Validator {
	/* (non-Javadoc)
	 * @see Validator#supports(Class)
	 */
	/**
	 * Validate the search form for from and to dates
	 *
	 * @param ReportForm
	 * @param Errors
	 * @return void
	 */
	public void fromDateNoGreaterThanToDate(
		ReportForm reportForm, Errors errors) {

		if (!StringUtils.isBlank(reportForm.getToDate()) &&
			!StringUtils.isBlank(reportForm.getFromDate())) {

			Date toDate = Utility.formatDate(
				reportForm.getToDate(), Utility.DATE_FORMAT);
			Date fromDate = Utility.formatDate(
				reportForm.getFromDate(), Utility.DATE_FORMAT);

			if (fromDate.after(toDate)) {
				errors.rejectValue(
					"fromDate", "switch.reports.fromDate.toDate");
			}
		}
	}

	public boolean supports(Class<?> arg0) {

		// TODO Auto-generated method stub

		return false;
	}

	/**
	 * Validate the search form
	 *
	 * @param Object
	 * @param Errors
	 * @return void
	 */
	public void validate(Object target, Errors errors) {
		ReportForm reportForm = (ReportForm)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(
			errors, "fromDate", "switch.reports.fromDate.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(
			errors, "toDate", "switch.reports.toDate.empty");
		fromDateNoGreaterThanToDate(reportForm, errors);
	}

	/*public void fromDateNoOlderThan18MonthDate(ReportForm reportForm,Errors errors ){
		if(!StringUtils.isBlank(reportForm.getFromDate())){
			Date month13AgoDate=Utility.get18MonthAgoDateFromTodayDate();
			Date toDate=Utility.formatDate(reportForm.getToDate());
			Date fromDate=Utility.formatDate(reportForm.getFromDate());
			if(fromDate.before(month13AgoDate)){
				errors.rejectValue("fromDate", "switch.reports.fromDate.18month");
			}
	     }
	}

    public void toDateNoGreaterThanTodayDate(ReportForm reportForm,Errors errors ){
    	if(!StringUtils.isBlank(reportForm.getToDate()) &&  !StringUtils.isBlank(reportForm.getFromDate())){
	    	Date toDate=Utility.formatDate(reportForm.getToDate());
	    	Date todayDate=Calendar.getInstance().getTime();
	    	if(toDate.after(todayDate)){
	    		errors.rejectValue("toDate", "switch.reports.toDate.currentDate");
	    	}
    	}

	}*/

}