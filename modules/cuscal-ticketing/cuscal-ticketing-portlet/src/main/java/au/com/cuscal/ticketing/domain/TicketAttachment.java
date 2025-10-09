//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.domain;

import java.util.Date;

public class TicketAttachment {

	public Date getAttachmentDate() {
		return this.attachmentDate;
	}

	public String getAttachmentId() {
		return this.attachmentId;
	}

	public String getFilename() {
		return this.filename;
	}

	public String getFileSize() {
		return this.fileSize;
	}

	public String getUploadedBy() {
		return this.uploadedBy;
	}

	public void setAttachmentDate(final Date attachmentDate) {
		this.attachmentDate = attachmentDate;
	}

	public void setAttachmentId(final String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public void setFileSize(final String fileSize) {
		this.fileSize = fileSize;
	}

	public void setUploadedBy(final String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	private Date attachmentDate;
	private String attachmentId;
	private String filename;
	private String fileSize;
	private String uploadedBy;

}