package au.com.cuscal.connect.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "REPORT_ACCESS")
public class ReportAccess implements Serializable {

	public Long getAccessId() {
		return accessId;
	}

	public ReportOwner getReportAccessOwner() {
		return reportAccessOwner;
	}

	public ReportOwner getReportOwner() {
		return reportOwner;
	}

	public void setAccessId(Long accessId) {
		this.accessId = accessId;
	}

	public void setReportAccessOwner(ReportOwner reportAccessOwner) {
		this.reportAccessOwner = reportAccessOwner;
	}

	public void setReportOwner(ReportOwner reportOwner) {
		this.reportOwner = reportOwner;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 8712859396515910506L;

	@Column(name = "ACCESS_ID")
	@Id
	private Long accessId;

	@JoinColumn(name = "REPORT_OWNER_ACCESS_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private ReportOwner reportAccessOwner;

	@JoinColumn(name = "REPORT_OWNER_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private ReportOwner reportOwner;

}