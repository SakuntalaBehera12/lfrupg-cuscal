package au.com.cuscal.connect.forms;

import java.io.Serializable;

public class ReportsPagingParameters implements Serializable {

	public int getPageNum() {
		return pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public void setPageSize(int pageSize) {
		//this.pageSize = pageSize > MAX_PAGESIZE ? MAX_PAGESIZE : pageSize;
		this.pageSize = pageSize;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 428784433902970466L;

	private int pageNum = 0;
	private int pageSize = 10;

}