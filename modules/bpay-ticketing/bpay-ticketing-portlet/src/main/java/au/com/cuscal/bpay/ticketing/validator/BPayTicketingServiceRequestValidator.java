package au.com.cuscal.bpay.ticketing.validator;

import au.com.cuscal.bpay.ticketing.common.Constants;
import au.com.cuscal.bpay.ticketing.forms.ServiceRequestForm;
import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * This class will house all the spring form validation methods for BPay.
 *
 *
 */
@Component("bPayTicketingServiceRequestValidator")
public class BPayTicketingServiceRequestValidator
	extends AbstractServiceRequestValidator {

	public void serverSideValidation(Errors errors) {
		logger.debug("server side validation - start");

		// Duplicate trn server side validation check

		errors.rejectValue(
			"attributesInformation.transactionRefNumber",
			"service.request.transactionRefNumber.duplicate");// TODO

		logger.debug("server side validation  - end");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		logger.debug("validate - start");

		ServiceRequestForm serviceRequestForm = (ServiceRequestForm)target;

		if (serviceRequestForm.getContactInformation() != null) {
		}

		if (serviceRequestForm.getRequestTypeInformation() != null) {
		}

		if (serviceRequestForm.getAttributesInformation() != null) {
			validateInvestigationType(serviceRequestForm, errors);
			validateReason(serviceRequestForm, errors);
			validateTransactionRefNumber(serviceRequestForm, errors);
			validateTransactionDate(serviceRequestForm, errors);
			validateBillerCode(serviceRequestForm, errors);
			validateCustomerRefNumber(serviceRequestForm, errors);
			validatePaymentAmount(serviceRequestForm, errors);
			validatePaymentMethod(serviceRequestForm, errors);
			validateEntryMethod(serviceRequestForm, errors);
			validatePayerName(serviceRequestForm, errors);
			validateMemberNumber(serviceRequestForm, errors);
			validateAdditionalInformation(serviceRequestForm, errors);
			validatePayerReportedDate(serviceRequestForm, errors);
		}

		validateDisclaimer(serviceRequestForm, errors);

		logger.debug("validate - end");
	}

	public void validateAdditionalInformation(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isNotBlank(
				form.getAttributesInformation(
				).getComment())) {

			if (containsPAN(
					form.getAttributesInformation(
					).getComment())) {

				errors.rejectValue(
					"attributesInformation.comment",
					"service.request.comment.clear.pan");
			}
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateBillerCode(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getBillerCode())) {

			errors.rejectValue(
				"attributesInformation.billerCode",
				"service.request.billerCode.empty");
		}
		else {
			if (!StringUtils.isNumeric(
					form.getAttributesInformation(
					).getBillerCode()) ||
				(form.getAttributesInformation(
				).getBillerCode(
				).length() > 10)) {

				errors.rejectValue(
					"attributesInformation.billerCode",
					"service.request.billerCode.invalid");
			}
		}
	}

	// Server side validation

	@Deprecated
	public void validateContactInfo(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getContactInformation(
				).getEmail())) {

			errors.rejectValue(
				"contactInformation.email", "service.request.email.empty");
		}
		else {
			pattern = Pattern.compile(Constants.REG_EXP_EMAIL_PATTERN);
			matcher = pattern.matcher(
				form.getContactInformation(
				).getEmail());

			if (!matcher.matches()) {
				errors.rejectValue(
					"contactInformation.email",
					"service.request.email.invalid");
			}
		}

		if (StringUtils.isBlank(
				form.getContactInformation(
				).getPhoneNumber())) {

			errors.rejectValue(
				"contactInformation.phoneNumber",
				"service.request.phone.empty");
		}
		else {
			pattern = Pattern.compile(Constants.REG_EXP_PHONE_NUMBER);
			matcher = pattern.matcher(
				form.getContactInformation(
				).getPhoneNumber());

			if (!matcher.matches()) {
				errors.rejectValue(
					"contactInformation.phoneNumber",
					"service.request.phone.invalid");
			}
		}

		ValidationUtils.rejectIfEmpty(
			errors, "contactInformation.givenName",
			"service.request.given.empty");
		ValidationUtils.rejectIfEmpty(
			errors, "contactInformation.surname",
			"service.request.surname.empty");
	}

	public void validateCorrectBillerCode(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getCorrectBillerCode())) {

			errors.rejectValue(
				"attributesInformation.correctBillerCode",
				"service.request.correct.billerCode.empty");
		}
		else {
			if (!StringUtils.isNumeric(
					form.getAttributesInformation(
					).getCorrectBillerCode()) ||
				(form.getAttributesInformation(
				).getCorrectBillerCode(
				).length() > 10)) {

				errors.rejectValue(
					"attributesInformation.correctBillerCode",
					"service.request.billerCode.invalid");
			}
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateCorrectCRN(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getCorrectCRN())) {

			errors.rejectValue(
				"attributesInformation.correctCRN",
				"service.request.correctCRN.empty");
		}
		else {
			if (!StringUtils.isNumeric(
					form.getAttributesInformation(
					).getCorrectCRN()) ||
				(form.getAttributesInformation(
				).getCorrectCRN(
				).length() > 20)) {

				errors.rejectValue(
					"attributesInformation.correctCRN",
					"service.request.customerRefNumber.invalid");
			}
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateCorrectFraudType(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getFraudType())) {

			errors.rejectValue(
				"attributesInformation.fraudType",
				"service.request.fraudType.empty");
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateCorrectScamType(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getScamType())) {

			errors.rejectValue(
				"attributesInformation.scamType",
				"service.request.scamType.empty");
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateCustomerRefNumber(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getCustomerRefNumber())) {

			errors.rejectValue(
				"attributesInformation.customerRefNumber",
				"service.request.customerRefNumber.empty");
		}
		else {
			if (!StringUtils.isNumeric(
					form.getAttributesInformation(
					).getCustomerRefNumber()) ||
				(form.getAttributesInformation(
				).getCustomerRefNumber(
				).length() > 20)) {

				errors.rejectValue(
					"attributesInformation.customerRefNumber",
					"service.request.customerRefNumber.invalid");
			}
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateEntryMethod(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getEntryMethod())) {

			errors.rejectValue(
				"attributesInformation.entryMethod",
				"service.request.entryMethod.empty");
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validatefraudTypeOtherCase(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getFraudInfo())) {

			errors.rejectValue(
				"attributesInformation.fraudInfo",
				"service.request.fraudInfo.empty");
		}
	}

	public void validateInvestigationType(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getInvestigationType())) {

			errors.rejectValue(
				"attributesInformation.investigationType",
				"service.request.type.empty");
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateMemberNumber(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getMemberNumber())) {

			errors.rejectValue(
				"attributesInformation.memberNumber",
				"service.request.memberNumber.empty");
		}
		else {
			if (!StringUtils.isNumeric(
					form.getAttributesInformation(
					).getMemberNumber()) ||
				(form.getAttributesInformation(
				).getMemberNumber(
				).length() > 10)) {

				errors.rejectValue(
					"attributesInformation.memberNumber",
					"service.request.memberNumber.invalid");
			}
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validatePayerName(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getPayerName())) {

			errors.rejectValue(
				"attributesInformation.payerName",
				"service.request.payerName.empty");
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validatePayerReportedDate(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getPayerReportedDate())) {

			errors.rejectValue(
				"attributesInformation.payerReportedDate",
				"service.request.payerReportedDate.empty");
		}
		else {
			if (!Utility.validateDate(
					form.getAttributesInformation(
					).getPayerReportedDate(),
					Constants.DATE_FORMAT)) {

				errors.rejectValue(
					"attributesInformation.payerReportedDate",
					"service.request.date.validation");
			}
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validatePaymentAmount(ServiceRequestForm form, Errors errors) {
		String amount = form.getAttributesInformation(
		).getPaymentAmount();

		if (StringUtils.isBlank(amount)) {
			errors.rejectValue(
				"attributesInformation.paymentAmount",
				"service.request.amount.empty");
		}
		else {
			pattern = Pattern.compile(Constants.REG_EXP_AMOUNT);
			matcher = pattern.matcher(amount);

			if (!matcher.matches() ||
				(removeDecimal(
					amount
				).length() > 10)) {

				errors.rejectValue(
					"attributesInformation.paymentAmount",
					"service.request.amount.invalid");
			}
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validatePaymentMethod(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getPaymentMethod())) {

			errors.rejectValue(
				"attributesInformation.paymentMethod",
				"service.request.paymentMethod.empty");
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateReason(ServiceRequestForm form, Errors errors) {
		if (Constants.BPAY_INVESTIGATION_ERROR_CORRECTION.equals(
				form.getAttributesInformation(
				).getInvestigationType())) {

			if (StringUtils.isBlank(
					form.getAttributesInformation(
					).getErrorCorrectionReason())) {

				errors.rejectValue(
					"attributesInformation.errorCorrectionReason",
					"service.request.reason.empty");
			}

			if (Constants.BPAY_EC_REQUEST_REASON_WRONG_CRN.equals(
					form.getAttributesInformation(
					).getErrorCorrectionReason())) {

				validateCorrectCRN(form, errors);
			}
			else if (Constants.BPAY_EC_REQUEST_REASON_WRONG_BILLER_CODE.equals(
						form.getAttributesInformation(
						).getErrorCorrectionReason())) {

				validateCorrectBillerCode(form, errors);
			}
			else if (Constants.BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE.
						equals(
							form.getAttributesInformation(
							).getErrorCorrectionReason())) {

				//First Validate Fraud Type
				validateCorrectFraudType(form, errors);

				if (Constants.
						BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE_AND_FRAUD_TYPE_VICTIM_OF_SCAM.
							equals(
								form.getAttributesInformation(
								).getFraudType())) {

					//Validate Scam Type
					validateCorrectScamType(form, errors);

					if (Constants.
							BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE_AND_SCAM_TYPE_OTHER.
								equals(
									form.getAttributesInformation(
									).getScamType())) {

						//Validate Other Type if scam type is other
						validateScamTypeOtherCase(form, errors);
					}
				}
				else if (Constants.
							BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE_AND_FRAUD_TYPE_OTHER.
								equals(
									form.getAttributesInformation(
									).getFraudType())) {

					//Validate Other Type
					validatefraudTypeOtherCase(form, errors);
				}
			}
		}
		else if (Constants.BPAY_INVESTIGATION_ENQUIRY.equals(
					form.getAttributesInformation(
					).getInvestigationType())) {

			if (StringUtils.isBlank(
					form.getAttributesInformation(
					).getPaymentInvestigationReason())) {

				errors.rejectValue(
					"attributesInformation.paymentInvestigationReason",
					"service.request.reason.empty");
			}
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateScamTypeOtherCase(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getScamInfo())) {

			errors.rejectValue(
				"attributesInformation.scamInfo",
				"service.request.scamInfo.empty");
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateTransactionDate(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getTransactionDate())) {

			errors.rejectValue(
				"attributesInformation.transactionDate",
				"service.request.transactionDate.empty");
		}
		else {
			if (!Utility.validateDate(
					form.getAttributesInformation(
					).getTransactionDate(),
					Constants.DATE_FORMAT)) {

				errors.rejectValue(
					"attributesInformation.transactionDate",
					"service.request.date.validation");
			}
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateTransactionRefNumber(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getTransactionRefNumber())) {

			errors.rejectValue(
				"attributesInformation.transactionRefNumber",
				"service.request.transactionRefNumber.empty");
		}
		else {
			if (!StringUtils.isNumeric(
					form.getAttributesInformation(
					).getTransactionRefNumber()) ||
				(form.getAttributesInformation(
				).getTransactionRefNumber(
				).length() > 21)) {

				errors.rejectValue(
					"attributesInformation.transactionRefNumber",
					"service.request.transactionRefNumber.invalid");
			}
		}
	}

	private static Logger logger = Logger.getLogger(
		BPayTicketingServiceRequestValidator.class);

	private Matcher matcher;
	private Pattern pattern;

}