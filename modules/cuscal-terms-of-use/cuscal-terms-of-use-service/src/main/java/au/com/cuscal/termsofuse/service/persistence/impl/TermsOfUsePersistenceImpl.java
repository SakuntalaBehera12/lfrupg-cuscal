/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package au.com.cuscal.termsofuse.service.persistence.impl;

import au.com.cuscal.termsofuse.exception.NoSuchTermsOfUseException;
import au.com.cuscal.termsofuse.model.TermsOfUse;
import au.com.cuscal.termsofuse.model.TermsOfUseTable;
import au.com.cuscal.termsofuse.model.impl.TermsOfUseImpl;
import au.com.cuscal.termsofuse.model.impl.TermsOfUseModelImpl;
import au.com.cuscal.termsofuse.service.persistence.TermsOfUsePersistence;
import au.com.cuscal.termsofuse.service.persistence.TermsOfUseUtil;
import au.com.cuscal.termsofuse.service.persistence.impl.constants.CuscalToCPersistenceConstants;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the terms of use service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = TermsOfUsePersistence.class)
public class TermsOfUsePersistenceImpl
	extends BasePersistenceImpl<TermsOfUse> implements TermsOfUsePersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>TermsOfUseUtil</code> to access the terms of use persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		TermsOfUseImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;

	public TermsOfUsePersistenceImpl() {
		setModelClass(TermsOfUse.class);

		setModelImplClass(TermsOfUseImpl.class);
		setModelPKClass(long.class);

		setTable(TermsOfUseTable.INSTANCE);
	}

	/**
	 * Caches the terms of use in the entity cache if it is enabled.
	 *
	 * @param termsOfUse the terms of use
	 */
	@Override
	public void cacheResult(TermsOfUse termsOfUse) {
		entityCache.putResult(
			TermsOfUseImpl.class, termsOfUse.getPrimaryKey(), termsOfUse);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the terms of uses in the entity cache if it is enabled.
	 *
	 * @param termsOfUses the terms of uses
	 */
	@Override
	public void cacheResult(List<TermsOfUse> termsOfUses) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (termsOfUses.size() > _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (TermsOfUse termsOfUse : termsOfUses) {
			if (entityCache.getResult(
					TermsOfUseImpl.class, termsOfUse.getPrimaryKey()) == null) {

				cacheResult(termsOfUse);
			}
		}
	}

	/**
	 * Clears the cache for all terms of uses.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(TermsOfUseImpl.class);

		finderCache.clearCache(TermsOfUseImpl.class);
	}

	/**
	 * Clears the cache for the terms of use.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(TermsOfUse termsOfUse) {
		entityCache.removeResult(TermsOfUseImpl.class, termsOfUse);
	}

	@Override
	public void clearCache(List<TermsOfUse> termsOfUses) {
		for (TermsOfUse termsOfUse : termsOfUses) {
			entityCache.removeResult(TermsOfUseImpl.class, termsOfUse);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(TermsOfUseImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(TermsOfUseImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new terms of use with the primary key. Does not add the terms of use to the database.
	 *
	 * @param entryId the primary key for the new terms of use
	 * @return the new terms of use
	 */
	@Override
	public TermsOfUse create(long entryId) {
		TermsOfUse termsOfUse = new TermsOfUseImpl();

		termsOfUse.setNew(true);
		termsOfUse.setPrimaryKey(entryId);

		termsOfUse.setCompanyId(CompanyThreadLocal.getCompanyId());

		return termsOfUse;
	}

	/**
	 * Removes the terms of use with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use that was removed
	 * @throws NoSuchTermsOfUseException if a terms of use with the primary key could not be found
	 */
	@Override
	public TermsOfUse remove(long entryId) throws NoSuchTermsOfUseException {
		return remove((Serializable)entryId);
	}

	/**
	 * Removes the terms of use with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the terms of use
	 * @return the terms of use that was removed
	 * @throws NoSuchTermsOfUseException if a terms of use with the primary key could not be found
	 */
	@Override
	public TermsOfUse remove(Serializable primaryKey)
		throws NoSuchTermsOfUseException {

		Session session = null;

		try {
			session = openSession();

			TermsOfUse termsOfUse = (TermsOfUse)session.get(
				TermsOfUseImpl.class, primaryKey);

			if (termsOfUse == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchTermsOfUseException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(termsOfUse);
		}
		catch (NoSuchTermsOfUseException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected TermsOfUse removeImpl(TermsOfUse termsOfUse) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(termsOfUse)) {
				termsOfUse = (TermsOfUse)session.get(
					TermsOfUseImpl.class, termsOfUse.getPrimaryKeyObj());
			}

			if (termsOfUse != null) {
				session.delete(termsOfUse);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (termsOfUse != null) {
			clearCache(termsOfUse);
		}

		return termsOfUse;
	}

	@Override
	public TermsOfUse updateImpl(TermsOfUse termsOfUse) {
		boolean isNew = termsOfUse.isNew();

		if (!(termsOfUse instanceof TermsOfUseModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(termsOfUse.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(termsOfUse);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in termsOfUse proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom TermsOfUse implementation " +
					termsOfUse.getClass());
		}

		TermsOfUseModelImpl termsOfUseModelImpl =
			(TermsOfUseModelImpl)termsOfUse;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (termsOfUse.getCreateDate() == null)) {
			if (serviceContext == null) {
				termsOfUse.setCreateDate(date);
			}
			else {
				termsOfUse.setCreateDate(serviceContext.getCreateDate(date));
			}
		}

		if (!termsOfUseModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				termsOfUse.setModifiedDate(date);
			}
			else {
				termsOfUse.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(termsOfUse);
			}
			else {
				termsOfUse = (TermsOfUse)session.merge(termsOfUse);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(TermsOfUseImpl.class, termsOfUse, false, true);

		if (isNew) {
			termsOfUse.setNew(false);
		}

		termsOfUse.resetOriginalValues();

		return termsOfUse;
	}

	/**
	 * Returns the terms of use with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the terms of use
	 * @return the terms of use
	 * @throws NoSuchTermsOfUseException if a terms of use with the primary key could not be found
	 */
	@Override
	public TermsOfUse findByPrimaryKey(Serializable primaryKey)
		throws NoSuchTermsOfUseException {

		TermsOfUse termsOfUse = fetchByPrimaryKey(primaryKey);

		if (termsOfUse == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchTermsOfUseException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return termsOfUse;
	}

	/**
	 * Returns the terms of use with the primary key or throws a <code>NoSuchTermsOfUseException</code> if it could not be found.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use
	 * @throws NoSuchTermsOfUseException if a terms of use with the primary key could not be found
	 */
	@Override
	public TermsOfUse findByPrimaryKey(long entryId)
		throws NoSuchTermsOfUseException {

		return findByPrimaryKey((Serializable)entryId);
	}

	/**
	 * Returns the terms of use with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the terms of use
	 * @return the terms of use, or <code>null</code> if a terms of use with the primary key could not be found
	 */
	@Override
	public TermsOfUse fetchByPrimaryKey(long entryId) {
		return fetchByPrimaryKey((Serializable)entryId);
	}

	/**
	 * Returns all the terms of uses.
	 *
	 * @return the terms of uses
	 */
	@Override
	public List<TermsOfUse> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<TermsOfUse> findAll(int start, int end) {
		return findAll(start, end, null);
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
	@Override
	public List<TermsOfUse> findAll(
		int start, int end, OrderByComparator<TermsOfUse> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
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
	@Override
	public List<TermsOfUse> findAll(
		int start, int end, OrderByComparator<TermsOfUse> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<TermsOfUse> list = null;

		if (useFinderCache) {
			list = (List<TermsOfUse>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_TERMSOFUSE);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_TERMSOFUSE;

				sql = sql.concat(TermsOfUseModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<TermsOfUse>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the terms of uses from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (TermsOfUse termsOfUse : findAll()) {
			remove(termsOfUse);
		}
	}

	/**
	 * Returns the number of terms of uses.
	 *
	 * @return the number of terms of uses
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_TERMSOFUSE);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "entryId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_TERMSOFUSE;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return TermsOfUseModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the terms of use persistence.
	 */
	@Activate
	public void activate() {
		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD));

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		TermsOfUseUtil.setPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		TermsOfUseUtil.setPersistence(null);

		entityCache.removeCache(TermsOfUseImpl.class.getName());
	}

	@Override
	@Reference(
		target = CuscalToCPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = CuscalToCPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = CuscalToCPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_TERMSOFUSE =
		"SELECT termsOfUse FROM TermsOfUse termsOfUse";

	private static final String _SQL_COUNT_TERMSOFUSE =
		"SELECT COUNT(termsOfUse) FROM TermsOfUse termsOfUse";

	private static final String _ORDER_BY_ENTITY_ALIAS = "termsOfUse.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No TermsOfUse exists with the primary key ";

	private static final Log _log = LogFactoryUtil.getLog(
		TermsOfUsePersistenceImpl.class);

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

}