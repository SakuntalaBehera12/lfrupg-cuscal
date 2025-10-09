package au.com.cuscal.connect.forms;

import au.com.cuscal.connect.domain.ReportMetadata;

import java.io.Serializable;

import java.util.List;

public class ReportResultFormResults implements Serializable {

	public List<ReportMetadata> getMetadatas() {
		return metadatas;
	}

	public int getPageNum() {
		return pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<ReportResultForm> getReportList() {
		return reportList;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setMetadatas(List<ReportMetadata> metadatas) {
		this.metadatas = metadatas;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setReportList(List<ReportResultForm> reportList) {
		this.reportList = reportList;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -5727245598550763224L;

	private List<ReportMetadata> metadatas = null;
	private int pageNum = 1;
	private int pageSize = 10;
	private List<ReportResultForm> reportList = null;
	private long totalCount = 0;

}