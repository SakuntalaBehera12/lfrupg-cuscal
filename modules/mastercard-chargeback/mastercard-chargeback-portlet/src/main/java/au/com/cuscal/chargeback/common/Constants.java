//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.common;

public interface Constants {

	public static final String ACTION = "action";

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

	public static final long CHARGEBACK_TYPE_ID = 101L;

	public static final String CHARGEBACK_UPDATE_NOTE =
		"MasterCard Chargeback was updated by ";

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

	public static final String FLAG_ONE = "1";

	public static final String FLAG_TRUE = "true";

	public static final long GENERIC_PRODUCT_ID = 1L;

	public static final String HOMEPAGE = "home";

	public static final String
		MASTERCARD_ARBITRATION_CHARGEBACK_ATTRIBUTES_LIST_ID = "891";

	public static final long MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID =
		4228847L;

	public static final String
		MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID_STR =
			"mastercard.dispute.arbitration.chargeback.type.id";

	public static final long MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID =
		4228838L;

	public static final String MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID_STR =
		"mastercard.dispute.first.chargeback.type.id";

	public static final long MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID = 4228853L;

	public static final String MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID_STR =
		"mastercard.dispute.report.fraud.type.id";

	public static final long MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID =
		4228841L;

	public static final String
		MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID_STR =
			"mastercard.dispute.retrieval.request.type.id";

	public static final String MASTERCARD_FIRST_CHARGEBACK_ATTRIBUTES_LIST_ID =
		"890";

	public static final String
		MASTERCARD_FIRST_PRESENTMENT_FUNCTION_CODE_DESCRIPTION =
			"First Presentment";

	public static final String
		MASTERCARD_PRESENTMENT_MESSAGE_TYPE_TRANSACTION_LOG = "0220";

	public static final String MASTERCARD_REPORT_FRAUD_ATTRIBUTES_LIST_ID =
		"946";

	public static final String MASTERCARD_RETRIEVAL_REQUEST_ATTRIBUTES_LIST_ID =
		"889";

	public static final String
		MASTERCARD_SECOND_PRESENTMENT_FULL_FUNCTION_CODE_DESCRIPTION =
			"Second Presentment";

	public static final String
		MASTERCARD_SECOND_PRESENTMENT_PARTIAL_FUNCTION_CODE_DESCRIPTION =
			"Second Presentment (partial)";

	public static final String ORG_SHORT_NAME = "orgShortName";

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

	public static final String TX_RESP = "respList";

	public static final String UCAF_COLLECTION_INDICATOR_ATTEMPTED_1 =
		"Authentication attempted";

	public static final String UCAF_COLLECTION_INDICATOR_AUTHENTICATED_2 =
		"Cardholder authenticated";

	public static final String UCAF_COLLECTION_INDICATOR_NOT_SUPPORTED_0 =
		"Not Supported";

	public static final String UNSELECTED_REASON = "0";

	public static final String UPDATE_CHARGEBACK = "UPDATE_CHARGEBACK";

}