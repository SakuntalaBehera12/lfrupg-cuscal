package au.com.cuscal.connect.util.resource.dxp;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Utility for commonly accessed information
 * @author jluu
 */
public class CommonUtil {

	/**
	 * Retrieves Integer elements from nodes
	 * @param ele
	 * @param tagName
	 * @return int
	 * @throws NumberFormatException
	 */
	public static int getIntValue(Element ele, String tagName)
		throws NumberFormatException {

		// in production application you would catch the exception

		return Integer.parseInt(getTextValue(ele, tagName));
	}

	/**
	 * Retrieves text value elements from nodes
	 * @param ele
	 * @param tagName
	 * @return textValue
	 */
	public static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);

		if ((nl != null) && (nl.getLength() > 0)) {
			Element el = (Element)nl.item(0);

			textVal = el.getFirstChild(
			).getNodeValue();
		}

		return textVal;
	}

	/**
	 * Finds a userId with the Administrator role
	 * @return long
	 */
	public static long retrieveAdminUser() {
		Role role;
		long[] adminUserIds = {};

		try {
			role = RoleLocalServiceUtil.getRole(
				retrieveCompanyId(), "Administrator");
			adminUserIds = UserLocalServiceUtil.getRoleUserIds(
				role.getRoleId());
		}
		catch (PortalException pe) {
			if (logger.isErrorEnabled()) {
				logger.error(pe.toString());
			}
		}
		catch (SystemException se) {
			if (logger.isErrorEnabled()) {
				logger.error(se.toString());
			}
		}

		return adminUserIds[0];
	}

	/**
	 * Retrieves the default company Id. This is the existing portal instance
	 * @return long
	 */
	public static long retrieveCompanyId() {
		return PortalUtil.getDefaultCompanyId();
	}

	private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

}