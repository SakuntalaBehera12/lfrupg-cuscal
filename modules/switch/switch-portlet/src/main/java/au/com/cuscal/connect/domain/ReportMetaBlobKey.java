package au.com.cuscal.connect.domain;

import java.io.Serializable;

import java.sql.Date;

import javax.persistence.Embeddable;

@Embeddable
public class ReportMetaBlobKey implements Serializable {

	public Long getReport_id() {
		return report_id;
	}

	public Date getReport_run_date() {
		return report_run_date;
	}

	public void setReport_id(Long report_id) {
		this.report_id = report_id;
	}

	public void setReport_run_date(Date report_run_date) {
		this.report_run_date = report_run_date;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 8893931647988139835L;

	private Long report_id;
	private Date report_run_date;

}