package au.com.cuscal.transactions.commons;

/**
 * Constants for Transaction Search View
 *
 * Transaction , CUD and Quick Search Portlet constants
 *
 * @author Rajni Bharara
 *
 */
public interface Constants {

	// Controller

	public static final String _2FA_ACTION = "2faAction";

	public static final String _RETURN_URL = "_RETURN_URL";

	// Services

	public static final String _USER_STEPPEDUP = "_USER_STEPPEDUP";

	public static final String ACQ_ISO_CODE_END_TAG = "</ACQ_ISO_CODE>";

	public static final String ACQ_ISO_CODE_START_TAG = "<ACQ_ISO_CODE>";

	public static final String ACTION = "action";

	// Validator

	public static final String ALLOWED_MESSAGE_CODE =
		"allowed.messageCode.Groups";

	public static final String AMOUNT = "amount";

	// Common

	public static final String AMP_ISSUER = "504574";

	public static final String ANDROID_PAY = "Android Pay";

	public static final String APPLE_PAY = "Apple Pay";

	public static final String APPLICATION_VND_MS_EXCEL =
		"application/vnd.ms-excel";

	public static final String ATM = "ATM";

	public static final String ATM_DEPOSIT_REASON_TYPEID_PROPSKEY =
		"atm.deposit.reason.type.id";

	public static final String ATM_REASON_TYPEID_PROPSKEY =
		"atm.reason.type.id";

	public static final String ATM_TYPE_ID = "atm.type.id";

	public static final String ATMPOS_PRODUCT_ID = "atmpos.product.code";

	public static final String ATTRIBUTES_TYPE_SERVICE_REQUEST_STATUS =
		"SERVICE.REQUEST.STATUS";

	public static final String AUTH_PROPERTY_PREFIX_KEY =
		"cud.detail.authorisedby.";

	public static final String AUTHORISATION = "Auth";

	public static final String AUTHORISATION_CUD = "Authorisation";

	public static final String AVAILABLE_FUNDS_ENQUIRY_30 = "30";

	public static final String BALANCE_ENQUIRY = "Balance Enquiry";

	public static final String BALANCE_INQUIRY_31 = "31";

	public static final String BILATERAL_ATM = "BILATERAL ATM";

	public static final String BILATERAL_POS = "BILATERAL POS";

	public static final String BILLING_AMOUNT = "billingAmount";

	public static final String BIN_SEARCH_DAYS_ALLOWED =
		"bin.search.days.allowed";

	public static final String BUS_DATE = "busDate";

	public static final String CANCEL = "cancel";

	// Transaction Search/List

	public static final String CARD_ACCEPTOR_ID = "cardAcceptorId";

	public static final String CARD_CONTROL_REJECT_CODE_END = "</A118>";

	public static final String CARD_CONTROL_REJECT_CODE_PROPSKEY =
		"card.control.reject.code.";

	public static final String CARD_CONTROL_REJECT_CODE_START = "<A118>";

	public static final String CARDHOLDER_ACC_TRANSFER_40 = "40";

	public static final String CASH_DEPOSITS_21 = "21";

	public static final String CENTRAL_SITE_BUSINESS_DATE_END = "</F048.158>";

	public static final String CENTRAL_SITE_BUSINESS_DATE_START = "<F048.158>";

	public static final String CHARGEBACK_REDIRECT_RENDER =
		"chargeBackRedirect";

	public static final String CHEQUE_DEPOSITS_24 = "24";

	public static final String COLON = ":";

	public static final String COMMA = ",";

	public static final String CREDIT_REVERSAL_2 = "2";

	public static final String CUD_2FA = "cud2FA";

	public static final String CUD_2FA_RENDER = "cud2faRender";

	public static final String CUD_AD_PANK = "cudAdPank";

	public static final String CUD_CLEAR = "cudClear";

	public static final String CUD_CUR_PAGE = "cudCurPage";

	public static final String CUD_CURR_ROW_NUM = "cudCurrRowNum";

	// CUD Transactions

	public static final String CUD_DETAIL = "cudDetail";

	public static final String CUD_ETL_DATE = "cudEtlDate";

	public static final String CUD_EXPORT = "cudExport";

	public static final String CUD_EXPORT_LIST = "cudExportxls";

	public static final String CUD_FRM = "cudFrm";

	public static final String CUD_IS_FA = "cudIsFa";

	public static final String CUD_LIST = "cudList";

	public static final String CUD_LIST_REQ = "cudListReq";

	public static final String CUD_LOGPOOL = "cudLogPool";

	public static final String CUD_NEXT = "cudNext";

	public static final String CUD_ORG_SHORT_NAME = "cudOrgShortName";

	public static final String CUD_PREV = "cudPrev";

	public static final String CUD_PRINT = "cudPrint";

	public static final String CUD_PURGEPOOL = "cudPurge";

	public static final String CUD_SEARCH_MAX_DAYS = "cud.search.max.days";

	public static final String CUD_SEARCH_RESULT = "cudResult";

	public static final String CUD_SORT_ORDER = "cudSortOrder";

	public static final String CUD_TERMINALLOCATION_DOUBLE_QUOTE_ENABLE =
		"cud.terminalLocation.double.quote.enable";

	public static final String CUD_TRANSACTION_CONTROLLER =
		"cudTransactionSearchController";

	public static final String CUD_TRANSACTION_FORM_VALIDATOR =
		"cudTransactionSearchValidator";

	public static final String CUD_TRANSACTION_SEARCH = "CudTransaction";

	public static final String CUD_TRANSACTION_SERVICE =
		"cudTransactionSearchService";

	public static final String CUD_TX_ID = "cudTxId";

	public static final String CUD_TX_SEARCH_PAGE = "cudTransactionSearch";

	public static final String CUD_TXN_DATE = "cudTransactionDate";

	public static final String CUD_TXN_DETAIL_K = "adTransactionDetailK";

	public static final String CUR_PAGE = "curPage";

	public static final String CURR_ROW_NUM = "currRowNum";

	public static final String CUSCAL_HCE = "Cuscal HCE";

	public static final String DASH = "-";

	public static final String DATA_BASE_ERROR = "dataBaseError";

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	public static final String DATE_FORMAT_1 = "yyyy-MM-dd";

	public static final String DATE_FORMAT_LONG = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

	// List export

	public static final String DATE_TIME = "dateTime";

	public static final String DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm";

	// Transaction Details page

	public static final String DATE_TIME_FORMAT_24HR = "dd/MM/yyyy HH:mm";

	public static final String DATE_WARNING_MSG = "dateWarningMsg";

	public static final String DEFAULT_AMOUNT_VALUE = "0";

	public static final String DEFAULT_CURRENCY_CODE = "AUD";

	public static final int DEFAULT_PAGE_NUMBER = 1;

	public static final int DEFAULT_PAGE_SIZE = 250;

	public static final String DEPOSIT = "Deposit";

	public static final String DESCRIPTION = "description";

	public static final String DISPLAY_FUNCTION_CODE = "displayFunctionCode";

	public static final String EFT_CONNECT_URI = "eft.connect.uri";

	public static final String ELECTRONIC_COMMERCE_INDCIATOR_END =
		"</M_P0042.1>";

	public static final String ELECTRONIC_COMMERCE_INDCIATOR_START =
		"<M_P0042.1>";

	public static final String EMPTY_STRING = "";

	public static final String EMV_ENABLED = "Emv Enabled";

	public static final String ENTITY_WS_URL = "webservice.entity.url";

	public static final String EQUALS = "=";

	public static final String ERROR = "error";

	//PIN Change History Transactions
	public static final String ERROR_MSG = "error";

	public static final String ERRORS = "errors";

	public static final String EXTERNAL_TRANSACTION_ID =
		"externalTransactionId";

	public static final String FALSE = "false";

	public static final String FAULTY_CARD_READER_END = "</F007.FCR>";

	public static final String FAULTY_CARD_READER_START = "<F007.FCR>";

	public static final String FINANCIAL = "Fina";

	public static final String FIRST = "first";

	public static final String FITBIT_PAY = "Fitbit Pay";

	public static final String FROM_FA = "fromFA";

	public static final String FUNCTION_CODE = "functionCode";

	public static final String GARMIN_PAY = "Garmin Pay";

	public static final String HCE_F39_0 = "0";

	public static final String HCE_F39_851 = "851";

	public static final String HCE_F39_852 = "852";

	public static final String HCE_F39_END_TAG = "</HCE_F39>";

	public static final String HCE_F39_START_TAG = "<HCE_F39>";

	public static final String HTML_SPACE = "&nbsp;";

	public static final String INCOMPLETE = "incomplete";

	public static final String INCOMPLETE_MSG = "incomplete";

	public static final int INT_30 = 30;

	public static final String INVALID_SERVICE_REQUEST =
		"invalidServiceRequest";

	public static final String IS_FA = "isFa";

	public static final String ISS_ISO_CODE_END_TAG = "</ISS_ISO_CODE>";

	public static final String ISS_ISO_CODE_START_TAG = "<ISS_ISO_CODE>";

	public static final String JSON2 = "json";

	public static final String LAST = "last";

	public static final String MASTERCARD_ACQ = "03604";

	public static final String
		MASTERCARD_ARBITRATION_CHARGEBACK_ATTRIBUTES_LIST_ID = "891";

	public static final String MASTERCARD_CIS = "MASTERCARD_CIS";

	public static final String MASTERCARD_DISPUTE = "MasterCard Dispute";

	public static final String
		MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID =
			"mastercard.dispute.arbitration.chargeback.type.id";

	public static final String MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID =
		"mastercard.dispute.first.chargeback.type.id";

	public static final String MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID =
		"mastercard.dispute.report.fraud.type.id";

	public static final String MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID =
		"mastercard.dispute.retrieval.request.type.id";

	// Transaction Quick Search

	public static final String MASTERCARD_FIRST_CHARGEBACK_ATTRIBUTES_LIST_ID =
		"890";

	public static final String
		MASTERCARD_FIRST_PRESENTMENT_FUNCTION_CODE_DESCRIPTION =
			"First Presentment";

	public static final String MASTERCARD_IPM = "MASTERCARD_IPM";

	public static final String
		MASTERCARD_PRESENTMENT_FUNCTION_CODE_TRANSACTION_LOG = "0220";

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

	public static final String MERCHANT_ID = "6011";

	// Transaction Constants

	public static final String MESSAGE_CODE_UI_VALUES_PROPSKEY =
		"ui.messageOption.display";

	public static final String MESSAGE_TYPE = "messageType";

	public static final String MESSAGE_TYPE_2 = "2";

	public static final String MORE_DATA_MSG = "moreDataMsg";

	public static final String MORE_ORG = "moreOrg";

	public static final String NATURAL = "natural";

	public static final String NO = "No";

	public static final String NO_DATA_FOUND_MSG = "noDataFoundMsg";

	public static final String NOT_AVAILABLE = "N/A";

	public static final String NULL = "null";

	public static final String OPTION_ATM = "ATM";

	public static final String OPTION_POS = "POS";

	public static final String ORG_OVERRRIDE = "organisation.id.override";

	public static final String ORG_SHORT_NAME = "orgShortName";

	public static final String OTHER = "Other";

	public static final String OTHER_ERROR = "otherError";

	public static final int PAGE_SIZE = 25;

	public static final String PAN = "pan";

	public static final String PAN_ENTRY_MODE_END = "</F022.1>";

	public static final String PAN_ENTRY_MODE_START = "<F022.1>";

	public static final String PIN_CHANGE_2FA = "pinChange2FAPage";

	public static final String PIN_CHANGE_2FA_RENDER = "pinChange2faRender";

	public static final String PIN_CHANGE_BUSINESS_DATE =
		"pinChangeBusinessDate";

	public static final String PIN_CHANGE_CONTROLLER = "pinChangeController";

	public static final String PIN_CHANGE_CUR_PAGE = "pinChangeCurPage";

	public static final String PIN_CHANGE_DETAIL = "pinChangeDetail";

	public static final String PIN_CHANGE_DETAIL_PAGE = "pinChangeDetailPage";

	public static final String PIN_CHANGE_DETAIL_RESOURCE =
		"pinChangeDetailResource";

	public static final String PIN_CHANGE_ERROR_PAGE = "pinChangeErrorPage";

	public static final String PIN_CHANGE_EXPORT_LIST = "pinChangeExportList";

	public static final String PIN_CHANGE_LIST = "pinChangeList";

	public static final String PIN_CHANGE_LIST_REQ = "pinChangeListReq";

	public static final String PIN_CHANGE_NEXT_DETAILS = "nextPinChangeDetails";

	public static final String PIN_CHANGE_NO_RESULTS = "pinChangeNoResults";

	public static final String PIN_CHANGE_OPERATOR_ID_END = "</OPERID>";

	public static final String PIN_CHANGE_OPERATOR_ID_START = "<OPERID>";

	/* Project 8847 */
	public static final String PIN_CHANGE_PREV_DETAILS = "prevPinChangeDetails";

	public static final String PIN_CHANGE_PRINT_DETAILS =
		"printPinChangeDetails";

	// Currency codes

	public static final String PIN_CHANGE_SEARCH_ACTION =
		"pinChangeSearchAction";

	public static final String PIN_CHANGE_SEARCH_FORM = "pinChangeSearchForm";

	public static final String PIN_CHANGE_SEARCH_PAGE = "pinChangeSearch";

	public static final String PIN_CHANGE_SEARCH_RENDER =
		"pinChangeSearchRender";

	public static final String PIN_CHANGE_SEARCH_RESULTS =
		"pinChangeSearchResults";

	public static final String PIN_CHANGE_SEARCH_RESULTS_LIST =
		"pinSearchResults";

	public static final String PIN_CHANGE_SEARCH_RESULTS_PAGE =
		"pinChangeSearchResults";

	public static final String PIN_CHANGE_SELECT_CODE_PRE = "PCT";

	public static final String PIN_CHANGE_SORT_ORDER = "SortOrder";

	//Sorting
	public static final String PIN_CHANGE_SOURCE = "pinChangeSource";

	public static final String PIN_CHANGE_SUPERVISOR_ID_END = "</SPRVSRID>";

	public static final String PIN_CHANGE_SUPERVISOR_ID_START = "<SPRVSRID>";

	public static final String PIN_CHANGE_TRANSACTION_ID =
		"pinChangeTransactionId";

	public static final String PIN_CHANGE_TRANSACTION_SEARCH =
		"PinChangeHistory";

	public static final String PIN_CHANGE_VALIDATOR =
		"pinChangeHistorySearchValidator";

	public static final String PIN_PRESENT_END = "</PINPRESENT>";

	public static final String PIN_PRESENT_NO = "No";

	public static final String PIN_PRESENT_NOORUNKNOWN = "NoOrUnknown";

	public static final String PIN_PRESENT_START = "<PINPRESENT>";

	public static final String PIN_PRESENT_YES = "Yes";

	public static final String PIN_SELECT_CODE = "76";

	public static final String POS = "POS";

	public static final String POS_CONDITION_CODE_END = "</F025>";

	public static final String POS_CONDITION_CODE_START = "<F025>";

	public static final String POS_DEPOSITS_21 = "21";

	public static final String POS_REASON_TYPEID_PROPSKEY =
		"pos.reason.type.id";

	//others
	public static final String POS_TYPE_ID = "pos.type.id";

	public static final String PRODUCT_ID = "generic.product.code";

	public static final String PURCHASE = "Purchase";

	public static final String PURCHASE_0 = "0";

	public static final String PURCHASE_CASHOUT_9 = "9";

	public static final String PURCHASE_WITH_CASHOUT = "Purchase with Cashout";

	public static final String QUICK = "quick";

	public static final String QUICK_SEARCH_PAGE = "txQuickSearch";

	public static final String RECORD_NOT_FOUND = "RECORD_NOT_FOUND";

	public static final String REDI_ATM = "REDI";

	public static final String REFUND = "Refund";

	public static final String REG_EXP_AMOUNT =
		"^(?:\\$\\s*)?(?:(?:\\d{0,3}(?:[, ]\\d{0,3})*[, ])+\\d{3}|\\d+)(?:\\.\\d{2})?(?:\\s*\\$)?$";

	public static final String REG_EXP_EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String REG_EXP_MASKED_PAN_SPECIAL_CHAR =
		"[^A-Wa-wY-Zy-z\\.\\&\\%\\@_\\-~$]+";

	public static final String REG_EXP_NEGATIVE_AMOUNT =
		"^-?(?:\\$\\s*)?(?:(?:\\d{0,3}(?:[, ]\\d{0,3})*[, ])+\\d{3}|\\d+)(?:\\.\\d{2})?(?:\\s*\\$)?$";

	public static final String REG_EXP_PHONE_NUMBER = "[0-9\\(\\)\\/\\+ \\-]*";

	public static final String REG_EXP_SPECIAL_CHAR =
		"[^A-Za-z\\.\\*\\&\\%\\@_\\-~#$]+";

	public static final String REG_EXP_SPECIAL_CHAR_AMOUNT =
		"[^A-Za-z\\*\\&\\%\\@_\\-~#$]+";

	public static final int RESP_CODE_100 = 100;

	public static final String RESPONSE_CODE = "responseCode";

	public static final String RETURNS_20 = "20";

	public static final String REVERSAL = "Reve";

	public static final String REVERSE = "reverse";

	public static final String SALES_REVERSAL_22 = "22";

	public static final String SAMSUNG_PAY = "Samsung Pay";

	public static final String SELF_SERVICE_NAME_PROPSKEY = "name";

	public static final String SELF_SERVICE_PORTLET_LOCATION_PROPSKEY =
		"portlet.location";

	public static final String SELF_SERVICE_PORTLET_NAME_PROPSKEY =
		"portlet.name";

	public static final String SELF_SERVICE_URI = "self.service.uri";

	public static final String SERVICE_CODE = "Service Code";

	public static final String SERVICE_REQUEST_PREFIX_PROPSKEY =
		"service.request.";

	public static final String SERVICE_REQUEST_STATUS_CANCELLED = "CANCELLED";

	public static final String SERVICE_REQUEST_STATUS_REVERSED = "REVERSED";

	public static final String SHOW_CUSCAL_ID = "showCuscalId";

	public static final String SORT_ORDER = "SortOrder";

	public static final String SPACE = " ";

	public static final String SPACE_DASH_SPACE = " - ";

	public static final String STATUS_ALARIC_PROPSKEY = "status.header.alaric";

	public static final String STATUS_CODE_INCOMPLETE = "CODS_ALARIC";

	//Service Request
	public static final String STATUS_CODE_SYSTEM_ERROR = "SYSTEM_ERROR";

	public static final String STATUS_CODE_SYSTEM_ERROR_0 = "ERROR";

	public static final String STATUS_CODE_SYSTEM_ERROR_1 = "CODS";

	public static final String STATUS_CODE_SYSTEM_ERROR_2 = "ALARIC";

	public static final String STATUS_CODE_VALIDATION = "VALIDATION";

	public static final String STATUS_CODS_PROPSKEY = "status.header.cods";

	public static final String STATUS_CODSALARIC_PROPSKEY =
		"status.header.cods.alaric";

	public static final String STATUS_ERROR_HSM_PROPSKEY =
		"status.header.hsm.error";

	public static final String STATUS_ERROR_PROPSKEY = "status.header.error";

	public static final String STATUS_SQL_TIMEOUT = "sqlTimeout";

	public static final String STATUS_SUCCESS = "SUCCESS";

	public static final String STATUS_SYSTEM_ERROR = "SYSTEM_ERROR";

	public static final String STATUS_SYSTEM_ERROR_PROPSKEY =
		"status.header.system.error";

	public static final String STATUS_VALIDATION_PROPSKEY =
		"status.header.validation";

	public static final String STRING_00 = "00";

	public static final String STRING_0 = "0";

	public static final String STRING_05 = "05";

	public static final String STRING_08 = "08";

	public static final String STRING_24 = "24";

	public static final String STRING_81 = "81";

	public static final String STRING_0100 = "0100";

	public static final String STRING_0120 = "0120";

	public static final String STRING_EQ = "eq";

	public static final String STRING_LOCAL = "local";

	public static final String STRING_T = "T";

	public static final String SUBMITTED = "Submitted";

	public static final String SUCCESS = "SUCCESS";

	//Cud Txn description
	public static final String SWITCH_DATE = "switchDate";

	public static final String SWITCHED_BY_CONFIG_DEFAULT =
		"cud.detail.switched.by.default";

	public static final String SWITCHED_BY_CONFIG_PREFIX =
		"cud.detail.switched.by.";

	public static final String SYSTEM_TRACE = "systemTrace";

	public static final String TC40_PRODUCT_ID = "tc40.product.code";

	public static final String TC40_TYPE_ID = "tc40.type.id";

	public static final String TC52_PRODUCT_ID = "tc52.product.code";

	public static final String TC52_TYPE_ID = "tc52.type.id";

	//Portlets titles
	public static final String TERMINAL_CAPACITY_END = "</F007.TCC>";

	public static final String TERMINAL_CAPACITY_START = "<F007.TCC>";

	public static final String TERMINAL_ID = "terminalId";

	public static final String TERMINAL_LOCATION = "terminalLocation";

	public static final String TEXT_CSV = "text/csv";

	public static final String TICKET_DETAILS_RENDER = "ticketDetails";

	public static final String TICKETING_PORTLET_NAME =
		"cuscalticketingportlet_WAR_cuscalticketingportlet";

	public static final String TIMEZONE_EST = "Australia/Sydney";

	public static final String TRANSACTION = "transaction";

	public static final String TRANSACTION_CATEGORY_CODE_END = "</M_P0000>";

	public static final String TRANSACTION_CATEGORY_CODE_START = "<M_P0000>";

	public static final String TRANSACTION_CLIENT_SERVICE =
		"transactionService";

	public static final String TRANSACTION_COMMON_WS_URL =
		"webservice.common.url";

	public static final String TRANSACTION_CONTROLLER =
		"transactionSearchController";

	public static final String TRANSACTION_CUD_DETAIL = "transactionCudDetail";

	public static final String TRANSACTION_DETAIL = "transactionDetail";

	public static final String TRANSACTION_FORM = "transactionForm";

	public static final String TRANSACTION_FORM_VALIDATOR =
		"transactionSearchValidator";

	public static final String TRANSACTION_QUALIFIER_ID_BEST_ADVICE = "BA";

	public static final String TRANSACTION_QUICK_SERVICE =
		"transactionQuickSearchService";

	public static final String TRANSACTION_SEARCH = "Transaction";

	public static final String TRANSACTION_SERVICE = "transactionSearchService";

	public static final String TRANSACTION_WS_URL =
		"webservice.transaction.url";

	public static final String TRL_PREV_TXN_SAME_PERIOD_1 = "1";

	public static final String TRL_PREV_TXN_SAME_PERIOD_2 = "2";

	public static final String TRL_PREV_TXN_SAME_PERIOD_3 = "3";

	public static final String TRL_PREV_TXN_SAME_PERIOD_4 = "4";

	public static final String TRL_PREV_TXN_SAME_PERIOD_5 = "5";

	public static final String TRL_PREV_TXN_SAME_PERIOD_6 = "6";

	public static final String TRL_PREV_TXN_SAME_PERIOD_END_TAG =
		"</TRL_PREV_TXN_SAME_PERIOD>";

	public static final String TRL_PREV_TXN_SAME_PERIOD_START_TAG =
		"<TRL_PREV_TXN_SAME_PERIOD>";
	//MasterCard Dispute - Retrieval Request, First Charge back,  Arbitration Charge back
	public static final String TRUE = "true";

	public static final String TX_2FA = "2FAPage";

	public static final String TX_2FA_RENDER = "2faRender";

	public static final String TX_ACTION = "action";

	public static final String TX_ATM_POS_SUBMIT_RENDER = "atmPosSubmitRender";

	public static final String TX_CLEAR = "clearValid";

	public static final String TX_CURRENT_PAGE = "currentPage";

	public static final String TX_CURRENT_SORT = "currentSort";

	public static final String TX_DETAIL = "txDetail";

	public static final String TX_DETAIL_PAGE = "transaction-details-wrapper";

	public static final String TX_DETAIL_RENDER = "detailRender";

	public static final String TX_ERROR = "txError";

	public static final String TX_ERROR_PAGE = "txErrorPage";

	public static final String TX_EXPORT_LIST = "exportxls";

	public static final String TX_FRM = "txFrm";

	public static final String TX_ID = "txId";

	public static final String TX_LIST = "txList";

	public static final String TX_LIST_REQ = "txListReq";

	public static final String TX_LOGPOOL = "logPool";

	public static final String TX_MSG = "msgList";

	public static final String TX_NEXT = "nextTxDet";

	public static final String TX_NODATA_FOUND =
		"No transactions were found that matched your criteria.";

	public static final String TX_PREV = "prevTxDet";

	public static final String TX_PRINT = "printAudit";

	public static final String TX_PURGEPOOL = "purge";

	public static final String TX_RENDER = "render";

	public static final String TX_RESP = "respList";

	public static final String TX_ROLE_FA = "2FA";

	public static final String TX_SEARCH_PAGE = "transactionSearch";

	public static final String TX_SEARCH_RESULT = "result";

	public static final String TX_SEARCH_RESULTS_PAGE =
		"transactionSearchResults";

	public static final String TX_SRC = "txSrc";

	public static final String TXN_DESCRIPTION_CONFIG_DEFAULT =
		"cud.transaction.description.default";

	public static final String TXN_DESCRIPTION_CONFIG_PREFIX =
		"cud.transaction.description.";

	public static final String TXN_LIST_HARD_LIMIT =
		"cud.transaction.list.hardLimit";

	public static final String UCAF_COLLECTION_INDICATOR_ATTEMPTED_1 =
		"Authentication attempted";

	public static final String UCAF_COLLECTION_INDICATOR_AUTHENTICATED_2 =
		"Cardholder authenticated";

	public static final String UCAF_COLLECTION_INDICATOR_END = "</M_P0042.3>";

	public static final String UCAF_COLLECTION_INDICATOR_NOT_SUPPORTED_0 =
		"Not Supported";

	public static final String UCAF_COLLECTION_INDICATOR_START = "<M_P0042.3>";

	public static final String UI_END_HR_PROPSKEY =
		"transaction.search.ui.end.hr";

	public static final String UI_END_MINS_PROPSKEY =
		"transaction.search.ui.end.mins";

	public static final String UI_START_HR_PROPSKEY =
		"transaction.search.ui.start.hr";

	public static final String UI_START_MINS_PROPSKEY =
		"transaction.search.ui.start.mins";

	public static final String USER_CANCEL = "USER_CANCEL";

	public static final String UTF_8 = "utf-8";

	public static final String VALIDATION = "validation";

	public static final String VISA = "VISA";

	public static final String VISA_ATM_6011 = "6011";

	public static final String VISA_BASE_2_CTF_TRANSACTIONBATCH =
		"CTF_TranscationBatch";

	public static final String VISA_BASE_I = "VISA_BASE_I";

	public static final String VISA_BASE_II = "VISA_BASE_II";

	public static final String VISA_CHARGEBACK = "Visa Chargeback";

	public static final String WITHDRAW = "Withdraw";

	//P0037
	public static final String WITHDRAWAL_1 = "1";

	public static final String YES = "Yes";

	public static final String MASTERCARD_CMS_TransactionBatch =
		"CMS_TransactionBatch";
	public static final String MASTERCARD_IPM_TransactionBatch =
		"IPM_TransactionBatch";

	//3DS Authentication
	public static final String ECPI_START_TAG = "<ECPI>";
	public static final String ECPI_END_TAG = "</ECPI>";
	public static final String F047_START_TAG="<F047>";
	public static final String F047_END_TAG="</F047>";
	public static final String STRING_SLI="SLI";

}