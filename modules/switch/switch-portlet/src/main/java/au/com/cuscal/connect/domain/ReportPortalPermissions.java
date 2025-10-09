package au.com.cuscal.connect.domain;

import au.com.cuscal.connect.dao.ReportMetadataDaoImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "REPORT_PORTAL_PERMISSIONS")
@NamedQueries({ @NamedQuery(name = ReportMetadataDaoImpl.QUERY_FIND_REPORT_TYPE_LIST,
		query = " SELECT  rpp FROM ReportPortalPermissions rpp WHERE rpp.portalRoleName IN ( :roleNames )")

// @NamedQuery(name="", query="")

})
public class ReportPortalPermissions {

	public Long getPortalPermissionId() {
		return portalPermissionId;
	}

	public String getPortalRoleName() {
		return portalRoleName;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setPortalPermissionId(Long portalPermissionId) {
		this.portalPermissionId = portalPermissionId;
	}

	public void setPortalRoleName(String portalRoleName) {
		this.portalRoleName = portalRoleName;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	@Column(name = "PORTAL_PERMISSION_ID")
	@Id
	private Long portalPermissionId;

	@Column(name = "PORTAL_ROLE_NAME")
	private String portalRoleName;

	@JoinColumn(name = "REPORT_TYPE_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private ReportType reportType;

}