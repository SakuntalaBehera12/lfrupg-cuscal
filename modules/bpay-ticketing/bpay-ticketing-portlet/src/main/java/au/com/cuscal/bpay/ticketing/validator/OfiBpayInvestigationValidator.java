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
@Component("ofiBpayInvestigationValidator")
public class OfiBpayInvestigationValidator
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
			validatePayerAddress(serviceRequestForm, errors);
			validateOfiComment(serviceRequestForm, errors);
		}

		validateDisclaimer(serviceRequestForm, errors);

		logger.debug("validate - end");
	}

	public void validatePayerAddress(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isBlank(
				form.getAttributesInformation(
				).getOfiPayerAddress())) {

			errors.rejectValue(
				"attributesInformation.ofiPayerAddress",
				"service.request.payerAddress.empty");
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

	private static Logger logger = Logger.getLogger(
		OfiBpayInvestigationValidator.class);

}