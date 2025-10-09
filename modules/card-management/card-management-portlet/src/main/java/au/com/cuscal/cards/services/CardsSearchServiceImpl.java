package au.com.cuscal.cards.services;

import au.com.cuscal.cards.commons.CardSearchAppProperties;
import au.com.cuscal.cards.commons.Constants;
import au.com.cuscal.cards.commons.Utility;
import au.com.cuscal.cards.commons.WebServiceException;
import au.com.cuscal.cards.commons.WebServiceFailureError;
import au.com.cuscal.cards.domain.Access;
import au.com.cuscal.cards.domain.AccountInformation;
import au.com.cuscal.cards.domain.CardChannelPermission;
import au.com.cuscal.cards.domain.CardHolder;
import au.com.cuscal.cards.domain.CardInformation;
import au.com.cuscal.cards.domain.CardLimits;
import au.com.cuscal.cards.domain.Cards;
import au.com.cuscal.cards.domain.StandardOverrideLimits;
import au.com.cuscal.cards.domain.TokenSearchResult;
import au.com.cuscal.cards.forms.CardSearchForm;
import au.com.cuscal.cards.services.client.LocalCardServiceImpl;
import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.common.framework.memory.PermGenUtil;
import au.com.cuscal.framework.audit.Audit;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.category.AuditCategories;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.audit.impl.log4j.Log4jAuditor;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.webservices.client.SchemeService;
import au.com.cuscal.framework.webservices.scheme.ActivateTokenRequest;
import au.com.cuscal.framework.webservices.scheme.ActivateTokenResponse;
import au.com.cuscal.framework.webservices.scheme.CommonRequestHeader;
import au.com.cuscal.framework.webservices.scheme.HandleTokenUpdateRequest;
import au.com.cuscal.framework.webservices.scheme.HandleTokenUpdateResponse;
import au.com.cuscal.framework.webservices.scheme.SearchTokensRequest;
import au.com.cuscal.framework.webservices.scheme.SearchTokensResponse;
import au.com.cuscal.framework.webservices.scheme.SourceChannelType;
import au.com.cuscal.framework.webservices.scheme.TokenDetailType;
import au.com.cuscal.framework.webservices.scheme.TokenLifecycleAction;
import au.com.cuscal.framework.webservices.scheme.TokenUpdateReason;
import au.com.cuscal.framework.webservices.transaction.AccountDetails;
import au.com.cuscal.framework.webservices.transaction.AuxAddresses;
import au.com.cuscal.framework.webservices.transaction.CardBasicInformation;
import au.com.cuscal.framework.webservices.transaction.CardStatus;
import au.com.cuscal.framework.webservices.transaction.ChannelPermissions;
import au.com.cuscal.framework.webservices.transaction.Customer;
import au.com.cuscal.framework.webservices.transaction.FindCardsRequestType;
import au.com.cuscal.framework.webservices.transaction.FindCardsResponseType;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdRequestType;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdResponseType;
import au.com.cuscal.framework.webservices.transaction.GetCardAccountsRequestType;
import au.com.cuscal.framework.webservices.transaction.GetCardAccountsResponseType;
import au.com.cuscal.framework.webservices.transaction.GetCardChannelPermissionsRequestType;
import au.com.cuscal.framework.webservices.transaction.GetCardChannelPermissionsResponseType;
import au.com.cuscal.framework.webservices.transaction.GetCardLimitsRequestType;
import au.com.cuscal.framework.webservices.transaction.GetCardLimitsResponseType;
import au.com.cuscal.framework.webservices.transaction.GetCardRequestType;
import au.com.cuscal.framework.webservices.transaction.GetCardResponseType;
import au.com.cuscal.framework.webservices.transaction.GetCardVigilBlockedRequestType;
import au.com.cuscal.framework.webservices.transaction.GetCardVigilBlockedResponseType;
import au.com.cuscal.framework.webservices.transaction.GetCustomerAccessViewRequestType;
import au.com.cuscal.framework.webservices.transaction.GetCustomerAccessViewResponseType;
import au.com.cuscal.framework.webservices.transaction.IsInPrimarySwitchModeRequestType;
import au.com.cuscal.framework.webservices.transaction.IsInPrimarySwitchModeResponseType;
import au.com.cuscal.framework.webservices.transaction.PanOrBinEnum;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;
import au.com.cuscal.framework.webservices.transaction.SwitchLimit;
import au.com.cuscal.framework.webservices.transaction.UpdateCardStatusRequestType;
import au.com.cuscal.framework.webservices.transaction.UpdateCardStatusResponseType;

import java.math.BigInteger;

import java.security.SecureRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.portlet.PortletRequest;

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
@Service(value = Constants.CARD_SEARCH_SERVICE)
public class CardsSearchServiceImpl implements CardsSearchService {

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(
		CardsSearchServiceImpl.class);

	/**
	 * Audit object
	 */
	private static final Auditor audit = Auditor.getInstance();
	private static SecureRandom random = new SecureRandom();

	private final LocalCardServiceImpl cardService = CuscalServiceLocator.getService(
			LocalCardServiceImpl.class.getName());

	@Autowired
	@Qualifier(Constants.CARD_SCHEME_SERVICE)
	private SchemeService schemeService;

	@Autowired
	@Qualifier("cardSearchAppProperties")
	private CardSearchAppProperties cardSearchAppProperties;

	/**
	 * @return the cardService
	 */
	public LocalCardServiceImpl getCardService() {
		return cardService;
	}

	/**
	 * Search Card List
	 *
	 * @param CardSearchForm
	 * @param SearchHeader
	 * @return List.Cards .
	 * @throws Exception
	 */
	public List<Cards> getCardListOnsearch(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		logger.debug("getCardListOnsearch - start");
		PermGenUtil.permGenDump("getCardListOnsearch - start");

		List<Cards> cards = null;

		FindCardsRequestType cardsRequestType = populateCardSearchParams(
			cardSearchForm, searchHeader);
		logger.debug(
			"getCardListOnsearch - calling the web service method for card search ");
		PermGenUtil.permGenDump(
			"getCardListOnsearch - webservice request object created");

		FindCardsResponseType cardResponseType = cardService.findCards(
			cardsRequestType);
		PermGenUtil.permGenDump("getCardListOnsearch - after webservice call");

		try {
			if (null != cardResponseType.getHeader()) {
				String statusCode = cardResponseType.getHeader(
				).getStatusCode();

				logger.debug(
					"getCardListOnsearch - the status code is = " + statusCode);

				if (Constants.SUCCESS.equalsIgnoreCase(statusCode)) {
					if (null != cardResponseType.getCards()
							//&& cardResponseType.getCards().size() > 0
							) {

						cards = populateCardListFromResponse(cardResponseType);
						cards.get(
							0
						).setTotalListSize(
							cardResponseType.getTotalRows()
						);

						logger.debug(
							"getCardListOnsearch - found " + cards.size() +
								" row(s)");
						audit.success(
							portletContext.getResponse(),
							portletContext.getRequest(),
							AuditOrigin.PORTAL_ORIGIN,
							AuditCategories.CARD_SEARCH,
							createSearchParamsStr(cardSearchForm) + ", found " +
								cards.size() + " row(s)");
					}
				}
				else if (Constants.RECORD_NOT_FOUND.equalsIgnoreCase(
							statusCode)) {

					audit.fail(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_SEARCH,
						createSearchParamsStr(cardSearchForm) +
							" soap call returned null object on card search and status code is =" +
								statusCode);
				}
				else if (Constants.NOT_SUCCESSFUL.equalsIgnoreCase(
							statusCode) ||
						 Constants.VALIDATION_FAILURE.equalsIgnoreCase(
							 statusCode)) {

					logger.error(
						"getCardListOnsearch - status code is = " + statusCode);

					audit.fail(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_SEARCH,
						createSearchParamsStr(cardSearchForm) +
							"CARD_SEARCH -  WebServiceFailureError due to status code in response header is = " +
								statusCode);

					throw new WebServiceFailureError();
				}
				else if (Constants.SYSTEM_ERROR.equalsIgnoreCase(statusCode) ||
						 Constants.HEARTBEAT_CONNECTIVITY_ERROR.
							 equalsIgnoreCase(statusCode)) {

					logger.error(
						"getCardListOnsearch - status code is = " + statusCode);
					audit.fail(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_SEARCH,
						createSearchParamsStr(cardSearchForm) +
							"CARD_SEARCH - WebServiceException due to status code in response header is = " +
								statusCode);

					throw new WebServiceException();
				}
			}
			else {
				logger.error(
					"The header for card search is null, so system assume some unknown for SYSTEM_ERROR");

				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_SEARCH,
					createSearchParamsStr(cardSearchForm) +
						"CARD_SEARCH -  WebServiceException due to NULL Header and so status code assumed to be : SYSTEM_ERROR");

				throw new WebServiceException();
			}
		}
		finally {
			logger.debug("getCardListOnsearch - end");
			PermGenUtil.permGenDump("getCardListOnsearch - end");
		}

		return cards;
	}

	/**
	 * Get Card Information details object
	 *
	 * @param cardId
	 * @param SearchHeader
	 * @return CardInformation .
	 */
	public CardInformation getCardInformationOnCardDetails(
			String cardId, SearchHeader searchHeader, List<String> groups,
			PortletContext portletContext)
		throws Exception {

		CardInformation cardInformation = null;

		logger.debug("getCardInformationOnCardDetails - start");
		PermGenUtil.permGenDump("getCardInformationOnCardDetails - start");

		GetCardRequestType cardRequestType = new GetCardRequestType();

		cardRequestType.setCardId(new BigInteger(cardId));
		cardRequestType.setHeader(searchHeader);

		if (Utility.isUserBelongToCsuAdminUserGroup(
				portletContext.getRequest(),
				cardSearchAppProperties.getCardsProps(
				).getProperty(
					Constants.CARD_STATUS_UPDATE_ADMIN_PROPKEY
				))) {

			cardRequestType.setShowAllStatus(true);
		}

		PermGenUtil.permGenDump(
			"getCardInformationOnCardDetails - created webservice request object");

		GetCardResponseType cardResponseType = cardService.getCard(
			cardRequestType);
		logger.debug(
			"getCardInformationOnCardDetails - Fininished Calling WebService Card" +
				" information and populateCardInformationFromResponse method");
		PermGenUtil.permGenDump(
			"getCardInformationOnCardDetails - after webservice call");

		try {
			if (null != cardResponseType.getHeader()) {
				String statusCode = cardResponseType.getHeader(
				).getStatusCode();

				logger.info(
					"[getCardInformationOnCardDetails] the response header is not null, the status code is  " +
						statusCode);

				if (Constants.SUCCESS.equalsIgnoreCase(statusCode)) {
					logger.debug(
						"In getCardInformationOnCardDetails -  Card information method as status code is =" +
							statusCode);

					cardInformation = populateCardInformationFromResponse(
						cardResponseType, groups, searchHeader);

					cardInformation = cardVigilBlockedStatus(
						cardInformation, cardRequestType);

					audit.success(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						" Card Information details called successfully for card.crd_id =" +
							cardId);
				}
				else if (Constants.NOT_SUCCESSFUL.equalsIgnoreCase(
							statusCode) ||
						 Constants.VALIDATION_FAILURE.equalsIgnoreCase(
							 statusCode) ||
						 Constants.RECORD_NOT_FOUND.equalsIgnoreCase(
							 statusCode)) {

					logger.error(
						"The header for card detail - CardInformation -status code NOT_SUCCESSFUL or VALIDATION_FAILURE or RECORD_NOT_FOUND issue, status code is " +
							statusCode);
					audit.fail(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						" Card Information details -  Failed to called for card.crd_id =" +
							cardId +
								" the status code in response header is =" +
									statusCode);

					throw new WebServiceFailureError();
				}
				else if (Constants.SYSTEM_ERROR.equalsIgnoreCase(statusCode) ||
						 Constants.HEARTBEAT_CONNECTIVITY_ERROR.
							 equalsIgnoreCase(statusCode)) {

					logger.error(
						"The header for card detail - CardInformation -status code SYSTEM_ERROR or HEARTBEAT_CONNECTIVITY_ERROR issue, status code is " +
							statusCode);
					audit.fail(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						" Card Information details - throw Exception to called for card.crd_id =" +
							cardId +
								" and the status code in response header is =" +
									statusCode);

					throw new WebServiceException();
				}
			}
			else {
				logger.error(
					"The header for card detail - CardInformation -  is null, so system assume some unknown for SYSTEM_ERROR");
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_DETAIL,
					" Card Information details - The response header is NULL while called for card.crd_id =" +
						cardId +
							" and the status code assumed to be the default, which is SYSTEM_ERROR");

				throw new WebServiceException();
			}
		}
		finally {
			logger.debug("getCardInformationOnCardDetails - end");
			PermGenUtil.permGenDump("getCardInformationOnCardDetails - end");
		}

		return cardInformation;
	}

	/**
	 * @param cardId
	 * @param cardInformation
	 * @param cardRequestType
	 */
	private CardInformation cardVigilBlockedStatus(
		CardInformation cardInformation, GetCardRequestType cardRequestType) {

		GetCardVigilBlockedRequestType cardBlockedRequest =
			Utility.convertToVigilBlockedRequest(cardRequestType);

		cardBlockedRequest.setCardId(cardInformation.getCardId());
		GetCardVigilBlockedResponseType cardBlockedResponse =
			cardService.getCardVigilBlocked(cardBlockedRequest);

		if (null != cardBlockedResponse) {
			if ((cardBlockedResponse.getHeader() != null) &&
				StringUtils.equalsIgnoreCase(
					Constants.SUCCESS,
					cardBlockedResponse.getHeader(
					).getStatusCode())) {

				cardInformation.setVigilBlocked(
					cardBlockedResponse.isIsVigilBlocked());
			}
			else {
				logger.error(
					"cardVigilBlockedStatus - Could not get Vigil blocked status for the card with cardId: " +
						cardInformation.getCardId());
			}
		}
		else {
			logger.error(
				"cardVigilBlockedStatus - Could not get Vigil blocked status for the card with cardId: " +
					cardInformation.getCardId());
		}

		return cardInformation;
	}

	/**
	 * Get Card Limits details object.
	 *
	 * @param cardId
	 * @param SearchHeader
	 * @return CardInformation
	 */
	public CardLimits getCardLimitsOnCardDetails(
			String cardId, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		PermGenUtil.permGenDump("getCardLimitsOnCardDetails - start");

		CardLimits cardLimits = null;

		GetCardLimitsRequestType cardLimitsRequestType =
			new GetCardLimitsRequestType();

		cardLimitsRequestType.setCardId(new BigInteger(cardId));
		cardLimitsRequestType.setHeader(searchHeader);
		PermGenUtil.permGenDump(
			"getCardAccountInformationOnDetails - request object created");

		GetCardLimitsResponseType cardLimitsResponseType =
			cardService.getCardLimits(cardLimitsRequestType);
		PermGenUtil.permGenDump(
			"getCardAccountInformationOnDetails - webservice returned");

		try {
			if (null != cardLimitsResponseType.getHeader()) {
				String statusCode = cardLimitsResponseType.getHeader(
				).getStatusCode();

				logger.debug(
					"getCardLimitsOnCardDetails - the response header is not null, the status code is =" +
						statusCode);

				if (Constants.SUCCESS.equalsIgnoreCase(statusCode)) {
					logger.debug(
						"getCardLimitsOnCardDetails - the response header is not null, the status code is =" +
							statusCode);
					cardLimits = populateCardLimitsFromResponse(
						cardLimitsResponseType);
					audit.success(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						" Card Limits details called for card.crd_id =" +
							cardId +
								" and the status code in response header is =" +
									statusCode);
				}
				else if (Constants.NOT_SUCCESSFUL.equalsIgnoreCase(
							statusCode)) {

					cardLimits = null;
					audit.fail(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						" Card Limits details Failed to called for card.crd_id =" +
							cardId +
								"  and the failed status code in response header is =" +
									statusCode);
				}
				else if (Constants.VALIDATION_FAILURE.equalsIgnoreCase(
							statusCode)) {

					logger.error(
						"The header for card detail - CardLimitsOnCardDetails status code can be - NOT_SUCCESSFUL or VALIDATION_FAILURE, the status code is =" +
							statusCode);

					audit.fail(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						" Card Limits details Failed to called for card.crd_id =" +
							cardId +
								"  and the failed status code in response header is =" +
									statusCode);

					throw new WebServiceFailureError();
				}
				else if (Constants.SYSTEM_ERROR.equalsIgnoreCase(statusCode) ||
						 Constants.HEARTBEAT_CONNECTIVITY_ERROR.
							 equalsIgnoreCase(statusCode)) {

					logger.error(
						"The header for card detail - CardLimitsOnCardDetails status code can be - SYSTEM_ERROR or HEARTBEAT_CONNECTIVITY_ERROR, the status code is =" +
							statusCode);
					audit.fail(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						" Card Limits details Failed to called for card.crd_id =" +
							cardId +
								"  and exception the status code in response header is =" +
									statusCode);

					throw new WebServiceException();
				}
			}
			else {
				logger.error(
					"The header for card detail - CardLimitsOnCardDetails -  is null, so system assume some unknown for SYSTEM_ERROR");
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_DETAIL,
					" Card Limits details Failed to called for card.crd_id =" +
						cardId +
							"  and  response header is null, so system assume some unknown for SYSTEM_ERROR ");

				throw new WebServiceException();
			}
		}
		finally {
			PermGenUtil.permGenDump("getCardLimitsOnCardDetails - end");
		}

		return cardLimits;
	}

	/**
	 * Get Card Account Information details object.
	 *
	 * @param cardId
	 * @param SearchHeader
	 * @return accountInformations
	 */
	public List<AccountInformation> getCardAccountInformationOnDetails(
			String cardId, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		PermGenUtil.permGenDump("getCardAccountInformationOnDetails - start");
		List<AccountInformation> accountInformations = null;

		GetCardAccountsRequestType accountsRequestType =
			new GetCardAccountsRequestType();

		accountsRequestType.setCardId(new BigInteger(cardId));
		accountsRequestType.setHeader(searchHeader);
		PermGenUtil.permGenDump(
			"getCardAccountInformationOnDetails - request object created");

		GetCardAccountsResponseType accountsResponseType =
			cardService.getCardAccounts(accountsRequestType);
		PermGenUtil.permGenDump(
			"getCardAccountInformationOnDetails - after webservice call");

		try {
			if (null != accountsResponseType.getHeader()) {
				String statusCode = accountsResponseType.getHeader(
				).getStatusCode();

				logger.info(
					"The header for card detail -AccountInformation - the response header is not null, the status code is =" +
						statusCode);

				if (Constants.SUCCESS.equalsIgnoreCase(statusCode)) {
					logger.debug(
						"The header for card detail -AccountInformation - the response header is not null, the status code is =" +
							statusCode);
					accountInformations = populatesCardAccountFromResponse(
						accountsResponseType);
					audit.success(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						" Card AccountInformation details called for card.crd_id =" +
							cardId +
								" and the status code in response header is =" +
									statusCode);
				}
				else if (Constants.NOT_SUCCESSFUL.equalsIgnoreCase(
							statusCode) ||
						 Constants.VALIDATION_FAILURE.equalsIgnoreCase(
							 statusCode)) {

					logger.error(
						"The header for card detail -AccountInformation status code can be - NOT_SUCCESSFUL or VALIDATION_FAILURE, the status code is =" +
							statusCode);
					audit.fail(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						" Card AccountInformation details Failed to called for card.crd_id =" +
							cardId +
								"  and Failed the status code in response header is =" +
									statusCode);

					throw new WebServiceFailureError();
				}
				else if (Constants.SYSTEM_ERROR.equalsIgnoreCase(statusCode) ||
						 Constants.HEARTBEAT_CONNECTIVITY_ERROR.
							 equalsIgnoreCase(statusCode)) {

					logger.error(
						"The header for card detail -AccountInformation  status code can be - SYSTEM_ERROR or HEARTBEAT_CONNECTIVITY_ERROR, the status code is =" +
							statusCode);
					audit.fail(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						" Card AccountInformation details Failed to called for card.crd_id =" +
							cardId +
								"  and exception the status code in response header is =" +
									statusCode);

					throw new WebServiceException();
				}
			}
			else {
				logger.error(
					"The header for card detail - AccountInformation -  is null, so system assume some unknown for SYSTEM_ERROR");
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_DETAIL,
					" Card AccountInformation details Failed to called for card.crd_id =" +
						cardId +
							"  and  response header is null, so system assume some unknown for SYSTEM_ERROR ");

				throw new WebServiceException();
			}
		}
		finally {
			PermGenUtil.permGenDump(
				"getCardAccountInformationOnDetails - start");
		}

		return accountInformations;
	}

	/**
	 * Populate AccountInformation Object from accountsResponseType
	 *
	 * @param accountsResponseType
	 * @return List<AccountInformation>
	 */
	public List<AccountInformation> populatesCardAccountFromResponse(
		GetCardAccountsResponseType accountsResponseType) {

		List<AccountInformation> accountInformations = new ArrayList<>();

		if (null != accountsResponseType) {
			if ((null != accountsResponseType.getAccounts()) &&
				(0 < accountsResponseType.getAccounts(
				).size())) {

				List<AccountDetails> accountDetails =
					accountsResponseType.getAccounts();

				for (AccountDetails accountDetailsObj : accountDetails) {
					AccountInformation accountInformation =
						new AccountInformation();

					if (StringUtils.isNotBlank(accountsResponseType.getPan()))
						accountInformation.setPan(
							accountsResponseType.getPan());

					if (null != accountsResponseType.getIssuer())
						accountInformation.setInstitution(
							accountsResponseType.getIssuer(
							).getId() + " - " +
								accountsResponseType.getIssuer(
								).getName());

					if (StringUtils.isNotBlank(
							accountDetailsObj.getAccountType()))
						accountInformation.setAccountType(
							accountDetailsObj.getAccountType());

					if (StringUtils.isNotBlank(
							accountDetailsObj.getAccountNumber()))
						accountInformation.setAccountNumber(
							accountDetailsObj.getAccountNumber());

					if (StringUtils.isNotBlank(
							accountDetailsObj.getAccountQualifier()))
						accountInformation.setAccountQualifier(
							accountDetailsObj.getAccountQualifier());

					if (accountDetailsObj.isIsFundingAccount()) {
						accountInformation.setFundingAccount("Yes");
					}
					else {
						accountInformation.setFundingAccount("No");
					}

					if (accountDetailsObj.isIsPrimaryAccount()) {
						accountInformation.setPrimaryAccount("Yes");
					}
					else {
						accountInformation.setPrimaryAccount("No");
					}

					if (accountDetailsObj.getLedgerBalance() != null) {
						accountInformation.setLedgerBalance(
							accountDetailsObj.getLedgerBalance(
							).toString());
					}
					else {
						accountInformation.setLedgerBalance(
							Constants.NOT_AVAILABLE);
					}

					if (accountDetailsObj.getAvailableBalance() != null) {
						accountInformation.setAvailableBalance(
							accountDetailsObj.getAvailableBalance(
							).toString());
					}
					else {
						accountInformation.setAvailableBalance(
							Constants.NOT_AVAILABLE);
					}

					logger.debug(
						"[populatesCardAccountFromResponse] available balance: " +
							accountDetailsObj.getAvailableBalance());

					if (accountDetailsObj.getCreditLine() != null) {
						accountInformation.setCreditLine(
							accountDetailsObj.getCreditLine(
							).toString());
					}
					else {
						accountInformation.setCreditLine(
							Constants.NOT_AVAILABLE);
					}

					logger.debug(
						"[populatesCardAccountFromResponse] the Last updated date for the account information is  " +
							accountDetailsObj.getLastUpdated());

					if (StringUtils.isNotBlank(
							accountDetailsObj.getLastUpdated())) {

						accountInformation.setLastUpdated(
							Utility.formatDate(
								accountDetailsObj.getLastUpdated(),
								Constants.DATE_24_HOUR_MINUTE_SECOND_FORMAT));
					}

					logger.debug(
						"[populatesCardAccountFromResponse] the Final Last updated date for the account information is  " +
							accountInformation.getLastUpdated());

					accountInformations.add(accountInformation);
				}
			}
		}

		return accountInformations;
	}

	/**
	 * Populate CardLimits Object from cardResposeType
	 *
	 * @param GetCardLimitsResponseType
	 * @return CardInformation .
	 */
	public CardLimits populateCardLimitsFromResponse(
		GetCardLimitsResponseType cardLimitsResponseType) {

		CardLimits cardLimits = new CardLimits();

		List<StandardOverrideLimits> standardOverrideLimits = new ArrayList<>();

		StandardOverrideLimits overrideLimits = null;

		if (StringUtils.isNotBlank(cardLimitsResponseType.getPan()))
			cardLimits.setPan(cardLimitsResponseType.getPan());

		if (null != cardLimitsResponseType.getIssuer())
			cardLimits.setInstitution(
				cardLimitsResponseType.getIssuer(
				).getId() + " " + Constants.DASH + " " +
					cardLimitsResponseType.getIssuer(
					).getName());

		if ((null != cardLimitsResponseType.getStandardOverrideLimits()) &&
			(0 < cardLimitsResponseType.getStandardOverrideLimits(
			).size())) {

			List<SwitchLimit> switchLimit =
				cardLimitsResponseType.getStandardOverrideLimits();

			for (SwitchLimit switchLimitObj : switchLimit) {
				overrideLimits = new StandardOverrideLimits();

				if (switchLimitObj.getLimitAmount() != null) {
					overrideLimits.setLimitAmount(
						switchLimitObj.getLimitAmount(
						).toString());
				}
				else {
					overrideLimits.setLimitAmount(Constants.DASH);
				}

				if (switchLimitObj.getLimitCount() != null) {
					overrideLimits.setLimitCount(
						switchLimitObj.getLimitCount(
						).toString());
				}
				else {
					overrideLimits.setLimitCount(Constants.DASH);
				}

				overrideLimits.setLimitType(switchLimitObj.getLimitType());

				if (switchLimitObj.getUsageAmount() != null) {
					overrideLimits.setUsageAmount(
						switchLimitObj.getUsageAmount(
						).toString());
				}
				else {
					overrideLimits.setUsageAmount(Constants.DASH);
				}

				if (switchLimitObj.getUsageCount() != null) {
					overrideLimits.setUsageCount(
						switchLimitObj.getUsageCount(
						).toString());
				}
				else {
					overrideLimits.setUsageCount(Constants.DASH);
				}

				standardOverrideLimits.add(overrideLimits);
			}
		}

		cardLimits.setStandardOverrideLimits(standardOverrideLimits);

		return cardLimits;
	}

	/**
	 * Returns the Card Channel Permissions for the card from the WebService.
	 *
	 * @param searchHeader
	 * @param cardId
	 * @return List
	 */
	private List<ChannelPermissions> cardChannelPermissions(
		SearchHeader searchHeader, BigInteger cardId,
		CardInformation cardInformation) {

		PermGenUtil.permGenDump("cardChannelPermissions - start");
		GetCardChannelPermissionsRequestType cardChannelPermissionsRequest =
			new GetCardChannelPermissionsRequestType();

		cardChannelPermissionsRequest.setHeader(searchHeader);
		cardChannelPermissionsRequest.setCardId(cardId);
		PermGenUtil.permGenDump(
			"cardChannelPermissions - webservice request object created");

		GetCardChannelPermissionsResponseType cardChannelPermissionsResponse =
			cardService.getCardChannelPermissions(
				cardChannelPermissionsRequest);
		PermGenUtil.permGenDump(
			"cardChannelPermissions - after webservice call");

		List<ChannelPermissions> cardChannelPermissions = new ArrayList<>();

		try {

			// Handle the errors from the Web Service. If there is anything besides
			// SUCCESS passed back or if the header is not returned then display the
			// error message.

			if (cardChannelPermissionsResponse.getHeader() != null) {
				String channelPermissionsStatusMessage =
					cardChannelPermissionsResponse.getHeader(
					).getStatusCode();

				if (StringUtils.isNotBlank(channelPermissionsStatusMessage) &&
					StringUtils.equalsIgnoreCase(
						channelPermissionsStatusMessage, Constants.SUCCESS)) {

					cardChannelPermissions =
						cardChannelPermissionsResponse.getChannels();
				}
				else {

					// Set the error message to display on the screen.

					logger.debug(
						"[cardChannelPermissions] A System error occured: " +
							cardChannelPermissionsResponse.getHeader(
							).getStatusCode());
					cardInformation.setCardChannelPermissionsMessage(
						Constants.CHANNEL_PERMISSIONS_ERROR);
				}
			}
			else {

				// Set the error message to display on the screen.

				logger.debug(
					"[cardChannelPermissions] Unspecified error message. No header was returned.");
				cardInformation.setCardChannelPermissionsMessage(
					Constants.CHANNEL_PERMISSIONS_ERROR);
			} // END if (cardChannelPermissionsResponse.getHeader() != null)
		}
		finally {
			PermGenUtil.permGenDump("cardChannelPermissions - end");
		}

		return cardChannelPermissions;
	}

	/**
	 * Populate CardInformation Object from cardResposeType
	 *
	 * @param GetCardResponseType
	 * @param groups
	 * @param searchHeader
	 * @return CardInformation .
	 */
	public CardInformation populateCardInformationFromResponse(
			GetCardResponseType cardResponseType, List<String> groups,
			SearchHeader searchHeader)
		throws Exception {

		CardInformation cardInformation = null;

		if (null != cardResponseType) {
			cardInformation = new CardInformation();

			if (cardResponseType.getCardId() != null)
				cardInformation.setCardId(cardResponseType.getCardId());

			logger.debug(" The Card Information response type is not null");

			if (StringUtils.isNotBlank(cardResponseType.getPan()))
				cardInformation.setPan(cardResponseType.getPan());

			if (cardResponseType.getIssuer() != null)
				cardInformation.setInstitution(
					cardResponseType.getIssuer(
					).getId() + " - " +
						cardResponseType.getIssuer(
						).getName());

			if (StringUtils.isNotBlank(cardResponseType.getCardHolderName()) &&
				(cardResponseType.getAddresses() != null)) {

				CardHolder cardHolder = new CardHolder();
				String address = createAddressFromAuxAddress(
					cardResponseType.getAddresses());

				if (StringUtils.isNotBlank(cardResponseType.getPostcode())) {
					logger.debug(
						" The Card Information response type is not null and post code is " +
							cardResponseType.getPostcode());
					address =
						address + Constants.COMMA_SPACE +
							cardResponseType.getPostcode();
				}

				cardHolder.setCardHolderName(
					cardResponseType.getCardHolderName());
				logger.debug(
					" The Card Information response type - the final address string is  " +
						address);
				cardHolder.setCardHolderAddress(address);

				cardInformation.setCardHolder(cardHolder);
			}

			if (null != cardResponseType.getCardStatus()) {
				CardStatus cardStatus = cardResponseType.getCardStatus();

				populateCardStatusMapForUI(
					cardStatus, cardInformation, groups, searchHeader);
			}
			else {
				cardInformation.setCardStatus(Constants.UNKNOWN);
			}

			if (StringUtils.isNotBlank(cardResponseType.getCardExpiryDate()))
				cardInformation.setExpiryDate(
					Utility.formatExpiryDate(
						cardResponseType.getCardExpiryDate()));

			if (StringUtils.isNotBlank(cardResponseType.getPinCountFailures()))
				cardInformation.setPinCountFailures(
					cardResponseType.getPinCountFailures());

			if (StringUtils.isNotBlank(cardResponseType.getLastUsedTimestamp()))
				cardInformation.setLastUsed(
					Utility.formatDate(
						cardResponseType.getLastUsedTimestamp(),
						Constants.DATE_TIMESTAMP_24_HOUR_MILLISECONDS));

			if (StringUtils.isNotBlank(
					cardResponseType.getLastUpdatedTimestamp())) {

				cardInformation.setLastMaintained(
					Utility.formatDate(
						cardResponseType.getLastUpdatedTimestamp(),
						Constants.DATE_TIMESTAMP_24_HOUR_MILLISECONDS));
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

			if (StringUtils.isNotBlank(
					cardResponseType.getVisaExceptionResponseCode()))
				cardInformation.setSwitchResponseCode(
					cardResponseType.getVisaExceptionResponseCode());

			if (StringUtils.isNotBlank(
					cardResponseType.getVisaExceptionResponseDescription()))
				cardInformation.setResponseDescription(
					cardResponseType.getVisaExceptionResponseDescription());

			if (StringUtils.isNotBlank(
					cardResponseType.getVisaExceptionReference()))
				cardInformation.setVisaReference(
					cardResponseType.getVisaExceptionReference());

			List<CardChannelPermission> cardChannelPermissions =
				new ArrayList<>();

			for (ChannelPermissions permission :
					cardChannelPermissions(
						searchHeader, cardInformation.getCardId(),
						cardInformation)) {

				CardChannelPermission cardChannelPermission =
					new CardChannelPermission();

				cardChannelPermission.setChannelPermissionName(
					cardChannelTranslatedName(permission.getChannelName()));
				cardChannelPermission.setAccessTypes(
					populateCardAccessTypeWithPermission(
						permission.getPermissions()));

				cardChannelPermissions.add(cardChannelPermission);
			}

			//Ordering the Channel Names using the channel order from the properties file.
			final Map<String, Integer> channelOrderMap = getChannelOrderMap();
			Collections.sort(
				cardChannelPermissions,
				new Comparator<CardChannelPermission>() {

					@Override
					public int compare(
						CardChannelPermission c1, CardChannelPermission c2) {

						return channelOrderMap.get(
							c1.getChannelPermissionName()
						).compareTo(
							channelOrderMap.get(c2.getChannelPermissionName())
						);
					}

				});

			cardInformation.setCardPermissions(cardChannelPermissions);

			logger.debug(" Finished the populate method ");
		}

		return cardInformation;
	}

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
			Constants.CHANNEL_PERMISSIONS_NAME_KEY
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
			if (StringUtils.equalsIgnoreCase(alaricName, channelNameKey[i])) {
				translatedName = channelNameValue[i];
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
	 * Populate CardStatus Map For UI display
	 *
	 * @param cardStatus
	 * @param cardInformation
	 * @param searchHeder
	 */
	public void populateCardStatusMapForUI(
			CardStatus cardStatus, CardInformation cardInformation,
			List<String> groups, SearchHeader searchHeader)
		throws Exception {

		Map<String, String> cardStatusMap = getStatusMap();
		Map<String, String> cardStatusUIMap = null;
		List<String> avaliableCardStatus = null;
		String currentStatus = cardStatus.getCurrentStatus();
		boolean isOneStatus = true;

		boolean checkForSecondarySwitchMode = Boolean.valueOf(
			cardSearchAppProperties.getCardsProps(
			).getProperty(
				Constants.CHECK_SECONDARY_SWITCH
			));

		logger.debug(
			"[populateCardStatusMapForUI] Is the switch in secondary mode: " +
				checkForSecondarySwitchMode);

		// Should we check for cards that have not been ported over to our
		// Switch.

		if (checkForSecondarySwitchMode) {
			if (StringUtils.isNotBlank(cardInformation.getPan())) {

				// Check if the card has been ported over to our switch or not.

				IsInPrimarySwitchModeRequestType switchInPrimaryModeRequest =
					new IsInPrimarySwitchModeRequestType();

				switchInPrimaryModeRequest.setPan(cardInformation.getPan());
				switchInPrimaryModeRequest.setHeader(searchHeader);

				IsInPrimarySwitchModeResponseType switchInPrimaryModeResponse =
					cardService.isInPrimarySwitchMode(
						switchInPrimaryModeRequest);

				// Check for a blank Header or if the Web Service returned
				// SUCCESS in the header status code.

				if (switchInPrimaryModeResponse.getHeader() != null) {
					String switchInPrimaryModeResponseStatus =
						switchInPrimaryModeResponse.getHeader(
						).getStatusCode();

					if (StringUtils.isNotBlank(
							switchInPrimaryModeResponseStatus) &&
						StringUtils.equalsIgnoreCase(
							switchInPrimaryModeResponseStatus,
							Constants.SUCCESS)) {

						cardInformation.setHasCardPortedToPrimarySwitch(
							switchInPrimaryModeResponse.
								isIsInPrimarySwitchMode());
					}
					else {
						cardInformation.setCardStatusMessage(
							Constants.CARD_UPDATE_DISABLED_ERROR);
					}
				}
				else {
					cardInformation.setCardStatusMessage(
						Constants.CARD_UPDATE_DISABLED_ERROR);
				}
			}
		}
		else {
			cardInformation.setHasCardPortedToPrimarySwitch(true);
		}	// if (checkForSecondarySwitchMode) {

		logger.debug(
			"[populateCardStatusMapForUI] Card Information primary Switch status: " +
				cardInformation.isHasCardPortedToPrimarySwitch());

		//NEED TO REMOVE BEFORE DEPLOYMENT
		cardInformation.setHasCardPortedToPrimarySwitch(true);
		//NEED TO REMOVE BEFORE DEPLOYMENT

		if (isUserAllowedToUpdateStatus(groups)) {
			logger.debug(
				"If card status update card permission is true, adding all card status to map from avaliable status list");

			if (null != cardStatus.getNextAvailableStatus()) {
				avaliableCardStatus = cardStatus.getNextAvailableStatus();
			}
			else {
				avaliableCardStatus = new ArrayList<>();
			}

			logger.debug(
				"Card status populate method avaliableCardStatus size is  " +
					avaliableCardStatus.size());
			avaliableCardStatus.add(currentStatus);
			logger.debug(
				"Card status populate method  cardStatusMap  is  " +
					cardStatusMap);

			if (avaliableCardStatus.size() > 1) {
				isOneStatus = false;
				cardStatusUIMap = new TreeMap<>();

				for (String string : avaliableCardStatus) {
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

			if (null != cardStatus.getNextAvailableStatus()) {
				avaliableCardStatus = cardStatus.getNextAvailableStatus();
			}
			else {
				avaliableCardStatus = new ArrayList<>();
			}

			logger.debug(
				"Card status populate method avaliableCardStatus size is  " +
					avaliableCardStatus.size());

			avaliableCardStatus.add(currentStatus);

			logger.debug(
				"Card status populate method  cardStatusMap  is  " +
					cardStatusMap);

			if (avaliableCardStatus.size() > 1) {
				isOneStatus = false;
				cardStatusUIMap = new TreeMap<>();

				for (String string : avaliableCardStatus) {
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
	 * Populate Card Access with permissions
	 *
	 * @param List
	 * @return List
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
	 * Populate Card Search list object from web service response object
	 *
	 * @param FindCardsResponseType
	 * @return List
	 */
	public List<Cards> populateCardListFromResponse(
		FindCardsResponseType cardResponseType) {

		List<Cards> cards = new ArrayList<>();
		logger.info(
			"[populateCardListFromResponse] inside the find cards list populate method");
		List<CardBasicInformation> cardBasic = cardResponseType.getCards();

		for (CardBasicInformation cardBasicInformation : cardBasic) {
			Cards card = new Cards();

			if (null != cardBasicInformation.getAddresses()) {
				AuxAddresses addresses = cardBasicInformation.getAddresses();

				String address = createAddressFromAuxAddress(addresses);

				if (StringUtils.isNotBlank(cardBasicInformation.getPostcode()))
					address =
						address + Constants.COMMA_SPACE +
							cardBasicInformation.getPostcode();

				card.setAddress(address);
			}

			if (cardBasicInformation.getCardExpiryDate() != null) {
				card.setExpiryDate(
					Utility.formatExpiryDate(
						cardBasicInformation.getCardExpiryDate()));
			}

			if (null != cardBasicInformation.getCardHolderName()) {
				card.setCardHolder(cardBasicInformation.getCardHolderName());
			}

			if (null != cardBasicInformation.getCardId()) {
				card.setCardId(
					cardBasicInformation.getCardId(
					).toString());
			}

			if (null != cardBasicInformation.getIssuer()) {
				card.setInstitution(
					cardBasicInformation.getIssuer(
					).getId() + "<br>" +
						cardBasicInformation.getIssuer(
						).getName());
			}

			if (null != cardBasicInformation.getCardLastUpdated()) {
				card.setLastUpdated(
					Utility.formatDate(
						cardBasicInformation.getCardLastUpdated(),
						Constants.DATE_TIMESTAMP_24_HOUR_MILLISECONDS));
			}

			if (null != cardBasicInformation.getPan()) {
				card.setPan(cardBasicInformation.getPan());
			}

			if (null != cardBasicInformation.getStatusCode()) {
				if (StringUtils.isNotBlank(
						cardBasicInformation.getStatusCode())) {

					Map<String, String> cardStatusCodes = getStatusMap();

					card.setStatus(
						cardStatusCodes.get(
							cardBasicInformation.getStatusCode()));
				}
				else {
					card.setStatus(Constants.UNKNOWN);
				}
			}
			else {
				card.setStatus(Constants.UNKNOWN);
			}

			cards.add(card);
		}

		logger.info(
			"[populateCardListFromResponse] returning the card of size " +
				cards.size());

		return cards;
	}

	/**
	 * Create Card holder address from AuXAddress object .
	 *
	 * @param AuxAddresses
	 * @return String .
	 */
	public String createAddressFromAuxAddress(AuxAddresses addresses) {
		StringBuilder builder = new StringBuilder();

		if (StringUtils.isNotBlank(addresses.getAddress1())) {
			builder.append(
				addresses.getAddress1(
				).trim());
		}

		if (StringUtils.isNotBlank(addresses.getAddress2())) {
			builder.append(
				Constants.COMMA_SPACE +
					addresses.getAddress2(
					).trim());
		}

		if (StringUtils.isNotBlank(addresses.getAddress3())) {
			builder.append(
				Constants.COMMA_SPACE +
					addresses.getAddress3(
					).trim());
		}

		if (StringUtils.isNotBlank(addresses.getAddress4())) {
			builder.append(
				Constants.COMMA_SPACE +
					addresses.getAddress4(
					).trim());
		}

		if (StringUtils.isNotBlank(addresses.getAddress5())) {
			builder.append(
				Constants.COMMA_SPACE +
					addresses.getAddress5(
					).trim());
		}

		return builder.toString();
	}

	/**
	 * Populate card Search web service request object from card search form
	 * object .
	 *
	 * @param CardSearchForm
	 * @param SearchHeader
	 * @return FindCardsRequestType .
	 */
	public FindCardsRequestType populateCardSearchParams(
		CardSearchForm cardSearchForm, SearchHeader searchHeader) {

		FindCardsRequestType cardsRequestType = new FindCardsRequestType();

		logger.info(
			" in the Setting the search header in the request org id is  " +
				searchHeader.getUserOrgId());

		logger.info(
			" in the Setting the search header in the request Page Number is  " +
				searchHeader.getPageNumber());

		cardsRequestType.setHeader(searchHeader);

		if (StringUtils.isNotBlank(cardSearchForm.getPanOrBin())) {
			cardsRequestType.setPanOrBin(
				Utility.removeAllSpacesFromPan(cardSearchForm.getPanOrBin()));

			Integer panBinCount = cardSearchForm.getPanOrBin(
			).length();

			if (panBinCount > 10) {
				cardsRequestType.setIsPanOrBin(PanOrBinEnum.PAN);
			}
			else {
				cardsRequestType.setIsPanOrBin(PanOrBinEnum.BIN);
			}
		}

		if (StringUtils.isNotBlank(cardSearchForm.getIssuerShortName()))
			cardsRequestType.setIssuer(cardSearchForm.getIssuerShortName());

		if (StringUtils.isNotBlank(cardSearchForm.getCardholderName()))
			cardsRequestType.setLastName(cardSearchForm.getCardholderName());

		if (StringUtils.isNotBlank(cardSearchForm.getPostCode()))
			cardsRequestType.setPostcode(cardSearchForm.getPostCode());

		cardsRequestType.setCardExpiryDate(
			cardSearchForm.getNormalisedExpiry());

		logger.info(
			"cardSearchForm.isNonExpired(): " + cardSearchForm.isNonExpired());

		if (cardSearchForm.isNonExpired()) {
			cardsRequestType.setIncludeExpiredCards(false);
		}
		else {
			cardsRequestType.setIncludeExpiredCards(true);
		}

		if (null != cardSearchForm.getCardStatus()) {
			logger.debug(
				"the search form value is  " + cardSearchForm.getCardStatus());

			for (String string : cardSearchForm.getCardStatus()) {
				logger.debug("the card status in the form  are  " + string);
				cardsRequestType.getCardStatus(
				).add(
					string
				);
			}
		}

		if (cardSearchForm.getSchemeType() != null) {
			String[] cardSchemeType = cardSearchForm.getSchemeType();
			logger.debug(
				"the card SchemeType list is of size " + cardSchemeType.length);

			for (String string : cardSchemeType) {
				logger.debug(
					"the card SchemeType  the scheme types are  " + string);
				cardsRequestType.getSchemeType(
				).add(
					string
				);
			}
		}

		logger.debug("Card status: " + cardsRequestType.getCardStatus());

		logger.debug(
			"Inside the service impl: getPanOrBin " +
				cardsRequestType.getPanOrBin());

		logger.debug(
			"Inside the service impl: getIssuer " +
				cardsRequestType.getIssuer());

		logger.debug(
			"Inside the service impl: getPostCode " +
				cardsRequestType.getPostcode());

		logger.debug(
			"Inside the service impl: getLastName " +
				cardsRequestType.getLastName());

		logger.debug(
			"Inside the service impl: setCardExpiryDate " +
				cardsRequestType.getCardExpiryDate());

		logger.debug(
			"Inside the service impl: IncludeExpiredCards " +
				cardsRequestType.isIncludeExpiredCards());

		logger.debug(
			"Inside the service impl: getSchemeType  " +
				cardsRequestType.getSchemeType());

		return cardsRequestType;
	}

	/**
	 * Populate all Access type exist.
	 *
	 * @param void
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
			if (StringUtils.isNotBlank(cardStatusValArray[i]) &&
				StringUtils.isNotBlank(cardStatusOptionArray[i]))
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
			if (StringUtils.isNotBlank(cardStatusValArray[i]) &&
				StringUtils.isNotBlank(cardStatusOptionArray[i]))
				cardStatusMapForAPIs.put(
					cardStatusValArray[i], cardStatusOptionArray[i]);
		}

		return cardStatusMapForAPIs;
	}

	/**
	 * Create new Card search form with UI attribute in form object .
	 *
	 * @param void
	 * @return CardSearchForm
	 */
	public CardSearchForm createCardSearchForm() {
		CardSearchForm cardSearchForm = new CardSearchForm();

		Map<String, String> schemeTypeMap = new TreeMap<>();

		try {
			String[] cardSchemeValArry = cardSearchAppProperties.getCardsProps(
			).getProperty(
				Constants.CARD_SCHEME_VAL_PROPKEY
			).split(
				Constants.COMMA
			);

			String[] cardSchemeOptionArry =
				cardSearchAppProperties.getCardsProps(
				).getProperty(
					Constants.CARD_SCHEME_OPT_PROPKEY
				).split(
					Constants.COMMA
				);

			for (int j = 0; j < cardSchemeOptionArry.length; j++) {
				if (StringUtils.isNotBlank(cardSchemeValArry[j]) &&
					StringUtils.isNotBlank(cardSchemeOptionArry[j]))
					schemeTypeMap.put(
						cardSchemeValArry[j], cardSchemeOptionArry[j]);
			}
		}
		catch (Exception e) {
			logger.info(
				"Exception message in populateDefaultUIValueForCardSearchForm is    " +
					e.getMessage());
			logger.info(
				"Exception message  populateDefaultUIValueForCardSearchForm " +
					e.getStackTrace()[0].toString());
		}

		cardSearchForm.setCardStatusMap(getStatusMap());
		cardSearchForm.setCardStatusMapForAPIs(getStatusMapForAPIs());
		cardSearchForm.setSchemeTypeMap(schemeTypeMap);

		return cardSearchForm;
	}

	/**
	 * Find the Customer access view using portal organisation id .
	 *
	 * @param portalOrgId
	 * @return List
	 */
	public List<Customer> getCustomerAccessView(
		long portalOrgId, PortletContext portletContext) {

		GetCustomerAccessViewRequestType params =
			new GetCustomerAccessViewRequestType();

		params.setCustomerId(String.valueOf(portalOrgId));
		params.setCustomerIdType("portalOrgId");
		params.setIgnoreCase(true);

		GetCustomerAccessViewResponseType response =
			cardService.getCustomerAccessView(params);

		List<Customer> customers = response.getCustomers();

		try {
			if (null != customers) {
				audit.success(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_SEARCH,
					" In Card search page - customer size is =" +
						customers.size());
			}
			else {
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_SEARCH,
					" In Card search page - webservice returned null object");
			}
		}
		catch (Exception e) {
			try {
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_SEARCH,
					" and Card search page  - custom Exception: " +
						e.getMessage());
			}
			catch (Exception ae) {
				logger.error(
					"Unable to write audit trail for Card search customer find failure: " +
						ae.getMessage() + ", original error: " +
							e.getMessage());
			}

			logger.error(
				"Unable to write audit trail for Card detail page - get customters failure: " +
					e.getMessage());
		}

		return customers;
	}

	/**
	 * Update the card status code
	 *
	 * @param cardId
	 * @param cardStatus
	 * @param searchHeader
	 *
	 * @return void .
	 */
	public String updateCardStatusCode(
			BigInteger cardId, String cardStatus, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		String referenceNumber = null;
		logger.debug(
			"In the update card Status code method , calling web service to update status is " +
				cardStatus);
		UpdateCardStatusRequestType cardStatusRequestType =
			new UpdateCardStatusRequestType();

		cardStatusRequestType.setCardId(cardId);
		cardStatusRequestType.setCardStatus(cardStatus);
		cardStatusRequestType.setHeader(searchHeader);

		if (Utility.isUserBelongToCsuAdminUserGroup(
				portletContext.getRequest(),
				cardSearchAppProperties.getCardsProps(
				).getProperty(
					Constants.CARD_STATUS_UPDATE_ADMIN_PROPKEY
				))) {

			cardStatusRequestType.setUpdateToAnyStatus(true);
		}

		UpdateCardStatusResponseType cardStatusResponseType =
			cardService.updateCardStatus(cardStatusRequestType);

		if (null != cardStatusResponseType.getHeader()) {
			String statusCode = cardStatusResponseType.getHeader(
			).getStatusCode();

			logger.info(
				"In the update card Status code method the response header is not null, the status code is  " +
					statusCode);

			// The Alaric Service could be down or some other error occurred.

			if (Constants.SUCCESS.equalsIgnoreCase(statusCode)) {
				logger.debug(
					"In the update card Status code method - the response status is SUCCESS and  isSuccessOrFail =" +
						cardStatusResponseType.isSuccessOrFail() +
							"  and description after status update =" +
								cardStatusResponseType.
									getResponseDescription());

				if (cardStatusResponseType.isSuccessOrFail()) {
					referenceNumber =
						cardStatusResponseType.getUpdateStatusReference();
					logger.debug(
						"[updateCardStatusCode] Reference number for the card status update is =" +
							referenceNumber);
					audit.success(
						portletContext.getResponse(),
						portletContext.getRequest(), AuditOrigin.PORTAL_ORIGIN,
						AuditCategories.CARD_DETAIL,
						"Update Card Status successful for user organisation =" +
							searchHeader.getUserOrgId() + " and user id =" +
								searchHeader.getUserId() + " card.crd_id =" +
									cardId + " and the new card status is =" +
										cardStatus +
											" and success Reference number is =" +
												referenceNumber);
					logger.debug(
						"[updateCardStatus] After the audit success message Reference number is =" +
							referenceNumber);
				}
			}
			else if (Constants.NOT_SUCCESSFUL.equalsIgnoreCase(statusCode) ||
					 Constants.VALIDATION_FAILURE.equalsIgnoreCase(
						 statusCode)) {

				logger.error(
					"[updateCardStatusCode] Could not update the Card Status due to  NOT_SUCCESSFUL or VALIDATION_FAILURE status code error description is " +
						cardStatusResponseType.getResponseDescription());
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_DETAIL,
					" Update Card Status Failed for user organisation =" +
						searchHeader.getUserOrgId() + " and user id =" +
							searchHeader.getUserId() + " and  card.crd_id =" +
								cardId +
									" new status, which failed to change is =" +
										cardStatus +
											" The failed status code in response header is =" +
												statusCode +
													" and description after status update =" +
														cardStatusResponseType.
															getResponseDescription());
			}
			else if (Constants.RECORD_NOT_FOUND.equalsIgnoreCase(statusCode)) {
				logger.error(
					"[updateCardStatusCode] Could not update the Card Status due to RECORD_NOT_FOUND ");
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_DETAIL,
					" Update Card Status Failed for user organisation =" +
						searchHeader.getUserOrgId() + " and user id =" +
							searchHeader.getUserId() + " and  card.crd_id =" +
								cardId +
									" and the new status, which failed to change is =" +
										cardStatus +
											" The failed status code in response header is =" +
												statusCode +
													" and description after status update =" +
														cardStatusResponseType.
															getResponseDescription());

				throw new WebServiceFailureError();
			}
			else if (Constants.SYSTEM_ERROR.equalsIgnoreCase(statusCode) ||
					 Constants.HEARTBEAT_CONNECTIVITY_ERROR.equalsIgnoreCase(
						 statusCode)) {

				logger.error(
					"[updateCardStatusCode] Could not update the Card Status due to SYSTEM_ERROR ");
				audit.fail(
					portletContext.getResponse(), portletContext.getRequest(),
					AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_DETAIL,
					" Update Card Status Failed for user organisation =" +
						searchHeader.getUserOrgId() + " and user id =" +
							searchHeader.getUserId() + " and  card.crd_id =" +
								cardId +
									" new status, which failed to change is =" +
										cardStatus +
											" The WebServiceException status code in response header is=" +
												statusCode +
													" and description after status update =" +
														cardStatusResponseType.
															getResponseDescription());

				throw new WebServiceException();
			}
		}
		else {
			logger.error(
				"The header for update the Card Status  -  is null, so system assume some unknown for SYSTEM_ERROR");
			audit.fail(
				portletContext.getResponse(), portletContext.getRequest(),
				AuditOrigin.PORTAL_ORIGIN, AuditCategories.CARD_DETAIL,
				" Update Card Status Failed for user organisation =" +
					searchHeader.getUserOrgId() + " and user id =" +
						searchHeader.getUserId() + " and  card.crd_id =" +
							cardId +
								" new status, which failed to change is =" +
									cardStatus +
										" The WebServiceException  as response header is  null so status assumed to be SYSTEM_ERROR and Null description");

			throw new WebServiceException();
		}

		return referenceNumber;
	}

	/**
	 * Create Search param for auditing
	 *
	 * @param cardSearchForm
	 * @return String
	 */
	public String createSearchParamsStr(CardSearchForm cardSearchForm) {
		StringBuilder stringBuilder = new StringBuilder();

		if (!StringUtils.isBlank(cardSearchForm.getPanOrBin())) {
			stringBuilder.append(
				"Parameters={PAN=" + cardSearchForm.getMaskedPan());
		}

		if (!StringUtils.isBlank(cardSearchForm.getIssuer())) {
			stringBuilder.append(", Issuer Name=" + cardSearchForm.getIssuer());
		}

		if (!StringUtils.isBlank(cardSearchForm.getCardholderName())) {
			stringBuilder.append(
				", Surname=" + cardSearchForm.getCardholderName());
		}

		if (!StringUtils.isBlank(cardSearchForm.getPostCode())) {
			stringBuilder.append(", Postcode=" + cardSearchForm.getPostCode());
		}

		if (null != cardSearchForm.getCardStatus()) {
			String[] cardStatus = cardSearchForm.getCardStatus();
			stringBuilder.append(", Card Status=");

			for (String string : cardStatus) {
				stringBuilder.append(" " + string);
			}
		}

		if (null != cardSearchForm.getSchemeType()) {
			String[] cardSchemeType = cardSearchForm.getSchemeType();
			stringBuilder.append(", Scheme Type=");

			for (String string : cardSchemeType) {
				stringBuilder.append(" " + string);
			}
		}

		stringBuilder.append(", NonExpired=" + cardSearchForm.isNonExpired());

		if (!StringUtils.isBlank(cardSearchForm.getExpiryMonth())) {
			stringBuilder.append(
				", Expiry Date(MMYYYY)=" + cardSearchForm.getExpiryMonth() +
					cardSearchForm.getExpiryYear());
		}

		stringBuilder.append("}");

		return stringBuilder.toString();
	}

	/**
	 * Get get org short name using liferay org id .
	 *
	 * @param liferayOrgId
	 * @return String
	 */
	public String getOrgShortName(String liferayOrgId) {
		String orgShortName = null;
		FindEntitiesByAttributeWithIdRequestType
			findEntitiesByAttributeWithIdRequest =
				new FindEntitiesByAttributeWithIdRequestType();
		findEntitiesByAttributeWithIdRequest.setAttributeValue(liferayOrgId);
		findEntitiesByAttributeWithIdRequest.setAttributeTypeId(4);

		FindEntitiesByAttributeWithIdResponseType
			findEntitiesByAttributeWithIdResponseType =
				cardService.findEntitiesByAttributeWithId(
					findEntitiesByAttributeWithIdRequest);

		if (!findEntitiesByAttributeWithIdResponseType.getEntities(
			).isEmpty())
			orgShortName =
				findEntitiesByAttributeWithIdResponseType.getEntities(
				).get(
					0
				).getShortName();

		logger.debug("The organisation short name is " + orgShortName);

		return orgShortName;
	}

	@Override
	public boolean isShowMccControls() {
		return Boolean.valueOf(
			cardSearchAppProperties.getCardsProps(
			).getProperty(
				Constants.SHOW_MCC_CONTROLS_TAB, Boolean.FALSE.toString()
			));
	}

	@Override
	public boolean isShowLimits() {
		return Boolean.valueOf(
			cardSearchAppProperties.getCardsProps(
			).getProperty(
				Constants.SHOW_HIDE_LIMITS_TAB
			));
	}

	@Override
	public boolean isShowCardLimits() {
		return Boolean.valueOf(
			cardSearchAppProperties.getCardsProps(
			).getProperty(
				Constants.SHOW_CARD_LIMITS_TAB, Boolean.FALSE.toString()
			));
	}

	@Override
	public String[] getShowCardLimitsOrgs() {
		String orgs = cardSearchAppProperties.getCardsProps(
		).getProperty(
			Constants.SHOW_CARD_LIMITS_TAB_FOR_ORGS
		);
		String[] orgList = null;

		if (StringUtils.isNotBlank(orgs)) {
			orgList = orgs.split(Constants.COMMA);
		}

		return orgList;
	}

	public void logPoolStatistics() {
		cardService.logPoolStatistics();
	}

	public void purgePools() {
		cardService.purgePools();
	}

	@Override
	public boolean isShowCardControlHistory() {
		return Boolean.valueOf(
			cardSearchAppProperties.getCardsProps(
			).getProperty(
				Constants.SHOW_CARD_CONTROLS_HISTORY, Boolean.FALSE.toString()
			));
	}

	@Override
	public boolean isShowMccControlHistory() {
		return Boolean.valueOf(
			cardSearchAppProperties.getCardsProps(
			).getProperty(
				Constants.SHOW_MCC_CONTROLS_HISTORY, Boolean.FALSE.toString()
			));
	}

	@Override
	public boolean isShowCardLimitsHistory() {
		return Boolean.valueOf(
			cardSearchAppProperties.getCardsProps(
			).getProperty(
				Constants.SHOW_CARD_LIMITS_HISTORY, Boolean.FALSE.toString()
			));
	}

	@Override
	public boolean isShowMobileDevices() {
		return Boolean.valueOf(
			cardSearchAppProperties.getCardsProps(
			).getProperty(
				Constants.SHOW_MOBILE_DEVICES_TAB, Boolean.FALSE.toString()
			));
	}

	/**
	 * @return the schemeService
	 */
	public SchemeService getSchemeService() {
		return schemeService;
	}

	/**
	 * @param schemeService the schemeService to set
	 */
	public void setSchemeService(SchemeService schemeService) {
		this.schemeService = schemeService;
	}

	public TokenSearchResult searchTokens(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		logger.info("CardSearchServiceImpl.searchTokens - start, ");
		PermGenUtil.permGenDump("CardSearchServiceImpl.searchTokens - start");

		TokenSearchResult result = new TokenSearchResult();

		result.setPartialSuccess(false);

		SearchTokensRequest request = new SearchTokensRequest();

		CommonRequestHeader header = new CommonRequestHeader();

		request.setHeader(header);
		request.setPan(cardSearchForm.getPanOrBin());

		// Support MND tokens

		request.setIncludeAuxTokens(true);

		logger.info(
			"CardSearchServiceImpl.searchTokens - calling the web service: schemeService.searchTokens(request)");
		PermGenUtil.permGenDump(
			"CardSearchServiceImpl.searchTokens - webservice request object created");
		SearchTokensResponse response = schemeService.searchTokens(request);
		PermGenUtil.permGenDump(
			"CardSearchServiceImpl.searchTokens - after webservice call");

		if (response != null) {
			if (Constants.TOKEN_SUCCESS.equals(
					response.getHeader(
					).getActionCode())) {

				String errorCdoe = response.getHeader(
				).getErrorCode();

				if (Constants.TOKEN_PRIMARY_SEARCH_FAILURE.equals(errorCdoe) ||
					Constants.TOKEN_SECONDARY_SEARCH_FAILURE.equals(
						errorCdoe)) {

					result.setPartialSuccess(true);
				}

				result.setTokens(response.getTokenDetails());
			}
			else {
				logger.error(
					response.getHeader(
					).getErrorMessage());
			}
		}

		logger.info("CardSearchServiceImpl.searchTokens - end. ");

		return result;
	}

	@Override
	public boolean activateToken(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		logger.info("CardSearchServiceImpl.activateToken - start, ");
		PermGenUtil.permGenDump("CardSearchServiceImpl.activateToken - start");

		ActivateTokenRequest request = new ActivateTokenRequest();

		CommonRequestHeader header = new CommonRequestHeader();

		TokenDetailType tokenDetail = getTokenDetail(
			cardSearchForm, searchHeader, portletContext);

		header.setScheme(tokenDetail.getScheme());

		header.setRequestId(generateRequestId());
		request.setHeader(header);
		request.setActivationChannel(SourceChannelType.PORTAL_CUSCAL);
		request.setCuscalToken(tokenDetail.getCuscalToken());
		request.setToken(tokenDetail.getTokenInfo());

		logger.info(
			"CardSearchServiceImpl.activateToken - calling the web service: schemeService.activateToken(request) - requestId: " +
				header.getRequestId());
		PermGenUtil.permGenDump(
			"CardSearchServiceImpl.activateToken - webservice request object created");
		ActivateTokenResponse response = schemeService.activateToken(request);
		PermGenUtil.permGenDump(
			"CardSearchServiceImpl.activateToken - after webservice call");

		boolean success = false;

		if (response != null) {
			if (Constants.TOKEN_SUCCESS.equals(
					response.getHeader(
					).getActionCode())) {

				success = true;
			}
			else {
				logger.error(
					response.getHeader(
					).getErrorMessage());
			}
		}

		logger.info("CardSearchServiceImpl.activateToken - end. ");

		return success;
	}

	@Override
	public boolean updateToken(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		logger.info("CardSearchServiceImpl.updateToken - start, ");
		PermGenUtil.permGenDump("CardSearchServiceImpl.updateToken - start");

		HandleTokenUpdateRequest request = new HandleTokenUpdateRequest();

		CommonRequestHeader header = new CommonRequestHeader();

		TokenDetailType tokenDetail = getTokenDetail(
			cardSearchForm, searchHeader, portletContext);

		header.setScheme(tokenDetail.getScheme());

		PortletRequest portletRequest = portletContext.getRequest();

		String action = portletRequest.getParameter(Constants.TOKEN_ACTION);
		String reason = null;
		String comment = null;

		TokenLifecycleAction tokenLifecycleAction = null;

		if (action != null) {
			tokenLifecycleAction = TokenLifecycleAction.valueOf(
				action.toUpperCase());

			if (tokenLifecycleAction.equals(TokenLifecycleAction.DELETE)) {
				reason = portletRequest.getParameter(Constants.DELETE_REASON);
				comment = portletRequest.getParameter(Constants.DELETE_COMMENT);
			}
			else if (tokenLifecycleAction.equals(
						TokenLifecycleAction.SUSPEND)) {

				reason = portletRequest.getParameter(Constants.SUSPEND_REASON);
				comment = portletRequest.getParameter(
					Constants.SUSPEND_COMMENT);
			}
			else if (tokenLifecycleAction.equals(
						TokenLifecycleAction.UNSUSPEND)) {

				reason = portletRequest.getParameter(
					Constants.UNSUSPEND_REASON);
				comment = portletRequest.getParameter(
					Constants.UNSUSPEND_COMMENT);
			}
		}

		TokenUpdateReason tokenUpdateReason = null;

		if (reason != null) {
			tokenUpdateReason = TokenUpdateReason.valueOf(reason);
		}

		header.setRequestId(generateRequestId());
		request.setHeader(header);
		request.setTokenUniqueReference(
			tokenDetail.getTokenInfo(
			).getTokenReference());
		request.setTokenLifecycleAction(tokenLifecycleAction);
		request.setSourceChannel(SourceChannelType.PORTAL_CUSCAL);
		request.setReason(tokenUpdateReason);
		request.setComment(comment);

		logger.info(
			"CardSearchServiceImpl.updateToken - calling the web service: schemeService.updateToken(request) - requestId: " +
				header.getRequestId());
		PermGenUtil.permGenDump(
			"CardSearchServiceImpl.updateToken - webservice request object created");
		HandleTokenUpdateResponse response = schemeService.updateToken(request);
		PermGenUtil.permGenDump(
			"CardSearchServiceImpl.updateToken - after webservice call");

		boolean success = false;

		if (response != null) {
			if (Constants.TOKEN_SUCCESS.equals(
					response.getHeader(
					).getActionCode())) {

				success = true;
			}
			else {
				logger.error(
					response.getHeader(
					).getErrorMessage());
			}
		}

		logger.info("CardSearchServiceImpl.updateToken - end. ");

		return success;
	}

	private TokenDetailType getTokenDetail(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		List<TokenDetailType> tokens = searchTokens(
			cardSearchForm, searchHeader, portletContext
		).getTokens();
		TokenDetailType tokenDetail = null;

		for (TokenDetailType tokenDetailType : tokens) {
			if (tokenDetailType.getTokenInfo(
				).getTokenReference(
				).getTokenReferenceId(
				).equals(
					cardSearchForm.getTokenId()
				)) {

				tokenDetail = tokenDetailType;

				break;
			}
		}

		return tokenDetail;
	}

	private BigInteger generateRequestId() {
		return new BigInteger(32, random);
	}

}