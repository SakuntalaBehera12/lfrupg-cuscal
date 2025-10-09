package au.com.cuscal.cards.services.rest;

import static au.com.cuscal.cards.commons.Constants.*;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import au.com.cuscal.cards.domain.*;
import au.com.cuscal.cards.forms.CardSearchForm;
import au.com.cuscal.cards.services.rest.domain.OrganisationAccess;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service(value = CARD_REST_SERVICE)
public class CardRestService extends BaseCuscalRestService {

	public String getCardAccounts(
			OrganisationAccess organisationAccess, String cuscalToken)
		throws Exception {

		Map<String, String> queryParams = new HashMap<>();
		String svcUrl = String.format(
			cardv2SvcUrl + "/cards/%s/accounts", cuscalToken);

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess));
	}

	public String getCardBasedLimits(
			OrganisationAccess organisationAccess, String cuscalToken)
		throws Exception {

		Map<String, String> queryParams = new HashMap<>();
		String svcUrl = String.format(
			cardv2SvcUrl + "/cards/%s/limits", cuscalToken);

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess));
	}

	public String getCardChannels(
			OrganisationAccess organisationAccess, String cuscalToken)
		throws Exception {

		Map<String, String> queryParams = new HashMap<>();
		String svcUrl = String.format(
			cardv2SvcUrl + "/cards/%s/channels", cuscalToken);

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess));
	}

	public String getCardControls(
			OrganisationAccess organisationAccess, String cuscalToken)
		throws Exception {

		Map<String, String> queryParams = new HashMap<>();

		queryParams.put(CHANNEL, CHANNEL_PORTAL);
		String svcUrl = String.format(
			cardControlsSvcUrl + "/cards/%s/cardControls", cuscalToken);

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess));
	}

	public String getCardDetails(
			OrganisationAccess organisationAccess, String cuscalToken)
		throws Exception {

		Map<String, String> queryParams = new HashMap<>();

		queryParams.put(INCLUDE_LAST_TRANSACTION_DETAILS, "true");
		queryParams.put(PAN_TYPE, "CLEAR");
		String svcUrl = String.format(cardv2SvcUrl + "/cards/%s", cuscalToken);

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess));
	}

	public CardLimit[] getCardLimits(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext,
			OrganisationAccess organisationAccess, String token)
		throws Exception {

		final Map<String, String> queryParams = new HashMap<>();
		String svcUrl = String.format(
			cardSvcUrl + "/%s/%s/limits", organisationAccess.getInstitutionId(),
			token);

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess),
			CardLimit[].class);
	}

	public String getCardNextAvailableStatus(
			OrganisationAccess organisationAccess, String cuscalToken,
			String status)
		throws Exception {

		Map<String, String> queryParams = new HashMap<>();

		if (null != status) {
			queryParams.put(STATUS, status);
		}

		String svcUrl = String.format(
			cardv2SvcUrl + "/config/cards/%s/status/change", cuscalToken);

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess));
	}

	public String getCardsList(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext,
			OrganisationAccess organisationAccess, String keyIdentifier,
			String encPan)
		throws Exception {

		Map<String, String> queryParams = new HashMap<>();

		queryParams.put(PAN_TYPE, "CLEAR");
		queryParams = addFiltersToQueryParamsGetCards(
			queryParams, cardSearchForm);

		if (null != cardSearchForm.getPanOrBin()) {
			queryParams.put(ENC_PAN, encPan);
			queryParams.put(KEY_IDENTIFIER, keyIdentifier);
		}
		else if (null != cardSearchForm.getIssuerShortName()) {
			queryParams.put(
				CARD_HOLDER_NAME, cardSearchForm.getCardholderName());
			queryParams.put(ORG_NAME, cardSearchForm.getIssuerShortName());
			queryParams.put(POSTCODE, cardSearchForm.getPostCode());
		}

		queryParams.put(PAGE, String.valueOf(searchHeader.getPageNumber()));
		String svcUrl = String.format(cardv2SvcUrl + "/cards");

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess));
	}

	public CardSpend getCardSpend(
			OrganisationAccess organisationAccess, String token, String period)
		throws Exception {

		final Map<String, String> queryParams = new HashMap<>();
		String svcUrl = String.format(
			cardSvcUrl + "/%s/%s/spend/%s",
			organisationAccess.getInstitutionId(), token, period);

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess),
			CardSpend.class);
	}

	public Key getEncryptionKey(OrganisationAccess organisationAccess)
		throws Exception {

		final Map<String, String> queryParams = new HashMap<>();
		String svcUrl = String.format(cardv2SvcUrl + GET_KEY_URI);

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess), Key.class);
	}

	public String getMccControls(
			OrganisationAccess organisationAccess, String cuscalToken)
		throws Exception {

		final Map<String, String> queryParams = new HashMap<>();
		queryParams.put(CHANNEL, CHANNEL_PORTAL);
		String svcUrl = String.format(
			cardControlsSvcUrl + "/cards/%s/MCCGroups", cuscalToken);

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess));
	}

	public OrganisationAccess getOrgAccess(SearchHeader searchHeader)
		throws Exception {

		String accessInfo = cardSearchAppProperties.getCardsProps(
		).getProperty(
			ORG + searchHeader.getUserOrgId()
		);

		if (isBlank(accessInfo)) {
			throw new Exception(
				"Organization [" + searchHeader.getUserOrgName() + "] Id [" +
					searchHeader.getUserOrgId() +
						"] is not configured for accessing microservices");
		}

		OrganisationAccess organisationAccess = new OrganisationAccess();

		organisationAccess.setInstitutionId(accessInfo.split(",")[0]);
		organisationAccess.setUserId(accessInfo.split(",")[1]);
		organisationAccess.setOrganisationId(searchHeader.getUserOrgId());
		organisationAccess.setOperatorId(searchHeader.getUserId());

		return organisationAccess;
	}

	public RouteCard getRoute(
			OrganisationAccess organisationAccess, String orgName,
			String eAccount, String keyIdentifier)
		throws Exception {

		final Map<String, String> queryParams = new HashMap<>();

		if (isNotBlank(orgName)) {
			queryParams.put(ORG_NAME, orgName);
		}
		else if (isNotBlank(eAccount) && isNotBlank(keyIdentifier)) {
			queryParams.put(ENC_PAN, eAccount);
			queryParams.put(KEY_IDENTIFIER, keyIdentifier);
		}

		String svcUrl = String.format(cardv2SvcUrl + "/route");

		return restService.doGet(
			svcUrl, queryParams, createHeader(organisationAccess),
			RouteCard.class);
	}

	public String updateCardControls(
			OrganisationAccess organisationAccess, String cuscalToken,
			String cardControlVals)
		throws Exception {

		String svcUrl = String.format(
			cardControlsSvcUrl + "/cards/%s/cardControls", cuscalToken);

		return restService.doPost(
			svcUrl, Collections.<String, String>emptyMap(),
			createHeader(organisationAccess), cardControlVals);
	}

	public void updateCardLimits(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext,
			OrganisationAccess organisationAccess, String token,
			String cardLimitsVals, String cardLimitId)
		throws Exception {

		final Map<String, String> queryParams = new HashMap<>();
		String svcUrl = String.format(
			cardSvcUrl + "/%s/%s/limits/%s",
			organisationAccess.getInstitutionId(), token, cardLimitId);

		restService.doPost(
			svcUrl, queryParams, createHeader(organisationAccess),
			cardLimitsVals, String.class);
	}

	public String updateCardStatus(
			OrganisationAccess organisationAccess, String cuscalToken,
			String cardStatus)
		throws Exception {

		String svcUrl = String.format(
			cardLifecycleSvcUrl + "/cards/%s/status", cuscalToken);

		return restService.doPost(
			svcUrl, Collections.<String, String>emptyMap(),
			createHeader(organisationAccess), cardStatus);
	}

	public void updateMccControls(
			OrganisationAccess organisationAccess, String token,
			String mccControlVals)
		throws Exception {

		final Map<String, String> queryParams = new HashMap<>();
		String svcUrl = String.format(
			cardSvcUrl + "/%s/%s/MCCGroups",
			organisationAccess.getInstitutionId(), token);

		restService.doPost(
			svcUrl, queryParams, createHeader(organisationAccess),
			mccControlVals);
	}

	private Map<String, String> addFiltersToQueryParamsGetCards(
		Map<String, String> queryParams, CardSearchForm cardSearchForm) {

		queryParams.put(
			EXCLUDE_EXPIRED_CARDS,
			String.valueOf(cardSearchForm.isNonExpired()));
		queryParams.put(EXPIRY_DATE, cardSearchForm.getFormattedExpiry());
		String statusForAPIs = "";

		if ((cardSearchForm != null) &&
			(cardSearchForm.getCardStatus() != null)) {

			for (String o : cardSearchForm.getCardStatus()) {
				statusForAPIs =
					statusForAPIs +
						cardSearchForm.getCardStatusMapForAPIs(
						).get(
							o
						) + ",";
			}
		}

		statusForAPIs = statusForAPIs.length() > 0 ?
			statusForAPIs.substring(0, statusForAPIs.length() - 1) : "";
		queryParams.put(STATUS, statusForAPIs);

		return queryParams;
	}

	private Map<String, String> createHeader(
		OrganisationAccess organisationAccess) {

		final Map<String, String> headers = new HashMap<>();
		headers.put(IV_USER, organisationAccess.getUserId());
		headers.put(INSTITUTION_ID, organisationAccess.getInstitutionId());
		headers.put(CHANNEL, CHANNEL_PORTAL);
		headers.put(X_CHANNEL, CHANNEL_PORTAL);
		headers.put(OPERATOR, organisationAccess.getOperatorId());

		return headers;
	}

	private static final String CHANNEL = "channel";

	private static final String ENC_PAN = "eAccount";

	private static final String EXCLUDE_EXPIRED_CARDS = "is-expired";

	private static final String INCLUDE_LAST_TRANSACTION_DETAILS =
		"includeLastTransactionDetails";

	private static final String INSTITUTION_ID = "institutionId";

	private static final String IV_USER = "iv-user";

	private static final String KEY_IDENTIFIER = "keyIdentifier";

	private static final String OPERATOR = "operator";

	private static final String PAGE = "page";

	private static final String PAN_TYPE = "panType";

	private static final String POSTCODE = "postCode";

	private static final String X_CHANNEL = "x-channel";

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(
		CardRestService.class);

	@Autowired
	@Value("${webservice.cardControls.svc.url}")
	private String cardControlsSvcUrl;

	@Autowired
	@Value("${webservice.cardLifecycle.svc.url}")
	private String cardLifecycleSvcUrl;

	@Autowired
	@Value("${webservice.card.svc.url}")
	private String cardSvcUrl;

	@Autowired
	@Value("${webservice.cardv2.svc.url}")
	private String cardv2SvcUrl;

	@Autowired
	@Value("${webservice.tokenVault.svc.url}")
	private String tokenVaultSvcUrl;

}