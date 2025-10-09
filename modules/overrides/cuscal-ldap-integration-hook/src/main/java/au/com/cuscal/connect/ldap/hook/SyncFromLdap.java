package au.com.cuscal.connect.ldap.hook;

import au.com.cuscal.connect.ldap.dao.PersonDao;
import au.com.cuscal.connect.ldap.domain.Person;
import au.com.cuscal.connect.util.resource.dxp.UserUtil;
import au.com.cuscal.connect.util.resource.ldap.LDAPUtil;
import au.com.cuscal.connect.util.resource.model.VsmUser;

import com.liferay.portal.kernel.concurrent.ThreadPoolExecutor;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.User;

import java.io.IOException;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SyncFromLdap extends Action {

	@Override
	public void run(
			final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse)
		throws ActionException {

		_setBeansFromApplicationContext();

		boolean debugEnabled = _LOG.isDebugEnabled();

		if (debugEnabled) {
			_LOG.debug("SyncFromLDAP.run() - start ");
		}

		long remoteUser = 0;

		try {
			remoteUser = Long.valueOf(httpServletRequest.getRemoteUser());
		}
		catch (NumberFormatException e1) {
			_LOG.warn(e1.getMessage());

			return;
		}

		if (remoteUser <= 0) {
			if (debugEnabled) {
				_LOG.debug("Not login yet ");
			}

			return;
		}

		final User user = _userUtil.getLiferayUserByUserId(remoteUser);

		if (user == null) {
			if (debugEnabled) {
				_LOG.debug("User not found " + remoteUser);
			}

			return;
		}

		if (!user.isActive()) {
			if (debugEnabled) {
				_LOG.debug(
					"***SyncFromLDAP: User is not Active. " +
						"Cleaning cookies and redirecting to hidden page called inactive***");
			}

			try {
				httpServletResponse.sendRedirect(
					"/c/portal/logout?redirect=inactivepage");
			}
			catch (IOException ioe) {
				_LOG.error(
					"Exception in ldap update for inActive User " +
						ioe.getMessage(),
					ioe);
			}

			return;
		}

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					_ldapSync(user);
				}
				catch (PortalException | SystemException e) {
					_LOG.error(
						"Error occured synching Ldap for user: " +
							user.getUserId() + e.getMessage(),
						e);
				}
				catch (Exception ne) {
					_LOG.error(
						"Error occured synching Ldap for user: " +
							user.getUserId() + ne.getMessage(),
						ne);
				}
			}

		};

		_THREAD_POOL_EXECUTOR.execute(runnable);
	}

	private void _ldapSync(final User user) throws Exception {
		Person ldapPersonRecord = null;

		try {
			ldapPersonRecord = _personDao.getPersonByUserId(user.getLogin());
		}
		catch (Exception e) {
			_LOG.error("Exception in ldap update" + e.getMessage(), e);

			return;
		}

		_LOG.debug(
			"**********SyncFromLDAP after login got triggered ******** ");

		final String targetOrganisationName =
			ldapPersonRecord.getOrganisation();

		long userId = user.getUserId();

		_LOG.debug(
			">>>>>>> User = " + userId + " / " + user.getScreenName() +
				" has orgs = " + targetOrganisationName);

		_userUtil.ensureUserIsMemberOfTargetOrganisationOnly(
			userId, targetOrganisationName);

		_LOG.debug(
			"Calling getTicketUser() to retrieve user from TKT_USER table");

		final VsmUser vsmUser = _userUtil.retrieveUserFromGetTicketUserService(
			user);

		if (null != vsmUser) {
			_LOG.debug(
				"Table user is last updated at " +
					vsmUser.getLastUpdatedDate());
			_LOG.debug(
				"Ldap user is last updated at " +
					ldapPersonRecord.getModifiedDateTime());

			boolean newerLdapPersonRecord = false;

			if (vsmUser.getLastUpdatedDate(
				).compareTo(
					ldapPersonRecord.getModifiedDateTime()
				) < 0) {

				newerLdapPersonRecord = true;
			}

			if (newerLdapPersonRecord) {
				final int updateStatusInLdap =
					_userUtil.syncUserAttributesIfChanged(
						userId, ldapPersonRecord.getFirstName(),
						ldapPersonRecord.getLastName(),
						ldapPersonRecord.getEmail(),
						ldapPersonRecord.getPhone());

				if (updateStatusInLdap != 0) {
					final User refreshedUser = _userUtil.getLiferayUserByUserId(
						userId);

					_LOG.debug("Updating User in TKT_USER table");

					_userUtil.addOrUpdateUserInGetTicketUserService(
						refreshedUser);
				}
			}
			else {
				final int updateStatusInVsm =
					_userUtil.syncUserAttributesIfChanged(
						userId, vsmUser.getFirstName(), vsmUser.getLastName(),
						vsmUser.getEmail(), vsmUser.getPhoneNo());

				if (updateStatusInVsm != 0) {
					final User refreshedUser = _userUtil.getLiferayUserByUserId(
						userId);

					_LOG.debug("Updating User in Ldap");

					Phone businessPhoneForUser =
						_userUtil.retrieveBusinessPhoneForUser(refreshedUser);

					String retrievedNumber = businessPhoneForUser != null ?
						businessPhoneForUser.getNumber() : vsmUser.getPhoneNo();

					LDAPUtil.getInstance(
					).modifyAttributesForUserDetails(
						refreshedUser.getScreenName(),
						refreshedUser.getFirstName(),
						refreshedUser.getLastName(),
						refreshedUser.getEmailAddress(), retrievedNumber
					);
				}
			}
		}
		else {
			_LOG.debug("Adding User to TKT_USER table");
			_userUtil.addOrUpdateUserInGetTicketUserService(user);
		}
	}

	private void _setBeansFromApplicationContext() {
		if ((_userUtil != null) && (_personDao != null)) {
			return;
		}

		final BeanFactory factory = new ClassPathXmlApplicationContext(
			_APPLICATION_CONTEXT_XML);
		ApplicationContext context = (ApplicationContext)factory;

		_userUtil = (UserUtil)factory.getBean("userUtil");
		_personDao = (PersonDao)factory.getBean("personDao");
		context = null;
	}

	private static final String _APPLICATION_CONTEXT_XML =
		"applicationContext.xml";

	private static final int _CORE_POOL_SIZE = 1;

	private static final int _KEEP_ALIVE_TIME_IN_SECONDS = 30;

	private static final Logger _LOG = Logger.getLogger(SyncFromLdap.class);

	private static final int _MAX_POOL_SIZE = 5;

	private static final int _MAX_QUEUE_SIZE = 200;

	private static final ThreadPoolExecutor _THREAD_POOL_EXECUTOR =
		new ThreadPoolExecutor(
			_CORE_POOL_SIZE, _MAX_POOL_SIZE, _KEEP_ALIVE_TIME_IN_SECONDS,
			TimeUnit.SECONDS, false, _MAX_QUEUE_SIZE);

	private static PersonDao _personDao = null;
	private static UserUtil _userUtil = null;

}