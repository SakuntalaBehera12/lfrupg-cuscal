//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.validator;

import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("deClaimValidator")
public class DEClaimValidator extends AbstractServiceRequestValidator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		DEClaimValidator.logger.debug((Object)"validate - start");
		final ServiceRequestForm serviceRequestForm =
			(ServiceRequestForm)target;

		if (serviceRequestForm.getContactInformation() != null) {
		}

		if (serviceRequestForm.getRequestTypeInformation() != null) {
		}

		if (serviceRequestForm.getAttributesInformation() != null) {
			if (!serviceRequestForm.isPrePopulated()) {
				this.validateBatchNumber(serviceRequestForm, errors);
				this.validateFinInstName(serviceRequestForm, errors);
				this.validateFinInstUserId(serviceRequestForm, errors);
				this.validateAccountTitle(serviceRequestForm, errors);
				this.validateDestinationBSB(serviceRequestForm, errors);
				this.validateDestinationAccountNumber(
					serviceRequestForm, errors);
			}

			this.validateCurrentDDRStatus(serviceRequestForm, errors);
			this.validateClaimReason(serviceRequestForm, errors);
			this.validateProcessedDates(serviceRequestForm, errors);
			this.validateDestinationLodgementRefs(
				serviceRequestForm, errors, true);
			this.validateTransactionAmounts(serviceRequestForm, errors);
			this.validateReceiverBSBNumber(serviceRequestForm, errors);
			this.validateReceiverAccountNumber(serviceRequestForm, errors);
			this.validateReceiverAccountName(serviceRequestForm, errors);
			this.validateAdditionalInformation(serviceRequestForm, errors);
		}

		this.validateDisclaimer(serviceRequestForm, errors);
		DEClaimValidator.logger.debug((Object)"validate - end");
	}

	public void validateClaimReason(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getClaimReason())) {

			errors.rejectValue(
				"attributesInformation.claimReason",
				"service.request.claimReason.empty");
		}
		else if ("2".equals(
					form.getAttributesInformation(
					).getClaimReason())) {

			this.validateDrawingInTheNameOf(form, errors);
		}
	}

	public void validateCurrentDDRStatus(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getCurrentDDRStatus())) {

			errors.rejectValue(
				"attributesInformation.currentDDRStatus",
				"service.request.currentDDRStatus.empty");
		}
	}

	public void validateDrawingInTheNameOf(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getDrawingInTheNameOf())) {

			errors.rejectValue(
				"attributesInformation.drawingInTheNameOf",
				"service.request.deClaim.drawingInTheNameOf.empty");
		}
	}

	private static Logger logger;

	static {
		DEClaimValidator.logger = Logger.getLogger(
			(Class)DEClaimValidator.class);
	}

}