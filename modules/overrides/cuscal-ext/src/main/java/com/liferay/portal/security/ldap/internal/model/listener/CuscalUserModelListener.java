package com.liferay.portal.security.ldap.internal.model.listener;

import static com.liferay.portal.security.ldap.internal.util.CuscalLDAPSyncUtil.*;

import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true, property = "service.ranking:Integer=100",
	service = ModelListener.class
)
public class CuscalUserModelListener extends BaseModelListener<User> {

	@Override
	public void onAfterCreate(User user) throws ModelListenerException {
		try {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"CuscalUserListener , we are here- onAfterCreate v1.0");
			}

			if (hasEmptyOrganisations(user)) {
				return;
			}

			if (hasInvalidPassword(user)) {
				return;
			}

			Organization organization = user.getOrganizations(
			).getFirst();

			updateCuscalBSB(user, organization);
			updateCuscalCUOrgId(user, organization);

			_userModelListener.onAfterCreate(user);
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Override
	public void onAfterUpdate(User originalUser, User user)
		throws ModelListenerException {

		try {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"CuscalUserListener , we are here- onAfterUpdate v1.0");
			}

			if (hasEmptyOrganisations(user)) {
				return;
			}

			if (hasInvalidPassword(user)) {
				return;
			}

			Organization organization = user.getOrganizations(
			).getFirst();

			updateCuscalBSB(originalUser, organization);
			updateCuscalCUOrgId(originalUser, organization);

			_userModelListener.onAfterUpdate(originalUser, user);
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CuscalUserModelListener.class);

	@Reference(
		target = "(component.name=com.liferay.portal.security.ldap.internal.model.listener.UserModelListener)"
	)
	private ModelListener<User> _userModelListener;

}