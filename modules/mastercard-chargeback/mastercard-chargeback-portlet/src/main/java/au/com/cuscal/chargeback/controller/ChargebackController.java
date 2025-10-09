//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.controller;

import static au.com.cuscal.chargeback.common.Constants.FLAG_TRUE;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID_STR;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID_STR;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID_STR;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID_STR;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_REPORT_FRAUD_ATTRIBUTES_LIST_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_RETRIEVAL_REQUEST_ATTRIBUTES_LIST_ID;

import static javax.portlet.PortletSession.APPLICATION_SCOPE;

import au.com.cuscal.chargeback.common.AttributesListId;
import au.com.cuscal.chargeback.common.AttributesTypeId;
import au.com.cuscal.chargeback.common.ChargebackProperties;
import au.com.cuscal.chargeback.common.ChargebacksUtility;
import au.com.cuscal.chargeback.common.Constants;
import au.com.cuscal.chargeback.common.FunctionCode;
import au.com.cuscal.chargeback.forms.ChargebackForm;
import au.com.cuscal.chargeback.validator.ChargeBackValidator;
import au.com.cuscal.common.framework.AuditWebServicesUtil;
import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.common.framework.dxp.service.request.controller.AbstractServiceRequestController;
import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.common.framework.dxp.service.request.services.ServiceRequestServiceImpl;
import au.com.cuscal.common.framework.service.request.domain.Transaction;
import au.com.cuscal.common.framework.service.request.services.ServiceRequestService;
import au.com.cuscal.connect.util.resource.dxp.UserUtilImpl;
import au.com.cuscal.framework.audit.category.AuditCategories;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.audit.xml.XmlMessage;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.AdditionalTransactionInfo;
import au.com.cuscal.framework.webservices.selfservice.AttachmentDataType;
import au.com.cuscal.framework.webservices.selfservice.AttributesListType;
import au.com.cuscal.framework.webservices.selfservice.ContactType;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.RequestTypeType;
import au.com.cuscal.framework.webservices.selfservice.ServiceRequestRequestType;
import au.com.cuscal.framework.webservices.selfservice.ServiceRequestResponseType;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.selfservice.TransactionType;
import au.com.cuscal.framework.webservices.selfservice.TypeType;
import au.com.cuscal.framework.webservices.selfservice.UpdateTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.UpdateTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.UserType;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.activation.DataHandler;

import javax.mail.util.ByteArrayDataSource;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller("chargebackController")
@RequestMapping({"VIEW"})
public class ChargebackController extends AbstractServiceRequestController {

	public static TktRequestHeader convertSearchHeaderToTktHeader(
		SearchHeader searchHeader) {

		TktRequestHeader tktHeader = null;

		if (null != searchHeader) {
			tktHeader = new TktRequestHeader();

			tktHeader.setOrigin(searchHeader.getOrigin());
			tktHeader.setUserId(searchHeader.getUserId());
			tktHeader.setUserOrgId(searchHeader.getUserOrgId());
			tktHeader.setUserOrgName(searchHeader.getUserOrgName());
		}

		return tktHeader;
	}

	/**
	 *
	 * @return
	 */
	public TicketType getTicketType() {
		return this.ticketType;
	}

	/**
	 * Link from the transaction details screen to here.
	 *
	 * @param request
	 * @param response
	 * @param ticketNote
	 * @return
	 */
	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.CHARGEBACK_REDIRECT
	)
	public String navigateToChargeBack(
		RenderRequest request, RenderResponse response, ModelMap map) {

		logger.debug("navigateToChargeBack - start");
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		PortletSession portletSession = request.getPortletSession(true);

		if (null == portletSession.getAttribute(
				Constants.TRANSACTION_DETAIL_BYTES, APPLICATION_SCOPE)) {

			logger.error(
				"navigateToChargeBack - Could not get the " +
					"transactionDetail from the session. Redirecting to error " +
						"page.");

			return Constants.ERROR_PAGE;
		}

		if (!map.containsAttribute("chargebackForm")) {
			map.addAttribute("chargebackForm", new ChargebackForm());
		}

		byte[] bytes = (byte[])portletSession.getAttribute(
			Constants.TRANSACTION_DETAIL_BYTES, APPLICATION_SCOPE);

		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		Transaction transaction = null;

		try {
			final ObjectInputStream o = new ObjectInputStream(b);
			final Object sessionObject = o.readObject();

			if (!(sessionObject instanceof Transaction)) {
				logger.error(
					"navigateToChargeBack - error retrieving transaction from session");

				return Constants.ERROR_PAGE;
			}

			transaction = (Transaction)sessionObject;
			request.setAttribute(Constants.TRANSACTION_DETAIL, transaction);
			session.removeAttribute(Constants.TRANSACTION_DETAIL_BYTES);
			final String functionCodeDescription =
				ChargebacksUtility.setFunctionCodeDescription(
					transaction.getFunctionCode());
			transaction.setFunctionCodeDescription(functionCodeDescription);
			final String ucafCollectionIndicator =
				ChargebacksUtility.getUCAFCollectionIndicatorDescription(
					transaction.getUCAFCollectionIndicator());
			transaction.setUCAFCollectionIndicatorDescription(
				ucafCollectionIndicator);
			portletSession.removeAttribute(
				Constants.TRANSACTION_DETAIL_BYTES,
				PortletSession.APPLICATION_SCOPE);
			session.setAttribute(Constants.TRANSACTION_DETAIL, transaction);
		}
		catch (IOException e) {
			logger.error("navigateToChargeBack - " + e.getMessage(), e);

			return Constants.ERROR_PAGE;
		}
		catch (ClassNotFoundException e2) {
			logger.error("navigateToChargeBack - " + e2.getMessage(), e2);

			return Constants.ERROR_PAGE;
		}

		SearchHeader searchHeader = getSearchHeaderData(request);

		if (null == searchHeader) {
			logger.error("navigateToChargeBack - SearchHeader is null.");

			return Constants.ERROR_PAGE;
		}

		setupRequestObjects(searchHeader, request, transaction, null, null);
		logger.debug("navigateToChargeBack - end");

		return Constants.HOMEPAGE;
	}

	/**
	 *
	 * @param ticketType
	 */
	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}

	/**
	 * Show the confirmation page after they have submitted the request.
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.CONFIRM
	)
	public String showConfirmationPage(
		RenderRequest request, RenderResponse response) {

		logger.debug("showConfirmationPage - start");

		HttpSession session = getSession(request);

		// Remove the transaction details from the session once the service
		// request has been submitted.

		if (null != session.getAttribute(Constants.TRANSACTION_DETAIL)) {
			session.removeAttribute(Constants.TRANSACTION_DETAIL);
		}

		logger.debug("showConfirmationPage - end");

		return Constants.CONFIRM;
	}

	/**
	 * Redirect to the error page.
	 *
	 * @param  request	RenderRequest
	 * @param  response	RenderResponse
	 * @return String
	 */
	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.ERROR_PAGE
	)
	public String showErrorPage(
		RenderRequest request, RenderResponse response) {

		logger.debug(
			"showErrorPage - " + request.getAttribute("sessionTimeout"));

		return Constants.ERROR_PAGE;
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RenderMapping
	public String showHomePage(
		RenderRequest request, RenderResponse response, ModelMap modelMap) {

		logger.debug("showHomePage - start");
		String requestId = request.getParameter("ticketId");

		if (StringUtils.isBlank(requestId)) {
			requestId = (String)request.getAttribute("ticketId");
		}

		String editTicket = request.getParameter("editTicket");

		if (StringUtils.isBlank(editTicket)) {
			editTicket = (String)request.getAttribute("editTicket");
		}

		logger.debug("editTicket=" + editTicket);
		HttpSession session = getSession(request);
		Transaction transaction = null;

		PortletSession portletSession = request.getPortletSession(true);

		byte[] bytes = (byte[])portletSession.getAttribute(
			Constants.TRANSACTION_DETAIL_BYTES);

		if (null != bytes) {
			ByteArrayInputStream b = new ByteArrayInputStream(bytes);

			try {
				ObjectInputStream o = new ObjectInputStream(b);

				Object sessionObject = o.readObject();

				if (sessionObject instanceof Transaction) {
					transaction = (Transaction)sessionObject;
				}
				else {
					logger.error(
						"navigateToChargeBack - error retrieving transaction from session");

					return Constants.ERROR_PAGE;
				}
			}
			catch (IOException e) {
				logger.error("navigateToChargeBack - " + e.getMessage(), e);

				return Constants.ERROR_PAGE;
			}
			catch (ClassNotFoundException e) {
				logger.error("navigateToChargeBack - " + e.getMessage(), e);

				return Constants.ERROR_PAGE;
			}
		}
		else {
			logger.error(
				"showHomePage - session.getAttribute(Constants.TRANSACTION_DETAIL_BYTES) is null");
			transaction = (Transaction)session.getAttribute(
				Constants.TRANSACTION_DETAIL);
		}

		if (null != transaction) {
			request.setAttribute(Constants.TRANSACTION_DETAIL, transaction);

			// remove the serialized object and add the non-serialized object

			session.removeAttribute(Constants.TRANSACTION_DETAIL_BYTES);
			String functionCodeDescription =
				ChargebacksUtility.setFunctionCodeDescription(
					transaction.getFunctionCode());

			transaction.setFunctionCodeDescription(functionCodeDescription);
			String ucafCollectionIndicatorDescription =
				ChargebacksUtility.getUCAFCollectionIndicatorDescription(
					transaction.getUCAFCollectionIndicator());

			transaction.setUCAFCollectionIndicatorDescription(
				ucafCollectionIndicatorDescription);
			session.setAttribute(Constants.TRANSACTION_DETAIL, transaction);
		}
		else {
			logger.warn(
				"submitChargeback - Could not retrieve the transaction details");

			if (!FLAG_TRUE.equals(editTicket)) {
				return Constants.ERROR_PAGE;
			}
		}

		logger.debug("showHomePage - requestId: " + requestId);

		SearchHeader searchHeader = getSearchHeaderData(request);

		if (StringUtils.isBlank(requestId)) {
			if (!modelMap.containsAttribute("chargebackForm")) {
				modelMap.addAttribute("chargebackForm", new ChargebackForm());
			}

			if ((null != searchHeader) &&
				StringUtils.isNotBlank(searchHeader.getUserOrgId())) {

				setupRequestObjects(
					searchHeader, request, transaction, null, null);

				return Constants.HOMEPAGE;
			}

			return Constants.ERROR_PAGE;
		}

		TktRequestHeader header = Utility.convertSearchHeaderToTktHeader(
			searchHeader);
		GetTicketRequest getTicketRequest = new GetTicketRequest();

		getTicketRequest.setHeader(header);
		getTicketRequest.setTicketId(requestId);
		GetTicketResponse resp = serviceRequestService.ticketDetails(
			getTicketRequest);

		if ((null == resp) || (null == resp.getTicket())) {
			ChargebackController.logger.error(
				"showHomePage - could not get service request details for ticket: " +
					requestId);

			return Constants.ERROR_PAGE;
		}

		if ((null == resp.getTicket(
			).getTransactions()) &&
			(resp.getTicket(
			).getTransactions(
			).
				size() <= 0)) {

			return Constants.ERROR_PAGE;
		}

		transaction = ChargebacksUtility.convertTransactionTypeToTransaction(
			resp.getTicket());

		request.setAttribute(Constants.TRANSACTION_DETAIL, transaction);
		session.setAttribute(Constants.TRANSACTION_DETAIL, transaction);

		request.setAttribute("updateTicket", Boolean.TRUE);
		TicketType ticket = resp.getTicket();

		if (FLAG_TRUE.equals(editTicket)) {
			ticket.setUpdateTicket(true);
		}

		ChargebackForm chargebackForm = new ChargebackForm(ticket);

		setTicketType(resp.getTicket());

		if (!modelMap.containsAttribute("chargebackForm")) {
			modelMap.addAttribute("chargebackForm", chargebackForm);
		}

		request.removeAttribute("ticketId");
		request.removeAttribute("editTicket");

		if ((null != searchHeader) &&
			StringUtils.isNotBlank(searchHeader.getUserOrgId())) {

			if (FLAG_TRUE.equals(editTicket)) {
				transaction.setMessageType(chargebackForm.getMessageType());
				transaction.setFunctionCode(chargebackForm.getFunctionCode());

				String outstandingMasterCardDisputeRetrievalRequest = null;
				String outstandingMasterCardDisputeFirstChargeback = null;
				String outstandingMasterCardDisputeArbitrationChargeback = null;
				String outstandingMasterCardDisputeReportFraud = null;
				long masterCardDisputeRetrievalRequestId =
					ChargebacksUtility.getServiceRequestTypeId(
						MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID_STR,
						MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID);
				long masterCardDisputeFirstChargebackId =
					ChargebacksUtility.getServiceRequestTypeId(
						MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID_STR,
						MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID);
				long masterCardDisputeArbitrationChargebackId =
					ChargebacksUtility.getServiceRequestTypeId(
						MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID_STR,
						MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID);
				long masterCardDisputeReportFraudId =
					ChargebacksUtility.getServiceRequestTypeId(
						MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID_STR,
						MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID);

				FindServiceRequestRequest paramFindServiceRequestRequest =
					new FindServiceRequestRequest();

				paramFindServiceRequestRequest.setTransactionId(
					Long.valueOf(transaction.getTransactionId()));
				paramFindServiceRequestRequest.setHeader(header);
				XMLGregorianCalendar businessXmlCal =
					Utility.createXmlGregorianCalendarFromString(
						transaction.getBusinessDate(), "dd/MM/yyyy",
						Boolean.FALSE);

				paramFindServiceRequestRequest.setBusinessDate(businessXmlCal);
				FindServiceRequestResponse findServiceRequestResponse =
					serviceRequestService.findServiceRequest(
						paramFindServiceRequestRequest);

				if (null != findServiceRequestResponse) {
					List<ServiceRequestResponseType>
						serviceRequestResponseTypeList =
							findServiceRequestResponse.getServiceRequests();

					if ((null != serviceRequestResponseTypeList) &&
						(serviceRequestResponseTypeList.size() > 0)) {

						for (ServiceRequestResponseType
								serviceRequestResponseType :
									serviceRequestResponseTypeList) {

							TicketType ticketType =
								serviceRequestResponseType.getTicket();
							logger.debug(
								"ticketType id =" + ticketType.getId() +
									" and requestId=" + requestId);

							if (requestId.equals(
									String.valueOf(ticketType.getId()))) {

								logger.debug(
									"Request id matches with requestId= " +
										requestId +
											" The edit request is for the ticket id " +
												ticketType.getId() + " and " +
													ticketType.
														getDescription());
							}
							else {
								TypeType typeType = ticketType.getType();

								long typeId = typeType.getTypeId();

								if (masterCardDisputeRetrievalRequestId ==
										typeId) {

									outstandingMasterCardDisputeRetrievalRequest =
										"true";
									ChargebackController.logger.debug(
										"addSubmissionValidationFlags - found existing MasterCardDisputeRetrievalRequest");
								}
								else if (masterCardDisputeFirstChargebackId ==
											typeId) {

									outstandingMasterCardDisputeFirstChargeback =
										"true";
									ChargebackController.logger.debug(
										"addSubmissionValidationFlags - found existing MasterCardDisputeFirstChargeback");
								}
								else if (masterCardDisputeArbitrationChargebackId ==
											typeId) {

									outstandingMasterCardDisputeArbitrationChargeback =
										"true";
									ChargebackController.logger.debug(
										"addSubmissionValidationFlags - found existing MasterCardDisputeArbitrationChargeback");
								}
								else {
									if (masterCardDisputeReportFraudId !=
											typeId) {

										continue;
									}

									outstandingMasterCardDisputeReportFraud =
										"true";
									ChargebackController.logger.debug(
										"addSubmissionValidationFlags - found existing MasterCardDisputeReportFraud");
								}
							}
						}
					}
				}

				logger.debug(
					"showHomePage - outstandingMasterCardDisputeRetrievalRequest=" +
						outstandingMasterCardDisputeRetrievalRequest);
				logger.debug(
					"showHomePage - outstandingMasterCardDisputeFirstChargeback=" +
						outstandingMasterCardDisputeFirstChargeback);
				logger.debug(
					"showHomePage - outstandingMasterCardDisputeArbitrationChargeback=" +
						outstandingMasterCardDisputeArbitrationChargeback);
				logger.debug(
					"showHomePage - outstandingMasterCardDisputeReportFraud=" +
						outstandingMasterCardDisputeReportFraud);

				session.setAttribute(
					"outstandingMasterCardDisputeArbitrationChargeback",
					outstandingMasterCardDisputeArbitrationChargeback);
				session.setAttribute(
					"outstandingMasterCardDisputeFirstChargeback",
					outstandingMasterCardDisputeFirstChargeback);
				session.setAttribute(
					"outstandingMasterCardDisputeReportFraud",
					outstandingMasterCardDisputeReportFraud);
				session.setAttribute(
					"outstandingMasterCardDisputeRetrievalRequest",
					outstandingMasterCardDisputeRetrievalRequest);
			}

			setupRequestObjects(
				searchHeader, request, transaction, editTicket,
				chargebackForm.getType());

			return Constants.HOMEPAGE;
		}

		return Constants.ERROR_PAGE;
	}

	/**
	 * Method to add a new chargeback Service Request for the transaction with
	 * attachments.
	 *
	 * @param request
	 * @param response
	 * @param serviceRequestForm
	 */
	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.SUBMIT_CHARGEBACK
	)
	public void submitChargeback(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("chargebackForm") ChargebackForm chargebackForm,
		BindingResult bindingResult) {

		logger.debug("submitChargeback - start");

		SearchHeader searchHeader = getSearchHeaderData(request);

		TktRequestHeader header = convertSearchHeaderToTktHeader(searchHeader);

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);
		String portletName = (String)request.getAttribute(WebKeys.PORTLET_ID);

		PortletURL url = PortletURLFactoryUtil.create(
			PortalUtil.getHttpServletRequest(request), portletName,
			themeDisplay.getLayout(
			).getPlid(),
			PortletRequest.RENDER_PHASE);

		User user = Utility.getUserFromRequest(request);

		if (null != user) {
			HttpSession session = getSession(request);

			Transaction transaction = (Transaction)session.getAttribute(
				Constants.TRANSACTION_DETAIL);

			if (null != transaction) {
				logger.debug(
					"submitChargeback - before validating type: " +
						chargebackForm.getType());
				//to validate dispute amount and card holder amount
				chargebackForm.setCardHolderAmount(
					transaction.getCardholderAmount());
				chargebackForm.setMessageType(transaction.getMessageType());
				chargebackForm.setFunctionCode(transaction.getFunctionCode());

				chargeBackValidator.validate(
					chargebackForm, bindingResult, chargebackProperties);

				try {
					if (!bindingResult.hasErrors()) {
						if (MASTERCARD_RETRIEVAL_REQUEST_ATTRIBUTES_LIST_ID.
								equals(chargebackForm.getType()) ||
							MASTERCARD_REPORT_FRAUD_ATTRIBUTES_LIST_ID.equals(
								chargebackForm.getType())) {

							//do not want to save dispute amount on Retrieval Request or Report Fraud
							chargebackForm.setDisputeAmount(null);
						}

						callAddServiceRequest(
							chargebackForm, bindingResult, header, user,
							transaction, request, response);
					}
				}
				catch (Exception e) {
					bindingResult.rejectValue(
						"errorMsg", "chargeback.submission.error");
					logger.error("addServiceRequest - There are errors: ", e);
				}

				if (!bindingResult.hasErrors()) {
					request.setAttribute(Constants.ERRORS, Boolean.FALSE);
					url.setParameter(Constants.RENDER, Constants.CONFIRM);

					try {
						String page = url.toString();

						if (page.indexOf("&amp;") > -1) {
							page = url.toString(
							).replace(
								"&amp;", "&"
							);
						}

						logger.debug("submitChargeback - url is: " + page);
						response.sendRedirect(page);
					}
					catch (IOException e) {
						logger.warn(
							"submitChargeback - Could not redirect to the success page.");
						response.setRenderParameter(
							Constants.RENDER, Constants.CONFIRM);
					}
				}
				else {
					logger.error(
						"submitChargeback - There are errors :-->" +
							bindingResult.getAllErrors());
					request.setAttribute(Constants.ERRORS, Boolean.TRUE);
				}

				request.setAttribute(
					Constants.TRANSACTION_BUSINESS_DATE,
					transaction.getBusinessDate());
				request.setAttribute(
					Constants.TRANSACTION_ID, transaction.getTransactionId());
			}
			else {
				logger.warn(
					"submitChargeback - Could not retrieve the " +
						"transaction details to add the chargeback for");
				url.setParameter(Constants.RENDER, Constants.ERROR_PAGE);

				try {
					String page = url.toString();

					if (page.indexOf("&amp;") > -1) {
						page.replace("&amp;", "&");
					}

					response.sendRedirect(page);
				}
				catch (IOException e) {
					logger.warn(
						"submitChargeback - Could not redirect to the error page");
					response.setRenderParameter(
						Constants.RENDER, Constants.ERROR_PAGE);
				}
			}
		}
		else {
			logger.warn(
				"submitChargeback - Could not get user details. " +
					"Redirecting to error page.");

			request.setAttribute("sessionTimeout", Boolean.TRUE);
			response.setRenderParameter(Constants.RENDER, Constants.ERROR_PAGE);
		}

		logger.debug("submitChargeback - end");
	}

	/**
	 * Action method to handle the update Chargeback call.
	 *
	 * @param request
	 * @param response
	 * @param chargebackForm
	 * @param bindingResult
	 */
	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_CHARGEBACK
	)
	public void updateChargeback(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("chargebackForm") ChargebackForm chargebackForm,
		BindingResult bindingResult) {

		logger.debug("updateChargeback - start");

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);
		String portletName = (String)request.getAttribute(WebKeys.PORTLET_ID);

		SearchHeader searchHeader = getSearchHeaderData(request);

		TktRequestHeader header = convertSearchHeaderToTktHeader(searchHeader);

		PortletURL url = PortletURLFactoryUtil.create(
			PortalUtil.getHttpServletRequest(request), portletName,
			themeDisplay.getLayout(
			).getPlid(),
			PortletRequest.RENDER_PHASE);

		User user = Utility.getUserFromRequest(request);

		//Check if the user session is still active or not.

		if (null == user) {
			url.setParameter(Constants.RENDER, Constants.ERROR_PAGE);

			try {
				String page = url.toString();

				if (page.indexOf("&amp;") > -1) {
					page = page.replace("&amp;", "&");
				}

				response.sendRedirect(page);
			}
			catch (Exception e) {
				logger.error(
					"updateChargeback - Could not redirect to error" + " page");
				response.setRenderParameter(
					Constants.RENDER, Constants.ERROR_PAGE);
			}

			logger.error("updateChargeback - User session has timed out.");
		}
		else {
			HttpSession session = getSession(request);

			Transaction transaction = (Transaction)session.getAttribute(
				Constants.TRANSACTION_DETAIL);

			if (null != transaction) {
				logger.debug(
					"submitChargeback - before validating type: " +
						chargebackForm.getType());
				//to validate dispute amount and card holder amount
				chargebackForm.setCardHolderAmount(
					transaction.getCardholderAmount());
				chargebackForm.setMessageType(transaction.getMessageType());
				chargebackForm.setFunctionCode(transaction.getFunctionCode());
				//set additionalTransactionInfo from session
				chargebackForm.setPosConditionCode(
					transaction.getPosConditionCode());
				chargebackForm.setPosConditionDescription(
					transaction.getPosConditionDescription());
				chargebackForm.setPosEntryModeCode(
					transaction.getPosEntryModeCode());
				chargebackForm.setPosEntryModeDescription(
					transaction.getPosEntryModeDescription());
				chargebackForm.setUCAFCollectionIndicator(
					transaction.getUCAFCollectionIndicator());
				chargebackForm.setUCAFCollectionIndicatorDescription(
					transaction.getUCAFCollectionIndicatorDescription());
				chargebackForm.setElectronicCommerceIndciator(
					transaction.getElectronicCommerceIndciator());
				chargebackForm.setCentralSiteBusinessDate(
					transaction.getCentralSiteBusinessDate());
				chargebackForm.setChipCardPresent(
					transaction.getChipCardPresent());
				chargebackForm.setTrack2DataServiceCode(
					transaction.getTrack2DataServiceCode());
			}

			chargeBackValidator.validate(
				chargebackForm, bindingResult, chargebackProperties);

			/*  */
			if (bindingResult.hasErrors()) {
				logger.error("updateChargeback - There are errors");
				request.setAttribute("ticketId", chargebackForm.getTicketId());
				request.setAttribute("editTicket", "true");
				request.setAttribute(Constants.ERRORS, Boolean.TRUE);

				logger.debug("udpateChargeback - end");
			}
			else {
				if (MASTERCARD_RETRIEVAL_REQUEST_ATTRIBUTES_LIST_ID.equals(
						chargebackForm.getType()) ||
					MASTERCARD_REPORT_FRAUD_ATTRIBUTES_LIST_ID.equals(
						chargebackForm.getType())) {

					//do not want to save dispute amount on Retrieval Request or Report Fraud
					chargebackForm.setDisputeAmount(null);
				}

				callUpdateServiceRequest(
					chargebackForm, bindingResult, header, user,
					getTicketType(), request, response);

				if (bindingResult.hasErrors()) {
					url.setParameter(Constants.RENDER, Constants.ERROR_PAGE);

					try {
						String page = url.toString();

						if (page.indexOf("&amp;") > -1) {
							page = page.replace("&amp;", "&");
						}

						response.sendRedirect(page);
					}
					catch (Exception e) {
						logger.error(
							"updateChargeback - Could not redirect to error" +
								" page");
						response.setRenderParameter(
							Constants.RENDER, Constants.ERROR_PAGE);
					}

					logger.error("updateChargeback - Could not update ticket.");
				}
				else {
					url.setParameter(Constants.RENDER, Constants.CONFIRM);

					try {
						String page = url.toString();

						if (page.indexOf("&amp;") > -1) {
							page = page.replace("&amp;", "&");
						}

						response.sendRedirect(page);
					}
					catch (Exception e) {
						logger.error(
							"updateChargeback - Could not redirect to the " +
								"success page, so using Liferay's Render method.");
						response.setRenderParameter(
							Constants.RENDER, Constants.CONFIRM);
					}
				}
			}
		}

		logger.debug("udpateChargeback - end");
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	protected HttpSession getSession(PortletRequest request) {
		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();

		return session;
	}

	/**
	 *
	 * @param chargebackForm
	 * @param bindingResult
	 * @param header
	 * @param user
	 * @param transaction
	 * @throws Exception
	 */
	private void callAddServiceRequest(
			ChargebackForm chargebackForm, BindingResult bindingResult,
			TktRequestHeader header, User user, Transaction transaction,
			PortletRequest request, PortletResponse response)
		throws Exception {

		logger.debug("callAddServiceRequest - start");

		String statusCode = null;
		AddServiceRequestResponse addServiceResponse = null;
		AddServiceRequestRequest addServiceRequest =
			new AddServiceRequestRequest();
		XmlMessage xmlMessage = new XmlMessage();

		if (null != header) {
			addServiceRequest.setHeader(header);
			xmlMessage =
				AuditWebServicesUtil.addTktRequestHeaderElementsToXmlMessage(
					header, xmlMessage);

			if (null != chargebackForm) {
				ServiceRequestRequestType requestType =
					setupRequestDataForAddServiceRequest(
						chargebackForm, user, transaction);

				requestType.getRequestType(
				).getChargeback(
				).setDisputeAmount(
					chargebackForm.getDisputeAmount()
				);
				requestType.getRequestType(
				).getChargeback(
				).setDocumentIndicator(
					chargebackForm.isDocumentIndicator()
				);
				AdditionalTransactionInfo additionalTransactionInfo =
					setAdditionalTransactionInformation(
						chargebackForm, transaction, requestType);

				requestType.getRequestType(
				).getChargeback(
				).setAdditionalTransactionInfo(
					additionalTransactionInfo
				);

				addServiceRequest.setServiceRequestRequest(requestType);

				addServiceResponse = serviceRequestService.addServiceRequest(
					addServiceRequest);
			}

			xmlMessage.addParameter(
				Constants.TRANSACTION_ID, transaction.getTransactionId());
			xmlMessage.addParameter(
				Constants.TRANSACTION_BUSINESS_DATE,
				transaction.getBusinessDate());

			if (null != addServiceResponse) {
				if (null != addServiceResponse.getHeader()) {
					statusCode = addServiceResponse.getHeader(
					).getStatusCode();

					if (StringUtils.equalsIgnoreCase(
							Constants.STATUS_SUCCESS, statusCode)) {

						xmlMessage.addResult(
							"ticketId",
							Long.valueOf(
								addServiceResponse.getServiceRequestResponse(
								).getTicket(
								).getId()
							).toString());

						logger.debug(
							"callAddServiceRequest - Successfully " +
								"added ticket. Status Code:" + statusCode);
					}
					else {
						xmlMessage.addComment(
							"responseMessage",
							addServiceResponse.getHeader(
							).getMessage());

						logger.error(
							"callAddServiceRequest - Response header is not successful. Status Code:" +
								statusCode);
					}
				}
				else {
					logger.error(
						"callAddServiceRequest - Response header is null");
					xmlMessage.addComment(
						"responseHeader", "Response Header is null");
				}
			}
			else {
				logger.error("callAddServiceRequest - Response is null");
				xmlMessage.addComment("response", "Response is null");
			}

			if (StringUtils.equalsIgnoreCase(
					Constants.STATUS_SUCCESS, statusCode)) {

				logger.debug(
					"callAddServiceRequest - message from add request is: " +
						statusCode);

				audit.success(
					response, request, header.getUserId(),
					AuditCategories.TICKETING_ADD, xmlMessage.toXml());
			}
			else {
				bindingResult.rejectValue(
					"errorMsg", "chargeback.submission.error");

				audit.fail(
					response, request, header.getUserId(),
					AuditCategories.TICKETING_ADD, xmlMessage.toXml());
				logger.error(
					"callAddServiceRequest - add service request failed");
			}
		}

		logger.debug("callAddServiceRequest - start");
	}

	/**
	 *
	 * @param form
	 * @param bindingResult
	 * @param header
	 * @param user
	 * @param transaction
	 * @param request
	 * @param response
	 */
	private void callUpdateServiceRequest(
		ChargebackForm form, BindingResult bindingResult,
		TktRequestHeader header, User user, TicketType ticket,
		PortletRequest request, PortletResponse response) {

		logger.debug("callUpdateServiceRequest - start");
		String statusCode = null;
		UpdateTicketResponse updateTicketResponse = null;
		UpdateTicketRequest updateTicketRequest = new UpdateTicketRequest();
		XmlMessage xmlMessage = new XmlMessage();

		if (null == header) {
			logger.error(
				"callUpdateServiceRequest - The header for the request is null.");
			bindingResult.rejectValue(
				"errorMsg", "chargeback.submission.error");
		}
		else {
			xmlMessage =
				AuditWebServicesUtil.addTktRequestHeaderElementsToXmlMessage(
					header, xmlMessage);

			updateTicketRequest.setHeader(header);

			if (null == form) {
				logger.error(
					"callUpdateServiceRequest - The chargeback form is null.");
				bindingResult.rejectValue(
					"errorMsg", "chargeback.submission.error");
			}
			else {
				ticket =
					ChargebacksUtility.
						updateChargebackInformationForUpdateChargeback(
							form, ticket, user);

				UserType userType = new UserType();

				userType.setUserId(header.getUserId());
				ticket.setLastReadBy(userType);

				try {
					GregorianCalendar cal = new GregorianCalendar();

					cal.setTime(new Date());
					XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance(
					).newXMLGregorianCalendar(
						cal
					);

					ticket.setLastReadTimestamp(xmlCal);
				}
				catch (DatatypeConfigurationException e) {
					logger.error(
						"callUpdateServiceRequest - " + e.getMessage(), e);
				}

				List<MultipartFile> attachments = form.getRequestAttachments();

				if ((null != attachments) && (attachments.size() > 0)) {
					List<AttachmentDataType> attachmentTypes =
						new ArrayList<>();

					for (MultipartFile multipartFile : attachments) {
						if (multipartFile.getSize() > 0) {
							AttachmentDataType attachmentType =
								new AttachmentDataType();

							attachmentType.setFilename(
								multipartFile.getOriginalFilename());
							attachmentType.setMimeType(
								multipartFile.getContentType());

							try {
								DataHandler handlerFile = new DataHandler(
									new ByteArrayDataSource(
										multipartFile.getInputStream(),
										"application/octet-stream"));

								attachmentType.setBlob(handlerFile);
							}
							catch (IOException exception) {
								logger.error(
									"Error occurred setting attachment:" +
										exception.getMessage());
							}

							attachmentTypes.add(attachmentType);
						}
					}

					updateTicketRequest.getAttachments(
					).addAll(
						attachmentTypes
					);
				}

				ticket.getServiceRequest(
				).getChargeback(
				).setDisputeAmount(
					form.getDisputeAmount()
				);
				ticket.getServiceRequest(
				).getChargeback(
				).setDocumentIndicator(
					form.isDocumentIndicator()
				);

				AdditionalTransactionInfo additionalTransactionInfo =
					setAdditionalTransactionInformationFromChargebackForm(
						ticket, form);

				ticket.getServiceRequest(
				).getChargeback(
				).setAdditionalTransactionInfo(
					additionalTransactionInfo
				);

				updateTicketRequest.setTicket(ticket);
				updateTicketResponse = serviceRequestService.updateTicket(
					updateTicketRequest);

				if (null == updateTicketResponse) {
					logger.error(
						"callUpdateServiceRequest - Update chargeback response is null.");
					xmlMessage.addComment("response", "Response is null");
					bindingResult.rejectValue(
						"errorMsg", "chargeback.submission.error");
				}
				else if (null == updateTicketResponse.getHeader()) {
					logger.error(
						"callUpdateServiceRequest - Update chargeback response header is null.");
					xmlMessage.addComment(
						"response", "Response header is null");
					bindingResult.rejectValue(
						"errorMsg", "chargeback.submission.error");
				}
				else {
					statusCode = updateTicketResponse.getHeader(
					).getStatusCode();

					if (StringUtils.equalsIgnoreCase(
							Constants.STATUS_SUCCESS, statusCode)) {

						xmlMessage.addResult(
							"ticketId",
							Long.valueOf(
								updateTicketResponse.getTicket(
								).getId()
							).toString());
					}
					else {
						xmlMessage.addComment(
							"responseMessage",
							updateTicketResponse.getHeader(
							).getMessage());
					}
				}

				if (StringUtils.equalsIgnoreCase(
						Constants.STATUS_SUCCESS, statusCode)) {

					audit.success(
						response, request, header.getUserId(),
						AuditCategories.TICKETING_UPDATE, xmlMessage.toXml());
				}
				else {
					audit.fail(
						response, request, header.getUserId(),
						AuditCategories.TICKETING_UPDATE, xmlMessage.toXml());
				}
			}
		}
	}

	/**
	 *
	 * @param searchHeader
	 * @param reasonType
	 * @return
	 */
	private Map<Long, String> getAttributeMapForReasonType(
		SearchHeader searchHeader, long reasonType) {

		List<AttributesListType> attributeList =
			serviceRequestService.getAttributeList(reasonType, searchHeader);
		Map<Long, String> reasonMap = new TreeMap<>();

		for (AttributesListType attributes : attributeList) {
			reasonMap.put(attributes.getId(), attributes.getDescription());
		}

		return reasonMap;
	}

	private AdditionalTransactionInfo setAdditionalTransactionInformation(
		ChargebackForm chargebackForm, Transaction transaction,
		ServiceRequestRequestType requestType) {

		//additional fields to display : additionalTransactionInfo

		AdditionalTransactionInfo additionalTransactionInfo =
			requestType.getRequestType(
			).getChargeback(
			).getAdditionalTransactionInfo();

		if (null == additionalTransactionInfo) {
			additionalTransactionInfo = new AdditionalTransactionInfo();
		}

		additionalTransactionInfo.setMessageType(
			chargebackForm.getMessageType());
		additionalTransactionInfo.setFunctionCode(
			chargebackForm.getFunctionCode());
		additionalTransactionInfo.setFunctionCodeDescription(
			transaction.getFunctionCodeDescription());
		additionalTransactionInfo.setPosConditionCode(
			transaction.getPosConditionCode());
		additionalTransactionInfo.setPosConditionDescription(
			transaction.getPosConditionDescription());
		additionalTransactionInfo.setPosEntryModeCode(
			transaction.getPosEntryModeCode());
		additionalTransactionInfo.setPosEntryModeDescription(
			transaction.getPosEntryModeDescription());
		additionalTransactionInfo.setUCAFCollectionIndicator(
			transaction.getUCAFCollectionIndicator());
		additionalTransactionInfo.setUCAFCollectionIndicatorDescription(
			transaction.getUCAFCollectionIndicatorDescription());
		additionalTransactionInfo.setElectronicCommerceIndciator(
			transaction.getElectronicCommerceIndciator());
		additionalTransactionInfo.setCentralSiteBusinessDate(
			transaction.getCentralSiteBusinessDate());
		additionalTransactionInfo.setChipCardPresent(
			transaction.getChipCardPresent());
		additionalTransactionInfo.setTrack2DataServiceCode(
			transaction.getTrack2DataServiceCode());

		return additionalTransactionInfo;
	}

	private AdditionalTransactionInfo
		setAdditionalTransactionInformationFromChargebackForm(
			TicketType ticket, ChargebackForm form) {

		AdditionalTransactionInfo additionalTransactionInfo =
			ticket.getServiceRequest(
			).getChargeback(
			).getAdditionalTransactionInfo();

		if (null == additionalTransactionInfo) {
			additionalTransactionInfo = new AdditionalTransactionInfo();
		}

		additionalTransactionInfo.setMessageType(form.getMessageType());
		additionalTransactionInfo.setFunctionCode(form.getFunctionCode());
		additionalTransactionInfo.setFunctionCodeDescription(
			form.getFunctionCodeDescription());
		additionalTransactionInfo.setPosConditionCode(
			form.getPosConditionCode());
		additionalTransactionInfo.setPosConditionDescription(
			form.getPosConditionDescription());
		additionalTransactionInfo.setPosEntryModeCode(
			form.getPosEntryModeCode());
		additionalTransactionInfo.setPosEntryModeDescription(
			form.getPosEntryModeDescription());
		additionalTransactionInfo.setUCAFCollectionIndicator(
			form.getUCAFCollectionIndicator());
		additionalTransactionInfo.setUCAFCollectionIndicatorDescription(
			form.getUCAFCollectionIndicatorDescription());
		additionalTransactionInfo.setElectronicCommerceIndciator(
			form.getElectronicCommerceIndciator());
		additionalTransactionInfo.setCentralSiteBusinessDate(
			form.getCentralSiteBusinessDate());
		additionalTransactionInfo.setChipCardPresent(form.getChipCardPresent());
		additionalTransactionInfo.setTrack2DataServiceCode(
			form.getTrack2DataServiceCode());

		return additionalTransactionInfo;
	}

	private Map<Long, String> setDisputeTypeMap(
		Transaction transaction,
		String outstandingMasterCardDisputeRetrievalRequest,
		String outstandingMasterCardDisputeFirstChargeback,
		String outstandingMasterCardDisputeArbitrationChargeback,
		String outstandingMasterCardDisputeReportFraud,
		Map<Long, String> cbTypeMap) {

		//1240  is equivalent to MasterCard MTI 1240

		if (Constants.MASTERCARD_PRESENTMENT_MESSAGE_TYPE_TRANSACTION_LOG.
				equals(transaction.getMessageType())) {

			if (FunctionCode.FIRST_PRESENTMENT.getValue(
				).equals(
					transaction.getFunctionCode()
				)) {

				logger.debug(
					" This is a first presentment, eligible to raise a first chargeback or fraud report");
				//remove arbitration charge back from drop down
				cbTypeMap.remove(
					AttributesListId.MASTERCARD_ARBITRATION_CHARGEBACK.
						getValue());
			}
			else if (FunctionCode.SECOND_PRESENTMENT_FULL.getValue(
					).equals(
						transaction.getFunctionCode()
					)) {

				logger.debug(
					" This is a second presentment full , eligible to raise a arbitration chargeback");
				//remove first charge back from drop down
				cbTypeMap.remove(
					AttributesListId.MASTERCARD_FIRST_CHARGEBACK.getValue());
				//remove report fraud from drop down
				cbTypeMap.remove(
					AttributesListId.MASTERCARD_REPORT_FRAUD.getValue());
			}
			else if (FunctionCode.SECOND_PRESENTMENT_PARTIAL.getValue(
					).equals(
						transaction.getFunctionCode()
					)) {

				logger.debug(
					" This is a second presentment partial, eligible to raise a arbitration chargeback");
				//remove first charge back from drop down
				cbTypeMap.remove(
					AttributesListId.MASTERCARD_FIRST_CHARGEBACK.getValue());
				//remove report fraud from drop down
				cbTypeMap.remove(
					AttributesListId.MASTERCARD_REPORT_FRAUD.getValue());
			}
		}

		//restrict multiple dispute	submission

		if (FLAG_TRUE.equalsIgnoreCase(
				outstandingMasterCardDisputeRetrievalRequest)) {

			logger.debug("There is already a retrieval request exists");
			cbTypeMap.remove(
				AttributesListId.MASTERCARD_RETRIEVAL_REQUEST.getValue());
		}

		if (FLAG_TRUE.equalsIgnoreCase(
				outstandingMasterCardDisputeFirstChargeback)) {

			logger.debug("There is already a first chargeback exists");
			cbTypeMap.remove(
				AttributesListId.MASTERCARD_FIRST_CHARGEBACK.getValue());
		}

		if (FLAG_TRUE.equalsIgnoreCase(
				outstandingMasterCardDisputeArbitrationChargeback)) {

			logger.debug("There is already a arbitration chargeback exists");
			cbTypeMap.remove(
				AttributesListId.MASTERCARD_ARBITRATION_CHARGEBACK.getValue());
		}

		if (FLAG_TRUE.equalsIgnoreCase(
				outstandingMasterCardDisputeReportFraud)) {

			logger.debug("There is already a report fraud exists");
			cbTypeMap.remove(
				AttributesListId.MASTERCARD_REPORT_FRAUD.getValue());
		}

		return cbTypeMap;
	}

	/**
	 * Create the ServiceRequestRequestType Object to add to the database.
	 *
	 * @param form
	 *            <code>ServiceRequestForm</code>
	 * @return ServiceRequestRequestType
	 */
	private ServiceRequestRequestType setupRequestDataForAddServiceRequest(
			ChargebackForm form, User user, Transaction transaction)
		throws Exception {

		logger.debug("setupRequestDataForAddServiceRequest - start");
		ServiceRequestRequestType request = null;

		if (null != form) {
			request = new ServiceRequestRequestType();

			// Contact Information section

			if (null != user) {
				ContactType contact = new ContactType();

				if (StringUtils.isNotBlank(user.getFirstName())) {
					contact.setFirstName(user.getFirstName());
				}

				if (StringUtils.isNotBlank(user.getLastName())) {
					contact.setSurname(user.getLastName());
				}

				if (StringUtils.isNotBlank(user.getEmailAddress())) {
					contact.setEmail(user.getEmailAddress());
				}

				UserUtilImpl userUtil = new UserUtilImpl();

				String phoneNo = userUtil.retrieveBusinessPhoneNumberForUser(
					user);

				if (StringUtils.isNotBlank(phoneNo)) {
					contact.setPhoneNo(phoneNo);
				}

				String clientName = "";
				List<Organization> organisations = user.getOrganizations();

				if ((organisations != null) && (organisations.size() > 0)) {
					for (Organization org : organisations) {
						if ((clientName != null) && (clientName.length() > 0)) {
							clientName += ", " + org.getName();
						}
						else {
							clientName = org.getName();
						}
					}

					contact.setOrganisation(clientName);
				}

				request.setContact(contact);
			}

			TransactionType transactionType = setupTransactionForServiceRequest(
				transaction);

			RequestTypeType requestType =
				ChargebacksUtility.setupChargebackInformationForServiceRequest(
					form);

			if (StringUtils.isNotBlank(form.getNote())) {
				transactionType.setComments(form.getNote());
			}

			request.setRequestType(requestType);
			request.setTransaction(transactionType);

			List<MultipartFile> attachments = form.getRequestAttachments();

			if ((null != attachments) && (attachments.size() > 0)) {
				List<AttachmentDataType> attachmentTypes = new ArrayList<>();

				for (MultipartFile multipartFile : attachments) {
					if (multipartFile.getSize() > 0) {
						AttachmentDataType attachmentType =
							new AttachmentDataType();

						attachmentType.setFilename(
							multipartFile.getOriginalFilename());
						attachmentType.setMimeType(
							multipartFile.getContentType());

						try {
							DataHandler handlerFile = new DataHandler(
								new ByteArrayDataSource(
									multipartFile.getInputStream(),
									"application/octet-stream"));

							attachmentType.setBlob(handlerFile);
						}
						catch (IOException exception) {
							logger.error(
								"Error occurred setting attachment:" +
									exception.getMessage());
						}

						attachmentTypes.add(attachmentType);
					}
				}

				request.getAttachments(
				).addAll(
					attachmentTypes
				);
			}
		}

		logger.debug("setupRequestDataForAddServiceRequest - end");

		return request;
	}

	/**
	 *
	 * @param searchHeader
	 * @param request
	 */
	private void setupRequestObjects(
		SearchHeader searchHeader, PortletRequest request,
		Transaction transaction, String editTicket, String type) {

		HttpServletRequest requestInsideThePortlet =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = requestInsideThePortlet.getSession();
		String outstandingMasterCardDisputeRetrievalRequest = null;
		String outstandingMasterCardDisputeFirstChargeback = null;
		String outstandingMasterCardDisputeArbitrationChargeback = null;
		String outstandingMasterCardDisputeReportFraud = null;
		String outstandingMasterCardDisputeRetrievalRequestReq =
			PortalUtil.getOriginalServletRequest(
				requestInsideThePortlet
			).getParameter(
				"outstandingMasterCardDisputeRetrievalRequest"
			);
		String outstandingMasterCardDisputeFirstChargebackReq =
			PortalUtil.getOriginalServletRequest(
				requestInsideThePortlet
			).getParameter(
				"outstandingMasterCardDisputeFirstChargeback"
			);
		String outstandingMasterCardDisputeArbitrationChargebackReq =
			PortalUtil.getOriginalServletRequest(
				requestInsideThePortlet
			).getParameter(
				"outstandingMasterCardDisputeArbitrationChargeback"
			);
		String outstandingMasterCardDisputeReportFraudReq =
			PortalUtil.getOriginalServletRequest(
				requestInsideThePortlet
			).getParameter(
				"outstandingMasterCardDisputeReportFraud"
			);
		String outstandingMasterCardDisputeRetrievalRequestSes =
			(String)session.getAttribute(
				"outstandingMasterCardDisputeRetrievalRequest");
		String outstandingMasterCardDisputeFirstChargebackSes =
			(String)session.getAttribute(
				"outstandingMasterCardDisputeFirstChargeback");
		String outstandingMasterCardDisputeArbitrationChargebackSes =
			(String)session.getAttribute(
				"outstandingMasterCardDisputeArbitrationChargeback");
		String outstandingMasterCardDisputeReportFraudSes =
			(String)session.getAttribute(
				"outstandingMasterCardDisputeReportFraud");

		//identify outstanding service requests
		//RR

		if (StringUtils.isNotBlank(
				outstandingMasterCardDisputeRetrievalRequestReq)) {

			outstandingMasterCardDisputeRetrievalRequest =
				outstandingMasterCardDisputeRetrievalRequestReq;
		}
		else if (StringUtils.isNotBlank(
					outstandingMasterCardDisputeRetrievalRequestSes)) {

			outstandingMasterCardDisputeRetrievalRequest =
				outstandingMasterCardDisputeRetrievalRequestSes;
		}

		session.setAttribute(
			"outstandingMasterCardDisputeRetrievalRequest",
			outstandingMasterCardDisputeRetrievalRequest);
		//FCB

		if (StringUtils.isNotBlank(
				outstandingMasterCardDisputeFirstChargebackReq)) {

			outstandingMasterCardDisputeFirstChargeback =
				outstandingMasterCardDisputeFirstChargebackReq;
		}
		else if (StringUtils.isNotBlank(
					outstandingMasterCardDisputeFirstChargebackSes)) {

			outstandingMasterCardDisputeFirstChargeback =
				outstandingMasterCardDisputeFirstChargebackSes;
		}

		session.setAttribute(
			"outstandingMasterCardDisputeFirstChargeback",
			outstandingMasterCardDisputeFirstChargeback);
		//ACB

		if (StringUtils.isNotBlank(
				outstandingMasterCardDisputeArbitrationChargebackReq)) {

			outstandingMasterCardDisputeArbitrationChargeback =
				outstandingMasterCardDisputeArbitrationChargebackReq;
		}
		else if (StringUtils.isNotBlank(
					outstandingMasterCardDisputeArbitrationChargebackSes)) {

			outstandingMasterCardDisputeArbitrationChargeback =
				outstandingMasterCardDisputeArbitrationChargebackSes;
		}

		session.setAttribute(
			"outstandingMasterCardDisputeArbitrationChargeback",
			outstandingMasterCardDisputeArbitrationChargeback);
		//RF

		if (StringUtils.isNotBlank(
				outstandingMasterCardDisputeReportFraudReq)) {

			outstandingMasterCardDisputeReportFraud =
				outstandingMasterCardDisputeReportFraudReq;
		}
		else if (StringUtils.isNotBlank(
					outstandingMasterCardDisputeReportFraudSes)) {

			outstandingMasterCardDisputeReportFraud =
				outstandingMasterCardDisputeReportFraudSes;
		}

		session.setAttribute(
			"outstandingMasterCardDisputeReportFraud",
			outstandingMasterCardDisputeReportFraud);

		logger.debug(
			"setupRequestObjects - outstandingMasterCardDisputeRetrievalRequest=" +
				outstandingMasterCardDisputeRetrievalRequest);
		logger.debug(
			"setupRequestObjects - outstandingMasterCardDisputeFirstChargeback=" +
				outstandingMasterCardDisputeFirstChargeback);
		logger.debug(
			"setupRequestObjects - outstandingMasterCardDisputeArbitrationChargeback=" +
				outstandingMasterCardDisputeArbitrationChargeback);
		logger.debug(
			"setupRequestObjects - outstandingMasterCardDisputeReportFraud=" +
				outstandingMasterCardDisputeReportFraud);

		//set Dispute Type drop down values
		Map<Long, String> cbTypeMap = getAttributeMapForReasonType(
			searchHeader, AttributesTypeId.MASTERCARD_DISPUTE_TYPES.getValue());

		if (FLAG_TRUE.equals(editTicket)) {
			if (StringUtils.isNotBlank(type)) {
				String value = cbTypeMap.get(Long.valueOf(type));
				cbTypeMap.clear();
				cbTypeMap.put(Long.valueOf(type), value);
			}
		}
		else {
			cbTypeMap = setDisputeTypeMap(
				transaction, outstandingMasterCardDisputeRetrievalRequest,
				outstandingMasterCardDisputeFirstChargeback,
				outstandingMasterCardDisputeArbitrationChargeback,
				outstandingMasterCardDisputeReportFraud, cbTypeMap);
		}

		request.setAttribute(
			"arbitrationChargebackReasonCodesMap",
			getAttributeMapForReasonType(
				searchHeader,
				AttributesTypeId.MASTERCARD_ARBITRATION_CHARGEBACK_REASON_CODES.
					getValue()));
		request.setAttribute("cbType", cbTypeMap);
		request.setAttribute(
			"firstChargebackReasonCodesMap",
			getAttributeMapForReasonType(
				searchHeader,
				AttributesTypeId.MASTERCARD_FIRST_CHARGEBACK_REASON_CODES.
					getValue()));
		request.setAttribute(
			"retrievalRequestReasonCodesMap",
			getAttributeMapForReasonType(
				searchHeader,
				AttributesTypeId.MASTERCARD_RETRIEVAL_REQUEST_REASON_CODES.
					getValue()));
	}

	/**
	 *
	 * @param trans
	 * @return
	 * @throws Exception
	 */
	private TransactionType setupTransactionForServiceRequest(Transaction trans)
		throws Exception {

		TransactionType transaction = new TransactionType();

		transaction.setAcquirerName(trans.getAcquirerName());
		transaction.setIssuerName(trans.getIssuerName());
		transaction.setMaskedPan(trans.getMaskedPan());
		transaction.setPosCondition(trans.getAtmPos());

		transaction.setAcquirerId(trans.getAcquirerId());

		try {
			transaction.setTrlId(Long.valueOf(trans.getTransactionId()));
		}
		catch (NumberFormatException nfe) {
			logger.error(
				"Error parsing TrlId cannot be parsed. TrlId is " +
					trans.getTransactionId());

			throw new Exception(nfe);
		}

		try {
			transaction.setIssuerId(Long.valueOf(trans.getIssuerId()));
		}
		catch (NumberFormatException nfe) {
			logger.error(
				"Error parsing IssuerId cannot be parsed. IssuerId is " +
					trans.getIssuerId());

			throw new Exception(nfe);
		}

		if (StringUtils.isNotBlank(trans.getStan())) {
			String stanString = trans.getStan();

			try {
				if (stanString.contains("/")) {
					String stan = stanString.substring(
						0, stanString.indexOf("/")
					).trim();

					transaction.setStan(Long.valueOf(stan));

					String advStan = stanString.substring(
						stanString.indexOf("/") + 1
					).trim();

					transaction.setAdvStan(advStan);
				}
				else {
					transaction.setStan(Long.valueOf(stanString));
				}
			}
			catch (NumberFormatException nfe) {
				logger.error(
					"Error parsing Stan cannot be parsed. Stan is " +
						stanString);

				throw new Exception(nfe);
			}
		}

		transaction.setTerminalId(trans.getTerminalId());
		transaction.setVisaId(trans.getVisaTransactionId());
		transaction.setFunctionCode(trans.getFunctionCode());

		logger.debug(
			"setupServiceRequestForTransaction - cardHolderAmount is: " +
				trans.getCardholderAmount());

		String cardHolderAmount = Utility.removeAndCovertInFloat(
			trans.getCardholderAmount());

		BigDecimal cardAmount = Utility.createBigDecimalAmountFromString(
			cardHolderAmount);

		if (null != cardAmount) {
			logger.debug(
				"setupServiceRequestForTransaction - getCardholderAmount: " +
					cardAmount);
			transaction.setBillingAmt(cardAmount);
		}

		logger.debug(
			"setupServiceRequestForTransaction - transactionAmount is: " +
				trans.getTransactionAmount());
		String transactionAmount = Utility.removeAndCovertInFloat(
			trans.getTransactionAmount());

		BigDecimal transAmount = Utility.createBigDecimalAmountFromString(
			transactionAmount);

		if (null != transAmount) {
			transaction.setTransactionAmt(transAmount);
			transaction.setTransactionCcy(trans.getTransactionAmountCurrency());
		}

		logger.debug(
			"trans.getTransactionLocalDate()=" +
				trans.getTransactionLocalDate());
		XMLGregorianCalendar localXmlCal = null;

		try {
			logger.debug(
				"trans.getTransactionLocalDateTimeZone()=" +
					trans.getTransactionLocalDateTimeZone());
			localXmlCal = Utility.createXmlGregorianCalendarFromString(
				trans.getTransactionLocalDateTimeZone(), "T",
				Constants.DATE_FORMAT_LONG, Boolean.TRUE);
		}
		catch (Exception e) {
			localXmlCal = Utility.createXmlGregorianCalendarFromString(
				trans.getTransactionLocalDate(),
				Constants.DATE_TIME_FORMAT_24HR, Boolean.TRUE);
		}

		logger.debug("localXmlCal=" + localXmlCal);
		transaction.setTrlLocalDate(localXmlCal);

		XMLGregorianCalendar businessXmlCal =
			Utility.createXmlGregorianCalendarFromString(
				trans.getBusinessDate(), "yyyyMMdd", Boolean.FALSE);

		transaction.setTrlBusinessDate(businessXmlCal);

		transaction.setMemberName(trans.getMemberName());
		transaction.setMemberNo(trans.getMemberNumber());

		return transaction;
	}

	private static Auditor audit = Auditor.getInstance();
	private static Logger logger = Logger.getLogger(ChargebackController.class);

	@Autowired
	@Qualifier("chargebackProperties")
	private ChargebackProperties chargebackProperties;

	@Autowired
	@Qualifier("chargeBackValidator")
	private ChargeBackValidator chargeBackValidator;

	private final ServiceRequestService serviceRequestService = CuscalServiceLocator.getService(
		ServiceRequestServiceImpl.class.getName());

	private TicketType ticketType;

}