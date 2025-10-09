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
@Table(name = "CODS_REPORTING.V_REPORT_TYPE_CATEGORY")
public class ReportTypeCategory implements Serializable {

	@Column(name = "REPORT_TYPE_CAT_DESCRIPTION")
	public String getDescription() {
		return description;
	}

	@Column(name = "REPORT_TYPE_CATEGORY_NAME")
	public String getName() {
		return name;
	}

	@Column(name = "REPORT_TYPE_CATEGORY_ID")
	@Id
	public Long getReport_type_category_id() {
		return report_type_category_id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setReport_type_category_id(Long report_type_category_id) {
		this.report_type_category_id = report_type_category_id;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -5210044145412956278L;

	private String description;
	private String name;
	private Long report_type_category_id;

}