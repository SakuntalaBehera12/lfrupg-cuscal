//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.domain;

import java.io.Serializable;

public class Ticket implements Serializable {

	public String getProduct() {
		return this.product;
	}

	public String getTicketCategory() {
		return this.ticketCategory;
	}

	public String getTicketDescription() {
		return this.ticketDescription;
	}

	public String getTicketType() {
		return this.ticketType;
	}

	public TicketUser getUser() {
		return this.user;
	}

	public void setProduct(final String product) {
		this.product = product;
	}

	public void setTicketCategory(final String ticketCategory) {
		this.ticketCategory = ticketCategory;
	}

	public void setTicketDescription(final String ticketDescription) {
		this.ticketDescription = ticketDescription;
	}

	public void setTicketType(final String ticketType) {
		this.ticketType = ticketType;
	}

	public void setUser(final TicketUser user) {
		this.user = user;
	}

	private static final long serialVersionUID = -5497793564526256120L;

	private String product;
	private String ticketCategory;
	private String ticketDescription;
	private String ticketType;
	private TicketUser user;

}