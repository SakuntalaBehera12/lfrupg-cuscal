//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.validator;

import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("deRecallValidator")
public class DERecallValidator extends AbstractServiceRequestValidator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		DERecallValidator.logger.debug((Object)"validate - start");
		final ServiceRequestForm serviceRequestForm =
			(ServiceRequestForm)target;

		if (serviceRequestForm.getContactInformation() != null) {
		}

		if (serviceRequestForm.getRequestTypeInformation() != null) {
		}

		if (serviceRequestForm.getAttributesInformation() != null) {
			if (!serviceRequestForm.isPrePopulated()) {
				this.validateFinInstName(serviceRequestForm, errors);
				this.validateFinInstUserId(serviceRequestForm, errors);
				this.validateProcessedDate(serviceRequestForm, errors);
				this.validateDestinationBSB(serviceRequestForm, errors);
				this.validateDestinationAccountNumber(
					serviceRequestForm, errors);
				this.validateTransactionCode(serviceRequestForm, errors);
				this.validateTransactionAmount(serviceRequestForm, errors);
				this.validateAccountTitle(serviceRequestForm, errors);
				this.validateDestinationLodgementRef(
					serviceRequestForm, errors, false);
			}

			this.validateReceiverBSBNumber(serviceRequestForm, errors);
			this.validateReceiverAccountNumber(serviceRequestForm, errors);
			this.validateReceiverAccountName(serviceRequestForm, errors);
			this.validateAdditionalInformation(serviceRequestForm, errors);
		}

		this.validateDisclaimer(serviceRequestForm, errors);
		DERecallValidator.logger.debug((Object)"validate - end");
	}

	private static Logger logger;

	static {
		DERecallValidator.logger = Logger.getLogger(
			(Class)DERecallValidator.class);
	}

}