//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.validator;

import au.com.cuscal.ticketing.common.Constants;
import au.com.cuscal.ticketing.domain.Ticket;
import au.com.cuscal.ticketing.domain.TicketUser;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component(Constants.TICKET_VALIDATOR)
public class TicketValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Ticket.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		logger.debug("validate - start");

		Ticket ticket = (Ticket)target;

		TicketUser user = ticket.getUser();

		if (StringUtils.isBlank(user.getPhoneNumber())) {
			errors.rejectValue("user.phoneNumber", "ticket.phone.empty");
		}

		if (StringUtils.isBlank(ticket.getTicketCategory())) {
			errors.rejectValue("ticketCategory", "ticket.category.empty");
		}

		if (StringUtils.isBlank(ticket.getProduct())) {
			errors.rejectValue("product", "ticket.product.empty");
		}

		if (StringUtils.isBlank(ticket.getTicketType())) {
			errors.rejectValue("ticketType", "ticket.type.empty");
		}

		if (StringUtils.isBlank(ticket.getTicketDescription())) {
			errors.rejectValue("ticketDescription", "ticket.description.empty");
		}
	}

	private static Logger logger = Logger.getLogger(TicketValidator.class);

}