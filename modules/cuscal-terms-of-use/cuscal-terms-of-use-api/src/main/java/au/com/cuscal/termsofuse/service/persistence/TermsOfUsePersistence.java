/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package au.com.cuscal.termsofuse.service.persistence;

import au.com.cuscal.termsofuse.exception.NoSuchTermsOfUseException;
import au.com.cuscal.termsofuse.model.TermsOfUse;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the terms of use service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TermsOfUseUtil
 * @generated
 */
@ProviderType
public interface TermsOfUsePersistence extends BasePersistence<TermsOfUse> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link TermsOfUseUtil} to access the terms of use persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Caches the terms of use in the entity cache if it is enabled.
	 *
	 * @param termsOfUse the terms of use
	 */
	public void cacheResult(TermsOfUse termsOfUse);

	/**
	 * Caches the terms of uses in the entity cache if it is enabled.
	 *
	 * @param termsOfUses the terms of uses
	 */
	public void cacheResult(java.util.List<TermsOfUse> termsOfUses);

	/**
	 * Creates a new terms of use with the primary key. Does not add the terms of use to the database.
	 *
	 * @param entryId the primary key for the new terms of use
	 * @return the new terms of use
	 */
	public TermsOfUse create(long entryId);

	/**
	 * Removes the terms of use with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use that was removed
	 * @throws NoSuchTermsOfUseException if a terms of use with the primary key could not be found
	 */
	public TermsOfUse remove(long entryId) throws NoSuchTermsOfUseException;

	public TermsOfUse updateImpl(TermsOfUse termsOfUse);

	/**
	 * Returns the terms of use with the primary key or throws a <code>NoSuchTermsOfUseException</code> if it could not be found.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use
	 * @throws NoSuchTermsOfUseException if a terms of use with the primary key could not be found
	 */
	public TermsOfUse findByPrimaryKey(long entryId)
		throws NoSuchTermsOfUseException;

	/**
	 * Returns the terms of use with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use, or <code>null</code> if a terms of use with the primary key could not be found
	 */
	public TermsOfUse fetchByPrimaryKey(long entryId);

	/**
	 * Returns all the terms of uses.
	 *
	 * @return the terms of uses
	 */
	public java.util.List<TermsOfUse> findAll();

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
	public java.util.List<TermsOfUse> findAll(int start, int end);

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
	public java.util.List<TermsOfUse> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TermsOfUse>
			orderByComparator);

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
	public java.util.List<TermsOfUse> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<TermsOfUse>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the terms of uses from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of terms of uses.
	 *
	 * @return the number of terms of uses
	 */
	public int countAll();

}