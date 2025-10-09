/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package au.com.cuscal.termsofuse.service.impl;

import au.com.cuscal.termsofuse.model.TermsOfUse;
import au.com.cuscal.termsofuse.service.base.TermsOfUseLocalServiceBaseImpl;

import com.liferay.portal.aop.AopService;

import java.util.Date;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the terms of use local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>au.com.cuscal.termsofuse.service.TermsOfUseLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TermsOfUseLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=au.com.cuscal.termsofuse.model.TermsOfUse",
	service = AopService.class
)
public class TermsOfUseLocalServiceImpl extends TermsOfUseLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>au.com.cuscal.termsofuse.service.TermsOfUseLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>au.com.cuscal.termsofuse.service.TermsOfUseLocalServiceUtil</code>.
	 */
	public TermsOfUse acceptTermsOfUse(
		long groupId, long companyId, long userId, String screenName,
		double version, Date acceptedDate) {

		return createTermsOfUse(
			groupId, companyId, userId, screenName, version, acceptedDate);
	}

	private TermsOfUse createTermsOfUse(
		long groupId, long companyId, long userId, String screenName,
		double version, Date acceptedDate) {

		TermsOfUse termsOfUse = termsOfUsePersistence.create(
			counterLocalService.increment(TermsOfUse.class.getName()));

		termsOfUse.setGroupId(groupId);
		termsOfUse.setCompanyId(companyId);
		termsOfUse.setUserId(userId);
		termsOfUse.setScreenName(screenName);
		termsOfUse.setVersion(version);
		termsOfUse.setAcceptedDate(acceptedDate);

		return updateTermsOfUse(termsOfUse);
	}

}