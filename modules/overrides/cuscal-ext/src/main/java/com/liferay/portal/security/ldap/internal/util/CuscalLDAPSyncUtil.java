package com.liferay.portal.security.ldap.internal.util;

import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoValueModel;
import com.liferay.expando.kernel.service.ExpandoColumnLocalServiceUtil;
import com.liferay.expando.kernel.service.ExpandoTableLocalServiceUtil;
import com.liferay.expando.kernel.service.ExpandoValueLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;

public class CuscalLDAPSyncUtil {

	public static String getOrgCustomValue(
			final User user, final Organization organization, final String key)
		throws PortalException {

		final long orgClassNameId = ClassNameLocalServiceUtil.getClassNameId(
			Organization.class.getName());
		final long companyId = user.getCompanyId();
		final ExpandoTable orgTable =
			ExpandoTableLocalServiceUtil.getDefaultTable(
				companyId, orgClassNameId);
		final ExpandoColumn orgColumn = ExpandoColumnLocalServiceUtil.getColumn(
			orgTable.getTableId(), key);
		final long tableId = orgTable.getTableId();
		final long columnId = orgColumn.getColumnId();
		final long classPK = organization.getPrimaryKey();
		final ExpandoValueModel value = ExpandoValueLocalServiceUtil.getValue(
			tableId, columnId, classPK);

		if (Validator.isNull(value)) {
			_log.error(
				"Custom field " + key + " for user: " + user.getScreenName() +
					" or org : " + organization.getName() +
						" is null please fix it for LDAP export");

			return "";
		}

		return value.getData();
	}

	public static boolean hasEmptyOrganisations(User user)
		throws PortalException {

		if (ArrayUtil.isEmpty(user.getOrganizationIds())) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"CuscalUserListener Error ! : LDAP User %s can't have empty organization",
						user.getScreenName()));
			}

			return true;
		}

		return false;
	}

	public static boolean hasInvalidPassword(User user) {
		if (Validator.isNull(user.getPassword())) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"CuscalUserListener: Error ! : LDAP User %s can't have empty password",
						user.getScreenName()));
			}

			return true;
		}

		return false;
	}

	public static void setUserCustomAttribute(
			final User user, final String key, final String data)
		throws PortalException {

		final long userClassNameId = ClassNameLocalServiceUtil.getClassNameId(
			User.class.getName());
		final long companyId = user.getCompanyId();
		final ExpandoTable orgTable =
			ExpandoTableLocalServiceUtil.getDefaultTable(
				companyId, userClassNameId);
		final ExpandoColumn orgColumn = ExpandoColumnLocalServiceUtil.getColumn(
			orgTable.getTableId(), key);
		final long tableId = orgTable.getTableId();
		final long columnId = orgColumn.getColumnId();
		final long classPK = user.getPrimaryKey();
		ExpandoValueLocalServiceUtil.addValue(
			userClassNameId, tableId, columnId, classPK, data);

		if (_log.isDebugEnabled()) {
			_log.debug(
				"value : " + data + " has been added to custom variables : " +
					key + " for user : " + user);
		}
	}

	public static void updateCuscalBSB(User user, Organization organization)
		throws PortalException {

		String cuscalBSB = getOrgCustomValue(user, organization, "cuscalBSB");

		if (_log.isDebugEnabled()) {
			_log.debug(
				String.format(
					"CuscalUserListener: cuscalBSB for org is : %s",
					cuscalBSB));
		}

		if (Validator.isNotNull(cuscalBSB)) {
			setUserCustomAttribute(user, "cuscalBSB", cuscalBSB);
		}
	}

	public static void updateCuscalCUOrgId(User user, Organization organization)
		throws PortalException {

		String cuscalCUOrgId = getOrgCustomValue(
			user, organization, "cuscalCUOrgId");

		if (_log.isDebugEnabled()) {
			_log.debug(
				String.format(
					"CuscalUserListener: cuscalCUOrgId for org is : %s",
					cuscalCUOrgId));
		}

		if (Validator.isNotNull(cuscalCUOrgId)) {
			setUserCustomAttribute(user, "cuscalCUOrgId", cuscalCUOrgId);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CuscalLDAPSyncUtil.class);

}