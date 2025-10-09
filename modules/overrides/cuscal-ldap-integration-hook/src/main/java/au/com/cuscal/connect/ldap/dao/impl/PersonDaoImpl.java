package au.com.cuscal.connect.ldap.dao.impl;

import au.com.cuscal.connect.ldap.dao.PersonDao;
import au.com.cuscal.connect.ldap.domain.Person;

import com.liferay.portal.kernel.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.naming.directory.SearchControls;

import org.apache.log4j.Logger;

import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

public class PersonDaoImpl implements PersonDao {

	public Person getPersonByUserId(final String userId) {
		LOG.debug("getPersonByUserId - start");

		if (Validator.isNull(userId)) {
			throw new IllegalArgumentException(
				"The supplied userId cannot be null or empty.");
		}

		final AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));
		filter.and(new EqualsFilter("uid", userId));
		final SearchControls controls = new SearchControls();
		controls.setSearchScope(2);
		controls.setReturningObjFlag(true);
		controls.setReturningAttributes(
			new String[] {
				"modifyTimeStamp", "uid", "givenName", "mail", "cuscalCUOrgId",
				"telephonenumber", "cn", "sn", "cuscalBSB"
			});
		final List<Person> results = this.ldapTemplate.search(
			DistinguishedName.EMPTY_PATH, filter.encode(), controls,
			this.getContextMapper());
		final int resultSetSize = results.size();

		if (resultSetSize == 0) {
			throw new RuntimeException(
				"Person record not found in directory for user: " + userId);
		}

		if (resultSetSize > 1) {
			throw new RuntimeException(
				"Unique Person record not found in directory for user: " +
					userId + ". Multiple records found.");
		}

		LOG.debug("getPersonByUserId - end");

		return results.get(0);
	}

	public void setLdapTemplate(final LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	private ContextMapper getContextMapper() {
		return new PersonContextMapper();
	}

	private static final Logger LOG = Logger.getLogger(PersonDaoImpl.class);

	private final String LDAP_OBJECTCLASS_FILTER_ATTRIBUTE = "objectclass";

	private LdapTemplate ldapTemplate;

	private static class PersonContextMapper implements ContextMapper {

		public Object mapFromContext(final Object ctx) {
			LOG.debug("mapFromContext - start");
			final DirContextAdapter context = (DirContextAdapter)ctx;
			final DistinguishedName dn = new DistinguishedName(context.getDn());
			final Person person = new Person();
			person.setUid(context.getStringAttribute("uid"));
			person.setFullName(context.getStringAttribute("cn"));
			person.setLastName(context.getStringAttribute("sn"));
			person.setCuscalBsb(context.getStringAttribute("cuscalBSB"));
			person.setCuscalCuOrgId(
				context.getStringAttribute("cuscalCUOrgId"));
			person.setPhone(context.getStringAttribute("telephonenumber"));
			person.setFirstName(context.getStringAttribute("givenName"));
			person.setEmail(context.getStringAttribute("mail"));
			final String dateTime = context.getStringAttribute(
				"modifyTimeStamp");
			final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
				"yyyyMMddHHmmss");
			DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));

			try {
				final Date modDate = DATE_TIME_FORMAT.parse(dateTime);
				person.setModifiedDateTime(modDate);
			}
			catch (ParseException e1) {
				e1.printStackTrace();
			}

			if (dn.getLdapRdn(
					0
				).getComponent(
				).getValue(
				).equals(
					"internal"
				)) {

				person.setOrganisation("Cuscal Org");
			}
			else {
				person.setOrganisation(
					dn.getLdapRdn(
						1
					).getComponent(
					).getValue());
			}

			LOG.debug("**** Mapped LDAP user *****");
			LOG.debug("dn: " + dn);
			LOG.debug("Full Name: " + person.getFullName());
			LOG.debug("First Name: " + person.getFirstName());
			LOG.debug("Last Name: " + person.getLastName());
			LOG.debug("Email: " + person.getEmail());
			LOG.debug("Cuscal BSB: " + person.getCuscalBsb());
			LOG.debug("Cuscal Org Id: " + person.getCuscalCuOrgId());
			LOG.debug("Organisation: " + person.getOrganisation());
			LOG.debug("Modified Date: " + person.getModifiedDateTime());
			LOG.debug("Phone Date: " + person.getPhone());
			LOG.debug("mapFromContext - end");

			return person;
		}

	}

}