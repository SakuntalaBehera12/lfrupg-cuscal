package au.com.cuscal.connect.forms;

import java.io.Serializable;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

public class ReportResultFormDisplayable
	implements PaginatedList, Serializable {

	public static ReportResultFormDisplayable wrap(
		ReportResultFormResults reportList) {

		ReportResultFormDisplayable displayable =
			new ReportResultFormDisplayable();

		displayable.reportList = reportList;

		return displayable;
	}

	public int getFullListSize() {
		return (int)reportList.getTotalCount();
	}

	public List<ReportResultForm> getList() {
		return reportList.getReportList();
	}

	public int getObjectsPerPage() {
		return reportList.getPageSize();
	}

	public int getPageNumber() {
		return reportList.getPageNum() + 1;
	}

	public String getSearchId() {

		// TODO Auto-generated method stub

		return null;
	}

	public String getSortCriterion() {

		// TODO Auto-generated method stub

		return null;
	}

	public SortOrderEnum getSortDirection() {

		// TODO Auto-generated method stub

		return null;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -9034012924625630651L;

	private ReportResultFormResults reportList = null;

}