package au.com.cuscal.chargeback.controller;

import static javax.portlet.PortletSession.APPLICATION_SCOPE;

import au.com.cuscal.chargeback.common.ChargebacksUtility;
import au.com.cuscal.chargeback.common.Constants;
import au.com.cuscal.chargeback.common.VisaChargebackProperties;
import au.com.cuscal.chargeback.forms.ChargebackForm;
import au.com.cuscal.chargeback.validator.VisaChargeBackValidator;
import au.com.cuscal.common.framework.AuditWebServicesUtil;
import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.common.framework.dxp.service.request.controller.AbstractServiceRequestController;
import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.common.framework.dxp.service.request.services.ServiceRequestServiceImpl;
import au.com.cuscal.common.framework.service.request.domain.Transaction;
import au.com.cuscal.connect.util.resource.dxp.UserUtilImpl;
import au.com.cuscal.framework.audit.category.AuditCategories;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.audit.xml.XmlMessage;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.AttachmentDataType;
import au.com.cuscal.framework.webservices.selfservice.AttributesListType;
import au.com.cuscal.framework.webservices.selfservice.ContactType;
import au.com.cuscal.framework.webservices.selfservice.GetTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.RequestTypeType;
import au.com.cuscal.framework.webservices.selfservice.ServiceRequestRequestType;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.selfservice.TransactionType;
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

@Controller("visaChargebackController")
@RequestMapping(value = "VIEW")
public class VisaChargebackController extends AbstractServiceRequestController {

	/**
	 *
	 * @param searchHeader
	 * @return
	 */
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
	 * Render method to send the cancellation info jsp to the ajax method. This is a temporary fix for the jsp being too big.
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(params = "render=cancellationInfo")
	public String cancellationInfoRender(
		RenderRequest request, RenderResponse response) {

		logger.debug("elaborationInfo - start");

		HttpSession session = getSession(request);

		String ticketId = request.getParameter("ticketId");
		ChargebackForm form = (ChargebackForm)session.getAttribute(
			"edittedChargebackForm");

		// We do this to make sure that we do not override the user's changes if there are validation errors when submitting the form.

		if (null == form) {
			logger.debug("cancellationInfoRender - ticketId is: " + ticketId);

			// Check if we could not get the ticketId or the header for the request is null.

			if (StringUtils.isNotBlank(ticketId)) {
				SearchHeader searchHeader = getSearchHeaderData(request);

				TktRequestHeader header = convertSearchHeaderToTktHeader(
					searchHeader);

				GetTicketRequest getTicketRequest = new GetTicketRequest();

				if (null != header) {
					getTicketRequest.setHeader(header);
					getTicketRequest.setTicketId(ticketId);

					GetTicketResponse resp =
						serviceRequestService.ticketDetails(getTicketRequest);

					if ((null != resp) && (null != resp.getTicket())) {
						form = new ChargebackForm(resp.getTicket());
					}
					else {
						logger.error(
							"cancellationInfoRender - could not get service request details for ticket: " +
								ticketId +
									". Returning an empty Cancellation form.");
						form = new ChargebackForm();
					}

					request.setAttribute(
						"cancellationInformation", form.getCancellationInfo());
				}
				else {
					logger.error(
						"cancellationInfoRender - Could not get the header details for the request. Returning an empty cancellation info section.");
					form = new ChargebackForm();
				}
			}
			else {

				// We don't have the ticketId from the request.

				form = new ChargebackForm();
			}
		}
		else {

			// We didn't get the form from the session.

			form = new ChargebackForm();
		}

		request.setAttribute(
			"cancellationInformation", form.getCancellationInfo());

		return "cancellation-info-section";
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

		try {
			ObjectInputStream o = new ObjectInputStream(b);

			Object sessionObject = o.readObject();

			if (sessionObject instanceof Transaction) {
				Transaction transaction = (Transaction)sessionObject;

				request.setAttribute(Constants.TRANSACTION_DETAIL, transaction);

				// remove the serialised object and add the unserialised object

				portletSession.removeAttribute(
					Constants.TRANSACTION_DETAIL_BYTES,
					PortletSession.APPLICATION_SCOPE);
				session.removeAttribute(Constants.TRANSACTION_DETAIL_BYTES);
				session.setAttribute(Constants.TRANSACTION_DETAIL, transaction);
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

		SearchHeader searchHeader = getSearchHeaderData(request);

		if (null == searchHeader) {
			logger.error("navigateToChargeBack - SearchHeader is null.");

			return Constants.ERROR_PAGE;
		}

		logger.debug("navigateToChargeBack - end");
		setupRequestObjects(searchHeader, request);

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

		/*SearchHeader searchHeader = getSearchHeaderData(request);
		TktRequestHeader header = Utility
				.convertSearchHeaderToTktHeader(searchHeader);

		String transId = (String) request.getAttribute("transactionId");
		String date = (String) request.getAttribute("businessDate");

		List<TicketDetails> ticketList = getTickets(transId, date, header);

		try {
			addLinkToCuscalScreen(request, ticketList);
		} catch (Exception e) {
			logger.error("showConfirmationPage - Could not get the URL for the self service page");
		}

		request.setAttribute("tickets", ticketList);*/

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

		try {
			String requestId = request.getParameter("ticketId");

			if (StringUtils.isBlank(requestId)) {
				requestId = (String)request.getAttribute("ticketId");
			}

			HttpSession session = getSession(request);

			Transaction transaction = (Transaction)session.getAttribute(
				Constants.TRANSACTION_DETAIL);

			logger.debug("showHomePage - requestId: " + requestId);
			SearchHeader searchHeader = getSearchHeaderData(request);

			if (StringUtils.isBlank(requestId)) {
				if (null == transaction) {
					logger.error(
						"showHomePage - Could not get transaction details or ticket ID.");

					return Constants.ERROR_PAGE;
				}

				if (!modelMap.containsAttribute("chargebackForm")) {
					modelMap.addAttribute(
						"chargebackForm", new ChargebackForm());
				}

				if ((null != searchHeader) &&
					StringUtils.isNotBlank(searchHeader.getUserOrgId())) {

					setupRequestObjects(searchHeader, request);

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

			if ((null != resp) && (null != resp.getTicket())) {
				if ((null != resp.getTicket(
					).getTransactions()) ||
					(resp.getTicket(
					).getTransactions(
					).size() > 0)) {

					transaction =
						ChargebacksUtility.convertTransactionTypeToTransaction(
							resp.getTicket(
							).getTransactions(
							).get(
								0
							));

					request.setAttribute(
						Constants.TRANSACTION_DETAIL, transaction);
					session.setAttribute(
						Constants.TRANSACTION_DETAIL, transaction);

					request.setAttribute("updateTicket", Boolean.TRUE);

					ChargebackForm chargebackForm = new ChargebackForm(
						resp.getTicket());

					setTicketType(resp.getTicket());

					if (!modelMap.containsAttribute("chargebackForm")) {
						modelMap.addAttribute("chargebackForm", chargebackForm);
					}

					request.removeAttribute("ticketId");

					if ((null != searchHeader) &&
						StringUtils.isNotBlank(searchHeader.getUserOrgId())) {

						setupRequestObjects(searchHeader, request);

						return Constants.HOMEPAGE;
					}

					return Constants.ERROR_PAGE;
				}

				return Constants.ERROR_PAGE;
			}

			logger.error(
				"showHomePage - could not get service request details for ticket: " +
					requestId);

			return Constants.ERROR_PAGE;
		}
		catch (Exception e) {
			logger.error("showHomePage - " + e.getMessage(), e);

			return Constants.ERROR_PAGE;
		}
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

			logger.debug(
				"submitChargeback - before validating type: " +
					chargebackForm.getType());
			visaChargeBackValidator.validate(
				chargebackForm, bindingResult, visaChargebackProperties);

			Transaction transaction = (Transaction)session.getAttribute(
				Constants.TRANSACTION_DETAIL);

			if (null != transaction) {
				try {
					if (!bindingResult.hasErrors()) {
						callAddServiceRequest(
							chargebackForm, bindingResult, header, user,
							transaction, request, response);
					}

					session.setAttribute(
						"edittedChargebackForm", chargebackForm);
				}
				catch (Exception e) {
					bindingResult.rejectValue(
						"errorMsg", "chargeback.submission.error");
					logger.error("addServiceRequest - There are errors: ", e);
					session.setAttribute(
						"edittedChargebackForm", chargebackForm);
				}

				if (!bindingResult.hasErrors()) {
					request.setAttribute(Constants.ERRORS, Boolean.FALSE);
					session.removeAttribute("edittedChargebackForm");

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

						// Using Liferay's render to the success page.

						logger.warn(
							"submitChargeback - Could not redirect to the success page.");
						response.setRenderParameter(
							Constants.RENDER, Constants.CONFIRM);
					}
				}
				else {
					logger.error("submitChargeback - There are errors");
					request.setAttribute(Constants.ERRORS, Boolean.TRUE);
					session.setAttribute(
						"edittedChargebackForm", chargebackForm);
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

				// TODO: This solves the resubmit issue. But with this I cannot
				// use the set attribute.

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

		HttpSession session = getSession(request);

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
			visaChargeBackValidator.validate(
				chargebackForm, bindingResult, visaChargebackProperties);
			/*  */
			if (bindingResult.hasErrors()) {
				logger.error("updateChargeback - There are errors");
				request.setAttribute("ticketId", chargebackForm.getTicketId());
				request.setAttribute(Constants.ERRORS, Boolean.TRUE);
				session.setAttribute("edittedChargebackForm", chargebackForm);

				logger.debug("udpateChargeback - end");
			}
			else {
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
					session.removeAttribute("edittedChargebackForm");

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

				// These form types do not have Elaboration information So
				// setting that to null otherwise the form looks half empty.

				if (Constants.CHARGEBACK_WITHOUT_ELABORATIONS.contains(
						chargebackForm.getReasonCode())) {

					requestType.getRequestType(
					).getChargeback(
					).setElaborationInfo(
						null
					);
				}

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

				// These form types do not have Elaboration information So
				// setting that to null otherwise the form looks half empty.

				if (Constants.CHARGEBACK_WITHOUT_ELABORATIONS.contains(
						form.getReasonCode())) {

					ticket.getServiceRequest(
					).getChargeback(
					).setElaborationInfo(
						null
					);
				}

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
				else {
					if (null == updateTicketResponse.getHeader()) {
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

	/**
	 * We are not using this anymore.
	 *
	 * Find ServiceRequests for a particular transaction.
	 *
	 * @param header
	 * @return
	 */
	/*public List<TicketDetails> getTickets(String transactionId,
			String businessDate, TktRequestHeader header) {

		logger.debug("getTickets - start");
		List<TicketDetails> ticketDetailsList = null;
		FindServiceRequestResponse response = null;
		FindServiceRequestRequest request = new FindServiceRequestRequest();

		if (null != header && StringUtils.isNotBlank(transactionId)
				&& StringUtils.isNotBlank(businessDate)) {

			request.setHeader(header);
			request.setTransactionId(Long.valueOf(transactionId));
			logger.debug("getTickets - transactionId: " + transactionId);
			logger.debug("getTickets - businessDate: " + businessDate);
			XMLGregorianCalendar xmlCal = Utility
					.createXmlGregorianCalendarFromString(businessDate,
							"yyyyMMdd", Boolean.FALSE);

			if (null != xmlCal) {
				request.setBusinessDate(xmlCal);

				response = new FindServiceRequestResponse();
				response = serviceRequestService.findServiceRequest(request);

				if (null != response) {
					if (null != response.getHeader()) {
						String statusCode = response.getHeader()
								.getStatusCode();

						if (StringUtils.equalsIgnoreCase("SUCCESS", statusCode)) {
							ticketDetailsList = Util.decoratedServiceRequestList(
									response.getServiceRequests());

							logger.debug("getTickets - response tickets size: "
									+ ticketDetailsList.size());
						} else if (StringUtils.equalsIgnoreCase(
								Constants.RECORD_NOT_FOUND, statusCode)) {

							ticketDetailsList = new ArrayList<>();
							logger.debug("getTickets - Find tickets response: "
									+ statusCode);
						} else {
							logger.error("getTickets - WS Response status is not success: "
									+ statusCode);
						}
					} else {
						logger.error("getTickets - WS Response Header is null");
					}
				} else {
					logger.error("getTickets - WS Response is null");
				}
			}
		}

		logger.debug("getTickets - end");

		return ticketDetailsList;
	}*/

	/**
	 * Create a link to let the user head back to the Cuscal Ticketing page.
	 *
	 * @param request
	 * @param ticketList
	 */
	/*private void addLinkToCuscalScreen(RenderRequest request,
			List<TicketDetails> ticketList) {

		logger.debug("addLinkToCuscalScreen - start");

		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);

		PortletURL link = PortletURLFactoryUtil.create(
				PortalUtil.getHttpServletRequest(request),
				Constants.TICKETING_PORTLET_NAME, themeDisplay.getPlid(),
				PortletRequest.RENDER_PHASE);

		link.setParameter(Constants.RENDER, Constants.TICKET_DETAILS_RENDER);

		for (TicketDetails ticket : ticketList) {
			link.setParameter("i", String.valueOf(ticket.getTicketId()));

			String pageUrl = visaChargebackProperties
					.getVisaChargebackProperty()
					.getProperty("self.service.uri");

			ticket.setTicketLink(pageUrl + "?"
					+ StringUtils.substringAfter(link.toString(), "?"));

		}

		logger.debug("addLinkToCuscalScreen - end");
	}*/

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

	/*******************************************************/
	/**
	 *
	 * @param searchHeader
	 * @param request
	 */
	private void setupRequestObjects(
		SearchHeader searchHeader, PortletRequest request) {

		request.setAttribute(
			"cbAuthMap", getAttributeMapForReasonType(searchHeader, 16));
		request.setAttribute(
			"cbCancelledMap", getAttributeMapForReasonType(searchHeader, 18));
		request.setAttribute(
			"cbFraudMap", getAttributeMapForReasonType(searchHeader, 15));
		request.setAttribute(
			"cbNRFSMap", getAttributeMapForReasonType(searchHeader, 19));
		request.setAttribute(
			"cbProcessingErrorMap",
			getAttributeMapForReasonType(searchHeader, 17));
		request.setAttribute(
			"cbRFIMap", getAttributeMapForReasonType(searchHeader, 14));
		request.setAttribute(
			"cbType", getAttributeMapForReasonType(searchHeader, 13));

		request.setAttribute(
			"cardStatus", getAttributeMapForReasonType(searchHeader, 24));
		request.setAttribute(
			"certifications", getAttributeMapForReasonType(searchHeader, 20));
		request.setAttribute(
			"memberMessage", getAttributeMapForReasonType(searchHeader, 21));
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

			// TODO:Need to add the currency

			// transaction.setBillingCcy(trans
			// .getCardholderAmountCurrency());

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

		XMLGregorianCalendar localXmlCal =
			Utility.createXmlGregorianCalendarFromString(
				trans.getTransactionLocalDate(), Constants.DATE_FORMAT,
				Boolean.FALSE);

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
	private static Logger logger = Logger.getLogger(
		VisaChargebackController.class);

	/**
	 * Service used to fetch data
	 */
	private final ServiceRequestServiceImpl serviceRequestService = CuscalServiceLocator.getService(
		ServiceRequestServiceImpl.class.getName());

	private TicketType ticketType;

	@Autowired
	@Qualifier("visaChargebackProperties")
	private VisaChargebackProperties visaChargebackProperties;

	@Autowired
	@Qualifier("visaChargeBackValidator")
	private VisaChargeBackValidator visaChargeBackValidator;

}