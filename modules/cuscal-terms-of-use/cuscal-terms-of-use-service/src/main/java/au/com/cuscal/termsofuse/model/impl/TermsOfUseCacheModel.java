/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package au.com.cuscal.termsofuse.model.impl;

import au.com.cuscal.termsofuse.model.TermsOfUse;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing TermsOfUse in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class TermsOfUseCacheModel
	implements CacheModel<TermsOfUse>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof TermsOfUseCacheModel)) {
			return false;
		}

		TermsOfUseCacheModel termsOfUseCacheModel =
			(TermsOfUseCacheModel)object;

		if (entryId == termsOfUseCacheModel.entryId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, entryId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(19);

		sb.append("{entryId=");
		sb.append(entryId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", screenName=");
		sb.append(screenName);
		sb.append(", version=");
		sb.append(version);
		sb.append(", acceptedDate=");
		sb.append(acceptedDate);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public TermsOfUse toEntityModel() {
		TermsOfUseImpl termsOfUseImpl = new TermsOfUseImpl();

		termsOfUseImpl.setEntryId(entryId);
		termsOfUseImpl.setGroupId(groupId);
		termsOfUseImpl.setCompanyId(companyId);
		termsOfUseImpl.setUserId(userId);

		if (screenName == null) {
			termsOfUseImpl.setScreenName("");
		}
		else {
			termsOfUseImpl.setScreenName(screenName);
		}

		termsOfUseImpl.setVersion(version);

		if (acceptedDate == Long.MIN_VALUE) {
			termsOfUseImpl.setAcceptedDate(null);
		}
		else {
			termsOfUseImpl.setAcceptedDate(new Date(acceptedDate));
		}

		if (createDate == Long.MIN_VALUE) {
			termsOfUseImpl.setCreateDate(null);
		}
		else {
			termsOfUseImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			termsOfUseImpl.setModifiedDate(null);
		}
		else {
			termsOfUseImpl.setModifiedDate(new Date(modifiedDate));
		}

		termsOfUseImpl.resetOriginalValues();

		return termsOfUseImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		entryId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		screenName = objectInput.readUTF();

		version = objectInput.readDouble();
		acceptedDate = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(entryId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (screenName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(screenName);
		}

		objectOutput.writeDouble(version);
		objectOutput.writeLong(acceptedDate);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);
	}

	public long entryId;
	public long groupId;
	public long companyId;
	public long userId;
	public String screenName;
	public double version;
	public long acceptedDate;
	public long createDate;
	public long modifiedDate;

}