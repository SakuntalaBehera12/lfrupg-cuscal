//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.common;

import java.util.Arrays;
import java.util.List;

public interface Constants {

	public static final String ACTION = "action";

	public static final String ALL = "all";

	public static final String ALL_ORGANISATIONS = "ALL_ORGANISATIONS";

	public static final String AMOUNT_COUNT = "amountCount";

	public static final String CC_PATTERN =
		"(4(?:\\d([ -]?)*?){13,19})|(5[1-5](?:\\d([ -]?)*?){13,19})|(3[47](?:\\d([ -])?){13})|(3(?:0[0-5]|[68][\\d])(?:\\d([ -]?)){11})|((?:2131|1800|35\\d{2})((?:([ -]?)\\d){11}|([ -]?)\\d){12})|(6(?:011|5\\d{2})(?:([ -]?)\\d){12})";

	public static final String CHARGEBACK_PAGE = "form";

	public static final String CUSCAL_BSB = "cuscalbsb";

	public static final String CUSCAL_TICKETING_SERVICE =
		"cuscalTicketingService";

	public static final String DASH = "-";

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	public static final String DE_CLAIMS_FORM_ACTION = "deClaimsFormAction";

	public static final String DE_CLAIMS_FORM_PAGE = "deClaimsFormPage";

	public static final String DE_CREDIT = "1";

	public static final String DE_CUSTOMER_SELECTED_WRONG_PAYEE = "2";

	public static final String DE_DEBIT = "2";

	public static final String DE_FINAL_DESTINATION = "1";

	public static final String DE_INTENDED_ACCOUNT_DETAILS = "1";

	public static final String DE_LIST_BOX_DATA = "deListBoxData";

	public static final String DE_MISTAKEN_PAYMENT_FORM_ACTION =
		"deMistakenPaymentFormAction";

	public static final String DE_MISTAKEN_PAYMENT_FORM_PAGE =
		"deMistakenPaymentFormPage";

	public static final String DE_PRODUCT_CODE = "de.product.code";

	public static final String DE_RECALL_FORM_ACTION = "deRecallFormAction";

	public static final String DE_RECALL_FORM_PAGE = "deRecallFormPage";

	public static final String DE_REMITTER_DETAILS = "2";

	public static final String DE_REQUEST_ACTION = "deRequest";

	public static final String DE_TRACE_FORM_ACTION = "deTraceFormAction";

	public static final String DE_TRACE_FORM_PAGE = "deTraceFormPage";

	public static final String DE_TRANS = "de_trans";

	public static final String DE_TRANSACTION_SEARCH_ACTION =
		"deTransactionSearchAction";

	public static final String DE_TRANSACTION_SEARCH_PAGE =
		"deTransactionSearch";

	public static final int DEFAULT_PAGE_NUMBER = 0;

	public static final int DEFAULT_PAGE_SIZE = 0;

	public static final String DESCRIPTION = "description";

	public static final String DOT = ".";

	public static final String EQUALS = "=";

	public static final String ERROR_PAGE = "error";

	public static final String ERRORS = null;

	public static final String FORM = "FORM";

	public static final String FORM_ID = "formId";

	public static final String FORM_ID_DE_CLAIMS = "4";

	public static final String FORM_ID_DE_MISTAKEN_PAYMENT = "3";

	public static final String FORM_ID_DE_RECALL = "2";

	public static final String FORM_ID_DE_TRACE = "1";

	public static final String FORM_ID_OFI_DE_CLAIMS = "8";

	public static final String FORM_ID_OFI_DE_MISTAKEN_PAYMENT = "7";

	public static final String FORM_ID_OFI_DE_RECALL = "6";

	public static final String FORM_ID_OFI_DE_TRACE = "5";

	public static final String HOMEPAGE = "home";

	public static final String ID = "id";

	public static final String IS_FA = null;

	public static final String OFI_DE_CLAIMS_FORM_PAGE = "ofiDeClaimsFormPage";

	public static final String OFI_DE_MISTAKEN_PAYMENT_FORM_PAGE =
		"ofiDeMistakenPaymentFormPage";

	public static final String OFI_DE_RECALL_FORM_PAGE = "ofiDeRecallFormPage";

	public static final String OFI_DE_TRACE_FORM_PAGE = "ofiDeTraceFormPage";

	public static final String OFI_UPDATE_NOTE =
		"Response has been submitted by {0}";

	public static final String ORG_SHORT_NAME = "orgShortName";

	public static final String PRE_POPULATED = "prepopulated";

	public static final String PROPERTIES_PREFIX_DE_CLAIM_REASON =
		"de.claim.reason";

	public static final String PROPERTIES_PREFIX_DE_CLAIM_REPLY =
		"de.claim.reply";

	public static final String PROPERTIES_PREFIX_DE_GENERAL_YES_NO =
		"de.general.yes.no";

	public static final String PROPERTIES_PREFIX_DE_INTENDED_ACCOUNT =
		"de.intended.account";

	public static final String PROPERTIES_PREFIX_DE_REMITTER = "de.remitter";

	public static final String PROPERTIES_PREFIX_DE_REQUEST_FOR_CODE =
		"de.request.for";

	public static final String PROPERTIES_PREFIX_DE_TIMEFRAME = "de.timeframe";

	public static final String PROPERTIES_PREFIX_DE_TRANSACTION_CODE =
		"de.transaction.code";

	public static final String PROPERTIES_PREFIX_OFI_DE_CLAIM_RESULT =
		"ofi.de.claim.result";

	public static final String
		PROPERTIES_PREFIX_OFI_DE_MISTAKEN_CHECKBOX_RESULT =
			"ofi.de.mistaken.result.checkbox";

	public static final String PROPERTIES_PREFIX_OFI_DE_MISTAKEN_RESULT =
		"ofi.de.mistaken.result";

	public static final String PROPERTIES_PREFIX_OFI_DE_RECALL_RESULT =
		"ofi.de.recall.result";

	public static final String REG_EXP_AMOUNT =
		"^(?:\\$\\s*)?(?:(?:\\d{0,3}(?:[, ]\\d{0,3})*[, ])+\\d{3}|\\d+)(?:\\.\\d{2})?(?:\\s*\\$)?$";

	public static final String REG_EXP_EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String REG_EXP_PHONE_NUMBER = "[0-9\\(\\)\\/\\+ \\-]*";

	public static final String RENDER = "render";

	public static final String REQUEST_FOR_FINAL_DESTINATION =
		"ofi.de.request.type.final.destination";

	public static final String REQUEST_TYPE_DE_CLAIM = "DE claim";

	public static final String REQUEST_TYPE_DE_MISTAKEN = "DE mistaken";

	public static final String REQUEST_TYPE_DE_RECALL = "DE recall";

	public static final String REQUEST_TYPE_DE_TRACE = "DE trace";

	public static final String REQUEST_TYPE_TYPE_ID_CLAIM = "de.claims.type.id";

	public static final String REQUEST_TYPE_TYPE_ID_MISTAKEN =
		"de.mistaken.payment.type.id";

	public static final String REQUEST_TYPE_TYPE_ID_OFI_CLAIM =
		"ofi.de.claims.type.id";

	public static final String REQUEST_TYPE_TYPE_ID_OFI_MISTAKEN =
		"ofi.de.mistaken.payment.type.id";

	public static final String REQUEST_TYPE_TYPE_ID_OFI_RECALL =
		"ofi.de.recall.type.id";

	public static final String REQUEST_TYPE_TYPE_ID_OFI_TRACE =
		"ofi.de.trace.type.id";

	public static final String REQUEST_TYPE_TYPE_ID_RECALL =
		"de.recall.type.id";

	public static final String REQUEST_TYPE_TYPE_ID_TRACE = "de.trace.type.id";

	public static final String RESET_ACTION = "resetAction";

	public static final String SESSION_TIMEOUT = "sessionTimeout";

	public static final String SPACE = " ";

	public static final List<String> SPX_CREDIT = Arrays.asList(
		"50", "51", "52", "53", "54", "55", "56", "57", "58", "59");

	public static final String SPX_DEBIT = "13";

	public static final String SUBMITTED = "Submitted";

	public static final String SUCCESS = "success";

	public static final String TICKET_ID = "ticketId";

	public static final String TICKET_NUMBER = "ticketNumber";

	public static final String TRANS = "trans";

	public static final String TX_ATM_POS_SUBMIT_RENDER = null;

	public static final String TX_RENDER = null;

	public static final String UPDATE_DE_CLAIMS_FORM_ACTION =
		"updateDeClaimsFormAction";

	public static final String UPDATE_DE_MISTAKEN_PAYMENT_FORM_ACTION =
		"updateDeMistakenPaymentFormAction";

	public static final String UPDATE_DE_RECALL_FORM_ACTION =
		"updateDeRecallFormAction";

	public static final String UPDATE_DE_TRACE_FORM_ACTION =
		"updateDeTraceFormAction";

	public static final String UPDATE_OFI_DE_CLAIMS_FORM_ACTION =
		"updateOfiDeClaimsFormAction";

	public static final String UPDATE_OFI_DE_MISTAKEN_PAYMENT_FORM_ACTION =
		"updateOfiDeMistakenPaymentFormAction";

	public static final String UPDATE_OFI_DE_RECALL_FORM_ACTION =
		"updateOfiDeRecallFormAction";

	public static final String UPDATE_OFI_DE_TRACE_FORM_ACTION =
		"updateOfiDeTraceFormAction";

	public static final String UPDATE_TICKET = "updateTicket";

}