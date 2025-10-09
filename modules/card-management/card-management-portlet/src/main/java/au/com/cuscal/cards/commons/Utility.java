package au.com.cuscal.cards.commons;

import au.com.cuscal.cards.domain.CardData;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.text.SimpleDateFormat;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for Card Search View
 *
 * @author Rajni Bharara
 *
 */
import au.com.cuscal.framework.webservices.transaction.GetCardRequestType;
import au.com.cuscal.framework.webservices.transaction.GetCardVigilBlockedRequestType;

public class Utility {

	/**
	 * Return all user User group for card status update .
	 *
	 * @param request
	 * @return List<Role>
	 * @throws PortalException, SystemException
	 */
	public static List<String> allUserGroupRoles(PortletRequest request)
		throws PortalException {

		User user = PortalUtil.getUser(request);
		List<UserGroup> allUserGrp = null;
		List<String> grpNames = null;

		if (user != null) {
			logger.debug("The user group for user  - " + user.getUserId());
			allUserGrp = new ArrayList<>();
			grpNames = new ArrayList<>();
			allUserGrp = (List<UserGroup>)user.getUserGroups();
			logger.debug(
				"The user group for user   allUserGrp size is  " +
					allUserGrp.size());

			for (UserGroup userGrp : allUserGrp) {
				logger.debug(
					"The user group for user  group name is  " +
						userGrp.getName());
				grpNames.add(userGrp.getName());
			}
		}

		return grpNames;
	}

	/**
	 * Return all user Roles ( direct and inherited )
	 *
	 * @param request
	 * @return List<Role>
	 */
	public static List<Role> allUserRoles(PortletRequest request)
		throws PortalException {

		User user = PortalUtil.getUser(request);
		List<Role> allRoles = new ArrayList<>();

		if (user != null) {
			Long userId = user.getUserId();

			List<Role> userRoles = RoleLocalServiceUtil.getUserRoles(userId);
			List<Role> groupRoles = findUserGroupRoleList(user);

			allRoles.addAll(userRoles);
			allRoles.addAll(groupRoles);
		}

		return allRoles;
	}

	public static GetCardVigilBlockedRequestType convertToVigilBlockedRequest(
		GetCardRequestType getCardRequestType) {

		GetCardVigilBlockedRequestType blockedRequestType =
			new GetCardVigilBlockedRequestType();

		blockedRequestType.setHeader(getCardRequestType.getHeader());

		return blockedRequestType;
	}

	public static List<Role> findUserGroupRoleList(User user) {
		List<Role> finalRolesList = new ArrayList<>();
		List<Role> rolesList = null;

		try {
			List<UserGroup> userGroups = (List<UserGroup>)user.getUserGroups();

			for (UserGroup userGroup : userGroups) {
				rolesList = new ArrayList<>();
				long groupId = userGroup.getGroup(
				).getGroupId();

				rolesList = RoleLocalServiceUtil.getGroupRoles(groupId);

				for (Role role : rolesList) {
					finalRolesList.add(role);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return finalRolesList;
	}

	public static Date formatDate(String ddMMyyyy, String format) {
		Date date = null;

		try {
			//if (!StringUtils.isBlank(ddMMyyyy)) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			date = sdf.parse(ddMMyyyy);
			//}
		}
		catch (Exception pe) {
			pe.printStackTrace();
		}

		return date;
	}

	/**
	 * Convert the card expiry date from yyyyMM to MM/yyyy.
	 *
	 * @param expiryDate
	 * @return
	 */
	public static String formatExpiryDate(String expiryDate) {
		String formattedExpiryDate = "";

		String tempYear = expiryDate.substring(0, 4);
		String tempMonth = expiryDate.substring(4, expiryDate.length());

		formattedExpiryDate = tempMonth + Constants.SLASH + tempYear;

		return formattedExpiryDate;
	}

	/**
	 * Convert the card expiry date from yyyy-MM-dd to MM/yyyy.
	 *
	 * @param expiryDate
	 * @return
	 */
	public static String formatExpiryDatev2(String expiryDate) {
		String inFormat = "yyyy-MM-dd";
		String outFormat = "MM/yyyy";

		try {
			Date date = formatDate(expiryDate, inFormat);
			SimpleDateFormat formatter = new SimpleDateFormat(outFormat);

			expiryDate = formatter.format(date);
		}
		catch (Exception pe) {
			return expiryDate;
		}

		return expiryDate;
	}

	public static Date get18MonthAgoDateFromTodayDate() {
		Date todayDate = Calendar.getInstance(
		).getTime();
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(todayDate);
		calendar.add(Calendar.MONTH, -18);
		Date newDate = calendar.getTime();

		return newDate;
	}

	/**
	 * Convert the card expiry date from yyyy-MM-dd to MM/yyyy.
	 *
	 * @param cuscalToken
	 * @return Cards object
	 */
	public static CardData getCardObjectByToken(
		String cuscalToken, PortletRequest request) {

		try {
			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpSession session = httpServletRequest.getSession();

			List<CardData> cardsList =
				(ArrayList<CardData>)session.getAttribute(Constants.CARD_LIST);

			if (null != cardsList) {
				for (CardData obj : cardsList) {
					if (obj.getCuscalToken(
						).equals(
							cuscalToken
						)) {

						return obj;
					}
				}
			}

			return null;
		}
		catch (Exception ex) {
			logger.info(
				"[getCardObjectByToken] the getCardObjectByToken  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);

			return null;
		}
	}

	/**
	 * return true if the currently logged in user has the specified rolename
	 * @param request
	 * @param roleName
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	public static boolean hasRole(PortletRequest request, String roleName) {
		User user = null;

		try {
			roleName = roleName.trim();
			user = PortalUtil.getUser(request);

			boolean result = false;

			try {
				result = RoleServiceUtil.hasUserRole(
					user.getUserId(), user.getCompanyId(), roleName, true);
			}
			catch (Exception e) {

				// is already set to false
				// result = false;

			}

			if (result) {
				logger.debug(
					"hasRole - " + user.getScreenName() + " has role " +
						roleName);

				return result;
			}

			logger.debug(
				"hasRole - RoleServiceUtil.hasUserRole return false... attempting manual lookups");

			List<Role> roles = allUserRoles(request);

			// check roles that are inherited from the organisation

			/*
			List<Organization> organisations = user.getOrganizations();

			for (Organization organisation : organisations) {
				long companyId = organisation.getCompanyId();
				List<Role> _roles = RoleLocalServiceUtil.getRoles(companyId);

				roles.addAll(_roles);
			}
			*/

			if (logger.isDebugEnabled()) {
				for (Role role : roles) {
					String _roleName = role.getName(
					).trim();

					logger.debug(
						"hasRole - " + user.getScreenName() + " has role " +
							_roleName);
				}
			}

			for (Role role : roles) {
				String _roleName = role.getName(
				).trim();

				if (_roleName.equals(roleName)) {
					logger.debug(
						"hasRole - " + user.getScreenName() + " has role " +
							_roleName + "... returning true");

					return true;
				}
			}
		}
		catch (Exception e) {
			logger.warn(
				"hasRole(PortletRequest request, '" + roleName +
					"') - exception thrown while obtaining role information: " +
						e.getMessage(),
				e);
		}

		logger.debug(
			"hasRole - " +
				(user != null ? user.getScreenName() : "[null user]") +
					" does not have role " + roleName);

		return false;
	}

	/**
	 * Find out whether login user is CSU Admin
	 * @param PortletRequest
	 * @return boolean
	 * @throws PortalException, SystemException
	 */
	public static boolean isUserBelongToCsuAdminUserGroup(
			PortletRequest request, String CsuAdmin)
		throws PortalException {

		boolean isCsuAdmin = false;

		List<String> userGrpsList = allUserGroupRoles(request);

		for (String userGrpName : userGrpsList) {
			if (userGrpName.equalsIgnoreCase(CsuAdmin)) {
				isCsuAdmin = true;
			}
		}

		return isCsuAdmin;
	}

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

	public static List<String> makeRolesNameString(List<Role> list) {
		List<String> arrayList = new ArrayList<>();

		for (Role role : list) {
			arrayList.add(role.getName());
		}

		return arrayList;
	}

	/**
	 * Remove all the spaces from the PAN entered by the user.
	 * @param pan <em>String</em>
	 * @return String
	 */
	public static String removeAllSpacesFromPan(String pan) {
		StringBuilder stringBuilder = new StringBuilder(pan);

		for (int i = 0; pan.length() > i; i++) {
			if (stringBuilder.indexOf(" ") != -1) {
				stringBuilder.deleteCharAt(stringBuilder.indexOf(" "));
			}
		}

		return stringBuilder.toString();
	}

	public static float Round(float Rval, int Rpl) {
		float p = (float)Math.pow(10, Rpl);
		Rval = Rval * p;
		float tmp = Math.round(Rval);

		return (float)tmp / p;
	}

	public static boolean validateDate(String ddMMyyyy, String format) {
		boolean isValidDate = true;

		try {
			if (!StringUtils.isBlank(ddMMyyyy)) {
				SimpleDateFormat sdf = new SimpleDateFormat(format);

				sdf.parse(ddMMyyyy);
			}
		}
		catch (Exception pe) {
			isValidDate = false;

			return false;
		}

		return isValidDate;
	}

	/**
	 * Validate the pattern matcher
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

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(Utility.class);

}