//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.validator;

import au.com.cuscal.ticketing.common.Constants;
import au.com.cuscal.ticketing.domain.TicketFilter;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component(Constants.TICKET_FILTER_VALIDATOR)
public class TicketFilterValidator implements Validator {

	public Map<String, String> getProductList() {
		return productList;
	}

	public Map<String, String> getSubmittedBy() {
		return submittedBy;
	}

	public Map<Integer, String> getSubmittedWithin() {
		return submittedWithin;
	}

	public Map<String, String> getTicketCategoryMap() {
		return ticketCategoryMap;
	}

	public Map<Integer, String> getUpdatedWithin() {
		return updatedWithin;
	}

	public void setProductList(Map<String, String> productList) {
		this.productList = productList;
	}

	public void setSubmittedBy(Map<String, String> submittedBy) {
		this.submittedBy = submittedBy;
	}

	public void setSubmittedWithin(Map<Integer, String> submittedWithin) {
		this.submittedWithin = submittedWithin;
	}

	/* Getters and Setters */
	public void setTicketCategoryMap(Map<String, String> ticketCategoryMap) {
		this.ticketCategoryMap = ticketCategoryMap;
	}

	public void setUpdatedWithin(Map<Integer, String> updatedWithin) {
		this.updatedWithin = updatedWithin;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return TicketFilter.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		logger.debug("validate - start");

		TicketFilter filter = (TicketFilter)target;

		try {
			logger.debug("convert to Integer: " + filter.getUpdatedWithin());
			Integer updated = Integer.parseInt(filter.getUpdatedWithin());

			logger.debug("after conversion: " + updated);

			logger.debug(
				"Value from map: <" + updatedWithin.get(updated) + ">");

			if (StringUtils.isBlank(updatedWithin.get(updated))) {
				errors.rejectValue(
					"submittedBy", "ticket.filter.submittedBy.invalid");
			}
		}
		catch (Exception e) {
			errors.rejectValue(
				"submittedBy", "ticket.filter.submittedBy.invalid");
		}

		logger.debug("validate - end");
	}

	private static Logger logger = Logger.getLogger(
		TicketFilterValidator.class);

	private Map<String, String> productList;
	private Map<String, String> submittedBy;
	private Map<Integer, String> submittedWithin;
	private Map<String, String> ticketCategoryMap;
	private Map<Integer, String> updatedWithin;

}