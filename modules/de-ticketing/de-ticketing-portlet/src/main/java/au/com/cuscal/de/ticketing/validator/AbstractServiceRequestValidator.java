//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.validator;

import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.de.ticketing.common.CommonUtil;
import au.com.cuscal.de.ticketing.common.Constants;
import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AbstractServiceRequestValidator implements Validator {

	public boolean supports(final Class<?> clazz) {
		return false;
	}

	public void validate(final Object target, final Errors errors) {
	}

	public void validateAccountNumberRemitter(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getAccountNumberRemitter())) {

			errors.rejectValue(
				"attributesInformation.accountNumberRemitter",
				"service.request.accountNumberRemitter.empty");
		}
		else if (!StringUtils.isAlphanumeric(
					(CharSequence)form.getAttributesInformation(
					).getAccountNumberRemitter()) ||
				 (form.getAttributesInformation(
				 ).getAccountNumberRemitter(
				 ).length() > 9)) {

			errors.rejectValue(
				"attributesInformation.accountNumberRemitter",
				"service.request.accountNumber.invalid");
		}
	}

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

	public void validateAdditionalInformation(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isNotBlank(
				(CharSequence)form.getAttributesInformation(
				).getDestinationAdditionalInfo()) &&
			CommonUtil.containsPAN(
				form.getAttributesInformation(
				).getDestinationAdditionalInfo())) {

			errors.rejectValue(
				"attributesInformation.comment",
				"service.request.comment.clear.pan");
		}
	}

	public void validateBatchNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getBatchNumber())) {

			errors.rejectValue(
				"attributesInformation.batchNumber",
				"service.request.batchNumber.empty");
		}
		else if (!StringUtils.isNumeric(
					(CharSequence)form.getAttributesInformation(
					).getBatchNumber()) ||
				 (form.getAttributesInformation(
				 ).getBatchNumber(
				 ).
					 length() < 9)) {

			errors.rejectValue(
				"attributesInformation.batchNumber",
				"service.request.batchNumber.invalid");
		}
	}

	public void validateDestinationAccountNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getDestinationAccountNumber())) {

			errors.rejectValue(
				"attributesInformation.destinationAccountNumber",
				"service.request.destinationAccountNumber.empty");
		}
		else if (!StringUtils.isAlphanumeric(
					(CharSequence)form.getAttributesInformation(
					).getDestinationAccountNumber()) ||
				 (form.getAttributesInformation(
				 ).getDestinationAccountNumber(
				 ).length() > 9)) {

			errors.rejectValue(
				"attributesInformation.destinationAccountNumber",
				"service.request.destinationAccountNumber.invalid");
		}
	}

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
		final ServiceRequestForm form, final Errors errors,
		final boolean isMandatory) {

		if (form.getAttributesInformation(
			).getDestinationLodgementRefs() != null) {

			if (StringUtils.isBlank(
					(CharSequence)form.getAttributesInformation(
					).getDestinationLodgementRefs()[0])) {

				if (isMandatory) {
					errors.rejectValue(
						"attributesInformation.destinationLodgementRefs[0]",
						"service.request.destinationLodgementRef.empty");
				}
			}
			else if (form.getAttributesInformation(
					).getDestinationLodgementRefs()[0].length() > 18) {

				errors.rejectValue(
					"attributesInformation.destinationLodgementRefs[0]",
					"service.request.destinationLodgementRef.invalid");
			}
		}
	}

	public void validateDestinationLodgementRefs(
		final ServiceRequestForm form, final Errors errors,
		final boolean isMandatory) {

		if (form.getAttributesInformation(
			).getDestinationLodgementRefs() != null) {

			int i = 0;

			if (form.isPrePopulated()) {
				i = 1;
			}

			while (i < form.getAttributesInformation(
					).getDestinationLodgementRefs(
					).length) {

				if (StringUtils.isBlank(
						(CharSequence)form.getAttributesInformation(
						).getDestinationLodgementRefs()[i])) {

					if (isMandatory) {
						errors.rejectValue(
							"attributesInformation.destinationLodgementRefs[" +
								i + "]",
							"service.request.destinationLodgementRef.empty");
					}
				}
				else if (form.getAttributesInformation(
						).getDestinationLodgementRefs()[i].length() > 18) {

					errors.rejectValue(
						"attributesInformation.destinationLodgementRefs[" + i +
							"]",
						"service.request.destinationLodgementRef.invalid");
				}

				++i;
			}
		}
	}

	public void validateDisclaimer(
		final ServiceRequestForm form, final Errors errors) {

		if (!form.isDisclaimer()) {
			errors.rejectValue(
				"disclaimer", "service.request.disclaimer.validation");
		}
	}

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

	public void validateFinInstTraceBSBNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getFinInstTraceBSBNumber())) {

			errors.rejectValue(
				"attributesInformation.finInstTraceBSBNumber",
				"service.request.finInstTraceBSBNumber.empty");
		}
		else if (!CommonUtil.isBsbValid(
					form.getAttributesInformation(
					).getFinInstTraceBSBNumber())) {

			errors.rejectValue(
				"attributesInformation.finInstTraceBSBNumber",
				"service.request.BSB.invalid");
		}
	}

	public void validateFinInstUserId(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getFinInstUserId())) {

			errors.rejectValue(
				"attributesInformation.finInstUserId",
				"service.request.finInstUserId.empty");
		}
		else if (!StringUtils.isNumeric(
					(CharSequence)form.getAttributesInformation(
					).getFinInstUserId()) ||
				 (form.getAttributesInformation(
				 ).getFinInstUserId(
				 ).length() != 6)) {

			errors.rejectValue(
				"attributesInformation.finInstUserId",
				"service.request.finInstUserId.invalid");
		}
	}

	public void validateOfiAdditionalInfo(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isNotBlank(
				(CharSequence)form.getAttributesInformation(
				).getOfiAdditionalInfo()) &&
			CommonUtil.containsPAN(
				form.getAttributesInformation(
				).getOfiAdditionalInfo())) {

			errors.rejectValue(
				"attributesInformation.ofiAdditionalInfo",
				"service.request.comment.clear.pan");
		}
	}

	public void validateProcessedDate(
		final ServiceRequestForm form, final Errors errors) {

		if (form.getAttributesInformation(
			).getProcessedDates() != null) {

			if (StringUtils.isBlank(
					(CharSequence)form.getAttributesInformation(
					).getProcessedDates()[0])) {

				errors.rejectValue(
					"attributesInformation.processedDates[0]",
					"service.request.processedDate.empty");
			}
			else if (!Utility.validateDate(
						form.getAttributesInformation(
						).getProcessedDates()[0],
						Constants.DATE_FORMAT)) {

				errors.rejectValue(
					"attributesInformation.processedDates[0]",
					"service.request.date.validation");
			}
		}
	}

	public void validateProcessedDates(
		final ServiceRequestForm form, final Errors errors) {

		if (form.getAttributesInformation(
			).getProcessedDates() != null) {

			int i = 0;

			if (form.isPrePopulated()) {
				i = 1;
			}

			while (i < form.getAttributesInformation(
					).getProcessedDates(
					).length) {

				if (StringUtils.isBlank(
						(CharSequence)form.getAttributesInformation(
						).getProcessedDates()[i])) {

					errors.rejectValue(
						"attributesInformation.processedDates[" + i + "]",
						"service.request.processedDate.empty");
				}
				else if (!Utility.validateDate(
							form.getAttributesInformation(
							).getProcessedDates()[i],
							Constants.DATE_FORMAT)) {

					errors.rejectValue(
						"attributesInformation.processedDates[" + i + "]",
						"service.request.date.validation");
				}

				++i;
			}
		}
	}

	public void validateReceiverAccountName(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getReceiverAccountName())) {

			errors.rejectValue(
				"attributesInformation.receiverAccountName",
				"service.request.receiverAccountName.empty");
		}
	}

	public void validateReceiverAccountNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getReceiverAccountNumber())) {

			errors.rejectValue(
				"attributesInformation.receiverAccountNumber",
				"service.request.receiverAccountNumber.empty");
		}
		else if (!StringUtils.isAlphanumeric(
					(CharSequence)form.getAttributesInformation(
					).getReceiverAccountNumber()) ||
				 (form.getAttributesInformation(
				 ).getReceiverAccountNumber(
				 ).length() > 9)) {

			errors.rejectValue(
				"attributesInformation.receiverAccountNumber",
				"service.request.accountNumber.invalid");
		}
	}

	public void validateReceiverBSBNumber(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getReceiverBSBNumber())) {

			errors.rejectValue(
				"attributesInformation.receiverBSBNumber",
				"service.request.receiverBSBNumber.empty");
		}
		else if (!CommonUtil.isBsbValid(
					form.getAttributesInformation(
					).getReceiverBSBNumber())) {

			errors.rejectValue(
				"attributesInformation.receiverBSBNumber",
				"service.request.BSB.invalid");
		}
	}

	public void validateReceiverLodgementRef(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getReceiverLodgementRef())) {

			errors.rejectValue(
				"attributesInformation.receiverLodgementRef",
				"service.request.receiverLodgementRef.empty");
		}
	}

	public void validateRemitterName(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getRemitterName())) {

			errors.rejectValue(
				"attributesInformation.remitterName",
				"service.request.remitterName.empty");
		}
	}

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
				this.pattern = Pattern.compile(Constants.REG_EXP_AMOUNT);
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

	public void validateTransactionAmounts(
		final ServiceRequestForm form, final Errors errors) {

		if (form.getAttributesInformation(
			).getTransactionAmounts() != null) {

			int i = 0;

			if (form.isPrePopulated()) {
				i = 1;
			}

			while (i < form.getAttributesInformation(
					).getTransactionAmounts(
					).length) {

				final String amount = form.getAttributesInformation(
				).getTransactionAmounts()[i];

				if (StringUtils.isBlank((CharSequence)amount)) {
					errors.rejectValue(
						"attributesInformation.transactionAmounts[" + i + "]",
						"service.request.transactionAmount.empty");
				}
				else {
					this.pattern = Pattern.compile(Constants.REG_EXP_AMOUNT);
					this.matcher = this.pattern.matcher(amount);

					if (!this.matcher.matches() ||
						(this.removeDecimal(
							amount
						).length() > 10)) {

						errors.rejectValue(
							"attributesInformation.transactionAmounts[" + i +
								"]",
							"service.request.amount.invalid");
					}
				}

				++i;
			}
		}
	}

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

	public Matcher matcher;
	public Pattern pattern;

	protected String removeDecimal(String amount) {
		if (StringUtils.isNotBlank((CharSequence)amount)) {
			amount = amount.replaceAll("\\.", "");
		}

		return amount;
	}

}