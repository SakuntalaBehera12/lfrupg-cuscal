//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.controller;

import static au.com.cuscal.ticketing.common.Constants.CANCEL_NOTE;
import static au.com.cuscal.ticketing.common.Constants.CANCEL_THIS_REQUEST;
import static au.com.cuscal.ticketing.common.Constants.EDIT_TICKET;
import static au.com.cuscal.ticketing.common.Constants.IS_A_MASTERCARD_DISPUTE;
import static au.com.cuscal.ticketing.common.Constants.REVERSE_THIS_REQUEST;
import static au.com.cuscal.ticketing.common.Constants.TICKET_ID;
import static au.com.cuscal.ticketing.common.Constants.TRUE;

import au.com.cuscal.connect.util.resource.dxp.UserUtil;
import au.com.cuscal.connect.util.resource.dxp.UserUtilImpl;
import au.com.cuscal.connect.util.resource.model.VsmUser;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.category.AuditCategories;
import au.com.cuscal.framework.audit.category.AuditTicketing;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.audit.xml.XmlMessage;
import au.com.cuscal.framework.webservices.selfservice.AddTicketNoteResponse;
import au.com.cuscal.framework.webservices.selfservice.AddTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.AttachmentDataType;
import au.com.cuscal.framework.webservices.selfservice.AttachmentSummaryType;
import au.com.cuscal.framework.webservices.selfservice.AttributesType;
import au.com.cuscal.framework.webservices.selfservice.DownloadAttachmentRequest;
import au.com.cuscal.framework.webservices.selfservice.DownloadAttachmentResponse;
import au.com.cuscal.framework.webservices.selfservice.FindTicketsResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.selfservice.TouchTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.UploadAttachmentRequest;
import au.com.cuscal.framework.webservices.selfservice.UploadAttachmentResponse;
import au.com.cuscal.framework.webservices.transaction.Customer;
import au.com.cuscal.ticketing.common.CommonUtil;
import au.com.cuscal.ticketing.common.Constants;
import au.com.cuscal.ticketing.common.SelfServiceProperties;
import au.com.cuscal.ticketing.domain.ServiceCatalogueData;
import au.com.cuscal.ticketing.domain.Ticket;
import au.com.cuscal.ticketing.domain.TicketFilter;
import au.com.cuscal.ticketing.domain.TicketNote;
import au.com.cuscal.ticketing.domain.TicketUser;
import au.com.cuscal.ticketing.domain.view.Attachment;
import au.com.cuscal.ticketing.domain.view.DecoratedTicket;
import au.com.cuscal.ticketing.forms.ServiceCatalogueForm;
import au.com.cuscal.ticketing.services.CuscalTicketingService;
import au.com.cuscal.ticketing.validator.ServiceCatalogueValidator;
import au.com.cuscal.ticketing.validator.TicketFilterValidator;
import au.com.cuscal.ticketing.validator.TicketLoadValidator;
import au.com.cuscal.ticketing.validator.TicketNoteValidator;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;
import com.liferay.portletmvc4spring.bind.annotation.ResourceMapping;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import javax.mail.util.ByteArrayDataSource;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.MutableRenderParameters;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("cuscalTicketingController")
@RequestMapping({"VIEW"})
public class CuscalTicketingController {

	/**
	 * Find Regular Roles assigned to an organisation (Not to be confused with
	 * organisation roles)
	 *
	 * @param user
	 * @return List<Role>
	 */
	public static List<Role> findRegularRolesAssignedToUsersOrganisationList(
		User user) {

		List<Role> finalRolesLst = new ArrayList<>();
		List<Role> rolesLst = null;

		try {
			List<Organization> orgs = user.getOrganizations();

			for (Organization org : orgs) {
				rolesLst = new ArrayList<>();
				long groupId = org.getGroup(
				).getGroupId();

				rolesLst = RoleLocalServiceUtil.getGroupRoles(groupId);

				for (Role role : rolesLst) {
					logger.debug(
						"findRegularRolesAssignedToUsersOrganisationList - Org roles for user:" +
							role.getName());
					finalRolesLst.add(role);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return finalRolesLst;
	}

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

	/**
	 * @param request
	 * @param response
	 * @param ticketNote
	 * @param bindingResult
	 */
	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.ADD_NOTE_ACTION
	)
	public void addNoteAction(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("ticketNote") TicketNote ticketNote,
		BindingResult bindingResult) {

		logger.debug("addNoteAction - start");

		ticketNoteValidator.validate(ticketNote, bindingResult);
		String id = ParamUtil.getString(request, "i");

		// Only make the request if there are no validation errors.

		if (!bindingResult.hasErrors()) {
			TktRequestHeader header = createRequestHeader(request);

			if (null != header) {
				if (StringUtils.isNotBlank(id)) {

					// cancel this request starts

					String cancelThisRequest = ParamUtil.getString(
						request, CANCEL_THIS_REQUEST);
					String reverseThisRequest = ParamUtil.getString(
						request, REVERSE_THIS_REQUEST);
					logger.debug(
						"reverseThisRequest =" + reverseThisRequest +
							" and cancelThisRequest=" + cancelThisRequest);
					String serviceStatus = null;

					if (Constants.TRUE.equals(cancelThisRequest) ||
						Constants.TRUE.equals(reverseThisRequest)) {

						request.setAttribute(CANCEL_THIS_REQUEST, Boolean.TRUE);
						request.setAttribute(
							REVERSE_THIS_REQUEST, Boolean.TRUE);

						String exportIPM = ParamUtil.getString(
							request, Constants.EXPORT_IPM);
						logger.debug("exportIPM=" + exportIPM);

						if (Constants.TICKET_EXPOERT_IPM_STATUS_PROCESSED.
								equalsIgnoreCase(exportIPM)) {

							serviceStatus =
								Constants.SERVICE_REQUEST_STATUS_REVERSED;
						}
						else {
							serviceStatus =
								Constants.SERVICE_REQUEST_STATUS_CANCELLED;
						}

						logger.debug("serviceStatus=" + serviceStatus);
					}

					// cancel this request ends

					Boolean addNoteSuccess = callWebServiceToAddNoteToTicket(
						id, ticketNote.getNote(), serviceStatus, header,
						request, response);

					if (addNoteSuccess) {
						ticketNote.setNote("");
						request.setAttribute("ticketId", id);
						request.setAttribute("addNoteFailed", Boolean.FALSE);

						String portletId = (String)request.getAttribute(
							WebKeys.PORTLET_ID);
						ThemeDisplay themeDisplay =
							(ThemeDisplay)request.getAttribute(
								WebKeys.THEME_DISPLAY);

						PortletURL redirectUrl = PortletURLFactoryUtil.create(
							PortalUtil.getHttpServletRequest(request),
							portletId,
							themeDisplay.getLayout(
							).getPlid(),
							PortletRequest.RENDER_PHASE);

						redirectUrl.setParameter(Constants.I, id);
						redirectUrl.setParameter(
							Constants.RENDER, Constants.TICKET_DETAILS_RENDER);

						try {
							response.sendRedirect(
								redirectUrl.toString(
								).replace(
									"&amp;", "&"
								));
						}
						catch (IOException e) {
							logger.error(
								"addNoteAction - " + e.getMessage(), e);
						}
					}
					else {
						request.setAttribute("addNoteFailed", Boolean.TRUE);
						request.setAttribute("ticketId", id);
						response.setRenderParameter(
							Constants.RENDER, Constants.TICKET_DETAILS_RENDER);
					}
				}
				else {
					logger.error("addNoteAction - ticket id is null");
					XmlMessage xmlMessage = new XmlMessage();

					xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
						header, xmlMessage);

					xmlMessage.addParameter("ticketId", id);
					xmlMessage.addComment("reason", "Ticket ID is null");

					CommonUtil.auditFailXmlMessage(
						xmlMessage, AuditTicketing.TICKETING_ADD_NOTE, request,
						response);

					response.setRenderParameter(
						Constants.RENDER, Constants.ERROR_PAGE_RENDER);
				}
			}
			else {
				logger.error("addNoteAction - request header is null");
				XmlMessage xmlMessage = new XmlMessage();

				xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
					header, xmlMessage);
				xmlMessage.addParameter("ticketId", id);

				CommonUtil.auditFailXmlMessage(
					xmlMessage, AuditTicketing.TICKETING_ADD_NOTE, request,
					response);

				response.setRenderParameter(
					Constants.RENDER, Constants.ERROR_PAGE_RENDER);
			}
		}
		else {
			logger.error("addNoteAction - There are validation errors");
			request.setAttribute("ticketId", id);
			request.setAttribute("addNoteFailed", Boolean.TRUE);
			response.setRenderParameter(
				Constants.RENDER, Constants.TICKET_DETAILS_RENDER);
		}

		logger.debug("addNoteAction - end");
	}

	/**
	 * @param request
	 * @param response
	 * @param ticket
	 */
	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.ADD_TICKET_ACTION
	)
	public void addTicketAction(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("ticket") Ticket ticket) {

		logger.debug("addTicketAction - start");

		TktRequestHeader header = createRequestHeader(request);

		if (null != header) {
			TicketType ticketType = callWebServiceToAddTicket(
				ticket, header, request, response);

			if (null != ticketType) {
				DecoratedTicket decoratedTicket = new DecoratedTicket(
					ticketType);

				request.setAttribute(Constants.TICKET, decoratedTicket);
				response.setRenderParameter(
					Constants.RENDER, Constants.ADD_TICKET_SUCCESS_RENDER);
			}
			else {
				logger.error("addTicketAction - Cannot add ticket.");
				response.setRenderParameter(
					Constants.RENDER, Constants.ERROR_PAGE_RENDER);
			}
		}
		else {
			logger.error(
				"addTicketAction - Cannot add ticket because header is null");
			response.setRenderParameter(
				Constants.RENDER, Constants.ERROR_PAGE_RENDER);
		}

		/**
		 * String portletName = (String)
		 * request.getAttribute(WebKeys.PORTLET_ID); ThemeDisplay themeDisplay =
		 * (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY); PortletURL
		 * redirectUrl = PortletURLFactoryUtil
		 * .create(PortalUtil.getHttpServletRequest(request), portletName,
		 * themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
		 * redirectUrl.setParameter("render", "addTicketSuccess"); try {
		 * response.sendRedirect(redirectUrl.toString().replace("&amp;", "&"); }
		 * catch (IOException e) { e.printStackTrace(); }
		 */
		logger.debug("addTicketAction - end");
	}

	@ResourceMapping(Constants.CANCEL_REQUEST)
	public void cancelRequest(
		ResourceRequest request, ResourceResponse response) {

		logger.debug("cancelRequest - Resource Mapping here");

		String ticketId = request.getParameter(TICKET_ID);
		logger.debug("ticketId =" + ticketId);
		String maskedFieldsJson = "";
		String cancelThisRequest = ParamUtil.getString(
			request, CANCEL_THIS_REQUEST);
		String cancelNote = ParamUtil.getString(request, CANCEL_NOTE);
		String reverseThisRequest = ParamUtil.getString(
			request, REVERSE_THIS_REQUEST);
		logger.debug(
			"reverseThisRequest =" + reverseThisRequest +
				"cancelThisRequest =" + cancelThisRequest);

		Map<String, String> protectedAttributes = new HashMap<>();

		protectedAttributes.put("error", "NoError");

		if (Constants.TRUE.equals(cancelThisRequest) ||
			Constants.TRUE.equals(reverseThisRequest)) {

			protectedAttributes = ticketNoteValidator.validateCancelNote(
				protectedAttributes, cancelNote);
		}

		try {
			JSONObject jsonObject = new JSONObject(protectedAttributes);

			maskedFieldsJson = jsonObject.toString();
			PrintWriter out = response.getWriter();
			response.setContentType("json");
			response.setCharacterEncoding("utf-8");
			out.print(maskedFieldsJson);
		}
		catch (IOException ex) {
			logger.error(
				"cancelRequest - Exception occurred during cancelRequest:",
				ex.getCause());
		}
	}

	/**
	 * Get the header from the global header instance. Will save time when we do
	 * not have to call the entity service over and over again to get the
	 * Organisation short name.
	 *
	 * @param request
	 *            <code>PortletRequest</code>
	 * @return VsmRequestHeader
	 */
	public TktRequestHeader createRequestHeader(PortletRequest request) {
		TktRequestHeader header = null;
		HttpSession session = getSessionFromRequest(request);
		UserUtil userUtil = new UserUtilImpl();

		if (null != request) {
			try {
				User user = CommonUtil.getUserFromRequest(request);

				if (null != user) {
					logger.debug("createRequestHeader - user is not null");
					VsmUser vsmUser;

					if (null == session.getAttribute("vsmUser")) {
						vsmUser = userUtil.retrieveUserFromGetTicketUserService(
							user);
						session.setAttribute("vsmUser", vsmUser);
					}
					else {
						vsmUser = (VsmUser)session.getAttribute("vsmUser");
					}

					if (null != vsmUser) {
						header = new TktRequestHeader();
						header.setOrigin(AuditOrigin.PORTAL_ORIGIN);
						header.setUserId(vsmUser.getUserId());
						header.setUserOrgId(vsmUser.getOrgShortName());

						Organization org = CommonUtil.getOrganisationForUser(
							user);

						if (null != org) {
							header.setUserOrgName(org.getName());
						} // User organisation is null
					}
				} // User object is null.
			}
			catch (Exception e) {
				logger.error("createRequestHeader - " + e.getMessage(), e);
			}
		}

		return header;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	// TODO

	@ResourceMapping(Constants.DOWNLOAD_ATTACHMENT)
	public void downloadAttachment(
		ResourceRequest request, ResourceResponse response) {

		logger.debug("downloadAttachment - start");

		PortletSession portletSession = request.getPortletSession();

		String userSteppedUp = GetterUtil.getString(
			portletSession.getAttribute(
				Constants._USER_STEPPEDUP, PortletSession.APPLICATION_SCOPE));

		if (Constants.TRUE.equalsIgnoreCase(userSteppedUp)) {
			Attachment attachment = callDownloadAttachmentWebservice(
				request, response);

			if (attachment != null) {
				response.setContentType(attachment.getMimeType());
				response.addProperty(
					"Content-Disposition",
					"attachment; filename=\"" + attachment.getFileName() +
						"\"");

				try {
					BufferedInputStream bufferedInputStream =
						new BufferedInputStream(attachment.getFile());

					byte[] bytes = new byte[bufferedInputStream.available()];
					OutputStream outputStream =
						response.getPortletOutputStream();

					response.setContentLength(bytes.length);
					int aByte = 0;

					while ((aByte = bufferedInputStream.read()) != -1) {
						outputStream.write(aByte);
					}

					outputStream.flush();
					bufferedInputStream.close();
					response.flushBuffer();
				}
				catch (IOException ex) {
					logger.error(
						"downloadAttachment - Exception occurred during output of file stream:",
						ex.getCause());
				}
			}
			else {
				logger.error("downloadAttachment - Error retrieving file");
			}
		}
		else {
			logger.error(
				"downloadAttachment - User trying to download file without stepping up");
		}

		logger.debug("downloadAttachment - end");
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.TICKET_FILTER_RENDER
	)
	public String filterTicketsListPage(
		RenderRequest request, RenderResponse response,
		@ModelAttribute(Constants.TICKET_FILTER) TicketFilter ticketFilter,
		BindingResult bindingResult) {

		logger.debug("filterTicketsListPage - start");
		String page = "";

		ticketFilterValidator.validate(ticketFilter, bindingResult);
		TktRequestHeader header = createRequestHeader(request);
		HttpSession session = getSessionFromRequest(request);
		/* adding for BPay and DE Service Catalogue */
		ServiceCatalogueData serviceCatalogueData = CommonUtil.populateSCData(
			selfServiceProperties);

		if (bindingResult.hasErrors()) {
			logger.debug("ticketFilterAction - there are validation errors");
			request.setAttribute("tickets", null);
			session.removeAttribute("sessionTickets");

			setupTicketFilterValues(
				request, ticketFilter, header, Boolean.FALSE);
			request.setAttribute("introText", selfServiceIntroText());
			request.setAttribute("heading", selfServiceHeading());
			/* adding for BPay and DE Service Catalogue */
			request.setAttribute("serviceCatalogueData", serviceCatalogueData);
			page = Constants.HOMEPAGE;
		}
		else {
			List<DecoratedTicket> tickets = callWebServiceToGetTicketList(
				ticketFilter, header, request, response);

			if (null != tickets) {
				request.setAttribute("tickets", tickets);

				setupTicketFilterValues(
					request, ticketFilter, header, Boolean.FALSE);

				request.setAttribute("heading", selfServiceHeading());
				request.setAttribute("introText", selfServiceIntroText());
				/* adding for BPay and DE Service Catalogue */
				request.setAttribute(
					"serviceCatalogueData", serviceCatalogueData);

				page = Constants.HOMEPAGE;
			}
			else {
				logger.error(
					"ticketFilterAction - error getting list of tickets for user");
				request.setAttribute("invalidResponse", Boolean.TRUE);
				page = Constants.ERROR_PAGE;
			}

			page = loadActionTickets(header, request, response, page);
		}

		logger.debug("filterTicketsListPage - end");

		return page;
	}

	@ModelAttribute(Constants.TICKET)
	public Ticket getCommandObject() {
		return new Ticket();
	}

	@ModelAttribute("serviceCatalogueForm")
	public ServiceCatalogueForm getCommandObjectServiceCatalogueForm() {
		return new ServiceCatalogueForm();
	}

	@ModelAttribute(Constants.TICKET_DETAILS_COMMAND)
	public TicketFilter getCommandObjectTicketDetails() {
		return new TicketFilter();
	}

	@ModelAttribute(Constants.TICKET_FILTER)
	public TicketFilter getCommandObjectTicketFilter() {
		return new TicketFilter();
	}

	/**
	 * @return Long
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
	 * Method to render the ticket details when the user searches for the
	 * request using the request number.
	 *
	 * @param request
	 * @param response
	 * @param ticketFilter
	 * @param ticketNote
	 * @return
	 */
	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.LOAD_TICKET_RENDER
	)
	@SuppressWarnings("unchecked")
	public String loadTicketPage(
		RenderRequest request, RenderResponse response,
		@ModelAttribute(Constants.TICKET_FILTER) TicketFilter ticketFilter,
		BindingResult bindingResult, Model model) {

		logger.debug("loadTicketPage - start");

		String page = "";
		TktRequestHeader header = createRequestHeader(request);
		HttpSession session = getSessionFromRequest(request);
		XmlMessage xmlMessage = new XmlMessage();
		/* adding for BPay and DE Service Catalogue */
		ServiceCatalogueData serviceCatalogueData = CommonUtil.populateSCData(
			selfServiceProperties);

		TicketNote ticketNote;

		ticketLoadValidator.validate(ticketFilter, bindingResult);

		if (!bindingResult.hasErrors()) {
			if (null != header) {
				String id = StringUtils.trim(ticketFilter.getTicketNumber());

				logger.debug("loadTicketPage - getting details for: " + id);

				if (StringUtils.isNotBlank(id)) {
					DecoratedTicket decoratedTicket =
						callWebServiceToGetTicketDetails(
							id, Boolean.TRUE, header, request, response,
							Boolean.FALSE);

					decoratedTicket.setOrgMap(
						(Map<String, String>)session.getAttribute("orgMap"));
					decoratedTicket.setCuscalShortNamesList(
						(List<String>)session.getAttribute(
							"cuscalShortNamesList"));
					decoratedTicket.setCuscalOrgDefaultName(
						(String)session.getAttribute("cuscalOrgDefaultName"));

					if ((Boolean)request.getAttribute(
							"ticketDetailsNotFound")) {

						request.setAttribute("ticketNotFound", Boolean.TRUE);
						setupTicketFilterValues(
							request, ticketFilter, header, Boolean.TRUE);
					}
					else {
						long typeIdLong = decoratedTicket.getTicket(
						).getType(
						).getTypeId();
						boolean isMasterCardDispute = isMasterCardDispute(
							typeIdLong);

						if (isMasterCardDispute) {
							request.setAttribute(IS_A_MASTERCARD_DISPUTE, TRUE);

							// cancel

							boolean cancelRequestFlag =
								CommonUtil.isEligibleToCancelMasterCardDispute(
									decoratedTicket);
							request.setAttribute(
								Constants.CANCEL_REQUEST_FLAG,
								Boolean.valueOf(cancelRequestFlag));

							// reverse

							boolean reverseRequestFlag =
								CommonUtil.isEligibleToReverseMasterCardDispute(
									selfServiceProperties, decoratedTicket,
									typeIdLong);
							request.setAttribute(
								Constants.REVERSE_REQUEST_FLAG,
								Boolean.valueOf(reverseRequestFlag));

							// edit

							boolean editRequestFlag =
								CommonUtil.isEligibleToEditMasterCardDispute(
									decoratedTicket, typeIdLong);
							request.setAttribute(
								Constants.EDIT_REQUEST_FLAG,
								Boolean.valueOf(editRequestFlag));
						}

						// TODO: On Hold status check

						if (Constants.ON_HOLD_STATUS.equalsIgnoreCase(
								decoratedTicket.getTicketStatus())) {

							if ((Constants.CHARGEBACK_ID == typeIdLong) ||
								isMasterCardDispute) {

								String chargebackLink = createEditTicketUrl(
									decoratedTicket.getTicket(
									).getId(),
									request, isMasterCardDispute);

								if (chargebackLink.indexOf("&amp;") != -1) {
									chargebackLink = chargebackLink.replace(
										"&amp;", "&");
								}

								if (StringUtils.isNotBlank(chargebackLink)) {
									request.setAttribute(
										Constants.CHARGEBACK_LINK,
										chargebackLink);
								}
							}
						}

						String typeId = String.valueOf(typeIdLong);

						if (Constants.ON_HOLD_STATUS.equalsIgnoreCase(
								decoratedTicket.getTicketStatus()) &&
							serviceCatalogueData.getTypeIdMap(
							).containsValue(
								typeId
							)) {

							String scItemId = null;

							for (Map.Entry<Long, String> entry :
									serviceCatalogueData.getTypeIdMap(
									).entrySet()) {

								if (typeId.equals(entry.getValue())) {
									scItemId = entry.getKey(
									).toString();

									break;
								}
							}

							request.setAttribute(
								Constants.SC_ITEM_ID, scItemId);
						}

						request.setAttribute("ticketDetails", decoratedTicket);

						try {
							if (doesUserHaveServiceRequestAttachmentRole(
									request)) {

								request.setAttribute(
									Constants.ATTACHMENT_ROLE, Boolean.TRUE);
							}
							else {
								request.setAttribute(
									Constants.ATTACHMENT_ROLE, Boolean.FALSE);
							}

							if (doesUserHaveServiceRequestNoteRole(request)) {
								request.setAttribute(
									Constants.NOTES_ROLE, Boolean.TRUE);
							}
							else {
								request.setAttribute(
									Constants.NOTES_ROLE, Boolean.FALSE);
							}
						}
						catch (SystemException se) {
							logger.error(
								"loadTicketPage - Error when checking user roles",
								se);
						}
						catch (PortalException pe) {
							logger.error(
								"loadTicketPage - Error when checking user roles",
								pe);
						}

						if (!setupTicketAndCommonAttributes(
								request, decoratedTicket, header)) {

							page = Constants.ERROR_PAGE;
						}
					}

					// Add the TicketNote class to the model for the form.

					if (model.containsAttribute(Constants.TICKET_NOTE)) {
						ticketNote = (TicketNote)model.asMap(
						).get(
							Constants.TICKET_NOTE
						);
					}
					else {
						ticketNote = new TicketNote();
					}

					model.addAttribute(Constants.TICKET_NOTE, ticketNote);

					request.setAttribute("destinationTicketId", id);
					page = Constants.TICKET_DETAILS_PAGE;
				}
				else {
					logger.error("loadTicketPage - Ticket ID is null.");
					xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
						header, xmlMessage);
					xmlMessage.addParameter("ticketId", id);
					xmlMessage.addComment("reason", "Ticket ID is null");

					CommonUtil.auditFailXmlMessage(
						xmlMessage, AuditCategories.TICKETING_DETAIL, request,
						response);

					page = Constants.ERROR_PAGE;
				}
			}
			else {
				logger.error("loadTicketPage - header is null.");

				xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
					header, xmlMessage);

				CommonUtil.auditFailXmlMessage(
					xmlMessage, AuditCategories.TICKETING_DETAIL, request,
					response);

				page = Constants.ERROR_PAGE;
			}
		}
		else {
			List<DecoratedTicket> tickets =
				(ArrayList<DecoratedTicket>)session.getAttribute(
					"sessionTickets");

			setupTicketFilterValues(
				request, ticketFilter, header, Boolean.TRUE);

			request.setAttribute("tickets", tickets);
			/* adding for BPay and DE Service Catalogue */
			request.setAttribute("serviceCatalogueData", serviceCatalogueData);
			page = Constants.HOMEPAGE;
			logger.debug(
				"loadTicketPage - " + session.getAttribute("sessionTickets"));
		}

		logger.debug("loadTicketPage - end");

		return page;
	}

	/**
	 * Get the orgShortName for the logged in user's organisation.
	 *
	 * @return String
	 */
	public String orgShortName(Long orgId) {
		String orgShortName = null;

		if (null != orgIdOverride) {
			orgShortName = ticketingService.getOrgShortName(orgIdOverride);
		}
		else {
			orgShortName = ticketingService.getOrgShortName(orgId);
		}

		return orgShortName;
	}

	/**
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
				DecoratedTicket ticket = callWebServiceToGetTicketDetails(
					ticketId, Boolean.FALSE, header, request, response,
					Boolean.TRUE);

				if (null != ticket) {
					for (AttributesType att : ticket.getAttributes()) {
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

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.ADD_NOTE_SUCCESS_RENDER
	)
	public String showAddNoteSuccessPage(
		RenderRequest request, RenderResponse response) {

		logger.debug("showAddNoteSuccessPage - start");

		logger.debug("showAddNoteSuccessPage - end");

		return Constants.TICKET_DETAILS_PAGE;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.ADD_TICKET_RENDER
	)
	public String showAddTicketPage(
		RenderRequest request, RenderResponse response,
		@ModelAttribute("ticket") Ticket ticket) {

		logger.debug("showAddTicketPage - start");

		String page = "";
		TicketUser user = CommonUtil.getUserDetailsFromRequest(request);

		if (null != user) {
			ticket.setUser(user);
			page = Constants.ADD_TICKET_PAGE;
		}
		else {
			page = Constants.ERROR_PAGE;
		}

		logger.debug("showAddTicketPage - end");

		return page;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.ADD_TICKET_SUCCESS_RENDER
	)
	public String showAddTicketSuccessPage(
		RenderRequest request, RenderResponse response) {

		logger.debug("showAddTicketSuccessPage - start");

		logger.debug("showAddTicketSuccessPage - end");

		return Constants.ADD_TICKET_SUCCESS_PAGE;
	}

	/**
	 * @param request
	 * @param response
	 * @param ticketNote
	 * @return
	 */
	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.TICKET_DETAILS_RENDER
	)
	@SuppressWarnings("unchecked")
	public String showDetailsPage(
		RenderRequest request, RenderResponse response,
		@ModelAttribute(Constants.TICKET_FILTER) TicketFilter ticketFilter,
		Model model) {

		logger.debug("showDetailsPage - start");

		String page = "";
		TktRequestHeader header = createRequestHeader(request);
		HttpSession session = getSessionFromRequest(request);
		XmlMessage xmlMessage = new XmlMessage();

		setCuscalShortNamesList(request);
		setCuscalOrgDefaultName(request);

		// This is done because with Spring forms the render phase keeps
		// resetting the bindingResult error messages there by not displaying
		// them on the screen.

		TicketNote ticketNote;

		if (model.containsAttribute(Constants.TICKET_NOTE)) {
			ticketNote = (TicketNote)model.asMap(
			).get(
				Constants.TICKET_NOTE
			);
		}
		else {
			ticketNote = new TicketNote();
		}

		model.addAttribute(Constants.TICKET_NOTE, ticketNote);

		if (null != header) {
			String id = "";

			id = (String)request.getAttribute("ticketId");

			if (StringUtils.isBlank(id)) {
				id = ParamUtil.getString(request, "i");

				if (StringUtils.isBlank(id))
					id = ticketFilter.getTicketNumber();
			}

			logger.debug("showDetailsPage - getting details for: " + id);

			if (StringUtils.isNotBlank(id)) {
				DecoratedTicket decoratedTicket =
					callWebServiceToGetTicketDetails(
						id, Boolean.FALSE, header, request, response,
						Boolean.FALSE);

				decoratedTicket.setOrgMap(
					(Map<String, String>)session.getAttribute("orgMap"));
				decoratedTicket.setCuscalShortNamesList(
					(List<String>)session.getAttribute("cuscalShortNamesList"));
				decoratedTicket.setCuscalOrgDefaultName(
					(String)session.getAttribute("cuscalOrgDefaultName"));

				if ((Boolean)request.getAttribute("ticketDetailsNotFound")) {
					request.setAttribute("ticketNotFound", Boolean.TRUE);
					setupTicketFilterValues(
						request, ticketFilter, header, Boolean.TRUE);
				}
				else {
					if (decoratedTicket.getOrgMap() == null) {
						setupTicketFilterValues(
							request, ticketFilter, header, Boolean.TRUE);
						decoratedTicket.setOrgMap(
							(Map<String, String>)session.getAttribute(
								"orgMap"));
					}

					// Update the Last Read Timestamp for non-cuscal users.

					if (!StringUtils.equalsIgnoreCase(
							Constants.CUSCAL, header.getUserOrgId())) {

						TicketType currentTicket = decoratedTicket.getTicket();

						currentTicket.setLastReadTimestamp(
							CommonUtil.getXmlCalendarDate(new Date()));
						callWebServiceToUpdateTicket(
							currentTicket, header, request, response);
					}

					long typeIdLong = decoratedTicket.getTicket(
					).getType(
					).getTypeId();
					boolean isMasterCardDispute = isMasterCardDispute(
						typeIdLong);

					if (isMasterCardDispute) {
						request.setAttribute(IS_A_MASTERCARD_DISPUTE, TRUE);

						// cancel

						boolean cancelRequestFlag =
							CommonUtil.isEligibleToCancelMasterCardDispute(
								decoratedTicket);
						request.setAttribute(
							Constants.CANCEL_REQUEST_FLAG,
							Boolean.valueOf(cancelRequestFlag));

						// reverse

						boolean reverseRequestFlag =
							CommonUtil.isEligibleToReverseMasterCardDispute(
								selfServiceProperties, decoratedTicket,
								typeIdLong);
						request.setAttribute(
							Constants.REVERSE_REQUEST_FLAG,
							Boolean.valueOf(reverseRequestFlag));

						// edit

						boolean editRequestFlag =
							CommonUtil.isEligibleToEditMasterCardDispute(
								decoratedTicket, typeIdLong);
						request.setAttribute(
							Constants.EDIT_REQUEST_FLAG,
							Boolean.valueOf(editRequestFlag));
					}

					// TODO: change this to on hold when the status is ready on
					// VSM

					if (Constants.ON_HOLD_STATUS.equalsIgnoreCase(
							decoratedTicket.getTicketStatus())) {

						if ((Constants.CHARGEBACK_ID == typeIdLong) ||
							isMasterCardDispute) {

							String chargebackLink = createEditTicketUrl(
								decoratedTicket.getTicket(
								).getId(),
								request, isMasterCardDispute);

							if (chargebackLink.indexOf("&amp;") != -1) {
								chargebackLink = chargebackLink.replace(
									"&amp;", "&");
							}

							if (StringUtils.isNotBlank(chargebackLink)) {
								request.setAttribute(
									Constants.CHARGEBACK_LINK, chargebackLink);
							}
						}
					}

					ServiceCatalogueData serviceCatalogueData =
						CommonUtil.populateSCData(selfServiceProperties);
					String typeId = String.valueOf(typeIdLong);

					if (Constants.ON_HOLD_STATUS.equalsIgnoreCase(
							decoratedTicket.getTicketStatus()) &&
						serviceCatalogueData.getTypeIdMap(
						).containsValue(
							typeId
						)) {

						String scItemId = null;

						for (Map.Entry<Long, String> entry :
								serviceCatalogueData.getTypeIdMap(
								).entrySet()) {

							if (typeId.equals(entry.getValue())) {
								scItemId = entry.getKey(
								).toString();

								break;
							}
						}

						request.setAttribute(Constants.SC_ITEM_ID, scItemId);
					}

					request.setAttribute("ticketDetails", decoratedTicket);

					try {
						if (doesUserHaveServiceRequestAttachmentRole(request)) {
							request.setAttribute(
								Constants.ATTACHMENT_ROLE, Boolean.TRUE);
						}
						else {
							request.setAttribute(
								Constants.ATTACHMENT_ROLE, Boolean.FALSE);
						}

						if (doesUserHaveServiceRequestNoteRole(request)) {
							request.setAttribute(
								Constants.NOTES_ROLE, Boolean.TRUE);
						}
						else {
							request.setAttribute(
								Constants.NOTES_ROLE, Boolean.FALSE);
						}
					}
					catch (SystemException se) {
						logger.error(
							"showDetailsPage - Error when checking user roles",
							se);
					}
					catch (PortalException pe) {
						logger.error(
							"showDetailsPage - Error when checking user roles",
							pe);
					}

					if (!setupTicketAndCommonAttributes(
							request, decoratedTicket, header)) {

						page = Constants.ERROR_PAGE;
					}
				}

				page = Constants.TICKET_DETAILS_PAGE;
			}
			else {
				logger.error("showDetailsPage - Ticket ID is null.");
				xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
					header, xmlMessage);
				xmlMessage.addParameter("ticketId", id);
				xmlMessage.addComment("reason", "Ticket ID is null");

				CommonUtil.auditFailXmlMessage(
					xmlMessage, AuditCategories.TICKETING_DETAIL, request,
					response);

				page = Constants.ERROR_PAGE;
			}
		}
		else {
			logger.error("showDetailsPage - header is null.");
			xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
				header, xmlMessage);

			CommonUtil.auditFailXmlMessage(
				xmlMessage, AuditCategories.TICKETING_DETAIL, request,
				response);

			page = Constants.ERROR_PAGE;
		}

		if (null != session.getAttribute(Constants.USER_CANCEL)) {
			session.removeAttribute(Constants.USER_CANCEL);
		}

		logger.debug("showDetailsPage - end");

		return page;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(
		params = Constants.RENDER + Constants.EQUALS + Constants.ERROR_PAGE_RENDER
	)
	public String showErrorPage(
		RenderRequest request, RenderResponse response) {

		return Constants.ERROR_PAGE;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping
	public String showHomePage(
		RenderRequest request, RenderResponse response,
		@ModelAttribute(Constants.TICKET_FILTER) TicketFilter ticketFilter) {

		logger.debug("showHomePage - start");

		// TicketRequestHeader header = getHeader(request);

		TktRequestHeader header = createRequestHeader(request);
		String page = "";
		HttpSession session = getSessionFromRequest(request);
		setCuscalShortNamesList(request);
		setCuscalOrgDefaultName(request);
		session.removeAttribute("sessionTickets");
		/* adding for BPay and DE Service Catalogue */
		ServiceCatalogueData serviceCatalogueData = CommonUtil.populateSCData(
			selfServiceProperties);

		if (null != header) {
			logger.debug(
				"showHomePage - header org is: " + header.getUserOrgId());

			if (StringUtils.isNotBlank(header.getUserOrgId())) {
				setupTicketFilterValues(
					request, ticketFilter, header, Boolean.TRUE);

				List<DecoratedTicket> tickets = callWebServiceToGetTicketList(
					null, header, request, response);

				if (null != tickets) {
					request.setAttribute("heading", selfServiceHeading());
					request.setAttribute("introText", selfServiceIntroText());
					request.setAttribute("tickets", tickets);

					// Adding

					session.setAttribute("sessionTickets", tickets);

					/* adding for BPay and DE Service Catalogue */
					request.setAttribute(
						"serviceCatalogueData", serviceCatalogueData);

					request.setAttribute("invalidUserSetup", Boolean.FALSE);
					page = Constants.HOMEPAGE;
				}
				else {
					logger.error(
						"showHomePage - error getting list of tickets for user");
					request.setAttribute("invalidResponse", Boolean.TRUE);
					page = Constants.ERROR_PAGE;
				} // Cannot get list of tickets.

				page = loadActionTickets(header, request, response, page);
			}
			else {
				request.setAttribute("invalidUserSetup", Boolean.TRUE);
				page = Constants.ERROR_PAGE;
			} // user organisation is not present.
		}
		else {
			request.setAttribute("userTimedOut", Boolean.TRUE);
			page = Constants.ERROR_PAGE;
		} // header is null.

		logger.debug("showHomePage - end");

		return page;
	}

	/**
	 * Shows the search results of the Transaction
	 *
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

		PortletSession portletSession = request.getPortletSession(true);
		HttpSession httpSession = getSessionFromRequest(request);
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		logger.debug("stepUp - The ticketIds attribute is: " + ticketId);

		String userSteppedUpSession = GetterUtil.getString(
			portletSession.getAttribute(
				Constants._USER_STEPPEDUP, PortletSession.APPLICATION_SCOPE));

		if (userSteppedUpSession.isEmpty()) {
			userSteppedUpSession = GetterUtil.getString(
				httpSession.getAttribute(Constants._USER_STEPPEDUP));
		}

		logger.debug(
			"stepUp - The Stepped Up session attribute is: " +
				userSteppedUpSession);

		String userCancelSession = GetterUtil.getString(
			portletSession.getAttribute(
				Constants.USER_CANCEL, PortletSession.APPLICATION_SCOPE));

		if (userCancelSession.isEmpty()) {
			userCancelSession = GetterUtil.getString(
				httpSession.getAttribute(Constants.USER_CANCEL));
		}

		if (Constants.CANCEL.equals(userCancelSession)) {
			httpSession.setAttribute(Constants.USER_CANCEL, null);
			portletSession.setAttribute(
				Constants.USER_CANCEL, null, PortletSession.APPLICATION_SCOPE);

			request.setAttribute(Constants.IS_FA, isUserHas2FARole(request));
			httpSession.setAttribute(Constants._RETURN_URL, null);
			portletSession.setAttribute(
				Constants._RETURN_URL, null, PortletSession.APPLICATION_SCOPE);
			logger.debug("steppedup: value is: " + userSteppedUpSession);
		}
		else if (userSteppedUpSession.isEmpty() ||
				 Constants.FALSE.equals(userSteppedUpSession) ||
				 Constants.ERROR.equals(userSteppedUpSession)) {

			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			PortletURL link = PortletURLFactoryUtil.create(
				httpServletRequest, PortalUtil.getPortletId(request),
				themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

			MutableRenderParameters renderParameters =
				link.getRenderParameters();

			renderParameters.setValue(Constants.I, ticketId);
			renderParameters.setValue(
				Constants.RENDER, Constants.TICKET_DETAILS_RENDER);

			String currentUrl = link.toString(
			).replace(
				"&amp;", "&"
			);

			logger.debug("currentUrl: " + currentUrl);

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
			logger.debug("steppedup: value is  " + userSteppedUpSession);
		}

		logger.debug("stepUp - end");
	}

	/**
	 * Adding for BPay
	 *
	 * @param request
	 * @param response
	 * @param ticket
	 */
	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.SUBMIT_SC_ACTION
	)
	public void submitServiceCatalogue(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("serviceCatalogueForm") ServiceCatalogueForm
			serviceCatalogueForm,
		BindingResult bindingResult) {

		logger.debug("submitServiceCatalogue - start");

		serviceCatalogueValidator.validate(serviceCatalogueForm, bindingResult);

		if (bindingResult.hasErrors()) {// error scenario
			logger.debug(
				"submit service catalogue action - there are validation errors");
			response.setRenderParameter(Constants.RENDER, StringUtils.EMPTY);
		}
		else {
			ServiceCatalogueData serviceCatalogueData =
				CommonUtil.populateSCData(selfServiceProperties);

			String pageUrl = serviceCatalogueData.getUrlMap(
			).get(
				serviceCatalogueForm.getScItem()
			);

			try {
				response.sendRedirect("/".concat(pageUrl));
			}
			catch (IOException e) {
				logger.error(
					"submitServiceCatalogue - error redirecting page. " + e);
			}
		}

		logger.debug("submitServiceCatalogue - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_OFI_SC_ACTION
	)
	public void updateOfiSimpleServiceRequest(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("serviceCatalogueForm") ServiceCatalogueForm
			serviceCatalogueForm,
		BindingResult bindingResult) {

		logger.debug("updateSimpleServiceRequest - start");

		ServiceCatalogueData ofiServiceCatalogueData =
			CommonUtil.populateOfiSCData(selfServiceProperties);

		String typeId = StringUtils.trimToEmpty(
			request.getParameter(Constants.TYPE_ID));
		String pageUrl = null;

		for (Map.Entry<Long, String> entry :
				ofiServiceCatalogueData.getTypeIdMap(
				).entrySet()) {

			if (typeId.equals(entry.getValue())) {
				pageUrl = ofiServiceCatalogueData.getUrlMap(
				).get(
					entry.getKey()
				);

				break;
			}
		}

		if (pageUrl == null) {
			response.setRenderParameter(Constants.RENDER, StringUtils.EMPTY);
		}
		else {
			try {
				response.sendRedirect(
					"/".concat(
						pageUrl
					).concat(
						"?ticketId="
					).concat(
						request.getParameter(Constants.TICKET_ID)
					));
			}
			catch (IOException e) {
				logger.error(
					"updateSimpleServiceRequest - error redirecting page. " +
						e);
			}
		}

		logger.debug("updateSimpleServiceRequest - end");
	}

	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.UPDATE_SC_ACTION
	)
	public void updateSimpleServiceRequest(
		ActionRequest request, ActionResponse response,
		@ModelAttribute("serviceCatalogueForm") ServiceCatalogueForm
			serviceCatalogueForm,
		BindingResult bindingResult) {

		logger.debug("updateSimpleServiceRequest - start");

		ServiceCatalogueData serviceCatalogueData = CommonUtil.populateSCData(
			selfServiceProperties);

		String pageUrl = serviceCatalogueData.getUrlMap(
		).get(
			Long.valueOf(request.getParameter(Constants.SC_ITEM_ID))
		);

		try {
			response.sendRedirect(
				"/".concat(
					pageUrl
				).concat(
					"&ticketId="
				).concat(
					request.getParameter(Constants.TICKET_ID)
				));
		}
		catch (IOException e) {
			logger.error(
				"submitServiceCatalogue - error redirecting page. " + e);
		}

		logger.debug("updateSimpleServiceRequest - end");
	}

	/**
	 *
	 */
	@ActionMapping(
		params = Constants.ACTION + Constants.EQUALS + Constants.ADD_ATTACHMENT_ACTION
	)
	public void uploadFile(ActionRequest request, ActionResponse response) {
		logger.debug("uploadFile - start");

		UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(
			request);

		File file = uploadRequest.getFile("file");
		String ticketId = uploadRequest.getParameter("ticketId");

		TktRequestHeader header = createRequestHeader(request);

		if (null != file) {
			logger.debug(
				"uploadFile - file name: " + uploadRequest.getFileName("file"));
			logger.debug(
				"uploadFile - file Complete name: " +
					uploadRequest.getFullFileName("file"));
			logger.debug(
				"uploadFile - file Content type: " +
					uploadRequest.getContentType("file"));

			logger.debug("uploadFile - file size: " + file.length());

			try {
				FileInputStream fis = new FileInputStream(file);

				logger.debug(
					"uploadFile - File channel size: " +
						fis.getChannel(
						).size());

				String fileAttachmentTypes =
					selfServiceProperties.getSelfserviceProperty(
					).getProperty(
						"file.attachment.types"
					);
				String maxFileSize =
					selfServiceProperties.getSelfserviceProperty(
					).getProperty(
						"file.attachment.size"
					);

				if (StringUtils.isNotBlank(fileAttachmentTypes)) {
					String[] attachmentTypes = fileAttachmentTypes.split(
						Constants.COMMA);
					Boolean allowUpload = Boolean.FALSE;
					Boolean invalidFileType = Boolean.FALSE;
					Boolean invalidFileSize = Boolean.FALSE;

					for (String attachmentType : attachmentTypes) {
						if (uploadRequest.getFileName(
								"file"
							).toLowerCase(
							).endsWith(
								attachmentType
							)) {

							invalidFileType = Boolean.FALSE;
						}
						else {
							invalidFileType = Boolean.TRUE;
						}
					}

					Long fileSize = Long.valueOf(maxFileSize);

					if (fis.getChannel(
						).
							size() <= fileSize) {

						allowUpload = Boolean.TRUE;
						invalidFileSize = Boolean.FALSE;

						// The file has the right name and size.

					}
					else {
						invalidFileSize = Boolean.TRUE;

						// File size error. Break out of the loop.

					}

					if (allowUpload) {
						boolean success = callUploadAttachmentWebservice(
							fis, uploadRequest.getFileName("file"), ticketId,
							uploadRequest.getContentType("file"), header);

						XmlMessage xmlMessage = new XmlMessage();

						xmlMessage =
							CommonUtil.addHeaderInformationToXmlMessage(
								header, xmlMessage);
						xmlMessage =
							CommonUtil.addNewAttachmentParamsToXmlMessage(
								fis.getChannel(
								).size(),
								uploadRequest.getFileName("file"), ticketId,
								xmlMessage);

						// Create a String and audit the message.

						if (success) {
							CommonUtil.auditSuccessXmlMessage(
								xmlMessage,
								AuditTicketing.TICKETING_UPLOAD_ATTACHMENT,
								request, response);
						}
						else {
							CommonUtil.auditFailXmlMessage(
								xmlMessage,
								AuditTicketing.TICKETING_UPLOAD_ATTACHMENT,
								request, response);
						}

						logger.debug("uploadFile - this file can be uploaded");
					}
					else {
						if (invalidFileSize) {
							logger.error("uploadFile - File size too big");
							request.setAttribute("fileTooBig", Boolean.TRUE);
						}

						if (invalidFileType) {
							logger.error("uploadFile - File type invalid.");
							request.setAttribute("fileInvalid", Boolean.TRUE);
						}
					}
				}

				request.setAttribute("ticketId", ticketId);
				response.setRenderParameter(
					Constants.RENDER, Constants.TICKET_DETAILS_RENDER);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
				request.setAttribute("invalidResponse", Boolean.TRUE);
				response.setRenderParameter(
					Constants.RENDER, Constants.ERROR_PAGE_RENDER);
			}
			catch (IOException e) {
				e.printStackTrace();
				request.setAttribute("invalidResponse", Boolean.TRUE);
				response.setRenderParameter(
					Constants.RENDER, Constants.ERROR_PAGE_RENDER);
			}

			logger.debug("uploadFile - end");
		}
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	private Attachment callDownloadAttachmentWebservice(
		ResourceRequest request, ResourceResponse response) {

		logger.debug("callDownloadAttachmentWebservice - start");

		Attachment attachment = new Attachment();

		TktRequestHeader header = createRequestHeader(request);

		String attachmentId = request.getParameter(Constants.ATTACHMENT_ID);
		DownloadAttachmentRequest downloadRequest =
			new DownloadAttachmentRequest();

		downloadRequest.setHeader(header);

		long id = Long.valueOf(attachmentId);
		downloadRequest.setId(id);
		DownloadAttachmentResponse downloadResponse =
			ticketingService.downloadAttachment(downloadRequest);

		XmlMessage xmlMessage = new XmlMessage();

		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);

		xmlMessage.addParameter("attachmentId", attachmentId);

		boolean isSuccess = Boolean.FALSE;

		if (null != downloadResponse) {
			if (null != downloadResponse.getHeader()) {
				String statusCode = downloadResponse.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					isSuccess = Boolean.TRUE;
					AttachmentDataType attachmentType =
						downloadResponse.getAttachment();

					logger.debug("Filename " + attachmentType.getFilename());
					logger.debug("mimeType " + attachmentType.getMimeType());
					logger.debug("blob " + attachmentType.getBlob());

					if (null != attachmentType.getFilename()) {
						attachment.setFileName(attachmentType.getFilename());
						xmlMessage.addParameter(
							"filename", attachmentType.getFilename());
					}
					else {
						attachment.setFileName("");
						logger.error(
							"callDownloadAttachmentWebservice - No filename");
					}

					if (null != attachmentType.getMimeType()) {
						attachment.setMimeType(attachmentType.getMimeType());
						xmlMessage.addParameter(
							"mimeType", attachmentType.getMimeType());
					}
					else {
						attachment.setMimeType("text/plain; charset=utf-8");
						logger.error(
							"callDownloadAttachmentWebservice - No mimetype");
					}

					if (null != attachmentType.getBlob()) {
						DataHandler data = attachmentType.getBlob();

						try {
							InputStream is = data.getInputStream();

							attachment.setFile(is);
						}
						catch (IOException e) {
							logger.error(
								"callDownloadAttachmentWebservice - Error while retrieving data stream " +
									e.getMessage(),
								e);
						}
					}
					else {
						logger.error(
							"callDownloadAttachmentWebservice - No data found");
					}
				}
				else {
					logger.error(
						"callDownloadAttachmentWebservice - WS response status is not success: " +
							statusCode);
				}
			}
			else {
				logger.error(
					"callDownloadAttachmentWebservice - WS downloadAttachment response header is null");
			}
		}
		else {
			logger.error(
				"callDownloadAttachmentWebservice - WS downloadAttachment response is null");
		}

		// Create a String and audit the message.

		if (isSuccess) {
			CommonUtil.auditSuccessXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_DOWNLOAD_ATTACHMENT,
				request, response);
		}
		else {
			CommonUtil.auditFailXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_DOWNLOAD_ATTACHMENT,
				request, response);
		}

		logger.debug("callDownloadAttachmentWebservice - end");

		return attachment;
	}

	/**
	 * @param fis
	 * @param filename
	 * @param ticketId
	 * @param mimeType
	 * @param header
	 * @return
	 */
	private boolean callUploadAttachmentWebservice(
		FileInputStream fis, String filename, String ticketId, String mimeType,
		TktRequestHeader header) {

		logger.debug("callUploadAttachmentWebservice - start");

		UploadAttachmentRequest uploadAttachmentRequest =
			new UploadAttachmentRequest();

		boolean isSuccess = Boolean.FALSE;

		DataHandler handlerFile;
		String sendingMimeType = mimeType;

		if ((mimeType != null) &&
			mimeType.startsWith(_APPLICATION_VND_OPENXMLFORMATS)) {

			sendingMimeType = _APPLICATION_OCTET_STREAM;
		}

		try {
			handlerFile = new DataHandler(
				new ByteArrayDataSource(fis, _APPLICATION_OCTET_STREAM));
			uploadAttachmentRequest.setAttachmentData(handlerFile);
		}
		catch (IOException e) {
			logger.error(
				"callUploadAttachmentWebservice - Error with Upload " +
					e.getMessage(),
				e);
		}

		uploadAttachmentRequest.setFilename(filename);
		uploadAttachmentRequest.setTitle(filename);
		uploadAttachmentRequest.setHeader(header);
		uploadAttachmentRequest.setMimeType(sendingMimeType);

		try {
			uploadAttachmentRequest.setTicketId(Long.parseLong(ticketId));
		}
		catch (NumberFormatException e) {
			logger.error(
				"callUploadAttachmentWebservice - TicketId is invalid " +
					ticketId + e);
		}

		UploadAttachmentResponse response = ticketingService.uploadAttachment(
			uploadAttachmentRequest);

		if (null != response) {
			if (null != response.getHeader()) {
				String statusCode = response.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					isSuccess = Boolean.TRUE;

					List<AttachmentSummaryType> attachments =
						response.getAttachments();

					for (AttachmentSummaryType attachment : attachments) {
						logger.debug("Filename " + attachment.getFilename());
						logger.debug("Id " + attachment.getId());
					}

					logger.debug(response.getAttachments());

					// do nothing?

				}
				else {
					logger.error(
						"uploadAttachment - WS response status is not success: " +
							statusCode);
				}
			}
			else {
				logger.error(
					"uploadAttachment - WS add ticket response header is null");
			}
		}
		else {
			logger.error("uploadAttachment - WS add ticket response is null");
		}

		logger.debug("callUploadAttachmentWebservice - end");

		return isSuccess;
	}

	/**
	 * @param id
	 * @param header
	 * @return
	 */
	private Boolean callWebServiceToAddNoteToTicket(
		String id, String note, String serviceStatus, TktRequestHeader header,
		PortletRequest request, PortletResponse response) {

		logger.debug("callWebServiceToAddNoteToTicket - start");

		XmlMessage xmlMessage = new XmlMessage();
		Boolean isSuccess = Boolean.FALSE;
		AddTicketNoteResponse responseType = ticketingService.addNote(
			id, note, serviceStatus, header);
		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);

		/**
		 * xmlMessage = AuditWebServicesUtil
		 * .addVsmRequestHeaderElementsToXmlMessage(header, xmlMessage);
		 */
		xmlMessage.addParameter(TICKET_ID, id);

		if (null != responseType) {
			if (null != responseType.getHeader()) {
				String statusCode = responseType.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					isSuccess = Boolean.TRUE;
					xmlMessage.addResult(
						"noteId",
						String.valueOf(
							responseType.getNote(
							).getNoteId()));

					// add comment

					if (StringUtils.isNotBlank(serviceStatus)) {

						// to update the default note cancelled by

						TicketUser user = CommonUtil.getUserDetailsFromRequest(
							request);
						String reverseThisRequest = ParamUtil.getString(
							request, REVERSE_THIS_REQUEST);
						String serviceStatusNote =
							Constants.CHARGEBACK_CANCEL_NOTE +
								user.getFullName();

						if (TRUE.equalsIgnoreCase(reverseThisRequest)) {
							serviceStatusNote =
								Constants.CHARGEBACK_REVERSE_NOTE +
									user.getFullName();
						}

						ticketingService.addNote(
							id, serviceStatusNote, null, header);
					}
				}
				else {
					logger.error(
						"callWebServiceToAddNoteToTicket - WS response status is: " +
							statusCode);
					xmlMessage.addComment("wsResponseCode", statusCode);
				}
			}
			else {
				logger.error(
					"callWebServiceToAddNoteToTicket - WS response header is null");
			}
		}
		else {
			logger.error(
				"callWebServiceToAddNoteToTicket - WS response is null");
		}

		// Create a String and audit the message.

		if (isSuccess) {
			CommonUtil.auditSuccessXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_ADD_NOTE, request,
				response);
		}
		else {
			CommonUtil.auditFailXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_ADD_NOTE, request,
				response);
		}

		logger.debug("callWebServiceToAddNoteToTicket - end");

		return isSuccess;
	}

	/**
	 * Call the WebService to add the ticket and return the TicketType object.
	 *
	 * @param ticket
	 *            <em>Ticket</em>
	 * @param header
	 *            <em>VsmRequestHeader</em>
	 * @return TicketType
	 */
	private TicketType callWebServiceToAddTicket(
		Ticket ticket, TktRequestHeader header, PortletRequest request,
		PortletResponse response) {

		logger.debug("callWebServiceToAddTicket - start");

		XmlMessage xmlMessage = new XmlMessage();
		Boolean isSuccess = Boolean.FALSE;
		TicketType ticketType = null;
		AddTicketResponse responseType = ticketingService.addTicket(
			ticket, header);

		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);

		/**
		 * xmlMessage = AuditWebServicesUtil
		 * .addVsmRequestHeaderElementsToXmlMessage(header, xmlMessage);
		 */
		xmlMessage = CommonUtil.addNewTicketParamsToXmlMessage(
			ticket, xmlMessage);

		if (null != responseType) {
			if (null != responseType.getHeader()) {
				String statusCode = responseType.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					isSuccess = Boolean.TRUE;
					ticketType = responseType.getTicket();

					xmlMessage.addResult(
						"ticketId",
						String.valueOf(
							responseType.getTicket(
							).getId()));
				}
				else {
					logger.error(
						"addTicket - WS response status is not success: " +
							statusCode);
					xmlMessage.addComment("error", statusCode);
				}
			}
			else {
				logger.error(
					"addTicket - WS add ticket response header is null");
			}
		}
		else {
			logger.error("addTicket - WS add ticket response is null");
		}

		// Create a String and audit the message.

		if (isSuccess) {
			CommonUtil.auditSuccessXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_ADD, request, response);
		}
		else {
			CommonUtil.auditFailXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_ADD, request, response);
		}

		logger.debug("callWebServiceToAddTicket - end");

		return ticketType;
	}

	/**
	 * @param id
	 * @param header
	 * @return
	 */
	private DecoratedTicket callWebServiceToGetTicketDetails(
		String id, Boolean isVsmTicket, TktRequestHeader header,
		PortletRequest request, PortletResponse response, Boolean isDecrypted) {

		logger.debug("callWebServiceToGetTicketDetails - start");

		XmlMessage xmlMessage = new XmlMessage();
		DecoratedTicket ticket = null;
		Boolean isSuccess = Boolean.FALSE;
		GetTicketResponse responseType = ticketingService.ticketDetails(
			id, isVsmTicket, header, isDecrypted);

		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);

		/**
		 * xmlMessage = AuditWebServicesUtil
		 * .addVsmRequestHeaderElementsToXmlMessage(header, xmlMessage);
		 */
		xmlMessage.addParameter("ticketId", id);

		if (null != responseType) {
			if (null != responseType.getHeader()) {
				String statusCode = responseType.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					isSuccess = Boolean.TRUE;
					ticket = new DecoratedTicket(responseType.getTicket());
					request.setAttribute(
						"ticketDetailsNotFound", Boolean.FALSE);
				}
				else if (StringUtils.equalsIgnoreCase(
							Constants.RECORD_NOT_FOUND, statusCode)) {

					ticket = new DecoratedTicket();
					isSuccess = Boolean.TRUE;
					xmlMessage.addComment("wsResponseStatus", statusCode);
					request.setAttribute("ticketDetailsNotFound", Boolean.TRUE);
				}
				else {
					logger.error(
						"callWebServiceToGetTicketDetails - WS response status code is: " +
							statusCode);
					ticket = new DecoratedTicket();
					request.setAttribute("ticketDetailsNull", Boolean.TRUE);
					xmlMessage.addComment("wsResponseStatus", statusCode);
					request.setAttribute("ticketDetailsNotFound", Boolean.TRUE);
				}
			}
			else {
				ticket = new DecoratedTicket();
				logger.error(
					"callWebServiceToGetTicketDetails - WS response header is null");
			} // Response Header is null.
		}
		else {
			ticket = new DecoratedTicket();
			logger.error(
				"callWebServiceToGetTicketDetails - WS response is null");
		} // Response is null.

		// Create a String and audit the message.

		String message = xmlMessage.toXml();

		if (isSuccess) {
			audit.success(
				response, request, AuditOrigin.PORTAL_ORIGIN,
				AuditTicketing.TICKETING_DETAIL, message);
		}

		logger.debug("callWebServiceToGetTicketDetails - end");

		return ticket;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DecoratedTicket> callWebServiceToGetTicketList(
		TicketFilter ticketFilter, TktRequestHeader header,
		PortletRequest request, PortletResponse response) {

		logger.debug("callWebServiceToGetTicketList - start");

		XmlMessage xmlMessage = new XmlMessage();
		Boolean isSuccess = Boolean.FALSE;
		List<DecoratedTicket> tickets = null;
		HttpSession session = getSessionFromRequest(request);
		FindTicketsResponse responseType = ticketingService.findTickets(
			ticketFilter, header);

		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);

		/**
		 * xmlMessage = AuditWebServicesUtil
		 * .addVsmRequestHeaderElementsToXmlMessage(header, xmlMessage);
		 */
		if (null != ticketFilter) {
			xmlMessage = CommonUtil.addFilterTicketParamsToXmlMessage(
				ticketFilter, xmlMessage);
		}

		if (null != responseType) {
			if (null != responseType.getHeader()) {
				String statusCode = responseType.getHeader(
				).getStatusCode();
				tickets = new ArrayList<>();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode) ||
					StringUtils.equalsIgnoreCase(
						Constants.RECORD_NOT_FOUND, statusCode)) {

					isSuccess = Boolean.TRUE;

					for (TicketType ticketType : responseType.getTickets()) {
						DecoratedTicket ticket = new DecoratedTicket(
							ticketType);

						ticket.setOrgMap(
							(Map<String, String>)session.getAttribute(
								"orgMap"));
						ticket.setCuscalOrgDefaultName(
							(String)session.getAttribute(
								"cuscalOrgDefaultName"));
						ticket.setCuscalShortNamesList(
							(List<String>)session.getAttribute(
								"cuscalShortNamesList"));

						tickets.add(ticket);
					} // Decorate every ticket and add that to the list.
					xmlMessage.addResult(
						"size", String.valueOf(tickets.size()));

					if (StringUtils.equalsIgnoreCase(
							Constants.RECORD_NOT_FOUND, statusCode)) {

						xmlMessage.addComment("wsResponseStatus", statusCode);
					}
				}
				else {
					if (StringUtils.equalsIgnoreCase("FAILED", statusCode) ||
						StringUtils.equalsIgnoreCase(
							"SYSTEM_ERROR", statusCode) ||
						StringUtils.equalsIgnoreCase(
							"SERVICE_BUSY", statusCode)) {

						tickets = null;
					}

					if (null != tickets) {
						xmlMessage.addResult(
							"size", String.valueOf(tickets.size()));
					}

					xmlMessage.addComment("wsResponseCode", statusCode);

					logger.error(
						"callWebServiceToGetTicketList - Find tickets status is not success: " +
							statusCode);
				} // Status code is not success
			}
			else {
				logger.error(
					"callWebServiceToGetTicketList - Find tickets response header is null");
			} // responseType or responseType Header is null.
		}
		else {
			logger.error(
				"callWebServiceToGetTicketList - Find tickets response is null");
		}

		// Create a String and audit the message.

		if (isSuccess) {
			CommonUtil.auditSuccessXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_SEARCH, request, response);
		}
		else {
			CommonUtil.auditFailXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_SEARCH, request, response);
		}

		logger.debug("callWebServiceToGetTicketList - end");

		return tickets;
	}

	/**
	 * Call the WebService to update the ticket and return the TicketType
	 * object.
	 *
	 * @param ticket
	 *            <em>Ticket</em>
	 * @param header
	 *            <em>VsmRequestHeader</em>
	 * @return TicketType
	 */
	private TicketType callWebServiceToUpdateTicket(
		TicketType ticketType, TktRequestHeader header, PortletRequest request,
		PortletResponse response) {

		logger.debug("callWebServiceToUpdateTicket - start");

		XmlMessage xmlMessage = new XmlMessage();

		TouchTicketResponse responseType = ticketingService.touchTicket(
			ticketType, header);

		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);

		if (null != responseType) {
			if (null != responseType.getHeader()) {
				String statusCode = responseType.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					ticketType = responseType.getTicket();

					xmlMessage.addResult(
						"ticketId",
						String.valueOf(
							responseType.getTicket(
							).getId()));
				}
				else {
					logger.error(
						"updateTicket - WS response status is not success: " +
							statusCode);
					xmlMessage.addComment("error", statusCode);
				}
			}
			else {
				logger.error(
					"updateTicket - WS update ticket response header is null");
			}
		}
		else {
			logger.error("updateTicket - WS update ticket response is null");
		}

		logger.debug("callWebServiceToUpdateTicket - end");

		return ticketType;
	}

	/**
	 * @param roleName
	 * @param request
	 * @return
	 */
	private Boolean checkRoleForUser(String roleName, PortletRequest request) {
		Boolean hasRole = Boolean.FALSE;
		logger.debug("checkRoleForUser - start");

		try {
			User user = PortalUtil.getUser(request);

			if (null != user) {
				Long userId = user.getUserId();

				List<Role> roles = RoleLocalServiceUtil.getUserRoles(userId);
				List<Role> groupRoles = findUserGroupRoleList(user);
				List<Role> orgRoles =
					findRegularRolesAssignedToUsersOrganisationList(user);
				List<Role> allUserRoles = new ArrayList<>();

				allUserRoles.addAll(roles);
				allUserRoles.addAll(groupRoles);
				allUserRoles.addAll(orgRoles);
				logger.debug(
					"checkRoleForUser - All Roles count is " +
						allUserRoles.size());

				for (Role role : allUserRoles) {
					if (roleName.equalsIgnoreCase(role.getName())) {
						hasRole = true;
						logger.debug(
							"checkRoleForUser - setting role as true for Role  " +
								role.getName());
					}
				}
			}
		}
		catch (Exception e) {
			logger.error(
				"Exception while checking user for " + roleName + ": " +
					e.getMessage(),
				e);
		}

		logger.debug("checkRoleForUser - end");

		return hasRole;
	}

	/**
	 * @param id
	 * @param request
	 * @return
	 */
	private String createEditTicketUrl(
		long id, PortletRequest request, boolean isMasterCardDispute) {

		String location = "";
		String portletName = "";
		long i = 1;

		while (true) {
			String key = "service.request." + i + ".name";

			String name = selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				key
			);

			if (isMasterCardDispute) {
				if (Constants.MASTERCARD_DISPUTE.equals(name)) {
					portletName = selfServiceProperties.getSelfserviceProperty(
					).getProperty(
						"service.request." + i + ".portlet.name"
					);
					location = selfServiceProperties.getSelfserviceProperty(
					).getProperty(
						"service.request." + i + ".portlet.location"
					);

					break;
				}
				else if (StringUtils.isBlank(name)) {
					break;
				}
			}
			else if (Constants.VISA_CHARGEBACK.equals(name)) {
				portletName = selfServiceProperties.getSelfserviceProperty(
				).getProperty(
					"service.request." + i + ".portlet.name"
				);
				location = selfServiceProperties.getSelfserviceProperty(
				).getProperty(
					"service.request." + i + ".portlet.location"
				);

				break;
			}
			else if (StringUtils.isBlank(name)) {
				break;
			}

			i++;
		}

		if (StringUtils.isNotBlank(portletName) &&
			StringUtils.isNotBlank(location)) {

			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			PortletURL link = PortletURLFactoryUtil.create(
				PortalUtil.getHttpServletRequest(request), portletName,
				themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

			link.setParameter(TICKET_ID, String.valueOf(id));

			if (isMasterCardDispute) {
				link.setParameter(EDIT_TICKET, TRUE);
			}

			String selfServiceURI =
				selfServiceProperties.getSelfserviceProperty(
				).getProperty(
					Constants.SELF_SERVICE_URI_LOOKUP
				);
			String destination = null;

			if (StringUtils.isNotBlank(selfServiceURI)) {
				destination = link.toString(
				).replace(
					selfServiceURI, location
				);
			}
			else {
				logger.error(
					"createEditTicketUrl - Could not read the " +
						Constants.SELF_SERVICE_URI_LOOKUP +
							" from the properties file. Cannot generate the edit URL.");
			}

			return destination;
		}

		return null;
	}

	/**
	 * @param request
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	private Boolean doesUserHaveServiceRequestAttachmentRole(
			PortletRequest request)
		throws PortalException {

		String attachmentRole = selfServiceProperties.getSelfserviceProperty(
		).getProperty(
			Constants.ATTACHMENT_LOOKUP
		);

		logger.debug(
			"doesUserHaveServiceRequestAttachmentRole - attachmentRole is: " +
				attachmentRole);

		return checkRoleForUser(attachmentRole, request);
	}

	/**
	 * @param request
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	private Boolean doesUserHaveServiceRequestNoteRole(PortletRequest request)
		throws PortalException {

		String attachmentRole = selfServiceProperties.getSelfserviceProperty(
		).getProperty(
			Constants.NOTES_LOOKUP
		);

		logger.debug(
			"doesUserHaveServiceRequestNoteRole - attachmentRole is: " +
				attachmentRole);

		return checkRoleForUser(attachmentRole, request);
	}

	/**
	 * Return the current Session from the request.
	 *
	 * @param request
	 *            <em>PortletRequest</em>
	 * @return HttpSession
	 */
	private HttpSession getSessionFromRequest(PortletRequest request) {
		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();

		return session;
	}

	private boolean isMasterCardDispute(long typeId) {
		boolean isMasterCardDispute = false;
		long masterCardDisputeRetrievalRequestId = Long.parseLong(
			selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				Constants.MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID
			));
		long masterCardDisputeFirstChargebackId = Long.parseLong(
			selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				Constants.MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID
			));
		long masterCardDisputeArbitrationChargebackId = Long.parseLong(
			selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				Constants.MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID
			));
		long masterCardDisputeReportFraudId = Long.parseLong(
			selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				Constants.MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID
			));

		if ((masterCardDisputeRetrievalRequestId == typeId) ||
			(masterCardDisputeFirstChargebackId == typeId) ||
			(masterCardDisputeArbitrationChargebackId == typeId) ||
			(masterCardDisputeReportFraudId == typeId)) {

			isMasterCardDispute = true;
		}

		return isMasterCardDispute;
	}

	private String loadActionTickets(
		TktRequestHeader header, PortletRequest request,
		PortletResponse response, String page) {

		TicketFilter actionTicketFilter = new TicketFilter();

		actionTicketFilter.setTicketStatus(Constants.ACTION_STATUS_ID);
		actionTicketFilter.setSubmittedBy(Constants.ALL_ORGANISATIONS);
		actionTicketFilter.setUpdatedWithin("365");
		actionTicketFilter.setSubmittedWithin("365");

		// ticketFilter.setTicketCategory(ticketCategory);

		List<DecoratedTicket> actionTickets = callWebServiceToGetTicketList(
			actionTicketFilter, header, request, response);

		if (null != actionTickets) {
			request.setAttribute("actionTickets", actionTickets);
		}
		else {
			logger.error("Error getting list of action tickets for user");
			request.setAttribute("invalidResponse", Boolean.TRUE);
			page = Constants.ERROR_PAGE;
		}

		return page;
	}

	/**
	 * @return
	 */
	private String selfServiceHeading() {
		String heading = selfServiceProperties.getSelfserviceProperty(
		).getProperty(
			"self.service.heading"
		);

		return heading;
	}

	/**
	 * @return
	 */
	private String selfServiceIntroText() {
		String introText = "";

		introText = selfServiceProperties.getSelfserviceProperty(
		).getProperty(
			"self.service.intro"
		);

		return introText;
	}

	/**
	 * Set the default org name for Cuscal.
	 *
	 * @param request
	 */
	private void setCuscalOrgDefaultName(PortletRequest request) {
		HttpSession session = getSessionFromRequest(request);

		if (null == session.getAttribute("cuscalOrgDefaultName")) {
			String cuscalName = selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				Constants.CUSCAL_ORG_DEFAULT_NAME
			);

			logger.debug(
				"setCuscalOrgDefaultName - cuscalOrgDefaultName: " +
					cuscalName);
			session.setAttribute("cuscalOrgDefaultName", cuscalName);
		}
	}

	/**
	 * Get a list of the org short names for Cuscal and store it in the session.
	 *
	 * @param request
	 */
	private void setCuscalShortNamesList(PortletRequest request) {
		HttpSession session = getSessionFromRequest(request);

		if (null == session.getAttribute("cuscalShortNamesList")) {
			List<String> cuscalShortNames = null;
			String cuscalName = selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				Constants.CUSCAL_ORG_SHORT_NAMES
			);

			if (StringUtils.isNotBlank(cuscalName)) {
				String[] cuscalNames = cuscalName.split(Constants.COMMA);
				cuscalShortNames = new ArrayList<>();

				for (String cuscalShortName : cuscalNames) {
					cuscalShortNames.add(cuscalShortName);
				}
			}

			session.setAttribute("cuscalShortNamesList", cuscalShortNames);
		}
	}

	/**
	 * Setup the ticket details and the other common attributes required for the
	 * details page.
	 *
	 * @param request
	 * @param decoratedTicket
	 * @param header
	 * @return PageName
	 */
	private Boolean setupTicketAndCommonAttributes(
		PortletRequest request, DecoratedTicket decoratedTicket,
		TktRequestHeader header) {

		request.setAttribute("ticketDetails", decoratedTicket);
		Boolean isValidTicketDetails = Boolean.TRUE;

		try {
			if (doesUserHaveServiceRequestAttachmentRole(request)) {
				request.setAttribute(Constants.ATTACHMENT_ROLE, Boolean.TRUE);

				// Only Cuscal users can add attachments after
				// the call is closed.

				if (StringUtils.equalsIgnoreCase(
						Constants.CUSCAL, header.getUserOrgId())) {

					request.setAttribute(
						Constants.CAN_ATTACH_AFTER_CLOSE, Boolean.TRUE);
				}
				else {
					request.setAttribute(
						Constants.CAN_ATTACH_AFTER_CLOSE, Boolean.FALSE);
				}
			}
			else {
				request.setAttribute(Constants.ATTACHMENT_ROLE, Boolean.FALSE);
			}

			if (doesUserHaveServiceRequestNoteRole(request)) {
				request.setAttribute(Constants.NOTES_ROLE, Boolean.TRUE);
			}
			else {
				request.setAttribute(Constants.NOTES_ROLE, Boolean.FALSE);
			}
		}
		catch (SystemException se) {
			logger.error(
				"setupTicketAndCommonAttributes - Error when checking user roles",
				se);
			isValidTicketDetails = Boolean.FALSE;
		}
		catch (PortalException pe) {
			logger.error(
				"setupTicketAndCommonAttributes - Error when checking user roles",
				pe);
			isValidTicketDetails = Boolean.FALSE;
		}

		return isValidTicketDetails;
	}

	/**
	 * @param request
	 * @param ticketFilter
	 * @param header
	 */
	private void setupTicketFilterValues(
		PortletRequest request, TicketFilter ticketFilter,
		TktRequestHeader header, Boolean defaultSearch) {

		Long orgId = null;
		String userFullName = null;
		HttpSession session = getSessionFromRequest(request);

		try {
			User user = CommonUtil.getUserFromRequest(request);

			if (null != user) {
				userFullName =
					user.getFirstName() + Constants.SPACE + user.getLastName();

				if (null != orgIdOverride) {
					orgId = orgIdOverride;
				}
				else {
					Organization org = CommonUtil.getOrganisationForUser(user);

					orgId = org.getOrganizationId();
				}
			}
		}
		catch (PortalException e) {
			logger.error(
				"setupTicketFilterValues - Could not get user from request" +
					e.getMessage(),
				e);
		}
		catch (SystemException e) {
			logger.error(
				"setupTicketFilterValues - Could not get user from request" +
					e.getMessage(),
				e);
		}

		Map<String, String> statusMap = ticketingService.getTicketStatusMap(
			header);
		Map<String, String> productMap = ticketingService.getProductMap(header);
		Map<String, String> ticketCategoryMap =
			ticketingService.getTicketCategoryMap(header);
		List<Customer> customerAccessList =
			ticketingService.getCustomerAccessViewList(orgId);

		@SuppressWarnings("unchecked")
		Map<String, String> submittedByMap =
			(Map<String, String>)session.getAttribute("orgMap");

		if ((null == submittedByMap) || submittedByMap.isEmpty()) {
			submittedByMap = CommonUtil.getSubmittedByMap(
				customerAccessList, header.getUserOrgId(), request);
			session.setAttribute("orgMap", submittedByMap);
		}

		if (defaultSearch) {
			ticketFilter.setUpdatedWithin("30");
			ticketFilter.setSubmittedWithin("30");
			ticketFilter.setSubmittedBy("0");
		}

		ticketFilterValidator.setProductList(productMap);
		ticketFilterValidator.setSubmittedBy(submittedByMap);
		ticketFilterValidator.setTicketCategoryMap(ticketCategoryMap);
		ticketFilterValidator.setSubmittedWithin(
			CommonUtil.getLastModifiedMap());
		ticketFilterValidator.setUpdatedWithin(CommonUtil.getLastModifiedMap());

		request.setAttribute(
			"lastModifiedMap", CommonUtil.getLastModifiedMap());
		request.setAttribute("productMap", productMap);
		request.setAttribute("submittedByMap", submittedByMap);
		request.setAttribute("submittedByUser", userFullName);
		request.setAttribute("ticketCategoryMap", ticketCategoryMap);
		request.setAttribute("ticketStatusMap", statusMap);
	}

	private static final String _APPLICATION_OCTET_STREAM =
		"application/octet-stream";

	private static final String _APPLICATION_VND_OPENXMLFORMATS =
		"application/vnd.openxmlformats";

	private static Auditor audit = Auditor.getInstance();
	private static Logger logger = Logger.getLogger(
		CuscalTicketingController.class);

	private Long orgIdOverride = null;

	@Autowired
	@Qualifier("selfServiceProperties")
	private SelfServiceProperties selfServiceProperties;

	@Autowired
	@Qualifier(Constants.SERVICE_CATALOGUE_VALIDATOR)
	private ServiceCatalogueValidator serviceCatalogueValidator;

	@Autowired
	@Qualifier(Constants.TICKET_FILTER_VALIDATOR)
	private TicketFilterValidator ticketFilterValidator;

	@Autowired
	@Qualifier(Constants.CUSCAL_TICKETING_SERVICE)
	private CuscalTicketingService ticketingService;

	@Autowired
	@Qualifier(Constants.TICKET_LOAD_VALIDATOR)
	private TicketLoadValidator ticketLoadValidator;

	/**
	 * @Autowired
	 * @Qualifier(Constants.TICKET_VALIDATOR) private TicketValidator
	 * ticketValidator;
	 */
	@Autowired
	@Qualifier(Constants.TICKET_NOTE_VALIDATOR)
	private TicketNoteValidator ticketNoteValidator;

}