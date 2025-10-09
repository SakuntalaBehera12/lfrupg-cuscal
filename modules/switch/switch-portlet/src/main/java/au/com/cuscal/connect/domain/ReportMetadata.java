package au.com.cuscal.connect.domain;

import au.com.cuscal.connect.dao.ReportMetadataDaoImpl;

import java.io.Serializable;

import java.sql.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * @author Rajni Bharara
 *
 * Report Owner name asc
 * Report End Date desc ,
 * Report Type Name asc,
 * Report Id asc ,
 * Report Run date asc
 */
@Entity
@NamedQueries(
	{
		@NamedQuery(
			name = ReportMetadataDaoImpl.QUERY_FIND_REPORT_LIST,
			query = " SELECT rpm FROM  ReportMetadata rpm WHERE rpm.reportOwner.reportOwnerId IN ( :ownerId ) " + " AND  rpm.reportType.reportTypeId IN ( :typeId ) AND ( rpm.isArchived ='N' OR rpm.isArchived='n') " + "  AND ( rpm.isCurrent='Y' OR  rpm.isCurrent='y') AND ( rpm.reportEndDate >= to_date( :fromDate , 'dd/mm/yyyy') " + "   AND rpm.reportEndDate <= to_date( :toDate , 'dd/mm/yyyy') )   " + " order by LOWER(rpm.reportOwner.name) asc , rpm.reportEndDate desc , LOWER(rpm.reportType.name) asc , rpm.reportMetaBlobKey.report_id asc , " + " rpm.reportMetaBlobKey.report_run_date asc , rpm.reportBin desc "
		),
		@NamedQuery(
			name = ReportMetadataDaoImpl.QUERY_FIND_REPORT_LIST_BY_DATES,
			query = " SELECT rpm FROM  ReportMetadata rpm WHERE rpm.reportOwner.reportOwnerId IN ( :ownerId )  " + " AND  rpm.reportType.reportTypeId IN ( :typeId ) AND ( rpm.reportEndDate >= to_date( :fromDate , 'dd/mm/yyyy') " + "   AND rpm.reportEndDate <= to_date( :toDate , 'dd/mm/yyyy') ) AND ( rpm.isArchived ='N' OR rpm.isArchived='n') " + "  AND ( rpm.isCurrent='Y' OR  rpm.isCurrent='y')  ORDER BY LOWER(rpm.reportOwner.name) asc , rpm.reportEndDate desc , LOWER(rpm.reportType.name) asc , " + " rpm.reportMetaBlobKey.report_id asc , rpm.reportMetaBlobKey.report_run_date asc ,rpm.reportBin desc"
		),
		@NamedQuery(
			name = ReportMetadataDaoImpl.QUERY_FIND_REPORT_LIST_BY_PARAMETERS,
			query = " SELECT rpm FROM  ReportMetadata rpm WHERE rpm.reportOwner.reportOwnerId IN ( :ownerId )  " + " AND  rpm.reportType.reportTypeId IN ( :typeId ) AND ( rpm.reportEndDate >= to_date( :fromDate , 'dd/mm/yyyy') " + " AND rpm.reportEndDate <= to_date( :toDate , 'dd/mm/yyyy') ) AND ( rpm.isArchived ='N' OR rpm.isArchived='n') " + " AND ( rpm.isCurrent='Y' OR  rpm.isCurrent='y') " + " AND ( UPPER(rpm.reportOwner.name) like (:reportOwnerName) ) " + " AND ( UPPER(rpm.reportType.name) like (:reportTypeName) ) " + " AND ( rpm.reportBin like (:reportBin) or rpm.reportBin is null ) " + " ORDER BY LOWER(rpm.reportOwner.name) asc , rpm.reportEndDate desc , LOWER(rpm.reportType.name) asc , " + " rpm.reportMetaBlobKey.report_id asc , rpm.reportMetaBlobKey.report_run_date asc ,rpm.reportBin desc "
		),
		@NamedQuery(
			name = ReportMetadataDaoImpl.QUERY_FIND_REPORT_LIST_BLOBID_RUNDATE,
			query = " SELECT rpm FROM  ReportMetadata rpm " + "    WHERE rpm.reportOwner.reportOwnerId IN ( :ownerId ) AND  rpm.reportType.reportTypeId IN ( :typeId ) " + "  AND  rpm.reportMetaBlobKey.report_id = :blobId  AND  rpm.reportMetaBlobKey.report_run_date = to_date(:runDate , 'dd/mm/yyyy') " + "  AND ( rpm.isCurrent='Y' OR  rpm.isCurrent='y') AND ( rpm.isArchived ='N' OR rpm.isArchived='n') " + "  order by LOWER(rpm.reportOwner.name) asc , rpm.reportEndDate desc , LOWER(rpm.reportType.name) asc , " + " rpm.reportMetaBlobKey.report_id asc , rpm.reportMetaBlobKey.report_run_date asc ,rpm.reportBin desc"
		)
	}
)
@Table(name = "REPORT_METADATA")
public class ReportMetadata implements Serializable {

	public Date getArchivedDate() {
		return archivedDate;
	}

	public String getFileName() {
		return fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public Boolean getIsArchived() {
		return isArchived;
	}

	public Boolean getIsCompressed() {
		return isCompressed;
	}

	public Boolean getIsCurrent() {
		return isCurrent;
	}

	public Boolean getIsEncrypted() {
		return isEncrypted;
	}

	public String getReportBin() {
		return reportBin;
	}

	public ReportBlob getReportBlob() {
		return reportBlob;
	}

	public Date getReportEndDate() {
		return reportEndDate;
	}

	public ReportMetaBlobKey getReportMetaBlobKey() {
		return reportMetaBlobKey;
	}

	public ReportOwner getReportOwner() {
		return reportOwner;
	}

	public Date getReportStartDate() {
		return reportStartDate;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setIsArchived(Boolean isArchived) {
		if (isArchived == null) {
			this.isArchived = Boolean.FALSE;
		}
		else {
			this.isArchived = isArchived;
		}
	}

	public void setIsCompressed(Boolean isCompressed) {
		if (isCompressed == null) {
			this.isCompressed = Boolean.FALSE;
		}
		else {
			this.isCompressed = isCompressed;
		}
	}

	public void setIsCurrent(Boolean isCurrent) {
		if (isCurrent == null) {
			this.isCurrent = Boolean.TRUE;
		}
		else {
			this.isCurrent = isCurrent;
		}
	}

	public void setIsEncrypted(Boolean isEncrypted) {
		if (isEncrypted == null) {
			this.isEncrypted = Boolean.FALSE;
		}
		else {
			this.isEncrypted = isEncrypted;
		}
	}

	public void setReportBin(String reportBin) {
		this.reportBin = reportBin;
	}

	public void setReportBlob(ReportBlob reportBlob) {
		this.reportBlob = reportBlob;
	}

	public void setReportEndDate(Date reportEndDate) {
		this.reportEndDate = reportEndDate;
	}

	public void setReportMetaBlobKey(ReportMetaBlobKey reportMetaBlobKey) {
		this.reportMetaBlobKey = reportMetaBlobKey;
	}

	public void setReportOwner(ReportOwner reportOwner) {
		this.reportOwner = reportOwner;
	}

	public void setReportStartDate(Date reportStartDate) {
		this.reportStartDate = reportStartDate;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -3313396071575037018L;

	@Column(name = "ARCHIVED_DATE")
	private Date archivedDate;

	@Column(name = "REPORT_FILE_NAME")
	private String fileName;

	@Column(name = "REPORT_FILE_SIZE")
	private Long fileSize;

	@Column(name = "REPORT_FILE_TYPE")
	private String fileType;

	@Column(name = "IS_ARCHIVED")
	@Type(type = "yes_no")
	private Boolean isArchived;

	@Column(name = "IS_COMPRESSED")
	@Type(type = "yes_no")
	private Boolean isCompressed;

	@Column(name = "IS_CURRENT")
	@Type(type = "yes_no")
	private Boolean isCurrent;

	@Column(name = "IS_ENCRYPTED")
	@Type(type = "yes_no")
	private Boolean isEncrypted;

	@Column(name = "REPORT_BIN")
	private String reportBin;

	@JoinColumns(
		{
			@JoinColumn(
				name = "REPORT_BLOB_ID", referencedColumnName = "report_id"
			),
			@JoinColumn(
				name = "REPORT_BLOB_DATE",
				referencedColumnName = "report_run_date"
			)
		}
	)
	@MapsId("reportMetaBlobKey")
	@OneToOne(fetch = FetchType.LAZY)
	private ReportBlob reportBlob;

	@Column(name = "REPORT_END_DATE")
	private Date reportEndDate;

	@AttributeOverrides(
		{
			@AttributeOverride(
				column = @Column(name = "REPORT_ID"), name = "report_id"
			),
			@AttributeOverride(
				column = @Column(name = "REPORT_RUN_DATE"),
				name = "report_run_date"
			)
		}
	)
	@EmbeddedId
	private ReportMetaBlobKey reportMetaBlobKey;

	@JoinColumn(name = "REPORT_OWNER_ID")
	@ManyToOne
	private ReportOwner reportOwner;

	@Column(name = "REPORT_START_DATE")
	private Date reportStartDate;

	@JoinColumn(name = "REPORT_TYPE_ID")
	@ManyToOne
	private ReportType reportType;

}