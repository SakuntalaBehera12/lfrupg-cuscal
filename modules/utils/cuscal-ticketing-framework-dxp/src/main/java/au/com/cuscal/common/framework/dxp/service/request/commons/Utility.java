package au.com.cuscal.common.framework.dxp.service.request.commons;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

/**
 * Utility class for Transaction Search View
 *
 * @author Rajni Bharara
 *
 */
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;

public class Utility {

	public static DecimalFormat AmountFormat = new DecimalFormat(
		"###,###,###,###,##0.00");

	/**
	 * Check if number
	 *
	 * @param String
	 * @return boolean
	 */
	public static boolean checkIfNumber(String in) {
		try {
			Integer.parseInt(in);
		}
		catch (NumberFormatException ex) {
			return false;
		}

		return true;
	}

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
	 * convert from cents value to dollar value
	 *
	 * @param float
	 * @return String
	 */
	public static float covertCentsToDollarValue(float centValue) {
		float dollarValue = 0f;
		dollarValue = centValue / 100;

		return dollarValue;
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	public static BigDecimal createBigDecimalAmountFromString(String value) {
		BigDecimal amount = null;

		value = removeAndCovertInFloat(value);

		if (StringUtils.isNotBlank(value)) {
			amount = new BigDecimal(value);
		}

		return amount;
	}

	/**
	 * Get the Expiry Date Format (must be YYMM for transaction detail page
	 *
	 * @param String
	 * @return String
	 */
	public static String createExpiryDateFormat(String expiryDate) {
		String completeDate = "";

		if (4 == expiryDate.length()) {
			HashMap<String, String> monthMap = getMonthMap();
			String year = "20" + expiryDate.substring(0, 2);
			String month = monthMap.get(
				expiryDate.substring(2, expiryDate.length()));

			if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month))
				completeDate = month + " " + year;
		}

		return completeDate;
	}

	/**
	 * Check for the existence of Stan and Advise Stan and creates a way of displaying the result
	 *
	 * @param TransactionBasicInformation
	 * @return String
	 */
	public static String createStanDisplayString(
		String stan, String adviceStan) {

		StringBuffer stanToDisplay = new StringBuffer("");

		if (StringUtils.isNotEmpty(stan) ||
			StringUtils.isNotEmpty(adviceStan)) {

			if (StringUtils.isNotEmpty(stan) &&
				StringUtils.isNotEmpty(adviceStan)) {

				stanToDisplay.append(stan);
				stanToDisplay.append("/");
				stanToDisplay.append(adviceStan);
			}
			else if (StringUtils.isNotEmpty(stan)) {
				stanToDisplay.append(stan);
			}
			else if (StringUtils.isNotEmpty(adviceStan)) {
				stanToDisplay.append(adviceStan);
			}
		}

		return stanToDisplay.toString();
	}

	/**
	 *
	 * @param value
	 * @param format
	 * @return
	 */
	public static XMLGregorianCalendar createXmlGregorianCalendarFromString(
		String value, String format, Boolean hasTimePart) {

		logger.debug(
			"createXmlGregorianCalendarFromString - creating date from: " +
				value);

		String[] parts = value.split(" ");

		GregorianCalendar cal = createCalendarFromString(value, format);
		XMLGregorianCalendar xmlCal = null;

		try {
			if (hasTimePart) {
				if (parts[1].contains(":")) {
					String[] times = parts[1].split(":");
					cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(times[0]));
					cal.set(Calendar.MINUTE, Integer.valueOf(times[1]));
				}
			}

			xmlCal = DatatypeFactory.newInstance(
			).newXMLGregorianCalendar(
				cal
			);
		}
		catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}

		return xmlCal;
	}

	public static XMLGregorianCalendar createXmlGregorianCalendarFromString(
		String value, String regex, String format, Boolean hasTimePart) {

		logger.debug(
			"createXmlGregorianCalendarFromString - creating date from: " +
				value);

		String[] parts = value.split(regex);

		GregorianCalendar cal = createCalendarFromString(value, format);
		XMLGregorianCalendar xmlCal = null;

		try {
			if (hasTimePart) {
				if (parts[1].contains(":")) {
					String[] times = parts[1].split(":");
					cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(times[0]));
					cal.set(Calendar.MINUTE, Integer.valueOf(times[1]));
				}
			}

			xmlCal = DatatypeFactory.newInstance(
			).newXMLGregorianCalendar(
				cal
			);
		}
		catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}

		return xmlCal;
	}

	/**
	 * Find the DefaultFractionDigits for currency specified
	 *
	 * @param String
	 * @return int
	 */
	public static int findDefaultFractionDigits(String currencyCode) {
		Currency currency = Currency.getInstance(currencyCode);

		return currency.getDefaultFractionDigits();
	}

	/**
	 * Find Regular Roles assigned to an organisation
	 * (Not to be confused with organisation roles)
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
	 * format Amount for currency specified
	 *
	 * @param String
	 * @param Double
	 * @return String
	 */
	public static String formatAmountByCurrencyCode(
		String currencyCode, Double amount) {

		Currency currency = Currency.getInstance(currencyCode);
		DecimalFormat decimalFormat = new DecimalFormat();

		decimalFormat.setCurrency(currency);
		decimalFormat.setMaximumFractionDigits(
			currency.getDefaultFractionDigits());
		decimalFormat.setMinimumFractionDigits(
			currency.getDefaultFractionDigits());

		return decimalFormat.format(amount);
	}

	/**
	 * Format string date in formatted date object
	 *
	 * @param String
	 * @param String
	 * @return Date
	 */
	public static Date formatDate(String ddMMyyyy, String format) {
		Date date = null;

		try {
			if (!StringUtils.isBlank(ddMMyyyy)) {
				SimpleDateFormat sdf = new SimpleDateFormat(format);

				date = sdf.parse(ddMMyyyy);
			}
		}
		catch (Exception pe) {
			pe.printStackTrace();
		}

		return date;
	}

	/**
	 * Format date in formatted date object
	 *
	 * @param Date
	 * @param String
	 * @return String
	 */
	public static String formatDateToString(Date date, String format) {
		String dateStr = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			dateStr = sdf.format(date);
		}
		catch (Exception pe) {
			pe.printStackTrace();
		}

		return dateStr;
	}

	/**
	 * format number value to string object
	 *
	 * @param String
	 * @param float
	 * @return String
	 */
	public static String formatNumberToString(float number, String format) {
		String floatStr = null;

		try {
			NumberFormat formatter = new DecimalFormat(format);

			floatStr = formatter.format(number);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return floatStr;
	}

	/**
	 * Get the 18 month old date from current date
	 *
	 * @return Date
	 */
	public static Date get18MonthAgoDateFromTodayDate() {
		Date todayDate = Calendar.getInstance(
		).getTime();
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(todayDate);
		calendar.add(Calendar.MONTH, -18);
		Date newDate = calendar.getTime();

		return newDate;
	}

	public static List<Role> getAllUserRoles(User user) {
		List<Role> allUserRoles = new ArrayList<>();

		if (null != user) {
			Long userId = user.getUserId();
			List<Role> userGroupRoles = findUserGroupRoleList(user);
			List<Role> organisationRoles =
				findRegularRolesAssignedToUsersOrganisationList(user);
			List<Role> roles = new ArrayList<>();

			try {
				roles = RoleLocalServiceUtil.getUserRoles(userId);
			}
			catch (SystemException e) {
				e.printStackTrace();
			}

			allUserRoles.addAll(userGroupRoles);
			allUserRoles.addAll(organisationRoles);
			allUserRoles.addAll(roles);
		}

		return allUserRoles;
	}

	/**
	 * Get the max date diff (specify in property file) days back date from end date
	 * @param Date
	 * @param int
	 * @return Date
	 */
	public static Date getDaysBackDateFromEndDate(Date endDate, int days) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(endDate);
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		Date newDate = calendar.getTime();

		return newDate;
	}

	/**
	 * create the map for months name
	 *
	 * @return HashMap<String, String>
	 */
	public static HashMap<String, String> getMonthMap() {
		HashMap<String, String> monthMap = new HashMap<>();

		monthMap.put("01", "January");
		monthMap.put("02", "February");
		monthMap.put("03", "March");
		monthMap.put("04", "April");
		monthMap.put("05", "May");
		monthMap.put("06", "June");
		monthMap.put("07", "July");
		monthMap.put("08", "August");
		monthMap.put("09", "September");
		monthMap.put("10", "October");
		monthMap.put("11", "November");
		monthMap.put("12", "December");

		return monthMap;
	}

	/**
	 * Get the unmasked part of Pan number
	 *
	 * @param String
	 * @return BigInteger
	 */
	public static BigInteger getUnmaskedPartOfPanNumber(String maskedPan) {
		BigInteger unmarkedPanOrBin = null;

		if (maskedPan.indexOf("*") != -1) {
			int start = maskedPan.indexOf("*");
			int end = maskedPan.lastIndexOf("*");
			String maskedPartPan =
				maskedPan.substring(0, start) +
					maskedPan.substring(end + 1, maskedPan.length());

			unmarkedPanOrBin = new BigInteger(maskedPartPan);
		}
		else {
			unmarkedPanOrBin = new BigInteger(maskedPan);
		}

		return unmarkedPanOrBin;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public static User getUserFromRequest(PortletRequest request) {
		User user = null;

		try {
			user = PortalUtil.getUser(request);
		}
		catch (PortalException e) {
			e.printStackTrace();
		}
		catch (SystemException e) {
			e.printStackTrace();
		}

		return user;
	}

	/**
	 * populate the list of ids in string object
	 *
	 * @param Set
	 *            <Long>
	 * @return String
	 */
	public static String makeIdString(Set<Long> ids) {
		StringBuilder stringBuilder = new StringBuilder();
		int count = 0;

		for (Long id : ids) {
			if ((ids.size() - 1) == count) {
				stringBuilder.append(id);
			}
			else {
				stringBuilder.append(id + ",");
			}

			count++;
		}

		return stringBuilder.toString();
	}

	/**
	 * populate the list of role in string object
	 *
	 * @param List
	 *            <Role>
	 * @return List<String>
	 */
	public static List<String> makeRolesNameString(List<Role> list) {
		List<String> arrayList = new ArrayList<>();

		for (Role role : list) {
			arrayList.add(role.getName());
		}

		return arrayList;
	}

	/**
	 * Mask the pan
	 *
	 * @param String
	 * @param char
	 * @return String
	 */
	public static String mask(String pan, char maskChar) {
		String maskedPan;

		if (pan.length() < 10) {

			// sanity check

			return pan;
		}

		maskedPan = pan.substring(0, 6);

		int panLength = pan.length();
		int maskedCharCount = panLength - 10;

		for (int i = 0; i < maskedCharCount; i++) {
			maskedPan += maskChar;
		}

		maskedPan += pan.substring(panLength - 4, panLength);

		return maskedPan;
	}

	/**
	 * Remove all spaces from string object
	 *
	 * @param String
	 * @return String
	 */
	public static String removeAllSpacesFromPanorBin(String panBin) {
		StringBuilder stringBuilder = new StringBuilder(panBin);

		for (int i = 0; panBin.length() > i; i++) {
			if (stringBuilder.indexOf(" ") != -1) {
				stringBuilder.deleteCharAt(stringBuilder.indexOf(" "));
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * Remove and convert the float
	 *
	 * @param String
	 * @return String
	 */
	public static String removeAndCovertInFloat(String amountVal) {
		StringBuilder builder = new StringBuilder(amountVal);

		for (int i = 0; amountVal.length() > i; i++) {
			if (builder.indexOf("$") != -1) {
				builder.deleteCharAt(builder.indexOf("$"));
			}

			if (builder.indexOf(",") != -1) {
				builder.deleteCharAt(builder.indexOf(","));
			}
		}

		return builder.toString();
	}

	/**
	 * Round the float value
	 *
	 * @param int
	 * @param float
	 * @return float
	 */
	public static float Round(float Rval, int Rpl) {
		float p = (float)Math.pow(10, Rpl);
		Rval = Rval * p;
		float tmp = Math.round(Rval);

		return tmp / p;
	}

	/**
	 * Validate date
	 *
	 * @return boolean
	 */
	public static boolean validateDate(String ddMMyyyy, String format) {
		boolean isValidDate = true;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;

		try {
			if (!StringUtils.isBlank(ddMMyyyy)) {
				date = sdf.parse(ddMMyyyy);

				if (!sdf.format(
						date
					).equals(
						ddMMyyyy
					)) {

					return false;
				}
			}
		}
		catch (Exception pe) {

			// pe.printStackTrace();

			isValidDate = false;

			return false;
		}

		return isValidDate;
	}

	/**
	 * Validate the pattern matcher
	 *
	 * @param String
	 * @param String
	 * @return boolean
	 */
	public static boolean validatePatternMatcher(
		String patternStr, String toMatch) {

		boolean isMatched = false;
		Pattern pattern = Pattern.compile(patternStr);

		Matcher matcher = pattern.matcher(toMatch);

		if (matcher.matches()) {
			isMatched = true;
		}
		else {
			isMatched = false;
		}

		return isMatched;
	}

	//

	// 	/**
	// 	 *
	// 	 * @param ticketsList
	// 	 * @return
	// 	 * @throws JSONException
	// 	 */

	//	public static JSONObject getJsonObjectFromTicketObject(

	// 			List<TicketDetails> ticketsList, PortletURL url) throws JSONException {

	//		logger.debug("getJsonObjectFromTicketObject - start");
	//		JSONObject finalJsonObject = new JSONObject();
	//		JSONArray jsonArray = new JSONArray();
	//
	//		for (TicketDetails ticket : ticketsList) {
	//			JSONObject ticketJson = new JSONObject();

	//			ticketJson.put("ticketNumber", ticket.getTicketNumber());
	//			ticketJson.put("ticketCategory", ticket.getTicketCategory());
	//			ticketJson.put("ticketStatus", ticket.getTicketStatus());
	//			ticketJson.put("ticketFirstName", ticket.getTicketFirstName());
	//			ticketJson.put("ticketLastName", ticket.getTicketLastName());
	//			ticketJson.put("ticketSubmittedDate",
	//					ticket.getTicketSubmittedDate());
	//			logger.debug("Service request formatted Created date is  " + ticket.getTicketSubmittedDate());
	//			ticketJson.put("ticketUpdateDate", ticket.getTicketUpdateDate());
	//			logger.debug("Service request formatted updated date is  " + ticket.getTicketUpdateDate());
	//
	//			ticketJson.put("ticketDetailsLink", createLinkToTicketDetails(url, ticket.getTicketId()));
	//			jsonArray.put(ticketJson);
	//		}
	//
	//		finalJsonObject.put("tickets", jsonArray);
	//		logger.debug("getJsonObjectFromTicketObject - finalJsonObject: " + finalJsonObject);
	//		logger.debug("getJsonObjectFromTicketObject - end");
	//		return finalJsonObject;
	//	}

	/**
	 *
	 * @param value
	 * @param format
	 * @return
	 */
	private static GregorianCalendar createCalendarFromString(
		String value, String format) {

		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		try {
			Date date = formatter.parse(value);

			cal.setTime(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		return cal;
	}

	/**
	 * Modify the link to the Ticket Details Portlet with the currect page so that
	 * eg.
	 * https://www.cuscal.com.au/group/eft-connect/transactions?p_p_id=cus...tlet_render=ticketDetails
	 * becomes:
	 * /ticketing?p_p_id=cuscaltic.....der=ticketDetails&_cuscalticketingportlet_WAR_cuscalticketingportlet_i=155454
	 *
	 * @param searchHeader
	 * @return
	 */
	private String createLinkToTicketDetails(
		PortletURL redirectUrl, long liferayTicketId) {

		redirectUrl.setParameter("i", String.valueOf(liferayTicketId));
		String detailsLink = StringUtils.substringAfter(
			redirectUrl.toString(), "?");

		return detailsLink;
	}

	private static Logger logger = Logger.getLogger(Utility.class);

}