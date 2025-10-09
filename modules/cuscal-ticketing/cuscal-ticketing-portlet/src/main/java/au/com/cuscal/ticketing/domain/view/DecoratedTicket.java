//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.domain.view;

import au.com.cuscal.framework.webservices.selfservice.AttachmentSummaryType;
import au.com.cuscal.framework.webservices.selfservice.AttributesType;
import au.com.cuscal.framework.webservices.selfservice.NoteType;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TransactionType;
import au.com.cuscal.ticketing.common.CommonUtil;
import au.com.cuscal.ticketing.common.Constants;
import au.com.cuscal.ticketing.domain.TicketAttachment;
import au.com.cuscal.ticketing.domain.TicketNote;
import au.com.cuscal.ticketing.domain.TicketTransaction;

import java.io.Serializable;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DecoratedTicket implements Serializable {

	public DecoratedTicket() {
	}

	public DecoratedTicket(TicketType ticket) {
		this.ticket = ticket;
	}

	public List<TicketAttachment> getAttachments() {
		List<TicketAttachment> attachments = new ArrayList<>();

		if ((null != ticket.getAttachments()) &&
			(ticket.getAttachments(
			).size() > 0)) {

			for (AttachmentSummaryType attachmentType :
					ticket.getAttachments()) {

				TicketAttachment attachment = new TicketAttachment();

				Date creationDate = CommonUtil.getDateFromXmlCalendar(
					attachmentType.getCreationTimestamp());

				attachment.setAttachmentId(
					String.valueOf(attachmentType.getId()));
				attachment.setAttachmentDate(creationDate);
				attachment.setFilename(attachmentType.getFilename());
				attachment.setFileSize(
					String.valueOf(attachmentType.getSize()));

				if (null != attachmentType.getCreatedBy()) {
					String uploadedBy = "";

					if (StringUtils.isNotBlank(
							attachmentType.getCreatedBy(
							).getOrgShortName())) {

						if (cuscalShortNamesList.contains(
								attachmentType.getCreatedBy(
								).getOrgShortName(
								).toLowerCase())) {

							/*if (null != orgMap && !orgMap.isEmpty()) {
								attachment.setUploadedBy(orgMap
										.get(attachmentType.getCreatedBy()
												.getOrgShortName()));
							}*/
							attachment.setUploadedBy(cuscalOrgDefaultName);
						}
						else {
							uploadedBy =
								attachmentType.getCreatedBy(
								).getFname() + Constants.SPACE +
									attachmentType.getCreatedBy(
									).getSname();

							if ((null != orgMap) && !orgMap.isEmpty()) {
								uploadedBy +=
									Constants.SPACE + Constants.OF +
										Constants.SPACE +
											orgMap.get(
												attachmentType.getCreatedBy(
												).getOrgShortName());
							}

							attachment.setUploadedBy(uploadedBy);
						}
					}
					else {
						attachment.setUploadedBy(uploadedBy);
					}
				}

				attachments.add(attachment);
			}
		}

		return attachments;
	}

	/**
	 * Adding all the ticket Attributes and passing them to the page. We will
	 * use this to see if the user needs to step up to see certain values or
	 * not.
	 *
	 * @return List&lt;AttributesType&gt;
	 */
	public List<AttributesType> getAttributes() {
		List<AttributesType> attributes = new ArrayList<>();

		if ((null != ticket.getAttributes()) &&
			(ticket.getAttributes(
			).size() > 0)) {

			attributes.addAll(ticket.getAttributes());
		}

		return attributes;
	}

	public String getCuscalOrgDefaultName() {
		return cuscalOrgDefaultName;
	}

	public List<String> getCuscalShortNamesList() {
		return cuscalShortNamesList;
	}

	public String getCuscalTicketNumber() {
		return String.valueOf(ticket.getId());
	}

	public String getDescription() {
		String desc = ticket.getDescription();

		if (StringUtils.isNotBlank(desc)) {
			desc = desc.replaceAll("\u00A0", "");
			desc = desc.replace("\n", "<br>");

			return desc;
		}

		return null;
	}

	public String getEmail() {
		return ticket.getCreatedBy(
		).getEmail();
	}

	public String getExportIPM() {
		return ticket.getExportIPM();
	}

	public Boolean getIsNewTicket() {
		if (null != ticket.getLastSyncTimestamp()) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * getIsUnread()
	 * Is Unread is returned as true if it has been last updated by a cuscal user and the last read time is before
	 * last updated
	 */
	public Boolean getIsUnread() {
		if ((null != ticket.getLastUpdatedTimestamp()) &&
			(null != ticket.getLastReadTimestamp()) &&
			(null != ticket.getLastReadBy()) &&
			(null != ticket.getLastUpdatedBy()) &&
			StringUtils.isNotBlank(
				ticket.getLastReadBy(
				).getOrgShortName()) &&
			StringUtils.isNotBlank(
				ticket.getLastUpdatedBy(
				).getOrgShortName())) {

			if (cuscalShortNamesList.contains(
					ticket.getLastUpdatedBy(
					).getOrgShortName(
					).toLowerCase()) &&
				ticket.getLastReadTimestamp(
				).toGregorianCalendar(
				).before(
					ticket.getLastUpdatedTimestamp(
					).toGregorianCalendar()
				)) {

				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	public String getLastUpdatedOrganisation() {
		if ((null != orgMap) && !orgMap.isEmpty()) {
			if (null != ticket.getLastUpdatedBy()) {
				if (StringUtils.isNotBlank(
						ticket.getLastUpdatedBy(
						).getOrgShortName())) {

					return orgMap.get(
						ticket.getLastUpdatedBy(
						).getOrgShortName());
				}
			}
		}

		return null;
	}

	/**
	 *
	 * @return
	 */
	public List<TicketNote> getNotes() {
		List<TicketNote> notes = new ArrayList<>();

		if ((null != ticket.getNotes()) &&
			!ticket.getNotes(
			).isEmpty()) {

			for (int i = 0;
				 i < ticket.getNotes(
				 ).size(); i++) {

				NoteType noteType = ticket.getNotes(
				).get(
					i
				);
				TicketNote note = new TicketNote();

				XMLGregorianCalendar xmlCal = ticket.getNotes(
				).get(
					i
				).getCreatorReadOnTimestamp();

				GregorianCalendar cal = xmlCal.toGregorianCalendar();

				note.setNoteDate(cal.getTime());

				String userFullName = "";

				if (StringUtils.isNotBlank(noteType.getFirstName())) {
					userFullName = noteType.getFirstName();
				}

				if (StringUtils.isNotBlank(noteType.getLastName())) {
					userFullName += Constants.SPACE + noteType.getLastName();
				}

				note.setNoteUser(userFullName);
				note.setIsDeleted(noteType.isDeleted());

				note.setOrganisation(noteType.getOrgName());

				logger.debug(
					"getNotes - cuscalShortNamesList: " + cuscalShortNamesList);

				if (cuscalShortNamesList.contains(
						noteType.getOrgName(
						).toLowerCase())) {

					note.setIsCuscalUser(Boolean.TRUE);

					if (StringUtils.isNotBlank(noteType.getResolverGroup())) {
						note.setNoteUser(noteType.getResolverGroup());
					}
					else {
						note.setNoteUser(cuscalOrgDefaultName);
					}
				}
				else {
					note.setIsCuscalUser(Boolean.FALSE);
				}

				String comment = noteType.getNote(
				).trim();

				note.setNote(comment);

				note.setNoteId(noteType.getDestinationNoteId());

				if (noteType.isDeleted()) {
					note.setIsDeleted(Boolean.TRUE);
				}
				else {
					note.setIsDeleted(Boolean.FALSE);
				}

				notes.add(note);
			}
		}

		return notes;
	}

	public String getOrganisation() {
		if ((null != orgMap) && !orgMap.isEmpty()) {
			if (StringUtils.isNotBlank(ticket.getOrgShortName())) {
				return orgMap.get(ticket.getOrgShortName());
			}
		}

		return null;
	}

	public Map<String, String> getOrgMap() {
		return orgMap;
	}

	public String getProduct() {
		if (null != ticket.getProduct()) {
			return ticket.getProduct(
			).getName();
		}

		return null;
	}

	public String getReferenceNumber() {
		List<AttributesType> attributes = ticket.getAttributes();

		for (AttributesType attribute : attributes) {
			if (StringUtils.equalsIgnoreCase(
					"CLAIM.EXTERNAL_TICKET_ID", attribute.getType())) {

				return attribute.getValue();
			}
		}

		return null;
	}

	public String getRequestType() {
		if (null != ticket.getType()) {
			return ticket.getType(
			).getDescription();
		}

		return null;
	}

	public String getScItemId() {
		return scItemId;
	}

	public String getServiceRequestStatus() {
		for (AttributesType attributesType : getAttributes()) {
			if (Constants.ATTRIBUTES_TYPE_SERVICE_REQUEST_STATUS.equals(
					attributesType.getType())) {

				return attributesType.getValue();
			}
		}

		return "";
	}

	public String getSubmittedByFirstName() {
		if (null != ticket.getCreatedBy()) {
			if (StringUtils.isNotBlank(
					ticket.getCreatedBy(
					).getFname())) {

				return ticket.getCreatedBy(
				).getFname();
			}
		}

		return null;
	}

	public String getSubmittedByLastName() {
		if (null != ticket.getCreatedBy()) {
			if (StringUtils.isNotBlank(
					ticket.getCreatedBy(
					).getSname())) {

				return ticket.getCreatedBy(
				).getSname();
			}
		}

		return null;
	}

	public Date getSubmittedDate() {
		if (null != ticket.getCreationTimestamp()) {
			return ticket.getCreationTimestamp(
			).toGregorianCalendar(
			).getTime();
		}

		return null;
	}

	/*public String getTicketLastUpdatedBy() {
		if (null != ticket.getLastUpdatedBy()) {
			String lastUpdatedBy = "";

			if (StringUtils.isNotBlank(ticket.getLastUpdatedBy()
					.getOrgShortName())) {

				if (StringUtils.equalsIgnoreCase(Constants.CUSCAL,
						ticket.getLastUpdatedBy().getOrgShortName())) {

					lastUpdatedBy = orgMap.get(ticket.getLastUpdatedBy()
							.getOrgShortName());
				} else {
					if (StringUtils.isNotBlank(ticket.getLastUpdatedBy()
							.getFname())) {

						lastUpdatedBy = ticket.getLastUpdatedBy().getFname();
					}

					if (StringUtils.isNotBlank(ticket.getLastUpdatedBy()
							.getSname())) {

						lastUpdatedBy += Constants.SPACE
								+ ticket.getLastUpdatedBy().getSname();
					}

					if (null != orgMap && !orgMap.isEmpty()) {
						if (StringUtils.isNotBlank(ticket.getLastUpdatedBy()
								.getOrgShortName())) {

							lastUpdatedBy += Constants.SPACE
									+ Constants.OF
									+ Constants.SPACE
									+ orgMap.get(ticket.getLastUpdatedBy()
											.getOrgShortName());
						}
					}
				}
			}

			return lastUpdatedBy;
		}

		return null;
	}*/

	public TicketType getTicket() {
		return ticket;
	}

	public String getTicketNumber() {
		String ticketNumber = ticket.getDestination();

		if (StringUtils.isNotBlank(ticketNumber) && !"0".equals(ticketNumber)) {
			return ticketNumber;
		}

		return Constants.SUBMITTED;
	}

	//We are not using the Product table. Using the Type Table instead.
	public String getTicketStatus() {
		if (null != ticket.getCurrentStatus()) {
			if (StringUtils.isNotBlank(
					ticket.getCurrentStatus(
					).getName())) {

				return ticket.getCurrentStatus(
				).getName();
			}
		}

		return null;
	}

	public long getTicketStatusId() {
		if (null != ticket.getCurrentStatus()) {
			if (StringUtils.isNotBlank(
					ticket.getCurrentStatus(
					).getName())) {

				return ticket.getCurrentStatus(
				).getStatusId();
			}
		}

		return 0;
	}

	public String getTicketSubmittedBy() {
		if (null != ticket.getCreatedBy()) {
			String createdBy = "";

			if (StringUtils.isNotBlank(
					ticket.getCreatedBy(
					).getOrgShortName())) {

				if (cuscalShortNamesList.contains(
						ticket.getCreatedBy(
						).getOrgShortName(
						).toLowerCase())) {

					createdBy = cuscalOrgDefaultName;
				}
				else {
					createdBy = ticket.getCreatedBy(
					).getFname();

					if (StringUtils.isNotBlank(
							ticket.getCreatedBy(
							).getSname())) {

						createdBy +=
							Constants.SPACE +
								ticket.getCreatedBy(
								).getSname();
					}

					createdBy +=
						Constants.SPACE + Constants.OF + Constants.SPACE +
							orgMap.get(
								ticket.getCreatedBy(
								).getOrgShortName());
				}
			}

			return createdBy;
		}

		return null;
	}

	public String getTicketType() {
		if (null != ticket.getCategory()) {
			return ticket.getCategory(
			).getName();
		}

		return null;
	}

	public long getTicketTypeId() {
		if (null != ticket.getType()) {
			return ticket.getType(
			).getTypeId();
		}

		return 0;
	}

	/**
	 *
	 * @return
	 */
	public List<TicketTransaction> getTransactions() {
		List<TicketTransaction> transactions = new ArrayList<>();

		if ((null != ticket.getTransactions()) &&
			(ticket.getTransactions(
			).size() > 0)) {

			for (TransactionType transactionType : ticket.getTransactions()) {
				TicketTransaction transaction = new TicketTransaction();

				transaction.setTicketId(
					String.valueOf(transactionType.getTicketId()));
				transaction.setTransactionId(
					String.valueOf(transactionType.getTrlId()));

				Date businessDate = CommonUtil.getDateFromXmlCalendar(
					transactionType.getTrlBusinessDate());

				transaction.setTransactionBusinessDate(businessDate);

				Date localDate = CommonUtil.getDateFromXmlCalendar(
					transactionType.getTrlLocalDate());

				transaction.setTransactionLocalDate(localDate);

				transaction.setMaskedPan(transactionType.getMaskedPan());
				transaction.setAcquirerId(
					String.valueOf(transactionType.getAcquirerId()));
				transaction.setAcquirerName(transactionType.getAcquirerName());
				transaction.setIssuerId(transactionType.getIssuerId());
				transaction.setIssuerName(transactionType.getIssuerName());
				transaction.setTerminalId(transactionType.getTerminalId());
				transaction.setAtmPos(transactionType.getPosCondition());

				if (null != transactionType.getTransactionAmt()) {
					transaction.setTransactionAmount(
						new DecimalFormat(
							"0.00"
						).format(
							transactionType.getTransactionAmt()
						));
				}

				transaction.setTransactionAmountCurrency(
					transactionType.getTransactionCcy());

				if (null != transactionType.getBillingAmt()) {
					transaction.setCardholderAmount(
						new DecimalFormat(
							"0.00"
						).format(
							transactionType.getBillingAmt()
						));
				}

				transaction.setCardholderAmountCurrency(
					transactionType.getBillingCcy());

				String fullStan = String.valueOf(transactionType.getStan());

				if (StringUtils.isNotBlank(transactionType.getAdvStan())) {
					fullStan +=
						Constants.SPACE + Constants.SLASH + Constants.SPACE +
							transactionType.getAdvStan();
				}

				transaction.setStan(fullStan);

				transaction.setAtmFeeAmount(transactionType.getAtmFee());
				transaction.setClaimAmount(transactionType.getClaimAmt());
				transaction.setAmountReceived(transactionType.getAmtReceived());
				transaction.setMemeberName(transactionType.getMemberName());
				transaction.setMemberNumber(transactionType.getMemberNo());

				if (StringUtils.isNotBlank(transactionType.getArn())) {
					transaction.setArnId(transactionType.getArn());
				}

				if (StringUtils.isNotBlank(transactionType.getVisaId())) {
					transaction.setVisaTransactionId(
						transactionType.getVisaId());
				}

				transactions.add(transaction);
			}
		}

		return transactions;
	}

	public String getUpdatedByFirstName() {
		if (null != ticket.getLastUpdatedBy()) {
			return ticket.getLastUpdatedBy(
			).getFname();
		}

		return null;
	}

	public String getUpdatedByLastName() {
		if (null != ticket.getLastUpdatedBy()) {
			return ticket.getLastUpdatedBy(
			).getSname();
		}

		return null;
	}

	public Date getUpdatedDate() {
		if (null != ticket.getLastUpdatedTimestamp()) {
			return ticket.getLastUpdatedTimestamp(
			).toGregorianCalendar(
			).getTime();
		}

		return null;
	}

	public void setCuscalOrgDefaultName(String cuscalOrgDefaultName) {
		if (StringUtils.isBlank(this.cuscalOrgDefaultName))
			this.cuscalOrgDefaultName = cuscalOrgDefaultName;
	}

	public void setCuscalShortNamesList(List<String> cuscalShortNamesList) {
		if ((null == this.cuscalShortNamesList) ||
			this.cuscalShortNamesList.isEmpty())
			this.cuscalShortNamesList = cuscalShortNamesList;
	}

	public void setOrgMap(Map<String, String> orgMap) {
		if ((null == this.orgMap) || this.orgMap.isEmpty())
			this.orgMap = orgMap;
	}

	public void setScItemId(String scItemId) {
		this.scItemId = scItemId;
	}

	public void setTicket(TicketType ticket) {
		this.ticket = ticket;
	}

	private static Logger logger = Logger.getLogger(DecoratedTicket.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 3838707351369958053L;

	private String cuscalOrgDefaultName;
	private List<String> cuscalShortNamesList;
	private Map<String, String> orgMap;
	private String scItemId;
	private TicketType ticket;

}