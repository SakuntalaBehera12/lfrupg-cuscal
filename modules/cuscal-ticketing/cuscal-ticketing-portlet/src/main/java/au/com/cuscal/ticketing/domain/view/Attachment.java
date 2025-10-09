//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.domain.view;

import java.io.InputStream;

public class Attachment {

	public InputStream getFile() {
		return this.file;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public void setFile(final InputStream file) {
		this.file = file;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

	private InputStream file;
	private String fileName;
	private String mimeType;

}