//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.validator;

import au.com.cuscal.de.ticketing.common.Constants;
import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("ofiDEClaimValidator")
public class OfiDEClaimValidator extends AbstractServiceRequestValidator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return ServiceRequestForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		OfiDEClaimValidator.logger.debug((Object)"validate - start");
		final ServiceRequestForm serviceRequestForm =
			(ServiceRequestForm)target;

		if (serviceRequestForm.getContactInformation() != null) {
		}

		if (serviceRequestForm.getRequestTypeInformation() != null) {
		}

		if (serviceRequestForm.getAttributesInformation() != null) {
			this.validateOfiClaimResult(serviceRequestForm, errors);
			this.validateOfiAdditionalInfo(serviceRequestForm, errors);
		}

		this.validateDisclaimer(serviceRequestForm, errors);
		OfiDEClaimValidator.logger.debug((Object)"validate - end");
	}

	public void validateDrawingAmount(
		final ServiceRequestForm form, final Errors errors) {

		final String amount = form.getAttributesInformation(
		).getOfiDrawingAmount();

		if (StringUtils.isBlank((CharSequence)amount)) {
			errors.rejectValue(
				"attributesInformation.ofiDrawingAmount",
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
					"attributesInformation.ofiDrawingAmount",
					"service.request.amount.invalid");
			}
		}
	}

	public void validateOfiClaimResult(
		final ServiceRequestForm form, final Errors errors) {

		if (StringUtils.isBlank(
				(CharSequence)form.getAttributesInformation(
				).getOfiClaimResult())) {

			errors.rejectValue(
				"attributesInformation.ofiClaimResult",
				"service.request.ofi.result.empty");
		}
		else if ("1".equals(
					form.getAttributesInformation(
					).getOfiClaimResult())) {

			this.validateDrawingAmount(form, errors);
		}
	}

	private static Logger logger;

	static {
		OfiDEClaimValidator.logger = Logger.getLogger(
			(Class)OfiDEClaimValidator.class);
	}

}