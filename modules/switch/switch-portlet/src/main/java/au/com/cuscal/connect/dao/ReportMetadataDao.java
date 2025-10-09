package au.com.cuscal.connect.dao;

import au.com.cuscal.connect.domain.ReportMetadata;
import au.com.cuscal.connect.forms.ReportForm;
import au.com.cuscal.connect.forms.ReportResultFormResults;
import au.com.cuscal.connect.forms.ReportsPagingParameters;

import java.util.List;
import java.util.Set;

/**
 * @author Rajni Bharara
 *
 */
public interface ReportMetadataDao {

	public Set<Long> findOwnerAccessIdStr(Long userOrgId);

	public ReportResultFormResults findReportsListByDates(
		Set<Long> ownerIds, Set<Long> reportTypesIds, ReportForm form,
		ReportsPagingParameters parameters);

	public Set<Long> findReportTypeIdsByRolesNames(List<String> roles);

	public ReportResultFormResults retriveReportResult(
		Set<Long> ownerIds, Set<Long> reportTypesIds,
		ReportsPagingParameters parameters);

	public ReportMetadata retriveReportResultByBlobIdAndRunDate(
		Set<Long> ownerIds, Set<Long> reportTypesIds, String blobId,
		String runDate);

}