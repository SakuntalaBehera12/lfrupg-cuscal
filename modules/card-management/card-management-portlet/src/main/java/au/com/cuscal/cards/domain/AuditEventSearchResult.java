package au.com.cuscal.cards.domain;

import java.util.Date;

public class AuditEventSearchResult {

	public String getAction() {
		return action;
	}

	public String getChannel() {
		return channel;
	}

	public Date getDatetime() {
		return datetime;
	}

	public String getEventDetails() {
		return eventDetails;
	}

	public String getUser() {
		return user;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public void setEventDetails(String eventDetails) {
		this.eventDetails = eventDetails;
	}

	public void setUser(String user) {
		this.user = user;
	}

	private String action;
	private String channel;
	private Date datetime;
	private String eventDetails;
	private String user;

}