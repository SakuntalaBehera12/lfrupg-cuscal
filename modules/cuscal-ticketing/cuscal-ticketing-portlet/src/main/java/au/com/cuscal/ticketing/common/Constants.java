//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.common;

public class Constants {

	public static final String _RETURN_URL = "_RETURN_URL";

	public static final String _USER_STEPPEDUP = "_USER_STEPPEDUP";

	public static final String ACTION = "action";

	public static final String ACTION_STATUS_ID = "6";

	public static final String ADD_ATTACHMENT_ACTION = "attachFile";

	public static final String ADD_NOTE_ACTION = "addNote";

	public static final String ADD_NOTE_SUCCESS_RENDER = "addNoteSuccess";

	public static final String ADD_TICKET_ACTION = "addTicket";

	public static final String ADD_TICKET_PAGE = "add";

	public static final String ADD_TICKET_RENDER = "addTicketPage";

	public static final String ADD_TICKET_SUCCESS_PAGE = "add-success";

	public static final String ADD_TICKET_SUCCESS_RENDER = "addTicketSuccess";

	public static final String ALL = "all";

	public static final String ALL_ORGANISATIONS = "ALL_ORGANISATIONS";

	public static final String ATTACHMENT_ID = "attachmentId";

	public static final String ATTACHMENT_LOOKUP =
		"service.request.role.attachment";

	public static final String ATTACHMENT_ROLE = "attachmentRole";
	//	public static final String CHARGEBACK = "Dispute Management/VISA Chargeback/Lodgement";
	public static final String ATTRIBUTES_TYPE_SERVICE_REQUEST_STATUS =
		"SERVICE.REQUEST.STATUS";

	public static final String CAN_ATTACH_AFTER_CLOSE = "attachAfterClose";

	public static final String CANCEL = "cancel";

	public static final String CANCEL_NOTE = "cancelNote";

	public static final String CANCEL_REQUEST = "cancelRequest";

	public static final String CANCEL_REQUEST_FLAG = "CancelRequestFlag";

	public static final String CANCEL_THIS_REQUEST = "cancelThisRequest";

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

	public static final String CHARGEBACK_CANCEL_NOTE =
		"MasterCard Chargeback was cancelled by ";

	public static final long CHARGEBACK_ID = 101;

	//properties
	public static final String CHARGEBACK_LINK = "chargebackLink";

	public static final String CHARGEBACK_REVERSE_NOTE =
		"MasterCard Chargeback was reversed by ";

	public static final String CLOSED = "CLOSED";

	public static final String COMMA = ",";

	public static final String CUSCAL = "cuscal";

	public static final String CUSCAL_ORG_DEFAULT_NAME =
		"cuscal.org.default.name";

	public static final String CUSCAL_ORG_SHORT_NAMES =
		"cuscal.org.short.names";

	/* Render/Action param values */
	public static final String CUSCAL_TICKETING_SERVICE =
		"cuscalTicketingService";

	public static final String DASH = "-";

	public static final String DOT = ".";

	public static final String DOWNLOAD_ATTACHMENT = "downloadFile";

	public static final String EDIT_REQUEST_FLAG = "EditRequestFlag";

	public static final String EDIT_TICKET = "editTicket";

	public static final String EQUALS = "=";

	public static final String ERROR = "error";

	public static final String ERROR_PAGE = "error";

	public static final String ERROR_PAGE_RENDER = "errorPage";

	public static final String EXPORT_IPM = "exportIPM";

	public static final String FALSE = "false";

	public static final String HOMEPAGE = "home";

	public static final String I = "i";

	/* JSP file names */
	public static final String IS_A_MASTERCARD_DISPUTE = "IsAMasterCardDispute";

	public static final String IS_FA = "isFa";

	public static final String LOAD_TICKET_RENDER = "loadTicket";

	public static final String MASTERCARD_DISPUTE = "MasterCard Dispute";

	public static final String
		MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID =
			"mastercard.dispute.arbitration.chargeback.type.id";

	/* 2FA */
	public static final String MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID =
		"mastercard.dispute.first.chargeback.type.id";

	public static final String MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID =
		"mastercard.dispute.report.fraud.type.id";

	public static final String MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID =
		"mastercard.dispute.retrieval.request.type.id";

	public static final String MAX_FILE_SIZE = "max.file.attachment.size";

	public static final String NOTES_LOOKUP = "service.request.role.note";

	public static final String NOTES_ROLE = "notesRole";

	public static final String OF = "of";

	public static final String ON_HOLD_STATUS = "On hold";

	public static final String PROPERTIES_PREFIX_SERVICE_REQUEST_CATALOGUE =
		"service.request.catalogue";

	public static final String PROPERTIES_PREFIX_SERVICE_REQUEST_CATALOGUE_OFI =
		"service.request.catalogue.ofi";

	public static final String PROPERTIES_SUFFIX_DESCRIPTION = "description";

	/* Upload */
	public static final String PROPERTIES_SUFFIX_ID = "id";

	public static final String PROPERTIES_SUFFIX_TYPE = "type";

	/* Roles*/
	public static final String PROPERTIES_SUFFIX_TYPE_ID = "type.id";

	public static final String PROPERTIES_SUFFIX_URL = "url";

	public static final String RE_OPENED = "Re-Opened";

	public static final String RECORD_NOT_FOUND = "RECORD_NOT_FOUND";

	public static final String RENDER = "render";

	public static final String REQUEST_HEADER = "header";

	//adding for SC
	public static final String REVERSE_REQUEST_FLAG = "ReverseRequestFlag";

	public static final String REVERSE_THIS_REQUEST = "reverseThisRequest";

	public static final String ROLE_FA = "2FA";

	public static final String SC_ITEM_ID = "scItemId";

	public static final String SELF_SERVICE_URI_LOOKUP = "self.service.uri";

	/* Service Catalogue related constants. */
	public static final String SERVICE_CATALOGUE_VALIDATOR =
		"serviceCatalogueValidator";

	public static final String SERVICE_REQUEST_STATUS_CANCELLED = "CANCELLED";

	public static final String SERVICE_REQUEST_STATUS_REVERSED = "REVERSED";

	public static final String SLASH = "/";

	public static final String SPACE = " ";

	public static final long STATUS_CLOSED = 3l;

	public static final long STATUS_ON_HOLD = 5l;

	public static final String STEP_UP_ACTION = "stepUp";

	public static final String SUBMIT_SC_ACTION = "submitScAction";

	public static final String SUBMITTED = "Submitted";

	public static final String SUCCESS = "SUCCESS";

	public static final String TICKET = "ticket";

	public static final String TICKET_DETAILS_COMMAND = "ticketDetailsCommand";

	public static final String TICKET_DETAILS_PAGE = "details";

	public static final String TICKET_DETAILS_RENDER = "ticketDetails";

	public static final String TICKET_EXPOERT_IPM_STATUS_PROCESSED = "P";

	public static final String TICKET_FILE_SIZE = "ticket.file.size";
	//cancel this request
	public static final String TICKET_FILTER = "ticketFilter";

	public static final String TICKET_FILTER_ACTION = "ticketFilterAction";

	public static final String TICKET_FILTER_RENDER = "ticketFilterRender";

	public static final String TICKET_FILTER_VALIDATOR =
		"ticketFilterValidator";

	public static final String TICKET_ID = "ticketId";

	public static final String TICKET_LOAD_VALIDATOR = "ticketLoadValidator";

	public static final String TICKET_NOTE = "ticketNote";

	public static final String TICKET_NOTE_VALIDATOR = "ticketNoteValidator";

	public static final String TICKET_VALIDATOR = "ticketValidator";

	public static final String TRUE = "true";

	public static final String TYPE_ID = "typeId";

	public static final String UPDATE_OFI_SC_ACTION = "updateOfiScAction";

	public static final String UPDATE_SC_ACTION = "updateScAction";

	public static final String UPLOAD_INVALID = "fileInvalid";

	public static final String UPLOAD_TOO_BIG = "fileTooBig";

	public static final String USER_CANCEL = "USER_CANCEL";

	public static final String VISA_CHARGEBACK = "Visa Chargeback";

}