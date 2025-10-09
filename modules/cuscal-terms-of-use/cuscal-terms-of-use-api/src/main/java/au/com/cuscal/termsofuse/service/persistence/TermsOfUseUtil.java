/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package au.com.cuscal.termsofuse.service.persistence;

import au.com.cuscal.termsofuse.model.TermsOfUse;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the terms of use service. This utility wraps <code>au.com.cuscal.termsofuse.service.persistence.impl.TermsOfUsePersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TermsOfUsePersistence
 * @generated
 */
public class TermsOfUseUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(TermsOfUse termsOfUse) {
		getPersistence().clearCache(termsOfUse);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, TermsOfUse> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<TermsOfUse> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<TermsOfUse> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<TermsOfUse> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<TermsOfUse> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static TermsOfUse update(TermsOfUse termsOfUse) {
		return getPersistence().update(termsOfUse);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static TermsOfUse update(
		TermsOfUse termsOfUse, ServiceContext serviceContext) {

		return getPersistence().update(termsOfUse, serviceContext);
	}

	/**
	 * Caches the terms of use in the entity cache if it is enabled.
	 *
	 * @param termsOfUse the terms of use
	 */
	public static void cacheResult(TermsOfUse termsOfUse) {
		getPersistence().cacheResult(termsOfUse);
	}

	/**
	 * Caches the terms of uses in the entity cache if it is enabled.
	 *
	 * @param termsOfUses the terms of uses
	 */
	public static void cacheResult(List<TermsOfUse> termsOfUses) {
		getPersistence().cacheResult(termsOfUses);
	}

	/**
	 * Creates a new terms of use with the primary key. Does not add the terms of use to the database.
	 *
	 * @param entryId the primary key for the new terms of use
	 * @return the new terms of use
	 */
	public static TermsOfUse create(long entryId) {
		return getPersistence().create(entryId);
	}

	/**
	 * Removes the terms of use with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use that was removed
	 * @throws NoSuchTermsOfUseException if a terms of use with the primary key could not be found
	 */
	public static TermsOfUse remove(long entryId)
		throws au.com.cuscal.termsofuse.exception.NoSuchTermsOfUseException {

		return getPersistence().remove(entryId);
	}

	public static TermsOfUse updateImpl(TermsOfUse termsOfUse) {
		return getPersistence().updateImpl(termsOfUse);
	}

	/**
	 * Returns the terms of use with the primary key or throws a <code>NoSuchTermsOfUseException</code> if it could not be found.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use
	 * @throws NoSuchTermsOfUseException if a terms of use with the primary key could not be found
	 */
	public static TermsOfUse findByPrimaryKey(long entryId)
		throws au.com.cuscal.termsofuse.exception.NoSuchTermsOfUseException {

		return getPersistence().findByPrimaryKey(entryId);
	}

	/**
	 * Returns the terms of use with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use, or <code>null</code> if a terms of use with the primary key could not be found
	 */
	public static TermsOfUse fetchByPrimaryKey(long entryId) {
		return getPersistence().fetchByPrimaryKey(entryId);
	}

	/**
	 * Returns all the terms of uses.
	 *
	 * @return the terms of uses
	 */
	public static List<TermsOfUse> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the terms of uses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TermsOfUseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of terms of uses
	 * @param end the upper bound of the range of terms of uses (not inclusive)
	 * @return the range of terms of uses
	 */
	public static List<TermsOfUse> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the terms of uses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TermsOfUseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of terms of uses
	 * @param end the upper bound of the range of terms of uses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of terms of uses
	 */
	public static List<TermsOfUse> findAll(
		int start, int end, OrderByComparator<TermsOfUse> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the terms of uses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TermsOfUseModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of terms of uses
	 * @param end the upper bound of the range of terms of uses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of terms of uses
	 */
	public static List<TermsOfUse> findAll(
		int start, int end, OrderByComparator<TermsOfUse> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the terms of uses from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of terms of uses.
	 *
	 * @return the number of terms of uses
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static TermsOfUsePersistence getPersistence() {
		return _persistence;
	}

	public static void setPersistence(TermsOfUsePersistence persistence) {
		_persistence = persistence;
	}

	private static volatile TermsOfUsePersistence _persistence;

}