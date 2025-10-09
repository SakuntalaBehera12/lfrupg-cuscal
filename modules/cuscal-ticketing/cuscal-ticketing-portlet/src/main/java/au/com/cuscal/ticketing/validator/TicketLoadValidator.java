//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.validator;

import au.com.cuscal.ticketing.common.Constants;
import au.com.cuscal.ticketing.domain.TicketFilter;

import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component(Constants.TICKET_LOAD_VALIDATOR)
public class TicketLoadValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return TicketFilter.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TicketFilter filter = (TicketFilter)target;

		if (StringUtils.isBlank(filter.getTicketNumber())) {
			errors.rejectValue("ticketNumber", "ticket.quick.load.empty");
		}
	}

}