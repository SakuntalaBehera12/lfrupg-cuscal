package au.com.cuscal.sso.saml.token.provider;

import au.com.cuscal.connect.service.saml.v1_0.SamlAttribute;
import au.com.cuscal.framework.webservices.transaction.Entity;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;

import java.util.List;
import java.util.Properties;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public interface ISamlProvider {

	void buildSamlAttributes(
			Properties properties, User liferayUser,
			Organization liferayOrganisation, Entity cernEntity,
			List<SamlAttribute> samlAttributes)
		throws Exception;

	String buildSamlToken(
			Properties properties, User impersonatingUser,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception;

	Entity loadCernOrganisation(
			Properties properties, User impersonatingUser,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception;

}