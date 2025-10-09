package au.com.cuscal.connect.util.resource.dxp;

import au.com.cuscal.connect.util.resource.xml.CustomField;

import com.liferay.expando.kernel.exception.DuplicateColumnNameException;
import com.liferay.expando.kernel.exception.NoSuchTableException;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoValue;
import com.liferay.expando.kernel.service.ExpandoColumnLocalServiceUtil;
import com.liferay.expando.kernel.service.ExpandoTableLocalServiceUtil;
import com.liferay.expando.kernel.service.ExpandoValueLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to allow the creation and use of Custom Fields
 *
 * @author jluu
 *
 */
public class CustomFieldUtil {

	/**
	 * Adds a custom field for a specific class
	 *
	 * @param name
	 * @param type
	 * @param className
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void addAttribute(String name, int type, String className)
		throws PortalException {

		ExpandoTable table = getExpandoTable(className);

		try {
			ExpandoColumnLocalServiceUtil.addColumn(
				table.getTableId(), name, type);

			if (logger.isInfoEnabled()) {
				logger.info("Added Custom Field " + name + " to " + type);
			}
		}
		catch (DuplicateColumnNameException dcne) {
			if (logger.isErrorEnabled()) {
				logger.error(dcne.getMessage());
				logger.error("Custom Field " + name + "already exists");
			}
		}
	}

	/**
	 * Adds a custom field for a group type
	 *
	 * @param customField
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void addCustomField(CustomField customField)
		throws PortalException {

		if ((customField.getClassName() != null) &&
			(customField.getType() != null)) {

			if (customField.getType(
				).equals(
					"String"
				)) {

				addAttribute(
					customField.getName(), ExpandoColumnConstants.STRING,
					getFullClassName(customField.getClassName()));
			}
		}
	}

	/**
	 * Adds a custom field for organisation class
	 *
	 * @param organisationId
	 * @param fieldName
	 * @param fieldValue
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void addCustomFieldValueToOrganisation(
			long organisationId, String fieldName, String fieldValue)
		throws PortalException {

		addValue(
			Organization.class.getName(), organisationId, fieldName,
			fieldValue);
	}

	/**
	 * Adds am custom field (eg.columnName = bsb) for a specific class (eg. className = organisation)
	 *
	 * @param className
	 * @param classPK
	 * @param columnName
	 * @param data
	 * @return ExpandoValue
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static ExpandoValue addValue(
			String className, long classPK, String columnName, String data)
		throws PortalException {

		ExpandoTable table = getExpandoTable(className);

		ExpandoValue expandoValue = ExpandoValueLocalServiceUtil.addValue(
			COMPANY_ID, className, table.getName(), columnName, classPK, data);

		return expandoValue;
	}

	/**
	 * Returns the default expando table and creates one if necessary. Mimics the existing remote service
	 *
	 * @param className
	 * @return ExpandoTable
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static ExpandoTable getExpandoTable(String className)
		throws PortalException {

		ExpandoTable table = null;

		try {
			table = ExpandoTableLocalServiceUtil.getDefaultTable(
				COMPANY_ID, className);
		}
		catch (NoSuchTableException nste) {
			table = ExpandoTableLocalServiceUtil.addDefaultTable(
				COMPANY_ID, className);
		}

		return table;
	}

	/**
	 * Retrieves the full classname based on a shorted name
	 * @param className
	 * @return fullClassName
	 */
	public static String getFullClassName(String className) {
		if (className.equals("Organization")) {
			return Organization.class.getName();
		}
		else if (className.equals("User")) {
			return User.class.getName();
		}

		return className;
	}

	/**
	 * Retrieves a customField for a specific class
	 *
	 * @param className
	 * @param attributeName
	 * @return ExpandoColumn
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static ExpandoColumn retrieveAttribute(
			String className, String attributeName)
		throws PortalException {

		ExpandoTable table = getExpandoTable(className);

		return ExpandoColumnLocalServiceUtil.fetchColumn(
			table.getTableId(), attributeName);
	}

	/**
	 * Retrieves a Custom field for an organisation
	 *
	 * @param attributeName
	 * @return ExpandoColumn
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static ExpandoColumn retrieveAttributeForOrganisation(
			String attributeName)
		throws PortalException {

		return retrieveAttribute(Organization.class.getName(), attributeName);
	}

	private static long COMPANY_ID = CommonUtil.retrieveCompanyId();

	private static Logger logger = LoggerFactory.getLogger(
		CustomFieldUtil.class);

}