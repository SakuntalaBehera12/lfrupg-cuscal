package au.com.cuscal.connect.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "REPORT_PORTAL_USERS_FAVOURITE")
public class ReportPortalUsersFavourite {

	public Long getPortalFavId() {
		return portalFavId;
	}

	public Long getPortalUserId() {
		return portalUserId;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setPortalFavId(Long portalFavId) {
		this.portalFavId = portalFavId;
	}

	public void setPortalUserId(Long portalUserId) {
		this.portalUserId = portalUserId;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	@Column(name = "PORTAL_FAV_ID")
	@Id
	private Long portalFavId;

	@Column(name = "PORTAL_USER_ID")
	private Long portalUserId;

	@JoinColumn(name = "REPORT_TYPE_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private ReportType reportType;

}