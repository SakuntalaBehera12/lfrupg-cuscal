/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package au.com.cuscal.termsofuse.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link TermsOfUse}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TermsOfUse
 * @generated
 */
public class TermsOfUseWrapper
	extends BaseModelWrapper<TermsOfUse>
	implements ModelWrapper<TermsOfUse>, TermsOfUse {

	public TermsOfUseWrapper(TermsOfUse termsOfUse) {
		super(termsOfUse);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("entryId", getEntryId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("screenName", getScreenName());
		attributes.put("version", getVersion());
		attributes.put("acceptedDate", getAcceptedDate());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long entryId = (Long)attributes.get("entryId");

		if (entryId != null) {
			setEntryId(entryId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String screenName = (String)attributes.get("screenName");

		if (screenName != null) {
			setScreenName(screenName);
		}

		Double version = (Double)attributes.get("version");

		if (version != null) {
			setVersion(version);
		}

		Date acceptedDate = (Date)attributes.get("acceptedDate");

		if (acceptedDate != null) {
			setAcceptedDate(acceptedDate);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}
	}

	@Override
	public TermsOfUse cloneWithOriginalValues() {
		return wrap(model.cloneWithOriginalValues());
	}

	/**
	 * Returns the accepted date of this terms of use.
	 *
	 * @return the accepted date of this terms of use
	 */
	@Override
	public Date getAcceptedDate() {
		return model.getAcceptedDate();
	}

	/**
	 * Returns the company ID of this terms of use.
	 *
	 * @return the company ID of this terms of use
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this terms of use.
	 *
	 * @return the create date of this terms of use
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the entry ID of this terms of use.
	 *
	 * @return the entry ID of this terms of use
	 */
	@Override
	public long getEntryId() {
		return model.getEntryId();
	}

	/**
	 * Returns the group ID of this terms of use.
	 *
	 * @return the group ID of this terms of use
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the modified date of this terms of use.
	 *
	 * @return the modified date of this terms of use
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the primary key of this terms of use.
	 *
	 * @return the primary key of this terms of use
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the screen name of this terms of use.
	 *
	 * @return the screen name of this terms of use
	 */
	@Override
	public String getScreenName() {
		return model.getScreenName();
	}

	/**
	 * Returns the user ID of this terms of use.
	 *
	 * @return the user ID of this terms of use
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user uuid of this terms of use.
	 *
	 * @return the user uuid of this terms of use
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns the version of this terms of use.
	 *
	 * @return the version of this terms of use
	 */
	@Override
	public double getVersion() {
		return model.getVersion();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the accepted date of this terms of use.
	 *
	 * @param acceptedDate the accepted date of this terms of use
	 */
	@Override
	public void setAcceptedDate(Date acceptedDate) {
		model.setAcceptedDate(acceptedDate);
	}

	/**
	 * Sets the company ID of this terms of use.
	 *
	 * @param companyId the company ID of this terms of use
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this terms of use.
	 *
	 * @param createDate the create date of this terms of use
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the entry ID of this terms of use.
	 *
	 * @param entryId the entry ID of this terms of use
	 */
	@Override
	public void setEntryId(long entryId) {
		model.setEntryId(entryId);
	}

	/**
	 * Sets the group ID of this terms of use.
	 *
	 * @param groupId the group ID of this terms of use
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the modified date of this terms of use.
	 *
	 * @param modifiedDate the modified date of this terms of use
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the primary key of this terms of use.
	 *
	 * @param primaryKey the primary key of this terms of use
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the screen name of this terms of use.
	 *
	 * @param screenName the screen name of this terms of use
	 */
	@Override
	public void setScreenName(String screenName) {
		model.setScreenName(screenName);
	}

	/**
	 * Sets the user ID of this terms of use.
	 *
	 * @param userId the user ID of this terms of use
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user uuid of this terms of use.
	 *
	 * @param userUuid the user uuid of this terms of use
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	/**
	 * Sets the version of this terms of use.
	 *
	 * @param version the version of this terms of use
	 */
	@Override
	public void setVersion(double version) {
		model.setVersion(version);
	}

	@Override
	public String toXmlString() {
		return model.toXmlString();
	}

	@Override
	protected TermsOfUseWrapper wrap(TermsOfUse termsOfUse) {
		return new TermsOfUseWrapper(termsOfUse);
	}

}