//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.validator;

import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.de.ticketing.common.CommonUtil;
import au.com.cuscal.de.ticketing.common.Constants;
import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("ofiDETraceValidator")
public class OfiDETraceValidator extends AbstractServiceRequestValidator {

	public String getFinalDestination() {
		return this.finalDestination;
	}

	public void setFinalDestination(final String finalDestination) {
		this.finalDestination = finalDestination;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		OfiDETraceValidator.logger.debug((Object)"validate - start");
		final ServiceRequestForm serviceRequestForm =
			(ServiceRequestForm)target;

		if (serviceRequestForm.getContactInformation() != null) {
		}

		if (serviceRequestForm.getRequestTypeInformation() != null) {
		}

		if (serviceRequestForm.getAttributesInformation() != null) {
			if (StringUtils.trimToEmpty(
					serviceRequestForm.getAttributesInformation(
					).getRequestFor()
				).equalsIgnoreCase(
					this.finalDestination
				)) {

				this.validateDatePosted(serviceRequestForm, errors);
				this.validateBSBNumber(serviceRequestForm, errors);
				this.validateBranch(serviceRequestForm, errors);
				this.validateAccountNumber(serviceRequestForm, errors);
				this.validateAccountName(serviceRequestForm, errors);
			}
			else {
				this.validateRemitterName(serviceRequestForm, errors);
			}

			this.validateOfiAdditionalInfo(serviceRequestForm, errors);
		}

		this.validateDisclaimer(serviceRequestForm, errors);
		OfiDETraceValidator.logger.debug((Object)"validate - end");
	}

	public void validateAccountName(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getOfiAccountName())) {

			errors.rejectValue(
				"attributesInformation.ofiAccountName",
				"service.request.receiverAccountName.empty");
		}
	}

	public void validateAccountNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getOfiAccountNumber())) {

			errors.rejectValue(
				"attributesInformation.ofiAccountNumber",
				"service.request.receiverAccountNumber.empty");
		}
		else if (!StringUtils.isAlphanumeric(
					(CharSequence)form.getAttributesInformation(
					).getOfiAccountNumber()) ||
				 (form.getAttributesInformation(
				 ).getOfiAccountNumber(
				 ).length() > 9)) {

			errors.rejectValue(
				"attributesInformation.ofiAccountNumber",
				"service.request.destinationAccountNumber.invalid");
		}
	}

	public void validateBranch(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getOfiBranch())) {

			errors.rejectValue(
				"attributesInformation.ofiBranch",
				"service.request.receiverBranch.empty");
		}
	}

	public void validateBSBNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getOfiBsbNumber())) {

			errors.rejectValue(
				"attributesInformation.ofiBsbNumber",
				"service.request.receiverBSBNumber.empty");
		}
		else if (!CommonUtil.isBsbValid(
					form.getAttributesInformation(
					).getOfiBsbNumber())) {

			errors.rejectValue(
				"attributesInformation.ofiBsbNumber",
				"service.request.BSB.invalid");
		}
	}

	public void validateDatePosted(
		final ServiceRequestForm form, final Errors errors) {

		if (form.getAttributesInformation(
			).getOfiDatePosted() != null) {

			if (StringUtils.isBlank(
					(CharSequence)form.getAttributesInformation(
					).getOfiDatePosted())) {

				errors.rejectValue(
					"attributesInformation.ofiDatePosted",
					"service.request.datePosted.empty");
			}
			else if (!Utility.validateDate(
						form.getAttributesInformation(
						).getOfiDatePosted(),
						Constants.DATE_FORMAT)) {

				errors.rejectValue(
					"attributesInformation.ofiDatePosted",
					"service.request.date.validation");
			}
		}
	}

	@Override
	public void validateRemitterName(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getOfiRemitterName())) {

			errors.rejectValue(
				"attributesInformation.ofiRemitterName",
				"service.request.remitterName.empty");
		}
	}

	private static Logger logger;

	static {
		OfiDETraceValidator.logger = Logger.getLogger(
			(Class)OfiDETraceValidator.class);
	}

	private String finalDestination;

}