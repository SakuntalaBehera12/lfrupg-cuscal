package au.com.cuscal.transactions.services;

import au.com.cuscal.common.framework.memory.PermGenUtil;
import au.com.cuscal.connect.util.resource.dxp.UserUtilImpl;
import au.com.cuscal.framework.audit.Audit;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.category.AuditCategories;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.audit.impl.log4j.Log4jAuditor;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.webservices.selfservice.ARNType;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.AttachmentDataType;
import au.com.cuscal.framework.webservices.selfservice.AttributesListType;
import au.com.cuscal.framework.webservices.selfservice.AttributesType;
import au.com.cuscal.framework.webservices.selfservice.CardHolderInfoType;
import au.com.cuscal.framework.webservices.selfservice.ContactType;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesListRequest;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesListResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTransactionDetailsRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTransactionDetailsResponse;
import au.com.cuscal.framework.webservices.selfservice.POSConditionType;
import au.com.cuscal.framework.webservices.selfservice.RawTransactionAttributesType;
import au.com.cuscal.framework.webservices.selfservice.RequestTypeType;
import au.com.cuscal.framework.webservices.selfservice.ServiceRequestRequestType;
import au.com.cuscal.framework.webservices.selfservice.ServiceRequestResponseType;
import au.com.cuscal.framework.webservices.selfservice.StatusType;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.selfservice.TransactionType;
import au.com.cuscal.framework.webservices.selfservice.TypeType;
import au.com.cuscal.framework.webservices.selfservice.UserType;
import au.com.cuscal.framework.webservices.transaction.AmountOperatorEnum;
import au.com.cuscal.framework.webservices.transaction.AmountTypeEnum;
import au.com.cuscal.framework.webservices.transaction.As2805ResponseCode;
import au.com.cuscal.framework.webservices.transaction.AttributeType;
import au.com.cuscal.framework.webservices.transaction.Customer;
import au.com.cuscal.framework.webservices.transaction.DataSourceEnum;
import au.com.cuscal.framework.webservices.transaction.DateTypeEnum;
import au.com.cuscal.framework.webservices.transaction.Entity;
import au.com.cuscal.framework.webservices.transaction.EntityAttribute;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdRequestType;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdResponseType;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsRequestType;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsResponseType;
import au.com.cuscal.framework.webservices.transaction.GetAs2805ResponseCodesRequestType;
import au.com.cuscal.framework.webservices.transaction.GetAs2805ResponseCodesResponseType;
import au.com.cuscal.framework.webservices.transaction.GetCustomerAccessViewRequestType;
import au.com.cuscal.framework.webservices.transaction.GetCustomerAccessViewResponseType;
import au.com.cuscal.framework.webservices.transaction.GetMessageCodesRequestType;
import au.com.cuscal.framework.webservices.transaction.GetMessageCodesResponseType;
import au.com.cuscal.framework.webservices.transaction.GetPCTUserByIdRequestType;
import au.com.cuscal.framework.webservices.transaction.GetPCTUserByIdResponseType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionRequestType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionResponseType;
import au.com.cuscal.framework.webservices.transaction.Institution;
import au.com.cuscal.framework.webservices.transaction.MessageCode;
import au.com.cuscal.framework.webservices.transaction.MessageCodeGroup;
import au.com.cuscal.framework.webservices.transaction.PanOrBinEnum;
import au.com.cuscal.framework.webservices.transaction.ResponseCode;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;
import au.com.cuscal.framework.webservices.transaction.StandardHeader;
import au.com.cuscal.framework.webservices.transaction.TransactionBasicInformation;
import au.com.cuscal.transactions.commons.AuthenticationCode;
import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.commons.FunctionCode;
import au.com.cuscal.transactions.commons.SelfServiceProperties;
import au.com.cuscal.transactions.commons.TransactionAppProperties;
import au.com.cuscal.transactions.commons.Utility;
import au.com.cuscal.transactions.domain.Codes;
import au.com.cuscal.transactions.domain.ContactInformation;
import au.com.cuscal.transactions.domain.MemberInformation;
import au.com.cuscal.transactions.domain.ReasonData;
import au.com.cuscal.transactions.domain.RequestDetails;
import au.com.cuscal.transactions.domain.ResponseDetails;
import au.com.cuscal.transactions.domain.TicketDetails;
import au.com.cuscal.transactions.domain.TransactionDetail;
import au.com.cuscal.transactions.domain.TransactionList;
import au.com.cuscal.transactions.forms.PinChangeSearchForm;
import au.com.cuscal.transactions.forms.RequestAdditionalTransactionInformation;
import au.com.cuscal.transactions.forms.RequestAtmPosClaimInformation;
import au.com.cuscal.transactions.forms.RequestMemberInformation;
import au.com.cuscal.transactions.forms.RequestTransactionInformation;
import au.com.cuscal.transactions.forms.ServiceRequestForm;
import au.com.cuscal.transactions.forms.TransactionForm;
import au.com.cuscal.transactions.services.client.CommonService;
import au.com.cuscal.transactions.services.client.PctUserSearchServiceImpl;
import au.com.cuscal.transactions.services.client.TransactionService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.activation.DataHandler;

import javax.mail.util.ByteArrayDataSource;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rajni Bharara
 *
 */

/**
 * @author jluu
 *
 */
@Service(Constants.TRANSACTION_SERVICE)
public class TransactionSearchServiceImpl implements TransactionSearchService {

	/**
	 *
	 * @return
	 */
	public static List<String> getChargebackPilotOrgs() {
		return chargebackPilotOrgs;
	}

	public static void setChargebackPilotOrgs(
		List<String> chargebackPilotOrgs) {

		TransactionSearchServiceImpl.chargebackPilotOrgs = chargebackPilotOrgs;
	}

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
		AddServiceRequestResponse response = null;
		AddServiceRequestRequest request = new AddServiceRequestRequest();

		if (null != header) {
			request.setHeader(header);

			if (null != form) {
				ServiceRequestRequestType requestType =
					setupServiceRequestForTransaction(form);

				request.setServiceRequestRequest(requestType);
				response = transactionService.addServiceRequest(request);

				if (null != response) {
					if (null != response.getHeader()) {
						String statusCode = response.getHeader(
						).getStatusCode();

						if (StringUtils.equalsIgnoreCase(
								"SUCCESS", statusCode)) {

							message = statusCode;

							audit.success(
								portletContext.getResponse(),
								portletContext.getRequest(),
								AuditOrigin.PORTAL_ORIGIN,
								AuditCategories.TICKETING_ADD,
								createSearchParamsForAddServiceRequest(
									form, statusCode, response));

							logger.debug(
								"addServiceRequest - Successfully added ticket. Status Code:" +
									statusCode);
						}
						else {
							message = statusCode;

							audit.fail(
								portletContext.getResponse(),
								portletContext.getRequest(),
								AuditOrigin.PORTAL_ORIGIN,
								AuditCategories.TICKETING_ADD,
								createSearchParamsForAddServiceRequest(
									form, statusCode, response));

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

		logger.debug("addServiceRequest - end");

		return message;
	}

	/**
	 * create date in XMLGregorianCalendar
	 *
	 * @param String
	 * @param String
	 * @param String
	 * @return XMLGregorianCalendar
	 */
	public XMLGregorianCalendar createDateAsGregorianCalendarType(
		String date, String dateHr, String dateMin) {

		DatatypeFactory datatypeFactory = null;

		try {
			datatypeFactory = DatatypeFactory.newInstance();
		}
		catch (DatatypeConfigurationException dce) {
			throw new IllegalStateException(
				"Failed to obtain DatatypeFactory instance:", dce);
		}

		Date testDate = Utility.formatDate(date, Constants.DATE_FORMAT);

		GregorianCalendar gc = new GregorianCalendar();

		gc.setTime(testDate);

		if ((dateHr != null) && (dateMin != null)) {
			gc.set(GregorianCalendar.HOUR_OF_DAY, Integer.valueOf(dateHr));
			gc.set(GregorianCalendar.MINUTE, Integer.valueOf(dateMin));
		}

		logger.debug(
			"END DATE in createDateAsGregorianCalendarType in gc is " +
				gc.getTime());
		logger.debug("Calender is gc.getTime " + gc.getTime());

		XMLGregorianCalendar gregoriandate =
			datatypeFactory.newXMLGregorianCalendar(gc);

		logger.debug(
			"Calender is XMLGregorianCalendar FINAL " +
				gregoriandate.toString());
		logger.debug(
			"END DATE in createDateAsGregorianCalendarType method is " +
				gregoriandate.toString());

		return gregoriandate;
	}

	/**
	 * Create a new PinChangeSearchForm object and return that to the Controller.
	 *
	 * @return	PinChangeSearchForm.
	 */
	public PinChangeSearchForm createPinChangeSearchFormObject() {
		PinChangeSearchForm pinChangeForm = new PinChangeSearchForm();

		String[] startMinuteDisplay = transactionAppProperties.getTransProps(
		).getProperty(
			Constants.UI_START_MINS_PROPSKEY
		).split(
			Constants.COMMA
		);
		String[] startHourDisplay = transactionAppProperties.getTransProps(
		).getProperty(
			Constants.UI_START_HR_PROPSKEY
		).split(
			Constants.COMMA
		);
		String[] endHourDisplay = transactionAppProperties.getTransProps(
		).getProperty(
			Constants.UI_END_HR_PROPSKEY
		).split(
			Constants.COMMA
		);
		String[] endMinuteDisplay = transactionAppProperties.getTransProps(
		).getProperty(
			Constants.UI_END_MINS_PROPSKEY
		).split(
			Constants.COMMA
		);

		pinChangeForm.setStartHourDisplay(startHourDisplay);
		pinChangeForm.setEndHourDisplay(endHourDisplay);
		pinChangeForm.setStartMinuteDisplay(startMinuteDisplay);
		pinChangeForm.setEndMinuteDisplay(endMinuteDisplay);

		return pinChangeForm;
	}

	/**
	 * Create Search params for auditing transaction and PCT history details
	 *
	 * @param TransactionDetail
	 * @param int
	 * @param String
	 * @return String
	 */
	public String createSearchParamsForAddServiceRequest(
		ServiceRequestForm serviceRequestForm, String statusCode,
		AddServiceRequestResponse response) {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<audit><parameters>");

		if (null != serviceRequestForm) {
			if (null != serviceRequestForm.getTransactionInformation()) {
				if (!StringUtils.isBlank(
						serviceRequestForm.getTransactionInformation(
						).getCuscalTransactionId())) {

					stringBuilder.append(
						"<transactionId>" +
							serviceRequestForm.getTransactionInformation(
							).getCuscalTransactionId() + "</transactionId>");
				}

				if (!StringUtils.isBlank(
						serviceRequestForm.getTransactionInformation(
						).getBusinessDate())) {

					stringBuilder.append(
						"<businessDate>" +
							serviceRequestForm.getTransactionInformation(
							).getBusinessDate() + "</businessDate>");
				}
			}
		}

		if (serviceRequestForm.getAtmPosClaimInformation() != null) {
			stringBuilder.append("<atmPosClaimInformation>");

			if (!StringUtils.isBlank(
					serviceRequestForm.getAtmPosClaimInformation(
					).getRequestDisputeType()) &&
				!"0".equals(
					serviceRequestForm.getAtmPosClaimInformation(
					).getRequestDisputeType())) {

				stringBuilder.append(
					"<disputeType>" +
						serviceRequestForm.getAtmPosClaimInformation(
						).getRequestDisputeType() + "</disputeType>");
			}

			if (!StringUtils.isBlank(
					serviceRequestForm.getAtmPosClaimInformation(
					).getRequestAtmReason()) &&
				!"0".equals(
					serviceRequestForm.getAtmPosClaimInformation(
					).getRequestAtmReason())) {

				stringBuilder.append(
					"<atmReason>" +
						serviceRequestForm.getAtmPosClaimInformation(
						).getRequestAtmReason() + "</atmReason>");
			}

			if (!StringUtils.isBlank(
					serviceRequestForm.getAtmPosClaimInformation(
					).getRequestPosReason()) &&
				!"0".equals(
					serviceRequestForm.getAtmPosClaimInformation(
					).getRequestPosReason())) {

				stringBuilder.append(
					"<posReason>" +
						serviceRequestForm.getAtmPosClaimInformation(
						).getRequestAtmFee() + "</posReason>");
			}

			if (!StringUtils.isBlank(
					serviceRequestForm.getAtmPosClaimInformation(
					).getRequestClaimAmount())) {

				stringBuilder.append(
					"<claimAmount>" +
						serviceRequestForm.getAtmPosClaimInformation(
						).getRequestClaimAmount() + "</claimAmount>");
			}

			stringBuilder.append("</atmPosClaimInformation>");
		}
		else if (serviceRequestForm.getVisaTransactionInformation() != null) {
			stringBuilder.append("<visaTransactionInformation>");

			if (!StringUtils.isBlank(
					serviceRequestForm.getVisaTransactionInformation(
					).getRequestType())) {

				stringBuilder.append(
					"<requestType>" +
						serviceRequestForm.getVisaTransactionInformation(
						).getRequestType() + "</requestType>");
			}

			if (!StringUtils.isBlank(
					serviceRequestForm.getVisaTransactionInformation(
					).getFraudType())) {

				stringBuilder.append(
					"<fraudType>" +
						serviceRequestForm.getVisaTransactionInformation(
						).getFraudType() + "</fraudType>");
			}

			if (!StringUtils.isBlank(
					serviceRequestForm.getVisaTransactionInformation(
					).getVoucherReason())) {

				stringBuilder.append(
					"<voucherReason>" +
						serviceRequestForm.getVisaTransactionInformation(
						).getVoucherReason() + "</voucherReason>");
			}

			if (!StringUtils.isBlank(
					serviceRequestForm.getVisaTransactionInformation(
					).getFraudNotificationCode())) {

				stringBuilder.append(
					"<fraudNotificationCode>" +
						serviceRequestForm.getVisaTransactionInformation(
						).getFraudNotificationCode() +
							"</fraudNotificationCode>");
			}

			if (!StringUtils.isBlank(
					serviceRequestForm.getVisaTransactionInformation(
					).getDetection())) {

				stringBuilder.append(
					"<detection>" +
						serviceRequestForm.getVisaTransactionInformation(
						).getDetection() + "</detection>");
			}

			stringBuilder.append("</visaTransactionInformation>");
		}

		stringBuilder.append("</parameters><result>");

		if (null != response) {
			if ((null != response.getServiceRequestResponse()) &&
				(null != response.getServiceRequestResponse(
				).getTicket())) {

				stringBuilder.append(
					"<ticketId>" +
						response.getServiceRequestResponse(
						).getTicket(
						).getId() + "</ticketId>");
			}
		}

		stringBuilder.append(
			"<statusCode>" + statusCode + "</statusCode></result></audit>");

		return stringBuilder.toString();
	}

	/**
	 * Create Search params for auditing transaction and PCT history details
	 *
	 * @param TransactionDetail
	 * @param int
	 * @param String
	 * @return String
	 */
	public String createSearchParamsForTransactionDetails(
		TransactionDetail transactionDetail, int sizeOfListFound,
		String statusCode) {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<audit><parameters>");

		stringBuilder.append(
			"<transactionId>" + transactionDetail.getTransactionId() +
				"</transactionId>");
		stringBuilder.append(
			"<busniessDate>" + transactionDetail.getBusDate() +
				"</busniessDate>");
		stringBuilder.append(
			"<dataSrc>" + transactionDetail.getDataSrc() + "</dataSrc>");

		stringBuilder.append(
			"</parameters><result><found>" + sizeOfListFound +
				"</found><statusCode>" + statusCode +
					"</statusCode></result></audit>");

		return stringBuilder.toString();
	}

	/**
	 * Create Search params for auditing for transaction search
	 *
	 * @param TransactionForm
	 * @param int
	 * @param String
	 * @return String
	 */
	public String createSearchParamsStr(
		TransactionForm transactionForm, int sizeOfListFound,
		String statusCode) {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<audit><parameters>");

		stringBuilder.append(
			"<PANorBin>" + transactionForm.getMaskedPanBin() +
				"</PANorBin><startDate>" + transactionForm.getStartDate() +
					"</startDate><startTimeHrMin>" +
						transactionForm.getStartTimeHr() + ":" +
							transactionForm.getStartTimeMin() +
								"</startTimeHrMin><endDate>" +
									transactionForm.getEndDate() +
										"</endDate><endTimeHrMin>" +
											transactionForm.getEndTimeHr() +
												":" +
													transactionForm.
														getEndTimeMin() +
															"</endTimeHrMin>");

		if (!StringUtils.isBlank(transactionForm.getTerminalId())) {
			stringBuilder.append(
				"<terminalId>" + transactionForm.getTerminalId() +
					"</terminalId>");
		}

		if (!StringUtils.isBlank(transactionForm.getStan())) {
			stringBuilder.append(
				"<stan>" + transactionForm.getStan() + "</stan>");
		}

		if (!StringUtils.isBlank(transactionForm.getTerminalId())) {
			stringBuilder.append(
				"<terminalId>" + transactionForm.getTerminalId() +
					"</terminalId>");
		}

		if (!StringUtils.isBlank(transactionForm.getTerminalId())) {
			stringBuilder.append(
				"<terminalId>" + transactionForm.getTerminalId() +
					"</terminalId>");
		}

		if (!StringUtils.isBlank(transactionForm.getMerchantId())) {
			stringBuilder.append(
				"<cardAcceptorId>" + transactionForm.getMerchantId() +
					"</cardAcceptorId>");
		}

		if (!StringUtils.isBlank(transactionForm.getExternalTransactionId())) {
			stringBuilder.append(
				"<externalTransactionId>" +
					transactionForm.getExternalTransactionId() +
						"</externalTransactionId>");
		}

		if (!StringUtils.isBlank(transactionForm.getAmount())) {
			if (!StringUtils.isBlank(transactionForm.getConditions())) {
				stringBuilder.append(
					"<conditions>" + transactionForm.getConditions() +
						"</conditions>");
			}

			stringBuilder.append(
				"<amount>" + transactionForm.getAmount() + "</amount>");
		}

		if (!StringUtils.isBlank(transactionForm.getResponseCode())) {
			stringBuilder.append(
				"<responseCode>" + transactionForm.getResponseCode() +
					"</responseCode>");
		}

		if (!StringUtils.isBlank(transactionForm.getMessageCode())) {
			stringBuilder.append(
				"<messageType>" + transactionForm.getMessageCode() +
					"</messageType>");
		}

		stringBuilder.append(
			"</parameters><result><found>" + sizeOfListFound +
				"</found><statusCode>" + statusCode +
					"</statusCode></result></audit>");

		return stringBuilder.toString();
	}

	/*@Autowired
	@Qualifier("ticketingService")
	private TransactionsTicketingServiceImpl ticketingService;*/

	/**
	 * Create the transaction description from processing code
	 *
	 * @param String
	 * @return String
	 */
	public String createTransactionDescriptionString(String processingCode) {
		StringBuilder builder = new StringBuilder();

		logger.debug("createTransactionDescriptionString - start");

		logger.debug(
			"Calling transactionService.getAccountIdentificationProps() to create a map of all allowed tyoes of Accuont Identifications  at  " +
				Calendar.getInstance(
				).getTime());

		perfLogger_.debug(
			"transactionService.getAccountIdentificationProps() - begin");
		HashMap<String, String> hashMap =
			transactionService.getAccountIdentificationProps();
		logger.debug(
			"Finished transactionService.getAccountIdentificationProps() at " +
				Calendar.getInstance(
				).getTime());

		perfLogger_.debug(
			"transactionService.getAccountIdentificationProps() - finish");

		int transactionCode = Integer.valueOf(processingCode.substring(0, 2));
		String acId1Pos = "" + processingCode.charAt(2);
		String acId2Pos = "" + processingCode.charAt(4);

		if ((transactionCode >= 40) && (transactionCode <= 49)) {
			if (hashMap.containsKey(acId1Pos) && hashMap.containsKey(acId2Pos))
				builder.append(
					hashMap.get(acId1Pos) + " to " + hashMap.get(acId2Pos));
		}
		else {
			if (hashMap.containsKey(acId1Pos))
				builder.append(hashMap.get(acId1Pos));
		}

		logger.debug("createTransactionDescriptionString - end");

		return builder.toString();
	}

	/**
	 * Create a new object of TransactionForm with default value .
	 *
	 * @return TransactionForm
	 */
	// @Cacheable("searchStartEndTime")

	public TransactionForm createTransactionFormObject() {
		TransactionForm transactionForm = new TransactionForm();

		String[] startMinuteDisplay = transactionAppProperties.getTransProps(
		).getProperty(
			Constants.UI_START_MINS_PROPSKEY
		).split(
			Constants.COMMA
		);
		String[] startHourDisplay = transactionAppProperties.getTransProps(
		).getProperty(
			Constants.UI_START_HR_PROPSKEY
		).split(
			Constants.COMMA
		);
		String[] endHourDisplay = transactionAppProperties.getTransProps(
		).getProperty(
			Constants.UI_END_HR_PROPSKEY
		).split(
			Constants.COMMA
		);
		String[] endMinuteDisplay = transactionAppProperties.getTransProps(
		).getProperty(
			Constants.UI_END_MINS_PROPSKEY
		).split(
			Constants.COMMA
		);

		transactionForm.setStartHourDisplay(startHourDisplay);
		transactionForm.setEndHourDisplay(endHourDisplay);
		transactionForm.setStartMinuteDisplay(startMinuteDisplay);
		transactionForm.setEndMinuteDisplay(endMinuteDisplay);
		transactionForm.setDateDiff(
			Integer.valueOf(
				transactionAppProperties.getTransProps(
				).getProperty(
					Constants.CUD_SEARCH_MAX_DAYS
				)
			).intValue());

		return transactionForm;
	}

	/**
	 * Find ServiceRequests for a particular transaction.
	 *
	 * @param header
	 * @return
	 */
	public List<TicketDetails> findServiceRequest(
		String transactionId, String businessDate, TktRequestHeader header) {

		logger.debug("findServiceRequest - start");
		List<TicketDetails> ticketDetailsList = null;
		FindServiceRequestResponse response = null;
		FindServiceRequestRequest request = new FindServiceRequestRequest();

		if ((null != header) && StringUtils.isNotBlank(transactionId) &&
			StringUtils.isNotBlank(businessDate)) {

			request.setHeader(header);
			request.setTransactionId(Long.valueOf(transactionId));
			logger.debug(
				"findServiceRequest - transactionId: " + transactionId);
			logger.debug("findServiceRequest - businessDate: " + businessDate);
			XMLGregorianCalendar xmlCal =
				Utility.createXmlGregorianCalendarFromString(
					businessDate, "yyyyMMdd", Boolean.FALSE);

			if (null != xmlCal) {
				request.setBusinessDate(xmlCal);

				response = new FindServiceRequestResponse();
				response = transactionService.findServiceRequest(request);

				if (null != response) {
					if (null != response.getHeader()) {
						String statusCode = response.getHeader(
						).getStatusCode();

						if (StringUtils.equalsIgnoreCase(
								"SUCCESS", statusCode)) {

							ticketDetailsList = decoratedServiceRequestList(
								response.getServiceRequests());

							logger.debug(
								"findServiceRequest - response tickets size: " +
									ticketDetailsList.size());
						}
						else if (StringUtils.equalsIgnoreCase(
									Constants.RECORD_NOT_FOUND, statusCode)) {

							ticketDetailsList = new ArrayList<>();
							logger.debug(
								"findServiceRequest - Find tickets response: " +
									statusCode);
						}
						else {
							logger.error(
								"findServiceRequest - WS Response status is not success: " +
									statusCode);
						}
					}
					else {
						logger.error(
							"findServiceRequest - WS Response Header is null");
					}
				}
				else {
					logger.error("findServiceRequest - WS Response is null");
				}
			}
		}

		logger.debug("findServiceRequest - end");

		return ticketDetailsList;
	}

	//private CardServiceImpl cardService;

	/**
	 * Find out the transaction code description based on transaction code and
	 * account identification from processing code.
	 *
	 * @param String
	 * @param String
	 * @param String
	 * @return String
	 */
	public String findTransactionCodeDescriptionFromProcessingCode(
		String processingCode, String trlId, String trlBusinessDate,
		String posMerchantCode) {

		logger.debug(
			"findTransactionCodeDescriptionFromProcessingCode - start");

		String processingCodeDescription = "";

		if (!StringUtils.isBlank(processingCode)) {
			int processCodeCount = processingCode.length();

			boolean validProcessingCode = false;

			if (6 == processCodeCount) {
				logger.debug(
					"The processing code is 6 digits " + processingCode);

				String transactionCode = processingCode.substring(0, 2);

				processingCodeDescription =
					transactionAppProperties.getTransProps(
					).getProperty(
						transactionCode
					);

				if (StringUtils.isNotBlank(processingCodeDescription)) {

					// Check if the return code is 76. If so then check for the
					// Merchant Code and add PCT if the Merchant code is not
					// 6011 otherwise leave is as it is.

					if (Constants.PIN_SELECT_CODE.equals(transactionCode) &&
						StringUtils.isNotBlank(posMerchantCode)) {

						processingCodeDescription =
							posMerchantCode.equals(Constants.MERCHANT_ID) ?
								processingCodeDescription :
									Constants.PIN_CHANGE_SELECT_CODE_PRE + " " +
										processingCodeDescription;
					}

					String transactionDescpSecPart =
						createTransactionDescriptionString(processingCode);

					if (StringUtils.isNotBlank(transactionDescpSecPart)) {
						processingCodeDescription =
							processingCodeDescription + " - " +
								transactionDescpSecPart;
						validProcessingCode = true;

						logger.debug(
							"The processing code is 6 digits FINAL Discription is   " +
								processingCodeDescription);
					}
				}
			}

			if (!validProcessingCode) {

				// invalid processing code so we can't calculate a description.

				logger.error(
					"Invalid processing code found=" + processingCode +
						", trl_id=" + trlId + ", trl_business_date=" +
							trlBusinessDate);
			}
		}

		logger.debug("findTransactionCodeDescriptionFromProcessingCode - end");

		return processingCodeDescription;
	}

	/**
	 * Get all message code group for UI
	 *
	 * @return String[]
	 */
	public String[] getAllMessageCodeTypeForUIDropDown() {
		String messageCodeGrps = transactionAppProperties.getTransProps(
		).getProperty(
			Constants.MESSAGE_CODE_UI_VALUES_PROPSKEY
		);

		if (null != messageCodeGrps) {
			return messageCodeGrps.split(Constants.COMMA);
		}

		return null;
	}

	/**
	 *
	 * @param lookUpType
	 * @param searchHeader
	 * @return
	 */
	public List<AttributesListType> getAttributeList(
		long lookUpType, SearchHeader searchHeader) {

		logger.debug("getAttributeList - start");

		GetAttributesListRequest request = new GetAttributesListRequest();

		if (null != searchHeader) {
			TktRequestHeader header = Utility.convertSearchHeaderToTktHeader(
				searchHeader);

			request.setHeader(header);
			request.setTypeId(lookUpType);

			GetAttributesListResponse response =
				transactionService.getAttributeList(request);

			if (null != response) {
				List<AttributesListType> attributeList =
					response.getAttributesLists();

				return attributeList;
			}

			logger.error("getAttributeList - no results returned");
		}
		else {
			logger.error("getAttributeList - No Search Header found");
		}

		logger.debug("getAttributeList - end");

		return null;
	}

	public List<Customer> getCustomerAccessViewList(Long liferayOrgId) {
		logger.debug("getCustomerAccessViewList - start");

		GetCustomerAccessViewRequestType params =
			new GetCustomerAccessViewRequestType();

		params.setCustomerId(String.valueOf(liferayOrgId));
		params.setCustomerIdType("portalOrgId");
		params.setIgnoreCase(true);

		logger.debug(
			"getCustomerAccessView - portalOrgId in the Service Impl is: " +
				liferayOrgId);

		perfLogger_.debug(
			"Calling commonService.getCustomerAccessView() - start");
		GetCustomerAccessViewResponseType response =
			commonService.getCustomerAccessView(params);

		perfLogger_.debug(
			"Calling commonService.getCustomerAccessView() - finished");

		List<Customer> customers = response.getCustomers();

		logger.debug("getCustomerAccessViewList - end");

		return customers;
	}

	/**
	 * Get Message code from common services. Removes duplicates because
	 * REF_MESSAGE_CODES has duplicate AS2805 codes to handle the M:1 AUX ->
	 * AS2805 mapping.
	 * @param PortletContext
	 * @return Map<String, List<Codes>>
	 * @throws Exception
	 */
	@Cacheable("messageCode")
	public Map<String, Set<Codes>> getMessageCodeDescription(
			PortletContext portletContext)
		throws Exception {

		logger.debug("getMessageCodeDescription - start");

		if (cacheMessageCodesMap == null) {
			cacheMessageCodesMap = new LinkedHashMap<>();

			Set<Codes> codes = null;
			Codes msgCode = null;
			String[] allowedMsgCode = transactionAppProperties.getTransProps(
			).getProperty(
				Constants.ALLOWED_MESSAGE_CODE
			).split(
				Constants.COMMA
			);

			GetMessageCodesRequestType getMessageCodesRequest =
				new GetMessageCodesRequestType();
			perfLogger_.debug(
				"Calling the   Common web service call for getting message codes  at " +
					Calendar.getInstance(
					).getTime());
			GetMessageCodesResponseType codesResponseType =
				transactionService.getMessageCodes(getMessageCodesRequest);
			perfLogger_.debug(
				" Finished Calling the   Common web service call for getting message codes  at " +
					Calendar.getInstance(
					).getTime());

			if ((codesResponseType != null) &&
				(codesResponseType.getMessageCodeGroups(
				).size() > 0)) {

				List<MessageCodeGroup> codeGroups =
					codesResponseType.getMessageCodeGroups();

				for (MessageCodeGroup messageCodeGroup : codeGroups) {
					if (isMessageCodeAllowed(
							allowedMsgCode, messageCodeGroup.getCode())) {

						logger.debug(
							"Allowed Message code groups are  " +
								messageCodeGroup.getCode());

						if (!cacheMessageCodesMap.containsKey(
								messageCodeGroup.getDescription())) {

							codes = new TreeSet<>();
						}
						else {
							codes = cacheMessageCodesMap.get(
								messageCodeGroup.getDescription());
						}

						List<MessageCode> messageCodes =
							messageCodeGroup.getMessageCodes();

						for (MessageCode messageCode : messageCodes) {
							msgCode = new Codes();
							msgCode.setCode(messageCode.getAs2805MessageCode());
							msgCode.setDescription(
								messageCode.getDescription());
							codes.add(msgCode);
						}

						cacheMessageCodesMap.put(
							messageCodeGroup.getDescription(), codes);
					}
				}

				audit.success(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.COMMON_SERVICE_MESSAGE,
					"	The Message code List size is  " +
						(codes != null ? codes.size() :
							" 0 as null code object returned"));
			}
			else {
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.COMMON_SERVICE_MESSAGE,
					"	The status code is not success or response is null: ");
			}
		}
		else {
			logger.debug(
				"getMessageCodeDescription - using cache (" +
					cacheMessageCodesMap.size() + " objects in cache)");
		}

		logger.debug("getMessageCodeDescription - end");

		return cacheMessageCodesMap;
	}

	/**
	 * Get get org short name using liferay org id .
	 *
	 * @param String
	 * @return String
	 */
	public String getOrgShortName(String liferayOrgId, PortletRequest request) {
		String orgShortName = null;
		FindEntitiesByAttributeWithIdRequestType
			findEntitiesByAttributeWithIdRequest =
				new FindEntitiesByAttributeWithIdRequestType();
		findEntitiesByAttributeWithIdRequest.setAttributeValue(liferayOrgId);
		findEntitiesByAttributeWithIdRequest.setAttributeTypeId(4);

		FindEntitiesByAttributeWithIdResponseType
			findEntitiesByAttributeWithIdResponseType = findEntitiesByAttribute(
				liferayOrgId);

		if (!findEntitiesByAttributeWithIdResponseType.getEntities(
			).isEmpty()) {

			orgShortName =
				findEntitiesByAttributeWithIdResponseType.getEntities(
				).get(
					0
				).getShortName();

			buildIssuerIdList(
				findEntitiesByAttributeWithIdResponseType, request);
		}

		logger.debug("The organisation short name is " + orgShortName);

		return orgShortName;
	}

	/**
	 * Get the OrignalAcquirer AtmOrPos Value
	 *
	 * @param GetTransactionResponseType
	 * @return String
	 */
	public String getOrignalAcquirerAtmOrPosValue(
		GetTransactionResponseType responseType) {

		String atmOrPos = "";

		if ((null != responseType.getMessageCodes()) &&
			(0 < responseType.getMessageCodes(
			).size())) {

			String[] allowedMsgCode = transactionAppProperties.getTransProps(
			).getProperty(
				Constants.ALLOWED_MESSAGE_CODE
			).split(
				Constants.COMMA
			);
			String messageCode = responseType.getMessageCodes(
			).get(
				0
			).getGroupCode();

			if (isMessageCodeAllowed(allowedMsgCode, messageCode)) {
				if (null != responseType.getPosMerchant()) {
					String merchantCode = responseType.getPosMerchant(
					).getCode();

					if (StringUtils.isNotBlank(merchantCode) &&
						Constants.MERCHANT_ID.equalsIgnoreCase(
							merchantCode.trim())) {

						atmOrPos = Constants.OPTION_ATM;
					}
					else {
						atmOrPos = Constants.OPTION_POS;
					}
				}
			}
			else {
				atmOrPos = "";
			}
		}

		return atmOrPos;
	}

	public PctUserSearchServiceImpl getPctSearchService() {
		return pctSearchService;
	}

	/**
	 * Get Pin Change details by id , business date and source
	 *
	 * @param TransactionDetail
	 * @param SearchHeader
	 * @param PortletContext
	 * @return TransactionDetail
	 * @throws Exception
	 */
	public TransactionDetail getPinChangeDetailByIdBusDateAndSrc(
			TransactionDetail pinChangeDetail, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		logger.debug("getPinChangeDetailByIdBusDateAndSrc - start");
		GetTransactionRequestType getTransactionRequest =
			new GetTransactionRequestType();

		getTransactionRequest.setTransactionId(
			pinChangeDetail.getTransactionId());
		getTransactionRequest.setBusinessDate(pinChangeDetail.getBusDate());
		getTransactionRequest.setSource(
			DataSourceEnum.fromValue(pinChangeDetail.getDataSrc()));

		getTransactionRequest.setUserSteppedUp(pinChangeDetail.isUserStepUp());

		getTransactionRequest.setHeader(searchHeader);
		perfLogger_.debug(
			"Calling the   Transaction Details web service call at " +
				Calendar.getInstance(
				).getTime());
		GetTransactionResponseType responseType =
			transactionService.getTransaction(getTransactionRequest);
		perfLogger_.debug(
			"Finished Calling the   Transaction Details web service call at " +
				Calendar.getInstance(
				).getTime());
		setHsmErrorForTransactionDetailsPage(responseType, pinChangeDetail);

		if (null != responseType.getHeader()) {
			String statusCode = responseType.getHeader(
			).getStatusCode();

			if (statusCode.equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
				pinChangeDetail = populatePinChangeFromResponse(
					responseType, pinChangeDetail, searchHeader);

				audit.success(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_DETAIL,
					createSearchParamsForTransactionDetails(
						pinChangeDetail, 1, statusCode));
			}
			else {
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_DETAIL,
					createSearchParamsForTransactionDetails(
						pinChangeDetail, 0, statusCode));
			}
		}

		logger.debug("getPinChangeDetailByIdBusDateAndSrc - end");

		return pinChangeDetail;
	}

	/**
	 * Get Transaction list by search params
	 *
	 * @param TransactionForm
	 * @param SearchHeader
	 * @param PortletContext
	 * @return List<TransactionList>
	 * @throws Exception
	 */
	public List<Object> getPinChangeListOnSearch(
			TransactionForm transactionForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		PermGenUtil.permGenDump("getPinChangeListOnSearch - start");

		List<TransactionList> transactionLists = null;
		List<Object> listReturn = new ArrayList<>();
		int panBinCount = transactionForm.getPanBin(
		).length();

		if (panBinCount > 10) {
			transactionForm.setPan(true);
		}
		else {
			transactionForm.setPan(false);
		}

		FindTransactionsRequestType findTransactionsRequest =
			populateSearchForPinChangeHistory(transactionForm, portletContext);

		findTransactionsRequest.setHeader(searchHeader);
		perfLogger_.debug(
			"Calling the  Find Transaction web service call at " +
				Calendar.getInstance(
				).getTime());

		PermGenUtil.permGenDump(
			"getPinChangeListOnSearch - about to call findTransactions web service ");

		FindTransactionsResponseType responseType =
			transactionService.findTransactions(findTransactionsRequest);

		PermGenUtil.permGenDump(
			"getPinChangeListOnSearch - findTransactions webservice completed");

		perfLogger_.debug(
			"Finished calling the Find Transaction web service call at " +
				Calendar.getInstance(
				).getTime());

		logger.debug(
			"getPinChangeListOnSearch - responseType is: " + responseType);
		logger.debug(
			"getPinChangeListOnSearch - responseType Header is: " +
				responseType.getHeader());

		if ((responseType != null) && (null != responseType.getHeader())) {
			String statusCode = responseType.getHeader(
			).getStatusCode();

			if (responseType.getTransactions(
				).size() > 0) {

				List<TransactionBasicInformation> listForms =
					responseType.getTransactions();
				logger.debug(
					"getPinChangeListOnSearch - The list size is " +
						responseType.getTransactions(
						).size());
				audit.success(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_SEARCH,
					createSearchParamsStr(
						transactionForm,
						responseType.getTransactions(
						).size(),
						statusCode));

				transactionLists = populatePinChangeResultsFromWS(listForms);

				listReturn.add(transactionLists);

				if (responseType.isMoreAvailable()) {
					transactionLists.get(
						0
					).setMoreRecAvail(
						responseType.isMoreAvailable()
					);
				}
			}
			else {
				logger.debug(
					"getPinChangeListOnSearch - response header is null. transactionList is: " +
						transactionLists);
				listReturn.add(transactionLists);
				logger.debug(
					"getPinChangeListOnSearch - listReturn is: " +
						listReturn.size());
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_SEARCH,
					createSearchParamsStr(transactionForm, 0, statusCode));
			}

			String temp = setWebServiceErrorForTransactionSearchPage(
				statusCode, portletContext);

			logger.debug("getPinChangeListOnSearch - WS message is: " + temp);
			listReturn.add(temp);

			logger.debug(
				"getPinChangeListOnSearch - listReturn size is: " +
					listReturn.size());
		}
		else {
			logger.debug(
				"getPinChangeListOnSearch - Something went wrong with the webservice.");
			logger.debug(
				"getPinChangeListOnSearch - responseType is: " + responseType);
			logger.debug(
				"getPinChangeListOnSearch - responseType Header is: " +
					responseType.getHeader());

			listReturn = null;
		}

		PermGenUtil.permGenDump("getPinChangeListOnSearch - end");

		logger.debug(
			"getPinChangeListOnSearch - the listReturn is: " + listReturn);

		return listReturn;
	}

	/**
	 * Get Responses code from common services
	 *
	 * @param PortletContext
	 * @return Map&lt;String, List&lt;Codes&gt;&gt;
	 * @throws Exception
	 */
	@Cacheable("responseCode")
	public List<Codes> getResponseCodeDescription(PortletContext portletContext)
		throws Exception {

		List<Codes> codes = new ArrayList<>();
		Codes respCode = null;
		GetAs2805ResponseCodesRequestType getAs2805ResponseCodesRequest =
			new GetAs2805ResponseCodesRequestType();
		perfLogger_.debug(
			"Calling the   Common web service call for getting Response codes  at " +
				Calendar.getInstance(
				).getTime());
		GetAs2805ResponseCodesResponseType codesResponseType =
			transactionService.getAs2805ResponseCodes(
				getAs2805ResponseCodesRequest);
		perfLogger_.debug(
			"Finished Calling the   Common web service call for getting Response codes  at " +
				Calendar.getInstance(
				).getTime());

		if ((codesResponseType != null) &&
			(codesResponseType.getAs2805ResponseCode(
			).size() > 0)) {

			List<As2805ResponseCode> codeGroups =
				codesResponseType.getAs2805ResponseCode();

			for (As2805ResponseCode as2805ResponseCode : codeGroups) {
				respCode = new Codes();
				respCode.setCode(as2805ResponseCode.getCode());
				respCode.setDescription(as2805ResponseCode.getDescription());
				codes.add(respCode);
			}

			audit.success(
				portletContext.getResponse(), portletContext.getRequest(),
				AuditOrigin.PORTAL_ORIGIN,
				AuditCategories.COMMON_SERVICE_RESPONSE,
				"	The Response code List size is  " +
					(codes != null ? codes.size() :
						" 0 as null code object returned"));
		}
		else {
			audit.fail(
				portletContext.getResponse(), portletContext.getRequest(),
				AuditOrigin.PORTAL_ORIGIN,
				AuditCategories.COMMON_SERVICE_RESPONSE,
				"	The status code is not success or response codes object is  null: ");
		}

		return codes;
	}

	/**
	 * Get Transaction details by id and business date
	 *
	 * @param TransactionDetail
	 * @param SearchHeader
	 * @param PortletContext
	 * @return TransactionDetail
	 * @throws Exception
	 */
	public TransactionDetail getTransactionDetailByTxIdBusDateAndSrc(
			TransactionDetail transactionDetail, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		logger.debug("getTransactionDetailByTxIdBusDateAndSrc - start");
		GetTransactionRequestType getTransactionRequest =
			new GetTransactionRequestType();

		getTransactionRequest.setTransactionId(
			transactionDetail.getTransactionId());
		getTransactionRequest.setBusinessDate(transactionDetail.getBusDate());

		getTransactionRequest.setSource(
			DataSourceEnum.fromValue(transactionDetail.getDataSrc()));
		getTransactionRequest.setUserSteppedUp(
			transactionDetail.isUserStepUp());
		getTransactionRequest.setHeader(searchHeader);

		perfLogger_.debug(
			"Calling the   Transaction Details web service call at " +
				Calendar.getInstance(
				).getTime());
		GetTransactionResponseType responseType =
			transactionService.getTransaction(getTransactionRequest);
		perfLogger_.debug(
			"Finished Calling the   Transaction Details web service call at " +
				Calendar.getInstance(
				).getTime());
		setHsmErrorForTransactionDetailsPage(responseType, transactionDetail);

		if (null != responseType.getHeader()) {
			String statusCode = responseType.getHeader(
			).getStatusCode();

			if (statusCode.equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
				transactionDetail = populateTransactionDetailFromResponseType(
					responseType, transactionDetail);

				// In here check if the session contains the Issuer list and
				// then check if the transactions Issuer is in the Issuer list
				// then get the extra details. Otherwise put in a generic
				// message.

				if (isUserAnIssuerForTransaction(
						transactionDetail, portletContext.getRequest())) {

					transactionDetail.setAccessibleIssuer(true);
				}
				else {
					transactionDetail.setAccessibleIssuer(false);
				}

				transactionDetail = addExtraTransactionDetails(
					transactionDetail, searchHeader);

				addContactInformation(transactionDetail, portletContext);

				retrieveListToDisplay(
					transactionDetail, searchHeader,
					portletContext.getRequest());

				audit.success(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_DETAIL,
					createSearchParamsForTransactionDetails(
						transactionDetail, 1, statusCode));
			}
			else {
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_DETAIL,
					createSearchParamsForTransactionDetails(
						transactionDetail, 0, statusCode));
			}
		}

		logger.debug("getTransactionDetailByTxIdBusDateAndSrc - end");

		return transactionDetail;
	}

	/**
	 * Get Transaction list by search params
	 *
	 * @param TransactionForm
	 * @param SearchHeader
	 * @param PortletContext
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> getTransactionListOnSearch(
			TransactionForm transactionForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		PermGenUtil.permGenDump("getTransactionListOnSearch - start");

		List<TransactionList> transactionLists = null;
		List<Object> listReturn = new ArrayList<>();
		/*int panBinCount = transactionForm.getPanBin().length();

		if (panBinCount > 10) {
			transactionForm.setPan(true);
		} else {
			transactionForm.setPan(false);
		}*/

		FindTransactionsRequestType findTransactionsRequest =
			populateSerchForTransaction(transactionForm, portletContext);

		findTransactionsRequest.setHeader(searchHeader);
		perfLogger_.debug(
			"Calling the  Find Transaction web service call at " +
				Calendar.getInstance(
				).getTime());

		PermGenUtil.permGenDump(
			"getTransactionListOnSearch - about to call findTransactions web service ");

		FindTransactionsResponseType responseType =
			transactionService.findTransactions(findTransactionsRequest);

		if (responseType != null) {
			logger.debug(
				"getTransactionListOnSearch - response transaction list size is: " +
					responseType.getTransactions(
					).size());
		}

		PermGenUtil.permGenDump(
			"getTransactionListOnSearch - findTransactions webservice completed");

		perfLogger_.debug(
			"Finished calling the Find Transaction web service call at " +
				Calendar.getInstance(
				).getTime());

		if (null != responseType.getHeader()) {
			String statusCode = responseType.getHeader(
			).getStatusCode();

			if ((responseType != null) &&
				(responseType.getTransactions(
				).size() > 0)) {

				List<TransactionBasicInformation> listForms =
					responseType.getTransactions();
				logger.debug(
					"The list size is getTransactionListOnSerch before calling populate " +
						responseType.getTransactions(
						).size());
				audit.success(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_SEARCH,
					createSearchParamsStr(
						transactionForm,
						responseType.getTransactions(
						).size(),
						statusCode));

				HttpServletRequest httpServletRequest =
					PortalUtil.getHttpServletRequest(
						portletContext.getRequest());

				HttpSession session = httpServletRequest.getSession(true);

				Boolean dateType = (Boolean)session.getAttribute(
					Constants.SWITCH_DATE);

				transactionLists = populateTransactionsearchResultFromWS(
					listForms, dateType);
				listReturn.add(transactionLists);

				if (responseType.isMoreAvailable()) {
					transactionLists.get(
						0
					).setMoreRecAvail(
						responseType.isMoreAvailable()
					);
				}
			}
			else {
				listReturn.add(transactionLists);
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN,
					AuditCategories.TRANSACTION_SEARCH,
					createSearchParamsStr(transactionForm, 0, statusCode));
			}

			listReturn.add(
				setWebServiceErrorForTransactionSearchPage(
					statusCode, portletContext));
		}

		PermGenUtil.permGenDump("getTransactionListOnSearch - end");

		return listReturn;
	}

	/**
	 * @return the transactionService
	 */
	public TransactionService getTransactionService() {
		return transactionService;
	}

	/**
	 * Check whether current message code is in allowed list or not .
	 *
	 * @param String[]
	 * @param String
	 * @return boolean
	 */
	public boolean isMessageCodeAllowed(
		String[] alloedMsgCodeLst, String messageCode) {

		boolean isAllowedMsgCode = false;

		for (String msgCode : alloedMsgCodeLst) {
			if (msgCode.equalsIgnoreCase(messageCode)) {
				isAllowedMsgCode = true;
			}
		}

		return isAllowedMsgCode;
	}

	public void logPoolStatistics() {
		transactionService.logPoolStatistics();
	}

	/**
	 * Get all message code based on the user selection of message group for UI
	 *
	 * @param FindTransactionsRequestType
	 * @param String
	 * @param portletContext
	 * @return FindTransactionsRequestType
	 */
	@Cacheable("messageCodeCriteria")
	public FindTransactionsRequestType populateAllMsgCodeForSelectedOption(
			FindTransactionsRequestType findTransactionsRequest,
			String msgCodeOption, PortletContext portletContext)
		throws Exception {

		logger.debug("populateAllMsgCodeForSelectedOption - start");

		Map<String, Set<Codes>> messageMap = getMessageCodeDescription(
			portletContext);
		perfLogger_.debug(
			"Calling the properties files to create the map of all message code  at " +
				Calendar.getInstance(
				).getTime());
		HashMap<String, String> msgCodePropsMap =
			transactionService.getMessageCodeProps();
		perfLogger_.debug(
			"Finished Calling the properties files to create the map of all message code  at " +
				Calendar.getInstance(
				).getTime());
		Set<Codes> codes = new TreeSet<>();

		if ((null != messageMap) && (messageMap.size() > 0)) {
			if (StringUtils.isBlank(msgCodeOption)) {

				// note that messageMap *ONLY* contains the list of message
				// codes that we are allowed to search upon.

				logger.debug(
					" Here is the list of codes for Empty option selected  ");
				Set<String> grpCodesSet = messageMap.keySet();

				for (Iterator<String> iterator = grpCodesSet.iterator();
					 iterator.hasNext();) {

					String codeGrpDesc = iterator.next();

					codes.addAll(messageMap.get(codeGrpDesc));
				}
			}
			else {
				if (msgCodePropsMap.containsKey(msgCodeOption)) {
					logger.debug(
						" The message codes option selected is  " +
							msgCodeOption + "  and the description is " +
								msgCodePropsMap.get(msgCodeOption));
					codes = messageMap.get(msgCodePropsMap.get(msgCodeOption));
				}
			}

			if (codes.size() > 0) {
				logger.debug(
					" Here is the size of codes for the option selected  " +
						codes.size());
				List<String> msgCode = new ArrayList<>();

				for (Codes codes2 : codes) {
					/*logger.debug(" Here is the codes are in the option selected  "
							+ codes2.getCode());*/
					msgCode.add(codes2.getCode());
				}

				logger.debug(
					" Here is the msgCode which is send to web service layer is  " +
						msgCode.size());
				findTransactionsRequest.getMessageCodes(
				).addAll(
					msgCode
				);
			}
		}

		logger.debug("populateAllMsgCodeForSelectedOption - end");

		return findTransactionsRequest;
	}

	/**
	 * populate the FindTransactionsRequestType from pinChangeSearch
	 *
	 * @param TransactionForm
	 * @param PortletContext
	 * @return FindTransactionsRequestType
	 */
	public FindTransactionsRequestType populateSearchForPinChangeHistory(
			TransactionForm transactionForm, PortletContext portletContext)
		throws Exception {

		logger.debug("populateSearchForPinChangeHistory - start");
		FindTransactionsRequestType findTransactionsRequest =
			new FindTransactionsRequestType();

		findTransactionsRequest.setMaxResults(250);
		findTransactionsRequest.setPanOrBin(
			transactionForm.getPanBin(
			).trim());

		if (transactionForm.isPan()) {
			findTransactionsRequest.setIsPanOrBin(PanOrBinEnum.PAN);
		}
		else {
			findTransactionsRequest.setIsPanOrBin(PanOrBinEnum.BIN);
		}

		logger.debug(
			"populateSearchForPinChangeHistory - 24 in end Hr is " +
				transactionForm.getEndTimeHr());

		if ("24".equals(transactionForm.getEndTimeHr())) {
			logger.debug(
				"populateSearchForPinChangeHistory - 24 in end Hr is " +
					transactionForm.getEndTimeHr());

			if (!StringUtils.isBlank(transactionForm.getEndDate())) {
				logger.debug(
					"populateSearchForPinChangeHistory - END DATE IS transactionForm.getEndDate() is 24 more " +
						transactionForm.getEndDate());
				String newEndDate = Utility.getNextDayForThisDate(
					transactionForm.getEndDate());

				logger.debug(
					"populateSearchForPinChangeHistory - END DATE IS transactionForm.getEndDate() is 24 more " +
						newEndDate);
				findTransactionsRequest.setEndDate(
					createDateAsGregorianCalendarType(newEndDate, "00", "00"));
				logger.debug(
					"populateSearchForPinChangeHistory - FINAL END DATE  time IS     is 24 more  " +
						findTransactionsRequest.getEndDate());
			}
		}
		else {
			logger.debug(
				"populateSearchForPinChangeHistory - END DATE IS transactionForm.getEndDate() in");
			findTransactionsRequest.setEndDate(
				createDateAsGregorianCalendarType(
					transactionForm.getEndDate(),
					transactionForm.getEndTimeHr(),
					transactionForm.getEndTimeMin()));
		}

		logger.debug(
			"populateSearchForPinChangeHistory - The End date is: " +
				findTransactionsRequest.getEndDate());

		findTransactionsRequest.setStartDate(
			createDateAsGregorianCalendarType(
				transactionForm.getStartDate(),
				transactionForm.getStartTimeHr(),
				transactionForm.getStartTimeMin()));

		logger.debug(
			"populateSearchForPinChangeHistory - The start date is: " +
				findTransactionsRequest.getStartDate());

		if (!StringUtils.isBlank(transactionForm.getTerminalId())) {
			findTransactionsRequest.getCardAcceptorTerminalIds(
			).add(
				transactionForm.getTerminalId(
				).trim()
			);

			logger.debug(
				"populateSerchForTransaction - terminalId is: [" +
					findTransactionsRequest.getCardAcceptorTerminalIds(
					).get(
						0
					) + "]");
		}

		findTransactionsRequest.getTransactionCodes(
		).addAll(
			getPinChangeTransactionCodes()
		);
		logger.debug("populateSearchForPinChangeHistory - end");

		return findTransactionsRequest;
	}

	/**
	 * populate the FindTransactionsRequestType from transactionForm
	 *
	 * @param TransactionForm
	 * @param PortletContext
	 * @return FindTransactionsRequestType
	 * @throws Exception
	 */
	public FindTransactionsRequestType populateSerchForTransaction(
			TransactionForm transactionForm, PortletContext portletContext)
		throws Exception {

		logger.debug("populateSerchForTransaction - start");
		FindTransactionsRequestType findTransactionsRequest =
			new FindTransactionsRequestType();

		findTransactionsRequest.setMaxResults(250);

		if (transactionForm.isPan()) {
			findTransactionsRequest.setIsPanOrBin(PanOrBinEnum.PAN);
			findTransactionsRequest.setPanOrBin(
				StringUtil.trim(transactionForm.getPanBin()));
		}
		else if (transactionForm.isMaskedPan()) {
			findTransactionsRequest.setIsPanOrBin(PanOrBinEnum.MASKED_PAN);
			findTransactionsRequest.setPanOrBin(
				StringUtil.trim(transactionForm.getNormalisedPan()));
		}
		else {
			findTransactionsRequest.setIsPanOrBin(PanOrBinEnum.BIN);
			findTransactionsRequest.setPanOrBin(
				StringUtil.trim(transactionForm.getPanBin()));
		}

		logger.debug(
			"[TransactionSearchServiceImpl.getTransactionListOnSerch] 24 in end Hr is " +
				transactionForm.getEndTimeHr());

		if ("24".equals(transactionForm.getEndTimeHr())) {
			logger.debug(
				"[TransactionSearchServiceImpl.getTransactionListOnSerch] 24 in end Hr is " +
					transactionForm.getEndTimeHr());

			if (!StringUtils.isBlank(transactionForm.getEndDate())) {
				logger.debug(
					"END DATE IS  transactionForm.getEndDate() is 24 more " +
						transactionForm.getEndDate());
				String newEndDate = Utility.getNextDayForThisDate(
					transactionForm.getEndDate());

				logger.debug(
					"END DATE IS   transactionForm.getEndDate() is 24 more " +
						newEndDate);
				findTransactionsRequest.setEndDate(
					createDateAsGregorianCalendarType(newEndDate, "00", "00"));
				logger.debug(
					"FINAL END DATE  time IS     is 24 more  " +
						findTransactionsRequest.getEndDate());
			}
		}
		else {
			logger.debug("END DATE IS transactionForm.getEndDate() in");
			findTransactionsRequest.setEndDate(
				createDateAsGregorianCalendarType(
					transactionForm.getEndDate(),
					transactionForm.getEndTimeHr(),
					transactionForm.getEndTimeMin()));
		}

		logger.debug(
			"The End date is: " + findTransactionsRequest.getEndDate());

		findTransactionsRequest.setStartDate(
			createDateAsGregorianCalendarType(
				transactionForm.getStartDate(),
				transactionForm.getStartTimeHr(),
				transactionForm.getStartTimeMin()));

		if (transactionForm.getDateType(
			).equals(
				"system"
			)) {

			findTransactionsRequest.setDateType(DateTypeEnum.SYSTEM);
		}
		else {
			findTransactionsRequest.setDateType(DateTypeEnum.LOCAL);
		}

		logger.debug(
			"The start date is: " + findTransactionsRequest.getStartDate());

		if ((transactionForm.getAmount() != null) &&
			!transactionForm.getAmount(
			).equals(
				""
			)) {

			logger.debug(
				" Amount Conditions  is  " + transactionForm.getConditions());

			if (transactionForm.getConditions(
				).equals(
					"eq"
				)) {

				findTransactionsRequest.setAmountOperator(
					AmountOperatorEnum.EQ);
			}
			else if (transactionForm.getConditions(
					).equals(
						"ge"
					)) {

				findTransactionsRequest.setAmountOperator(
					AmountOperatorEnum.GE);
			}
			else if (transactionForm.getConditions(
					).equals(
						"gt"
					)) {

				findTransactionsRequest.setAmountOperator(
					AmountOperatorEnum.GT);
			}
			else if (transactionForm.getConditions(
					).equals(
						"le"
					)) {

				findTransactionsRequest.setAmountOperator(
					AmountOperatorEnum.LE);
			}
			else if (transactionForm.getConditions(
					).equals(
						"lt"
					)) {

				findTransactionsRequest.setAmountOperator(
					AmountOperatorEnum.LT);
			}

			findTransactionsRequest.setAmount(
				Float.valueOf(
					Utility.removeAndCovertInFloat(transactionForm.getAmount())
				).floatValue());

			if (transactionForm.getAmountType(
				).equals(
					"billingAmount"
				)) {

				findTransactionsRequest.setAmountType(
					AmountTypeEnum.BILLING_AMOUNT);
			}
			else {
				findTransactionsRequest.setAmountType(
					AmountTypeEnum.TRANSACTION_AMOUNT);
			}
		}

		if (!StringUtils.isBlank(transactionForm.getTerminalId())) {
			findTransactionsRequest.getCardAcceptorTerminalIds(
			).add(
				transactionForm.getTerminalId(
				).trim()
			);

			logger.debug(
				"populateSerchForTransaction - terminalId is: [" +
					findTransactionsRequest.getCardAcceptorTerminalIds(
					).get(
						0
					) + "]");
		}

		if (!StringUtils.isBlank(transactionForm.getMerchantId())) {
			findTransactionsRequest.getCardAcceptorIdCodes(
			).add(
				transactionForm.getMerchantId(
				).trim()
			);
		}

		if (!StringUtils.isBlank(transactionForm.getResponseCode())) {
			findTransactionsRequest.getResponseCodes(
			).add(
				transactionForm.getResponseCode(
				).trim()
			);
		}

		if (!StringUtils.isBlank(transactionForm.getExternalTransactionId())) {
			findTransactionsRequest.setExternalTransactionId(
				transactionForm.getExternalTransactionId(
				).trim());
		}

		if (!StringUtils.isBlank(transactionForm.getStan())) {
			findTransactionsRequest.setStan(
				transactionForm.getStan(
				).trim());
		}

		if (!StringUtils.isBlank(transactionForm.getRrn())) {
			findTransactionsRequest.setRrn(
				transactionForm.getRrn(
				).trim());
		}

		if (!StringUtils.isBlank(transactionForm.getCuscalId())) {
			findTransactionsRequest.setTransactionId(
				transactionForm.getCuscalId(
				).trim());
		}

		findTransactionsRequest = populateAllMsgCodeForSelectedOption(
			findTransactionsRequest, transactionForm.getMessageCode(),
			portletContext);
		logger.debug("populateSerchForTransaction - end");

		return findTransactionsRequest;
	}

	/**
	 * Populate the TransactionDetail from response object
	 *
	 * @param GetTransactionResponseType
	 * @param TransactionDetail
	 * @return TransactionDetail
	 */
	public TransactionDetail populateTransactionDetailFromResponseType(
		GetTransactionResponseType responseType,
		TransactionDetail transactionDetail) {

		logger.debug("populateTransactionDetailFromResponseType - start");
		RequestDetails requestDetails = new RequestDetails();
		ResponseDetails responseDetails = new ResponseDetails();

		if (responseType.getAcquirer() != null) {
			requestDetails.setAcquirerId(
				responseType.getAcquirer(
				).getId());
			requestDetails.setAcquirerName(
				responseType.getAcquirer(
				).getName());
		}

		if ((responseType.getStan() != null) ||
			(responseType.getAdvStan() != null))
			requestDetails.setAcquirerSystemTrace(
				Utility.createStanDisplayString(
					responseType.getStan(), responseType.getAdvStan()));

		if (responseType.getCardAcceptor() != null)
			requestDetails.setCardAcceptorId(
				responseType.getCardAcceptor(
				).getId());

		if (responseType.getCardAcceptor() != null)
			requestDetails.setCardAcceptorName(
				responseType.getCardAcceptor(
				).getName());

		if (responseType.getRetrievalReferenceNumber() != null)
			requestDetails.setCardAcceptorRetrievalReferenceNumber(
				responseType.getRetrievalReferenceNumber());

		requestDetails.setTransactionAmt(
			Utility.AmountFormat.format(responseType.getTransactionAmount()));

		requestDetails.setCardholderAmt(
			Utility.AmountFormat.format(responseType.getCardholderAmount()));

		requestDetails.setCashAmt(
			Utility.AmountFormat.format(responseType.getCashAmount()));

		if (responseType.getExpiryDate() != null) {
			String expiryDate = Utility.createExpiryDateFormat(
				responseType.getExpiryDate());

			if (StringUtils.isNotBlank(expiryDate)) {
				requestDetails.setExpiryDate(expiryDate);
			}
			else {
				requestDetails.setExpiryDate(responseType.getExpiryDate());
			}
		}

		if (responseType.getFeeAmount() != 0) {
			float dollarVal = Utility.covertCentsToDollarValue(
				responseType.getFeeAmount());
			requestDetails.setFeeAmt(Utility.AmountFormat.format(dollarVal));
		}
		else {
			requestDetails.setFeeAmt(
				Utility.AmountFormat.format(responseType.getFeeAmount()));
		}

		if (responseType.getIssuer() != null) {
			requestDetails.setIssuerId(
				responseType.getIssuer(
				).getId());
			requestDetails.setIssuerOrgName(
				responseType.getIssuer(
				).getOrgName());
			requestDetails.setIssuerName(
				responseType.getIssuer(
				).getName());
		}

		if (responseType.getRetrievalReferenceNumber() != null)
			requestDetails.setIssuerRetrievalReferenceNumber(
				responseType.getRetrievalReferenceNumber());

		String atmOrPos = getOrignalAcquirerAtmOrPosValue(responseType);

		requestDetails.setOrignalAcquirerAtmOrPos(atmOrPos);

		if (responseType.getOriginalAcquirer() != null)
			requestDetails.setOrignalAcquirerId(
				responseType.getOriginalAcquirer(
				).getId());

		if (responseType.getOriginalAcquirer() != null)
			requestDetails.setOrignalAcquirerName(
				responseType.getOriginalAcquirer(
				).getName());

		if (responseType.getOriginalAcquirerTerminalId() != null)
			requestDetails.setOrignalAcquirerTerminalId(
				responseType.getOriginalAcquirerTerminalId());

		if (responseType.getPan() != null) {
			requestDetails.setPan(responseType.getPan());
			requestDetails.setMaskedPan(
				Utility.mask(responseType.getPan(), 'X'));
		}

		/*if (responseType.getPinPresent() != null) {
			if (responseType.getPinPresent().toString()
					.equalsIgnoreCase(Constants.PIN_PRESENT_YES)) {

				requestDetails
						.setPinPresent(transactionAppProperties.getTransProps()
								.getProperty(Constants.PIN_PRESENT_YES));
			} else {
				requestDetails.setPinPresent(transactionAppProperties
						.getTransProps().getProperty(
								Constants.PIN_PRESENT_NOORUNKNOWN));
			}
		}*/
		if (responseType.getPosCondition() != null)
			requestDetails.setPosConditionCode(
				responseType.getPosCondition(
				).getCode());

		if (responseType.getSettlementDate() != null)
			requestDetails.setSettlementDateTime(
				Utility.formatDateToString(
					responseType.getSettlementDate(
					).toGregorianCalendar(
					).getTime(),
					Constants.DATE_FORMAT));

		if (responseType.getSwitchDate() != null)
			requestDetails.setSwitchDateTime(
				Utility.formatDateToString(
					responseType.getSwitchDate(
					).toGregorianCalendar(
					).getTime(),
					Constants.DATE_TIME_FORMAT_24HR));

		// set currency code

		if (StringUtils.isNotBlank(responseType.getCustomData())) {
			String customData = responseType.getCustomData();

			String currencyCodeAcq = getAcqCurrencyCode(customData);
			String currencyCodeIss = getIssCurrencyCode(customData);
			String pinPresent = getPinPresent(customData);

			if (StringUtils.isNotBlank(pinPresent)) {
				if (pinPresent.equalsIgnoreCase("Y")) {
					requestDetails.setPinPresent(Constants.PIN_PRESENT_YES);
				}
				else if (pinPresent.equalsIgnoreCase("N")) {
					requestDetails.setPinPresent(Constants.PIN_PRESENT_NO);
				}
				else {
					requestDetails.setPinPresent(
						transactionAppProperties.getTransProps(
						).getProperty(
							Constants.PIN_PRESENT_NOORUNKNOWN
						));
				}
			}
			else {
				requestDetails.setPinPresent(
					transactionAppProperties.getTransProps(
					).getProperty(
						Constants.PIN_PRESENT_NOORUNKNOWN
					));
			}

			if (StringUtils.isNotBlank(currencyCodeAcq)) {
				requestDetails.setCurrencyCodeAcq(currencyCodeAcq);
			}
			else {
				requestDetails.setCurrencyCodeAcq(
					Constants.DEFAULT_CURRENCY_CODE);
			}

			if (StringUtils.isNotBlank(currencyCodeIss)) {
				requestDetails.setCurrencyCodeIss(currencyCodeIss);
			}
			else {
				requestDetails.setCurrencyCodeIss(
					Constants.DEFAULT_CURRENCY_CODE);
			}

			// set 3DS Authentication value

			String eciCode = getECICode(customData);
			String sliCode = getSLICode(customData);

			String authValue =
				StringUtils.isNotBlank(eciCode) ? eciCode : sliCode;

			if (StringUtils.isNotBlank(authValue)) {
				String value = getValid3DSAuthenticationValue(authValue);

				requestDetails.setPos3dsAuthentication(value);
				requestDetails.setPos3dsAuthDescription(
					AuthenticationCode.getDescriptionByValue(value));
			}
			else {
				requestDetails.setPos3dsAuthentication(Constants.EMPTY_STRING);
				requestDetails.setPos3dsAuthDescription(Constants.EMPTY_STRING);
			}
		}
		else {
			requestDetails.setCurrencyCodeAcq(Constants.DEFAULT_CURRENCY_CODE);
			requestDetails.setCurrencyCodeIss(Constants.DEFAULT_CURRENCY_CODE);
		}

		requestDetails.setIsMobile(isMobile(responseType.getCustomData()));

		if (responseType.getTransactionDate() != null) {
			requestDetails.setTransactionDateTime(
				Utility.formatDateToString(
					responseType.getTransactionDate(
					).toGregorianCalendar(
					).getTime(),
					Constants.DATE_TIME_FORMAT_24HR));
		}

		if (responseType.getLocalTransactionDate() != null) {
			requestDetails.setTransactionLocationDateTime(
				Utility.formatDateToString(
					responseType.getLocalTransactionDate(
					).toGregorianCalendar(
					).getTime(),
					Constants.DATE_TIME_FORMAT_24HR));
		}

		try {
			if (responseType.getLocalTransactionDate() != null) {
				requestDetails.setTransactionLocalDateTimeZone(
					Utility.formatDateToString(
						responseType.getLocalTransactionDate(
						).toGregorianCalendar(
						).getTime(),
						Constants.DATE_FORMAT_LONG));
			}
		}
		catch (Exception e) {

			// can be ignored, no action

			logger.error(
				"populateTransactionDetailFromResponseType - " + e.getMessage(),
				e);
		}

		if (responseType.getPosCondition() != null)
			requestDetails.setPosConditionDescription(
				responseType.getPosCondition(
				).getDescription());

		if (responseType.getPosEntryMode() != null)
			requestDetails.setPosEntryDescription(
				responseType.getPosEntryMode(
				).getDescription());

		if (responseType.getPosEntryMode() != null)
			requestDetails.setPosEntryMode(
				responseType.getPosEntryMode(
				).getCode());

		if (responseType.getPosMerchant() != null)
			requestDetails.setPosMerchantCode(
				responseType.getPosMerchant(
				).getCode());

		if (responseType.getPosMerchant() != null)
			requestDetails.setPosMerchantDescription(
				responseType.getPosMerchant(
				).getDescription());

		if (responseType.getResponseCode() != null)
			responseDetails.setCode(
				responseType.getResponseCode(
				).getCode());

		if (responseType.getResponseAuthorisationId() != null)
			responseDetails.setAuthorisationId(
				responseType.getResponseAuthorisationId());

		if (responseType.getResponseAuthorisedBy() != null)
			responseDetails.setAuthorisedBy(
				responseType.getResponseAuthorisedBy());

		if (responseType.getResponseCode() != null)
			responseDetails.setDescription(
				responseType.getResponseCode(
				).getDescription());

		if (!StringUtils.isBlank(responseType.getProcessingCode()))
			transactionDetail.setCode(responseType.getProcessingCode());

		if (!StringUtils.isBlank(responseType.getProcessingCode())) {
			transactionDetail.setDescription(
				findTransactionCodeDescriptionFromProcessingCode(
					responseType.getProcessingCode(),
					transactionDetail.getTransactionId(),
					transactionDetail.getBusDate(),
					requestDetails.getPosMerchantCode()));
		}

		if ((responseType.getMessageCodes() != null) &&
			(responseType.getMessageCodes(
			).size() > 0)) {

			String mesType = "";

			if (!StringUtils.isBlank(
					responseType.getMessageCodes(
					).get(
						0
					).getGroupCode())) {

				mesType = responseType.getMessageCodes(
				).get(
					0
				).getGroupShortDesc();
			}

			transactionDetail.setMessageType(mesType);
		}

		transactionDetail.setRequestDetails(requestDetails);
		transactionDetail.setResponseDetails(responseDetails);

		if (StringUtils.isNotBlank(responseType.getExternalTransactionId()))
			transactionDetail.setExternalTransactionId(
				responseType.getExternalTransactionId());

		if (StringUtils.isNotBlank(responseType.getFunctionCode())) {
			transactionDetail.setFunctionCode(responseType.getFunctionCode());
			String functionCodeDescription = setFunctionCodeDescription(
				responseType.getFunctionCode());

			transactionDetail.setFunctionCodeDescription(
				functionCodeDescription);
		}

		if (StringUtils.isNotBlank(responseType.getTransactionQualifierId())) {
			transactionDetail.setTransactionQualifierId(
				responseType.getTransactionQualifierId());
		}

		if (StringUtils.isNotBlank(responseType.getTransactionCode())) {
			transactionDetail.setTransactionCode(
				responseType.getTransactionCode());
		}

		if (StringUtils.isNotBlank(responseType.getCustomData())) {
			transactionDetail.setCustomData(responseType.getCustomData());
		}

		if (responseType.getPosCondition() != null) {
			transactionDetail.setPosCondition(responseType.getPosCondition());
		}

		if (responseType.getPosCondition() != null) {
			transactionDetail.setPosEntryMode(responseType.getPosEntryMode());
		}
		/* Project 8847 */
		/* As per agreement with Rajish, we check to see if Card Acceptor Name starts */
		/* with Redi, if yes, then we assume this is a Redi ATM and since all the */
		/* Redi ATM are EMV enabled, we display "EMV Enabled" as Terminal Capability */
		/* else we display nothing.  Further analysis and work need to be done if we */
		/* were to display Terminal Capability for All terminal types correctly */
		//This code is committed as per the new requirements in APCA Mandate project P0037
		/*requestDetails.setTerminalCapability(Constants.SPACE);

		if (responseType.getCardAcceptor() != null &&
			responseType.getCardAcceptor().getName() != null)
		{

			String cardAcceptorName = responseType.getCardAcceptor().getName().toUpperCase();

			if (cardAcceptorName.startsWith(Constants.REDI_ATM))
			{
				requestDetails.setTerminalCapability(Constants.EMV_ENABLED);
			}
		}*/

		/**
		 * This code is added as per the new requirements in APCA Mandate project P0037
		 */
		if (StringUtils.isNotBlank(responseType.getCustomData())) {
			String faultyCardReader = Utility.getF047FCRFromCustomData(
				responseType.getCustomData());
			String terminalCapabilityCode = Utility.getF047TCCFromCustomData(
				responseType.getCustomData());
			//boolean posEntryCheck = Arrays.asList(new String[]{"05","07"}).contains(responseType.getPosEntryMode());
			//boolean terminalCapabilityCheck = Arrays.asList(new String[]{"02","03","05","06","07"}).contains(terminalCapabilityCode);
			//if (posEntryCheck && terminalCapabilityCheck)
			//{
			requestDetails.setTerminalCapability(terminalCapabilityCode);
			//}
			//logger.debug("terminalCapabilityCode "+terminalCapabilityCode);
			//logger.debug("faultyCardReader "+faultyCardReader);

			if (!StringUtils.isBlank(faultyCardReader) &&
				faultyCardReader.equalsIgnoreCase("Y")) {

				requestDetails.setFaultyCardReader(Constants.YES);
			}
		}

		transactionDetail.setTrack2DataServiceCode(
			String.valueOf(responseType.getServiceCode()));

		logger.debug("populateTransactionDetailFromResponseType - end");

		return transactionDetail;
	}

	/**
	 * Populate the TransactionList from response object
	 *
	 * @param List&lt;TransactionBasicInformation&gt;
	 * @return List&lt;TransactionList&gt;
	 */
	public List<TransactionList> populateTransactionsearchResultFromWS(
		List<TransactionBasicInformation> transactionBasicInformations,
		Boolean switchDateType) {

		logger.debug("populateTransactionsearchResultFromWS - start");
		List<TransactionList> txList = new ArrayList<>();
		TransactionList transactionList = null;

		for (TransactionBasicInformation basicInformation :
				transactionBasicInformations) {

			transactionList = new TransactionList();

			if (basicInformation.getBusinessDate() != null) {
				transactionList.setBusniessDate(
					basicInformation.getBusinessDate());
			}

			if (basicInformation.getSource() != null)
				transactionList.setDataSource(basicInformation.getSource());

			if (basicInformation.getTransactionId() != null)
				transactionList.setTransactionId(
					basicInformation.getTransactionId());

			if (basicInformation.getPan() != null)
				transactionList.setPan(basicInformation.getPan());

			if (basicInformation.getCardAcceptorTerminalId() != null)
				transactionList.setTerminalId(
					basicInformation.getCardAcceptorTerminalId());

			if (basicInformation.getCardAcceptorIdCode() != null)
				transactionList.setCardAcceptorId(
					basicInformation.getCardAcceptorIdCode());

			if (StringUtils.isNotBlank(
					basicInformation.getExternalTransactionId()))
				transactionList.setExternalTransactionId(
					basicInformation.getExternalTransactionId());

			if (basicInformation.getResponseCode() != null)
				transactionList.setResponseCode(
					basicInformation.getResponseCode(
					).getCode());

			if (basicInformation.getResponseCode() != null)
				transactionList.setResponseDescription(
					basicInformation.getResponseCode(
					).getDescription());
			//function code
			String functionCode = "";

			if (StringUtils.isNotBlank(basicInformation.getFunctionCode())) {
				functionCode = basicInformation.getFunctionCode();

				if (Constants.STRING_0.equals(functionCode.trim())) {
					functionCode = "";
				}
				else {
					String functionCodeDescription = setFunctionCodeDescription(
						functionCode);

					transactionList.setFunctionCodeDescription(
						functionCodeDescription);
				}
			}

			transactionList.setFunctionCode(functionCode);

			if (basicInformation.getMessageCodes(
				).size() > 0) {

				String mesType = "";

				if (!StringUtils.isBlank(
						basicInformation.getMessageCodes(
						).get(
							0
						).getGroupCode())) {

					mesType = basicInformation.getMessageCodes(
					).get(
						0
					).getGroupShortDesc();
					transactionList.setSortableMsgType(
						basicInformation.getMessageCodes(
						).get(
							0
						).getGroupCode());
				}

				transactionList.setMessageType(mesType);
				transactionList.setMessageDescription(
					basicInformation.getMessageCodes(
					).get(
						0
					).getDescription());
			}

			transactionList.setSystemTrace(
				Utility.createStanDisplayString(
					basicInformation.getStan(), basicInformation.getAdvStan()));

			if (switchDateType) {
				if (basicInformation.getSystemTimestamp() != null) {
					transactionList.setDateTime(
						basicInformation.getSystemTimestamp(
						).toGregorianCalendar(
						).getTime());
				}
			}
			else {
				if (basicInformation.getTransactionDate() != null) {
					transactionList.setDateTime(
						basicInformation.getTransactionDate(
						).toGregorianCalendar(
						).getTime());
				}
			}

			if (!StringUtils.isBlank(basicInformation.getProcessingCode())) {
				transactionList.setDescription(
					findTransactionCodeDescriptionFromProcessingCode(
						basicInformation.getProcessingCode(),
						basicInformation.getTransactionId(),
						basicInformation.getBusinessDate(), null));
			}

			if (basicInformation.getAmount() > 0.0)
				transactionList.setAmount(basicInformation.getAmount());

			logger.debug(
				"basicInformation.getCardHolderBillAmount()  is " +
					basicInformation.getCardHolderBillAmount());
			logger.debug(
				"basicInformation.getAmount()  is " +
					basicInformation.getAmount());

			//if (basicInformation.getCardHolderBillAmount() > 0.0)
			transactionList.setCardHolderBillAmount(
				basicInformation.getCardHolderBillAmount());

			// set currency code

			if (!StringUtils.isBlank(basicInformation.getCustomData())) {
				String currencyCodeAcq = getAcqCurrencyCode(
					basicInformation.getCustomData());
				String currencyCodeIss = getIssCurrencyCode(
					basicInformation.getCustomData());

				if (StringUtils.isNotBlank(currencyCodeAcq)) {
					transactionList.setCurrencyCodeAcq(currencyCodeAcq);
				}
				else {
					transactionList.setCurrencyCodeAcq(
						Constants.DEFAULT_CURRENCY_CODE);
				}

				if (StringUtils.isNotBlank(currencyCodeIss)) {
					transactionList.setCurrencyCodeIss(currencyCodeIss);
				}
				else {
					transactionList.setCurrencyCodeIss(
						Constants.DEFAULT_CURRENCY_CODE);
				}
			}
			else {
				transactionList.setCurrencyCodeAcq(
					Constants.DEFAULT_CURRENCY_CODE);
				transactionList.setCurrencyCodeIss(
					Constants.DEFAULT_CURRENCY_CODE);
			}

			txList.add(transactionList);
		}

		logger.debug("populateTransactionsearchResultFromWS - end");

		return txList;
	}

	/**
	 * clear out the pooled connections to Cuscasl Web Services.
	 */
	public void purgePools() {
		transactionService.purgePools();
	}

	/**
	 * Retrieves and sets all the values for the drop down lists for Service requests
	 *
	 * @param TransactionDetail
	 * @param SearchHeader
	 *
	 */
	public ReasonData retrieveListToDisplay(
		TransactionDetail transactionDetail, SearchHeader searchHeader,
		PortletRequest request) {

		logger.debug("retrieveListToDisplay - start");

		ReasonData reasonData = new ReasonData();

		reasonData.setVisaType(
			retrieveSelfServiceMapFromProperty(
				Constants.SELF_SERVICE_NAME_PROPSKEY));
		reasonData.setVisaDestination(
			retrieveSelfServiceMapFromProperty(
				Constants.SELF_SERVICE_PORTLET_LOCATION_PROPSKEY));

		updateDestinationWithPortletLinkAndParameters(
			request,
			retrieveSelfServiceMapFromProperty(
				Constants.SELF_SERVICE_PORTLET_NAME_PROPSKEY),
			reasonData, transactionDetail);

		Boolean checkForPilotCustomers = Boolean.parseBoolean(
			transactionAppProperties.getTransProps(
			).getProperty(
				"check.for.pilot"
			));

		// TODO: This is just a temporary fix because we don't have a lot of
		// time to do this properly. Change this to be a proper User Role check
		// for the next iteration.

		if (checkForPilotCustomers) {
			setupChargebacksPilotOrgList();

			if ((null != getChargebackPilotOrgs()) &&
				!getChargebackPilotOrgs().isEmpty() &&
				!Utility.caseInsensitiveContains(
					searchHeader.getUserOrgId(), getChargebackPilotOrgs())) {

				logger.debug(
					"retrieveListToDisplay - " + searchHeader.getUserOrgId() +
						" is not in the chargeback pilots list");
				reasonData.getVisaType(
				).remove(
					Long.valueOf("3")
				);
				reasonData.getVisaType(
				).remove(
					Long.valueOf("4")
				);
			}
		}

		reasonData.setTc40FraudTypeMap(
			getAttributeMapForReasonType(searchHeader, 2));
		reasonData.setTc40NotificationTypeMap(
			getAttributeMapForReasonType(searchHeader, 3));
		reasonData.setTc40DetectionMap(
			getAttributeMapForReasonType(searchHeader, 12));
		reasonData.setTc52Map(getAttributeMapForReasonType(searchHeader, 4));

		HashMap<Long, String> atmPosMap = getAttributeMapForReasonType(
			searchHeader, 5);

		reasonData.setPosMap(
			retrieveMapBasedOnPropLookUp(
				atmPosMap, Constants.POS_REASON_TYPEID_PROPSKEY));
		reasonData.setAtmMap(
			retrieveMapBasedOnPropLookUp(
				atmPosMap, Constants.ATM_REASON_TYPEID_PROPSKEY));
		reasonData.setAtmDepositDisputeReasonsMap(
			retrieveMapBasedOnPropLookUp(
				atmPosMap, Constants.ATM_DEPOSIT_REASON_TYPEID_PROPSKEY));

		transactionDetail.setReasonData(reasonData);

		logger.debug("retrieveListToDisplay - end");

		return reasonData;
	}

	/**
	 * Set the isHsmUp Attribute in transaction details object based on status
	 * code in response from WS .
	 *
	 * @param GetTransactionResponseType
	 * @param TransactionDetail
	 * @return void .
	 */
	public void setHsmErrorForTransactionDetailsPage(
		GetTransactionResponseType responseType,
		TransactionDetail transactionDetail) {

		logger.debug("setHsmErrorForTransactionDetailsPage  - start");

		boolean isHsmErrorInWS = true;

		if (null != responseType.getHeader()) {
			if (StringUtils.isNotBlank(
					responseType.getHeader(
					).getStatusCode())) {

				String statusCode = responseType.getHeader(
				).getStatusCode();

				logger.debug("The response status for HSM is   " + statusCode);

				if (statusCode.equalsIgnoreCase(
						transactionAppProperties.getTransProps(
						).getProperty(
							Constants.STATUS_ERROR_HSM_PROPSKEY
						))) {

					logger.debug("The response status for HSM is FALSE ");
					isHsmErrorInWS = false;
				}
				else if (statusCode.equalsIgnoreCase(
							transactionAppProperties.getTransProps(
							).getProperty(
								Constants.STATUS_VALIDATION_PROPSKEY
							))) {

					logger.warn(
						"Validation error happended, no message will be display on portlet, when processing Transaction List page: statusCode is  " +
							statusCode);
				}
			}
		}

		transactionDetail.setHsmUp(isHsmErrorInWS);
		logger.debug("setHsmErrorForTransactionDetailsPage  - end");
	}

	public void setPctSearchService(PctUserSearchServiceImpl pctSearchService) {
		this.pctSearchService = pctSearchService;
	}

	/**
	 * @param transactionService
	 *            the transactionService to set
	 */
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	/**
	 * Add the member information to the Service Request.
	 *
	 * @param 	form			<em>ServiceRequestForm</em>
	 * @param 	transaction		<em>TransactionType</em>
	 * @return	TransactionType
	 */
	public TransactionType setupMemberInformationForTransaction(
		ServiceRequestForm form, TransactionType transaction) {

		logger.debug("setupMemberInformationForTransaction - start");

		// Member Information section.

		if (null != form.getMemberInformation()) {
			RequestMemberInformation member = form.getMemberInformation();

			transaction.setMemberName(member.getMemberName());
			transaction.setMemberNo(member.getMemberNumber());
		}

		logger.debug("setupMemberInformationForTransaction - end");

		return transaction;
	}

	/**
	 * Set the Error msg Attribute in list object based on status code in
	 * response from WS .
	 *
	 * @param String
	 * @param PortletContext
	 * @return String .
	 * @throws Exception
	 */
	public String setWebServiceErrorForTransactionSearchPage(
			String statusCode, PortletContext portletContext)
		throws Exception {

		String msgToDisplayOnPage = "";

		HashMap<String, String> headerStatusCodeMap =
			transactionService.getWebServStatusCodeProps();

		/**
		 * - if CODS down: returning in status code: 1,SYSTEM_ERROR - if the
		 * search is involved Alaric only and Alaric is down: returning status
		 * code: 2,SYSTEM_ERROR - if the search involves both CODS and Alaric
		 * and CODS is running and Alaric is down: returning status code:
		 * 2,SYSTEM_ERROR,IN COMPLETE - if there are errors that we don't know
		 * the cause then the returning status code: 0,SYSTEM_ERROR - if there
		 * is a generic error message from the web service: status code message
		 * is: SYSTEM_ERROR
		 Debug code...Start
		 Iterator it = headerStatusCodeMap.keySet().iterator();
		 while (it.hasNext()) {
		 String key = it.next().toString();
		 String value = headerStatusCodeMap.get(key);
		 logger.debug("setWebServiceErrorForTransactionSearchPage header status is: "
		 + key + " : " + value);
		 }
		 Debug code...END  */
		logger.debug("The status code for the response is " + statusCode);

		if (headerStatusCodeMap.get(
				Constants.STATUS_CODE_SYSTEM_ERROR_1
			).equalsIgnoreCase(
				statusCode
			)) {

			// problem with CODS - cant perform any searches.

			logger.warn("CODS down - cant perform any searches.");
			msgToDisplayOnPage = Constants.ERROR_MSG;
			audit.fail(
				portletContext.getResponse(), portletContext.getRequest(),
				AuditOrigin.PORTAL_ORIGIN, AuditCategories.TRANSACTION_SEARCH,
				" soap call returned null object on Transaction search ");
		}
		else if (headerStatusCodeMap.get(
					Constants.STATUS_CODE_SYSTEM_ERROR_2
				).equalsIgnoreCase(
					statusCode
				)) {

			// CODS up, Alaric down & search against Alaric only

			logger.warn("CODS up, Alaric down & search against Alaric only");
			msgToDisplayOnPage = Constants.ERROR_MSG;
		}
		else if (headerStatusCodeMap.get(
					Constants.STATUS_CODE_INCOMPLETE
				).equalsIgnoreCase(
					statusCode
				)) {

			// CODS up, search both CODS & Alaric - but alaric down

			logger.warn("CODS up, search both CODS & Alaric - but alaric down");
			msgToDisplayOnPage = Constants.INCOMPLETE_MSG;
		}
		else if (headerStatusCodeMap.get(
					Constants.STATUS_CODE_SYSTEM_ERROR_0
				).equalsIgnoreCase(
					statusCode
				)) {

			// other error

			logger.warn(
				"Generic / unknown error - Cuscal web service returned generic error when processing");
			msgToDisplayOnPage = Constants.ERROR_MSG;
		}
		else if (headerStatusCodeMap.get(
					Constants.STATUS_CODE_SYSTEM_ERROR
				).equalsIgnoreCase(
					statusCode
				)) {

			logger.warn(
				"A generic error has occured. Check the WebService logs for more information.");
			msgToDisplayOnPage = Constants.STATUS_SQL_TIMEOUT;
		}
		else if (headerStatusCodeMap.get(
					Constants.STATUS_CODE_VALIDATION
				).equalsIgnoreCase(
					statusCode
				)) {

			logger.warn(
				"Validation error occured, no message will be displayed on the portlet. When processing the Transaction List page: statusCode is " +
					statusCode);
		}

		audit.fail(
			portletContext.getResponse(), portletContext.getRequest(),
			AuditOrigin.PORTAL_ORIGIN, AuditCategories.TRANSACTION_SEARCH,
			" Status code for soap call on Transaction search  is " +
				statusCode + " and the message code for portlet display is " +
					msgToDisplayOnPage);

		return msgToDisplayOnPage;
	}

	/**
	 *
	 * @param transactionDetail
	 * @param portletContext
	 * @throws NumberFormatException
	 * @throws PortalException
	 * @throws SystemException
	 */
	private void addContactInformation(
			TransactionDetail transactionDetail, PortletContext portletContext)
		throws NumberFormatException, PortalException {

		logger.debug("addContactInformation - start");

		ContactInformation contactInfo = new ContactInformation();

		User user = UserLocalServiceUtil.getUser(
			Long.parseLong(
				portletContext.getRequest(
				).getUserPrincipal(
				).getName()));

		if (user != null) {
			if (null != user.getFirstName()) {
				contactInfo.setGivenName(user.getFirstName());
			}

			if (null != user.getLastName()) {
				contactInfo.setSurname(user.getLastName());
			}

			if (null != user.getEmailAddress()) {
				contactInfo.setEmail(user.getEmailAddress());
			}

			UserUtilImpl userUtil = new UserUtilImpl();

			contactInfo.setPhoneNo(
				userUtil.retrieveBusinessPhoneNumberForUser(user));

			String clientName = "";
			List<Organization> organisations = user.getOrganizations();

			if ((organisations != null) && (organisations.size() > 0)) {
				for (Organization org : organisations) {
					if ((clientName != null) && (clientName.length() > 0)) {
						clientName += ", " + org.getName();
					}
					else {
						clientName = org.getName();
					}
				}
			}

			contactInfo.setOrganisation(clientName);

			transactionDetail.setContactInformation(contactInfo);
		}

		logger.debug("addContactInformation - end");
	}

	/**
	 * Method to get the Extra transaction details for the Service Request screen.
	 *
	 * @param transactionDetail
	 * @param header
	 */
	private TransactionDetail addExtraTransactionDetails(
		TransactionDetail transactionDetail, SearchHeader searchHeader) {

		logger.debug("addExtraTransactionDetails - start");

		if (StringUtils.isNotBlank(transactionDetail.getTransactionId())) {
			TktRequestHeader header = Utility.convertSearchHeaderToTktHeader(
				searchHeader);

			if (null != header) {
				GetTransactionDetailsRequest request =
					new GetTransactionDetailsRequest();

				request.setHeader(header);
				XMLGregorianCalendar busDate =
					Utility.createXmlGregorianCalendarFromString(
						transactionDetail.getBusDate(),
						Constants.DATE_FORMAT_YYYYMMDD, Boolean.FALSE);

				request.setBusinessDate(busDate);
				request.setTrlId(
					Long.valueOf(transactionDetail.getTransactionId()));

				perfLogger_.debug(
					"transactionService.getTransactionExtraDetails() - begin");
				GetTransactionDetailsResponse response =
					transactionService.getTransactionExtraDetails(request);
				perfLogger_.debug(
					"transactionService.getTransactionExtraDetails() - end");

				if ((null != response) && (null != response.getHeader())) {
					ARNType responseType = response.getArn();

					if (null != responseType) {
						transactionDetail.setVisaArn(responseType.getArn());
					}

					// Cardholder has member information.

					CardHolderInfoType cardholderInfo = response.getMember();

					if (null != cardholderInfo) {
						MemberInformation memberInfo = new MemberInformation();

						memberInfo.setMemberName(
							cardholderInfo.getMemberName());
						memberInfo.setMemberNumber(
							cardholderInfo.getMemberNo());

						logger.debug(
							"addExtraTransactionDetails - setting cardholder information");
						transactionDetail.setMemberInformation(memberInfo);
					}

					setPosConditionAndDoValidationChecks(
						transactionDetail, response, busDate);
				}
			}
		}

		logger.debug("addExtraTransactionDetails - end");

		return transactionDetail;
	}

	/**
	 * Build a list of the Cuscal Issuer ID's for the logged in user.
	 *
	 * @param responseType	<code>FindEntitiesByAttributeWithIdResponseType</code>
	 * @param request		<code>PortletRequest</code>
	 */
	private void buildIssuerIdList(
		FindEntitiesByAttributeWithIdResponseType responseType,
		PortletRequest request) {

		logger.debug("buildIssuerIdList - start");

		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();
		List<String> issuerIdList = new ArrayList<>();

		if (null != session.getAttribute("issuerIdList")) {
			for (Entity entity : responseType.getEntities()) {
				if (entity.isIssuer()) {
					for (EntityAttribute att : entity.getEntityArrtibutes()) {
						AttributeType attType = att.getAttributeType();

						// Check if it is Cuscal Issuer ID which has an ID of 9.

						//TODO: This is not currently in production.

						if (attType.getAttributeTypeId() == 9) {
							issuerIdList.add(att.getAttributeTypeValue());
						}
					}
				}
			}

			session.setAttribute("issuerIdList", issuerIdList);
		}

		logger.debug("buildIssuerIdList - stop");
	}

	/**
	 * Decorate the ServiceRequest list.
	 *
	 * @param 	tickets	<code>List&lt;ServiceRequestResponseType&gt;</code>
	 * @return	List<TicketDetails>
	 */
	private List<TicketDetails> decoratedServiceRequestList(
		List<ServiceRequestResponseType> tickets) {

		List<TicketDetails> ticketList = new ArrayList<>();

		for (ServiceRequestResponseType requestType : tickets) {
			logger.debug(
				"decoratedTicketDetailsList - parsing ticket: " +
					requestType.getTicket(
					).getId());
			TicketDetails ticket = new TicketDetails();
			TicketType ticketType = requestType.getTicket();

			if (null != ticketType) {
				//Ticket Number
				/*DestinationType ticketDestination = ticketType.getDestination();

				if (null != ticketDestination) {
					if (ticketDestination.getDestinationId() != 0) {
						ticket.setTicketNumber(String.valueOf(
								ticketType.getId(), 10)
								+ "-"
								+ Long.toString(
										ticketDestination.getDestinationId(),
										10));
					} else {
						ticket.setTicketNumber(String.valueOf(
								ticketType.getId(), 10));
					}
				}*/
				if (StringUtils.isBlank(ticketType.getDestination())) {
					ticket.setTicketNumber(Constants.SUBMITTED);
				}
				else {
					ticket.setTicketNumber(ticketType.getDestination());
				}

				List<AttributesType> attributes = requestType.getTicket(
				).getAttributes();

				if (null != attributes) {
					ticket.setTicketAttributesList(attributes);
					/*

					for (AttributesType att : attributes) {
						if (StringUtils.equalsIgnoreCase("REQUESTTYPE",att.getType())) {
							//ticket.setTicketCategory(att.getValue());
						}
					}
					*/
				}

				TypeType ticketTypeType = ticketType.getType();

				if (null != ticketTypeType) {
					ticket.setTicketCategory(ticketTypeType.getDescription());
					ticket.setTicketTypeId(ticketTypeType.getTypeId());
				}

				//Ticket Category
				/*CategoryType ticketCategory = ticketType.getCategory();

				if (null != ticketCategory) {
					ticket.setTicketCategory(ticketCategory.getDescription());
				}*/

				//Ticket Created by
				UserType ticketUser = ticketType.getCreatedBy();

				if (null != ticketUser) {
					ticket.setTicketFirstName(ticketUser.getFname());
					ticket.setTicketLastName(ticketUser.getSname());
				}

				//Ticket Status
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
					logger.debug(
						"Service request formatted Created date is  " +
							ticket.getTicketSubmittedDate());
				}

				if (null != ticketType.getLastUpdatedTimestamp()) {
					GregorianCalendar cal2 = ticketType.getLastUpdatedTimestamp(
					).toGregorianCalendar();

					ticket.setTicketUpdateDate(
						Utility.formatDateToString(
							cal2.getTime(), Constants.DATE_TIME_FORMAT_24HR));
					logger.debug(
						"Service request formatted Last Updated date is  " +
							ticket.getTicketUpdateDate());
				}

				ticket.setTicketId(ticketType.getId());
			}

			ticketList.add(ticket);
		}

		return ticketList;
	}

	/**
	 *
	 * @param liferayOrgId
	 * @return
	 */
	private FindEntitiesByAttributeWithIdResponseType findEntitiesByAttribute(
		String liferayOrgId) {

		FindEntitiesByAttributeWithIdRequestType
			findEntitiesByAttributeWithIdRequest =
				new FindEntitiesByAttributeWithIdRequestType();
		findEntitiesByAttributeWithIdRequest.setAttributeValue(liferayOrgId);
		findEntitiesByAttributeWithIdRequest.setAttributeTypeId(4);

		FindEntitiesByAttributeWithIdResponseType
			findEntitiesByAttributeWithIdResponseType =
				transactionService.findEntitiesByAttributeWithId(
					findEntitiesByAttributeWithIdRequest);

		return findEntitiesByAttributeWithIdResponseType;
	}

	/**
	 * Get Acquire CurrencyCode
	 *
	 * @param String
	 * @return String
	 */
	private String getAcqCurrencyCode(String customData) {
		String result = "";

		if (customData.indexOf(Constants.ACQ_ISO_CODE_START_TAG) >= 0) {
			int position1 = customData.indexOf(
				Constants.ACQ_ISO_CODE_START_TAG);
			int position2 = customData.indexOf(Constants.ACQ_ISO_CODE_END_TAG);

			if (position2 > position1) {
				result = customData.substring(
					position1 + Constants.ACQ_ISO_CODE_START_TAG.length(),
					position2
				).trim();
			}
		}

		return result;
	}

	private HashMap<Long, String> getAttributeMapForReasonType(
		SearchHeader searchHeader, long reasonType) {

		List<AttributesListType> attributeList = getAttributeList(
			reasonType, searchHeader);
		HashMap<Long, String> reasonMap = new HashMap<>();

		for (AttributesListType attributes : attributeList) {
			reasonMap.put(attributes.getId(), attributes.getDescription());
		}

		return reasonMap;
	}

	/**
	 * Get ECI code for VISA/Master card
	 *
	 * @param customData
	 * @return String
	 */
	private String getECICode(String customData) {
		String result = Constants.EMPTY_STRING;

		if (customData.indexOf(Constants.ECPI_START_TAG) >= 0) {
			int position1 = customData.indexOf(Constants.ECPI_START_TAG);
			int position2 = customData.indexOf(Constants.ECPI_END_TAG);

			if (position2 > position1) {
				result = customData.substring(
					position1 + Constants.ECPI_START_TAG.length(), position2
				).trim();
			}
		}

		return result;
	}

	/**
	 * Get Issuer CurrencyCode
	 *
	 * @param String
	 * @return String
	 */
	private String getIssCurrencyCode(String customData) {
		String result = "";

		if (customData.indexOf(Constants.ISS_ISO_CODE_START_TAG) >= 0) {
			int position1 = customData.indexOf(
				Constants.ISS_ISO_CODE_START_TAG);
			int position2 = customData.indexOf(Constants.ISS_ISO_CODE_END_TAG);

			if (position2 > position1) {
				result = customData.substring(
					position1 + Constants.ISS_ISO_CODE_START_TAG.length(),
					position2
				).trim();
			}
		}

		return result;
	}

	/**
	 * Get the list of the PIN Change Transaction codes.
	 *
	 * @return	List<String>  of Transaction Codes that are going to be used for the search.
	 */
	private List<String> getPinChangeTransactionCodes() {
		List<String> pinChangeTransactionCodes = new ArrayList<>();

		String[] transactionCodes = transactionAppProperties.getTransProps(
		).getProperty(
			"pin.change.transaction.codes"
		).split(
			Constants.COMMA
		);

		for (String code : transactionCodes) {
			pinChangeTransactionCodes.add(code);
		}

		return pinChangeTransactionCodes;
	}

	/**
	 * Get Pin Present value
	 *
	 * @param String
	 * @return String
	 */
	private String getPinPresent(String customData) {
		String result = "";

		if (customData.indexOf(Constants.PIN_PRESENT_START) >= 0) {
			int position1 = customData.indexOf(Constants.PIN_PRESENT_START);
			int position2 = customData.indexOf(Constants.PIN_PRESENT_END);

			if (position2 > position1) {
				result = customData.substring(
					position1 + Constants.PIN_PRESENT_START.length(), position2
				).trim();
			}
		}

		return result;
	}

	/**
	 * Get SLI code for EFTPOS
	 *
	 * @param customData
	 * @return String
	 */
	private String getSLICode(String customData) {
		String result = Constants.EMPTY_STRING;

		if (customData.indexOf(Constants.F047_START_TAG) >= 0) {
			int position1 = customData.indexOf(Constants.F047_START_TAG);
			int position2 = customData.indexOf(Constants.F047_END_TAG);

			if (position2 > position1) {
				String f047Value = customData.substring(
					position1 + Constants.F047_START_TAG.length(), position2
				).trim();

				if (f047Value.indexOf(Constants.STRING_SLI) >= 0) {
					result = StringUtils.substringBefore(
						f047Value.substring(
							f047Value.indexOf(Constants.STRING_SLI) + 3),
						"\\");
				}
			}
		}

		return result;
	}

	/**
	 * Get Valid 3DS Authentication value
	 *
	 * @param eci_sli_value
	 * @return
	 */
	private String getValid3DSAuthenticationValue(String eci_sli_value) {
		String value = "";

		if (StringUtils.isNumeric(eci_sli_value)) {
			eci_sli_value = StringUtils.leftPad(eci_sli_value, 2, "0");

			if (eci_sli_value.matches("00|01|02|05|06|07")) {
				value = eci_sli_value;
			}
		}

		return value;
	}

	/**
	 * Check if the transaction is done on mobile
	 *
	 * @param String
	 * @return String
	 */
	private String isMobile(String customData) {
		String value = "";

		if (StringUtils.isNotBlank(customData)) {
			if (customData.indexOf(
					Constants.TRL_PREV_TXN_SAME_PERIOD_START_TAG) >= 0) {

				int startPos = customData.indexOf(
					Constants.TRL_PREV_TXN_SAME_PERIOD_START_TAG);
				int endPos = customData.indexOf(
					Constants.TRL_PREV_TXN_SAME_PERIOD_END_TAG);

				if (endPos > startPos) {
					value = StringUtils.trim(
						customData.substring(
							startPos +
								Constants.TRL_PREV_TXN_SAME_PERIOD_START_TAG.
									length(),
							endPos));

					if (Constants.TRL_PREV_TXN_SAME_PERIOD_1.equals(value)) {
						value = Constants.CUSCAL_HCE;
					}
					else if (Constants.TRL_PREV_TXN_SAME_PERIOD_2.equals(
								value)) {

						value = Constants.APPLE_PAY;
					}
					else if (Constants.TRL_PREV_TXN_SAME_PERIOD_3.equals(
								value)) {

						value = Constants.ANDROID_PAY;
					}
					else if (Constants.TRL_PREV_TXN_SAME_PERIOD_4.equals(
								value)) {

						value = Constants.SAMSUNG_PAY;
					}
					else if (Constants.TRL_PREV_TXN_SAME_PERIOD_5.equals(
								value)) {

						value = Constants.FITBIT_PAY;
					}
					else if (Constants.TRL_PREV_TXN_SAME_PERIOD_6.equals(
								value)) {

						value = Constants.GARMIN_PAY;
					}
				}
			}
		}

		if (StringUtils.isBlank(value)) {
			return Constants.NO;
		}

		return value;
	}

	/**
	 *
	 * @param transactionDetail
	 * @param request
	 * @return
	 */
	private Boolean isUserAnIssuerForTransaction(
		TransactionDetail transactionDetail, PortletRequest request) {

		logger.debug("isUserAnIssuerForTransaction - start");

		Boolean isIssuer = false;
		User user = Utility.getUserFromRequest(request);
		long[] orgIds;
		long liferayOrgId;

		try {
			orgIds = user.getOrganizationIds();
			liferayOrgId = orgIds[0];

			String organisationIdOverride =
				transactionAppProperties.getTransProps(
				).getProperty(
					Constants.ORG_OVERRRIDE
				);

			if (StringUtils.isNotEmpty(organisationIdOverride)) {
				logger.debug(
					"getOrgShortName - end, organisationIdOverride has been set.  Using " +
						organisationIdOverride);
				liferayOrgId = Long.parseLong(organisationIdOverride);
			}

			String transactionIssuer = transactionDetail.getRequestDetails(
			).getIssuerOrgName();

			List<Customer> customerAccessList = getCustomerAccessViewList(
				liferayOrgId);

			for (Customer customers : customerAccessList) {
				if (null != customers.getAuthenticIssuerShortName()) {
					String customerIssuerName =
						customers.getAuthenticIssuerShortName();

					if (StringUtils.isNotBlank(transactionIssuer) &&
						transactionIssuer.equals(customerIssuerName)) {

						logger.debug(
							"isUserAnIssuerForTransaction - User has access to Issuer " +
								customerIssuerName);

						return true;
					}

					logger.debug(
						"User has access to:" +
							customers.getAuthenticIssuerShortName());
				}
			}

			logger.debug(
				"isUserAnIssuerForTransaction - User does not have access to Issuer " +
					transactionDetail.getRequestDetails(
					).getIssuerOrgName());
		}
		catch (PortalException e) {
			logger.error(
				"isUserAnIssuerForTransaction - error retrieving Issuser list",
				e);
		}
		catch (SystemException e) {
			logger.error(
				"isUserAnIssuerForTransaction - error retrieving Issuser list",
				e);
		}

		logger.debug("isUserAnIssuerForTransaction - end");

		return isIssuer;
	}

	private boolean isValidMasterCardServiceRequest(
		TransactionDetail transactionDetail,
		GetTransactionDetailsResponse response) {

		boolean isValidServiceRequest = false;

		RawTransactionAttributesType transactionAttributes =
			response.getRawTransactionAttributes();
		logger.debug(
			"isValidMasterCardServiceRequest - start transactionAttributes=" +
				transactionAttributes);

		if (null != transactionAttributes) {
			//Prevent the user from seeing the screen at all and show in eligible message
			//Is valid message type?
			String messageType = transactionDetail.getMessageType();
			logger.debug("messageType =" + messageType);

			if (StringUtils.isBlank(messageType) ||
				!messageType.startsWith(Constants.MESSAGE_TYPE_2)) {

				logger.debug(
					"setPosConditionAndDoValidationChecks - Transaction is not 02XX and messageType=" +
						messageType);

				return false;
			}

			//is a declined transaction

			if (StringUtils.isNotBlank(
					transactionAttributes.getTrlActionResponseCode())) {

				Integer respCode = Integer.parseInt(
					transactionAttributes.getTrlActionResponseCode());
				logger.debug("respCode =" + respCode);

				if (respCode.compareTo(Constants.RESP_CODE_100) >= 0) {
					logger.debug(
						"setPosConditionAndDoValidationChecks - Transaction is declined and respCode=" +
							respCode);

					return false;
				}
			}
			else {
				logger.debug(
					"ransactionAttributes.getTrlActionResponseCode() is blank/null");

				return false;
			}

			logger.debug(
				"transactionAttributes.getTrlTscId() =" +
					transactionAttributes.getTrlTscId());

			if (StringUtils.isNotBlank(transactionAttributes.getTrlTscId()) &&
				(transactionAttributes.getTrlTscId(
				).equals(
					Constants.AVAILABLE_FUNDS_ENQUIRY_30
				) ||
				 transactionAttributes.getTrlTscId(
				 ).equals(
					 Constants.BALANCE_INQUIRY_31
				 ))) {

				logger.debug(
					"setPosConditionAndDoValidationChecks - Transaction is not a valid transaction type =" +
						transactionAttributes.getTrlTscId());

				return false;
			}

			POSConditionType posCondition = response.getPosCondition();

			if (null != posCondition) {
				String condition = posCondition.getPosCondition();
				logger.debug(
					"transactionAttributes.getTrlOriginIchName(): " +
						transactionAttributes.getTrlOriginIchName());

				if (StringUtils.isNotBlank(
						transactionAttributes.getTrlOriginIchName())) {

					//if MasterCard base1 base 1 then in eligible

					if (transactionAttributes.getTrlOriginIchName(
						).equalsIgnoreCase(
							Constants.MASTERCARD_CIS
						)) {

						logger.debug(
							"setPosConditionAndDoValidationChecks - Transaction is mastercard transaction");

						return false;
					}
					else if (Constants.MASTERCARD_IPM_TransactionBatch.
								equalsIgnoreCase(
									transactionAttributes.
										getTrlOriginIchName()) ||
							 Constants.MASTERCARD_CMS_TransactionBatch.
								 equalsIgnoreCase(
									 transactionAttributes.
										 getTrlOriginIchName())) {

						if (Constants.TRANSACTION_QUALIFIER_ID_BEST_ADVICE.
								equalsIgnoreCase(
									transactionDetail.
										getTransactionQualifierId())) {

							logger.debug(
								"Valid Transaction is mastercard transaction " +
									transactionAttributes.
										getTrlOriginIchName());
							isValidServiceRequest = true;
						}
						else {
							logger.debug(
								"TRANSACTION_QUALIFIER_ID_BEST_ADVICE is not BA and value is =" +
									transactionDetail.
										getTransactionQualifierId());

							return false;
						}
					}
					else {

						// do atm pos validation

						if (StringUtils.isNotBlank(condition) &&
							(condition.equals(Constants.ATM) ||
							 condition.equals(Constants.BILATERAL_ATM) ||
							 condition.equals(Constants.BILATERAL_POS))) {

							if ((StringUtils.isNotBlank(
									transactionAttributes.getTrlTscId()) &&
								 transactionAttributes.getTrlTscId(
								 ).equals(
									 Constants.SALES_REVERSAL_22
								 )) ||
								transactionAttributes.getTrlTscId(
								).equals(
									Constants.CARDHOLDER_ACC_TRANSFER_40
								)) {

								logger.debug(
									"Not a valid record transactionAttributes.getTrlTscId() = " +
										transactionAttributes.getTrlTscId());

								return false;
							}
							else if (StringUtils.isNotBlank(
										transactionDetail.getRequestDetails(
										).getIssuerId()) &&
									 transactionDetail.getRequestDetails(
									 ).getIssuerId(
									 ).equals(
										 Constants.AMP_ISSUER
									 ) &&
									 StringUtils.isNotBlank(
										 transactionDetail.getRequestDetails(
										 ).getAcquirerId()) &&
									 transactionDetail.getRequestDetails(
									 ).getAcquirerId(
									 ).equals(
										 Constants.MASTERCARD_ACQ
									 )) {

								logger.debug(
									"setPosConditionAndDoValidationChecks - Transaction is for an international card holder IssuerId=" +
										transactionDetail.getRequestDetails(
										).getIssuerId());

								return false;
							}

							if (condition.equals(Constants.BILATERAL_POS)) {
								if (transactionAttributes.getTrlTscId(
									).equals(
										Constants.RETURNS_20
									)) {

									logger.debug(
										"condition BILATERAL_POS and transactionAttributes.getTrlTscId is RETURNS_20");

									return false;
								}
							}
							else {
								if (transactionAttributes.getTrlTscId(
									).equals(
										Constants.PURCHASE_0
									)) {

									logger.debug(
										"It is a purchase transaction");

									return false;
								}
							}
						}
						else {
							logger.debug(
								"setPosConditionAndDoValidationChecks - Transaction is not a valid ATM/POS");

							return false;
						}
					}
				}
			}

			logger.debug("setPosConditionAndDoValidationChecks - end");
		}

		/**
		 * It is a valid MasterCard transaction
		 * TRL_MESSAGE_TYPE = 0220,
		 * TRL_ORIGIN_ICH_NAME = IPM_TransactionBatch
		 * TRL_FUNCTION_CODE = 200 or 205 or 282
		 * TRL_TQU_ID = BA ( Best Advice - for settlement or clearing) - Identity of Transaction Qualifier in Transaction Qualifier Table
		 */
		transactionDetail.setMessageTypeCode(
			transactionAttributes.getTrlMessageType());
		transactionDetail.setOriginIchName(
			transactionAttributes.getTrlOriginIchName());

		logger.debug("transactionDetail= " + transactionDetail.toString());

		return isValidServiceRequest;
	}

	/**
	 * Return the PCT User Details Response from the WS.
	 *
	 * @param pctUserId		The pctUserId who's details we are trying to get.
	 * @param searchHeader	The header to send to the WS request.
	 * @return				GetPCTUserByIdResponseType.
	 */
	private GetPCTUserByIdResponseType pctUserDetailsResponse(
		String pctUserId, SearchHeader searchHeader) {

		logger.debug("returnPctUserFullName - start");

		StandardHeader header = new StandardHeader();

		if (searchHeader != null) {
			header.setOrigin(searchHeader.getOrigin());
			header.setUserId(searchHeader.getUserId());
			header.setUserOrgId(searchHeader.getUserOrgId());
			header.setUserOrgName(searchHeader.getUserOrgName());

			GetPCTUserByIdRequestType pctUserRequest =
				new GetPCTUserByIdRequestType();

			pctUserRequest.setHeader(header);
			pctUserRequest.setPctUserId(pctUserId);

			GetPCTUserByIdResponseType pctUserResponse =
				pctSearchService.pctGetUserById(pctUserRequest);

			if (pctUserResponse != null) {
				return pctUserResponse;
			}

			logger.error(
				"returnPctUserFullName - WebService response is null. WebService could be uncontactable");

			return null;
		}

		logger.error(
			"returnPctUserFullName - searchHeader is null. Cannot search for user without the searchHeader");

		return null;
	}

	/**
	 * Return the PCT User's name for PIN Change History Search
	 *
	 * @param GetPCTUserByIdResponseType	Response Type from the WebService.
	 * @param String				First or Last.
	 * @return	String				The first or last name based on the type.
	 */
	private String pctUserName(
		GetPCTUserByIdResponseType pctUserResponse, String type) {

		String name = null;

		if (pctUserResponse == null) {
			logger.error(
				"populatePinChangeFromResponse - WS response to get PCT user details is null");
		}
		else {
			String statusCode = pctUserResponse.getHeader(
			).getStatusCode();

			if (StringUtils.equalsIgnoreCase(
					statusCode, Constants.STATUS_SUCCESS)) {

				if (StringUtils.equalsIgnoreCase(Constants.FIRST, type)) {
					name = pctUserResponse.getPctUser(
					).getFirstName();
				}
				else {
					name = pctUserResponse.getPctUser(
					).getLastName();
				}
			}
			else {
				logger.error(
					"populatePinChangeFromResponse - WS Status code for pctGetUserByUserId is: " +
						statusCode);
			}
		}

		return name;
	}

	/**
	 * Traverse the response for the PinChange Details
	 *
	 * @param GetTransactionResponseType
	 * @param TransactionDetail
	 * @param SearchHeader
	 * @return TransactionDetail
	 */
	private TransactionDetail populatePinChangeFromResponse(
		GetTransactionResponseType pinChangeResponse,
		TransactionDetail pinChangeTransactionDetail,
		SearchHeader searchHeader) {

		logger.debug("populatePinChangeFromResponse - start");
		RequestDetails requestDetails = new RequestDetails();
		ResponseDetails responseDetails = new ResponseDetails();

		// Set the Transaction Request Details.

		if (pinChangeResponse.getIssuer() != null) {
			Institution issuer = pinChangeResponse.getIssuer();

			requestDetails.setIssuerId(issuer.getId());
			requestDetails.setIssuerName(issuer.getName());
		}

		requestDetails.setCardAcceptorRetrievalReferenceNumber(
			pinChangeResponse.getRetrievalReferenceNumber());

		if (pinChangeResponse.getAcquirer() != null) {
			Institution acq = pinChangeResponse.getAcquirer();

			requestDetails.setAcquirerId(acq.getId());
			requestDetails.setAcquirerName(acq.getName());
		}

		if (pinChangeResponse.getTransactionDate() != null) {
			requestDetails.setTransactionDateTime(
				Utility.formatDateToString(
					pinChangeResponse.getTransactionDate(
					).toGregorianCalendar(
					).getTime(),
					Constants.DATE_TIME_FORMAT_24HR));
		}

		if (pinChangeResponse.getLocalTransactionDate() != null) {
			requestDetails.setTransactionLocationDateTime(
				Utility.formatDateToString(
					pinChangeResponse.getLocalTransactionDate(
					).toGregorianCalendar(
					).getTime(),
					Constants.DATE_TIME_FORMAT_24HR));
		}

		if (pinChangeResponse.getSwitchDate() != null) {
			requestDetails.setSwitchDateTime(
				Utility.formatDateToString(
					pinChangeResponse.getSwitchDate(
					).toGregorianCalendar(
					).getTime(),
					Constants.DATE_TIME_FORMAT_24HR));
		}

		if (StringUtils.isNotBlank(pinChangeResponse.getStan())) {
			requestDetails.setAcquirerSystemTrace(pinChangeResponse.getStan());
		}

		if (StringUtils.isNotBlank(
				pinChangeResponse.getPosMerchant(
				).getCode())) {

			requestDetails.setPosMerchantCode(
				pinChangeResponse.getPosMerchant(
				).getCode());
		}

		// Set the Transaction Response Details

		if (pinChangeResponse.getResponseCode() != null) {
			ResponseCode response = pinChangeResponse.getResponseCode();

			responseDetails.setCode(response.getCode());
			responseDetails.setDescription(response.getDescription());
		}

		if (StringUtils.isNotBlank(
				pinChangeResponse.getResponseAuthorisationId())) {

			responseDetails.setAuthorisationId(
				pinChangeResponse.getResponseAuthorisationId());
		}

		// Set the rest of the transaction detail object.

		if (pinChangeResponse.getPan() != null) {
			requestDetails.setPan(pinChangeResponse.getPan());
			requestDetails.setMaskedPan(
				Utility.mask(pinChangeResponse.getPan(), 'X'));
		}

		if (pinChangeResponse.getExpiryDate() != null) {
			String expiryDate = Utility.createExpiryDateFormat(
				pinChangeResponse.getExpiryDate());

			if (StringUtils.isNotBlank(expiryDate)) {
				requestDetails.setExpiryDate(expiryDate);
			}
			else {
				requestDetails.setExpiryDate(pinChangeResponse.getExpiryDate());
			}
		}

		if (StringUtils.isNotBlank(pinChangeResponse.getProcessingCode())) {
			pinChangeTransactionDetail.setCode(
				pinChangeResponse.getProcessingCode());

			pinChangeTransactionDetail.setDescription(
				findTransactionCodeDescriptionFromProcessingCode(
					pinChangeResponse.getProcessingCode(),
					pinChangeTransactionDetail.getTransactionId(),
					pinChangeTransactionDetail.getBusDate(),
					requestDetails.getPosMerchantCode()));
		}

		if ((pinChangeResponse.getMessageCodes() != null) &&
			(pinChangeResponse.getMessageCodes(
			).size() > 0)) {

			String messageType = "";

			if (StringUtils.isNotBlank(
					pinChangeResponse.getMessageCodes(
					).get(
						0
					).getGroupCode())) {

				messageType = pinChangeResponse.getMessageCodes(
				).get(
					0
				).getGroupShortDesc();
			}

			pinChangeTransactionDetail.setMessageType(messageType);
		}

		if (StringUtils.isNotBlank(pinChangeResponse.getCustomData())) {
			String pctOperatorId = returnPctOperatorId(
				pinChangeResponse.getCustomData());
			String pctSupervisorId = returnPctSupervisorId(
				pinChangeResponse.getCustomData());

			if (StringUtils.isNotBlank(pctOperatorId)) {
				GetPCTUserByIdResponseType pctUserResponse =
					pctUserDetailsResponse(pctOperatorId, searchHeader);

				String firstName = pctUserName(
					pctUserResponse, Constants.FIRST);
				String lastName = pctUserName(pctUserResponse, Constants.LAST);
				firstName = StringUtils.isNotBlank(firstName) ?
					Constants.SPACE_DASH_SPACE + firstName : "";
				lastName =
					StringUtils.isNotBlank(lastName) ?
						Constants.SPACE + lastName : "";

				if (StringUtils.isBlank(firstName) &&
					StringUtils.isBlank(lastName)) {

					firstName =
						Constants.SPACE_DASH_SPACE + Constants.NOT_AVAILABLE;
				}

				pctOperatorId = pctOperatorId + firstName + lastName;

				logger.debug(
					"populatePinChangeFromResponse - pctOperatorId is: " +
						pctOperatorId);
			}

			if (StringUtils.isNotBlank(pctSupervisorId)) {
				GetPCTUserByIdResponseType pctUserResponse =
					pctUserDetailsResponse(pctSupervisorId, searchHeader);

				String firstName = pctUserName(
					pctUserResponse, Constants.FIRST);
				String lastName = pctUserName(pctUserResponse, Constants.LAST);
				firstName = StringUtils.isNotBlank(firstName) ?
					Constants.SPACE_DASH_SPACE + firstName : "";
				lastName =
					StringUtils.isNotBlank(lastName) ?
						Constants.SPACE + lastName : "";

				if (StringUtils.isBlank(firstName) &&
					StringUtils.isBlank(lastName)) {

					firstName =
						Constants.SPACE_DASH_SPACE + Constants.NOT_AVAILABLE;
				}

				pctSupervisorId = pctSupervisorId + firstName + lastName;
			}

			pinChangeTransactionDetail.setPinChangeOperatorId(pctOperatorId);

			pinChangeTransactionDetail.setPinChangeSupervisorId(
				pctSupervisorId);
		}

		pinChangeTransactionDetail.setRequestDetails(requestDetails);
		pinChangeTransactionDetail.setResponseDetails(responseDetails);
		logger.debug("populatePinChangeFromResponse - end");

		return pinChangeTransactionDetail;
	}

	/**
	 * Populate the Pin Change History from response object
	 *
	 * @param List<TransactionBasicInformation>
	 * @return List<TransactionList>
	 */
	private List<TransactionList> populatePinChangeResultsFromWS(
		List<TransactionBasicInformation> transactionBasicInformations) {

		logger.debug("populatePinChangeResultsFromWS - start");
		List<TransactionList> pinChangeList = new ArrayList<>();
		TransactionList pinChangeTransaction = null;

		for (TransactionBasicInformation transaction :
				transactionBasicInformations) {

			pinChangeTransaction = new TransactionList();

			pinChangeTransaction.setPan(transaction.getPan());
			pinChangeTransaction.setTransactionId(
				transaction.getTransactionId());

			pinChangeTransaction.setBusniessDate(transaction.getBusinessDate());
			pinChangeTransaction.setDataSource(transaction.getSource());
			pinChangeTransaction.setCardAcceptorId(
				transaction.getCardAcceptorIdCode());

			pinChangeTransaction.setTerminalId(
				transaction.getCardAcceptorTerminalId());

			pinChangeTransaction.setDescription(transaction.getDescription());
			pinChangeTransaction.setSystemTrace(transaction.getStan());

			if (transaction.getResponseCode() != null) {
				pinChangeTransaction.setResponseCode(
					transaction.getResponseCode(
					).getCode());
				pinChangeTransaction.setResponseDescription(
					transaction.getResponseCode(
					).getDescription());
			}

			pinChangeTransaction.setDateTime(
				transaction.getTransactionDate(
				).toGregorianCalendar(
				).getTime());

			pinChangeList.add(pinChangeTransaction);
		}

		logger.debug("populatePinChangeResultsFromWS - end");

		return pinChangeList;
	}

	/**
	 * Look up property value to evaluate what to put in the map
	 *
	 */
	private HashMap<Long, String> retrieveMapBasedOnPropLookUp(
		HashMap<Long, String> atmPosMap, String key) {

		HashMap<Long, String> disputeMap = new HashMap<>();

		if (StringUtils.isNotBlank(key)) {
			String[] lookupValues = transactionAppProperties.getTransProps(
			).getProperty(
				key
			).split(
				Constants.COMMA
			);

			for (String id : lookupValues) {
				Long lookUpid = Long.valueOf(id);

				disputeMap.put(lookUpid, atmPosMap.get(lookUpid));
			}
		}

		return disputeMap;
	}

	/**
	 * Look up property value to evaluate what to put in the map
	 *
	 */
	private HashMap<Long, String> retrieveSelfServiceMapFromProperty(
		String type) {

		logger.debug(
			"retrieveSelfServiceMapFromProperty - start . type=" + type);

		HashMap<Long, String> nameMap = new HashMap<>();

		long i = 1;

		while (true) {
			String key =
				Constants.SERVICE_REQUEST_PREFIX_PROPSKEY + i + "." + type;

			String name = selfServiceProperties.getSelfserviceProperty(
			).getProperty(
				key
			);

			if (StringUtils.isBlank(name)) {
				break;
			}

			nameMap.put(i, name);

			i++;

			if (i > 200)

				break; // prevent infinite loop
		}

		logger.debug("retrieveSelfServiceMapFromProperty - end");

		return nameMap;
	}

	/**
	 * Get the PIN Change Operator ID.
	 *
	 * @param String	Custom data to search for the PCT Operator ID.
	 * @return	String			The pctUserId from the custom field.
	 */
	private String returnPctOperatorId(String customData) {
		String result = "";

		if (customData.indexOf(Constants.PIN_CHANGE_OPERATOR_ID_START) >= 0) {
			int startPos = customData.indexOf(
				Constants.PIN_CHANGE_OPERATOR_ID_START);
			int endPos = customData.indexOf(
				Constants.PIN_CHANGE_OPERATOR_ID_END);

			if (endPos > startPos) {
				result = customData.substring(
					startPos + Constants.PIN_CHANGE_OPERATOR_ID_START.length(),
					endPos);
			}

			logger.debug("returnPctOperatorId - pctOperatorId is: " + result);
		}

		return result;
	}

	/**
	 * Get the PIN Change Supervisor ID
	 *
	 * @param String	Custom data to search for the PCT Supervisor ID.
	 * @return	String			The pctUserId from the custom field.
	 */
	private String returnPctSupervisorId(String customData) {
		String result = "";

		if (customData.indexOf(Constants.PIN_CHANGE_SUPERVISOR_ID_START) >= 0) {
			int startPos = customData.indexOf(
				Constants.PIN_CHANGE_SUPERVISOR_ID_START);
			int endPos = customData.indexOf(
				Constants.PIN_CHANGE_SUPERVISOR_ID_END);

			if (endPos > startPos) {
				result = customData.substring(
					startPos +
						Constants.PIN_CHANGE_SUPERVISOR_ID_START.length(),
					endPos);
			}
		}

		return result;
	}

	private String setFunctionCodeDescription(String functionCode) {
		String functionCodeDescription = "";

		if (FunctionCode.MASTERCARD_FIRST_PRESENTMENT.getValue(
			).equals(
				functionCode
			)) {

			functionCodeDescription =
				Constants.
					MASTERCARD_FIRST_PRESENTMENT_FUNCTION_CODE_DESCRIPTION;
		}
		else if (FunctionCode.MASTERCARD_SECOND_PRESENTMENT_FULL.getValue(
				).equals(
					functionCode
				)) {

			functionCodeDescription =
				Constants.
					MASTERCARD_SECOND_PRESENTMENT_FULL_FUNCTION_CODE_DESCRIPTION;
		}
		else if (FunctionCode.MASTERCARD_SECOND_PRESENTMENT_PARTIAL.getValue(
				).equals(
					functionCode
				)) {

			functionCodeDescription =
				Constants.
					MASTERCARD_SECOND_PRESENTMENT_PARTIAL_FUNCTION_CODE_DESCRIPTION;
		}

		return functionCodeDescription;
	}

	private void setMasterCardDisputeParameters(
		PortletURL link, TransactionDetail transDetails) {

		logger.debug("setMasterCardDisputeParameters - start");
		//the parameter can be different, now Visa and MasterCard parameters are same
		link.setParameter(
			Constants.TX_RENDER, Constants.CHARGEBACK_REDIRECT_RENDER);
		link.setParameter(Constants.TX_ID, transDetails.getTransactionId());
		link.setParameter(Constants.BUS_DATE, transDetails.getBusDate());

		logger.debug("setMasterCardDisputeParameters - end");
	}

	private void setMasterCardPosConditionAndDoValidationChecks(
		TransactionDetail transactionDetail,
		GetTransactionDetailsResponse response) {

		logger.debug("setMasterCardPosConditionAndDoValidationChecks - start");
		//setup default values
		transactionDetail.setValidServiceRequest(
			isValidMasterCardServiceRequest(transactionDetail, response));
		transactionDetail.setVisaAtm(false);
		transactionDetail.setReversal(false);

		//Cardholder has member information.
		RawTransactionAttributesType transactionAttributes =
			response.getRawTransactionAttributes();

		if (null != transactionAttributes) {

			//Prevent the user from seeing the screen at all and show in eligible message
			//Is valid message type?
			String messageType = transactionDetail.getMessageType();
			logger.debug("messageType =" + messageType);

			if (StringUtils.isBlank(messageType) ||
				!messageType.startsWith(Constants.MESSAGE_TYPE_2)) {

				//transactionDetail.setValidServiceRequest(false);
				logger.debug(
					"setPosConditionAndDoValidationChecks - Transaction is not 02XX");
			}

			//is a declined transaction

			if (StringUtils.isNotBlank(
					transactionAttributes.getTrlActionResponseCode())) {

				Integer respCode = Integer.parseInt(
					transactionAttributes.getTrlActionResponseCode());
				logger.debug("respCode =" + respCode);

				if (respCode.compareTo(Constants.RESP_CODE_100) >= 0) {
					//transactionDetail.setValidServiceRequest(false);
					logger.debug(
						"setPosConditionAndDoValidationChecks - Transaction is declined and respCode=" +
							respCode);
				}
			}
			else {
				//transactionDetail.setValidServiceRequest(false);
				logger.debug(
					"ransactionAttributes.getTrlActionResponseCode() is blank/null");
			}

			// The full set of rules will be implemented in next release

			//			if (StringUtils.isNotBlank(transactionAttributes.getTrlTscId()) &&
			//					!(transactionAttributes.getTrlTscId().equals(Constants.PURCHASE_0) ||
			//					 transactionAttributes.getTrlTscId().equals(Constants.WITHDRAWAL_1) ||
			//					 transactionAttributes.getTrlTscId().equals(Constants.REVERSAL_22) || //add 02
			//					 transactionAttributes.getTrlTscId().equals(Constants.PURCHASE_CASHOUT_9))){
			//				transactionDetail.setValidServiceRequest(false);
			//				logger.debug("setPosConditionAndDoValidationChecks - Transaction is not a valid transaction type");
			//			}

			logger.debug(
				"transactionAttributes.getTrlTscId() =" +
					transactionAttributes.getTrlTscId());

			if (StringUtils.isNotBlank(transactionAttributes.getTrlTscId()) &&
				(transactionAttributes.getTrlTscId(
				).equals(
					Constants.AVAILABLE_FUNDS_ENQUIRY_30
				) ||
				 transactionAttributes.getTrlTscId(
				 ).equals(
					 Constants.BALANCE_INQUIRY_31
				 ))) {

				//transactionDetail.setValidServiceRequest(false);
				logger.debug(
					"setPosConditionAndDoValidationChecks - Transaction is not a valid transaction type =" +
						transactionAttributes.getTrlTscId());
			}

			POSConditionType posCondition = response.getPosCondition();
			logger.debug("posCondition =" + posCondition);

			if (null != posCondition) {
				String condition = posCondition.getPosCondition();

				logger.debug(
					"setPosConditionAndDoValidationChecks - POS Condition: " +
						condition);
				transactionDetail.setVisaOrAtmPos(condition);

				//Do MC validation
				//moved this check before POS condition check for MC as sometimes TRL_CHANNEL is null for MC transactions.
				logger.debug(
					"setPosConditionAndDoValidationChecks - POS Condition: " +
						condition);
				logger.debug(
					"transactionAttributes.getTrlOriginIchName(): " +
						transactionAttributes.getTrlOriginIchName());

				if (StringUtils.isNotBlank(
						transactionAttributes.getTrlOriginIchName())) {

					//if MasterCard base1 base 1 then in eligible

					if (transactionAttributes.getTrlOriginIchName(
						).equalsIgnoreCase(
							Constants.MASTERCARD_CIS
						)) {

						//transactionDetail.setValidServiceRequest(false);
						logger.debug(
							"setPosConditionAndDoValidationChecks - Transaction is mastercard transaction");
					}
					else if (Constants.MASTERCARD_IPM_TransactionBatch.
								equalsIgnoreCase(
									transactionAttributes.
										getTrlOriginIchName()) ||
							 Constants.MASTERCARD_CMS_TransactionBatch.
								 equalsIgnoreCase(
									 transactionAttributes.
										 getTrlOriginIchName())) {

						logger.debug(
							"setPosConditionAndDoValidationChecks - Transaction is mastercard presentmment");
						String visaType = posCondition.getVisaType();
						logger.debug("visaType=" + visaType);

						if (StringUtils.isNotBlank(visaType)) {
							transactionDetail.setVisaType(visaType);
							logger.debug(
								"setPosConditionAndDoValidationChecks - Visa Type " +
									visaType);
						}
						else {
							transactionDetail.setVisaType(
								Constants.MASTERCARD_IPM);
							logger.debug(
								"setPosConditionAndDoValidationChecks - Visa Type MASTERCARD_IPM");
						}

						//Set to MASTERCARD if its blank

						if (StringUtils.isBlank(condition)) {
							transactionDetail.setVisaOrAtmPos(
								Constants.MASTERCARD_IPM);
						}

						//if Visa ATM : stop TC52 submission - display "A Voucher Request cannot be submitted for an ATM transaction"

						logger.debug(
							"transactionAttributes.getTrlMccId()=" +
								transactionAttributes.getTrlMccId());
						logger.debug("visaType=" + visaType);

						if (StringUtils.isNotBlank(
								transactionAttributes.getTrlMccId()) &&
							Constants.VISA_ATM_6011.equals(
								transactionAttributes.getTrlMccId())) {

							transactionDetail.setVisaAtm(true);
							logger.debug(
								"setPosConditionAndDoValidationChecks - Transaction is an ATM");
						}

						//if reversal : stop TC52 submission - display "A Voucher Request cannot be submitted for an ATM transaction"

						if (StringUtils.isNotBlank(
								transactionAttributes.getTrlTscId()) &&
							(transactionAttributes.getTrlTscId(
							).equals(
								Constants.SALES_REVERSAL_22
							) ||
							 transactionAttributes.getTrlTscId(
							 ).equals(
								 Constants.CREDIT_REVERSAL_2
							 ))) {

							transactionDetail.setReversal(true);
							logger.debug(
								"setPosConditionAndDoValidationChecks - Transaction is a VISA Reversal");
						}
					}
					else {

						// do atm pos validation

						if (StringUtils.isNotBlank(condition) &&
							(condition.equals(Constants.ATM) ||
							 condition.equals(Constants.BILATERAL_ATM) ||
							 condition.equals(Constants.BILATERAL_POS))) {

							if ((StringUtils.isNotBlank(
									transactionAttributes.getTrlTscId()) &&
								 transactionAttributes.getTrlTscId(
								 ).equals(
									 Constants.SALES_REVERSAL_22
								 )) ||
								transactionAttributes.getTrlTscId(
								).equals(
									Constants.CARDHOLDER_ACC_TRANSFER_40
								)) {

								//transactionDetail.setValidServiceRequest(false);
							}

							if (StringUtils.isNotBlank(
									transactionDetail.getRequestDetails(
									).getIssuerId()) &&
								transactionDetail.getRequestDetails(
								).getIssuerId(
								).equals(
									Constants.AMP_ISSUER
								) &&
								StringUtils.isNotBlank(
									transactionDetail.getRequestDetails(
									).getAcquirerId()) &&
								transactionDetail.getRequestDetails(
								).getAcquirerId(
								).equals(
									Constants.MASTERCARD_ACQ
								)) {

								//transactionDetail.setValidServiceRequest(false);
								logger.debug(
									"setPosConditionAndDoValidationChecks - Transaction is for an international card holder IssuerId=" +
										transactionDetail.getRequestDetails(
										).getIssuerId());
							}

							if (condition.equals(Constants.BILATERAL_POS)) {
								if (transactionAttributes.getTrlTscId(
									).equals(
										Constants.RETURNS_20
									)) {

									//transactionDetail.setValidServiceRequest(false);
								}
							}
							else {
								if (transactionAttributes.getTrlTscId(
									).equals(
										Constants.PURCHASE_0
									)) {

									//transactionDetail.setValidServiceRequest(false);
								}
							}
						}
						else {
							//transactionDetail.setValidServiceRequest(false);
							logger.debug(
								"setPosConditionAndDoValidationChecks - Transaction is not a valid ATM/POS");
						}
					}
				}
			}

			logger.debug("setPosConditionAndDoValidationChecks - end");
		}
	}

	/**
	 *
	 * @param searchHeader
	 * @param reasonType
	 * @return
	 */
	//TODO: Remove this.
	/*private HashMap<Long, String> getAttributeMapForReasonTypeWithId(
			SearchHeader searchHeader, long reasonType) {

		List<AttributesListType> attributeList = getAttributeList(reasonType,
				searchHeader);
		HashMap<Long, String> reasonMap = new HashMap<>();

		for (AttributesListType attributes : attributeList) {
			String temp = attributes.getValue() + " - "
					+ attributes.getDescription();

			reasonMap.put(attributes.getId(), temp);
		}

		return reasonMap;
	}*/

	private void setPosConditionAndDoValidationChecks(
		TransactionDetail transactionDetail,
		GetTransactionDetailsResponse response, XMLGregorianCalendar busDate) {

		boolean isMasterCardTransaction = false;
		RawTransactionAttributesType transactionAttributes =
			response.getRawTransactionAttributes();
		logger.debug(
			"transactionAttributes.transactionAttributes: " +
				transactionAttributes);

		if (null != transactionAttributes) {
			String originalICHName =
				transactionAttributes.getTrlOriginIchName();

			logger.debug(
				"transactionAttributes.getTrlOriginIchName(): " +
					originalICHName);

			if (Constants.MASTERCARD_CIS.equalsIgnoreCase(originalICHName) ||
				Constants.MASTERCARD_IPM_TransactionBatch.equalsIgnoreCase(
					originalICHName) ||
				Constants.MASTERCARD_CMS_TransactionBatch.equalsIgnoreCase(
					originalICHName)) {

				isMasterCardTransaction = true;
			}
		}

		logger.debug("isMasterCardTransaction: " + isMasterCardTransaction);

		if (isMasterCardTransaction) {
			setMasterCardPosConditionAndDoValidationChecks(
				transactionDetail, response);
		}
		else {
			setVisaPosConditionAndDoValidationChecks(
				transactionDetail, response, busDate);
		}
	}

	/**
	 *
	 * @param requestType
	 * @param form
	 */
	private void setReasonForAtmPosServiceRequest(
		RequestTypeType requestType, ServiceRequestForm form) {

		if (form.getTransactionInformation(
			).getAtmPos(
			).contains(
				"POS"
			)) {

			requestType.setTypeId(
				Long.valueOf(
					transactionAppProperties.getTransProps(
					).getProperty(
						Constants.POS_TYPE_ID
					)));
			requestType.setReason(
				Long.valueOf(
					form.getAtmPosClaimInformation(
					).getRequestPosReason()));
		}
		else {
			requestType.setTypeId(
				Long.valueOf(
					transactionAppProperties.getTransProps(
					).getProperty(
						Constants.ATM_TYPE_ID
					)));
			requestType.setReason(
				Long.valueOf(
					form.getAtmPosClaimInformation(
					).getRequestAtmReason()));
		}
	}

	/**
	 *
	 * @param atmPosClaim
	 * @param transaction
	 * @return
	 */
	private RequestTypeType setupAtmPosClaimInformationForServiceRequest(
		RequestAtmPosClaimInformation atmPosClaim,
		TransactionType transaction) {

		RequestTypeType requestType = new RequestTypeType();

		requestType.setProductId(
			Long.valueOf(
				transactionAppProperties.getTransProps(
				).getProperty(
					Constants.PRODUCT_ID
				)));

		try {
			if (null != atmPosClaim.getRequestAtmFee()) {
				BigDecimal atmFee = Utility.createBigDecimalAmountFromString(
					atmPosClaim.getRequestAtmFee());

				transaction.setAtmFee(atmFee);
			}
		}
		catch (NumberFormatException e) {
			logger.error(
				"Error parsing Atm Fee for ATM POS claim dispute " +
					atmPosClaim.getRequestAtmFee(),
				e);
		}

		try {
			if (null != atmPosClaim.getRequestAmountReceived()) {
				BigDecimal atmReceived =
					Utility.createBigDecimalAmountFromString(
						atmPosClaim.getRequestAmountReceived());

				transaction.setAmtReceived(atmReceived);
			}
		}
		catch (NumberFormatException e) {
			logger.error(
				"Error parsing Atm Received for ATM POS claim dispute " +
					atmPosClaim.getRequestAmountReceived(),
				e);
		}

		try {
			if (null != atmPosClaim.getRequestClaimAmount()) {
				BigDecimal claimAmt = Utility.createBigDecimalAmountFromString(
					atmPosClaim.getRequestClaimAmount());

				transaction.setClaimAmt(claimAmt);
			}
		}
		catch (NumberFormatException e) {
			logger.error(
				"Error parsing Claim Amt for ATM POS claim dispute " +
					atmPosClaim.getRequestClaimAmount(),
				e);
		}

		requestType.setDisputeTypeId(
			Long.valueOf(atmPosClaim.getRequestDisputeType()));
		transaction.setComments(atmPosClaim.getRequestComments());

		return requestType;
	}

	private void setupChargebacksPilotOrgList() {
		String orgString = transactionAppProperties.getTransProps(
		).getProperty(
			"chargeback.pilot.orgs"
		);

		if (StringUtils.isNotBlank(orgString)) {
			List<String> tempList = new ArrayList<>();
			String[] orgs = orgString.split(",");

			for (String org : orgs) {
				tempList.add(org);
			}

			setChargebackPilotOrgs(tempList);
		}
	}

	/**
	 * Create the ServiceRequestRequestType Object to add to the database.
	 *
	 * @param 	form	<code>ServiceRequestForm</code>
	 * @return	ServiceRequestRequestType
	 */
	private ServiceRequestRequestType setupServiceRequestForTransaction(
			ServiceRequestForm form)
		throws Exception {

		ServiceRequestRequestType request = null;

		if (null != form) {
			request = new ServiceRequestRequestType();

			//Contact Information section

			if (null != form.getContactInformation()) {
				ContactType contact = new ContactType();

				contact.setFirstName(
					form.getContactInformation(
					).getGivenName());
				contact.setSurname(
					form.getContactInformation(
					).getSurname());
				contact.setEmail(
					form.getContactInformation(
					).getEmail());
				contact.setPhoneNo(
					form.getContactInformation(
					).getPhoneNumber());
				contact.setOrganisation(
					form.getContactInformation(
					).getOrganisation());

				request.setContact(contact);
			}

			//Transaction Details section

			if (null != form.getTransactionInformation()) {
				RequestTransactionInformation trans =
					form.getTransactionInformation();

				TransactionType transaction = setupTransactionForServiceRequest(
					trans);

				transaction = setupMemberInformationForTransaction(
					form, transaction);

				request.setTransaction(transaction);

				//VISA Request section.

				if (null != form.getVisaTransactionInformation()) {
					RequestAdditionalTransactionInformation visaTrans =
						form.getVisaTransactionInformation();

					RequestTypeType requestType =
						setupVisaTransactionInformationForServiceRequest(
							visaTrans, transaction);

					request.setRequestType(requestType);

					// ATM/POS Setup

				}
				else if (null != form.getAtmPosClaimInformation()) {
					RequestAtmPosClaimInformation atmPosClaim =
						form.getAtmPosClaimInformation();

					RequestTypeType requestType =
						setupAtmPosClaimInformationForServiceRequest(
							atmPosClaim, transaction);

					setReasonForAtmPosServiceRequest(requestType, form);
					request.setRequestType(requestType);

					List<MultipartFile> attachments =
						atmPosClaim.getRequestAttachments();

					if ((null != attachments) && (attachments.size() > 0)) {
						List<AttachmentDataType> attachmentTypes =
							new ArrayList<>();

						for (MultipartFile multipartFile : attachments) {
							if (multipartFile.getSize() > 0) {
								AttachmentDataType attachmentType =
									new AttachmentDataType();

								attachmentType.setFilename(
									multipartFile.getOriginalFilename());
								attachmentType.setMimeType(
									multipartFile.getContentType());

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

						request.getAttachments(
						).addAll(
							attachmentTypes
						);
					}
				}
			}
			else {
				logger.error(
					"setupServiceRequestForTransaction - ServiceRequestForm is null");
			}
		}

		return request;
	}

	/**
	 * Setup the transaction information for the Service Request.
	 *
	 * @param	trans	<em>RequestTreansctionInformation</em>
	 * @return	TransctionType
	 */
	private TransactionType setupTransactionForServiceRequest(
			RequestTransactionInformation trans)
		throws Exception {

		TransactionType transaction = new TransactionType();

		transaction.setAcquirerName(trans.getAcquirerName());
		transaction.setIssuerName(trans.getIssuerName());
		transaction.setMaskedPan(trans.getMaskedPan());
		transaction.setPosCondition(trans.getAtmPos());

		transaction.setAcquirerId(trans.getAcquirerId());

		try {
			transaction.setTrlId(Long.valueOf(trans.getCuscalTransactionId()));
		}
		catch (NumberFormatException nfe) {
			logger.error(
				"Error parsing TrlId cannot be parsed. TrlId is " +
					trans.getCuscalTransactionId());

			throw new Exception(nfe);
		}

		try {
			transaction.setIssuerId(Long.valueOf(trans.getIssuerId()));
		}
		catch (NumberFormatException nfe) {
			logger.error(
				"Error parsing IssuerId cannot be parsed. IssuerId is " +
					trans.getCuscalTransactionId());

			throw new Exception(nfe);
		}

		if (StringUtils.isNotBlank(trans.getStan())) {
			String stanString = trans.getStan();

			try {
				if (stanString.contains("/")) {
					String stan = stanString.substring(
						0, stanString.indexOf("/")
					).trim();

					transaction.setStan(Long.valueOf(stan));

					String advStan = stanString.substring(
						stanString.indexOf("/") + 1
					).trim();

					transaction.setAdvStan(advStan);
				}
				else {
					transaction.setStan(Long.valueOf(stanString));
				}
			}
			catch (NumberFormatException nfe) {
				logger.error(
					"Error parsing Stan cannot be parsed. Stan is " +
						stanString);

				throw new Exception(nfe);
			}
		}

		if (StringUtils.isNotBlank(trans.getArn()) &&
			!"undefined".equals(trans.getArn())) {

			transaction.setArn(trans.getArn());
		}
		else {
			transaction.setArn("");
		}

		transaction.setTerminalId(trans.getTerminalId());
		transaction.setVisaId(trans.getVisaTransactionId());

		logger.debug(
			"setupServiceRequestForTransaction - cardHolderAmount is: " +
				trans.getCardholderAmount());

		String cardHolderAmount = Utility.removeAndCovertInFloat(
			trans.getCardholderAmount());

		BigDecimal cardAmount = Utility.createBigDecimalAmountFromString(
			cardHolderAmount);

		if (null != cardAmount) {
			logger.debug(
				"setupServiceRequestForTransaction - getCardholderAmount: " +
					cardAmount);
			transaction.setBillingAmt(cardAmount);
			transaction.setBillingCcy(trans.getCardholderAmountCurrency());
		}

		logger.debug(
			"setupServiceRequestForTransaction - transactionAmount is: " +
				trans.getTransactionAmount());
		String transactionAmount = Utility.removeAndCovertInFloat(
			trans.getTransactionAmount());

		BigDecimal transAmount = Utility.createBigDecimalAmountFromString(
			transactionAmount);

		if (null != transAmount) {
			transaction.setTransactionAmt(transAmount);
			transaction.setTransactionCcy(trans.getTransactionAmountCurrency());
		}

		XMLGregorianCalendar localXmlCal =
			Utility.createXmlGregorianCalendarFromString(
				trans.getLocationDate(), "dd/MM/yyyy", Boolean.TRUE);

		transaction.setTrlLocalDate(localXmlCal);

		XMLGregorianCalendar businessXmlCal =
			Utility.createXmlGregorianCalendarFromString(
				trans.getBusinessDate(), "yyyyMMdd", Boolean.FALSE);

		transaction.setTrlBusinessDate(businessXmlCal);

		return transaction;
	}

	/**
	 * Setup the Visa Transaction Information for the Service Request.
	 *
	 * @param 	visaTrans	<em>RequestVisaTransactionInformation</em>
	 * @param transaction
	 * @return	RequestTypeType
	 */
	private RequestTypeType setupVisaTransactionInformationForServiceRequest(
		RequestAdditionalTransactionInformation visaTrans,
		TransactionType transaction) {

		RequestTypeType requestType = new RequestTypeType();

		if (StringUtils.isNotBlank(visaTrans.getRequestType())) {
			if (StringUtils.equalsIgnoreCase("2", visaTrans.getRequestType())) {
				requestType.setTypeDescription("TC52");
				requestType.setReason(
					Long.valueOf(visaTrans.getVoucherReason()));
				requestType.setProductId(
					Long.valueOf(
						transactionAppProperties.getTransProps(
						).getProperty(
							Constants.PRODUCT_ID
						)));
				requestType.setTypeId(
					Long.valueOf(
						transactionAppProperties.getTransProps(
						).getProperty(
							Constants.TC52_TYPE_ID
						)));
			}
			else {
				requestType.setTypeDescription("TC40");
				requestType.setFraudType(
					Long.valueOf(visaTrans.getFraudType()));
				requestType.setNotificationCode(
					Long.valueOf(visaTrans.getFraudNotificationCode()));
				requestType.setDetection(
					Long.valueOf(visaTrans.getDetection()));

				requestType.setProductId(
					Long.valueOf(
						transactionAppProperties.getTransProps(
						).getProperty(
							Constants.PRODUCT_ID
						)));

				requestType.setTypeId(
					Long.valueOf(
						transactionAppProperties.getTransProps(
						).getProperty(
							Constants.TC40_TYPE_ID
						)));
			}

			if (StringUtils.isNotBlank(visaTrans.getComments())) {
				transaction.setComments(visaTrans.getComments());
			}
		}

		return requestType;
	}

	/**
	 *
	 * @param link
	 * @param transDetails
	 */
	private void setVisaChargeBackParameters(
		PortletURL link, TransactionDetail transDetails) {

		logger.debug("setVisaChargeBackParameters - start");

		link.setParameter(
			Constants.TX_RENDER, Constants.CHARGEBACK_REDIRECT_RENDER);
		link.setParameter(Constants.TX_ID, transDetails.getTransactionId());
		link.setParameter(Constants.BUS_DATE, transDetails.getBusDate());

		logger.debug("setVisaChargeBackParameters - end");
	}

	/**
	 *
	 * @param transactionDetail
	 * @param response
	 * @param busDate
	 */
	private void setVisaPosConditionAndDoValidationChecks(
		TransactionDetail transactionDetail,
		GetTransactionDetailsResponse response, XMLGregorianCalendar busDate) {

		logger.debug("setPosConditionAndDoValidationChecks - start");

		//setup default values
		transactionDetail.setValidServiceRequest(true);
		transactionDetail.setVisaAtm(false);
		transactionDetail.setReversal(false);

		//Cardholder has member information.
		RawTransactionAttributesType transactionAttributes =
			response.getRawTransactionAttributes();

		if (null != transactionAttributes) {

			//Prevent the user from seeing the screen at all and show ineligible message

			//is valid message type

			if (StringUtils.isNotBlank(transactionDetail.getMessageType()) &&
				!transactionDetail.getMessageType(
				).startsWith(
					Constants.MESSAGE_TYPE_2
				)) {

				transactionDetail.setValidServiceRequest(false);
				logger.debug(
					"setPosConditionAndDoValidationChecks - Transaction is not 02XX message type");
			}

			//is a declined transaction

			if (StringUtils.isNotBlank(
					transactionAttributes.getTrlActionResponseCode())) {

				Integer respCode = Integer.parseInt(
					transactionAttributes.getTrlActionResponseCode());

				if (respCode.compareTo(Constants.RESP_CODE_100) >= 0) {
					transactionDetail.setValidServiceRequest(false);
					logger.debug(
						"setPosConditionAndDoValidationChecks - Transaction is declined");
				}
			}
			else {
				transactionDetail.setValidServiceRequest(false);
			}

			// The full set of rules will be implemented in next release

			//			if (StringUtils.isNotBlank(transactionAttributes.getTrlTscId()) &&
			//					!(transactionAttributes.getTrlTscId().equals(Constants.PURCHASE_0) ||
			//					 transactionAttributes.getTrlTscId().equals(Constants.WITHDRAWAL_1) ||
			//					 transactionAttributes.getTrlTscId().equals(Constants.REVERSAL_22) || //add 02
			//					 transactionAttributes.getTrlTscId().equals(Constants.PURCHASE_CASHOUT_9))){
			//				transactionDetail.setValidServiceRequest(false);
			//				logger.debug("setPosConditionAndDoValidationChecks - Transaction is not a valid transaction type");
			//			}

			if (StringUtils.isNotBlank(transactionAttributes.getTrlTscId()) &&
				(transactionAttributes.getTrlTscId(
				).equals(
					Constants.AVAILABLE_FUNDS_ENQUIRY_30
				) ||
				 transactionAttributes.getTrlTscId(
				 ).equals(
					 Constants.BALANCE_INQUIRY_31
				 ))) {

				transactionDetail.setValidServiceRequest(false);
				logger.debug(
					"setPosConditionAndDoValidationChecks - Transaction is not a valid transaction type");
			}

			POSConditionType posCondition = response.getPosCondition();

			if (null != posCondition) {
				String condition = posCondition.getPosCondition();

				transactionDetail.setVisaOrAtmPos(condition);
				logger.debug(
					"setPosConditionAndDoValidationChecks - POS Condition: " +
						condition);

				//Do visa validation
				//moved this check before POS condition check for Visa as sometimes TRL_CHANNEL is null for Visa transactions.

				if (StringUtils.isNotBlank(
						transactionAttributes.getTrlOriginIchName())) {

					//if Visa base 1 then ineligible

					if (transactionAttributes.getTrlOriginIchName(
						).equals(
							Constants.VISA_BASE_I
						)) {

						transactionDetail.setValidServiceRequest(false);
						logger.debug(
							"setPosConditionAndDoValidationChecks - Transaction is VISA Base 1");
					}
					else if (transactionAttributes.getTrlOriginIchName(
							).equals(
								Constants.VISA_BASE_2_CTF_TRANSACTIONBATCH
							)) {

						//valid Visa Transaction
						logger.debug(
							"setPosConditionAndDoValidationChecks - Transaction is VISA Base 2");

						String visaType = posCondition.getVisaType();

						if (StringUtils.isNotBlank(visaType)) {
							transactionDetail.setVisaType(visaType);
							logger.debug(
								"setPosConditionAndDoValidationChecks - Visa Type " +
									visaType);
						}
						else {
							transactionDetail.setVisaType(
								Constants.VISA_BASE_II);
							logger.debug(
								"setPosConditionAndDoValidationChecks - Visa Type VISA_BASE_II");
						}

						//Set to Visa if its blank

						if (StringUtils.isBlank(condition)) {
							transactionDetail.setVisaOrAtmPos("VISA");
						}

						//if Visa ATM : stop TC52 submission - display "A Voucher Request cannot be submitted for an ATM transaction"

						if (StringUtils.isNotBlank(
								transactionAttributes.getTrlMccId()) &&
							Constants.VISA_ATM_6011.equals(
								transactionAttributes.getTrlMccId())) {

							transactionDetail.setVisaAtm(true);
							logger.debug(
								"setPosConditionAndDoValidationChecks - Transaction is a VISA ATM");
						}

						//if Visa reversal : stop TC52 submission - display "A Voucher Request cannot be submitted for an ATM transaction"

						if (StringUtils.isNotBlank(
								transactionAttributes.getTrlTscId()) &&
							(transactionAttributes.getTrlTscId(
							).equals(
								Constants.SALES_REVERSAL_22
							) ||
							 transactionAttributes.getTrlTscId(
							 ).equals(
								 Constants.CREDIT_REVERSAL_2
							 ))) {

							transactionDetail.setReversal(true);
							logger.debug(
								"setPosConditionAndDoValidationChecks - Transaction is a VISA Reversal");
						}
					}
					else {

						// do atm pos validation

						if (StringUtils.isNotBlank(condition) &&
							(condition.equals(Constants.ATM) ||
							 condition.equals(Constants.BILATERAL_ATM) ||
							 condition.equals(Constants.BILATERAL_POS))) {

							if ((StringUtils.isNotBlank(
									transactionAttributes.getTrlTscId()) &&
								 transactionAttributes.getTrlTscId(
								 ).equals(
									 Constants.SALES_REVERSAL_22
								 )) ||
								transactionAttributes.getTrlTscId(
								).equals(
									Constants.CARDHOLDER_ACC_TRANSFER_40
								)) {

								transactionDetail.setValidServiceRequest(false);
							}

							if (StringUtils.isNotBlank(
									transactionDetail.getRequestDetails(
									).getIssuerId()) &&
								transactionDetail.getRequestDetails(
								).getIssuerId(
								).equals(
									Constants.AMP_ISSUER
								) &&
								StringUtils.isNotBlank(
									transactionDetail.getRequestDetails(
									).getAcquirerId()) &&
								transactionDetail.getRequestDetails(
								).getAcquirerId(
								).equals(
									Constants.MASTERCARD_ACQ
								)) {

								transactionDetail.setValidServiceRequest(false);
								logger.debug(
									"setPosConditionAndDoValidationChecks - Transaction is for an international card holder");
							}

							if (condition.equals(Constants.BILATERAL_POS)) {
								if (transactionAttributes.getTrlTscId(
									).equals(
										Constants.RETURNS_20
									)) {

									transactionDetail.setValidServiceRequest(
										false);
								}
							}
							else {
								if (transactionAttributes.getTrlTscId(
									).equals(
										Constants.PURCHASE_0
									)) {

									transactionDetail.setValidServiceRequest(
										false);
								}
							}
						}
						else {
							transactionDetail.setValidServiceRequest(false);
							logger.debug(
								"setPosConditionAndDoValidationChecks - Transaction is not a valid ATM/POS");
						}
					}
				}
			}

			logger.debug("setPosConditionAndDoValidationChecks - end");
		}
	}

	/**
	 *
	 * @param request
	 * @param portletNameMap
	 * @param reasonData
	 * @param transactionDetails
	 */
	private void updateDestinationWithPortletLinkAndParameters(
		PortletRequest request, HashMap<Long, String> portletNameMap,
		ReasonData reasonData, TransactionDetail transactionDetails) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		for (long i = 1; i <= portletNameMap.size(); i++) {
			String portletName = portletNameMap.get(i);

			PortletURL link = PortletURLFactoryUtil.create(
				PortalUtil.getHttpServletRequest(request), portletName,
				themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

			if (reasonData.getVisaType(
				).get(
					i
				).equals(
					Constants.VISA_CHARGEBACK
				)) {

				setVisaChargeBackParameters(link, transactionDetails);
			}
			else if (reasonData.getVisaType(
					).get(
						i
					).equalsIgnoreCase(
						Constants.MASTERCARD_DISPUTE
					)) {

				setMasterCardDisputeParameters(link, transactionDetails);
			}

			String visaPage = reasonData.getVisaDestination(
			).get(
				i
			);
			String eftConnectURI = transactionAppProperties.getTransProps(
			).getProperty(
				Constants.EFT_CONNECT_URI
			);

			String destination = link.toString(
			).replace(
				eftConnectURI, visaPage
			);

			reasonData.getVisaDestination(
			).put(
				i, destination
			);
		}
	}

	private static final Auditor audit = Auditor.getInstance();

	/**
	 * Map<String, Set<Codes>> object
	 */
	private static Map<String, Set<Codes>> cacheMessageCodesMap = null;

	private static List<String> chargebackPilotOrgs = null;

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(
		TransactionSearchServiceImpl.class);

	/**
	 * Logger object for performance logging
	 */
	private static Logger perfLogger_ = LoggerFactory.getLogger("perfLogging");

	/**
	 * TransactionService object
	 */
	@Autowired
	@Qualifier("commonService")
	private CommonService commonService;

	/**
	 * PctUserSearchServiceImpl object
	 */
	@Autowired
	@Qualifier("pctUserSearchService")
	private PctUserSearchServiceImpl pctSearchService;

	@Autowired
	@Qualifier("selfServiceProperties")
	private SelfServiceProperties selfServiceProperties;

	/**
	 * TransactionAppProperties object
	 */
	@Autowired
	@Qualifier("transactionAppProperties")
	private TransactionAppProperties transactionAppProperties;

	/**
	 * TransactionService object
	 */
	@Autowired
	@Qualifier("transactionService")
	private TransactionService transactionService;

}