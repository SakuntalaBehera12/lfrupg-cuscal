package au.com.cuscal.bpay.ticketing.forms;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service request main form class.
 *
 *
 */
public class ServiceRequestForm {//TODO

	/* Contact Information */
	public void clearForm() {
		setContactInformation(new RequestContactInformation());
		setRequestTypeInformation(new RequestTypeInformation());
		setAttributesInformation(new AttributesInformation());
	}

	/* Request Type Information */
	/**
	 * @return the attributesInformation
	 */
	public AttributesInformation getAttributesInformation() {
		return attributesInformation;
	}

	/* Attributes Information */
	public RequestContactInformation getContactInformation() {
		return contactInformation;
	}

	public List<MultipartFile> getRequestAttachments() {
		return requestAttachments;
	}

	public RequestTypeInformation getRequestTypeInformation() {
		return requestTypeInformation;
	}

	public String getTicketId() {
		return ticketId;
	}

	public boolean isDisclaimer() {
		return disclaimer;
	}

	public boolean isOfi() {
		return isOfi;
	}

	/**
	 * @param attributesInformation the attributesInformation to set
	 */
	public void setAttributesInformation(
		AttributesInformation attributesInformation) {

		this.attributesInformation = attributesInformation;
	}

	public void setContactInformation(
		RequestContactInformation contactInformation) {

		this.contactInformation = contactInformation;
	}

	public void setDisclaimer(boolean disclaimer) {
		this.disclaimer = disclaimer;
	}

	public void setOfi(boolean isOfi) {
		this.isOfi = isOfi;
	}

	public void setRequestAttachments(List<MultipartFile> requestAttachments) {
		this.requestAttachments = requestAttachments;
	}

	public void setRequestTypeInformation(
		RequestTypeInformation requestTypeInformation) {

		this.requestTypeInformation = requestTypeInformation;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	private AttributesInformation attributesInformation;
	private RequestContactInformation contactInformation;
	private boolean disclaimer;
	private boolean isOfi;
	private List<MultipartFile> requestAttachments;
	private RequestTypeInformation requestTypeInformation;
	private String ticketId;

}