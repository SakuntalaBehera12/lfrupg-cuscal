package au.com.cuscal.common.framework.dxp.service.request.controller;

import au.com.cuscal.common.framework.service.request.commons.Constants;
import au.com.cuscal.common.framework.service.request.services.ServiceRequestService;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.List;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class AbstractServiceRequestController {

	/**
	 *
	 * @param searchHeader
	 * @return
	 */
	public static TktRequestHeader convertSearchHeaderToTktHeader(
		SearchHeader searchHeader) {

		TktRequestHeader tktHeader = null;

		if (null != searchHeader) {
			tktHeader = new TktRequestHeader();

			tktHeader.setOrigin(searchHeader.getOrigin());
			tktHeader.setUserId(searchHeader.getUserId());
			tktHeader.setUserOrgId(searchHeader.getUserOrgId());
			tktHeader.setUserOrgName(searchHeader.getUserOrgName());
		}

		return tktHeader;
	}

	public Boolean doesUserHaveServiceRequestAddRole(PortletRequest request) {
		Boolean hasRole = Boolean.FALSE;
		//		String checkingForRole = transactionAppProperties.getTransProps()
		//				.getProperty("service.request.role.add");
		//
		//		logger.debug("doesUserHaveServiceRequestRole - checkingForRole is: "
		//				+ checkingForRole);
		//		User user = Utility.getUserFromRequest(request);
		//		if (null != user) {
		//			List<Role> userRoleList = Utility.getAllUserRoles(user);
		//
		//			for (Role role : userRoleList) {
		//				if (StringUtils.equalsIgnoreCase(checkingForRole,
		//						role.getName())) {
		//					hasRole = Boolean.TRUE;
		//				}
		//			}
		//		}

		return hasRole;
	}

	/**
	 * organisationIdOverride object getter
	 */
	public String getOrganisationIdOverride() {
		return organisationIdOverride;
	}

	/**
	 * Get the organisation short name by liferay organisation id
	 *
	 * @param String
	 * @param portletRequest
	 * @return String
	 */
	public String getOrgShortName(
		String liferayOrgId, PortletRequest portletRequest) {

		logger.debug("getOrgShortName - start, liferayOrgId=" + liferayOrgId);

		if (StringUtils.isNotEmpty(organisationIdOverride)) {
			logger.debug(
				"getOrgShortName - end, organisationIdOverride has been set.  Using " +
					organisationIdOverride);
			liferayOrgId = organisationIdOverride;
		}

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		HttpSession session = httpServletRequest.getSession();

		String orgShortName = (String)session.getAttribute(
			Constants.ORG_SHORT_NAME);

		if (orgShortName == null) {
			orgShortName = serviceRequestService.getOrgShortName(
				liferayOrgId, portletRequest);

			if (StringUtils.isNotBlank(orgShortName)) {
				session.setAttribute(Constants.ORG_SHORT_NAME, orgShortName);
			}
			else {
				logger.warn(
					"getOrgShortName - Could not find orgShortName for liferayOrgId=" +
						liferayOrgId);
			}
		}

		logger.debug(
			"getOrgShortName - end, returning organisation short name as " +
				orgShortName);

		return orgShortName;
	}

	/**
	 * create the searchHeader object for web service.
	 *
	 * @param PortletRequest
	 * @return SearchHeader
	 */
	public SearchHeader getSearchHeaderData(PortletRequest request) {
		logger.debug("getSearchHeaderData - start");
		SearchHeader searchHeader = null;
		User user = null;

		try {
			user = PortalUtil.getUser(request);
			String screenName = user.getScreenName();

			if (null != user) {
				searchHeader = new SearchHeader();
				Long userId = user.getUserId();

				List<Organization> organizations =
					OrganizationLocalServiceUtil.getUserOrganizations(userId);

				if ((null != organizations) && (1 == organizations.size())) {
					String userOrgName = organizations.get(
						0
					).getName();
					String orgShortName = getOrgShortName(
						Long.valueOf(
							organizations.get(
								0
							).getOrganizationId()
						).toString(),
						request);

					if (StringUtils.isNotBlank(orgShortName)) {
						searchHeader.setUserOrgId(orgShortName);
						logger.debug(
							"getSearchHeaderData - orgShortName is : " +
								orgShortName);
					}
					else {
						logger.error(
							"The Search header is not null but org short name is null for org id ");
						searchHeader.setUserOrgId(null);
					}

					searchHeader.setUserOrgName(userOrgName);
					searchHeader.setUserId(user.getScreenName());
					searchHeader.setOrigin(AuditOrigin.PORTAL_ORIGIN);
				}
				else {
					searchHeader.setUserOrgId(null);

					if (organizations == null) {
						logger.warn(
							screenName + " has no organisations assigned!");
					}
					else {
						String orgNames = null;

						if (organizations != null) {
							for (Organization organization : organizations) {
								orgNames += " " + organization.getName();
							}
						}

						logger.warn(
							screenName + " has " + organizations.size() +
								" organisations assigned: " + orgNames);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error(
				"Exception setting search header: " + e.getMessage(), e);
		}

		logger.debug("getSearchHeaderData - end");

		return searchHeader;
	}

	/**
	 * organisationIdOverride object setter
	 */
	public void setOrganisationIdOverride(String organisationIdOverride) {
		this.organisationIdOverride = organisationIdOverride;
	}

	private static Logger logger = Logger.getLogger(
		AbstractServiceRequestController.class);

	/**
	 * organisationIdOverride object
	 */
	private String organisationIdOverride = null;

	@Autowired
	@Qualifier("dxpServiceRequestService")
	private ServiceRequestService serviceRequestService;

}