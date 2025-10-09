package au.com.cuscal.ticketing.validator;

import au.com.cuscal.ticketing.common.Constants;
import au.com.cuscal.ticketing.forms.ServiceCatalogueForm;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component(Constants.SERVICE_CATALOGUE_VALIDATOR)
public class ServiceCatalogueValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ServiceCatalogueForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		logger.debug("validate - start");

		ValidationUtils.rejectIfEmpty(
			errors, "scItem", "service.catalouge.list.empty");
	}

	private static Logger logger = Logger.getLogger(
		ServiceCatalogueValidator.class);

}