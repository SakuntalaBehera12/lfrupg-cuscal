package au.com.cuscal.transactions.domain;

import au.com.cuscal.framework.webservices.selfservice.AttributesType;

import java.io.Serializable;

import java.util.List;

public class TicketDetails implements Serializable {

	public String getServiceRequestStatus() {
		return serviceRequestStatus;
	}

	public List<AttributesType> getTicketAttributesList() {
		return ticketAttributesList;
	}

	public String getTicketCategory() {
		return ticketCategory;
	}

	public String getTicketFirstName() {
		return ticketFirstName;
	}

	public long getTicketId() {
		return ticketId;
	}

	public String getTicketLastName() {
		return ticketLastName;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public String getTicketSubmittedDate() {
		return ticketSubmittedDate;
	}

	public long getTicketTypeId() {
		return ticketTypeId;
	}

	public String getTicketUpdateDate() {
		return ticketUpdateDate;
	}

	public void setServiceRequestStatus(String serviceRequestStatus) {
		this.serviceRequestStatus = serviceRequestStatus;
	}

	public void setTicketAttributesList(
		List<AttributesType> ticketAttributesList) {

		this.ticketAttributesList = ticketAttributesList;
	}

	public void setTicketCategory(String ticketCategory) {
		this.ticketCategory = ticketCategory;
	}

	public void setTicketFirstName(String ticketFirstName) {
		this.ticketFirstName = ticketFirstName;
	}

	public void setTicketId(long ticketId) {
		this.ticketId = ticketId;
	}

	public void setTicketLastName(String ticketLastName) {
		this.ticketLastName = ticketLastName;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public void setTicketSubmittedDate(String ticketSubmittedDate) {
		this.ticketSubmittedDate = ticketSubmittedDate;
	}

	public void setTicketTypeId(long ticketTypeId) {
		this.ticketTypeId = ticketTypeId;
	}

	public void setTicketUpdateDate(String ticketUpdateDate) {
		this.ticketUpdateDate = ticketUpdateDate;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String serviceRequestStatus;
	private List<AttributesType> ticketAttributesList;
	private String ticketCategory;
	private String ticketFirstName;
	private long ticketId;
	private String ticketLastName;
	private String ticketNumber;
	private String ticketStatus;
	private String ticketSubmittedDate;
	private long ticketTypeId;
	private String ticketUpdateDate;

}