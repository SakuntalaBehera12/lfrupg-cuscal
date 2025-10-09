/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package au.com.cuscal.termsofuse.service;

import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * Provides a wrapper for {@link TermsOfUseLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see TermsOfUseLocalService
 * @generated
 */
public class TermsOfUseLocalServiceWrapper
	implements ServiceWrapper<TermsOfUseLocalService>, TermsOfUseLocalService {

	public TermsOfUseLocalServiceWrapper() {
		this(null);
	}

	public TermsOfUseLocalServiceWrapper(
		TermsOfUseLocalService termsOfUseLocalService) {

		_termsOfUseLocalService = termsOfUseLocalService;
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>TermsOfUseLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>TermsOfUseLocalServiceUtil</code>.
	 */
	@Override
	public au.com.cuscal.termsofuse.model.TermsOfUse acceptTermsOfUse(
		long groupId, long companyId, long userId, String screenName,
		double version, java.util.Date acceptedDate) {

		return _termsOfUseLocalService.acceptTermsOfUse(
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
	@Override
	public au.com.cuscal.termsofuse.model.TermsOfUse addTermsOfUse(
		au.com.cuscal.termsofuse.model.TermsOfUse termsOfUse) {

		return _termsOfUseLocalService.addTermsOfUse(termsOfUse);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _termsOfUseLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new terms of use with the primary key. Does not add the terms of use to the database.
	 *
	 * @param entryId the primary key for the new terms of use
	 * @return the new terms of use
	 */
	@Override
	public au.com.cuscal.termsofuse.model.TermsOfUse createTermsOfUse(
		long entryId) {

		return _termsOfUseLocalService.createTermsOfUse(entryId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _termsOfUseLocalService.deletePersistedModel(persistedModel);
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
	@Override
	public au.com.cuscal.termsofuse.model.TermsOfUse deleteTermsOfUse(
			long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _termsOfUseLocalService.deleteTermsOfUse(entryId);
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
	@Override
	public au.com.cuscal.termsofuse.model.TermsOfUse deleteTermsOfUse(
		au.com.cuscal.termsofuse.model.TermsOfUse termsOfUse) {

		return _termsOfUseLocalService.deleteTermsOfUse(termsOfUse);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _termsOfUseLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _termsOfUseLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _termsOfUseLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _termsOfUseLocalService.dynamicQuery(dynamicQuery);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _termsOfUseLocalService.dynamicQuery(dynamicQuery, start, end);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _termsOfUseLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _termsOfUseLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _termsOfUseLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public au.com.cuscal.termsofuse.model.TermsOfUse fetchTermsOfUse(
		long entryId) {

		return _termsOfUseLocalService.fetchTermsOfUse(entryId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _termsOfUseLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _termsOfUseLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _termsOfUseLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _termsOfUseLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the terms of use with the primary key.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use
	 * @throws PortalException if a terms of use with the primary key could not be found
	 */
	@Override
	public au.com.cuscal.termsofuse.model.TermsOfUse getTermsOfUse(long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _termsOfUseLocalService.getTermsOfUse(entryId);
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
	@Override
	public java.util.List<au.com.cuscal.termsofuse.model.TermsOfUse>
		getTermsOfUses(int start, int end) {

		return _termsOfUseLocalService.getTermsOfUses(start, end);
	}

	/**
	 * Returns the number of terms of uses.
	 *
	 * @return the number of terms of uses
	 */
	@Override
	public int getTermsOfUsesCount() {
		return _termsOfUseLocalService.getTermsOfUsesCount();
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
	@Override
	public au.com.cuscal.termsofuse.model.TermsOfUse updateTermsOfUse(
		au.com.cuscal.termsofuse.model.TermsOfUse termsOfUse) {

		return _termsOfUseLocalService.updateTermsOfUse(termsOfUse);
	}

	@Override
	public BasePersistence<?> getBasePersistence() {
		return _termsOfUseLocalService.getBasePersistence();
	}

	@Override
	public TermsOfUseLocalService getWrappedService() {
		return _termsOfUseLocalService;
	}

	@Override
	public void setWrappedService(
		TermsOfUseLocalService termsOfUseLocalService) {

		_termsOfUseLocalService = termsOfUseLocalService;
	}

	private TermsOfUseLocalService _termsOfUseLocalService;

}