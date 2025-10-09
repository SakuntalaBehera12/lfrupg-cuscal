//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AbstractServiceRequestValidator implements Validator {

	public boolean supports(final Class<?> clazz) {
		return false;
	}

	public void validate(final Object target, final Errors errors) {
	}

}