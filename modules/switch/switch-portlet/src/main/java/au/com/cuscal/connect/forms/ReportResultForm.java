package au.com.cuscal.connect.forms;

import java.io.Serializable;

import java.sql.Blob;

import java.util.Date;

public class ReportResultForm implements Serializable {

	public String getBlobFileId() {
		return blobFileId;
	}

	public String getFileDownloadTime() {
		return fileDownloadTime;
	}

	public String getFileName() {
		return fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public String getFileSizeType() {
		return fileSizeType;
	}

	public String getFileType() {
		return fileType;
	}

	public Boolean getIsArchived() {
		return isArchived;
	}

	public Boolean getIsEncrypted() {
		return isEncrypted;
	}

	public Long getMaxFileSize() {
		return maxFileSize;
	}

	public String getReportBin() {
		return reportBin;
	}

	public Blob getReportData() {
		return reportData;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public Date getReportEndDate() {
		return reportEndDate;
	}

	public String getReportName() {
		return reportName;
	}

	public String getReportOwnerName() {
		return reportOwnerName;
	}

	public Date getReportStartDate() {
		return reportStartDate;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public Date getRunDate() {
		return runDate;
	}

	public void setBlobFileId(String blobFileId) {
		this.blobFileId = blobFileId;
	}

	public void setFileDownloadTime(String fileDownloadTime) {
		this.fileDownloadTime = fileDownloadTime;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public void setFileSizeType(String fileSizeType) {
		this.fileSizeType = fileSizeType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setIsArchived(Boolean isArchived) {
		this.isArchived = isArchived;
	}

	public void setIsEncrypted(Boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}

	public void setMaxFileSize(Long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public void setReportBin(String reportBin) {
		this.reportBin = reportBin;
	}

	public void setReportData(Blob reportData) {
		this.reportData = reportData;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public void setReportEndDate(Date reportEndDate) {
		this.reportEndDate = reportEndDate;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public void setReportOwnerName(String reportOwnerName) {
		this.reportOwnerName = reportOwnerName;
	}

	public void setReportStartDate(Date reportStartDate) {
		this.reportStartDate = reportStartDate;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -4781790221367008694L;

	private String blobFileId;
	private String fileDownloadTime;
	private String fileName;
	private Long fileSize;
	private String fileSizeType;
	private String fileType;
	private Boolean isArchived;
	private Boolean isEncrypted;
	private Long maxFileSize = Long.valueOf(2097152);
	private String reportBin;
	private Blob reportData;
	private Date reportDate;
	private Date reportEndDate;
	private String reportName;
	private String reportOwnerName;
	private Date reportStartDate;
	private String reportTitle;
	private Date runDate;

}