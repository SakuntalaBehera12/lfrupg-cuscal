package au.com.cuscal.connect.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Rajni Bharara
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
@Table(name = "CODS_REPORTING.V_REPORT_TYPE")
public class ReportType implements Serializable {

	@Column(name = "REPORT_TYPE_DESCRIPTION")
	public String getDescription() {
		return description;
	}

	@Column(name = "IS_BY_ACQUIRER")
	public String getIsByAcquirer() {
		return isByAcquirer;
	}

	@Column(name = "IS_BY_ISSUER")
	public String getIsByIssuer() {
		return isByIssuer;
	}

	@Column(name = "IS_BY_ISSUERBIN")
	public String getIsByIssuingBin() {
		return isByIssuingBin;
	}

	@Column(name = "REPORT_TYPE_NAME")
	public String getName() {
		return name;
	}

	@Column(name = "REPORT_FILENAME_TEMPLATE")
	public String getReportFilenameTemplate() {
		return reportFilenameTemplate;
	}

	@JoinColumn(name = "REPORT_FREQUENCY_ID")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public ReportFrequency getReportFrequency() {
		return reportFrequency;
	}

	@JoinColumn(name = "REPORT_TYPE_CATEGORY_ID")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public ReportTypeCategory getReportTypeCategory() {
		return reportTypeCategory;
	}

	@Column(name = "REPORT_TYPE_ID")
	@Id
	public Long getReportTypeId() {
		return reportTypeId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setIsByAcquirer(String isByAcquirer) {
		this.isByAcquirer = isByAcquirer;
	}

	public void setIsByIssuer(String isByIssuer) {
		this.isByIssuer = isByIssuer;
	}

	public void setIsByIssuingBin(String isByIssuingBin) {
		this.isByIssuingBin = isByIssuingBin;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setReportFilenameTemplate(String reportFilenameTemplate) {
		this.reportFilenameTemplate = reportFilenameTemplate;
	}

	public void setReportFrequency(ReportFrequency reportFrequency) {
		this.reportFrequency = reportFrequency;
	}

	public void setReportTypeCategory(ReportTypeCategory reportTypeCategory) {
		this.reportTypeCategory = reportTypeCategory;
	}

	public void setReportTypeId(Long reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String description;
	private String isByAcquirer;
	private String isByIssuer;
	private String isByIssuingBin;
	private String name;
	private String reportFilenameTemplate;
	private ReportFrequency reportFrequency;
	private ReportTypeCategory reportTypeCategory;
	private Long reportTypeId;

}