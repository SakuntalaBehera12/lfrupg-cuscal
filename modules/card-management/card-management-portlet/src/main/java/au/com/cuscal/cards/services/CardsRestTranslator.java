package au.com.cuscal.cards.services;

import static au.com.cuscal.cards.commons.Constants.CARD_SEARCH_APP_PROPERTIES;
import static au.com.cuscal.cards.commons.Utility.formatExpiryDatev2;
import static au.com.cuscal.cards.commons.Utility.getCardObjectByToken;

import static org.apache.commons.lang.StringUtils.*;

import au.com.cuscal.cards.commons.CardSearchAppProperties;
import au.com.cuscal.cards.commons.Constants;
import au.com.cuscal.cards.commons.Utility;
import au.com.cuscal.cards.domain.*;
import au.com.cuscal.cards.services.rest.CardRestService;
import au.com.cuscal.cards.services.rest.domain.OrganisationAccess;
import au.com.cuscal.framework.audit.context.PortletContext;

import com.google.gson.*;

import java.text.SimpleDateFormat;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Helper class that routes the Cards API calls to either Authentic 3 or Authentic 5
 *
 * @author Amit Wadhwa
 *
 */
@Service(Constants.CARD_REST_TRANSLATOR)
public class CardsRestTranslator {

	/**
	 * Operation to translate GetCardAccounts Json String response to List<AccountInformation> Object
	 *
	 * @param svcResponse String svcResponse
	 * @return List<AccountInformation>
	 */
	public List<AccountInformation> formatGetCardAccounts(
		String svcResponse, String cuscalToken, PortletContext portletContext) {

		logger.info(
			"[formatGetCardAccounts] Inside method formatGetCardAccounts");
		List<AccountInformation> accountInformationList = null;

		try {
			JsonObject response = new Gson().fromJson(
				svcResponse, JsonObject.class);

			JsonArray accounts = response.getAsJsonObject(
				Constants.DATA
			).getAsJsonArray(
				Constants.ACCOUNTS
			);

			if ((null != accounts) && (accounts.size() > 0)) {
				accountInformationList = new ArrayList<>();
				CardData card = getCardObjectByToken(
					cuscalToken, portletContext.getRequest());
				String pan = Constants.DASH;
				String institution = Constants.DASH;

				if (null != card) {
					institution = card.getInstitution(
					).replace(
						"<br>", " - "
					);
					pan = card.getPan();
				}

				for (JsonElement o : accounts) {
					AccountInformation accountInformation =
						new AccountInformation();

					accountInformation.setPan(pan);
					accountInformation.setInstitution(institution);
					JsonObject _o = o.getAsJsonObject();

					String accountNumber = getAsString(
						_o, Constants.ACCOUNT_NUMBER);
					String accountType = getAsString(
						_o, Constants.ACCOUNT_TYPE);
					String accountTypeDescription = getAsString(
						_o, Constants.ACCOUNT_TYPE_DESC);
					String isPrimaryAccount = getAsString(
						_o, Constants.IS_PRIMARY_ACCOUNT);
					String lastModifiedDate = getAsString(
						_o, Constants.LAST_MODIFIED_DATE);
					JsonObject balances = getAsJsonObject(
						_o, Constants.BALANCES);

					String availableBalance = balances != null ?
						getAsString(balances, Constants.AVAILABLE_BALANCE) :
							null;
					String creditLineBalance = balances != null ?
						getAsString(balances, Constants.CREDIT_LINE_BALANCE) :
							null;
					String ledgerBalance = balances != null ?
						getAsString(balances, Constants.LEDGER_BALANCE) : null;

					if (isNotBlank(accountNumber)) {
						accountInformation.setAccountNumber(accountNumber);
					}

					if (isNotBlank(accountTypeDescription)) {
						accountInformation.setAccountType(
							accountTypeDescription);
					}

					if (isNotBlank(accountType)) {
						accountInformation.setAccountQualifier(accountType);
					}

					if (isNotBlank(isPrimaryAccount) &&
						"true".equalsIgnoreCase(isPrimaryAccount)) {

						accountInformation.setPrimaryAccount("Yes");
					}
					else {
						accountInformation.setPrimaryAccount("No");
					}

					if (isNotBlank(lastModifiedDate)) {
						accountInformation.setLastUpdated(
							Utility.formatDate(
								lastModifiedDate,
								Constants.DATE_24_HOUR_MINUTE_SECOND_FORMAT));
					}

					if (isNotBlank(ledgerBalance)) {
						accountInformation.setLedgerBalance(ledgerBalance);
					}
					else {
						accountInformation.setLedgerBalance(
							Constants.NOT_AVAILABLE);
					}

					if (isNotBlank(availableBalance)) {
						accountInformation.setAvailableBalance(
							availableBalance);
					}
					else {
						accountInformation.setAvailableBalance(
							Constants.NOT_AVAILABLE);
					}

					if (isNotBlank(creditLineBalance)) {
						accountInformation.setCreditLine(creditLineBalance);
					}
					else {
						accountInformation.setCreditLine(
							Constants.NOT_AVAILABLE);
					}

					accountInformationList.add(accountInformation);
				}

				logger.info(
					"[formatGetCardAccounts] the formatGetCardAccounts - extracted {} accounts from service and added {} to the list",
					accounts.size(), accountInformationList.size());
			}

			return accountInformationList;
		}
		catch (Exception ex) {
			logger.error(
				"[formatGetCardAccounts] the formatGetCardAccounts  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}

		return accountInformationList;
	}

	/**
	 * Operation to translate GetCardChannels Json String response to CardChannelPermission Object
	 *
	 * @param svcResponse String svcResponse
	 * @return CardInformation
	 */
	public CardInformation formatGetCardChannelsResponse(
		CardInformation cardInformation, String svcResponse) {

		logger.info(
			"[formatGetCardChannelsResponse] Inside method formatGetCardChannelsResponse");

		try {
			JsonObject response = new Gson().fromJson(
				svcResponse, JsonObject.class);

			JsonArray cardChannels = response.getAsJsonObject(
				Constants.DATA
			).getAsJsonArray(
				"cardChannels"
			);
			List<CardChannelPermission> cardChannelPermissions =
				new ArrayList<>();

			if ((null != cardChannels) && (cardChannels.size() > 0)) {
				for (JsonElement o : cardChannels) {
					JsonObject _o = o.getAsJsonObject();

					String channelName = getAsString(_o, Constants.NAME);
					JsonArray permissions = _o.getAsJsonArray(
						Constants.PERMISSIONS);
					List<String> cardAccessResponse = new ArrayList<>();

					for (JsonElement u : permissions) {
						JsonObject _u = u.getAsJsonObject();

						String permissionName = getAsString(_u, Constants.NAME);
						String permissionValue = getAsString(
							_u, Constants.VALUE);

						if (isNotBlank(permissionValue) &&
							equalsIgnoreCase(permissionValue, "Yes")) {

							cardAccessResponse.add(permissionName);
						}
					}

					CardChannelPermission cardChannelPermission =
						new CardChannelPermission();
					String channelPermissionName = cardChannelTranslatedName(
						channelName);

					if (isNotBlank(channelPermissionName)) {
						cardChannelPermission.setChannelPermissionName(
							channelPermissionName);
						cardChannelPermission.setAccessTypes(
							populateCardAccessTypeWithPermission(
								cardAccessResponse));
						cardChannelPermissions.add(cardChannelPermission);
					}
				}
				//Ordering the Channel Names using the channel order from the properties file.
				final Map<String, Integer> channelOrderMap =
					getChannelOrderMap();
				Collections.sort(
					cardChannelPermissions,
					new Comparator<CardChannelPermission>() {

						@Override
						public int compare(
							CardChannelPermission c1,
							CardChannelPermission c2) {

							return channelOrderMap.get(
								c1.getChannelPermissionName()
							).compareTo(
								channelOrderMap.get(
									c2.getChannelPermissionName())
							);
						}

					});
				logger.info(
					"[formatGetCardChannelsResponse] the formatGetCardChannelsResponse - extracted no. of channels - " +
						cardChannelPermissions.size());
			}
			else {
				cardInformation.setCardChannelPermissionsMessage(
					Constants.CHANNEL_PERMISSIONS_ERROR);
			}

			cardInformation.setCardPermissions(cardChannelPermissions);

			return cardInformation;
		}
		catch (Exception ex) {
			cardInformation.setCardChannelPermissionsMessage(
				Constants.CHANNEL_PERMISSIONS_ERROR);
			logger.error(
				"[formatGetCardChannelsResponse] the formatGetCardChannelsResponse  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}

		return cardInformation;
	}

	/**
	 * Operation to translate GetCardDetails Json String response to CardInformation Object
	 *
	 * @param svcResponse String svcResponse
	 * @return CardInformation
	 */
	public CardInformation formatGetCardDetailsResponse(
		CardInformation cardInformation, String svcResponse,
		OrganisationAccess organisationAccess, String cuscalToken,
		List<String> groups, PortletContext portletContext) {

		logger.info(
			"[formatGetCardDetailsResponse] Inside method formatGetCardDetailsResponse");

		try {
			JsonObject response = new Gson().fromJson(
				svcResponse, JsonObject.class);

			JsonObject cardDetails = response.getAsJsonObject(
				Constants.DATA
			).getAsJsonObject(
				Constants.CARD_DETAIL_OBJECT
			);

			String pan = getAsJsonObject(
				cardDetails, Constants.PAN
			).get(
				"value"
			).getAsString();
			String cardHolderName = getAsString(
				cardDetails, Constants.CARD_HOLDER_NAME);
			JsonObject mailAddress = getAsJsonObject(
				cardDetails, Constants.MAIL_ADDRESS);

			String address =
				null == mailAddress ? null : getAddress(mailAddress);
			String institution =
				getAsString(cardDetails, Constants.ISSUER_ID) == null ?
					getAsString(cardDetails, Constants.ISSUER_SHORT_NAME) :
						getAsString(cardDetails, Constants.ISSUER_ID) + " - " +
							getAsString(
								cardDetails, Constants.ISSUER_SHORT_NAME);
			String expiryDate = formatExpiryDatev2(
				getAsString(cardDetails, Constants.EXPIRY_DATE));
			String pinRetryCount = getAsString(
				cardDetails, Constants.PIN_RETRY_COUNT);
			String lastModifiedDate = getAsString(
				cardDetails, Constants.LAST_MODIFIED_DATE);
			String lastUsedTimestamp = getAsString(
				cardDetails, Constants.LAST_USED_TIMESTAMP);
			String status = getAsString(cardDetails, Constants.STATUS);

			cardInformation = new CardInformation();

			//			if(Utility.isUserBelongToCsuAdminUserGroup(portletContext.getRequest(), cardSearchAppProperties
			//					.getCardsProps().getProperty(Constants.CARD_STATUS_UPDATE_ADMIN_PROPKEY))){
			//				// cardRequestType.setShowAllStatus(true); // get all the statuses back
			//			}

			if (isNotBlank(pan)) {
				cardInformation.setPan(pan);
			}

			if (isNotBlank(institution)) {
				cardInformation.setInstitution(institution);
			}

			if (isNotBlank(address) && isNotBlank(cardHolderName)) {
				CardHolder cardHolder = new CardHolder();

				cardHolder.setCardHolderName(cardHolderName);
				logger.debug(
					" The Card Information response type - the final address string is  " +
						address);
				cardHolder.setCardHolderAddress(address);
				cardInformation.setCardHolder(cardHolder);
			}

			if (isNotBlank(status)) {
				String key = getKeyFromStatusMapForAPIs(status);
				List<String> nextAvailableStatus = null;

				try {
					nextAvailableStatus =
						formatGetCardNextAvailableStatusResponse(
							status,
							cardRestService.getCardNextAvailableStatus(
								organisationAccess, cuscalToken, status));
				}
				catch (Exception ex) {
					logger.error(
						"[formatGetCardDetailsResponse] the formatGetCardNextAvailableStatusResponse service response EXCEPTION CAME ERROR MSG e.getCause()  is " +
							ex.getCause(),
						ex);
				}

				populateCardStatusMapForUI(
					key, nextAvailableStatus, cardInformation, groups);
			}
			else {
				cardInformation.setCardStatus(Constants.UNKNOWN);
			}

			if (isNotBlank(expiryDate)) {
				cardInformation.setExpiryDate(expiryDate);
			}

			if (isNotBlank(pinRetryCount)) {
				cardInformation.setPinCountFailures(pinRetryCount);
			}

			if (isNotBlank(lastUsedTimestamp)) {
				cardInformation.setLastUsed(
					Utility.formatDate(
						lastUsedTimestamp,
						Constants.DATE_24_HOUR_MINUTE_SECOND_FORMAT));
			}

			if (isNotBlank(lastModifiedDate)) {
				cardInformation.setLastMaintained(
					Utility.formatDate(lastModifiedDate, "yyyy-MM-dd"));
			}

			Date lastUpdatedTime = null;

			if ((cardInformation.getLastMaintained() != null) &&
				(cardInformation.getLastUsed() != null)) {

				if (cardInformation.getLastMaintained(
					).after(
						cardInformation.getLastUsed()
					)) {

					lastUpdatedTime = cardInformation.getLastMaintained();
				}
				else {
					lastUpdatedTime = cardInformation.getLastUsed();
				}
			}
			else {
				if (cardInformation.getLastMaintained() == null) {
					lastUpdatedTime = cardInformation.getLastUsed();
				}
				else if (cardInformation.getLastUsed() == null) {
					lastUpdatedTime = cardInformation.getLastMaintained();
				}
			}

			cardInformation.setLastUpdated(lastUpdatedTime);

			cardInformation = formatGetCardChannelsResponse(
				cardInformation,
				cardRestService.getCardChannels(
					organisationAccess, cuscalToken));
			logger.info(
				"[formatGetCardDetailsResponse] the card information is populated for pan {}",
				cardInformation.getMaskedPan());

			return cardInformation;
		}
		catch (Exception ex) {
			cardInformation.setCardChannelPermissionsMessage(
				Constants.CHANNEL_PERMISSIONS_ERROR);
			logger.error(
				"[formatGetCardDetailsResponse] the formatGetCardDetailsResponse  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}

		return cardInformation;
	}

	/**
	 * Operation to translate GetCardBasedLimits Json String response to CardLimits Object
	 *
	 * @param svcResponse String svcResponse
	 * @return CardLimits
	 */
	public CardLimits formatGetCardLimitsResponse(
		CardLimits cardLimits, String svcResponse, String cuscalToken,
		PortletContext portletContext) {

		logger.info(
			"[formatGetCardLimitsResponse] Inside method formatGetCardLimitsResponse");

		try {
			JsonObject response = new Gson().fromJson(
				svcResponse, JsonObject.class);

			JsonArray limits = response.getAsJsonObject(
				Constants.DATA
			).getAsJsonArray(
				Constants.LIMITS
			);
			List<StandardOverrideLimits> standardOverrideLimits =
				new ArrayList<>();
			StandardOverrideLimits overrideLimits = null;

			if ((null != limits) && (limits.size() > 0)) {
				CardData card = getCardObjectByToken(
					cuscalToken, portletContext.getRequest());

				if (null != card) {
					cardLimits.setInstitution(
						card.getInstitution(
						).replace(
							"<br>", " - "
						));
					cardLimits.setPan(card.getPan());
				}
				else {
					cardLimits.setInstitution(Constants.DASH);
					cardLimits.setPan(Constants.DASH);
				}

				for (JsonElement o : limits) {
					JsonObject _o = o.getAsJsonObject();

					String name = getAsString(_o, Constants.NAME);
					String type = getAsString(_o, Constants.TYPE);
					String value = getAsString(_o, Constants.VALUE);
					String usage = getAsString(_o, Constants.USAGE);
					overrideLimits = new StandardOverrideLimits();
					overrideLimits.setLimitType(name);

					if (equalsIgnoreCase("Amount", type)) {
						overrideLimits.setLimitAmount(value.toString());
						overrideLimits.setUsageAmount(usage.toString());
						overrideLimits.setLimitCount(Constants.DASH);
						overrideLimits.setUsageCount("0");
					}
					else if (equalsIgnoreCase("Count", type)) {
						overrideLimits.setLimitCount(value.toString());
						overrideLimits.setUsageCount(usage.toString());
						overrideLimits.setLimitAmount(Constants.DASH);
						overrideLimits.setUsageAmount("0");
					}

					standardOverrideLimits.add(overrideLimits);
				}

				cardLimits.setStandardOverrideLimits(standardOverrideLimits);
				logger.info(
					"[formatGetCardLimitsResponse] the formatGetCardLimitsResponse - extracted {} limits from service for pan {}",
					limits.size(), cardLimits.getMaskedPan());
			}

			return cardLimits;
		}
		catch (Exception ex) {
			logger.error(
				"[formatGetCardChannelsResponse] the formatGetCardChannelsResponse  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}

		return cardLimits;
	}

	/**
	 * Operation to translate GetCardNextAvailableStatus Json String response to List<String> Object
	 *
	 * @param svcResponse String svcResponse
	 * @return List<String>
	 */
	public List<String> formatGetCardNextAvailableStatusResponse(
		String currentStatus, String svcResponse) {

		logger.info(
			"[formatGetCardNextAvailableStatusResponse] Inside method formatGetCardNextAvailableStatusResponse");

		try {
			List<String> nextAvailableStatus = new ArrayList<>();
			JsonObject response = new Gson().fromJson(
				svcResponse, JsonObject.class);

			JsonArray statusChanges = response.getAsJsonObject(
				Constants.DATA
			).getAsJsonArray(
				Constants.STATUS_CHANGES
			);

			if ((null != statusChanges) && (statusChanges.size() > 0)) {
				for (JsonElement o : statusChanges) {
					JsonObject _o = o.getAsJsonObject();

					String status = getAsString(_o, Constants.STATUS);

					if (isNotBlank(status) &&
						status.equalsIgnoreCase(currentStatus)) {

						JsonArray nextStatusList = _o.getAsJsonArray(
							Constants.NEXT_STATUS);

						for (JsonElement nextStatus : nextStatusList) {
							String key = getKeyFromStatusMapForAPIs(
								nextStatus.getAsString());

							if (isNotBlank(key) && (key.length() > 0)) {
								nextAvailableStatus.add(key);
							}
						}
					}
				}
			}

			logger.info(
				"[formatGetCardNextAvailableStatusResponse] Added {} next available status to the list for current status {}",
				nextAvailableStatus.size(), currentStatus);

			return nextAvailableStatus.size() > 0 ? nextAvailableStatus : null;
		}
		catch (Exception ex) {
			logger.error(
				"[formatGetCardNextAvailableStatusResponse] the formatGetCardNextAvailableStatusResponse  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);

			return null;
		}
	}

	/**
	 * Operation to translate GetCards Json String response to Cards Object
	 *
	 * @param svcResponse String svcResponse
	 * @return List<Cards>
	 */
	public List<Cards> formatGetCardsResponse(String svcResponse) {
		logger.info(
			"[formatGetCardsResponse] Inside method formatGetCardsResponse");

		try {
			JsonObject response = new Gson().fromJson(
				svcResponse, JsonObject.class);

			JsonArray cardsList = response.getAsJsonObject(
				Constants.DATA
			).getAsJsonArray(
				Constants.CARDS
			);
			int totalRecords = response.getAsJsonObject(
				Constants.META
			).get(
				Constants.TOTAL_RECORDS
			).getAsInt();
			List<Cards> cardsListToReturn = new ArrayList<>();

			for (JsonElement o : cardsList) {
				JsonObject _o = o.getAsJsonObject();

				String pan = getAsJsonObject(
					_o, Constants.PAN
				).get(
					"value"
				).getAsString();
				JsonObject mailAddress = getAsJsonObject(
					_o, Constants.MAIL_ADDRESS);

				String address =
					null == mailAddress ? null : getAddress(mailAddress);
				String cardHolderName = getAsString(
					_o, Constants.CARD_HOLDER_NAME);
				String institution =
					getAsString(_o, Constants.ISSUER_ID) == null ?
						getAsString(_o, Constants.ISSUER_SHORT_NAME) :
							getAsString(_o, Constants.ISSUER_ID) + "<br>" +
								getAsString(_o, Constants.ISSUER_SHORT_NAME);
				String expiryDate = formatExpiryDatev2(
					getAsString(_o, Constants.EXPIRY_DATE));
				String status = getCardStatusForUI(
					getAsString(_o, Constants.STATUS));
				String cuscalToken = getAsString(_o, Constants.CUSCAL_TOKEN);
				Date lastUpdated =
					null != getAsString(_o, Constants.LAST_MODIFIED_DATE) ?
						new SimpleDateFormat(
							"yyyy-MM-dd"
						).parse(
							getAsString(_o, Constants.LAST_MODIFIED_DATE)
						) : null;

				Cards cardObject = new Cards.Builder().setPan(
					pan
				).setAddress(
					address
				).setCardHolder(
					cardHolderName
				).setInstitution(
					institution
				).setExpiryDate(
					expiryDate
				).setLastUpdated(
					lastUpdated
				).setStatus(
					status
				).setCuscalToken(
					cuscalToken
				).setTotalListSize(
					totalRecords
				).build();

				cardsListToReturn.add(cardObject);
			}

			logger.info(
				"[formatGetCardsResponse] {} cards returned", totalRecords);

			return cardsListToReturn;
		}
		catch (Exception ex) {
			logger.debug(
				"[formatGetCardsResponse] the formatGetCardsResponse  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}

		return null;
	}

	/**
	 * Operation to translate UpdateCardStatus Json String response to String Object
	 *
	 * @param svcResponse String svcResponse
	 * @return String updateReferenceNumber
	 */
	public String formatUpdateCardStatus(String svcResponse) {
		logger.info(
			"[formatUpdateCardStatus] Inside method formatUpdateCardStatus");
		logger.info(
			"[formatUpdateCardStatus] the formatUpdateCardStatus - response from API call - {}",
			svcResponse);
		String updateReferenceNumber = null;

		try {
			JsonObject response = new Gson().fromJson(
				svcResponse, JsonObject.class);

			JsonObject data = response.getAsJsonObject(Constants.DATA);
			JsonObject metaData = response.getAsJsonObject(Constants.META);

			String cardStatus = getAsString(data, "cardStatus");
			String code = getAsString(metaData, "code");
			String description = getAsString(metaData, "description");
			String reference = getAsString(metaData, "reference");

			if (isNotBlank(code) && isNotBlank(description)) {
				if (Constants.SUCCESS.equalsIgnoreCase(code)) {
					updateReferenceNumber = reference;
				}
			}

			logger.info(
				"[formatUpdateCardStatus] the formatUpdateCardStatus - reference number extracted {}",
				updateReferenceNumber);

			return updateReferenceNumber;
		}
		catch (Exception ex) {
			logger.error(
				"[formatUpdateCardStatus] the formatUpdateCardStatus  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
			updateReferenceNumber = null;
		}

		return null;
	}

	/**
	 * Populate all Access type exist.
	 *
	 * @return List
	 */
	public List<String> getAccessTypesForCard() {
		List<String> accessTypes = new ArrayList<>();
		String[] accessTypesArry = cardSearchAppProperties.getCardsProps(
		).getProperty(
			Constants.ACCESS_TYPE_PROPKEY
		).split(
			Constants.COMMA
		);

		for (String accessType : accessTypesArry) {
			accessTypes.add(accessType);
		}

		return accessTypes;
	}

	public String getAddress(JsonObject _o) {
		String address1 = null != getAsString(_o, Constants.ADDRESS1) ?
			getAsString(_o, Constants.ADDRESS1) : "";
		String address2 = null != getAsString(_o, Constants.ADDRESS2) ?
			getAsString(_o, Constants.ADDRESS2) : "";
		String suburb = null != getAsString(_o, Constants.SUBURB) ?
			getAsString(_o, Constants.SUBURB) : "";
		String state = null != getAsString(_o, Constants.STATE) ?
			getAsString(_o, Constants.STATE) : "";
		String country = null != getAsString(_o, Constants.COUNTRY) ?
			getAsString(_o, Constants.COUNTRY) : "";
		String postcode = null != getAsString(_o, Constants.POSTCODE) ?
			getAsString(_o, Constants.POSTCODE) : "";

		return (address1 + " " + address2 + ", " + postcode).trim();
	}

	public JsonObject getAsJsonObject(JsonObject _o, String key) {
		if (_o.has(key)) {
			return _o.getAsJsonObject(key);
		}

		return null;
	}

	public String getAsString(JsonObject _o, String key) {
		if (_o.has(key)) {
			return _o.get(
				key
			).getAsString();
		}

		return null;
	}

	/**
	 * Maps the card Status value from two Collections
	 * Map(k,v) and Map(k,u) - Maps v -> u
	 * @param status
	 * @return String
	 */
	public String getCardStatusForUI(String status) {
		logger.debug("[getCardStatusForUI] Inside method getCardStatusForUI");

		if (null == status) {
			return null;
		}

		String key = "";
		Map<String, String> cardStatusMap = getStatusMap();
		Map<String, String> cardStatusMapForAPIs = getStatusMapForAPIs();

		for (Map.Entry<String, String> entry :
				cardStatusMapForAPIs.entrySet()) {

			if (entry.getValue(
				).equals(
					status
				)) {

				key = entry.getKey();
				logger.debug("[getCardStatusForUI] Found key " + key);

				break;
			}
		}

		logger.debug("[getCardStatusForUI] return " + cardStatusMap.get(key));

		return cardStatusMap.get(key);
	}

	/**
	 * Return the key from the MAP of Card Status for b2b APIs.
	 * For e.g. NORMAL -> 00
	 *
	 * @param statusForAPI String
	 * @return String key
	 */
	public String getKeyFromStatusMapForAPIs(String statusForAPI) {
		String key = "";
		Map<String, String> cardStatusMapForAPIs = getStatusMapForAPIs();

		for (Map.Entry<String, String> entry :
				cardStatusMapForAPIs.entrySet()) {

			if (entry.getValue(
				).equals(
					statusForAPI
				)) {

				key = entry.getKey();
				logger.debug(
					"Found key - {} for status {} ", key, statusForAPI);

				return key;
			}
		}

		return key;
	}

	/**
	 * Return the TreeMap of the Card Status.
	 *
	 * @return <em>TreeMap</em> cardStatusMap
	 */
	public Map<String, String> getStatusMap() {
		Map<String, String> cardStatusMap = new TreeMap<>();

		String[] cardStatusValArray = cardSearchAppProperties.getCardsProps(
		).getProperty(
			Constants.CARD_STATUS_VAL_PROPKEY
		).split(
			Constants.COMMA
		);

		String[] cardStatusOptionArray = cardSearchAppProperties.getCardsProps(
		).getProperty(
			Constants.CARD_STATUS_OPT_PROPKEY
		).split(
			Constants.COMMA
		);

		for (int i = 0; i < cardStatusOptionArray.length; i++) {
			if (isNotBlank(cardStatusValArray[i]) &&
				isNotBlank(cardStatusOptionArray[i]))
				cardStatusMap.put(
					cardStatusValArray[i], cardStatusOptionArray[i]);
		}

		return cardStatusMap;
	}

	/**
	 * Return the TreeMap of the Card Status for b2b APIs.
	 *
	 * @return <em>TreeMap</em> cardStatusMapForAPIs
	 */
	public Map<String, String> getStatusMapForAPIs() {
		Map<String, String> cardStatusMapForAPIs = new TreeMap<>();

		String[] cardStatusValArray = cardSearchAppProperties.getCardsProps(
		).getProperty(
			Constants.CARD_STATUS_VAL_PROPKEY
		).split(
			Constants.COMMA
		);

		String[] cardStatusOptionArray = cardSearchAppProperties.getCardsProps(
		).getProperty(
			Constants.CARD_STATUS_API_OPT_PROPKEY
		).split(
			Constants.COMMA
		);

		for (int i = 0; i < cardStatusOptionArray.length; i++) {
			if (isNotBlank(cardStatusValArray[i]) &&
				isNotBlank(cardStatusOptionArray[i]))
				cardStatusMapForAPIs.put(
					cardStatusValArray[i], cardStatusOptionArray[i]);
		}

		return cardStatusMapForAPIs;
	}

	/**
	 * Find out whether user has permission to update card status not to Deleted
	 * card
	 *
	 * @param groups
	 * @return boolean .
	 */
	public boolean isUserAllowedToUpdateNotToDeleteStatus(List<String> groups) {
		boolean isNotAllowed = false;
		String[] groupStatusUpdateNoDelArry =
			cardSearchAppProperties.getCardsProps(
			).getProperty(
				Constants.CARD_STATUS_UPDATE_NO_DELETE_PROPKEY
			).split(
				Constants.COMMA
			);

		for (String string : groupStatusUpdateNoDelArry) {
			if (groups.contains(string)) {
				isNotAllowed = true;

				return isNotAllowed;
			}
		}

		return isNotAllowed;
	}

	/**
	 * Find out whether user has permission to update card status
	 *
	 * @param groups
	 * @return boolean .
	 */
	public boolean isUserAllowedToUpdateStatus(List<String> groups) {
		boolean isAllowed = false;

		if ((null != groups) && (groups.size() > 0)) {
			String[] groupStatusUpdateArry =
				cardSearchAppProperties.getCardsProps(
				).getProperty(
					Constants.CARD_STATUS_UPDATE_PROPKEY
				).split(
					Constants.COMMA
				);

			for (String string : groupStatusUpdateArry) {
				if (groups.contains(string)) {
					isAllowed = true;

					return isAllowed;
				}
			}
		}

		return isAllowed;
	}

	/**
	 * Populate Card Access with permissions
	 *
	 * @param cardAccessResponse List<String>
	 * @return List<Access>
	 */
	public List<Access> populateCardAccessTypeWithPermission(
		List<String> cardAccessResponse) {

		List<Access> accesses = new ArrayList<>();
		List<String> accessTypes = getAccessTypesForCard();
		Access accesObj = null;

		for (String access : accessTypes) {
			accesObj = new Access();
			accesObj.setAccessType(access);

			if (cardAccessResponse.contains(access)) {
				accesObj.setAccessAvailable("Y");
			}
			else {
				accesObj.setAccessAvailable("N");
			}

			accesses.add(accesObj);
		}

		return accesses;
	}

	/**
	 * Populate CardStatus Map For UI display
	 *
	 * @param currentCardStatus
	 * @param cardInformation
	 * @param nextAvailableStatus
	 */
	public void populateCardStatusMapForUI(
			String currentCardStatus, List<String> nextAvailableStatus,
			CardInformation cardInformation, List<String> groups)
		throws Exception {

		Map<String, String> cardStatusMap = getStatusMap();
		Map<String, String> cardStatusUIMap = null;
		List<String> availableCardStatus = null;
		String currentStatus = currentCardStatus;
		boolean isOneStatus = true;

		cardInformation.setHasCardPortedToPrimarySwitch(true);

		if (isUserAllowedToUpdateStatus(groups)) {
			logger.debug(
				"If card status update card permission is true, adding all card status to map from available status list");

			if (null != nextAvailableStatus) {
				availableCardStatus = nextAvailableStatus;
			}
			else {
				availableCardStatus = new ArrayList<>();
			}

			logger.debug(
				"Card status populate method avaliableCardStatus size is  " +
					availableCardStatus.size());
			availableCardStatus.add(currentStatus);
			logger.debug(
				"Card status populate method  cardStatusMap  is  " +
					cardStatusMap);

			if (availableCardStatus.size() > 1) {
				isOneStatus = false;
				cardStatusUIMap = new TreeMap<>();

				for (String string : availableCardStatus) {
					logger.debug(
						"Card status populate method avaliableCardStatus are " +
							string);
					cardStatusUIMap.put(string, cardStatusMap.get(string));
				}
			}
		}
		else if (isUserAllowedToUpdateNotToDeleteStatus(groups)) {
			logger.debug(
				"If Delete card permission is true, removing the delete card from map");

			if (null != nextAvailableStatus) {
				availableCardStatus = nextAvailableStatus;
			}
			else {
				availableCardStatus = new ArrayList<>();
			}

			logger.debug(
				"Card status populate method avaliableCardStatus size is  " +
					availableCardStatus.size());

			availableCardStatus.add(currentStatus);

			logger.debug(
				"Card status populate method  cardStatusMap  is  " +
					cardStatusMap);

			if (availableCardStatus.size() > 1) {
				isOneStatus = false;
				cardStatusUIMap = new TreeMap<>();

				for (String string : availableCardStatus) {
					logger.debug(
						"Card status populate method avaliableCardStatus are " +
							string);
					cardStatusUIMap.put(string, cardStatusMap.get(string));
				}

				logger.debug(
					"If Delete card permission is true, removing the delete card from map - Size BEFORE removal  " +
						cardStatusUIMap.size());
				cardStatusUIMap.remove("1B");
				logger.debug(
					"Delete card permission is true and removing the delete card from map - Size  AFTER removal  " +
						cardStatusUIMap.size());
			}
		}

		if (isOneStatus) {
			logger.debug(
				"Card status populate method Not Allowed to update card status so setting current status" +
					currentStatus);
			cardStatusUIMap = new TreeMap<>();
			cardStatusUIMap.put(
				currentStatus, cardStatusMap.get(currentStatus));
		}

		logger.debug(
			"Card status populate method setting the cardInformation data");
		cardInformation.setCardStatusCode(currentStatus);
		cardInformation.setCardStatus(cardStatusMap.get(currentStatus));
		cardInformation.setCardStatusMap(cardStatusUIMap);
	}

	@Autowired
	@Qualifier(CARD_SEARCH_APP_PROPERTIES)
	protected CardSearchAppProperties cardSearchAppProperties;

	/**
	 * Returns the Translated Channel Name that Alaric pass to us.
	 *
	 * @param alaricName
	 * @return
	 */
	private String cardChannelTranslatedName(String alaricName) {
		String translatedName = null;

		String[] channelNameKey = cardSearchAppProperties.getCardsProps(
		).getProperty(
			Constants.CHANNEL_PERMISSIONS_NAME_EARTH_KEY
		).split(
			Constants.COMMA
		);

		String[] channelNameValue = cardSearchAppProperties.getCardsProps(
		).getProperty(
			Constants.CHANNEL_PERMISSIONS_NAME_VALUE
		).split(
			Constants.COMMA
		);

		for (int i = 0; i < channelNameKey.length; i++) {
			String[] channelNameEarthKey = channelNameKey[i].split(
				Constants.COLON);

			for (int j = 0; j < channelNameEarthKey.length; j++) {
				if (equalsIgnoreCase(alaricName, channelNameEarthKey[j])) {
					translatedName = channelNameValue[i];
				}
			}
		}

		return translatedName;
	}

	/**
	 * Return the order in which the Channel Permissions should be displayed.
	 *
	 * @return Map<String, Integer>
	 */
	private Map<String, Integer> getChannelOrderMap() {
		String[] channelName = cardSearchAppProperties.getCardsProps(
		).getProperty(
			Constants.CHANNEL_PERMISSIONS_NAME_VALUE
		).split(
			Constants.COMMA
		);

		String[] channelOrder = cardSearchAppProperties.getCardsProps(
		).getProperty(
			Constants.CHANNEL_PERMISSIONS_ORDER
		).split(
			Constants.COMMA
		);

		Map<String, Integer> channelOrderMap = new HashMap<>();

		for (int i = 0; i < channelName.length; i++) {
			channelOrderMap.put(
				channelName[i], Integer.parseInt(channelOrder[i]));
		}

		return channelOrderMap;
	}

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(
		CardsRestTranslator.class);

	@Autowired
	@Qualifier(Constants.CARD_REST_SERVICE)
	private CardRestService cardRestService;

}