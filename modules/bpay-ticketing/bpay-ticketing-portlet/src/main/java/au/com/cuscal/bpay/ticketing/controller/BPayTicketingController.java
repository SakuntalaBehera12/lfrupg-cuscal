package au.com.cuscal.bpay.ticketing.controller;

import au.com.cuscal.bpay.ticketing.common.BPayProperties;
import au.com.cuscal.bpay.ticketing.common.CommonUtil;
import au.com.cuscal.bpay.ticketing.common.Constants;
import au.com.cuscal.bpay.ticketing.domain.BPayListBoxData;
import au.com.cuscal.bpay.ticketing.forms.RequestContactInformation;
import au.com.cuscal.bpay.ticketing.forms.RequestTypeInformation;
import au.com.cuscal.bpay.ticketing.forms.ServiceRequestForm;
import au.com.cuscal.bpay.ticketing.services.CuscalTicketingService;
import au.com.cuscal.bpay.ticketing.validator.BPayTicketingServiceRequestValidator;
import au.com.cuscal.bpay.ticketing.validator.OfiBpayInvestigationValidator;
import au.com.cuscal.bpay.ticketing.validator.OfiErrorCorrectionValidator;
import au.com.cuscal.common.framework.dxp.service.request.controller.AbstractServiceRequestController;
import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.common.framework.dxp.service.request.services.ServiceRequestServiceImpl;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.selfservice.*;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;
import com.liferay.portletmvc4spring.bind.annotation.ResourceMapping;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
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

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * BPay main Controller.
 *
 *
 */
@Controller("bPayController")
@RequestMapping("VIEW")
public class BPayTicketingController extends AbstractServiceRequestController {

	/**
	 * Find user group role list
	 *
	 * @param user
	 * @return List<Role>
	 */
	public static List<Role> findUserGroupRoleList(User user) {
		List<Role> finalRolesLst = new ArrayList<>();
		List<Role> rolesLst = null;

		try {
			List<UserGroup> userGrps = user.getUserGroups();

			for (UserGroup userGroup : userGrps) {
				rolesLst = new ArrayList<>();
				long groupId = userGroup.getGroup(
				).getGroupId();

				rolesLst = RoleLocalServiceUtil.getGroupRoles(groupId);

				for (Role role : rolesLst) {
					finalRolesLst.add(role);
					logger.debug(
						"findUserGroupRoleList - User Group roles for user:" +
							role.getName());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return finalRolesLst;
	}

	@ModelAttribute("serviceRequestForm")
	public ServiceRequestForm getCommandObjectServiceRequestForm() {
		return new ServiceRequestForm();
	}

	/**
	 *
	 * @return	Long
	 */
	public Long getOrgIdOverride() {
		return orgIdOverride;
	}

	/**
	 * Check and set the user 2FA roles detail in object.
	 *
	 * @param PortletRequest
	 * @return boolean
	 */
	public boolean isUserHas2FARole(PortletRequest request) {
		logger.debug("isUserHas2FARole - start");
		boolean is2faAccessible = false;
		User user = null;

		try {
			user = PortalUtil.getUser(request);

			if (null != user) {
				Long userId = user.getUserId();

				List<Role> roles = RoleLocalServiceUtil.getUserRoles(userId);
				List<Role> grpRoles = findUserGroupRoleList(user);
				List<Role> allUserRoles = new ArrayList<>();

				allUserRoles.addAll(roles);
				allUserRoles.addAll(grpRoles);
				logger.debug(
					"isUserHas2FARole - All Roles count is " +
						allUserRoles.size());

				for (Role role : allUserRoles) {
					if (Constants.ROLE_FA.equalsIgnoreCase(role.getName())) {
						is2faAccessible = true;
						logger.debug(
							"[isUserHas2FARole] setting role as true for Role  " +
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
	 *
	 * @param request
	 * @param response
	 */
	@ResourceMapping("retrieveMaskedFeilds")
	public void retrieveMaskedFeilds(
		ResourceRequest request, ResourceResponse response) {

		logger.debug("retrieveMaskedDetails - Resource Mapping here");

		String ticketId = request.getParameter("ticketId");
		String maskedFieldsJson = "";

		try {
			TktRequestHeader header = createRequestHeader(request);

			Map<String, String> protectedAttributes = new HashMap<>();

			if (StringUtils.isNotBlank(ticketId)) {
				GetTicketRequest getTicketRequest = new GetTicketRequest();

				getTicketRequest.setHeader(header);
				getTicketRequest.setTicketId(ticketId);
				getTicketRequest.setDecrypted(true);

				GetTicketResponse resp = serviceRequestService.ticketDetails(
					getTicketRequest);

				if ((null != resp) && (null != resp.getTicket())) {
					for (AttributesType att :
							resp.getTicket(
							).getAttributes()) {

						if (att.isEncrypted()) {
							protectedAttributes.put(
								att.getDisplayTitle(), att.getValue());
						}
					}
				}
				else {
					logger.error(
						"retrieveMaskedFeilds - ticket details are null.");
					protectedAttributes.put(
						"error", "Could not retrieve the request details.");
				}
			}
			else {
				logger.error("retrieveMaskedFeilds - ticketId is null.");
				protectedAttributes.put(
					"error", "Could not retrieve the request details.");
			}

			JSONObject jsonObject = new JSONObject(protectedAttributes);

			maskedFieldsJson = jsonObject.toString();
			PrintWriter out = response.getWriter();
			response.setContentType("json");
			response.setCharacterEncoding("utf-8");
			out.print(maskedFieldsJson);
		}
		catch (IOException ex) {
			logger.error(
				"downloadAttachment - Exception occurred during output of file stream:",
				ex.getCause());
		}
	}

	public void setOrgIdOverride(Long orgIdOverride) {
		this.orgIdOverride = orgIdOverride;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.BPAY_INVESTIGATION_FORM_PAGE
	)
	public String showBpayPage(RenderRequest request, RenderResponse response) {
		return Constants.BPAY_INVESTIGATION_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.ERROR_PAGE
	)
	public String showErrorPage(
		RenderRequest request, RenderResponse response) {

		return Constants.ERROR_PAGE;
	}

	/**
	 * Handles and shows the BPay Ticketing Portlet home page.
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping
	public String showHomePage(
		RenderRequest request, RenderResponse response,
		@ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		BindingResult bindingResult) {

		logger.debug("showHomePage - start");

		String page = Constants.ERROR_PAGE;

		TktRequestHeader header = createRequestHeader(request);

		if (null == header) {
			request.setAttribute(Constants.SESSION_TIMEOUT, Boolean.TRUE);

			return page;
		}

		HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(
			PortalUtil.getHttpServletRequest(request));

		String ticketId = StringUtils.trimToEmpty(
			httpReq.getParameter(Constants.TICKET_ID));
		String formId = StringUtils.trimToEmpty(
			httpReq.getParameter(Constants.FORM_ID));

		BPayListBoxData bPayListBoxData = CommonUtil.populateBPayListBoxData(
			bpayProperties);

		RequestContactInformation contactInformation =
			CommonUtil.getUserDetailsFromRequest(request, bpayProperties);

		serviceRequestForm.setContactInformation(contactInformation);

		if (StringUtils.isNotBlank(ticketId)) {
			GetTicketRequest getTicketRequest = new GetTicketRequest();

			getTicketRequest.setHeader(header);
			getTicketRequest.setTicketId(ticketId);

			GetTicketResponse resp = serviceRequestService.ticketDetails(
				getTicketRequest);

			if ((null != resp) && (null != resp.getTicket())) {
				request.setAttribute(
					Constants.BPAY_LIST_BOX_DATA, bPayListBoxData);
				request.setAttribute(
					Constants.TICKET_NUMBER, getTicketNumber(resp.getTicket()));
				request.setAttribute(Constants.UPDATE_TICKET, Boolean.TRUE);

				serviceRequestForm.setAttributesInformation(
					cuscalTicketingService.setupAttributesInformation(
						resp.getTicket()));
				serviceRequestForm.setTicketId(ticketId);

				Properties prop = bpayProperties.getBpayProperty();

				String ticketTypeId = String.valueOf(
					resp.getTicket(
					).getType(
					).getTypeId());

				if (ticketTypeId.equalsIgnoreCase(
						prop.getProperty(Constants.REQUEST_TYPE_TYPE_ID_CI))) {//Bpay investigation
					page = Constants.BPAY_INVESTIGATION_FORM_PAGE;
				}
				else if (ticketTypeId.equalsIgnoreCase(
							prop.getProperty(
								Constants.REQUEST_TYPE_TYPE_ID_OFI))) {//OFI Bpay investigation

					String ofiBpayType = prop.getProperty(
						Constants.REQUEST_TYPE_OFI_BPAY_INVESTIGATION);

					if (StringUtils.trimToEmpty(
							serviceRequestForm.getAttributesInformation(
							).getInvestigationType()
						).equalsIgnoreCase(
							ofiBpayType
						)) {

						page = Constants.OFI_BPAY_INVESTIGATION_FORM_PAGE;
					}
					else {
						page = Constants.OFI_ERROR_CORRECTION_FORM_PAGE;
					}
				}
				else {
					logger.error(
						"showHomePage - invalid ticket type: " + ticketTypeId);
				}
			}
			else {
				logger.error(
					"showHomePage - could not get service request details for ticket: " +
						ticketId);
			}
		}
		else {
			if (formId.equalsIgnoreCase(
					Constants.FORM_ID_CLIENT_INITIATED_BPAY)) {

				request.setAttribute(
					Constants.BPAY_LIST_BOX_DATA, bPayListBoxData);
				page = Constants.BPAY_INVESTIGATION_FORM_PAGE;
			}
			else {
				logger.error("showHomePage - could not get form id.");
				page = Constants.ERROR_PAGE;
			}
		}

		logger.debug("showHomePage - end");

		return page;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.OFI_BPAY_INVESTIGATION_FORM_PAGE
	)
	public String showOfiBpayInvestigatoinPage(
		RenderRequest request, RenderResponse response) {

		return Constants.OFI_BPAY_INVESTIGATION_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.OFI_ERROR_CORRECTION_FORM_PAGE
	)
	public String showOfiErrorCorrectionPage(
		RenderRequest request, RenderResponse response) {

		return Constants.OFI_ERROR_CORRECTION_FORM_PAGE;
	}

	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.SUCCESS
	)
	public String showSuccessPage(
		RenderRequest request, RenderResponse response) {

		return Constants.SUCCESS;
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
		params = Constants.ACTION + Constants.EQUALS + Constants.STEP_UP_ACTION
	)
	public void stepUp(
		ActionRequest request, ActionResponse response,
		@RequestParam("ticketIdStepUp") String ticketId) {

		logger.debug("stepUp - start");

		HttpSession httpSession = getSessionFromRequest(request);
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		PortletSession portletSession = request.getPortletSession();

		logger.debug("stepUp - The ticketIds attribute is: " + ticketId);

		String userSteppedupSession = GetterUtil.getString(
			portletSession.getAttribute(
				Constants._USER_STEPPEDUP, PortletSession.APPLICATION_SCOPE));

		logger.debug(
			"stepUp - The Stepped Up session attribute is: " +
				userSteppedupSession);

		String userCancelSession = GetterUtil.getString(
			portletSession.getAttribute(
				Constants.USER_CANCEL, PortletSession.APPLICATION_SCOPE));

		if (Constants.CANCEL.equals(userCancelSession)) {
			httpSession.setAttribute(Constants.USER_CANCEL, null);

			portletSession.setAttribute(
				Constants.USER_CANCEL, null, PortletSession.APPLICATION_SCOPE);
			request.setAttribute(Constants.IS_FA, isUserHas2FARole(request));
			httpSession.setAttribute(Constants._RETURN_URL, null);

			portletSession.setAttribute(
				Constants._RETURN_URL, null, PortletSession.APPLICATION_SCOPE);
			logger.debug("steppedup: value is: " + userSteppedupSession);
		}
		else if (userSteppedupSession.isEmpty() ||
				 Constants.FALSE.equals(userSteppedupSession) ||
				 Constants.ERROR.equals(userSteppedupSession)) {

			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			PortletURL link = PortletURLFactoryUtil.create(
				httpServletRequest, PortalUtil.getPortletId(request),
				themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

			String currentUrl = link.toString(
			).replace(
				"&amp;", "&"
			);

			currentUrl = currentUrl.concat(
				"&ticketId="
			).concat(
				ticketId
			);
			httpSession.setAttribute(Constants._RETURN_URL, currentUrl);

			portletSession.setAttribute(
				Constants._RETURN_URL, currentUrl,
				PortletSession.APPLICATION_SCOPE);

			try {
				response.sendRedirect("/2fa");
			}
			catch (IOException ioe) {
				logger.error(
					"Exception while sending the url to 2FA page from self service portlet: " +
						ioe.getMessage(),
					ioe);
			}
		}
		else {
			logger.debug("steppedup: value is  " + userSteppedupSession);
		}

		logger.debug("stepUp - end");
	}

	/**
	 * @param request
	 * @param response
	 * @param serviceRequestForm
	 * @param bindingResult
	 */
	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_OFI_BPAY_INVESTIGATION_ACTION
	)
	public void submitOfiBpayInvestigation(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		BindingResult bindingResult) {

		logger.debug("submit ofi bpay investigation - start");

		String page = updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_OFI_BPAY_INVESTIGATION);

		response.setRenderParameter(Constants.RENDER, page);

		logger.debug("submit ofi bpay investigation - end");
	}

	/**
	 * @param request
	 * @param response
	 * @param serviceRequestForm
	 * @param bindingResult
	 */
	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_OFI_ERROR_CORRECTION_ACTION
	)
	public void submitOfiErrorCorrection(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		BindingResult bindingResult) {

		logger.debug("submit ofi error correction - start");

		String page = updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_OFI_ERROR_CORRECTION);

		response.setRenderParameter(Constants.RENDER, page);

		logger.debug("submit ofi error correction - end");
	}

	/**
	 * @param request
	 * @param response
	 * @param serviceRequestForm
	 * @param bindingResult
	 */
	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.SUBMIT_BPAY_INVESTIGATION_ACTION
	)
	public void sumbitBpayInvetigation(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		BindingResult bindingResult) {

		logger.debug("sumbit bpay invetigation - start");

		TktRequestHeader header = createRequestHeader(request);

		if (null == header) {
			request.setAttribute(Constants.SESSION_TIMEOUT, Boolean.TRUE);
			response.setRenderParameter(Constants.RENDER, Constants.ERROR_PAGE);

			return;
		}

		String message = "";

		BPayListBoxData bPayListBoxData = CommonUtil.populateBPayListBoxData(
			bpayProperties);

		bPayTicketingServiceRequestValidator.validate(
			serviceRequestForm, bindingResult);

		if (bindingResult.hasErrors()) {//error scenario
			logger.debug(
				"submit bpay service request action - there are validation errors");
			request.setAttribute(Constants.BPAY_LIST_BOX_DATA, bPayListBoxData);
			response.setRenderParameter(
				Constants.RENDER, Constants.BPAY_INVESTIGATION_FORM_PAGE);
		}
		else {//success scenario
			//Request type information
			Properties prop = bpayProperties.getBpayProperty();
			RequestTypeInformation requestTypeInformation =
				new RequestTypeInformation();

			requestTypeInformation.setRequestTypeId(
				prop.getProperty("bpay.type.id"));//bpay Type
			requestTypeInformation.setProductCode(
				prop.getProperty("bpay.product.code"));//bpay product code
			serviceRequestForm.setRequestTypeInformation(
				requestTypeInformation);

			try {
				message = callAddServiceRequest(
					PortletContext.newContext(response, request),
					serviceRequestForm, bindingResult, header);//service wrapper call leads to local service and downstream call.

			}
			catch (Exception e1) {
				logger.error("addServiceRequest - There are errors");
			}

			if (StringUtils.equalsIgnoreCase("SUCCESS", message)) {
				response.setRenderParameter(
					Constants.RENDER, Constants.SUCCESS);
			}
			else if (StringUtils.equalsIgnoreCase(
						"DUPLICATE_TRANSACTION_ERROR", message)) {

				bPayTicketingServiceRequestValidator.serverSideValidation(
					bindingResult);
				request.setAttribute("bPayListBoxData", bPayListBoxData);
				response.setRenderParameter(
					Constants.RENDER, Constants.BPAY_INVESTIGATION_FORM_PAGE);
			}
			else {
				response.setRenderParameter(
					Constants.RENDER, Constants.ERROR_PAGE);
			}
		}

		logger.debug("sumbit bpay invetigation - end");
	}

	/**
	 * @param request
	 * @param response
	 * @param serviceRequestForm
	 * @param bindingResult
	 */
	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_BPAY_INVESTIGATION_ACTION
	)
	public void updateBpayInvestigation(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("serviceRequestForm") ServiceRequestForm
			serviceRequestForm,
		BindingResult bindingResult) {

		logger.debug("update bpay investigation - start");

		String page = updateTicket(
			request, response, serviceRequestForm, bindingResult,
			Constants.FORM_ID_CLIENT_INITIATED_BPAY);

		response.setRenderParameter(Constants.RENDER, page);

		logger.debug("update bpay investigation - end");
	}

	/**
	 * Validates service request, calls ldap and addServiceRequest for BPay One.
	 *
	 * @param request
	 * @param serviceRequestForm
	 * @param bindingResult
	 * @param header
	 * @return	String
	 * @throws Exception
	 */
	private String callAddServiceRequest(
			PortletContext portletContext,
			ServiceRequestForm serviceRequestForm, BindingResult bindingResult,
			TktRequestHeader header)
		throws Exception {

		String message = cuscalTicketingService.addServiceRequest(
			serviceRequestForm, header, portletContext);//local service layer integration.

		if (StringUtils.equalsIgnoreCase("SUCCESS", message)) {
			logger.debug(
				"callAddServiceRequest - message from add request is: " +
					message);
		}
		else if (StringUtils.equalsIgnoreCase(
					"STATUS_CODE_DUPLICATE_TRANSACTION_ERROR", message)) {

			logger.debug(
				"callAddServiceRequest - message from add request is: " +
					message);
		}
		else {
			logger.error("callAddServiceRequest - add service request failed");
		}

		return message;
	}

	/**
	 * @param portletContext
	 * @param serviceRequestForm
	 * @param bindingResult
	 * @param header
	 * @param isAppendMode
	 * @return
	 * @throws Exception
	 */
	private String callUpdateServiceRequest(
			PortletContext portletContext,
			ServiceRequestForm serviceRequestForm, BindingResult bindingResult,
			TktRequestHeader header, User user)
		throws Exception {

		String message = cuscalTicketingService.updateServiceRequest(
			serviceRequestForm, header, portletContext, user);//local service layer integration.

		if (StringUtils.equalsIgnoreCase("SUCCESS", message)) {
			logger.debug(
				"callUpdateServiceRequest - message from update request is: " +
					message);
		}
		else if (StringUtils.equalsIgnoreCase(
					"STATUS_CODE_DUPLICATE_TRANSACTION_ERROR", message)) {

			logger.debug(
				"callUpdateServiceRequest - message from update request is: " +
					message);
		}
		else {
			logger.error(
				"callUpdateServiceRequest - update service request failed");
		}

		return message;
	}

	/**
	 * Get the header from the global header instance. Will save time when we do
	 * not have to call the entity service over and over again to get the
	 * Organisation short name.
	 *
	 * @param request	<code>PortletRequest</code>
	 * @return VsmRequestHeader
	 */
	private TktRequestHeader createRequestHeader(PortletRequest request) {
		TktRequestHeader header = null;
		HttpSession session = getSessionFromRequest(request);

		if (null != request) {
			try {
				User user = CommonUtil.getUserFromRequest(request);

				if (null != user) {
					logger.debug("createRequestHeader - user is not null");
					header = new TktRequestHeader();
					header.setOrigin(AuditOrigin.PORTAL_ORIGIN);
					header.setUserId(user.getScreenName());

					Organization org = CommonUtil.getOrganisationForUser(user);

					if (null != org) {
						String orgShortName;
						logger.debug(
							"createRequestHeader - session orgShortName: " +
								session.getAttribute("orgShortName"));

						if (null == session.getAttribute("orgShortName")) {
							orgShortName = orgShortName(
								org.getOrganizationId());
							session.setAttribute("orgShortName", orgShortName);
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
							logger.error(
								"createRequestHeader - Could not get orgShortName for organisation: " +
									org.getName() + " with orgId: " +
										org.getOrganizationId());
						}
					} // User organisation is null
				} // User object is null.
			}
			catch (Exception e) {
				logger.error("createRequestHeader - " + e.getMessage(), e);
			}
		}

		return header;
	}

	/**
	 * Return the current Session from the request.
	 *
	 * @param 	request	<em>PortletRequest</em>
	 * @return	HttpSession
	 */
	private HttpSession getSessionFromRequest(PortletRequest request) {
		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();

		return session;
	}

	private String getTicketNumber(TicketType ticket) {
		String ticketNumber = ticket.getDestination();

		if (StringUtils.isNotBlank(ticketNumber) && !"0".equals(ticketNumber)) {
			return ticketNumber;
		}

		return Constants.SUBMITTED;
	}

	/**
	 * Get the orgShortName for the logged in user's organisation.
	 *
	 * @return	String
	 */
	private String orgShortName(Long orgId) {
		String orgShortName = null;

		if (null != orgIdOverride) {
			orgShortName = cuscalTicketingService.getOrgShortName(
				orgIdOverride);
		}
		else {
			orgShortName = cuscalTicketingService.getOrgShortName(orgId);
		}

		return orgShortName;
	}

	/**
	 * @param request
	 * @param response
	 * @param serviceRequestForm
	 * @param bindingResult
	 * @param formId
	 * @return
	 */
	private String updateTicket(
		ActionRequest request, ActionResponse response,
		ServiceRequestForm serviceRequestForm, BindingResult bindingResult,
		String formId) {

		String page = Constants.ERROR_PAGE;

		TktRequestHeader header = createRequestHeader(request);

		if (null == header) {
			request.setAttribute(Constants.SESSION_TIMEOUT, Boolean.TRUE);

			return Constants.ERROR_PAGE;
		}

		if (Constants.FORM_ID_CLIENT_INITIATED_BPAY.equals(formId)) {
			bPayTicketingServiceRequestValidator.validate(
				serviceRequestForm, bindingResult);
			page = Constants.BPAY_INVESTIGATION_FORM_PAGE;
		}
		else if (Constants.FORM_ID_OFI_BPAY_INVESTIGATION.equals(formId)) {
			serviceRequestForm.setOfi(true);
			ofiBpayInvestigationValidator.validate(
				serviceRequestForm, bindingResult);
			page = Constants.OFI_BPAY_INVESTIGATION_FORM_PAGE;
		}
		else if (Constants.FORM_ID_OFI_ERROR_CORRECTION.equals(formId)) {
			serviceRequestForm.setOfi(true);
			ofiErrorCorrectionValidator.validate(
				serviceRequestForm, bindingResult);
			page = Constants.OFI_ERROR_CORRECTION_FORM_PAGE;
		}
		else {
			return Constants.ERROR_PAGE;
		}

		BPayListBoxData bPayListBoxData = CommonUtil.populateBPayListBoxData(
			bpayProperties);

		if (bindingResult.hasErrors()) {//error scenario
			logger.debug(
				"Update BPay service request action - there are validation errors");
			request.setAttribute(Constants.BPAY_LIST_BOX_DATA, bPayListBoxData);
			request.setAttribute(Constants.UPDATE_TICKET, Boolean.TRUE);
			request.setAttribute(
				Constants.TICKET_NUMBER,
				request.getParameter(Constants.TICKET_NUMBER));
		}
		else {//success scenario
			//Request type information
			RequestTypeInformation requestTypeInformation =
				new RequestTypeInformation();

			serviceRequestForm.setRequestTypeInformation(
				requestTypeInformation);
			Properties prop = bpayProperties.getBpayProperty();

			requestTypeInformation.setRequestTypeId(
				prop.getProperty("bpay.type.id"));//bpay Type
			requestTypeInformation.setProductCode(
				prop.getProperty("bpay.product.code"));//bpay product code

			String message = "";

			try {
				message = callUpdateServiceRequest(
					PortletContext.newContext(response, request),
					serviceRequestForm, bindingResult, header,
					CommonUtil.getUserFromRequest(request));
			}
			catch (Exception e1) {
				logger.error(
					"updateServiceRequest - There are errors: " +
						e1.getMessage());
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

	private static Logger logger = Logger.getLogger(
		BPayTicketingController.class);

	@Autowired
	@Qualifier("bpayProperties")
	private BPayProperties bpayProperties;

	@Autowired
	@Qualifier("bPayTicketingServiceRequestValidator")
	private BPayTicketingServiceRequestValidator
		bPayTicketingServiceRequestValidator;

	@Autowired
	@Qualifier(Constants.CUSCAL_TICKETING_SERVICE)
	private CuscalTicketingService cuscalTicketingService;

	@Autowired
	@Qualifier("ofiBpayInvestigationValidator")
	private OfiBpayInvestigationValidator ofiBpayInvestigationValidator;

	@Autowired
	@Qualifier("ofiErrorCorrectionValidator")
	private OfiErrorCorrectionValidator ofiErrorCorrectionValidator;

	private Long orgIdOverride = null;

	private final ServiceRequestServiceImpl serviceRequestService = CuscalServiceLocator.getService(
		ServiceRequestServiceImpl.class.getName());

}