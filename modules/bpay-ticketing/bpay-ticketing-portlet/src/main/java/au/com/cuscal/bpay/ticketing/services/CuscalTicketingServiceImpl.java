package au.com.cuscal.bpay.ticketing.services;

import au.com.cuscal.bpay.ticketing.common.BPayProperties;
import au.com.cuscal.bpay.ticketing.common.CommonUtil;
import au.com.cuscal.bpay.ticketing.common.Constants;
import au.com.cuscal.bpay.ticketing.domain.BPayListBoxData;
import au.com.cuscal.bpay.ticketing.forms.AttributesInformation;
import au.com.cuscal.bpay.ticketing.forms.RequestContactInformation;
import au.com.cuscal.bpay.ticketing.forms.RequestTypeInformation;
import au.com.cuscal.bpay.ticketing.forms.ServiceRequestForm;
import au.com.cuscal.common.framework.AuditWebServicesUtil;
import au.com.cuscal.framework.audit.category.AuditTicketing;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.audit.xml.XmlMessage;
import au.com.cuscal.framework.webservices.client.CustomerSelfServiceService;
import au.com.cuscal.framework.webservices.client.EntityService;
import au.com.cuscal.framework.webservices.liferay.dxp.LiferayClientUtil;
import au.com.cuscal.framework.webservices.selfservice.*;

import com.liferay.portal.kernel.model.User;

import java.io.IOException;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;

import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implements local CuscalTicketingServive  interface.
 *
 *
 */
@Service(Constants.CUSCAL_TICKETING_SERVICE)
public class CuscalTicketingServiceImpl implements CuscalTicketingService {

	/**
	 * Add Service Request for transaction.
	 *
	 * @param 	form 		<code>ServiceRequestForm</code>
	 * @param 	header		<code>TktRequestHeader</code>
	 * @return 	String
	 * @throws Exception
	 */
	public String addServiceRequest(
			ServiceRequestForm form, TktRequestHeader header,
			PortletContext portletContext)
		throws Exception {

		logger.debug("addServiceRequest - start");

		String message = null;
		AddSimpleServiceRequestResponse response = null;
		AddSimpleServiceRequestRequest request =
			new AddSimpleServiceRequestRequest();

		XmlMessage xmlMessage = new XmlMessage();

		xmlMessage =
			AuditWebServicesUtil.addTktRequestHeaderElementsToXmlMessage(
				header, xmlMessage);
		boolean isSuccess = Boolean.FALSE;

		if (null != header) {
			request.setHeader(header);

			if (null != form) {
				SimpleServiceRequestRequestType requestType =
					setupServiceRequest(form);//handshake between form data vs domain.
				request.setSimpleServiceRequestRequest(requestType);

				try {
					response = ticketingService.addSimpleServiceRequest(
						request);//actual framework web service request integration.
				}
				catch (Exception ex) {
					logger.error("addServiceRequest - There are errors");
				}

				if (null != response) {
					if (null != response.getHeader()) {
						String statusCode = response.getHeader(
						).getStatusCode();

						if (StringUtils.equalsIgnoreCase(
								"SUCCESS", statusCode)) {

							message = statusCode;
							isSuccess = Boolean.TRUE;

							createXmlMessageForServiceRequest(
								xmlMessage, form, statusCode,
								String.valueOf(
									response.getServiceRequestResponse(
									).getTicket(
									).getId()));

							logger.debug(
								"addServiceRequest - Successfully added ticket. Status Code:" +
									statusCode);
						}
						else {
							message = statusCode;

							createXmlMessageForServiceRequest(
								xmlMessage, form, statusCode, null);

							logger.error(
								"addServiceRequest - Response header is not successful. Status Code:" +
									statusCode);
						}
					}
					else {
						logger.error(
							"addServiceRequest - Response header is null");
					}
				}
				else {
					logger.error("addServiceRequest - Response is null");
				}
			}
			else {
				logger.error(
					"addServiceRequest - ServiceRequestForm is null. Cannot add ticket");
			}
		}
		else {
			logger.error(
				"addServiceRequest - RequestHeader is null. Cannot add ticket");
		}

		/*audit information.*/
		if (isSuccess) {
			CommonUtil.auditSuccessXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_ADD,
				portletContext.getRequest(), portletContext.getResponse());
		}
		else {
			CommonUtil.auditFailXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_ADD,
				portletContext.getRequest(), portletContext.getResponse());
		}

		logger.debug("addServiceRequest - end");

		return message;
	}

	public void createXmlMessageForServiceRequest(
		XmlMessage xmlMessage, ServiceRequestForm serviceRequestForm,
		String statusCode, String ticketId) {

		if (null != serviceRequestForm) {
			if (null != serviceRequestForm.getRequestTypeInformation()) {
				if (StringUtils.isNotBlank(
						serviceRequestForm.getRequestTypeInformation(
						).getRequestTypeId())) {

					xmlMessage.addParameter(
						"requestTypeId",
						serviceRequestForm.getRequestTypeInformation(
						).getRequestTypeId());
				}

				if (null != serviceRequestForm.getAttributesInformation()) {
					if (StringUtils.isNotBlank(
							serviceRequestForm.getAttributesInformation(
							).getTransactionRefNumber())) {

						xmlMessage.addParameter(
							"transactionRefNumber",
							serviceRequestForm.getAttributesInformation(
							).getTransactionRefNumber());
					}
				}
			}
		}

		if (ticketId != null) {
			xmlMessage.addResult("ticketId", ticketId);
		}

		xmlMessage.addResult("statusCode", statusCode);
	}

	public EntityService getEntityService() {
		return entityService;
	}

	/**
	 *
	 */
	@Override
	public String getOrgShortName(Long liferayOrgId) {
		logger.debug("getOrgShortName - start");
		String orgShortName = null;

		try {
			orgShortName = LiferayClientUtil.getOrgShortName(
				entityService, liferayOrgId);
		}
		catch (Exception e) {
			logger.error(
				"getOrgShortName - Could not get the OrgShortName from LiferayClientUtil. " +
					e.getMessage(),
				e);
		}

		logger.debug("getOrgShortName - end [" + orgShortName + "]");

		return orgShortName;
	}

	public CustomerSelfServiceService getTicketingService() {
		return ticketingService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public void setTicketingService(
		CustomerSelfServiceService ticketingService) {

		this.ticketingService = ticketingService;
	}

	public AttributesInformation setupAttributesInformation(TicketType ticket) {
		BPayListBoxData bPayListBoxData = CommonUtil.populateBPayListBoxData(
			bpayProperties);

		Properties prop = bpayProperties.getBpayProperty();

		boolean isOfi = false;

		if (String.valueOf(
				ticket.getType(
				).getTypeId()
			).equalsIgnoreCase(
				prop.getProperty(Constants.REQUEST_TYPE_TYPE_ID_OFI)
			)) {

			isOfi = true;
		}

		AttributesInformation attributesInformation =
			new AttributesInformation();

		List<AttributesType> attType = ticket.getAttributes();

		for (AttributesType att : attType) {

			// clent BSB

			if (StringUtils.equals("BPAY.CLIENT_REF_NUMBER", att.getType())) {
				attributesInformation.setClientRefNumber(att.getValue());
			}

			//client BSB

			if (StringUtils.equals("BPAY.BSB", att.getType())) {
				attributesInformation.setBsb(att.getValue());
			}

			//Investigation type

			if (StringUtils.equals("BPAY.REQUEST_TYPE", att.getType())) {
				if (prop.getProperty(
						Constants.REQUEST_TYPE_OFI_ERROR_CORRECTION
					).equalsIgnoreCase(
						StringUtils.trimToEmpty(att.getValue())
					)) {

					att.setValue(Constants.BPAY_INVESTIGATION_ERROR_CORRECTION);
				}
				else if (prop.getProperty(
							Constants.REQUEST_TYPE_OFI_ERROR_CORRECTION
						).equalsIgnoreCase(
							StringUtils.trimToEmpty(att.getValue())
						)) {

					att.setValue(Constants.BPAY_INVESTIGATION_ENQUIRY);
				}

				attributesInformation.setInvestigationType(att.getValue());
			}

			//reason

			if (StringUtils.equals("BPAY.REASON", att.getType())) {
				attributesInformation.setReason(att.getValue());
			}

			//Transaction Reference Number

			if (StringUtils.equals(
					"BPAY.TRANSACTION_REF_NUMBER", att.getType())) {

				attributesInformation.setTransactionRefNumber(att.getValue());
			}

			//Transaction Date

			if (StringUtils.equals("BPAY.TRANSACTION_DATE", att.getType())) {
				attributesInformation.setTransactionDate(
					formatDate(att.getValue()));
			}

			//Biller Name

			if (StringUtils.equals("BPAY.BILLER_NAME", att.getType())) {
				attributesInformation.setBillerName(att.getValue());
			}

			//Biller Code

			if (StringUtils.equals("BPAY.BILLER_CODE", att.getType())) {
				attributesInformation.setBillerCode(att.getValue());
			}

			//Correct Biller Code

			if (StringUtils.equals("BPAY.CORRECT_BILLER_CODE", att.getType())) {
				attributesInformation.setCorrectBillerCode(att.getValue());
			}

			if (isOfi) {
				//CRN

				if (StringUtils.equals("BPAY.CRN", att.getType())) {
					attributesInformation.setCustomerRefNumber(att.getValue());
				}

				//Correct CRN

				if (StringUtils.equals("BPAY.CORRECT_CRN", att.getType())) {
					attributesInformation.setCorrectCRN(att.getValue());
				}
			}

			//Payment Method

			if (StringUtils.equals("BPAY.PAYMENT_AMOUNT", att.getType())) {
				attributesInformation.setPaymentAmount(
					formatAmount(att.getValue()));
			}

			//Payment Method

			if (StringUtils.equals("BPAY.PAYMENT_METHOD", att.getType())) {
				if (StringUtils.isNotBlank(att.getValue())) {
					if (isOfi) {
						attributesInformation.setOfiPaymentMethod(
							att.getValue());
					}
					else {
						attributesInformation.setPaymentMethod(att.getValue());
					}
				}
			}

			//Entry Method

			if (StringUtils.equals("BPAY.ENTRY_METHOD", att.getType())) {
				if (StringUtils.isNotBlank(att.getValue())) {
					if (isOfi) {
						attributesInformation.setOfiEntryMethod(att.getValue());
					}
					else {
						attributesInformation.setEntryMethod(att.getValue());
					}
				}
			}

			//Payer Name

			if (StringUtils.equals("BPAY.PAYER_NAME", att.getType())) {
				attributesInformation.setPayerName(att.getValue());
			}

			//Account Name

			if (StringUtils.equals("BPAY.MEMBER_NAME", att.getType())) {
				attributesInformation.setMemberName(att.getValue());
			}

			//Account Number

			if (StringUtils.equals("BPAY.MEMBER_NUMBER", att.getType())) {
				attributesInformation.setMemberNumber(att.getValue());
			}

			//ofi

			if (StringUtils.equals("BPAY.PAYER_NAME", att.getType())) {
				attributesInformation.setOfiPayerName(att.getValue());
			}

			if (StringUtils.equals("BPAY.PAYER_ADDRESS", att.getType())) {
				attributesInformation.setOfiPayerAddress(att.getValue());
			}

			if (StringUtils.equals("BPAY.MEMBER_NUMBER", att.getType())) {
				attributesInformation.setOfiMemberNumber(att.getValue());
			}

			if (StringUtils.equals("BPAY.COMMENT", att.getType())) {
				attributesInformation.setOfiComment(att.getValue());
			}

			if (StringUtils.equals("BPAY.BPAY_ID", att.getType())) {
				attributesInformation.setBpayId(att.getValue());
			}

			//Payer Reported Date

			if (StringUtils.equals("BPAY.PAYER_REPORTED_DATE", att.getType()) &&
				StringUtils.isNotBlank(att.getValue())) {

				attributesInformation.setPayerReportedDate(
					formatDate(att.getValue()));
			}

			//Fraud Type

			if (StringUtils.equals("BPAY.FRAUD_TYPE", att.getType()) &&
				StringUtils.isNotBlank(att.getValue())) {

				attributesInformation.setFraudType(att.getValue());
			}

			//Fraud Info(Other)

			if (StringUtils.equals("BPAY.FRAUD_INFO", att.getType()) &&
				StringUtils.isNotBlank(att.getValue())) {

				attributesInformation.setFraudInfo(att.getValue());
			}

			//Scam Type

			if (StringUtils.equals("BPAY.SCAM_TYPE", att.getType()) &&
				StringUtils.isNotBlank(att.getValue())) {

				attributesInformation.setScamType(att.getValue());
			}

			//Scam Info (Other)

			if (StringUtils.equals("BPAY.SCAM_INFO", att.getType()) &&
				StringUtils.isNotBlank(att.getValue())) {

				attributesInformation.setScamInfo(att.getValue());
			}
		}

		if (!isOfi) {
			if (Constants.BPAY_INVESTIGATION_ENQUIRY.equals(
					attributesInformation.getInvestigationType())) {

				attributesInformation.setPaymentInvestigationReason(
					attributesInformation.getReason());
			}
			else if (Constants.BPAY_INVESTIGATION_ERROR_CORRECTION.equals(
						attributesInformation.getInvestigationType())) {

				attributesInformation.setErrorCorrectionReason(
					attributesInformation.getReason());
			}
		}

		return attributesInformation;
	}

	@Override
	public String updateServiceRequest(
			ServiceRequestForm form, TktRequestHeader header,
			PortletContext portletContext, User user)
		throws Exception {

		logger.debug("updateServiceRequest - start");

		String message = null;
		UpdateSimpleServiceRequestResponse response = null;
		UpdateSimpleServiceRequestRequest request =
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

				setupUpdateServiceRequest(request, form, header, user);

				try {
					response = ticketingService.updateSimpleServiceRequest(
						request);//actual framework web service request integration.
				}
				catch (Exception ex) {
					logger.error(
						"updateServiceRequest - updateSimpleServiceRequest webservice call failed: " +
							ex.getMessage());
				}

				if (null != response) {
					if (null != response.getHeader()) {
						String statusCode = response.getHeader(
						).getStatusCode();
						String ticketId = String.valueOf(
							request.getTicket(
							).getId());

						if (StringUtils.equalsIgnoreCase(
								"SUCCESS", statusCode)) {

							message = statusCode;
							isSuccess = Boolean.TRUE;

							createXmlMessageForServiceRequest(
								xmlMessage, form, statusCode, ticketId);

							logger.debug(
								"addServiceRequest - Successfully added ticket. Status Code:" +
									statusCode);
						}
						else {
							message = statusCode;

							createXmlMessageForServiceRequest(
								xmlMessage, form, statusCode, ticketId);

							logger.error(
								"addServiceRequest - Response header is not successful. Status Code:" +
									statusCode);
						}
					}
					else {
						logger.error(
							"addServiceRequest - Response header is null");
					}
				}
				else {
					logger.error("addServiceRequest - Response is null");
				}
			}
			else {
				logger.error(
					"addServiceRequest - ServiceRequestForm is null. Cannot add ticket");
			}
		}
		else {
			logger.error(
				"addServiceRequest - RequestHeader is null. Cannot add ticket");
		}

		/*audit information.*/
		if (isSuccess) {
			CommonUtil.auditSuccessXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_ADD,
				portletContext.getRequest(), portletContext.getResponse());
		}
		else {
			CommonUtil.auditFailXmlMessage(
				xmlMessage, AuditTicketing.TICKETING_ADD,
				portletContext.getRequest(), portletContext.getResponse());
		}

		logger.debug("addServiceRequest - end");

		return message;
	}

	private String formatAmount(String amountStr) {
		try {
			BigDecimal amount = new BigDecimal(
				StringUtils.trimToEmpty(amountStr));
			DecimalFormat decimalFormat = new DecimalFormat("0.00");

			amountStr = decimalFormat.format(amount);
		}
		catch (Exception e) {
			//ignore
		}

		return amountStr;
	}

	private String formatDate(String dateStr) {
		try {
			SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

			Date date = dt.parse(StringUtils.trimToEmpty(dateStr));
			SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yy");

			dateStr = dt1.format(date);
		}
		catch (Exception e) {
			try {
				SimpleDateFormat dt = new SimpleDateFormat(
					"dd-MMM-yyyy hh:mm:ss");

				Date date = dt.parse(StringUtils.trimToEmpty(dateStr));
				SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yy");

				dateStr = dt1.format(date);
			}
			catch (Exception e2) {
				//ignore
			}
		}

		return dateStr;
	}

	private List<AttachmentDataType> setupAttachmentForServiceRequest(
		ServiceRequestForm form) {

		List<MultipartFile> attachments = form.getRequestAttachments();
		List<AttachmentDataType> attachmentTypes = new ArrayList<>();

		if ((null != attachments) && (attachments.size() > 0)) {
			for (MultipartFile multipartFile : attachments) {
				if (multipartFile.getSize() > 0) {
					AttachmentDataType attachmentType =
						new AttachmentDataType();

					attachmentType.setFilename(
						multipartFile.getOriginalFilename());
					attachmentType.setMimeType(multipartFile.getContentType());

					try {
						DataHandler handlerFile = new DataHandler(
							new ByteArrayDataSource(
								multipartFile.getInputStream(),
								"application/octet-stream"));

						attachmentType.setBlob(handlerFile);
					}
					catch (IOException exception) {
						logger.error(
							"Error occurred setting attachment:" +
								exception.getMessage());
					}

					attachmentTypes.add(attachmentType);
				}
			}
		}

		return attachmentTypes;
	}

	private List<SimpleAttributesType>
		setupAttributesInformationForOfiServiceRequest(
			ServiceRequestForm form) {

		AttributesInformation attributesInformation =
			form.getAttributesInformation();

		Properties prop = bpayProperties.getBpayProperty();
		List<SimpleAttributesType> attributes = new ArrayList<>();
		SimpleAttributesType simpleAttributesType = null;

		BPayListBoxData bPayListBoxData = CommonUtil.populateBPayListBoxData(
			bpayProperties);

		// Payment method

		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			Long.valueOf(prop.getProperty("bpay.payment.method.id")));

		if (StringUtils.isNotBlank(
				attributesInformation.getOfiPaymentMethod())) {

			String paymentMethod = bPayListBoxData.getPaymentMethodMap(
			).get(
				Long.valueOf(
					attributesInformation.getOfiPaymentMethod(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getOfiPaymentMethod(
				).trim());
			simpleAttributesType.setDescription(paymentMethod);
		}

		attributes.add(simpleAttributesType);

		// Entry method

		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			Long.valueOf(prop.getProperty("bpay.entry.method.id")));

		if (StringUtils.isNotBlank(attributesInformation.getOfiEntryMethod())) {
			String entryMethod = bPayListBoxData.getEntryMethodMap(
			).get(
				Long.valueOf(
					attributesInformation.getOfiEntryMethod(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getOfiEntryMethod(
				).trim());
			simpleAttributesType.setDescription(entryMethod);
		}

		attributes.add(simpleAttributesType);

		// Payer name

		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			Long.valueOf(prop.getProperty("bpay.payer.name.id")));

		if (StringUtils.isNotBlank(attributesInformation.getOfiPayerName())) {
			simpleAttributesType.setValue(
				attributesInformation.getOfiPayerName(
				).trim());
		}

		attributes.add(simpleAttributesType);

		// Member Number

		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			Long.valueOf(prop.getProperty("bpay.member.number.id")));

		if (StringUtils.isNotBlank(
				attributesInformation.getOfiMemberNumber())) {

			simpleAttributesType.setValue(
				attributesInformation.getOfiMemberNumber(
				).trim());
		}

		attributes.add(simpleAttributesType);

		// Payer Address

		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			Long.valueOf(prop.getProperty("bpay.payer.address.id")));

		if (StringUtils.isNotBlank(
				attributesInformation.getOfiPayerAddress())) {

			simpleAttributesType.setValue(
				attributesInformation.getOfiPayerAddress(
				).trim());
		}

		attributes.add(simpleAttributesType);

		// Additional information

		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			Long.valueOf(prop.getProperty("bpay.comment.id")));

		if (StringUtils.isNotBlank(attributesInformation.getOfiComment())) {
			simpleAttributesType.setValue(
				attributesInformation.getOfiComment(
				).trim());
		}

		attributes.add(simpleAttributesType);

		// bsb

		simpleAttributesType = new SimpleAttributesType();
		simpleAttributesType.setId(
			Long.valueOf(prop.getProperty("bpay.bsb.id")));

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
		setupAttributesInformationForServiceRequest(ServiceRequestForm form) {

		AttributesInformation attributesInformation =
			form.getAttributesInformation();
		RequestTypeInformation requestTypeInformation =
			form.getRequestTypeInformation();

		Properties prop = bpayProperties.getBpayProperty();
		List<SimpleAttributesType> attributes = new ArrayList<>();
		SimpleAttributesType simpleAttributesType = null;

		BPayListBoxData bPayListBoxData = CommonUtil.populateBPayListBoxData(
			bpayProperties);

		//Client Reference Number

		if (StringUtils.isNotBlank(
				attributesInformation.getClientRefNumber())) {

			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.client.ref.number.id")));
			simpleAttributesType.setValue(
				attributesInformation.getClientRefNumber(
				).trim());

			attributes.add(simpleAttributesType);
		}

		//BSB

		if (StringUtils.isNotBlank(attributesInformation.getBsb())) {
			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.bsb.id")));
			simpleAttributesType.setValue(
				attributesInformation.getBsb(
				).trim());

			attributes.add(simpleAttributesType);
		}

		//request type

		if (StringUtils.isNotBlank(
				attributesInformation.getInvestigationType())) {

			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.request.type.id")));
			simpleAttributesType.setValue(
				attributesInformation.getInvestigationType(
				).trim());
			simpleAttributesType.setDescription(
				bPayListBoxData.getInvestigationTypeMap(
				).get(
					Long.valueOf(
						attributesInformation.getInvestigationType(
						).trim())
				));

			attributes.add(simpleAttributesType);
		}

		//reason

		if (Constants.BPAY_INVESTIGATION_ENQUIRY.equals(
				attributesInformation.getInvestigationType()) &&
			StringUtils.isNotBlank(
				attributesInformation.getPaymentInvestigationReason())) {

			String reason = bPayListBoxData.getReasonPaymentInvestigationMap(
			).get(
				Long.valueOf(
					attributesInformation.getPaymentInvestigationReason(
					).trim())
			);

			simpleAttributesType = new SimpleAttributesType();
			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.reason.id")));
			simpleAttributesType.setValue(
				attributesInformation.getPaymentInvestigationReason(
				).trim());
			simpleAttributesType.setDescription(reason);
			attributes.add(simpleAttributesType);
		}
		else if (Constants.BPAY_INVESTIGATION_ERROR_CORRECTION.equals(
					attributesInformation.getInvestigationType())) {

			if (StringUtils.isNotBlank(
					attributesInformation.getErrorCorrectionReason())) {

				String reason = bPayListBoxData.getReasonErrorCorrectionMap(
				).get(
					Long.valueOf(
						attributesInformation.getErrorCorrectionReason(
						).trim())
				);
				simpleAttributesType = new SimpleAttributesType();
				simpleAttributesType.setId(
					Long.valueOf(prop.getProperty("bpay.reason.id")));
				simpleAttributesType.setValue(
					attributesInformation.getErrorCorrectionReason(
					).trim());
				simpleAttributesType.setDescription(reason);
				attributes.add(simpleAttributesType);
			}

			if (Constants.BPAY_EC_REQUEST_REASON_WRONG_CRN.equals(
					attributesInformation.getErrorCorrectionReason())) {

				//Correct CRN

				if (StringUtils.isNotBlank(
						attributesInformation.getCorrectCRN())) {

					simpleAttributesType = new SimpleAttributesType();

					simpleAttributesType.setId(
						Long.valueOf(prop.getProperty("bpay.correct.crn.id")));
					simpleAttributesType.setValue(
						attributesInformation.getCorrectCRN(
						).trim());

					attributes.add(simpleAttributesType);
				}
			}
			else if (Constants.BPAY_EC_REQUEST_REASON_WRONG_BILLER_CODE.equals(
						attributesInformation.getErrorCorrectionReason())) {

				//correct biller code

				if (StringUtils.isNotBlank(
						attributesInformation.getCorrectBillerCode())) {

					simpleAttributesType = new SimpleAttributesType();

					simpleAttributesType.setId(
						Long.valueOf(
							prop.getProperty("bpay.correct.biller.code.id")));
					simpleAttributesType.setValue(
						attributesInformation.getCorrectBillerCode(
						).trim());

					attributes.add(simpleAttributesType);
				}
			}
			//Fraud Type
			else if (Constants.BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE.
						equals(
							attributesInformation.getErrorCorrectionReason())) {

				if (StringUtils.isNotBlank(
						attributesInformation.getFraudType())) {

					simpleAttributesType = new SimpleAttributesType();

					simpleAttributesType.setId(
						Long.valueOf(prop.getProperty("bpay.fraud.type.id")));
					String fraudType = bPayListBoxData.getFraudTypeMap(
					).get(
						Long.valueOf(
							attributesInformation.getFraudType(
							).trim())
					);
					simpleAttributesType.setValue(
						attributesInformation.getFraudType(
						).trim());
					simpleAttributesType.setDescription(fraudType);
					attributes.add(simpleAttributesType);

					//if fraud type is victim of scam

					if (Constants.
							BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE_AND_FRAUD_TYPE_VICTIM_OF_SCAM.
								equals(attributesInformation.getFraudType())) {

						if (StringUtils.isNotBlank(
								attributesInformation.getScamType())) {

							simpleAttributesType = new SimpleAttributesType();

							simpleAttributesType.setId(
								Long.valueOf(
									prop.getProperty("bpay.scam.type.id")));
							String scamType = bPayListBoxData.getScamTypeMap(
							).get(
								Long.valueOf(
									attributesInformation.getScamType(
									).trim())
							);
							simpleAttributesType.setValue(
								attributesInformation.getScamType(
								).trim());
							simpleAttributesType.setDescription(scamType);
							attributes.add(simpleAttributesType);

							//If Scam type is Other

							if (Constants.
									BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE_AND_SCAM_TYPE_OTHER.
										equals(
											attributesInformation.
												getScamType())) {

								if (StringUtils.isNotBlank(
										attributesInformation.getScamInfo())) {

									simpleAttributesType =
										new SimpleAttributesType();

									simpleAttributesType.setId(
										Long.valueOf(
											prop.getProperty(
												"bpay.scam.info.id")));
									simpleAttributesType.setValue(
										attributesInformation.getScamInfo(
										).trim());

									attributes.add(simpleAttributesType);
								}
							}
						}
					}
					//if fraud type is other
					else if (Constants.
								BPAY_EC_REQUEST_REASON_PAYER_DID_NOT_AUTHORIZE_AND_FRAUD_TYPE_OTHER.
									equals(
										attributesInformation.getFraudType())) {

						if (StringUtils.isNotBlank(
								attributesInformation.getFraudInfo())) {

							simpleAttributesType = new SimpleAttributesType();

							simpleAttributesType.setId(
								Long.valueOf(
									prop.getProperty("bpay.fraud.info.id")));
							simpleAttributesType.setValue(
								attributesInformation.getFraudInfo(
								).trim());

							attributes.add(simpleAttributesType);
						}
					}
				}
			}
		}

		//Transaction reference number

		if (StringUtils.isNotBlank(
				attributesInformation.getTransactionRefNumber())) {

			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.trn.id")));
			simpleAttributesType.setValue(
				attributesInformation.getTransactionRefNumber(
				).trim());

			attributes.add(simpleAttributesType);
		}
		//Transaction date

		if (StringUtils.isNotBlank(
				attributesInformation.getTransactionDate())) {

			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.trn.date.id")));
			simpleAttributesType.setValue(
				attributesInformation.getTransactionDate(
				).trim());

			attributes.add(simpleAttributesType);
		}

		//Biller name

		if (StringUtils.isNotBlank(attributesInformation.getBillerName())) {
			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.biller.name.id")));
			simpleAttributesType.setValue(
				attributesInformation.getBillerName(
				).trim());

			attributes.add(simpleAttributesType);
		}

		//Biller code

		if (StringUtils.isNotBlank(attributesInformation.getBillerCode())) {
			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.biller.code.id")));
			simpleAttributesType.setValue(
				attributesInformation.getBillerCode(
				).trim());

			attributes.add(simpleAttributesType);
		}

		//CRN

		if (StringUtils.isNotBlank(
				attributesInformation.getCustomerRefNumber())) {

			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.crn.id")));
			simpleAttributesType.setValue(
				attributesInformation.getCustomerRefNumber(
				).trim());

			attributes.add(simpleAttributesType);
		}

		//Payment amount

		if (StringUtils.isNotBlank(attributesInformation.getPaymentAmount())) {
			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.payment.amount.id")));
			simpleAttributesType.setValue(
				attributesInformation.getPaymentAmount(
				).trim());

			attributes.add(simpleAttributesType);
		}

		//Payment method

		if (StringUtils.isNotBlank(attributesInformation.getPaymentMethod())) {
			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.payment.method.id")));
			String paymentMethod = bPayListBoxData.getPaymentMethodMap(
			).get(
				Long.valueOf(
					attributesInformation.getPaymentMethod(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getPaymentMethod(
				).trim());
			simpleAttributesType.setDescription(paymentMethod);
			attributes.add(simpleAttributesType);
		}

		//Entry method

		if (StringUtils.isNotBlank(attributesInformation.getEntryMethod())) {
			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.entry.method.id")));
			String entryMethod = bPayListBoxData.getEntryMethodMap(
			).get(
				Long.valueOf(
					attributesInformation.getEntryMethod(
					).trim())
			);
			simpleAttributesType.setValue(
				attributesInformation.getEntryMethod(
				).trim());
			simpleAttributesType.setDescription(entryMethod);

			attributes.add(simpleAttributesType);
		}

		//Payer name

		if (StringUtils.isNotBlank(attributesInformation.getPayerName())) {
			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.payer.name.id")));
			simpleAttributesType.setValue(
				attributesInformation.getPayerName(
				).trim());

			attributes.add(simpleAttributesType);
		}

		//Member name

		if (StringUtils.isNotBlank(attributesInformation.getMemberName())) {
			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.member.name.id")));
			simpleAttributesType.setValue(
				attributesInformation.getMemberName(
				).trim());

			attributes.add(simpleAttributesType);
		}

		//Member Number

		if (StringUtils.isNotBlank(attributesInformation.getMemberNumber())) {
			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.member.number.id")));
			simpleAttributesType.setValue(
				attributesInformation.getMemberNumber(
				).trim());

			attributes.add(simpleAttributesType);
		}

		if (StringUtils.isNotBlank(attributesInformation.getBpayId())) {
			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.bpay.id.id")));
			simpleAttributesType.setValue(
				attributesInformation.getBpayId(
				).trim());

			attributes.add(simpleAttributesType);
		}

		//Payer Reported Date

		if (StringUtils.isNotBlank(
				attributesInformation.getPayerReportedDate())) {

			simpleAttributesType = new SimpleAttributesType();

			simpleAttributesType.setId(
				Long.valueOf(prop.getProperty("bpay.payer.reported.date.id")));
			simpleAttributesType.setValue(
				attributesInformation.getPayerReportedDate(
				).trim());

			attributes.add(simpleAttributesType);
		}

		return attributes;
	}

	private String setupNoteForServiceRequest(ServiceRequestForm form) {
		return StringUtils.trimToNull(
			form.getAttributesInformation(
			).getComment());
	}

	/**
	 * Setup the Request Type for the Service Request.
	 *
	 * @param 	requestTypeInformation	<em>RequestTypeInformation</em>
	 *
	 * @return	RequestTypeType
	 */
	private RequestTypeType setupRequestTypeInformationForServiceRequest(
		ServiceRequestForm form) {

		AttributesInformation attributesInformation =
			form.getAttributesInformation();
		RequestTypeInformation requestTypeInformation =
			form.getRequestTypeInformation();

		BPayListBoxData bPayListBoxData = CommonUtil.populateBPayListBoxData(
			bpayProperties);

		RequestTypeType requestType = new RequestTypeType();

		if (null != attributesInformation) {
			requestType.setProductId(
				Long.valueOf(requestTypeInformation.getProductCode()));
			requestType.setTypeId(
				Long.valueOf(requestTypeInformation.getRequestTypeId()));

			if (form.isOfi()) {
				requestType.setTypeDescription(
					attributesInformation.getInvestigationType());
			}
			else {
				if (StringUtils.isNotBlank(
						attributesInformation.getInvestigationType())) {

					requestType.setTypeDescription(
						bPayListBoxData.getInvestigationTypeMap(
						).get(
							Long.valueOf(
								attributesInformation.getInvestigationType(
								).trim())
						));
				}
			}
		}

		return requestType;
	}

	private SimpleServiceRequestRequestType setupServiceRequest(
			ServiceRequestForm form)
		throws Exception {

		SimpleServiceRequestRequestType request = null;

		if (null != form) {
			request = new SimpleServiceRequestRequestType();

			//Contact Information section.

			if (null != form.getContactInformation()) {
				ContactType contact = setupServiceRequestForContactInfo(
					form.getContactInformation());

				request.setContact(contact);
			}

			//Request Type Information section.

			if (null != form.getRequestTypeInformation()) {
				RequestTypeType requestType =
					setupRequestTypeInformationForServiceRequest(form);

				request.setRequestType(requestType);
			}

			//Attributes Information section.

			if (null != form.getAttributesInformation()) {
				List<SimpleAttributesType> attributes =
					setupAttributesInformationForServiceRequest(form);

				request.getAttributes(
				).addAll(
					attributes
				);

				request.setNote(setupNoteForServiceRequest(form));
			}

			if (null != form.getRequestAttachments()) {
				List<AttachmentDataType> attachmentTypes =
					setupAttachmentForServiceRequest(form);

				request.getAttachments(
				).addAll(
					attachmentTypes
				);
			}
		}

		return request;
	}

	/**
	 * Create the ContactType Object to add Contact Information to the database.
	 *
	 * @param 	form	<code>ServiceRequestForm</code>
	 * @return	ContactType
	 */
	private ContactType setupServiceRequestForContactInfo(
			RequestContactInformation form)
		throws Exception {

		ContactType contact = new ContactType();

		// Contact Information section

		contact.setFirstName(form.getGivenName());
		contact.setSurname(form.getSurname());
		contact.setEmail(form.getEmail());
		contact.setPhoneNo(form.getPhoneNumber());
		contact.setOrganisation(form.getOrganisation());

		return contact;
	}

	private void setupUpdateServiceRequest(
			UpdateSimpleServiceRequestRequest request, ServiceRequestForm form,
			TktRequestHeader header, User user)
		throws Exception {

		TicketType ticket = new TicketType();

		ticket.setId(Long.valueOf(form.getTicketId()));
		ticket.setOrgShortName(header.getUserOrgId());
		request.setTicket(ticket);

		if (null != form.getRequestTypeInformation()) {
			RequestTypeType requestType =
				setupRequestTypeInformationForServiceRequest(form);

			request.setRequestType(requestType);
		}

		//Attributes Information section.

		if (null != form.getAttributesInformation()) {
			List<SimpleAttributesType> attributes, ofiAttributes = null;

			if (form.isOfi()) {
				ofiAttributes = setupAttributesInformationForOfiServiceRequest(
					form);
				request.getAttributes(
				).addAll(
					ofiAttributes
				);

				String ofiUpdateNote = MessageFormat.format(
					Constants.OFI_UPDATE_NOTE, user.getFullName());

				form.getAttributesInformation(
				).setComment(
					ofiUpdateNote
				);
			}
			else {
				attributes = setupAttributesInformationForServiceRequest(form);
				request.getAttributes(
				).addAll(
					attributes
				);
			}

			request.setNote(setupNoteForServiceRequest(form));
		}

		if (null != form.getRequestAttachments()) {
			List<AttachmentDataType> attachmentTypes =
				setupAttachmentForServiceRequest(form);

			request.getAttachments(
			).addAll(
				attachmentTypes
			);
		}
	}

	private static Logger logger = Logger.getLogger(
		CuscalTicketingServiceImpl.class);

	@Autowired
	@Qualifier("bpayProperties")
	private BPayProperties bpayProperties;

	private EntityService entityService;
	private CustomerSelfServiceService ticketingService;

}