//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.common;

import static au.com.cuscal.chargeback.common.Constants.CHARGEBACK_UPDATE_NOTE;
import static au.com.cuscal.chargeback.common.Constants.DATE_TIME_FORMAT_24HR;
import static au.com.cuscal.chargeback.common.Constants.GENERIC_PRODUCT_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_ARBITRATION_CHARGEBACK_ATTRIBUTES_LIST_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID_STR;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID_STR;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID_STR;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID_STR;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_FIRST_CHARGEBACK_ATTRIBUTES_LIST_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_REPORT_FRAUD_ATTRIBUTES_LIST_ID;
import static au.com.cuscal.chargeback.common.Constants.MASTERCARD_RETRIEVAL_REQUEST_ATTRIBUTES_LIST_ID;
import static au.com.cuscal.chargeback.common.Constants.SUBMITTED;

import au.com.cuscal.chargeback.forms.ChargebackForm;
import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.common.framework.service.request.domain.TicketDetails;
import au.com.cuscal.common.framework.service.request.domain.Transaction;
import au.com.cuscal.framework.webservices.selfservice.AttributesType;
import au.com.cuscal.framework.webservices.selfservice.ChargebackType;
import au.com.cuscal.framework.webservices.selfservice.NoteType;
import au.com.cuscal.framework.webservices.selfservice.RequestTypeType;
import au.com.cuscal.framework.webservices.selfservice.ServiceRequestResponseType;
import au.com.cuscal.framework.webservices.selfservice.StatusType;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TransactionType;
import au.com.cuscal.framework.webservices.selfservice.TypeType;
import au.com.cuscal.framework.webservices.selfservice.UserType;

import com.liferay.portal.kernel.model.User;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ChargebacksUtility {

	/**
	 * Convert the String date from one format to another. For e.g. convert from
	 * 2014-07-02T00:00:00.000 to 02/07/2014
	 *
	 * @param stringDate
	 * @param formatFrom
	 * @param formatTo
	 * @return
	 */
	public static String convertDateFromOneFormatToAnother(
		String stringDate, String formatFrom, String formatTo) {

		String date = null;
		logger.debug(
			"convertDateFromOneFormatToAnother - Converting: " + stringDate);

		if (StringUtils.isNotBlank(stringDate) &&
			StringUtils.isNotBlank(formatFrom) &&
			StringUtils.isNotBlank(formatTo)) {

			DateFormat fromFormat = new SimpleDateFormat(formatFrom);
			DateFormat toFormat = new SimpleDateFormat(formatTo);

			try {

				// The date we get from the database is a string and has the
				// timezone appended at the end of the date as +10:00. This is
				// something that we cannot figure out using the
				// SimpleDateFormat.

				if (stringDate.indexOf("+") != -1) {
					stringDate = stringDate.substring(
						0, stringDate.indexOf("+"));
				}
				else if (stringDate.indexOf("Z") != -1) {
					stringDate = stringDate.substring(
						0, stringDate.indexOf("Z"));
				}

				Date d = fromFormat.parse(stringDate);

				date = toFormat.format(d);
			}
			catch (Exception e) {
				logger.error(
					"convertDateFromOneFormatToAnother - " + e.getMessage(), e);

				return date;
			}
		}

		return date;
	}

	public static Transaction convertTransactionTypeToTransaction(
		TicketType ticketType) {

		TransactionType transactionType = ticketType.getTransactions(
		).get(
			0
		);
		Transaction transaction = new Transaction();

		if (null != transactionType) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			DecimalFormat decimalFormat = new DecimalFormat("0.00");

			transaction.setTransactionId(
				Long.valueOf(
					transactionType.getTrlId()
				).toString());

			transaction.setBusinessDate(
				dateFormat.format(
					transactionType.getTrlBusinessDate(
					).toGregorianCalendar(
					).getTime()));

			transaction.setTransactionLocalDate(
				dateFormat.format(
					transactionType.getTrlLocalDate(
					).toGregorianCalendar(
					).getTime()));

			transaction.setAcquirerId(transactionType.getAcquirerId());
			transaction.setAcquirerName(transactionType.getAcquirerName());
			transaction.setIssuerId(
				Long.valueOf(
					transactionType.getIssuerId()
				).toString());
			transaction.setIssuerName(transactionType.getIssuerName());

			logger.debug(
				"convertTransactionTypeToTransaction - cardholderAmount: " +
					transactionType.getBillingAmt(
					).doubleValue());

			transaction.setCardholderAmount(
				decimalFormat.format(transactionType.getBillingAmt()));

			transaction.setMaskedPan(transactionType.getMaskedPan());
			transaction.setMemberName(transactionType.getMemberName());
			transaction.setMemberNumber(transactionType.getMemberNo());
			transaction.setStan(
				Long.valueOf(
					transactionType.getStan()
				).toString());
			transaction.setTerminalId(transactionType.getTerminalId());
			transaction.setTransactionAmount(
				decimalFormat.format(transactionType.getTransactionAmt()));

			transaction.setTransactionAmountCurrency(
				transactionType.getTransactionCcy());
			logger.debug(
				"convertTransactionTypeToTransaction - amount currency: " +
					transaction.getTransactionAmountCurrency());
			transaction.setAtmPos(transactionType.getPosCondition());

			String arn = transactionType.getArn();

			if (StringUtils.isNotBlank(arn)) {
				transaction.setVisaTransactionId(arn);
			}
			else {
				transaction.setVisaTransactionId(transactionType.getVisaId());
			}

			transaction = setAdditionalTransactionInfo(
				ticketType, transactionType, transaction);
		}

		return transaction;
	}

	public static List<TicketDetails> decoratedServiceRequestList(
		List<ServiceRequestResponseType> tickets) {

		List<TicketDetails> ticketList = new ArrayList<>();

		for (ServiceRequestResponseType requestType : tickets) {
			logger.debug(
				"decoratedServiceRequestList - parsing ticket: " +
					requestType.getTicket(
					).getId());
			TicketDetails ticket = new TicketDetails();
			TicketType ticketType = requestType.getTicket();

			if (null != ticketType) {
				if (StringUtils.isBlank(ticketType.getDestination())) {
					ticket.setTicketNumber(SUBMITTED);
				}
				else {
					ticket.setTicketNumber(ticketType.getDestination());
				}

				TypeType ticketTypeType = ticketType.getType();

				if (null != ticketTypeType) {
					ticket.setTicketCategory(ticketTypeType.getDescription());
					ticket.setTicketTypeId(ticketTypeType.getTypeId());
				}

				UserType ticketUser = ticketType.getCreatedBy();

				if (null != ticketUser) {
					ticket.setTicketFirstName(ticketUser.getFname());
					ticket.setTicketLastName(ticketUser.getSname());
				}

				StatusType ticketStatus = ticketType.getCurrentStatus();

				if (null != ticketStatus) {
					ticket.setTicketStatus(ticketStatus.getName());
				}

				if (null != ticketType.getCreationTimestamp()) {
					GregorianCalendar cal = ticketType.getCreationTimestamp(
					).toGregorianCalendar();

					ticket.setTicketSubmittedDate(
						Utility.formatDateToString(
							cal.getTime(), DATE_TIME_FORMAT_24HR));
				}

				if (null != ticketType.getLastUpdatedTimestamp()) {
					GregorianCalendar cal2 = ticketType.getLastUpdatedTimestamp(
					).toGregorianCalendar();

					ticket.setTicketUpdateDate(
						Utility.formatDateToString(
							cal2.getTime(), DATE_TIME_FORMAT_24HR));
				}

				ticket.setTicketId(ticketType.getId());
			}

			ticketList.add(ticket);
		}

		return ticketList;
	}

	public static Date getDateFromXmlCalendar(XMLGregorianCalendar xmlCal) {
		Date date = null;

		if (null != xmlCal) {
			GregorianCalendar cal = xmlCal.toGregorianCalendar();
			date = new Date();
			date = cal.getTime();
		}

		return date;
	}

	public static String getFunctionCode(TicketType ticket) {
		TypeType typeType = ticket.getType();

		long typeId = typeType.getTypeId();
		String functionCode = "";

		if ((getServiceRequestTypeId(
				MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID_STR,
				MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID) == typeId) ||
			(getServiceRequestTypeId(
				MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID_STR,
				MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID) == typeId)) {

			functionCode = FunctionCode.FIRST_PRESENTMENT.getValue();
		}
		else if (getServiceRequestTypeId(
					MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID_STR,
					MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID) ==
						typeId) {

			// this only for validation

			functionCode = FunctionCode.SECOND_PRESENTMENT_FULL.getValue();
		}

		return functionCode;
	}

	public static long getServiceRequestTypeId(
		String propertyKey, long defaultValue) {

		try {
			return Long.parseLong(
				chargebackProperties.getChargebackProperty(
				).getProperty(
					propertyKey
				));
		}
		catch (Exception e) {
			//can ignore the exception default value would set

			return defaultValue;
		}
	}

	public static String getUCAFCollectionIndicatorDescription(
		String ucafIndicator) {

		String ucafIndicatorDescription = "";

		if (StringUtils.isNotBlank(ucafIndicator)) {
			if (UCAFCollectionIndicatorValue.NOT_SUPPORTED.getValue(
				).equals(
					ucafIndicator
				)) {

				ucafIndicatorDescription =
					Constants.UCAF_COLLECTION_INDICATOR_NOT_SUPPORTED_0;
			}
			else if (UCAFCollectionIndicatorValue.ATTEMPTED.getValue(
					).equals(
						ucafIndicator
					)) {

				ucafIndicatorDescription =
					Constants.UCAF_COLLECTION_INDICATOR_ATTEMPTED_1;
			}
			else if (UCAFCollectionIndicatorValue.AUTHENTICATED.getValue(
					).equals(
						ucafIndicator
					)) {

				ucafIndicatorDescription =
					Constants.UCAF_COLLECTION_INDICATOR_AUTHENTICATED_2;
			}
		}

		return ucafIndicatorDescription;
	}

	public static String setFunctionCodeDescription(String functionCode) {
		String functionCodeDescription = "";

		if (FunctionCode.FIRST_PRESENTMENT.getValue(
			).equals(
				functionCode
			)) {

			functionCodeDescription =
				Constants.
					MASTERCARD_FIRST_PRESENTMENT_FUNCTION_CODE_DESCRIPTION;
		}
		else if (FunctionCode.SECOND_PRESENTMENT_FULL.getValue(
				).equals(
					functionCode
				)) {

			functionCodeDescription =
				Constants.
					MASTERCARD_SECOND_PRESENTMENT_FULL_FUNCTION_CODE_DESCRIPTION;
		}
		else if (FunctionCode.SECOND_PRESENTMENT_PARTIAL.getValue(
				).equals(
					functionCode
				)) {

			functionCodeDescription =
				Constants.
					MASTERCARD_SECOND_PRESENTMENT_PARTIAL_FUNCTION_CODE_DESCRIPTION;
		}

		return functionCodeDescription;
	}

	public static RequestTypeType setupChargebackInformationForServiceRequest(
		ChargebackForm form) {

		logger.debug("setupChargebackInformationForServiceRequest - start");
		RequestTypeType requestType = setRequestTypeType(form.getType());
		ChargebackType chargeback = setChargebackType(form);

		requestType.setChargeback(chargeback);
		logger.debug("setupChargebackInformationForServiceRequest - end");

		return requestType;
	}

	/**
	 * This is for the edit chargeback functionality.
	 *
	 * @param form
	 * @param ticket
	 * @param user
	 * @return
	 */
	public static TicketType updateChargebackInformationForUpdateChargeback(
		ChargebackForm form, TicketType ticket, User user) {

		logger.debug("updateChargebackInformationForUpdateChargeback - start");
		RequestTypeType requestType = setRequestTypeType(form.getType());
		ChargebackType chargeback = setChargebackType(form);

		requestType.setChargeback(chargeback);

		// Clear all the current notes before adding new ones. This prevents the
		// old notes being added again and again.

		ticket.getNotes(
		).clear();
		//Add generic note
		NoteType genericUpdateNote = new NoteType();

		genericUpdateNote.setNote(CHARGEBACK_UPDATE_NOTE + user.getFullName());
		ticket.getNotes(
		).add(
			genericUpdateNote
		);

		//Add the notes to the TicketType

		if (StringUtils.isNotBlank(form.getNote())) {
			NoteType note = new NoteType();

			note.setNote(form.getNote());
			ticket.getNotes(
			).add(
				note
			);
		}

		ticket.setServiceRequest(requestType);

		logger.debug("updateChargebackInformationForUpdateChargeback - end");

		return ticket;
	}

	private static Transaction
		getAdditionalTransactionInfoFromAttributesTypeList(
			TicketType ticket, Transaction transaction) {

		List<AttributesType> attType = ticket.getAttributes();

		for (AttributesType att : attType) {
			String attributeType = att.getType();
			String attributeValue = att.getValue();

			if (StringUtils.equalsIgnoreCase(
					AttributesTypeType.POS_CONDITION_CODE.getValue(),
					attributeType)) {

				transaction.setPosConditionCode(attributeValue);
			}
			else if (StringUtils.equalsIgnoreCase(
						AttributesTypeType.POS_CONDITION_DESCRIPTION.getValue(),
						attributeType)) {

				transaction.setPosConditionDescription(attributeValue);
			}
			else if (StringUtils.equalsIgnoreCase(
						AttributesTypeType.POS_ENTRY_MODE_CODE.getValue(),
						attributeType)) {

				transaction.setPosEntryModeCode(attributeValue);
			}
			else if (StringUtils.equalsIgnoreCase(
						AttributesTypeType.POS_ENTRY_MODE_DESCRIPTION.
							getValue(),
						attributeType)) {

				transaction.setPosEntryModeDescription(attributeValue);
			}
			else if (StringUtils.equalsIgnoreCase(
						AttributesTypeType.UCAF_COLLECTION_INDICATOR.getValue(),
						attributeType)) {

				transaction.setUCAFCollectionIndicator(attributeValue);
				transaction.setUCAFCollectionIndicatorDescription(
					getUCAFCollectionIndicatorDescription(attributeValue));
			}
			else if (StringUtils.equalsIgnoreCase(
						AttributesTypeType.ELECTRONIC_COMMERCE_INDCIATOR.
							getValue(),
						attributeType)) {

				transaction.setElectronicCommerceIndciator(attributeValue);
			}
			else if (StringUtils.equalsIgnoreCase(
						AttributesTypeType.CENTRAL_SITE_BUSINESS_DATE.
							getValue(),
						attributeType)) {

				transaction.setCentralSiteBusinessDate(attributeValue);
			}
			else if (StringUtils.equalsIgnoreCase(
						AttributesTypeType.CHIP_CARD_PRESENT.getValue(),
						attributeType)) {

				transaction.setChipCardPresent(attributeValue);
			}
			else {
				if (!StringUtils.equalsIgnoreCase(
						AttributesTypeType.TRACK_2_DATA_SERVICE_CODE.getValue(),
						attributeType)) {

					continue;
				}

				transaction.setTrack2DataServiceCode(attributeValue);
			}
		}

		return transaction;
	}

	private static String getFunctionCodeFromAttributes(
		List<AttributesType> attributes) {

		String functionCode = "";

		for (AttributesType attributesType : attributes) {
			if (AttributesTypeType.FUNCTION_CODE.getValue(
				).equals(
					attributesType.getType()
				)) {

				functionCode = attributesType.getValue();

				break;
			}
		}

		return functionCode;
	}

	private static Transaction setAdditionalTransactionInfo(
		TicketType ticketType, TransactionType transactionType,
		Transaction transaction) {

		String functionCode = transactionType.getFunctionCode();

		if (StringUtils.isBlank(functionCode)) {
			functionCode = getFunctionCodeFromAttributes(
				ticketType.getAttributes());
		}

		if (StringUtils.isBlank(functionCode)) {
			functionCode = getFunctionCode(ticketType);
		}

		transaction.setFunctionCode(functionCode);
		String functionCodeDescription = setFunctionCodeDescription(
			functionCode);

		transaction.setFunctionCodeDescription(functionCodeDescription);

		return getAdditionalTransactionInfoFromAttributesTypeList(
			ticketType, transaction);
	}

	private static ChargebackType setChargebackType(ChargebackForm form) {
		ChargebackType chargeback = new ChargebackType();

		if (StringUtils.isNotBlank(form.getReasonCode())) {
			chargeback.setReason(Long.valueOf(form.getReasonCode()));
		}

		if (StringUtils.isNotBlank(form.getType())) {
			chargeback.setType(Long.valueOf(form.getType()));
		}

		return chargeback;
	}

	private static RequestTypeType setRequestTypeType(String type) {
		RequestTypeType requestType = new RequestTypeType();

		requestType.setProductId(GENERIC_PRODUCT_ID);

		if (MASTERCARD_RETRIEVAL_REQUEST_ATTRIBUTES_LIST_ID.equals(type)) {
			requestType.setTypeId(
				getServiceRequestTypeId(
					MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID_STR,
					MASTERCARD_DISPUTE_RETRIEVAL_REQUEST_TYPE_ID));
		}
		else if (MASTERCARD_FIRST_CHARGEBACK_ATTRIBUTES_LIST_ID.equals(type)) {
			requestType.setTypeId(
				getServiceRequestTypeId(
					MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID_STR,
					MASTERCARD_DISPUTE_FIRST_CHARGEBACK_TYPE_ID));
		}
		else if (MASTERCARD_ARBITRATION_CHARGEBACK_ATTRIBUTES_LIST_ID.equals(
					type)) {

			requestType.setTypeId(
				getServiceRequestTypeId(
					MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID_STR,
					MASTERCARD_DISPUTE_ARBITRATION_CHARGEBACK_TYPE_ID));
		}
		else if (MASTERCARD_REPORT_FRAUD_ATTRIBUTES_LIST_ID.equals(type)) {
			requestType.setTypeId(
				getServiceRequestTypeId(
					MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID_STR,
					MASTERCARD_DISPUTE_REPORT_FRAUD_TYPE_ID));
		}

		return requestType;
	}

	@Autowired
	@Qualifier("chargebackProperties")
	private static ChargebackProperties chargebackProperties;

	private static final Logger logger = Logger.getLogger(
		ChargebacksUtility.class);

}