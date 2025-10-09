package au.com.cuscal.transactions.services.client;

import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.framework.webservices.client.CustomerSelfServiceService;
import au.com.cuscal.framework.webservices.client.impl.AbstractCuscalWebServices;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesListRequest;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesListResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTransactionDetailsRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTransactionDetailsResponse;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdRequestType;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdResponseType;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsRequestType;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsResponseType;
import au.com.cuscal.framework.webservices.transaction.GetAs2805ResponseCodesRequestType;
import au.com.cuscal.framework.webservices.transaction.GetAs2805ResponseCodesResponseType;
import au.com.cuscal.framework.webservices.transaction.GetMessageCodesRequestType;
import au.com.cuscal.framework.webservices.transaction.GetMessageCodesResponseType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionRequestType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionResponseType;
import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.commons.TransactionAppProperties;

import java.util.HashMap;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * despite being autowired into the TransactionSearchService class, and having
 * autowired on the url properties, this class needs to be configured in the
 * transaction-portlet.xml as a bean to obtain the url configuration.
 * @author dchong
 *
 */
@Component("transactionService")
public class TransactionServiceImpl extends AbstractCuscalWebServices
	implements TransactionService {

	@Override
	public FindTransactionsResponseType findTransactions(
		FindTransactionsRequestType findTransactionsRequestType) {

		return _transactionService.findTransactions(findTransactionsRequestType);
	}

	@Override
	public GetTransactionResponseType getTransaction(
		GetTransactionRequestType getTransactionRequestType) {

		return _transactionService.getTransaction(getTransactionRequestType);
	}

	@Override
	public GetMessageCodesResponseType getMessageCodes(
		GetMessageCodesRequestType getMessageCodesRequestType) {

		return _transactionService.getMessageCodes(getMessageCodesRequestType);
	}

	@Override
	public GetAs2805ResponseCodesResponseType getAs2805ResponseCodes(
		GetAs2805ResponseCodesRequestType getAs2805ResponseCodesRequestType) {

		return _transactionService.getAs2805ResponseCodes(getAs2805ResponseCodesRequestType);
	}

	@Override
	public FindEntitiesByAttributeWithIdResponseType findEntitiesByAttributeWithId(
		FindEntitiesByAttributeWithIdRequestType findEntitiesByAttributeWithIdRequestType) {

		return _transactionService.findEntitiesByAttributeWithId(
			findEntitiesByAttributeWithIdRequestType);
	}

	/**
	 *
	 */
	public AddServiceRequestResponse addServiceRequest(
		AddServiceRequestRequest addServiceRequestRequest) {

		logger.debug("addServiceRequest - start");

		AddServiceRequestResponse response = null;

		if (null != addServiceRequestRequest) {
			response = ticketingService.addServiceRequest(
				addServiceRequestRequest);
		}

		logger.debug("addServiceRequest - end");

		return response;
	}

	/**
	 *
	 */
	public FindServiceRequestResponse findServiceRequest(
		FindServiceRequestRequest findServiceRequestRequest) {

		logger.debug("findServiceRequest - start");

		FindServiceRequestResponse response = null;

		if (null != findServiceRequestRequest) {
			response = ticketingService.findServiceRequest(
				findServiceRequestRequest);
		}

		logger.debug("findServiceRequest - end");

		return response;
	}

	/**
	 *
	 */
	public FindServiceRequestResponse fisdServiceRequest(
		FindServiceRequestRequest findServiceRequestRequest) {

		logger.debug("findServiceRequest - start");

		FindServiceRequestResponse response = null;

		if (null != findServiceRequestRequest) {
		}

		logger.debug("findServiceRequest - end");

		return response;
	}

	/**
	 * create the map for Account identification properties map.
	 *
	 * @return HashMap< String, String>
	 */
	public HashMap<String, String> getAccountIdentificationProps() {
		HashMap<String, String> hashMap = new HashMap<>();

		for (int i = 0; i < 10; i++) {
			String propKey = "account.identi." + i;

			hashMap.put(
				"" + i,
				transactionAppProperties.getTransProps(
				).getProperty(
					propKey
				));
		}

		return hashMap;
	}

	/**
	 *
	 */
	public GetAttributesListResponse getAttributeList(
		GetAttributesListRequest getAttributesListRequest) {

		logger.debug("getAttributeList - start");

		GetAttributesListResponse response = null;

		if (null != getAttributesListRequest) {
			response = ticketingService.getAttributesList(
				getAttributesListRequest);
		}

		logger.debug("getAttributeList - end");

		return response;
	}

	//	private TicketingService ticketingService;

	/**
	 * create the map for message code properties map.
	 * @return HashMap< String, String>
	 */
	public HashMap<String, String> getMessageCodeProps() {
		HashMap<String, String> hashMap = new HashMap<>();

		hashMap.put(
			Constants.AUTHORISATION,
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.AUTHORISATION
			));
		hashMap.put(
			Constants.FINANCIAL,
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.FINANCIAL
			));
		hashMap.put(
			Constants.REVERSAL,
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.REVERSAL
			));

		return hashMap;
	}

	/**
	 *
	 */
	public GetTicketResponse getTicket(GetTicketRequest getTicketRequest) {
		logger.debug("getTicket - start");

		GetTicketResponse response = null;

		if (null != getTicketRequest) {
			TktRequestHeader header = getTicketRequest.getHeader();
			logger.debug(
				"---------------------------Get Ticket start------------------------");
			logger.debug("getTicket - header userId: " + header.getUserId());
			logger.debug(
				"getTicket - header userOrgShortName: " +
					header.getUserOrgId());
			logger.debug(
				"getTicket - header userOrgName: " + header.getUserOrgName());
			logger.debug(
				"getTicket - request ticketId: " +
					getTicketRequest.getTicketId());
			logger.debug(
				"---------------------------Get Ticket end------------------------");

			logger.debug("getTicket - Calling the service now");
			response = ticketingService.getServiceRequest(getTicketRequest);
			logger.debug("getTicket - response from service: " + response);
		}

		logger.debug("getTicket - end");

		return response;
	}

	public CustomerSelfServiceService getTicketingService() {
		return ticketingService;
	}

	/**
	 *
	 */
	public GetTransactionDetailsResponse getTransactionExtraDetails(
		GetTransactionDetailsRequest arnAndCardholderRequest) {

		logger.debug("getVisaTransactionArn - start");

		GetTransactionDetailsResponse response = null;

		if (null != arnAndCardholderRequest) {
			TktRequestHeader header = arnAndCardholderRequest.getHeader();
			logger.debug(
				"---------------------------Get ARN start------------------------");
			logger.debug(
				"getVisaTransactionArn - header userId: " + header.getUserId());
			logger.debug(
				"getVisaTransactionArn - header userOrgShortName: " +
					header.getUserOrgId());
			logger.debug(
				"getVisaTransactionArn - header userOrgName: " +
					header.getUserOrgName());
			logger.debug(
				"---------------------------Get ARN end------------------------");

			logger.debug("getVisaTransactionArn - Calling the service now");
			response = ticketingService.getTransactionDetails(
				arnAndCardholderRequest);
			logger.debug(
				"getVisaTransactionArn - response from service: " + response);
		}

		logger.debug("getVisaTransactionArn - end");

		return response;
	}

	/**
	 * create the map for Web service Status code properties map.
	 * @return HashMap< String, String>
	 */
	public HashMap<String, String> getWebServStatusCodeProps() {
		HashMap<String, String> hashMap = new HashMap<>();

		hashMap.put(
			Constants.STATUS_CODE_INCOMPLETE,
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.STATUS_CODSALARIC_PROPSKEY
			));
		hashMap.put(
			Constants.STATUS_CODE_SYSTEM_ERROR,
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.STATUS_SYSTEM_ERROR_PROPSKEY
			));
		hashMap.put(
			Constants.STATUS_CODE_SYSTEM_ERROR_0,
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.STATUS_ERROR_PROPSKEY
			));
		hashMap.put(
			Constants.STATUS_CODE_SYSTEM_ERROR_1,
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.STATUS_CODS_PROPSKEY
			));
		hashMap.put(
			Constants.STATUS_CODE_SYSTEM_ERROR_2,
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.STATUS_ALARIC_PROPSKEY
			));
		hashMap.put(
			Constants.STATUS_CODE_VALIDATION,
			transactionAppProperties.getTransProps(
			).getProperty(
				Constants.STATUS_VALIDATION_PROPSKEY
			));

		return hashMap;
	}

	@Autowired
	@Value("${webservice.cards.url}")
	protected String cardServicesUrl = null;

	@Autowired
	@Value("${webservice.common.url}")
	protected String commonServicesUrl = null;

	@Autowired
	@Value("${webservice.entity.url}")
	protected String entityServicesUrl = null;

	@Autowired
	@Value("${webservice.transaction.url}")
	protected String transactionServicesUrl = null;

	/**
	 * Logger object
	 */
	private static Logger logger = Logger.getLogger(
		TransactionServiceImpl.class);


	@Autowired
	@Qualifier("transactionAppProperties")
	private TransactionAppProperties transactionAppProperties;

	private final CustomerSelfServiceService ticketingService = CuscalServiceLocator.getService(
			CustomerSelfServiceService.class.getName());

	private final au.com.cuscal.framework.webservices.client.TransactionService _transactionService =
		CuscalServiceLocator.getService(
			au.com.cuscal.framework.webservices.client.TransactionService.class.getName());

}