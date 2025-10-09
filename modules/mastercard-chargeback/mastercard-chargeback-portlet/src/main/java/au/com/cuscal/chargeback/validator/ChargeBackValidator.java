//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.validator;

import static au.com.cuscal.chargeback.common.Constants.*;

import au.com.cuscal.chargeback.common.ChargebackProperties;
import au.com.cuscal.chargeback.common.FunctionCode;
import au.com.cuscal.chargeback.forms.ChargebackForm;
import au.com.cuscal.framework.cards.CardUtil;

import java.math.BigDecimal;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

@Component("chargeBackValidator")
public class ChargeBackValidator extends AbstractServiceRequestValidator {

	public ChargeBackValidator() {
		this.cardUtil = CardUtil.getInstance();
	}

	public void validate(
		ChargebackForm chargebackForm, Errors errors,
		ChargebackProperties chargebackProperties) {

		String disputeType = chargebackForm.getType();
		logger.debug("disputeType - type: " + disputeType);

		if (UNSELECTED_REASON.equals(disputeType)) {
			errors.rejectValue("type", "chargeback.type.validation");
		}

		String reasonCode = chargebackForm.getReasonCode();

		if (!MASTERCARD_REPORT_FRAUD_ATTRIBUTES_LIST_ID.equals(disputeType)) {
			if (StringUtils.isNotBlank(reasonCode)) {
				if (reasonCode.equals(UNSELECTED_REASON)) {
					errors.rejectValue(
						"reasonCode", "chargeback.reasonCode.validation");
				}
			}
			else {
				errors.rejectValue(
					"reasonCode", "chargeback.reasonCode.validation");
			}
		}

		if (MASTERCARD_FIRST_CHARGEBACK_ATTRIBUTES_LIST_ID.equals(
				disputeType) ||
			MASTERCARD_ARBITRATION_CHARGEBACK_ATTRIBUTES_LIST_ID.equals(
				disputeType)) {

			if (!isValidDisputeAmount(
					chargebackForm.getDisputeAmount(),
					chargebackForm.getCardHolderAmount())) {

				logger.debug(
					"There is error on dispute amount valdiation - disputeAmount=" +
						chargebackForm.getDisputeAmount() +
							", cardHolderAmount=" +
								chargebackForm.getCardHolderAmount());
				errors.rejectValue(
					"disputeAmount", "chargeback.disputeAmount.validation");
			}
		}

		if (StringUtils.isNotBlank(chargebackForm.getNote())) {
			if (containsPAN(chargebackForm.getNote())) {
				errors.rejectValue("note", "chargeback.note.clear.pan");
			}
		}
		else if (MASTERCARD_REPORT_FRAUD_ATTRIBUTES_LIST_ID.equals(
					disputeType)) {

			errors.rejectValue("note", "chargeback.note.empty");
		}

		boolean hasFileAttached = hasFileAttached(chargebackForm);

		if (chargebackForm.isDocumentIndicator()) {
			if (!hasFileAttached) {
				errors.rejectValue(
					"requestAttachments",
					"chargeback.requestAttachments.validation");
			}
			else {
				checkAttachmentSize(
					chargebackForm, errors, chargebackProperties);
			}
		}
		else if (hasFileAttached) {

			//file attached but document indicator flag not selected

			errors.rejectValue(
				"documentIndicator", "chargeback.documentIndicator.validation");
		}
		else if (Arrays.asList(
					docuemntRequiredReasonCodes
				).contains(
					reasonCode
				)) {

			errors.rejectValue(
				"documentIndicator", "chargeback.documentIndicator.required");
		}

		if (!chargebackForm.isDisclaimer()) {
			errors.rejectValue(
				"disclaimer", "chargeback.disclaimer.validation");
		}

		if (MASTERCARD_FIRST_CHARGEBACK_ATTRIBUTES_LIST_ID.equals(
				disputeType)) {

			logger.debug(
				"about to raise first chargeback, verfiy message type and fucntion code");

			if (!(MASTERCARD_PRESENTMENT_MESSAGE_TYPE_TRANSACTION_LOG.equals(
					chargebackForm.getMessageType()) &&
				  FunctionCode.FIRST_PRESENTMENT.getValue(
				  ).equals(
					  chargebackForm.getFunctionCode()
				  ))) {

				errors.rejectValue("type", "chargeback.type.validation");
			}
		}
		else if (MASTERCARD_ARBITRATION_CHARGEBACK_ATTRIBUTES_LIST_ID.equals(
					disputeType)) {

			if (!MASTERCARD_PRESENTMENT_MESSAGE_TYPE_TRANSACTION_LOG.equals(
					chargebackForm.getMessageType()) ||
				(!FunctionCode.SECOND_PRESENTMENT_FULL.getValue(
				).equals(
					chargebackForm.getFunctionCode()
				) &&
				 !FunctionCode.SECOND_PRESENTMENT_PARTIAL.getValue(
				 ).equals(
					 chargebackForm.getFunctionCode()
				 ))) {

				errors.rejectValue("type", "chargeback.type.validation");
			}
		}
	}

	private void checkAttachmentSize(
		ChargebackForm form, Errors errors,
		ChargebackProperties chargebackProperties) {

		String maxFileSize = chargebackProperties.getChargebackProperty(
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

		Pattern p = Pattern.compile(CC_PATTERN);

		Matcher m;
		Boolean result;
		String number;

		for (m = p.matcher(text), result = Boolean.FALSE; m.find() && !result;
			 result = this.cardUtil.panValidate(number)) {

			number = text.substring(m.start(), m.end());
			number = number.replaceAll(
				DASH, ""
			).replaceAll(
				SPACE, ""
			);
		}

		return result;
	}

	CardUtil cardUtil = CardUtil.getInstance();

	/**
	 * Verify the charge back supporting document has been attached or not
	 *
	 * @param chargebackForm
	 * @return boolean
	 */
	private boolean hasFileAttached(ChargebackForm chargebackForm) {
		boolean hasFileAttached = false;
		List<MultipartFile> requestAttachments =
			chargebackForm.getRequestAttachments();

		if ((null != requestAttachments) && (requestAttachments.size() > 0)) {
			for (MultipartFile multipartFile : requestAttachments) {
				if (multipartFile.getSize() > 0) {
					logger.info(
						"Attached file name =" + multipartFile.getName() +
							" : size =" + multipartFile.getSize() +
								": OriginalFilename = " +
									multipartFile.getOriginalFilename() +
										" :ContentType= " +
											multipartFile.getContentType());
					hasFileAttached = true;
					//not breaking the loop, want to log the attached file details
				}
				else {
					logger.debug("no file attachhed");
				}
			}
		}

		return hasFileAttached;
	}

	private boolean isValidDisputeAmount(
		String disputeAmount, String cardHolderAmount) {

		boolean isValid = false;

		try {
			if (StringUtils.isNotBlank((CharSequence)disputeAmount) &&
				disputeAmount.matches(NON_NEGATIVE_MONEY_FIELD)) {

				final BigDecimal dAmount = new BigDecimal(disputeAmount);
				final BigDecimal chAmount = new BigDecimal(
					cardHolderAmount.replaceAll(",", ""));
				logger.debug(
					(Object)("chAmount.compareTo(dAmount)=" +
						chAmount.compareTo(dAmount)));

				if (chAmount.compareTo(dAmount) >= 0) {
					isValid = true;
				}
				else {
					logger.debug(
						(Object)
							("Validation failed disputeAmount=" +
								disputeAmount + " and cardHolderAmount =" +
									cardHolderAmount));
				}
			}
		}
		catch (Exception e) {
			//do nothing, the entered dispute amount value is not valid
			logger.debug(
				"disputeAmount=" + disputeAmount + ", cardHolderAmount=" +
					cardHolderAmount);
		}

		return isValid;
	}

	private static final String NON_NEGATIVE_MONEY_FIELD =
		"^(?=.*[1-9])\\d*(?:\\.\\d{1,2})?$";

	private static final String[] docuemntRequiredReasonCodes = {
		"905", "906", "912", "913", "914", "917", "918"
	};
	private static final Logger logger = Logger.getLogger(
		ChargeBackValidator.class);

}