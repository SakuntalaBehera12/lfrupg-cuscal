package au.com.cuscal.connect.domain;

import au.com.cuscal.connect.dao.ReportMetadataDaoImpl;

import java.io.Serializable;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "REPORT_BLOB")
@NamedQueries({ @NamedQuery(name = ReportMetadataDaoImpl.QUERY_FIND_REPORT_BLOB_LIST,
		query = " SELECT  rb FROM ReportBlob rb WHERE rb.reportMetaBlobKey.report_id = ? " )

// @NamedQuery(name="", query="")

})
public class ReportBlob implements Serializable {

	public Blob getReportData() {
		return reportData;
	}

	public ReportMetaBlobKey getReportMetaBlobKey() {
		return reportMetaBlobKey;
	}

	public void setReportData(Blob reportData) {
		this.reportData = reportData;
	}

	public void setReportMetaBlobKey(ReportMetaBlobKey reportMetaBlobKey) {
		this.reportMetaBlobKey = reportMetaBlobKey;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 7514055171054077899L;

	@Column(name = "REPORT_DATA")
	@Lob
	private Blob reportData;

	@EmbeddedId
	private ReportMetaBlobKey reportMetaBlobKey;

}