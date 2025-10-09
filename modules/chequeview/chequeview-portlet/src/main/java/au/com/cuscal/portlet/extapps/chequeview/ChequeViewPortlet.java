package au.com.cuscal.portlet.extapps.chequeview;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;

import com.cuscal.chequeview.TokenString;
import com.cuscal.common.constants.AttributeField;
import com.cuscal.common.ldap.beans.LDAPQuery;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.IOException;
import java.io.InputStream;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Properties;

import javax.portlet.*;

public class ChequeViewPortlet extends GenericPortlet {

	public ChequeViewPortlet() {
		this.url = null;
		this.factoryContext = null;
		this.ldapProviderUrl = null;
		this.ldapProviderVersion = null;
		this.ldapBase = null;
	}

	public void doView(
			final RenderRequest renderRequest,
			final RenderResponse renderResponse)
		throws IOException, PortletException {

		this.include(this.viewJSP, renderRequest, renderResponse);
	}

	public void init() {
		this.viewJSP = this.getInitParameter("view-jsp");
		this.loadConfiguration();
	}

	public void loadConfiguration() {
		InputStream iStream = null;

		try {
			iStream = CuscalSharedPropsUtil.getResourceStream(
				getClass(), "chequeview.properties");
			Properties props = new Properties();

			props.load(iStream);
			this.url = props.getProperty("url");
			this.factoryContext = props.getProperty("ldap.factory.context");
			this.ldapProviderUrl = props.getProperty("ldap.provider.url");
			this.ldapProviderVersion = props.getProperty(
				"ldap.provider.version");
			this.ldapBase = props.getProperty("ldap.base");

			log.info("EasyConf chequeview properties load complete!");
		}
		catch (Exception e) {
			log.error(
				"Could not load chequeview properties: " + e.getMessage(), e);
		}
	}

	public void processAction(
			final ActionRequest request, final ActionResponse response)
		throws IOException, PortletException {

		log.info("processAction.. start");

		try {
			final Hashtable ldapQueryProps = new Hashtable();
			ldapQueryProps.put(
				"java.naming.factory.initial", this.factoryContext);
			ldapQueryProps.put(
				"java.naming.provider.url", this.ldapProviderUrl);
			ldapQueryProps.put(
				"java.naming.ldap.version", this.ldapProviderVersion);
			final Calendar cal = Calendar.getInstance();
			String day = new StringBuilder(
				String.valueOf(cal.get(5))
			).toString();

			if (day.length() == 1) {
				day = "0" + day;
			}

			String month = new StringBuilder(
				String.valueOf(cal.get(2) + 1)
			).toString();

			if (month.length() == 1) {
				month = "0" + month;
			}

			final String dateString = String.valueOf(cal.get(1)) + month + day;
			final User user = PortalUtil.getUser(request);
			final String userId = user.getScreenName();
			final String[] filter = {AttributeField.UID.getKey(), userId};
			final LDAPQuery ldapQuery = new LDAPQuery(ldapQueryProps);
			ldapQuery.setFilter(filter);
			log.info("About to call ldap...");
			ldapQuery.query(
				this.ldapBase, ldapQuery.getFilters(),
				AttributeField.getChequeViewAttributeNames());
			log.info("ldap call complete");
			final String bsb = ldapQuery.getBSB();
			final String uid = ldapQuery.getUserID();
			final String gn = ldapQuery.getGivenName();
			final String sn = ldapQuery.getSurname();
			final String ou = ldapQuery.getOrganisationalUnit();
			final String ph = ldapQuery.getTelephoneNumber();
			final String em = ldapQuery.getMail();

			if (bsb != null) {
				String tokenStr =
					"org=" + bsb + "&user=" + userId + "&date=" + dateString;

				final TokenString chqViewUser = new TokenString(tokenStr);
				tokenStr = chqViewUser.encode();
				final String redirectUrl =
					String.valueOf(this.url) + "?" + tokenStr;
				log.info("Redirecting to " + redirectUrl);
				response.sendRedirect(
					String.valueOf(this.url) + "?" + tokenStr);
			}
			else {
				response.setPortletMode(PortletMode.VIEW);
			}
		}
		catch (Exception e) {
			log.error("Error processing action: " + e.getMessage(), e);
			response.setPortletMode(PortletMode.VIEW);
		}

		log.info("processAction.. end");
	}

	protected void include(
			final String path, final RenderRequest renderRequest,
			final RenderResponse renderResponse)
		throws IOException, PortletException {

		final PortletRequestDispatcher portletRequestDispatcher =
			this.getPortletContext(
			).getRequestDispatcher(
				path
			);

		if (portletRequestDispatcher == null) {
			log.error(String.valueOf(path) + " is not a valid include");
		}
		else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	protected String viewJSP;

	private static Log log;

	static {
		log = LogFactoryUtil.getLog(ChequeViewPortlet.class);
	}

	private String factoryContext;
	private String ldapBase;
	private String ldapProviderUrl;
	private String ldapProviderVersion;
	private final transient String propertyConfiguration = "chequeview";
	private String url;

}