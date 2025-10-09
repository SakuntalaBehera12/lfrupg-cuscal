package au.com.cuscal.chargeback.validator;

import au.com.cuscal.chargeback.common.Constants;
import au.com.cuscal.chargeback.common.VisaChargebackProperties;
import au.com.cuscal.chargeback.forms.ChargebackForm;
import au.com.cuscal.chargeback.forms.QuestionnaireInformation;
import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.framework.cards.CardUtil;

import java.math.BigInteger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

@Component("visaChargeBackValidator")
public class VisaChargeBackValidator extends AbstractServiceRequestValidator {

	/**
	 *
	 * @param chargebackForm
	 * @param errors
	 */
	public void validate(
		ChargebackForm chargebackForm, Errors errors,
		VisaChargebackProperties visaChargebackProperties) {

		logger.debug("validate - type: " + chargebackForm.getType());

		if (chargebackForm.getType(
			).equals(
				Constants.UNSELECTED_REASON
			)) {

			errors.rejectValue("type", "chargeback.type.validation");
		}

		if (StringUtils.isNotBlank(chargebackForm.getReasonCode())) {
			if (chargebackForm.getReasonCode(
				).equals(
					Constants.UNSELECTED_REASON
				)) {

				errors.rejectValue(
					"reasonCode", "chargeback.reasonCode.validation");
			}
		}
		else {
			errors.rejectValue(
				"reasonCode", "chargeback.reasonCode.validation");
		}

		/*if (!"59".equals(chargebackForm.getReasonCode())) {
			validateMemberMessages(chargebackForm, errors);
		}*/

		if (!chargebackForm.isDisclaimer()) {
			errors.rejectValue(
				"disclaimer", "chargeback.disclaimer.validation");
		}

		if (chargebackForm.getType(
			).equals(
				Constants.REASON_FRAUD
			)) {

			validateFraud(chargebackForm, errors);
		}
		/*else if (chargebackForm.getType().equals(
				Constants.REASON_REQUEST_FOR_INFORMATION)) {

			validateRequestForInformation(chargebackForm, errors);
		}

		if (chargebackForm.getType().equals(Constants.REASON_AUTHORISATION)) {
			validateAuthorisation(chargebackForm, errors);
		}

		if (chargebackForm.getType().equals(Constants.REASON_PROCESSING_ERROR)) {
			validateProcessingError(chargebackForm, errors);
		}

		if (chargebackForm.getType().equals(Constants.REASON_CANCELLED_RETURNED)) {
			validateCancelledReturned(chargebackForm, errors);
		}

		if (chargebackForm.getType().equals(Constants.REASON_NON_RECEIPT_GOODS)) {
			validateNonReceiptGoods(chargebackForm, errors);
		}*/

		if (StringUtils.isNotBlank(chargebackForm.getNote())) {
			if (containsPAN(chargebackForm.getNote())) {
				errors.rejectValue("note", "chargeback.note.clear.pan");
			}
		}

		checkAttachmentSize(chargebackForm, errors, visaChargebackProperties);
		//Commented this out because we are not checking anything with it.
		//chargebackForm.getRequestAttachments();
	}

	CardUtil cardUtil = CardUtil.getInstance();

	protected boolean isValidAmount(String amount) {
		boolean isValid = false;

		try {
			amount = amount.replaceAll("([\\s\\.,]+)", "");
			new BigInteger(amount);
			isValid = true;
		}
		catch (NumberFormatException e) {
			//Do nothing. The amount entered now is not correct.
		}

		return isValid;
	}

	private void checkAttachmentSize(
		ChargebackForm form, Errors errors,
		VisaChargebackProperties visaChargebackProperties) {

		String maxFileSize = visaChargebackProperties.getVisaChargebackProperty(
		).getProperty(
			"file.attachment.size"
		);

		Long fileSize = Long.valueOf(maxFileSize);

		List<MultipartFile> attachments = form.getRequestAttachments();

		if ((null != attachments) && (attachments.size() > 0)) {
			for (MultipartFile multipartFile : attachments) {
				if (multipartFile.getSize() > fileSize) {
					errors.rejectValue(
						"requestAttachments",
						"chargeback.attachment.error.size");

					break;
				}
			}
		}
	}

	/**
	 * Check if the note contains full PAN or not.
	 *
	 * @param  text
	 * @return Boolean
	 */
	private Boolean containsPAN(String text) {
		if (StringUtils.isBlank(text) || (text.length() < 14)) {
			return Boolean.FALSE;
		}

		Pattern p = Pattern.compile(Constants.CC_PATTERN);

		Matcher m = p.matcher(text);
		Boolean result = Boolean.FALSE;

		while (m.find() && !result) {
			String number = text.substring(m.start(), m.end());

			number = number.replaceAll(
				Constants.DASH, ""
			).replaceAll(
				Constants.SPACE, ""
			);
			result = cardUtil.panValidate(number);
		}

		return result;
	}

	private void validateAttemptToResolve(
		ChargebackForm chargebackForm, Errors errors) {

		if (StringUtils.isBlank(
				chargebackForm.getAttemptToResolve(
				).getResolveWithMerchant()) ||
			"0".equals(
				chargebackForm.getAttemptToResolve(
				).getResolveWithMerchant())) {

			errors.rejectValue(
				"attemptToResolve.resolveWithMerchant",
				"chargeback.select.validation");
		}
		else if ("yes".equals(
					chargebackForm.getAttemptToResolve(
					).getResolveWithMerchant())) {

			if (StringUtils.isNotBlank(
					chargebackForm.getAttemptToResolve(
					).getErrorDateContact())) {

				if (!Utility.validateDate(
						chargebackForm.getAttemptToResolve(
						).getErrorDateContact(),
						Constants.DATE_FORMAT)) {

					errors.rejectValue(
						"attemptToResolve.errorDateContact",
						"chargeback.date.validation");
				}
			}
			else {
				errors.rejectValue(
					"attemptToResolve.errorDateContact",
					"chargeback.date.validation");
			}

			if (StringUtils.isBlank(
					chargebackForm.getAttemptToResolve(
					).getNameContact())) {

				errors.rejectValue(
					"attemptToResolve.nameContact",
					"chargeback.elaborationInfo.fraudAdviceNo.validation");
			}

			if (StringUtils.isBlank(
					chargebackForm.getAttemptToResolve(
					).getMethodContact())) {

				errors.rejectValue(
					"attemptToResolve.methodContact",
					"chargeback.elaborationInfo.fraudAdviceNo.validation");
			}

			if (StringUtils.isBlank(
					chargebackForm.getAttemptToResolve(
					).getMerchantResponse())) {

				errors.rejectValue(
					"attemptToResolve.merchantResponse",
					"chargeback.elaborationInfo.fraudAdviceNo.validation");
			}
		}
		else if ("no".equals(
					chargebackForm.getAttemptToResolve(
					).getResolveWithMerchant())) {

			if (!chargebackForm.getAttemptToResolve(
				).isNotApplicableLocalLaw()) {

				errors.rejectValue(
					"attemptToResolve.notApplicableLocalLaw",
					"chargeback.checkbox.validation");
			}

			if (StringUtils.isBlank(
					chargebackForm.getAttemptToResolve(
					).getExplainWhyNotResolve())) {

				errors.rejectValue(
					"attemptToResolve.explainWhyNotResolve",
					"chargeback.elaborationInfo.fraudAdviceNo.validation");
			}
		}
	}

	private void validateAuthorisation(
		ChargebackForm chargebackForm, Errors errors) {

		if ("56".equals(chargebackForm.getReasonCode())) {
			if (StringUtils.isNotBlank(
					chargebackForm.getElaborationInfo(
					).getAuthDeclinedDate())) {

				if (!Utility.validateDate(
						chargebackForm.getElaborationInfo(
						).getAuthDeclinedDate(),
						Constants.DATE_FORMAT)) {

					errors.rejectValue(
						"elaborationInfo.authDeclinedDate",
						"chargeback.date.validation");
				}
			}
			else {
				errors.rejectValue(
					"elaborationInfo.authDeclinedDate",
					"chargeback.date.validation");
			}
		}

		if ("58".equals(chargebackForm.getReasonCode())) {
			if (StringUtils.isNotBlank(
					chargebackForm.getElaborationInfo(
					).getCardExpiredDate())) {

				if (!Utility.validateDate(
						chargebackForm.getElaborationInfo(
						).getCardExpiredDate(),
						Constants.DATE_FORMAT)) {

					errors.rejectValue(
						"elaborationInfo.cardExpiredDate",
						"chargeback.date.validation");
				}
			}
			else {
				errors.rejectValue(
					"elaborationInfo.cardExpiredDate",
					"chargeback.date.validation");
			}
		}
	}

	private void validateCancelledReturned(
		ChargebackForm chargebackForm, Errors errors) {

		//41 - Cancelled Recurring Transactions.

		if ("66".equals(chargebackForm.getReasonCode())) {
			String dateRecurringTransCancelled =
				chargebackForm.getCancellationInfo(
				).getDateRecurringTransCancelled();
			String dateAcquirerNotified = chargebackForm.getCancellationInfo(
			).getDateAcquirerNotified();
			String datePreviousChargeback = chargebackForm.getCancellationInfo(
			).getDatePreviousChargeback();

			if (StringUtils.isBlank(dateRecurringTransCancelled) &&
				StringUtils.isBlank(dateAcquirerNotified) &&
				StringUtils.isBlank(datePreviousChargeback)) {

				errors.rejectValue(
					"cancellationInfo.dateInfo", "chargeback.date.one.atleast");
			}

			if (StringUtils.isNotBlank(dateRecurringTransCancelled)) {
				if (!Utility.validateDate(
						dateRecurringTransCancelled, Constants.DATE_FORMAT)) {

					errors.rejectValue(
						"cancellationInfo.dateRecurringTransCancelled",
						"chargeback.date.validation");
				}
			}

			if (StringUtils.isNotBlank(dateAcquirerNotified)) {
				if (!Utility.validateDate(
						dateAcquirerNotified, Constants.DATE_FORMAT)) {

					errors.rejectValue(
						"cancellationInfo.dateAcquirerNotified",
						"chargeback.date.validation");
				}
			}

			if (StringUtils.isNotBlank(datePreviousChargeback)) {
				if (!Utility.validateDate(
						datePreviousChargeback, Constants.DATE_FORMAT)) {

					errors.rejectValue(
						"cancellationInfo.datePreviousChargeback",
						"chargeback.date.validation");
				}
			}

			validateAttemptToResolve(chargebackForm, errors);
		}

		//85 - Credit not processed

		if ("68".equals(chargebackForm.getReasonCode())) {
			String creditVoucherRequest = chargebackForm.getCreditInfo(
			).getCreditVoucherGiven();

			if (StringUtils.isBlank(creditVoucherRequest) ||
				"0".equals(creditVoucherRequest)) {

				errors.rejectValue(
					"creditInfo.creditVoucherGiven",
					"chargeback.select.validation");
			}

			if ("354".equals(creditVoucherRequest)) { //This is yes. Validate the credit request section.
				String creditVoucherDated = chargebackForm.getCreditInfo(
				).getCreditVoucherDated();

				if (StringUtils.isBlank(creditVoucherDated) ||
					"0".equals(creditVoucherDated)) {

					errors.rejectValue(
						"creditInfo.creditVoucherDated",
						"chargeback.select.validation");
				}
			}

			String whatWasPurchased = chargebackForm.getElaborationInfo(
			).getWhatWasPurchased();

			if (StringUtils.isBlank(whatWasPurchased) ||
				"0".equals(whatWasPurchased)) {

				errors.rejectValue(
					"elaborationInfo.whatWasPurchased",
					"chargeback.select.validation");
			}

			if (whatWasPurchased.equals("294")) { // Merchandise
				String wasMerchandiseReturned =
					chargebackForm.getElaborationInfo(
					).getMerchandiseReturn();

				if (StringUtils.isBlank(wasMerchandiseReturned) ||
					"0".equals(wasMerchandiseReturned)) {

					errors.rejectValue(
						"elaborationInfo.merchandiseReturn",
						"chargeback.select.validation");
				}

				if ("yes".equalsIgnoreCase(wasMerchandiseReturned)) {
					String merchantRefuse = chargebackForm.getElaborationInfo(
					).getMerchantRefuse();

					if (StringUtils.isBlank(merchantRefuse) ||
						"0".equals(merchantRefuse)) {

						errors.rejectValue(
							"elaborationInfo.merchantRefuse",
							"chargeback.select.value");
					}
				}
			}

			String didYouCancel = chargebackForm.getElaborationInfo(
			).getDidCancel();

			if (StringUtils.isBlank(didYouCancel) || "0".equals(didYouCancel)) {
				errors.rejectValue(
					"elaborationInfo.didCancel",
					"chargeback.select.validation");
			}

			String wasCardholderGivenCancellationPolicy =
				chargebackForm.getCancellationInfo(
				).getWasCardholderGivenCancellationPolicy();

			if (StringUtils.isBlank(wasCardholderGivenCancellationPolicy) ||
				"0".equals(wasCardholderGivenCancellationPolicy)) {

				errors.rejectValue(
					"cancellationInfo.wasCardholderGivenCancellationPolicy",
					"chargeback.select.validation");
			}

			validateAttemptToResolve(chargebackForm, errors);
		}
	}

	/**
	 * Validate the Memeber messages section.
	 *
	 * @param chargebackForm
	 * @param errors
	 */
	/*private void validateMemberMessages(ChargebackForm chargebackForm, Errors errors) {
		//Validate Member Messages

		if (chargebackForm.getMemberMessage().equals(
				Constants.UNSELECTED_REASON)) {

			errors.rejectValue("memberMessage",
					"chargeback.memberMessage.validation");
		}

		if (StringUtils.isBlank(chargebackForm.getUpdateMessage())) {
			errors.rejectValue("updateMessage",
					"chargeback.updateMessage.validation");
		}
	}*/

	/**
	 *
	 * @param chargebackForm
	 * @param errors
	 */
	private void validateFraud(ChargebackForm chargebackForm, Errors errors) {
		if (!Constants.CHARGEBACK_WITHOUT_CERTIFICATIONS.contains(
				chargebackForm.getReasonCode())) {

			if (chargebackForm.getCertifications().length == 0) {
				errors.rejectValue(
					"certifications", "chargeback.certification.validation");
			}
		}

		//Validate for Fraud Reporting date.

		if (chargebackForm.getReasonCode(
			).equals(
				"54"
			)) {

			if (StringUtils.isBlank(chargebackForm.getFraudReportingDate())) {
				errors.rejectValue(
					"fraudReportingDate",
					"chargeback.fraud.reporting.date.validation");
			}
		}

		if (!Constants.CHARGEBACK_WITHOUT_ELABORATIONS.contains(
				chargebackForm.getReasonCode())) {

			if (null != chargebackForm.getElaborationInfo()) {
				if (StringUtils.isBlank(
						chargebackForm.getElaborationInfo(
						).getFraudAdviceNo())) {

					errors.rejectValue(
						"elaborationInfo.fraudAdviceNo",
						"chargeback.elaborationInfo.fraudAdviceNo.validation");
				}

				if (StringUtils.isNotBlank(
						chargebackForm.getElaborationInfo(
						).getCardDate())) {

					if (!Utility.validateDate(
							chargebackForm.getElaborationInfo(
							).getCardDate(),
							Constants.DATE_FORMAT)) {

						errors.rejectValue(
							"elaborationInfo.cardDate",
							"chargeback.date.validation");
					}
				}
				else {
					errors.rejectValue(
						"elaborationInfo.cardDate",
						"chargeback.date.validation");
				}

				if (StringUtils.isNotBlank(
						chargebackForm.getElaborationInfo(
						).getFraudDate())) {

					if (!Utility.validateDate(
							chargebackForm.getElaborationInfo(
							).getFraudDate(),
							Constants.DATE_FORMAT)) {

						errors.rejectValue(
							"elaborationInfo.fraudDate",
							"chargeback.date.validation");
					}
				}
				else {
					errors.rejectValue(
						"elaborationInfo.fraudDate",
						"chargeback.date.validation");
				}

				if (!chargebackForm.getElaborationInfo(
					).isAcctNoFictitiousOrInvalid()) {

					if (chargebackForm.getElaborationInfo(
						).getCardStatus(
						).equals(
							"0"
						)) {

						errors.rejectValue(
							"elaborationInfo.cardStatus",
							"chargeback.elaborationInfo.cardStatus.validation");
					}
				}
			}
		}
		else {
			logger.debug(
				"We do not check for elaboration codes because they are not required.");
		}
	}

	private void validateNonReceiptGoods(
		ChargebackForm chargebackForm, Errors errors) {

		//90 - Non Receipt of cash or Load transaction value at ATM.

		if ("70".equals(chargebackForm.getReasonCode())) {
			QuestionnaireInformation questionInfo =
				chargebackForm.getQuestionInfo();

			if (StringUtils.isBlank(questionInfo.getCashLoadInformation()) ||
				"0".equals(questionInfo.getCashLoadInformation())) {

				errors.rejectValue(
					"questionInfo.cashLoadInformation",
					"chargeback.select.validation");
			}

			if (StringUtils.isBlank(questionInfo.getAmountRequested()) ||
				!isValidAmount(questionInfo.getAmountRequested())) {

				errors.rejectValue(
					"questionInfo.amountRequested",
					"chargeback.amount.validation");
			}

			if (StringUtils.isBlank(questionInfo.getAmountReceived()) ||
				!isValidAmount(questionInfo.getAmountReceived())) {

				errors.rejectValue(
					"questionInfo.amountReceived",
					"chargeback.amount.validation");
			}
		}
	}

	private void validateProcessingError(
		ChargebackForm chargebackForm, Errors errors) {

		//80 - Incorrect Transaction Amount.

		if ("63".equals(chargebackForm.getReasonCode())) {
			String infoIncorrect = chargebackForm.getElaborationInfo(
			).getInfoIncorrect();

			if (StringUtils.isBlank(infoIncorrect) ||
				"0".equals(infoIncorrect)) {

				errors.rejectValue(
					"elaborationInfo.infoIncorrect",
					"chargeback.select.validation");
			}
			else if ("1".equals(infoIncorrect)) {
				String amountIncorrect = chargebackForm.getElaborationInfo(
				).getAmountIncorrect();

				if (StringUtils.isBlank(amountIncorrect) ||
					"0".equals(amountIncorrect)) {

					errors.rejectValue(
						"elaborationInfo.amountIncorrect",
						"chargeback.select.validation");
				}
			}
		}

		//82 - Duplicate Processing.

		if ("64".equals(chargebackForm.getReasonCode())) {
			if (StringUtils.isBlank(
					chargebackForm.getElaborationInfo(
					).getProcessingErrorARN())) {

				errors.rejectValue(
					"elaborationInfo.processingErrorARN",
					"chargeback.elaborationInfo.arn.validation");
			}

			if (StringUtils.isBlank(
					chargebackForm.getElaborationInfo(
					).getProcessingErrorTransactionDate())) {

				errors.rejectValue(
					"elaborationInfo.processingErrorTransactionDate",
					"chargeback.date.validation");
			}
		}

		//86 - Paid by other means.

		if ("65".equals(chargebackForm.getReasonCode())) {
			String paymentMethod = chargebackForm.getElaborationInfo(
			).getProcessingErrorPaymentMethod();

			if (StringUtils.isBlank(paymentMethod) ||
				"0".equals(paymentMethod)) {

				errors.rejectValue(
					"elaborationInfo.processingErrorPaymentMethod",
					"chargeback.select.validation");
			}

			if (!chargebackForm.getElaborationInfo(
				).isProcessingErrorProofOtherMeans()) {

				errors.rejectValue(
					"elaborationInfo.processingErrorProofOtherMeans",
					"chargeback.checkbox.validation");
			}

			validateAttemptToResolve(chargebackForm, errors);
		}
	}

	//Validate the amount.
	/**
	 *
	 * @param chargebackForm
	 * @param errors
	 */
	private void validateRequestForInformation(
		ChargebackForm chargebackForm, Errors errors) {

		if (StringUtils.isNotBlank(
				chargebackForm.getRetrievalRequest(
				).getTc52Date())) {

			if (!StringUtils.isBlank(
					chargebackForm.getRetrievalRequest(
					).getTc52Date())) {

				if (!Utility.validateDate(
						chargebackForm.getRetrievalRequest(
						).getTc52Date(),
						Constants.DATE_FORMAT)) {

					errors.rejectValue(
						"retrievalRequest.tc52Date",
						"chargeback.date.validation");
				}
			}
		}
		else {
			errors.rejectValue(
				"retrievalRequest.tc52Date", "chargeback.date.validation");
		}
	}

	private static final Logger logger = Logger.getLogger(
		VisaChargeBackValidator.class);

}