//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.validator;

import au.com.cuscal.de.ticketing.common.CommonUtil;
import au.com.cuscal.de.ticketing.common.Constants;
import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("deMistakenPaymentValidator")
public class DEMistakenPaymentValidator
	extends AbstractServiceRequestValidator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		DEMistakenPaymentValidator.logger.debug((Object)"validate - start");
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
				this.validateDestinationBSB(serviceRequestForm, errors);
				this.validateDestinationAccountNumber(
					serviceRequestForm, errors);
				this.validateAccountTitle(serviceRequestForm, errors);
				this.validateFinInstTraceBSBNumber(serviceRequestForm, errors);
				this.validateAccountNumberRemitter(serviceRequestForm, errors);
				this.validateRemitterName(serviceRequestForm, errors);
			}

			this.validateTimeframe(serviceRequestForm, errors);
			this.validateTransactionAmounts(serviceRequestForm, errors);
			this.validateDestinationLodgementRefs(
				serviceRequestForm, errors, false);
			this.validateProcessedDates(serviceRequestForm, errors);
			this.validateIntendedAccountOrWrongPayee(
				serviceRequestForm, errors);
			this.validateReceiverAccountName(serviceRequestForm, errors);
			this.validateReceiverBSBNumber(serviceRequestForm, errors);
			this.validateReceiverAccountNumber(serviceRequestForm, errors);
			this.validateReceiverLodgementRef(serviceRequestForm, errors);
			this.validateAdditionalInformation(serviceRequestForm, errors);
		}

		this.validateDisclaimer(serviceRequestForm, errors);
		DEMistakenPaymentValidator.logger.debug((Object)"validate - end");
	}

	public void validateCustomerDeclarationAttached(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getCustomerDeclarationAttached())) {

			errors.rejectValue(
				"attributesInformation.customerDeclarationAttached",
				"service.request.customerDeclarationAttached.empty");
		}
	}

	public void validateIntendedAccountBSBNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getIntendedAccountBSBNumber())) {

			errors.rejectValue(
				"attributesInformation.intendedAccountBSBNumber",
				"service.request.intendedAccountBSBNumber.empty");
		}
		else if (!CommonUtil.isBsbValid(
					form.getAttributesInformation(
					).getIntendedAccountBSBNumber())) {

			errors.rejectValue(
				"attributesInformation.intendedAccountBSBNumber",
				"service.request.BSB.invalid");
		}
	}

	public void validateIntendedAccountNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getIntendedAccountNumber())) {

			errors.rejectValue(
				"attributesInformation.intendedAccountNumber",
				"service.request.intendedAccountNumber.empty");
		}
		else if (!StringUtils.isAlphanumeric(
					(CharSequence)form.getAttributesInformation(
					).getIntendedAccountNumber()) ||
				 (form.getAttributesInformation(
				 ).getIntendedAccountNumber(
				 ).length() > 9)) {

			errors.rejectValue(
				"attributesInformation.intendedAccountNumber",
				"service.request.destinationAccountNumber.invalid");
		}
	}

	public void validateIntendedAccountOrWrongPayee(
		final ServiceRequestForm serviceRequestForm, final Errors errors) {

		if (Constants.DE_INTENDED_ACCOUNT_DETAILS.equals(
				serviceRequestForm.getAttributesInformation(
				).getIntendedAccountOrWrongPayee())) {

			this.validateIntendedAccountBSBNumber(serviceRequestForm, errors);
			this.validateIntendedAccountNumber(serviceRequestForm, errors);
			this.validateIntendedAccountTitle(serviceRequestForm, errors);
		}
		else if (Constants.DE_CUSTOMER_SELECTED_WRONG_PAYEE.equals(
					serviceRequestForm.getAttributesInformation(
					).getIntendedAccountOrWrongPayee())) {

			this.validateCustomerDeclarationAttached(
				serviceRequestForm, errors);
		}
		else {
			errors.rejectValue(
				"attributesInformation.intendedAccountOrWrongPayee",
				"service.request.intendedAccountOrWrongPayee.empty");
		}
	}

	public void validateIntendedAccountTitle(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getIntendedAccountTitle())) {

			errors.rejectValue(
				"attributesInformation.intendedAccountTitle",
				"service.request.intendedAccountTitle.empty");
		}
	}

	public void validateTimeframe(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getTimeframe())) {

			errors.rejectValue(
				"attributesInformation.timeframe",
				"service.request.timeframe.empty");
		}
	}

	private static Logger logger;

	static {
		DEMistakenPaymentValidator.logger = Logger.getLogger(
			(Class)DEMistakenPaymentValidator.class);
	}

}