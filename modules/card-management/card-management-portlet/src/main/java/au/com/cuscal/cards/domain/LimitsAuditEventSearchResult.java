package au.com.cuscal.cards.domain;

public class LimitsAuditEventSearchResult extends AuditEventSearchResult {

	public String getNewLimit() {
		return newLimit;
	}

	public String getOldLimit() {
		return oldLimit;
	}

	public void setNewLimit(String newLimit) {
		this.newLimit = newLimit;
	}

	public void setOldLimit(String oldLimit) {
		this.oldLimit = oldLimit;
	}

	private String newLimit;
	private String oldLimit;

}