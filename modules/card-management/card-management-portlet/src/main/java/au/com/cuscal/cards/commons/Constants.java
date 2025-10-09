package au.com.cuscal.cards.commons;

/**
 * Constants for Transaction Search View
 *
 * Transaction and Quick Search Portlet constants
 *
 * @author Rajni Bharara
 *
 */
public interface Constants {

	// Services

	public static final String ACCESS_TYPE_PROPKEY = "accessTypes";

	public static final String ACCOUNT_NUMBER = "accountNumber";

	public static final String ACCOUNT_TYPE = "accountType";

	public static final String ACCOUNT_TYPE_DESC = "accountTypeDescription";

	public static final String ACCOUNTS = "accounts";

	public static final String ADDRESS1 = "address1";

	public static final String ADDRESS2 = "address2";

	public static final String ASSCENDING = "asc";

	public static final String AUDIT_HISTORY = "auditHistory";

	public static final String AUDIT_REST_SERVICE = "auditRestService";

	// Validator

	public static final String AVAILABLE_BALANCE = "availableBalance";

	// controller

	public static final String BALANCES = "balances";
	//	public static final String QUICK_SEARCH_CONTROLLER = "quickSearchController";

	// Common

	public static final String BIN = "bin";

	public static final String CARD_2FASUCCESS_PAGE = "sucessFa";

	public static final String CARD_ACTION = "action";

	public static final String CARD_CHANNELS = "cardChannels";

	public static final String CARD_CONTROLS = "cardControls";

	public static final String CARD_CONTROLS_HISTORY = "showCardControlHistory";

	public static final String CARD_CONTROLS_UPDATE = "updateCardControls";

	public static final String CARD_CUSCALAPI_SERVICE = "cuscalApiService";

	public static final String CARD_DEREG_DEVICE = "dereg";

	public static final String CARD_DETAIL = "cardD";

	public static final String CARD_DETAIL_LIST = "cardDetailList";

	public static final String CARD_DETAIL_OBJECT = "cardDetail";

	public static final String CARD_DETAIL_PAGE = "cardsDetails";

	public static final String CARD_DETAILS_FORM = "cardDetailForm";

	public static final String CARD_DEVICE_SERVICE = "switchService";

	public static final String CARD_DEVICES = "devices";

	public static final String CARD_ERROR = "cardError";

	public static final String CARD_ERROR_FAILURE = "cardSearchError";

	public static final String CARD_ERROR_MSG = "errorMsg";

	public static final String CARD_ERROR_PAGE = "cardErrorPage";

	public static final String CARD_FORM = "cardSearchForm";

	public static final String CARD_FORM_VALIDATOR = "cardsSearchValidator";

	public static final String CARD_HOLDER_NAME = "cardHolderName";

	public static final String CARD_ID = "cardId";

	public static final String CARD_INFO_TAB = "cardTab";

	public static final String CARD_LIMITS = "cardLimits";

	public static final String CARD_LIMITS_HISTORY = "showLimitsHistory";

	public static final String CARD_LIMITS_UPDATE = "updateCardLimits";

	public static final String CARD_LIST = "cardList";

	public static final String CARD_NODATA_FOUND =
		"No cards were found that matched your criteria.";

	public static final String CARD_REDIRECT2FA_RESULT = "stepUpRedirect";

	public static final String CARD_REFERENCE_LIST = "cardReferenceList";

	public static final String CARD_RENDER = "render";

	public static final String CARD_RESET = "resetCardsForm";

	public static final String CARD_REST_SERVICE = "cardRestService";

	public static final String CARD_REST_TRANSLATOR = "cardsRestTranslator";

	public static final String CARD_ROLE_FA = "2FA";

	public static final String CARD_ROUTE_SERVICE = "cardsRouteService";

	public static final String CARD_SCHEME_OPT_PROPKEY =
		"card.schemeType.ui.option";

	public static final String CARD_SCHEME_SERVICE = "schemeService";

	public static final String CARD_SCHEME_VAL_PROPKEY =
		"card.schemeType.ui.val";

	public static final String CARD_SEARCH_APP_PROPERTIES =
		"cardSearchAppProperties";

	public static final String CARD_SEARCH_CONTROLLER = "cardsSearchController";

	public static final String CARD_SEARCH_PAGE = "cardsSearch";

	public static final String CARD_SEARCH_RESULT = "result";

	public static final String CARD_SEARCH_SERVICE = "cardsSearchService";

	public static final String CARD_SPEND = "getCardSpend";

	public static final String CARD_STATUS_API_OPT_PROPKEY =
		"card.status.ui.api.option";

	public static final String CARD_STATUS_OPT_PROPKEY =
		"card.status.ui.option";

	public static final String CARD_STATUS_UPDATE_ADMIN_PROPKEY =
		"card.status.update.roles.admin";

	public static final String CARD_STATUS_UPDATE_NO_DELETE_PROPKEY =
		"card.status.update.no.delete.roles";

	public static final String CARD_STATUS_UPDATE_PROPKEY =
		"card.status.update.roles";

	public static final String CARD_STATUS_VAL_PROPKEY = "card.status.ui.val";

	public static final String CARD_UPDATE_DISABLED_ERROR =
		"cards.error.card.update.disabled";

	public static final String CARD_UPDATE_ERROR = "cardUpdateError";

	// Cards Search/List

	public static final String CARD_UTIL_SERVICE = "cardService";

	public static final String CARDS = "cards";

	public static final String CARDS_WSDL_URL = "webservice.cards.url";

	public static final String CHANNEL_PERMISSIONS_ERROR =
		"cards.permissions.error";

	public static final String CHANNEL_PERMISSIONS_NAME_EARTH_KEY =
		"card.channel.authenticEarth.key";

	public static final String CHANNEL_PERMISSIONS_NAME_KEY =
		"card.channel.key";

	public static final String CHANNEL_PERMISSIONS_NAME_VALUE =
		"card.channel.value";

	public static final String CHANNEL_PERMISSIONS_ORDER = "card.channel.order";

	public static final String CHANNEL_PORTAL = "PORTAL";

	public static final String CHECK_SECONDARY_SWITCH =
		"check.secondary.switch";

	public static final String CLEAR_CARD_REFERENCE_LIST =
		"clearCardReferenceList";

	// Cards Details page

	public static final String COLON = ":";

	public static final String COMMA = ",";

	public static final String COMMA_SPACE = ", ";

	public static final String COMMONS_WSDL_URL = "webservice.common.url";

	public static final String COUNTRY = "country";

	public static final String CREDIT_LINE_BALANCE = "creditLineBalance";

	public static final String CURRENT_PAGE = "currentPage";

	public static final String CURRENT_SORT = "currentSort";

	public static final String CUSCAL_TOKEN = "cuscalToken";

	public static final String CUSCAL_TOKEN_WEBSERVICE =
		"cuscalTokenWebService";

	public static final String DASH = "-";

	public static final String DATA = "data";

	public static final String DATE_24_HOUR_MINUTE_SECOND_FORMAT =
		"yyyy-MM-dd HH:mm:ss";

	// properties constant

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	public static final String DATE_TIMESTAMP_24_HOUR = "yyyyMMdd HH:mm:ss";

	public static final String DATE_TIMESTAMP_24_HOUR_MILLISECONDS =
		"yyyyMMdd HH:mm:ss.SSS";

	public static final int DEFAULT_PAGE_NUMBER = 1;

	public static final int DEFAULT_PAGE_SIZE = 25;

	public static final String DEFUALT_SORT_NAME = "lastName";

	public static final String DELETE_COMMENT = "deleteComment";

	public static final String DELETE_REASON = "deleteReason";

	public static final String DESSCENDING = "desc";

	public static final String DEVICE_ID = "deviceId";

	public static final String DISPLAYTYPE_SWITCH = "UI_SWITCH";

	public static final String ENTITY_WS_URL = "webservice.entity.url";

	public static final String EQUALS = "=";

	// commonly used constants..

	public static final String EXPIRY_DATE = "expiryDate";

	public static final String GET_KEY_URI = "/key";

	public static final String HEARTBEAT_CONNECTIVITY_ERROR =
		"HEARTBEAT_CONNECTIVITY_ERROR";

	public static final String IS_CARD_STATUS_UPADTE = "isSuccessOrFail";

	public static final String IS_DATA_FROM_AUTHENTIC_EARTH =
		"isDataFromAuthenticEarth";

	public static final String IS_PRIMARY_ACCOUNT = "isPrimaryAccount";

	public static final String ISSUER_ID = "issuerId";

	public static final String ISSUER_NAME = "issuerName";

	public static final String ISSUER_SHORT_NAME = "issuerShortName";

	public static final String LAST_MODIFIED_DATE = "lastModifiedDate";

	public static final String LAST_USED_TIMESTAMP = "lastUsedTimestamp";

	public static final String LEDGER_BALANCE = "ledgerBalance";

	public static final String LIMITS = "limits";

	//Status code from alaric
	public static final String LOG_POOLS = "logPools";

	public static final String MAIL_ADDRESS = "mailAddress";

	public static final int MAXIMUM_CARDHOLDER_LENGTH = 50;

	public static final int MAXIMUM_ISSUER_NAME_LENGTH = 300;

	public static final int MAXIMUM_PAN_LENGTH = 19;

	public static final String MCC_CONTROLS = "mccControls";

	public static final String MCC_CONTROLS_HISTORY = "showMccHistory";

	public static final String MCC_CONTROLS_UPDATE = "updateMccControls";

	public static final String META = "meta";

	public static final int MINIMUM_PAN_LENGTH = 13;

	public static final String MOBILE_DEVICE_BLURB = "mobile.blurb";

	public static final String NAME = "name";

	public static final String NEXT_STATUS = "nextStatus";

	public static final String NOT_AVAILABLE = "Not available";

	public static final String NOT_SUCCESSFUL = "NOT_SUCCESSFUL";

	public static final String ORG = "org.";

	public static final String ORG_LIST = "orgList";

	public static final String ORG_NAME = "orgName";

	public static final String ORG_SHORT_NAME = "orgShortName";

	public static final String PAN = "pan";

	public static final char PAN_MASKED_CHARACTER = 'X';

	// constants for web service pool management

	public static final String PERMISSIONS = "permissions";

	public static final String PIN_RETRY_COUNT = "pinRetryCount";

	// mobile devices tab

	public static final String POSTCODE = "postcode";

	public static final String PURGE_POOLS = "purge";

	//token
	public static final String RECORD_NOT_FOUND = "RECORD_NOT_FOUND";

	public static final String REG_EXP_ONLY_DIGITS = "^[1-9]\\d*$";

	public static final String REG_EXP_SPECIAL_CHAR =
		"[^A-Za-z\\.\\*\\&\\%\\@_\\-~#$]+";

	public static final String REST_SERVICE = "restService";

	public static final String ROUTE_AUTHENTIC_EARTH = "authenticEarth";

	public static final String ROUTE_AUTHENTIC_THREE = "authentic";

	public static final String SHOW_CARD_CONTROLS_HISTORY =
		"card.details.showCardControlsHistory";

	public static final String SHOW_CARD_LIMITS_HISTORY =
		"card.details.showCardLimitsHistory";

	public static final String SHOW_CARD_LIMITS_TAB =
		"card.details.showCardLimitsTab";

	public static final String SHOW_CARD_LIMITS_TAB_FOR_ORGS =
		"card.details.showCardLimitsTabForOrgs";

	//controls
	public static final String SHOW_HIDE_LIMITS_TAB =
		"card.details.showLimitsTab";

	public static final String SHOW_MCC_CONTROLS_HISTORY =
		"card.details.showMccControlsHistory";

	public static final String SHOW_MCC_CONTROLS_TAB =
		"card.details.showMccControlsTab";

	public static final String SHOW_MOBILE_DEVICES = "showMobileDevices";

	public static final String SHOW_MOBILE_DEVICES_TAB =
		"card.details.showMobileDevicesTab";

	//MCC
	public static final String SLASH = "/";

	public static final String SORT_ORDER_MAP = "SortOrderMap";

	public static final String STATE = "state";

	public static final String STATUS = "status";

	public static final String STATUS_CHANGES = "statusChanges";

	public static final String STATUS_UPDATE = "statusUpdate";

	//limits
	public static final String SUBURB = "suburb";

	public static final String SUCCESS = "SUCCESS";

	public static final String SUSPEND_COMMENT = "suspendComment";

	public static final String SUSPEND_REASON = "suspendReason";

	//spend
	public static final String SYSTEM_ERROR = "SYSTEM_ERROR";

	// Audit history

	public static final String TO_FA_PAGE = "faPage";

	// Route Strings to Authentic 3 / Authentic Earth

	public static final String TOKEN_ACTION = "tokenAction";

	public static final String TOKEN_ACTIVATE = "activateToken";

	// Address

	public static final String TOKEN_ID = "tokenId";

	public static final String TOKEN_LASTUPDATEDBY_BLURB =
		"tokenLastUpdatedByBlurb";

	public static final String TOKEN_PRIMARY_SEARCH_FAILURE = "526";

	public static final String TOKEN_SECONDARY_SEARCH_FAILURE = "527";

	public static final String TOKEN_STATUS = "tokenStatus";

	public static final String TOKEN_SUCCESS = "00";

	public static final String TOKEN_UPDATE = "updateToken";

	// Accounts

	public static final String TOKEN_WALLETS = "tokenWallets";

	public static final String TOTAL_RECORDS = "totalRecords";

	public static final String TYPE = "type";

	public static final String UNKNOWN = "Unknown";

	public static final String UNSUSPEND_COMMENT = "unsuspendComment";

	public static final String UNSUSPEND_REASON = "unsuspendReason";

	public static final String UPDATE_CARD_REFERENCE_LIST =
		"updateCardReferenceList";

	public static final String USAGE = "usage";

	public static final String USER_STEPUP = "_USER_STEPPEDUP";

	// URI

	public static final String VALIDATION_FAILURE = "VALIDATION_FAILURE";

	public static final String VALUE = "value";

}