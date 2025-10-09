//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.services;

import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.framework.webservices.client.CardService;
import au.com.cuscal.framework.webservices.client.CustomerSelfServiceService;
import au.com.cuscal.framework.webservices.client.EntityService;
import au.com.cuscal.framework.webservices.liferay.dxp.LiferayClientUtil;
import au.com.cuscal.framework.webservices.selfservice.AddTicketNoteRequest;
import au.com.cuscal.framework.webservices.selfservice.AddTicketNoteResponse;
import au.com.cuscal.framework.webservices.selfservice.AddTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.AddTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.CategoryType;
import au.com.cuscal.framework.webservices.selfservice.DownloadAttachmentRequest;
import au.com.cuscal.framework.webservices.selfservice.DownloadAttachmentResponse;
import au.com.cuscal.framework.webservices.selfservice.FindCategoriesRequest;
import au.com.cuscal.framework.webservices.selfservice.FindCategoriesResponse;
import au.com.cuscal.framework.webservices.selfservice.FindStatusRequest;
import au.com.cuscal.framework.webservices.selfservice.FindStatusesResponse;
import au.com.cuscal.framework.webservices.selfservice.FindTicketsRequest;
import au.com.cuscal.framework.webservices.selfservice.FindTicketsResponse;
import au.com.cuscal.framework.webservices.selfservice.FindTypesRequest;
import au.com.cuscal.framework.webservices.selfservice.FindTypesResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.StatusType;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.selfservice.TouchTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.TouchTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.TypeType;
import au.com.cuscal.framework.webservices.selfservice.UploadAttachmentRequest;
import au.com.cuscal.framework.webservices.selfservice.UploadAttachmentResponse;
import au.com.cuscal.framework.webservices.transaction.Customer;
import au.com.cuscal.framework.webservices.transaction.GetCustomerAccessViewRequestType;
import au.com.cuscal.framework.webservices.transaction.GetCustomerAccessViewResponseType;
import au.com.cuscal.ticketing.common.CommonUtil;
import au.com.cuscal.ticketing.common.Constants;
import au.com.cuscal.ticketing.domain.Ticket;
import au.com.cuscal.ticketing.domain.TicketFilter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.xml.datatype.XMLGregorianCalendar;

import com.liferay.portal.template.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service(value = Constants.CUSCAL_TICKETING_SERVICE)
public class CuscalTicketingServiceImpl implements CuscalTicketingService {

	@Override
	public AddTicketNoteResponse addNote(
		String ticketId, String note, String serviceStatus,
		TktRequestHeader header) {

		AddTicketNoteResponse response = null;
		AddTicketNoteRequest request = new AddTicketNoteRequest();

		if (null != header) {
			request.setHeader(header);

			if (StringUtils.isNotBlank(ticketId)) {
				String[] parts = ticketId.split(Constants.DASH);
				request.setTicketId(Long.valueOf(parts[0]));

				request.setNote(note);
				request.setServiceStatus(serviceStatus);
				response = ticketingService.addNote(request);
			}
		}

		return response;
	}

	/**
	 *
	 */
	/*@Override
	public UpdateTicketResponse updateTicket(
			TicketType ticketType,
			TktRequestHeader header) {

		logger.debug("updateTicket - start");

		UpdateTicketRequest request = new UpdateTicketRequest();

		request.setHeader(header);

		if (null != ticketType) {
			request.setTicket(ticketType);

			logger.debug("updateTicket - Ticket Id: " + request.getTicket().getId());
			logger.debug("updateTicket - Last Read Timestamp: " + request.getTicket().getLastReadTimestamp());

		} else {
			logger.error("updateTicket - ticket is null");
		}

		UpdateTicketResponse response = ticketingService.updateTicket(request);

		logger.debug("updateTicket - end");

		return response;
	}*/

	@Override
	public AddTicketResponse addTicket(Ticket ticket, TktRequestHeader header) {
		AddTicketResponse response = null;
		AddTicketRequest request = new AddTicketRequest();

		if (null != header) {
			request.setHeader(header);
			TicketType ticketType = prepareTicketToAdd(ticket);

			if (null != ticketType) {
				request.setTicket(ticketType);

				//				response = ticketingService.addTicket(request);
			}
		}

		return response;
	}

	/**
	 *
	 */
	@Override
	public DownloadAttachmentResponse downloadAttachment(
		DownloadAttachmentRequest request) {

		DownloadAttachmentResponse response =
			ticketingService.downloadAttachment(request);

		if (null != response) {
			if (null != response.getHeader()) {
				String statusCode = response.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					return response;
				}
			}
		}

		return response;
	}

	/**
	 *
	 */
	@Override
	public FindTicketsResponse findTickets(
		TicketFilter ticketFilter, TktRequestHeader header) {

		logger.debug("findTickets - start");

		FindTicketsRequest request = new FindTicketsRequest();

		request.setHeader(header);

		if (null != ticketFilter) {
			request = addFilterParameters(ticketFilter, request);

			logger.debug(
				"----------------------------Filter tickets----------------------");
			logger.debug(
				"findTickets - request.getCategory: " + request.getCategory());
			logger.debug(
				"findTickets - request.getOrganization: " +
					request.getOrganization());
			logger.debug(
				"findTickets - request.getProduct: " + request.getProduct());
			logger.debug(
				"findTickets - request.getStatus: " + request.getStatus());
			logger.debug(
				"----------------------------Filter tickets----------------------");
		}
		else {
			XMLGregorianCalendar pastDate =
				CommonUtil.getXmlCalendarDateInThePast(30);

			if (null != pastDate) {
				request.setUpdatedWithin(pastDate);
			}
		}

		logger.debug(
			"----------------------------Find tickets----------------------");
		logger.debug("findTickets - header.getUserId: " + header.getUserId());
		logger.debug(
			"findTickets - header.getUserOrgId: " + header.getUserOrgId());
		logger.debug(
			"findTickets - header.getUserOrgName: " + header.getUserOrgName());
		logger.debug(
			"findTickets - request.getSubmittedDate: " +
				request.getSubmittedWithin());
		logger.debug(
			"findTickets - request.getUpdatedDate: " +
				request.getUpdatedWithin());
		logger.debug(
			"----------------------------Find tickets----------------------");

		FindTicketsResponse response = ticketingService.findTickets(request);

		logger.debug("findTickets - end");

		return response;
	}

	public CardService getCardService() {
		return cardService;
	}

	/**
	 *
	 * @param header
	 * @return
	 */
	@Override
	public List<Customer> getCustomerAccessViewList(Long liferayOrgId) {
		GetCustomerAccessViewRequestType params =
			new GetCustomerAccessViewRequestType();

		params.setCustomerId(String.valueOf(liferayOrgId));
		params.setCustomerIdType("portalOrgId");
		params.setIgnoreCase(true);

		logger.debug(
			"getCustomerAccessView - portalOrgId in the Service Impl is: " +
				liferayOrgId);

		GetCustomerAccessViewResponseType response =
			cardService.getCustomerAccessView(params);

		List<Customer> customers = response.getCustomers();

		return customers;
	}

	public EntityService getEntityService() {
		return entityService;
	}

	/**
	 *
	 */
	@Override
	public String getOrgShortName(Long liferayOrgId) {
		logger.debug("getOrgShortName - start");
		String orgShortName = null;

		try {
			orgShortName = LiferayClientUtil.getOrgShortName(
				entityService, liferayOrgId);
		}
		catch (Exception e) {
			logger.error(
				"getOrgShortName - Could not get the OrgShortName from LiferayClientUtil. " +
					e.getMessage(),
				e);
		}

		logger.debug("getOrgShortName - end [" + orgShortName + "]");

		return orgShortName;
	}

	/**
	 * Using product description for this. Might have to change it to Prodcut
	 * Name in the future.
	 */
	@Cacheable("productList")
	@Override
	public Map<String, String> getProductMap(TktRequestHeader header) {
		if (productMap == null) {
			productMap = new HashMap<>();

			setProductMap(header);
		}

		return productMap;
	}

	/**
	 *
	 */
	@Override
	public Map<String, String> getTicketCategoryMap(TktRequestHeader header) {
		logger.debug("getTicketTypeList - start");

		if (null == ticketCategoryMap) {
			ticketCategoryMap = new HashMap<>();

			setTicketCategoryMap(header);
		}

		logger.debug("getTicketTypeList - end");

		return ticketCategoryMap;
	}

	public CustomerSelfServiceService getTicketingService() {
		return ticketingService;
	}

	/**
	 *
	 */
	@Cacheable("ticketStatusList")
	@Override
	public Map<String, String> getTicketStatusMap(TktRequestHeader header) {
		if (null == ticketStatusMap) {
			ticketStatusMap = new LinkedHashMap<>();

			setTicketStatusMap(header);
		}

		return ticketStatusMap;
	}

	/**
	 * @param	ticketCategory 	<code>String</code>
	 * @param	header			<code>TktRequestHeader</code>
	 * @return	Map&lt;String, String&gt;
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> getTicketTypeMap(
		String ticketCategory, TktRequestHeader header) {

		Map<String, String> requestTypeMap = new HashMap<>();

		FindTypesRequest serviceRequest = new FindTypesRequest();

		serviceRequest.setHeader(header);
		serviceRequest.setDestinationId("0");
		serviceRequest.setActive(Boolean.TRUE);

		FindTypesResponse response = ticketingService.findTicketTypes(
			serviceRequest);

		if (null != response) {
			if (null != response.getHeader()) {
				String statusCode = response.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					List<TypeType> types = response.getTypes();

					for (TypeType type : types) {
						requestTypeMap.put(
							String.valueOf(type.getTypeId()), type.getName());
					}

					requestTypeMap = CommonUtil.sortMapByValueComparator(
						requestTypeMap);
				}
			}
		}

		return requestTypeMap;
	}

	@Override
	public GetTicketResponse ticketDetails(
		String ticketId, Boolean isVsmTicket, TktRequestHeader header,
		Boolean isDecrypted) {

		GetTicketResponse response = null;
		GetTicketRequest request = new GetTicketRequest();

		if (null != header) {
			request.setHeader(header);

			if (StringUtils.isNotBlank(ticketId)) {
				if (!isVsmTicket) {
					String[] parts = ticketId.split(Constants.DASH);
					request.setTicketId(parts[0]);
				}
				else {
					request.setDestinationTicketId(ticketId);
				}

				request.setDecrypted(isDecrypted);

				response = ticketingService.getServiceRequest(request);
			}
			else {
				logger.error(
					"ticketDetails - ticketId is null. Cannot find ticket details.");
			}
		}

		return response;
	}

	/**
	 *
	 */
	@Override
	public TouchTicketResponse touchTicket(
		TicketType ticket, TktRequestHeader header) {

		logger.debug("touchTicket - start");

		TouchTicketRequest request = new TouchTicketRequest();

		request.setHeader(header);

		if (null != ticket) {
			request.setTicket(ticket);

			logger.debug(
				"touchTicket - Ticket Id: " +
					request.getTicket(
					).getId());
			logger.debug(
				"touchTicket - Last Read Timestamp: " +
					request.getTicket(
					).getLastReadTimestamp());
		}
		else {
			logger.error("touchTicket - ticket is null");
		}

		TouchTicketResponse response = ticketingService.touchTicket(request);

		logger.debug("touchTicket - end");

		return response;
	}

	/**
	 *
	 */
	@Override
	public UploadAttachmentResponse uploadAttachment(
		UploadAttachmentRequest request) {

		UploadAttachmentResponse response = ticketingService.uploadAttachment(
			request);

		if (null != response) {
			if (null != response.getHeader()) {
				String statusCode = response.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					return response;
				}
			}
		}

		return response;
	}

	private FindTicketsRequest addFilterParameters(
		TicketFilter filter, FindTicketsRequest request) {

		if (StringUtils.isNotBlank(filter.getProduct()) &&
			!StringUtils.equalsIgnoreCase(Constants.ALL, filter.getProduct())) {

			request.setProduct(filter.getProduct());
		}

		if (StringUtils.isNotBlank(filter.getTicketCategory()) &&
			!StringUtils.equalsIgnoreCase(
				Constants.ALL, filter.getTicketCategory())) {

			request.setCategory(filter.getTicketCategory());
		}

		if (StringUtils.isNotBlank(filter.getTicketStatus()) &&
			!StringUtils.equalsIgnoreCase(
				Constants.ALL, filter.getTicketStatus())) {

			request.setStatus(filter.getTicketStatus());
		}

		if (StringUtils.isNotBlank(filter.getSubmittedBy())) {
			if (!StringUtils.equalsIgnoreCase("0", filter.getSubmittedBy())) {
				request.setOrganization(filter.getSubmittedBy());
			}
		}

		if (StringUtils.isNotBlank(filter.getSubmittedWithin())) {
			XMLGregorianCalendar cal = CommonUtil.getXmlCalendarDateInThePast(
				Integer.parseInt(filter.getSubmittedWithin()));

			request.setSubmittedWithin(cal);
		}

		if (StringUtils.isNotBlank(filter.getUpdatedWithin())) {
			XMLGregorianCalendar cal = CommonUtil.getXmlCalendarDateInThePast(
				Integer.parseInt(filter.getUpdatedWithin()));

			logger.debug("addFilterParameters - updatedWithin is: " + cal);
			request.setUpdatedWithin(cal);
		}

		return request;
	}

	/**
	 *
	 * @param ticket
	 * @return
	 */
	// TODO: Uncomment this method when the users are allowed to add requests
	// from the Service Request portlet.

	private TicketType prepareTicketToAdd(Ticket ticket) {
		TicketType ticketType = null;

		/*if (ticket != null) {
			ticketType = new TicketType();

			TicketUser user = ticket.getUser();

			ticketType.setFirstName(user.getFirstName());
			ticketType.setLastName(user.getLastName());
			ticketType.setEmail(user.getEmail());
			ticketType.setPhoneNo(user.getPhoneNumber());
			ticketType.setOrgShortName(user.getOrganisation());
			ticketType.setCategory(ticket.getTicketCategory());
			ticketType.setProduct(ticket.getProduct());
			ticketType.setType(ticket.getTicketType());
			ticketType.setDescription(ticket.getTicketDescription());
		}*/

		return ticketType;
	}

	/**
	 *
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	private void setProductMap(TktRequestHeader header) {
		FindTypesRequest serviceRequest = new FindTypesRequest();

		serviceRequest.setHeader(header);
		serviceRequest.setDestinationId("0");
		serviceRequest.setActive(Boolean.TRUE);
		FindTypesResponse response = ticketingService.findTicketTypes(
			serviceRequest);

		if (null != response) {
			if (null != response.getHeader()) {
				String statusCode = response.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase("success", statusCode)) {
					List<TypeType> productTypes = response.getTypes();

					for (TypeType productType : productTypes) {
						/*logger.debug("setProductMap - adding product: "
								+ productType.getDescription());*/
						if (StringUtils.isNotBlank(
								productType.getDescription())) {

							productMap.put(
								productType.getDescription(),
								productType.getDescription());
						}
					}
				}
			}

			productMap = CommonUtil.sortMapByValueComparator(productMap);
			/*Set<String> s = productMap.keySet();
			Iterator<String> i = s.iterator();
			while(i.hasNext()) {
				logger.debug(i.next());
			}*/
		}
	}

	/**
	 *
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	private void setTicketCategoryMap(TktRequestHeader header) {
		FindCategoriesRequest serviceRequest = new FindCategoriesRequest();

		serviceRequest.setHeader(header);
		FindCategoriesResponse response = ticketingService.findCategories(
			serviceRequest);

		if (null != response) {
			if (null != response.getHeader()) {
				String statusCode = response.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase("Success", statusCode)) {
					List<CategoryType> categories = response.getCategories();

					for (CategoryType category : categories) {
						ticketCategoryMap.put(
							String.valueOf(category.getCategoryId()),
							category.getName());
					}
				}

				ticketCategoryMap = CommonUtil.sortMapByValueComparator(
					ticketCategoryMap);
			}
		}
	}

	/**
	 *
	 * @param request
	 */
	private void setTicketStatusMap(TktRequestHeader header) {
		FindStatusRequest serviceRequest = new FindStatusRequest();

		serviceRequest.setHeader(header);
		FindStatusesResponse response = ticketingService.findStatus(
			serviceRequest);

		if (null != response) {
			if (null != response.getHeader()) {
				String statusCode = response.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					List<StatusType> statuses = response.getStatuses();

					for (StatusType status : statuses) {
						if (!StringUtils.equalsIgnoreCase(
								Constants.RE_OPENED, status.getName())) {

							ticketStatusMap.put(
								String.valueOf(status.getStatusId()),
								status.getName());
						}
					}
				}
			}
		}
	}

	/* Private attributes */

	private static final Logger logger = Logger.getLogger(
			CuscalTicketingServiceImpl.class);

	private Map<String, String> productMap = null;
	private Map<String, String> ticketCategoryMap = null;
	private Map<String, String> ticketStatusMap = null;

	private final CardService cardService = CuscalServiceLocator.getService(
		CardService.class.getName());
	private final EntityService entityService = CuscalServiceLocator.getService(
		EntityService.class.getName());
	private final CustomerSelfServiceService ticketingService= CuscalServiceLocator.getService(
		CustomerSelfServiceService.class.getName());

}