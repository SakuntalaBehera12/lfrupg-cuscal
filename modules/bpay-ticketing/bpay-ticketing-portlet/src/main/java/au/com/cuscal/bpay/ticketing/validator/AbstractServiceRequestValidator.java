package au.com.cuscal.bpay.ticketing.validator;

import au.com.cuscal.bpay.ticketing.common.Constants;
import au.com.cuscal.bpay.ticketing.forms.ServiceRequestForm;
import au.com.cuscal.framework.cards.CardUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * BPay abstract Service Request Validator base class.
 *
 *
 */
public class AbstractServiceRequestValidator implements Validator {

	public Boolean containsPAN(String text) {
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

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
	}

	public void validateDisclaimer(ServiceRequestForm form, Errors errors) {
		if (!form.isDisclaimer()) {
			errors.rejectValue(
				"disclaimer", "service.request.disclaimer.validation");
		}
	}

	public void validateOfiComment(ServiceRequestForm form, Errors errors) {
		if (StringUtils.isNotBlank(
				form.getAttributesInformation(
				).getOfiComment())) {

			if (containsPAN(
					form.getAttributesInformation(
					).getComment())) {

				errors.rejectValue(
					"attributesInformation.ofiComment",
					"service.request.comment.clear.pan");
			}
		}
	}

	protected String removeDecimal(String amount) {
		if (StringUtils.isNotBlank(amount)) {
			amount = amount.replaceAll("\\.", "");
		}

		return amount;
	}

	private CardUtil cardUtil = CardUtil.getInstance();

}