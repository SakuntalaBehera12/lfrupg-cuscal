package au.com.cuscal.connect.util.resource.dxp;

import au.com.cuscal.connect.util.resource.model.VsmUser;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.User;

public interface UserUtil {

	User getLiferayUserByUserId(long liferayUserId);

	void ensureUserIsMemberOfTargetOrganisationOnly(
			long userIdAsLong, String targetOrganisationName)
		throws PortalException;

	public void addOrUpdatePhoneNumberForUser(User user, String phoneNo)
		throws PortalException;

	public void addOrUpdateUserInGetTicketUserService(User user)
		throws PortalException;

	public Phone retrieveBusinessPhoneForUser(User user) throws PortalException;

	public VsmUser retrieveUserFromGetTicketUserService(User user)
		throws PortalException;

	public int syncUserAttributesIfChanged(
			long userId, String firstName, String lastName, String email,
			String phoneNumber)
		throws PortalException;

}