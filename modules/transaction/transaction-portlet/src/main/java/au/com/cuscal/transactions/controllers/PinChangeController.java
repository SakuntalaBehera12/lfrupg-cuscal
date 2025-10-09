package au.com.cuscal.transactions.controllers;

import au.com.cuscal.framework.audit.Audit;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.category.AuditCategories;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.audit.impl.log4j.Log4jAuditor;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;
import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.commons.Sortable;
import au.com.cuscal.transactions.commons.TransactionAppProperties;
import au.com.cuscal.transactions.commons.Utility;
import au.com.cuscal.transactions.domain.TransactionDetail;
import au.com.cuscal.transactions.domain.TransactionList;
import au.com.cuscal.transactions.forms.PinChangeSearchForm;
import au.com.cuscal.transactions.services.TransactionSearchService;
import au.com.cuscal.transactions.validator.PinChangeHistorySearchValidator;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;
import com.liferay.portletmvc4spring.bind.annotation.ResourceMapping;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
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

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller(Constants.PIN_CHANGE_CONTROLLER)
@RequestMapping(value = "VIEW")
@SessionAttributes(types = PinChangeSearchForm.class)
public class PinChangeController {

	/**
	 * Audit while printing the view of the Transaction detail page
	 *
	 * @param response
	 * @param request
	 * @return void
	 */
	@ResourceMapping(Constants.PIN_CHANGE_PRINT_DETAILS)
	public void auditOnPrintTxDetails(
		ResourceRequest request, ResourceResponse response) {

		audit.success(
			response, request, AuditOrigin.PORTAL_ORIGIN,
			AuditCategories.TRANSACTION_DETAIL,
			"The user is printing the transaction details ");
	}

	/**
	 * create the export file data.
	 *
	 * @param List <TransactionList>
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook createExcelFile(List<TransactionList> lists) {
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
			"Card Acceptor Name"
		);
		header.createCell(
			3
		).setCellValue(
			"Response Code"
		);
		header.createCell(
			4
		).setCellValue(
			"System Trace"
		);
		header.createCell(
			5
		).setCellValue(
			"Date/Time"
		);
		header.createCell(
			6
		).setCellValue(
			"Description"
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
			row.createCell(
				4
			).setCellValue(
				list.getSystemTrace()
			);
			row.createCell(
				5
			).setCellValue(
				Utility.formatDateToString(
					list.getDateTime(), Constants.DATE_TIME_FORMAT)
			);
			row.createCell(
				6
			).setCellValue(
				list.getDescription()
			);
		}

		return workbook;
	}

	/**
	 * create the export file for transaction list and send to portlet
	 *
	 * @param response
	 * @param request
	 * @return void
	 */
	@ResourceMapping(Constants.PIN_CHANGE_EXPORT_LIST)
	@SuppressWarnings("unchecked")
	public void exportDataXls(
		ResourceRequest request, ResourceResponse response) {

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		List<TransactionList> lists = null;
		HSSFWorkbook workbook = null;

		if (null != session.getAttribute(
				Constants.PIN_CHANGE_SEARCH_RESULTS_LIST)) {

			lists = (List<TransactionList>)session.getAttribute(
				Constants.PIN_CHANGE_SEARCH_RESULTS_LIST);
			workbook = createExcelFile(lists);

			audit.success(
				response, request, AuditOrigin.PORTAL_ORIGIN,
				AuditCategories.TRANSACTION_SEARCH,
				"	The Export Transactions List size is  " + lists.size());

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
					"Exception while Exporting Transactions List in xls format, exception message is  " +
						e.getMessage());
				audit.fail(
					response, request, AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_SEARCH,
					" Export Transactions, the Exception: " + e.getMessage());
			}
		}
	}

	/**
	 * Shows the search results of the Transaction
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@ActionMapping(
		params = Constants.TX_ACTION + Constants.EQUALS + Constants.PIN_CHANGE_2FA
	)
	public void forwardTo2FAPageAndBackToTxDetails(
		@ModelAttribute(Constants.PIN_CHANGE_SEARCH_FORM) PinChangeSearchForm
			pinChangeSearchForm,
		ActionResponse response, SessionStatus sessionStatus,
		ActionRequest request) {

		logger.debug("forwardto2FAPageAndBackToTxDetails - start");

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		String steppedUp = "";

		logger.debug(
			"forwardto2FAPageAndBackToTxDetails - The _USER_STEPPEDUP session attribute is " +
				session.getAttribute(Constants._USER_STEPPEDUP));

		if ((null != session.getAttribute(Constants.USER_CANCEL)) &&
			Constants.CANCEL.equals(
				session.getAttribute(Constants.USER_CANCEL))) {

			session.setAttribute(Constants.USER_CANCEL, null);

			logger.debug(
				"forwardto2FAPageAndBackToTxDetails - session.getAttribute(Constants.USER_CANCEL) " +
					session.getAttribute(Constants.USER_CANCEL));

			if (null != session.getAttribute(
					Constants.PIN_CHANGE_SEARCH_RESULTS_LIST)) {

				@SuppressWarnings("unchecked")
				List<TransactionList> pinChangeList =
					(List<TransactionList>)session.getAttribute(
						Constants.PIN_CHANGE_SEARCH_RESULTS_LIST);

				logger.debug(
					"forwardTo2FAPageAndBackToTxDetails - search results list is: " +
						pinChangeList.size());

				request.setAttribute(
					Constants.PIN_CHANGE_LIST_REQ, pinChangeList);
			}

			request.setAttribute(Constants.IS_FA, setUser2FaRoles(request));

			session.setAttribute(Constants._RETURN_URL, null);

			steppedUp = (String)session.getAttribute(Constants._USER_STEPPEDUP);

			logger.debug(
				"forwardto2FAPageAndBackToTxDetails - steppedup: value is  " +
					steppedUp);

			request.setAttribute(Constants.ACTION, Constants._2FA_ACTION);
			response.setRenderParameter(
				Constants.TX_RENDER, Constants.PIN_CHANGE_2FA_RENDER);
		}
		else {
			if ((null != session.getAttribute(Constants._USER_STEPPEDUP)) &&
				(Constants.FALSE.equals(
					session.getAttribute(Constants._USER_STEPPEDUP)) ||
				 Constants.ERROR.equals(
					 session.getAttribute(Constants._USER_STEPPEDUP)))) {

				String url = PortalUtil.getCurrentURL(httpServletRequest);

				session.setAttribute(Constants._RETURN_URL, url);

				try {
					response.sendRedirect("/2fa");
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			else {
				logger.debug(
					"In else part after the 2fa ^^^^^^^^^^^^^^^^ " +
						pinChangeSearchForm.getPanBin());

				// SearchHeader searchHeader = getSearchHeaderData(request);

				if (null != session.getAttribute(
						Constants.PIN_CHANGE_SEARCH_RESULTS_LIST)) {

					@SuppressWarnings("unchecked")
					List<TransactionList> listForms =
						(List<TransactionList>)session.getAttribute(
							Constants.PIN_CHANGE_SEARCH_RESULTS_LIST);

					request.setAttribute(
						Constants.PIN_CHANGE_LIST_REQ, listForms);
				}

				steppedUp = (String)session.getAttribute(
					Constants._USER_STEPPEDUP);

				logger.debug("steppedup:   value is  " + steppedUp);

				request.setAttribute(Constants.ACTION, Constants._2FA_ACTION);
				response.setRenderParameter(
					Constants.TX_RENDER, Constants.PIN_CHANGE_2FA_RENDER);
			}
		}
	}

	/**
	 * Next page for the view of the Transaction detail page
	 *
	 * @param response
	 * @param request
	 * @return void
	 */
	@ResourceMapping(Constants.PIN_CHANGE_NEXT_DETAILS)
	@SuppressWarnings("unchecked")
	public void getNextTxDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
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

			if (null != session.getAttribute(
					Constants.PIN_CHANGE_SEARCH_RESULTS_LIST)) {

				lists = (List<TransactionList>)session.getAttribute(
					Constants.PIN_CHANGE_SEARCH_RESULTS_LIST);

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

					session.setAttribute(Constants.CURR_ROW_NUM, curRowNum);
					session.setAttribute(
						Constants.PIN_CHANGE_BUSINESS_DATE,
						transactionDetail.getBusDate());
					session.setAttribute(
						Constants.PIN_CHANGE_SOURCE,
						transactionDetail.getDataSrc());
					session.setAttribute(
						Constants.PIN_CHANGE_TRANSACTION_ID,
						transactionDetail.getTransactionId());

					if ((null != session.getAttribute(
							Constants._USER_STEPPEDUP)) &&
						(Constants.FALSE.equals(
							session.getAttribute(Constants._USER_STEPPEDUP)) ||
						 Constants.ERROR.equals(
							 session.getAttribute(
								 Constants._USER_STEPPEDUP)))) {

						logger.debug(
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP) is false " +
								session.getAttribute(
									Constants._USER_STEPPEDUP));
						transactionDetail.setUserStepUp(false);
					}
					else if ((null != session.getAttribute(
								Constants._USER_STEPPEDUP)) &&
							 Constants.TRUE.equals(
								 session.getAttribute(
									 Constants._USER_STEPPEDUP))) {

						logger.debug(
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP) is true  " +
								session.getAttribute(
									Constants._USER_STEPPEDUP));
						transactionDetail.setUserStepUp(true);
					}
					else {
						logger.debug(
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP) final else false");
						transactionDetail.setUserStepUp(false);
					}

					try {
						SearchHeader searchHeader = getSearchHeader(request);

						if ((null != searchHeader) &&
							(searchHeader.getUserOrgId() != null)) {

							transactionDetail =
								transactionSearchService.
									getPinChangeDetailByIdBusDateAndSrc(
										transactionDetail, searchHeader,
										PortletContext.newContext(
											response, request));
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
								"The Search header is null or  user is not logged in or org short name is null");
						}
					}
					catch (Exception e) {
						logger.error(
							"[TransactionSearchController.getTransactionDetails] EXCEPTION CAME ERROR MSG e.getCause()  is " +
								e.getCause());
					}
				}
			}
		}
	}

	public String getOrgIdOverride() {
		return orgIdOverride;
	}

	/**
	 * Get the org short name
	 *
	 * @param liferayOrgId
	 * @param portletRequest
	 * @return String
	 */
	public String getOrgShortName(
		String liferayOrgId, PortletRequest portletRequest) {

		logger.debug("getOrgShortName - start, liferayOrgId=" + liferayOrgId);

		if (StringUtils.isNotEmpty(orgIdOverride)) {
			logger.debug(
				"getOrgShortName - end, organisationIdOverride has been set. Using " +
					orgIdOverride);
			liferayOrgId = orgIdOverride;
			logger.debug("getOrgShortName - liferayOrgId is: " + liferayOrgId);
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

	@ModelAttribute(Constants.PIN_CHANGE_SEARCH_FORM)
	public PinChangeSearchForm getPinChangeCommandObject() {
		logger.debug(
			"getPinChangeCommandObject - Inside the PinChangeController");

		PinChangeSearchForm pinChangeForm =
			transactionSearchService.createPinChangeSearchFormObject();

		return pinChangeForm;
	}

	//Override values only for Development.
	/**
	 * previous page for the view of the Transaction detail page
	 *
	 * @param response
	 * @param request
	 * @return void
	 */
	@ResourceMapping(Constants.PIN_CHANGE_PREV_DETAILS)
	@SuppressWarnings("unchecked")
	public void getPrevTxDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		TransactionDetail transactionDetail = new TransactionDetail();
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
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

				if (null != session.getAttribute(
						Constants.PIN_CHANGE_SEARCH_RESULTS_LIST)) {

					lists = (List<TransactionList>)session.getAttribute(
						Constants.PIN_CHANGE_SEARCH_RESULTS_LIST);
					TransactionList transactionList = lists.get(curRowNum - 1);

					transactionDetail.setBusDate(
						transactionList.getBusniessDate());
					logger.debug(
						"[getPrevTxDetails] getBusniessDate " +
							transactionList.getBusniessDate());
					transactionDetail.setTransactionId(
						transactionList.getTransactionId());
					transactionDetail.setDataSrc(
						transactionList.getDataSource());

					transactionDetail.setNextRowNum(curRowNum + 1);

					logger.debug(
						"[getPrevTxDetails] setting the session valuess attributes ");
					session.setAttribute(
						Constants.PIN_CHANGE_TRANSACTION_ID,
						transactionDetail.getTransactionId());
					session.setAttribute(
						Constants.PIN_CHANGE_BUSINESS_DATE,
						transactionDetail.getBusDate());
					session.setAttribute(
						Constants.PIN_CHANGE_SOURCE,
						transactionDetail.getDataSrc());
					session.setAttribute(Constants.CURR_ROW_NUM, curRowNum);

					if ((null != session.getAttribute(
							Constants._USER_STEPPEDUP)) &&
						(Constants.FALSE.equals(
							session.getAttribute(Constants._USER_STEPPEDUP)) ||
						 Constants.ERROR.equals(
							 session.getAttribute(
								 Constants._USER_STEPPEDUP)))) {

						logger.debug(
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP) is false " +
								session.getAttribute(
									Constants._USER_STEPPEDUP));
						transactionDetail.setUserStepUp(false);
					}
					else if ((null != session.getAttribute(
								Constants._USER_STEPPEDUP)) &&
							 Constants.TRUE.equals(
								 session.getAttribute(
									 Constants._USER_STEPPEDUP))) {

						logger.debug(
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP) is true  " +
								session.getAttribute(
									Constants._USER_STEPPEDUP));
						transactionDetail.setUserStepUp(true);
					}
					else {
						logger.debug(
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP) final else false");
						transactionDetail.setUserStepUp(false);
					}

					try {
						SearchHeader searchHeader = getSearchHeader(request);

						if ((null != searchHeader) &&
							(searchHeader.getUserOrgId() != null)) {

							transactionDetail =
								transactionSearchService.
									getPinChangeDetailByIdBusDateAndSrc(
										transactionDetail, searchHeader,
										PortletContext.newContext(
											response, request));

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
								"The Search header is null or  user is not logged in");
						}
					}
					catch (Exception e) {
						logger.error(
							"[TransactionSearchController.getTransactionDetails] EXCEPTION CAME ERROR MSG e.getCause()  is " +
								e.getCause());
					}
				}
			}
		}
	}

	/**
	 * Shows the initial view of the Transaction detail page
	 *
	 * @param response
	 * @param request
	 * @return void
	 */
	@ResourceMapping(Constants.PIN_CHANGE_DETAIL_RESOURCE)
	public void getTransactionDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.debug(
			"getTransactionDetails - inside the @ResourceMapping Role is  ");

		boolean is2faRole = setUser2FaRoles(request);

		logger.debug("getTransactionDetails - is2faRole: " + is2faRole);

		TransactionDetail transactionDetail = new TransactionDetail();
		request.setAttribute(Constants.FROM_FA, Boolean.FALSE);
		request.setAttribute(Constants.IS_FA, is2faRole);

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		if (!StringUtils.isBlank(
				request.getParameter(Constants.PIN_CHANGE_TRANSACTION_ID)) &&
			!StringUtils.isBlank(
				request.getParameter(Constants.PIN_CHANGE_BUSINESS_DATE)) &&
			!StringUtils.isBlank(
				request.getParameter(Constants.PIN_CHANGE_SOURCE))) {

			if (!StringUtils.isBlank(
					request.getParameter(Constants.CURR_ROW_NUM)) &&
				!Constants.NULL.equalsIgnoreCase(
					request.getParameter(Constants.CURR_ROW_NUM))) {

				session.setAttribute(
					Constants.CURR_ROW_NUM,
					request.getParameter(Constants.CURR_ROW_NUM));
			}

			logger.debug(
				"getTransactionDetails - setting the request attributes ");

			session.setAttribute(
				Constants.PIN_CHANGE_BUSINESS_DATE,
				request.getParameter(Constants.PIN_CHANGE_BUSINESS_DATE));
			session.setAttribute(
				Constants.PIN_CHANGE_SOURCE,
				request.getParameter(Constants.PIN_CHANGE_SOURCE));
			session.setAttribute(
				Constants.PIN_CHANGE_TRANSACTION_ID,
				request.getParameter(Constants.PIN_CHANGE_TRANSACTION_ID));

			transactionDetail.setBusDate(
				request.getParameter(
					Constants.PIN_CHANGE_BUSINESS_DATE
				).toString());
			transactionDetail.setTransactionId(
				request.getParameter(
					Constants.PIN_CHANGE_TRANSACTION_ID
				).toString());
			transactionDetail.setDataSrc(
				request.getParameter(
					Constants.PIN_CHANGE_SOURCE
				).toString());
		}

		logger.debug(
			"getTransactionDetails - Constants._USER_STEPPEDUP: " +
				session.getAttribute(Constants._USER_STEPPEDUP));

		if ((null != session.getAttribute(Constants._USER_STEPPEDUP)) &&
			(Constants.FALSE.equals(
				session.getAttribute(Constants._USER_STEPPEDUP)) ||
			 Constants.ERROR.equals(
				 session.getAttribute(Constants._USER_STEPPEDUP)))) {

			logger.debug(
				"getTransactionDetails - session.getAttribute(_USER_STEPPEDUP) is false " +
					session.getAttribute(Constants._USER_STEPPEDUP));
			transactionDetail.setUserStepUp(false);
		}
		else if ((null != session.getAttribute(Constants._USER_STEPPEDUP)) &&
				 Constants.TRUE.equals(
					 session.getAttribute(Constants._USER_STEPPEDUP))) {

			logger.debug(
				"getTransactionDetails - session.getAttribute(_USER_STEPPEDUP) is true  " +
					session.getAttribute(Constants._USER_STEPPEDUP));
			transactionDetail.setUserStepUp(true);
		}
		else {
			logger.debug(
				"getTransactionDetails - session.getAttribute(_USER_STEPPEDUP) final else false");
			transactionDetail.setUserStepUp(false);
		}

		try {
			SearchHeader searchHeader = getSearchHeader(request);

			if ((null != searchHeader) &&
				(searchHeader.getUserOrgId() != null)) {

				transactionDetail =
					transactionSearchService.
						getPinChangeDetailByIdBusDateAndSrc(
							transactionDetail, searchHeader,
							PortletContext.newContext(response, request));

				ObjectMapper mapper = new ObjectMapper();

				String json = mapper.writeValueAsString(transactionDetail);
				PrintWriter out = response.getWriter();
				response.setContentType(Constants.JSON2);
				response.setCharacterEncoding(Constants.UTF_8);
				out.print(json);
			}
			else {
				logger.error(
					"The Search header is null or  user is not logged in or org short name is null");
			}
		}
		catch (Exception e) {
			logger.error(
				"[TransactionSearchController.getTransactionDetails] EXCEPTION CAME ERROR MSG e.getCause() is " +
					e.getCause(),
				e);
		}
	}

	@ActionMapping(
		params = Constants.TX_ACTION + Constants.EQUALS + Constants.PIN_CHANGE_SEARCH_ACTION
	)
	public void pinChangeSearch(
		ActionRequest request, ActionResponse response,
		@ModelAttribute(Constants.PIN_CHANGE_SEARCH_FORM) PinChangeSearchForm
			pinChangeSearchForm,
		BindingResult bindingResult, SessionStatus sessionStatus) {

		logger.debug("pinChangeSearch - Start the PIN Change History Search");

		boolean isDataNotNull = false;

		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();

		if (StringUtils.isNotBlank(pinChangeSearchForm.getPanBin())) {
			String pinChangePan = Utility.removeAllSpacesFromPanorBin(
				pinChangeSearchForm.getPanBin());

			pinChangeSearchForm.setPanBin(pinChangePan);
		}

		pinChangeHistorySearchValidator.validate(
			pinChangeSearchForm, bindingResult);

		if (!bindingResult.hasErrors()) {
			SearchHeader searchHeader = getSearchHeader(request);

			if (searchHeader != null) {
				try {
					List<Object> searchResults =
						transactionSearchService.getPinChangeListOnSearch(
							pinChangeSearchForm, searchHeader,
							PortletContext.newContext(response, request));

					if (searchResults != null) {
						@SuppressWarnings("unchecked")
						List<TransactionList> pinChangeSearchResults =
							(List<TransactionList>)searchResults.get(0);

						session.setAttribute(
							Constants.PIN_CHANGE_SEARCH_RESULTS_LIST,
							pinChangeSearchResults);
						String wsStatusMessage = (String)searchResults.get(1);

						if ((null != pinChangeSearchResults) &&
							(0 < pinChangeSearchResults.size())) {

							logger.info(
								"pinChangeSearch - setting the response render listForms size is  " +
									pinChangeSearchResults.size());

							isDataNotNull = true;

							request.setAttribute(
								Constants.PIN_CHANGE_LIST_REQ,
								pinChangeSearchResults);

							if (pinChangeSearchResults.get(
									0
								).isMoreRecAvail()) {

								logger.info(
									"pinChangeSearch - dates range have more than 250 records for transaction search ");
								request.setAttribute(
									Constants.MORE_DATA_MSG, Boolean.TRUE);
							}
						}
						else {
							isDataNotNull = false;
						}

						if (!isDataNotNull) {
							request.setAttribute(
								Constants.PIN_CHANGE_NO_RESULTS, Boolean.TRUE);
						}

						if (!Utility.
								anyOneOfToDateAndFromDateAreNOT18MonthPastDate(
									pinChangeSearchForm)) {

							logger.info(
								"pinChangeSearch - dates are past 18 months ");
							request.setAttribute(
								Constants.DATE_WARNING_MSG, Boolean.TRUE);
						}

						Map<String, String> sortOrderMap =
							setDefaultSortOrderMap();

						session.setAttribute(Constants.PIN_CHANGE_LIST, null);
						session.setAttribute(
							Constants.PIN_CHANGE_SORT_ORDER, sortOrderMap);
						sessionStatus.setComplete();

						String[] startMinuteDisplay =
							transactionAppProperties.getTransProps(
							).getProperty(
								Constants.UI_START_MINS_PROPSKEY
							).split(
								Constants.COMMA
							);
						String[] startHourDisplay =
							transactionAppProperties.getTransProps(
							).getProperty(
								Constants.UI_START_HR_PROPSKEY
							).split(
								Constants.COMMA
							);
						String[] endHourDisplay =
							transactionAppProperties.getTransProps(
							).getProperty(
								Constants.UI_END_HR_PROPSKEY
							).split(
								Constants.COMMA
							);
						String[] endMinuteDisplay =
							transactionAppProperties.getTransProps(
							).getProperty(
								Constants.UI_END_MINS_PROPSKEY
							).split(
								Constants.COMMA
							);

						pinChangeSearchForm.setEndHourDisplay(endHourDisplay);
						pinChangeSearchForm.setEndMinuteDisplay(
							endMinuteDisplay);
						pinChangeSearchForm.setStartHourDisplay(
							startHourDisplay);
						pinChangeSearchForm.setStartMinuteDisplay(
							startMinuteDisplay);

						response.setRenderParameter(
							Constants.TX_RENDER,
							Constants.PIN_CHANGE_SEARCH_RESULTS);
					}
					else {
						logger.debug(
							"pinChangeSearch - Could not get the search results");

						response.setRenderParameter(
							Constants.TX_RENDER,
							Constants.PIN_CHANGE_ERROR_PAGE);
					}
				}
				catch (Exception e) {
					logger.error(
						"pinChangeSearch - Exception retrieving the list. " +
							e.getMessage(),
						e);

					response.setRenderParameter(
						Constants.TX_RENDER, Constants.PIN_CHANGE_ERROR_PAGE);
				}
			}
			else {
				logger.error(
					"pinChangeSearch - Search header is null. Cannot complete request.");

				String[] startMinuteDisplay =
					transactionAppProperties.getTransProps(
					).getProperty(
						Constants.UI_START_MINS_PROPSKEY
					).split(
						Constants.COMMA
					);
				String[] startHourDisplay =
					transactionAppProperties.getTransProps(
					).getProperty(
						Constants.UI_START_HR_PROPSKEY
					).split(
						Constants.COMMA
					);
				String[] endHourDisplay =
					transactionAppProperties.getTransProps(
					).getProperty(
						Constants.UI_END_HR_PROPSKEY
					).split(
						Constants.COMMA
					);
				String[] endMinuteDisplay =
					transactionAppProperties.getTransProps(
					).getProperty(
						Constants.UI_END_MINS_PROPSKEY
					).split(
						Constants.COMMA
					);

				pinChangeSearchForm.setEndHourDisplay(endHourDisplay);
				pinChangeSearchForm.setEndMinuteDisplay(endMinuteDisplay);
				pinChangeSearchForm.setStartHourDisplay(startHourDisplay);
				pinChangeSearchForm.setStartMinuteDisplay(startMinuteDisplay);

				request.setAttribute(Constants.MORE_ORG, Boolean.TRUE);
				response.setRenderParameter(
					Constants.TX_RENDER, Constants.PIN_CHANGE_SEARCH_RENDER);
			}
		}
		else {
			logger.error(
				"pinChangeSearch - There are validation error messages");

			String[] startMinuteDisplay =
				transactionAppProperties.getTransProps(
				).getProperty(
					Constants.UI_START_MINS_PROPSKEY
				).split(
					Constants.COMMA
				);
			String[] startHourDisplay = transactionAppProperties.getTransProps(
			).getProperty(
				Constants.UI_START_HR_PROPSKEY
			).split(
				Constants.COMMA
			);
			String[] endHourDisplay = transactionAppProperties.getTransProps(
			).getProperty(
				Constants.UI_END_HR_PROPSKEY
			).split(
				Constants.COMMA
			);
			String[] endMinuteDisplay = transactionAppProperties.getTransProps(
			).getProperty(
				Constants.UI_END_MINS_PROPSKEY
			).split(
				Constants.COMMA
			);

			pinChangeSearchForm.setEndHourDisplay(endHourDisplay);
			pinChangeSearchForm.setEndMinuteDisplay(endMinuteDisplay);
			pinChangeSearchForm.setStartHourDisplay(startHourDisplay);
			pinChangeSearchForm.setStartMinuteDisplay(startMinuteDisplay);

			response.setRenderParameter(
				Constants.TX_RENDER, Constants.PIN_CHANGE_SEARCH_RENDER);
		}

		logger.debug("pinChangeSearch - end the PIN Change History Search");
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.PIN_CHANGE_SEARCH_RESULTS
	)
	public String pinChangeSearchResults(
		RenderRequest request, RenderResponse response) {

		logger.debug("pinChangeSearchResults - start ");

		if ((null != request.getAttribute(Constants.ACTION)) &&
			Constants._2FA_ACTION.equals(
				request.getAttribute(Constants.ACTION))) {

			logger.debug(
				"pinChangeSearchResults - request.getAttribute(Constants.ACTION): " +
					request.getAttribute(Constants.ACTION));
			logger.debug(
				"pinChangeSearchResults - Constants._2FA_ACTION: " +
					Constants._2FA_ACTION);
			logger.debug(
				"pinChangeSearchResults - forwarding the request to 2FA page");

			renderPinChangeOn2FAAction(request, response);
		}
		else {
			logger.debug(
				"pinChangeSearchResults - Inside the show non 2FA Page");
			renderPinChangeOnNon2FAAction(request, response);
		}

		logger.debug("pinChangeSearchResults - end ");

		return Constants.PIN_CHANGE_SEARCH_RESULTS_PAGE;
	}

	/**
	 * renderOn2FAAction call inside the main render method.
	 *
	 * @param response
	 * @param request
	 * @return void
	 */
	public void renderPinChangeOn2FAAction(
		RenderRequest request, RenderResponse response) {

		logger.debug(
			"[renderToTxDetailsPageAndBackToTxDetails] : 2FA PAGE RENDER ");

		if (null != request.getAttribute(Constants.PIN_CHANGE_LIST_REQ))
			request.setAttribute(Constants.FROM_FA, Boolean.TRUE);
		request.setAttribute(
			Constants.PIN_CHANGE_SEARCH_RESULTS_LIST,
			request.getAttribute(Constants.PIN_CHANGE_LIST_REQ));
		logger.debug(
			"[renderToTxDetailsPageAndBackToTxDetails] getting the request objects");
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		if (!StringUtils.isBlank(
				session.getAttribute(
					Constants.PIN_CHANGE_TRANSACTION_ID
				).toString()) &&
			!StringUtils.isBlank(
				session.getAttribute(
					Constants.PIN_CHANGE_BUSINESS_DATE
				).toString()) &&
			!StringUtils.isBlank(
				session.getAttribute(
					Constants.PIN_CHANGE_SOURCE
				).toString())) {

			logger.debug(
				"[renderToTxDetailsPageAndBackToTxDetails] setting the request in RENDER MAPPING attributes  TX ID" +
					session.getAttribute(
						Constants.PIN_CHANGE_TRANSACTION_ID
					).toString());
			request.setAttribute(
				Constants.PIN_CHANGE_TRANSACTION_ID,
				session.getAttribute(
					Constants.PIN_CHANGE_TRANSACTION_ID
				).toString());
			request.setAttribute(
				Constants.PIN_CHANGE_BUSINESS_DATE,
				session.getAttribute(
					Constants.PIN_CHANGE_BUSINESS_DATE
				).toString());
			request.setAttribute(
				Constants.PIN_CHANGE_SOURCE,
				session.getAttribute(
					Constants.PIN_CHANGE_SOURCE
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
	}

	/**
	 * renderOnNon2FAAction call inside the main render method.
	 *
	 * @param response
	 * @param request
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void renderPinChangeOnNon2FAAction(
		RenderRequest request, RenderResponse response) {

		logger.info(
			"[TransactionSearchController.showTransactionSearchResult] INSIDE THE TX MAIN SEARCH RESULT RENDER MAPPING  MAP IS  " +
				request.getParameterMap());
		boolean is2FaRole = setUser2FaRoles(request);
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		Map<String, String> sortOrderMap = null;

		if (null == session.getAttribute(
				Constants.PIN_CHANGE_SEARCH_RESULTS_LIST)) {

			logger.debug(
				"renderOnNon2FAAction - inside the pinSearchResults null check");

			if ((null != request.getAttribute(Constants.PIN_CHANGE_LIST_REQ)) &&
				(((List<TransactionList>)request.getAttribute(
					Constants.PIN_CHANGE_LIST_REQ)).size() > 0)) {

				List<TransactionList> pinChangeList =
					(List<TransactionList>)request.getAttribute(
						Constants.PIN_CHANGE_LIST_REQ);
				sortOrderMap = setDefaultSortOrderMap();
				perfLogger_.debug(
					"[TransactionSearchController] Sorting the Transction list at " +
						Calendar.getInstance(
						).getTime());
				pinChangeList = Sortable.sort(
					pinChangeList, null, sortOrderMap.get(Constants.DATE_TIME));
				perfLogger_.debug(
					"[TransactionSearchController] Sorting Finished for the Transction list at " +
						Calendar.getInstance(
						).getTime());
				String pageBy = request.getParameter(
					new ParamEncoder(
						"pinChangeResults"
					).encodeParameterName(
						TableTagParameters.PARAMETER_PAGE
					));

				logger.debug(
					"[TransactionSearchController.showTransactionSearchResult]   is  Page  is " +
						pageBy);
				session.setAttribute(
					Constants.PIN_CHANGE_SEARCH_RESULTS_LIST, pinChangeList);
				request.setAttribute(Constants.IS_FA, is2FaRole);
				logger.debug(
					"renderOnNon2FAAction - session.getAttribute(Constants.PIN_CHANGE_SORT_ORDER) before: " +
						session.getAttribute(Constants.PIN_CHANGE_SORT_ORDER));

				session.setAttribute(
					Constants.PIN_CHANGE_SORT_ORDER, sortOrderMap);

				logger.debug(
					"renderOnNon2FAAction - session.getAttribute(Constants.PIN_CHANGE_SORT_ORDER) after: " +
						session.getAttribute(Constants.PIN_CHANGE_SORT_ORDER));
			}
			else {
				if ((null == request.getAttribute(Constants.MORE_ORG)) &&
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
					"pinChangeResults"
				).encodeParameterName(
					TableTagParameters.PARAMETER_PAGE
				));

			if ("1".equals(pageBy) &&
				((null == session.getAttribute(
					Constants.PIN_CHANGE_CUR_PAGE)) ||
				 ((null != session.getAttribute(
					 Constants.PIN_CHANGE_CUR_PAGE)) &&
				  !pageBy.equals(
					  session.getAttribute(Constants.PIN_CHANGE_CUR_PAGE))))) {

				logger.debug("[renderOnNon2FAAction] pageBy is  " + pageBy);
				String sortBy = request.getParameter(
					new ParamEncoder(
						"pinChangeResults"
					).encodeParameterName(
						TableTagParameters.PARAMETER_SORT
					));
				logger.debug(
					"[renderOnNon2FAAction]  sortBy is   " + sortBy +
						"  Page session  is  " +
							session.getAttribute(
								Constants.PIN_CHANGE_CUR_PAGE));

				sortOrderMap = (HashMap<String, String>)session.getAttribute(
					Constants.PIN_CHANGE_SORT_ORDER);

				logger.debug(
					"renderOnNon2FAAction - sortOrderMap afterwards is: " +
						sortOrderMap);

				String orderBy = request.getParameter(
					new ParamEncoder(
						"pinChangeResults"
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
					"[renderOnNon2FAAction] Sorting the Transction list at " +
						Calendar.getInstance(
						).getTime());

				session.setAttribute(
					Constants.PIN_CHANGE_SEARCH_RESULTS_LIST,
					Sortable.sort(
						(List<TransactionList>)session.getAttribute(
							Constants.PIN_CHANGE_SEARCH_RESULTS_LIST),
						sortBy, sortOrderMap.get(sortBy)));
				perfLogger_.debug(
					"[renderOnNon2FAAction] Finished Sorting the Transction list at " +
						Calendar.getInstance(
						).getTime());
				request.setAttribute(Constants.IS_FA, is2FaRole);

				if (null != session.getAttribute(Constants.PIN_CHANGE_CUR_PAGE))
					session.setAttribute(
						Constants.PIN_CHANGE_CUR_PAGE,
						session.getAttribute(Constants.PIN_CHANGE_CUR_PAGE));
			}
			else {
				logger.debug(
					"[renderOnNon2FAAction] paging only Number  " + pageBy);
				request.setAttribute(Constants.IS_FA, is2FaRole);
				session.setAttribute(Constants.PIN_CHANGE_CUR_PAGE, pageBy);
			}
		}

		if (null != request.getAttribute(Constants.DATE_WARNING_MSG)) {
			logger.debug(
				"[renderOnNon2FAAction] INSIDE setting the msg for warning for 18 months old date range in Render ");
			request.setAttribute(Constants.DATE_WARNING_MSG, Boolean.TRUE);
		}

		if (null != request.getAttribute(Constants.MORE_DATA_MSG)) {
			logger.debug(
				"[renderOnNon2FAAction] INSIDE setting the msg for warning for more than 250 found but display only 250 in Render ");
			request.setAttribute(Constants.MORE_DATA_MSG, Boolean.TRUE);
		}
	}

	/**
	 * Transaction 2FA authentication process render
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.PIN_CHANGE_2FA_RENDER
	)
	@SuppressWarnings("unchecked")
	public String renderToTxDetailsPageAndBackToTxDetails(
		RenderResponse response, RenderRequest request) {

		logger.debug("renderToTxDetailsPageAndBackToTxDetails - start");

		if ((null != request.getAttribute(Constants.ACTION)) &&
			Constants._2FA_ACTION.equals(
				request.getAttribute(Constants.ACTION))) {

			logger.debug(
				"[renderToTxDetailsPageAndBackToTxDetails] : 2FA PAGE RENDER ");

			if (null != request.getAttribute(Constants.PIN_CHANGE_LIST_REQ))
				request.setAttribute(
					Constants.PIN_CHANGE_SEARCH_RESULTS_LIST,
					request.getAttribute(Constants.PIN_CHANGE_LIST_REQ));

			request.setAttribute(Constants.FROM_FA, Boolean.TRUE);

			logger.debug(
				"[renderToTxDetailsPageAndBackToTxDetails] getting the request objects");

			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpSession session = httpServletRequest.getSession();

			if (!StringUtils.isBlank(
					session.getAttribute(
						Constants.PIN_CHANGE_TRANSACTION_ID
					).toString()) &&
				!StringUtils.isBlank(
					session.getAttribute(
						Constants.PIN_CHANGE_BUSINESS_DATE
					).toString()) &&
				!StringUtils.isBlank(
					session.getAttribute(
						Constants.PIN_CHANGE_SOURCE
					).toString())) {

				logger.debug(
					"[renderToTxDetailsPageAndBackToTxDetails] setting the request in RENDER MAPPING attributes");

				request.setAttribute(
					Constants.PIN_CHANGE_TRANSACTION_ID,
					session.getAttribute(
						Constants.PIN_CHANGE_TRANSACTION_ID
					).toString());

				request.setAttribute(
					Constants.PIN_CHANGE_BUSINESS_DATE,
					session.getAttribute(
						Constants.PIN_CHANGE_BUSINESS_DATE
					).toString());

				request.setAttribute(
					Constants.PIN_CHANGE_SOURCE,
					session.getAttribute(
						Constants.PIN_CHANGE_SOURCE
					).toString());

				logger.debug(
					"[renderToTxDetailsPageAndBackToTxDetails]  %%%%%%%%%%%%%%%%%%% " +
						session.getAttribute(
							Constants.PIN_CHANGE_TRANSACTION_ID
						).toString());
			}

			//Sets the current row number of the detail.

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
		}
		else {
			logger.debug(
				"[TransactionSearchController.showTransactionSearchResult] INSIDE THE TX MAIN SEARCH RESULT RENDER MAPPING  MAP IS  " +
					request.getParameterMap());

			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpSession session = httpServletRequest.getSession();
			Map<String, String> sortOrderMap = null;
			boolean is2FaRole = setUser2FaRoles(request);

			if (null == session.getAttribute(
					Constants.PIN_CHANGE_SEARCH_RESULTS_LIST)) {

				if ((null != request.getAttribute(
						Constants.PIN_CHANGE_LIST_REQ)) &&
					(((List<TransactionList>)request.getAttribute(
						Constants.PIN_CHANGE_LIST_REQ)).size() > 0)) {

					List<TransactionList> pinChangeList =
						(List<TransactionList>)request.getAttribute(
							Constants.PIN_CHANGE_LIST_REQ);

					sortOrderMap = setDefaultSortOrderMap();
					perfLogger_.debug(
						"[TransactionSearchController.showTransactionSearchResult] Sorting First time the Transction list at " +
							Calendar.getInstance(
							).getTime());
					pinChangeList = Sortable.sort(
						pinChangeList, null,
						sortOrderMap.get(Constants.DATE_TIME));

					perfLogger_.debug(
						"[TransactionSearchController.showTransactionSearchResult] Finished Sorting First time  FINISHED the Transction list at " +
							Calendar.getInstance(
							).getTime());
					String pageBy = request.getParameter(
						new ParamEncoder(
							"pinChangeResults"
						).encodeParameterName(
							TableTagParameters.PARAMETER_PAGE
						));

					logger.debug(
						"[TransactionSearchController.showTransactionSearchResult]   is %%%%%%%%%%%%%%   Page  is $$$$$$$$$$$  " +
							pageBy);
					session.setAttribute(
						Constants.PIN_CHANGE_SEARCH_RESULTS_LIST,
						pinChangeList);
					request.setAttribute(Constants.IS_FA, is2FaRole);
					session.setAttribute(
						Constants.PIN_CHANGE_SORT_ORDER, sortOrderMap);
				}
				else {
					request.setAttribute(
						Constants.NO_DATA_FOUND_MSG, Constants.TX_NODATA_FOUND);
				}
			}
			else {
				String pageBy = request.getParameter(
					new ParamEncoder(
						"pinChangeResults"
					).encodeParameterName(
						TableTagParameters.PARAMETER_PAGE
					));

				if ("1".equals(pageBy) &&
					((null == session.getAttribute(
						Constants.PIN_CHANGE_CUR_PAGE)) ||
					 ((null != session.getAttribute(
						 Constants.PIN_CHANGE_CUR_PAGE)) &&
					  pageBy.equals(
						  session.getAttribute(
							  Constants.PIN_CHANGE_CUR_PAGE))))) {

					String sortBy = request.getParameter(
						new ParamEncoder(
							"pinChangeResults"
						).encodeParameterName(
							TableTagParameters.PARAMETER_SORT
						));
					logger.debug(
						"[TransactionSearchController.showTransactionSearchResult]  sortBy is   " +
							sortBy + "  Page session is " +
								session.getAttribute(
									Constants.PIN_CHANGE_CUR_PAGE));

					sortOrderMap =
						(HashMap<String, String>)session.getAttribute(
							Constants.PIN_CHANGE_SORT_ORDER);
					String orderBy = request.getParameter(
						new ParamEncoder(
							"pinChangeResults"
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
						Constants.PIN_CHANGE_SEARCH_RESULTS_LIST,
						Sortable.sort(
							(List<TransactionList>)session.getAttribute(
								Constants.PIN_CHANGE_SEARCH_RESULTS_LIST),
							sortBy, sortOrderMap.get(sortBy)));
					perfLogger_.debug(
						"[TransactionSearchController.showTransactionSearchResult] Sorting FINISHED the Transction list at " +
							Calendar.getInstance(
							).getTime());
					request.setAttribute(Constants.IS_FA, is2FaRole);

					if (null != session.getAttribute(
							Constants.PIN_CHANGE_CUR_PAGE))
						session.setAttribute(
							Constants.PIN_CHANGE_CUR_PAGE,
							session.getAttribute(
								Constants.PIN_CHANGE_CUR_PAGE));
				}
				else {
					logger.debug(
						"[TransactionSearchController.showTransactionSearchResult] pageing only Number  " +
							pageBy);
					request.setAttribute(Constants.IS_FA, is2FaRole);
					session.setAttribute(Constants.PIN_CHANGE_CUR_PAGE, pageBy);
				}
			}

			if (null != request.getAttribute(Constants.DATE_WARNING_MSG)) {
				request.setAttribute(Constants.DATE_WARNING_MSG, Boolean.TRUE);
			}

			if (null != request.getAttribute(Constants.MORE_DATA_MSG)) {
				request.setAttribute(Constants.MORE_DATA_MSG, Boolean.TRUE);
			}
		}

		return Constants.PIN_CHANGE_SEARCH_RESULTS_PAGE;
	}

	/**
	 * Default value for sort order map for Transaction list search
	 *
	 * @param void
	 * @return void
	 */
	public Map<String, String> setDefaultSortOrderMap() {
		Map<String, String> sortOrderMap = new HashMap<>();

		sortOrderMap.put(Constants.CARD_ACCEPTOR_ID, Constants.NATURAL);
		sortOrderMap.put(Constants.DATE_TIME, Constants.REVERSE);
		sortOrderMap.put(Constants.DESCRIPTION, Constants.NATURAL);
		sortOrderMap.put(Constants.PAN, Constants.NATURAL);
		sortOrderMap.put(Constants.RESPONSE_CODE, Constants.NATURAL);
		sortOrderMap.put(Constants.SYSTEM_TRACE, Constants.NATURAL);
		sortOrderMap.put(Constants.TERMINAL_ID, Constants.NATURAL);

		return sortOrderMap;
	}

	public void setOrgIdOverride(String orgIdOverride) {
		this.orgIdOverride = orgIdOverride;
	}

	/**
	 * Check and set the user 2 FA roles detail in object.
	 *
	 * @param request
	 * @return boolean
	 */
	public boolean setUser2FaRoles(PortletRequest request) {
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
				logger.info(
					"[setUser2FaRoles]  All Roles count is " +
						allUserRoles.size());

				for (Role role : allUserRoles) {
					if (Constants.TX_ROLE_FA.equalsIgnoreCase(role.getName())) {
						is2faAccessible = true;
						logger.info(
							"[setUser2FaRoles] setting role as true for Role  " +
								role.getName());
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return is2faAccessible;
	}

	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.PIN_CHANGE_ERROR_PAGE
	)
	public String showErrorPage(
		RenderRequest request, RenderResponse response) {

		logger.debug("showErrorPage - Showing the error page now.");

		return Constants.TX_ERROR;
	}

	/**
	 * Shows the search results of the Transaction
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.PIN_CHANGE_DETAIL_RESOURCE
	)
	public String showPinChangeDetailsPageRender(
		RenderResponse response, RenderRequest request) {

		logger.debug("showPinChangeDetailsPageRender - DETAILS PAGE ACTION ");

		boolean is2FaRole = setUser2FaRoles(request);

		logger.debug(
			"showPinChangeDetailsPageRender - is2FaRole is: " + is2FaRole);

		TransactionDetail transactionDetail = null;

		if (!StringUtils.isBlank(
				request.getParameter(Constants.PIN_CHANGE_TRANSACTION_ID)) &&
			!StringUtils.isBlank(
				request.getParameter(Constants.PIN_CHANGE_BUSINESS_DATE)) &&
			!StringUtils.isBlank(
				request.getParameter(Constants.PIN_CHANGE_SOURCE))) {

			request.setAttribute(
				Constants.PIN_CHANGE_TRANSACTION_ID,
				request.getParameter(Constants.PIN_CHANGE_TRANSACTION_ID));

			request.setAttribute(
				Constants.PIN_CHANGE_BUSINESS_DATE,
				request.getParameter(Constants.PIN_CHANGE_BUSINESS_DATE));

			request.setAttribute(
				Constants.PIN_CHANGE_SOURCE,
				request.getParameter(Constants.PIN_CHANGE_SOURCE));

			request.setAttribute(
				Constants.PIN_CHANGE_DETAIL, transactionDetail);

			logger.debug(
				"showPinChangeDetailsPageRender - pin change transaction id is: " +
					request.getAttribute(Constants.PIN_CHANGE_TRANSACTION_ID));
			logger.debug(
				"showPinChangeDetailsPageRender - pin change business date is: " +
					request.getAttribute(Constants.PIN_CHANGE_BUSINESS_DATE));
			logger.debug(
				"showPinChangeDetailsPageRender - pin change source is: " +
					request.getAttribute(Constants.PIN_CHANGE_SOURCE));

			request.setAttribute(Constants.FROM_FA, Boolean.FALSE);
			request.setAttribute(Constants.IS_FA, is2FaRole);

			logger.debug(
				"showPinChangeDetailsPageRender - the is2FaRole from request is: " +
					request.getAttribute(Constants.IS_FA));
		}

		return Constants.PIN_CHANGE_DETAIL_PAGE;
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(
		params = Constants.TX_ERROR + Constants.EQUALS + Constants.PIN_CHANGE_SEARCH_RENDER
	)
	public String showSearchPage(
		RenderRequest request, RenderResponse response) {

		return Constants.PIN_CHANGE_SEARCH_PAGE;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	private SearchHeader getSearchHeader(PortletRequest request) {
		logger.debug("getSearchHeader - Start creating the searchHeader");

		SearchHeader header = new SearchHeader();
		User user = null;

		try {
			user = PortalUtil.getUser(request);

			if (user != null) {
				Organization org = getUserOrg(user);

				if (org != null) {
					header.setOrigin(AuditOrigin.PORTAL_ORIGIN);
					header.setUserId(user.getScreenName());

					String orgShortName = getOrgShortName(
						Long.valueOf(
							org.getOrganizationId()
						).toString(),
						request);

					header.setUserOrgId(orgShortName);
					header.setUserOrgName(org.getName());
					header.setPageNumber(1);
					header.setPageSize(Constants.PAGE_SIZE);
				}
				else {
					return null;
				}
			}
			else {
				return null;
			}
		}
		catch (PortalException e) {
			e.printStackTrace();
		}
		catch (SystemException e) {
			e.printStackTrace();
		}

		logger.debug("getSearchHeader - End creating the searchHeader");

		return header;
	}

	/**
	 *
	 * @param user
	 * @return
	 */
	private Organization getUserOrg(User user) {
		Organization userOrg = null;

		try {
			List<Organization> orgs = user.getOrganizations();

			if (orgs != null) {
				if (orgs.size() == 1) {
					userOrg = orgs.get(0);
				}
				else {
					logger.error(
						"getUserOrg - User " + user.getScreenName() + " has " +
							orgs.size() + " organisations assigned to them.");
				}
			}
		}
		catch (PortalException pe) {
			logger.error(
				"getUserOrg - PortalException thrown getting the user org: " +
					pe.getMessage(),
				pe);
		}
		catch (SystemException se) {
			logger.error(
				"getUserOrg - SystemException thrown getting the user org: " +
					se.getMessage(),
				se);
		}

		return userOrg;
	}

	private static final Auditor audit = Auditor.getInstance();

	private static Logger logger = Logger.getLogger(
		"pinChangeControllerLogging");

	/**
	 * Logger object for performance logging
	 */
	private static Logger perfLogger_ = Logger.getLogger("perfLogging");

	private String orgIdOverride = null;

	@Autowired
	@Qualifier(Constants.PIN_CHANGE_VALIDATOR)
	private PinChangeHistorySearchValidator pinChangeHistorySearchValidator;

	@Autowired
	@Qualifier("transactionAppProperties")
	private TransactionAppProperties transactionAppProperties;

	@Autowired
	@Qualifier(Constants.TRANSACTION_SERVICE)
	private TransactionSearchService transactionSearchService;

}