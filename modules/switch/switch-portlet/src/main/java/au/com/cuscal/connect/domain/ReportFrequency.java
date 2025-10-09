package au.com.cuscal.connect.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Rajni Bharara
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
@Table(name = "CODS_REPORTING.V_REPORT_FREQUENCY")
public class ReportFrequency implements Serializable {

	@Column(name = "REPORT_FREQUENCY_DESCRIPTION")
	public String getDescription() {
		return description;
	}

	@Column(name = "REPORT_FREQUENCY_NAME")
	public String getName() {
		return name;
	}

	@Column(name = "REPORT_FREQUENCY_ID")
	@Id
	public Long getReport_frequency_id() {
		return report_frequency_id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setReport_frequency_id(Long report_frequency_id) {
		this.report_frequency_id = report_frequency_id;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -2551609566890644452L;

	private String description;
	private String name;
	private Long report_frequency_id;

}