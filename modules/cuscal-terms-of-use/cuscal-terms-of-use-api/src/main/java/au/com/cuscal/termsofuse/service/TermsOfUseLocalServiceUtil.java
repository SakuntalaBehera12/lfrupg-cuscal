/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package au.com.cuscal.termsofuse.service;

import au.com.cuscal.termsofuse.model.TermsOfUse;

import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;

/**
 * Provides the local service utility for TermsOfUse. This utility wraps
 * <code>au.com.cuscal.termsofuse.service.impl.TermsOfUseLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see TermsOfUseLocalService
 * @generated
 */
public class TermsOfUseLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>au.com.cuscal.termsofuse.service.impl.TermsOfUseLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>TermsOfUseLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>TermsOfUseLocalServiceUtil</code>.
	 */
	public static TermsOfUse acceptTermsOfUse(
		long groupId, long companyId, long userId, String screenName,
		double version, java.util.Date acceptedDate) {

		return getService().acceptTermsOfUse(
			groupId, companyId, userId, screenName, version, acceptedDate);
	}

	/**
	 * Adds the terms of use to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TermsOfUseLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param termsOfUse the terms of use
	 * @return the terms of use that was added
	 */
	public static TermsOfUse addTermsOfUse(TermsOfUse termsOfUse) {
		return getService().addTermsOfUse(termsOfUse);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel createPersistedModel(
			Serializable primaryKeyObj)
		throws PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new terms of use with the primary key. Does not add the terms of use to the database.
	 *
	 * @param entryId the primary key for the new terms of use
	 * @return the new terms of use
	 */
	public static TermsOfUse createTermsOfUse(long entryId) {
		return getService().createTermsOfUse(entryId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel deletePersistedModel(
			PersistedModel persistedModel)
		throws PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	/**
	 * Deletes the terms of use with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TermsOfUseLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use that was removed
	 * @throws PortalException if a terms of use with the primary key could not be found
	 */
	public static TermsOfUse deleteTermsOfUse(long entryId)
		throws PortalException {

		return getService().deleteTermsOfUse(entryId);
	}

	/**
	 * Deletes the terms of use from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TermsOfUseLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param termsOfUse the terms of use
	 * @return the terms of use that was removed
	 */
	public static TermsOfUse deleteTermsOfUse(TermsOfUse termsOfUse) {
		return getService().deleteTermsOfUse(termsOfUse);
	}

	public static <T> T dslQuery(DSLQuery dslQuery) {
		return getService().dslQuery(dslQuery);
	}

	public static int dslQueryCount(DSLQuery dslQuery) {
		return getService().dslQueryCount(dslQuery);
	}

	public static DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>au.com.cuscal.termsofuse.model.impl.TermsOfUseModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>au.com.cuscal.termsofuse.model.impl.TermsOfUseModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static TermsOfUse fetchTermsOfUse(long entryId) {
		return getService().fetchTermsOfUse(entryId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the terms of use with the primary key.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use
	 * @throws PortalException if a terms of use with the primary key could not be found
	 */
	public static TermsOfUse getTermsOfUse(long entryId)
		throws PortalException {

		return getService().getTermsOfUse(entryId);
	}

	/**
	 * Returns a range of all the terms of uses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>au.com.cuscal.termsofuse.model.impl.TermsOfUseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of terms of uses
	 * @param end the upper bound of the range of terms of uses (not inclusive)
	 * @return the range of terms of uses
	 */
	public static List<TermsOfUse> getTermsOfUses(int start, int end) {
		return getService().getTermsOfUses(start, end);
	}

	/**
	 * Returns the number of terms of uses.
	 *
	 * @return the number of terms of uses
	 */
	public static int getTermsOfUsesCount() {
		return getService().getTermsOfUsesCount();
	}

	/**
	 * Updates the terms of use in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect TermsOfUseLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param termsOfUse the terms of use
	 * @return the terms of use that was updated
	 */
	public static TermsOfUse updateTermsOfUse(TermsOfUse termsOfUse) {
		return getService().updateTermsOfUse(termsOfUse);
	}

	public static TermsOfUseLocalService getService() {
		return _serviceSnapshot.get();
	}

	private static final Snapshot<TermsOfUseLocalService> _serviceSnapshot =
		new Snapshot<>(
			TermsOfUseLocalServiceUtil.class, TermsOfUseLocalService.class);

}