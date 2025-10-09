package au.com.cuscal.chargeback.common;

import java.util.Arrays;
import java.util.List;

public interface Constants {

	public static final String ACTION = "action";

	public static final List<String> CANCELLATION_WITHOUT_CREDIT_INFO =
		Arrays.asList("66", "67");

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
	public static final String CC_PATTERN =
		"(4(?:\\d([ -]?)*?){13,19})|(5[1-5](?:\\d([ -]?)*?){13,19})|(3[47](?:\\d([ -])?){13})|(3(?:0[0-5]|[68][\\d])(?:\\d([ -]?)){11})|((?:2131|1800|35\\d{2})((?:([ -]?)\\d){11}|([ -]?)\\d){12})|(6(?:011|5\\d{2})(?:([ -]?)\\d){12})";

	public static final String CHARGEBACK_PAGE = "form";

	public static final String CHARGEBACK_REDIRECT = "chargeBackRedirect";

	public static final long CHARGEBACK_TYPE_ID = 101;

	public static final String CHARGEBACK_UPDATE_NOTE =
		"Visa Chargeback was updated by ";

	public static final List<String> CHARGEBACK_WITHOUT_CERTIFICATIONS =
		Arrays.asList(
			"54", "55", "56", "57", "58", "59", "60", "62", "63", "64", "65",
			"66", "69", "70");

	public static final List<String> CHARGEBACK_WITHOUT_ELABORATIONS =
		Arrays.asList("52", "54", "59", "66", "70");

	public static final String CHECKBOX_SELECT_ALL_VALUE = "all";

	public static final String CONFIRM = "confirm";

	public static final String DASH = "-";

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	public static final String DATE_FORMAT_LONG = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	public static final String DATE_FORMAT_SHORT = "dd/MM/yyyy";

	public static final String DATE_TIME_FORMAT_24HR = "dd/MM/yyyy HH:mm";

	public static final String EQUALS = "=";

	public static final String ERROR_PAGE = "error";

	public static final String ERRORS = "errors";

	public static final long GENERIC_PRODUCT_ID = 1;

	public static final String HOMEPAGE = "home";

	public static final String ORG_SHORT_NAME = "orgShortName";

	public static final String REASON_AUTHORISATION = "46";

	public static final String REASON_CANCELLED_RETURNED = "48";

	public static final String REASON_FRAUD = "45";

	public static final String REASON_NON_RECEIPT_GOODS = "49";

	public static final String REASON_PROCESSING_ERROR = "47";

	public static final String REASON_REQUEST_FOR_INFORMATION = "44";

	public static final String RECORD_NOT_FOUND = "RECORD_NOT_FOUND";

	public static final String RENDER = "render";

	public static final String SPACE = " ";

	public static final String STATUS_SUCCESS = "SUCCESS";

	public static final String SUBMIT_CHARGEBACK = "SUBMIT_CHARGEBACK";

	public static final String SUBMITTED = "Submitted";

	public static final String TICKET_DETAILS_RENDER = "ticketDetails";

	public static final String TICKETING_PORTLET_NAME =
		"cuscalticketingportlet_WAR_cuscalticketingportlet";

	public static final String TRANSACTION_BUSINESS_DATE = "businessDate";

	public static final String TRANSACTION_DETAIL = "transaction";

	public static final String TRANSACTION_DETAIL_BYTES = "transactionDetail";

	public static final String TRANSACTION_ID = "transactionId";

	public static final String UNSELECTED_REASON = "0";

	public static final String UPDATE_CHARGEBACK = "UPDATE_CHARGEBACK";

}