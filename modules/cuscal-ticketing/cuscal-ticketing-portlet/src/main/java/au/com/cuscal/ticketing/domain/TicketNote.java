//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.domain;

import java.util.Date;

public class TicketNote {

	public Boolean getIsCuscalUser() {
		return this.isCuscalUser;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public Boolean getIsNewDay() {
		return this.isNewDay;
	}

	public String getNote() {
		return this.note;
	}

	public Date getNoteDate() {
		return this.noteDate;
	}

	public String getNoteId() {
		return this.noteId;
	}

	public String getNoteTitle() {
		return this.noteTitle;
	}

	public String getNoteUser() {
		return this.noteUser;
	}

	public String getOrganisation() {
		return this.organisation;
	}

	public String getResolverGroup() {
		return this.resolverGroup;
	}

	public void setIsCuscalUser(final Boolean isCuscalUser) {
		this.isCuscalUser = isCuscalUser;
	}

	public void setIsDeleted(final Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setIsNewDay(final Boolean isNewDay) {
		this.isNewDay = isNewDay;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	public void setNoteDate(final Date noteDate) {
		this.noteDate = noteDate;
	}

	public void setNoteId(final String noteId) {
		this.noteId = noteId;
	}

	public void setNoteTitle(final String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public void setNoteUser(final String noteUser) {
		this.noteUser = noteUser;
	}

	public void setOrganisation(final String organisation) {
		this.organisation = organisation;
	}

	public void setResolverGroup(final String resolverGroup) {
		this.resolverGroup = resolverGroup;
	}

	private Boolean isCuscalUser;
	private Boolean isDeleted;
	private Boolean isNewDay;
	private String note;
	private Date noteDate;
	private String noteId;
	private String noteTitle;
	private String noteUser;
	private String organisation;
	private String resolverGroup;

}