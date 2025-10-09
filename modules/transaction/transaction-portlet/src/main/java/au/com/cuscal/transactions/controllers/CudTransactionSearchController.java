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
import au.com.cuscal.transactions.commons.Utility;
import au.com.cuscal.transactions.domain.TransactionDetail;
import au.com.cuscal.transactions.domain.TransactionList;
import au.com.cuscal.transactions.forms.TransactionForm;
import au.com.cuscal.transactions.services.CudTransactionSearchService;
import au.com.cuscal.transactions.validator.CudTransactionSearchValidator;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.EventMapping;
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
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Controller that returns that CUD Transaction Search View
 *
 * @author Rajni Bharara
 *
 */
@Controller(Constants.CUD_TRANSACTION_CONTROLLER)
@RequestMapping("VIEW")
@SessionAttributes(types = TransactionForm.class)
public class CudTransactionSearchController {

	/**
	 * Audit while printing the view of the Transaction detail page
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 * @throws Exception
	 */
	@ResourceMapping(Constants.CUD_PRINT)
	public void auditOnPrintTxDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.info("auditOnPrintTxDetails - start ");
		audit.success(
			response, request, AuditOrigin.PORTAL_ORIGIN,
			AuditCategories.CUD_DETAIL,
			"The user is printing the transaction details ");

		logger.info("auditOnPrintTxDetails - end ");
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
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.CUD_CLEAR
	)
	public String clearTransactionSearchResult(
		RenderResponse response, RenderRequest request,
		@ModelAttribute TransactionForm transactionForm) {

		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();
		transactionForm.resetForm();
		session.removeAttribute(Constants.CUD_LIST);

		return Constants.CUD_TX_SEARCH_PAGE;
	}

	/**
	 * create the export file data.
	 *
	 * @param List<TransactionList>
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook createExcelFile(List<TransactionList> lists) {
		logger.info("createExcelFile - start ");
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet("CUD Transaction Report");

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
			"Terminal Location"
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
		header.createCell(
			7
		).setCellValue(
			"Amount"
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
				list.getTerminalLocation()
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
					list.getCudTransactionDate(), Constants.DATE_FORMAT) + " " +
						list.getCudTranscationTime()
			);
			row.createCell(
				6
			).setCellValue(
				list.getDescription()
			);
			row.createCell(
				7
			).setCellValue(
				Utility.formatNumberToString(
					Utility.Round(list.getAmount(), 2), "0.00") + " " +
						list.getCurrencyCodeAcq()
			);
		}

		logger.info("createExcelFile - end ");

		return workbook;
	}

	/**
	 * create the export file for transaction list and send to portlet
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 */
	@ResourceMapping(Constants.CUD_EXPORT_LIST)
	@SuppressWarnings("unchecked")
	public void exportDataXls(
		ResourceRequest request, ResourceResponse response) {

		logger.info("exportDataXls - start ");
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		List<TransactionList> lists = null;
		HSSFWorkbook workbook = null;

		if (null != session.getAttribute(Constants.CUD_LIST)) {
			lists = (List<TransactionList>)session.getAttribute(
				Constants.CUD_LIST);
			workbook = createExcelFile(lists);

			try {
				audit.success(
					response, request, AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.CUD_SEARCH,
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
					"Exception while Exporting Transactions List in xls format, exception message is  " +
						e.getMessage());

				try {
					audit.fail(
						response, request, AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CUD_SEARCH,
						" Export Transactions, the Exception: " +
							e.getMessage());
				}
				catch (Exception ae) {
					logger.error(
						"Unable to write audit trail for Export Transactions List failure: " +
							ae.getMessage() + ", original error: " +
								e.getMessage());
				}
			}
		}

		logger.info("exportDataXls - end ");
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
		logger.info(
			"[CudTransactionSearchController.findTransactionListForQuickSearch]  in event mapping event is  " +
				event);
		TransactionForm transactionForm = (TransactionForm)event.getValue();

		map.put("TransactionForm", transactionForm);
		logger.info(
			"[CudTransactionSearchController.findTransactionListForQuickSearch]  getPanBin  value is  " +
				transactionForm.getPanBin());
		eventResponse.setRenderParameter(
			Constants.TX_RENDER, Constants.CUD_SEARCH_RESULT);
		sessionStatus.setComplete();
	}

	/**
	 * Action mapping for the search results of the Transaction
	 *
	 * @param TransactionForm
	 * @param BindingResult
	 * @param ActionResponse
	 * @param SessionStatus
	 * @param ActionRequest
	 * @return void
	 */
	@ActionMapping(
		params = Constants.TX_ACTION + Constants.EQUALS + Constants.CUD_LIST
	)
	public void findTransactionSearchList(
		@ModelAttribute TransactionForm transactionForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.info("findTransactionSearchList - start");
		logger.debug(
			"Inside the Action for get list display transactionForm is  " +
				transactionForm);
		perfLogger_.debug("findTransactionSearchList - start");
		boolean isValidation = false;
		boolean isNotMoreOrg = false;
		boolean isDataNotNull = false;
		boolean isUserNull = false;

		if (!StringUtils.isBlank(transactionForm.getPanBin())) {
			String panOrBin = Utility.removeAllSpacesFromPanorBin(
				transactionForm.getPanBin(
				).trim());

			transactionForm.setPanBin(panOrBin);
		}

		cudTransactionSearchValidator.validate(transactionForm, bindingResult);
		perfLogger_.debug("findTransactionSearchList - search form validated");

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession(true);

		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				SearchHeader searchHeader = getSearchHeaderData(request);
				isUserNull = true;

				if ((null != searchHeader) &&
					(null != searchHeader.getUserOrgId())) {

					isNotMoreOrg = true;

					if (!bindingResult.hasErrors()) {
						isValidation = true;
						perfLogger_.debug(
							"findTransactionSearchList - calling web service");

						if (Utility.isDateDifferenceIsmoreThanXDays(
								transactionForm)) {

							logger.debug(
								"[CudTransactionSearchController.showTransactionSearchResult] dates are past 30 days ");
							request.setAttribute(
								Constants.DATE_WARNING_MSG, Boolean.TRUE);
						}

						List<Object> listSearch =
							cudTransactionSearchService.
								getTransactionCudListOnSearch(
									transactionForm, searchHeader,
									PortletContext.newContext(
										response, request));
						perfLogger_.debug(
							"findTransactionSearchList - web service returned");
						@SuppressWarnings("unchecked")
						List<TransactionList> listForms =
							(List<TransactionList>)listSearch.get(0);

						if ((null != listForms) && (0 < listForms.size())) {
							logger.debug(
								"[CudTransactionSearchController.showTransactionSearchResult]  setting the response render listForms size is  " +
									listForms.size());
							isDataNotNull = true;
							request.setAttribute(
								Constants.CUD_LIST_REQ, listForms);

							if (listForms.get(
									0
								).isMoreRecAvail()) {

								logger.debug(
									"[CudTransactionSearchController.showTransactionSearchResult] dates range have more than 250 records for transaction search ");
								request.setAttribute(
									Constants.MORE_DATA_MSG, Boolean.TRUE);
							}
						}

						response.setRenderParameter(
							Constants.TX_RENDER, Constants.CUD_SEARCH_RESULT);
						request.setAttribute(Constants.FROM_FA, Boolean.FALSE);
					}
				}
			}

			if (!isUserNull) {
				logger.warn(
					"User tried to search transactions after logging out or their session timed out.");
				request.setAttribute(Constants.CUD_LIST_REQ, null);
				response.setRenderParameter(
					Constants.TX_RENDER, Constants.TX_ERROR_PAGE);
			}
			else if (!isNotMoreOrg) {
				logger.warn(
					"User tried to search transactions have no or more than one organisations.");
				request.setAttribute(Constants.MORE_ORG, Boolean.TRUE);
				request.setAttribute(Constants.CUD_LIST_REQ, null);
				response.setRenderParameter(
					Constants.TX_RENDER, Constants.CUD_SEARCH_RESULT);
			}
			else if (!isValidation) {
				logger.warn(
					"User tried to search transactions have validation issues.");
				request.setAttribute(Constants.VALIDATION, Constants.FALSE);
				request.setAttribute(Constants.CUD_LIST_REQ, null);
				response.setRenderParameter(
					Constants.TX_RENDER, Constants.CUD_SEARCH_RESULT);
			}
			else {
				if (!isDataNotNull) {
					logger.warn(
						"User tried to search transactions have null object from web service call.");
					response.setRenderParameter(
						Constants.TX_RENDER, Constants.CUD_SEARCH_RESULT);
					request.setAttribute(Constants.CUD_LIST_REQ, null);
				}
			}

			session.setAttribute(Constants.CUD_LIST, null);
			session.setAttribute(Constants.CUD_SORT_ORDER, null);
			sessionStatus.setComplete();
		}
		catch (Exception e) {
			logger.error(
				"[CudTransactionSearchController.showTransactionSearchResult] EXCEPTION CAME ERROR MSG e.getCause()  is " +
					e.getCause());
			response.setRenderParameter(
				Constants.TX_RENDER, Constants.TX_ERROR_PAGE);
		}

		logger.info("findTransactionSearchList - end");
		perfLogger_.debug("findTransactionSearchList - end");
	}

	/**
	 * Shows the search results of the Transaction
	 *
	 * @param TransactionForm
	 * @param ActionResponse
	 * @param SessionStatus
	 * @param ActionRequest
	 * @return string
	 */
	@ActionMapping(
		params = Constants.TX_ACTION + Constants.EQUALS + Constants.CUD_2FA
	)
	public void forwardTo2FAPageAndBackToTxDetails(
		@ModelAttribute TransactionForm transactionForm,
		ActionResponse response, SessionStatus sessionStatus,
		ActionRequest request) {

		logger.info("forwardTo2FAPageAndBackToTxDetails - start ");
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		String steppedUp = "";
		logger.debug(
			"The session attribute is    " +
				session.getAttribute(Constants._USER_STEPPEDUP));

		if ((null != session.getAttribute(Constants.USER_CANCEL)) &&
			Constants.CANCEL.equals(
				session.getAttribute(Constants.USER_CANCEL))) {

			session.setAttribute(Constants.USER_CANCEL, null);

			if (null != session.getAttribute(Constants.CUD_LIST)) {
				@SuppressWarnings("unchecked")
				List<TransactionList> listForms =
					(List<TransactionList>)session.getAttribute(
						Constants.CUD_LIST);

				request.setAttribute(Constants.CUD_LIST_REQ, listForms);
			}

			request.setAttribute(Constants.CUD_IS_FA, setUser2FaRoles(request));
			session.setAttribute(Constants._RETURN_URL, null);
			steppedUp = (String)session.getAttribute(Constants._USER_STEPPEDUP);
			logger.debug("steppedup:   value is  " + steppedUp);
			request.setAttribute(Constants.ACTION, Constants._2FA_ACTION);
			response.setRenderParameter(
				Constants.TX_RENDER, Constants.CUD_2FA_RENDER);
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
					"In else part after the 2fa  " +
						transactionForm.getPanBin());

				if (null != session.getAttribute(Constants.CUD_LIST)) {
					@SuppressWarnings("unchecked")
					List<TransactionList> listForms =
						(List<TransactionList>)session.getAttribute(
							Constants.CUD_LIST);

					request.setAttribute(Constants.CUD_LIST_REQ, listForms);
				}

				steppedUp = (String)session.getAttribute(
					Constants._USER_STEPPEDUP);
				logger.debug("steppedup:   value is  " + steppedUp);
				request.setAttribute(Constants.ACTION, Constants._2FA_ACTION);
				response.setRenderParameter(
					Constants.TX_RENDER, Constants.CUD_2FA_RENDER);
			}
		}

		logger.info("forwardTo2FAPageAndBackToTxDetails - end ");
	}

	/**
	 * Creates the Transaction Form based
	 *
	 * @return TransactionForm
	 */
	@ModelAttribute(Constants.TRANSACTION_FORM)
	public TransactionForm getCommandObject() {
		TransactionForm transactionForm =
			cudTransactionSearchService.createTransactionFormObject();

		return transactionForm;
	}

	/**
	 * Next page for the view of the Transaction detail page
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 * @throws Exception
	 */
	@ResourceMapping(Constants.CUD_NEXT)
	@SuppressWarnings("unchecked")
	public void getNextTxDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.info("getNextTxDetails - start ");
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		List<TransactionList> lists = null;
		TransactionDetail transactionDetail = new TransactionDetail();

		if (!StringUtils.isBlank(
				session.getAttribute(
					Constants.CUD_CURR_ROW_NUM
				).toString()) &&
			!Constants.NULL.equalsIgnoreCase(
				session.getAttribute(
					Constants.CUD_CURR_ROW_NUM
				).toString())) {

			int curRowNum = Integer.valueOf(
				session.getAttribute(
					Constants.CUD_CURR_ROW_NUM
				).toString());

			if (null != session.getAttribute(Constants.CUD_LIST)) {
				lists = (List<TransactionList>)session.getAttribute(
					Constants.CUD_LIST);

				if (curRowNum != lists.size()) {
					curRowNum = curRowNum + 1;

					TransactionList transactionList = lists.get(curRowNum - 1);

					transactionDetail.setTransactionId(
						transactionList.getTransactionId());
					transactionDetail.setAdPank(transactionList.getAdPank());
					transactionDetail.setEtlProcessEndDate(
						transactionList.getEtlProcessEndDate());
					transactionDetail.setAdTransactionDetailK(
						transactionList.getAdTransactionDetailK());
					transactionDetail.setNextRowNum(curRowNum + 1);

					session.setAttribute(
						Constants.CUD_AD_PANK, transactionDetail.getAdPank());
					session.setAttribute(
						Constants.CUD_ETL_DATE,
						transactionDetail.getEtlProcessEndDate());
					session.setAttribute(
						Constants.CUD_TX_ID,
						transactionDetail.getTransactionId());
					session.setAttribute(
						Constants.CUD_TXN_DETAIL_K,
						transactionDetail.getAdTransactionDetailK());

					session.setAttribute(Constants.CUD_CURR_ROW_NUM, curRowNum);

					if ((null != session.getAttribute(
							Constants._USER_STEPPEDUP)) &&
						(Constants.FALSE.equals(
							session.getAttribute(Constants._USER_STEPPEDUP)) ||
						 Constants.ERROR.equals(
							 session.getAttribute(
								 Constants._USER_STEPPEDUP)))) {

						logger.debug(
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP)    is false " +
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
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP)    is true  " +
								session.getAttribute(
									Constants._USER_STEPPEDUP));
						transactionDetail.setUserStepUp(true);
					}
					else {
						logger.debug(
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP)  final else false");
						transactionDetail.setUserStepUp(false);
					}

					try {
						SearchHeader searchHeader = getSearchHeaderData(
							request);

						if ((null != searchHeader) &&
							(searchHeader.getUserOrgId() != null)) {

							transactionDetail =
								cudTransactionSearchService.
									getTransactionCudDetailByTxIdBusDateAndSrc(
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
							"[CudTransactionSearchController.getTransactionDetails] EXCEPTION CAME ERROR MSG e.getCause()  is " +
								e.getCause());
					}
				}
			}
		}

		logger.info("getNextTxDetails - end ");
	}

	public String getOrganisationIdOverride() {
		return organisationIdOverride;
	}

	/**
	 * Get the org short name
	 *
	 * @param String
	 * @param PortletRequest
	 * @return String
	 */
	public String getOrgShortName(
		String liferayOrgId, PortletRequest portletRequest) {

		logger.debug("getOrgShortName - start, liferayOrgId=" + liferayOrgId);

		if (StringUtils.isNotEmpty(organisationIdOverride)) {
			logger.debug(
				"getOrgShortName - end, organisationIdOverride has been set.  Using " +
					organisationIdOverride);

			return organisationIdOverride;
		}

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		HttpSession session = httpServletRequest.getSession();

		String orgShortName = (String)session.getAttribute(
			Constants.CUD_ORG_SHORT_NAME);

		if (orgShortName == null) {
			orgShortName = cudTransactionSearchService.getOrgShortName(
				liferayOrgId);

			if (StringUtils.isNotBlank(orgShortName)) {
				session.setAttribute(
					Constants.CUD_ORG_SHORT_NAME, orgShortName);
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
	@ResourceMapping(Constants.CUD_PREV)
	@SuppressWarnings("unchecked")
	public void getPrevTxDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.info("getPrevTxDetails - start ");
		TransactionDetail transactionDetail = new TransactionDetail();
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		List<TransactionList> lists = null;

		if (!StringUtils.isBlank(
				session.getAttribute(
					Constants.CUD_CURR_ROW_NUM
				).toString()) &&
			!Constants.NULL.equalsIgnoreCase(
				session.getAttribute(
					Constants.CUD_CURR_ROW_NUM
				).toString())) {

			int curRowNum = Integer.valueOf(
				session.getAttribute(
					Constants.CUD_CURR_ROW_NUM
				).toString());

			if (curRowNum != 1) {
				curRowNum = curRowNum - 1;

				if (null != session.getAttribute(Constants.CUD_LIST)) {
					lists = (List<TransactionList>)session.getAttribute(
						Constants.CUD_LIST);
					TransactionList transactionList = lists.get(curRowNum - 1);

					logger.debug(
						"[getPrevTxDetails]   getEtlProcessEndDate  " +
							transactionList.getEtlProcessEndDate());
					transactionDetail.setTransactionId(
						transactionList.getTransactionId());
					transactionDetail.setAdPank(transactionList.getAdPank());
					transactionDetail.setEtlProcessEndDate(
						transactionList.getEtlProcessEndDate());
					transactionDetail.setAdTransactionDetailK(
						transactionList.getAdTransactionDetailK());
					transactionDetail.setNextRowNum(curRowNum + 1);

					logger.debug(
						"[getPrevTxDetails] setting the session valuess attributes ");
					session.setAttribute(
						Constants.CUD_TX_ID,
						transactionDetail.getTransactionId());
					session.setAttribute(
						Constants.CUD_ETL_DATE,
						transactionDetail.getEtlProcessEndDate());
					session.setAttribute(
						Constants.CUD_AD_PANK, transactionDetail.getAdPank());
					session.setAttribute(
						Constants.CUD_TXN_DETAIL_K,
						transactionDetail.getAdTransactionDetailK());

					session.setAttribute(Constants.CUD_CURR_ROW_NUM, curRowNum);

					if ((null != session.getAttribute(
							Constants._USER_STEPPEDUP)) &&
						(Constants.FALSE.equals(
							session.getAttribute(Constants._USER_STEPPEDUP)) ||
						 Constants.ERROR.equals(
							 session.getAttribute(
								 Constants._USER_STEPPEDUP)))) {

						logger.debug(
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP)    is false " +
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
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP)    is true  " +
								session.getAttribute(
									Constants._USER_STEPPEDUP));
						transactionDetail.setUserStepUp(true);
					}
					else {
						logger.debug(
							"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP)  final else false");
						transactionDetail.setUserStepUp(false);
					}

					try {
						SearchHeader searchHeader = getSearchHeaderData(
							request);

						if ((null != searchHeader) &&
							(searchHeader.getUserOrgId() != null)) {

							transactionDetail =
								cudTransactionSearchService.
									getTransactionCudDetailByTxIdBusDateAndSrc(
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
							"[CudTransactionSearchController.getTransactionDetails] EXCEPTION CAME ERROR MSG e.getCause()  is " +
								e.getCause());
					}
				}
			}
		}

		logger.info("getPrevTxDetails - end ");
	}

	/**
	 * create the searchHeader object for web service.
	 *
	 * @param PortletRequest
	 * @return SearchHeader
	 */
	public SearchHeader getSearchHeaderData(PortletRequest request) {
		logger.info("getSearchHeaderData - start ");
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
			logger.error("Error setting search header: " + e.getMessage(), e);
		}

		logger.info("getSearchHeaderData - end ");

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
	@ResourceMapping(Constants.CUD_DETAIL)
	public void getTransactionDetails(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.info("getTransactionDetails - start ");
		boolean is2faRole = setUser2FaRoles(request);
		logger.debug(
			"[getTransactionDetails] inside the @ResourceMapping Role is  ");
		TransactionDetail transactionDetail = new TransactionDetail();
		request.setAttribute(Constants.FROM_FA, Boolean.FALSE);
		request.setAttribute(Constants.CUD_IS_FA, is2faRole);
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		if (!StringUtils.isBlank(request.getParameter(Constants.CUD_TX_ID)) &&
			!StringUtils.isBlank(
				request.getParameter(Constants.CUD_ETL_DATE)) &&
			!StringUtils.isBlank(request.getParameter(Constants.CUD_AD_PANK)) &&
			!StringUtils.isBlank(
				request.getParameter(Constants.CUD_TXN_DETAIL_K))) {

			if (!StringUtils.isBlank(
					request.getParameter(Constants.CUD_CURR_ROW_NUM)) &&
				!Constants.NULL.equalsIgnoreCase(
					request.getParameter(Constants.CUD_CURR_ROW_NUM))) {

				session.setAttribute(
					Constants.CUD_CURR_ROW_NUM,
					request.getParameter(Constants.CUD_CURR_ROW_NUM));
			}

			logger.debug("[showTxDetailsPage] setting the request attributes ");
			session.setAttribute(
				Constants.CUD_TX_ID, request.getParameter(Constants.CUD_TX_ID));
			session.setAttribute(
				Constants.CUD_ETL_DATE,
				request.getParameter(Constants.CUD_ETL_DATE));
			session.setAttribute(
				Constants.CUD_AD_PANK,
				request.getParameter(Constants.CUD_AD_PANK));
			session.setAttribute(
				Constants.CUD_TXN_DETAIL_K,
				request.getParameter(Constants.CUD_TXN_DETAIL_K));

			transactionDetail.setEtlProcessEndDate(
				request.getParameter(
					Constants.CUD_ETL_DATE
				).toString());
			transactionDetail.setTransactionId(
				request.getParameter(
					Constants.CUD_TX_ID
				).toString());
			transactionDetail.setAdPank(
				request.getParameter(
					Constants.CUD_AD_PANK
				).toString());
			transactionDetail.setAdTransactionDetailK(
				request.getParameter(
					Constants.CUD_TXN_DETAIL_K
				).toString());
		}

		if ((null != session.getAttribute(Constants._USER_STEPPEDUP)) &&
			(Constants.FALSE.equals(
				session.getAttribute(Constants._USER_STEPPEDUP)) ||
			 Constants.ERROR.equals(
				 session.getAttribute(Constants._USER_STEPPEDUP)))) {

			logger.debug(
				"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP)    is false " +
					session.getAttribute(Constants._USER_STEPPEDUP));
			transactionDetail.setUserStepUp(false);
		}
		else if ((null != session.getAttribute(Constants._USER_STEPPEDUP)) &&
				 Constants.TRUE.equals(
					 session.getAttribute(Constants._USER_STEPPEDUP))) {

			logger.debug(
				"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP)    is true  " +
					session.getAttribute(Constants._USER_STEPPEDUP));
			transactionDetail.setUserStepUp(true);
		}
		else {
			logger.debug(
				"[showTxDetailsPage] session.getAttribute(_USER_STEPPEDUP)  final else false");
			transactionDetail.setUserStepUp(false);
		}

		try {
			SearchHeader searchHeader = getSearchHeaderData(request);

			if ((null != searchHeader) &&
				(searchHeader.getUserOrgId() != null)) {

				transactionDetail =
					cudTransactionSearchService.
						getTransactionCudDetailByTxIdBusDateAndSrc(
							transactionDetail, searchHeader,
							PortletContext.newContext(response, request));

				// map to json object

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
				"[CudTransactionSearchController.getTransactionDetails] EXCEPTION CAME ERROR MSG e.getCause() is " +
					e.getCause(),
				e);
		}

		logger.info("getTransactionDetails - end ");
	}

	/**
	 * renderOn2FAAction call inside the main render method.
	 *
	 * @param RenderRequest
	 * @param RenderResponse
	 * @return void
	 */
	public void renderOn2FAAction(
		RenderRequest request, RenderResponse response) {

		logger.info("renderOn2FAAction - start ");

		if (null != request.getAttribute(Constants.CUD_LIST_REQ))
			request.setAttribute(
				Constants.CUD_LIST,
				request.getAttribute(Constants.CUD_LIST_REQ));
		request.setAttribute(Constants.FROM_FA, Boolean.TRUE);
		logger.debug(
			"[renderToTxDetailsPageAndBackToTxDetails] getting the request objects");
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		if (!StringUtils.isBlank(
				session.getAttribute(
					Constants.CUD_TX_ID
				).toString()) &&
			!StringUtils.isBlank(
				session.getAttribute(
					Constants.CUD_ETL_DATE
				).toString()) &&
			!StringUtils.isBlank(
				session.getAttribute(
					Constants.CUD_AD_PANK
				).toString()) &&
			!StringUtils.isBlank(
				session.getAttribute(
					Constants.CUD_TXN_DETAIL_K
				).toString())) {

			logger.debug(
				" setting the request in RENDER MAPPING attributes  Cud ID" +
					session.getAttribute(
						Constants.CUD_TX_ID
					).toString());
			request.setAttribute(
				Constants.CUD_TX_ID,
				session.getAttribute(
					Constants.CUD_TX_ID
				).toString());
			request.setAttribute(
				Constants.CUD_ETL_DATE,
				session.getAttribute(
					Constants.CUD_ETL_DATE
				).toString());
			request.setAttribute(
				Constants.CUD_AD_PANK,
				session.getAttribute(
					Constants.CUD_AD_PANK
				).toString());
			request.setAttribute(
				Constants.CUD_TXN_DETAIL_K,
				session.getAttribute(
					Constants.CUD_TXN_DETAIL_K
				).toString());
		}

		if (!StringUtils.isBlank(
				session.getAttribute(
					Constants.CUD_CURR_ROW_NUM
				).toString())) {

			request.setAttribute(
				Constants.CUD_CURR_ROW_NUM,
				session.getAttribute(
					Constants.CUD_CURR_ROW_NUM
				).toString());
		}

		logger.info("renderOn2FAAction - end ");
	}

	/**
	 * renderOnNon2FAAction call inside the main render method.
	 *
	 * @param RenderRequest
	 * @param RenderResponse
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void renderOnNon2FAAction(
		RenderRequest request, RenderResponse response) {

		logger.info("renderOnNon2FAAction - start ");
		boolean is2FaRole = setUser2FaRoles(request);
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		Map<String, String> sortOrderMap = null;

		if (null == session.getAttribute(Constants.CUD_LIST)) {
			if ((null != request.getAttribute(Constants.CUD_LIST_REQ)) &&
				(((List<TransactionList>)request.getAttribute(
					Constants.CUD_LIST_REQ)).size() > 0)) {

				List<TransactionList> transactionLists =
					(List<TransactionList>)request.getAttribute(
						Constants.CUD_LIST_REQ);
				sortOrderMap = setDefaultSortOrderMap();
				perfLogger_.debug(
					"[CudTransactionSearchController] Sorting the Transction list at " +
						Calendar.getInstance(
						).getTime());
				transactionLists = Sortable.sort(
					transactionLists, Constants.CUD_TXN_DATE,
					sortOrderMap.get(Constants.CUD_TXN_DATE));
				perfLogger_.debug(
					"[CudTransactionSearchController] Sorting Finished for the Transction list at " +
						Calendar.getInstance(
						).getTime());
				String pageBy = request.getParameter(
					new ParamEncoder(
						Constants.CUD_FRM
					).encodeParameterName(
						TableTagParameters.PARAMETER_PAGE
					));

				logger.debug(
					"[CudTransactionSearchController.showTransactionSearchResult]   is  Page  is " +
						pageBy);
				session.setAttribute(Constants.CUD_LIST, transactionLists);
				request.setAttribute(Constants.CUD_IS_FA, is2FaRole);
				session.setAttribute(Constants.CUD_SORT_ORDER, sortOrderMap);
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
					Constants.CUD_FRM
				).encodeParameterName(
					TableTagParameters.PARAMETER_PAGE
				));

			if (null != session.getAttribute(Constants.CUD_CUR_PAGE))
				logger.debug(
					"[CudTransactionSearchController.showTransactionSearchResult]  session.getAttribute  " +
						session.getAttribute(
							Constants.CUD_CUR_PAGE
						).toString());

			if ("1".equals(pageBy) &&
				((null == session.getAttribute(Constants.CUD_CUR_PAGE)) ||
				 ((null != session.getAttribute(Constants.CUD_CUR_PAGE)) &&
				  !pageBy.equals(
					  session.getAttribute(Constants.CUD_CUR_PAGE))))) {

				logger.debug(
					"[CudTransactionSearchController.showTransactionSearchResult] pageBypageBypageBy is  " +
						pageBy);
				String sortBy = request.getParameter(
					new ParamEncoder(
						Constants.CUD_FRM
					).encodeParameterName(
						TableTagParameters.PARAMETER_SORT
					));
				logger.debug(
					"[CudTransactionSearchController.showTransactionSearchResult]  sortBy is   " +
						sortBy + "  Page session  is  " +
							session.getAttribute(Constants.CUD_CUR_PAGE));

				sortOrderMap = (HashMap<String, String>)session.getAttribute(
					Constants.CUD_SORT_ORDER);
				String orderBy = request.getParameter(
					new ParamEncoder(
						Constants.CUD_FRM
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
					"[CudTransactionSearchController.showTransactionSearchResult] Sorting the Transction list at " +
						Calendar.getInstance(
						).getTime());
				session.setAttribute(
					Constants.CUD_LIST,
					Sortable.sort(
						(List<TransactionList>)session.getAttribute(
							Constants.CUD_LIST),
						sortBy, sortOrderMap.get(sortBy)));
				perfLogger_.debug(
					"[CudTransactionSearchController.showTransactionSearchResult] Finished Sorting the Transction list at " +
						Calendar.getInstance(
						).getTime());
				request.setAttribute(Constants.CUD_IS_FA, is2FaRole);

				if (null != session.getAttribute(Constants.CUD_CUR_PAGE))
					session.setAttribute(
						Constants.CUD_CUR_PAGE,
						session.getAttribute(Constants.CUD_CUR_PAGE));
			}
			else {
				logger.debug(
					"[CudTransactionSearchController.showTransactionSearchResult] pageing only Number  " +
						pageBy);
				request.setAttribute(Constants.CUD_IS_FA, is2FaRole);
				session.setAttribute(Constants.CUD_CUR_PAGE, pageBy);
			}
		}

		if (null != request.getAttribute(Constants.DATE_WARNING_MSG)) {
			logger.debug(
				"[CudTransactionSearchController.showTransactionSearchResult] INSIDE setting the msg for warning for 18 months old date range in Render ");
			request.setAttribute(Constants.DATE_WARNING_MSG, Boolean.TRUE);
		}

		if (null != request.getAttribute(Constants.MORE_DATA_MSG)) {
			logger.debug(
				"[CudTransactionSearchController.showTransactionSearchResult] INSIDE setting the msg for warning for more than 250 found but display only 250 in Render ");
			request.setAttribute(Constants.MORE_DATA_MSG, Boolean.TRUE);
		}

		logger.info("renderOnNon2FAAction - end ");
	}

	/**
	 * Transaction 2FA authentication process render
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.CUD_2FA_RENDER
	)
	@SuppressWarnings("unchecked")
	public String renderToTxDetailsPageAndBackToTxDetails(
		RenderResponse response, RenderRequest request) {

		logger.info("renderToTxDetailsPageAndBackToTxDetails - start ");

		if ((null != request.getAttribute(Constants.ACTION)) &&
			Constants._2FA_ACTION.equals(
				request.getAttribute(Constants.ACTION))) {

			logger.debug(
				"[renderToTxDetailsPageAndBackToTxDetails] : 2FA PAGE RENDER ");

			if (null != request.getAttribute(Constants.CUD_LIST_REQ))
				request.setAttribute(
					Constants.CUD_LIST,
					request.getAttribute(Constants.CUD_LIST_REQ));
			request.setAttribute(Constants.FROM_FA, Boolean.TRUE);
			logger.debug(
				"[renderToTxDetailsPageAndBackToTxDetails] getting the request objects");
			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpSession session = httpServletRequest.getSession();

			if (!StringUtils.isBlank(
					session.getAttribute(
						Constants.CUD_TX_ID
					).toString()) &&
				!StringUtils.isBlank(
					session.getAttribute(
						Constants.CUD_ETL_DATE
					).toString()) &&
				!StringUtils.isBlank(
					session.getAttribute(
						Constants.CUD_AD_PANK
					).toString()) &&
				!StringUtils.isBlank(
					session.getAttribute(
						Constants.CUD_TXN_DETAIL_K
					).toString())) {

				logger.debug(
					"[renderToTxDetailsPageAndBackToTxDetails] setting the request in RENDER MAPPING attributes ");
				request.setAttribute(
					Constants.CUD_TX_ID,
					session.getAttribute(
						Constants.CUD_TX_ID
					).toString());

				request.setAttribute(
					Constants.CUD_AD_PANK,
					session.getAttribute(
						Constants.CUD_AD_PANK
					).toString());
				request.setAttribute(
					Constants.CUD_ETL_DATE,
					session.getAttribute(
						Constants.CUD_ETL_DATE
					).toString());
				request.setAttribute(
					Constants.CUD_TXN_DETAIL_K,
					session.getAttribute(
						Constants.CUD_TXN_DETAIL_K
					).toString());

				logger.debug(
					"[renderToTxDetailsPageAndBackToTxDetails]  %%%%%%%%%%%%%%%%%%% " +
						session.getAttribute(
							Constants.CUD_TX_ID
						).toString());
			}

			if (!StringUtils.isBlank(
					session.getAttribute(
						Constants.CUD_CURR_ROW_NUM
					).toString()) &&
				!Constants.NULL.equalsIgnoreCase(
					session.getAttribute(
						Constants.CUD_CURR_ROW_NUM
					).toString())) {

				request.setAttribute(
					Constants.CUD_CURR_ROW_NUM,
					session.getAttribute(Constants.CUD_CURR_ROW_NUM));
			}
		}
		else {
			logger.debug(
				"[CudTransactionSearchController.showTransactionSearchResult] INSIDE THE TX MAIN SEARCH RESULT RENDER MAPPING  MAP IS  " +
					request.getParameterMap());

			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpSession session = httpServletRequest.getSession();
			Map<String, String> sortOrderMap = null;
			boolean is2FaRole = setUser2FaRoles(request);

			if (null == session.getAttribute(Constants.CUD_LIST)) {
				if ((null != request.getAttribute(Constants.CUD_LIST_REQ)) &&
					(((List<TransactionList>)request.getAttribute(
						Constants.CUD_LIST_REQ)).size() > 0)) {

					List<TransactionList> transactionLists =
						(List<TransactionList>)request.getAttribute(
							Constants.CUD_LIST_REQ);
					sortOrderMap = setDefaultSortOrderMap();
					perfLogger_.debug(
						"[CudTransactionSearchController.showTransactionSearchResult] Sorting First time the Transction list at " +
							Calendar.getInstance(
							).getTime());
					transactionLists = Sortable.sort(
						transactionLists, Constants.CUD_TXN_DATE,
						sortOrderMap.get(Constants.CUD_TXN_DATE));
					perfLogger_.debug(
						"[CudTransactionSearchController.showTransactionSearchResult] Finished Sorting First time  FINISHED the Transction list at " +
							Calendar.getInstance(
							).getTime());
					String pageBy = request.getParameter(
						new ParamEncoder(
							Constants.CUD_FRM
						).encodeParameterName(
							TableTagParameters.PARAMETER_PAGE
						));

					logger.debug(
						"[CudTransactionSearchController.showTransactionSearchResult]   is %%%%%%%%%%%%%%   Page  is $$$$$$$$$$$  " +
							pageBy);
					session.setAttribute(Constants.CUD_LIST, transactionLists);
					request.setAttribute(Constants.CUD_IS_FA, is2FaRole);
					session.setAttribute(
						Constants.CUD_SORT_ORDER, sortOrderMap);
				}
				else {
					request.setAttribute(
						Constants.NO_DATA_FOUND_MSG, Constants.TX_NODATA_FOUND);
				}
			}
			else {
				String pageBy = request.getParameter(
					new ParamEncoder(
						Constants.CUD_FRM
					).encodeParameterName(
						TableTagParameters.PARAMETER_PAGE
					));

				if ("1".equals(pageBy) &&
					((null == session.getAttribute(Constants.CUD_CUR_PAGE)) ||
					 ((null != session.getAttribute(Constants.CUD_CUR_PAGE)) &&
					  pageBy.equals(
						  session.getAttribute(Constants.CUD_CUR_PAGE))))) {

					String sortBy = request.getParameter(
						new ParamEncoder(
							Constants.CUD_FRM
						).encodeParameterName(
							TableTagParameters.PARAMETER_SORT
						));
					logger.debug(
						"[CudTransactionSearchController.showTransactionSearchResult]  sortBy is   " +
							sortBy + "  Page session  is   " +
								session.getAttribute(Constants.CUD_CUR_PAGE));

					sortOrderMap =
						(HashMap<String, String>)session.getAttribute(
							Constants.CUD_SORT_ORDER);
					String orderBy = request.getParameter(
						new ParamEncoder(
							Constants.CUD_FRM
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
						"[CudTransactionSearchController.showTransactionSearchResult] Sorting the Transction list at " +
							Calendar.getInstance(
							).getTime());
					session.setAttribute(
						Constants.CUD_LIST,
						Sortable.sort(
							(List<TransactionList>)session.getAttribute(
								Constants.CUD_LIST),
							sortBy, sortOrderMap.get(sortBy)));
					perfLogger_.debug(
						"[CudTransactionSearchController.showTransactionSearchResult] Sorting FINISHED the Transction list at " +
							Calendar.getInstance(
							).getTime());
					request.setAttribute(Constants.CUD_IS_FA, is2FaRole);

					if (null != session.getAttribute(Constants.CUD_CUR_PAGE))
						session.setAttribute(
							Constants.CUD_CUR_PAGE,
							session.getAttribute(Constants.CUD_CUR_PAGE));
				}
				else {
					logger.debug(
						"[CudTransactionSearchController.showTransactionSearchResult] pageing only Number  " +
							pageBy);
					request.setAttribute(Constants.CUD_IS_FA, is2FaRole);
					session.setAttribute(Constants.CUD_CUR_PAGE, pageBy);
				}
			}

			if (null != request.getAttribute(Constants.DATE_WARNING_MSG)) {
				request.setAttribute(Constants.DATE_WARNING_MSG, Boolean.TRUE);
			}

			if (null != request.getAttribute(Constants.MORE_DATA_MSG)) {
				request.setAttribute(Constants.MORE_DATA_MSG, Boolean.TRUE);
			}
		}

		logger.info("renderToTxDetailsPageAndBackToTxDetails - end ");

		return Constants.CUD_TX_SEARCH_PAGE;
	}

	/**
	 * Default value for sort order map for Transaction list search
	 *
	 * @return  Map<String, String>
	 */
	public Map<String, String> setDefaultSortOrderMap() {
		logger.info("setDefaultSortOrderMap - start ");
		Map<String, String> sortOrderMap = new HashMap<>();

		sortOrderMap.put(Constants.AMOUNT, Constants.NATURAL);
		sortOrderMap.put(Constants.CARD_ACCEPTOR_ID, Constants.NATURAL);
		sortOrderMap.put(Constants.DESCRIPTION, Constants.NATURAL);
		sortOrderMap.put(Constants.MESSAGE_TYPE, Constants.NATURAL);
		sortOrderMap.put(Constants.PAN, Constants.NATURAL);
		sortOrderMap.put(Constants.RESPONSE_CODE, Constants.NATURAL);
		sortOrderMap.put(Constants.SYSTEM_TRACE, Constants.NATURAL);
		sortOrderMap.put(Constants.TERMINAL_ID, Constants.NATURAL);
		//sortOrderMap.put(Constants.DATE_TIME, Constants.REVERSE);

		// added for CUD TXN

		sortOrderMap.put(Constants.CUD_TXN_DATE, Constants.REVERSE);
		sortOrderMap.put(Constants.TERMINAL_LOCATION, Constants.NATURAL);
		logger.info("setDefaultSortOrderMap - end ");

		return sortOrderMap;
	}

	public void setOrganisationIdOverride(String organisationIdOverride) {
		this.organisationIdOverride = organisationIdOverride;
	}

	/**
	 * Check and set the user 2 FA roles detail in object.
	 *
	 * @param PortletRequest
	 * @return boolean
	 */
	public boolean setUser2FaRoles(PortletRequest request) {
		logger.info("setUser2FaRoles - start ");
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
					"[setUser2FaRoles]  All Roles count is " +
						allUserRoles.size());

				for (Role role : allUserRoles) {
					if (Constants.TX_ROLE_FA.equalsIgnoreCase(role.getName())) {
						is2faAccessible = true;
						logger.debug(
							"[setUser2FaRoles] setting role as true for Role  " +
								role.getName());
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("setUser2FaRoles - end ");

		return is2faAccessible;
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
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.CUD_LOGPOOL
	)
	public String showLogPools(
		RenderResponse response, RenderRequest request,
		@ModelAttribute TransactionForm transactionForm) {

		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();

		cudTransactionSearchService.logPoolStatistics();

		transactionForm.resetForm();
		session.removeAttribute(Constants.CUD_LIST);

		return Constants.CUD_TX_SEARCH_PAGE;
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
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.CUD_PURGEPOOL
	)
	public String showPurgePools(
		RenderResponse response, RenderRequest request,
		@ModelAttribute TransactionForm transactionForm) {

		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();
		cudTransactionSearchService.purgePools();
		transactionForm.resetForm();
		session.removeAttribute(Constants.CUD_LIST);

		return Constants.CUD_TX_SEARCH_PAGE;
	}

	/**
	 * Shows the search results of the Transaction
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.CUD_SEARCH_RESULT
	)
	public String showTransactionSearchResult(
		RenderResponse response, RenderRequest request) {

		logger.info("showTransactionSearchResult - start ");

		if ((null != request.getAttribute(Constants.ACTION)) &&
			Constants._2FA_ACTION.equals(
				request.getAttribute(Constants.ACTION))) {

			renderOn2FAAction(request, response);
		}
		else {
			renderOnNon2FAAction(request, response);
		}

		logger.info("showTransactionSearchResult - end ");

		return Constants.CUD_TX_SEARCH_PAGE;
	}

	/**
	 * Shows the search results of the Transaction
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.CUD_DETAIL
	)
	public String showTxDetailsPageRender(
		RenderResponse response, RenderRequest request) {

		logger.info("showTxDetailsPageRender - start ");
		boolean is2FaRole = setUser2FaRoles(request);
		TransactionDetail transactionDetail = null;

		if (!StringUtils.isBlank(request.getParameter(Constants.CUD_TX_ID)) &&
			!StringUtils.isBlank(
				request.getParameter(Constants.CUD_ETL_DATE)) &&
			!StringUtils.isBlank(request.getParameter(Constants.CUD_AD_PANK)) &&
			!StringUtils.isBlank(
				request.getParameter(Constants.CUD_TXN_DETAIL_K))) {

			request.setAttribute(
				Constants.CUD_AD_PANK,
				request.getParameter(Constants.CUD_AD_PANK));
			request.setAttribute(
				Constants.CUD_ETL_DATE,
				request.getParameter(Constants.CUD_ETL_DATE));
			request.setAttribute(
				Constants.CUD_TX_ID, request.getParameter(Constants.CUD_TX_ID));
			request.setAttribute(
				Constants.CUD_TXN_DETAIL_K,
				request.getParameter(Constants.CUD_TXN_DETAIL_K));

			request.setAttribute(Constants.CUD_IS_FA, is2FaRole);
			request.setAttribute(Constants.FROM_FA, Boolean.FALSE);
			request.setAttribute(
				Constants.TRANSACTION_CUD_DETAIL, transactionDetail);
		}

		logger.info("showTxDetailsPageRender - end ");

		return Constants.TX_DETAIL_PAGE;
	}

	private static final Auditor audit = Auditor.getInstance();

	/**
	 * Logger object
	 */
	private static Logger logger = Logger.getLogger("cudLogging");

	/**
	 * Logger object for performance logging
	 */
	private static Logger perfLogger_ = Logger.getLogger("perfLogging");

	/**
	 * CUD Transaction Service used to fetch data
	 */
	@Autowired
	@Qualifier(Constants.CUD_TRANSACTION_SERVICE)
	private CudTransactionSearchService cudTransactionSearchService;

	/**
	 * CUD Transaction Validator
	 */
	@Autowired
	@Qualifier(Constants.CUD_TRANSACTION_FORM_VALIDATOR)
	private CudTransactionSearchValidator cudTransactionSearchValidator;

	/**
	 * organisationIdOverride object
	 */
	private String organisationIdOverride = null;

}