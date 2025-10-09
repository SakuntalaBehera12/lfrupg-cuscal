package com.tcs.cuscal.dao;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;

import java.io.InputStream;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LDAPUtil {

	public static LDAPUtil getInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		}

		INSTANCE = new LDAPUtil();

		return INSTANCE;
	}

	public Attributes getAttribute(String userId) throws NamingException {
		String name = this.getSearchResult(
			"uid=" + userId, this.searchBase
		).getName();
		DirContext contx = null;
		Attributes result = null;

		try {
			contx = this.getDirContext();
			result = contx.getAttributes(name + "," + this.searchBase);
		}
		finally {
			contx.close();
		}

		return result;
	}

	public SearchResult getSearchResult(String searchFilter, String searchBase)
		throws NamingException {

		SearchResult searchResult = null;
		DirContext contx = null;

		try {
			SearchControls constraints = new SearchControls();

			constraints.setSearchScope(2);
			logger.debug("[getSearchResult] search filter is: " + searchFilter);
			logger.debug("[getSearchResult] search base is: " + searchBase);
			contx = this.getDirContext();
			logger.debug("[getSearchResult] context is: " + contx);
			NamingEnumeration<SearchResult> searchResults = contx.search(
				searchBase, searchFilter, constraints);

			if ((searchResults != null) && searchResults.hasMore()) {
				searchResult = (SearchResult)searchResults.next();
			}
		}
		catch (Exception var10) {
			var10.printStackTrace();
		}
		finally {
			logger.debug("[getSearchResult] (finally) context is: " + contx);
			contx.close();
		}

		return searchResult;
	}

	public void modifyAttribute(String userId, String answer1, String answer2)
		throws NamingException {

		String name = this.getSearchResult(
			"uid=" + userId, this.searchBase
		).getName();
		DirContext contx = null;
		ModificationItem[] mods = {
			new ModificationItem(2, new BasicAttribute("secretansw1", answer1)),
			new ModificationItem(2, new BasicAttribute("secretansw2", answer2))
		};

		try {
			contx = this.getDirContext();
			contx.modifyAttributes(name + "," + this.searchBase, mods);
		}
		finally {
			contx.close();
		}
	}

	public boolean modifyAttributesForUserDetails(
			String userId, String firstName, String secondName, String email,
			String phoneNumber)
		throws NamingException {

		boolean result = false;

		if (this.updateUserDetailsInLDAP) {
			String name = this.getSearchResult(
				"uid=" + userId, this.searchBase
			).getName();
			DirContext contx = null;
			ModificationItem[] mods = {
				new ModificationItem(
					2, new BasicAttribute("givenName", firstName)),
				new ModificationItem(2, new BasicAttribute("sn", secondName)),
				new ModificationItem(2, new BasicAttribute("mail", email)),
				new ModificationItem(
					2, new BasicAttribute("telephoneNumber", phoneNumber))
			};

			try {
				contx = this.getDirContext();
				contx.modifyAttributes(name + "," + this.searchBase, mods);
				result = true;
			}
			finally {
				contx.close();
			}
		}

		return result;
	}

	public boolean modifyPasswordAttributeForForgotPassword(
			String userId, String Password)
		throws NamingException {

		boolean result = false;
		String name = this.getSearchResult(
			"uid=" + userId, this.searchBase
		).getName();
		DirContext contx = null;
		ModificationItem[] mods = {
			new ModificationItem(
				2, new BasicAttribute("userPassword", Password))
		};

		try {
			contx = this.getDirContext();
			contx.modifyAttributes(name + "," + this.searchBase, mods);
			result = true;
		}
		finally {
			contx.close();
		}

		return result;
	}

	public String toString() {
		return "LDAPUtil [InitialDirectoryContext=" +
			this.InitialDirectoryContext + ", ProviderURL=" + this.ProviderURL +
				", URLPackagePrefix=" + this.URLPackagePrefix + ", Referral=" +
					this.Referral + ", SecurityAuthentication=" +
						this.SecurityAuthentication + ", SecurityPrincipal=" +
							this.SecurityPrincipal + ", searchBase=" +
								this.searchBase + "] ";
	}

	public boolean verifyPassword(String userDN, String password) {
		boolean result = false;

		try {
			String verifiedDN =
				this.getSearchResult(
					"uid=" + userDN, this.searchBase
				).getName() + "," + this.searchBase;
			logger.debug("[verifyPassword] userDN is: " + userDN);
			logger.debug("[verifyPassword] password is: " + password);
			logger.debug("[verifyPassword] verifiedDN is: " + verifiedDN);
			this.getDirContext(verifiedDN, password);
			result = true;
		}
		catch (AuthenticationException var6) {
			result = false;
		}
		catch (NamingException var7) {
			result = false;
		}

		return result;
	}

	public final String KEY_CONTEXT_CONNECTION_POOLING =
		"com.sun.jndi.ldap.connect.pool";

	public final String KEY_INITIAL_CONTEXT_FACTORY = "initialContextFactory";

	public final String KEY_PROVIDER_URL = "providerURL";

	public final String KEY_REFERRAL = "referral";

	public final String KEY_SEARCH_BASE = "searchBase";

	public final String KEY_SECURITY_AUTHENTICATION = "securityAuthentication";

	public final String KEY_SECURITY_CREDENTIALS = "securityCredentials";

	public final String KEY_SECURITY_PRINCIPAL = "securityPrincipal";

	public final String KEY_UPDATE_USER_DETAILS_IN_LDAP =
		"update.user_details_in_ldap";

	public final String KEY_URL_PKG_PREFIX = "urlPackagePrefix";

	private LDAPUtil() {
		InputStream iStream = null;

		try {
			iStream = CuscalSharedPropsUtil.getResourceStream(
				getClass(), "chequeview.properties");
			Properties props = new Properties();

			props.load(iStream);

			this.InitialDirectoryContext = props.getProperty(
				"initialContextFactory");
			this.pooling = Boolean.parseBoolean(
				props.getProperty("com.sun.jndi.ldap.connect.pool"));
			this.ProviderURL = props.getProperty("providerURL");
			this.URLPackagePrefix = props.getProperty("urlPackagePrefix");
			this.Referral = props.getProperty("referral");
			this.SecurityAuthentication = props.getProperty(
				"securityAuthentication");
			this.SecurityPrincipal = props.getProperty("securityPrincipal");
			this.SecurityCredentials = props.getProperty("securityCredentials");
			this.searchBase = props.getProperty("searchBase");
			this.updateUserDetailsInLDAP = Boolean.parseBoolean(
				props.getProperty("update.user_details_in_ldap"));
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private DirContext getDirContext() throws NamingException {
		return this.getDirContext(
			this.SecurityPrincipal, this.SecurityCredentials);
	}

	private DirContext getDirContext(String principal, String credentials)
		throws NamingException {

		Hashtable<String, String> env = new Hashtable(8);

		env.put("java.naming.factory.initial", this.InitialDirectoryContext);
		env.put(
			"com.sun.jndi.ldap.connect.pool", this.pooling ? "true" : "false");
		env.put("java.naming.provider.url", this.ProviderURL);
		env.put("java.naming.factory.url.pkgs", this.URLPackagePrefix);
		env.put("java.naming.referral", this.Referral);
		env.put(
			"java.naming.security.authentication", this.SecurityAuthentication);
		env.put("java.naming.security.principal", principal);
		env.put("java.naming.security.credentials", credentials);

		return new InitialDirContext(env);
	}

	private static LDAPUtil INSTANCE = null;

	String propertyConfiguration = "ldap-config";

	private static Logger logger = LoggerFactory.getLogger(LDAPUtil.class);

	private String InitialDirectoryContext = null;
	private boolean pooling = true;
	private String ProviderURL = null;
	private String Referral = null;
	private String searchBase = null;
	private String SecurityAuthentication = null;
	private String SecurityCredentials = null;
	private String SecurityPrincipal = null;
	private boolean updateUserDetailsInLDAP = true;
	private String URLPackagePrefix = null;

}