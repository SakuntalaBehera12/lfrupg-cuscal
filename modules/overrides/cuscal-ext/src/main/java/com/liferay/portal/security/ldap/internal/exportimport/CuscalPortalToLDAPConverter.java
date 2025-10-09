package com.liferay.portal.security.ldap.internal.exportimport;

import static com.liferay.portal.security.ldap.UserConverterKeys.*;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.exportimport.UserOperation;
import com.liferay.portal.security.ldap.SafeLdapName;
import com.liferay.portal.security.ldap.SafePortalLDAP;
import com.liferay.portal.security.ldap.exportimport.Modifications;
import com.liferay.portal.security.ldap.exportimport.PortalToLDAPConverter;
import com.liferay.portal.security.ldap.internal.util.CuscalLDAPSyncUtil;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Binding;
import javax.naming.directory.Attributes;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

@Component(
	immediate = true, property = "service.ranking:Integer=100",
	service = PortalToLDAPConverter.class
)
public class CuscalPortalToLDAPConverter implements PortalToLDAPConverter {

	@Deprecated
	@Override
	public String getGroupDNName(
			long ldapServerId, UserGroup userGroup, Properties groupMappings)
		throws Exception {

		return _portalToLDAPConverter.getGroupDNName(
			ldapServerId, userGroup, groupMappings);
	}

	@Override
	public SafeLdapName getGroupSafeLdapName(
			long ldapServerId, UserGroup userGroup, Properties groupMappings)
		throws Exception {

		return _portalToLDAPConverter.getGroupSafeLdapName(
			ldapServerId, userGroup, groupMappings);
	}

	@Override
	public Modifications getLDAPContactModifications(
			Contact contact, Map<String, Serializable> contactExpandoAttributes,
			Properties contactMappings, Properties contactExpandoMappings)
		throws Exception {

		return _portalToLDAPConverter.getLDAPContactModifications(
			contact, contactExpandoAttributes, contactMappings,
			contactExpandoMappings);
	}

	@Override
	public Attributes getLDAPGroupAttributes(
			long ldapServerId, UserGroup userGroup, User user,
			Properties groupMappings, Properties userMappings)
		throws Exception {

		return _portalToLDAPConverter.getLDAPGroupAttributes(
			ldapServerId, userGroup, user, groupMappings, userMappings);
	}

	@Override
	public Modifications getLDAPGroupModifications(
			long ldapServerId, UserGroup userGroup, User user,
			Properties groupMappings, Properties userMappings,
			UserOperation userOperation)
		throws Exception {

		return _portalToLDAPConverter.getLDAPGroupModifications(
			ldapServerId, userGroup, user, groupMappings, userMappings,
			userOperation);
	}

	@Override
	public Attributes getLDAPUserAttributes(
			long ldapServerId, User user, Properties userMappings)
		throws Exception {

		return _portalToLDAPConverter.getLDAPUserAttributes(
			ldapServerId, user, userMappings);
	}

	@Override
	public Modifications getLDAPUserGroupModifications(
			long ldapServerId, List<UserGroup> userGroups, User user,
			Properties userMappings)
		throws Exception {

		return _portalToLDAPConverter.getLDAPUserGroupModifications(
			ldapServerId, userGroups, user, userMappings);
	}

	@Override
	public Modifications getLDAPUserModifications(
			User user, Map<String, Serializable> userExpandoAttributes,
			Properties userMappings, Properties userExpandoMappings)
		throws Exception {

		return _portalToLDAPConverter.getLDAPUserModifications(
			user, userExpandoAttributes, userMappings, userExpandoMappings);
	}

	@Deprecated
	@Override
	public String getUserDNName(
			long ldapServerId, User user, Properties userMappings)
		throws Exception {

		Binding userBinding = _safePortalLDAP.getUser(
			ldapServerId, user.getCompanyId(), user.getScreenName(),
			user.getEmailAddress());

		if (userBinding != null) {
			return userBinding.getNameInNamespace();
		}

		StringBundler sb = new StringBundler();

		sb.append(
			GetterUtil.getString(userMappings.getProperty(SCREEN_NAME), "cn"));
		sb.append(StringPool.EQUAL);
		sb.append(BeanPropertiesUtil.getStringSilent(user, SCREEN_NAME));
		sb.append(StringPool.COMMA);

		if (!user.getOrganizations(
			).isEmpty()) {

			final Organization userFirstOrg = user.getOrganizations(
			).get(
				0
			);
			final String UserLDAPDN = CuscalLDAPSyncUtil.getOrgCustomValue(
				user, userFirstOrg, "LDAPUserDN");

			if (_log.isDebugEnabled()) {
				_log.debug("LDAPUSerDN for this org is : " + UserLDAPDN);
			}

			if (Validator.isNotNull(UserLDAPDN)) {
				sb.append(UserLDAPDN);
				sb.append(StringPool.COMMA);
			}
		}
		else if (_log.isDebugEnabled()) {
			_log.debug(
				"getUserDNName: Organization for user can't be empty 19 ");
		}

		sb.append(
			_safePortalLDAP.getUsersDNSafeLdapName(
				ldapServerId, user.getCompanyId()));

		return sb.toString();
	}

	@Override
	public SafeLdapName getUserSafeLdapName(
			long ldapServerId, User user, Properties userMappings)
		throws Exception {

		return _portalToLDAPConverter.getUserSafeLdapName(
			ldapServerId, user, userMappings);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CuscalPortalToLDAPConverter.class);

	@Reference(
		target = "(component.name=com.liferay.portal.security.ldap.internal.exportimport.DefaultPortalToLDAPConverter)"
	)
	private PortalToLDAPConverter _portalToLDAPConverter;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile SafePortalLDAP _safePortalLDAP;

}