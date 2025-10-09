package au.com.cuscal.transactions.services;

import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.common.framework.memory.PermGenUtil;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.category.AuditCategories;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.webservices.client.TransactionArchiveService;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdRequestType;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdResponseType;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsArchiveRequestType;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsArchiveResponseType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionArchiveDetailsRequestType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionArchiveDetailsResponseType;
import au.com.cuscal.framework.webservices.transaction.PanOrBinEnum;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;
import au.com.cuscal.framework.webservices.transaction.TransactionArchiveBasicInformation;
import au.com.cuscal.framework.webservices.transaction.TransactionArchiveDetailsInformation;
import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.commons.Utility;
import au.com.cuscal.transactions.domain.RequestDetails;
import au.com.cuscal.transactions.domain.ResponseDetails;
import au.com.cuscal.transactions.domain.TransactionDetail;
import au.com.cuscal.transactions.domain.TransactionList;
import au.com.cuscal.transactions.forms.TransactionForm;
import au.com.cuscal.transactions.services.client.TransactionService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Rajni Bharara
 *
 */
@Service(Constants.CUD_TRANSACTION_SERVICE)
public class CudTransactionSearchServiceImpl
	implements CudTransactionSearchService {

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

		logger.debug("createDateAsGregorianCalendarType - start");
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
			"END DATE in createDateAsGregorianCalendarType  in gc is " +
				gc.getTime());
		logger.debug("Calender is  gc.getTime     " + gc.getTime());
		XMLGregorianCalendar gregoriandate =
			datatypeFactory.newXMLGregorianCalendar(gc);

		logger.debug(
			"Calender is  XMLGregorianCalendar  FINAL  " +
				gregoriandate.toString());
		logger.debug(
			"END DATE in createDateAsGregorianCalendarType method is  " +
				gregoriandate.toString());
		logger.debug("createDateAsGregorianCalendarType - end");

		return gregoriandate;
	}

	/**
	 * Create  auditing params for  Cud transaction  details
	 *
	 * @param TransactionDetail
	 * @param int
	 * @return String
	 */
	public String createSearchParamsForCudTransactionDetails(
		TransactionDetail transactionDetail, int sizeOfListFound,
		String statusCode) {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<audit><parameters>");

		stringBuilder.append(
			"<cudTransactionId>" + transactionDetail.getTransactionId() +
				"</transactionId>");
		stringBuilder.append(
			"<eTLProcessDate>" + transactionDetail.getEtlProcessEndDate() +
				"</eTLProcessDate>");
		stringBuilder.append(
			"<adPanK>" + transactionDetail.getAdPank() + "</adPanK>");
		stringBuilder.append(
			"<adTranscationDetailsK>" +
				transactionDetail.getAdTransactionDetailK() +
					"</adTranscationDetailsK>");

		stringBuilder.append(
			"</parameters><result><found>" + sizeOfListFound +
				"</found><statusCode>" + statusCode +
					"</statusCode></result></audit>");

		return stringBuilder.toString();
	}

	/**
	 *Create  auditing params for  Cud transaction  search
	 *
	 * @param transactionForm
	 * @param String
	 * @return String
	 */
	public String createSearchParamsStr(
		TransactionForm transactionForm, int sizeOfListFound,
		String statusCode) {

		logger.debug("createSearchParamsStr - start");
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

		stringBuilder.append(
			"</parameters><result><found>" + sizeOfListFound +
				"</found><statusCode>" + statusCode +
					"</statusCode></result></audit>");
		logger.debug("createSearchParamsStr - end");

		return stringBuilder.toString();
	}

	/**
	 * Create the transaction cud dates
	 *
	 * @param String
	 * @return String
	 */
	public String createTransactionDates(
		XMLGregorianCalendar transactionDate, String transactionTime) {

		logger.debug(
			"createTransactionDates - start, transactionDate=" +
				transactionDate);

		String cudTxnDate = null;

		if (transactionDate != null) {
			Calendar cal = Calendar.getInstance();

			cal.set(Calendar.YEAR, transactionDate.getYear());
			cal.set(Calendar.MONTH, transactionDate.getMonth() - 1);
			cal.set(Calendar.DAY_OF_MONTH, transactionDate.getDay());
			logger.debug(
				"createTransactionDates - transactionDate converted to Calendar=" +
					cal);

			cudTxnDate =
				Utility.formatDateToString(
					cal.getTime(), Constants.DATE_FORMAT) + " " +
						transactionTime;
		}

		logger.debug("createTransactionDates - end, returning " + cudTxnDate);

		return cudTxnDate;
	}

	/**
	 * Create the transaction description from transaction type and account type
	 *
	 * @param String
	 * @param String
	 * @return String
	 */
	public String createTransactionDescriptionString(
		String transactionType, String accounttype) {

		logger.debug("createTransactionDescriptionString - start");
		String cudTxnDescription = null;
		String inbetwenStr = null;

		if (txnDescriptionMap == null) {

			// initial map

			txnDescriptionMap = new HashMap<>();
			Enumeration e = config.keys();

			while (e.hasMoreElements()) {
				String key = (String)e.nextElement();

				if (key.startsWith(Constants.TXN_DESCRIPTION_CONFIG_PREFIX)) {
					String values = config.getProperty(key);
					String title = key.substring(
						Constants.TXN_DESCRIPTION_CONFIG_PREFIX.length());
					String[] valueArray = values.split(",");

					for (String value : valueArray) {
						txnDescriptionMap.put(value, title);
					}
				}
			}
		}

		if (txnDescriptionMap.containsKey(transactionType)) {
			inbetwenStr = txnDescriptionMap.get(transactionType);
		}
		else {
			inbetwenStr = config.getProperty(
				Constants.TXN_DESCRIPTION_CONFIG_DEFAULT);
		}

		cudTxnDescription =
			transactionType + " " + inbetwenStr + " " + accounttype;

		logger.debug(
			"createTransactionDescriptionString - end  cudTxnDescription is " +
				cudTxnDescription);

		return cudTxnDescription;
	}

	/**
	 * Create a new object of TransactionForm with default value .
	 *
	 * @return TransactionForm
	 */
	public TransactionForm createTransactionFormObject() {
		logger.debug("createTransactionFormObject - start");
		TransactionForm transactionForm = new TransactionForm();

		String[] startMinuteDisplay = config.getProperty(
			Constants.UI_START_MINS_PROPSKEY
		).split(
			Constants.COMMA
		);
		String[] startHourDisplay = config.getProperty(
			Constants.UI_START_HR_PROPSKEY
		).split(
			Constants.COMMA
		);
		String[] endHourDisplay = config.getProperty(
			Constants.UI_END_HR_PROPSKEY
		).split(
			Constants.COMMA
		);
		String[] endMinuteDisplay = config.getProperty(
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
				config.getProperty(Constants.CUD_SEARCH_MAX_DAYS)
			).intValue());
		logger.debug("createTransactionFormObject - end");

		return transactionForm;
	}

	/**
	 * Get Authorised By Description from properties files
	 *
	 * @param String
	 * @return String
	 */
	public String getAuthorisedByDescription(String authorisedByKey) {
		logger.debug("getAuthorisedByDescription - start");
		String authBy = null;
		String propertyKey =
			Constants.AUTH_PROPERTY_PREFIX_KEY + authorisedByKey.toLowerCase();

		if (config.containsKey(propertyKey)) {
			authBy = config.getProperty(propertyKey);
		}

		logger.debug("getAuthorisedByDescription - end");

		return authBy;
	}

	/**
	 * Get get org short name using liferay org id .
	 *
	 * @param String
	 * @return String
	 */
	public String getOrgShortName(String liferayOrgId) {
		logger.debug("getOrgShortName - start");
		String orgShortName = null;
		FindEntitiesByAttributeWithIdRequestType
			findEntitiesByAttributeWithIdRequest =
				new FindEntitiesByAttributeWithIdRequestType();
		findEntitiesByAttributeWithIdRequest.setAttributeValue(liferayOrgId);
		findEntitiesByAttributeWithIdRequest.setAttributeTypeId(4);

		FindEntitiesByAttributeWithIdResponseType
			findEntitiesByAttributeWithIdResponseType =
				transactionService.findEntitiesByAttributeWithId(
					findEntitiesByAttributeWithIdRequest);

		if (!findEntitiesByAttributeWithIdResponseType.getEntities(
			).isEmpty())
			orgShortName =
				findEntitiesByAttributeWithIdResponseType.getEntities(
				).get(
					0
				).getShortName();

		logger.debug("The organisation short name is " + orgShortName);
		logger.debug("getOrgShortName - end");

		return orgShortName;
	}

	/**
	 * Get Switched By Description from properties files
	 *
	 * @param String
	 * @return String
	 */
	public String getSwitchedByDescription(String source) {
		logger.debug("getSwitchedByDescription - start");
		String description = null;

		if (switchedByMap == null) {

			// initial map

			switchedByMap = new HashMap<>();
			Enumeration e = config.keys();

			while (e.hasMoreElements()) {
				String key = (String)e.nextElement();

				if (key.startsWith(Constants.SWITCHED_BY_CONFIG_PREFIX)) {
					String values = config.getProperty(key);
					String title = key.substring(
						Constants.SWITCHED_BY_CONFIG_PREFIX.length());
					String[] valueArray = values.split(",");

					for (String value : valueArray) {
						switchedByMap.put(value.toUpperCase(), title);
					}
				}
			}
		}

		if (source != null) {
			description = switchedByMap.get(source.toUpperCase());
		}

		logger.debug("Before description is   " + description);

		if (description == null) {
			logger.debug(" description is null ");
			description = config.getProperty(
				Constants.SWITCHED_BY_CONFIG_DEFAULT);
		}

		logger.debug("After description is   " + description);
		logger.debug("getSwitchedByDescription - end");

		return description;
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
	public TransactionDetail getTransactionCudDetailByTxIdBusDateAndSrc(
			TransactionDetail transactionDetail, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		logger.debug("getTransactionCudDetailByTxIdBusDateAndSrc - start");
		GetTransactionArchiveDetailsRequestType archiveDetailsRequestType =
			new GetTransactionArchiveDetailsRequestType();

		archiveDetailsRequestType.setAdPanK(transactionDetail.getAdPank());
		archiveDetailsRequestType.setEtlProcessEndDate(
			transactionDetail.getEtlProcessEndDate());
		archiveDetailsRequestType.setStan(transactionDetail.getTransactionId());
		archiveDetailsRequestType.setAdTransactionDetailK(
			transactionDetail.getAdTransactionDetailK());

		archiveDetailsRequestType.setUserSteppedUp(
			transactionDetail.isUserStepUp());
		archiveDetailsRequestType.setHeader(searchHeader);
		logger.debug(
			"Transacton details objects getAdPank  " +
				transactionDetail.getAdPank());
		logger.debug(
			"Transacton details objects getEtlProcessEndDate  " +
				transactionDetail.getEtlProcessEndDate());
		logger.debug(
			"Transacton details objects getTransactionId  " +
				transactionDetail.getTransactionId());
		logger.debug(
			"Transacton details objects getAdTransactionDetailK  " +
				transactionDetail.getAdTransactionDetailK());

		perfLogger_.debug(
			"Calling the   Transaction Cud Details web service call at " +
				Calendar.getInstance(
				).getTime());
		GetTransactionArchiveDetailsResponseType responseType =
			_transactionArchiveService.getTransactionArchiveDetails(
				archiveDetailsRequestType);
		perfLogger_.debug(
			"Finished Calling the   Transaction Cud Details web service call at " +
				Calendar.getInstance(
				).getTime());

		if (null != responseType.getHeader()) {
			String statusCode = responseType.getHeader(
			).getStatusCode();

			if (statusCode.equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
				transactionDetail = populateTransactionDetailFromResponseType(
					responseType, transactionDetail);

				audit.success(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CUD_DETAIL,
					createSearchParamsForCudTransactionDetails(
						transactionDetail, 1, statusCode));
			}
			else if (statusCode.equalsIgnoreCase(
						Constants.STATUS_SYSTEM_ERROR)) {

				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CUD_DETAIL,
					createSearchParamsForCudTransactionDetails(
						transactionDetail, 0, statusCode));

				throw new Exception();
			}
			else {
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CUD_DETAIL,
					createSearchParamsForCudTransactionDetails(
						transactionDetail, 0, statusCode));
			}
		}

		logger.debug("getTransactionCudDetailByTxIdBusDateAndSrc - end");

		return transactionDetail;
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
	public List<Object> getTransactionCudListOnSearch(
			TransactionForm transactionForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		PermGenUtil.permGenDump("getTransactionCudListOnSearch - start");
		logger.debug("getTransactionCudListOnSearch - start");
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

		FindTransactionsArchiveRequestType archiveRequestType =
			populateSerchForTransaction(transactionForm, portletContext);

		archiveRequestType.setHeader(searchHeader);
		perfLogger_.debug(
			"Calling the  Find Transaction web service call at " +
				Calendar.getInstance(
				).getTime());

		PermGenUtil.permGenDump(
			"getTransactionCudListOnSearch - about to call findTransactions web service ");

		FindTransactionsArchiveResponseType responseType =
			_transactionArchiveService.findTransactionsArchive(
				archiveRequestType);

		PermGenUtil.permGenDump(
			"getTransactionCudListOnSearch - findTransactions webservice completed");

		perfLogger_.debug(
			"Finished calling the Find Transaction web service call at " +
				Calendar.getInstance(
				).getTime());

		if (null != responseType.getHeader()) {
			String statusCode = responseType.getHeader(
			).getStatusCode();

			if (statusCode.equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
				if ((responseType != null) &&
					(responseType.getTransactionArchive(
					).size() > 0)) {

					List<TransactionArchiveBasicInformation> listForms =
						responseType.getTransactionArchive();
					logger.debug(
						"The list size is getTransactionCudListOnSearch before calling populate " +
							responseType.getTransactionArchive(
							).size());
					audit.success(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CUD_SEARCH,
						createSearchParamsStr(
							transactionForm,
							responseType.getTransactionArchive(
							).size(),
							statusCode));

					transactionLists = populateTransactionsearchResultFromWS(
						responseType.getTransactionArchive());
					listReturn.add(transactionLists);

					if (responseType.isMoreAvailable()) {
						transactionLists.get(
							0
						).setMoreRecAvail(
							responseType.isMoreAvailable()
						);
					}
				}
			}
			else if (statusCode.equalsIgnoreCase(
						Constants.STATUS_SYSTEM_ERROR)) {

				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CUD_SEARCH,
					createSearchParamsStr(transactionForm, 0, statusCode));

				throw new Exception();
			}
			else {
				listReturn.add(transactionLists);
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CUD_SEARCH,
					createSearchParamsStr(transactionForm, 0, statusCode));
			}
		}

		PermGenUtil.permGenDump("getTransactionCudListOnSearch - end");
		logger.debug("getTransactionCudListOnSearch - end");

		return listReturn;
	}

	public void logPoolStatistics() {
		transactionService.logPoolStatistics();
	}

	/**
	 * populate the FindTransactionsArchiveRequestType from transactionForm
	 *
	 * @param TransactionForm
	 * @param PortletContext
	 * @return FindTransactionsArchiveRequestType
	 */
	public FindTransactionsArchiveRequestType populateSerchForTransaction(
			TransactionForm transactionForm, PortletContext portletContext)
		throws Exception {

		logger.debug("populateSerchForTransaction - start");
		FindTransactionsArchiveRequestType archiveRequestType =
			new FindTransactionsArchiveRequestType();

		archiveRequestType.setMaxResults(
			Integer.valueOf(
				config.getProperty(Constants.TXN_LIST_HARD_LIMIT)
			).intValue());
		archiveRequestType.setPanOrBin(
			transactionForm.getPanBin(
			).trim());

		if (transactionForm.isPan()) {
			archiveRequestType.setIsPanOrBin(PanOrBinEnum.PAN);
		}
		else {
			archiveRequestType.setIsPanOrBin(PanOrBinEnum.BIN);
		}

		logger.debug(
			"populateSerchForTransaction  24 in end Hr is " +
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
				archiveRequestType.setDateTimeTo(
					createDateAsGregorianCalendarType(newEndDate, "00", "00"));
				logger.debug(
					"FINAL END DATE  time IS     is 24 more  " +
						archiveRequestType.getDateTimeTo());
			}
		}
		else {
			logger.debug("END DATE IS transactionForm.getEndDate() in");
			archiveRequestType.setDateTimeTo(
				createDateAsGregorianCalendarType(
					transactionForm.getEndDate(),
					transactionForm.getEndTimeHr(),
					transactionForm.getEndTimeMin()));
		}

		logger.debug("The End date is: " + archiveRequestType.getDateTimeTo());

		archiveRequestType.setDateTimeFrom(
			createDateAsGregorianCalendarType(
				transactionForm.getStartDate(),
				transactionForm.getStartTimeHr(),
				transactionForm.getStartTimeMin()));

		logger.debug(
			"The start date is: " + archiveRequestType.getDateTimeFrom());
		logger.debug("populateSerchForTransaction - end");

		return archiveRequestType;
	}

	/**
	 * Populate the TransactionDetail from response object
	 *
	 * @param GetTransactionResponseType
	 * @param TransactionDetail
	 * @return TransactionDetail
	 */
	public TransactionDetail populateTransactionDetailFromResponseType(
		GetTransactionArchiveDetailsResponseType responseType,
		TransactionDetail transactionDetail) {

		logger.debug("populateTransactionDetailFromResponseType - start");
		RequestDetails requestDetails = new RequestDetails();
		ResponseDetails responseDetails = new ResponseDetails();

		if (responseType.getTransactionArchive() != null) {
			TransactionArchiveDetailsInformation archiveDetailsInformation =
				responseType.getTransactionArchive();

			if (archiveDetailsInformation.getPanClear() != null) {
				requestDetails.setPan(archiveDetailsInformation.getPanClear());
			}

			if (archiveDetailsInformation.getPanMasked() != null) {
				requestDetails.setMaskedPan(
					archiveDetailsInformation.getPanMasked());
			}

			if ((archiveDetailsInformation.getTransactionTypeDescription() !=
					null) &&
				(archiveDetailsInformation.getAccountDescription() != null)) {

				transactionDetail.setDescription(
					createTransactionDescriptionString(
						archiveDetailsInformation.
							getTransactionTypeDescription(),
						archiveDetailsInformation.getAccountDescription()));
			}

			if (StringUtils.isNotBlank(
					archiveDetailsInformation.
						getTransactionCurrencyAlphaCode())) {

				requestDetails.setCurrencyCodeAcq(
					archiveDetailsInformation.
						getTransactionCurrencyAlphaCode());
			}
			else {
				requestDetails.setCurrencyCodeAcq(
					Constants.DEFAULT_CURRENCY_CODE);
			}

			// Amounts

			if (archiveDetailsInformation.getCashAmount() == null) {
				archiveDetailsInformation.setCashAmount(
					Constants.DEFAULT_AMOUNT_VALUE);
			}

			if (archiveDetailsInformation.getTransactionAmount() == null) {
				archiveDetailsInformation.setTransactionAmount(
					Constants.DEFAULT_AMOUNT_VALUE);
			}

			if (archiveDetailsInformation.getFeeAmount() == null) {
				archiveDetailsInformation.setFeeAmount(
					Constants.DEFAULT_AMOUNT_VALUE);
			}

			requestDetails.setCashAmt(
				Utility.formatAmountByCurrencyCode(
					requestDetails.getCurrencyCodeAcq(),
					Double.valueOf(archiveDetailsInformation.getCashAmount())));
			requestDetails.setTransactionAmt(
				Utility.formatAmountByCurrencyCode(
					requestDetails.getCurrencyCodeAcq(),
					Double.valueOf(
						archiveDetailsInformation.getTransactionAmount())));
			requestDetails.setFeeAmt(
				Utility.formatAmountByCurrencyCode(
					requestDetails.getCurrencyCodeAcq(),
					Double.valueOf(archiveDetailsInformation.getFeeAmount())));

			// Date/Time

			if ((archiveDetailsInformation.getSwitchDate() != null) &&
				(archiveDetailsInformation.getSwitchTime() != null)) {

				requestDetails.setSwitchDateTime(
					createTransactionDates(
						archiveDetailsInformation.getSwitchDate(),
						archiveDetailsInformation.getSwitchTime()));
			}

			if ((archiveDetailsInformation.getLocationDate() != null) &&
				(archiveDetailsInformation.getLocationTime() != null)) {

				requestDetails.setTransactionLocationDateTime(
					archiveDetailsInformation.getLocationTime());
			}

			// Acquirer

			if (archiveDetailsInformation.getAcquirerId() != null)
				requestDetails.setAcquirerId(
					archiveDetailsInformation.getAcquirerId());

			if (archiveDetailsInformation.getAcquirerName() != null)
				requestDetails.setAcquirerName(
					archiveDetailsInformation.getAcquirerName());

			if (archiveDetailsInformation.getStan() != null)
				requestDetails.setAcquirerSystemTrace(
					archiveDetailsInformation.getStan());

			// Issuer

			if (archiveDetailsInformation.getIssuerId() != null)
				requestDetails.setIssuerId(
					archiveDetailsInformation.getIssuerId());

			if (archiveDetailsInformation.getIssuerName() != null)
				requestDetails.setIssuerName(
					archiveDetailsInformation.getIssuerName());

			// Card Acceptor

			if (archiveDetailsInformation.getCardAcceptorId() != null)
				requestDetails.setCardAcceptorId(
					archiveDetailsInformation.getCardAcceptorId());

			if (archiveDetailsInformation.getCardAcceptorName() != null)
				requestDetails.setCardAcceptorName(
					archiveDetailsInformation.getCardAcceptorName());

			if (archiveDetailsInformation.getCardAcceptorName() != null) {
				if (config.getProperty(
						Constants.CUD_TERMINALLOCATION_DOUBLE_QUOTE_ENABLE
					).equalsIgnoreCase(
						"true"
					)) {

					requestDetails.setCardAcceptorName(
						archiveDetailsInformation.getCardAcceptorName());
				}
				else {
					requestDetails.setCardAcceptorName(
						archiveDetailsInformation.getCardAcceptorName(
						).replaceAll(
							"\"", ""
						));
				}
			}

			if (archiveDetailsInformation.getCardAcceptorTerminalId() != null)
				requestDetails.setCardAcceptorTerminalId(
					archiveDetailsInformation.getCardAcceptorTerminalId());

			// Pos

			if (archiveDetailsInformation.getPosEntryMode() != null)
				requestDetails.setPosEntryMode(
					archiveDetailsInformation.getPosEntryMode());

			if (archiveDetailsInformation.getPosConditionCode() != null)
				requestDetails.setPosConditionCode(
					archiveDetailsInformation.getPosConditionCode());

			if (archiveDetailsInformation.getPosMerchantCode() != null)
				requestDetails.setPosMerchantCode(
					archiveDetailsInformation.getPosMerchantCode());

			if (archiveDetailsInformation.getPosEntryModeDescription() != null)
				requestDetails.setPosEntryDescription(
					archiveDetailsInformation.getPosEntryModeDescription());

			if (archiveDetailsInformation.getPosConditionCodeDescription() !=
					null)
				requestDetails.setPosConditionDescription(
					archiveDetailsInformation.getPosConditionCodeDescription());

			if (archiveDetailsInformation.getPosMerchantCodeDescription() !=
					null)
				requestDetails.setPosMerchantDescription(
					archiveDetailsInformation.getPosMerchantCodeDescription());

			// Response Details

			if (archiveDetailsInformation.getResponseDetailsCode() != null)
				responseDetails.setCode(
					archiveDetailsInformation.getResponseDetailsCode());

			if (archiveDetailsInformation.getResponseDetailsDescription() !=
					null)
				responseDetails.setDescription(
					archiveDetailsInformation.getResponseDetailsDescription());

			if (archiveDetailsInformation.getResponseDetailsSwitchedBy() !=
					null) {

				responseDetails.setSwitchedBy(
					getSwitchedByDescription(
						archiveDetailsInformation.
							getResponseDetailsSwitchedBy()));
			}
			else {
				responseDetails.setSwitchedBy(
					config.getProperty(Constants.SWITCHED_BY_CONFIG_DEFAULT));
			}

			if (archiveDetailsInformation.getResponseDetailsAuthorisedBy() !=
					null)
				responseDetails.setAuthorisedBy(
					getAuthorisedByDescription(
						archiveDetailsInformation.
							getResponseDetailsAuthorisedBy()));

			transactionDetail.setRequestDetails(requestDetails);
			transactionDetail.setResponseDetails(responseDetails);
		}

		logger.debug("populateTransactionDetailFromResponseType - start");

		return transactionDetail;
	}

	/**
	 * Populate the TransactionList from response object
	 *
	 * @param List
	 *            <TransactionBasicInformation>
	 * @return List<TransactionList>
	 */
	public List<TransactionList> populateTransactionsearchResultFromWS(
		List<TransactionArchiveBasicInformation> archiveBasicInformations) {

		logger.debug("populateTransactionsearchResultFromWS - start");
		List<TransactionList> cudList = new ArrayList<>();
		TransactionList transactionList = null;

		for (TransactionArchiveBasicInformation basicInformation :
				archiveBasicInformations) {

			transactionList = new TransactionList();

			if (basicInformation.getStan() != null) {
				transactionList.setTransactionId(basicInformation.getStan());
				transactionList.setSystemTrace(basicInformation.getStan());
			}

			if (basicInformation.getAdPanK() != null)
				transactionList.setAdPank(basicInformation.getAdPanK());

			if (basicInformation.getEtlProcessEndDate() != null)
				transactionList.setEtlProcessEndDate(
					basicInformation.getEtlProcessEndDate());

			if (basicInformation.getAdTransactionDetailK() != null)
				transactionList.setAdTransactionDetailK(
					basicInformation.getAdTransactionDetailK());

			if (basicInformation.getPanMasked() != null)
				transactionList.setPan(basicInformation.getPanMasked());

			if (basicInformation.getTerminalId() != null)
				transactionList.setTerminalId(basicInformation.getTerminalId());

			if (basicInformation.getTerminalLocation() != null) {
				if (config.getProperty(
						Constants.CUD_TERMINALLOCATION_DOUBLE_QUOTE_ENABLE
					).equalsIgnoreCase(
						"true"
					)) {

					transactionList.setTerminalLocation(
						basicInformation.getTerminalLocation());
				}
				else {
					transactionList.setTerminalLocation(
						basicInformation.getTerminalLocation(
						).replaceAll(
							"\"", ""
						));
				}
			}

			if (basicInformation.getResponseCodeDescription() != null)
				transactionList.setResponseCode(
					basicInformation.getResponseCodeDescription());

			if (basicInformation.getAccountDescription() != null)
				transactionList.setDescription(
					createTransactionDescriptionString(
						basicInformation.getTransactionTypeDescription(),
						basicInformation.getAccountDescription()));

			if (basicInformation.getTransactionAmount() != null)
				transactionList.setAmount(
					Float.valueOf(basicInformation.getTransactionAmount()));

			if (basicInformation.getTransactionDate() != null)
				transactionList.setCudTransactionDate(
					Utility.formatDate(
						basicInformation.getTransactionDate(),
						Constants.DATE_FORMAT_1));

			if (basicInformation.getSwitchTransactionTime() != null) {
				logger.debug(
					"The getSwitchTransactionTime is  " +
						basicInformation.getSwitchTransactionTime());
				transactionList.setCudTranscationTime(
					basicInformation.getSwitchTransactionTime());
			}

			if (basicInformation.getCurrencyAlphaCode() != null) {
				transactionList.setCurrencyCodeAcq(
					basicInformation.getCurrencyAlphaCode());
				transactionList.setCurrencyCodeIss(
					basicInformation.getCurrencyAlphaCode());
				transactionList.setCurrencyFormatVal(
					Utility.findDefaultFractionDigits(
						basicInformation.getCurrencyAlphaCode()));
			}
			else {
				transactionList.setCurrencyCodeAcq(
					Constants.DEFAULT_CURRENCY_CODE);
				transactionList.setCurrencyCodeIss(
					Constants.DEFAULT_CURRENCY_CODE);
			}

			cudList.add(transactionList);
		}

		logger.debug("Size of the transaction cud list is  " + cudList.size());
		logger.debug("populateTransactionsearchResultFromWS - end");

		return cudList;
	}

	/**
	 * clear out the pooled connections to Cuscal Web Services.
	 */
	public void purgePools() {
		transactionService.purgePools();
	}


	private static final Auditor audit = Auditor.getInstance();

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger("cudLogging");

	/**
	 * Logger object for performance logging
	 */
	private static Logger perfLogger_ = LoggerFactory.getLogger("perfLogging");

	@Autowired
	@Qualifier("propUtil")
	private Properties config;

	private Map<String, String> switchedByMap = null;

	@Autowired
	@Qualifier("transactionService")
	private TransactionService transactionService;

	private final TransactionArchiveService _transactionArchiveService = CuscalServiceLocator.getService(
		TransactionArchiveService.class.getName());

	private Map<String, String> txnDescriptionMap = null;

}