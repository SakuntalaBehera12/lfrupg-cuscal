package au.com.cuscal.framework.webservices.liferay.dxp;

import au.com.cuscal.framework.webservices.client.EntityService;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdRequestType;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdResponseType;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.List;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LiferayClientUtil {

	public static final int ENTITY_ATTRIBUTE_LIFERAY_ORGANISATION_ID = 4;

	public static final String ORG_SHORT_NAME = "orgShortName";

	public static String getOrgShortName(
		EntityService entityService, long liferayOrgId) {

		logger.debug("getOrgShortName - start, liferayOrgId=" + liferayOrgId);

		String orgShortName = null;
		FindEntitiesByAttributeWithIdRequestType request =
			new FindEntitiesByAttributeWithIdRequestType();

		request.setAttributeValue(String.valueOf(liferayOrgId));
		request.setAttributeTypeId(ENTITY_ATTRIBUTE_LIFERAY_ORGANISATION_ID);

		logger.debug(
			"getOrgShortName - looking up liferayOrgId=" + liferayOrgId);

		FindEntitiesByAttributeWithIdResponseType
			findEntitiesByAttributeWithIdResponseType =
				entityService.findEntitiesByAttributeWithId(request);

		if (!findEntitiesByAttributeWithIdResponseType.getEntities(
			).isEmpty())
			orgShortName =
				findEntitiesByAttributeWithIdResponseType.getEntities(
				).getFirst(
				).getShortName();

		logger.debug("getOrgShortName - end, returning=" + orgShortName);

		return orgShortName;
	}

	public static String getOrgShortName(
			EntityService entityService, PortletRequest request)
		throws Exception {

		logger.debug("getOrgShortName - start, with portlet request");

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		String orgShortName = (String)session.getAttribute(ORG_SHORT_NAME);

		if (orgShortName == null) {
			logger.debug(
				"getOrgShortName - " + ORG_SHORT_NAME +
					" variable is not in session... looking up");

			User user =
				au.com.cuscal.framework.audit.liferay.dxp.LiferayUtil.getUser(
					request);

			orgShortName = getOrgShortName(entityService, user);

			if (!orgShortName.isBlank()) {
				session.setAttribute(ORG_SHORT_NAME, orgShortName);
			}
			else {
				logger.warn(
					"getOrgShortName - Could not find orgShortName for User=" +
						user.getUserId());
			}
		}

		logger.debug(
			"getOrgShortName - end, returning organisation short name as " +
				orgShortName);

		return orgShortName;
	}

	/**
	 * @param user
	 * @return
	 * @throws PortalException
	 * @throws Exception
	 */
	public static String getOrgShortName(EntityService entityService, User user)
		throws Exception, PortalException {

		long userId = user.getUserId();
		String screenName = user.getScreenName();

		logger.debug("getOrgShortName - Found details for user " + screenName);

		List<Organization> organizations =
			OrganizationLocalServiceUtil.getUserOrganizations(userId);

		if (organizations.size() > 1) {

			// TODO: handle this better

			throw new Exception(
				"Unsupported configuration: User " + screenName + " has " +
					organizations.size() + " organisation(s) assigned");
		}

		long liferayOrgId = organizations.getFirst().getOrganizationId();

		return getOrgShortName(entityService, liferayOrgId);
	}

	private static final Log logger = LogFactoryUtil.getLog(LiferayClientUtil.class);

}