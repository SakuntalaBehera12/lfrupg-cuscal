package au.com.cuscal.bpay.ticketing.common;

/**
 * Contains all constants used by BPay module.
 *
 *
 */
public interface Constants {

	public static final String _RETURN_URL = "_RETURN_URL";

	public static final String _USER_STEPPEDUP = "_USER_STEPPEDUP";

	public static final String ACTION = "action";

	public static final String ALL = "all";

	public static final String ALL_ORGANISATIONS = "ALL_ORGANISATIONS";

	public static final String BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE =
		"5";

	public static final String
		BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE_AND_FRAUD_TYPE_OTHER =
			"4";

	public static final String
		BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE_AND_FRAUD_TYPE_VICTIM_OF_SCAM =
			"3";

	public static final String
		BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE_AND_SCAM_TYPE_OTHER =
			"8";

	public static final String BPAY_EC_REQUEST_REASON_WRONG_BILLER_CODE = "3";

	public static final String BPAY_EC_REQUEST_REASON_WRONG_CRN = "2";

	public static final String BPAY_EMPTY_ACTION = "emptyAction";

	public static final String BPAY_INVESTIGATION_ENQUIRY = "1";

	public static final String BPAY_INVESTIGATION_ERROR_CORRECTION = "2";

	public static final String BPAY_INVESTIGATION_FORM_ACTION =
		"bpayInvestigationFormAction";

	public static final String BPAY_INVESTIGATION_FORM_PAGE =
		"bpayInvestigationFormPage";

	public static final String BPAY_LIST_BOX_DATA = "bPayListBoxData";

	public static final String CANCEL = "cancel";

	public static final String CC_PATTERN =
		"(4(?:\\d([ -]?)*?){13,19})|(5[1-5](?:\\d([ -]?)*?){13,19})|(3[47](?:\\d([ -])?){13})|(3(?:0[0-5]|[68][\\d])(?:\\d([ -]?)){11})|((?:2131|1800|35\\d{2})((?:([ -]?)\\d){11}|([ -]?)\\d){12})|(6(?:011|5\\d{2})(?:([ -]?)\\d){12})";

	public static final String CHARGEBACK_PAGE = "form";

	public static final String CUSCAL_BSB = "cuscalbsb";

	public static final String CUSCAL_TICKETING_SERVICE =
		"cuscalTicketingService";

	public static final String DASH = "-";

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	public static final int DEFAULT_PAGE_NUMBER = 0;

	public static final int DEFAULT_PAGE_SIZE = 0;

	public static final String DESCRIPTION = "description";

	public static final String DOT = ".";

	public static final String EQUALS = "=";

	public static final String ERROR = "error";
	//	public static final String OFI_UPDATE_NOTE = "Response has been submitted by {0} on {1}";
	public static final String ERROR_CORRECTION_FORM_ACTION =
		"errorCorrectionFormAction";

	//Validation related constants
	public static final String ERROR_PAGE = "error";

	public static final String ERRORS = null;

	public static final String FALSE = "false";

	/*  This section will house BPay specific constants. */
	public static final String FORM_ID = "formId";

	public static final String FORM_ID_CLIENT_INITIATED_BPAY = "1";

	public static final String FORM_ID_OFI_BPAY = "2";

	public static final String FORM_ID_OFI_BPAY_INVESTIGATION = "2.1";

	public static final String FORM_ID_OFI_ERROR_CORRECTION = "2.2";

	public static final String HOMEPAGE = "home";

	public static final String I = "i";

	public static final String ID = "id";

	public static final String IS_FA = "isFa";

	public static final String OFI_BPAY_INVESTIGATION_FORM_PAGE =
		"ofiBpayInvestigationFormPage";

	public static final String OFI_ERROR_CORRECTION_FORM_PAGE =
		"ofiErrorCorrectionFormPage";

	/* This section will house Service related constants. */
	public static final String OFI_UPDATE_NOTE =
		"Response has been submitted by {0}";

	public static final String ORG_SHORT_NAME = "orgShortName";

	public static final String PAYMENT_INVESTIGATION_FORM_ACTION =
		"paymentInvestigationFormAction";

	public static final String PROPERTIES_PREFIX_BPAY_EC_REQUEST_REASON =
		"bpay.request.ec.reason";

	public static final String PROPERTIES_PREFIX_BPAY_ENTRY_METHOD =
		"bpay.entry.method";

	public static final String PROPERTIES_PREFIX_BPAY_FRAUD_TOOL_TIP =
		"bpay.request.fraudtype.tooltip";

	public static final String PROPERTIES_PREFIX_BPAY_FRAUD_TYPE =
		"bpay.request.ec.fraudtype";

	public static final String PROPERTIES_PREFIX_BPAY_INVESTIGATION_TYPE =
		"bpay.investigation.type";

	public static final String PROPERTIES_PREFIX_BPAY_PAYMENT_METHOD =
		"bpay.payment.method";

	public static final String PROPERTIES_PREFIX_BPAY_PI_REQUEST_REASON =
		"bpay.request.pi.reason";

	public static final String PROPERTIES_PREFIX_BPAY_SCAM_TOOL_TIP =
		"bpay.request.scamtype.tooltip";

	public static final String PROPERTIES_PREFIX_BPAY_SCAM_TYPE =
		"bpay.request.ec.scamtype";

	public static final String REG_EXP_AMOUNT =
		"^(?:\\$\\s*)?(?:(?:\\d{0,3}(?:[, ]\\d{0,3})*[, ])+\\d{3}|\\d+)(?:\\.\\d{2})?(?:\\s*\\$)?$";

	public static final String REG_EXP_EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String REG_EXP_PHONE_NUMBER = "[0-9\\(\\)\\/\\+ \\-]*";

	public static final String RENDER = "render";

	public static final String REQUEST_TYPE_OFI_BPAY_INVESTIGATION =
		"ofi.bpay.investigation";

	public static final String REQUEST_TYPE_OFI_ERROR_CORRECTION =
		"ofi.bpay.error.correction";

	public static final String REQUEST_TYPE_TYPE_ID_CI = "bpay.type.id";

	public static final String REQUEST_TYPE_TYPE_ID_OFI = "ofi.bpay.type.id";

	public static final String ROLE_FA = "2FA";

	public static final String SESSION_TIMEOUT = "sessionTimeout";

	public static final String SPACE = " ";

	public static final String STEP_UP_ACTION = "stepUp";

	public static final String SUBMIT_BPAY_INVESTIGATION_ACTION =
		"submitBpayInvestigationAction";

	public static final String SUBMITTED = "Submitted";

	/* 2FA */
	public static final String SUCCESS = "success";

	public static final String TICKET_ID = "ticketId";

	public static final String TICKET_NUMBER = "ticketNumber";

	public static final String TRUE = "true";

	public static final String TX_ATM_POS_SUBMIT_RENDER = null;

	public static final String TX_RENDER = null;

	public static final String UPDATE_BPAY_INVESTIGATION_ACTION =
		"updateBpayInvestigationAction";

	public static final String UPDATE_OFI_BPAY_INVESTIGATION_ACTION =
		"updateOfiBpayInvestigationAction";

	public static final String UPDATE_OFI_ERROR_CORRECTION_ACTION =
		"updateOfiErrorCorrectionAction";

	public static final String UPDATE_TICKET = "updateTicket";

	public static final String USER_CANCEL = "USER_CANCEL";

}