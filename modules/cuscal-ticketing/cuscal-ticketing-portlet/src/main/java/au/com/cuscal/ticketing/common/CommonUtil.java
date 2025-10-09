//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.common;

import static au.com.cuscal.ticketing.common.Constants.MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID;
import static au.com.cuscal.ticketing.common.Constants.MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID;
import static au.com.cuscal.ticketing.common.Constants.MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID;
import static au.com.cuscal.ticketing.common.Constants.SERVICE_REQUEST_STATUS_CANCELLED;
import static au.com.cuscal.ticketing.common.Constants.SERVICE_REQUEST_STATUS_REVERSED;
import static au.com.cuscal.ticketing.common.Constants.STATUS_CLOSED;
import static au.com.cuscal.ticketing.common.Constants.STATUS_ON_HOLD;

import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.audit.xml.XmlMessage;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.transaction.Customer;
import au.com.cuscal.ticketing.domain.ServiceCatalogueData;
import au.com.cuscal.ticketing.domain.Ticket;
import au.com.cuscal.ticketing.domain.TicketFilter;
import au.com.cuscal.ticketing.domain.TicketUser;
import au.com.cuscal.ticketing.domain.view.DecoratedTicket;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CommonUtil {

	/**
	 *
	 * @param ticketFilter
	 * @param xmlMessage
	 * @return
	 */
	public static XmlMessage addFilterTicketParamsToXmlMessage(
		TicketFilter ticketFilter, XmlMessage xmlMessage) {

		if (StringUtils.isNotBlank(ticketFilter.getTicketNumber())) {
			xmlMessage.addParameter(
				"ticketNumber", ticketFilter.getTicketNumber());
		}

		if (StringUtils.isNotBlank(ticketFilter.getProduct()) &&
			!StringUtils.equalsIgnoreCase(
				Constants.ALL, ticketFilter.getProduct())) {

			xmlMessage.addParameter("ticketProduct", ticketFilter.getProduct());
		}

		if (StringUtils.isNotBlank(ticketFilter.getTicketCategory()) &&
			!StringUtils.equalsIgnoreCase(
				Constants.ALL, ticketFilter.getTicketCategory())) {

			xmlMessage.addParameter(
				"ticketCategory", ticketFilter.getTicketCategory());
		}

		if (StringUtils.isNotBlank(ticketFilter.getTicketStatus()) &&
			!StringUtils.equalsIgnoreCase(
				Constants.ALL, ticketFilter.getTicketStatus())) {

			xmlMessage.addParameter(
				"ticketStatus", ticketFilter.getTicketStatus());
		}

		if (StringUtils.isNotBlank(ticketFilter.getSubmittedBy())) {
			if (!StringUtils.equalsIgnoreCase(
					"0", ticketFilter.getSubmittedBy())) {

				xmlMessage.addParameter(
					"ticketSubmittedBy", ticketFilter.getSubmittedBy());
			}
		}

		if (StringUtils.isNotBlank(ticketFilter.getSubmittedWithin())) {
			XMLGregorianCalendar cal = CommonUtil.getXmlCalendarDateInThePast(
				Integer.parseInt(ticketFilter.getSubmittedWithin()));

			xmlMessage.addParameter("ticketSubmittedWithin", cal.toString());
		}

		if (StringUtils.isNotBlank(ticketFilter.getUpdatedWithin())) {
			XMLGregorianCalendar cal = CommonUtil.getXmlCalendarDateInThePast(
				Integer.parseInt(ticketFilter.getUpdatedWithin()));

			xmlMessage.addParameter("ticketUpdatedWithin", cal.toString());
		}

		return xmlMessage;
	}

	/**
	 *
	 * @param header
	 * @param xmlMessage
	 * @return
	 */
	public static XmlMessage addHeaderInformationToXmlMessage(
		TktRequestHeader header, XmlMessage xmlMessage) {

		if (null != header) {
			xmlMessage.addParameter("userId", header.getUserId());
			xmlMessage.addParameter("userOrgId", header.getUserOrgId());
			xmlMessage.addParameter("origin", header.getOrigin());
			/*if (StringUtils.isNotBlank(header.getPageNo())) {
				xmlMessage.addParameter("pageNumber", header.getPageNo());
			}*/
		}
		else {
			xmlMessage.addParameter("error", "header is null");
		}

		return xmlMessage;
	}

	/**
	 *
	 * @param ticket
	 * @param xmlMessage
	 * @return
	 */
	public static XmlMessage addNewAttachmentParamsToXmlMessage(
		long size, String fileName, String ticketId, XmlMessage xmlMessage) {

		xmlMessage.addParameter("ticketId", ticketId);
		xmlMessage.addParameter("fileName", fileName);
		xmlMessage.addParameter("size", String.valueOf(size));

		return xmlMessage;
	}

	/**
	 *
	 * @param ticket
	 * @param xmlMessage
	 * @return
	 */
	public static XmlMessage addNewTicketParamsToXmlMessage(
		Ticket ticket, XmlMessage xmlMessage) {

		TicketUser user = ticket.getUser();

		xmlMessage.addParameter("firstName", user.getFirstName());
		xmlMessage.addParameter("lastName", user.getLastName());
		xmlMessage.addParameter("email", user.getEmail());
		xmlMessage.addParameter("phoneNumber", user.getPhoneNumber());
		xmlMessage.addParameter("organisation", user.getOrganisation());
		xmlMessage.addParameter("ticketProduct", ticket.getProduct());
		xmlMessage.addParameter("ticketCategory", ticket.getTicketCategory());
		xmlMessage.addParameter("ticketRequestType", ticket.getTicketType());
		xmlMessage.addParameter(
			"ticketDescription", ticket.getTicketDescription());

		return xmlMessage;
	}

	/**
	 *
	 * @param xmlMessage
	 * @param category
	 * @param request
	 * @param response
	 */
	public static void auditFailXmlMessage(
		XmlMessage xmlMessage, String category, PortletRequest request,
		PortletResponse response) {

		String message = xmlMessage.toXml();

		audit.fail(
			response, request, AuditOrigin.PORTAL_ORIGIN, category, message);
	}

	/**
	 *
	 * @param xmlMessage
	 * @param category
	 * @param request
	 * @param response
	 */
	public static void auditSuccessXmlMessage(
		XmlMessage xmlMessage, String category, PortletRequest request,
		PortletResponse response) {

		String message = xmlMessage.toXml();

		audit.success(
			response, request, AuditOrigin.PORTAL_ORIGIN, category, message);
	}

	/**
	 *
	 * @param 	xmlCal	<em>XMLGregorianCalendar</em>
	 * @return	Date
	 */
	public static Date getDateFromXmlCalendar(XMLGregorianCalendar xmlCal) {
		Date date = null;

		if (null != xmlCal) {
			GregorianCalendar cal = xmlCal.toGregorianCalendar();
			date = new Date();
			date = cal.getTime();
		}

		return date;
	}

	/**
	 *
	 * @return
	 */
	public static Map<Integer, String> getLastModifiedMap() {
		final Map<Integer, String> map = new TreeMap<>();
		map.put(0, "Today");
		map.put(3, "Last 3 days");
		map.put(7, "Last 7 days");
		map.put(30, "Last 30 days");
		map.put(180, "Last 180 days");
		map.put(-1, "Any date");

		return map;
	}

	/**
	 *
	 * @param user
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static Organization getOrganisationForUser(User user)
		throws PortalException {

		Organization org = null;

		List<Organization> orgs = user.getOrganizations();

		logger.debug("getOrganisationForUser - Found: " + orgs.size());

		if (orgs.size() == 1) {
			org = orgs.get(0);
		}
		else {
			logger.error(
				"getOrganisationForUser - Organisations assigned to user are: " +
					orgs.size());
		}

		return org;
	}

	/**
	 *
	 * @param customers
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getSubmittedByMap(
		List<Customer> customers, String userOrgShortName,
		PortletRequest request) {

		Map<String, String> unsortedMap = new HashMap<>();
		Map<String, String> sortedMap = new LinkedHashMap<>();

		for (Customer customer : customers) {
			if ((null != customer) &&
				StringUtils.isNotBlank(customer.getName())) {

				if (!(StringUtils.equalsIgnoreCase(
						customer.getAuthenticAcquirerShortName(),
						userOrgShortName) ||
					  StringUtils.equalsIgnoreCase(
						  customer.getAuthenticIssuerShortName(),
						  userOrgShortName))) {

					if (StringUtils.isNotBlank(
							customer.getAuthenticIssuerShortName())) {

						unsortedMap.put(
							customer.getAuthenticIssuerShortName(),
							customer.getName());
					}
					else {
						if (!StringUtils.isNotBlank(
								customer.getAuthenticAcquirerShortName())) {

							continue;
						}

						unsortedMap.put(
							customer.getAuthenticAcquirerShortName(),
							customer.getName());
					}
				}
				else {
					sortedMap.put(userOrgShortName, customer.getName());
				}
			}
		}

		sortedMap.put(Constants.ALL_ORGANISATIONS, "All Organisations");
		sortedMap.putAll(sortMapByValueComparator(unsortedMap));

		return sortedMap;
	}

	/**
	 *
	 * @param 	request	<em>PortletRequest</em>
	 * @return	TicketUser
	 */
	public static TicketUser getUserDetailsFromRequest(PortletRequest request) {
		TicketUser ticketUser = null;

		try {
			User user = getUserFromRequest(request);

			if (null != user) {
				Organization userOrg = getOrganisationForUser(user);

				if (null != userOrg) {
					ticketUser = new TicketUser();

					ticketUser.setFirstName(user.getFirstName());
					ticketUser.setLastName(user.getLastName());
					ticketUser.setFullName(user.getFullName());
					ticketUser.setEmail(user.getEmailAddress());
					ticketUser.setOrganisation(userOrg.getName());
				}
			}
		}
		catch (PortalException e) {
			logger.error("getUserDetailsFromRequest - " + e.getMessage(), e);
		}
		catch (SystemException e) {
			logger.error("getUserDetailsFromRequest - " + e.getMessage(), e);
		}

		return ticketUser;
	}

	/**
	 *
	 * @param request
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static User getUserFromRequest(PortletRequest request)
		throws PortalException {

		return PortalUtil.getUser(request);
	}

	/**
	 *
	 * @param 	when	<em>Integer</em>
	 * @return	XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar getXmlCalendarDate(Date date) {
		XMLGregorianCalendar xmlCal = null;

		GregorianCalendar cal = new GregorianCalendar();

		cal.setTime(date);

		try {
			xmlCal = DatatypeFactory.newInstance(
			).newXMLGregorianCalendar(
				cal
			);
		}
		catch (DatatypeConfigurationException e) {
			logger.error("getXmlCalendarDateInThePast - " + e.getMessage(), e);
		}

		return xmlCal;
	}

	/**
	 *
	 * @param 	when	<em>Integer</em>
	 * @return	XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar getXmlCalendarDateInThePast(
		Integer when) {

		XMLGregorianCalendar xmlCal = null;

		GregorianCalendar cal = new GregorianCalendar();

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		try {

			// Set when to 365 days when the user selects Any Date in the
			// filter.

			if (when == -1) {
				when = 365;
			}

			cal.add(Calendar.DAY_OF_YEAR, -when);
			xmlCal = DatatypeFactory.newInstance(
			).newXMLGregorianCalendar(
				cal
			);
		}
		catch (DatatypeConfigurationException e) {
			logger.error("getXmlCalendarDateInThePast - " + e.getMessage(), e);
		}

		return xmlCal;
	}

	public static boolean isEligibleToCancelMasterCardDispute(
		DecoratedTicket decoratedTicket) {

		boolean cancelFlag = false;

		if (null == decoratedTicket.getTicket(
			).getCurrentStatus()) {

			return false;
		}

		String serviceRequestStatus = decoratedTicket.getServiceRequestStatus();

		if (SERVICE_REQUEST_STATUS_CANCELLED.equals(serviceRequestStatus) ||
			SERVICE_REQUEST_STATUS_REVERSED.equals(serviceRequestStatus)) {

			return false;
		}

		int statusId = (int)decoratedTicket.getTicket(
		).getCurrentStatus(
		).getStatusId();
		//0	Submitted : 1	Pending : 2	Open : 3	Closed : 4	Re-opened : 5	On Hold : 6	Action
		switch (statusId) {
			case 0:
			case 1:
			case 2:
			case 5:
				cancelFlag = true;

				break;
			default:
				cancelFlag = false;
		}

		return cancelFlag;
	}

	public static boolean isEligibleToEditMasterCardDispute(
		DecoratedTicket decoratedTicket, long typeIdLong) {

		boolean editFlag = false;

		if (null == decoratedTicket.getTicket(
			).getCurrentStatus()) {

			return false;
		}

		String serviceRequestStatus = decoratedTicket.getServiceRequestStatus();

		if (SERVICE_REQUEST_STATUS_CANCELLED.equals(serviceRequestStatus) ||
			SERVICE_REQUEST_STATUS_REVERSED.equals(serviceRequestStatus)) {

			return false;
		}

		if (STATUS_ON_HOLD == decoratedTicket.getTicket(
			).getCurrentStatus(
			).getStatusId()) {

			editFlag = true;
		}

		return editFlag;
	}

	public static boolean isEligibleToReverseMasterCardDispute(
		SelfServiceProperties selfServiceProperties,
		DecoratedTicket decoratedTicket, long typeIdLong) {

		boolean reverseFlag = false;

		if (null == decoratedTicket.getTicket(
			).getCurrentStatus()) {

			return false;
		}
		//0	Submitted : 1	Pending : 2	Open : 3	Closed : 4	Re-opened : 5	On Hold : 6	Action

		if (STATUS_CLOSED != decoratedTicket.getTicket(
			).getCurrentStatus(
			).getStatusId()) {

			return false;
		}

		String serviceRequestStatus = decoratedTicket.getServiceRequestStatus();

		if (SERVICE_REQUEST_STATUS_CANCELLED.equals(serviceRequestStatus) ||
			SERVICE_REQUEST_STATUS_REVERSED.equals(serviceRequestStatus)) {

			return false;
		}
		//if(TICKET_EXPOERT_IPM_STATUS_PROCESSED.equals(decoratedTicket.getExportIPM())){
		long masterCardDisputeRetrievalRequestId = Long.parseLong(
			selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID
			));
		long masterCardDisputeFirstChargebackId = Long.parseLong(
			selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID
			));
		long masterCardDisputeArbitrationChargebackId = Long.parseLong(
			selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID
			));

		if ((typeIdLong == masterCardDisputeRetrievalRequestId) ||
			(typeIdLong == masterCardDisputeFirstChargebackId) ||
			(typeIdLong == masterCardDisputeArbitrationChargebackId)) {

			reverseFlag = true;
		}
		//}

		return reverseFlag;
	}

	public static ServiceCatalogueData populateOfiSCData(
		SelfServiceProperties selfServiceProperties) {

		ServiceCatalogueData serviceCatalogueData = new ServiceCatalogueData();

		serviceCatalogueData.setDescriptionMap(
			retrieveSelfServiceSCProperty(
				selfServiceProperties, Constants.PROPERTIES_SUFFIX_DESCRIPTION,
				Constants.PROPERTIES_PREFIX_SERVICE_REQUEST_CATALOGUE_OFI));
		serviceCatalogueData.setUrlMap(
			retrieveSelfServiceSCProperty(
				selfServiceProperties, Constants.PROPERTIES_SUFFIX_URL,
				Constants.PROPERTIES_PREFIX_SERVICE_REQUEST_CATALOGUE_OFI));
		serviceCatalogueData.setTypeIdMap(
			retrieveSelfServiceSCProperty(
				selfServiceProperties, Constants.PROPERTIES_SUFFIX_TYPE_ID,
				Constants.PROPERTIES_PREFIX_SERVICE_REQUEST_CATALOGUE_OFI));

		return serviceCatalogueData;
	}

	public static ServiceCatalogueData populateSCData(
		SelfServiceProperties selfServiceProperties) {

		ServiceCatalogueData serviceCatalogueData = new ServiceCatalogueData();

		serviceCatalogueData.setDescriptionMap(
			retrieveSelfServiceSCProperty(
				selfServiceProperties, Constants.PROPERTIES_SUFFIX_DESCRIPTION,
				Constants.PROPERTIES_PREFIX_SERVICE_REQUEST_CATALOGUE));
		serviceCatalogueData.setUrlMap(
			retrieveSelfServiceSCProperty(
				selfServiceProperties, Constants.PROPERTIES_SUFFIX_URL,
				Constants.PROPERTIES_PREFIX_SERVICE_REQUEST_CATALOGUE));
		serviceCatalogueData.setTypeIdMap(
			retrieveSelfServiceSCProperty(
				selfServiceProperties, Constants.PROPERTIES_SUFFIX_TYPE_ID,
				Constants.PROPERTIES_PREFIX_SERVICE_REQUEST_CATALOGUE));

		return serviceCatalogueData;
	}

	/**
	 *
	 * @param unsortedMap
	 * @return
	 */
	public static Map sortMapByValueComparator(final Map unsortedMap) {
		final List list = new LinkedList(unsortedMap.entrySet());
		Collections.sort(
			(List<Object>)list,
			new Comparator() {

				@Override
				public int compare(final Object o1, final Object o2) {
					if ((null != ((Map.Entry)o1).getValue()) &&
						(null != ((Map.Entry)o2).getValue())) {

						return ((Map.Entry)o1).getValue(
						).toString(
						).toLowerCase(
						).compareTo(
							((Map.Entry)o2).getValue(
							).toString(
							).toLowerCase()
						);
					}

					return 0;
				}

			});
		Map sortedMap = new LinkedHashMap();

		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	/**
	 * Read selfservice service catalogue properties.
	 *
	 */
	private static HashMap<Long, String> retrieveSelfServiceSCProperty(
		SelfServiceProperties selfServiceProperties, String type,
		String prefix) {

		HashMap<Long, String> nameMap = new HashMap<>();

		long i = 1;

		while (true) {
			String key = prefix.concat(
				Constants.DOT
			).concat(
				i + Constants.DOT
			).concat(
				type
			);

			String name = selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				key
			);

			if (StringUtils.isBlank(name)) {
				break;
			}

			nameMap.put(i, name);

			i++;
		}

		return nameMap;
	}

	private static Auditor audit = Auditor.getInstance();
	private static Logger logger = Logger.getLogger(CommonUtil.class);

}