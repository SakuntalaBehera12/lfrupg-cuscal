package au.com.cuscal.sso.saml.token.provider.impl;

import au.com.cuscal.connect.service.saml.v1_0.GenerateSamlTokenRequest;
import au.com.cuscal.connect.service.saml.v1_0.GenerateSamlTokenResponse;
import au.com.cuscal.connect.service.saml.v1_0.SamlAttribute;
import au.com.cuscal.connect.service.saml.v1_0.SamlWebServicesPortProxy;
import au.com.cuscal.framework.webservices.transaction.Entity;
import au.com.cuscal.sso.saml.SsoSamlPortlet;
import au.com.cuscal.sso.saml.token.provider.AbstractSamlProvider;
import au.com.cuscal.sso.saml.token.provider.ISamlProvider;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

public class StandardSamlProvider
	extends AbstractSamlProvider implements ISamlProvider {

	public String buildSamlToken(
			Properties properties, User impersonatingUser,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		logger.info(
			"buildSamlToken - start, " +
				this.getClass(
				).getName());

		try {
			boolean encryptToken = false;
			String token = null;

			String url = properties.getProperty(
				"webservice.url", ""
			).trim();
			String samlConfigKey = properties.getProperty(
				"webservice.saml.config", "default"
			).trim();
			logger.info("buildSamlToken - url=" + url);
			logger.info("buildSamlToken - samlConfigKey=" + samlConfigKey);

			User liferayUser = null;

			if (impersonatingUser != null) {
				liferayUser = impersonatingUser;
			}
			else {
				liferayUser = PortalUtil.getUser(renderRequest);
			}

			String screenName = liferayUser.getScreenName();
			Long userId = liferayUser.getUserId();
			logger.debug(
				"buildSamlToken - nameId (liferay screenName) / userId=" +
					screenName + "/" + userId);

			List<Organization> organizations =
				OrganizationLocalServiceUtil.getUserOrganizations(userId);

			if (organizations.size() != 1) {
				logger.warn(
					"buildSamlToken - " + screenName + " belows to " +
						organizations.size() +
							", system only supports belonging to a single organisation.");

				return token;
			}

			Organization liferayOrganisation = organizations.get(0);

			// get the liferay organisation and lookup the orgshortname from CERN

			Entity cernEntity = loadCernOrganisation(
				properties, impersonatingUser, renderRequest, renderResponse);

			String samlOrgName = cernEntity.getShortName();

			logger.debug("buildSamlToken - organisation=" + samlOrgName);

			SamlWebServicesPortProxy samlProxy = new SamlWebServicesPortProxy();

			if ((url != null) &&
				(url.trim(
				).length() > 0)) {

				samlProxy.setEndpoint(url);
			}
			//			screenName = "samltest";
			GenerateSamlTokenRequest samlRequest =
				new GenerateSamlTokenRequest();

			samlRequest.setSamlConfigKey(samlConfigKey);
			samlRequest.setEncryptToken(encryptToken);
			samlRequest.setNameId(screenName);

			List<SamlAttribute> samlAttributes = new ArrayList<>();

			buildSamlAttributes(
				properties, liferayUser, liferayOrganisation, cernEntity,
				samlAttributes);

			// only call if there are extra attributes to add

			SamlAttribute[] attributeArray = samlAttributes.toArray(
				new SamlAttribute[0]);

			if ((attributeArray != null) && (attributeArray.length > 0)) {
				samlRequest.setSamlAttributes(attributeArray);
			}

			GenerateSamlTokenResponse samlResponse =
				samlProxy.generateSamlToken(samlRequest);

			token = samlResponse.getToken();

			logger.debug("buildSamlToken - token=" + token);

			return token;
		}
		finally {
			logger.info("buildSamlToken - end");
		}
	}

	/* (non-Javadoc)
	 * @see au.com.cuscal.sso.saml.token.provider.liferay.ISamlProvider#buildSamlToken(Properties, RenderRequest, RenderResponse)
	 */
	//	@Override
	private static final Logger logger = Logger.getLogger(SsoSamlPortlet.class);

}