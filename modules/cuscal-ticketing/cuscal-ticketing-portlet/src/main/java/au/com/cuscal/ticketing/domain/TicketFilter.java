//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.domain;

public class TicketFilter {

	public String getProduct() {
		return this.product;
	}

	public String getSubmittedBy() {
		return this.submittedBy;
	}

	public String getSubmittedWithin() {
		return this.submittedWithin;
	}

	public String getTicketCategory() {
		return this.ticketCategory;
	}

	public String getTicketNumber() {
		return this.ticketNumber;
	}

	public String getTicketStatus() {
		return this.ticketStatus;
	}

	public String getUpdatedWithin() {
		return this.updatedWithin;
	}

	public void setProduct(final String product) {
		this.product = product;
	}

	public void setSubmittedBy(final String submittedBy) {
		this.submittedBy = submittedBy;
	}

	public void setSubmittedWithin(final String submittedWithin) {
		this.submittedWithin = submittedWithin;
	}

	public void setTicketCategory(final String ticketCategory) {
		this.ticketCategory = ticketCategory;
	}

	public void setTicketNumber(final String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public void setTicketStatus(final String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public void setUpdatedWithin(final String updatedWithin) {
		this.updatedWithin = updatedWithin;
	}

	private String product;
	private String submittedBy;
	private String submittedWithin;
	private String ticketCategory;
	private String ticketNumber;
	private String ticketStatus;
	private String updatedWithin;

}