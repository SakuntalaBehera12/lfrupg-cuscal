//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.validator;

import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.de.ticketing.common.Constants;
import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("ofiDERecallValidator")
public class OfiDERecallValidator extends AbstractServiceRequestValidator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		OfiDERecallValidator.logger.debug((Object)"validate - start");
		final ServiceRequestForm serviceRequestForm =
			(ServiceRequestForm)target;

		if (serviceRequestForm.getContactInformation() != null) {
		}

		if (serviceRequestForm.getRequestTypeInformation() != null) {
		}

		if (serviceRequestForm.getAttributesInformation() != null) {
			this.validateOfiRecallResult(serviceRequestForm, errors);
			this.validateDateResolved(serviceRequestForm, errors);
			this.validateOfiAdditionalInfo(serviceRequestForm, errors);
		}

		this.validateDisclaimer(serviceRequestForm, errors);
		OfiDERecallValidator.logger.debug((Object)"validate - end");
	}

	public void validateDateResolved(
		final ServiceRequestForm form, final Errors errors) {

		if (form.getAttributesInformation(
			).getOfiDateResolved() != null) {

			if (StringUtils.isBlank(
					(CharSequence)form.getAttributesInformation(
					).getOfiDateResolved())) {

				errors.rejectValue(
					"attributesInformation.ofiDateResolved",
					"service.request.date.resolved.empty");
			}
			else if (!Utility.validateDate(
						form.getAttributesInformation(
						).getOfiDateResolved(),
						Constants.DATE_FORMAT)) {

				errors.rejectValue(
					"attributesInformation.ofiDateResolved",
					"service.request.date.validation");
			}
		}
	}

	public void validateOfiRecallResult(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getOfiRecallResult())) {

			errors.rejectValue(
				"attributesInformation.ofiRecallResult",
				"service.request.ofi.result.empty");
		}
	}

	private static Logger logger;

	static {
		OfiDERecallValidator.logger = Logger.getLogger(
			(Class)OfiDERecallValidator.class);
	}

}