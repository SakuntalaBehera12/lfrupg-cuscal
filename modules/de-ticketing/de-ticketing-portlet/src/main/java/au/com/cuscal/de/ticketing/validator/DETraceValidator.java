//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.validator;

import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("deTraceValidator")
public class DETraceValidator extends AbstractServiceRequestValidator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		DETraceValidator.logger.debug((Object)"validate - start");
		final ServiceRequestForm serviceRequestForm =
			(ServiceRequestForm)target;

		if (serviceRequestForm.getContactInformation() != null) {
		}

		if (serviceRequestForm.getRequestTypeInformation() != null) {
		}

		if (serviceRequestForm.getAttributesInformation() != null) {
			if (!serviceRequestForm.isPrePopulated()) {
				if ("1".equals(
						serviceRequestForm.getAttributesInformation(
						).getRequestFor())) {

					this.validateFinInstName(serviceRequestForm, errors);
					this.validateFinInstUserId(serviceRequestForm, errors);
				}

				this.validateProcessedDate(serviceRequestForm, errors);
				this.validateDestinationBSB(serviceRequestForm, errors);
				this.validateDestinationAccountNumber(
					serviceRequestForm, errors);
				this.validateTransactionCode(serviceRequestForm, errors);
				this.validateTransactionAmount(serviceRequestForm, errors);
				this.validateAccountTitle(serviceRequestForm, errors);
				this.validateFinInstTraceBSBNumber(serviceRequestForm, errors);
				this.validateAccountNumberRemitter(serviceRequestForm, errors);
				this.validateDestinationLodgementRef(
					serviceRequestForm, errors, false);
			}

			this.validateRequestFor(serviceRequestForm, errors);
			this.validateAdditionalInformation(serviceRequestForm, errors);
		}

		this.validateDisclaimer(serviceRequestForm, errors);
		DETraceValidator.logger.debug((Object)"validate - end");
	}

	private static Logger logger;

	static {
		DETraceValidator.logger = Logger.getLogger(
			(Class)DETraceValidator.class);
	}

}