//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.validator;

import au.com.cuscal.framework.cards.CardUtil;
import au.com.cuscal.ticketing.common.Constants;
import au.com.cuscal.ticketing.domain.TicketNote;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component(Constants.TICKET_NOTE_VALIDATOR)
public class TicketNoteValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return TicketNote.class.equals(clazz);
	}

	CardUtil cardUtil = CardUtil.getInstance();

	@Override
	public void validate(Object target, Errors errors) {
		TicketNote ticketNote = (TicketNote)target;

		if (StringUtils.isBlank(ticketNote.getNote())) {
			errors.rejectValue("note", "ticket.note.empty");
		}
		else {
			if (containsPAN(ticketNote.getNote())) {
				errors.rejectValue("note", "ticket.note.clear.pan");
			}
		}
	}

	public Map<String, String> validateCancelNote(
		Map<String, String> protectedAttributes, String cancelNote) {

		if (StringUtils.isBlank(cancelNote)) {
			protectedAttributes.put("error", "Please enter a comment.");
		}
		else {
			if (containsPAN(cancelNote)) {
				protectedAttributes.put(
					"error", "Please remove the clear PAN from the comment.");
			}
		}

		return protectedAttributes;
	}

	/**
	 * Check the note if it contains a PAN.
	 *
	 * @param 	txt
	 * @return	Boolean
	 */
	private Boolean containsPAN(String txt) {
		//Return false if the string is less than 14 characters in length.

		if (StringUtils.isBlank(txt) || (txt.length() < 14)) {
			return false;
		}

		Pattern p = Pattern.compile(Constants.CC_PATTERN);

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

}