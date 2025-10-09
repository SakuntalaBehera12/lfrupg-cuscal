package au.com.cuscal.common.framework.dxp.service.request.services;

import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.common.framework.dxp.service.request.core.AbstractSelfRegisteringService;
import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.common.framework.service.request.services.ServiceRequestService;
import au.com.cuscal.framework.audit.Audit;
import au.com.cuscal.framework.audit.impl.log4j.Log4jAuditor;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.webservices.client.CustomerSelfServiceService;
import au.com.cuscal.framework.webservices.client.TransactionService;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.AttributesListType;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesListRequest;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesListResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.selfservice.UpdateTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.UpdateTicketResponse;
import au.com.cuscal.framework.webservices.transaction.AttributeType;
import au.com.cuscal.framework.webservices.transaction.Entity;
import au.com.cuscal.framework.webservices.transaction.EntityAttribute;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdRequestType;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdResponseType;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;

import com.liferay.portal.kernel.util.PortalUtil;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;

@Service("dxpServiceRequestService")
public class ServiceRequestServiceImpl extends AbstractSelfRegisteringService
	implements ServiceRequestService {

	/**
	 *
	 */
	public AddServiceRequestResponse addServiceRequest(
		AddServiceRequestRequest addServiceRequestRequest) {

		logger.debug("addServiceRequest - start");

		AddServiceRequestResponse response = null;

		if (null != addServiceRequestRequest) {
			response = ticketingService.addServiceRequest(
				addServiceRequestRequest);
		}

		logger.debug("addServiceRequest - end");

		return response;
	}

	/**
	 *
	 */
	public FindServiceRequestResponse findServiceRequest(
		FindServiceRequestRequest findServiceRequestRequest) {

		logger.debug("findServiceRequest - start");

		FindServiceRequestResponse response = null;

		if (null != findServiceRequestRequest) {
			response = ticketingService.findServiceRequest(
				findServiceRequestRequest);
		}

		logger.debug("findServiceRequest - end");

		return response;
	}

	public List<AttributesListType> getAttributeList(
		long lookUpType, SearchHeader searchHeader) {

		logger.debug("getAttributeList - start");

		GetAttributesListRequest request = new GetAttributesListRequest();

		if (null != searchHeader) {
			TktRequestHeader header = Utility.convertSearchHeaderToTktHeader(
				searchHeader);

			request.setHeader(header);
			request.setTypeId(lookUpType);

			logger.debug(
				"getAttributeList - calling transactionService.getAttributeList()");

			GetAttributesListResponse response =
				ticketingService.getAttributesList(request);

			if (null != response) {
				List<AttributesListType> attributeList =
					response.getAttributesLists();

				logger.debug(
					"getAttributeList - results returned by getAttributeList()");

				return attributeList;
			}

			logger.error("getAttributeList - no results returned");
		}
		else {
			logger.error("getAttributeList - No Search Header found");
		}

		logger.debug("getAttributeList - end");

		return null;
	}

	/**
	 * Get get org short name using liferay org id .
	 *
	 * @param String
	 * @return String
	 */
	public String getOrgShortName(String liferayOrgId, PortletRequest request) {
		String orgShortName = null;
		FindEntitiesByAttributeWithIdRequestType
			findEntitiesByAttributeWithIdRequest =
				new FindEntitiesByAttributeWithIdRequestType();
		findEntitiesByAttributeWithIdRequest.setAttributeValue(liferayOrgId);
		findEntitiesByAttributeWithIdRequest.setAttributeTypeId(4);

		FindEntitiesByAttributeWithIdResponseType
			findEntitiesByAttributeWithIdResponseType = findEntitiesByAttribute(
				liferayOrgId);

		if (!findEntitiesByAttributeWithIdResponseType.getEntities(
			).isEmpty()) {

			orgShortName =
				findEntitiesByAttributeWithIdResponseType.getEntities(
				).get(
					0
				).getShortName();

			buildIssuerIdList(
				findEntitiesByAttributeWithIdResponseType, request);
		}

		logger.debug("The organisation short name is " + orgShortName);

		return orgShortName;
	}

	public CustomerSelfServiceService getTicketingService() {
		return ticketingService;
	}

	public TransactionService getTransactionService() {
		return transactionService;
	}

	/**
	 *
	 * @param getTicketRequest
	 * @return
	 */
	public GetTicketResponse ticketDetails(GetTicketRequest getTicketRequest) {
		logger.debug("ticketDetails - start");

		GetTicketResponse response = null;

		if (null != getTicketRequest) {
			response = ticketingService.getServiceRequest(getTicketRequest);
		}

		logger.debug("ticketDetails - end");

		return response;
	}

	/**
	 * WebService call to update the ticket.
	 *
	 * @param  updateTicketRequest
	 * @return UpdateTicketResponse
	 */
	public UpdateTicketResponse updateTicket(
		UpdateTicketRequest updateTicketRequest) {

		logger.debug("updateTicket - start");

		UpdateTicketResponse response = null;

		if (null != updateTicketRequest) {
			response = ticketingService.updateTicket(updateTicketRequest);
		}

		logger.debug("updateTicket - end");

		return response;
	}

	/**
	 * Build a list of the Cuscal Issuer ID's for the logged in user.
	 *
	 * @param responseType	<code>FindEntitiesByAttributeWithIdResponseType</code>
	 * @param request		<code>PortletRequest</code>
	 */
	private void buildIssuerIdList(
		FindEntitiesByAttributeWithIdResponseType responseType,
		PortletRequest request) {

		logger.debug("buildIssuerIdList - start");

		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();
		List<String> issuerIdList = new ArrayList<>();

		if (null != session.getAttribute("issuerIdList")) {
			for (Entity entity : responseType.getEntities()) {
				if (entity.isIssuer()) {
					for (EntityAttribute att : entity.getEntityArrtibutes()) {
						AttributeType attType = att.getAttributeType();

						// Check if it is Cuscal Issuer ID which has an ID of 9.

						// TODO: This is not currently in production.

						if (attType.getAttributeTypeId() == 9) {
							issuerIdList.add(att.getAttributeTypeValue());
						}
					}
				}
			}

			session.setAttribute("issuerIdList", issuerIdList);
		}

		logger.debug("buildIssuerIdList - stop");
	}

	/**
	 *
	 * @param liferayOrgId
	 * @return
	 */
	private FindEntitiesByAttributeWithIdResponseType findEntitiesByAttribute(
		String liferayOrgId) {

		FindEntitiesByAttributeWithIdRequestType
			findEntitiesByAttributeWithIdRequest =
				new FindEntitiesByAttributeWithIdRequestType();
		findEntitiesByAttributeWithIdRequest.setAttributeValue(liferayOrgId);
		findEntitiesByAttributeWithIdRequest.setAttributeTypeId(4);

		FindEntitiesByAttributeWithIdResponseType
			findEntitiesByAttributeWithIdResponseType =
				transactionService.findEntitiesByAttributeWithId(
					findEntitiesByAttributeWithIdRequest);

		return findEntitiesByAttributeWithIdResponseType;
	}

	private static final Auditor audit = Auditor.getInstance();

	/**
	 * Logger object
	 */
	private static Logger logger = Logger.getLogger(
		ServiceRequestServiceImpl.class);

	private final CustomerSelfServiceService ticketingService = CuscalServiceLocator.getService(
		CustomerSelfServiceService.class.getName());
	private final TransactionService transactionService = CuscalServiceLocator.getService(
		TransactionService.class.getName());

}