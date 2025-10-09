//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.services;

import au.com.cuscal.framework.webservices.selfservice.AddTicketNoteResponse;
import au.com.cuscal.framework.webservices.selfservice.AddTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.DownloadAttachmentRequest;
import au.com.cuscal.framework.webservices.selfservice.DownloadAttachmentResponse;
import au.com.cuscal.framework.webservices.selfservice.FindTicketsResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.selfservice.TouchTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.UploadAttachmentRequest;
import au.com.cuscal.framework.webservices.selfservice.UploadAttachmentResponse;
import au.com.cuscal.framework.webservices.transaction.Customer;
import au.com.cuscal.ticketing.domain.Ticket;
import au.com.cuscal.ticketing.domain.TicketFilter;

import java.util.List;
import java.util.Map;

public interface CuscalTicketingService {

	FindTicketsResponse findTickets(
		final TicketFilter p0, final TktRequestHeader p1);

	AddTicketResponse addTicket(final Ticket p0, final TktRequestHeader p1);

	GetTicketResponse ticketDetails(
		final String p0, final Boolean p1, final TktRequestHeader p2,
		final Boolean p3);

	AddTicketNoteResponse addNote(
		final String p0, final String p1, final String p2,
		final TktRequestHeader p3);

	UploadAttachmentResponse uploadAttachment(final UploadAttachmentRequest p0);

	DownloadAttachmentResponse downloadAttachment(
		final DownloadAttachmentRequest p0);

	TouchTicketResponse touchTicket(
		final TicketType p0, final TktRequestHeader p1);

	Map<String, String> getTicketCategoryMap(final TktRequestHeader p0);

	Map<String, String> getProductMap(final TktRequestHeader p0);

	Map<String, String> getTicketStatusMap(final TktRequestHeader p0);

	Map<String, String> getTicketTypeMap(
		final String p0, final TktRequestHeader p1);

	List<Customer> getCustomerAccessViewList(final Long p0);

	String getOrgShortName(final Long p0);

}