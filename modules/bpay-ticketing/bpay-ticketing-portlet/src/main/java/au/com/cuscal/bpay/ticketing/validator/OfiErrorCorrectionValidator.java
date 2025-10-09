package au.com.cuscal.bpay.ticketing.validator;

import au.com.cuscal.bpay.ticketing.forms.ServiceRequestForm;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * This class will house all the spring form validation methods for BPay.
 *
 *
 */
@Component("ofiErrorCorrectionValidator")
public class OfiErrorCorrectionValidator
	extends AbstractServiceRequestValidator {

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
			validatePayerName(serviceRequestForm, errors);
			validateMemberNumber(serviceRequestForm, errors);
			validatePaymentMethod(serviceRequestForm, errors);
			validateEntryMethod(serviceRequestForm, errors);
			validateOfiComment(serviceRequestForm, errors);
		}

		validateDisclaimer(serviceRequestForm, errors);

		logger.debug("validate - end");
	}

	public void validateEntryMethod(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getOfiEntryMethod())) {

			errors.rejectValue(
				"attributesInformation.ofiEntryMethod",
				"service.request.entryMethod.empty");
		}
	}

	public void validateMemberNumber(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getOfiMemberNumber())) {

			errors.rejectValue(
				"attributesInformation.ofiMemberNumber",
				"service.request.memberNumber.empty");
		}
		else {
			if (!StringUtils.isNumeric(
					form.getAttributesInformation(
					).getOfiMemberNumber()) ||
				(form.getAttributesInformation(
				).getOfiMemberNumber(
				).length() > 10)) {

				errors.rejectValue(
					"attributesInformation.ofiMemberNumber",
					"service.request.memberNumber.invalid");
			}
		}
	}

	public void validatePayerName(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getOfiPayerName())) {

			errors.rejectValue(
				"attributesInformation.ofiPayerName",
				"service.request.payerName.empty");
		}
	}

	public void validatePaymentMethod(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getOfiPaymentMethod())) {

			errors.rejectValue(
				"attributesInformation.ofiPaymentMethod",
				"service.request.paymentMethod.empty");
		}
	}

	private static Logger logger = Logger.getLogger(
		OfiErrorCorrectionValidator.class);

}