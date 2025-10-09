//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.common;

import au.com.cuscal.de.ticketing.domain.DEListBoxData;
import au.com.cuscal.de.ticketing.forms.AttributesInformation;
import au.com.cuscal.de.ticketing.forms.RequestContactInformation;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.audit.xml.XmlMessage;
import au.com.cuscal.framework.cards.CardUtil;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.transaction.Customer;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.PortalUtil;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CommonUtil {

	public static XmlMessage addHeaderInformationToXmlMessage(
		final TktRequestHeader header, final XmlMessage xmlMessage) {

		if (null != header) {
			xmlMessage.addParameter("userId", header.getUserId());
			xmlMessage.addParameter("userOrgId", header.getUserOrgId());
			xmlMessage.addParameter("origin", header.getOrigin());
		}
		else {
			xmlMessage.addParameter("error", "header is null");
		}

		return xmlMessage;
	}

	public static XmlMessage addNewAttachmentParamsToXmlMessage(
		final long size, final String fileName, final String ticketId,
		final XmlMessage xmlMessage) {

		xmlMessage.addParameter("ticketId", ticketId);
		xmlMessage.addParameter("fileName", fileName);
		xmlMessage.addParameter("size", String.valueOf(size));

		return xmlMessage;
	}

	public static void auditFailXmlMessage(
		final XmlMessage xmlMessage, final String category,
		final PortletRequest request, final PortletResponse response) {

		final String message = xmlMessage.toXml();
		CommonUtil.audit.fail(response, request, "PORTAL", category, message);
	}

	public static void auditSuccessXmlMessage(
		final XmlMessage xmlMessage, final String category,
		final PortletRequest request, final PortletResponse response) {

		final String message = xmlMessage.toXml();
		CommonUtil.audit.success(
			response, request, "PORTAL", category, message);
	}

	public static Boolean containsPAN(final String text) {
		if (StringUtils.isBlank(text) || (text.length() < 14)) {
			return Boolean.FALSE;
		}

		final Pattern p = Pattern.compile(Constants.CC_PATTERN);
		Matcher m;
		Boolean result;
		String number;

		for (m = p.matcher(text), result = Boolean.FALSE; m.find() && !result;
			 result = CommonUtil.cardUtil.panValidate(number)) {

			number = text.substring(m.start(), m.end());
			number = number.replaceAll(
				Constants.DASH, ""
			).replaceAll(
				Constants.SPACE, ""
			);
		}

		return result;
	}

	public static String formatBsb(String bsb) {
		if (StringUtils.isNotBlank(bsb)) {
			bsb = bsb.replaceAll("\\.", "");
			bsb = bsb.substring(0, 3) + "." + bsb.substring(3, bsb.length());
		}

		return bsb;
	}

	public static Date getDateFromXmlCalendar(
		final XMLGregorianCalendar xmlCal) {

		Date date = null;

		if (null != xmlCal) {
			final GregorianCalendar cal = xmlCal.toGregorianCalendar();
			date = new Date();
			date = cal.getTime();
		}

		return date;
	}

	public static String getKeyFromValue(
		final HashMap<Long, String> map, final String value) {

		String key = "";

		for (final Map.Entry<Long, String> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				key = entry.getKey(
				).toString();

				break;
			}
		}

		return key;
	}

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

	public static Organization getOrganisationForUser(final User user)
		throws PortalException {

		Organization org = null;
		final List<Organization> orgs = user.getOrganizations();
		CommonUtil.logger.debug(
			(Object)("getOrganisationForUser - Found: " + orgs.size()));

		if (orgs.size() == 1) {
			org = orgs.get(0);
		}
		else {
			CommonUtil.logger.error(
				(Object)
					("getOrganisationForUser - Organisations assigned to user are: " +
						orgs.size()));
		}

		return org;
	}

	public static Map<String, String> getSubmittedByMap(
		final List<Customer> customers, final String userOrgShortName,
		final PortletRequest request) {

		final Map<String, String> unsortedMap = new HashMap<>();
		final Map<String, String> sortedMap = new LinkedHashMap<>();

		for (final Customer customer : customers) {
			if ((null != customer) &&
				StringUtils.isNotBlank(customer.getName())) {

				if (!StringUtils.equalsIgnoreCase(
						customer.getAuthenticAcquirerShortName(),
						userOrgShortName) &&
					!StringUtils.equalsIgnoreCase(
						customer.getAuthenticIssuerShortName(),
						userOrgShortName)) {

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

	public static RequestContactInformation getUserDetailsFromRequest(
		final PortletRequest request, final DEProperties deProperties) {

		RequestContactInformation requestContactInformation = null;

		try {
			final User user = getUserFromRequest(request);

			if (null != user) {
				final Organization userOrg = getOrganisationForUser(user);

				if (null != userOrg) {
					requestContactInformation = new RequestContactInformation();
					requestContactInformation.setContactOfficerFirstName(
						user.getFirstName());
					requestContactInformation.setContactOfficerLastName(
						user.getLastName());
					requestContactInformation.setEmailFrom(
						user.getEmailAddress());
					requestContactInformation.setReqInstFullNamefrom(
						userOrg.getName());
					final ExpandoBridge eb = userOrg.getExpandoBridge();

					if (eb != null) {
						final Properties prop = deProperties.getDeProperty();
						requestContactInformation.setBsb(
							(String)eb.getAttribute(
								prop.getProperty(Constants.CUSCAL_BSB)));
					}
				}
			}
		}
		catch (PortalException e) {
			CommonUtil.logger.error(
				(Object)("getUserDetailsFromRequest - " + e.getMessage()),
				(Throwable)e);
		}
		catch (SystemException e2) {
			CommonUtil.logger.error(
				(Object)("getUserDetailsFromRequest - " + e2.getMessage()),
				(Throwable)e2);
		}

		return requestContactInformation;
	}

	public static User getUserFromRequest(final PortletRequest request)
		throws PortalException {

		return PortalUtil.getUser(request);
	}

	public static XMLGregorianCalendar getXmlCalendarDate(final Date date) {
		XMLGregorianCalendar xmlCal = null;
		final GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);

		try {
			xmlCal = DatatypeFactory.newInstance(
			).newXMLGregorianCalendar(
				cal
			);
		}
		catch (DatatypeConfigurationException e) {
			CommonUtil.logger.error(
				(Object)("getXmlCalendarDateInThePast - " + e.getMessage()),
				(Throwable)e);
		}

		return xmlCal;
	}

	public static XMLGregorianCalendar getXmlCalendarDateInThePast(
		Integer when) {

		XMLGregorianCalendar xmlCal = null;
		final GregorianCalendar cal = new GregorianCalendar();
		cal.set(11, 0);
		cal.set(12, 0);
		cal.set(13, 0);
		cal.set(14, 0);

		try {
			if (when == -1) {
				when = 365;
			}

			cal.add(6, -when);
			xmlCal = DatatypeFactory.newInstance(
			).newXMLGregorianCalendar(
				cal
			);
		}
		catch (DatatypeConfigurationException e) {
			CommonUtil.logger.error(
				(Object)("getXmlCalendarDateInThePast - " + e.getMessage()),
				(Throwable)e);
		}

		return xmlCal;
	}

	public static boolean isAmountValid(final String amount) {
		if (StringUtils.isNumeric(amount) && (amount.length() <= 10)) {
			return true;
		}

		return false;
	}

	public static boolean isBsbValid(String bsb) {
		bsb = bsb.replaceAll("\\.", "");

		if (StringUtils.isNumeric(bsb) && (bsb.length() == 6)) {
			return true;
		}

		return false;
	}

	public static void mapSpxTransaction(
		final AttributesInformation attr, final DEListBoxData deListBoxData) {

		final String transactionType = StringUtils.trimToEmpty(
			attr.getTransactionCode());

		if (Constants.SPX_CREDIT.contains(transactionType)) {
			attr.setTransactionCode(Constants.DE_CREDIT);
			attr.setTransactionCodeName(
				deListBoxData.getTransactionCodeMap(
				).get(
					Long.valueOf(Constants.DE_CREDIT)
				));
		}
		else if (Constants.SPX_DEBIT.equals(transactionType)) {
			attr.setTransactionCode(Constants.DE_DEBIT);
			attr.setTransactionCodeName(
				deListBoxData.getTransactionCodeMap(
				).get(
					Long.valueOf(Constants.DE_DEBIT)
				));
		}
	}

	public static DEListBoxData populateDEListBoxData(
		final DEProperties deProperties) {

		if (CommonUtil.deListBoxData == null) {
			(CommonUtil.deListBoxData = new DEListBoxData()).setRequestForMap(
				retrieveDEListBoxProperty(
					deProperties,
					Constants.PROPERTIES_PREFIX_DE_REQUEST_FOR_CODE,
					Constants.DESCRIPTION));
			CommonUtil.deListBoxData.setTransactionCodeMap(
				retrieveDEListBoxProperty(
					deProperties,
					Constants.PROPERTIES_PREFIX_DE_TRANSACTION_CODE,
					Constants.DESCRIPTION));
			CommonUtil.deListBoxData.setTimeframeMap(
				retrieveDEListBoxProperty(
					deProperties, Constants.PROPERTIES_PREFIX_DE_TIMEFRAME,
					Constants.DESCRIPTION));
			CommonUtil.deListBoxData.setGeneralYesNoMap(
				retrieveDEListBoxProperty(
					deProperties, Constants.PROPERTIES_PREFIX_DE_GENERAL_YES_NO,
					Constants.DESCRIPTION));
			CommonUtil.deListBoxData.setClaimReasonMap(
				retrieveDEListBoxProperty(
					deProperties, Constants.PROPERTIES_PREFIX_DE_CLAIM_REASON,
					Constants.DESCRIPTION));
			CommonUtil.deListBoxData.setClaimReplyMap(
				retrieveDEListBoxProperty(
					deProperties, Constants.PROPERTIES_PREFIX_DE_CLAIM_REPLY,
					Constants.DESCRIPTION));
			CommonUtil.deListBoxData.setIntendedAccountDetailsMap(
				retrieveDEListBoxProperty(
					deProperties,
					Constants.PROPERTIES_PREFIX_DE_INTENDED_ACCOUNT,
					Constants.DESCRIPTION));
			CommonUtil.deListBoxData.setOfiRecallResultMap(
				retrieveDEListBoxProperty(
					deProperties,
					Constants.PROPERTIES_PREFIX_OFI_DE_RECALL_RESULT,
					Constants.DESCRIPTION));
			CommonUtil.deListBoxData.setOfiMistakenResultMap(
				retrieveDEListBoxProperty(
					deProperties,
					Constants.PROPERTIES_PREFIX_OFI_DE_MISTAKEN_RESULT,
					Constants.DESCRIPTION));
			CommonUtil.deListBoxData.setOfiMistakenCheckboxResultMap(
				retrieveDEListBoxProperty(
					deProperties,
					Constants.PROPERTIES_PREFIX_OFI_DE_MISTAKEN_CHECKBOX_RESULT,
					Constants.DESCRIPTION));
			CommonUtil.deListBoxData.setOfiClaimResultMap(
				retrieveDEListBoxProperty(
					deProperties,
					Constants.PROPERTIES_PREFIX_OFI_DE_CLAIM_RESULT,
					Constants.DESCRIPTION));
		}

		return CommonUtil.deListBoxData;
	}

	public static String removeDotFromBSB(String bsb) {
		if (StringUtils.isNotBlank(bsb)) {
			bsb = bsb.replaceAll("\\.", "");
		}

		return bsb;
	}

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

		// put sorted list into map again
		// LinkedHashMap make sure order in which keys were inserted

		Map sortedMap = new LinkedHashMap();

		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	private static HashMap<Long, String> retrieveDEListBoxProperty(
		final DEProperties deProperties, final String prefix,
		final String suffix) {

		final HashMap<Long, String> nameMap = new HashMap<>();
		long i = 1L;

		while (true) {
			final String key = prefix.concat(
				Constants.DOT + i
			).concat(
				Constants.DOT
			).concat(
				suffix
			);
			final String name = deProperties.getDeProperty(
			).getProperty(
				key
			);

			if (StringUtils.isBlank(name)) {
				break;
			}

			nameMap.put(i, name);
			++i;
		}

		return nameMap;
	}

	private static Auditor audit;
	private static CardUtil cardUtil;
	private static DEListBoxData deListBoxData;
	private static Logger logger;

	static {
		CommonUtil.logger = Logger.getLogger((Class)CommonUtil.class);
		CommonUtil.audit = Auditor.getInstance();
		CommonUtil.cardUtil = CardUtil.getInstance();
	}

}