package au.com.cuscal.connect.ldap.dao;

import au.com.cuscal.connect.ldap.domain.Person;

public interface PersonDao {

	Person getPersonByUserId(final String userId);

}