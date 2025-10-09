package au.com.cuscal.cards.services;

import static au.com.cuscal.cards.commons.Utility.removeAllSpacesFromPan;
import static au.com.cuscal.cards.services.CryptoHelper.encrypt;

import au.com.cuscal.cards.commons.CardSearchAppProperties;
import au.com.cuscal.cards.commons.Constants;
import au.com.cuscal.cards.commons.WebServiceException;
import au.com.cuscal.cards.domain.*;
import au.com.cuscal.cards.forms.CardSearchForm;
import au.com.cuscal.cards.services.rest.CardRestService;
import au.com.cuscal.cards.services.rest.domain.OrganisationAccess;
import au.com.cuscal.common.framework.memory.PermGenUtil;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.portal.kernel.util.PortalUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
@Service(Constants.CARD_ROUTE_SERVICE)
public class CardsRouteService {

	/**
	 * Make an API call to AuthenticEarth to get the Card Accounts
	 *
	 * @param response
	 * @param request
	 * @return CardLimits
	 */
	public List<AccountInformation> getCardAccounts(
			String cuscalToken, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		logger.info("getCardAccounts - start");
		List<AccountInformation> accountInformation = null;

		try {
			OrganisationAccess orgAccess = cardRestService.getOrgAccess(
				searchHeader);

			accountInformation = cardsRestTranslator.formatGetCardAccounts(
				cardRestService.getCardAccounts(orgAccess, cuscalToken),
				cuscalToken, portletContext);
		}
		catch (Exception ex) {
			logger.error(
				"[getCardAccounts] the getCardAccounts  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}
		finally {
			logger.info("getCardAccounts - end");
		}

		return accountInformation;
	}

	/**
	 * Make an API call to AuthenticEarth to get the Card Limits
	 *
	 * @param response
	 * @param request
	 * @return CardLimits
	 */
	public CardLimits getCardBasedLimits(
			String cuscalToken, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception {

		logger.info("getCardBasedLimits - start");
		CardLimits cardLimits = new CardLimits();

		try {
			OrganisationAccess orgAccess = cardRestService.getOrgAccess(
				searchHeader);

			cardLimits = cardsRestTranslator.formatGetCardLimitsResponse(
				cardLimits,
				cardRestService.getCardBasedLimits(orgAccess, cuscalToken),
				cuscalToken, portletContext);
		}
		catch (Exception ex) {
			logger.error(
				"[getCardBasedLimits] the getCardBasedLimits  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}
		finally {
			logger.info("getCardBasedLimits - end");
			PermGenUtil.permGenDump("getCardBasedLimits - end");
		}

		return cardLimits;
	}

	/**
	 * Make an API call to AuthenticEarth to get the Card Controls
	 *
	 * @param cuscalToken
	 * @param searchHeader
	 * @return String
	 */
	public String getCardControls(String cuscalToken, SearchHeader searchHeader)
		throws Exception {

		logger.info("getCardControls - start");
		String svcResponse = null;

		try {
			OrganisationAccess orgAccess = cardRestService.getOrgAccess(
				searchHeader);

			svcResponse = cardRestService.getCardControls(
				orgAccess, cuscalToken);
		}
		catch (Exception ex) {
			logger.error(
				"[getCardControls] the getCardControls  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}
		finally {
			logger.info("getCardControls - end");
		}

		return svcResponse;
	}

	/**
	 * Make an API call to either AuthenticEarth to get the Card Information
	 *
	 * @param response
	 * @param request
	 * @return CardInformation
	 */
	public CardInformation getCardInformation(
			String cuscalToken, SearchHeader searchHeader, List<String> groups,
			PortletContext portletContext)
		throws Exception {

		logger.info("getCardInformation - start");
		CardInformation cardInformation = null;

		try {
			OrganisationAccess orgAccess = cardRestService.getOrgAccess(
				searchHeader);

			cardInformation = cardsRestTranslator.formatGetCardDetailsResponse(
				cardInformation,
				cardRestService.getCardDetails(orgAccess, cuscalToken),
				orgAccess, cuscalToken, groups, portletContext);

			// Fixed values

			cardInformation.setSwitchResponseCode(null);
			cardInformation.setVisaReference(null);
			cardInformation.setResponseDescription(null);
		}
		catch (Exception ex) {
			logger.error(
				"[getCardInformation] the getCardInformation  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);

			throw new WebServiceException();
		}
		finally {
			logger.info("getCardInformation - end");
			PermGenUtil.permGenDump("getCardInformation - end");
		}

		return cardInformation;
	}

	/**
	 * Make an API call to either Authentic 3 or Authentic 5 to search the Cards
	 *
	 * @param response
	 * @param request
	 * @return List<Cards>
	 */
	public List<Cards> getCardsList(
		CardSearchForm cardSearchForm, SearchHeader searchHeader,
		PortletContext portletContext) {

		logger.info("getCardsList - start");

		try {
			String _routeStatus = getRouteStatus(cardSearchForm, searchHeader);

			if (Constants.ROUTE_AUTHENTIC_EARTH.equals(_routeStatus)) {
				OrganisationAccess orgAccess = cardRestService.getOrgAccess(
					searchHeader);
				String encPan = "";
				String keyIdentifier = "";

				if (null != cardSearchForm.getPanOrBin()) {
					encPan = encrypt(
						this.key,
						removeAllSpacesFromPan(cardSearchForm.getPanOrBin()));
					keyIdentifier = this.key.getIdentifier(
					).toString();
				}

				logger.info(
					"getCardsList - about to call cardRestService.getCardsList as default option - call to AE");
				List<Cards> returnedCards =
					cardsRestTranslator.formatGetCardsResponse(
						cardRestService.getCardsList(
							cardSearchForm, searchHeader, portletContext,
							orgAccess, keyIdentifier, encPan));

				if ((returnedCards != null) && (returnedCards.size() > 0)) {

					// IS_DATA_FROM_AUTHENTIC_EARTH session variable
					// set to TRUE on getting data from Authentic Earth

					HttpServletRequest httpServletRequest =
						PortalUtil.getHttpServletRequest(
							portletContext.getRequest());

					HttpSession session = httpServletRequest.getSession();

					session.setAttribute(
						Constants.IS_DATA_FROM_AUTHENTIC_EARTH, Boolean.TRUE);
				}

				return returnedCards;
			}

			logger.debug(
				"-- Route is not AuthenticEarth. It is " + _routeStatus);
			logger.info(
				"getCardsList - about to call cardsSearchService.getCardListOnsearch as default option - call to authentic");

			return cardsSearchService.getCardListOnsearch(
				cardSearchForm, searchHeader, portletContext);
		}
		catch (Exception ex) {
			logger.error(
				"[getCardsList] the getCardsList  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}

		return null;
	}

	/**
	 * Make an API call to security API to check the route - Authentic 3 or Authentic 5
	 *
	 * @param cardSearchForm
	 * @param searchHeader
	 * @return String
	 */
	public String getRouteStatus(
		CardSearchForm cardSearchForm, SearchHeader searchHeader) {

		logger.info("getRouteStatus - start");

		try {
			OrganisationAccess orgAccess = cardRestService.getOrgAccess(
				searchHeader);
			RouteCard routeResponse = null;

			if (cardSearchForm.getPanOrBin() != null) {
				logger.info("getRouteStatus - for Pan or Bin");
				Key key = cardRestService.getEncryptionKey(orgAccess);

				String encPan = encrypt(
					key, removeAllSpacesFromPan(cardSearchForm.getPanOrBin()));
				String keyIdentifier = key.getIdentifier();
				this.key = key;
				routeResponse = cardRestService.getRoute(
					orgAccess, null, encPan, keyIdentifier);
			}
			else if (null != cardSearchForm.getIssuerShortName()) {
				logger.info("getRouteStatus - for Issuer Short Name");
				routeResponse = cardRestService.getRoute(
					orgAccess, cardSearchForm.getIssuerShortName(), null, null);
			}

			logger.info(
				"getRouteStatus - cardRestService.getRoute returned " +
					routeResponse.getResponse());

			return routeResponse.getResponse();
		}
		catch (Exception ex) {
			logger.info(
				"getRouteStatus - cardRestService.getRoute returned systemError -> falling back to authentic");
			logger.debug(
				"[getRouteStatus] the getRouteStatus  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}

		return null;
	}

	/**
	 * Make an API call to AuthenticEarth to update the Card Controls
	 *
	 * @param cuscalToken
	 * @param searchHeader
	 * @return String
	 */
	public String updateCardControls(
			String cuscalToken, String request, SearchHeader searchHeader)
		throws Exception {

		logger.info("updateCardControls - start");
		String svcResponse = null;

		try {
			OrganisationAccess orgAccess = cardRestService.getOrgAccess(
				searchHeader);

			svcResponse = cardRestService.updateCardControls(
				orgAccess, cuscalToken, request);
		}
		catch (Exception ex) {
			logger.error(
				"[updateCardControls] the getCardControls  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}
		finally {
			logger.info("updateCardControls - end");
		}

		return svcResponse;
	}

	/**
	 * Make an API call to AuthenticEarth to update the Card Status
	 *
	 * @param cuscalToken - Card Token
	 * @param cardStatus - New Status selected by User
	 * @param searchHeader - user information
	 * @return String updateReferenceNumber
	 */
	public String updateCardStatus(
			String cuscalToken, String cardStatus, SearchHeader searchHeader)
		throws Exception {

		logger.info("updateCardStatus - start");
		String updateReferenceNumber = null;

		try {
			OrganisationAccess orgAccess = cardRestService.getOrgAccess(
				searchHeader);
			Map<String, String> elements = new HashMap();

			elements.put(
				Constants.STATUS,
				cardsRestTranslator.getStatusMapForAPIs(
				).get(
					cardStatus
				));
			ObjectMapper objectMapper = new ObjectMapper();

			String cardStatusVals = objectMapper.writeValueAsString(elements);

			logger.info(
				"updateCardStatus - calling POST API with new card status {}",
				cardStatusVals);
			updateReferenceNumber = cardsRestTranslator.formatUpdateCardStatus(
				cardRestService.updateCardStatus(
					orgAccess, cuscalToken, cardStatusVals));
		}
		catch (Exception ex) {
			logger.error(
				"[updateCardStatus] the updateCardStatus  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
		}
		finally {
			logger.info("updateCardStatus - end");
		}

		return updateReferenceNumber;
	}

	private static Key key;

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(
		CardsRouteService.class);

	@Autowired
	@Qualifier(Constants.CARD_REST_SERVICE)
	private CardRestService cardRestService;

	@Autowired
	@Qualifier("cardSearchAppProperties")
	private CardSearchAppProperties cardSearchAppProperties;

	@Autowired
	@Qualifier(Constants.CARD_REST_TRANSLATOR)
	private CardsRestTranslator cardsRestTranslator;

	/**
	 * Service used to fetch data
	 */
	@Autowired
	@Qualifier(Constants.CARD_SEARCH_SERVICE)
	private CardsSearchService cardsSearchService;

}