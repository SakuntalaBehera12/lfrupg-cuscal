package au.com.cuscal.transactions.validator;

import au.com.cuscal.framework.cards.CardUtil;
import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.commons.TransactionAppProperties;
import au.com.cuscal.transactions.commons.Utility;
import au.com.cuscal.transactions.forms.RequestAdditionalTransactionInformation;
import au.com.cuscal.transactions.forms.RequestAtmPosClaimInformation;
import au.com.cuscal.transactions.forms.ServiceRequestForm;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.Days;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component("serviceRequestValidator")
public class ServiceRequestValidator implements Validator {

	public boolean isValidNegativeAmount(
		RequestAtmPosClaimInformation form, Errors errors) {

		pattern = Pattern.compile(Constants.REG_EXP_NEGATIVE_AMOUNT);
		matcher = pattern.matcher(form.getRequestAmountReceived());

		if (!matcher.matches()) {
			errors.rejectValue(
				"atmPosClaimInformation.requestAmountReceived",
				"service.request.amount.invalid");

			return false;
		}

		return true;
	}

	public boolean supports(Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		ServiceRequestForm form = (ServiceRequestForm)target;

		if (form.getVisaTransactionInformation() != null) {
			validateContactInfoForVisa(form, errors);
			validateVisaInformation(
				form.getVisaTransactionInformation(), errors);

			if (!form.isDisclaimer()) {
				errors.rejectValue(
					"disclaimer",
					"Please ensure you have selected the agreement");
			}

			if (transactionAppProperties.getTransProps(
				).getProperty(
					Constants.TC52_PRODUCT_ID
				).equals(
					form.getVisaTransactionInformation(
					).getRequestType()
				)) {

				if (form.getTransactionInformation(
					).getAtmPos(
					).contains(
						"POS"
					)) {

					if (form.getTransactionInformation(
						).getBusinessDate() != null) {

						String businessDateString =
							form.getTransactionInformation(
							).getBusinessDate();

						Date businessDate = Utility.formatDate(
							businessDateString, Constants.DATE_FORMAT_YYYYMMDD);

						// if business date is more than a year for POS
						// transactions: reject

						if (Days.daysBetween(
								new DateTime(businessDate), new DateTime()
							).getDays() > 365) {

							errors.rejectValue(
								"visaTransactionInformation.errorMsg",
								"A Voucher Request cannot be submitted for transaction older than 12 months ");
						}
					}
				}

				if (form.getVisaTransactionInformation(
					).isReversal()) {

					errors.rejectValue(
						"visaTransactionInformation.errorMsg",
						"A Voucher Request cannot be submitted for a Reversal Transaction");
				}

				if (form.getVisaTransactionInformation(
					).isVisaAtm()) {

					errors.rejectValue(
						"visaTransactionInformation.errorMsg",
						"A Voucher Request cannot be submitted for an ATM transaction");
				}

				if (form.getVisaTransactionInformation(
					).isOutstandingTC52()) {

					errors.rejectValue(
						"visaTransactionInformation.errorMsg",
						"A 'Voucher Request' request has already been logged for this transaction");
				}
			}
			else if (transactionAppProperties.getTransProps(
					).getProperty(
						Constants.TC40_PRODUCT_ID
					).equals(
						form.getVisaTransactionInformation(
						).getRequestType()
					)) {

				if (form.getVisaTransactionInformation(
					).isOutstandingTC40()) {

					errors.rejectValue(
						"visaTransactionInformation.errorMsg",
						"A 'TC40 Fraud Reporting' request has already been logged for this transaction");
				}
			}
		}
		else if (form.getAtmPosClaimInformation() != null) {
			validateContactInfoForAtmPos(form, errors);
			validateAtmPOSInformation(
				form.getAtmPosClaimInformation(),
				form.getTransactionInformation(
				).getTransactionAmount(),
				form.getTransactionInformation(
				).getAtmPos(),
				errors);

			if (!form.isDisclaimer()) {
				errors.rejectValue(
					"disclaimer", "service.request.disclaimer.validation");
			}

			if (form.getTransactionInformation(
				).getAtmPos(
				).contains(
					"POS"
				)) {

				if (form.getAtmPosClaimInformation(
					).getRequestPosReason(
					).equals(
						"0"
					)) {

					errors.rejectValue(
						"atmPosClaimInformation.requestPosReason",
						"service.request.pos.reason.empty");
				}

				if (form.getAtmPosClaimInformation(
					).getRequestAttachments(
					).isEmpty()) {

					errors.rejectValue(
						"atmPosClaimInformation.requestAttachments",
						"service.request.attachment.empty");
				}
				else {
					List<MultipartFile> files = form.getAtmPosClaimInformation(
					).getRequestAttachments();

					if (files.get(
							0
						).getSize() == 0) {

						errors.rejectValue(
							"atmPosClaimInformation.requestAttachments",
							"service.request.attachment.empty");
					}
				}
			}
			else {
				if (form.getAtmPosClaimInformation(
					).getRequestAtmReason(
					).equals(
						"0"
					)) {

					errors.rejectValue(
						"atmPosClaimInformation.requestAtmReason",
						"service.request.atm.reason.empty");
				}
			}

			if (!form.getAtmPosClaimInformation(
				).getRequestAttachments(
				).isEmpty()) {

				List<MultipartFile> files = form.getAtmPosClaimInformation(
				).getRequestAttachments();

				for (MultipartFile file : files) {
					if (files.get(
							0
						).getSize() != 0) {

						if ((file.getSize() > 0) &&
							(file.getSize() > 2097152)) {

							errors.rejectValue(
								"atmPosClaimInformation.requestAttachments",
								"service.request.attachment.size");

							break;
						}

						if (!(file.getOriginalFilename(
							).toLowerCase(
							).endsWith(
								".pdf"
							) ||
							  file.getOriginalFilename(
							  ).toLowerCase(
							  ).endsWith(
								  ".jpg"
							  ) ||
							  file.getOriginalFilename(
							  ).toLowerCase(
							  ).endsWith(
								  ".jpeg"
							  ) ||
							  file.getOriginalFilename(
							  ).toLowerCase(
							  ).endsWith(
								  ".tif"
							  ) ||
							  file.getOriginalFilename(
							  ).toLowerCase(
							  ).endsWith(
								  ".gif"
							  ))) {

							errors.rejectValue(
								"atmPosClaimInformation.requestAttachments",
								"service.request.attachment.invalid");

							break;
						}
					}
				}
			}
		}

		//Check if the comment has a clear PAN in it or not.

		if (null != form.getAtmPosClaimInformation()) {
			if (StringUtils.isNotBlank(
					form.getAtmPosClaimInformation(
					).getRequestComments())) {

				if (containsPAN(
						form.getAtmPosClaimInformation(
						).getRequestComments())) {

					errors.rejectValue(
						"atmPosClaimInformation.requestComments",
						"service.request.comment.pan");
				}
			}
		}
		else {
			if (StringUtils.isNotBlank(
					form.getVisaTransactionInformation(
					).getComments())) {

				if (containsPAN(
						form.getVisaTransactionInformation(
						).getComments())) {

					errors.rejectValue(
						"visaTransactionInformation.comments",
						"Please remove the clear PAN from the comment");
				}
			}
		}
	}

	CardUtil cardUtil = CardUtil.getInstance();

	public boolean validateAmount(
		RequestAtmPosClaimInformation form, Errors errors) {

		pattern = Pattern.compile(Constants.REG_EXP_AMOUNT);
		matcher = pattern.matcher(form.getRequestAmountReceived());

		if (!matcher.matches()) {
			errors.rejectValue(
				"atmPosClaimInformation.requestAmountReceived",
				"service.request.amount.invalid");

			return false;
		}

		return true;
	}

	public void validateAtmPOSInformation(
		RequestAtmPosClaimInformation atmPosInfo, String transactionAmount,
		String atmpos, Errors errors) {

		boolean isATMDepositDispute = false;
		String transactionCode = atmPosInfo.getTransactionCode();

		boolean isPOSDepositDispute = false;

		if (Constants.POS.equals(atmpos) &&
			Constants.POS_DEPOSITS_21.equals(transactionCode)) {

			isPOSDepositDispute = true;
		}

		if (Constants.ATM.equals(atmpos) &&
			(Constants.CASH_DEPOSITS_21.equals(transactionCode) ||
			 Constants.CHEQUE_DEPOSITS_24.equals(transactionCode))) {

			isATMDepositDispute = true;
		}

		if (atmPosInfo.getRequestDisputeType(
			).equals(
				"0"
			)) {

			errors.rejectValue(
				"atmPosClaimInformation.requestDisputeType",
				"service.request.dispute.type.empty");
		}
		else if (atmPosInfo.getRequestDisputeType(
				).equals(
					"29"
				)) {

			if (atmPosInfo.isOutstandingDispute()) {
				if (isATMDepositDispute) {
					errors.rejectValue(
						"atmPosClaimInformation.errorMsg",
						"service.request.deposit.dispute.exists");
				}
				else {
					errors.rejectValue(
						"atmPosClaimInformation.errorMsg",
						"service.request.dispute.exists");
				}
			}
		}
		else if (atmPosInfo.getRequestDisputeType(
				).equals(
					"30"
				)) {

			if (isATMDepositDispute) {
				if (atmPosInfo.isOutstandingReinvestigation()) {
					errors.rejectValue(
						"atmPosClaimInformation.errorMsg",
						"service.request.deposit.reinvestigation.exists");
				}
			}

			if (!atmPosInfo.isOutstandingDispute()) {
				if (isATMDepositDispute) {
					errors.rejectValue(
						"atmPosClaimInformation.errorMsg",
						"service.request.deposit.reinvestigation.nodispute");
				}
				else {
					errors.rejectValue(
						"atmPosClaimInformation.errorMsg",
						"service.request.reinvestigation.nodispute");
				}
			}
			else if (!atmPosInfo.isOutstandingClosedDispute()) {
				errors.rejectValue(
					"atmPosClaimInformation.errorMsg",
					"service.request.reinvestigation.nocloseddispute");
			}
		}

		if (StringUtils.isBlank(atmPosInfo.getRequestAmountReceived())) {
			if (isATMDepositDispute) {
				errors.rejectValue(
					"atmPosClaimInformation.requestAmountReceived",
					"service.deposit.amount.empty");
			}
			else {
				errors.rejectValue(
					"atmPosClaimInformation.requestAmountReceived",
					"service.request.amount.empty");
			}
		}
		else {
			boolean valid;

			if (isATMDepositDispute) {
				valid = isValidNegativeAmount(atmPosInfo, errors);
			}
			else {
				valid = validateAmount(atmPosInfo, errors);
			}

			//only validate if not "Amount received more than debited"

			if (valid) {
				BigDecimal atmFee = new BigDecimal(0);
				BigDecimal txAmount = new BigDecimal(0);

				if (StringUtils.isNotBlank(atmPosInfo.getRequestAtmFee()) &&
					!"undefined".equals(atmPosInfo.getRequestAtmFee())) {

					atmFee = Utility.createBigDecimalAmountFromString(
						atmPosInfo.getRequestAtmFee());
				}

				if (StringUtils.isNotBlank(transactionAmount) &&
					!"undefined".equals(transactionAmount)) {

					txAmount = Utility.createBigDecimalAmountFromString(
						transactionAmount);
				}

				BigDecimal amtReceived =
					Utility.createBigDecimalAmountFromString(
						atmPosInfo.getRequestAmountReceived());

				if (isATMDepositDispute) {
					if (amtReceived.compareTo(BigDecimal.ZERO) > 0) {
						errors.rejectValue(
							"atmPosClaimInformation.requestAmountReceived",
							"service.request.deposit.credit.amount.invalid");
					}
				}
				else {
					if (amtReceived.compareTo(txAmount.add(atmFee)) > 0) {
						if (Constants.POS.equals(atmpos)) {
							//for pos withdrawal transaction type, display below error

							if (!isPOSDepositDispute) {
								errors.rejectValue(
									"atmPosClaimInformation.requestAmountReceived",
									"service.request.pos.withdrawal.amount.expected.big",
									new String[] {
										txAmount.add(
											atmFee
										).toString()
									},
									null);
							}
						}
						else {
							//for all atm transaction type, display below error
							errors.rejectValue(
								"atmPosClaimInformation.requestAmountReceived",
								"service.request.amount.big",
								new String[] {
									txAmount.add(
										atmFee
									).toString()
								},
								null);
						}
					}
				}
			}
		}

		//Claim amount cannot be 0

		if ((StringUtils.isNotEmpty(atmPosInfo.getRequestClaimAmount()) &&
			 atmPosInfo.getRequestClaimAmount(
			 ).equals(
				 "0.00"
			 )) ||
			StringUtils.isBlank(atmPosInfo.getRequestClaimAmount())) {

			if (isATMDepositDispute) {
				if (StringUtils.isNotBlank(
						atmPosInfo.getRequestAmountReceived())) {

					errors.rejectValue(
						"atmPosClaimInformation.requestAmountReceived",
						"service.deposit.claim.amount.zero");
				}
			}
			else {
				errors.rejectValue(
					"atmPosClaimInformation.errorMsg",
					"service.request.claim.empty");
			}
		}
	}

	public void validateContactInfoForAtmPos(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getContactInformation(
				).getEmail())) {

			errors.rejectValue(
				"contactInformation.email", "service.request.email.empty");
		}
		else {
			pattern = Pattern.compile(Constants.REG_EXP_EMAIL_PATTERN);
			matcher = pattern.matcher(
				form.getContactInformation(
				).getEmail());

			if (!matcher.matches()) {
				errors.rejectValue(
					"contactInformation.email", "service.request.email.empty");
			}
		}

		if (StringUtils.isBlank(
				form.getContactInformation(
				).getPhoneNumber())) {

			errors.rejectValue(
				"contactInformation.phoneNumber",
				"service.request.phone.invalid");
		}
		else {
			pattern = Pattern.compile(Constants.REG_EXP_PHONE_NUMBER);
			matcher = pattern.matcher(
				form.getContactInformation(
				).getPhoneNumber());

			if (!matcher.matches()) {
				errors.rejectValue(
					"contactInformation.phoneNumber",
					"service.request.phone.invalid");
			}
		}

		if (StringUtils.isBlank(
				form.getContactInformation(
				).getGivenName())) {

			errors.rejectValue(
				"contactInformation.givenName", "service.request.given.empty");
		}

		if (StringUtils.isBlank(
				form.getContactInformation(
				).getSurname())) {

			errors.rejectValue(
				"contactInformation.surname", "service.request.surname.empty");
		}
	}

	public void validateContactInfoForVisa(
		ServiceRequestForm form, Errors errors) {

		if (StringUtils.isBlank(
				form.getContactInformation(
				).getEmail())) {

			errors.rejectValue(
				"contactInformation.email", "Email cannot be empty.");
		}
		else {
			pattern = Pattern.compile(Constants.REG_EXP_EMAIL_PATTERN);
			matcher = pattern.matcher(
				form.getContactInformation(
				).getEmail());

			if (!matcher.matches()) {
				errors.rejectValue(
					"contactInformation.email.invalid",
					"Please enter a valid email address.");
			}
		}

		if (StringUtils.isBlank(
				form.getContactInformation(
				).getPhoneNumber())) {

			errors.rejectValue(
				"contactInformation.phoneNumber",
				"Please enter a valid phone no.");
		}
		else {
			pattern = Pattern.compile(Constants.REG_EXP_PHONE_NUMBER);
			matcher = pattern.matcher(
				form.getContactInformation(
				).getPhoneNumber());

			if (!matcher.matches()) {
				errors.rejectValue(
					"contactInformation.phoneNumber",
					"Please enter a valid phone number");
			}
		}

		ValidationUtils.rejectIfEmpty(
			errors, "contactInformation.givenName",
			"Please enter a first name.");
		ValidationUtils.rejectIfEmpty(
			errors, "contactInformation.surname", "Please enter a last name.");
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validateEmail(ServiceRequestForm form, Errors errors) {
		pattern = Pattern.compile(Constants.REG_EXP_EMAIL_PATTERN);
		matcher = pattern.matcher(
			form.getContactInformation(
			).getEmail());

		if (!matcher.matches()) {
			errors.rejectValue(
				"visa.contactInformation.email.invalid",
				"Please enter a valid email address.");
			errors.rejectValue(
				"contactInformation.email", "service.request.email.empty");
		}
	}

	/**
	 *
	 * @param form
	 * @param errors
	 */
	public void validatePhoneNumber(ServiceRequestForm form, Errors errors) {
		pattern = Pattern.compile(Constants.REG_EXP_PHONE_NUMBER);
		matcher = pattern.matcher(
			form.getContactInformation(
			).getPhoneNumber());

		if (!matcher.matches()) {
			errors.rejectValue(
				"contactInformation.phoneNumber.invalid",
				"Please enter a valid phone number");
			errors.rejectValue(
				"contactInformation.phoneNumber",
				"service.request.phone.invalid");
		}
	}

	public void validateVisaInformation(
		RequestAdditionalTransactionInformation visaInfo, Errors errors) {

		if (visaInfo.getRequestType(
			).equals(
				"0"
			)) {

			errors.rejectValue(
				"visaTransactionInformation.requestType",
				"Please select a Request Type");
		}
		else {
			if (visaInfo.getRequestType(
				).equals(
					"1"
				)) { // tc40

				if (visaInfo.getFraudType(
					).equals(
						"0"
					)) {

					errors.rejectValue(
						"visaTransactionInformation.fraudType",
						"Please select a Fraud Type");
				}

				if (visaInfo.getFraudNotificationCode(
					).equals(
						"0"
					)) {

					errors.rejectValue(
						"visaTransactionInformation.fraudNotificationCode",
						"Please select a Notification Code");
				}

				if (visaInfo.getDetection(
					).equals(
						"0"
					)) {

					errors.rejectValue(
						"visaTransactionInformation.detection",
						"Please select a Detected By");
				}
			}
			else if (visaInfo.getRequestType(
					).equals(
						"2"
					)) { // TC 52

				if (visaInfo.getVoucherReason(
					).equals(
						"0"
					)) {

					errors.rejectValue(
						"visaTransactionInformation.voucherReason",
						"Please select a Voucher Reason");
				}
			}
		}
	}

	/**
	 * Check the note if it contains a PAN.
	 *
	 * @param 	txt
	 * @return	Boolean
	 */
	private Boolean containsPAN(String txt) {
		if (StringUtils.isBlank(txt) || (txt.length() < 13)) {
			return false;
		}

		/***************
		 * Explanation of pattern
		 *
		 * (4(?:\\d([ -]?)*?){13,19}) 									- Validate Visa cards. They start with 4 and are 13-19 characters in length.
		 * (5[1-5](?:\\d([ -]?)*?){13,19}) 								- Validate Mastercard cards. They start with 5 and are 16 characters in length.
		 * (3[47](?:\\d([ -])?){13}) 									- Validate AMEX cards. They start with 3 and are 15 characters in length.
		 * (3(?:0[0-5]|[68][\\d])(?:\\d([ -]?)){11}) 					- Validate Diners club cards. They start with 3 and are 14 characters in length.
		 * ((?:2131|1800|35\\d{2})((?:([ -]?)\\d){11}|([ -]?)\\d){12}) 	- Validate JCB cards. Cards that start with 35 are 16 characters in length and the ones that start with 2131 or 1800 are 15 characters in length.
		 * (6(?:011|5\\d{2})(?:([ -]?)\\d){12}) 						- Validate Discover cards. Cards begin with 6011 or 65 and are always 16 characters in length.
		 ***/
		String pattern =
			"(4(?:\\d([ -]?)*?){13,19})|(5[1-5](?:\\d([ -]?)*?){13,19})|(3[47](?:\\d([ -])?){13})|(3(?:0[0-5]|[68][\\d])(?:\\d([ -]?)){11})|((?:2131|1800|35\\d{2})((?:([ -]?)\\d){11}|([ -]?)\\d){12})|(6(?:011|5\\d{2})(?:([ -]?)\\d){12})";

		Pattern p = Pattern.compile(pattern);

		Matcher m = p.matcher(txt);
		Boolean result = Boolean.FALSE;

		while (m.find() && !result) {
			String number = txt.substring(m.start(), m.end());

			number = number.replaceAll(
				Constants.DASH, ""
			).replaceAll(
				Constants.SPACE, ""
			);
			result = cardUtil.panValidate(number);
		}

		return result;
	}

	private Matcher matcher;
	private Pattern pattern;

	@Autowired
	@Qualifier("transactionAppProperties")
	private TransactionAppProperties transactionAppProperties;

}