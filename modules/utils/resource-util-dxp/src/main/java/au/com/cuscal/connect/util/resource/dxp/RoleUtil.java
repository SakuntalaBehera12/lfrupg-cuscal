package au.com.cuscal.connect.util.resource.dxp;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.NoSuchOrganizationException;
import com.liferay.portal.kernel.exception.NoSuchRoleException;
import com.liferay.portal.kernel.exception.NoSuchUserGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupLocalServiceUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to create and associate roles in
 * @author jluu
 *
 */
public class RoleUtil {

	/**
	 *
	 * @param role
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void createRole(
			au.com.cuscal.connect.util.resource.xml.Role role)
		throws PortalException {

		createRole(role.getRoleName(), role.getDescription(), role.getType());
	}

	/**
	 * @param roleName
	 * @param description
	 * @return Role
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static Role createRole(String roleName, String description)
		throws PortalException {

		try {
			Role role = RoleLocalServiceUtil.getRole(COMPANY_ID, roleName);

			if (logger.isInfoEnabled()) {
				logger.info(roleName + " already exists");
			}

			return role;
		}
		catch (NoSuchRoleException nsre) {
			return createRole(roleName, description, REGULAR_ROLE);
		}
	}

	/**
	 * Creates a new Role using the Liferay API or retrieves an existing Role
	 *
	 * @param roleName
	 * @param description
	 * @param type
	 * @return Role
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static Role createRole(String roleName, String description, int type)
		throws PortalException {

		if (logger.isInfoEnabled()) {
			logger.info("Adding New Role: " + roleName);
		}

		try {
			Role role = RoleLocalServiceUtil.getRole(COMPANY_ID, roleName);

			if (logger.isInfoEnabled()) {
				logger.info(roleName + " already exists");
			}

			return role;
		}
		catch (NoSuchRoleException nsre) {
			Map<Locale, String> descriptionMap = new HashMap<>();

			descriptionMap.put(
				LocaleThreadLocal.getDefaultLocale(), description);
			//long userId, long companyId, String name, Map<Locale, String> titleMap, String description, int type-normal = 1 , community -2, organisation 3
			//			Role role = RoleLocalServiceUtil.addRole(ADMIN_USER, COMPANY_ID, roleName, null, description, type);

			Role role = RoleLocalServiceUtil.addRole(
				StringPool.BLANK, ADMIN_USER, null, 0, roleName, null, descriptionMap, type, null,
				null);

			if (logger.isInfoEnabled()) {
				logger.info(
					"New Role " + roleName + " with Id Added:" +
						role.getRoleId());
			}

			return role;
		}
	}

	// The choice of which role to use should come from configuration or import data.

	/**
	 * Assigns a role to an organisation if there is no existing relationship
	 *
	 * @param roleName
	 * @param organisationName
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void linkOrganisationToRole(
			String roleName, String organisationName)
		throws PortalException {

		if (logger.isInfoEnabled()) {
			logger.info(
				"Link Role " + roleName + " to Organisation" +
					organisationName);
		}

		try {
			Role role = RoleLocalServiceUtil.getRole(COMPANY_ID, roleName);
			long groupId = OrganizationLocalServiceUtil.getOrganization(
				COMPANY_ID, organisationName
			).getGroup(
			).getGroupId();

			if (!GroupLocalServiceUtil.hasRoleGroup(
					role.getRoleId(), groupId)) {

				GroupLocalServiceUtil.addRoleGroups(
					role.getRoleId(), new long[] {groupId});

				if (logger.isInfoEnabled()) {
					logger.info(
						"Successfully linked Role " + roleName +
							" with Organisation" + organisationName);
				}
			}
			else {
				if (logger.isInfoEnabled()) {
					logger.info(
						organisationName + " already linked to " + roleName);
				}
			}
		}
		catch (NoSuchOrganizationException nsoe) {
			if (logger.isErrorEnabled()) {
				logger.error(organisationName + "does not exist");
				logger.error(nsoe.getMessage());
			}
		}
		catch (NoSuchRoleException nsre) {
			if (logger.isErrorEnabled()) {
				logger.error(roleName + "does not exist");
				logger.error(nsre.getMessage());
			}
		}
	}

	//	private static int ORGANISATION_ROLE = 2;
	//	private static int COMMUNITY_ROLE = 3;
	/**
	 * Assigns a role to an user group if there is no existing relationship
	 *
	 * @param roleName
	 * @param userGroupName
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void linkUserGroupToRole(
			String roleName, String userGroupName)
		throws PortalException {

		if (logger.isInfoEnabled()) {
			logger.info(
				"Link Role " + roleName + " to UserGroup " + userGroupName);
		}

		try {
			Role role = RoleLocalServiceUtil.getRole(COMPANY_ID, roleName);
			long groupId = UserGroupLocalServiceUtil.getUserGroup(
				COMPANY_ID, userGroupName
			).getGroup(
			).getGroupId();

			if (!GroupLocalServiceUtil.hasRoleGroup(
					role.getRoleId(), groupId)) {

				GroupLocalServiceUtil.addRoleGroups(
					role.getRoleId(), new long[] {groupId});

				if (logger.isInfoEnabled()) {
					logger.info(
						"Successfully linked Role " + roleName +
							" with UserGroup " + userGroupName);
				}
			}
			else {
				if (logger.isInfoEnabled()) {
					logger.info(
						userGroupName + " already linked to " + roleName);
				}
			}
		}
		catch (NoSuchUserGroupException nsuge) {
			if (logger.isErrorEnabled()) {
				logger.error(userGroupName + "does not exist");
				logger.error(nsuge.getMessage());
			}
		}
		catch (NoSuchRoleException nsre) {
			if (logger.isErrorEnabled()) {
				logger.error(roleName + "does not exist");
				logger.error(nsre.getMessage());
			}
		}
	}

	/**
	 * Removes a role from an Organisation if there is an existing relationship
	 *
	 * @param roleName
	 * @param organisationName
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void unlinkOrganisationToRole(
			String roleName, String organisationName)
		throws PortalException {

		if (logger.isInfoEnabled()) {
			logger.info(
				"Unlink Role " + roleName + " to Organisation" +
					organisationName);
		}

		try {
			Role role = RoleLocalServiceUtil.getRole(COMPANY_ID, roleName);
			long groupId = OrganizationLocalServiceUtil.getOrganization(
				COMPANY_ID, organisationName
			).getGroup(
			).getGroupId();

			if (GroupLocalServiceUtil.hasRoleGroup(role.getRoleId(), groupId)) {
				GroupLocalServiceUtil.unsetRoleGroups(
					role.getRoleId(), new long[] {groupId});

				if (logger.isInfoEnabled()) {
					logger.info(
						"Successfully unlinked Role " + roleName +
							" to Organisation" + organisationName);
				}
			}
			else {
				if (logger.isInfoEnabled()) {
					logger.info(
						organisationName + " is not linked to " + roleName);
				}
			}
		}
		catch (NoSuchOrganizationException nsoe) {
			if (logger.isErrorEnabled()) {
				logger.error(organisationName + "does not exist");
				logger.error(nsoe.getMessage());
			}
		}
		catch (NoSuchRoleException nsre) {
			if (logger.isErrorEnabled()) {
				logger.error(roleName + "does not exist");
				logger.error(nsre.getMessage());
			}
		}
	}

	/**
	 * Removes a role from an user group if there is an existing relationship
	 *
	 * @param roleName
	 * @param userGroupName
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void unlinkUserGroupToRole(
			String roleName, String userGroupName)
		throws PortalException {

		if (logger.isInfoEnabled()) {
			logger.info(
				"Unlink Role " + roleName + " to UserGroup " + userGroupName);
		}

		try {
			Role role = RoleLocalServiceUtil.getRole(COMPANY_ID, roleName);
			long groupId = UserGroupLocalServiceUtil.getUserGroup(
				COMPANY_ID, userGroupName
			).getGroup(
			).getGroupId();

			if (GroupLocalServiceUtil.hasRoleGroup(role.getRoleId(), groupId)) {
				GroupLocalServiceUtil.unsetRoleGroups(
					role.getRoleId(), new long[] {groupId});

				if (logger.isInfoEnabled()) {
					logger.info(
						"Successfully unlinked Role " + roleName +
							" with UserGroup " + userGroupName);
				}
			}
			else {
				if (logger.isInfoEnabled()) {
					logger.info(
						userGroupName + " is not linked to " + roleName);
				}
			}
		}
		catch (NoSuchUserGroupException nsuge) {
			if (logger.isErrorEnabled()) {
				logger.error(userGroupName + "does not exist");
				logger.error(nsuge.getMessage());
			}
		}
		catch (NoSuchRoleException nsre) {
			if (logger.isErrorEnabled()) {
				logger.error(roleName + "does not exist");
				logger.error(nsre.getMessage());
			}
		}
	}

	private static long ADMIN_USER = CommonUtil.retrieveAdminUser();

	private static long COMPANY_ID = CommonUtil.retrieveCompanyId();

	private static int REGULAR_ROLE = 1;

	private static Logger logger = LoggerFactory.getLogger(RoleUtil.class);

}