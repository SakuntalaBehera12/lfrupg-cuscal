package au.com.cuscal.connect.util.resource.dxp;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;
import au.com.cuscal.connect.util.resource.commons.Constants;
import au.com.cuscal.connect.util.resource.commons.LDAPConstants;
import au.com.cuscal.connect.util.resource.dxp.client.CustomerSelfServiceServiceLocal;
import au.com.cuscal.connect.util.resource.model.VsmUser;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.webservices.client.impl.EntityServiceImpl;
import au.com.cuscal.framework.webservices.liferay.dxp.LiferayClientUtil;
import au.com.cuscal.framework.webservices.selfservice.ContactType;
import au.com.cuscal.framework.webservices.selfservice.GetTicketUserRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTicketUserResponse;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.selfservice.UserType;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.PhoneLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserUtilImpl implements UserUtil {

	private final CustomerSelfServiceServiceLocal ticketingService =
		new CustomerSelfServiceServiceLocal();

	private final EntityServiceImpl entityService = new EntityServiceImpl();

	@Override
	public void addOrUpdatePhoneNumberForUser(User user, String phoneNo)
		throws PortalException {

		_addOrUpdatePhoneNumberForUser(user, phoneNo, null);
	}

	/**
	 *
	 */
	public void addOrUpdateUserInGetTicketUserService(User user)
		throws PortalException {

		_log.debug("addOrUpdateUserInGetTicketUserService -  start");

		GetTicketUserRequest getTicketUserRequest = new GetTicketUserRequest();

		TktRequestHeader header = new TktRequestHeader();
		ContactType userRequestBody = new ContactType();

		header = createTktRequestHeader(user);
		getTicketUserRequest.setHeader(header);

		userRequestBody = createUserRequestBody(user, header.getUserOrgId());
		getTicketUserRequest.setUser(userRequestBody);

		_log.debug("**** User will be added to TKTUSER table ****");
		_log.debug("First Name: " + userRequestBody.getFirstName());
        _log.debug("Last Name: {}", userRequestBody.getSurname());
		_log.debug("Email: " + userRequestBody.getEmail());
		_log.debug("Phone: " + userRequestBody.getPhoneNo());
		_log.debug("Organisation: " + userRequestBody.getOrganisation());

		GetTicketUserResponse response = new GetTicketUserResponse();

		response = getTicketUser(getTicketUserRequest);

		if (null != response) {
			if (null != response.getHeader()) {
				String statusCode = response.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase("SUCCESS", statusCode)) {
					UserType userType = response.getUser();

					if (null != userType) {
						_log.debug(
							"addOrUpdateUserInGetTicketUserService - successfully added/updated User" +
								response.getUser(
								).getUserId() + "to TKTUSER table");
					}
				}
				else {
					_log.error(
						"addOrUpdateUserInGetTicketUserService - WS Response status is not success: " +
							statusCode);
				}
			}
			else {
				_log.error(
					"addOrUpdateUserInGetTicketUserService - WS Response Header is null");
			}
		}
		else {
			_log.error(
				"addOrUpdateUserInGetTicketUserService - WS Response is null");
		}

		_log.debug("addOrUpdateUserInGetTicketUserService -  end");
	}

	/**
	 *
	 * @param user
	 * @return
	 */
	public TktRequestHeader createTktRequestHeader(User user) {
		_log.debug("createTktRequestHeader - start");

		TktRequestHeader requestHeader = new TktRequestHeader();

		try {
			if (null != user) {
				String screenName = user.getScreenName();
				Long userId = user.getUserId();

				List<Organization> organizations =
					OrganizationLocalServiceUtil.getUserOrganizations(userId);

				if ((null != organizations) && (1 == organizations.size())) {
					String userOrgName = organizations.get(
						0
					).getName();

					String orgShortName = getUserOrgShortName(
						Long.valueOf(
							organizations.get(
								0
							).getOrganizationId()));

					if (StringUtils.isNotBlank(orgShortName)) {
						requestHeader.setUserOrgId(orgShortName);
						_log.debug(
							"createTktRequestHeader - orgShortName is : " +
								orgShortName);
					}
					else {
						_log.error(
							"The header is not null but org short name is null for org id " +
								Long.valueOf(
									organizations.get(
										0
									).getOrganizationId()));
						requestHeader.setUserOrgId(null);
					}

					requestHeader.setUserOrgName(userOrgName);
					requestHeader.setUserId(user.getScreenName());

					_log.debug(
						"createTktRequestHeader - userId is : " +
							user.getScreenName());

					requestHeader.setOrigin(AuditOrigin.PORTAL_ORIGIN);
				}
				else {
					requestHeader.setUserOrgId(null);

					if (organizations == null) {
						_log.warn(
							screenName + " has no organisations assigned!");
					}
					else {
						String orgNames = null;

						if (organizations != null) {
							for (Organization organization : organizations) {
								orgNames += " " + organization.getName();
							}
						}

						_log.warn(
							screenName + " has " + organizations.size() +
								" organisations assigned: " + orgNames);
					}
				}
			}
		}
		catch (Exception e) {
			_log.error(
				"Exception setting tktRequestHeader header: " + e.getMessage(),
				e);
		}

		_log.debug("createTktRequestHeader - end");

		return requestHeader;
	}

	/**
	 *
	 */
	public void ensureUserIsMemberOfTargetOrganisationOnly(
			long userIdAsLong, String targetOrganisationName)
		throws PortalException {

		long defaultCompanyId = PortalUtil.getDefaultCompanyId();
		long targetOrgId = OrganizationLocalServiceUtil.getOrganizationId(
			defaultCompanyId, targetOrganisationName);

		if (!UserLocalServiceUtil.hasOrganizationUser(
				targetOrgId, userIdAsLong)) {

			long[] userIdAsLongArray = {userIdAsLong};
			UserLocalServiceUtil.addOrganizationUsers(
				targetOrgId, userIdAsLongArray);

			if (_log.isDebugEnabled())
				_log.debug(
					" >>> User" + userIdAsLong +
						" has got access to organization : " + targetOrgId);
		}
	}

	/**
	 *
	 */
	public User getLiferayUserByUserId(long liferayUserId) {
		User liferayUser = null;

		try {
			liferayUser = UserLocalServiceUtil.getUserById(liferayUserId);
		}
		catch (Exception e) {
			_log.error("Cannot find user with ID:" + liferayUserId);
		}

		return liferayUser;
	}

	/**
	 *
	 * @param getTicketUserRequest
	 * @return
	 */
	public GetTicketUserResponse getTicketUser(
		GetTicketUserRequest getTicketUserRequest) {

		_log.debug("getTicketUser - start");

		GetTicketUserResponse response = null;

		if (null != getTicketUserRequest) {
			_log.debug("getTicketUser - calling getTicketUser service");
			response = ticketingService.getTicketUser(getTicketUserRequest);
		}

		_log.debug("getTicketUser - end");

		return response;
	}

	/**
	 *
	 */
	public Phone retrieveBusinessPhoneForUser(User user)
		throws PortalException {

		List<Phone> phones = PhoneLocalServiceUtil.getPhones(
			user.getCompanyId(), Contact.class.getName(), user.getContactId());

		for (Phone phone : phones) {
			String phoneType = phone.getListType(
			).getName();

			if ("Business".equals(phoneType)) {
				return phone;
			}
		}

		return null;
	}

	/**
	 *
	 * @param user
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	public String retrieveBusinessPhoneNumberForUser(User user)
		throws PortalException {

		String phoneNo = "";
		Phone phone = retrieveBusinessPhoneForUser(user);

		if (null != phone) {
			phoneNo = phone.getNumber();
		}

		return phoneNo;
	}

	/**
	 *
	 */
	public VsmUser retrieveUserFromGetTicketUserService(User user)
		throws PortalException {

		_log.debug("retrieveUserFromGetTicketUserService - start");

		GetTicketUserRequest getTicketUserRequest = new GetTicketUserRequest();

		TktRequestHeader header = new TktRequestHeader();
		ContactType userRequestBody = new ContactType();
		VsmUser vsmUser = null;

		header = createTktRequestHeader(user);
		userRequestBody.setUpdateMyContactDetails(false);

		userRequestBody.setFirstName(user.getFirstName());
		userRequestBody.setSurname(user.getLastName());
		userRequestBody.setEmail(user.getEmailAddress());

		if (user.getOrganizations(
			).size() == 1) {

			userRequestBody.setOrganisation(
				user.getOrganizations(
				).get(
					0
				).getName());
		}
		else if (user.getOrganizations(
				).size() == 0) {

			_log.error(
				"retrieveUserFromGetTicketUserService - User " +
					user.getScreenName() +
						" has no organizations assigned to them.");
		}
		else {
			_log.error(
				"retrieveUserFromGetTicketUserService - User " +
					user.getScreenName() +
						" has more than 1 organization assigned to them.");
		}

		getTicketUserRequest.setHeader(header);
		getTicketUserRequest.setUser(userRequestBody);

		GetTicketUserResponse response = new GetTicketUserResponse();

		response = getTicketUser(getTicketUserRequest);

		if (null != response) {
			if (null != response.getHeader()) {
				String statusCode = response.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase("SUCCESS", statusCode)) {
					UserType userType = response.getUser();

					if (null != userType) {
						vsmUser = new VsmUser();

						if (StringUtils.isNotBlank(userType.getUserId()))
							vsmUser.setUserId(userType.getUserId());

						if (null != userType.getCreationTimestamp()) {
							Date creationDate = userType.getCreationTimestamp(
							).toGregorianCalendar(
							).getTime();

							vsmUser.setCreationDate(creationDate);
						}

						if (null != userType.getLastUpdatedTimestamp()) {
							Date lastUpdatedDate =
								userType.getLastUpdatedTimestamp(
								).toGregorianCalendar(
								).getTime();

							vsmUser.setLastUpdatedDate(lastUpdatedDate);
						}

						if (StringUtils.isNotBlank(userType.getEmail()))
							vsmUser.setEmail(userType.getEmail());

						if (StringUtils.isNotBlank(userType.getFname()))
							vsmUser.setFirstName(userType.getFname());

						if (StringUtils.isNotBlank(userType.getUserId()))
							vsmUser.setLastName(userType.getSname());

						if (StringUtils.isNotBlank(userType.getPhone()))
							vsmUser.setPhoneNo(userType.getPhone());

						if (StringUtils.isNotBlank(userType.getFax()))
							vsmUser.setFax(userType.getFax());

						if (StringUtils.isNotBlank(userType.getOrgShortName()))
							vsmUser.setOrgShortName(userType.getOrgShortName());

						if (StringUtils.isNotBlank(userType.getUserId()))
							vsmUser.setUserId(userType.getUserId());

						_log.debug(
							"***User Retrieved Successfully From TKT_USER***");
						_log.debug("UserId: " + userType.getUserId());
						_log.debug(
							"Creation Timestamp: " +
								userType.getCreationTimestamp());
						_log.debug(
							"Last Updated Timestamp: " +
								userType.getLastUpdatedTimestamp());
						_log.debug("First name: " + userType.getFname());
						_log.debug("Last name: " + userType.getSname());
						_log.debug("Phone No: " + userType.getPhone());
						_log.debug("Fax No: " + userType.getFax());
						_log.debug("Email: " + userType.getEmail());
						_log.debug(
							"Org Short Name: " + userType.getOrgShortName());
					}
				}
				else {
					_log.error(
						"retrieveUserFromGetTicketUserService - WS Response status is not success: " +
							statusCode);
				}
			}
			else {
				_log.error(
					"retrieveUserFromGetTicketUserService - WS Response Header is null");
			}
		}
		else {
			_log.error(
				"retrieveUserFromGetTicketUserService - WS Response is null");
		}

		_log.debug("retrieveUserFromGetTicketUserService - end");

		return vsmUser;
	}

	/**
	 *
	 */
	public int syncUserAttributesIfChanged(
			long userId, String updatingFirstName, String updatingLastName,
			String updatingEmail, String updatingPhoneNumber)
		throws PortalException {

		int status = LDAPConstants.NO_CHANGE;

		_log.debug("syncUserAttributesIfChanged - start");
		_log.debug(
			"syncUserAttributesIfChanged - Checking for changes to Liferay User");

		User user = UserLocalServiceUtil.fetchUser(userId);

		if (user != null) {
			String firstName = user.getFirstName();
			String lastName = user.getLastName();
			String emailAddress = user.getEmailAddress();

			boolean firstNameChange = _checkIfNewAttributeNeedsUpdating(
				firstName, updatingFirstName);

			if (firstNameChange) {
				_log.debug(
					"First Name will be updated from " + firstName + " to " +
						updatingFirstName);
				user.setFirstName(updatingFirstName);
			}

			boolean lastNameChange = _checkIfNewAttributeNeedsUpdating(
				lastName, updatingLastName);

			if (lastNameChange) {
				_log.debug(
					"Last Name will be updated from " + lastName + " to " +
						updatingLastName);
				user.setLastName(updatingLastName);
			}

			boolean emailAddressChange = _checkIfNewAttributeNeedsUpdating(
				emailAddress, updatingEmail);

			if (emailAddressChange) {
				_log.debug(
					"Email will be updated from " + emailAddress + " to " +
						updatingEmail);
				user.setEmailAddress(updatingEmail);
			}

			if (firstNameChange || lastNameChange || emailAddressChange) {
				try {
					user = UserLocalServiceUtil.updateUser(user);
					status = LDAPConstants.UPDATED;
				}
				catch (Exception e) {
					_log.error(
						String.format(
							"syncUserAttributesIfChanged - Error while updating user attributes to %s %s %s",
							updatingFirstName, updatingLastName,
							updatingEmail));
					status = LDAPConstants.UPDATE_ERROR;
					e.printStackTrace();
				}
			}

			// phone must be last or the user object will not be updated

			try {
				Phone liferayPhone = retrieveBusinessPhoneForUser(user);

				boolean existingPhoneChanged = false;

				if (((null != liferayPhone) &&
					 StringUtils.isNotBlank(liferayPhone.getNumber()) &&
					 !liferayPhone.getNumber(
					 ).equals(
						 updatingPhoneNumber
					 )) ||
					((null != liferayPhone) &&
					 StringUtils.isBlank(liferayPhone.getNumber()) &&
					 StringUtils.isNotBlank(updatingPhoneNumber))) {

					existingPhoneChanged = true;
				}

				boolean addingPhone = false;

				if ((null == liferayPhone) &&
					StringUtils.isNotBlank(updatingPhoneNumber)) {

					addingPhone = true;
				}

				if (existingPhoneChanged || addingPhone) {
					_log.debug(
						"Updated phone number from " + liferayPhone + " to " +
							updatingPhoneNumber);

					_addOrUpdatePhoneNumberForUser(
						user, updatingPhoneNumber, liferayPhone);

					status = LDAPConstants.UPDATED;
				}
			}
			catch (SystemException e) {
				e.printStackTrace();
				_log.error(
					"syncUserAttributesIfChanged - Error Updating Phone to " +
						updatingPhoneNumber);
				status = LDAPConstants.UPDATE_ERROR;
			}
			catch (PortalException e) {
				e.printStackTrace();
				_log.error(
					"syncUserAttributesIfChanged - Error Updating Phone to " +
						updatingPhoneNumber);
				status = LDAPConstants.UPDATE_ERROR;
			}
		}

		_log.debug("syncUserAttributesIfChanged - status returned: " + status);

		_log.debug("syncUserAttributesIfChanged - end");

		return status;
	}

	/**
	 *
	 */
	private void _addOrUpdatePhoneNumberForUser(
			User user, String phoneNo, Phone currentPhone)
		throws PortalException {

		Phone phone = currentPhone;

		if (phone == null) {
			phone = retrieveBusinessPhoneForUser(user);
		}

		if (null != phone) {
			phone.setNumber(phoneNo);
			PhoneLocalServiceUtil.updatePhone(phone);
		}
		else {
			ServiceContext serviceContext = new ServiceContext();

			PhoneLocalServiceUtil.addPhone(
				StringPool.BLANK, user.getUserId(), Contact.class.getName(), user.getContactId(),
				phoneNo, null, 11006, true, serviceContext);

			_log.debug(
				"Phone number added for " + user.getFullName() + ": " +
					phoneNo);
		}
	}

	private boolean _checkIfNewAttributeNeedsUpdating(
		String oldAttribute, String newAttribute) {

		if (StringUtils.isNotBlank(newAttribute) &&
			!StringUtils.equals(oldAttribute, newAttribute)) {

			return true;
		}

		return false;
	}

	/**
	 *
	 * @return
	 */
	private String checkConfigValueOfOrgOverrideFlag() {
		String orgOverride;

		InputStream iStream = CuscalSharedPropsUtil.getResourceStream(
			getClass(), Constants.UTILS_PROPERTIES);
		Properties properties = new Properties();

		try {
			properties.load(iStream);
			orgOverride = properties.getProperty(Constants.OVERRIDE_ORG_ID);

			if (null != orgOverride) {
				return orgOverride;
			}
		}
		catch (IOException e) {
			_log.error(Constants.UTILS_PROPERTIES + " file not found");

			return null;
		}

		return orgOverride;
	}

	/**
	 *
	 * @param user
	 * @param orgShortName
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	private ContactType createUserRequestBody(User user, String orgShortName)
		throws PortalException {

		ContactType userContactType = new ContactType();

		if (null != user) {
			userContactType.setFirstName(user.getFirstName());
			userContactType.setSurname(user.getLastName());
			userContactType.setUpdateMyContactDetails(true);
			userContactType.setOrganisation(orgShortName);
			userContactType.setEmail(user.getEmailAddress());
			userContactType.setPhoneNo(
				retrieveBusinessPhoneNumberForUser(user));
		}

		return userContactType;
	}

	/**
	 *
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	private String getUserOrgShortName(long orgId) throws Exception {
		String orgIdOverride = checkConfigValueOfOrgOverrideFlag();

		if (orgIdOverride == null) {
			return LiferayClientUtil.getOrgShortName(entityService, orgId);
		}

		try {
			long overrideOrgId = Long.parseLong(orgIdOverride);

			return LiferayClientUtil.getOrgShortName(
				entityService, overrideOrgId);
		}
		catch (NumberFormatException e) {
			_log.error(
				Constants.OVERRIDE_ORG_ID + " flag in " +
					Constants.UTILS_PROPERTIES +
						" is not valid and has been set as " + orgIdOverride);

			return LiferayClientUtil.getOrgShortName(entityService, orgId);
		}
	}

	/**
	 *
	 */
	private static final Logger _log = LoggerFactory.getLogger(UserUtilImpl.class);

}