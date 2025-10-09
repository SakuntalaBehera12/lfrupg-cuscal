package au.com.cuscal.chargeback.common;

import au.com.cuscal.chargeback.forms.AttemptToResolve;
import au.com.cuscal.chargeback.forms.CancellationInfo;
import au.com.cuscal.chargeback.forms.ChargebackForm;
import au.com.cuscal.chargeback.forms.CreditInformation;
import au.com.cuscal.chargeback.forms.ElaborationInfo;
import au.com.cuscal.chargeback.forms.ReturnInformation;
import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.common.framework.service.request.domain.TicketDetails;
import au.com.cuscal.common.framework.service.request.domain.Transaction;
import au.com.cuscal.framework.webservices.selfservice.AttemptToResolveType;
import au.com.cuscal.framework.webservices.selfservice.CancellationInfoType;
import au.com.cuscal.framework.webservices.selfservice.CertificationsType;
import au.com.cuscal.framework.webservices.selfservice.ChargebackType;
import au.com.cuscal.framework.webservices.selfservice.CreditInfoType;
import au.com.cuscal.framework.webservices.selfservice.ElaborationInfoType;
import au.com.cuscal.framework.webservices.selfservice.NoteType;
import au.com.cuscal.framework.webservices.selfservice.QuestionnaireInfoType;
import au.com.cuscal.framework.webservices.selfservice.RequestTypeType;
import au.com.cuscal.framework.webservices.selfservice.ReturnInfoType;
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

	/**
	 *
	 * @param transactionType
	 * @return Transaction
	 */
	public static Transaction convertTransactionTypeToTransaction(
		TransactionType transactionType) {

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
			transaction.setVisaTransactionId(transactionType.getArn());
		}

		return transaction;
	}

	/**
	 * Decorate the ServiceRequest list.
	 *
	 * @param  tickets <code>List&lt;ServiceRequestResponseType&gt;</code>
	 * @return List<TicketDetails>
	 */
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
					ticket.setTicketNumber(Constants.SUBMITTED);
				}
				else {
					ticket.setTicketNumber(ticketType.getDestination());
				}

				TypeType ticketTypeType = ticketType.getType();

				if (null != ticketTypeType) {
					ticket.setTicketCategory(ticketTypeType.getDescription());
					ticket.setTicketTypeId(ticketTypeType.getTypeId());
				}

				// Ticket Created by

				UserType ticketUser = ticketType.getCreatedBy();

				if (null != ticketUser) {
					ticket.setTicketFirstName(ticketUser.getFname());
					ticket.setTicketLastName(ticketUser.getSname());
				}

				// Ticket Status

				StatusType ticketStatus = ticketType.getCurrentStatus();

				if (null != ticketStatus) {
					ticket.setTicketStatus(ticketStatus.getName());
				}

				if (null != ticketType.getCreationTimestamp()) {
					GregorianCalendar cal = ticketType.getCreationTimestamp(
					).toGregorianCalendar();

					ticket.setTicketSubmittedDate(
						Utility.formatDateToString(
							cal.getTime(), Constants.DATE_TIME_FORMAT_24HR));
				}

				if (null != ticketType.getLastUpdatedTimestamp()) {
					GregorianCalendar cal2 = ticketType.getLastUpdatedTimestamp(
					).toGregorianCalendar();

					ticket.setTicketUpdateDate(
						Utility.formatDateToString(
							cal2.getTime(), Constants.DATE_TIME_FORMAT_24HR));
				}

				ticket.setTicketId(ticketType.getId());
			}

			ticketList.add(ticket);
		}

		return ticketList;
	}

	/**
	 *
	 * @param xmlCal
	 * @return
	 */
	public static Date getDateFromXmlCalendar(XMLGregorianCalendar xmlCal) {
		Date date = null;

		if (null != xmlCal) {
			GregorianCalendar cal = xmlCal.toGregorianCalendar();
			date = new Date();
			date = cal.getTime();
		}

		return date;
	}

	/**
	 *
	 * @param form
	 * @return
	 */
	public static RequestTypeType setupChargebackInformationForServiceRequest(
		ChargebackForm form) {

		logger.debug("setupChargebackInformationForServiceRequest - start");

		RequestTypeType requestType = new RequestTypeType();

		requestType.setProductId(Constants.GENERIC_PRODUCT_ID);
		requestType.setTypeId(Constants.CHARGEBACK_TYPE_ID);

		ChargebackType chargeback = new ChargebackType();

		if (StringUtils.isNotBlank(form.getReasonCode())) {
			chargeback.setReason(Long.valueOf(form.getReasonCode()));
		}

		if (StringUtils.isNotBlank(form.getType())) {
			chargeback.setType(Long.valueOf(form.getType()));
		}

		//Removed the Memeber Messages for the time being. This was causing an issue.
		/*if (StringUtils.isNotBlank(form.getMemberMessage())) {
			chargeback.setMemberMessage(Long.valueOf(form.getMemberMessage()));
		}

		if (StringUtils.isNotBlank(form.getUpdateMessage())) {
			chargeback.setUpdatedMemberMessage(form.getUpdateMessage());
		}*/

		//Fraud section

		if (Constants.REASON_FRAUD.equals(form.getType())) {
			String[] certifications = form.getCertifications();

			for (String certification : certifications) {
				CertificationsType certType = new CertificationsType();

				certType.setTypeId(Long.valueOf(certification));

				chargeback.getCertifications(
				).add(
					certType
				);
			}

			ChargebacksUtility.setupElaborationInformationForFraud(
				form, chargeback);
		}

		//Request for information section

		if (Constants.REASON_REQUEST_FOR_INFORMATION.equals(form.getType())) {
			if ((null != form.getRetrievalRequest()) &&
				StringUtils.isNotBlank(
					form.getRetrievalRequest(
					).getTc52Date())) {

				XMLGregorianCalendar tc52XmlDate =
					Utility.createXmlGregorianCalendarFromString(
						form.getRetrievalRequest(
						).getTc52Date(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				chargeback.setTc52Date(tc52XmlDate);
			}
		}

		//Authorisation section

		if (Constants.REASON_AUTHORISATION.equals(form.getType())) {
			String[] certifications = form.getCertifications();

			for (String certification : certifications) {
				CertificationsType certType = new CertificationsType();

				certType.setTypeId(Long.valueOf(certification));

				chargeback.getCertifications(
				).add(
					certType
				);
			}

			setupElaborationInformationForAuthorisation(form, chargeback);
		}

		//Processing Error

		if (Constants.REASON_PROCESSING_ERROR.equals(form.getType())) {
			String[] certifications = form.getCertifications();

			for (String certification : certifications) {
				CertificationsType certType = new CertificationsType();

				certType.setTypeId(Long.valueOf(certification));

				chargeback.getCertifications(
				).add(
					certType
				);
			}

			setupElaborationInformationForProcessingError(form, chargeback);

			setupAttemptToResolve(form, chargeback);
		}

		//Cancelled/Returned

		if (Constants.REASON_CANCELLED_RETURNED.equals(form.getType())) {
			setupElaborationInformationForCancelled(form, chargeback);

			setupCancellationInfo(form, chargeback);

			if (StringUtils.isNotBlank(
					chargeback.getElaborationInfo(
					).getDidYouCancel()) &&
				(chargeback.getElaborationInfo(
				).getDidYouCancel(
				).equalsIgnoreCase(
					"no"
				) ||
				 chargeback.getElaborationInfo(
				 ).getDidYouCancel(
				 ).equalsIgnoreCase(
					 "not applicable"
				 ))) {

				chargeback.setCancellationInfo(null);
			}

			setupAttemptToResolve(form, chargeback);
			setupCreditInfo(form, chargeback);

			if (Constants.CANCELLATION_WITHOUT_CREDIT_INFO.contains(
					form.getReasonCode())) {

				chargeback.setCreditInfo(null);
			}
		}

		//Non-Receipt Goods/Services

		if (Constants.REASON_NON_RECEIPT_GOODS.equals(form.getType())) {
			setupQuestionnaireInfo(form, chargeback);
			setupNonReceiptElaborationType(form, chargeback);
			setupAttemptToResolve(form, chargeback);

			if (null != chargeback.getElaborationInfo()) {
				if (StringUtils.isNotBlank(
						chargeback.getElaborationInfo(
						).getWasMerchandiseReturned()) &&
					chargeback.getElaborationInfo(
					).getWasMerchandiseReturned(
					).equals(
						"yes"
					)) {

					setupMerchandiseReturnInfo(form, chargeback);
				}

				if (StringUtils.isNotBlank(
						chargeback.getElaborationInfo(
						).getWasMerchandiseOrServiceCancelledNoReceipt()) &&
					chargeback.getElaborationInfo(
					).getWasMerchandiseOrServiceCancelledNoReceipt(
					).equalsIgnoreCase(
						"yes"
					)) {

					setupCancellationInfo(form, chargeback);
				}
			}

			if (form.getReasonCode(
				).equals(
					"69"
				)) { //Form 30
				chargeback.setQuestionnaireInfo(null);
			}
			else if (form.getReasonCode(
					).equals(
						"70"
					)) { //Form 90
				chargeback.setAttemptToResolve(null);
				chargeback.setReturnInfo(null);
				chargeback.setCancellationInfo(null);
			}
		}

		requestType.setChargeback(chargeback);

		logger.debug("setupChargebackInformationForServiceRequest - end");

		return requestType;
	}

	/**
	 * This is for the edit chargeback functionality.
	 *
	 * @param form
	 * @param ticket
	 * @return
	 */
	public static TicketType updateChargebackInformationForUpdateChargeback(
		ChargebackForm form, TicketType ticket, User user) {

		logger.debug("updateChargebackInformationForUpdateChargeback - start");

		RequestTypeType requestType = new RequestTypeType();

		requestType.setProductId(Constants.GENERIC_PRODUCT_ID);
		requestType.setTypeId(Constants.CHARGEBACK_TYPE_ID);

		ChargebackType chargeback = new ChargebackType();

		if (StringUtils.isNotBlank(form.getReasonCode())) {
			chargeback.setReason(Long.valueOf(form.getReasonCode()));
		}

		if (StringUtils.isNotBlank(form.getType())) {
			chargeback.setType(Long.valueOf(form.getType()));
		}

		//Removed the Memeber Messages for the time being. This was causing an issue.
		/*if (StringUtils.isNotBlank(form.getMemberMessage())) {
			chargeback.setMemberMessage(Long.valueOf(form.getMemberMessage()));
		}

		if (StringUtils.isNotBlank(form.getUpdateMessage())) {
			chargeback.setUpdatedMemberMessage(form.getUpdateMessage());
		}*/

		//Fraud section

		if (Constants.REASON_FRAUD.equals(form.getType())) {
			String[] certifications = form.getCertifications();

			for (String certification : certifications) {
				CertificationsType certType = new CertificationsType();

				certType.setTypeId(Long.valueOf(certification));

				chargeback.getCertifications(
				).add(
					certType
				);
			}

			setupElaborationInformationForFraud(form, chargeback);
		}

		//Request for information section

		if (Constants.REASON_REQUEST_FOR_INFORMATION.equals(form.getType())) {
			if ((null != form.getRetrievalRequest()) &&
				(null != form.getRetrievalRequest(
				).getTc52Date())) {

				XMLGregorianCalendar tc52XmlDate =
					Utility.createXmlGregorianCalendarFromString(
						form.getRetrievalRequest(
						).getTc52Date(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				chargeback.setTc52Date(tc52XmlDate);
			}
		}

		//Authorisation section

		if (Constants.REASON_AUTHORISATION.equals(form.getType())) {
			String[] certifications = form.getCertifications();

			for (String certification : certifications) {
				CertificationsType certType = new CertificationsType();

				certType.setTypeId(Long.valueOf(certification));

				chargeback.getCertifications(
				).add(
					certType
				);
			}

			setupElaborationInformationForAuthorisation(form, chargeback);
		}

		//Processing Error section

		if (Constants.REASON_PROCESSING_ERROR.equals(form.getType())) {
			String[] certifications = form.getCertifications();

			for (String certification : certifications) {
				CertificationsType certType = new CertificationsType();

				certType.setTypeId(Long.valueOf(certification));

				chargeback.getCertifications(
				).add(
					certType
				);
			}

			setupElaborationInformationForProcessingError(form, chargeback);

			setupAttemptToResolve(form, chargeback);
		}

		//Cancelled/Returned

		if (Constants.REASON_CANCELLED_RETURNED.equals(form.getType())) {
			setupElaborationInformationForCancelled(form, chargeback);

			setupCancellationInfo(form, chargeback);

			if (StringUtils.isNotBlank(
					chargeback.getElaborationInfo(
					).getDidYouCancel()) &&
				(chargeback.getElaborationInfo(
				).getDidYouCancel(
				).equalsIgnoreCase(
					"no"
				) ||
				 chargeback.getElaborationInfo(
				 ).getDidYouCancel(
				 ).equalsIgnoreCase(
					 "not applicable"
				 ))) {

				chargeback.setCancellationInfo(null);
			}

			setupAttemptToResolve(form, chargeback);
			setupCreditInfo(form, chargeback);

			if (Constants.CANCELLATION_WITHOUT_CREDIT_INFO.contains(
					form.getReasonCode())) {

				chargeback.setCreditInfo(null);
			}
		}

		//Non Receipt Goods

		if (Constants.REASON_NON_RECEIPT_GOODS.equals(form.getType())) {
			setupQuestionnaireInfo(form, chargeback);
			setupNonReceiptElaborationType(form, chargeback);
			setupAttemptToResolve(form, chargeback);

			if (null != chargeback.getElaborationInfo()) {
				if (StringUtils.isNotBlank(
						chargeback.getElaborationInfo(
						).getWasMerchandiseReturned()) &&
					chargeback.getElaborationInfo(
					).getWasMerchandiseReturned(
					).equals(
						"yes"
					)) {

					setupMerchandiseReturnInfo(form, chargeback);
				}

				if (StringUtils.isNotBlank(
						chargeback.getElaborationInfo(
						).getWasMerchandiseOrServiceCancelledNoReceipt()) &&
					chargeback.getElaborationInfo(
					).getWasMerchandiseOrServiceCancelledNoReceipt(
					).equalsIgnoreCase(
						"yes"
					)) {

					setupCancellationInfo(form, chargeback);
				}
			}

			if (form.getReasonCode(
				).equals(
					"69"
				)) { //Form 30
				chargeback.setQuestionnaireInfo(null);
			}
			else if (form.getReasonCode(
					).equals(
						"70"
					)) { //Form 90
				chargeback.setAttemptToResolve(null);
				chargeback.setReturnInfo(null);
				chargeback.setCancellationInfo(null);
			}
		}

		requestType.setChargeback(chargeback);

		// Clear all the current notes before adding new ones. This prevents the
		// old notes being added again and again.

		ticket.getNotes(
		).clear();
		//Add generic note
		NoteType genericUpdateNote = new NoteType();

		genericUpdateNote.setNote(
			Constants.CHARGEBACK_UPDATE_NOTE + user.getFullName());
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

	/**
	 * Attempt to resolve section
	 *
	 * @param form
	 * @param type
	 */
	private static void setupAttemptToResolve(
		ChargebackForm form, ChargebackType chargeback) {

		if ((null != form.getAttemptToResolve()) &&
			!"0".equals(
				form.getAttemptToResolve(
				).getResolveWithMerchant())) {

			AttemptToResolveType resolveType = new AttemptToResolveType();

			AttemptToResolve resolve = form.getAttemptToResolve();

			if (StringUtils.isNotBlank(resolve.getResolveWithMerchant())) {
				resolveType.setAttemptToResolve(
					resolve.getResolveWithMerchant());

				if (StringUtils.equalsIgnoreCase(
						resolve.getResolveWithMerchant(), "yes")) {

					if (StringUtils.isNotBlank(resolve.getErrorDateContact())) {
						XMLGregorianCalendar date =
							Utility.createXmlGregorianCalendarFromString(
								resolve.getErrorDateContact(),
								Constants.DATE_FORMAT, Boolean.FALSE);

						resolveType.setDateOfContact(date);
					}

					if (StringUtils.isNotBlank(resolve.getNameContact())) {
						resolveType.setNameOfContact(resolve.getNameContact());
					}

					if (StringUtils.isNotBlank(resolve.getMethodContact())) {
						resolveType.setMethodOfContact(
							resolve.getMethodContact());
					}

					if (StringUtils.isNotBlank(resolve.getMerchantResponse())) {
						resolveType.setMerchantsResponse(
							resolve.getMerchantResponse());
					}
				}
				else if (StringUtils.equalsIgnoreCase(
							resolve.getResolveWithMerchant(), "no")) {

					resolveType.setNotApplicableLocalLaw(
						resolve.isNotApplicableLocalLaw());

					if (StringUtils.isNotBlank(
							resolve.getExplainWhyNotResolve())) {

						resolveType.setExplainWhyNotResolve(
							resolve.getExplainWhyNotResolve());
					}
				}
			}

			chargeback.setAttemptToResolve(resolveType);
		}
	}

	/**
	 * Cancellation section
	 *
	 * @param form
	 * @param chargeback
	 */
	private static void setupCancellationInfo(
		ChargebackForm form, ChargebackType chargeback) {

		if (null != form.getCancellationInfo()) {
			CancellationInfoType cancellationInfoType =
				new CancellationInfoType();
			CancellationInfo cancellationInfo = form.getCancellationInfo();

			if (StringUtils.isNotBlank(
					cancellationInfo.getDateRecurringTransCancelled())) {

				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						cancellationInfo.getDateRecurringTransCancelled(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				cancellationInfoType.setDateRecurringTransCancelled(date);
			}

			if (StringUtils.isNotBlank(
					cancellationInfo.getDateAcquirerNotified())) {

				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						cancellationInfo.getDateAcquirerNotified(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				cancellationInfoType.setDateAcquirerNotified(date);
			}

			if (StringUtils.isNotBlank(
					cancellationInfo.getDatePreviousChargeback())) {

				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						cancellationInfo.getDatePreviousChargeback(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				cancellationInfoType.setDatePreviousChargeback(date);
			}

			if (StringUtils.isNotBlank(cancellationInfo.getDateCancelled())) {
				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						cancellationInfo.getDateCancelled(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				cancellationInfoType.setCancelDate(date);
			}

			if (StringUtils.isNotBlank(cancellationInfo.getCancelledArn())) {
				cancellationInfoType.setArn(cancellationInfo.getCancelledArn());
			}

			if (StringUtils.isNotBlank(
					cancellationInfo.getReasonCancelledRecurring())) {

				cancellationInfoType.setReasonCancelledRecurring(
					cancellationInfo.getReasonCancelledRecurring());
			}

			if (StringUtils.isNotBlank(cancellationInfo.getReasonCancelled())) {
				cancellationInfoType.setReasonCancelled(
					cancellationInfo.getReasonCancelled());
			}

			if (StringUtils.isNotBlank(cancellationInfo.getSpokeWith())) {
				cancellationInfoType.setSpokeWith(
					cancellationInfo.getSpokeWith());
			}

			if (StringUtils.isNotBlank(
					cancellationInfo.getWasCancellationCodeGiven())) {

				cancellationInfoType.setWasCancellationCodeGiven(
					Long.valueOf(
						cancellationInfo.getWasCancellationCodeGiven()));
			}

			if (StringUtils.isNotBlank(
					cancellationInfo.getCancellationCode())) {

				cancellationInfoType.setCancellationCode(
					cancellationInfo.getCancellationCode());
			}

			cancellationInfoType.setCancellationCodeGivenButNotRetained(
				cancellationInfo.isCancellationCodeGivenButNotRetained());
			cancellationInfoType.setReservationWithin72Hours(
				cancellationInfo.isReservationWithin72Hours());
			cancellationInfoType.setMerchantNotAcceptCancellation(
				cancellationInfo.isMerchantNotAcceptCancellation());

			if (StringUtils.isNotBlank(
					cancellationInfo.getExplainWhyCancellationCodeNotGiven())) {

				cancellationInfoType.setExplainWhyCancellationCodeNotGiven(
					cancellationInfo.getExplainWhyCancellationCodeNotGiven());
			}

			if (StringUtils.isNotBlank(
					cancellationInfo.
						getWasCardholderGivenCancellationPolicy())) {

				cancellationInfoType.setWasCardholderGivenCancellationPolicy(
					Long.valueOf(
						cancellationInfo.
							getWasCardholderGivenCancellationPolicy()));
			}

			if (StringUtils.isNotBlank(
					cancellationInfo.getWhatWasCancellationPolicy())) {

				cancellationInfoType.setWhatWasCancellationPolicy(
					cancellationInfo.getWhatWasCancellationPolicy());
			}

			chargeback.setCancellationInfo(cancellationInfoType);
		}
	}

	/**
	 *
	 * @param form
	 * @param chargeback
	 */
	private static void setupCreditInfo(
		ChargebackForm form, ChargebackType chargeback) {

		if (null != form.getCreditInfo()) {
			CreditInfoType infoType = new CreditInfoType();

			CreditInformation info = form.getCreditInfo();

			infoType.setCreditVoucherGiven(
				Long.valueOf(info.getCreditVoucherGiven()));

			if (info.getCreditVoucherGiven(
				).equalsIgnoreCase(
					"354"
				)) {

				infoType.setCreditVoucherDated(
					Long.valueOf(info.getCreditVoucherDated()));

				if (info.getCreditVoucherDated(
					).equalsIgnoreCase(
						"357"
					)) {

					XMLGregorianCalendar date =
						Utility.createXmlGregorianCalendarFromString(
							info.getDateCreditVoucher(), Constants.DATE_FORMAT,
							Boolean.FALSE);

					infoType.setDateCreditVoucher(date);
				}

				infoType.setCreditAmount(info.getCreditAmount());
				infoType.setInvoiceNumber(info.getInvoiceNumber());
			}

			chargeback.setCreditInfo(infoType);
		}
	}

	/**
	 *
	 * @param form
	 * @param chargeback
	 */
	private static void setupElaborationInformationForAuthorisation(
		ChargebackForm form, ChargebackType chargeback) {

		if (null != form.getElaborationInfo()) {
			ElaborationInfoType elaborationInfoType = new ElaborationInfoType();

			ElaborationInfo elaborationInfo = form.getElaborationInfo();

			if (StringUtils.isNotBlank(elaborationInfo.getAuthDate())) {
				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						elaborationInfo.getAuthDate(), Constants.DATE_FORMAT,
						Boolean.FALSE);

				elaborationInfoType.setCardDate(date);
			}

			if (StringUtils.isNotBlank(elaborationInfo.getAuthCrbDate())) {
				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						elaborationInfo.getAuthCrbDate(), Constants.DATE_FORMAT,
						Boolean.FALSE);

				elaborationInfoType.setCrbDate(date);
			}

			// Add CRB Regions.

			List<String> regions = new ArrayList<>();

			if ((null != elaborationInfo.getAuthCrbRegions()) &&
				(elaborationInfo.getAuthCrbRegions().length > 0)) {

				for (String region : elaborationInfo.getAuthCrbRegions()) {
					if (!StringUtils.equalsIgnoreCase(
							Constants.CHECKBOX_SELECT_ALL_VALUE, region)) {

						regions.add(region);
					}
				}
			}

			elaborationInfoType.getCrbRegions(
			).addAll(
				regions
			);

			// End adding CRB Regions.

			elaborationInfoType.setAuthRequiredNoTransDate(
				elaborationInfo.isAuthRequiredNoTransDate());

			elaborationInfoType.setAuthObtainedIncorrectData(
				elaborationInfo.isAuthObtainedIncorrectData());

			if (elaborationInfo.isAuthObtainedIncorrectData()) {
				if (StringUtils.isNotBlank(
						elaborationInfo.
							getAuthObtainedIncorrectDataExplain())) {

					elaborationInfoType.setAuthObtainedIncorrectDataExplain(
						elaborationInfo.getAuthObtainedIncorrectDataExplain());
				}
			}

			elaborationInfoType.setAuthTransExceedsAuthAmount(
				elaborationInfo.isAuthTransExceedsAuthAmount());

			if (elaborationInfo.isAuthTransExceedsAuthAmount()) {
				if (StringUtils.isNotBlank(
						elaborationInfo.
							getAuthTransExceedsAuthAmountExplain())) {

					elaborationInfoType.setAuthTransExceedsAuthAmountExplain(
						elaborationInfo.getAuthTransExceedsAuthAmountExplain());
				}
			}

			elaborationInfoType.setAccountNumberOnExceptionFile(
				elaborationInfo.isAuthAccountNumberOnExceptionFile());

			elaborationInfoType.setAuthNonMatchingMCC(
				elaborationInfo.isAuthNonMatchingMCC());

			if (elaborationInfo.isAuthNonMatchingMCC()) {
				if (StringUtils.isNotBlank(
						elaborationInfo.getAuthMCCInClearing())) {

					elaborationInfoType.setAuthMCCInClearing(
						elaborationInfo.getAuthMCCInClearing());
				}

				if (StringUtils.isNotBlank(
						elaborationInfo.getAuthMCCInSystemAuthorisation())) {

					elaborationInfoType.setAuthMCCInSystemAuthorisation(
						elaborationInfo.getAuthMCCInSystemAuthorisation());
				}
			}

			if (StringUtils.isNotBlank(elaborationInfo.getAuthDeclinedDate())) {
				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						elaborationInfo.getAuthDeclinedDate(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				elaborationInfoType.setAuthDeclinedDate(date);
			}

			elaborationInfoType.setCardStatusChanged(
				elaborationInfo.isCardStatusChanged());

			if (StringUtils.isNotBlank(elaborationInfo.getCardExpiredDate())) {
				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						elaborationInfo.getCardExpiredDate(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				elaborationInfoType.setCardExpiredDate(date);
			}

			// Set this for the request.

			chargeback.setElaborationInfo(elaborationInfoType);
		}
	}

	/**
	 *
	 * @param form
	 * @param chargeback
	 */
	private static void setupElaborationInformationForCancelled(
		ChargebackForm form, ChargebackType chargeback) {

		if (null != form.getElaborationInfo()) {
			ElaborationInfoType elabType = new ElaborationInfoType();
			ElaborationInfo elab = form.getElaborationInfo();

			elabType.setWhatWasPurchased(
				Long.valueOf(elab.getWhatWasPurchased()));
			//Merchandise section

			if (elab.getWhatWasPurchased(
				).equals(
					"294"
				)) {

				elabType.setWhatWasWrong(
					Long.valueOf(elab.getMerchandiseWhatWasWrong()));

				if (elab.getMerchandiseWhatWasWrong(
					).equals(
						"296"
					)) {

					elabType.setExplainWhatWasWrong(
						elab.getMerchandiseDetailsNotAsDescribed());
				}
				else if (elab.getMerchandiseWhatWasWrong(
						).equals(
							"297"
						)) {

					elabType.setExplainWhatWasWrong(elab.getDetailsDefective());
				}
				else if (elab.getMerchandiseWhatWasWrong(
						).equals(
							"298"
						)) {

					elabType.setExplainWhatWasWrong(elab.getExpertIdentified());

					if (StringUtils.isNotBlank(elab.getDateInformed())) {
						XMLGregorianCalendar date =
							Utility.createXmlGregorianCalendarFromString(
								elab.getDateInformed(), Constants.DATE_FORMAT,
								Boolean.FALSE);

						elabType.setDateMerchandiseCounterfeit(date);
					}

					elabType.setCertificationCounterfeit(
						elab.isCounterfeitCertification());
					elabType.setDocumentationCounterfeit(
						elab.isCounterfeitDocumentation());
				}
				else if (elab.getMerchandiseWhatWasWrong(
						).equals(
							"299"
						)) {

					elabType.setExplainWhatWasWrong(
						elab.getMerchandiseExplainTermsOfSale());

					elabType.setAttachingDocumentationTOS(
						elab.isMerchandiseAttachingIncidentReporting());
				}
				else if (elab.getMerchandiseWhatWasWrong(
						).equals(
							"378"
						)) {

					elabType.setExplainWhatWasWrong(
						elab.getMerchandiseQualityExplain());

					elabType.setPayToWorkRedone(elab.getWorkRedone());

					if (elab.getWorkRedone(
						).equalsIgnoreCase(
							"yes"
						)) {

						elabType.setWhereWorkRedone(elab.getWhereWorkRedone());

						if (StringUtils.isNotBlank(elab.getWhenWorkRedone())) {
							XMLGregorianCalendar date =
								Utility.createXmlGregorianCalendarFromString(
									elab.getWhenWorkRedone(),
									Constants.DATE_FORMAT, Boolean.FALSE);

							elabType.setWhenWorkRedone(date);
						}

						elabType.setAmountWorkRedone(
							elab.getAmountWorkRedone());
					}
				}

				elabType.setDescriptionWhatWasPurchased(
					elab.getMerchandiseOrdered());
				elabType.setWasMerchandiseReturned(elab.getMerchandiseReturn());

				if (elab.getMerchandiseReturn(
					).equalsIgnoreCase(
						"yes"
					)) {

					setupMerchandiseReturnInfo(form, chargeback);
				}
				else if (elab.getMerchandiseReturn(
						).equalsIgnoreCase(
							"no"
						)) {

					elabType.setExplainWhyNotReturned(
						elab.getWhyMerchandiseNotReturned());
					elabType.setDidMerchantRefuseReturn(
						Long.valueOf(elab.getMerchantRefuse()));
					elabType.setMerchantRefusedReason(
						Long.valueOf(elab.getMerchantRefusedReason()));
					/*elabType.setMerchantRefusedAuthorisation(elab.isMerchantRefusedAuthorisation());
					elabType.setMerchantRefuseMerchandise(elab.isMerchantRefusedReturn());
					elabType.setMerchantAdviseNotReturn(elab.isMerchantAdvisedNoReturn());*/
				}
			}
			else if (elab.getWhatWasPurchased(
					).equals(
						"295"
					)) {

				//Services section
				elabType.setWhatWasWrong(
					Long.valueOf(elab.getServicesWhatWasWrong()));

				if (elab.getServicesWhatWasWrong(
					).equals(
						"296"
					)) {

					elabType.setExplainWhatWasWrong(
						elab.getServicesDetailsNotAsDescribed());
				}
				else if (elab.getServicesWhatWasWrong(
						).equals(
							"299"
						)) {

					elabType.setExplainWhatWasWrong(
						elab.getServicesExplainTermsOfSale());

					elabType.setAttachingDocumentationTOS(
						elab.isServicesAttachingIncidentReporting());
				}

				if (StringUtils.isNotBlank(elab.getExpectedServicesDate())) {
					XMLGregorianCalendar date =
						Utility.createXmlGregorianCalendarFromString(
							elab.getExpectedServicesDate(),
							Constants.DATE_FORMAT, Boolean.FALSE);

					elabType.setExpectedServicesOn(date);
				}

				elabType.setDescriptionWhatWasPurchased(
					elab.getServicesOrdered());
			}

			elabType.setMerchantBilledMore(elab.isMerchantBilledMore());

			elabType.setDidYouCancel(elab.getDidCancel());

			if (!elab.getOriginalCreditNotAccepted(
				).equals(
					"0"
				)) {

				elabType.setOriginalCreditNotAccepted(
					Long.valueOf(elab.getOriginalCreditNotAccepted()));
			}

			chargeback.setElaborationInfo(elabType);
		}
	}

	/**
	 * @param form
	 * @param chargeback
	 */
	private static void setupElaborationInformationForFraud(
		ChargebackForm form, ChargebackType chargeback) {

		if (null != form.getElaborationInfo()) {
			ElaborationInfoType elaborationInfoType = new ElaborationInfoType();

			if (StringUtils.isNotBlank(
					form.getElaborationInfo(
					).getCardDate())) {

				XMLGregorianCalendar cardXmlDate =
					Utility.createXmlGregorianCalendarFromString(
						form.getElaborationInfo(
						).getCardDate(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				elaborationInfoType.setCardDate(cardXmlDate);
			}

			if (StringUtils.isNotBlank(
					form.getElaborationInfo(
					).getFraudDate())) {

				XMLGregorianCalendar fraudXmlDate =
					Utility.createXmlGregorianCalendarFromString(
						form.getElaborationInfo(
						).getFraudDate(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				elaborationInfoType.setFraudDate(fraudXmlDate);
			}

			if (StringUtils.isNotBlank(
					form.getElaborationInfo(
					).getFraudAdviceNo())) {

				elaborationInfoType.setFraudNo(
					form.getElaborationInfo(
					).getFraudAdviceNo());
			}

			if (StringUtils.isNotBlank(
					form.getElaborationInfo(
					).getCardStatus())) {

				elaborationInfoType.setCardStatus(
					Long.valueOf(
						form.getElaborationInfo(
						).getCardStatus()));
			}

			//Add the voucher request date to the request.

			if ((null != form.getRetrievalRequest()) &&
				StringUtils.isNotBlank(
					form.getRetrievalRequest(
					).getTc52Date())) {

				XMLGregorianCalendar tc52XmlDate =
					Utility.createXmlGregorianCalendarFromString(
						form.getRetrievalRequest(
						).getTc52Date(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				chargeback.setTc52Date(tc52XmlDate);
			}

			if (StringUtils.isNotBlank(form.getFraudReportingDate())) {
				XMLGregorianCalendar fraudDate =
					Utility.createXmlGregorianCalendarFromString(
						form.getFraudReportingDate(), Constants.DATE_FORMAT,
						Boolean.FALSE);

				chargeback.setFraudReportingDate(fraudDate);
			}

			elaborationInfoType.setMsrtt(
				form.getElaborationInfo(
				).isMsrtt());
			elaborationInfoType.setAcctNoFictiousOrInvalid(
				form.getElaborationInfo(
				).isAcctNoFictitiousOrInvalid());

			elaborationInfoType.setVisaEuropeOnlyData(
				form.getElaborationInfo(
				).isVisaEuropeOnlyData());

			if (form.getElaborationInfo(
				).isVisaEuropeOnlyData()) {

				if (StringUtils.isNotBlank(
						form.getElaborationInfo(
						).getVisaEuropeOnlyDate())) {

					elaborationInfoType.setVisaEuropeOnlyDate(
						form.getElaborationInfo(
						).getVisaEuropeOnlyDate());
				}
			}

			chargeback.setElaborationInfo(elaborationInfoType);
		}
	}

	/**
	 *
	 * @param form
	 * @param chargeback
	 */
	private static void setupElaborationInformationForProcessingError(
		ChargebackForm form, ChargebackType chargeback) {

		if (null != form.getElaborationInfo()) {
			ElaborationInfoType elaborationInfoType = new ElaborationInfoType();

			ElaborationInfo elaborationInfo = form.getElaborationInfo();

			//Transaction date

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorTransDate())) {

				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						elaborationInfo.getProcessingErrorTransDate(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				elaborationInfoType.setTransactionDate(date);
			}

			//Settlement date

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorSettleDate())) {

				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						elaborationInfo.getProcessingErrorSettleDate(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				elaborationInfoType.setSettlementDate(date);
			}

			elaborationInfoType.setCounterfeit(
				elaborationInfo.isProcessingErrorCounterfeit());

			elaborationInfoType.setAccountClosed(
				elaborationInfo.isProcessingErrorAccountClosed());

			elaborationInfoType.setOtherFraud(
				elaborationInfo.isProcessingErrorOtherFraud());

			elaborationInfoType.setAccountNumberOnExceptionFile(
				elaborationInfo.
					isProcessingErrorAccountNumberNegativeResponse());

			elaborationInfoType.setPresentmentMoreThan180Days(
				elaborationInfo.isProcessingErrorPresentmentOlder180Days());

			elaborationInfoType.setTransProcessedMoreThan10Days(
				elaborationInfo.
					isProcessingErrorTransProcessedAfterTransactionDate());

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorTransactionIncorrect())) {

				elaborationInfoType.setTransactionIncorrect(
					Long.valueOf(
						elaborationInfo.
							getProcessingErrorTransactionIncorrect()));
			}

			elaborationInfoType.
				setTransactionCurrencyDiffFromTransmittedCurrency(
					elaborationInfo.
						isProcessingErrorTransactionCurrencyDifferentTransmitted());

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorCurrencyTransaction())) {

				elaborationInfoType.setTransactionCurrency(
					elaborationInfo.getProcessingErrorCurrencyTransaction());
			}

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorCurrencyTransmitted())) {

				elaborationInfoType.setTransmittedCurrency(
					elaborationInfo.getProcessingErrorCurrencyTransmitted());
			}

			elaborationInfoType.setTransactionCountryDifferentFromPortalCountry(
				elaborationInfo.isProcessingErrorTransactionCountryDifferent());

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorTransactionCountry())) {

				elaborationInfoType.setTransactionCountry(
					elaborationInfo.getProcessingErrorTransactionCountry());
			}

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorPortalCountry())) {

				elaborationInfoType.setPortalCountry(
					elaborationInfo.getProcessingErrorPortalCountry());
			}

			elaborationInfoType.setNonMatchingAccountNumber(
				elaborationInfo.isProcessingErrorNonMatchingAccountNumber());

			elaborationInfoType.setNonExistingAccount(
				elaborationInfo.isProcessingErrorNonExistingAccount());

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorARN())) {

				elaborationInfoType.setArn(
					elaborationInfo.getProcessingErrorARN());
			}

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorTransactionDate())) {

				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						elaborationInfo.getProcessingErrorTransactionDate(),
						Constants.DATE_FORMAT, Boolean.FALSE);

				elaborationInfoType.setTransactionDate(date);
			}

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorPaymentMethod())) {

				elaborationInfoType.setPaymentMethod(
					Long.valueOf(
						elaborationInfo.getProcessingErrorPaymentMethod()));
			}

			elaborationInfoType.setProofOfOtherMeans(
				elaborationInfo.isProcessingErrorProofOtherMeans());

			elaborationInfoType.setCardStatusChanged(
				elaborationInfo.isProcessingErrorCardStatusChanged());

			if (StringUtils.isNotBlank(
					elaborationInfo.getProcessingErrorDueTo())) {

				elaborationInfoType.setProcessingErrorDueTo(
					Long.valueOf(elaborationInfo.getProcessingErrorDueTo()));
			}

			if (StringUtils.isNotBlank(elaborationInfo.getInfoIncorrect())) {
				elaborationInfoType.setInfoIncorrect(
					Long.valueOf(elaborationInfo.getInfoIncorrect()));

				if (StringUtils.equalsIgnoreCase(
						elaborationInfo.getInfoIncorrect(), "286")) {

					if (StringUtils.isNotBlank(
							elaborationInfo.getAmountIncorrect())) {

						elaborationInfoType.setAmountIncorrect(
							Long.valueOf(elaborationInfo.getAmountIncorrect()));
					}

					if (StringUtils.isNotBlank(
							elaborationInfo.getIncorrectAmountWas())) {

						elaborationInfoType.setIncorrectAmountWas(
							elaborationInfo.getIncorrectAmountWas());
					}

					if (StringUtils.isNotBlank(
							elaborationInfo.getIncorrectAmountShouldBe())) {

						elaborationInfoType.setIncorrectAmountShouldBe(
							elaborationInfo.getIncorrectAmountShouldBe());
					}
				}
				else if (StringUtils.equalsIgnoreCase(
							elaborationInfo.getInfoIncorrect(), "287")) {

					if (StringUtils.isNotBlank(
							elaborationInfo.getCardNumber())) {

						elaborationInfoType.setCardNumber(
							elaborationInfo.getCardNumber());
					}

					if (StringUtils.isNotBlank(
							elaborationInfo.getNameOnVoucher())) {

						elaborationInfoType.setNameOnVoucher(
							elaborationInfo.getNameOnVoucher());
					}
				}
			}

			chargeback.setElaborationInfo(elaborationInfoType);
		}
	}

	/**
	 *
	 * @param form
	 * @param chargeback
	 */
	private static void setupMerchandiseReturnInfo(
		ChargebackForm form, ChargebackType chargeback) {

		ReturnInfoType returnType = new ReturnInfoType();
		ReturnInformation retInfo = form.getReturnInfo();

		if (null != retInfo) {
			if (StringUtils.isNotBlank(retInfo.getReturnDate())) {
				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						retInfo.getReturnDate(), Constants.DATE_FORMAT,
						Boolean.FALSE);

				returnType.setReturnDate(date);
			}

			if (StringUtils.isNotBlank(retInfo.getReturnMethod())) {
				returnType.setReturnMethod(
					Long.valueOf(retInfo.getReturnMethod()));

				if (retInfo.getReturnMethod(
					).equals(
						"327"
					)) {

					if (StringUtils.isNotBlank(retInfo.getOtherMethod())) {
						returnType.setOtherReturnMethod(
							retInfo.getOtherMethod());
					}
				}
			}

			if (StringUtils.isNotBlank(retInfo.getShippingNumber())) {
				returnType.setShippingNumber(retInfo.getShippingNumber());
			}

			if (StringUtils.isNotBlank(retInfo.getWhoSignedPackage())) {
				returnType.setWhoSignedPackage(retInfo.getWhoSignedPackage());
			}

			if (StringUtils.isNotBlank(retInfo.getDeliveryAddress())) {
				returnType.setDeliveryAddress(retInfo.getDeliveryAddress());
			}

			if (StringUtils.isNotBlank(retInfo.getMerchantReceivedOn())) {
				XMLGregorianCalendar date =
					Utility.createXmlGregorianCalendarFromString(
						retInfo.getMerchantReceivedOn(), Constants.DATE_FORMAT,
						Boolean.FALSE);

				returnType.setMerchantReceivedOn(date);
			}

			if (StringUtils.isNotBlank(
					retInfo.getReturnAuthorisationNumber())) {

				returnType.setReturnAuthorisationNumber(
					retInfo.getReturnAuthorisationNumber());
			}

			if (StringUtils.isNotBlank(retInfo.getReturnInstruction())) {
				returnType.setReturnInstructions(
					retInfo.getReturnInstruction());
			}

			chargeback.setReturnInfo(returnType);
		}
	}

	/**
	 *
	 * @param form
	 * @param chargeback
	 */
	private static void setupNonReceiptElaborationType(
		ChargebackForm form, ChargebackType chargeback) {

		ElaborationInfoType elabType = new ElaborationInfoType();
		ElaborationInfo elabInfo = form.getElaborationInfo();

		if (null != elabInfo) {
			if (StringUtils.isNotBlank(
					elabInfo.getQuestionnaireWhatWasPurchased()) &&
				!elabInfo.equals("0")) {

				elabType.setWhatWasPurchased(
					Long.valueOf(elabInfo.getQuestionnaireWhatWasPurchased()));

				if (StringUtils.equals(
						"362", elabInfo.getQuestionnaireWhatWasPurchased())) { //Merchandise section

					if (StringUtils.isNotBlank(
							elabInfo.getQuestionnaireMerchandiseOrdered())) {

						elabType.setDescriptionWhatWasPurchased(
							elabInfo.getQuestionnaireMerchandiseOrdered());
					}

					if (StringUtils.isNotBlank(
							elabInfo.
								getQuestionnaireMerchandiseDateExpected())) {

						XMLGregorianCalendar date =
							Utility.createXmlGregorianCalendarFromString(
								elabInfo.
									getQuestionnaireMerchandiseDateExpected(),
								Constants.DATE_FORMAT, Boolean.FALSE);

						elabType.setDateExpectedReceipt(date);
					}

					elabType.setMerchandiseOrServiceNotReceived(
						elabInfo.isQuestionnaireMerchandiseNotReceived());

					if (elabInfo.isQuestionnaireMerchandiseNotReceived()) {
						if (StringUtils.isNotBlank(
								elabInfo.
									getQuestionnaireMerchandiseReceivedOn())) {

							XMLGregorianCalendar date =
								Utility.createXmlGregorianCalendarFromString(
									elabInfo.
										getQuestionnaireMerchandiseReceivedOn(),
									Constants.DATE_FORMAT, Boolean.FALSE);

							elabType.setDateReceived(date);
						}
					}

					if (StringUtils.isNotBlank(
							elabInfo.getQuestionnaireMerchandiseReturn()) &&
						!elabInfo.getQuestionnaireMerchandiseReturn(
						).equals(
							"0"
						)) {

						elabType.setWasMerchandiseReturned(
							elabInfo.getQuestionnaireMerchandiseReturn());

						if (elabInfo.getQuestionnaireMerchandiseReturn(
							).equalsIgnoreCase(
								"no"
							)) {

							if (StringUtils.isNotBlank(
									elabInfo.
										getQuestionnaireWhereIsMerchandise())) {

								elabType.setWhereIsMerchandise(
									elabInfo.
										getQuestionnaireWhereIsMerchandise());
							}
						}
					}

					if (StringUtils.isNotBlank(
							elabInfo.
								getQuestionnaireMerchandiseAgreedLocation())) {

						elabType.setAgreedMerchandiseDeliveryAddress(
							elabInfo.
								getQuestionnaireMerchandiseAgreedLocation());
					}

					if (StringUtils.isNotBlank(
							elabInfo.
								getQuestionnaireWasMerchandiseCancelledNonReceipt())) {

						elabType.setWasMerchandiseOrServiceCancelledNoReceipt(
							elabInfo.
								getQuestionnaireWasMerchandiseCancelledNonReceipt());
					}
				}
				else if (StringUtils.equals(
							"295",
							elabInfo.getQuestionnaireWhatWasPurchased())) { //Services section

					if (StringUtils.isNotBlank(
							elabInfo.getQuestionnaireServicesOrdered())) {

						elabType.setDescriptionWhatWasPurchased(
							elabInfo.getQuestionnaireServicesOrdered());
					}

					elabType.setMerchantUnwilling(
						elabInfo.isQuestionnaireMerchantUnwilling());

					if (StringUtils.isNotBlank(
							elabInfo.getQuestionnaireServicesDateExpected())) {

						XMLGregorianCalendar date =
							Utility.createXmlGregorianCalendarFromString(
								elabInfo.getQuestionnaireServicesDateExpected(),
								Constants.DATE_FORMAT, Boolean.FALSE);

						elabType.setDateExpectedReceipt(date);
					}

					elabType.setMerchandiseOrServiceNotReceived(
						elabInfo.isQuestionnaireServicesNotReceived());

					if (elabInfo.isQuestionnaireServicesNotReceived()) {
						if (StringUtils.isNotBlank(
								elabInfo.
									getQuestionnaireServicesReceivedOn())) {

							XMLGregorianCalendar date =
								Utility.createXmlGregorianCalendarFromString(
									elabInfo.
										getQuestionnaireServicesReceivedOn(),
									Constants.DATE_FORMAT, Boolean.FALSE);

							elabType.setDateReceived(date);
						}
					}

					if (StringUtils.isNotBlank(
							elabInfo.
								getQuestionnaireWasServicesCancelledNonReceipt())) {

						elabType.setWasMerchandiseOrServiceCancelledNoReceipt(
							elabInfo.
								getQuestionnaireWasServicesCancelledNonReceipt());
					}
				}
			}

			chargeback.setElaborationInfo(elabType);
		}
	}

	/**
	 * Questionnaire section
	 *
	 * @param form
	 * @param type
	 */
	private static void setupQuestionnaireInfo(
		ChargebackForm form, ChargebackType type) {

		if (null != form.getQuestionInfo()) {
			QuestionnaireInfoType questionnaireInfo =
				new QuestionnaireInfoType();

			if (form.getQuestionInfo(
				).isCashOrLoadValueNotReceived()) {

				questionnaireInfo.setCashOrLoadValueNotReceived(
					form.getQuestionInfo(
					).isCashOrLoadValueNotReceived());
			}

			if (form.getQuestionInfo(
				).isPartialCashOrLoadValueReceived()) {

				questionnaireInfo.setPartialCashOrLoadValueReceived(
					form.getQuestionInfo(
					).isPartialCashOrLoadValueReceived());
			}

			if (StringUtils.isNotBlank(
					form.getQuestionInfo(
					).getAmountReceived())) {

				questionnaireInfo.setAmountReceived(
					form.getQuestionInfo(
					).getAmountReceived());
			}

			if (StringUtils.isNotBlank(
					form.getQuestionInfo(
					).getAmountRequested())) {

				questionnaireInfo.setAmountRequested(
					form.getQuestionInfo(
					).getAmountRequested());
			}

			if (StringUtils.isNotBlank(
					form.getQuestionInfo(
					).getCashLoadInformation())) {

				questionnaireInfo.setCashLoadInformation(
					Long.valueOf(
						form.getQuestionInfo(
						).getCashLoadInformation()));
			}

			type.setQuestionnaireInfo(questionnaireInfo);
		}
	}

	private static final Logger logger = Logger.getLogger(
		ChargebacksUtility.class);

}