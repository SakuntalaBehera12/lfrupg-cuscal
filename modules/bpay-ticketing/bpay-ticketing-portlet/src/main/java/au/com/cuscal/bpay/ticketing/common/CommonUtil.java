package au.com.cuscal.bpay.ticketing.common;

import au.com.cuscal.bpay.ticketing.domain.*;
import au.com.cuscal.bpay.ticketing.forms.*;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.audit.xml.XmlMessage;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.transaction.Customer;

import com.liferay.expando.kernel.model.ExpandoBridge;
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
import java.util.Properties;
import java.util.TreeMap;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Common Utility class for BPay module.
 *
 *
 */
public class CommonUtil {

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

	public static String getKeyFromValue(
		HashMap<Long, String> map, String value) {

		String key = "";
		value = StringUtils.trimToEmpty(value);

		for (Map.Entry<Long, String> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				key = entry.getKey(
				).toString();

				break;
			}
		}

		return key;
	}

	/**
	 *
	 * @return
	 */
	public static Map<Integer, String> getLastModifiedMap() {
		Map<Integer, String> map = new TreeMap<>();

		map.put(-1, "Any date");
		map.put(0, "Today");
		map.put(3, "Last 3 days");
		map.put(7, "Last 7 days");
		map.put(30, "Last 30 days");
		map.put(180, "Last 180 days");

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
					else if (StringUtils.isNotBlank(
								customer.getAuthenticAcquirerShortName())) {

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
	 * @return	RequestContactInformation
	 */
	public static RequestContactInformation getUserDetailsFromRequest(
		PortletRequest request, BPayProperties bpayProperties) {

		RequestContactInformation requestContactInformation = null;

		try {
			User user = getUserFromRequest(request);

			if (null != user) {
				Organization userOrg = getOrganisationForUser(user);

				if (null != userOrg) {
					requestContactInformation = new RequestContactInformation();

					requestContactInformation.setGivenName(user.getFirstName());
					requestContactInformation.setSurname(user.getLastName());
					requestContactInformation.setEmail(user.getEmailAddress());
					requestContactInformation.setOrganisation(
						userOrg.getName());

					ExpandoBridge eb = userOrg.getExpandoBridge();

					if (eb != null) {
						Properties prop = bpayProperties.getBpayProperty();

						requestContactInformation.setBsb(
							(String)eb.getAttribute(
								prop.getProperty(Constants.CUSCAL_BSB)));
					}
				}
			}
		}
		catch (PortalException e) {
			logger.error("getUserDetailsFromRequest - " + e.getMessage(), e);
		}
		catch (SystemException e) {
			logger.error("getUserDetailsFromRequest - " + e.getMessage(), e);
		}

		return requestContactInformation;
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

	/**
	 * fill the object with the list data from properties.
	 *
	 */
	public static BPayListBoxData populateBPayListBoxData(
		BPayProperties bpayProperties) {

		BPayListBoxData bPayListBoxData = new BPayListBoxData();

		bPayListBoxData.setInvestigationTypeMap(
			retrieveBPayListBoxProperty(
				bpayProperties,
				Constants.PROPERTIES_PREFIX_BPAY_INVESTIGATION_TYPE,
				Constants.DESCRIPTION));
		bPayListBoxData.setEntryMethodMap(
			retrieveBPayListBoxProperty(
				bpayProperties, Constants.PROPERTIES_PREFIX_BPAY_ENTRY_METHOD,
				Constants.DESCRIPTION));
		bPayListBoxData.setPaymentMethodMap(
			retrieveBPayListBoxProperty(
				bpayProperties, Constants.PROPERTIES_PREFIX_BPAY_PAYMENT_METHOD,
				Constants.DESCRIPTION));
		bPayListBoxData.setReasonPaymentInvestigationMap(
			retrieveBPayListBoxProperty(
				bpayProperties,
				Constants.PROPERTIES_PREFIX_BPAY_PI_REQUEST_REASON,
				Constants.DESCRIPTION));
		bPayListBoxData.setReasonErrorCorrectionMap(
			retrieveBPayListBoxProperty(
				bpayProperties,
				Constants.PROPERTIES_PREFIX_BPAY_EC_REQUEST_REASON,
				Constants.DESCRIPTION));
		//		bPayListBoxData.setBpayTypeOfi(retrieveBPayListBoxProperty(bpayProperties,Constants.PROPERTIES_PREFIX_BPAY_TYPE_OFI,Constants.ID));

		bPayListBoxData.setFraudTypeMap(
			retrieveBPayListBoxProperty(
				bpayProperties, Constants.PROPERTIES_PREFIX_BPAY_FRAUD_TYPE,
				Constants.DESCRIPTION));
		bPayListBoxData.setScamTypeMap(
			retrieveBPayListBoxProperty(
				bpayProperties, Constants.PROPERTIES_PREFIX_BPAY_SCAM_TYPE,
				Constants.DESCRIPTION));
		bPayListBoxData.setFraudTypeToolTipMap(
			retrieveBPayListBoxProperty(
				bpayProperties, Constants.PROPERTIES_PREFIX_BPAY_FRAUD_TOOL_TIP,
				Constants.DESCRIPTION));
		bPayListBoxData.setScamTypeToolTipMap(
			retrieveBPayListBoxProperty(
				bpayProperties, Constants.PROPERTIES_PREFIX_BPAY_SCAM_TOOL_TIP,
				Constants.DESCRIPTION));

		return bPayListBoxData;
	}

	/**
	 *
	 * @param unsortedMap
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Map sortMapByValueComparator(Map unsortedMap) {
		List list = new LinkedList(unsortedMap.entrySet());

		// sort list based on comparator

		Collections.sort(
			list,
			new Comparator() {

				public int compare(Object o1, Object o2) {
					if ((null != ((Map.Entry)o1).getValue()) &&
						(null != ((Map.Entry)o2).getValue())) {

						return ((Comparable)((Map.Entry)o1).getValue(
						).toString(
						).toLowerCase()).compareTo(
							((Map.Entry)o2).getValue(
							).toString(
							).toLowerCase());
					}

					return 0;
				}

			});

		// put sorted list into map again
		// LinkedHashMap make sure order in which keys were inserted

		Map sortedMap = new LinkedHashMap();

		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	/**
	 * Read bpay drop down list box properties.
	 *
	 */
	private static HashMap<Long, String> retrieveBPayListBoxProperty(
		BPayProperties bpayProperties, String prefix, String suffix) {

		HashMap<Long, String> nameMap = new HashMap<>();

		long i = 1;

		while (true) {
			String key = prefix.concat(
				Constants.DOT + i
			).concat(
				Constants.DOT
			).concat(
				suffix
			);

			String name = bpayProperties.getBpayProperty(
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