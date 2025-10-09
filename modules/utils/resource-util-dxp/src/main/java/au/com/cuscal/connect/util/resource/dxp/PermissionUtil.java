package au.com.cuscal.connect.util.resource.dxp;

import au.com.cuscal.connect.util.resource.xml.Permission;

import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.portal.kernel.exception.NoSuchResourceActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to allow access to add, remove and manage Liferay permissions on roles
 * @author jluu
 *
 */
public class PermissionUtil {

	/**
	 * Adds permissions to Custom Fields already setup
	 *
	 * @param roleName
	 * @param actionId
	 * @param customFieldName
	 * @param customClassName
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void addCustomFieldPermissionToRole(
			String roleName, String actionId, String customFieldName,
			String customClassName)
		throws PortalException {

		Role role = RoleLocalServiceUtil.getRole(COMPANY_ID, roleName);

		long roleId = role.getRoleId();

		String attributeClassName = CustomFieldUtil.getFullClassName(
			customClassName);

		ExpandoColumn customField = CustomFieldUtil.retrieveAttribute(
			attributeClassName, customFieldName);

		String primaryKey = String.valueOf(customField.getPrimaryKey());

		setPermission(
			roleName, CUSTOM_FIELD_RESOURCE_NAME, actionId, roleId,
			ResourceConstants.SCOPE_INDIVIDUAL, primaryKey);
	}

	/**
	 * Adds a customField Permission other permission to a specific Role
	 *
	 * @param roleName
	 * @param permission
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void addPermissionToRole(
			String roleName, Permission permission)
		throws PortalException {

		if ((permission.getCustomFieldName() != null) &&
			(permission.getCustomFieldClass() != null)) {

			addCustomFieldPermissionToRole(
				roleName, permission.getActionId(),
				permission.getCustomFieldName(),
				permission.getCustomFieldClass());
		}
		else {
			addPermissionToRole(
				roleName, permission.getTargetResource(),
				permission.getActionId());
		}
	}

	/**
	 * Adds a permission to a role using Liferay API. Scope and Primary Key are different for portal roles and regular
	 *
	 * @param roleName
	 * @param resourceClassName
	 * @param actionId
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void addPermissionToRole(
			String roleName, String resourceClassName, String actionId)
		throws PortalException {

		Role role = RoleLocalServiceUtil.getRole(COMPANY_ID, roleName);

		long roleId = role.getRoleId();
		int scope = ResourceConstants.SCOPE_COMPANY;
		String primKey = String.valueOf(COMPANY_ID);

		if (role.getType() == RoleConstants.TYPE_REGULAR) {
			scope = ResourceConstants.SCOPE_COMPANY;
		}
		else if ((role.getType() == RoleConstants.TYPE_SITE) ||
				 (role.getType() == RoleConstants.TYPE_ORGANIZATION)) {

			// Liferay specific implementation for non portal roles

			scope = ResourceConstants.SCOPE_GROUP_TEMPLATE;
			primKey = "0";
		}

		addPermission(
			roleName, resourceClassName, actionId, roleId, scope, primKey);
	}

	/**
	 * Access the Liferay API to remove a permission from a role
	 *
	 * @param roleName
	 * @param resourceClassName
	 * @param actionId
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void removePermissionFromRole(
			String roleName, String resourceClassName, String actionId)
		throws PortalException {

		Role role = RoleLocalServiceUtil.getRole(COMPANY_ID, roleName);

		long roleId = role.getRoleId();
		int scope = ResourceConstants.SCOPE_COMPANY;
		String primKey = String.valueOf(COMPANY_ID);

		if (role.getType() == RoleConstants.TYPE_REGULAR) {
			scope = ResourceConstants.SCOPE_COMPANY;
		}
		else if ((role.getType() == RoleConstants.TYPE_SITE) ||
				 (role.getType() == RoleConstants.TYPE_ORGANIZATION)) {

			scope = ResourceConstants.SCOPE_GROUP_TEMPLATE;
			primKey = "0";
		}

		try {
			ResourcePermissionLocalServiceUtil.removeResourcePermission(
				COMPANY_ID, resourceClassName, scope, primKey, roleId,
				actionId);

			if (logger.isInfoEnabled()) {
				logger.info(
					"Removed Permission [" + actionId + "," +
						resourceClassName + "] to role" + roleName +
							" and scope " + scope);
			}
		}
		catch (NoSuchResourceActionException nsrae) {
			if (logger.isInfoEnabled()) {
				logger.info(nsrae.getMessage());
				logger.info(
					"Resource Action [" + actionId + "," + resourceClassName +
						"] does not exist or is invalid");
			}
		}
	}

	/**
	 *
	 * @param roleName
	 * @param resourceClassName
	 * @param actionId
	 * @param roleId
	 * @param scope
	 * @param primKey
	 * @throws PortalException
	 * @throws SystemException
	 */
	protected static void addPermission(
			String roleName, String resourceClassName, String actionId,
			long roleId, int scope, String primKey)
		throws PortalException {

		try {
			//parameters (long companyId, java.lang.String name, int scope, java.lang.String primKey, long roleId, java.lang.String actionId
			ResourcePermissionLocalServiceUtil.addResourcePermission(
				COMPANY_ID, resourceClassName, scope, primKey, roleId,
				actionId);

			if (logger.isInfoEnabled()) {
				logger.info(
					"Added Permission [" + actionId + "," + resourceClassName +
						"] to role " + roleName + " and scope " + scope);
			}
		}
		catch (NoSuchResourceActionException nsrae) {
			if (logger.isInfoEnabled()) {
				logger.info(nsrae.getMessage());
				logger.info(
					"Resource Action [" + actionId + "," + resourceClassName +
						"] does not exist or is invalid");
			}
		}
	}

	/**
	 * Uses the LifeRay API to set a permission against an existing resource.
	 * Note that this also overrides existing permissions. User addPermission instead to simply add permissions
	 *
	 * @param roleName
	 * @param resourceClassName
	 * @param actionId
	 * @param roleId
	 * @param scope
	 * @param primKey
	 * @throws PortalException
	 * @throws SystemException
	 */
	protected static void setPermission(
			String roleName, String resourceClassName, String actionId,
			long roleId, int scope, String primKey)
		throws PortalException {

		try {
			ResourcePermissionLocalServiceUtil.setResourcePermissions(
				COMPANY_ID, resourceClassName, scope, primKey, roleId,
				new String[] {actionId});

			if (logger.isInfoEnabled()) {
				logger.info(
					"Added Permission [" + actionId + "," + resourceClassName +
						"] to role" + roleName + " and scope " + scope);
			}
		}
		catch (NoSuchResourceActionException nsrae) {
			if (logger.isInfoEnabled()) {
				logger.info(nsrae.getMessage());
				logger.info(
					"Resource Action [" + actionId + "," + resourceClassName +
						"] does not exist or is invalid");
			}
		}
	}

	private static long COMPANY_ID = CommonUtil.retrieveCompanyId();

	private static String CUSTOM_FIELD_RESOURCE_NAME =
		"com.liferay.expando.kernel.model.ExpandoColumn";

	private static Logger logger = LoggerFactory.getLogger(
		PermissionUtil.class);

}