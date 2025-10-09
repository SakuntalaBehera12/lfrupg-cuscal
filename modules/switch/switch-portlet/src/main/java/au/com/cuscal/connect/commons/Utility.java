package au.com.cuscal.connect.commons;

import au.com.cuscal.connect.forms.ReportForm;

import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class Utility {

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	public static boolean anyOneOfToDateAndFromDateAreNOT18MonthPastDate(
		ReportForm reportForm) {

		boolean isDatesValid = true;
		Date date18MonthPast = get18MonthAgoDateFromTodayDate();
		Date toDate = formatDate(reportForm.getToDate(), DATE_FORMAT);
		Date fromDate = formatDate(reportForm.getFromDate(), DATE_FORMAT);

		if (fromDate.before(date18MonthPast) ||
			toDate.before(date18MonthPast)) {

			isDatesValid = false;
		}

		return isDatesValid;
	}

	public static boolean bothToDateAndFromDateAreNOT18MonthPastDate(
		ReportForm reportForm) {

		boolean isDatesValid = true;
		Date date18MonthPast = get18MonthAgoDateFromTodayDate();
		Date toDate = formatDate(reportForm.getToDate(), DATE_FORMAT);
		Date fromDate = formatDate(reportForm.getFromDate(), DATE_FORMAT);

		if (fromDate.before(date18MonthPast) &&
			toDate.before(date18MonthPast)) {

			isDatesValid = false;
		}

		return isDatesValid;
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
			List<UserGroup> userGrps = (List<UserGroup>)user.getUserGroups();

			for (UserGroup userGroup : userGrps) {
				rolesLst = new ArrayList<>();
				long groupId = userGroup.getGroup(
				).getGroupId();

				rolesLst = RoleLocalServiceUtil.getGroupRoles(groupId);

				for (Role role : rolesLst) {
					finalRolesLst.add(role);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return finalRolesLst;
	}

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

	public static Date get18MonthAgoDateFromTodayDate() {
		Date todayDate = Calendar.getInstance(
		).getTime();
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(todayDate);
		calendar.add(Calendar.MONTH, -18);
		Date newDate = calendar.getTime();

		return newDate;
	}

	public static String getYesterdayDate() {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		cal.add(Calendar.DATE, -1);

		return dateFormat.format(cal.getTime());
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

	public static float Round(float Rval, int Rpl) {
		float p = (float)Math.pow(10, Rpl);
		Rval = Rval * p;
		float tmp = Math.round(Rval);

		return (float)tmp / p;
	}

	/*public static String getFromDateIfPast18Months(ReportForm reportForm){
		String fromDateValid=null;
		Date date18MonthPast = get18MonthAgoDateFromTodayDate();
		Date fromDate = formatDate(reportForm.getFromDate());

		if(fromDate.before(date18MonthPast)){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			fromDateValid=sdf.format(date18MonthPast);
		}else{
			fromDateValid=reportForm.getFromDate();
		}

		return fromDateValid;
	}*/
}