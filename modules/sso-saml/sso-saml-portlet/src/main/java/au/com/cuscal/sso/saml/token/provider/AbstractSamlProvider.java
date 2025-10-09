package au.com.cuscal.sso.saml.token.provider;

import au.com.cuscal.connect.service.saml.v1_0.SamlAttribute;
import au.com.cuscal.framework.webservices.pooling.WebServicePooling;
import au.com.cuscal.framework.webservices.transaction.Entity;
import au.com.cuscal.framework.webservices.transaction.EntityAttribute;
import au.com.cuscal.framework.webservices.transaction.EntityServicePortType;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdRequestType;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdResponseType;
import au.com.cuscal.framework.webservices.transaction.StandardHeader;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.log4j.Logger;

public abstract class AbstractSamlProvider implements ISamlProvider {

	public static final String CERN_ORGANISATION = "[CERN.org.";

	public static final String CERN_ORGANISATION_ATTRIBUTE_TYPE_ID =
		"[CERN.org.attributeTypeId=";

	public static final String LIFERAY_ORGANISATION = "[LIFERAY.org.";

	public static final String LIFERAY_USER = "[LIFERAY.user.";

	public static final String SAML_ATTRIBUTE_PREFIX = "saml.attribute.";

	public static final String TIME = "[XMLTIME/";

	public static final String TIME_GMT = "[XMLTIME/GMT";

	public static final String TIME_LOCAL = "[XMLTIME/LOCAL";

	public AbstractSamlProvider() {
	}

	public void buildSamlAttributes(
			Properties properties, User liferayUser,
			Organization liferayOrganisation, Entity cernEntity,
			List<SamlAttribute> samlAttributes)
		throws Exception {

		Enumeration<Object> keys = properties.keys();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement(
			).toString();

			String value = properties.getProperty(key);

			if (value == null) {
				continue;	// sanity check
			}

			// the key must start with the saml attribute prefix... the remainder is the attribute name

			if (!key.startsWith(SAML_ATTRIBUTE_PREFIX))

				continue;

			logger.debug(
				"buildSamlAttributes - candidate for saml property " + key +
					"=[" + value + "]");

			String samlAttributeName = key.substring(
				SAML_ATTRIBUTE_PREFIX.length());
			String samlAttributeValueMacro = value.trim();
			String samlAttributeValue = value;
			String macroPrefix = null;

			logger.debug(
				"buildSamlAttributes -     calculated SAML attributeName=[" +
					samlAttributeName + "], samlAttributeValueMacro=" +
						samlAttributeValueMacro);

			Object targetObject = null;

			if (samlAttributeValueMacro.startsWith(
					CERN_ORGANISATION_ATTRIBUTE_TYPE_ID)) {

				List<EntityAttribute> entityAttributes =
					cernEntity.getEntityArrtibutes();

				String _attributeTypeId = samlAttributeValueMacro.substring(
					CERN_ORGANISATION_ATTRIBUTE_TYPE_ID.length(),
					samlAttributeValueMacro.length() - 1
				).trim();

				int attributeTypeId = Integer.parseInt(_attributeTypeId);
				logger.debug(
					"buildSamlAttributes -     attributeName=[" +
						samlAttributeName + "], CERN Entity.attributeTypeId=[" +
							attributeTypeId + "]");

				for (EntityAttribute entityAttribute : entityAttributes) {
					int typeId = entityAttribute.getAttributeType(
					).getAttributeTypeId();

					if (typeId == attributeTypeId) {
						samlAttributeValue =
							entityAttribute.getAttributeTypeValue();

						break;
					}
				}

				if (samlAttributeValue == null) {
					logger.warn(
						"buildSamlAttributes -     attributeName=[" +
							samlAttributeName +
								"], CERN Entity.attributeTypeId=[" +
									attributeTypeId +
										"] - no attribute found for this attributeTypeId.");
				}
				else {
					logger.debug(
						"buildSamlAttributes -     attributeName=[" +
							samlAttributeName +
								"], CERN Entity.attributeTypeId=[" +
									attributeTypeId +
										"], found attributeValue=" +
											samlAttributeValue);
				}
			}
			else if (samlAttributeValueMacro.startsWith(CERN_ORGANISATION)) {
				macroPrefix = CERN_ORGANISATION;
				targetObject = cernEntity;
			}
			else if (samlAttributeValueMacro.startsWith(LIFERAY_ORGANISATION)) {
				macroPrefix = LIFERAY_ORGANISATION;
				targetObject = liferayOrganisation;
			}
			else if (samlAttributeValueMacro.startsWith(LIFERAY_USER)) {
				macroPrefix = LIFERAY_USER;
				targetObject = liferayUser;
			}
			else if (samlAttributeValueMacro.startsWith(TIME)) {
				logger.debug("buildSamlAttributes - calculating xml time");

				targetObject = null;
				Calendar now = Calendar.getInstance();
				int nowOffsetSeconds = 0;
				DateFormat xmlDateTimeFormatter = new SimpleDateFormat(
					"yyyy-MM-DD HH:mm:ss"); 	// note the whitespace between the date and time portion still needs to be replaced by a 'T'

				if (samlAttributeValueMacro.startsWith(TIME_GMT)) {
					logger.debug(
						"buildSamlAttributes -     setting GMT timezone");

					xmlDateTimeFormatter.setTimeZone(
						TimeZone.getTimeZone("GMT"));
					String s = samlAttributeValueMacro.substring(
						TIME_GMT.length(), samlAttributeValueMacro.length() - 1
					).trim();

					nowOffsetSeconds = Integer.parseInt(s);
				}
				else if (samlAttributeValueMacro.startsWith(TIME_LOCAL)) {
					logger.debug(
						"buildSamlAttributes -     using local/default timezone");

					String s = samlAttributeValueMacro.substring(
						TIME_LOCAL.length(),
						samlAttributeValueMacro.length() - 1
					).trim();

					nowOffsetSeconds = Integer.parseInt(s);
				}

				logger.debug(
					"buildSamlAttributes -     time offset=" +
						nowOffsetSeconds + " seconds");
				now.add(Calendar.SECOND, nowOffsetSeconds);
				Date date = now.getTime();

				String xmlDateTime = xmlDateTimeFormatter.format(
					date
				).replace(
					' ', 'T'
				);

				samlAttributeValue = xmlDateTime;
			}
			else {
				targetObject = null;
				samlAttributeValue = value;
			}

			logger.debug(
				"buildSamlAttributes -     using SAML macro processor for [" +
					macroPrefix + "]");

			if (targetObject != null) {
				Class<?> targetObjectClass = targetObject.getClass();

				logger.debug(
					"buildSamlAttributes -     targetObject for macro processor=" +
						targetObjectClass.getName());

				String methodName = samlAttributeValueMacro.substring(
					macroPrefix.length(), samlAttributeValueMacro.length() - 1
				).trim();

				logger.debug(
					"buildSamlAttributes -     invoking method=" + methodName);

				Object methodResult = MethodUtils.invokeMethod(
					targetObject, methodName);

				logger.debug(
					"buildSamlAttributes -     method invocation result=" +
						methodResult);

				if (methodResult != null) {
					samlAttributeValue = methodResult.toString();
				}
			}

			if (samlAttributeValue != null) {
				SamlAttribute samlAttribute = new SamlAttribute(
					samlAttributeName, samlAttributeValue);

				samlAttributes.add(samlAttribute);
				logger.debug(
					"buildSamlAttributes - added new SAML attribute [" +
						samlAttributeName + "=" + samlAttributeValue + "]");
			}
			else {
				logger.debug(
					"buildSamlAttributes - SAML attribute [" +
						samlAttributeName + "=" + samlAttributeValue +
							"] ignored");
			}
		}
	}

	public Entity loadCernOrganisation(
			Properties properties, User impersonatingUser,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		Entity entity = null;
		FindEntitiesByAttributeWithIdRequestType request =
			new FindEntitiesByAttributeWithIdRequestType();
		StandardHeader header = new StandardHeader();

		request.setHeader(header);

		String _attributeTypeId = properties.getProperty(
			"cern.attributeTypeId", "0");
		String url = properties.getProperty(
			"cern.url",
			"https://services.cuscal-d.com.au:8080/transaction-services/EntityService?wsdl");
		String origin = properties.getProperty("cern.origin", "PORTAL");
		String _organisationIdOverride = properties.getProperty(
			"cern.organisation.override", "");

		int attributeTypeId = Integer.parseInt(_attributeTypeId);

		User user = null;

		if (impersonatingUser != null) {
			user = impersonatingUser;
		}
		else {
			user = PortalUtil.getUser(renderRequest);
		}

		String screenName = user.getScreenName();
		Long userId = user.getUserId();

		List<Organization> organizations =
			OrganizationLocalServiceUtil.getUserOrganizations(userId);

		if (organizations.size() > 1) {
			logger.warn(
				"loadCernOrganisation - " + screenName + " belows to " +
					organizations.size() +
						", system only supports belonging to a single organisation.");

			return null;
		}

		Organization organization = organizations.get(0);

		String organisationId = "".equals(_organisationIdOverride) ?
			String.valueOf(organization.getOrganizationId()) :
				_organisationIdOverride;

		header.setOrigin(origin);
		header.setUserId(screenName);
		header.setUserOrgId(organisationId);
		header.setUserOrgName(organization.getName());

		request.setAttributeTypeId(attributeTypeId);
		request.setAttributeValue(organisationId);

		WebServicePooling wsp = new WebServicePooling();

		wsp.setEntityServicesUrl(url);
		EntityServicePortType service = wsp.newEntityService();

		FindEntitiesByAttributeWithIdResponseType response =
			service.findEntitiesByAttributeWithId(request);

		List<Entity> entities = response.getEntities();

		switch (entities.size()) {
			case 0:
				logger.warn(
					"loadCernOrganisation - No CERN entity found for attributeTypeId=" +
						attributeTypeId + ", attributeValue=" + organisationId);

				break;
			case 1:
				entity = entities.get(0);

				break;
			default:
				entity = entities.get(0);
				logger.warn(
					"loadCernOrganisation - Multiple CERN entities found for attributeTypeId=" +
						attributeTypeId + ", attributeValue=" + organisationId +
							", found=" + entities.size() +
								", using first entity=" +
									entity.getShortName() +
										", returned entities are:");

				for (Entity _entity : entities) {
					logger.warn(
						"loadCernOrganisation -    Found entity: " +
							_entity.getShortName());
				}
		}

		return entity;
	}

	private static final Logger logger = Logger.getLogger(
		AbstractSamlProvider.class);

}