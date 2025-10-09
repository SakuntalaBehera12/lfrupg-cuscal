package au.com.cuscal.connect.util.resource.dxp;

import au.com.cuscal.connect.util.resource.xml.Community;
import au.com.cuscal.connect.util.resource.xml.CustomField;
import au.com.cuscal.connect.util.resource.xml.GroupLinkRelationship;
import au.com.cuscal.connect.util.resource.xml.GroupUnlinkRelationship;
import au.com.cuscal.connect.util.resource.xml.Organisation;
import au.com.cuscal.connect.util.resource.xml.Permission;
import au.com.cuscal.connect.util.resource.xml.PermissionLinkRelationship;
import au.com.cuscal.connect.util.resource.xml.PermissionSet;
import au.com.cuscal.connect.util.resource.xml.PermissionUnlinkRelationship;
import au.com.cuscal.connect.util.resource.xml.Relationship;
import au.com.cuscal.connect.util.resource.xml.Resource;
import au.com.cuscal.connect.util.resource.xml.Role;
import au.com.cuscal.connect.util.resource.xml.UserGroup;

import com.liferay.portal.kernel.exception.PortalException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

/**
 *
 * Utility that parses XML data and performs the appropriate processing on the input
 * @author jluu
 *
 */
public class InputReaderUtil {

	/**
	 * Based on a set of permissions and links between permissions and roles, sends them off to be assigned
	 *
	 * @param permissionSetList
	 * @param relationships
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void addPermissionSetsToRoles(
			List<PermissionSet> permissionSetList,
			List<Relationship> relationships)
		throws PortalException {

		Iterator<Relationship> rlIterator = relationships.iterator();

		while (rlIterator.hasNext()) {
			Relationship relationship = (Relationship)rlIterator.next();

			if ((relationship.getPermissionSet() != null) &&
				(relationship.getRoleName() != null)) {

				retrieveAndSetPermissionSetForRole(
					permissionSetList, relationship);
			}
		}
	}

	/**
	 * Creates a relationship between two resources
	 *
	 * @param link
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void createLink(GroupLinkRelationship link)
		throws PortalException {

		if ((link.getRoleName() != null) &&
			!link.getRoleName(
			).equals(
				""
			)) {

			if ((link.getUserGroup() != null) &&
				!link.getUserGroup(
				).equals(
					""
				)) {

				RoleUtil.linkUserGroupToRole(
					link.getRoleName(), link.getUserGroup());
			}

			if ((link.getOrganisation() != null) &&
				!link.getOrganisation(
				).equals(
					""
				)) {

				RoleUtil.linkOrganisationToRole(
					link.getRoleName(), link.getOrganisation());
			}
		}

		if ((link.getCommunity() != null) &&
			!link.getCommunity(
			).equals(
				""
			)) {

			if ((link.getUserGroup() != null) &&
				!link.getUserGroup(
				).equals(
					""
				)) {

				GroupUtil.linkUserGroupToCommunity(
					link.getUserGroup(), link.getCommunity());
			}
		}
	}

	public static void main(String[] args) {

		//RoleUtil.addPermissionToRole(roleName, resourceClassName, actionKeys, scope);

		//sretrievePermissionsAndDisplay();

		//retrieveRelationship();

	}

	/**
	 * Accepts the location of an input file  and returns the parsed document
	 *
	 * @param inputFile location
	 * @return Document
	 */
	public static Document parseInputFile(String inputFile) {
		DocumentBuilderFactory documentBuilderFactory =
			DocumentBuilderFactory.newInstance();
		Document dom = null;

		try {
			DocumentBuilder documentBuilder =
				documentBuilderFactory.newDocumentBuilder();

			dom = documentBuilder.parse(inputFile);
		}
		catch (ParserConfigurationException pce) {
			if (logger.isErrorEnabled()) {
				logger.error(pce.toString());
			}
		}
		catch (SAXException se) {
			if (logger.isErrorEnabled()) {
				logger.error(se.toString());
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();

			if (logger.isErrorEnabled()) {
				logger.error(ioe.toString());
			}
		}

		return dom;
	}

	/**
	 * Removes the relationship between two resources
	 *
	 * @param unlink
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void removeLink(GroupUnlinkRelationship unlink)
		throws PortalException {

		if ((unlink.getRoleName() != null) &&
			!unlink.getRoleName(
			).equals(
				""
			)) {

			if ((unlink.getUserGroup() != null) &&
				!unlink.getUserGroup(
				).equals(
					""
				)) {

				RoleUtil.unlinkUserGroupToRole(
					unlink.getRoleName(), unlink.getUserGroup());
			}

			if ((unlink.getOrganisation() != null) &&
				!unlink.getOrganisation(
				).equals(
					""
				)) {

				RoleUtil.unlinkOrganisationToRole(
					unlink.getRoleName(), unlink.getOrganisation());
			}
		}

		if ((unlink.getCommunity() != null) &&
			!unlink.getCommunity(
			).equals(
				""
			)) {

			if ((unlink.getUserGroup() != null) &&
				!unlink.getUserGroup(
				).equals(
					""
				)) {

				GroupUtil.unlinkUserGroupToCommunity(
					unlink.getUserGroup(), unlink.getCommunity());
			}
		}
	}

	/**
	 * Retrieves and adds to a list of Resource objects defined by the XML input file and the resource type
	 *
	 * @param resourceList
	 * @param inputFile
	 * @param resource
	 * @return List<Resource>
	 */
	public static List<Resource> retrieveResources(
		List<Resource> resourceList, String inputFile, Resource resource) {

		Document doc = parseInputFile(inputFile);

		List<Resource> elementsList = resource.retrieveResourceElements(doc);

		resourceList.addAll(elementsList);

		return resourceList;
	}

	/**
	 *
	 *
	 * @param customFieldList
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void setupCustomFields(List<Resource> customFieldList)
		throws PortalException {

		Iterator<Resource> cfIterator = customFieldList.iterator();

		while (cfIterator.hasNext()) {
			Resource resource = (Resource)cfIterator.next();

			if (resource instanceof CustomField) {
				CustomField customField = (CustomField)resource;

				CustomFieldUtil.addCustomField(customField);
			}
		}
	}

	/**
	 * Creates Roles, usergroups, organisations and communitites based on an inputed list
	 *
	 * @param groupCreationList
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void setupGroups(List<Resource> groupCreationList)
		throws PortalException {

		Iterator<Resource> gpIterator = groupCreationList.iterator();

		while (gpIterator.hasNext()) {
			Resource resource = (Resource)gpIterator.next();

			if (resource instanceof Role) {
				Role role = (Role)resource;

				RoleUtil.createRole(
					role.getRoleName(), role.getDescription(), role.getType());
			}
			else if (resource instanceof Organisation) {
				Organisation organisation = (Organisation)resource;

				GroupUtil.createOrganisation(organisation);
			}
			else if (resource instanceof Community) {
				Community community = (Community)resource;

				GroupUtil.createCommunityGroup(community);
			}
			else if (resource instanceof UserGroup) {
				UserGroup userGroup = (UserGroup)resource;

				GroupUtil.createUserGroup(userGroup);
			}
		}
	}

	/**
	 * Based on the list of resource objects passed in extract the resource links and the full permission sets
	 *
	 * @param resources
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void setupPermissions(List<Resource> resources)
		throws PortalException {

		Iterator<Resource> resourceIterator = resources.iterator();
		List<PermissionSet> permissionSetList = new ArrayList<>();
		List<Relationship> relationships = new ArrayList<>();

		while (resourceIterator.hasNext()) {
			Resource resource = (Resource)resourceIterator.next();

			if (resource instanceof Relationship) {
				Relationship relationship = (Relationship)resource;

				relationships.add(relationship);
			}
			else if (resource instanceof PermissionSet) {
				PermissionSet permSet = (PermissionSet)resource;

				permissionSetList.add(permSet);
			}
		}

		addPermissionSetsToRoles(permissionSetList, relationships);
	}

	/**
	 * Adds a link between a group
	 *
	 * @param permissionSet
	 * @param roleName
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void setupRelationShips(List<Resource> groupCreationList)
		throws PortalException {

		Iterator<Resource> gpIterator = groupCreationList.iterator();

		while (gpIterator.hasNext()) {
			Resource resource = (Resource)gpIterator.next();

			if (resource instanceof GroupLinkRelationship) {
				GroupLinkRelationship link = (GroupLinkRelationship)resource;

				createLink(link);
			}
			else if (resource instanceof GroupUnlinkRelationship) {
				GroupUnlinkRelationship unlink =
					(GroupUnlinkRelationship)resource;

				removeLink(unlink);
			}
		}
	}

	/**
	 * Loops through all the permission sets in memory and checks whether the name
	 * is the same as the one that is used for assignment to the role eg. Edit Document for Document Editor
	 * If this is the case then execute the permission assignment
	 *
	 * @param permissionSetList
	 * @param relationship
	 * @throws PortalException
	 * @throws SystemException
	 */
	protected static void retrieveAndSetPermissionSetForRole(
			List<PermissionSet> permissionSetList, Relationship relationship)
		throws PortalException {

		Iterator<PermissionSet> pslIterator = permissionSetList.iterator();

		while (pslIterator.hasNext()) {
			PermissionSet definedPermissionSet = pslIterator.next();

			String linkingPermissionSetName = relationship.getPermissionSet();

			if (linkingPermissionSetName.equals(
					definedPermissionSet.getName())) {

				if (relationship instanceof PermissionLinkRelationship) {
					setPermissionsForRole(
						definedPermissionSet, relationship.getRoleName(), true);
				}
				else if (relationship instanceof PermissionUnlinkRelationship) {
					setPermissionsForRole(
						definedPermissionSet, relationship.getRoleName(),
						false);
				}
			}
		}
	}

	/**
	 * Assigns an entire set of permissions as stated in permissions.xml to a role.
	 * It will add the permission based on the addPermission boolean being true otherwise
	 * it will remove the permission
	 *
	 * @param permissionSet
	 * @param roleName
	 * @throws PortalException
	 * @throws SystemException
	 */
	protected static void setPermissionsForRole(
			PermissionSet permissionSet, String roleName, boolean addPermission)
		throws PortalException {

		List<Permission> permissionList = permissionSet.getPermissionSet();

		Iterator<Permission> permIt = permissionList.iterator();

		while (permIt.hasNext()) {
			Permission permission = (Permission)permIt.next();

			if (addPermission) {
				PermissionUtil.addPermissionToRole(roleName, permission);
			}
			else {
				PermissionUtil.removePermissionFromRole(
					roleName, permission.getTargetResource(),
					permission.getActionId());
			}
		}
	}

	private static Logger logger = LoggerFactory.getLogger(
		InputReaderUtil.class);

}