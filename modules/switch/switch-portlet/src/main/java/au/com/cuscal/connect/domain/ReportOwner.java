package au.com.cuscal.connect.domain;

import au.com.cuscal.connect.dao.ReportMetadataDaoImpl;

import java.io.Serializable;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Rajni Bharara
 *
 */
@Entity
@Table(name = "REPORT_ORG_OWNER")

// @Cache (usage=CacheConcurrencyStrategy.READ_ONLY)

@NamedQueries({ @NamedQuery(name = ReportMetadataDaoImpl.QUERY_FIND_OWNER_LIST, query = " select ro from ReportOwner ro where ro.portalOrgId = ?")

// @NamedQuery(name="", query="")

})
public class ReportOwner implements Serializable {

	public String getAuthenticAcquirerId() {
		return authenticAcquirerId;
	}

	public Long getAuthenticIssuerId() {
		return authenticIssuerId;
	}

	public String getDescription() {
		return description;
	}

	public String getLdapOrgId() {
		return ldapOrgId;
	}

	public String getName() {
		return name;
	}

	public String getOsovId() {
		return osovId;
	}

	public Long getPortalOrgId() {
		return portalOrgId;
	}

	public Set<ReportAccess> getReportAccesses() {
		return reportAccesses;
	}

	public String getReportingId() {
		return reportingId;
	}

	public Long getReportOwnerId() {
		return reportOwnerId;
	}

	public String getThirdPartyId() {
		return thirdPartyId;
	}

	public void setAuthenticAcquirerId(String authenticAcquirerId) {
		this.authenticAcquirerId = authenticAcquirerId;
	}

	public void setAuthenticIssuerId(Long authenticIssuerId) {
		this.authenticIssuerId = authenticIssuerId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLdapOrgId(String ldapOrgId) {
		this.ldapOrgId = ldapOrgId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOsovId(String osovId) {
		this.osovId = osovId;
	}

	public void setPortalOrgId(Long portalOrgId) {
		this.portalOrgId = portalOrgId;
	}

	public void setReportAccesses(Set<ReportAccess> reportAccesses) {
		this.reportAccesses = reportAccesses;
	}

	public void setReportingId(String reportingId) {
		this.reportingId = reportingId;
	}

	public void setReportOwnerId(Long reportOwnerId) {
		this.reportOwnerId = reportOwnerId;
	}

	public void setThirdPartyId(String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1094100758193414228L;

	@Column(name = "AUTHENTIC_ACQUIRER_ID")
	private String authenticAcquirerId;

	@Column(name = "AUTHENTIC_ISSUER_ID")
	private Long authenticIssuerId;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "LDAP_ORG_ID")
	private String ldapOrgId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "OSOV_ID")
	private String osovId;

	@Column(name = "PORTAL_ORG_ID")
	private Long portalOrgId;

	@OneToMany(mappedBy = "reportOwner")

	// @JoinColumn(name="REPORT_OWNER_ID")

	private Set<ReportAccess> reportAccesses;

	@Column(name = "REPORTING_ID")
	private String reportingId;

	@Column(name = "REPORT_OWNER_ID")
	@GeneratedValue(
		generator = "RPT_QWN_SEQ", strategy = GenerationType.SEQUENCE
	)
	@Id
	@SequenceGenerator(name = "RPT_QWN_SEQ", sequenceName = "RPT_QWN_SEQ")
	private Long reportOwnerId;

	@Column(name = "THIRD_PARTY_ID")
	private String thirdPartyId;

}