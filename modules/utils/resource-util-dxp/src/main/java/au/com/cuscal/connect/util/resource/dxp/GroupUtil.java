package au.com.cuscal.connect.util.resource.dxp;

import au.com.cuscal.connect.util.resource.xml.Community;
import au.com.cuscal.connect.util.resource.xml.Organisation;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.NoSuchOrganizationException;
import com.liferay.portal.kernel.exception.NoSuchUserGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.ListType;
import com.liferay.portal.kernel.model.ListTypeConstants;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.OrganizationConstants;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ListTypeServiceUtil;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupLocalServiceUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility wrapper class to access Liferay specific functionality for groups
 * @author jluu
 *
 */
public class GroupUtil {

	/**
	 * Creates a new community based on the community XML object
	 * @param community
	 * @return Group
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static Group createCommunityGroup(Community community)
		throws PortalException {

		return createCommunityGroup(
			community.getCommunityName(), community.getDescription());
	}

	/**
	 * Creates a community using the Liferay API
	 *
	 * @param companyId
	 * @param groupName
	 * @param description
	 * @param type
	 * @param friendlyURL
	 * @param active
	 * @return Group
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static Group createCommunityGroup(
			long companyId, String groupName, String description, int type,
			String friendlyURL, boolean active)
		throws PortalException {

		try {
			Group group = GroupLocalServiceUtil.getGroup(COMPANY_ID, groupName);

			if (logger.isInfoEnabled()) {
				logger.info(groupName + " already exists");
			}

			return group;
		}
		catch (NoSuchGroupException nsge) {
			Locale locale = LocaleThreadLocal.getDefaultLocale();
			Map<Locale, String> nameMap = new HashMap<>();

			nameMap.put(locale, groupName);

			Map<Locale, String> descriptionMap = new HashMap<>();

			descriptionMap.put(locale, description);

			//parameters are userId, className, classPK, name, description, type, friendlyURL, active, serviceContext);
			//Group group = GroupLocalServiceUtil.addGroup(ADMIN_USER, null, 0, groupName, description, type, friendlyURL, active, null);
			Group group = GroupLocalServiceUtil.addGroup(
				ADMIN_USER, 0, null, 0, 0, nameMap, descriptionMap, type, true,
				0, friendlyURL, true, active, null);

			if (logger.isInfoEnabled()) {
				logger.info("New GroupId:" + group.getGroupId());
			}

			return group;
		}
	}

	/**
	 *
	 * @param groupName
	 * @param description
	 * @return Group
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static Group createCommunityGroup(
			String groupName, String description)
		throws PortalException {

		return createCommunityGroup(
			COMPANY_ID, groupName, description, GroupConstants.TYPE_SITE_OPEN,
			null, ACTIVE);
	}

	/**
	 * Creates a new organisation based on organisation resource
	 *
	 * @param organisation
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static Organization createOrganisation(Organisation organisation)
		throws PortalException {

		Organization organization = createOrganisationGroup(
			organisation.getOrganisationName(), organisation.getDescription(),
			organisation.getCuscalOrgId(), organisation.getBsb(),
			organisation.getDn());

		return organization;
	}

	/**
	 * Creates a new organisation based on the organisation object
	 *
	 * @param orgName
	 * @param orgDescription
	 * @param cuscalId
	 * @param bsb
	 * @param dn
	 * @return Organization
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static Organization createOrganisationGroup(
			String orgName, String orgDescription, String cuscalId, String bsb,
			String dn)
		throws PortalException {

		return createOrganisationGroup(
			orgName, orgDescription, cuscalId, bsb, dn,
			OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID,
			OrganizationConstants.TYPE_ORGANIZATION, true, REGION_ID,
			COUNTRY_ID);
	}

	/**
	 * Creates a new organissation or updates the existing one using the LiferayAPI
	 *
	 * @param orgName
	 * @param orgDescription
	 * @param cuscalId
	 * @param bsb
	 * @param dn
	 * @param parentOrgId
	 * @param organisationType
	 * @param recursible
	 * @param regionId
	 * @param countryId
	 * @return Organization
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static Organization createOrganisationGroup(
			String orgName, String orgDescription, String cuscalId, String bsb,
			String dn, int parentOrgId, String organisationType,
			boolean recursible, int regionId, int countryId)
		throws PortalException {

		Organization org;
		List<ListType> statuses = ListTypeServiceUtil.getListTypes(
			COMPANY_ID, ListTypeConstants.ORGANIZATION_STATUS);

		ListType status = statuses.get(0);

		long statusId = status.getListTypeId();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setUserId(ADMIN_USER);

		try {
			Organization existingOrg =
				OrganizationLocalServiceUtil.getOrganization(
					COMPANY_ID, orgName);

			//				org = OrganizationLocalServiceUtil.updateOrganization(COMPANY_ID, existingOrg.getOrganizationId(), parentOrgId, orgName, organisationType,
			//						recursible, regionId, countryId, statusId, orgDescription, serviceContext);

			// TODO fix it

			byte[] logoBytes = new byte[0];

			// TODO fix it

			boolean site = false;

			if (existingOrg.hasPrivateLayouts() ||
				existingOrg.hasPublicLayouts()) {

				site = true;
			}

			org = OrganizationLocalServiceUtil.updateOrganization(
				StringPool.BLANK, COMPANY_ID, existingOrg.getOrganizationId(), parentOrgId,
				orgName, organisationType, regionId, countryId, statusId,
				orgDescription, existingOrg.getLogoId() > 0, logoBytes, site,
				serviceContext);

			// TODO check and fix?

			org.setRecursable(recursible);
			org = OrganizationLocalServiceUtil.updateOrganization(org);

			if (logger.isInfoEnabled()) {
				logger.info(
					"Updated Organisation " + orgName + " OrganizationId:" +
						org.getOrganizationId());
			}
		}
		catch (NoSuchOrganizationException nsoe) {

			//				org = OrganizationLocalServiceUtil.addOrganization(ADMIN_USER, parentOrgId, orgName, organisationType,
			//						recursible, regionId, countryId, statusId, orgDescription, serviceContext);

			// TODO fix it

			boolean site = false;

			org = OrganizationLocalServiceUtil.addOrganization(
				StringPool.BLANK, ADMIN_USER, parentOrgId, orgName, organisationType, regionId,
				countryId, statusId, orgDescription, site, serviceContext);

			// TODO check and fix?

			org.setRecursable(recursible);
			org = OrganizationLocalServiceUtil.updateOrganization(org);

			if (logger.isInfoEnabled()) {
				logger.info(
					"Created Organisation " + orgName + " OrganizationId:" +
						org.getOrganizationId());
			}
		}

		CustomFieldUtil.addCustomFieldValueToOrganisation(
			org.getOrganizationId(), "cuscalCUOrgId", cuscalId);
		CustomFieldUtil.addCustomFieldValueToOrganisation(
			org.getOrganizationId(), "cuscalBSB", bsb);
		CustomFieldUtil.addCustomFieldValueToOrganisation(
			org.getOrganizationId(), "LDAPUserDN", dn);

		return org;
	}

	/**
	 * Adds a user group using the Liferay API
	 *
	 * @param userGroupName
	 * @param userGroupDescription
	 * @return UserGroup
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static UserGroup createUserGroup(
			String userGroupName, String userGroupDescription)
		throws PortalException {

		if (logger.isInfoEnabled()) {
			logger.info("Adding User Group " + userGroupName);
		}

		try {
			UserGroup userGroup = UserGroupLocalServiceUtil.getUserGroup(
				COMPANY_ID, userGroupName);

			if (logger.isInfoEnabled()) {
				logger.info("Usergroup " + userGroupName + "already exists");
			}

			return userGroup;
		}
		catch (NoSuchUserGroupException nsge) {

			//			UserGroup userGroup = UserGroupLocalServiceUtil.addUserGroup(ADMIN_USER, COMPANY_ID, userGroupName, userGroupDescription);

			UserGroup userGroup = UserGroupLocalServiceUtil.addUserGroup(
				ADMIN_USER, COMPANY_ID, userGroupName, userGroupDescription,
				null);

			System.out.println("New UserGroupId:" + userGroup.getUserGroupId());

			if (logger.isInfoEnabled()) {
				logger.info(
					"New User Group " + userGroupName +
						"added with UserGroupId:" + userGroup.getUserGroupId());
			}

			return userGroup;
		}
	}

	/**
	 * Adds a user group with the User Group XML resource
	 *
	 * @param usergroup
	 * @return UserGroup
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static UserGroup createUserGroup(
			au.com.cuscal.connect.util.resource.xml.UserGroup usergroup)
		throws PortalException {

		return createUserGroup(
			usergroup.getUserGroupName(), usergroup.getDescription());
	}

	/**
	 * Adds a user group to a community if it exists. Checks if the link is already present aswell.
	 *
	 * @param userGroupName
	 * @param communityName
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void linkUserGroupToCommunity(
			String userGroupName, String communityName)
		throws PortalException {

		if (logger.isInfoEnabled()) {
			logger.info(
				"Linking UserGroup " + userGroupName + " to Community " +
					communityName);
		}

		try {
			UserGroup userGroup = UserGroupLocalServiceUtil.getUserGroup(
				COMPANY_ID, userGroupName);
			Group community = GroupLocalServiceUtil.getGroup(
				COMPANY_ID, communityName);

			long communityId = community.getGroupId();
			long userGroupId = userGroup.getUserGroupId();

			if (!UserGroupLocalServiceUtil.hasGroupUserGroup(
					communityId, userGroupId)) {

				UserGroupLocalServiceUtil.addGroupUserGroups(
					communityId, new long[] {userGroupId});

				if (logger.isInfoEnabled()) {
					logger.info(
						"Successfully linked UserGroup " + userGroupName +
							" to Community " + communityName);
				}
			}
			else {
				if (logger.isInfoEnabled()) {
					logger.info(
						"UserGroup " + userGroupName +
							" already linked to Community " + communityName);
				}
			}
		}
		catch (NoSuchUserGroupException nsge) {
			if (logger.isInfoEnabled()) {
				logger.info(
					"Usergroup " + userGroupName + "does not exist exists");
				logger.info(nsge.getMessage());
			}
		}
		catch (NoSuchGroupException nsge) {
			if (logger.isInfoEnabled()) {
				logger.info(
					"Community " + userGroupName + "does not exist exists");
				logger.info(nsge.getMessage());
			}
		}
	}

	/**
	 * Removes a user group from a community if it exists.
	 *
	 * @param userGroupName
	 * @param communityName
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void unlinkUserGroupToCommunity(
			String userGroupName, String communityName)
		throws PortalException {

		if (logger.isInfoEnabled()) {
			logger.info(
				"Unlinking UserGroup " + userGroupName + " to Community " +
					communityName);
		}

		try {
			UserGroup userGroup = UserGroupLocalServiceUtil.getUserGroup(
				COMPANY_ID, userGroupName);
			Group community = GroupLocalServiceUtil.getGroup(
				COMPANY_ID, communityName);

			long communityId = community.getGroupId();
			long userGroupId = userGroup.getUserGroupId();

			if (UserGroupLocalServiceUtil.hasGroupUserGroup(
					communityId, userGroupId)) {

				UserGroupLocalServiceUtil.unsetGroupUserGroups(
					communityId, new long[] {userGroupId});

				if (logger.isInfoEnabled()) {
					logger.info(
						"Successfully unlinked UserGroup " + userGroupName +
							" from Community " + communityName);
				}
			}
			else {
				if (logger.isInfoEnabled()) {
					logger.info(
						"UserGroup " + userGroupName +
							" is not linked to Community " + communityName);
				}
			}
		}
		catch (NoSuchUserGroupException nsge) {
			if (logger.isInfoEnabled()) {
				logger.info(
					"Usergroup " + userGroupName + "does not exist exists");
				logger.info(nsge.getMessage());
			}
		}
		catch (NoSuchGroupException nsge) {
			if (logger.isInfoEnabled()) {
				logger.info(
					"Community " + userGroupName + "does not exist exists");
				logger.info(nsge.getMessage());
			}
		}
	}

	private static boolean ACTIVE = true;

	private static long ADMIN_USER = CommonUtil.retrieveAdminUser();

	private static long COMPANY_ID = CommonUtil.retrieveCompanyId();

	private static int COUNTRY_ID = 0;

	private static int REGION_ID = 0;

	private static final Logger logger = LoggerFactory.getLogger(
		GroupUtil.class);

}