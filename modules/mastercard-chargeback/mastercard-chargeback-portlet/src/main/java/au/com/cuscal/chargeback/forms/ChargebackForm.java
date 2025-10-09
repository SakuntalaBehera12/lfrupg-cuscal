//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.forms;

import static au.com.cuscal.chargeback.common.Constants.*;

import au.com.cuscal.chargeback.common.AttributesTypeType;
import au.com.cuscal.chargeback.common.ChargebackCodes;
import au.com.cuscal.chargeback.common.ChargebacksUtility;
import au.com.cuscal.framework.webservices.selfservice.AttributesType;
import au.com.cuscal.framework.webservices.selfservice.TicketType;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.springframework.web.multipart.MultipartFile;

public class ChargebackForm {

	public ChargebackForm() {
	}

	public ChargebackForm(TicketType ticket) {
		this.ticketId = Long.valueOf(
			ticket.getId()
		).toString();

		if (ticket.isUpdateTicket()) {
			List<AttributesType> attType = ticket.getAttributes();

			for (AttributesType att : attType) {
				String attributeType = att.getType();
				String attributeValue = att.getValue();
				//set the user selected value on edit screen

				if (StringUtils.equalsIgnoreCase(
						AttributesTypeType.MASTERCARD_DISPUTE_TYPE.getValue(),
						attributeType)) {

					this.type = Long.valueOf(
						att.getRequestCode()
					).toString();
				}
				else if (StringUtils.equalsIgnoreCase(
							AttributesTypeType.DISPUTE_AMOUNT.getValue(),
							attributeType)) {

					this.disputeAmount = attributeValue;
				}
				else if (StringUtils.equalsIgnoreCase(
							AttributesTypeType.DOCUMENT_INDICATOR.getValue(),
							attributeType)) {

					if (!FLAG_ONE.equals(attributeValue)) {
						continue;
					}

					this.documentIndicator = true;
				}
				else if (StringUtils.equalsIgnoreCase(
							AttributesTypeType.MASTERCARD_RETRIEVAL_REQUEST.
								getValue(),
							attributeType) ||
						 StringUtils.equalsIgnoreCase(
							 AttributesTypeType.MASTERCARD_FIRST_CHARGEBACK.
								 getValue(),
							 attributeType) ||
						 StringUtils.equalsIgnoreCase(
							 AttributesTypeType.
								 MASTERCARD_ARBITRATION_CHARGEBACK.getValue(),
							 attributeType)) {

					this.reasonCode = Long.valueOf(
						att.getRequestCode()
					).toString();
				}
				else {
					setAdditionalTransactionInformation(
						attributeType, attributeValue);
				}
			}
			//if no value inserted in attribute, then assign as default value

			if (StringUtils.isBlank(this.messageType)) {
				this.messageType =
					MASTERCARD_PRESENTMENT_MESSAGE_TYPE_TRANSACTION_LOG;
			}

			if (StringUtils.isBlank(this.functionCode)) {
				this.functionCode = ChargebacksUtility.getFunctionCode(ticket);
			}
		}
	}

	public String getCardHolderAmount() {
		return cardHolderAmount;
	}

	public String getCentralSiteBusinessDate() {
		return centralSiteBusinessDate;
	}

	public ChargebackCodes getChargebackCodes() {
		return chargebackCodes;
	}

	public String getChipCardPresent() {
		return chipCardPresent;
	}

	public String getDisputeAmount() {
		return disputeAmount;
	}

	public String getElectronicCommerceIndciator() {
		return electronicCommerceIndciator;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getFraudReportingDate() {
		return fraudReportingDate;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public String getFunctionCodeDescription() {
		return functionCodeDescription;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getNote() {
		return note;
	}

	public String getPosConditionCode() {
		return posConditionCode;
	}

	public String getPosConditionDescription() {
		return posConditionDescription;
	}

	public String getPosEntryModeCode() {
		return posEntryModeCode;
	}

	public String getPosEntryModeDescription() {
		return posEntryModeDescription;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public List<MultipartFile> getRequestAttachments() {
		return requestAttachments;
	}

	public String getTicketId() {
		return ticketId;
	}

	public String getTrack2DataServiceCode() {
		return track2DataServiceCode;
	}

	public String getType() {
		return type;
	}

	public String getUCAFCollectionIndicator() {
		return UCAFCollectionIndicator;
	}

	public String getUCAFCollectionIndicatorDescription() {
		return UCAFCollectionIndicatorDescription;
	}

	public boolean isDisclaimer() {
		return disclaimer;
	}

	public boolean isDocumentIndicator() {
		return documentIndicator;
	}

	public void setCardHolderAmount(String cardHolderAmount) {
		this.cardHolderAmount = cardHolderAmount;
	}

	public void setCentralSiteBusinessDate(String centralSiteBusinessDate) {
		this.centralSiteBusinessDate = centralSiteBusinessDate;
	}

	public void setChargebackCodes(ChargebackCodes chargebackCodes) {
		this.chargebackCodes = chargebackCodes;
	}

	public void setChipCardPresent(String chipCardPresent) {
		this.chipCardPresent = chipCardPresent;
	}

	public void setDisclaimer(boolean disclaimer) {
		this.disclaimer = disclaimer;
	}

	public void setDisputeAmount(String disputeAmount) {
		this.disputeAmount = disputeAmount;
	}

	public void setDocumentIndicator(boolean documentIndicator) {
		this.documentIndicator = documentIndicator;
	}

	public void setElectronicCommerceIndciator(
		String electronicCommerceIndciator) {

		this.electronicCommerceIndciator = electronicCommerceIndciator;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setFraudReportingDate(String fraudReportingDate) {
		this.fraudReportingDate = fraudReportingDate;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public void setFunctionCodeDescription(String functionCodeDescription) {
		this.functionCodeDescription = functionCodeDescription;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setPosConditionCode(String posConditionCode) {
		this.posConditionCode = posConditionCode;
	}

	public void setPosConditionDescription(String posConditionDescription) {
		this.posConditionDescription = posConditionDescription;
	}

	public void setPosEntryModeCode(String posEntryModeCode) {
		this.posEntryModeCode = posEntryModeCode;
	}

	public void setPosEntryModeDescription(String posEntryModeDescription) {
		this.posEntryModeDescription = posEntryModeDescription;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public void setRequestAttachments(List<MultipartFile> requestAttachments) {
		this.requestAttachments = requestAttachments;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public void setTrack2DataServiceCode(String track2DataServiceCode) {
		this.track2DataServiceCode = track2DataServiceCode;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUCAFCollectionIndicator(String uCAFCollectionIndicator) {
		UCAFCollectionIndicator = uCAFCollectionIndicator;
	}

	public void setUCAFCollectionIndicatorDescription(
		String uCAFCollectionIndicatorDescription) {

		UCAFCollectionIndicatorDescription = uCAFCollectionIndicatorDescription;
	}

	private void setAdditionalTransactionInformation(
		String attributeType, String attributeValue) {

		if (StringUtils.equalsIgnoreCase(
				AttributesTypeType.MESSAGE_TYPE.getValue(), attributeType)) {

			this.messageType = attributeValue;
		}
		else if (StringUtils.equalsIgnoreCase(
					AttributesTypeType.FUNCTION_CODE.getValue(),
					attributeType)) {

			this.functionCode = attributeValue;
			this.functionCodeDescription =
				ChargebacksUtility.setFunctionCodeDescription(attributeValue);
		}
		else if (StringUtils.equalsIgnoreCase(
					AttributesTypeType.POS_CONDITION_CODE.getValue(),
					attributeType)) {

			this.posConditionCode = attributeValue;
		}
		else if (StringUtils.equalsIgnoreCase(
					AttributesTypeType.POS_CONDITION_DESCRIPTION.getValue(),
					attributeType)) {

			this.posConditionDescription = attributeValue;
		}
		else if (StringUtils.equalsIgnoreCase(
					AttributesTypeType.POS_ENTRY_MODE_CODE.getValue(),
					attributeType)) {

			this.posEntryModeCode = attributeValue;
		}
		else if (StringUtils.equalsIgnoreCase(
					AttributesTypeType.POS_ENTRY_MODE_DESCRIPTION.getValue(),
					attributeType)) {

			this.posEntryModeDescription = attributeValue;
		}
		else if (StringUtils.equalsIgnoreCase(
					AttributesTypeType.UCAF_COLLECTION_INDICATOR.getValue(),
					attributeType)) {

			this.UCAFCollectionIndicator = attributeValue;
		}
		else if (StringUtils.equalsIgnoreCase(
					AttributesTypeType.CENTRAL_SITE_BUSINESS_DATE.getValue(),
					attributeType)) {

			this.centralSiteBusinessDate = attributeValue;
		}
		else if (StringUtils.equalsIgnoreCase(
					AttributesTypeType.CHIP_CARD_PRESENT.getValue(),
					attributeType)) {

			this.chipCardPresent = attributeValue;
		}
		else if (StringUtils.equalsIgnoreCase(
					AttributesTypeType.TRACK_2_DATA_SERVICE_CODE.getValue(),
					attributeType)) {

			this.track2DataServiceCode = attributeValue;
		}
	}

	private String cardHolderAmount;
	private String centralSiteBusinessDate;
	private ChargebackCodes chargebackCodes;
	private String chipCardPresent;
	private boolean disclaimer;
	private String disputeAmount;
	private boolean documentIndicator;
	private String electronicCommerceIndciator;
	private String errorMsg;
	private String fraudReportingDate;
	private String functionCode;
	private String functionCodeDescription;
	private String messageType;
	private String note;
	private String posConditionCode;
	private String posConditionDescription;
	private String posEntryModeCode;
	private String posEntryModeDescription;
	private String reasonCode;
	private List<MultipartFile> requestAttachments;
	private String ticketId;
	private String track2DataServiceCode;
	private String type;
	private String UCAFCollectionIndicator;
	private String UCAFCollectionIndicatorDescription;

}