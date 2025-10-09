package au.com.cuscal.connect.dao;

import au.com.cuscal.connect.commons.Utility;
import au.com.cuscal.connect.domain.ReportAccess;
import au.com.cuscal.connect.domain.ReportMetadata;
import au.com.cuscal.connect.domain.ReportOwner;
import au.com.cuscal.connect.domain.ReportPortalPermissions;
import au.com.cuscal.connect.forms.ReportForm;
import au.com.cuscal.connect.forms.ReportResultFormResults;
import au.com.cuscal.connect.forms.ReportsPagingParameters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author Rajni Bharara
 *
 */
@Component
@Repository("reportMetadataDao")
public class ReportMetadataDaoImpl implements ReportMetadataDao {

	/**
	 * SessionFactory object
	 */
	@Autowired
	SessionFactory sessionFactory;

	public static final String QUERY_FIND_OWNER_LIST =
		"ReportMetadata.findOwnersList";

	public static final String QUERY_FIND_REPORT_BLOB_LIST =
		"ReportMetadata.findReportBlobListById";

	/**
	 * Named query constants
	 */
	public static final String QUERY_FIND_REPORT_LIST =
		"ReportMetadata.findReportsList";

	public static final String QUERY_FIND_REPORT_LIST_BLOBID_RUNDATE =
		"ReportMetadata.findReportsListByBlobIdAndRunDate";

	public static final String QUERY_FIND_REPORT_LIST_BY_DATES =
		"ReportMetadata.findReportsListByDates";

	public static final String QUERY_FIND_REPORT_LIST_BY_PARAMETERS =
		"ReportMetadata.findReportsListByParameters";

	public static final String QUERY_FIND_REPORT_TYPE_LIST =
		"ReportMetadata.findReportTypeList";

	/**
	 * Find Owner access id by org id
	 *
	 * @param Long
	 * @return Set<Long>
	 */
	@SuppressWarnings("unchecked")
	public Set<Long> findOwnerAccessIdStr(Long userOrgId) {
		Set<Long> longs = new HashSet<>();
		Query query = sessionFactory.getCurrentSession(
		).getNamedQuery(
			ReportMetadataDaoImpl.QUERY_FIND_OWNER_LIST
		);

		query.setLong(0, userOrgId);
		List<ReportOwner> list = (ArrayList<ReportOwner>)query.list();

		for (ReportOwner reportOwner : list) {
			Set<ReportAccess> reportAccesses = reportOwner.getReportAccesses();

			for (ReportAccess reportAccess : reportAccesses) {
				longs.add(
					reportAccess.getReportAccessOwner(
					).getReportOwnerId());
			}
		}

		return longs;
	}

	/**
	 * Get report list by dates provided
	 *
	 * @param Set
	 *            <Long>
	 * @param Set
	 *            <Long>
	 * @param ReportForm
	 * @param ReportsPagingParameters
	 * @return ReportResultFormResults
	 */
	@SuppressWarnings("unchecked")
	public ReportResultFormResults findReportsListByDates(
		Set<Long> ownerIds, Set<Long> reportTypesIds, ReportForm form,
		ReportsPagingParameters parameters) {

		ReportResultFormResults reportResultFormResults =
			new ReportResultFormResults();
		Query query = sessionFactory.getCurrentSession(
		).getNamedQuery(
			ReportMetadataDaoImpl.QUERY_FIND_REPORT_LIST_BY_PARAMETERS
		);
		logger.debug(
			"[ReportMetadataDaoImpl.findReportsListByDates] :  DATE To And FROM is " +
				form.getFromDate() + "" + form.getToDate());
		query.setParameterList("ownerId", ownerIds.toArray());
		query.setParameterList("typeId", reportTypesIds.toArray());
		query.setString("fromDate", form.getFromDate());
		query.setString("toDate", form.getToDate());
		query.setString(
			"reportOwnerName", HSQL_likeParameter(form.getCustomerName()));
		query.setString(
			"reportTypeName", HSQL_likeParameter(form.getReportName()));
		query.setString("reportBin", HSQL_likeParameter(form.getCustomerBin()));

		logger.debug(
			"[ReportMetadataDaoImpl.findReportsListByDates] :  The Query for DATE filter   " +
				query.getQueryString());
		List<ReportMetadata> list = (ArrayList<ReportMetadata>)query.list();

		if ((list != null) && (list.size() > 0)) {
			logger.debug(
				"[ReportMetadataDaoImpl.findReportsListByDates] : The NON-Paging size of results list   " +
					list.size());
			int totalCount = list.size();

			query.setFirstResult(
				parameters.getPageSize() * parameters.getPageNum());
			query.setMaxResults(parameters.getPageSize());

			list = (ArrayList<ReportMetadata>)query.list();

			if (list != null) {
				logger.debug(
					"[ReportMetadataDaoImpl.findReportsListByDates] : The Paging size of results list   " +
						list.size());
				reportResultFormResults.setPageNum(parameters.getPageNum());
				reportResultFormResults.setPageSize(parameters.getPageSize());
				reportResultFormResults.setTotalCount(totalCount);
				reportResultFormResults.setMetadatas(list);
			}
			else {
				logger.debug(
					"[ReportMetadataDaoImpl.findReportsListByDates] : The results LIST IS NULL");
			}
		}

		return reportResultFormResults;// list;
	}

	/**
	 * Find list of Report type id by role names
	 *
	 * @param List
	 *            <String>
	 * @return Set<Long>
	 */
	@SuppressWarnings("unchecked")
	public Set<Long> findReportTypeIdsByRolesNames(List<String> roles) {
		Set<Long> longs = new HashSet<>();
		Query query = sessionFactory.getCurrentSession(
		).getNamedQuery(
			ReportMetadataDaoImpl.QUERY_FIND_REPORT_TYPE_LIST
		);

		query.setParameterList("roleNames", roles);
		List<ReportPortalPermissions> list =
			(ArrayList<ReportPortalPermissions>)query.list();
		logger.debug(
			"[ReportMetadataDaoImpl.findReportTypeIdsByRolesNames] : Size of Report Type list is   " +
				list.size());

		for (ReportPortalPermissions reportPortalPermissions : list) {
			logger.debug(
				"[ReportMetadataDaoImpl.findReportTypeIdsByRolesNames] :  The results from Role Name query   " +
					reportPortalPermissions.getReportType(
					).getReportTypeId());
			longs.add(
				reportPortalPermissions.getReportType(
				).getReportTypeId());
		}

		return longs;
	}

	/**
	 * Get report list by default search params
	 *
	 * @param Set
	 *            <Long>
	 * @param Set
	 *            <Long>
	 * @param ReportsPagingParameters
	 * @return ReportResultFormResults
	 */
	@SuppressWarnings("unchecked")
	public ReportResultFormResults retriveReportResult(
		Set<Long> ownerIds, Set<Long> reportTypesIds,
		ReportsPagingParameters parameters) {

		ReportResultFormResults reportResultFormResults =
			new ReportResultFormResults();
		String yesterdayDate = Utility.getYesterdayDate();

		logger.debug(
			"[ReportMetadataDaoImpl.retriveReportResult] : default search for yesterday date is   " +
				yesterdayDate);
		Query query = sessionFactory.getCurrentSession(
		).getNamedQuery(
			ReportMetadataDaoImpl.QUERY_FIND_REPORT_LIST
		);

		query.setParameterList("ownerId", ownerIds.toArray());
		query.setParameterList("typeId", reportTypesIds.toArray());
		query.setString("fromDate", yesterdayDate);
		query.setString("toDate", yesterdayDate);

		List<ReportMetadata> list = (ArrayList<ReportMetadata>)query.list();

		if (list != null) {
			logger.debug(
				"[ReportMetadataDaoImpl.retriveReportResult] : Found " +
					list.size() + " row(s)");
		}

		if ((list != null) && (list.size() > 0)) {
			logger.debug(
				"[ReportMetadataDaoImpl.retriveReportResult] : The NON-Paging size of results list   " +
					list.size());
			int totalCount = list.size();

			query.setFirstResult(
				parameters.getPageSize() * parameters.getPageNum());
			query.setMaxResults(parameters.getPageSize());

			logger.debug(
				"[ReportMetadataDaoImpl.retriveReportResult] : The Query is  " +
					query.getQueryString());
			list = (ArrayList<ReportMetadata>)query.list();

			if (list != null) {
				logger.debug(
					"[ReportMetadataDaoImpl.retriveReportResult] : The Paging size of results list   " +
						list.size());
				reportResultFormResults.setPageNum(parameters.getPageNum());
				reportResultFormResults.setPageSize(parameters.getPageSize());
				reportResultFormResults.setTotalCount(totalCount);
				reportResultFormResults.setMetadatas(list);
			}
			else {
				logger.debug(
					"[ReportMetadataDaoImpl.retriveReportResult] : The results LIST IS NULL");
			}
		}

		return reportResultFormResults;
	}

	/**
	 * Get report details by blob id and run date
	 *
	 * @param Set
	 *            <Long>
	 * @param Set
	 *            <Long>
	 * @param String
	 * @param String
	 * @return ReportMetadata
	 */
	public ReportMetadata retriveReportResultByBlobIdAndRunDate(
		Set<Long> ownerIds, Set<Long> reportTypesIds, String blobId,
		String runDate) {

		Query query = sessionFactory.getCurrentSession(
		).getNamedQuery(
			ReportMetadataDaoImpl.QUERY_FIND_REPORT_LIST_BLOBID_RUNDATE
		);

		query.setParameterList("ownerId", ownerIds.toArray());
		query.setParameterList("typeId", reportTypesIds.toArray());
		query.setString("blobId", blobId);
		logger.debug(
			"[ReportMetadataDaoImpl.retriveReportResultByBlobIdAndRunDate] : run date   " +
				runDate);
		query.setString("runDate", runDate);
		logger.debug(
			"[ReportMetadataDaoImpl.retriveReportResultByBlobIdAndRunDate] : The Query is  " +
				query.getQueryString());
		ReportMetadata metadata = (ReportMetadata)query.uniqueResult();

		if (metadata != null) {
			logger.debug(
				"[ReportMetadataDaoImpl.retriveReportResultByBlobIdAndRunDate] : The size of results list   " +
					metadata);
		}
		else {
			logger.debug(
				"[ReportMetadataDaoImpl.retriveReportResultByBlobIdAndRunDate] : The results LIST IS NULL");
		}

		return metadata;
	}

	/**
	 * Sql like query
	 *
	 * @param String
	 * @return String
	 */
	private String HSQL_likeParameter(String value) {
		if ((value == null) ||
			(value.trim(
			).length() == 0)) {

			return "%";
		}

		value = value.trim(
		).replace(
			'*', '%'
		);
		value = "%" + value + "%";

		return value;
	}

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(
		ReportMetadataDaoImpl.class);

}