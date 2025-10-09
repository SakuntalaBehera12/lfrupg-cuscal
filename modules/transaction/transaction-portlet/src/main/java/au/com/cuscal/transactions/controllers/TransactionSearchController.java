package au.com.cuscal.transactions.controllers;

import static au.com.cuscal.transactions.commons.Constants.BILLING_AMOUNT;
import static au.com.cuscal.transactions.commons.Constants.BUS_DATE;
import static au.com.cuscal.transactions.commons.Constants.COMMA;
import static au.com.cuscal.transactions.commons.Constants.DATE_FORMAT;
import static au.com.cuscal.transactions.commons.Constants.DATE_FORMAT_YYYYMMDD;
import static au.com.cuscal.transactions.commons.Constants.EMPTY_STRING;
import static au.com.cuscal.transactions.commons.Constants.INT_30;
import static au.com.cuscal.transactions.commons.Constants.MASTERCARD_CMS_TransactionBatch;
import static au.com.cuscal.transactions.commons.Constants.MASTERCARD_IPM_TransactionBatch;
import static au.com.cuscal.transactions.commons.Constants.NO;
import static au.com.cuscal.transactions.commons.Constants.SERVICE_CODE;
import static au.com.cuscal.transactions.commons.Constants.SPACE;
import static au.com.cuscal.transactions.commons.Constants.STRING_0;
import static au.com.cuscal.transactions.commons.Constants.STRING_00;
import static au.com.cuscal.transactions.commons.Constants.STRING_0100;
import static au.com.cuscal.transactions.commons.Constants.STRING_0120;
import static au.com.cuscal.transactions.commons.Constants.STRING_24;
import static au.com.cuscal.transactions.commons.Constants.STRING_EQ;
import static au.com.cuscal.transactions.commons.Constants.STRING_LOCAL;
import static au.com.cuscal.transactions.commons.Constants.STRING_T;
import static au.com.cuscal.transactions.commons.Constants.YES;

import au.com.cuscal.common.framework.service.request.domain.Transaction;
import au.com.cuscal.connect.util.resource.commons.LDAPConstants;
import au.com.cuscal.connect.util.resource.dxp.UserUtilImpl;
import au.com.cuscal.connect.util.resource.ldap.LDAPUtil;
import au.com.cuscal.framework.audit.Audit;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.category.AuditCategories;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.audit.impl.log4j.Log4jAuditor;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.webservices.selfservice.AttributesType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;
import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.commons.FunctionCode;
import au.com.cuscal.transactions.commons.Sortable;
import au.com.cuscal.transactions.commons.TransactionAppProperties;
import au.com.cuscal.transactions.commons.Utility;
import au.com.cuscal.transactions.domain.Codes;
import au.com.cuscal.transactions.domain.JsonResponseStatus;
import au.com.cuscal.transactions.domain.TicketDetails;
import au.com.cuscal.transactions.domain.TransactionDetail;
import au.com.cuscal.transactions.domain.TransactionList;
import au.com.cuscal.transactions.forms.RequestContactInformation;
import au.com.cuscal.transactions.forms.ServiceRequestForm;
import au.com.cuscal.transactions.forms.TransactionForm;
import au.com.cuscal.transactions.services.TransactionSearchService;
import au.com.cuscal.transactions.validator.ServiceRequestValidator;
import au.com.cuscal.transactions.validator.TransactionSearchValidator;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.EventMapping;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;
import com.liferay.portletmvc4spring.bind.annotation.ResourceMapping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.displaytag.export.CsvView;
import org.displaytag.model.TableModel;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Controller that returns that Transaction Search View
 *
 * @author Rajni Bharara
 *
 */
@Controller(Constants.TRANSACTION_CONTROLLER)
@RequestMapping("VIEW")
@SessionAttributes(types = TransactionForm.class)
public class TransactionSearchController {

	/**
	 * Method to add a new Service Request for the transaction.
	 *
	 * @param request
	 * @param response
	 * @param serviceRequestForm
	 */
	@ResourceMapping("serviceRequest")
	public void addServiceRequest(
		ResourceRequest request, ResourceResponse response,
		@ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		BindingResult bindingResult) {

		logger.debug("addServiceRequest - start");

		SearchHeader searchHeader = getSearchHeaderData(request);

		TktRequestHeader header = Utility.convertSearchHeaderToTktHeader(
			searchHeader);
		JsonResponseStatus resp = new JsonResponseStatus();

		logger.debug(
			"addServiceRequest - serviceRequestForm firstName: " +
				serviceRequestForm.getContactInformation(
				).getGivenName());
		logger.debug(
			"addServiceRequest - serviceRequestForm cardholderAmount: " +
				serviceRequestForm.getTransactionInformation(
				).getCardholderAmount());

		request.setAttribute(
			"dat",
			serviceRequestForm.getTransactionInformation(
			).getBusinessDate());
		request.setAttribute(
			"trans",
			serviceRequestForm.getTransactionInformation(
			).getCuscalTransactionId());

		try {
			callAddServiceRequestForVisa(
				PortletContext.newContext(response, request),
				serviceRequestForm, bindingResult, header);
		}
		catch (Exception e1) {
			bindingResult.rejectValue(
				"visaTransactionInformation.errorMsg",
				"Error adding Service Request. Please contact CallDirect on 1300 650 501");
			logger.error(
				TransactionSearchController.class.toString() +
					"addServiceRequest - There are errors - Exception e1- ",
				e1);
		}

		if (!bindingResult.hasErrors()) {
			getTransactionTickets(request, response, serviceRequestForm);
		}
		else {
			resp.setStatus("FAIL");
			resp.setResult(bindingResult.getAllErrors());

			ObjectMapper mapper = new ObjectMapper();
			String json;

			try {
				json = mapper.writeValueAsString(resp);
				PrintWriter out = response.getWriter();
				response.setContentType(Constants.JSON2);
				response.setCharacterEncoding(Constants.UTF_8);
				out.print(json);
			}
			catch (JsonGenerationException e) {
				logger.error(
					TransactionSearchController.class.toString() +
						" addServiceRequest - JsonGenerationException ",
					e);
			}
			catch (JsonMappingException e) {
				logger.error(
					TransactionSearchController.class.toString() +
						" addServiceRequest - JsonMappingException ",
					e);
			}
			catch (IOException e) {
				logger.error(
					TransactionSearchController.class.toString() +
						" addServiceRequest - IOException ",
					e);
			}

			logger.error("addServiceRequest - There are errors");
		}

		logger.debug("addServiceRequest - end");
	}

	/**
	 * Method to add a new Service Request for the transaction with attachments.
	 *
	 * @param request
	 * @param response
	 * @param serviceRequestForm
	 */
	@ActionMapping(
		params = {
			Constants.ACTION + Constants.EQUALS +
				"SERVICE_REQUEST_WITH_ATTACHMENTS",
			"create"
		}
	)
	public void addServiceRequestWithAttachments(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		BindingResult bindingResult) {

		logger.debug("addServiceRequestWithAttachments - start");

		SearchHeader searchHeader = getSearchHeaderData(request);

		TktRequestHeader header = Utility.convertSearchHeaderToTktHeader(
			searchHeader);

		if (serviceRequestForm != null) {
			if (serviceRequestForm.getAtmPosClaimInformation(
				).getRequestDisputeType() == null) {

				serviceRequestForm.getAtmPosClaimInformation(
				).setRequestDisputeType(
					ParamUtil.getString(request, "disputeType")
				);
			}

			if (serviceRequestForm.getAtmPosClaimInformation(
				).getRequestPosReason() == null) {

				serviceRequestForm.getAtmPosClaimInformation(
				).setRequestPosReason(
					ParamUtil.getString(request, "posReason")
				);
			}

			if (serviceRequestForm.getAtmPosClaimInformation(
				).getRequestAtmReason() == null) {

				serviceRequestForm.getAtmPosClaimInformation(
				).setRequestAtmReason(
					ParamUtil.getString(request, "atmReason")
				);
			}
		}

		try {
			callAddServiceRequestForAtmPos(
				PortletContext.newContext(response, request),
				serviceRequestForm, bindingResult, header);
		}
		catch (Exception e) {
			bindingResult.rejectValue(
				"atmPosClaimInformation.errorMsg",
				"service.request.atmpos.error");
			logger.error(
				"addServiceRequestWithAttachments - There are errors: ", e);
		}

		if (!bindingResult.hasErrors()) {
			request.setAttribute(Constants.ERRORS, Boolean.FALSE);
		}
		else {
			logger.error("addServiceRequestWithAttachments - There are errors");
			request.setAttribute(Constants.ERRORS, Boolean.TRUE);
		}

		setPagingAndSortingParameters(request, response);

		logger.debug(
			"serviceRequestForm.getAtmPosClaimInformation().getRequestAtmReason() " +
				serviceRequestForm.getAtmPosClaimInformation(
				).getRequestPosReason());

		request.setAttribute("loadTXDetails", Boolean.TRUE);
		request.setAttribute(
			"reasonCode",
			serviceRequestForm.getAtmPosClaimInformation(
			).getRequestPosReason());

		request.setAttribute(Constants.IS_FA, isUserHas2FARole(request));

		response.setRenderParameter(
			Constants.TX_RENDER, Constants.TX_ATM_POS_SUBMIT_RENDER);
		logger.debug("addServiceRequestWithAttachments - end");
	}

	/**
	 * Audit while printing the view of the Transaction detail page
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 * @throws Exception
	 */
	@ResourceMapping(Constants.TX_PRINT)
	public void auditOnPrintTxDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		audit.success(
			response, request, AuditOrigin.PORTAL_ORIGIN,
			AuditCategories.TRANSACTION_DETAIL,
			"The user is printing the transaction details.");
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResourceMapping("requestPrint")
	public void auditOnServiceRequestPrint(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		audit.success(
			response, request, AuditOrigin.PORTAL_ORIGIN,
			AuditCategories.TRANSACTION_DETAIL,
			"The user is printing the Service Request for the transaction.");
	}

	/**
	 * create the export file data.
	 *
	 * @param List<TransactionList>
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook createExcelFile(
		List<TransactionList> lists, Boolean isSwitchDate) {

		logger.debug("createExcelFile - start");
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet("Transaction Report");

		HSSFRow header = sheet.createRow(0);

		header.createCell(
			0
		).setCellValue(
			"PAN"
		);
		header.createCell(
			1
		).setCellValue(
			"Terminal ID"
		);
		header.createCell(
			2
		).setCellValue(
			"Card Acceptor ID"
		);
		header.createCell(
			3
		).setCellValue(
			"Response Code"
		);
		header.createCell(
			4
		).setCellValue(
			"Message Type"
		);
		header.createCell(
			5
		).setCellValue(
			"System Trace"
		);

		if (isSwitchDate) {
			header.createCell(
				6
			).setCellValue(
				"Switch Date/Time"
			);
		}
		else {
			header.createCell(
				6
			).setCellValue(
				"Location Date/Time"
			);
		}

		header.createCell(
			7
		).setCellValue(
			"Description"
		);

		header.createCell(
			8
		).setCellValue(
			"Acquirer Amount"
		);
		header.createCell(
			9
		).setCellValue(
			"Acq Currency"
		);
		header.createCell(
			10
		).setCellValue(
			"Issuer Amount"
		);
		header.createCell(
			11
		).setCellValue(
			"Iss Currency"
		);
		int rowNum = 1;

		for (TransactionList list : lists) {
			HSSFRow row = sheet.createRow(rowNum++);

			row.createCell(
				0
			).setCellValue(
				list.getPan()
			);
			row.createCell(
				1
			).setCellValue(
				list.getTerminalId()
			);
			row.createCell(
				2
			).setCellValue(
				list.getCardAcceptorId()
			);
			row.createCell(
				3
			).setCellValue(
				list.getResponseCode()
			);

			if (StringUtils.isNotBlank(list.getMessageType()) &&
				(list.getMessageType(
				).indexOf(
					"<br/>"
				) != -1)) {

				String msgType = list.getMessageType(
				).replaceAll(
					"<br/>", " "
				);

				row.createCell(
					4
				).setCellValue(
					msgType
				);
			}
			else {
				row.createCell(
					4
				).setCellValue(
					list.getMessageType()
				);
			}

			row.createCell(
				5
			).setCellValue(
				list.getSystemTrace()
			);
			row.createCell(
				6
			).setCellValue(
				Utility.formatDateToString(
					list.getDateTime(), Constants.DATE_TIME_FORMAT_24HR)
			);
			row.createCell(
				7
			).setCellValue(
				list.getDescription()
			);
			row.createCell(
				8
			).setCellValue(
				"$" +
					Utility.formatNumberToString(
						Utility.Round(list.getAmount(), 2), "0.00")
			);
			row.createCell(
				9
			).setCellValue(
				list.getCurrencyCodeAcq()
			);
			row.createCell(
				10
			).setCellValue(
				"$" +
					Utility.formatNumberToString(
						Utility.Round(list.getCardHolderBillAmount(), 2),
						"0.00")
			);
			row.createCell(
				11
			).setCellValue(
				list.getCurrencyCodeIss()
			);
		}

		logger.debug("createExcelFile - end");

		return workbook;
	}

	/**
	 * create the Message code Html From Codes response Object
	 *
	 * @param Map<String, Set<Codes>>
	 * @param ResourceRequest
	 * @return String
	 */
	public String createMessageCodeHtmlFromObj(
		Map<String, Set<Codes>> codes, ResourceRequest request) {

		logger.debug("createMessageCodeHtmlFromObj - start");
		StringBuffer returnStr = new StringBuffer(
			"<table class='tx-details' " +
				"cellpadding='0' cellspacing='0' border='0' width='100%'>" +
					"<tr><td class='tx-titles message-code' align='left'>Code</td>" +
						"<td class='tx-titles'>Description</td></tr>" +
							"</table>");

		Set<String> grpCodesSet = codes.keySet();

		for (Iterator<String> iterator = grpCodesSet.iterator();
			 iterator.hasNext();) {

			String codeGrpDesc = iterator.next();

			returnStr.append(
				"<fieldset><legend>" + codeGrpDesc + "</legend>" +
					"<table class='tx-details' cellpadding='0' cellspacing='0' border='0' width='100%'>");

			int i = 0;
			Set<Codes> codesList = codes.get(codeGrpDesc);

			for (Codes codes2 : codesList) {
				if ((i % 2) == 1) {
					returnStr.append("<tr class='alt'>");
				}
				else {
					returnStr.append("<tr>\n\t");
				}

				returnStr.append(
					"<td class='message-code'>" + codes2.getCode() + "</td> " +
						"<td>" + codes2.getDescription() + "</td></tr>");
				i++;
			}

			returnStr.append("</table></fieldset>");
		}

		logger.debug("createMessageCodeHtmlFromObj - end");

		return returnStr.toString();
	}

	/**
	 * create the ResponseCode Html From Codes response Object
	 *
	 * @param List<Codes>
	 * @return String
	 */
	public String createResponseCodeHtmlFromCodesObj(List<Codes> codes) {
		StringBuffer returnStr = new StringBuffer(
			"<tr><td class='tx-titles' align='left'>Code</td>" +
				"<td class='tx-titles'>Description</td></tr>");
		int i = 0;

		for (Codes codes2 : codes) {
			if ((i % 2) == 1) {
				returnStr.append("<tr class='alt'>");
			}
			else {
				returnStr.append("<tr>");
			}

			returnStr.append(
				"<td><a href=\"javascript:selectMsgOrRespCodeId('" +
					codes2.getCode() + "','response');\">" + codes2.getCode() +
						"</a></td>" + "<td>" + codes2.getDescription() +
							"</td></tr>");
			i++;
		}

		return returnStr.toString();
	}

	/**
	 * create the export file for transaction list
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 */
	@ResourceMapping("exportcsv")
	public void exportDataCsv(
		ResourceRequest request, ResourceResponse response) {

		// Replicate export logic here from displaytag

		logger.debug("exportDataCsv - start");
		TableModel exportModel = (TableModel)request.getPortletSession(
		).getAttribute(
			request.getParameter("tableId"), PortletSession.APPLICATION_SCOPE
		);

		CsvView exportCsv = new CsvView();

		exportCsv.setParameters(exportModel, true, true, false);

		try {
			response.setContentType(Constants.TEXT_CSV);
			response.setProperty(
				"Content-Disposition",
				"attachment;filename=\"" + "export.csv" + "\"");
			Writer writer = response.getWriter();
		}
		catch (IOException e) {
			logger.error(
				"Exception while creating export file in cvs : " +
					e.getMessage(),
				e);
		}

		logger.debug("exportDataCsv - end");
	}

	/**
	 * create the export file for transaction list and send to portlet
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 */
	@ResourceMapping(Constants.TX_EXPORT_LIST)
	@SuppressWarnings("unchecked")
	public void exportDataXls(
		ResourceRequest request, ResourceResponse response) {

		logger.debug("exportDataXls - start");
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		List<TransactionList> lists = null;
		HSSFWorkbook workbook = null;
		Boolean isSwitchDate = false;

		if (null != session.getAttribute(Constants.TX_LIST)) {
			lists = (List<TransactionList>)session.getAttribute(
				Constants.TX_LIST);

			if (null != session.getAttribute(Constants.SWITCH_DATE)) {
				isSwitchDate = (Boolean)session.getAttribute(
					Constants.SWITCH_DATE);
			}

			workbook = createExcelFile(lists, isSwitchDate);

			try {
				audit.success(
					response, request, AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_SEARCH,
					"	The Export Transactions List size is  " + lists.size());
			}
			catch (Exception ae) {
				logger.error(
					"Unable to write audit trail for Export Transactions List Success : " +
						ae.getMessage() + ", original error: " +
							ae.getMessage());
			}

			try {
				OutputStream output = response.getPortletOutputStream();
				response.setContentType(Constants.APPLICATION_VND_MS_EXCEL);
				response.setProperty(
					"Content-Disposition",
					"attachment;filename=\"" + "export.xls" + "\"");
				workbook.write(output);

				output.flush();
				output.close();
			}
			catch (IOException e) {
				logger.error(
					"Exception while Exporting Transactions List in " +
						"xls format, exception message is  " + e.getMessage());

				try {
					audit.fail(
						response, request, AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.TRANSACTION_SEARCH,
						" Export Transactions, the Exception: " +
							e.getMessage());
				}
				catch (Exception ae) {
					logger.error(
						"Unable to write audit trail for Export " +
							"Transactions List failure: " + ae.getMessage() +
								", original error: " + e.getMessage());
				}
			}
		}

		logger.debug("exportDataXls - end");
	}

	/**
	 * Mapped the Event sent from Quick Search Page
	 *
	 * @param EventRequest
	 * @param EventResponse
	 * @param ModelMap
	 * @param SessionStatus
	 * @return void
	 */
	@EventMapping("{http://liferay.com/events}forms.TransactionForm")
	public void findTransactionListForQuickSearch(
		EventRequest eventRequest, EventResponse eventResponse, ModelMap map,
		SessionStatus sessionStatus) {

		Event event = eventRequest.getEvent();
		logger.debug(
			"findTransactionListForQuickSearch - in event mapping event is: " +
				event);
		TransactionForm transactionForm = (TransactionForm)event.getValue();

		map.put("TransactionForm", transactionForm);
		logger.debug(
			"findTransactionListForQuickSearch - getPanBin value is: " +
				transactionForm.getPanBin());
		eventResponse.setRenderParameter(
			Constants.TX_RENDER, Constants.TX_SEARCH_RESULT);
		sessionStatus.setComplete();
	}

	/**
	 * Shows the search results of the Transaction
	 *
	 * @param TransactionForm
	 * @param BindingResult
	 * @param ActionResponse
	 * @param SessionStatus
	 * @param ActionRequest
	 * @return void
	 */
	@ActionMapping(
		params = Constants.TX_ACTION + Constants.EQUALS + Constants.TX_LIST
	)
	public void findTransactionSearchList(
		@ModelAttribute TransactionForm transactionForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.debug("findTransactionSearchList - start ");

		perfLogger_.debug("findTransactionSearchList - start");
		boolean isValidation = false;
		boolean isNotMoreOrg = false;
		boolean isDataNotNull = false;
		boolean isUserNotNull = false;
		boolean isSwitchDate = false;

		request.setAttribute(Constants.IS_FA, isUserHas2FARole(request));

		if (!StringUtils.isBlank(transactionForm.getPanBin())) {
			String panOrBin = Utility.removeAllSpacesFromPanorBin(
				transactionForm.getPanBin(
				).trim());

			transactionForm.setPanBin(panOrBin);
		}

		if (!StringUtils.isBlank(transactionForm.getDateType())) {
			if (transactionForm.getDateType(
				).equals(
					"system"
				)) {

				isSwitchDate = true;
			}
		}

		if (!StringUtils.isBlank(transactionForm.getSearchView())) {
			if (transactionForm.getSearchView(
				).equals(
					Constants.QUICK
				)) {

				response.setRenderParameter("searchView", Constants.QUICK);
			}
		}

		transactionSearchValidator.validate(transactionForm, bindingResult);
		perfLogger_.debug("findTransactionSearchList - search form validated");

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession httpSession = httpServletRequest.getSession(true);
		PortletSession portletSession = request.getPortletSession();

		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				SearchHeader searchHeader = getSearchHeaderData(request);
				isUserNotNull = true;

				if ((null != searchHeader) &&
					(null != searchHeader.getUserOrgId())) {

					isNotMoreOrg = true;

					if (!bindingResult.hasErrors()) {
						isValidation = true;

						httpSession.setAttribute(
							Constants.SWITCH_DATE, isSwitchDate);
						portletSession.setAttribute(
							Constants.SWITCH_DATE, isSwitchDate,
							PortletSession.APPLICATION_SCOPE);

						perfLogger_.debug(
							"findTransactionSearchList - calling web service");
						List<Object> listSearch =
							transactionSearchService.getTransactionListOnSearch(
								transactionForm, searchHeader,
								PortletContext.newContext(response, request));

						perfLogger_.debug(
							"findTransactionSearchList - web service returned");
						@SuppressWarnings("unchecked")
						List<TransactionList> listForms =
							(List<TransactionList>)listSearch.get(0);

						String webServiceStatusMsg = (String)listSearch.get(1);

						if ((null != listForms) && (0 < listForms.size())) {
							logger.debug(
								"findTransactionSearchList - list size " +
									"in controller is: " + listForms.size());
							logger.debug(
								"findTransactionSearchList - setting " +
									"the response render listForms size is  " +
										listForms.size());
							isDataNotNull = true;
							request.setAttribute(
								Constants.TX_LIST_REQ, listForms);

							if (listForms.get(
									0
								).isMoreRecAvail()) {

								logger.debug(
									"findTransactionSearchList - dates" +
										" range have more than 250 records for " +
											"transaction search ");
								request.setAttribute(
									Constants.MORE_DATA_MSG, Boolean.TRUE);
							}
						}

						if (!Utility.
								anyOneOfToDateAndFromDateAreNOT18MonthPastDate(
									transactionForm)) {

							logger.debug(
								"findTransactionSearchList - dates are" +
									" past 18 months ");
							request.setAttribute(
								Constants.DATE_WARNING_MSG, Boolean.TRUE);
						}

						response.setRenderParameter(
							Constants.TX_RENDER, Constants.TX_SEARCH_RESULT);
						request.setAttribute(Constants.FROM_FA, Boolean.FALSE);

						if (StringUtils.isNotBlank(webServiceStatusMsg)) {
							if (Constants.INCOMPLETE.equalsIgnoreCase(
									webServiceStatusMsg)) {

								logger.debug(
									"[TransactionSearchController.showTransactionSearchResult] Either CODS or Alaric Database is done for transaction search ");
								request.setAttribute(
									Constants.DATA_BASE_ERROR, Boolean.TRUE);
							}
							else if (Constants.ERROR.equalsIgnoreCase(
										webServiceStatusMsg)) {

								request.setAttribute(
									Constants.OTHER_ERROR, Boolean.TRUE);
							}
							else if (Constants.STATUS_SQL_TIMEOUT.
										equalsIgnoreCase(webServiceStatusMsg)) {

								request.setAttribute(
									Constants.STATUS_SQL_TIMEOUT, Boolean.TRUE);
							}
						}
					}
				}
			}

			if (!isUserNotNull) {
				logger.warn(
					"User tried to search transactions after logging " +
						"out or their session timed out.");
				request.setAttribute(Constants.TX_LIST_REQ, null);
				response.setRenderParameter(
					Constants.TX_RENDER, Constants.TX_ERROR_PAGE);
			}
			else if (!isNotMoreOrg) {
				logger.warn(
					"User tried to search transactions and has none or more " +
						"than one organisations assigned to them.");
				request.setAttribute(Constants.MORE_ORG, Boolean.TRUE);
				request.setAttribute(Constants.TX_LIST_REQ, null);
				response.setRenderParameter(
					Constants.TX_RENDER, Constants.TX_SEARCH_RESULT);
			}
			else if (!isValidation) {
				logger.warn(
					"User tried to search transactions have validation " +
						"issues.");
				request.setAttribute(Constants.VALIDATION, Constants.FALSE);
				request.setAttribute(Constants.TX_LIST_REQ, null);
				response.setRenderParameter(
					Constants.TX_RENDER, Constants.TX_SEARCH_RESULT);
			}
			else {
				if (!isDataNotNull) {
					logger.warn(
						"User tried to search transactions have null " +
							"object from web service call.");
					request.setAttribute(Constants.TX_LIST_REQ, null);
					response.setRenderParameter(
						Constants.TX_RENDER, Constants.TX_SEARCH_RESULT);
				}
			}

			httpSession.setAttribute(Constants.SORT_ORDER, null);
			httpSession.setAttribute(Constants.TX_LIST, null);
			portletSession.setAttribute(
				Constants.SORT_ORDER, null, PortletSession.APPLICATION_SCOPE);
			portletSession.setAttribute(
				Constants.TX_LIST, null, PortletSession.APPLICATION_SCOPE);

			portletSession.setAttribute(
				_TRANSACTION_FORM, transactionForm,
				PortletSession.APPLICATION_SCOPE);

			sessionStatus.setComplete();
		}
		catch (Exception e) {
			logger.error(
				"findTransactionSearchList - EXCEPTION CAME ERROR MSG " +
					"e.getCause()  is " + e.getMessage(),
				e);
			response.setRenderParameter(
				Constants.TX_RENDER, Constants.TX_ERROR_PAGE);
		}

		logger.debug("findTransactionSearchList - end");
		perfLogger_.debug("findTransactionSearchList - end");
	}

	/**
	 * Shows the search results of the Transaction
	 * @param TransactionForm
	 * @param ActionResponse
	 * @param SessionStatus
	 * @param ActionRequest
	 * @return void
	 */
	@ActionMapping(
		params = Constants.TX_ACTION + Constants.EQUALS + Constants.TX_2FA
	)
	public void forwardTo2FAPageAndBackToTxDetails(
		@ModelAttribute TransactionForm transactionForm,
		ActionResponse response, SessionStatus sessionStatus,
		ActionRequest request) {

		logger.debug("forwardTo2FAPageAndBackToTxDetails - start");
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession httpSession = httpServletRequest.getSession();

		PortletSession portletSession = request.getPortletSession();

		request.setAttribute(Constants.IS_FA, isUserHas2FARole(request));

		String userSteppedupSession = GetterUtil.getString(
			portletSession.getAttribute(
				Constants._USER_STEPPEDUP, PortletSession.APPLICATION_SCOPE));

		logger.debug(
			"forwardTo2FAPageAndBackToTxDetails - The session " +
				"steppedup attribute is: " + userSteppedupSession);

		//Always set the session to false if the _USER_STEPPEDUP session variable is null.

		if (userSteppedupSession.isEmpty()) {
			logger.warn(
				"forwardTo2FAPageAndBackToTxDetails - _USER_STEPPEDUP" +
					" session variable is null. Setting to false as default.");
			httpSession.setAttribute(
				Constants._USER_STEPPEDUP, Constants.FALSE);

			portletSession.setAttribute(
				Constants._USER_STEPPEDUP, Constants.FALSE,
				PortletSession.APPLICATION_SCOPE);
		}

		Boolean checkServiceRequestRole = doesUserHaveServiceRequestRole(
			request);

		request.setAttribute("serviceRequestRole", checkServiceRequestRole);
		logger.debug(
			"forwardTo2FAPageAndBackToTxDetails - The session attribute " +
				"after checkServiceRequestRole is: " + userSteppedupSession);

		String userCancelSession = GetterUtil.getString(
			portletSession.getAttribute(
				Constants.USER_CANCEL, PortletSession.APPLICATION_SCOPE));

		logger.debug(
			"forwardTo2FAPageAndBackToTxDetails - " +
				"session.getAttribute(Constants.USER_CANCEL): " +
					userCancelSession);

		setPagingAndSortingParameters(request, response);

		if (Constants.CANCEL.equals(userCancelSession)) {
			logger.debug(
				"forwardTo2FAPageAndBackToTxDetails - The session " +
					"attribute after checking Cancel is: " +
						userSteppedupSession);

			portletSession.setAttribute(
				Constants.USER_CANCEL, Constants.FALSE,
				PortletSession.APPLICATION_SCOPE);

			httpSession.setAttribute(Constants.USER_CANCEL, null);

			// SearchHeader searchHeader = getSearchHeaderData(request);

			if (null != httpSession.getAttribute(Constants.TX_LIST)) {
				@SuppressWarnings("unchecked")
				List<TransactionList> listForms =
					(List<TransactionList>)httpSession.getAttribute(
						Constants.TX_LIST);

				request.setAttribute(Constants.TX_LIST_REQ, listForms);
			}

			httpSession.setAttribute(Constants._RETURN_URL, null);

			portletSession.setAttribute(
				Constants._RETURN_URL, null, PortletSession.APPLICATION_SCOPE);

			logger.debug(
				"forwardTo2FAPageAndBackToTxDetails - steppedUp value" +
					" in session is: " + userSteppedupSession);
			request.setAttribute(Constants.ACTION, Constants._2FA_ACTION);
			response.getRenderParameters(
			).setValue(
				Constants.TX_RENDER, Constants.TX_2FA_RENDER
			);
		}
		else if (Constants.FALSE.equals(userSteppedupSession) ||
				 Constants.ERROR.equals(userSteppedupSession)) {

			logger.debug(
				"forwardTo2FAPageAndBackToTxDetails - The session " +
					"attribute after checking ERROR and in case of success is: " +
						userSteppedupSession);

			String url = PortalUtil.getCurrentURL(httpServletRequest);

			httpSession.setAttribute(Constants._RETURN_URL, url);

			portletSession.setAttribute(
				Constants._RETURN_URL, url, PortletSession.APPLICATION_SCOPE);

			try {
				logger.debug(
					"forwardTo2FAPageAndBackToTxDetails - The " +
						"session attribute before calling sendRedirect to 2FA page is: " +
							userSteppedupSession);
				response.sendRedirect("/2fa");
			}
			catch (IOException ioe) {
				logger.error(
					"Exception while sending the url to 2 FA page from transaction : " +
						ioe.getMessage(),
					ioe);
			}
		}
		else {
			logger.debug(
				"forwardTo2FAPageAndBackToTxDetails - In else part after the 2fa: <" +
					userSteppedupSession + ">");

			// SearchHeader searchHeader = getSearchHeaderData(request);

			if (null != httpSession.getAttribute(Constants.TX_LIST)) {
				@SuppressWarnings("unchecked")
				List<TransactionList> listForms =
					(List<TransactionList>)httpSession.getAttribute(
						Constants.TX_LIST);

				request.setAttribute(Constants.TX_LIST_REQ, listForms);
			}

			if (userSteppedupSession.isEmpty()) {
				httpSession.setAttribute(
					Constants._USER_STEPPEDUP, Constants.FALSE);

				portletSession.setAttribute(
					Constants._USER_STEPPEDUP, Constants.FALSE,
					PortletSession.APPLICATION_SCOPE);
			}

			logger.debug(
				"forwardTo2FAPageAndBackToTxDetails - steppedup " +
					"value is: " + userSteppedupSession);
			request.setAttribute(Constants.ACTION, Constants._2FA_ACTION);
			logger.debug(
				"forwardTo2FAPageAndBackToTxDetails - The session " +
					"attribute before calling TX_2FA_RENDER is: " +
						userSteppedupSession);
			response.getRenderParameters(
			).setValue(
				Constants.TX_RENDER, Constants.TX_2FA_RENDER
			);
		}

		logger.debug("forwardTo2FAPageAndBackToTxDetails - end");
	}

	/**
	 * Creates the Transaction Form default values
	 *
	 * @return TransactionForm
	 */
	@ModelAttribute(Constants.TRANSACTION_FORM)
	public TransactionForm getCommandObject() {
		TransactionForm transactionForm =
			transactionSearchService.createTransactionFormObject();
		String[] allMessageCodeOptions =
			transactionSearchService.getAllMessageCodeTypeForUIDropDown();

		if (null != allMessageCodeOptions) {
			transactionForm.setMsgTypeOptionDisplay(allMessageCodeOptions);
		}
		else {
			transactionForm.setMsgTypeOptionDisplay(null);
		}

		return transactionForm;
	}

	@ModelAttribute("serviceRequestForm")
	public ServiceRequestForm getCommandObjectServiceRequestForm() {
		return new ServiceRequestForm();
	}

	/**
	 * Get the message code for transaction search page page
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 * @throws Exception
	 */
	@ResourceMapping(Constants.TX_MSG)
	public void getMessageCodeDescrpAutoList(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.debug("getMessageCodeDescrpAutoList - start");
		PrintWriter out = response.getWriter();

		try {
			Map<String, Set<Codes>> codes =
				transactionSearchService.getMessageCodeDescription(
					PortletContext.newContext(response, request));

			String finalHtmlStr = createMessageCodeHtmlFromObj(codes, request);

			out.print(finalHtmlStr);
		}
		catch (Exception e) {
			logger.error(
				"Exception happened while trying to find the Message " +
					"code list from web service : exception msg is : " +
						e.getMessage());
		}

		logger.debug("getMessageCodeDescrpAutoList - end");
	}

	/**
	 * Next page for the view of the Transaction detail page
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 * @throws Exception
	 */
	@ResourceMapping(Constants.TX_NEXT)
	@SuppressWarnings("unchecked")
	public void getNextTxDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.debug("getNextTxDetails - start");

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		PortletSession portletSession = request.getPortletSession();

		List<TransactionList> lists = null;
		TransactionDetail transactionDetail = new TransactionDetail();

		if (!StringUtils.isBlank(
				session.getAttribute(
					Constants.CURR_ROW_NUM
				).toString()) &&
			!Constants.NULL.equalsIgnoreCase(
				session.getAttribute(
					Constants.CURR_ROW_NUM
				).toString())) {

			int curRowNum = Integer.valueOf(
				session.getAttribute(
					Constants.CURR_ROW_NUM
				).toString());

			if (null != session.getAttribute(Constants.TX_LIST)) {
				lists = (List<TransactionList>)session.getAttribute(
					Constants.TX_LIST);

				if (curRowNum != lists.size()) {
					curRowNum = curRowNum + 1;

					TransactionList transactionList = lists.get(curRowNum - 1);

					transactionDetail.setBusDate(
						transactionList.getBusniessDate());
					transactionDetail.setTransactionId(
						transactionList.getTransactionId());
					transactionDetail.setDataSrc(
						transactionList.getDataSource());
					transactionDetail.setNextRowNum(curRowNum + 1);

					session.setAttribute(
						Constants.BUS_DATE, transactionDetail.getBusDate());
					session.setAttribute(Constants.CURR_ROW_NUM, curRowNum);
					session.setAttribute(
						Constants.TX_ID, transactionDetail.getTransactionId());
					session.setAttribute(
						Constants.TX_SRC, transactionDetail.getDataSrc());

					String userSteppedUpSession = GetterUtil.getString(
						session.getAttribute(Constants._USER_STEPPEDUP));
					String userSteppedUpPortletSession = GetterUtil.getString(
						portletSession.getAttribute(
							Constants._USER_STEPPEDUP,
							PortletSession.APPLICATION_SCOPE));

					if (Constants.TRUE.equals(userSteppedUpSession) ||
						Constants.TRUE.equals(userSteppedUpPortletSession)) {

						logger.debug(
							"getNextTxDetails - " +
								"session.getAttribute(_USER_STEPPEDUP) is: " +
									portletSession.getAttribute(
										Constants._USER_STEPPEDUP,
										PortletSession.APPLICATION_SCOPE));
						transactionDetail.setUserStepUp(true);
					}
					else {
						logger.debug(
							"getNextTxDetails - " +
								"session.getAttribute(_USER_STEPPEDUP) final " +
									"else false");
						transactionDetail.setUserStepUp(false);
					}

					try {
						SearchHeader searchHeader = getSearchHeaderData(
							request);

						if ((null != searchHeader) &&
							(searchHeader.getUserOrgId() != null)) {

							transactionDetail =
								transactionSearchService.
									getTransactionDetailByTxIdBusDateAndSrc(
										transactionDetail, searchHeader,
										PortletContext.newContext(
											response, request));

							Transaction transaction =
								createSessionTransactionObject(
									transactionDetail);
							//if a valid service request then only make the call to get additional transaction info

							if (transactionDetail.isValidServiceRequest()) {
								logger.debug(
									"getTransactionDetails - ExternalTransactionId : " +
										transactionDetail.
											getExternalTransactionId() +
												" and OriginIchName : " +
													transaction.
														getOriginIchName());

								// additional transaction attributes like UCAf,
								// ECI, CSBD, Chip Present, Track 2 Data Service
								// code

								if (StringUtils.isNotBlank(
										transactionDetail.
											getExternalTransactionId()) &&
									(MASTERCARD_IPM_TransactionBatch.
										equalsIgnoreCase(
											transaction.getOriginIchName()) ||
									 MASTERCARD_CMS_TransactionBatch.
										 equalsIgnoreCase(
											 transaction.getOriginIchName()))) {

									transaction =
										setAdditionalTransactionAttributes(
											request, response, searchHeader,
											transactionDetail.
												getExternalTransactionId(),
											transaction);
									transactionDetail.
										setUCAFCollectionIndicator(
											transaction.
												getUCAFCollectionIndicator());
									transactionDetail.
										setUCAFCollectionIndicatorDescription(
											Utility.
												getUCAFCollectionIndicatorDescription(
													transaction.
														getUCAFCollectionIndicator()));
									transactionDetail.
										setElectronicCommerceIndciator(
											transaction.
												getElectronicCommerceIndciator());
									transactionDetail.
										setCentralSiteBusinessDate(
											transaction.
												getCentralSiteBusinessDate());
									transactionDetail.setChipCardPresent(
										transaction.getChipCardPresent());
									transactionDetail.
										setElectronicCommerceIndciator(
											transaction.
												getElectronicCommerceIndciator());
									transactionDetail.setTrack2DataServiceCode(
										transaction.getTrack2DataServiceCode());
								}
							}

							transactionDetail.setCardControlRejectCode(
								getCardControlRejectCode(
									transactionDetail.getCustomData()));
							ByteArrayOutputStream bos =
								new ByteArrayOutputStream();

							ObjectOutputStream os = new ObjectOutputStream(bos);

							os.writeObject(transaction);
							os.close();
							session.setAttribute(
								Constants.TRANSACTION_DETAIL,
								bos.toByteArray());

							ObjectMapper mapper = new ObjectMapper();

							String json = mapper.writeValueAsString(
								transactionDetail);
							PrintWriter out = response.getWriter();
							response.setContentType(Constants.JSON2);
							response.setCharacterEncoding(Constants.UTF_8);
							out.print(json);
						}
						else {
							logger.error(
								"getNextTxDetails - The Search header " +
									"is null or  user is not logged in or org " +
										"short name is null");
						}
					}
					catch (Exception e) {
						logger.error(
							"getNextTxDetails - Error getting next transaction details. " +
								e.getMessage(),
							e);
					}
				}
			}
		}

		logger.debug("getNextTxDetails - end");
	}

	/**
	 * organisationIdOverride object getter
	 */
	public String getOrganisationIdOverride() {
		return organisationIdOverride;
	}

	/**
	 * Get the organisation short name by liferay organisation id
	 *
	 * @param String
	 * @param portletRequest
	 * @return String
	 */
	public String getOrgShortName(
		String liferayOrgId, PortletRequest portletRequest) {

		logger.debug("getOrgShortName - start, liferayOrgId=" + liferayOrgId);

		if (StringUtils.isNotEmpty(organisationIdOverride)) {
			logger.debug(
				"getOrgShortName - end, organisationIdOverride has been set.  Using " +
					organisationIdOverride);
			liferayOrgId = organisationIdOverride;
		}

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		HttpSession session = httpServletRequest.getSession();

		String orgShortName = (String)session.getAttribute(
			Constants.ORG_SHORT_NAME);

		if (orgShortName == null) {
			orgShortName = transactionSearchService.getOrgShortName(
				liferayOrgId, portletRequest);

			if (StringUtils.isNotBlank(orgShortName)) {
				session.setAttribute(Constants.ORG_SHORT_NAME, orgShortName);
			}
			else {
				logger.warn(
					"getOrgShortName - Could not find orgShortName for liferayOrgId=" +
						liferayOrgId);
			}
		}

		logger.debug(
			"getOrgShortName - end, returning organisation short name as " +
				orgShortName);

		return orgShortName;
	}

	/**
	 * previous page for the view of the Transaction detail page
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 * @throws Exception
	 */
	@ResourceMapping(Constants.TX_PREV)
	@SuppressWarnings("unchecked")
	public void getPrevTxDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.debug("getPrevTxDetails - start");

		TransactionDetail transactionDetail = new TransactionDetail();
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		PortletSession portletSession = request.getPortletSession();
		List<TransactionList> lists = null;

		if (!StringUtils.isBlank(
				session.getAttribute(
					Constants.CURR_ROW_NUM
				).toString()) &&
			!Constants.NULL.equalsIgnoreCase(
				session.getAttribute(
					Constants.CURR_ROW_NUM
				).toString())) {

			int curRowNum = Integer.valueOf(
				session.getAttribute(
					Constants.CURR_ROW_NUM
				).toString());

			if (curRowNum != 1) {
				curRowNum = curRowNum - 1;

				if (null != session.getAttribute(Constants.TX_LIST)) {
					lists = (List<TransactionList>)session.getAttribute(
						Constants.TX_LIST);
					TransactionList transactionList = lists.get(curRowNum - 1);

					transactionDetail.setBusDate(
						transactionList.getBusniessDate());
					logger.debug(
						"getPrevTxDetails - getBusniessDate: " +
							transactionList.getBusniessDate());
					transactionDetail.setTransactionId(
						transactionList.getTransactionId());
					transactionDetail.setDataSrc(
						transactionList.getDataSource());

					transactionDetail.setNextRowNum(curRowNum + 1);

					logger.debug(
						"getPrevTxDetails - setting the session valuess attributes.");
					session.setAttribute(
						Constants.TX_ID, transactionDetail.getTransactionId());
					session.setAttribute(
						Constants.BUS_DATE, transactionDetail.getBusDate());
					session.setAttribute(
						Constants.TX_SRC, transactionDetail.getDataSrc());
					session.setAttribute(Constants.CURR_ROW_NUM, curRowNum);

					String userSteppedUpSession = GetterUtil.getString(
						session.getAttribute(Constants._USER_STEPPEDUP));
					String userSteppedUpPortletSession = GetterUtil.getString(
						portletSession.getAttribute(
							Constants._USER_STEPPEDUP,
							PortletSession.APPLICATION_SCOPE));

					if (Constants.TRUE.equals(userSteppedUpSession) ||
						Constants.TRUE.equals(userSteppedUpPortletSession)) {

						logger.debug(
							"getNextTxDetails - " +
								"session.getAttribute(_USER_STEPPEDUP) is: " +
									portletSession.getAttribute(
										Constants._USER_STEPPEDUP,
										PortletSession.APPLICATION_SCOPE));
						transactionDetail.setUserStepUp(true);
					}
					else {
						logger.debug(
							"getNextTxDetails - " +
								"session.getAttribute(_USER_STEPPEDUP) final " +
									"else false");
						transactionDetail.setUserStepUp(false);
					}

					try {
						SearchHeader searchHeader = getSearchHeaderData(
							request);

						if ((null != searchHeader) &&
							(searchHeader.getUserOrgId() != null)) {

							transactionDetail =
								transactionSearchService.
									getTransactionDetailByTxIdBusDateAndSrc(
										transactionDetail, searchHeader,
										PortletContext.newContext(
											response, request));
							Transaction transaction =
								createSessionTransactionObject(
									transactionDetail);
							//if a valid service request then only make the call to get additional transaction info

							if (transactionDetail.isValidServiceRequest()) {
								logger.debug(
									"getTransactionDetails - ExternalTransactionId : " +
										transactionDetail.
											getExternalTransactionId() +
												" and OriginIchName : " +
													transaction.
														getOriginIchName());
								//additional transaction attributes like UCAf, CSBD, Chip Present, Track 2 Data Service code

								if (StringUtils.isNotBlank(
										transactionDetail.
											getExternalTransactionId()) &&
									(MASTERCARD_IPM_TransactionBatch.
										equalsIgnoreCase(
											transaction.getOriginIchName()) ||
									 MASTERCARD_CMS_TransactionBatch.
										 equalsIgnoreCase(
											 transaction.getOriginIchName()))) {

									transaction =
										setAdditionalTransactionAttributes(
											request, response, searchHeader,
											transactionDetail.
												getExternalTransactionId(),
											transaction);
									transactionDetail.
										setUCAFCollectionIndicator(
											transaction.
												getUCAFCollectionIndicator());
									transactionDetail.
										setUCAFCollectionIndicatorDescription(
											Utility.
												getUCAFCollectionIndicatorDescription(
													transaction.
														getUCAFCollectionIndicator()));
									transactionDetail.
										setCentralSiteBusinessDate(
											transaction.
												getCentralSiteBusinessDate());
									transactionDetail.setChipCardPresent(
										transaction.getChipCardPresent());
									transactionDetail.
										setElectronicCommerceIndciator(
											transaction.
												getElectronicCommerceIndciator());
									transactionDetail.setTrack2DataServiceCode(
										transaction.getTrack2DataServiceCode());
								}
							}

							transactionDetail.setCardControlRejectCode(
								getCardControlRejectCode(
									transactionDetail.getCustomData()));
							ByteArrayOutputStream bos =
								new ByteArrayOutputStream();

							ObjectOutputStream os = new ObjectOutputStream(bos);

							os.writeObject(transaction);
							os.close();
							session.setAttribute(
								Constants.TRANSACTION_DETAIL,
								bos.toByteArray());

							ObjectMapper mapper = new ObjectMapper();

							String json = mapper.writeValueAsString(
								transactionDetail);
							PrintWriter out = response.getWriter();
							response.setContentType(Constants.JSON2);
							response.setCharacterEncoding(Constants.UTF_8);
							out.print(json);
						}
						else {
							logger.error(
								"getPrevTxDetails - The Search header" +
									" is null or  user is not logged in");
						}
					}
					catch (Exception e) {
						logger.error(
							"getPrevTxDetails - Could not get " + e.getCause());
					}
				}
			}
		}

		logger.debug("getPrevTxDetails - end");
	}

	/**
	 * Get the response code for transaction search page page
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 * @throws Exception
	 */
	@ResourceMapping(Constants.TX_RESP)
	public void getResponseCodeDescrpAutoList(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.debug(" getResponseCodeDescrpAutoList - start");
		PrintWriter out = response.getWriter();

		try {
			List<Codes> codes =
				transactionSearchService.getResponseCodeDescription(
					PortletContext.newContext(response, request));

			String finalHtmlStr = createResponseCodeHtmlFromCodesObj(codes);

			out.print(finalHtmlStr);
		}
		catch (Exception e) {
			logger.error(
				"Exception while getting the Response code list, exception message is " +
					e.getMessage());
		}

		logger.debug(" getResponseCodeDescrpAutoList - end");
	}

	/**
	 * create the searchHeader object for web service.
	 *
	 * @param PortletRequest
	 * @return SearchHeader
	 */
	public SearchHeader getSearchHeaderData(PortletRequest request) {
		logger.debug("getSearchHeaderData - start");
		SearchHeader searchHeader = null;
		User user = null;

		try {
			user = PortalUtil.getUser(request);
			String screenName = user.getScreenName();

			if (null != user) {
				searchHeader = new SearchHeader();
				Long userId = user.getUserId();

				List<Organization> organizations =
					OrganizationLocalServiceUtil.getUserOrganizations(userId);

				if ((null != organizations) && (1 == organizations.size())) {
					String userOrgName = organizations.get(
						0
					).getName();
					String orgShortName = getOrgShortName(
						Long.valueOf(
							organizations.get(
								0
							).getOrganizationId()
						).toString(),
						request);

					if (StringUtils.isNotBlank(orgShortName)) {
						searchHeader.setUserOrgId(orgShortName);
						logger.debug(
							"getSearchHeaderData - orgShortName is : " +
								orgShortName);
					}
					else {
						logger.error(
							"The Search header is not null but org short name is null for org id ");
						searchHeader.setUserOrgId(null);
					}

					searchHeader.setUserOrgName(userOrgName);
					searchHeader.setUserId(user.getScreenName());
					searchHeader.setOrigin(AuditOrigin.PORTAL_ORIGIN);
					searchHeader.setPageNumber(Constants.DEFAULT_PAGE_NUMBER);
					searchHeader.setPageSize(Constants.DEFAULT_PAGE_SIZE);
				}
				else {
					searchHeader.setUserOrgId(null);

					if (organizations == null) {
						logger.warn(
							screenName + " has no organisations assigned!");
					}
					else {
						String orgNames = null;

						if (organizations != null) {
							for (Organization organization : organizations) {
								orgNames += " " + organization.getName();
							}
						}

						logger.warn(
							screenName + " has " + organizations.size() +
								" organisations assigned: " + orgNames);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error(
				"Exception setting search header: " + e.getMessage(), e);
		}

		logger.debug("getSearchHeaderData - end");

		return searchHeader;
	}

	/**
	 * Shows the initial view of the Transaction detail page
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 * @throws Exception
	 */
	@ResourceMapping(Constants.TX_DETAIL)
	public void getTransactionDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.debug("getTransactionDetails - start");

		TransactionDetail transactionDetail = new TransactionDetail();
		request.setAttribute(Constants.FROM_FA, Boolean.FALSE);
		request.setAttribute(Constants.IS_FA, isUserHas2FARole(request));

		HttpServletRequest httpRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = httpRequest.getSession();

		PortletSession portletSession = request.getPortletSession();

		if (!StringUtils.isBlank(request.getParameter(Constants.TX_ID)) &&
			!StringUtils.isBlank(request.getParameter(Constants.BUS_DATE)) &&
			!StringUtils.isBlank(request.getParameter(Constants.TX_SRC))) {

			if (!StringUtils.isBlank(
					request.getParameter(Constants.CURR_ROW_NUM)) &&
				!Constants.NULL.equalsIgnoreCase(
					request.getParameter(Constants.CURR_ROW_NUM))) {

				session.setAttribute(
					Constants.CURR_ROW_NUM,
					request.getParameter(Constants.CURR_ROW_NUM));
			}

			logger.debug(
				"[getTransactionDetails] setting the request attributes ");
			session.setAttribute(
				Constants.TX_ID, request.getParameter(Constants.TX_ID));
			session.setAttribute(
				Constants.BUS_DATE, request.getParameter(Constants.BUS_DATE));
			session.setAttribute(
				Constants.TX_SRC, request.getParameter(Constants.TX_SRC));

			transactionDetail.setBusDate(
				request.getParameter(
					Constants.BUS_DATE
				).toString());
			transactionDetail.setTransactionId(
				request.getParameter(
					Constants.TX_ID
				).toString());
			transactionDetail.setDataSrc(
				request.getParameter(
					Constants.TX_SRC
				).toString());
		}

		if ((null != portletSession.getAttribute(
				Constants._USER_STEPPEDUP, PortletSession.APPLICATION_SCOPE)) &&
			(Constants.FALSE.equals(
				portletSession.getAttribute(
					Constants._USER_STEPPEDUP,
					PortletSession.APPLICATION_SCOPE)) ||
			 Constants.ERROR.equals(
				 portletSession.getAttribute(
					 Constants._USER_STEPPEDUP,
					 PortletSession.APPLICATION_SCOPE)))) {

			logger.debug(
				"getTransactionDetails - session.getAttribute(_USER_STEPPEDUP) is false " +
					portletSession.getAttribute(
						Constants._USER_STEPPEDUP,
						PortletSession.APPLICATION_SCOPE));
			transactionDetail.setUserStepUp(false);
		}
		else if ((null != portletSession.getAttribute(
					Constants._USER_STEPPEDUP,
					PortletSession.APPLICATION_SCOPE)) &&
				 Constants.TRUE.equals(
					 portletSession.getAttribute(
						 Constants._USER_STEPPEDUP,
						 PortletSession.APPLICATION_SCOPE))) {

			logger.debug(
				"getTransactionDetails - session.getAttribute(_USER_STEPPEDUP) is true  " +
					portletSession.getAttribute(
						Constants._USER_STEPPEDUP,
						PortletSession.APPLICATION_SCOPE));
			transactionDetail.setUserStepUp(true);
		}
		else {
			logger.debug(
				"getTransactionDetails - session.getAttribute(_USER_STEPPEDUP) final else false");
			transactionDetail.setUserStepUp(false);
		}

		try {
			SearchHeader searchHeader = getSearchHeaderData(request);

			if ((null != searchHeader) &&
				(searchHeader.getUserOrgId() != null)) {

				logger.debug(
					"getTransactionDetails - begin retrieval of transaction details data");
				transactionDetail =
					transactionSearchService.
						getTransactionDetailByTxIdBusDateAndSrc(
							transactionDetail, searchHeader,
							PortletContext.newContext(response, request));
				logger.debug(
					"getTransactionDetails - end retrieval of transaction details data");
				Transaction transaction = createSessionTransactionObject(
					transactionDetail);
				//if a valid service request then only make the call to get additional transaction info

				if (transactionDetail.isValidServiceRequest()) {
					logger.debug(
						"getTransactionDetails - ExternalTransactionId : " +
							transactionDetail.getExternalTransactionId() +
								" and OriginIchName : " +
									transaction.getOriginIchName());
					//additional transaction attributes like UCAf, CSBD, Chip Present, Track 2 Data Service code

					if (StringUtils.isNotBlank(
							transactionDetail.getExternalTransactionId()) &&
						(MASTERCARD_IPM_TransactionBatch.equalsIgnoreCase(
							transaction.getOriginIchName()) ||
						 MASTERCARD_CMS_TransactionBatch.equalsIgnoreCase(
							 transaction.getOriginIchName()))) {

						transaction = setAdditionalTransactionAttributes(
							request, response, searchHeader,
							transactionDetail.getExternalTransactionId(),
							transaction);
						transactionDetail.setUCAFCollectionIndicator(
							transaction.getUCAFCollectionIndicator());
						transactionDetail.setUCAFCollectionIndicatorDescription(
							Utility.getUCAFCollectionIndicatorDescription(
								transaction.getUCAFCollectionIndicator()));
						transactionDetail.setCentralSiteBusinessDate(
							transaction.getCentralSiteBusinessDate());
						transactionDetail.setChipCardPresent(
							transaction.getChipCardPresent());
						transactionDetail.setElectronicCommerceIndciator(
							transaction.getElectronicCommerceIndciator());
						transactionDetail.setTrack2DataServiceCode(
							transaction.getTrack2DataServiceCode());
					}
				}

				transactionDetail.setCardControlRejectCode(
					getCardControlRejectCode(
						transactionDetail.getCustomData()));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();

				ObjectOutputStream os = new ObjectOutputStream(bos);

				os.writeObject(transaction);
				os.close();

				request.getPortletSession(
				).setAttribute(
					Constants.TRANSACTION_DETAIL, bos.toByteArray(),
					PortletSession.APPLICATION_SCOPE
				);

				session.setAttribute(
					Constants.TRANSACTION_DETAIL, bos.toByteArray());
				logger.debug(
					"getTransactionDetails - creating JSON object to create form");
				ObjectMapper mapper = new ObjectMapper();

				String json = mapper.writeValueAsString(transactionDetail);
				logger.debug("json is: " + json);
				PrintWriter out = response.getWriter();
				response.setContentType(Constants.JSON2);
				response.setCharacterEncoding(Constants.UTF_8);
				out.print(json);
			}
			else {
				logger.error(
					"getTransactionDetails - The Search header is null or  user is not logged in or org short name is null");
			}
		}
		catch (Exception e) {
			logger.error(
				"getTransactionDetails - EXCEPTION CAME ERROR MSG e.getCause() is " +
					e.getCause(),
				e);
		}

		logger.debug("getTransactionDetails - end");
	}

	/**
	 * Retrieve a list of the Service Requests raised for a particular
	 * transaction.
	 *
	 * @param request
	 * @param response
	 */
	@ResourceMapping("transactionTickets")
	public void getTransactionTickets(
		ResourceRequest request, ResourceResponse response,
		@ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm) {

		logger.debug("getTransactionTickets - start");

		SearchHeader searchHeader = getSearchHeaderData(request);

		TktRequestHeader header = Utility.convertSearchHeaderToTktHeader(
			searchHeader);
		JsonResponseStatus resp = new JsonResponseStatus();
		ObjectMapper mapper = new ObjectMapper();
		String json;
		String transactionId = (String)request.getAttribute("trans");
		String businessDate = (String)request.getAttribute("dat");

		if ((StringUtils.isBlank(transactionId) &&
			 StringUtils.isBlank(businessDate)) ||
			"undefined".equals(transactionId) ||
			"undefined".equals(businessDate)) {

			transactionId = ParamUtil.getString(request, "trans");
			businessDate = ParamUtil.getString(request, "dat");
		}

		if ((null != header) && (null != header.getUserOrgId())) {
			List<TicketDetails> ticketList =
				transactionSearchService.findServiceRequest(
					transactionId, businessDate, header);

			if (null != ticketList) {
				try {
					//TBA
					ThemeDisplay themeDisplay =
						(ThemeDisplay)request.getAttribute(
							WebKeys.THEME_DISPLAY);

					PortletURL link = PortletURLFactoryUtil.create(
						PortalUtil.getHttpServletRequest(request),
						Constants.TICKETING_PORTLET_NAME,
						themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

					link.setParameter(
						Constants.TX_RENDER, Constants.TICKET_DETAILS_RENDER);

					String eftConnectURI =
						transactionAppProperties.getTransProps(
						).getProperty(
							Constants.EFT_CONNECT_URI
						);

					String selfServiceURI =
						transactionAppProperties.getTransProps(
						).getProperty(
							Constants.SELF_SERVICE_URI
						);

					JSONObject jsonObj = Utility.getJsonObjectFromTicketObject(
						ticketList, link, eftConnectURI, selfServiceURI);

					addSubmissionValidationFlags(
						jsonObj, ticketList, request, searchHeader);

					//Check if it is a Cuscal user or not.

					if (StringUtils.equalsIgnoreCase(
							"cuscal", header.getUserOrgId())) {

						jsonObj.put("cuscalUser", Boolean.TRUE);
					}
					else {
						jsonObj.put("cuscalUser", Boolean.FALSE);
					}

					logger.debug(
						"getTransactionTickets - Json Object is: " + jsonObj);

					PrintWriter out = response.getWriter();
					response.setContentType(Constants.JSON2);
					response.setCharacterEncoding(Constants.UTF_8);
					out.print(jsonObj);
				}
				catch (JSONException e) {
					logger.error(
						TransactionSearchController.class.toString() +
							" getTransactionTickets - JSONException : ",
						e);
				}
				catch (IOException e) {
					logger.error(
						TransactionSearchController.class.toString() +
							" getTransactionTickets - IOException : ",
						e);
				}
			}
			else {
				logger.error(
					"getTransactionTickets - ticketDetails object is null");
				resp.setStatus("FAIL");
				Map<String, String> tempMap = new HashMap<>();

				tempMap.put(
					"message",
					"Cannot get tickets for this transaction: " +
						transactionId);
				resp.setResult(tempMap);

				try {
					json = mapper.writeValueAsString(resp);
					PrintWriter out = response.getWriter();
					response.setContentType(Constants.JSON2);
					response.setCharacterEncoding(Constants.UTF_8);
					out.print(json);
				}
				catch (JsonGenerationException e) {
					logger.error(
						TransactionSearchController.class.toString() +
							" getTransactionTickets - JsonGenerationException : ",
						e);
				}
				catch (JsonMappingException e) {
					logger.error(
						TransactionSearchController.class.toString() +
							" getTransactionTickets - JsonMappingException : ",
						e);
				}
				catch (IOException e) {
					logger.error(
						TransactionSearchController.class.toString() +
							" getTransactionTickets - IOException two : ",
						e);
				}
			}
		}
		else {
			logger.error(
				"getTransactionTickets - header or header.getUserOrgId is null");
		}

		logger.debug("getTransactionTickets - end");
	}

	/**
	 * Transaction ATM POS Submit render
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.TX_ATM_POS_SUBMIT_RENDER
	)
	@SuppressWarnings("unchecked")
	public String gotoTXDetailsFromAtmPosSubmission(
		RenderResponse response, RenderRequest request) {

		logger.debug("gotoTXDetailsFromAtmPosSubmission - start");

		if (null != request.getAttribute(Constants.TX_LIST_REQ))
			request.setAttribute(
				Constants.TX_LIST, request.getAttribute(Constants.TX_LIST_REQ));
		logger.debug(
			"[gotoTXDetailsFromAtmPosSubmission] getting the request objects");
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		if ((null != session.getAttribute(Constants.TX_ID)) &&
			(null != session.getAttribute(Constants.BUS_DATE)) &&
			(null != session.getAttribute(Constants.TX_SRC))) {

			logger.debug(
				"[gotoTXDetailsFromAtmPosSubmission] setting the request in RENDER MAPPING attributes ");
			request.setAttribute(
				Constants.TX_ID,
				session.getAttribute(
					Constants.TX_ID
				).toString());
			request.setAttribute(
				"trans",
				session.getAttribute(
					Constants.TX_ID
				).toString());
			request.setAttribute(
				Constants.BUS_DATE,
				session.getAttribute(
					Constants.BUS_DATE
				).toString());

			request.setAttribute(
				Constants.TX_SRC,
				session.getAttribute(
					Constants.TX_SRC
				).toString());
			request.setAttribute(
				"dat",
				session.getAttribute(
					Constants.BUS_DATE
				).toString());
			logger.debug(
				"[gotoTXDetailsFromAtmPosSubmission]  %%%%%%%%%%%%%%%%%%% " +
					session.getAttribute(
						Constants.TX_ID
					).toString());

			if (null != session.getAttribute(Constants.TX_LIST)) {
				List<TransactionList> listForms =
					(List<TransactionList>)session.getAttribute(
						Constants.TX_LIST);
				logger.debug(
					"gotoTXDetailsFromAtmPosSubmission - adding " +
						"transactionDetails upon return ");
				request.setAttribute(Constants.TX_LIST_REQ, listForms);
			}

			retrieveAndSetPagingAndSortingParameters(request);
		}

		if (!StringUtils.isBlank(
				session.getAttribute(
					Constants.CURR_ROW_NUM
				).toString()) &&
			!Constants.NULL.equalsIgnoreCase(
				session.getAttribute(
					Constants.CURR_ROW_NUM
				).toString())) {

			request.setAttribute(
				Constants.CURR_ROW_NUM,
				session.getAttribute(Constants.CURR_ROW_NUM));
		}

		Boolean checkServiceRequestRole = doesUserHaveServiceRequestRole(
			request);

		request.setAttribute("serviceRequestRole", checkServiceRequestRole);

		logger.debug("gotoTXDetailsFromAtmPosSubmission - end");

		return Constants.TX_SEARCH_RESULTS_PAGE;
	}

	/**
	 * Check and set the user 2FA roles detail in object.
	 *
	 * @param PortletRequest
	 * @return boolean
	 */
	public boolean isUserHas2FARole(PortletRequest request) {
		logger.debug("setUser2FaRoles - start");
		boolean is2faAccessible = false;
		User user = null;

		try {
			user = PortalUtil.getUser(request);

			if (null != user) {
				Long userId = user.getUserId();

				List<Role> roles = RoleLocalServiceUtil.getUserRoles(userId);
				List<Role> grpRoles = Utility.findUserGroupRoleList(user);
				List<Role> allUserRoles = new ArrayList<>();

				allUserRoles.addAll(roles);
				allUserRoles.addAll(grpRoles);
				logger.debug(
					"isUserHas2FARole - All Roles count is " +
						allUserRoles.size());

				for (Role role : allUserRoles) {
					if (Constants.TX_ROLE_FA.equalsIgnoreCase(role.getName())) {
						is2faAccessible = true;
						logger.debug(
							"isUserHas2FARole - setting role as true for Role  " +
								role.getName());
					}
				}
			}
		}
		catch (Exception e) {
			logger.error(
				"Exception while checking user has 2fa Roles or not : " +
					e.getMessage(),
				e);
		}

		logger.debug("setUser2FaRoles - start");

		return is2faAccessible;
	}

	/**
	 * Process Render Request For 2FA Action call inside the main render method.
	 *
	 * @param RenderRequest
	 * @param RenderResponse
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void processRenderRequestFor2FAAction(
		RenderRequest request, RenderResponse response) {

		logger.debug("processRenderRequestFor2FAAction - start");

		if (null != request.getAttribute(Constants.TX_LIST_REQ))
			request.setAttribute(Constants.FROM_FA, Boolean.TRUE);
		request.setAttribute(
			Constants.TX_LIST, request.getAttribute(Constants.TX_LIST_REQ));
		logger.debug(
			"processRenderRequestFor2FAAction - getting the request objects");
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		if (!StringUtils.isBlank(
				session.getAttribute(
					Constants.TX_ID
				).toString()) &&
			!StringUtils.isBlank(
				session.getAttribute(
					Constants.BUS_DATE
				).toString()) &&
			!StringUtils.isBlank(
				session.getAttribute(
					Constants.TX_SRC
				).toString())) {

			logger.debug(
				"processRenderRequestFor2FAAction - setting the request in RENDER MAPPING attributes  TX ID" +
					session.getAttribute(
						Constants.TX_ID
					).toString());
			request.setAttribute(
				Constants.TX_ID,
				session.getAttribute(
					Constants.TX_ID
				).toString());
			request.setAttribute(
				Constants.BUS_DATE,
				session.getAttribute(
					Constants.BUS_DATE
				).toString());
			request.setAttribute(
				Constants.TX_SRC,
				session.getAttribute(
					Constants.TX_SRC
				).toString());
		}

		if (!StringUtils.isBlank(
				session.getAttribute(
					Constants.CURR_ROW_NUM
				).toString())) {

			request.setAttribute(
				Constants.CURR_ROW_NUM,
				session.getAttribute(
					Constants.CURR_ROW_NUM
				).toString());
		}
		//verify - display function code

		if (displayFunctionCode(
				(List<TransactionList>)session.getAttribute(
					Constants.TX_LIST))) {

			request.setAttribute(Constants.DISPLAY_FUNCTION_CODE, Boolean.TRUE);
		}

		logger.debug("processRenderRequestFor2FAAction - end");
	}

	/**
	 *  Process Render Request For non 2FA Action call inside the main render method.
	 *
	 * @param RenderRequest
	 * @param RenderResponse
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void processRenderRequestForNon2FAAction(
		RenderRequest request, RenderResponse response) {

		logger.debug("processRenderRequestForNon2FAAction -start  ");

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		Map<String, String> sortOrderMap = null;
		request.setAttribute(Constants.IS_FA, isUserHas2FARole(request));

		if (null == session.getAttribute(Constants.TX_LIST)) {
			if ((null != request.getAttribute(Constants.TX_LIST_REQ)) &&
				(((List<TransactionList>)request.getAttribute(
					Constants.TX_LIST_REQ)).size() > 0)) {

				List<TransactionList> transactionLists =
					(List<TransactionList>)request.getAttribute(
						Constants.TX_LIST_REQ);
				sortOrderMap = setDefaultSortOrderMap();
				perfLogger_.debug(
					"processRenderRequestForNon2FAAction - Sorting the Transction list at " +
						Calendar.getInstance(
						).getTime());
				transactionLists = Sortable.sort(
					transactionLists, null,
					sortOrderMap.get(Constants.DATE_TIME));
				perfLogger_.debug(
					"processRenderRequestForNon2FAAction - Sorting Finished for the Transction list at " +
						Calendar.getInstance(
						).getTime());
				String pageBy = request.getParameter(
					new ParamEncoder(
						Constants.TX_FRM
					).encodeParameterName(
						TableTagParameters.PARAMETER_PAGE
					));
				logger.debug(
					"processRenderRequestForNon2FAAction - pageBy is: " +
						pageBy);

				session.setAttribute(Constants.SORT_ORDER, sortOrderMap);
				session.setAttribute(Constants.TX_LIST, transactionLists);

				//verify - display function code

				if (displayFunctionCode(transactionLists)) {
					request.setAttribute(
						Constants.DISPLAY_FUNCTION_CODE, Boolean.TRUE);
				}
			}
			else {
				if ((null == request.getAttribute(Constants.VALIDATION)) &&
					(null == request.getAttribute(Constants.MORE_ORG)) &&
					((null == request.getAttribute(
						Constants.DATA_BASE_ERROR)) ||
					 (null == request.getAttribute(Constants.OTHER_ERROR)))) {

					request.setAttribute(
						Constants.NO_DATA_FOUND_MSG, Constants.TX_NODATA_FOUND);
				}
			}
		}
		else {
			String pageBy = request.getParameter(
				new ParamEncoder(
					Constants.TX_FRM
				).encodeParameterName(
					TableTagParameters.PARAMETER_PAGE
				));

			if (null != session.getAttribute(Constants.CUR_PAGE))
				logger.debug(
					"processRenderRequestForNon2FAAction - session.getAttribute  " +
						session.getAttribute(
							Constants.CUR_PAGE
						).toString());
			//verify - display function code

			if (displayFunctionCode(
					(List<TransactionList>)session.getAttribute(
						Constants.TX_LIST))) {

				request.setAttribute(
					Constants.DISPLAY_FUNCTION_CODE, Boolean.TRUE);
			}

			if ("1".equals(pageBy) &&
				((null == session.getAttribute(Constants.CUR_PAGE)) ||
				 ((null != session.getAttribute(Constants.CUR_PAGE)) &&
				  !pageBy.equals(session.getAttribute(Constants.CUR_PAGE))))) {

				logger.debug(
					"processRenderRequestForNon2FAAction - pageBypageBypageBy is  " +
						pageBy);
				String sortBy = request.getParameter(
					new ParamEncoder(
						Constants.TX_FRM
					).encodeParameterName(
						TableTagParameters.PARAMETER_SORT
					));
				logger.debug(
					"processRenderRequestForNon2FAAction - sortBy is   " +
						sortBy + "  Page session  is  " +
							session.getAttribute(Constants.CUR_PAGE));

				sortOrderMap = (HashMap<String, String>)session.getAttribute(
					Constants.SORT_ORDER);
				String orderBy = request.getParameter(
					new ParamEncoder(
						Constants.TX_FRM
					).encodeParameterName(
						TableTagParameters.PARAMETER_ORDER
					));

				if (orderBy != null) {
					if (orderBy.equals("1")) {
						sortOrderMap.put(sortBy, Constants.REVERSE);
					}
					else {
						sortOrderMap.put(sortBy, Constants.NATURAL);
					}
				}

				perfLogger_.debug(
					"processRenderRequestForNon2FAAction - Sorting the Transction list at " +
						Calendar.getInstance(
						).getTime());
				session.setAttribute(
					Constants.TX_LIST,
					Sortable.sort(
						(List<TransactionList>)session.getAttribute(
							Constants.TX_LIST),
						sortBy, sortOrderMap.get(sortBy)));
				perfLogger_.debug(
					"processRenderRequestForNon2FAAction - Finished Sorting the Transction list at " +
						Calendar.getInstance(
						).getTime());

				if (null != session.getAttribute(Constants.CUR_PAGE))
					session.setAttribute(
						Constants.CUR_PAGE,
						session.getAttribute(Constants.CUR_PAGE));
			}
			else {
				logger.debug(
					"processRenderRequestForNon2FAAction - pageing only Number  " +
						pageBy);
				session.setAttribute(Constants.CUR_PAGE, pageBy);
			}
		}

		if (null != request.getAttribute(Constants.DATE_WARNING_MSG)) {
			logger.debug(
				"processRenderRequestForNon2FAAction - INSIDE setting the msg for warning for 18 months old date range in Render ");
			request.setAttribute(Constants.DATE_WARNING_MSG, Boolean.TRUE);
		}

		if (null != request.getAttribute(Constants.MORE_DATA_MSG)) {
			logger.debug(
				"processRenderRequestForNon2FAAction - INSIDE setting the msg for warning for more than 250 found but display only 250 in Render ");
			request.setAttribute(Constants.MORE_DATA_MSG, Boolean.TRUE);
		}

		logger.debug("processRenderRequestForNon2FAAction -end  ");
	}

	/**
	 * Transaction 2FA authentication process render
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.TX_2FA_RENDER
	)
	@SuppressWarnings("unchecked")
	public String renderToTxDetailsPageAndBackToTxDetails(
		RenderResponse response, RenderRequest request) {

		logger.debug("renderToTxDetailsPageAndBackToTxDetails - start");

		//Set if the user has the 2FA Role to make sure that the link shows up properly.
		request.setAttribute(Constants.IS_FA, isUserHas2FARole(request));

		if ((null != request.getAttribute(Constants.ACTION)) &&
			Constants._2FA_ACTION.equals(
				request.getAttribute(Constants.ACTION))) {

			logger.debug(
				"renderToTxDetailsPageAndBackToTxDetails - 2FA PAGE RENDER");

			if (null != request.getAttribute(Constants.TX_LIST_REQ))
				request.setAttribute(Constants.FROM_FA, Boolean.TRUE);
			request.setAttribute(
				Constants.TX_LIST, request.getAttribute(Constants.TX_LIST_REQ));

			logger.debug(
				"renderToTxDetailsPageAndBackToTxDetails - getting the request objects");
			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpSession session = httpServletRequest.getSession();

			logger.debug(
				"renderToTxDetailsPageAndBackToTxDetails - The session attribute in renderToTxDetailsPageAndBackToTxDetails is: " +
					session.getAttribute(Constants._USER_STEPPEDUP));

			if (!StringUtils.isBlank(
					session.getAttribute(
						Constants.TX_ID
					).toString()) &&
				!StringUtils.isBlank(
					session.getAttribute(
						Constants.BUS_DATE
					).toString()) &&
				!StringUtils.isBlank(
					session.getAttribute(
						Constants.TX_SRC
					).toString())) {

				logger.debug(
					"renderToTxDetailsPageAndBackToTxDetails - setting the request in RENDER MAPPING attributes ");
				request.setAttribute(
					Constants.TX_ID,
					session.getAttribute(
						Constants.TX_ID
					).toString());
				request.setAttribute(
					Constants.BUS_DATE,
					session.getAttribute(
						Constants.BUS_DATE
					).toString());
				request.setAttribute(
					Constants.TX_SRC,
					session.getAttribute(
						Constants.TX_SRC
					).toString());
				logger.debug(
					"renderToTxDetailsPageAndBackToTxDetails - " +
						session.getAttribute(
							Constants.TX_ID
						).toString());
			}

			if (!StringUtils.isBlank(
					session.getAttribute(
						Constants.CURR_ROW_NUM
					).toString()) &&
				!Constants.NULL.equalsIgnoreCase(
					session.getAttribute(
						Constants.CURR_ROW_NUM
					).toString())) {

				request.setAttribute(
					Constants.CURR_ROW_NUM,
					session.getAttribute(Constants.CURR_ROW_NUM));
			}

			logger.debug(
				"renderToTxDetailsPageAndBackToTxDetails - Current row num for request: " +
					request.getAttribute(Constants.CURR_ROW_NUM));
		}
		else {
			logger.debug(
				"renderToTxDetailsPageAndBackToTxDetails - INSIDE THE TX MAIN SEARCH RESULT RENDER MAPPING MAP IS: " +
					request.getParameterMap());

			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpSession session = httpServletRequest.getSession();
			logger.debug(
				"renderToTxDetailsPageAndBackToTxDetails - _USER_STEPPEDUP session value is: " +
					session.getAttribute(Constants._USER_STEPPEDUP));
			Map<String, String> sortOrderMap = null;
			//			boolean is2FaRole = isUserHas2FARole(request);

			if (null == session.getAttribute(Constants.TX_LIST)) {
				if ((null != request.getAttribute(Constants.TX_LIST_REQ)) &&
					(((List<TransactionList>)request.getAttribute(
						Constants.TX_LIST_REQ)).size() > 0)) {

					List<TransactionList> transactionLists =
						(List<TransactionList>)request.getAttribute(
							Constants.TX_LIST_REQ);
					sortOrderMap = setDefaultSortOrderMap();

					perfLogger_.debug(
						"showTransactionSearchResult - Sorting First time the Transction list at " +
							Calendar.getInstance(
							).getTime());

					transactionLists = Sortable.sort(
						transactionLists, null,
						sortOrderMap.get(Constants.DATE_TIME));

					perfLogger_.debug(
						"showTransactionSearchResult - Finished Sorting First time FINISHED the Transction list at " +
							Calendar.getInstance(
							).getTime());
					String pageBy = request.getParameter(
						new ParamEncoder(
							Constants.TX_FRM
						).encodeParameterName(
							TableTagParameters.PARAMETER_PAGE
						));

					logger.debug(
						"renderToTxDetailsPageAndBackToTxDetails - Page is: " +
							pageBy);
					session.setAttribute(Constants.TX_LIST, transactionLists);
					session.setAttribute(Constants.SORT_ORDER, sortOrderMap);
				}
				else {
					request.setAttribute(
						Constants.NO_DATA_FOUND_MSG, Constants.TX_NODATA_FOUND);
				}
			}
			else {
				String pageBy = request.getParameter(
					new ParamEncoder(
						Constants.TX_FRM
					).encodeParameterName(
						TableTagParameters.PARAMETER_PAGE
					));

				if ("1".equals(pageBy) &&
					((null == session.getAttribute(Constants.CUR_PAGE)) ||
					 ((null != session.getAttribute(Constants.CUR_PAGE)) &&
					  pageBy.equals(
						  session.getAttribute(Constants.CUR_PAGE))))) {

					String sortBy = request.getParameter(
						new ParamEncoder(
							Constants.TX_FRM
						).encodeParameterName(
							TableTagParameters.PARAMETER_SORT
						));
					logger.debug(
						"showTransactionSearchResult - sortBy is: " + sortBy +
							" Page session is: " +
								session.getAttribute(Constants.CUR_PAGE));

					sortOrderMap =
						(HashMap<String, String>)session.getAttribute(
							Constants.SORT_ORDER);
					String orderBy = request.getParameter(
						new ParamEncoder(
							Constants.TX_FRM
						).encodeParameterName(
							TableTagParameters.PARAMETER_ORDER
						));

					if (orderBy != null) {
						if (orderBy.equals("1")) {
							sortOrderMap.put(sortBy, Constants.REVERSE);
						}
						else {
							sortOrderMap.put(sortBy, Constants.NATURAL);
						}
					}

					perfLogger_.debug(
						"[TransactionSearchController.showTransactionSearchResult] Sorting the Transction list at " +
							Calendar.getInstance(
							).getTime());
					session.setAttribute(
						Constants.TX_LIST,
						Sortable.sort(
							(List<TransactionList>)session.getAttribute(
								Constants.TX_LIST),
							sortBy, sortOrderMap.get(sortBy)));
					perfLogger_.debug(
						"[TransactionSearchController.showTransactionSearchResult] Sorting FINISHED the Transction list at " +
							Calendar.getInstance(
							).getTime());

					if (null != session.getAttribute(Constants.CUR_PAGE))
						session.setAttribute(
							Constants.CUR_PAGE,
							session.getAttribute(Constants.CUR_PAGE));
				}
				else {
					logger.debug(
						"[TransactionSearchController.showTransactionSearchResult] pageing only Number  " +
							pageBy);
					session.setAttribute(Constants.CUR_PAGE, pageBy);
				}
			}

			if (null != request.getAttribute(Constants.DATE_WARNING_MSG)) {
				request.setAttribute(Constants.DATE_WARNING_MSG, Boolean.TRUE);
			}

			if (null != request.getAttribute(Constants.MORE_DATA_MSG)) {
				request.setAttribute(Constants.MORE_DATA_MSG, Boolean.TRUE);
			}
		}

		logger.debug("renderToTxDetailsPageAndBackToTxDetails - end");

		return Constants.TX_SEARCH_RESULTS_PAGE;
	}

	/**
	 * Method to add a new Service Request for the transaction with attachments.
	 *
	 * @param request
	 * @param response
	 * @param serviceRequestForm
	 */
	@ActionMapping(
		params = {
			Constants.ACTION + Constants.EQUALS +
				"SERVICE_REQUEST_WITH_ATTACHMENTS",
			"close"
		}
	)
	public void resetAtmPosForm(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		BindingResult bindingResult) {

		logger.debug("resetAtmPosForm - start");

		ServiceRequestForm newServiceRequestForm = serviceRequestForm;

		newServiceRequestForm.clearForm();

		request.setAttribute(Constants.IS_FA, isUserHas2FARole(request));
		request.setAttribute("loadTXDetails", Boolean.FALSE);

		setPagingAndSortingParameters(request, response);

		response.setRenderParameter(
			Constants.TX_RENDER, Constants.TX_ATM_POS_SUBMIT_RENDER);
		logger.debug("resetAtmPosForm - end");
	}

	/**
	 * Default value for sort order map for Transaction list search
	 *
	 * @return Map<String, String>
	 */
	public Map<String, String> setDefaultSortOrderMap() {
		Map<String, String> sortOrderMap = new HashMap<>();

		sortOrderMap.put(Constants.AMOUNT, Constants.NATURAL);
		sortOrderMap.put(Constants.CARD_ACCEPTOR_ID, Constants.NATURAL);
		sortOrderMap.put(Constants.DATE_TIME, Constants.REVERSE);
		sortOrderMap.put(Constants.DESCRIPTION, Constants.NATURAL);
		sortOrderMap.put(Constants.EXTERNAL_TRANSACTION_ID, Constants.NATURAL);
		sortOrderMap.put(Constants.MESSAGE_TYPE, Constants.NATURAL);
		sortOrderMap.put(Constants.PAN, Constants.NATURAL);
		sortOrderMap.put(Constants.RESPONSE_CODE, Constants.NATURAL);
		sortOrderMap.put(Constants.SYSTEM_TRACE, Constants.NATURAL);
		sortOrderMap.put(Constants.TERMINAL_ID, Constants.NATURAL);

		return sortOrderMap;
	}

	/**
	 * organisationIdOverride object setter
	 */
	public void setOrganisationIdOverride(String organisationIdOverride) {
		this.organisationIdOverride = organisationIdOverride;
	}

	/**
	 * For TX_LOGPOOL render call
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @param TransactionForm
	 * @return String
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.TX_LOGPOOL
	)
	public String showLogPools(
		RenderResponse response, RenderRequest request,
		@ModelAttribute TransactionForm transactionForm) {

		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();

		transactionSearchService.logPoolStatistics();

		transactionForm.resetForm();
		session.removeAttribute(Constants.TX_LIST);

		return Constants.TX_SEARCH_PAGE;
	}

	/**
	 * For TX_PURGEPOOL render call
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @param TransactionForm
	 * @return String
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.TX_PURGEPOOL
	)
	public String showPurgePools(
		RenderResponse response, RenderRequest request,
		@ModelAttribute TransactionForm transactionForm) {

		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();
		transactionSearchService.purgePools();
		transactionForm.resetForm();
		session.removeAttribute(Constants.TX_LIST);

		return Constants.TX_SEARCH_PAGE;
	}

	/**
	 * Shows the search results of the Transaction
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.TX_SEARCH_RESULT
	)
	public String showTransactionSearchResult(
		RenderResponse response, RenderRequest request) {

		logger.debug("showTransactionSearchResult - start ");

		if ((null != request.getAttribute(Constants.ACTION)) &&
			Constants._2FA_ACTION.equals(
				request.getAttribute(Constants.ACTION))) {

			processRenderRequestFor2FAAction(request, response);
		}
		else {
			processRenderRequestForNon2FAAction(request, response);
		}

		retrieveAndSetPagingAndSortingParameters(request);

		request.setAttribute(
			_TRANSACTION_FORM,
			request.getPortletSession(
			).getAttribute(
				_TRANSACTION_FORM, PortletSession.APPLICATION_SCOPE
			));

		Boolean checkServiceRequestRole = doesUserHaveServiceRequestRole(
			request);

		request.setAttribute("serviceRequestRole", checkServiceRequestRole);

		logger.debug(
			"showTransactionSearchResult - checkServiceRequestRole is: " +
				checkServiceRequestRole);
		logger.debug("showTransactionSearchResult - end ");
		/*return Constants.TX_SEARCH_PAGE;*/

		return "transactionSearchResults";
	}

	/**
	 * Clear the list and search form validation of the Transaction form
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @param TransactionForm
	 * @return String
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.TX_CLEAR
	)
	public String showTransactionSearchResult(
		RenderResponse response, RenderRequest request,
		@ModelAttribute TransactionForm transactionForm) {

		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();
		transactionForm.resetForm();
		session.removeAttribute(Constants.TX_LIST);
		request.getPortletSession(
		).setAttribute(
			_TRANSACTION_FORM, null, PortletSession.APPLICATION_SCOPE
		);

		return Constants.TX_SEARCH_PAGE;
	}

	/**
	 *
	 * @param jsonObj
	 * @param ticketList
	 * @throws JSONException
	 */
	private void addSubmissionValidationFlags(
			JSONObject jsonObj, List<TicketDetails> ticketList,
			ResourceRequest request, SearchHeader searchHeader)
		throws JSONException {

		logger.debug("addSubmissionValidationFlags - start");

		jsonObj.put("displayMasterCardDispute", Boolean.TRUE);
		jsonObj.put("existingClosedDispute", Boolean.FALSE);
		jsonObj.put("existingDispute", Boolean.FALSE);
		jsonObj.put(
			"existingMasterCardDisputeArbitrationChargeback", Boolean.FALSE);
		jsonObj.put("existingMasterCardDisputeFirstChargeback", Boolean.FALSE);
		jsonObj.put("existingMasterCardDisputeReportFraud", Boolean.FALSE);
		jsonObj.put("existingMasterCardDisputeRetrievalRequest", Boolean.FALSE);
		jsonObj.put("existingReinvestigation", Boolean.FALSE);
		jsonObj.put("existingTC40", Boolean.FALSE);
		jsonObj.put("existingTC52", Boolean.FALSE);

		long tc40Id = Long.parseLong(
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.TC40_TYPE_ID
			));
		long tc52Id = Long.parseLong(
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.TC52_TYPE_ID
			));
		long masterCardDisputeRetrievalRequestId = Long.parseLong(
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID
			));
		long masterCardDisputeFirstChargebackId = Long.parseLong(
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID
			));
		long masterCardDisputeArbitrationChargebackId = Long.parseLong(
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID
			));
		long masterCardDisputeReportFraudId = Long.parseLong(
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID
			));

		logger.debug("addSubmissionValidationFlags - tc40Id: " + tc40Id);
		logger.debug("addSubmissionValidationFlags - tc52Id: " + tc52Id);
		logger.debug(
			"addSubmissionValidationFlags - masterCardDisputeRetrievalRequestId: " +
				masterCardDisputeRetrievalRequestId);
		logger.debug(
			"addSubmissionValidationFlags - masterCardDisputeFirstChargebackId: " +
				masterCardDisputeFirstChargebackId);
		logger.debug(
			"addSubmissionValidationFlags - masterCardDisputeArbitrationChargebackId: " +
				masterCardDisputeArbitrationChargebackId);
		logger.debug(
			"addSubmissionValidationFlags - masterCardDisputeReportFraudId: " +
				masterCardDisputeReportFraudId);

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		Transaction transaction = null;
		String functionCode = null;
		String messageType = null;
		boolean existingMasterCardDisputeRetrievalRequest = false;
		boolean existingMasterCardDisputeFirstChargeback = false;
		boolean existingMasterCardDisputeArbitrationChargeback = false;
		boolean existingMasterCardDisputeReportFraud = false;
		boolean existingReinvestigation = false;

		try {
			if (null == session.getAttribute(Constants.TRANSACTION_DETAIL)) {
				logger.error(
					"addSubmissionValidationFlags - Could not get the transactionDetail from the session. Redirecting to error page.");

				return;
			}

			byte[] bytes = (byte[])session.getAttribute(
				Constants.TRANSACTION_DETAIL);
			ByteArrayInputStream b = new ByteArrayInputStream(bytes);

			ObjectInputStream o = new ObjectInputStream(b);

			Object sessionObject = o.readObject();

			if (sessionObject instanceof Transaction) {
				transaction = (Transaction)sessionObject;
			}
			//transaction = (Transaction) session.getAttribute(Constants.TRANSACTION);

			if (null != transaction) {
				functionCode = transaction.getFunctionCode();
				messageType = transaction.getMessageType();
			}

			request.removeAttribute(
				"outstandingMasterCardDisputeRetrievalRequest");
			request.removeAttribute(
				"outstandingMasterCardDisputeFirstChargeback");
			request.removeAttribute(
				"outstandingMasterCardDisputeArbitrationChargeback");
			request.removeAttribute("outstandingMasterCardDisputeReportFraud");

			session.removeAttribute(
				"outstandingMasterCardDisputeRetrievalRequest");
			session.removeAttribute(
				"outstandingMasterCardDisputeFirstChargeback");
			session.removeAttribute(
				"outstandingMasterCardDisputeArbitrationChargeback");
			session.removeAttribute("outstandingMasterCardDisputeReportFraud");
		}
		catch (Exception e) {
			//can ignore
			logger.error("addSubmissionValidationFlags - " + e.getMessage(), e);
		}

		for (TicketDetails ticket : ticketList) {
			logger.debug("TicketDetails: " + ticket);

			if (StringUtils.isNotBlank(ticket.getTicketCategory())) {
				long ticketType = ticket.getTicketTypeId();
				logger.debug(
					"addSubmissionValidationFlags - ticketType: " + ticketType);

				try {
					if (tc40Id == ticketType) {
						jsonObj.remove("existingTC40");
						jsonObj.put("existingTC40", Boolean.TRUE);
						logger.debug(
							"addSubmissionValidationFlags - found existing TC40");
					}
					else if (tc52Id == ticketType) {
						jsonObj.remove("existingTC52");
						jsonObj.put("existingTC52", Boolean.TRUE);
						logger.debug(
							"addSubmissionValidationFlags - found existing TC52");
					}
					else if (masterCardDisputeRetrievalRequestId ==
								ticketType) {

						jsonObj.remove(
							"existingMasterCardDisputeRetrievalRequest");
						jsonObj.put(
							"existingMasterCardDisputeRetrievalRequest",
							Boolean.TRUE);
						existingMasterCardDisputeRetrievalRequest = true;
						logger.debug(
							"addSubmissionValidationFlags - found existing MasterCardDisputeRetrievalRequest");
					}
					else if (masterCardDisputeFirstChargebackId == ticketType) {
						jsonObj.remove(
							"existingMasterCardDisputeFirstChargeback");
						jsonObj.put(
							"existingMasterCardDisputeFirstChargeback",
							Boolean.TRUE);
						existingMasterCardDisputeFirstChargeback = true;
						logger.debug(
							"addSubmissionValidationFlags - found existing MasterCardDisputeFirstChargeback");
					}
					else if (masterCardDisputeArbitrationChargebackId ==
								ticketType) {

						jsonObj.remove(
							"existingMasterCardDisputeArbitrationChargeback");
						jsonObj.put(
							"existingMasterCardDisputeArbitrationChargeback",
							Boolean.TRUE);
						existingMasterCardDisputeArbitrationChargeback = true;
						logger.debug(
							"addSubmissionValidationFlags - found existing MasterCardDisputeArbitrationChargeback");
					}
					else if (masterCardDisputeReportFraudId == ticketType) {
						jsonObj.remove("existingMasterCardDisputeReportFraud");
						jsonObj.put(
							"existingMasterCardDisputeReportFraud",
							Boolean.TRUE);
						existingMasterCardDisputeReportFraud = true;
						logger.debug(
							"addSubmissionValidationFlags - found existing MasterCardDisputeReportFraud");
					}
					else {
						//dispute stuff here
						jsonObj.remove("existingDispute");
						jsonObj.put("existingDispute", Boolean.TRUE);
						logger.debug(
							"addSubmissionValidationFlags - found existing dispute");

						if ("Closed".equals(ticket.getTicketStatus())) {
							jsonObj.remove("existingClosedDispute");
							jsonObj.put("existingClosedDispute", Boolean.TRUE);
							logger.debug(
								"addSubmissionValidationFlags - found existing closed dispute");
						}
						else {
							existingReinvestigation =
								hasExistingReinvestigation(
									ticket, existingReinvestigation);
							logger.debug(
								"existingReinvestigation=" +
									existingReinvestigation);
						}
					}
				}
				catch (Exception e) {
					logger.error(
						TransactionSearchController.class.toString() +
							"addSubmissionValidationFlags - Error reading transactionAppProperties for TypeIds  Exception  : ",
						e);
				}
			}
		}

		if (existingReinvestigation) {
			jsonObj.remove("existingReinvestigation");
			jsonObj.put("existingReinvestigation", Boolean.TRUE);
		}

		if ((null != ticketList) && (ticketList.size() > 0) &&
			(Constants.MASTERCARD_IPM_TransactionBatch.equalsIgnoreCase(
				transaction.getOriginIchName()) ||
			 Constants.MASTERCARD_CMS_TransactionBatch.equalsIgnoreCase(
				 transaction.getOriginIchName()))) {

			boolean displayMasterCardDispute = displayMasterCardDisputeFunction(
				functionCode, messageType,
				existingMasterCardDisputeRetrievalRequest,
				existingMasterCardDisputeFirstChargeback,
				existingMasterCardDisputeArbitrationChargeback,
				existingMasterCardDisputeReportFraud, searchHeader);
			logger.debug(
				"displayMasterCardDispute=" + displayMasterCardDispute);
			jsonObj.remove("displayMasterCardDispute");
			jsonObj.put("displayMasterCardDispute", displayMasterCardDispute);
			jsonObj.remove("isMasterCardDispute");
			jsonObj.put("isMasterCardDispute", Boolean.TRUE);
		}

		logger.debug("addSubmissionValidationFlags - else");
	}

	/**
	 * Validates service request, calls ldap and addServiceRequest
	 *
	 * @param request
	 * @param serviceRequestForm
	 * @param bindingResult
	 * @param header
	 * @throws Exception
	 */
	private void callAddServiceRequestForAtmPos(
			PortletContext portletContext,
			ServiceRequestForm serviceRequestForm, BindingResult bindingResult,
			TktRequestHeader header)
		throws Exception {

		//boolean userDetailsUpdateSuccessful = true;
		int userDetailsUpdateStatus = LDAPConstants.NO_CHANGE;

		PortletRequest request = portletContext.getRequest();
		serviceRequestValidator.validate(serviceRequestForm, bindingResult);

		if (!doesUserHaveServiceRequestAddRole(request)) {
			bindingResult.rejectValue(
				"atmPosClaimInformation.errorMsg",
				"service.request.role.selfservice.error");

			logger.debug(
				"callAddServiceRequest - user does not have self " +
					"service role");
		}

		request.setAttribute(
			"dat",
			serviceRequestForm.getTransactionInformation(
			).getBusinessDate());
		request.setAttribute(
			"trans",
			serviceRequestForm.getTransactionInformation(
			).getCuscalTransactionId());

		if (!bindingResult.hasErrors()) {
			try {
				userDetailsUpdateStatus = updateUserDetailsInLdapAndWebService(
					serviceRequestForm.getContactInformation(), request);

				if (userDetailsUpdateStatus == LDAPConstants.EMAIL_ERROR) {
					bindingResult.rejectValue(
						"atmPosClaimInformation.errorMsg",
						"service.request.email.exists");
				}
				else if (userDetailsUpdateStatus ==
							LDAPConstants.UPDATE_ERROR) {

					bindingResult.rejectValue(
						"atmPosClaimInformation.errorMsg",
						"service.request.contact.details.error");
				}
			}
			catch (SystemException e1) {
				bindingResult.rejectValue(
					"atmPosClaimInformation.errorMsg",
					"service.request.contact.details.error");

				logger.error("callAddServiceRequest - user update fail", e1);
			}
			catch (PortalException e1) {
				bindingResult.rejectValue(
					"atmPosClaimInformation.errorMsg",
					"service.request.contact.details.error");

				logger.error("callAddServiceRequest - user update fail", e1);
			}
		}

		if (!bindingResult.hasErrors() &&
			(userDetailsUpdateStatus == LDAPConstants.UPDATED)) {

			String message = transactionSearchService.addServiceRequest(
				serviceRequestForm, header, portletContext);

			if (StringUtils.equalsIgnoreCase("SUCCESS", message)) {
				logger.debug(
					"callAddServiceRequest - message from add request is: " +
						message);
			}
			else {
				bindingResult.rejectValue(
					"atmPosClaimInformation.errorMsg",
					"service.request.atmpos.error");

				logger.error(
					"callAddServiceRequest - add service request failed");
			}
		}
	}

	/**
	 * Validates service request, calls ldap and addServiceRequest
	 *
	 * @param request
	 * @param serviceRequestForm
	 * @param bindingResult
	 * @param header
	 * @throws Exception
	 */
	private void callAddServiceRequestForVisa(
			PortletContext portletContext,
			ServiceRequestForm serviceRequestForm, BindingResult bindingResult,
			TktRequestHeader header)
		throws Exception {

		int userDetailsUpdateStatus = LDAPConstants.NO_CHANGE;

		String errorContactDetails =
			"Could not update your contact details. Contact " +
				"<a class='normal-link' href='mailto:calldirect@cuscal.com.au'>" +
					"CallDirect</a> on <span class='nowrap'>1300 650 501</span>";

		String errorEmail =
			"Could not update your contact details: 'Email address is already in use by another user'. " +
				"Please contact Cuscal CallDirect on 1300 650 501 if you need assistance updating your details.";

		String errorRole =
			"You are not authorised to perform this function. " +
				"Please contact <a class='normal-link' href='mailto:calldirect@cuscal.com.au'>" +
					"CallDirect</a> on <span class='nowrap'>1300 650 501";

		PortletRequest request = portletContext.getRequest();
		serviceRequestValidator.validate(serviceRequestForm, bindingResult);

		if (!doesUserHaveServiceRequestAddRole(request)) {
			bindingResult.rejectValue(
				"visaTransactionInformation.errorMsg", errorRole);

			logger.debug(
				"callAddServiceRequestForVisa - user does not have self " +
					"service role");
		}

		if (!bindingResult.hasErrors()) {
			try {
				userDetailsUpdateStatus = updateUserDetailsInLdapAndWebService(
					serviceRequestForm.getContactInformation(), request);

				if (userDetailsUpdateStatus == LDAPConstants.EMAIL_ERROR) {
					bindingResult.rejectValue(
						"visaTransactionInformation.errorMsg", errorEmail);
				}
				else if (userDetailsUpdateStatus ==
							LDAPConstants.UPDATE_ERROR) {

					bindingResult.rejectValue(
						"visaTransactionInformation.errorMsg",
						errorContactDetails);
				}
			}
			catch (SystemException e1) {
				bindingResult.rejectValue(
					"visaTransactionInformation.errorMsg", errorContactDetails);
				logger.error(
					TransactionSearchController.class.toString() +
						"callAddServiceRequestForVisa - user update fail - SystemException ",
					e1);
			}
			catch (PortalException e1) {
				bindingResult.rejectValue(
					"visaTransactionInformation.errorMsg", errorContactDetails);
				logger.error(
					TransactionSearchController.class.toString() +
						"callAddServiceRequestForVisa - user update fail - PortalException - ",
					e1);
			}
		}

		if (!bindingResult.hasErrors() &&
			(userDetailsUpdateStatus == LDAPConstants.UPDATED)) {

			String message = transactionSearchService.addServiceRequest(
				serviceRequestForm, header, portletContext);

			if (StringUtils.equalsIgnoreCase("SUCCESS", message)) {
				logger.debug(
					"callAddServiceRequestForVisa - message from add request is: " +
						message);
			}
			else {
				bindingResult.rejectValue(
					"visaTransactionInformation.errorMsg",
					"Errors processing Service request");

				logger.error(
					"callAddServiceRequest - add service request failed");
			}
		}
	}

	/**
	 *
	 */
	private Transaction createSessionTransactionObject(
		TransactionDetail transactionDetail) {

		Transaction transaction = new Transaction();

		transaction.setTransactionAmount(
			transactionDetail.getRequestDetails(
			).getTransactionAmt());
		//request.setAttribute("transactionAmountCurrency", transactionDetail.getRequestDetails().getTransactionAmt());//
		transaction.setMaskedPan(
			transactionDetail.getRequestDetails(
			).getMaskedPan());
		transaction.setAcquirerId(
			transactionDetail.getRequestDetails(
			).getAcquirerId());
		transaction.setAcquirerName(
			transactionDetail.getRequestDetails(
			).getAcquirerName());
		transaction.setIssuerId(
			transactionDetail.getRequestDetails(
			).getIssuerId());
		transaction.setIssuerName(
			transactionDetail.getRequestDetails(
			).getIssuerName());
		transaction.setTerminalId(
			transactionDetail.getRequestDetails(
			).getOrignalAcquirerTerminalId());
		transaction.setTransactionLocalDate(
			transactionDetail.getRequestDetails(
			).getTransactionLocationDateTime());
		transaction.setTransactionLocalDateTimeZone(
			transactionDetail.getRequestDetails(
			).getTransactionLocalDateTimeZone());
		transaction.setBusinessDate(transactionDetail.getBusDate());
		transaction.setCardholderAmount(
			transactionDetail.getRequestDetails(
			).getCardholderAmt());
		transaction.setTransactionId(transactionDetail.getTransactionId());
		transaction.setStan(
			transactionDetail.getRequestDetails(
			).getAcquirerSystemTrace());
		transaction.setVisaTransactionId(
			transactionDetail.getExternalTransactionId());
		transaction.setAtmPos(
			transactionDetail.getRequestDetails(
			).getOrignalAcquirerAtmOrPos());

		if (transactionDetail.getMemberInformation() != null) {
			transaction.setMemberName(
				transactionDetail.getMemberInformation(
				).getMemberName());
			transaction.setMemberNumber(
				transactionDetail.getMemberInformation(
				).getMemberNumber());
		}

		transaction.setMessageType(transactionDetail.getMessageTypeCode());
		transaction.setOriginIchName(transactionDetail.getOriginIchName());
		transaction.setFunctionCode(transactionDetail.getFunctionCode());
		transaction.setFunctionCodeDescription(
			transactionDetail.getFunctionCodeDescription());
		transaction.setTransactionQualifierId(
			transactionDetail.getTransactionQualifierId());

		if (null != transactionDetail.getPosCondition()) {
			transaction.setPosConditionCode(
				transactionDetail.getPosCondition(
				).getCode());
			transaction.setPosConditionDescription(
				transactionDetail.getPosCondition(
				).getDescription());
		}

		if (null != transactionDetail.getPosEntryMode()) {
			transaction.setPosEntryModeCode(
				transactionDetail.getPosEntryMode(
				).getCode());
			transaction.setPosEntryModeDescription(
				transactionDetail.getPosEntryMode(
				).getDescription());
		}

		return transaction;
	}

	private boolean displayFunctionCode(List<TransactionList> listForms) {
		boolean displayFunctionCode = false;

		if (null != listForms) {
			for (TransactionList transactionList : listForms) {
				String functionCode = transactionList.getFunctionCode();

				if (StringUtils.isNotBlank(functionCode)) {
					functionCode = functionCode.trim();

					if (FunctionCode.MASTERCARD_FIRST_PRESENTMENT.getValue(
						).equals(
							functionCode
						) ||
						FunctionCode.MASTERCARD_FIRST_PRESENTMENT.getValue(
						).equals(
							functionCode
						) ||
						FunctionCode.MASTERCARD_FIRST_PRESENTMENT.getValue(
						).equals(
							functionCode
						)) {

						displayFunctionCode = true;

						break;
					}
				}
			}
		}

		return displayFunctionCode;
	}

	private Boolean displayMasterCardDisputeFunction(
		String functionCode, String messageType,
		boolean existingMasterCardDisputeRetrievalRequest,
		boolean existingMasterCardDisputeFirstChargeback,
		boolean existingMasterCardDisputeArbitrationChargeback,
		boolean existingMasterCardDisputeReportFraud,
		SearchHeader searchHeader) {

		logger.debug(
			"functionCode=" + functionCode + ";messageType=" + messageType +
				";existingMasterCardDisputeRetrievalRequest=" +
					existingMasterCardDisputeRetrievalRequest +
						";existingMasterCardDisputeFirstChargeback=" +
							existingMasterCardDisputeFirstChargeback +
								";existingMasterCardDisputeArbitrationChargeback=" +
									existingMasterCardDisputeArbitrationChargeback +
										";existingMasterCardDisputeReportFraud=" +
											existingMasterCardDisputeReportFraud);

		Boolean showMasterCardDispute = Boolean.TRUE;

		if (Constants.MASTERCARD_PRESENTMENT_FUNCTION_CODE_TRANSACTION_LOG.
				equals(messageType)) {

			if (FunctionCode.MASTERCARD_FIRST_PRESENTMENT.getValue(
				).equals(
					functionCode
				)) {

				if (existingMasterCardDisputeRetrievalRequest &&
					existingMasterCardDisputeFirstChargeback &&
					existingMasterCardDisputeReportFraud) {

					logger.debug(
						" This is a first presentment, there are existing retrieval request,first chargeback and report fraud");
					showMasterCardDispute = Boolean.FALSE;
				}
			}
			else if (FunctionCode.MASTERCARD_SECOND_PRESENTMENT_FULL.getValue(
					).equals(
						functionCode
					)) {

				if (existingMasterCardDisputeRetrievalRequest &&
					existingMasterCardDisputeArbitrationChargeback) {

					logger.debug(
						" This is a second presentment full , there are existing retrieval request and arbitration chargeback");
					showMasterCardDispute = Boolean.FALSE;
				}
			}
			else if (FunctionCode.MASTERCARD_SECOND_PRESENTMENT_PARTIAL.
						getValue(
						).equals(
							functionCode
						)) {

				if (existingMasterCardDisputeRetrievalRequest &&
					existingMasterCardDisputeArbitrationChargeback) {

					logger.debug(
						" This is a second presentment partial, there are existing retrieval request and arbitration chargeback");
					showMasterCardDispute = Boolean.FALSE;
				}
			}
		}

		return showMasterCardDispute;
	}

	private Boolean doesUserHaveServiceRequestAddRole(PortletRequest request) {
		Boolean hasRole = Boolean.FALSE;
		String checkingForRole = transactionAppProperties.getTransProps(
		).getProperty(
			"service.request.role.add"
		);

		logger.debug(
			"doesUserHaveServiceRequestRole - checkingForRole is: " +
				checkingForRole);
		User user = Utility.getUserFromRequest(request);

		if (null != user) {
			List<Role> userRoleList = Utility.getAllUserRoles(user);

			for (Role role : userRoleList) {
				if (StringUtils.equalsIgnoreCase(
						checkingForRole, role.getName())) {

					hasRole = Boolean.TRUE;
				}
			}
		}

		return hasRole;
	}

	/**
	 *
	 * @param request
	 * @return	Boolean
	 */
	private Boolean doesUserHaveServiceRequestRole(PortletRequest request) {
		Boolean hasRole = Boolean.FALSE;
		String checkingForRole = transactionAppProperties.getTransProps(
		).getProperty(
			"service.request.role.view"
		);

		User user = Utility.getUserFromRequest(request);

		if (null != user) {
			List<Role> userRoleList = Utility.getAllUserRoles(user);

			for (Role role : userRoleList) {
				if (StringUtils.equalsIgnoreCase(
						checkingForRole, role.getName())) {

					hasRole = Boolean.TRUE;
				}
			}
		}

		return hasRole;
	}

	private String getCardControlRejectCode(String customData) {
		String code = Utility.getCardControlRejectCodeFromCustomData(
			customData);

		if (StringUtils.isNotBlank(code)) {
			return transactionAppProperties.getTransProps(
			).getProperty(
				Constants.CARD_CONTROL_REJECT_CODE_PROPSKEY + code, ""
			);
		}

		return "";
	}

	private boolean hasExistingReinvestigation(
		TicketDetails ticket, boolean existingReinvestigation) {

		List<AttributesType> ticketAttributesList =
			ticket.getTicketAttributesList();

		if (null != ticketAttributesList) {
			for (AttributesType attributesType : ticketAttributesList) {
				if (attributesType.getRequestCode() == 30) {
					logger.debug(
						"hasExistingReinvestigation - found existing reinvestigation for ticket id =" +
							ticket.getTicketId());
					existingReinvestigation = true;

					break;
				}
			}
		}

		return existingReinvestigation;
	}

	/**
	 * Retrieve the paging and the sorting attributes from session and set them in the request
	 *
	 * @param RenderResponse
	 */
	private void retrieveAndSetPagingAndSortingParameters(
		RenderRequest request) {

		String pagingParam = new ParamEncoder(
			Constants.TX_FRM
		).encodeParameterName(
			TableTagParameters.PARAMETER_PAGE
		);
		String sortingParam = new ParamEncoder(
			Constants.TX_FRM
		).encodeParameterName(
			TableTagParameters.PARAMETER_SORT
		);

		String currentPage = request.getParameter(pagingParam);
		String currentSort = request.getParameter(sortingParam);

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession(true);

		if (StringUtils.isNotBlank(pagingParam) &&
			StringUtils.isNotBlank(currentPage)) {

			session.setAttribute(Constants.TX_CURRENT_PAGE, currentPage);
		}
		else {
			session.removeAttribute(Constants.TX_CURRENT_PAGE);
		}

		if (StringUtils.isNotBlank(sortingParam) &&
			StringUtils.isNotBlank(sortingParam)) {

			session.setAttribute(Constants.TX_CURRENT_SORT, currentSort);
		}
		else {
			session.removeAttribute(Constants.TX_CURRENT_SORT);
		}
	}

	private Transaction setAdditionalTransactionAttributes(
			ResourceRequest request, ResourceResponse response,
			SearchHeader searchHeader, String externalTransactionId,
			Transaction transaction)
		throws Exception {
		//pramod
		TransactionForm transactionForm = setInputRequestFields(request);

		if (null == transactionForm) {
			return transaction;
		}
		//search by externalTransactionId
		transactionForm.setExternalTransactionId(externalTransactionId);
		logger.debug(
			"setAdditionalTransactionAttributes clearing externalTransactionId=" +
				externalTransactionId);
		List<Object> listSearch =
			transactionSearchService.getTransactionListOnSearch(
				transactionForm, searchHeader,
				PortletContext.newContext(response, request));

		if ((null != listSearch) && (listSearch.size() > 0)) {
			@SuppressWarnings("unchecked")
			List<TransactionList> listForms =
				(List<TransactionList>)listSearch.get(0);

			if (null != listForms) {
				for (TransactionList transactionList : listForms) {
					//if success then only, proceed
					String sortableMsgType = null;

					if (StringUtils.isNotBlank(
							transactionList.getResponseCode()) &&
						STRING_00.equals(
							transactionList.getResponseCode(
							).trim())) {

						sortableMsgType = transactionList.getSortableMsgType();
					}
					//MasterCard Auth and Auth advice

					if (StringUtils.isNotBlank(sortableMsgType) &&
						(sortableMsgType.equals(STRING_0100) ||
						 sortableMsgType.equals(STRING_0120))) {

						TransactionDetail transactionDetailAuth =
							new TransactionDetail();

						logger.debug(
							"setAdditionalTransactionAttributes transactionList.getTransactionId()=" +
								transactionList.getTransactionId());
						String busniessDate = transactionList.getBusniessDate();
						logger.debug(
							"setAdditionalTransactionAttributes busniessDate=" +
								busniessDate);

						transactionDetailAuth.setBusDate(busniessDate);
						transactionDetailAuth.setTransactionId(
							transactionList.getTransactionId());

						String transactionSource = request.getParameter(
							Constants.TX_SRC);

						if (StringUtils.isBlank(transactionSource)) {
							HttpServletRequest httpServletRequest =
								PortalUtil.getHttpServletRequest(request);

							HttpSession session =
								httpServletRequest.getSession();

							transactionSource = (String)session.getAttribute(
								Constants.TX_SRC);
						}

						transactionDetailAuth.setDataSrc(transactionSource);

						transactionDetailAuth =
							transactionSearchService.
								getTransactionDetailByTxIdBusDateAndSrc(
									transactionDetailAuth, searchHeader,
									PortletContext.newContext(
										response, request));

						if (null != transactionDetailAuth) {
							logger.debug(
								"transactionDetailAuth.getTransactionId()=" +
									transactionDetailAuth.getTransactionId());
							String customData =
								transactionDetailAuth.getCustomData();
							logger.debug(
								"transactionDetailAuth customData=" +
									customData);

							if (StringUtils.isNotBlank(customData)) {
								//ucaf
								String ucaf = Utility.getUCAFFromCustomData(
									customData);
								logger.debug(
									"setAdditionalTransactionAttributes ucaf=" +
										ucaf);

								if (StringUtils.isNotBlank(ucaf)) {
									transaction.setUCAFCollectionIndicator(
										ucaf);
									transaction.
										setUCAFCollectionIndicatorDescription(
											Utility.
												getUCAFCollectionIndicatorDescription(
													transaction.
														getUCAFCollectionIndicator()));
								}
								//csbd
								String csbd = Utility.getCSBDFromCustomData(
									customData);
								logger.debug(
									"setAdditionalTransactionAttributes csbd=" +
										csbd);

								if (StringUtils.isNotBlank(csbd)) {
									transaction.setCentralSiteBusinessDate(
										Utility.formatCSBDDate(csbd));
								}
								else {
									transaction.setCentralSiteBusinessDate(
										EMPTY_STRING);
								}
								//pan entry mode
								String panEntryMode =
									Utility.getPanEntryModeFromCustomData(
										customData);
								String chipCardPresent = NO;
								logger.debug(
									"setAdditionalTransactionAttributes panEntryMode=" +
										panEntryMode);

								if (StringUtils.isNotBlank(panEntryMode)) {
									//Chip Card
									String[] chipPanEntryMode = {"05", "07"};
									chipCardPresent = Arrays.asList(
										chipPanEntryMode
									).contains(
										panEntryMode.trim()
									) ? YES : NO;
								}

								transaction.setChipCardPresent(chipCardPresent);
								//eCommerce Transaction
								String transactionCategoryCode =
									Utility.
										getTransactionCategoryCodeFromCustomData(
											customData);
								logger.debug(
									"setAdditionalTransactionAttributes transactionCategoryCode=" +
										transactionCategoryCode);

								if (StringUtils.isNotBlank(
										transactionCategoryCode)) {

									// DE48 - TCC - transaction category Code T for Phone, Mail, or Electronic Commerce Order

									String eci = transactionCategoryCode.trim(
									).equalsIgnoreCase(
										STRING_T
									) ? YES : NO;

									transaction.setElectronicCommerceIndciator(
										eci);
								}
								else {
									transaction.setElectronicCommerceIndciator(
										NO);
								}
								//service code
								String track2ServiceCode =
									transactionDetailAuth.
										getTrack2DataServiceCode();//Utility.getTrack2ServiceCodeFromCustomData(customData);
								logger.debug(
									"setAdditionalTransactionAttributes track2ServiceCode=" +
										track2ServiceCode);

								if (StringUtils.isNotBlank(track2ServiceCode) &&
									!STRING_0.equals(
										track2ServiceCode.trim())) {

									transaction.setTrack2DataServiceCode(
										YES + COMMA + SPACE + SERVICE_CODE +
											SPACE + track2ServiceCode);
								}
								else {
									transaction.setTrack2DataServiceCode(NO);
								}
							}
						}
					}
				}
			}
		}

		return transaction;
	}

	private TransactionForm setInputRequestFields(ResourceRequest request) {
		//set input fields
		TransactionForm transactionForm = new TransactionForm();

		transactionForm.setAmount(EMPTY_STRING);
		transactionForm.setAmountType(BILLING_AMOUNT);
		transactionForm.setConditions(STRING_EQ);
		transactionForm.setCuscalId(EMPTY_STRING);
		transactionForm.setDateDiff(INT_30);
		transactionForm.setDateType(STRING_LOCAL);
		String businessDateString = request.getParameter(BUS_DATE);

		if (StringUtils.isBlank(businessDateString)) {
			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpSession session = httpServletRequest.getSession();

			businessDateString = (String)session.getAttribute(
				Constants.BUS_DATE);
		}

		if (StringUtils.isBlank(businessDateString)) {
			return null;
		}

		Date businessDate = Utility.formatDate(
			businessDateString, DATE_FORMAT_YYYYMMDD);

		Date startBusinessDate = Utility.getDaysForwardDateFromEndDate(
			businessDate, INT_30);
		Date endBusinessDate = Utility.getDaysBackDateFromEndDate(
			businessDate, INT_30);
		String endDateString = Utility.formatDateToString(
			startBusinessDate, DATE_FORMAT);
		String startDateString = Utility.formatDateToString(
			endBusinessDate, DATE_FORMAT);
		transactionForm.setEndDate(endDateString);
		transactionForm.setEndTimeHr(STRING_24);
		transactionForm.setEndTimeMin(STRING_00);
		transactionForm.setMerchantId(EMPTY_STRING);
		transactionForm.setMessageCode(EMPTY_STRING);
		transactionForm.setPanBin(EMPTY_STRING);
		transactionForm.setResponseCode(EMPTY_STRING);
		transactionForm.setRrn(EMPTY_STRING);
		transactionForm.setStan(EMPTY_STRING);
		transactionForm.setStartDate(startDateString);
		transactionForm.setStartTimeHr(STRING_00);
		transactionForm.setStartTimeMin(STRING_00);

		return transactionForm;
	}

	/**
	 * Method to set the paging and sorting parameters retrieved from session
	 *
	 * @param request
	 * @param response
	 */
	private void setPagingAndSortingParameters(
		ActionRequest request, ActionResponse response) {

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession(true);

		if (null != session.getAttribute(Constants.TX_CURRENT_PAGE)) {
			response.setRenderParameter(
				new ParamEncoder(
					Constants.TX_FRM
				).encodeParameterName(
					TableTagParameters.PARAMETER_PAGE
				),
				(String)session.getAttribute(Constants.TX_CURRENT_PAGE));
		}

		if (null != session.getAttribute(Constants.TX_CURRENT_SORT)) {
			response.setRenderParameter(
				new ParamEncoder(
					Constants.TX_FRM
				).encodeParameterName(
					TableTagParameters.PARAMETER_SORT
				),
				(String)session.getAttribute(Constants.TX_CURRENT_SORT));
		}
	}

	/**
	 *
	 * @param contactInfo
	 * @param request
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	private int updateUserDetailsInLdapAndWebService(
			RequestContactInformation contactInfo, PortletRequest request)
		throws PortalException {

		logger.debug(
			TransactionSearchController.class.toString() +
				" - updateUserDetailsInLdapAndWebService() - Start.");

		String screenName = "";
		User user = PortalUtil.getUser(request);

		if (user != null) {
			screenName = user.getScreenName();
		}

		String fName = contactInfo.getGivenName();
		String lName = contactInfo.getSurname();
		String emailId = contactInfo.getEmail();
		String phoneNo = contactInfo.getPhoneNumber();

		if (!fName.equalsIgnoreCase("") && !lName.equalsIgnoreCase("") &&
			!emailId.equalsIgnoreCase("") && !phoneNo.equalsIgnoreCase("")) {

			UserUtilImpl userUtil = new UserUtilImpl();

			int updateStatus = userUtil.syncUserAttributesIfChanged(
				user.getUserId(), fName, lName, emailId, phoneNo);

			if (updateStatus != LDAPConstants.NO_CHANGE) {
				if ((updateStatus == LDAPConstants.EMAIL_ERROR) ||
					(updateStatus == LDAPConstants.UPDATE_ERROR)) {

					return updateStatus;
				}

				try {
					LDAPUtil.getInstance(
					).modifyAttributesForUserDetails(
						screenName, fName, lName, emailId, phoneNo
					);
				}
				catch (NamingException e) {
					logger.error(
						TransactionSearchController.class.toString() +
							" - updateUserDetailsInLdapAndWebService() - Exception: Failed to " +
								"update User details directly in LDAP",
						e);
					request.setAttribute(
						"userDetailsUpdateFailure", Boolean.TRUE);

					return LDAPConstants.UPDATE_ERROR;
				}

				try {
					user = UserLocalServiceUtil.getUser(user.getUserId());
					userUtil.addOrUpdateUserInGetTicketUserService(user);
				}
				catch (SystemException e) {
					logger.error(
						TransactionSearchController.class.toString() +
							" - updateUserDetailsInLdapAndWebService() - Exception: Failed to " +
								"update User details in Business objects with GetTicketUser service",
						e);

					request.setAttribute(
						"userDetailsUpdateFailure", Boolean.TRUE);

					return LDAPConstants.UPDATE_ERROR;
				}
				catch (PortalException e) {
					logger.error(
						TransactionSearchController.class.toString() +
							" - updateUserDetailsInLdapAndWebService() - Exception: Failed to " +
								"update User details in Business objects with GetTicketUser service",
						e);
					request.setAttribute(
						"userDetailsUpdateFailure", Boolean.TRUE);

					return LDAPConstants.UPDATE_ERROR;
				}

				logger.debug(
					TransactionSearchController.class.toString() +
						" - updateUserDetails() - User Details updated successfully.");
				request.setAttribute("userDetailsUpdateSuccess", Boolean.TRUE);

				audit.success(
					null, request, AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_DETAIL,
					screenName + " has updated firstname, surname and email");
			}
			else {
				logger.debug(
					TransactionSearchController.class.toString() +
						" - updateUserDetailsInLdapAndWebService() - No Update for user required:" +
							screenName);
			}
		}

		logger.debug(
			TransactionSearchController.class.toString() +
				" - updateUserDetailsInLdapAndWebService() - End.");

		return LDAPConstants.UPDATED;
	}

	private static final String _TRANSACTION_FORM = "_transactionForm";

	/**
	 * Audit object
	 */
	private static final Audit _audit = new Log4jAuditor();

	private static final Auditor audit = Auditor.getInstance();

	/**
	 * Logger object
	 */
	private static Logger logger = Logger.getLogger(
		TransactionSearchController.class);

	/**
	 * Logger object for performance logging
	 */
	private static Logger perfLogger_ = Logger.getLogger("perfLogging");

	/**
	 * organisationIdOverride object
	 */
	private String organisationIdOverride = null;

	@Autowired
	@Qualifier("serviceRequestValidator")
	private ServiceRequestValidator serviceRequestValidator;

	@Autowired
	@Qualifier("transactionAppProperties")
	private TransactionAppProperties transactionAppProperties;

	/**
	 * Service used to fetch data
	 */
	@Autowired
	@Qualifier(Constants.TRANSACTION_SERVICE)
	private TransactionSearchService transactionSearchService;

	/**
	 * Transaction Validator
	 */
	@Autowired
	@Qualifier(Constants.TRANSACTION_FORM_VALIDATOR)
	private TransactionSearchValidator transactionSearchValidator;

}