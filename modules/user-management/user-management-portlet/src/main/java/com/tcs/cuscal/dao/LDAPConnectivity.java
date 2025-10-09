
package com.tcs.cuscal.dao;

import com.germinus.easyconf.ComponentConfiguration;
import com.germinus.easyconf.ComponentProperties;
import com.germinus.easyconf.EasyConf;

import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.InvalidAttributeValueException;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LDAPConnectivity {

	static String uid;
	static String searchBase = "dc=cuscal, dc= com";
	static ComponentProperties props;
	static DirContext dirContext;
	static Properties properties = new Properties();
	static String propertyConfiguration = "ldap-config";
	String url = "";

	public static Attributes getAttribute(String userId)
		throws NamingException {

		String name =
			getSearchResult(
				"uid=" + userId, searchBase
			).getName() + "," + searchBase;

		Attributes attributes = getDirectoryContext().getAttributes(name);

		return attributes;
	}

	public static DirContext getDirectoryContext() {
		try {
			dirContext = new InitialDirContext(props.getProperties());
		}
		catch (NamingException var1) {
			var1.printStackTrace();
		}

		return dirContext;
	}

	public static boolean hasUser(String userId) throws NamingException {
		boolean userExists = false;
		uid = "uid=" + userId;
		SearchResult result = getSearchResult(uid, searchBase);

		if (result != null) {
			userExists = true;
		}

		return userExists;
	}

	public static void modifyAttribute(
			String userId, String answer1, String answer2)
		throws NamingException {

		String name =
			getSearchResult(
				"uid=" + userId, searchBase
			).getName() + "," + searchBase;
		ModificationItem[] mods = {
			new ModificationItem(
				2, new BasicAttribute("secretansw1 ", answer1)),
			new ModificationItem(2, new BasicAttribute("secretansw2 ", answer2))
		};

		try {
			getDirectoryContext().modifyAttributes(name, mods);
		}
		catch (NamingException var6) {
			var6.printStackTrace();
		}
	}

	public static boolean modifyAttributesForUserDetails(
			String userId, String firstName, String secondName, String email)
		throws NamingException {

		String name =
			getSearchResult(
				"uid=" + userId, searchBase
			).getName() + "," + searchBase;
		ModificationItem[] mods = {
			new ModificationItem(2, new BasicAttribute("givenName", firstName)),
			new ModificationItem(2, new BasicAttribute("sn", secondName)),
			new ModificationItem(2, new BasicAttribute("mail", email)),
			new ModificationItem(
				2, new BasicAttribute("telephoneNumber", email))
		};

		try {
			getDirectoryContext().modifyAttributes(name, mods);

			return true;
		}
		catch (NamingException var7) {
			var7.printStackTrace();

			return false;
		}
	}

	public static boolean modifyPasswordAttributeForForgotPassword(
			String userId, String Password)
		throws NamingException {

		boolean modified = false;
		String name =
			getSearchResult(
				"uid=" + userId, searchBase
			).getName() + "," + searchBase;
		ModificationItem[] mods = {
			new ModificationItem(
				2, new BasicAttribute("userPassword", Password))
		};

		try {
			getDirectoryContext().modifyAttributes(name, mods);
			modified = true;
		}
		catch (InvalidAttributeValueException var6) {
			System.out.println("Invalid attribute value exception");
			modified = false;
		}
		catch (NamingException var7) {
			modified = false;
			var7.printStackTrace();
		}

		System.out.println("modified is " + modified);

		return modified;
	}

	public static boolean verifyPassword(String userDN, String password) {
		String newDN = "";

		try {
			newDN =
				getSearchResult(
					"uid=" + userDN, searchBase
				).getName() + "," + searchBase;
		}
		catch (Exception var9) {
			var9.printStackTrace();
		}

		ComponentConfiguration conf = EasyConf.getConfiguration(
			propertyConfiguration);

		ComponentProperties props = conf.getProperties();

		props.setProperty(
			"java.naming.factory.initial",
			props.getProperty("initialContextFactory"));
		props.setProperty(
			"java.naming.provider.url", props.getProperty("providerURL"));
		props.setProperty(
			"java.naming.factory.url.pkgs",
			props.getProperty("urlPackagePrefix"));
		props.setProperty(
			"java.naming.referral", props.getProperty("referral"));
		props.setProperty(
			"java.naming.security.authentication",
			props.getProperty("securityAuthentication"));
		props.setProperty("java.naming.security.principal", newDN);
		props.setProperty("java.naming.security.credentials", password);

		try {
			new InitialDirContext(props.getProperties());

			return true;
		}
		catch (AuthenticationException var7) {
			var7.printStackTrace();

			return false;
		}
		catch (NamingException var8) {
			var8.printStackTrace();

			return false;
		}
	}

	public LDAPConnectivity() {
	}

	public String getEnvironment() {
		String env = properties.getProperty("ENV");

		return env;
	}

	private static final SearchResult getSearchResult(
			String searchFilter, String searchBase)
		throws NamingException {

		SearchResult searchResult = null;
		SearchControls constraints = new SearchControls();

		constraints.setSearchScope(2);
		dirContext = getDirectoryContext();
		NamingEnumeration<SearchResult> searchResults = dirContext.search(
			searchBase, searchFilter, constraints);

		if ((searchResults != null) && searchResults.hasMore()) {
			searchResult = (SearchResult)searchResults.next();
		}

		return searchResult;
	}

	private static Log log;

	static {
		ComponentConfiguration conf = EasyConf.getConfiguration(
			propertyConfiguration);

		props = conf.getProperties();
		props.setProperty(
			"java.naming.factory.initial",
			props.getProperty("initialContextFactory"));
		props.setProperty(
			"java.naming.provider.url", props.getProperty("providerURL"));
		props.setProperty(
			"java.naming.factory.url.pkgs",
			props.getProperty("urlPackagePrefix"));
		props.setProperty(
			"java.naming.referral", props.getProperty("referral"));
		props.setProperty(
			"java.naming.security.authentication",
			props.getProperty("securityAuthentication"));
		props.setProperty(
			"java.naming.security.principal",
			props.getProperty("securityPrincipal"));
		props.setProperty(
			"java.naming.security.credentials",
			props.getProperty("securityCredentials"));
		log = LogFactory.getLog(LDAPConnectivity.class);
	}

}