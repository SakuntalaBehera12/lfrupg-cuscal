package au.com.cuscal.chargeback.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AbstractServiceRequestValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {

		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
	}

}