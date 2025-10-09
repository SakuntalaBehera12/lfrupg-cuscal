package au.com.cuscal.cards.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardSpend {

	public String getPeriod() {
		return period;
	}

	public long getSpend() {
		return spend;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public void setSpend(long spend) {
		this.spend = spend;
	}

	private String period;
	private long spend;

}