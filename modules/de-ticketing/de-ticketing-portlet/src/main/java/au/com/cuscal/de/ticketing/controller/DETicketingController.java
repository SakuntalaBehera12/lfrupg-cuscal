//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.controller;

import au.com.cuscal.common.framework.dxp.service.request.controller.AbstractServiceRequestController;
import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.common.framework.dxp.service.request.services.ServiceRequestServiceImpl;
import au.com.cuscal.de.ticketing.common.CommonUtil;
import au.com.cuscal.de.ticketing.common.Constants;
import au.com.cuscal.de.ticketing.common.DEProperties;
import au.com.cuscal.de.ticketing.domain.DEListBoxData;
import au.com.cuscal.de.ticketing.forms.AttributesInformation;
import au.com.cuscal.de.ticketing.forms.RequestContactInformation;
import au.com.cuscal.de.ticketing.forms.RequestTypeInformation;
import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;
import au.com.cuscal.de.ticketing.services.CuscalTicketingService;
import au.com.cuscal.de.ticketing.validator.DEClaimValidator;
import au.com.cuscal.de.ticketing.validator.DEMistakenPaymentValidator;
import au.com.cuscal.de.ticketing.validator.DERecallValidator;
import au.com.cuscal.de.ticketing.validator.DETraceValidator;
import au.com.cuscal.de.ticketing.validator.DETransactionSearchValidator;
import au.com.cuscal.de.ticketing.validator.OfiDEClaimValidator;
import au.com.cuscal.de.ticketing.validator.OfiDEMistakenPaymentValidator;
import au.com.cuscal.de.ticketing.validator.OfiDERecallValidator;
import au.com.cuscal.de.ticketing.validator.OfiDETraceValidator;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.selfservice.GetTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;

import java.util.List;
import java.util.Properties;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("deController")
@RequestMapping({"VIEW"})
public class DETicketingController extends AbstractServiceRequestController {

	@Autowired
	public DETicketingController(
		@Qualifier("deClaimValidator") DEClaimValidator deClaimValidator,
		@Qualifier(Constants.CUSCAL_TICKETING_SERVICE) CuscalTicketingService cuscalTicketingService,
		@Qualifier("deMistakenPaymentValidator") DEMistakenPaymentValidator deMistakenPaymentValidator,
		@Qualifier("deProperties") DEProperties deProperties,
		@Qualifier("deRecallValidator") DERecallValidator deRecallValidator,
		@Qualifier("deTraceValidator") DETraceValidator deTraceValidator,
		@Qualifier("deTransactionSearchValidator") DETransactionSearchValidator deTransactionSearchValidator,
		@Qualifier("ofiDEClaimValidator") OfiDEClaimValidator ofiDEClaimValidator,
		@Qualifier("ofiDEMistakenPaymentValidator") OfiDEMistakenPaymentValidator ofiDEMistakenPaymentValidator,
		@Qualifier("ofiDERecallValidator") OfiDERecallValidator ofiDERecallValidator,
		@Qualifier("ofiDETraceValidator") OfiDETraceValidator ofiDETraceValidator) {

		this.orgIdOverride = null;
		this.deClaimValidator = deClaimValidator;
		this.cuscalTicketingService = cuscalTicketingService;
		this.deMistakenPaymentValidator = deMistakenPaymentValidator;
		this.deProperties = deProperties;
		this.deRecallValidator = deRecallValidator;
		this.deTraceValidator = deTraceValidator;
		this.deTransactionSearchValidator = deTransactionSearchValidator;
		this.ofiDEClaimValidator = ofiDEClaimValidator;
		this.ofiDEMistakenPaymentValidator = ofiDEMistakenPaymentValidator;
		this.ofiDERecallValidator = ofiDERecallValidator;
		this.ofiDETraceValidator = ofiDETraceValidator;
	}

	@ModelAttribute("serviceRequestForm")
	public ServiceRequestForm getCommandObjectServiceRequestForm() {
		return new ServiceRequestForm();
	}

	public Long getOrgIdOverride() {
		return this.orgIdOverride;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.RESET_ACTION
	)
	public String reset(
		final RenderRequest request, final RenderResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug("reset de transaction search - start");

		String page = "";

		final String formId = request.getParameter(Constants.FORM_ID);

		if (StringUtils.isNotBlank(formId)) {
			request.setAttribute(Constants.FORM_ID, (Object)formId);
			request.removeAttribute(Constants.TRANS);

			if (formId.equalsIgnoreCase(Constants.FORM_ID_DE_TRACE) ||
				formId.equalsIgnoreCase(Constants.FORM_ID_DE_RECALL) ||
				formId.equalsIgnoreCase(
					Constants.FORM_ID_DE_MISTAKEN_PAYMENT) ||
				formId.equalsIgnoreCase(Constants.FORM_ID_DE_CLAIMS)) {

				page = Constants.DE_TRANSACTION_SEARCH_PAGE;
			}
			else {
				page = Constants.ERROR_PAGE;
			}
		}
		else {
			page = Constants.ERROR_PAGE;
		}

		DETicketingController.logger.debug(
			(Object)"reset de transaction search - end");

		return page;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.DE_TRANSACTION_SEARCH_ACTION
	)
	public String searchTransaction(
		final RenderRequest request, final RenderResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug(
			(Object)"spx transaction search - start");
		final TktRequestHeader header = this.createRequestHeader(
			(PortletRequest)request);

		if (null == header) {
			request.setAttribute(
				Constants.SESSION_TIMEOUT, (Object)Boolean.TRUE);

			return Constants.ERROR_PAGE;
		}

		String page = "";
		final String formId = StringUtils.trimToEmpty(
			request.getParameter(Constants.FORM_ID));

		if (StringUtils.isBlank(formId)) {
			DETicketingController.logger.error(
				(Object)"searchTransaction - could not get form id.");

			return Constants.ERROR_PAGE;
		}

		request.setAttribute(Constants.FORM_ID, (Object)formId);
		serviceRequestForm.setFormId(formId);
		this.deTransactionSearchValidator.validate(
			serviceRequestForm, (Errors)bindingResult);

		if (bindingResult.hasErrors()) {
			DETicketingController.logger.debug(
				(Object)
					"transaction search request action - there are validation errors");
			page = Constants.DE_TRANSACTION_SEARCH_PAGE;
		}
		else {
			final Properties prop = this.deProperties.getDeProperty();
			final RequestTypeInformation requestTypeInformation =
				new RequestTypeInformation();
			serviceRequestForm.setRequestTypeInformation(
				requestTypeInformation);
			final RequestContactInformation contactInformation =
				CommonUtil.getUserDetailsFromRequest(
					(PortletRequest)request, this.deProperties);
			serviceRequestForm.getAttributesInformation(
			).setBsb(
				contactInformation.getBsb()
			);
			List<AttributesInformation> trans = null;

			try {
				if (formId.equalsIgnoreCase(Constants.FORM_ID_DE_TRACE)) {
					trans = this.callSpxTransactionSearch(
						PortletContext.newContext(
							(PortletResponse)response, (PortletRequest)request),
						serviceRequestForm, bindingResult, header);
				}
				else if (formId.equalsIgnoreCase(Constants.FORM_ID_DE_RECALL) ||
						 formId.equalsIgnoreCase(
							 Constants.FORM_ID_DE_MISTAKEN_PAYMENT)) {

					trans = this.callSpxPaymentTransactionSearch(
						PortletContext.newContext(
							(PortletResponse)response, (PortletRequest)request),
						serviceRequestForm, bindingResult, header);
				}
				else if (formId.equalsIgnoreCase(Constants.FORM_ID_DE_CLAIMS)) {
					trans = this.callSpxClaimTransactionSearch(
						PortletContext.newContext(
							(PortletResponse)response, (PortletRequest)request),
						serviceRequestForm, bindingResult, header);
				}
			}
			catch (Exception e1) {
				DETicketingController.logger.error(
					(Object)"addServiceRequest - There are errors");
			}

			if (null != trans) {
				final DEListBoxData deListBoxData =
					CommonUtil.populateDEListBoxData(this.deProperties);
				final HttpSession session = this.getSessionFromRequest(
					(PortletRequest)request);

				for (final AttributesInformation tran : trans) {
					CommonUtil.mapSpxTransaction(tran, deListBoxData);
				}

				session.setAttribute(Constants.DE_TRANS, (Object)trans);
				request.setAttribute(Constants.TRANS, (Object)trans);
				page = Constants.DE_TRANSACTION_SEARCH_PAGE;
			}
			else {
				page = Constants.ERROR_PAGE;
			}
		}

		DETicketingController.logger.debug((Object)"transaction search - end");

		return page;
	}

	public void setOrgIdOverride(final Long orgIdOverride) {
		this.orgIdOverride = orgIdOverride;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.DE_CLAIMS_FORM_PAGE
	)
	public String showDeClaimPage(
		final RenderRequest request, final RenderResponse response) {

		return Constants.DE_CLAIMS_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.DE_MISTAKEN_PAYMENT_FORM_PAGE
	)
	public String showDeMistakenPage(
		final RenderRequest request, final RenderResponse response) {

		return Constants.DE_MISTAKEN_PAYMENT_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.DE_REQUEST_ACTION
	)
	public String showDePage(
		final RenderRequest request, final RenderResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug((Object)"showDePage - start");
		final String formId = StringUtils.trimToEmpty(
			request.getParameter(Constants.FORM_ID));
		final String id = StringUtils.trimToEmpty(
			request.getParameter(Constants.ID));
		String page = "";

		if (StringUtils.isBlank(formId)) {
			DETicketingController.logger.error(
				(Object)"showDePage - could not get form id.");

			return Constants.ERROR_PAGE;
		}

		request.setAttribute(Constants.FORM_ID, (Object)formId);
		final RequestContactInformation contactInformation =
			CommonUtil.getUserDetailsFromRequest(
				(PortletRequest)request, this.deProperties);
		serviceRequestForm.setContactInformation(contactInformation);
		final DEListBoxData deListBoxData = CommonUtil.populateDEListBoxData(
			this.deProperties);
		request.setAttribute(Constants.DE_LIST_BOX_DATA, (Object)deListBoxData);

		if (StringUtils.isNotBlank(id)) {
			final HttpSession session = this.getSessionFromRequest(
				(PortletRequest)request);
			final List<AttributesInformation> trans =
				(List<AttributesInformation>)session.getAttribute(
					Constants.DE_TRANS);

			try {
				final AttributesInformation attr = trans.get(
					Integer.parseInt(id));
				serviceRequestForm.setAttributesInformation(attr);
				request.setAttribute(
					Constants.PRE_POPULATED, (Object)Boolean.TRUE);
			}
			catch (NumberFormatException e) {
				return Constants.ERROR_PAGE;
			}
		}

		if (formId.equalsIgnoreCase(Constants.FORM_ID_DE_TRACE)) {
			page = Constants.DE_TRACE_FORM_PAGE;
		}
		else if (formId.equalsIgnoreCase(Constants.FORM_ID_DE_RECALL)) {
			page = Constants.DE_RECALL_FORM_PAGE;
		}
		else if (formId.equalsIgnoreCase(
					Constants.FORM_ID_DE_MISTAKEN_PAYMENT)) {

			request.setAttribute(Constants.AMOUNT_COUNT, (Object)0);
			page = Constants.DE_MISTAKEN_PAYMENT_FORM_PAGE;
		}
		else if (formId.equalsIgnoreCase(Constants.FORM_ID_DE_CLAIMS)) {
			request.setAttribute(Constants.AMOUNT_COUNT, (Object)0);
			page = Constants.DE_CLAIMS_FORM_PAGE;
		}
		else {
			page = Constants.ERROR_PAGE;
		}

		return page;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.DE_RECALL_FORM_PAGE
	)
	public String showDeRecallPage(
		final RenderRequest request, final RenderResponse response) {

		return Constants.DE_RECALL_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.DE_TRACE_FORM_PAGE
	)
	public String showDeTracePage(
		final RenderRequest request, final RenderResponse response) {

		return Constants.DE_TRACE_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.ERROR_PAGE
	)
	public String showErrorPage(
		final RenderRequest request, final RenderResponse response) {

		return Constants.ERROR_PAGE;
	}

	@RenderMapping
	public String showHomePage(
		final RenderRequest request, final RenderResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm) {

		DETicketingController.logger.debug((Object)"showHomePage - start");
		final TktRequestHeader header = this.createRequestHeader(
			(PortletRequest)request);

		if (null == header) {
			request.setAttribute(
				Constants.SESSION_TIMEOUT, (Object)Boolean.TRUE);

			return Constants.ERROR_PAGE;
		}

		final HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(
			PortalUtil.getHttpServletRequest((PortletRequest)request));
		final String formId = StringUtils.trimToEmpty(
			httpReq.getParameter(Constants.FORM_ID));
		final String ticketId = StringUtils.trimToEmpty(
			httpReq.getParameter(Constants.TICKET_ID));
		String page = "";

		if (StringUtils.isNotBlank(ticketId)) {
			final GetTicketRequest getTicketRequest = new GetTicketRequest();
			getTicketRequest.setHeader(header);
			getTicketRequest.setTicketId(ticketId);
			GetTicketResponse resp = null;

			try {
				resp = this.serviceRequestService.ticketDetails(
					getTicketRequest);
			}
			catch (Exception e) {
				DETicketingController.logger.error((Object)e);
				page = Constants.ERROR_PAGE;
			}

			if ((null != resp) && (null != resp.getTicket())) {
				boolean isOfi = false;
				final Properties prop = this.deProperties.getDeProperty();
				final String ticketTypeId = String.valueOf(
					resp.getTicket(
					).getType(
					).getTypeId());

				if (ticketTypeId.equalsIgnoreCase(
						prop.getProperty(
							Constants.REQUEST_TYPE_TYPE_ID_TRACE))) {

					page = Constants.DE_TRACE_FORM_PAGE;
				}
				else if (ticketTypeId.equalsIgnoreCase(
							prop.getProperty(
								Constants.REQUEST_TYPE_TYPE_ID_RECALL))) {

					page = Constants.DE_RECALL_FORM_PAGE;
				}
				else if (ticketTypeId.equalsIgnoreCase(
							prop.getProperty(
								Constants.REQUEST_TYPE_TYPE_ID_MISTAKEN))) {

					page = Constants.DE_MISTAKEN_PAYMENT_FORM_PAGE;
				}
				else if (ticketTypeId.equalsIgnoreCase(
							prop.getProperty(
								Constants.REQUEST_TYPE_TYPE_ID_CLAIM))) {

					page = Constants.DE_CLAIMS_FORM_PAGE;
				}
				else if (ticketTypeId.equalsIgnoreCase(
							prop.getProperty(
								Constants.REQUEST_TYPE_TYPE_ID_OFI_TRACE))) {

					isOfi = true;
					serviceRequestForm.setFinalDestination(
						prop.getProperty(
							Constants.REQUEST_FOR_FINAL_DESTINATION));
					page = Constants.OFI_DE_TRACE_FORM_PAGE;
				}
				else if (ticketTypeId.equalsIgnoreCase(
							prop.getProperty(
								Constants.REQUEST_TYPE_TYPE_ID_OFI_RECALL))) {

					isOfi = true;
					page = Constants.OFI_DE_RECALL_FORM_PAGE;
				}
				else if (ticketTypeId.equalsIgnoreCase(
							prop.getProperty(
								Constants.REQUEST_TYPE_TYPE_ID_OFI_MISTAKEN))) {

					isOfi = true;
					page = Constants.OFI_DE_MISTAKEN_PAYMENT_FORM_PAGE;
				}
				else if (ticketTypeId.equalsIgnoreCase(
							prop.getProperty(
								Constants.REQUEST_TYPE_TYPE_ID_OFI_CLAIM))) {

					isOfi = true;
					page = Constants.OFI_DE_CLAIMS_FORM_PAGE;
				}
				else {
					DETicketingController.logger.error(
						(Object)
							("showHomePage - invalid ticket type: " +
								ticketTypeId));
					page = Constants.ERROR_PAGE;
				}

				request.setAttribute(
					Constants.UPDATE_TICKET, (Object)Boolean.TRUE);
				final DEListBoxData deListBoxData =
					CommonUtil.populateDEListBoxData(this.deProperties);
				request.setAttribute(
					Constants.DE_LIST_BOX_DATA, (Object)deListBoxData);
				request.setAttribute(
					Constants.TICKET_NUMBER,
					(Object)this.getTicketNumber(resp.getTicket()));
				serviceRequestForm.setTicketId(ticketId);
				final RequestContactInformation contactInformation =
					CommonUtil.getUserDetailsFromRequest(
						(PortletRequest)request, this.deProperties);
				serviceRequestForm.setContactInformation(contactInformation);
				serviceRequestForm.setAttributesInformation(
					this.cuscalTicketingService.setupAttributesInformation(
						resp.getTicket(), isOfi));
				request.setAttribute(
					Constants.AMOUNT_COUNT,
					(Object)
						(serviceRequestForm.getAttributesInformation(
						).getProcessedDates(
						).length - 1));
			}
			else {
				DETicketingController.logger.error(
					(Object)
						("showHomePage - could not get service request details for ticket: " +
							ticketId));
				page = Constants.ERROR_PAGE;
			}
		}
		else if (formId.equalsIgnoreCase(Constants.FORM_ID_DE_TRACE) ||
				 formId.equalsIgnoreCase(Constants.FORM_ID_DE_RECALL) ||
				 formId.equalsIgnoreCase(
					 Constants.FORM_ID_DE_MISTAKEN_PAYMENT) ||
				 formId.equalsIgnoreCase(Constants.FORM_ID_DE_CLAIMS)) {

			request.setAttribute(Constants.FORM_ID, (Object)formId);
			page = Constants.DE_TRANSACTION_SEARCH_PAGE;
		}
		else {
			DETicketingController.logger.error(
				(Object)"showHomePage - could not get form id.");
			page = Constants.ERROR_PAGE;
		}

		DETicketingController.logger.debug((Object)"showHomePage - end");

		return page;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.OFI_DE_CLAIMS_FORM_PAGE
	)
	public String showOfiDeClaimPage(
		final RenderRequest request, final RenderResponse response) {

		return Constants.OFI_DE_CLAIMS_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.OFI_DE_MISTAKEN_PAYMENT_FORM_PAGE
	)
	public String showOfiDeMistakenPage(
		final RenderRequest request, final RenderResponse response) {

		return Constants.OFI_DE_MISTAKEN_PAYMENT_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.OFI_DE_RECALL_FORM_PAGE
	)
	public String showOfiDeRecallPage(
		final RenderRequest request, final RenderResponse response) {

		return Constants.OFI_DE_RECALL_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.OFI_DE_TRACE_FORM_PAGE
	)
	public String showOfiDeTracePage(
		final RenderRequest request, final RenderResponse response) {

		return Constants.OFI_DE_TRACE_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.SUCCESS
	)
	public String showSuccessPage(
		final RenderRequest request, final RenderResponse response) {

		return Constants.SUCCESS;
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.DE_CLAIMS_FORM_ACTION
	)
	public void sumbitDeClaims(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug((Object)"sumbit DE Claims - start");
		final String page = this.submitTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_DE_CLAIMS);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug((Object)"sumbit DE Claims - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.DE_MISTAKEN_PAYMENT_FORM_ACTION
	)
	public void sumbitDeMistakenPayment(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug(
			(Object)"sumbit DE Mistaken Payment - start");
		final String page = this.submitTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_DE_MISTAKEN_PAYMENT);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug(
			(Object)"sumbit DE Mistaken Payment - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.DE_RECALL_FORM_ACTION
	)
	public void sumbitDeRecall(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug((Object)"sumbit DE Recall - start");
		final String page = this.submitTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_DE_RECALL);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug((Object)"sumbit DE Recall - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.DE_TRACE_FORM_ACTION
	)
	public void sumbitDeTrace(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug((Object)"sumbit DE Trace - start");
		final String page = this.submitTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_DE_TRACE);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug((Object)"sumbit DE Trace - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_DE_CLAIMS_FORM_ACTION
	)
	public void updateDeClaims(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug((Object)"update DE Claims - start");
		final String page = this.updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_DE_CLAIMS);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug((Object)"update DE Claims - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_DE_MISTAKEN_PAYMENT_FORM_ACTION
	)
	public void updateDeMistakenPayment(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug(
			(Object)"update DE Mistaken Payment - start");
		final String page = this.updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_DE_MISTAKEN_PAYMENT);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug(
			(Object)"update DE Mistaken Payment - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_DE_RECALL_FORM_ACTION
	)
	public void updateDeRecall(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug((Object)"update DE Recall - start");
		final String page = this.updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_DE_RECALL);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug((Object)"update DE Recall - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_DE_TRACE_FORM_ACTION
	)
	public void updateDeTrace(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug((Object)"update DE Trace - start");
		final String page = this.updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_DE_TRACE);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug((Object)"update DE Trace - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_OFI_DE_CLAIMS_FORM_ACTION
	)
	public void updateOfiDeClaim(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug(
			(Object)"update OFI DE claim ticket - start");
		final String page = this.updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_OFI_DE_CLAIMS);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug(
			(Object)"update OFI DE claim ticket - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_OFI_DE_MISTAKEN_PAYMENT_FORM_ACTION
	)
	public void updateOfiDeMistaken(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug(
			(Object)"update OFI DE mistaken ticket - start");
		final String page = this.updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_OFI_DE_MISTAKEN_PAYMENT);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug(
			(Object)"update OFI DE mistaken ticket - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_OFI_DE_RECALL_FORM_ACTION
	)
	public void updateOfiDeRecall(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug(
			(Object)"update OFI DE recall ticket - start");
		final String page = this.updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_OFI_DE_RECALL);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug(
			(Object)"update OFI DE recall ticket - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_OFI_DE_TRACE_FORM_ACTION
	)
	public void updateOfiDeTrace(
		final ActionRequest request, final ActionResponse response,
		final @ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		final BindingResult bindingResult) {

		DETicketingController.logger.debug(
			(Object)"update OFI DE trace ticket - start");
		final String page = this.updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_OFI_DE_TRACE);
		response.setRenderParameter(Constants.RENDER, page);
		DETicketingController.logger.debug(
			(Object)"update OFI DE trace ticket - end");
	}

	private String callAddServiceRequest(
			final PortletContext portletContext,
			final ServiceRequestForm serviceRequestForm,
			final BindingResult bindingResult, final TktRequestHeader header)
		throws Exception {

		final String message = this.cuscalTicketingService.addServiceRequest(
			serviceRequestForm, header, portletContext);

		if (StringUtils.equalsIgnoreCase("SUCCESS", message)) {
			DETicketingController.logger.debug(
				(Object)
					("callAddServiceRequest - message from add request is: " +
						message));
		}
		else if (StringUtils.equalsIgnoreCase(
					"STATUS_CODE_DUPLICATE_TRANSACTION_ERROR", message)) {

			DETicketingController.logger.debug(
				(Object)
					("callAddServiceRequest - message from add request is: " +
						message));
		}
		else {
			DETicketingController.logger.error(
				(Object)"callAddServiceRequest - add service request failed");
		}

		return message;
	}

	private List<AttributesInformation> callSpxClaimTransactionSearch(
			final PortletContext portletContext,
			final ServiceRequestForm serviceRequestForm,
			final BindingResult bindingResult, final TktRequestHeader header)
		throws Exception {

		return this.cuscalTicketingService.spxClaimTransactionSearch(
			serviceRequestForm, header, portletContext);
	}

	private List<AttributesInformation> callSpxPaymentTransactionSearch(
			final PortletContext portletContext,
			final ServiceRequestForm serviceRequestForm,
			final BindingResult bindingResult, final TktRequestHeader header)
		throws Exception {

		return this.cuscalTicketingService.spxPaymentTransactionSearch(
			serviceRequestForm, header, portletContext);
	}

	private List<AttributesInformation> callSpxTransactionSearch(
			final PortletContext portletContext,
			final ServiceRequestForm serviceRequestForm,
			final BindingResult bindingResult, final TktRequestHeader header)
		throws Exception {

		return this.cuscalTicketingService.spxTransactionSearch(
			serviceRequestForm, header, portletContext);
	}

	private String callUpdateServiceRequest(
			final PortletContext portletContext,
			final ServiceRequestForm serviceRequestForm,
			final BindingResult bindingResult, final TktRequestHeader header,
			final User user)
		throws Exception {

		final String message = this.cuscalTicketingService.updateServiceRequest(
			serviceRequestForm, header, portletContext, user);

		if (StringUtils.equalsIgnoreCase("SUCCESS", message)) {
			DETicketingController.logger.debug(
				(Object)
					("callUpdateServiceRequest - message from update request is: " +
						message));
		}
		else if (StringUtils.equalsIgnoreCase(
					"STATUS_CODE_DUPLICATE_TRANSACTION_ERROR", message)) {

			DETicketingController.logger.debug(
				(Object)
					("callUpdateServiceRequest - message from update request is: " +
						message));
		}
		else {
			DETicketingController.logger.error(
				(Object)
					"callUpdateServiceRequest - update service request failed");
		}

		return message;
	}

	private TktRequestHeader createRequestHeader(final PortletRequest request) {
		TktRequestHeader header = null;
		final HttpSession session = this.getSessionFromRequest(request);

		if (null != request) {
			try {
				final User user = CommonUtil.getUserFromRequest(request);

				if (null != user) {
					DETicketingController.logger.debug(
						(Object)"createRequestHeader - user is not null");
					header = new TktRequestHeader();
					header.setOrigin("PORTAL");
					header.setUserId(user.getScreenName());
					final Organization org = CommonUtil.getOrganisationForUser(
						user);

					if (null != org) {
						DETicketingController.logger.debug(
							(Object)
								("createRequestHeader - session orgShortName: " +
									session.getAttribute("orgShortName")));
						String orgShortName;

						if (null == session.getAttribute("orgShortName")) {
							orgShortName = this.orgShortName(
								org.getOrganizationId());
							session.setAttribute(
								"orgShortName", (Object)orgShortName);
						}
						else {
							orgShortName = (String)session.getAttribute(
								"orgShortName");
						}

						if (StringUtils.isNotBlank(orgShortName)) {
							header.setUserOrgId(orgShortName);
							header.setUserOrgName(org.getName());
						}
						else {
							DETicketingController.logger.error(
								(Object)
									("createRequestHeader - Could not get orgShortName for organisation: " +
										org.getName() + " with orgId: " +
											org.getOrganizationId()));
						}
					}
				}
			}
			catch (Exception e) {
				DETicketingController.logger.error(
					(Object)("createRequestHeader - " + e.getMessage()),
					(Throwable)e);
			}
		}

		return header;
	}

	private HttpSession getSessionFromRequest(final PortletRequest request) {
		final HttpServletRequest servletRequest =
			PortalUtil.getHttpServletRequest(request);
		final HttpSession session = servletRequest.getSession();

		return session;
	}

	private String getTicketNumber(final TicketType ticket) {
		final String ticketNumber = ticket.getDestination();

		if (StringUtils.isNotBlank(ticketNumber) && !"0".equals(ticketNumber)) {
			return ticketNumber;
		}

		return Constants.SUBMITTED;
	}

	private String orgShortName(final Long orgId) {
		String orgShortName = null;

		if (null != this.orgIdOverride) {
			orgShortName = this.cuscalTicketingService.getOrgShortName(
				this.orgIdOverride);
		}
		else {
			orgShortName = this.cuscalTicketingService.getOrgShortName(orgId);
		}

		return orgShortName;
	}

	private String submitTicket(
		final ActionRequest request, final ActionResponse response,
		final ServiceRequestForm serviceRequestForm,
		final BindingResult bindingResult, final String formId) {

		String page = Constants.ERROR_PAGE;
		String requestType = "";
		String typeId = "";
		final TktRequestHeader header = this.createRequestHeader(
			(PortletRequest)request);

		if (null == header) {
			request.setAttribute(
				Constants.SESSION_TIMEOUT, (Object)Boolean.TRUE);
			page = Constants.ERROR_PAGE;

			return page;
		}

		try {
			final String prePopulated = request.getParameter(
				Constants.PRE_POPULATED);
			serviceRequestForm.setPrePopulated(
				Boolean.parseBoolean(prePopulated));
		}
		catch (Exception ex) {
		}

		if (Constants.FORM_ID_DE_TRACE.equals(formId)) {
			this.deTraceValidator.validate(
				serviceRequestForm, (Errors)bindingResult);
			requestType = Constants.REQUEST_TYPE_DE_TRACE;
			typeId = Constants.REQUEST_TYPE_TYPE_ID_TRACE;
			page = Constants.DE_TRACE_FORM_PAGE;
		}
		else if (Constants.FORM_ID_DE_RECALL.equals(formId)) {
			this.deRecallValidator.validate(
				serviceRequestForm, (Errors)bindingResult);
			requestType = Constants.REQUEST_TYPE_DE_RECALL;
			typeId = Constants.REQUEST_TYPE_TYPE_ID_RECALL;
			page = Constants.DE_RECALL_FORM_PAGE;
		}
		else if (Constants.FORM_ID_DE_MISTAKEN_PAYMENT.equals(formId)) {
			this.deMistakenPaymentValidator.validate(
				serviceRequestForm, (Errors)bindingResult);
			requestType = Constants.REQUEST_TYPE_DE_MISTAKEN;
			typeId = Constants.REQUEST_TYPE_TYPE_ID_MISTAKEN;
			page = Constants.DE_MISTAKEN_PAYMENT_FORM_PAGE;
		}
		else {
			if (!Constants.FORM_ID_DE_CLAIMS.equals(formId)) {
				page = Constants.ERROR_PAGE;

				return page;
			}

			this.deClaimValidator.validate(
				serviceRequestForm, (Errors)bindingResult);
			requestType = Constants.REQUEST_TYPE_DE_CLAIM;
			typeId = Constants.REQUEST_TYPE_TYPE_ID_CLAIM;
			page = Constants.DE_CLAIMS_FORM_PAGE;
		}

		if (bindingResult.hasErrors()) {
			DETicketingController.logger.debug(
				(Object)
					"submit DE service request action - there are validation errors");
			final DEListBoxData deListBoxData =
				CommonUtil.populateDEListBoxData(this.deProperties);
			request.setAttribute(
				Constants.DE_LIST_BOX_DATA, (Object)deListBoxData);
			request.setAttribute(
				Constants.PRE_POPULATED,
				(Object)Boolean.valueOf(
					request.getParameter(Constants.PRE_POPULATED)));
			request.setAttribute(
				Constants.AMOUNT_COUNT,
				(Object)
					(serviceRequestForm.getAttributesInformation(
					).getProcessedDates(
					).length - 1));
		}
		else {
			final RequestTypeInformation requestTypeInformation =
				new RequestTypeInformation();
			serviceRequestForm.setRequestTypeInformation(
				requestTypeInformation);
			final Properties prop = this.deProperties.getDeProperty();
			requestTypeInformation.setRequestTypeId(prop.getProperty(typeId));
			requestTypeInformation.setProductCode(
				prop.getProperty(Constants.DE_PRODUCT_CODE));
			serviceRequestForm.getAttributesInformation(
			).setRequestType(
				requestType
			);
			String message = "";

			try {
				message = this.callAddServiceRequest(
					PortletContext.newContext(
						(PortletResponse)response, (PortletRequest)request),
					serviceRequestForm, bindingResult, header);
			}
			catch (Exception e1) {
				DETicketingController.logger.error(
					(Object)("addServiceRequest - There are errors: " + e1));
			}

			if (StringUtils.equalsIgnoreCase("SUCCESS", message)) {
				page = Constants.SUCCESS;
			}
			else {
				page = Constants.ERROR_PAGE;
			}
		}

		return page;
	}

	private String updateTicket(
		final ActionRequest request, final ActionResponse response,
		final ServiceRequestForm serviceRequestForm,
		final BindingResult bindingResult, final String formId) {

		String page = Constants.ERROR_PAGE;
		final TktRequestHeader header = this.createRequestHeader(
			(PortletRequest)request);

		if (null == header) {
			request.setAttribute(
				Constants.SESSION_TIMEOUT, (Object)Boolean.TRUE);

			return Constants.ERROR_PAGE;
		}

		if (Constants.FORM_ID_DE_TRACE.equals(formId)) {
			deTraceValidator.validate(serviceRequestForm, bindingResult);
			page = Constants.DE_TRACE_FORM_PAGE;
		}
		else if (Constants.FORM_ID_DE_RECALL.equals(formId)) {
			deRecallValidator.validate(serviceRequestForm, bindingResult);
			page = Constants.DE_RECALL_FORM_PAGE;
		}
		else if (Constants.FORM_ID_DE_MISTAKEN_PAYMENT.equals(formId)) {
			deMistakenPaymentValidator.validate(
				serviceRequestForm, bindingResult);
			page = Constants.DE_MISTAKEN_PAYMENT_FORM_PAGE;
		}
		else if (Constants.FORM_ID_DE_CLAIMS.equals(formId)) {
			deClaimValidator.validate(serviceRequestForm, bindingResult);
			page = Constants.DE_CLAIMS_FORM_PAGE;
		}
		else if (Constants.FORM_ID_OFI_DE_TRACE.equals(formId)) {
			Properties prop = deProperties.getDeProperty();

			ofiDETraceValidator.setFinalDestination(
				prop.getProperty(Constants.REQUEST_FOR_FINAL_DESTINATION));
			serviceRequestForm.setFinalDestination(
				prop.getProperty(Constants.REQUEST_FOR_FINAL_DESTINATION));
			ofiDETraceValidator.validate(serviceRequestForm, bindingResult);
			serviceRequestForm.setOfi(true);
			page = Constants.OFI_DE_TRACE_FORM_PAGE;
		}
		else if (Constants.FORM_ID_OFI_DE_RECALL.equals(formId)) {
			ofiDERecallValidator.validate(serviceRequestForm, bindingResult);
			serviceRequestForm.setOfi(true);
			page = Constants.OFI_DE_RECALL_FORM_PAGE;
		}
		else if (Constants.FORM_ID_OFI_DE_MISTAKEN_PAYMENT.equals(formId)) {
			ofiDEMistakenPaymentValidator.validate(
				serviceRequestForm, bindingResult);
			serviceRequestForm.setOfi(true);
			page = Constants.OFI_DE_MISTAKEN_PAYMENT_FORM_PAGE;
		}
		else {
			if (!Constants.FORM_ID_OFI_DE_CLAIMS.equals(formId)) {
				return Constants.ERROR_PAGE;
			}

			this.ofiDEClaimValidator.validate(
				serviceRequestForm, (Errors)bindingResult);
			serviceRequestForm.setOfi(true);
			page = Constants.OFI_DE_CLAIMS_FORM_PAGE;
		}

		if (bindingResult.hasErrors()) {
			DETicketingController.logger.debug(
				(Object)
					"update DE service request action - there are validation errors");
			final DEListBoxData deListBoxData =
				CommonUtil.populateDEListBoxData(this.deProperties);
			request.setAttribute(
				Constants.DE_LIST_BOX_DATA, (Object)deListBoxData);
			request.setAttribute(Constants.UPDATE_TICKET, (Object)Boolean.TRUE);
			request.setAttribute(
				Constants.AMOUNT_COUNT,
				(Object)
					(serviceRequestForm.getAttributesInformation(
					).getProcessedDates(
					).length - 1));
			request.setAttribute(
				Constants.TICKET_NUMBER,
				(Object)request.getParameter(Constants.TICKET_NUMBER));
		}
		else {
			final RequestTypeInformation requestTypeInformation =
				new RequestTypeInformation();
			serviceRequestForm.setRequestTypeInformation(
				requestTypeInformation);
			String message = "";

			try {
				message = this.callUpdateServiceRequest(
					PortletContext.newContext(
						(PortletResponse)response, (PortletRequest)request),
					serviceRequestForm, bindingResult, header,
					CommonUtil.getUserFromRequest((PortletRequest)request));
			}
			catch (Exception e1) {
				DETicketingController.logger.error(
					(Object)("updateServiceRequest - There are errors: " + e1));
			}

			if (StringUtils.equalsIgnoreCase("SUCCESS", message)) {
				page = Constants.SUCCESS;
			}
			else {
				page = Constants.ERROR_PAGE;
			}
		}

		return page;
	}

	private static Logger logger;

	static {
		DETicketingController.logger = Logger.getLogger(
			(Class)DETicketingController.class);
	}

	private final CuscalTicketingService cuscalTicketingService;

	private final DEClaimValidator deClaimValidator;

	private final DEMistakenPaymentValidator deMistakenPaymentValidator;

	private final DEProperties deProperties;

	private final DERecallValidator deRecallValidator;

	private final DETraceValidator deTraceValidator;

	private final DETransactionSearchValidator deTransactionSearchValidator;

	private final OfiDEClaimValidator ofiDEClaimValidator;

	private final OfiDEMistakenPaymentValidator ofiDEMistakenPaymentValidator;

	private final OfiDERecallValidator ofiDERecallValidator;

	private final OfiDETraceValidator ofiDETraceValidator;

	private Long orgIdOverride;

	private final ServiceRequestServiceImpl serviceRequestService =
		CuscalServiceLocator.getService(ServiceRequestServiceImpl.class.getName());

}