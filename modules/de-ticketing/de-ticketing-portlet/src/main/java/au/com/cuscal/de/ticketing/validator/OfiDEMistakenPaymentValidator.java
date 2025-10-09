//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.validator;

import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("ofiDEMistakenPaymentValidator")
public class OfiDEMistakenPaymentValidator
	extends AbstractServiceRequestValidator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		OfiDEMistakenPaymentValidator.logger.debug((Object)"validate - start");
		final ServiceRequestForm serviceRequestForm =
			(ServiceRequestForm)target;

		if (serviceRequestForm.getContactInformation() != null) {
		}

		if (serviceRequestForm.getRequestTypeInformation() != null) {
		}

		if (serviceRequestForm.getAttributesInformation() != null) {
			this.validateOfiFundAvailability(serviceRequestForm, errors);
			this.validateOfiAdditionalInfo(serviceRequestForm, errors);
		}

		this.validateDisclaimer(serviceRequestForm, errors);
		OfiDEMistakenPaymentValidator.logger.debug((Object)"validate - end");
	}

	public void validateOfiFundAvailability(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getOfiFundAvailability())) {

			errors.rejectValue(
				"attributesInformation.ofiFundAvailability",
				"service.request.ofi.result.empty");
		}
	}

	private static Logger logger;

	static {
		OfiDEMistakenPaymentValidator.logger = Logger.getLogger(
			(Class)OfiDEMistakenPaymentValidator.class);
	}

}