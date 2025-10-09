//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.common;

public enum TicketStatus {

	SUBMITTED(0), PENDING(1), OPEN(2), CLOSED(3), RE_OPENED(4), ON_HOLD(5),
	ACTION(6);

	public int getValue() {
		return this.value;
	}

	private TicketStatus(final int value) {
		this.value = value;
	}

	private int value;

}