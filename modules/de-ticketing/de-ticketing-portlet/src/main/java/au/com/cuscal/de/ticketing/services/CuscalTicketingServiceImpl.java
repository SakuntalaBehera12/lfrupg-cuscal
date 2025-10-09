//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.services;

import au.com.cuscal.common.framework.dxp.service.request.commons.Utility;
import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.de.ticketing.common.CommonUtil;
import au.com.cuscal.de.ticketing.common.Constants;
import au.com.cuscal.de.ticketing.common.DEProperties;
import au.com.cuscal.de.ticketing.domain.DEListBoxData;
import au.com.cuscal.de.ticketing.forms.AttributesInformation;
import au.com.cuscal.de.ticketing.forms.RequestContactInformation;
import au.com.cuscal.de.ticketing.forms.RequestTypeInformation;
import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.audit.xml.XmlMessage;
import au.com.cuscal.framework.webservices.client.CustomerSelfServiceService;
import au.com.cuscal.framework.webservices.client.EntityService;
import au.com.cuscal.framework.webservices.liferay.dxp.LiferayClientUtil;
import au.com.cuscal.framework.webservices.selfservice.AddSimpleServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.AddSimpleServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.AttachmentDataType;
import au.com.cuscal.framework.webservices.selfservice.AttributesType;
import au.com.cuscal.framework.webservices.selfservice.ContactType;
import au.com.cuscal.framework.webservices.selfservice.RequestTypeType;
import au.com.cuscal.framework.webservices.selfservice.SimpleAttributesType;
import au.com.cuscal.framework.webservices.selfservice.SimpleServiceRequestRequestType;
import au.com.cuscal.framework.webservices.selfservice.SpxClaimTransactionSearchRequest;
import au.com.cuscal.framework.webservices.selfservice.SpxClaimTransactionSearchResponse;
import au.com.cuscal.framework.webservices.selfservice.SpxPaymentTransactionSearchRequest;
import au.com.cuscal.framework.webservices.selfservice.SpxPaymentTransactionSearchResponse;
import au.com.cuscal.framework.webservices.selfservice.SpxTransactionSearchRequest;
import au.com.cuscal.framework.webservices.selfservice.SpxTransactionSearchResponse;
import au.com.cuscal.framework.webservices.selfservice.SpxTransactionType;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.selfservice.UpdateSimpleServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.UpdateSimpleServiceRequestResponse;

import com.liferay.portal.kernel.model.User;

import java.io.IOException;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service(value = Constants.CUSCAL_TICKETING_SERVICE)
public class CuscalTicketingServiceImpl implements CuscalTicketingService {

	@Override
	public String addServiceRequest(
			final ServiceRequestForm form, final TktRequestHeader header,
			final PortletContext portletContext)
		throws Exception {

		CuscalTicketingServiceImpl.logger.debug(
			(Object)"addServiceRequest - start");
		String message = null;
		AddSimpleServiceRequestResponse response = null;
		final AddSimpleServiceRequestRequest request =
			new AddSimpleServiceRequestRequest();
		XmlMessage xmlMessage = new XmlMessage();

		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);
		boolean isSuccess = Boolean.FALSE;

		if (null != header) {
			request.setHeader(header);

			if (null != form) {
				final SimpleServiceRequestRequestType requestType =
					this.setupServiceRequest(form);
				request.setSimpleServiceRequestRequest(requestType);

				try {
					response = this.ticketingService.addSimpleServiceRequest(
						request);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}

				if (null != response) {
					if (null != response.getHeader()) {
						final String statusCode = response.getHeader(
						).getStatusCode();

						if (StringUtils.equalsIgnoreCase(
								"SUCCESS", statusCode)) {

							message = statusCode;
							isSuccess = Boolean.TRUE;
							this.createXmlMessageForServiceRequest(
								xmlMessage, form, statusCode,
								String.valueOf(
									response.getServiceRequestResponse(
									).getTicket(
									).getId()));
							CuscalTicketingServiceImpl.logger.debug(
								(Object)
									("addServiceRequest - Successfully added ticket. Status Code:" +
										statusCode));
						}
						else {
							message = statusCode;
							this.createXmlMessageForServiceRequest(
								xmlMessage, form, statusCode, null);
							CuscalTicketingServiceImpl.logger.error(
								(Object)
									("addServiceRequest - Response header is not successful. Status Code:" +
										statusCode));
						}
					}
					else {
						CuscalTicketingServiceImpl.logger.error(
							(Object)
								"addServiceRequest - Response header is null");
					}
				}
				else {
					CuscalTicketingServiceImpl.logger.error(
						(Object)"addServiceRequest - Response is null");
				}
			}
			else {
				CuscalTicketingServiceImpl.logger.error(
					(Object)
						"addServiceRequest - ServiceRequestForm is null. Cannot add ticket");
			}
		}
		else {
			CuscalTicketingServiceImpl.logger.error(
				(Object)
					"addServiceRequest - RequestHeader is null. Cannot add ticket");
		}

		if (isSuccess) {
			CommonUtil.auditSuccessXmlMessage(
				xmlMessage, "CSS/TICKET/ADD", portletContext.getRequest(),
				portletContext.getResponse());
		}
		else {
			CommonUtil.auditFailXmlMessage(
				xmlMessage, "CSS/TICKET/ADD", portletContext.getRequest(),
				portletContext.getResponse());
		}

		CuscalTicketingServiceImpl.logger.debug(
			(Object)"addServiceRequest - end");

		return message;
	}

	public void createXmlMessageForServiceRequest(
		final XmlMessage xmlMessage,
		final ServiceRequestForm serviceRequestForm, final String statusCode,
		final String ticketId) {

		if ((null != serviceRequestForm) &&
			(null != serviceRequestForm.getRequestTypeInformation())) {

			if (StringUtils.isNotBlank(
					serviceRequestForm.getRequestTypeInformation(
					).getRequestTypeId())) {

				xmlMessage.addParameter(
					"requestTypeId",
					serviceRequestForm.getRequestTypeInformation(
					).getRequestTypeId());
			}

			if ((null != serviceRequestForm.getAttributesInformation()) &&
				StringUtils.isNotBlank(
					serviceRequestForm.getAttributesInformation(
					).getClientRefNumber())) {

				xmlMessage.addParameter(
					"orgBSB",
					serviceRequestForm.getAttributesInformation(
					).getBsb());
			}
		}

		if (ticketId != null) {
			xmlMessage.addResult("ticketId", ticketId);
		}

		xmlMessage.addResult("statusCode", statusCode);
	}

	public EntityService getEntityService() {
		return this.entityService;
	}

	@Override
	public String getOrgShortName(final Long liferayOrgId) {
		CuscalTicketingServiceImpl.logger.debug(
			(Object)"getOrgShortName - start");
		String orgShortName = null;

		try {
			orgShortName = LiferayClientUtil.getOrgShortName(
				this.entityService, (long)liferayOrgId);
		}
		catch (Exception e) {
			CuscalTicketingServiceImpl.logger.error(
				(Object)
					("getOrgShortName - Could not get the OrgShortName from LiferayClientUtil. " +
						e.getMessage()),
				(Throwable)e);
		}

		CuscalTicketingServiceImpl.logger.debug(
			(Object)("getOrgShortName - end [" + orgShortName + "]"));

		return orgShortName;
	}

	public CustomerSelfServiceService getTicketingService() {
		return this.ticketingService;
	}

	@Override
	public AttributesInformation setupAttributesInformation(
		final TicketType ticket, final boolean isOfi) {

		final Map<String, String> amountMap = new HashMap<>();
		final Map<String, String> dateMap = new HashMap<>();
		final Map<String, String> refMap = new HashMap<>();
		final List<String> amountList = new ArrayList<>();
		final List<String> dateList = new ArrayList<>();
		final List<String> refList = new ArrayList<>();
		final List<String> mistakenResultList = new ArrayList<>();
		final DEListBoxData deListBoxData = CommonUtil.populateDEListBoxData(
			this.deProperties);
		final AttributesInformation attributesInformation =
			new AttributesInformation();
		final List<AttributesType> attType = ticket.getAttributes();

		for (final AttributesType att : attType) {
			if (StringUtils.equals("DE.CLIENT_REF_NUMBER", att.getType())) {
				attributesInformation.setClientRefNumber(att.getValue());
			}

			if (StringUtils.equals("DE.REQUEST_FOR", att.getType())) {
				attributesInformation.setRequestFor(att.getValue());
			}

			if (StringUtils.equals("DE.FI_INSTITUTION_NAME", att.getType())) {
				attributesInformation.setFinInstName(att.getValue());
			}

			if (StringUtils.equals(
					"DE.FI_INSTITUTION_USER_ID", att.getType())) {

				attributesInformation.setFinInstUserId(att.getValue());
			}

			if (StringUtils.contains(att.getType(), "DE.PROCESSED_DATE")) {
				dateMap.put(
					att.getType(
					).substring(
						"DE.PROCESSED_DATE".length()
					),
					this.formatDate(att.getValue()));
			}

			if (StringUtils.equals("DE.DESTINATION_BSB", att.getType())) {
				attributesInformation.setDestinationBSB(att.getValue());
			}

			if (StringUtils.equals(
					"DE.DESTINATION_ACCOUNT_NUMBER", att.getType())) {

				attributesInformation.setDestinationAccountNumber(
					att.getValue());
			}

			if (StringUtils.contains(att.getType(), "DE.TRANSACTION_AMOUNT")) {
				amountMap.put(
					att.getType(
					).substring(
						"DE.TRANSACTION_AMOUNT".length()
					),
					this.formatAmount(att.getValue()));
			}

			if (StringUtils.equals("DE.TRANSACTION_CODE", att.getType())) {
				attributesInformation.setTransactionCode(att.getValue());
			}

			if (StringUtils.equals("DE.ACCOUNT_TITLE", att.getType())) {
				attributesInformation.setAccountTitle(att.getValue());
			}

			if (StringUtils.contains(
					att.getType(), "DE.DESTINATION_LODGEMENT_REF")) {

				refMap.put(
					att.getType(
					).substring(
						"DE.DESTINATION_LODGEMENT_REF".length()
					),
					att.getValue());
			}

			if (StringUtils.equals(
					"DE.FI_INSTITUTION_TRACE_BSB", att.getType())) {

				attributesInformation.setFinInstTraceBSBNumber(att.getValue());
			}

			if (StringUtils.equals("DE.REMITTER_ACCOUNT_NUM", att.getType())) {
				attributesInformation.setAccountNumberRemitter(att.getValue());
			}

			if (StringUtils.equals("DE.REMITTER_NAME", att.getType())) {
				attributesInformation.setRemitterName(att.getValue());
			}

			if (StringUtils.equals(
					"DE.DESTINATION_ADDITIONAL_INFO", att.getType())) {

				attributesInformation.setDestinationAdditionalInfo(
					att.getValue());
			}

			if (StringUtils.equals("DE.RECEIVER_ACCOUNT_NAME", att.getType())) {
				attributesInformation.setReceiverAccountName(att.getValue());
			}

			if (StringUtils.equals("DE.RECEIVER_BSB", att.getType())) {
				attributesInformation.setReceiverBSBNumber(att.getValue());
			}

			if (StringUtils.equals(
					"DE.RECEIVER_ACCOUNT_NUMBER", att.getType())) {

				attributesInformation.setReceiverAccountNumber(att.getValue());
			}

			if (StringUtils.equals(
					"DE.RECEIVER_ACCOUNT_NUMBER", att.getType())) {

				attributesInformation.setReceiverAccountNumber(att.getValue());
			}

			if (StringUtils.equals(
					"DE.RECEIVER_LODGEMENT_REF", att.getType())) {

				attributesInformation.setReceiverLodgementRef(att.getValue());
			}

			if (StringUtils.equals("DE.REFERENCE", att.getType())) {
				attributesInformation.setReference(att.getValue());
			}

			if (StringUtils.equals("DE.TIME_FRAME", att.getType())) {
				attributesInformation.setTimeframe(att.getValue());
			}

			if (StringUtils.equals("DE.REASON_FOR_DISPUTE", att.getType())) {
				attributesInformation.setIntendedAccountOrWrongPayee(
					att.getValue());
			}

			if (StringUtils.equals(
					"DE.INTENDED_ACCOUNT_BSB_NUMBER", att.getType())) {

				attributesInformation.setIntendedAccountBSBNumber(
					att.getValue());
			}

			if (StringUtils.equals(
					"DE.INTENDED_ACCOUNT_NUMBER", att.getType())) {

				attributesInformation.setIntendedAccountNumber(att.getValue());
			}

			if (StringUtils.equals(
					"DE.INTENDED_ACCOUNT_TITLE", att.getType())) {

				attributesInformation.setIntendedAccountTitle(att.getValue());
			}

			if (StringUtils.equals(
					"DE.CUSTOMER_DECLARATION_ATTACHED", att.getType())) {

				attributesInformation.setCustomerDeclarationAttached(
					att.getValue());
			}

			if (StringUtils.equals("DE.CLAIM_REASON", att.getType())) {
				attributesInformation.setClaimReason(att.getValue());
			}

			if (StringUtils.equals("DE.CURRENT_DDR_STATUS", att.getType())) {
				attributesInformation.setCurrentDDRStatus(att.getValue());
			}

			if (StringUtils.equals("DE.BATCH_NUMBER", att.getType())) {
				attributesInformation.setBatchNumber(att.getValue());
			}

			if (StringUtils.equals(
					"DE.DRAWING_IN_THE_NAME_OF", att.getType())) {

				attributesInformation.setDrawingInTheNameOf(att.getValue());
			}

			if (StringUtils.equals("DE.RESULT_ACCOUNT_NAME", att.getType())) {
				attributesInformation.setOfiAccountName(att.getValue());
			}

			if (StringUtils.equals("DE.RESULT_ACCOUNT_NUMBER", att.getType())) {
				attributesInformation.setOfiAccountNumber(att.getValue());
			}

			if (StringUtils.equals("DE.RECEIVER_BRANCH", att.getType())) {
				attributesInformation.setOfiBranch(att.getValue());
			}

			if (StringUtils.equals("DE.RESULT_BSB", att.getType())) {
				attributesInformation.setOfiBsbNumber(att.getValue());
			}

			if (StringUtils.equals("DE.DATE_POSTED", att.getType())) {
				attributesInformation.setOfiDatePosted(att.getValue());
			}

			if (StringUtils.equals("DE.DATE_RESOLVED", att.getType())) {
				attributesInformation.setOfiDateResolved(att.getValue());
			}

			if (StringUtils.equals("DE.ADDITIONAL_INFO", att.getType())) {
				attributesInformation.setOfiAdditionalInfo(att.getValue());
			}

			if (StringUtils.equals("DE.DRAWING_AMOUNT", att.getType())) {
				attributesInformation.setOfiDrawingAmount(att.getValue());
			}

			if (StringUtils.equals("DE.RESULT_REMITTER_NAME", att.getType())) {
				attributesInformation.setOfiRemitterName(att.getValue());
			}

			if (StringUtils.equals("DE.RECALL_RESULT", att.getType())) {
				attributesInformation.setOfiRecallResult(att.getValue());
			}

			if (StringUtils.equals("DE.FUND_AVAILABILITY", att.getType())) {
				attributesInformation.setOfiFundAvailability(att.getValue());
			}

			if (StringUtils.equals("DE.CLAIM_RESULT", att.getType())) {
				attributesInformation.setOfiClaimResult(att.getValue());
			}

			if (StringUtils.equals("DE.MISTAKEN_RESULT", att.getType())) {
				mistakenResultList.add(att.getValue());
			}
		}

		attributesInformation.setOfiMistakenResults(
			mistakenResultList.toArray(new String[0]));

		for (int i = 1; i < (amountMap.size() + 1); ++i) {
			amountList.add(amountMap.get(String.valueOf(i)));
			dateList.add(dateMap.get(String.valueOf(i)));
			refList.add(refMap.get(String.valueOf(i)));
		}

		attributesInformation.setTransactionAmounts(
			amountList.toArray(new String[0]));
		attributesInformation.setProcessedDates(
			dateList.toArray(new String[0]));
		attributesInformation.setDestinationLodgementRefs(
			refList.toArray(new String[0]));

		return attributesInformation;
	}

	@Override
	public List<AttributesInformation> spxClaimTransactionSearch(
			final ServiceRequestForm form, final TktRequestHeader header,
			final PortletContext portletContext)
		throws Exception {

		CuscalTicketingServiceImpl.logger.debug(
			(Object)"spxClaimTransactionSearch - start");
		String message = null;
		List<AttributesInformation> trans = null;
		SpxClaimTransactionSearchResponse response = null;
		final SpxClaimTransactionSearchRequest request =
			new SpxClaimTransactionSearchRequest();
		XmlMessage xmlMessage = new XmlMessage();

		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);
		boolean isSuccess = Boolean.FALSE;

		if (null != header) {
			request.setHeader(header);

			if (null != form) {
				this.setupSpxClaimTransactionSearch(request, form, header);

				try {
					response = this.ticketingService.spxClaimTransactionSearch(
						request);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}

				if (null != response) {
					if (null != response.getHeader()) {
						final String statusCode = response.getHeader(
						).getStatusCode();

						if (StringUtils.equalsIgnoreCase(
								"SUCCESS", statusCode)) {

							message = statusCode;
							isSuccess = Boolean.TRUE;
							trans = this.mapSpxTransactionResponse(
								response.getTrans());
							CuscalTicketingServiceImpl.logger.debug(
								(Object)
									("spxClaimTransactionSearch - Successful spxClaimTransactionSearch. Status Code:" +
										statusCode));
						}
						else {
							message = statusCode;
							CuscalTicketingServiceImpl.logger.error(
								(Object)
									("spxClaimTransactionSearch - Response header is not successful. Status Code:" +
										statusCode));
						}
					}
					else {
						CuscalTicketingServiceImpl.logger.error(
							(Object)
								"spxClaimTransactionSearch - Response header is null");
					}
				}
				else {
					CuscalTicketingServiceImpl.logger.error(
						(Object)"spxClaimTransactionSearch - Response is null");
				}
			}
			else {
				CuscalTicketingServiceImpl.logger.error(
					(Object)
						"spxClaimTransactionSearch - ServiceRequestForm is null.");
			}
		}
		else {
			CuscalTicketingServiceImpl.logger.error(
				(Object)"spxClaimTransactionSearch - RequestHeader is null.");
		}

		CuscalTicketingServiceImpl.logger.debug(
			(Object)"spxClaimTransactionSearch - end");

		return trans;
	}

	@Override
	public List<AttributesInformation> spxPaymentTransactionSearch(
			final ServiceRequestForm form, final TktRequestHeader header,
			final PortletContext portletContext)
		throws Exception {

		CuscalTicketingServiceImpl.logger.debug(
			(Object)"spxPaymentTransactionSearch - start");
		String message = null;
		List<AttributesInformation> trans = null;
		SpxPaymentTransactionSearchResponse response = null;
		final SpxPaymentTransactionSearchRequest request =
			new SpxPaymentTransactionSearchRequest();
		XmlMessage xmlMessage = new XmlMessage();

		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);
		boolean isSuccess = Boolean.FALSE;

		if (null != header) {
			request.setHeader(header);

			if (null != form) {
				this.setupSpxPaymentTransactionSearch(request, form, header);

				try {
					response =
						this.ticketingService.spxPaymentTransactionSearch(
							request);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}

				if (null != response) {
					if (null != response.getHeader()) {
						final String statusCode = response.getHeader(
						).getStatusCode();

						if (StringUtils.equalsIgnoreCase(
								"SUCCESS", statusCode)) {

							message = statusCode;
							isSuccess = Boolean.TRUE;
							trans = this.mapSpxTransactionResponse(
								response.getTrans());
							CuscalTicketingServiceImpl.logger.debug(
								(Object)
									("spxPaymentTransactionSearch - Successful spxPaymentTransactionSearch. Status Code:" +
										statusCode));
						}
						else {
							message = statusCode;
							CuscalTicketingServiceImpl.logger.error(
								(Object)
									("spxPaymentTransactionSearch - Response header is not successful. Status Code:" +
										statusCode));
						}
					}
					else {
						CuscalTicketingServiceImpl.logger.error(
							(Object)
								"spxPaymentTransactionSearch - Response header is null");
					}
				}
				else {
					CuscalTicketingServiceImpl.logger.error(
						(Object)
							"spxPaymentTransactionSearch - Response is null");
				}
			}
			else {
				CuscalTicketingServiceImpl.logger.error(
					(Object)
						"spxPaymentTransactionSearch - ServiceRequestForm is null.");
			}
		}
		else {
			CuscalTicketingServiceImpl.logger.error(
				(Object)"spxPaymentTransactionSearch - RequestHeader is null.");
		}

		CuscalTicketingServiceImpl.logger.debug(
			(Object)"spxPaymentTransactionSearch - end");

		return trans;
	}

	@Override
	public List<AttributesInformation> spxTransactionSearch(
			final ServiceRequestForm form, final TktRequestHeader header,
			final PortletContext portletContext)
		throws Exception {

		CuscalTicketingServiceImpl.logger.debug(
			(Object)"spxTransactionSearch - start");
		String message = null;
		List<AttributesInformation> trans = null;
		SpxTransactionSearchResponse response = null;
		final SpxTransactionSearchRequest request =
			new SpxTransactionSearchRequest();
		XmlMessage xmlMessage = new XmlMessage();

		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);
		boolean isSuccess = Boolean.FALSE;

		if (null != header) {
			request.setHeader(header);

			if (null != form) {
				this.setupSpxTransactionSearch(request, form, header);

				try {
					response = this.ticketingService.spxTransactionSearch(
						request);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}

				if (null != response) {
					if (null != response.getHeader()) {
						final String statusCode = response.getHeader(
						).getStatusCode();

						if (StringUtils.equalsIgnoreCase(
								"SUCCESS", statusCode)) {

							message = statusCode;
							isSuccess = Boolean.TRUE;
							trans = this.mapSpxTransactionResponse(
								response.getTrans());
							CuscalTicketingServiceImpl.logger.debug(
								(Object)
									("spxTransactionSearch - Successful spxTransactionSearch. Status Code:" +
										statusCode));
						}
						else {
							message = statusCode;
							CuscalTicketingServiceImpl.logger.error(
								(Object)
									("spxTransactionSearch - Response header is not successful. Status Code:" +
										statusCode));
						}
					}
					else {
						CuscalTicketingServiceImpl.logger.error(
							(Object)
								"spxTransactionSearch - Response header is null");
					}
				}
				else {
					CuscalTicketingServiceImpl.logger.error(
						(Object)"spxTransactionSearch - Response is null");
				}
			}
			else {
				CuscalTicketingServiceImpl.logger.error(
					(Object)
						"spxTransactionSearch - ServiceRequestForm is null");
			}
		}
		else {
			CuscalTicketingServiceImpl.logger.error(
				(Object)"spxTransactionSearch - RequestHeader is null.");
		}

		CuscalTicketingServiceImpl.logger.debug(
			(Object)"spxTransactionSearch - end");

		return trans;
	}

	@Override
	public String updateServiceRequest(
			final ServiceRequestForm form, final TktRequestHeader header,
			final PortletContext portletContext, final User user)
		throws Exception {

		CuscalTicketingServiceImpl.logger.debug(
			(Object)"updateServiceRequest - start");
		String message = null;
		UpdateSimpleServiceRequestResponse response = null;
		final UpdateSimpleServiceRequestRequest request =
			new UpdateSimpleServiceRequestRequest();
		XmlMessage xmlMessage = new XmlMessage();

		xmlMessage = CommonUtil.addHeaderInformationToXmlMessage(
			header, xmlMessage);
		boolean isSuccess = Boolean.FALSE;

		if (null != header) {
			request.setHeader(header);

			if (null != form) {
				if (form.isOfi()) {
					request.setAppendMode(true);
				}

				this.setupUpdateServiceRequest(request, form, header, user);

				try {
					response = this.ticketingService.updateSimpleServiceRequest(
						request);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}

				if (null != response) {
					if (null != response.getHeader()) {
						final String statusCode = response.getHeader(
						).getStatusCode();
						final String ticketId = String.valueOf(
							request.getTicket(
							).getId());

						if (StringUtils.equalsIgnoreCase(
								"SUCCESS", statusCode)) {

							message = statusCode;
							isSuccess = Boolean.TRUE;
							this.createXmlMessageForServiceRequest(
								xmlMessage, form, statusCode, ticketId);
							CuscalTicketingServiceImpl.logger.debug(
								(Object)
									("addServiceRequest - Successfully added ticket. Status Code:" +
										statusCode));
						}
						else {
							message = statusCode;
							this.createXmlMessageForServiceRequest(
								xmlMessage, form, statusCode, ticketId);
							CuscalTicketingServiceImpl.logger.error(
								(Object)
									("addServiceRequest - Response header is not successful. Status Code:" +
										statusCode));
						}
					}
					else {
						CuscalTicketingServiceImpl.logger.error(
							(Object)
								"addServiceRequest - Response header is null");
					}
				}
				else {
					CuscalTicketingServiceImpl.logger.error(
						(Object)"addServiceRequest - Response is null");
				}
			}
			else {
				CuscalTicketingServiceImpl.logger.error(
					(Object)
						"addServiceRequest - ServiceRequestForm is null. Cannot add ticket");
			}
		}
		else {
			CuscalTicketingServiceImpl.logger.error(
				(Object)
					"addServiceRequest - RequestHeader is null. Cannot add ticket");
		}

		if (isSuccess) {
			CommonUtil.auditSuccessXmlMessage(
				xmlMessage, "CSS/TICKET/ADD", portletContext.getRequest(),
				portletContext.getResponse());
		}
		else {
			CommonUtil.auditFailXmlMessage(
				xmlMessage, "CSS/TICKET/ADD", portletContext.getRequest(),
				portletContext.getResponse());
		}

		CuscalTicketingServiceImpl.logger.debug(
			(Object)"addServiceRequest - end");
		System.out.println("-----addServiceRequest - end");

		return message;
	}

	private String formatAmount(String amountStr) {
		try {
			final BigDecimal amount = new BigDecimal(
				StringUtils.trimToEmpty(amountStr));
			final DecimalFormat decimalFormat = new DecimalFormat("0.00");
			amountStr = decimalFormat.format(amount);
		}
		catch (Exception ex) {
		}

		return amountStr;
	}

	private String formatDate(String dateStr) {
		try {
			final SimpleDateFormat dt = new SimpleDateFormat(
				"dd/MM/yyyy hh:mm:ss");
			final Date date = dt.parse(StringUtils.trimToEmpty(dateStr));
			final SimpleDateFormat dt2 = new SimpleDateFormat("dd/MM/yy");
			dateStr = dt2.format(date);
		}
		catch (Exception e) {
			try {
				final SimpleDateFormat dt3 = new SimpleDateFormat(
					"dd-MMM-yyyy hh:mm:ss");
				final Date date2 = dt3.parse(StringUtils.trimToEmpty(dateStr));
				final SimpleDateFormat dt4 = new SimpleDateFormat("dd/MM/yy");
				dateStr = dt4.format(date2);
			}
			catch (Exception ex) {
			}
		}

		return dateStr;
	}

	private List<AttributesInformation> mapSpxTransactionResponse(
		final List<SpxTransactionType> spxTrans) {

		final List<AttributesInformation> trans = new ArrayList<>();
		int i = 0;

		for (final SpxTransactionType spxTran : spxTrans) {
			final AttributesInformation tran = new AttributesInformation();
			tran.setFinInstName(spxTran.getFiName());
			tran.setFinInstUserId(spxTran.getUserID());
			tran.setProcessedDates(new String[] {spxTran.getDate()});
			tran.setDestinationBSB(
				CommonUtil.removeDotFromBSB(spxTran.getDestinationBsb()));
			tran.setDestinationAccountNumber(
				spxTran.getDestinationAccountNumber());
			tran.setTransactionAmounts(new String[] {spxTran.getAmount()});
			tran.setAccountTitle(spxTran.getAccountName());
			tran.setDestinationLodgementRefs(
				new String[] {spxTran.getLodgementReference()});
			tran.setTransactionCode(spxTran.getTransactionType());
			tran.setFinInstTraceBSBNumber(
				CommonUtil.removeDotFromBSB(spxTran.getTraceBsb()));
			tran.setAccountNumberRemitter(spxTran.getTraceAccountNumber());
			tran.setRemitterName(spxTran.getRemitterName());
			tran.setBatchNumber(spxTran.getBatchNumber());
			tran.setId(i++);
			trans.add(tran);
		}

		return trans;
	}

	private List<AttachmentDataType> setupAttachmentForServiceRequest(
		final ServiceRequestForm form) {

		final List<MultipartFile> attachments = form.getRequestAttachments();
		final List<AttachmentDataType> attachmentTypes = new ArrayList<>();

		if ((null != attachments) && (attachments.size() > 0)) {
			for (final MultipartFile multipartFile : attachments) {
				if (multipartFile.getSize() > 0L) {
					final AttachmentDataType attachmentType =
						new AttachmentDataType();
					attachmentType.setFilename(
						multipartFile.getOriginalFilename());
					attachmentType.setMimeType(multipartFile.getContentType());

					try {
						final DataHandler handlerFile = new DataHandler(
							(DataSource)new ByteArrayDataSource(
								multipartFile.getInputStream(),
								"application/octet-stream"));
						attachmentType.setBlob(handlerFile);
					}
					catch (IOException exception) {
						CuscalTicketingServiceImpl.logger.error(
							(Object)
								("Error occurred setting attachment:" +
									exception.getMessage()));
					}

					attachmentTypes.add(attachmentType);
				}
			}
		}

		return attachmentTypes;
	}

	private List<SimpleAttributesType>
		setupAttributesInformationForOfiServiceRequest(
			final ServiceRequestForm form) {

		final AttributesInformation attributesInformation =
			form.getAttributesInformation();
		final Properties prop = this.deProperties.getDeProperty();
		final DEListBoxData deListBoxData = CommonUtil.populateDEListBoxData(
			this.deProperties);
		final List<SimpleAttributesType> attributes = new ArrayList<>();
		SimpleAttributesType simpleAttributesType = null;

		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.datePosted.id")));

		if (StringUtils.isNotBlank(attributesInformation.getOfiDatePosted())) {
			simpleAttributesType.setValue(
				attributesInformation.getOfiDatePosted(
				).trim());
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.resultBSB.id")));

		if (StringUtils.isNotBlank(attributesInformation.getOfiBsbNumber())) {
			simpleAttributesType.setValue(
				CommonUtil.removeDotFromBSB(
					attributesInformation.getOfiBsbNumber(
					).trim()));
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.receiverBranch.id")));

		if (StringUtils.isNotBlank(attributesInformation.getOfiBranch())) {
			simpleAttributesType.setValue(
				attributesInformation.getOfiBranch(
				).trim());
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.resultAccountNumber.id")));

		if (StringUtils.isNotBlank(
				attributesInformation.getOfiAccountNumber())) {

			simpleAttributesType.setValue(
				attributesInformation.getOfiAccountNumber(
				).trim());
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.resultAccountName.id")));

		if (StringUtils.isNotBlank(attributesInformation.getOfiAccountName())) {
			simpleAttributesType.setValue(
				attributesInformation.getOfiAccountName(
				).trim());
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.recallResult.id")));

		if (StringUtils.isNotBlank(
				attributesInformation.getOfiRecallResult())) {

			final String result = deListBoxData.getOfiRecallResultMap(
			).get(
				Long.valueOf(
					attributesInformation.getOfiRecallResult(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getOfiRecallResult(
				).trim());
			simpleAttributesType.setDescription(result);
		}

		attributes.add(simpleAttributesType);

		if (attributesInformation.getOfiMistakenResults() != null) {
			for (final String result2 :
					attributesInformation.getOfiMistakenResults()) {

				final String ofiMistakenResult =
					deListBoxData.getOfiMistakenCheckboxResultMap(
					).get(
						Long.valueOf(result2)
					);
				simpleAttributesType = new SimpleAttributesType();
				simpleAttributesType.setId(
					(long)Long.valueOf(
						prop.getProperty("de.mistakenResult.id")));
				simpleAttributesType.setValue(result2);
				simpleAttributesType.setDescription(ofiMistakenResult);
				attributes.add(simpleAttributesType);
			}
		}
		else {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.mistakenResult.id")));
			attributes.add(simpleAttributesType);
		}

		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.claimResult.id")));

		if (StringUtils.isNotBlank(attributesInformation.getOfiClaimResult())) {
			final String result = deListBoxData.getOfiClaimResultMap(
			).get(
				Long.valueOf(
					attributesInformation.getOfiClaimResult(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getOfiClaimResult(
				).trim());
			simpleAttributesType.setDescription(result);
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.dateResolved.id")));

		if (StringUtils.isNotBlank(
				attributesInformation.getOfiDateResolved())) {

			simpleAttributesType.setValue(
				attributesInformation.getOfiDateResolved(
				).trim());
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.drawingAmount.id")));

		if (StringUtils.isNotBlank(
				attributesInformation.getOfiDrawingAmount())) {

			simpleAttributesType.setValue(
				attributesInformation.getOfiDrawingAmount(
				).trim());
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.additionalInfo.id")));

		if (StringUtils.isNotBlank(
				attributesInformation.getOfiAdditionalInfo())) {

			simpleAttributesType.setValue(
				attributesInformation.getOfiAdditionalInfo(
				).trim());
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.resultRemitterName.id")));

		if (StringUtils.isNotBlank(
				attributesInformation.getOfiRemitterName())) {

			simpleAttributesType.setValue(
				attributesInformation.getOfiRemitterName(
				).trim());
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.fundAvailability.id")));

		if (StringUtils.isNotBlank(
				attributesInformation.getOfiFundAvailability())) {

			final String fundsAvailability =
				deListBoxData.getOfiMistakenResultMap(
				).get(
					Long.valueOf(
						attributesInformation.getOfiFundAvailability(
						).trim())
				);
			simpleAttributesType.setValue(
				attributesInformation.getOfiFundAvailability(
				).trim());
			simpleAttributesType.setDescription(fundsAvailability);
		}

		attributes.add(simpleAttributesType);
		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			(long)Long.valueOf(prop.getProperty("de.bsb.id")));

		if (StringUtils.isNotBlank(
				form.getContactInformation(
				).getBsb())) {

			simpleAttributesType.setValue(
				form.getContactInformation(
				).getBsb(
				).trim());
		}

		attributes.add(simpleAttributesType);

		return attributes;
	}

	private List<SimpleAttributesType>
		setupAttributesInformationForServiceRequest(
			final ServiceRequestForm form) {

		final AttributesInformation attributesInformation =
			form.getAttributesInformation();
		final RequestTypeInformation requestTypeInformation =
			form.getRequestTypeInformation();
		final Properties prop = this.deProperties.getDeProperty();
		final List<SimpleAttributesType> attributes = new ArrayList<>();
		SimpleAttributesType simpleAttributesType = null;
		final DEListBoxData deListBoxData = CommonUtil.populateDEListBoxData(
			this.deProperties);

		if (StringUtils.isNotBlank(
				attributesInformation.getClientRefNumber())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.clientRefNumber.id")));
			simpleAttributesType.setValue(
				attributesInformation.getClientRefNumber(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getBsb())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.bsb.id")));
			simpleAttributesType.setValue(
				CommonUtil.removeDotFromBSB(
					attributesInformation.getBsb(
					).trim()));
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getRequestType())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.request.type.id")));
			simpleAttributesType.setValue(
				attributesInformation.getRequestType(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getRequestFor())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.requestFor.id")));
			final String requestFor = deListBoxData.getRequestForMap(
			).get(
				Long.valueOf(
					attributesInformation.getRequestFor(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getRequestFor(
				).trim());
			simpleAttributesType.setDescription(requestFor);
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getFinInstName())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.finInstName.id")));
			simpleAttributesType.setValue(
				attributesInformation.getFinInstName(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getFinInstUserId())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.finInstUserId.id")));
			simpleAttributesType.setValue(
				attributesInformation.getFinInstUserId(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (null != attributesInformation.getProcessedDates()) {
			final long id1 = Long.valueOf(
				prop.getProperty("de.processedDate1.id"));

			for (int i = 0;
				 i < attributesInformation.getProcessedDates().length; ++i) {

				simpleAttributesType = new SimpleAttributesType();
				simpleAttributesType.setId(id1 + i);
				simpleAttributesType.setValue(
					StringUtils.trimToNull(
						attributesInformation.getProcessedDates()[i]));
				attributes.add(simpleAttributesType);
			}
		}

		if (StringUtils.isNotBlank(attributesInformation.getDestinationBSB())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.destinationBSB.id")));
			simpleAttributesType.setValue(
				CommonUtil.removeDotFromBSB(
					attributesInformation.getDestinationBSB(
					).trim()));
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getDestinationAccountNumber())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(
					prop.getProperty("de.destinationAccountNumber.id")));
			simpleAttributesType.setValue(
				attributesInformation.getDestinationAccountNumber(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getTransactionCode())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.transactionCode.id")));
			final String transactionCode = deListBoxData.getTransactionCodeMap(
			).get(
				Long.valueOf(
					attributesInformation.getTransactionCode(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getTransactionCode(
				).trim());
			simpleAttributesType.setDescription(transactionCode);
			attributes.add(simpleAttributesType);
		}

		if (null != attributesInformation.getTransactionAmounts()) {
			final long id1 = Long.valueOf(
				prop.getProperty("de.transactionAmount1.id"));

			for (int i = 0;
				 i < attributesInformation.getTransactionAmounts().length;
				 ++i) {

				simpleAttributesType = new SimpleAttributesType();
				simpleAttributesType.setId(id1 + i);
				simpleAttributesType.setValue(
					StringUtils.trimToNull(
						attributesInformation.getTransactionAmounts()[i]));
				attributes.add(simpleAttributesType);
			}
		}

		if (StringUtils.isNotBlank(attributesInformation.getAccountTitle())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.accountTitle.id")));
			simpleAttributesType.setValue(
				attributesInformation.getAccountTitle(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getFinInstTraceBSBNumber())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(
					prop.getProperty("de.finInstTraceBSBNumber.id")));
			simpleAttributesType.setValue(
				CommonUtil.removeDotFromBSB(
					attributesInformation.getFinInstTraceBSBNumber(
					).trim()));
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getAccountNumberRemitter())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(
					prop.getProperty("de.accountNumberRemitter.id")));
			simpleAttributesType.setValue(
				attributesInformation.getAccountNumberRemitter(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getRemitterName()) &&
			!Constants.DE_REMITTER_DETAILS.equals(
				attributesInformation.getRequestFor())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.remitterName.id")));
			simpleAttributesType.setValue(
				attributesInformation.getRemitterName(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (null != attributesInformation.getDestinationLodgementRefs()) {
			final long id1 = Long.valueOf(
				prop.getProperty("de.destinationLodgementRef1.id"));

			for (int i = 0;
				 i < attributesInformation.getDestinationLodgementRefs().length;
				 ++i) {

				simpleAttributesType = new SimpleAttributesType();
				simpleAttributesType.setId(id1 + i);
				simpleAttributesType.setValue(
					StringUtils.trimToNull(
						attributesInformation.getDestinationLodgementRefs()
							[i]));
				attributes.add(simpleAttributesType);
			}
		}

		if (StringUtils.isNotBlank(attributesInformation.getDatePosted())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.datePosted.id")));
			simpleAttributesType.setValue(
				attributesInformation.getDatePosted(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getReceiverBSBNumber())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(
					prop.getProperty("de.receiverBSBNumber.id")));
			simpleAttributesType.setValue(
				CommonUtil.removeDotFromBSB(
					attributesInformation.getReceiverBSBNumber(
					).trim()));
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getReceiverBranch())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.receiverBranch.id")));
			simpleAttributesType.setValue(
				attributesInformation.getReceiverBranch(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getReceiverAccountNumber())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(
					prop.getProperty("de.receiverAccountNumber.id")));
			simpleAttributesType.setValue(
				attributesInformation.getReceiverAccountNumber(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getReceiverAccountName())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(
					prop.getProperty("de.receiverAccountName.id")));
			simpleAttributesType.setValue(
				attributesInformation.getReceiverAccountName(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getReceiverLodgementRef())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(
					prop.getProperty("de.receiverLodgementRef.id")));
			simpleAttributesType.setValue(
				attributesInformation.getReceiverLodgementRef(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (Constants.DE_INTENDED_ACCOUNT_DETAILS.equals(
				StringUtils.trimToEmpty(
					attributesInformation.getIntendedAccountOrWrongPayee()))) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.reasonForDispute.id")));
			final String reasonForDispute =
				deListBoxData.getIntendedAccountDetailsMap(
				).get(
					Long.valueOf(
						attributesInformation.getIntendedAccountOrWrongPayee(
						).trim())
				);
			simpleAttributesType.setValue(
				attributesInformation.getIntendedAccountOrWrongPayee(
				).trim());
			simpleAttributesType.setDescription(reasonForDispute);
			attributes.add(simpleAttributesType);

			if (StringUtils.isNotBlank(
					attributesInformation.getIntendedAccountBSBNumber())) {

				simpleAttributesType = new SimpleAttributesType();
				simpleAttributesType.setId(
					(long)Long.valueOf(
						prop.getProperty("de.intendedAccountBSBNumber.id")));
				simpleAttributesType.setValue(
					CommonUtil.removeDotFromBSB(
						attributesInformation.getIntendedAccountBSBNumber(
						).trim()));
				attributes.add(simpleAttributesType);
			}

			if (StringUtils.isNotBlank(
					attributesInformation.getIntendedAccountNumber())) {

				simpleAttributesType = new SimpleAttributesType();
				simpleAttributesType.setId(
					(long)Long.valueOf(
						prop.getProperty("de.intendedAccountNumber.id")));
				simpleAttributesType.setValue(
					attributesInformation.getIntendedAccountNumber(
					).trim());
				attributes.add(simpleAttributesType);
			}

			if (StringUtils.isNotBlank(
					attributesInformation.getIntendedAccountTitle())) {

				simpleAttributesType = new SimpleAttributesType();
				simpleAttributesType.setId(
					(long)Long.valueOf(
						prop.getProperty("de.intendedAccountTitle.id")));
				simpleAttributesType.setValue(
					attributesInformation.getIntendedAccountTitle(
					).trim());
				attributes.add(simpleAttributesType);
			}
		}
		else if (Constants.DE_CUSTOMER_SELECTED_WRONG_PAYEE.equals(
					StringUtils.trimToEmpty(
						attributesInformation.
							getIntendedAccountOrWrongPayee()))) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.reasonForDispute.id")));
			final String reasonForDispute =
				deListBoxData.getIntendedAccountDetailsMap(
				).get(
					Long.valueOf(
						attributesInformation.getIntendedAccountOrWrongPayee(
						).trim())
				);
			simpleAttributesType.setValue(
				attributesInformation.getIntendedAccountOrWrongPayee(
				).trim());
			simpleAttributesType.setDescription(reasonForDispute);
			attributes.add(simpleAttributesType);

			if (StringUtils.isNotBlank(
					attributesInformation.getCustomerDeclarationAttached())) {

				simpleAttributesType = new SimpleAttributesType();
				simpleAttributesType.setId(
					(long)Long.valueOf(
						prop.getProperty("de.customerDeclarationAttached.id")));
				final String customerDeclarationAttached =
					deListBoxData.getGeneralYesNoMap(
					).get(
						Long.valueOf(
							attributesInformation.
								getCustomerDeclarationAttached(
								).trim())
					);
				simpleAttributesType.setValue(
					attributesInformation.getCustomerDeclarationAttached(
					).trim());
				simpleAttributesType.setDescription(
					customerDeclarationAttached);
				attributes.add(simpleAttributesType);
			}
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getRequestReceived())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.requestReceived.id")));
			final String requestReceived = deListBoxData.getGeneralYesNoMap(
			).get(
				Long.valueOf(
					attributesInformation.getRequestReceived(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getRequestReceived(
				).trim());
			simpleAttributesType.setDescription(requestReceived);
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getFundsAvailable())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.fundsAvailable.id")));
			final String fundsAvailable = deListBoxData.getGeneralYesNoMap(
			).get(
				Long.valueOf(
					attributesInformation.getFundsAvailable(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getFundsAvailable(
				).trim());
			simpleAttributesType.setDescription(fundsAvailable);
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getTimeframe())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.timeframe.id")));
			final String timeframe = deListBoxData.getTimeframeMap(
			).get(
				Long.valueOf(
					attributesInformation.getTimeframe(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getTimeframe(
				).trim());
			simpleAttributesType.setDescription(timeframe);
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getClaimReason())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.claimReason.id")));
			final String claimReason = deListBoxData.getClaimReasonMap(
			).get(
				Long.valueOf(
					attributesInformation.getClaimReason(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getClaimReason(
				).trim());
			simpleAttributesType.setDescription(claimReason);
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getBatchNumber())) {
			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.batchNumber.id")));
			simpleAttributesType.setValue(
				attributesInformation.getBatchNumber(
				).trim());
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getCurrentDDRStatus())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(prop.getProperty("de.currentDDRStatus.id")));
			final String currentDDRStatus = deListBoxData.getGeneralYesNoMap(
			).get(
				Long.valueOf(
					attributesInformation.getCurrentDDRStatus(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getCurrentDDRStatus(
				).trim());
			simpleAttributesType.setDescription(currentDDRStatus);
			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(
				attributesInformation.getDrawingInTheNameOf())) {

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				(long)Long.valueOf(
					prop.getProperty("de.drawingInTheNameOf.id")));
			simpleAttributesType.setValue(
				attributesInformation.getDrawingInTheNameOf(
				).trim());
			attributes.add(simpleAttributesType);
		}

		return attributes;
	}

	private String setupNoteForServiceRequest(final ServiceRequestForm form) {
		return StringUtils.trimToNull(
			form.getAttributesInformation(
			).getDestinationAdditionalInfo());
	}

	private RequestTypeType setupRequestTypeInformationForServiceRequest(
		final RequestTypeInformation requestTypeInformation) {

		final RequestTypeType requestType = new RequestTypeType();

		if (StringUtils.isNotBlank(requestTypeInformation.getRequestTypeId())) {
			requestType.setProductId(
				(long)Long.valueOf(requestTypeInformation.getProductCode()));
			requestType.setTypeId(
				(long)Long.valueOf(requestTypeInformation.getRequestTypeId()));
		}

		return requestType;
	}

	private SimpleServiceRequestRequestType setupServiceRequest(
			final ServiceRequestForm form)
		throws Exception {

		SimpleServiceRequestRequestType request = null;

		if (null != form) {
			request = new SimpleServiceRequestRequestType();

			if (null != form.getContactInformation()) {
				final ContactType contact =
					this.setupServiceRequestForContactInfo(
						form.getContactInformation());
				request.setContact(contact);
			}

			if (null != form.getRequestTypeInformation()) {
				final RequestTypeType requestType =
					this.setupRequestTypeInformationForServiceRequest(
						form.getRequestTypeInformation());
				request.setRequestType(requestType);
			}

			if (null != form.getAttributesInformation()) {
				final List<SimpleAttributesType> attributes =
					this.setupAttributesInformationForServiceRequest(form);
				request.getAttributes(
				).addAll(
					attributes
				);
				request.setNote(this.setupNoteForServiceRequest(form));
			}

			if (null != form.getRequestAttachments()) {
				final List<AttachmentDataType> attachmentTypes =
					this.setupAttachmentForServiceRequest(form);
				request.getAttachments(
				).addAll(
					attachmentTypes
				);
			}
		}

		return request;
	}

	private ContactType setupServiceRequestForContactInfo(
			final RequestContactInformation form)
		throws Exception {

		final ContactType contact = new ContactType();
		contact.setFirstName(form.getContactOfficerFirstName());
		contact.setSurname(form.getContactOfficerLastName());
		contact.setEmail(form.getEmailFrom());
		contact.setPhoneNo(form.getPhoneNumberFrom());
		contact.setOrganisation(form.getReqInstFullNamefrom());

		return contact;
	}

	private void setupSpxClaimTransactionSearch(
			final SpxClaimTransactionSearchRequest request,
			final ServiceRequestForm form, final TktRequestHeader header)
		throws Exception {

		final SpxTransactionType tran = new SpxTransactionType();
		tran.setDestinationBsb(
			CommonUtil.formatBsb(
				StringUtils.trimToNull(
					form.getAttributesInformation(
					).getDestinationBSB())));
		tran.setDestinationAccountNumber(
			StringUtils.trimToNull(
				form.getAttributesInformation(
				).getDestinationAccountNumber()));
		tran.setLodgementReference(
			StringUtils.trimToNull(
				form.getAttributesInformation(
				).getDestinationLodgementRefs()[0]));
		tran.setAmount(
			StringUtils.trimToNull(
				form.getAttributesInformation(
				).getTransactionAmounts()[0]));
		tran.setBatchNumber(
			StringUtils.trimToNull(
				form.getAttributesInformation(
				).getBatchNumber()));
		tran.setBsb(
			CommonUtil.formatBsb(
				StringUtils.trimToNull(
					form.getAttributesInformation(
					).getBsb())));
		final Date date = Utility.createXmlGregorianCalendarFromString(
			form.getAttributesInformation(
			).getProcessedDates()[0],
			Constants.DATE_FORMAT, Boolean.FALSE
		).toGregorianCalendar(
		).getTime();
		final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		tran.setDate(dateFormat.format(date));
		request.setTran(tran);
	}

	private void setupSpxPaymentTransactionSearch(
			final SpxPaymentTransactionSearchRequest request,
			final ServiceRequestForm form, final TktRequestHeader header)
		throws Exception {

		final SpxTransactionType tran = this.setupTransactionSearch(
			form, header);
		request.setTran(tran);
	}

	private void setupSpxTransactionSearch(
			final SpxTransactionSearchRequest request,
			final ServiceRequestForm form, final TktRequestHeader header)
		throws Exception {

		final SpxTransactionType tran = this.setupTransactionSearch(
			form, header);
		request.setTran(tran);
	}

	private SpxTransactionType setupTransactionSearch(
		final ServiceRequestForm form, final TktRequestHeader header) {

		final SpxTransactionType tran = new SpxTransactionType();
		tran.setDestinationBsb(
			CommonUtil.formatBsb(
				StringUtils.trimToNull(
					form.getAttributesInformation(
					).getDestinationBSB())));
		tran.setAmount(
			StringUtils.trimToNull(
				form.getAttributesInformation(
				).getTransactionAmounts()[0]));
		final Date date = Utility.createXmlGregorianCalendarFromString(
			form.getAttributesInformation(
			).getProcessedDates()[0],
			Constants.DATE_FORMAT, Boolean.FALSE
		).toGregorianCalendar(
		).getTime();
		final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		tran.setDate(dateFormat.format(date));
		tran.setUserID(
			StringUtils.trimToNull(
				form.getAttributesInformation(
				).getFinInstUserId()));
		tran.setLodgementReference(
			StringUtils.trimToNull(
				form.getAttributesInformation(
				).getDestinationLodgementRefs()[0]));
		tran.setDestinationAccountNumber(
			StringUtils.trimToNull(
				form.getAttributesInformation(
				).getDestinationAccountNumber()));
		tran.setBatchNumber(
			StringUtils.trimToNull(
				form.getAttributesInformation(
				).getBatchNumber()));
		tran.setBsb(
			CommonUtil.formatBsb(
				StringUtils.trimToNull(
					form.getAttributesInformation(
					).getBsb())));

		return tran;
	}

	private void setupUpdateServiceRequest(
			final UpdateSimpleServiceRequestRequest request,
			final ServiceRequestForm form, final TktRequestHeader header,
			final User user)
		throws Exception {

		final TicketType ticket = new TicketType();
		ticket.setId((long)Long.valueOf(form.getTicketId()));
		ticket.setOrgShortName(header.getUserOrgId());
		request.setTicket(ticket);

		if (null != form.getAttributesInformation()) {
			List<SimpleAttributesType> ofiAttributes = null;

			if (form.isOfi()) {
				ofiAttributes =
					this.setupAttributesInformationForOfiServiceRequest(form);
				request.getAttributes(
				).addAll(
					ofiAttributes
				);
				final String ofiUpdateNote = MessageFormat.format(
					Constants.OFI_UPDATE_NOTE, user.getFullName());
				form.getAttributesInformation(
				).setDestinationAdditionalInfo(
					ofiUpdateNote
				);
			}
			else {
				final List<SimpleAttributesType> attributes =
					this.setupAttributesInformationForServiceRequest(form);
				request.getAttributes(
				).addAll(
					attributes
				);
			}

			request.setNote(this.setupNoteForServiceRequest(form));
		}

		if (null != form.getRequestAttachments()) {
			final List<AttachmentDataType> attachmentTypes =
				this.setupAttachmentForServiceRequest(form);
			request.getAttachments(
			).addAll(
				attachmentTypes
			);
		}
	}

	private static Logger logger;

	static {
		CuscalTicketingServiceImpl.logger = Logger.getLogger(
			CuscalTicketingServiceImpl.class);
	}

	@Autowired
	@Qualifier("deProperties")
	private DEProperties deProperties;

	private final CustomerSelfServiceService ticketingService = CuscalServiceLocator.getService(
		CustomerSelfServiceService.class.getName());
	private final EntityService entityService = CuscalServiceLocator.getService(
		EntityService.class.getName());

}