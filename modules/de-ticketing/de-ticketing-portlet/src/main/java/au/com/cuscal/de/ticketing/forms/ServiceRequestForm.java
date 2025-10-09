//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.forms;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ServiceRequestForm {

	public void clearForm() {
		this.setContactInformation(new RequestContactInformation());
		this.setRequestTypeInformation(new RequestTypeInformation());
	}

	public AttributesInformation getAttributesInformation() {
		return this.attributesInformation;
	}

	public RequestContactInformation getContactInformation() {
		return this.contactInformation;
	}

	public String getFinalDestination() {
		return this.finalDestination;
	}

	public String getFormId() {
		return this.formId;
	}

	public List<MultipartFile> getRequestAttachments() {
		return this.requestAttachments;
	}

	public RequestTypeInformation getRequestTypeInformation() {
		return this.requestTypeInformation;
	}

	public String getTicketId() {
		return this.ticketId;
	}

	public boolean isDisclaimer() {
		return this.disclaimer;
	}

	public boolean isOfi() {
		return this.isOfi;
	}

	public boolean isPrePopulated() {
		return this.isPrePopulated;
	}

	public void setAttributesInformation(
		final AttributesInformation attributesInformation) {

		this.attributesInformation = attributesInformation;
	}

	public void setContactInformation(
		final RequestContactInformation contactInformation) {

		this.contactInformation = contactInformation;
	}

	public void setDisclaimer(final boolean disclaimer) {
		this.disclaimer = disclaimer;
	}

	public void setFinalDestination(final String finalDestination) {
		this.finalDestination = finalDestination;
	}

	public void setFormId(final String formId) {
		this.formId = formId;
	}

	public void setOfi(final boolean isOfi) {
		this.isOfi = isOfi;
	}

	public void setPrePopulated(final boolean isPrePopulated) {
		this.isPrePopulated = isPrePopulated;
	}

	public void setRequestAttachments(
		final List<MultipartFile> requestAttachments) {

		this.requestAttachments = requestAttachments;
	}

	public void setRequestTypeInformation(
		final RequestTypeInformation requestTypeInformation) {

		this.requestTypeInformation = requestTypeInformation;
	}

	public void setTicketId(final String ticketId) {
		this.ticketId = ticketId;
	}

	private AttributesInformation attributesInformation;
	private RequestContactInformation contactInformation;
	private boolean disclaimer;
	private String finalDestination;
	private String formId;
	private boolean isOfi;
	private boolean isPrePopulated;
	private List<MultipartFile> requestAttachments;
	private RequestTypeInformation requestTypeInformation;
	private String ticketId;

}