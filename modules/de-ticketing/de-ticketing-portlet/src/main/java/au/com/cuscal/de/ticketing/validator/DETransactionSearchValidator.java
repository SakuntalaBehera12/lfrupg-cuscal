//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.validator;

import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.de.ticketing.common.CommonUtil;
import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("deTransactionSearchValidator")
public class DETransactionSearchValidator
	extends AbstractServiceRequestValidator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		DETransactionSearchValidator.logger.debug((Object)"validate - start");
		final ServiceRequestForm serviceRequestForm =
			(ServiceRequestForm)target;

		if (serviceRequestForm.getContactInformation() != null) {
		}

		if (serviceRequestForm.getRequestTypeInformation() != null) {
		}

		if (serviceRequestForm.getAttributesInformation() != null) {
			if ("4".equals(serviceRequestForm.getFormId())) {
				this.validateDestinationBSB(serviceRequestForm, errors);
				this.validateDestinationAccountNumber(
					serviceRequestForm, errors, true);
				this.validateDestinationLodgementRef(
					serviceRequestForm, errors);
				this.validateTransactionAmount(serviceRequestForm, errors);
				this.validateBatchNumber(serviceRequestForm, errors);
				this.validateProcessedDate(serviceRequestForm, errors);
			}
			else {
				this.validateDestinationBSB(serviceRequestForm, errors);
				this.validateTransactionAmount(serviceRequestForm, errors);
				this.validateProcessedDate(serviceRequestForm, errors);

				if ("1".equals(serviceRequestForm.getFormId())) {
					this.validateFinInstUserId(
						serviceRequestForm, errors, false);
				}
				else {
					this.validateFinInstUserId(
						serviceRequestForm, errors, true);
				}

				this.validateBatchNumber(serviceRequestForm, errors);
				this.validateDestinationAccountNumber(
					serviceRequestForm, errors, false);
				this.validateDestinationLodgementRef(
					serviceRequestForm, errors);
			}
		}

		DETransactionSearchValidator.logger.debug((Object)"validate - end");
	}

	@Override
	public void validateAccountNumberRemitter(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getAccountNumberRemitter())) {

			errors.rejectValue(
				"attributesInformation.accountNumberRemitter",
				"service.request.accountNumberRemitter.empty");
		}
	}

	@Override
	public void validateAccountTitle(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getAccountTitle())) {

			errors.rejectValue(
				"attributesInformation.accountTitle",
				"service.request.accountTitle.empty");
		}
	}

	@Override
	public void validateBatchNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isNotBlank(
				(CharSequence)form.getAttributesInformation(
				).getBatchNumber()) &&
			(!StringUtils.isNumeric(
				(CharSequence)form.getAttributesInformation(
				).getBatchNumber()) ||
			 (form.getAttributesInformation(
			 ).getBatchNumber(
			 ).
				 length() < 9))) {

			errors.rejectValue(
				"attributesInformation.batchNumber",
				"service.request.batchNumber.invalid");
		}
	}

	public void validateClaimTransactionAmount(
		final ServiceRequestForm form, final Errors errors) {

		if (form.getAttributesInformation(
			).getTransactionAmounts() != null) {

			final String amount = form.getAttributesInformation(
			).getTransactionAmounts()[0];

			if (StringUtils.isNotBlank((CharSequence)amount)) {
				this.pattern = Pattern.compile(
					"^(?:\\$\\s*)?(?:(?:\\d{0,3}(?:[, ]\\d{0,3})*[, ])+\\d{3}|\\d+)(?:\\.\\d{2})?(?:\\s*\\$)?$");
				this.matcher = this.pattern.matcher(amount);

				if (!this.matcher.matches()) {
					errors.rejectValue(
						"attributesInformation.transactionAmounts[0]",
						"service.request.amount.invalid");
				}
				else if (this.removeDecimal(
							amount
						).length() > 10) {

					errors.rejectValue(
						"attributesInformation.transactionAmounts[0]",
						"service.request.amount.invalid");
				}
			}
		}
	}

	public void validateDestinationAccountNumber(
		final ServiceRequestForm form, final Errors errors,
		final boolean isMandatory) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getDestinationAccountNumber())) {

			if (isMandatory) {
				errors.rejectValue(
					"attributesInformation.destinationAccountNumber",
					"service.request.destinationAccount.empty");
			}
		}
		else if (!StringUtils.isAlphanumeric(
					(CharSequence)form.getAttributesInformation(
					).getDestinationAccountNumber()) ||
				 (form.getAttributesInformation(
				 ).getDestinationAccountNumber(
				 ).length() > 9)) {

			errors.rejectValue(
				"attributesInformation.destinationAccountNumber",
				"service.request.destinationAccount.invalid");
		}
	}

	@Override
	public void validateDestinationBSB(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getDestinationBSB())) {

			errors.rejectValue(
				"attributesInformation.destinationBSB",
				"service.request.destinationBSB.empty");
		}
		else if (!CommonUtil.isBsbValid(
					form.getAttributesInformation(
					).getDestinationBSB())) {

			errors.rejectValue(
				"attributesInformation.destinationBSB",
				"service.request.BSB.invalid");
		}
	}

	public void validateDestinationLodgementRef(
		final ServiceRequestForm form, final Errors errors) {

		if ((form.getAttributesInformation(
			).getDestinationLodgementRefs() != null) &&
			(form.getAttributesInformation(
			).getDestinationLodgementRefs(
			).length == 1)) {

			final String ref = form.getAttributesInformation(
			).getDestinationLodgementRefs()[0];

			if (StringUtils.isNotBlank((CharSequence)ref) &&
				(ref.length() > 18)) {

				errors.rejectValue(
					"attributesInformation.destinationLodgementRefs[0]",
					"service.request.destinationLodgementRef.invalid");
			}
		}
	}

	@Override
	public void validateDisclaimer(
		final ServiceRequestForm form, final Errors errors) {

		if (!form.isDisclaimer()) {
			errors.rejectValue(
				"disclaimer", "service.request.disclaimer.validation");
		}
	}

	@Override
	public void validateFinInstName(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getFinInstName())) {

			errors.rejectValue(
				"attributesInformation.finInstName",
				"service.request.finInstName.empty");
		}
	}

	@Override
	public void validateFinInstTraceBSBNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getFinInstTraceBSBNumber())) {

			errors.rejectValue(
				"attributesInformation.finInstTraceBSBNumber",
				"service.request.finInstTraceBSBNumber.empty");
		}
	}

	public void validateFinInstUserId(
		final ServiceRequestForm form, final Errors errors,
		final boolean isMandatory) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getFinInstUserId())) {

			if (isMandatory) {
				errors.rejectValue(
					"attributesInformation.finInstUserId",
					"service.request.supplierId.empty");
			}
		}
		else if (!StringUtils.isNumeric(
					(CharSequence)form.getAttributesInformation(
					).getFinInstUserId()) ||
				 (form.getAttributesInformation(
				 ).getFinInstUserId(
				 ).length() != 6)) {

			errors.rejectValue(
				"attributesInformation.finInstUserId",
				"service.request.supplierId.invalid");
		}
	}

	@Override
	public void validateProcessedDate(
		final ServiceRequestForm form, final Errors errors) {

		if (form.getAttributesInformation(
			).getProcessedDates() != null) {

			if (StringUtils.isBlank(
					(CharSequence)form.getAttributesInformation(
					).getProcessedDates()[0])) {

				errors.rejectValue(
					"attributesInformation.processedDates[0]",
					"service.request.date.empty");
			}
			else if (!Utility.validateDate(
						form.getAttributesInformation(
						).getProcessedDates()[0],
						"dd/MM/yyyy")) {

				errors.rejectValue(
					"attributesInformation.processedDates[0]",
					"service.request.date.validation");
			}
		}
	}

	@Override
	public void validateRequestFor(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getRequestFor())) {

			errors.rejectValue(
				"attributesInformation.requestFor",
				"service.request.requestFor.empty");
		}
	}

	@Override
	public void validateTransactionAmount(
		final ServiceRequestForm form, final Errors errors) {

		if (form.getAttributesInformation(
			).getTransactionAmounts() != null) {

			final String amount = form.getAttributesInformation(
			).getTransactionAmounts()[0];

			if (StringUtils.isBlank((CharSequence)amount)) {
				errors.rejectValue(
					"attributesInformation.transactionAmounts[0]",
					"service.request.transactionAmount.empty");
			}
			else {
				this.pattern = Pattern.compile(
					"^(?:\\$\\s*)?(?:(?:\\d{0,3}(?:[, ]\\d{0,3})*[, ])+\\d{3}|\\d+)(?:\\.\\d{2})?(?:\\s*\\$)?$");
				this.matcher = this.pattern.matcher(amount);

				if (!this.matcher.matches() ||
					(this.removeDecimal(
						amount
					).length() > 10)) {

					errors.rejectValue(
						"attributesInformation.transactionAmounts[0]",
						"service.request.amount.invalid");
				}
			}
		}
	}

	@Override
	public void validateTransactionCode(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getTransactionCode())) {

			errors.rejectValue(
				"attributesInformation.transactionCode",
				"service.request.transactionCode.empty");
		}
	}

	private static Logger logger;

	static {
		DETransactionSearchValidator.logger = Logger.getLogger(
			(Class)DETransactionSearchValidator.class);
	}

}