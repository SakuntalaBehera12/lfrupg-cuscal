package au.com.cuscal.cards.controllers;

import static au.com.cuscal.cards.commons.Constants.SHOW_MOBILE_DEVICES;
import static au.com.cuscal.cards.domain.CardLimitAction.*;
import static au.com.cuscal.cards.domain.CardLimitPeriod.*;
import static au.com.cuscal.cards.domain.CardLimitStatus.ON;
import static au.com.cuscal.cards.domain.CardLimitStatus.SUSPENDED;

import static org.apache.commons.lang.StringEscapeUtils.escapeXml;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import au.com.cuscal.cards.commons.*;
import au.com.cuscal.cards.domain.*;
import au.com.cuscal.cards.domain.ui.DecoratedDevice;
import au.com.cuscal.cards.domain.ui.DecoratedToken;
import au.com.cuscal.cards.forms.CardSearchForm;
import au.com.cuscal.cards.services.CardsRouteService;
import au.com.cuscal.cards.services.CardsSearchService;
import au.com.cuscal.cards.services.client.CuscalTokenWebService;
import au.com.cuscal.cards.services.rest.AuditRestService;
import au.com.cuscal.cards.services.rest.CardRestService;
import au.com.cuscal.cards.services.rest.domain.OrganisationAccess;
import au.com.cuscal.cards.validator.CardsSearchValidator;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.scheme.TokenDetailType;
import au.com.cuscal.framework.webservices.scheme.TokenLifecycleAction;
import au.com.cuscal.framework.webservices.transaction.*;
import au.com.cuscal.service.mobile.v1_0.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.*;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;
import com.liferay.portletmvc4spring.bind.annotation.ResourceMapping;

import java.io.IOException;
import java.io.PrintWriter;

import java.math.BigInteger;

import java.text.SimpleDateFormat;

import java.util.*;

import javax.portlet.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import org.displaytag.properties.SortOrderEnum;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Controller that returns the Cards Search and details View
 *
 * @author Rajni Bharara
 *
 */
@Controller(Constants.CARD_SEARCH_CONTROLLER)
@RequestMapping("VIEW")
@SessionAttributes(types = CardSearchForm.class)
public class CardsSearchController {

	/**
	 *
	 * @param cardSearchForm
	 * @param bindingResult
	 * @param response
	 * @param sessionStatus
	 * @param request
	 */
	@ActionMapping(
		params = Constants.CARD_ACTION + Constants.EQUALS + Constants.TOKEN_ACTIVATE
	)
	public void activateToken(
		@ModelAttribute CardSearchForm cardSearchForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.info("[activateToken] start");

		String tokenId = ParamUtil.getString(request, Constants.TOKEN_ID);
		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String cuscalToken = getCuscalTokenFromRequest(request);
		cardId = escapeXml(cardId);
		cardSearchForm.setCardId(cardId);
		cardSearchForm.setTokenId(tokenId);

		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);

		logger.info("[activateToken] cardId - " + cardId);
		logger.info("[activateToken] cuscalTokenFromRequest - " + cuscalToken);
		logger.info("[activateToken] tokenId - " + tokenId);

		try {
			SearchHeader searchHeader = getSearchHeaderData(request);
			CardInformation cardInformation = getCardInformationFromSwitch(
				response, request, cardId, cuscalToken);

			request.setAttribute("cardInfoData", cardInformation);
			cardSearchForm.setPanOrBin(cardInformation.getPan());

			boolean success = cardsSearchService.activateToken(
				cardSearchForm, searchHeader,
				PortletContext.newContext(response, request));

			if (!success) {
				throw new Exception("[activateToken] fail");
			}

			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.TOKEN_WALLETS);
			response.setRenderParameter(Constants.CARD_ID, cardId);
		}
		catch (Exception ex) {
			logger.error(
				"[activateToken] the exception came message is  " +
					ex.getMessage(),
				ex);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}

		sessionStatus.setComplete();
		logger.info("[activateToken] end");
	}

	/**
	 * Clear out the Card Reference List when the user clicks the button.
	 *
	 * @param request
	 * @param response
	 */
	@ResourceMapping(Constants.CLEAR_CARD_REFERENCE_LIST)
	public void clearCardReferenceList(
		ResourceRequest request, ResourceResponse response,
		SessionStatus sessionStatus) {

		HttpServletRequest httpServletRequest = _getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		@SuppressWarnings("unchecked")
		ArrayList<SimpleCard> cardReferenceList =
			(ArrayList<SimpleCard>)session.getAttribute(
				Constants.CARD_REFERENCE_LIST);

		if (cardReferenceList != null) {
			cardReferenceList.clear();
			logger.debug(
				"[clearCardReferenceList] Clearing the card reference list");
			session.removeAttribute(Constants.CARD_REFERENCE_LIST);
		}

		PrintWriter out;

		try {
			out = ((MimeResponse)response).getWriter();

			if (session.getAttribute(Constants.CARD_REFERENCE_LIST) == null) {
				out.print(Boolean.TRUE);
			}
			else {
				out.print(Boolean.FALSE);
			}
		}
		catch (IOException e) {
			logger.error(
				"Could not write to the output stream. All cards could have been removed from the list, but doesn't show that on the display.");
		}

		logger.debug(
			"[clearCardReferenceList] The list has been cleared. " +
				session.getAttribute("cardReferenceList"));
		sessionStatus.setComplete();
	}

	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.CARD_DEREG_DEVICE
	)
	public String deregisterDevicePage(
		RenderResponse response, RenderRequest request,
		@ModelAttribute CardSearchForm cardSearchForm) {

		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String deviceHash = ParamUtil.getString(request, Constants.DEVICE_ID);
		String cuscalToken = getCuscalTokenFromRequest(request);
		logger.info(
			"[deregisterDevicePage] inside the Card Devices Render Mapping cardId [" +
				cardId + "], deviceHash [" + deviceHash + "]");
		logger.info(
			"[deregisterDevicePage] inside the Card Devices cuscalToken" +
				cuscalToken);
		showOptionalTabs(request);

		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);
		request.setAttribute(Constants.MOBILE_DEVICE_BLURB, mobileTabBlurb);
		showMobileTabs(request);

		logger.debug("deregisterDevicePage - mobile.blurb=" + mobileTabBlurb);

		try {
			final User user = PortalUtil.getUser(request);

			if (cardId != null) {
				CardInformation cardInformation = getCardInformationFromSwitch(
					response, request, cardId, cuscalToken);

				request.setAttribute("cardInfoData", cardInformation);
				String ipAddress = _getHttpServletRequest(
					request
				).getRemoteAddr();

				final DeActivateDevicePortal deActivateDeviceRequest =
					new DeActivateDevicePortal();
				deActivateDeviceRequest.setHeader(
					getCuscalAPIRequestHeader(ipAddress, user));
				deActivateDeviceRequest.setDeviceHash(deviceHash);

				DeActivateDevicePortalResponse deActivateDeviceResponse =
					cuscalApiService.deActivateDevicePortal(
						deActivateDeviceRequest);

				if (deActivateDeviceResponse.getHeader(
					).getResponseCode() == 0) {

					logger.debug(
						"[deregisterDevicePage] device [" + deviceHash +
							"] has been deactivated successfully.");
				}
				else {
					logger.error(
						"[deregisterDevicePage] failed to deactivate device [" +
							deviceHash + "] with response code [" +
								deActivateDeviceResponse.getHeader(
								).getResponseCode() + "]");
				}

				// Reload the device list

				reloadDeviceList(
					request, cardId, cardInformation.getPan(), ipAddress, user);
			}
			else {
				if (null != user)
					logger.warn(
						"[deregisterDevicePage] The cardId parameter is not provided or null for user  " +
							user.getUserId());
			}
		}
		catch (Exception e) {
			logger.error(
				"[deregisterDevicePage] the exception came message is  " +
					e.getMessage(),
				e);
			request.setAttribute(Constants.CARD_ERROR_MSG, Boolean.TRUE);

			return Constants.CARD_ERROR_PAGE;
		}

		return Constants.CARD_DETAIL_PAGE;
	}

	/**
	 * Shows the search results of the Cards
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@ActionMapping(
		params = Constants.CARD_ACTION + Constants.EQUALS + Constants.CARD_LIST
	)
	public void findCardsSearchList(
		@ModelAttribute CardSearchForm cardSearchForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.debug("findCardsSearchList - start");

		// IS_DATA_FROM_AUTHENTIC_EARTH session variable
		// set to False on click on Submit button

		HttpServletRequest httpServletRequest = _getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		session.setAttribute(Constants.CARD_LIST, null);
		session.setAttribute(
			Constants.IS_DATA_FROM_AUTHENTIC_EARTH, Boolean.FALSE);
		SearchHeader searchHeader = null;
		boolean isValidation = false;
		boolean isNotMoreOrg = false;
		boolean isDataNotNull = false;
		CardsListSearchResult cardsListSearchResult = null;

		try {
			searchHeader = getSearchHeaderData(request);

			if ((null != searchHeader) &&
				(null != searchHeader.getUserOrgId())) {

				isNotMoreOrg = true;

				// cardSearchForm.normalise(); no requirement for normalising
				// the input

				setOrganisationMapInCardSearchform(
					request, response, cardSearchForm);
				cardsSearchValidator.validate(cardSearchForm, bindingResult);
				logger.debug(
					"[findCardsSearchList] the validation is done  bindingResult.errorCount = " +
						bindingResult.getErrorCount());

				if (!bindingResult.hasErrors()) {
					isValidation = true;
					logger.info(
						"findCardsSearchList - the findCardsSearchList before calling setSortByAndPageNumberOnBackToListPage");
					logger.debug(
						"findCardsSearchList - cardSearchForm.getIssuerShortName=" +
							cardSearchForm.getIssuerShortName());
					cardsListSearchResult = new CardsListSearchResult();
					setSortByAndPageNumberOnBackToListPage(
						searchHeader, request, cardsListSearchResult);
					logger.debug(
						"findCardsSearchList - about to call cardsRouteService.getCardsList");

					List<Cards> cards = cardsRouteService.getCardsList(
						cardSearchForm, searchHeader,
						PortletContext.newContext(response, request));
					logger.debug(
						"findCardsSearchList - cardsRouteService.getCardsList returned");

					if ((null != cards) && (0 < cards.size())) {
						isDataNotNull = true;
						cardsListSearchResult.setCards(cards);
						cardsListSearchResult.setTotalCount(
							cards.get(
								0
							).getTotalListSize());
						List<CardData> cardsToPersist = new ArrayList<>();

						for (Cards o : cards) {
							CardData cardObject = new CardData().build(
								o.getPan(), o.getCuscalToken(),
								o.getInstitution());

							cardsToPersist.add(cardObject);
						}

						session.setAttribute(
							Constants.CARD_LIST, cardsToPersist);
						request.setAttribute(
							Constants.CARD_LIST,
							CardsListDisplayable.wrap(cardsListSearchResult));
						logger.info(
							"[findCardsSearchList] the findCardsSearchList after the web service method call list size is  " +
								cards.size());
					}
				}
				else {

					// log that we have bindingErrors (ie validation errors on the form)

					logger.warn(
						"findCardsSearchList - " +
							bindingResult.getErrorCount() +
								" error(s) found in card search parameters");
				}
			}

			if (!isNotMoreOrg) {
				request.setAttribute("moreOrg", Boolean.TRUE);
			}
			else if (!isValidation) {
				request.setAttribute("validationIssue", Boolean.TRUE);
			}
			else {
				if (!isDataNotNull)
					request.setAttribute(Constants.CARD_LIST, null);
			}

			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_SEARCH_RESULT);

			_setRenderParams(cardSearchForm, response);
		}
		catch (WebServiceFailureError fe) {
			logger.error(
				"[findCardsSearchList] the findCardsSearchList  WebServiceFailureError CAME ERROR MSG e.getCause()  is " +
					fe.getCause(),
				fe);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR_FAILURE);
		}
		catch (WebServiceException e) {
			logger.error(
				"[findCardsSearchList] the findCardsSearchList  WebServiceException CAME ERROR MSG e.getCause()  is " +
					e.getCause(),
				e);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}
		catch (Exception ex) {
			logger.error(
				"[findCardsSearchList] the findCardsSearchList  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}

		sessionStatus.setComplete();
		logger.debug("findCardsSearchList - end");
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
	 * Creates the Card Form based
	 *
	 * @return CardSearchForm
	 */
	@ModelAttribute(Constants.CARD_FORM)
	public CardSearchForm getCommandObject() {
		return cardsSearchService.createCardSearchForm();
	}

	/**
	 * @return the cuscalApiService
	 */
	public PortalServices getCuscalApiService() {
		return cuscalApiService;
	}

	/**
	 * Find the Customer access view using portal organisation id .
	 *
	 * @param portalOrgId
	 *            .
	 * @return List .
	 */
	public List<Customer> getCustomerAccessView(
			PortletRequest request, PortletResponse response)
		throws PortalException {

		long organisationId = 0l;
		User user = PortalUtil.getUser(request);

		Long userId = user.getUserId();

		if (StringUtils.isNotEmpty(organisationIdOverride)) {
			organisationId = Long.parseLong(organisationIdOverride);
		}
		else {
			List<Organization> organizations =
				OrganizationLocalServiceUtil.getUserOrganizations(userId);

			if (organizations.size() > 1) {
				logger.error(
					"User " + user.getScreenName() +
						" is not configured correctly with " +
							organizations.size() + " organisation(s) assigned");

				return null;
			}

			Organization org = organizations.get(0);

			organisationId = org.getOrganizationId();
		}

		List<Customer> customers = cardsSearchService.getCustomerAccessView(
			organisationId, PortletContext.newContext(response, request));

		if ((null != customers) && (customers.size() > 0)) {
			logger.debug(
				"User " + user.getScreenName() + " has access to " +
					customers.size() + " organisation(s): " + customers);
		}
		else {
			logger.error(
				"User " + user.getScreenName() +
					" has access to zero organisations ");
		}

		return customers;
	}

	/**
	 * @return the mobileDeviceRole
	 */
	public String getMobileDeviceRole() {
		return mobileDeviceRole;
	}

	/**
	 * @return the mobileTabBlurb
	 */
	public String getMobileTabBlurb() {
		return mobileTabBlurb;
	}

	public String getOrganisationIdOverride() {
		return organisationIdOverride;
	}

	/**
	 * Get the organisation list from Web service
	 *
	 * @param response
	 * @param request
	 * @return void
	 */
	public Map<String, String> getOrganisationList(
			PortletRequest request, PortletResponse response)
		throws Exception {

		logger.debug(
			"Inside the organisation resource mapping  method, calling the getCustomerAccessView method");
		Map<String, String> organisationsMap = new TreeMap<>();
		List<Customer> customers = getCustomerAccessView(request, response);

		for (Customer customer : customers) {
			if (isNotBlank(customer.getAuthenticIssuerShortName())) {
				logger.debug(
					"Adding  The customer name and  short names in map - customer name  =" +
						customer.getName() + " and short name  = " +
							customer.getAuthenticIssuerShortName());

				organisationsMap.put(
					customer.getName(), customer.getAuthenticIssuerShortName());
			}
		}

		return organisationsMap;
	}

	public String getOrganisationShortNameOverride() {
		return organisationShortNameOverride;
	}

	/**
	 * Get the org short name
	 *
	 * @param liferayOrgId
	 * @param portletRequest
	 * @return String
	 */
	public String getOrgShortName(
		String liferayOrgId, PortletRequest portletRequest) {

		logger.debug("getOrgShortName - start, liferayOrgId=" + liferayOrgId);

		if (StringUtils.isNotEmpty(organisationShortNameOverride)) {
			logger.debug(
				"getOrgShortName - end, organisationShortNameOverride has been set.  Using " +
					organisationShortNameOverride);

			return organisationShortNameOverride;
		}

		HttpServletRequest httpServletRequest = _getHttpServletRequest(
			portletRequest);

		HttpSession session = httpServletRequest.getSession();

		String orgShortName = (String)session.getAttribute(
			Constants.ORG_SHORT_NAME);

		if (orgShortName == null) {
			orgShortName = cardsSearchService.getOrgShortName(liferayOrgId);

			if (isNotBlank(orgShortName)) {
				session.setAttribute(Constants.ORG_SHORT_NAME, orgShortName);
			}
			else {
				logger.warn(
					"getOrgShortName - Could not find orgShortName for liferayOrgId=" +
						liferayOrgId);
			}
		}

		logger.debug(
			"getOrgShortName - end, returning organisation short name as " +
				orgShortName);

		return orgShortName;
	}

	/**
	 * create the searchHeader object for web service.
	 *
	 * @param request
	 * @return SearchHeader
	 */
	public SearchHeader getSearchHeaderData(PortletRequest request) {
		SearchHeader searchHeader = null;
		User user = null;

		try {
			user = PortalUtil.getUser(request);
			String screenName = null;

			if (null != user) {
				screenName = user.getScreenName();
				searchHeader = new SearchHeader();
				Long userId = user.getUserId();

				List<Organization> organizations =
					OrganizationLocalServiceUtil.getUserOrganizations(userId);

				if ((null != organizations) && (1 == organizations.size())) {
					String orgShortName = getOrgShortName(
						Long.valueOf(
							organizations.get(
								0
							).getOrganizationId()
						).toString(),
						request);
					String userOrgName = organizations.get(
						0
					).getName();

					if (isNotBlank(orgShortName)) {
						searchHeader.setUserOrgId(orgShortName);
					}
					else {
						searchHeader.setUserOrgId(null);
					}

					searchHeader.setUserOrgName(userOrgName);
					searchHeader.setUserId(screenName);
					searchHeader.setOrigin(AuditOrigin.PORTAL_ORIGIN);
					searchHeader.setPageNumber(Constants.DEFAULT_PAGE_NUMBER);
					searchHeader.setPageSize(Constants.DEFAULT_PAGE_SIZE);
				}
				else {
					searchHeader.setUserOrgId(null);

					if (organizations == null) {
						logger.warn(
							screenName + " has no organisations assigned!");
					}
					else {
						String orgNames = null;

						if (organizations != null) {
							for (Organization organization : organizations) {
								orgNames += " " + organization.getName();
							}
						}

						logger.warn(
							screenName + " has " + organizations.size() +
								" organisations assigned: " + orgNames);
					}
				}
			}
			else {
				logger.error(
					"User tried to search on cards after logging out or their session timed out.");
			}
		}
		catch (Exception e) {
			logger.error("Error setting search header: " + e.getMessage(), e);
		}

		return searchHeader;
	}

	/**
	 * @return the tokenLastUpdatedByBlurb
	 */
	public String getTokenLastUpdatedByBlurb() {
		return tokenLastUpdatedByBlurb;
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResourceMapping(Constants.TOKEN_LASTUPDATEDBY_BLURB)
	public void getTokenLastUpdatedByBlurb(
			ResourceRequest request, ResourceResponse response)
		throws Exception {

		logger.debug(" getTokenLastUpdatedByBlurb - start");
		PrintWriter out = response.getWriter();

		try {
			out.print(tokenLastUpdatedByBlurb);
		}
		catch (Exception e) {
			logger.error(
				"Exception while getting the Token LastUpdatedBy Blurb, exception message is " +
					e.getMessage());
		}

		logger.debug(" getTokenLastUpdatedByBlurb - end");
	}

	public void handleGetCardControlsResponseFromAuthentic3(
			GetCardControlsPortalResponse getCardControlsResponse,
			Map<Integer, CardControlsDomain> controls, RenderRequest request,
			CardSearchForm cardSearchForm)
		throws ParseException {

		if (null != getCardControlsResponse) {
			logger.debug(
				"getCardControls response header: " +
					getCardControlsResponse.getHeader(
					).getResponseCode());
			logger.debug(
				"getCardControls: " +
					getCardControlsResponse.getCardControls());

			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject)jsonParser.parse(
				getCardControlsResponse.getCardControls());

			String cuscalToken = (String)jsonObject.get("cuscalToken");

			logger.info("getCardControls: " + cuscalToken);
			request.setAttribute("cuscalToken", cuscalToken);

			Map<String, List<String>> hideIfEnabledMap = new HashMap<>();
			Map<String, String> controlValMap = new HashMap<>();

			JSONArray jsonArray = (JSONArray)jsonObject.get("cardControls");

			Iterator<JSONObject> it = jsonArray.iterator();
			int i = 0;

			while (it.hasNext()) {
				JSONObject control = it.next();

				String hideIfEnabled = null;

				if (control.get("dependency") != null) {
					JSONObject dependency = (JSONObject)jsonParser.parse(
						(String)control.get("dependency"));

					hideIfEnabled = (String)dependency.get("hide_if_enabled");

					if (hideIfEnabledMap.get(hideIfEnabled) == null) {
						hideIfEnabledMap.put(
							hideIfEnabled, new ArrayList<String>());
					}

					hideIfEnabledMap.get(
						hideIfEnabled
					).add(
						(String)control.get("controlID")
					);
				}

				if (control.get("currentValue") != null) {
					controlValMap.put(
						(String)control.get("controlID"),
						(String)control.get("currentValue"));
				}

				CardControlsDomain domain = new CardControlsDomain(
					(String)control.get("displayName"),
					(String)control.get("displayDesc"),
					(String)control.get("controlID"), hideIfEnabled,
					(String)control.get("displayType"),
					(Long)control.get("displayOrder"),
					(String)control.get("currentValue"));

				logger.debug(
					"getCardControls: " + control.get("displayName") + " : " +
						control.get("displayDesc"));

				controls.put(i, domain);
				i++;
			}

			JSONObject hideIfEnabledJsObject = new JSONObject(hideIfEnabledMap);

			request.setAttribute(
				"hideIfEnableds", hideIfEnabledJsObject.toJSONString());

			if (!controlValMap.isEmpty()) {
				JSONObject controlValJsObject = new JSONObject(controlValMap);

				request.setAttribute(
					"existingControlVals", controlValJsObject.toJSONString());
			}
			else {
				request.setAttribute("existingControlVals", "");
			}
		}

		if (getCardControlsResponse.getHeader(
			).getResponseCode() != 0) {

			String message =
				"There has been an error getting the card controls for " +
					cardSearchForm.getMaskedPan() + ". Error: " +
						getCardControlsResponse.getHeader(
						).getMessage();

			logger.error("showCardControlsPage - " + message);
			request.setAttribute("cardControlsError", Boolean.TRUE);
		}
	}

	public void handleGetCardControlsResponseFromAuthenticEarth(
		String getCardControlsSvcResponse,
		Map<Integer, CardControlsDomain> controls, RenderRequest request,
		CardSearchForm cardSearchForm, String cuscalToken) {

		try {
			logger.info("START - getCardControls handle Aux Earth response");
			JsonObject response = new Gson().fromJson(
				getCardControlsSvcResponse, JsonObject.class);

			JsonArray cardControls = response.getAsJsonObject(
				Constants.DATA
			).getAsJsonArray(
				"cardControls"
			);

			if ((cardControls != null) && (cardControls.size() > 0)) {
				logger.info("getCardControls - cuscal token : " + cuscalToken);
				logger.info(
					"getCardControls - cardControls retrieved : " +
						cardControls.size());
				request.setAttribute("cuscalToken", cuscalToken);
				Map<String, List<String>> hideIfEnabledMap = new HashMap<>();
				Map<String, String> controlValMap = new HashMap<>();
				Iterator<JsonElement> it = cardControls.iterator();
				int i = 0;

				while (it.hasNext()) {
					JsonElement o = it.next();

					JsonObject control = o.getAsJsonObject();

					String controlId = getAsString(control, "id");
					String value = getAsString(control, "value");
					String name = getAsString(control, "name");
					String description = getAsString(control, "description");
					String displayOrder = getAsString(control, "displayOrder");
					String displayType = getAsString(control, "displayType");
					JsonArray dependencies = control.getAsJsonArray(
						"dependencies");
					String hideIfEnabled = null;

					if ((dependencies != null) && (dependencies.size() > 0)) {
						for (JsonElement dependency : dependencies) {
							JsonObject _o = dependency.getAsJsonObject();

							hideIfEnabled = getAsString(_o, "key");

							if ("hide_if_enabled".equalsIgnoreCase(
									hideIfEnabled)) {

								JsonArray hideIfEnabledValue =
									_o.getAsJsonArray("value");

								if (hideIfEnabledMap.get(
										hideIfEnabledValue.get(
											0
										).getAsString()) == null) {

									hideIfEnabledMap.put(
										hideIfEnabledValue.get(
											0
										).getAsString(),
										new ArrayList<String>());
								}

								hideIfEnabledMap.get(
									hideIfEnabledValue.get(
										0
									).getAsString()
								).add(
									controlId
								);
							}
						}
					}

					if (isNotBlank(value)) {
						controlValMap.put(controlId, value);
					}

					CardControlsDomain domain = new CardControlsDomain(
						name, description, controlId, hideIfEnabled,
						displayType, Long.parseLong(displayOrder), value);

					logger.debug(
						"getCardControls: " + name + " : " + description);

					controls.put(i, domain);
					i++;
				}

				JSONObject hideIfEnabledJsObject = new JSONObject(
					hideIfEnabledMap);

				request.setAttribute(
					"hideIfEnableds", hideIfEnabledJsObject.toJSONString());

				if (!controlValMap.isEmpty()) {
					JSONObject controlValJsObject = new JSONObject(
						controlValMap);

					request.setAttribute(
						"existingControlVals",
						controlValJsObject.toJSONString());
				}
				else {
					request.setAttribute("existingControlVals", "");
				}
			}
		}
		catch (Exception ex) {
			String message =
				"There has been an error getting the card controls for " +
					cardSearchForm.getMaskedPan() + ". Error: " +
						ex.getMessage();

			logger.error("showCardControlsPage - " + message);
			request.setAttribute("cardControlsError", Boolean.TRUE);
		}
	}

	@ResourceMapping("removeCardFromCardReferenceList")
	@SuppressWarnings("unchecked")
	public void removeCardFromCardReferenceList(
		ResourceRequest request, ResourceResponse response,
		SessionStatus sessionStatus) {

		HttpServletRequest httpServletRequest = _getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		SearchHeader searchHeader = getSearchHeaderData(request);
		String cardId = ParamUtil.getString(request, "cardId");

		String pan = panFromCardId(searchHeader, cardId);

		SimpleCard card = new SimpleCard(cardId, pan);

		logger.debug(
			"[removeCardFromCardReferenceList] Removing the following card " +
				cardId);

		PrintWriter out;

		try {
			out = ((MimeResponse)response).getWriter();
			ArrayList<SimpleCard> cardReferenceList =
				(ArrayList<SimpleCard>)session.getAttribute(
					Constants.CARD_REFERENCE_LIST);
			//session.removeAttribute(Constants.CARD_REFERENCE_LIST);

			if (cardReferenceList.contains(card)) {
				cardReferenceList.remove(card);

				//session.setAttribute(Constants.CARD_REFERENCE_LIST, cardReferenceList);

				out.print(Boolean.TRUE);
			}
			else {
				out.print(Boolean.FALSE);
			}
		}
		catch (Exception e) {
			logger.error(
				"Could not write to the output stream. The card could have been removed from the list, but doesn't show that on the display");
		}

		sessionStatus.setComplete();
	}

	/**
	 * @param cuscalApiService the cuscalApiService to set
	 */
	public void setCuscalApiService(PortalServices cuscalApiService) {
		this.cuscalApiService = cuscalApiService;
	}

	/**
	 * Default value for sort order map for Cards list search
	 *
	 * @param void
	 * @return void
	 */
	public Map<String, String> setDefaultSortOrderMap() {
		Map<String, String> sortOrderMap = new HashMap<>();

		sortOrderMap.put(Constants.CURRENT_PAGE, "0");
		sortOrderMap.put(Constants.CURRENT_SORT, null);
		sortOrderMap.put(Constants.DEFUALT_SORT_NAME, Constants.ASSCENDING);
		sortOrderMap.put("expiryDate", Constants.DESSCENDING);
		sortOrderMap.put("issuerId", Constants.DESSCENDING);
		sortOrderMap.put("lastUpdated", Constants.ASSCENDING);
		sortOrderMap.put("pan", Constants.DESSCENDING);
		sortOrderMap.put("statusCode", Constants.DESSCENDING);

		return sortOrderMap;
	}

	/**
	 * @param mobileDeviceRole the mobileDeviceRole to set
	 */
	public void setMobileDeviceRole(String mobileDeviceRole) {
		this.mobileDeviceRole = mobileDeviceRole;
	}

	/**
	 * @param mobileTabBlurb the mobileTabBlurb to set
	 */
	public void setMobileTabBlurb(String mobileTabBlurb) {
		this.mobileTabBlurb = mobileTabBlurb;
	}

	public void setOrganisationIdOverride(String organisationIdOverride) {
		this.organisationIdOverride = organisationIdOverride;
	}

	/**
	 * Set OrganisationMap In CardSearchform
	 *
	 * @param response
	 * @param request
	 * @param cardSearchForm
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void setOrganisationMapInCardSearchform(
			PortletRequest request, PortletResponse response,
			CardSearchForm cardSearchForm)
		throws Exception {

		logger.debug("setOrganisationMapInCardSearchform - start");
		HttpServletRequest httpServletRequest = _getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();

		Map<String, String> organisationsMap =
			(Map<String, String>)session.getAttribute(Constants.ORG_LIST);

		if ((organisationsMap == null) || (organisationsMap.size() == 0)) {
			logger.debug(
				"setOrganisationMapInCardSearchform - organisationsMap is null or empty, requesting update from web service");
			organisationsMap = getOrganisationList(request, response);
			logger.debug(
				"setOrganisationMapInCardSearchform - found " +
					organisationsMap.size() + " organisation(s)");
		}
		else {
			logger.debug(
				"setOrganisationMapInCardSearchform - using cached value in session for organisationsMap");
		}

		logger.debug(
			"setOrganisationMapInCardSearchform - adding " +
				organisationsMap.size() + " organisation(s) to cardSearchForm");
		session.setAttribute(Constants.ORG_LIST, organisationsMap);
		cardSearchForm.setOrganisationsMap(organisationsMap);
	}

	public void setOrganisationShortNameOverride(
		String organisationShortNameOverride) {

		this.organisationShortNameOverride = organisationShortNameOverride;
	}

	/**
	 * Set the Sort order and page number in search header to perform sorting
	 * and paginations
	 *
	 * @param response
	 * @param portletRequest
	 * @param cardsListSearchResult
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void setSearchHeaderForSortingAndPaging(
		SearchHeader searchHeader, PortletRequest portletRequest,
		CardsListSearchResult cardsListSearchResult) {

		HttpServletRequest httpServletRequest = _getHttpServletRequest(
			portletRequest);

		HttpSession session = httpServletRequest.getSession();
		Map<String, String> sortOrderMap = new HashMap<>();
		logger.info(
			" Using ParamUtil, the page number in request is " +
				ParamUtil.getInteger(portletRequest, "page"));

		String sortBy = ParamUtil.getString(portletRequest, "sort");

		String orderBy = null;
		SortOrder order = new SortOrder();
		int pageBy = ParamUtil.getInteger(portletRequest, "page");

		if (null != session.getAttribute(Constants.SORT_ORDER_MAP)) {
			sortOrderMap = (HashMap<String, String>)session.getAttribute(
				Constants.SORT_ORDER_MAP);
			orderBy = sortOrderMap.get(sortBy);
		}
		else {
			sortOrderMap = setDefaultSortOrderMap();
			orderBy = sortOrderMap.get(sortBy);
		}

		if (0 == pageBy) {
			if (isNotBlank(sortBy)) {
				logger.info(
					"Sorting need to be perform and sort by is  " + sortBy +
						"order by is  " + orderBy);

				if (null != orderBy) {
					if (Constants.ASSCENDING.equalsIgnoreCase(orderBy)) {
						logger.info(
							"Sorting need to be perform  order by is DESCENDING ");
						order.setFieldToSort(sortBy);
						order.setOrderToSort(Constants.DESSCENDING);
						sortOrderMap.put(sortBy, Constants.DESSCENDING);
						cardsListSearchResult.setSortName(sortBy);
						cardsListSearchResult.setSortOrder(
							SortOrderEnum.DESCENDING);
					}
					else {
						logger.info(
							"Sorting need to be perform  order by is ASCENDING ");
						order.setFieldToSort(sortBy);
						order.setOrderToSort(Constants.ASSCENDING);
						sortOrderMap.put(sortBy, Constants.ASSCENDING);
						cardsListSearchResult.setSortName(sortBy);
						cardsListSearchResult.setSortOrder(
							SortOrderEnum.ASCENDING);
					}

					sortOrderMap.put(Constants.CURRENT_PAGE, "0");
					sortOrderMap.put(Constants.CURRENT_SORT, sortBy);
				}
			}
		}
		else {
			logger.info(
				"page number is not null and is int value is for ONlY Pagination " +
					pageBy);

			if (isNotBlank(sortBy)) {
				logger.info(
					"page number is not null and sort by is also NOTTT null ");
				order.setFieldToSort(sortBy);
				order.setOrderToSort(orderBy);

				if (Constants.ASSCENDING.equalsIgnoreCase(orderBy)) {
					cardsListSearchResult.setSortOrder(SortOrderEnum.ASCENDING);
				}
				else {
					cardsListSearchResult.setSortOrder(
						SortOrderEnum.DESCENDING);
				}

				cardsListSearchResult.setSortName(sortBy);
				sortOrderMap.put(Constants.CURRENT_SORT, sortBy);
			}
			else {
				logger.info(
					"page number is not null and sort by is also  null ");
				order.setFieldToSort(Constants.DEFUALT_SORT_NAME);
				order.setOrderToSort(Constants.ASSCENDING);
				sortOrderMap.put(Constants.CURRENT_SORT, null);
				cardsListSearchResult.setSortName(Constants.DEFUALT_SORT_NAME);
				cardsListSearchResult.setSortOrder(SortOrderEnum.ASCENDING);
			}

			logger.info(
				"page number is not null setting page number as integer " +
					Integer.valueOf(
						pageBy
					).toString());
			sortOrderMap.put(
				Constants.CURRENT_PAGE,
				Integer.valueOf(
					pageBy
				).toString());
			logger.info(
				"Finished : page number is not null setting page number as integer ");
			searchHeader.setPageNumber(pageBy);
			cardsListSearchResult.setPageNum(pageBy);
		}

		searchHeader.getSortOrders(
		).add(
			order
		);
		session.setAttribute(Constants.SORT_ORDER_MAP, sortOrderMap);
	}

	/**
	 * Session value for sort order map for Cards list search
	 *
	 * @param void
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void setSortByAndPageNumberOnBackToListPage(
		SearchHeader searchHeader, PortletRequest request,
		CardsListSearchResult cardsListSearchResult) {

		HttpServletRequest httpServletRequest = _getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		SortOrder order = new SortOrder();
		logger.info(
			"setSortByAndPageNumberOnBackToListPage  mapping. searchHeader org id " +
				searchHeader.getUserOrgId());

		String from = ParamUtil.getString(request, "from");

		if (!from.isEmpty() && "searchFrm".equalsIgnoreCase(from)) {
			logger.info(
				"setSortByAndPageNumberOnBackToListPage request.getParameter(from) mapping. set the session map as default ");
			session.setAttribute(
				Constants.SORT_ORDER_MAP, setDefaultSortOrderMap());
			cardsListSearchResult.setSortName(Constants.DEFUALT_SORT_NAME);
			cardsListSearchResult.setSortOrder(SortOrderEnum.ASCENDING);
			order.setFieldToSort(Constants.DEFUALT_SORT_NAME);
			order.setOrderToSort(Constants.ASSCENDING);
			searchHeader.getSortOrders(
			).add(
				order
			);
		}
		else {
			if (null != session.getAttribute(Constants.SORT_ORDER_MAP)) {
				Map<String, String> sortOrderMap =
					(HashMap<String, String>)session.getAttribute(
						Constants.SORT_ORDER_MAP);

				logger.info(
					"setSortByAndPageNumberOnBackToListPage  mapping. set the session map not default and of size " +
						sortOrderMap.size());

				if (null != sortOrderMap.get(Constants.CURRENT_SORT)) {
					String sortBy = sortOrderMap.get(
						Constants.CURRENT_SORT
					).toString();

					String orderBy = sortOrderMap.get(
						sortBy
					).toString();

					logger.info(
						"setSortByAndPageNumberOnBackToListPage  sort by and order by is " +
							sortBy + " and  " + orderBy);
					order.setFieldToSort(sortBy);
					order.setOrderToSort(orderBy);
					searchHeader.getSortOrders(
					).add(
						order
					);
					cardsListSearchResult.setSortName(sortBy);

					if (Constants.ASSCENDING.equalsIgnoreCase(orderBy)) {
						cardsListSearchResult.setSortOrder(
							SortOrderEnum.ASCENDING);
					}
					else {
						cardsListSearchResult.setSortOrder(
							SortOrderEnum.DESCENDING);
					}
				}
				else {
					cardsListSearchResult.setSortName(
						Constants.DEFUALT_SORT_NAME);
					cardsListSearchResult.setSortOrder(SortOrderEnum.ASCENDING);
					order.setFieldToSort(Constants.DEFUALT_SORT_NAME);
					order.setOrderToSort(Constants.ASSCENDING);
					searchHeader.getSortOrders(
					).add(
						order
					);
				}

				if (!"0".equals(
						sortOrderMap.get(
							Constants.CURRENT_PAGE
						).toString())) {

					int pageBy = Integer.valueOf(
						sortOrderMap.get(Constants.CURRENT_PAGE)
					).intValue();
					logger.info(
						"setSortByAndPageNumberOnBackToListPage  page by is  " +
							pageBy);
					searchHeader.setPageNumber(pageBy);
					cardsListSearchResult.setPageNum(pageBy);
				}
			}
		}
	}

	/**
	 * @param tokenLastUpdatedByBlurb the tokenLastUpdatedByBlurb to set
	 */
	public void setTokenLastUpdatedByBlurb(String tokenLastUpdatedByBlurb) {
		this.tokenLastUpdatedByBlurb = tokenLastUpdatedByBlurb;
	}

	@ActionMapping(
		params = Constants.CARD_ACTION + Constants.EQUALS + Constants.CARD_CONTROLS_HISTORY
	)
	public void showCardControlHistory(
		@ModelAttribute CardSearchForm cardSearchForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.info("[showCardControlHistory] start");

		String cardId = ParamUtil.getString(request, "cardId");
		final String cuscalToken = getCuscalTokenFromRequest(request);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);
		final String auditPageIndexStr = ParamUtil.getString(
			request, "ccHistoryPageIndex");
		cardId = escapeXml(cardId);
		showOptionalTabs(request);

		logger.info(
			"[showCardControlHistory] cuscal token = " + cuscalToken +
				", page index = " + auditPageIndexStr);

		int auditPageIndex = 1;

		if (auditPageIndexStr != null) {
			try {
				auditPageIndex = Integer.valueOf(
					auditPageIndexStr
				).intValue();

				if (auditPageIndex < 1) {
					auditPageIndex = 1;
				}
			}
			catch (NumberFormatException e) {
			}
		}

		try {
			List<String> eventTypes = new ArrayList<>();

			eventTypes.add(AuditRestService.EVENT_TYPE_CARD_CONTROL);

			List<AuditEvent> auditEvents = auditRestService.getAuditEvents(
				cuscalToken, auditPageIndex, AUDIT_HISTORY_PAGE_SIZE,
				eventTypes);

			final boolean auditPageMoreResults =
				(auditEvents != null) &&
				(auditEvents.size() == AUDIT_HISTORY_PAGE_SIZE);

			request.setAttribute(
				"ccHistoryEvents", mapAuditHistoryResults(auditEvents));
			request.setAttribute("ccHistoryMoreResults", auditPageMoreResults);
			request.setAttribute("ccHistoryPageIndex", auditPageIndex);
			request.setAttribute("toggleCardControlHistory", Boolean.TRUE);

			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_CONTROLS);
			response.setRenderParameter(Constants.CARD_ID, cardId);
		}
		catch (Exception e) {
			String message =
				"There has been an error getting card control audit history for " +
					cuscalToken + ". Error: " + e.getMessage();

			logger.error("showCardControlHistory - " + message);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}

		sessionStatus.setComplete();

		logger.info("[showCardControlHistory] end");
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(params = "render=cardControls")
	public String showCardControlsPage(
			RenderRequest request, RenderResponse response,
			@ModelAttribute CardSearchForm cardSearchForm)
		throws Exception {

		showMobileTabs(request);

		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String cuscalTokenFromRequest = getCuscalTokenFromRequest(request);
		logger.debug("Showing card controls for card ID: " + cardId);
		logger.info(
			"Showing card controls for card cuscal token: " +
				cuscalTokenFromRequest);
		cardSearchForm.setCardId(cardId);

		showOptionalTabs(request);

		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalTokenFromRequest);

		boolean cardControlsError = false;

		if (isNotBlank(ParamUtil.getString(request, "cardControlsError"))) {
			cardControlsError = Boolean.valueOf(
				ParamUtil.getString(request, "cardControlsError"));
		}

		if (!cardControlsError) {
			String ipAddress = _getHttpServletRequest(
				request
			).getRemoteAddr();
			CardInformation cardInformation = getCardInformationFromSwitch(
				response, request, cardId, cuscalTokenFromRequest);

			request.setAttribute("cardInfoData", cardInformation);
			cardSearchForm.setPanOrBin(cardInformation.getPan());
			GetCardControlsPortalResponse getCardControlsResponse = null;
			Map<Integer, CardControlsDomain> controls = new HashMap<>();
			request.setAttribute("controls", controls);

			if (isDataFromAuthenticEarth(request)) {
				String getCardControlsSvcResponse =
					cardsRouteService.getCardControls(
						cuscalTokenFromRequest, getSearchHeaderData(request));

				handleGetCardControlsResponseFromAuthenticEarth(
					getCardControlsSvcResponse, controls, request,
					cardSearchForm, cuscalTokenFromRequest);
			}
			else {
				final User user = PortalUtil.getUser(request);
				getCardControlsResponse = getCardControls(
					ipAddress, user, cardInformation.getPan());
				handleGetCardControlsResponseFromAuthentic3(
					getCardControlsResponse, controls, request, cardSearchForm);
			}
		}
		else {
			request.setAttribute("cardControlsError", Boolean.TRUE);
		}

		request.setAttribute("show7", Boolean.TRUE);

		return Constants.CARD_DETAIL_PAGE;
	}

	/**
	 * Shows the detail page with Error message in failure case
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.CARD_UPDATE_ERROR
	)
	public String showCardDetailPageWithErrorMsg(
		RenderResponse response, RenderRequest request) {

		logger.info(
			"Failure for VALIDATION_ERROR  or NOT SUCCESSSFUL  on card Detail in case of card status update start");
		request.setAttribute("cardInfoData", null);
		request.setAttribute("accInfoData", null);
		request.setAttribute("CardLmtData", null);
		request.setAttribute("show1", Boolean.TRUE);
		logger.info(
			"Failure for VALIDATION_ERROR  or NOT SUCCESSSFUL  on card Detail in case of card status update END");

		return Constants.CARD_DETAIL_PAGE;
	}

	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.CARD_DEVICES
	)
	public String showCardDevicesPage(
		RenderResponse response, RenderRequest request,
		@ModelAttribute CardSearchForm cardSearchForm) {

		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String cuscalToken = getCuscalTokenFromRequest(request);

		logger.info(
			"[showCardDevicesPage] inside the Card Devices Render Mapping cardId - " +
				cardId + ", cuscalToken - " + cuscalToken);
		showOptionalTabs(request);

		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);
		request.setAttribute(Constants.MOBILE_DEVICE_BLURB, mobileTabBlurb);
		showMobileTabs(request);
		logger.info(
			"showCardDevicesPage - " + Constants.MOBILE_DEVICE_BLURB + "=" +
				mobileTabBlurb);

		try {
			final User user = PortalUtil.getUser(request);

			if (cardId != null) {
				CardInformation cardInformation = getCardInformationFromSwitch(
					response, request, cardId, cuscalToken);

				request.setAttribute("cardInfoData", cardInformation);
				final String ipAddress = _getHttpServletRequest(
					request
				).getRemoteAddr();
				reloadDeviceList(
					request, cardId, cardInformation.getPan(), ipAddress, user);
			}
			else {
				if (null != user)
					logger.warn(
						"[showCardDevicesPage] The cardId parameter is not provided or null for user  " +
							user.getUserId());
			}
		}
		catch (Exception e) {
			logger.error(
				"[showCardDevicesPage] the exception came message is  " +
					e.getMessage(),
				e);
			request.setAttribute(Constants.CARD_ERROR_MSG, Boolean.TRUE);

			return Constants.CARD_ERROR_PAGE;
		}

		return Constants.CARD_DETAIL_PAGE;
	}

	/**
	 * Shows the Error page of the Cards
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.CARD_ERROR
	)
	public String showCardErrorPage(
		RenderResponse response, RenderRequest request) {

		logger.error("INSIDE THE CARD_ERROR URL OF RENDER MAPPING  Start");
		request.setAttribute(Constants.CARD_ERROR_MSG, Boolean.TRUE);
		logger.error("INSIDE THE CARD_ERROR URL OF RENDER MAPPING  End ");

		return Constants.CARD_ERROR_PAGE;
	}

	/**
	 *
	 * @param response
	 * @param request
	 * @param cardSearchForm
	 * @return
	 */
	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.CARD_LIMITS
	)
	public String showCardLimitsPage(
			RenderResponse response, RenderRequest request,
			@ModelAttribute CardSearchForm cardSearchForm)
		throws Exception {

		logger.info("[showLimitsPage] start");

		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String cuscalTokenFromRequest = getCuscalTokenFromRequest(request);
		cardSearchForm.setCardId(cardId);
		logger.info("[showLimitsPage] cardId - " + cardId);
		logger.info(
			"[showLimitsPage] cuscalTokenFromRequest - " +
				cuscalTokenFromRequest);

		showOptionalTabs(request);

		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalTokenFromRequest);

		showMobileTabs(request);

		boolean limitsError = false;

		if (isNotBlank(ParamUtil.getString(request, "limitsError"))) {
			limitsError = Boolean.valueOf(
				ParamUtil.getString(request, "limitsError"));
		}

		if (!limitsError) {
			try {
				if (cardId != null) {
					SearchHeader searchHeader = getSearchHeaderData(request);
					CardInformation cardInformation =
						getCardInformationFromSwitch(
							response, request, cardId, cuscalTokenFromRequest);

					request.setAttribute("cardInfoData", cardInformation);
					cardSearchForm.setPanOrBin(cardInformation.getPan());

					OrganisationAccess orgAccess = cardRestService.getOrgAccess(
						searchHeader);

					String cuscalToken = cuscalTokenWebService.getToken(
						cardInformation.getPan());
					request.setAttribute("cuscalToken", cuscalToken);

					List<CardLimit> cardLimits = null;
					CardLimit[] cardLimitArray = cardRestService.getCardLimits(
						cardSearchForm, searchHeader,
						PortletContext.newContext(response, request), orgAccess,
						cuscalToken);

					if ((cardLimitArray != null) &&
						(cardLimitArray.length != 0)) {

						cardLimits = new ArrayList<>();

						for (CardLimit cardLimit : cardLimitArray) {
							cardLimits.add(cardLimit);

							Map<String, String> limitActionMap =
								new HashMap<>();

							cardLimit.setLimitActionMap(limitActionMap);

							if (ON.getName(
								).equals(
									cardLimit.getStatus()
								)) {

								limitActionMap.put(
									REMOVE.getName(), REMOVE.getCode());

								if (!TRANSACTION.getName(
									).equals(
										cardLimit.getPeriod()
									)) {

									limitActionMap.put(
										SUSPEND.getName(), SUSPEND.getCode());
								}

								limitActionMap.put(
									SET.getName(), SET.getCode());
							}
							else if (SUSPENDED.getName(
									).equals(
										cardLimit.getStatus()
									)) {

								limitActionMap.put(
									REMOVE.getName(), REMOVE.getCode());
								limitActionMap.put(
									RESUME.getName(), RESUME.getCode());

								cardLimit.setStatus(
									"Suspended until " +
										dt.format(cardLimit.getResumeDate()));
							}
							else {
								limitActionMap.put(
									SET.getName(), SET.getCode());
							}

							CardSpend cardSpend = null;

							if (TRANSACTION.getName(
								).equals(
									cardLimit.getPeriod()
								)) {

								cardLimit.setSpend("N/A");
							}
							else if (DAY.getName(
									).equals(
										cardLimit.getPeriod()
									)) {

								cardSpend = cardRestService.getCardSpend(
									orgAccess, cuscalToken, DAY.getName());
								cardLimit.setSpend(
									Long.valueOf(
										cardSpend.getSpend()
									).toString());
							}
							else if (WEEK.getName(
									).equals(
										cardLimit.getPeriod()
									)) {

								cardSpend = cardRestService.getCardSpend(
									orgAccess, cuscalToken, WEEK.getName());
								cardLimit.setSpend(
									Long.valueOf(
										cardSpend.getSpend()
									).toString());
							}
						}

						request.setAttribute("cardLimits", cardLimits);
					}
				}
				else {
					User user = PortalUtil.getUser(request);

					if (null != user)
						logger.warn(
							"[showLimitsPage] The cardId parameter is not provided or null for user  " +
								user.getUserId());
				}
			}
			catch (Exception e) {
				String message =
					"There has been an error getting the card limits for" +
						cardSearchForm.getMaskedPan() + ". Error: " +
							e.getMessage();

				logger.error("showLimitsPage - " + message);
				request.setAttribute("cardLimitsError", Boolean.TRUE);
			}
		}
		else {
			request.setAttribute("cardLimitsError", Boolean.TRUE);
		}

		request.setAttribute("show9", Boolean.TRUE);

		logger.info("[showLimitsPage] end");

		return Constants.CARD_DETAIL_PAGE;
	}

	/**
	 * Shows the Details results of the Cards
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.CARD_DETAIL
	)
	public String showCardsDetailsPage(
		RenderResponse response, RenderRequest request,
		@ModelAttribute CardSearchForm cardSearchForm) {

		String cardId = null;
		String cuscalToken = null;
		String cardTab = null;
		logger.info(
			"[showCardsDetailsPage] inside the Card Details Render Mapping ");
		final boolean showCardLimits = isShowCardLimitsTab(request);

		showOptionalTabs(request);

		showMobileTabs(request);

		try {
			String paramCuscalToken = ParamUtil.getString(
				request, Constants.CUSCAL_TOKEN);

			if (!paramCuscalToken.isEmpty()) {
				cuscalToken = paramCuscalToken;
			}
			else if (null != request.getAttribute(Constants.CUSCAL_TOKEN)) {
				cuscalToken = request.getAttribute(
					Constants.CUSCAL_TOKEN
				).toString();
			}

			String paramCardId = ParamUtil.getString(
				request, Constants.CARD_ID);

			if (!paramCardId.isEmpty()) {
				cardId = paramCardId;
			}
			else if (null != request.getAttribute(Constants.CARD_ID)) {
				cardId = request.getAttribute(
					Constants.CARD_ID
				).toString();
			}

			if ((null != cardId) || (null != cuscalToken)) {
				SearchHeader searchHeader = getSearchHeaderData(request);
				logger.debug("[showCardsDetailsPage] card Id: " + cardId);

				String paramCardInfoTab = ParamUtil.getString(
					request, Constants.CARD_INFO_TAB);

				if (!paramCardInfoTab.isEmpty()) {
					cardTab = paramCardInfoTab;
				}
				else if (null != request.getAttribute(
							Constants.CARD_INFO_TAB)) {

					cardTab = request.getAttribute(
						Constants.CARD_INFO_TAB
					).toString();
				}

				if ((null != cardTab) && "CardInfo".equalsIgnoreCase(cardTab)) {
					List<String> groups = Utility.allUserGroupRoles(request);

					logger.debug(
						"The user group for this user is of size  " +
							groups.size());
					request.setAttribute("show1", Boolean.TRUE);
					request.setAttribute(Constants.CARD_ID, cardId);
					request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);

					CardInformation cardInformation = new CardInformation();

					if (isDataFromAuthenticEarth(request)) {
						logger.info(
							"[showCardsDetailsPage] Going to call authentic Earth");
						cardInformation = cardsRouteService.getCardInformation(
							cuscalToken, searchHeader, groups,
							PortletContext.newContext(response, request));
					}
					else {
						logger.info(
							"[showCardsDetailsPage] Going to call authentic");
						cardInformation =
							cardsSearchService.getCardInformationOnCardDetails(
								cardId, searchHeader, groups,
								PortletContext.newContext(response, request));
					}

					if (null != request.getAttribute(
							Constants.IS_CARD_STATUS_UPADTE)) {

						String isUpdate = request.getAttribute(
							Constants.IS_CARD_STATUS_UPADTE
						).toString();
						request.setAttribute("noMsg", Boolean.TRUE);

						if (!"failed".equalsIgnoreCase(isUpdate)) {
							logger.debug(
								"Finished web service call update message display on UI Reference number is " +
									isUpdate);
							request.setAttribute(
								Constants.IS_CARD_STATUS_UPADTE, isUpdate);
						}
					}

					request.setAttribute("cardInfoData", cardInformation);
					logger.debug(
						"Finished web service call for cardInformation ");
				}
				else if ((null != cardTab) &&
						 "CardLmt".equalsIgnoreCase(cardTab) &&
						 showCardLimits) {

					request.setAttribute(Constants.CARD_ID, cardId);
					request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);
					request.setAttribute("show2", Boolean.TRUE);

					CardLimits cardLimits = null;

					if (isDataFromAuthenticEarth(request)) {
						logger.info(
							"[showCardsDetailsPage] Going to call authentic Earth");
						cardLimits = cardsRouteService.getCardBasedLimits(
							cuscalToken, searchHeader,
							PortletContext.newContext(response, request));
					}
					else {
						logger.info(
							"[showCardsDetailsPage] Going to call authentic");
						cardLimits =
							cardsSearchService.getCardLimitsOnCardDetails(
								cardId, searchHeader,
								PortletContext.newContext(response, request));
					}

					request.setAttribute("CardLmtData", cardLimits);

					if (null == cardLimits) {
						request.setAttribute("noCardLimit", Boolean.TRUE);
					}
				}
				else {
					request.setAttribute(Constants.CARD_ID, cardId);
					request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);
					request.setAttribute("show3", Boolean.TRUE);

					List<AccountInformation> accountInformations = null;

					if (isDataFromAuthenticEarth(request)) {
						logger.info(
							"[showCardsDetailsPage] Going to call authentic Earth");
						accountInformations = cardsRouteService.getCardAccounts(
							cuscalToken, searchHeader,
							PortletContext.newContext(response, request));
					}
					else {
						logger.info(
							"[showCardsDetailsPage] Going to call authentic");
						accountInformations =
							cardsSearchService.
								getCardAccountInformationOnDetails(
									cardId, searchHeader,
									PortletContext.newContext(
										response, request));
					}

					request.setAttribute("accInfoData", accountInformations);

					if (null == accountInformations) {
						request.setAttribute("noCardInfo", Boolean.TRUE);
					}
				}
			}
			else {
				User user = PortalUtil.getUser(request);

				if (null != user)
					logger.warn(
						"The cardId parameter is not provided or null for user  " +
							user.getUserId());
			}
		}
		catch (WebServiceFailureError fe) {
			logger.error(
				"[showCardsDetailsPage] the exception came message is  " +
					fe.getMessage(),
				fe);

			request.setAttribute("accInfoData", null);
			request.setAttribute("cardInfoData", null);
			request.setAttribute("CardLmtData", null);

			return Constants.CARD_DETAIL_PAGE;
		}
		catch (WebServiceException we) {
			logger.error(
				"[showCardsDetailsPage] the exception came message is  " +
					we.getMessage(),
				we);
			request.setAttribute(Constants.CARD_ERROR_MSG, Boolean.TRUE);

			return Constants.CARD_ERROR_PAGE;
		}
		catch (Exception e) {
			logger.error(
				"[showCardsDetailsPage] the exception came message is  " +
					e.getMessage(),
				e);
			request.setAttribute(Constants.CARD_ERROR_MSG, Boolean.TRUE);

			return Constants.CARD_ERROR_PAGE;
		}

		return Constants.CARD_DETAIL_PAGE;
	}

	/**
	 * Shows the initial view of the Card form
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@RenderMapping
	public String showCardSearchPage(
		RenderResponse response, RenderRequest request,
		@ModelAttribute CardSearchForm cardSearchForm) {

		logger.debug("showCardSearchPage - start");

		String returnPage = null;

		try {
			cardSearchForm.resetForm();
			request.setAttribute(Constants.CARD_LIST, null);
			setOrganisationMapInCardSearchform(
				request, response, cardSearchForm);
			returnPage = Constants.CARD_SEARCH_PAGE;
		}
		catch (Exception e) {
			request.setAttribute(Constants.CARD_ERROR_MSG, Boolean.TRUE);
			returnPage = Constants.CARD_ERROR_PAGE;

			logger.error("Exception message in showCardSearchPage is ", e);
		}

		logger.debug("showCardSearchPage - end, returnng page=" + returnPage);

		return returnPage;
	}

	/**
	 * Shows the search page with Error message in failure case
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.CARD_ERROR_FAILURE
	)
	public String showCardSearchPageWithErrorMsg(
		RenderResponse response, RenderRequest request,
		@ModelAttribute CardSearchForm cardSearchForm) {

		logger.info(
			"Failure for VALIDATION_ERROR  or NOT SUCCESSSFUL  on card search start");
		String returnPage = null;

		try {
			cardSearchForm.resetForm();
			request.setAttribute(Constants.CARD_LIST, null);
			setOrganisationMapInCardSearchform(
				request, response, cardSearchForm);
			request.setAttribute("noRecordsFound", "");
			request.setAttribute("failureError", Boolean.TRUE);
			returnPage = Constants.CARD_SEARCH_PAGE;
			logger.info(
				"Failure for VALIDATION_ERROR  or NOT SUCCESSSFUL  on card search End");
		}
		catch (Exception e) {
			request.setAttribute(Constants.CARD_ERROR_MSG, Boolean.TRUE);
			returnPage = Constants.CARD_ERROR_PAGE;

			logger.error(
				"Exception message in showCardSearchPage is    " +
					e.getMessage(),
				e);
		}

		return returnPage;
	}

	/**
	 * Clear the list and search form validation of the Card Search form
	 *
	 * @param response
	 * @param request
	 * @param cardSearchForm
	 * @return string
	 * @throws Exception
	 */
	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.CARD_RESET
	)
	public String showCardsSearchResult(
			RenderResponse response, RenderRequest request,
			@ModelAttribute CardSearchForm cardSearchForm)
		throws Exception {

		logger.debug(" INSIDE THE CARD_RESET URL OF RENDER MAPPING  ");
		cardSearchForm.resetForm();
		CardsListSearchResult cardsListSearchResult =
			new CardsListSearchResult();

		cardsListSearchResult.setCards(null);
		request.setAttribute(Constants.CARD_LIST, null);
		setOrganisationMapInCardSearchform(request, response, cardSearchForm);

		return Constants.CARD_SEARCH_PAGE;
	}

	/**
	 * Shows the search results of the Cards
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.CARD_SEARCH_RESULT
	)
	public String showCardsSearchResultList(
		RenderResponse response, RenderRequest request, ModelMap model) {

		logger.debug("INSIDE THE CARDS MAIN SEARCH RESULT RENDER MAPPING  ");
		CardsListSearchResult cardsListSearchResult = null;
		HttpServletRequest httpServletRequest = _getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession();
		String sortBy = ParamUtil.getString(request, "sort");
		int pageBy = ParamUtil.getInteger(request, "page");

		try {
			if (null != request.getAttribute(Constants.CARD_LIST)) {
				logger.debug(
					"[showCardsSearchResultList]  the card list is not null and default sorting and pagination ");
				request.setAttribute(
					Constants.CARD_LIST,
					request.getAttribute(Constants.CARD_LIST));
			}
			else if (isNotBlank(sortBy) || (0 != pageBy)) {
				logger.debug(
					" Using ParamUtil, the sort  in request is " + sortBy);
				logger.debug(
					"[showCardsSearchResultList] the pageBy  value is  " +
						pageBy);
				cardsListSearchResult = new CardsListSearchResult();
				SearchHeader searchHeader = getSearchHeaderData(request);

				setSearchHeaderForSortingAndPaging(
					searchHeader, request, cardsListSearchResult);

				if (null != searchHeader) {
					logger.info(
						"[showCardsSearchResultList]  the showCardsSearchResultList render mapping. searchHeader org id " +
							searchHeader.getUserOrgId());

					CardSearchForm cardSearchForm = (CardSearchForm)model.get(
						"cardSearchForm");

					logger.debug(
						"[showCardsSearchResultList]  the search form object is " +
							cardSearchForm);

					showCardSearchPage(response, request, cardSearchForm);

					if (ParamUtil.getBoolean(request, "implicitModel")) {
						_overrideAttributes(request, cardSearchForm);
					}

					List<Cards> cards = cardsRouteService.getCardsList(
						cardSearchForm, searchHeader,
						PortletContext.newContext(response, request));

					if ((null != cards) && (cards.size() > 0)) {
						logger.debug(
							"[showCardsSearchResultList]  after web service call Finished size of cards is " +
								cards.size());
						cardsListSearchResult.setCards(cards);
						cardsListSearchResult.setTotalCount(
							cards.get(
								0
							).getTotalListSize());
						List<CardData> cardsToPersist = new ArrayList<>();

						for (Cards o : cards) {
							CardData cardObject = new CardData().build(
								o.getPan(), o.getCuscalToken(),
								o.getInstitution());

							cardsToPersist.add(cardObject);
						}

						session.setAttribute(
							Constants.CARD_LIST, cardsToPersist);
						request.setAttribute(
							Constants.CARD_LIST,
							CardsListDisplayable.wrap(cardsListSearchResult));
					}
				}
			}
			else {
				if ((null == request.getAttribute("moreOrg")) &&
					(null == request.getAttribute("validationIssue"))) {

					logger.debug(
						"[showCardsSearchResultList] set no result found msg ");
					request.setAttribute(
						"noRecordsFound", Constants.CARD_NODATA_FOUND);
				}

				request.setAttribute(Constants.CARD_LIST, null);
			}
		}
		catch (Exception e) {
			logger.error(
				"The showCardsSearchResultList  EXCEPTION CAME ERROR MSG   is " +
					e.getMessage(),
				e);
			request.setAttribute(Constants.CARD_ERROR_MSG, Boolean.TRUE);

			return Constants.CARD_ERROR_PAGE;
		}

		return Constants.CARD_SEARCH_PAGE;
	}

	/**
	 *
	 * @param response
	 * @param request
	 * @param cardSearchForm
	 * @return
	 */
	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.TOKEN_WALLETS
	)
	public String showCardWalletsPage(
		RenderResponse response, RenderRequest request,
		@ModelAttribute CardSearchForm cardSearchForm) {

		logger.info("[showCardWalletsPage] start");

		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String cuscalToken = getCuscalTokenFromRequest(request);
		cardSearchForm.setCardId(cardId);
		logger.info("[showCardWalletsPage] cardId - " + cardId);
		logger.info("[showCardWalletsPage] cuscalToken - " + cuscalToken);
		showOptionalTabs(request);

		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);
		showMobileTabs(request);

		try {
			if (cardId != null) {
				SearchHeader searchHeader = getSearchHeaderData(request);
				CardInformation cardInformation = getCardInformationFromSwitch(
					response, request, cardId, cuscalToken);

				cardSearchForm.setPanOrBin(cardInformation.getPan());
				request.setAttribute("cardInfoData", cardInformation);
				TokenSearchResult searchResult =
					cardsSearchService.searchTokens(
						cardSearchForm, searchHeader,
						PortletContext.newContext(response, request));

				logger.debug(
					"[showCardWalletsPage] cardId: " + cardId + " has " +
						(searchResult.getTokens() == null ? 0 :
							searchResult.getTokens(
							).size()) + " tokens attached to it" +
								(searchResult.isPartialSuccess() ?
									", and it's partial successful." : "."));

				request.setAttribute("show5", Boolean.TRUE);
				request.setAttribute(
					"tokens", decorateTokens(searchResult.getTokens()));
				request.setAttribute(
					"tokenSearchPartialSuccess",
					searchResult.isPartialSuccess());
			}
			else {
				User user = PortalUtil.getUser(request);

				if (null != user)
					logger.warn(
						"[showCardWalletsPage] The cardId parameter is not provided or null for user  " +
							user.getUserId());
			}
		}
		catch (Exception e) {
			logger.error(
				"[showCardWalletsPage] the exception came message is  " +
					e.getMessage(),
				e);
			request.setAttribute(Constants.CARD_ERROR_MSG, Boolean.TRUE);

			return Constants.CARD_ERROR_PAGE;
		}

		logger.info("[showCardWalletsPage] end");

		return Constants.CARD_DETAIL_PAGE;
	}

	@ActionMapping(
		params = Constants.CARD_ACTION + Constants.EQUALS + Constants.CARD_LIMITS_HISTORY
	)
	public void showLimitslHistory(
		@ModelAttribute CardSearchForm cardSearchForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.info("[showLimitslHistory] start");

		String cardId = ParamUtil.getString(request, "cardId");
		final String cuscalToken = getCuscalTokenFromRequest(request);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);
		final String auditPageIndexStr = ParamUtil.getString(
			request, "limitsHistoryPageIndex");
		cardId = escapeXml(cardId);
		showOptionalTabs(request);

		logger.info(
			"[showLimitslHistory] cuscal token = " + cuscalToken +
				", page index = " + auditPageIndexStr);

		int auditPageIndex = 1;

		if (auditPageIndexStr != null) {
			try {
				auditPageIndex = Integer.valueOf(
					auditPageIndexStr
				).intValue();

				if (auditPageIndex < 1) {
					auditPageIndex = 1;
				}
			}
			catch (NumberFormatException e) {
			}
		}

		try {
			List<String> eventTypes = new ArrayList<>();

			eventTypes.add(AuditRestService.EVENT_TYPE_CARD_LIMITS);

			List<AuditEvent> auditEvents = auditRestService.getAuditEvents(
				cuscalToken, auditPageIndex, AUDIT_HISTORY_PAGE_SIZE,
				eventTypes);

			final boolean auditPageMoreResults =
				(auditEvents != null) &&
				(auditEvents.size() == AUDIT_HISTORY_PAGE_SIZE);

			request.setAttribute(
				"limitsHistoryEvents",
				mapLimitsAuditHistoryResults(auditEvents));
			request.setAttribute(
				"limitsHistoryMoreResults", auditPageMoreResults);
			request.setAttribute("limitsHistoryPageIndex", auditPageIndex);
			request.setAttribute("toggleLimitsHistory", Boolean.TRUE);

			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_LIMITS);
			response.setRenderParameter(Constants.CARD_ID, cardId);
		}
		catch (Exception e) {
			String message =
				"There has been an error getting card limits audit history for " +
					cuscalToken + ". Error: " + e.getMessage();

			logger.error("showLimitslHistory - " + message);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}

		sessionStatus.setComplete();

		logger.info("[showLimitslHistory] end");
	}

	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.LOG_POOLS
	)
	public String showLogPools(
		RenderResponse response, RenderRequest request,
		@ModelAttribute CardSearchForm cardSearchForm) {

		logger.debug("showLogPools - start");
		String returnPage = showCardSearchPage(
			response, request, cardSearchForm);
		cardsSearchService.logPoolStatistics();
		logger.info("showLogPools - end");

		return returnPage;
	}

	/**
	 *
	 * @param response
	 * @param request
	 * @param cardSearchForm
	 * @return
	 */
	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.MCC_CONTROLS
	)
	public String showMccControlsPage(
			RenderResponse response, RenderRequest request,
			@ModelAttribute CardSearchForm cardSearchForm)
		throws Exception {

		logger.info("[showMccControlsPage] start");

		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String cuscalTokenFromRequest = getCuscalTokenFromRequest(request);
		cardSearchForm.setCardId(cardId);
		logger.info("[showMccControlsPage] cardId - " + cardId);
		logger.info(
			"[showMccControlsPage] cuscalTokenFromRequest - " +
				cuscalTokenFromRequest);

		showOptionalTabs(request);

		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalTokenFromRequest);

		showMobileTabs(request);

		boolean mccControlsError = false;

		if (isNotBlank(ParamUtil.getString(request, "mccControlsError"))) {
			mccControlsError = Boolean.valueOf(
				ParamUtil.getString(request, "mccControlsError"));
		}

		if (!mccControlsError) {
			try {
				if (cardId != null) {
					SearchHeader searchHeader = getSearchHeaderData(request);
					CardInformation cardInformation =
						getCardInformationFromSwitch(
							response, request, cardId, cuscalTokenFromRequest);

					request.setAttribute("cardInfoData", cardInformation);
					cardSearchForm.setPanOrBin(cardInformation.getPan());

					OrganisationAccess orgAccess = cardRestService.getOrgAccess(
						searchHeader);

					String cuscalToken = cuscalTokenWebService.getToken(
						cardInformation.getPan());

					logger.info(
						String.format(
							"Cuscal token [%s] for PAN [%s]", cuscalToken,
							cardInformation.getMaskedPan()));
					request.setAttribute("cuscalToken", cuscalToken);

					int mccCheckedTotal = 0;
					int mccTotal = 0;

					List<MccGroup> mccGroups = null;

					String getMccControlsSvcResponse =
						cardRestService.getMccControls(orgAccess, cuscalToken);

					JsonObject jsonResponse = new Gson().fromJson(
						getMccControlsSvcResponse, JsonObject.class);

					JsonArray mccGroupArray = jsonResponse.getAsJsonObject(
						Constants.DATA
					).getAsJsonArray(
						"mccGroups"
					);

					if ((mccGroupArray != null) &&
						(mccGroupArray.size() != 0)) {

						mccGroups = new ArrayList<>();

						boolean isAllBlocked = true;

						Iterator<JsonElement> it = mccGroupArray.iterator();
						int i = 0;

						while (it.hasNext()) {
							JsonObject mccGroupJson = it.next(
							).getAsJsonObject();

							MccGroup mccGroup = new MccGroup();

							mccGroup.setId(
								Long.valueOf(getAsString(mccGroupJson, "id")));
							mccGroup.setName(getAsString(mccGroupJson, "name"));
							mccGroup.setValue(
								getAsString(mccGroupJson, "value"));
							mccGroup.setDescription(
								getAsString(mccGroupJson, "description"));

							String mccGroupDesc = "MCCs: ";
							JsonArray mccs = mccGroupJson.getAsJsonArray(
								"MCCs");

							Iterator<JsonElement> mccsIt = mccs.iterator();

							while (mccsIt.hasNext()) {
								JsonObject mcc = mccsIt.next(
								).getAsJsonObject();

								mccGroupDesc +=
									getAsString(mcc, "code") + "-" +
										getAsString(mcc, "merchantType") + "; ";
							}

							mccGroup.setDescription(
								mccGroupDesc.substring(
									0, mccGroupDesc.length() - 2));
							mccGroups.add(mccGroup);

							isAllBlocked =
								isAllBlocked &&
								mccGroup.getValue(
								).equals(
									"1"
								);

							if (mccGroup.getValue(
								).equals(
									"1"
								)) {

								mccCheckedTotal++;
							}

							mccTotal++;
						}

						mccGroups.sort(
							new Comparator<MccGroup>() {

								@Override
								public int compare(MccGroup o1, MccGroup o2) {
									return Long.compare(o1.getId(), o2.getId());
								}

							});

						request.setAttribute(
							"mccCheckedTotal", mccCheckedTotal);
						request.setAttribute("mccControls", mccGroups);
						request.setAttribute("mccTotal", mccTotal);
					}
				}
				else {
					User user = PortalUtil.getUser(request);

					if (null != user)
						logger.warn(
							"[showMccControlsPage] The cardId parameter is not provided or null for user  " +
								user.getUserId());
				}
			}
			catch (Exception e) {
				String message =
					"There has been an error getting the mcc controls for" +
						cardSearchForm.getMaskedPan() + ". Error: " +
							e.getMessage();

				logger.error("showMccControlsPage - " + message);
				request.setAttribute("mccControlsError", Boolean.TRUE);
			}
		}
		else {
			request.setAttribute("mccControlsError", Boolean.TRUE);
		}

		request.setAttribute("show8", Boolean.TRUE);

		logger.info("[showMccControlsPage] end");

		return Constants.CARD_DETAIL_PAGE;
	}

	@ActionMapping(
		params = Constants.CARD_ACTION + Constants.EQUALS + Constants.MCC_CONTROLS_HISTORY
	)
	public void showMcclHistory(
		@ModelAttribute CardSearchForm cardSearchForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.info("[showMcclHistory] start");

		String cardId = ParamUtil.getString(request, "cardId");
		final String cuscalToken = getCuscalTokenFromRequest(request);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);
		final String auditPageIndexStr = ParamUtil.getString(
			request, "mccHistoryPageIndex");
		cardId = escapeXml(cardId);
		showOptionalTabs(request);

		logger.info(
			"[showMcclHistory] cuscal token = " + cuscalToken +
				", page index = " + auditPageIndexStr);

		int auditPageIndex = 1;

		if (auditPageIndexStr != null) {
			try {
				auditPageIndex = Integer.valueOf(
					auditPageIndexStr
				).intValue();

				if (auditPageIndex < 1) {
					auditPageIndex = 1;
				}
			}
			catch (NumberFormatException e) {
			}
		}

		try {
			List<String> eventTypes = new ArrayList<>();

			eventTypes.add(AuditRestService.EVENT_TYPE_MCC_BLOCKING);

			List<AuditEvent> auditEvents = auditRestService.getAuditEvents(
				cuscalToken, auditPageIndex, AUDIT_HISTORY_PAGE_SIZE,
				eventTypes);

			final boolean auditPageMoreResults =
				(auditEvents != null) &&
				(auditEvents.size() == AUDIT_HISTORY_PAGE_SIZE);

			request.setAttribute(
				"mccHistoryEvents", mapAuditHistoryResults(auditEvents));
			request.setAttribute("mccHistoryMoreResults", auditPageMoreResults);
			request.setAttribute("mccHistoryPageIndex", auditPageIndex);
			request.setAttribute("toggleMccHistory", Boolean.TRUE);

			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.MCC_CONTROLS);
			response.setRenderParameter(Constants.CARD_ID, cardId);
		}
		catch (Exception e) {
			String message =
				"There has been an error getting mcc blocking audit history for " +
					cuscalToken + ". Error: " + e.getMessage();

			logger.error("showMcclHistory - " + message);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}

		sessionStatus.setComplete();

		logger.info("[showMcclHistory] end");
	}

	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.PURGE_POOLS
	)
	public String showPurgePools(
		RenderResponse response, RenderRequest request,
		@ModelAttribute CardSearchForm cardSearchForm) {

		logger.debug("showPurgePools - start");
		String returnPage = showCardSearchPage(
			response, request, cardSearchForm);
		cardsSearchService.purgePools();
		logger.info("showPurgePools - end");

		return returnPage;
	}

	/**
	 *
	 * @param response
	 * @param request
	 * @param cardSearchForm
	 * @return
	 */
	@RenderMapping(
		params = Constants.CARD_RENDER + Constants.EQUALS + Constants.TOKEN_STATUS
	)
	public String showTokenStatusPage(
		RenderResponse response, RenderRequest request,
		@ModelAttribute CardSearchForm cardSearchForm) {

		logger.info("[showCardWalletsPage] start");

		String tokenId = ParamUtil.getString(request, Constants.TOKEN_ID);
		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String cuscalToken = getCuscalTokenFromRequest(request);
		cardSearchForm.setCardId(cardId);
		cardSearchForm.setTokenId(tokenId);

		showOptionalTabs(request);

		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);

		logger.info("[activateToken] cardId - " + cardId);
		logger.info("[activateToken] cuscalToken - " + cuscalToken);
		logger.info("[activateToken] tokenId - " + tokenId);

		showMobileTabs(request);

		try {
			if (cardId != null) {
				SearchHeader searchHeader = getSearchHeaderData(request);
				CardInformation cardInformation = getCardInformationFromSwitch(
					response, request, cardId, cuscalToken);

				request.setAttribute("cardInfoData", cardInformation);
				cardSearchForm.setPanOrBin(cardInformation.getPan());

				List<TokenDetailType> tokens = cardsSearchService.searchTokens(
					cardSearchForm, searchHeader,
					PortletContext.newContext(response, request)
				).getTokens();

				TokenDetailType tokenDetail = null;

				for (TokenDetailType tokenDetailType : tokens) {
					if (tokenDetailType.getTokenInfo(
						).getTokenReference(
						).getTokenReferenceId(
						).toString(
						).equals(
							tokenId
						)) {

						tokenDetail = tokenDetailType;

						break;
					}
				}

				tokens.clear();
				tokens.add(tokenDetail);

				request.setAttribute("show6", Boolean.TRUE);
				request.setAttribute("tokens", decorateTokens(tokens));
			}
			else {
				User user = PortalUtil.getUser(request);

				if (null != user)
					logger.warn(
						"[showCardWalletsPage] The cardId parameter is not provided or null for user  " +
							user.getUserId());
			}
		}
		catch (Exception e) {
			logger.error(
				"[showCardWalletsPage] the exception came message is  " +
					e.getMessage(),
				e);
			request.setAttribute(Constants.CARD_ERROR_MSG, Boolean.TRUE);

			return Constants.CARD_ERROR_PAGE;
		}

		logger.info("[showCardWalletsPage] end");

		return Constants.CARD_DETAIL_PAGE;
	}

	/**
	 *
	 * @param cardSearchForm
	 * @param bindingResult
	 * @param response
	 * @param sessionStatus
	 * @param request
	 */
	@ActionMapping(
		params = Constants.CARD_ACTION + Constants.EQUALS + Constants.CARD_CONTROLS_UPDATE
	)
	public void updateCardControls(
		@ModelAttribute CardSearchForm cardSearchForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.info("[updateCardControls] start");

		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String cuscalToken = getCuscalTokenFromRequest(request);
		String controlVals = ParamUtil.getString(request, "controlVals");
		String existingControlVals = ParamUtil.getString(
			request, "existingControlVals");
		cardId = escapeXml(cardId);
		cardSearchForm.setCardId(cardId);
		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);

		logger.info("[updateCardControls] cardId - " + cardId);
		logger.info("[updateCardControls] cuscalToken - " + cuscalToken);

		try {
			CardInformation cardInformation = getCardInformationFromSwitch(
				response, request, cardId, cuscalToken);

			request.setAttribute("cardInfoData", cardInformation);
			cardSearchForm.setPanOrBin(cardInformation.getPan());

			String ipAddress = _getHttpServletRequest(
				request
			).getRemoteAddr();

			HashMap<String, Object> controlValMap = null;
			HashMap<String, Object> existingControlValMap = null;

			if (StringUtils.isBlank(controlVals)) {
				controlValMap = new HashMap<>();
			}
			else {
				controlValMap = new ObjectMapper().readValue(
					controlVals, HashMap.class);
			}

			if (StringUtils.isBlank(existingControlVals)) {
				existingControlValMap = new HashMap<>();
			}
			else {
				existingControlValMap = new ObjectMapper().readValue(
					existingControlVals, HashMap.class);
			}

			for (String key : existingControlValMap.keySet()) {
				if (controlValMap.containsKey(key)) {
					if (controlValMap.get(key) == null) {
						//controlValMap.remove(key);
						controlValMap.put(key, "0");
					}
				}
				else {
					controlValMap.put(key, existingControlValMap.get(key));
				}
			}

			JSONObject obj = new JSONObject();

			obj.put("cardControls", controlValMap);
			obj.put("cuscalToken", cuscalToken);
			UpdateCardControlsPortalResponse updateCardControlsPortalResponse =
				null;

			if (isDataFromAuthenticEarth(request)) {
				JSONArray controlValMapAuthenticEarth = new JSONArray();

				for (String key : controlValMap.keySet()) {
					JSONObject controlObj = new JSONObject();

					controlObj.put("id", key);
					controlObj.put("value", controlValMap.get(key));
					controlValMapAuthenticEarth.add(controlObj);
				}

				obj.put("cardControls", controlValMapAuthenticEarth);
				obj.put("channel", Constants.CHANNEL_PORTAL);
				String updateCardControlsSvcResponse =
					cardsRouteService.updateCardControls(
						cuscalToken, obj.toJSONString(),
						getSearchHeaderData(request));

				handleUpdateCardControlsResponseFromAuthenticEarth(
					updateCardControlsSvcResponse, cardSearchForm, response);
			}
			else {
				final User user = PortalUtil.getUser(request);
				updateCardControlsPortalResponse = updateCardControls(
					cardInformation.getPan(), obj.toJSONString(), ipAddress,
					user);
				handleUpdateCardControlsResponseFromAuthentic3(
					updateCardControlsPortalResponse, cardSearchForm, response);
			}

			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_CONTROLS);
			response.setRenderParameter(Constants.CARD_ID, cardId);
		}
		catch (Exception ex) {
			logger.error(
				"[updateCardControls] the exception came message is  " +
					ex.getMessage(),
				ex);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}

		sessionStatus.setComplete();
		logger.info("[updateCardControls] end");
	}

	/**
	 *
	 * @param cardSearchForm
	 * @param bindingResult
	 * @param response
	 * @param sessionStatus
	 * @param request
	 */
	@ActionMapping(
		params = Constants.CARD_ACTION + Constants.EQUALS + Constants.CARD_LIMITS_UPDATE
	)
	public void updateCardLimits(
		@ModelAttribute CardSearchForm cardSearchForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.info("[updateCardLimits] start");

		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String cuscalToken = getCuscalTokenFromRequest(request);
		String cardLimitId = ParamUtil.getString(request, "cardLimitId");
		String cardLimitsVals = ParamUtil.getString(request, "cardLimitsVals");
		cardId = escapeXml(cardId);
		cardSearchForm.setCardId(cardId);
		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);

		logger.info("[updateCardLimits] cardId - " + cardId);
		logger.info("[updateCardLimits] cuscalToken - " + cuscalToken);

		try {
			SearchHeader searchHeader = getSearchHeaderData(request);
			CardInformation cardInformation = getCardInformationFromSwitch(
				response, request, cardId, cuscalToken);

			request.setAttribute("cardInfoData", cardInformation);
			cardSearchForm.setPanOrBin(cardInformation.getPan());

			OrganisationAccess orgAccess = cardRestService.getOrgAccess(
				searchHeader);

			cardRestService.updateCardLimits(
				cardSearchForm, searchHeader,
				PortletContext.newContext(response, request), orgAccess,
				cuscalToken, cardLimitsVals, cardLimitId);

			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_LIMITS);
			response.setRenderParameter(Constants.CARD_ID, cardId);
		}
		catch (Exception ex) {
			logger.error(
				"[updateCardLimits] the exception message is  " +
					ex.getMessage(),
				ex);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}

		sessionStatus.setComplete();
		logger.info("[updateCardLimits] end");
	}

	/**
	 * Update/Create the Card Reference List for the session depending on if the
	 * card is already in the list or not.
	 *
	 * @param request
	 * @param response
	 */
	@ResourceMapping(Constants.UPDATE_CARD_REFERENCE_LIST)
	@SuppressWarnings("unchecked")
	public void updateCardReferenceList(
		ResourceRequest request, ResourceResponse response,
		SessionStatus sessionStatus) {

		HttpServletRequest httpServletRequest = _getHttpServletRequest(request);

		HttpSession session = httpServletRequest.getSession(true);

		String cardId = ParamUtil.getString(request, "cardId");

		ArrayList<SimpleCard> cardReferenceList =
			(ArrayList<SimpleCard>)session.getAttribute(
				Constants.CARD_REFERENCE_LIST);

		SearchHeader searchHeader = getSearchHeaderData(request);

		String pan = panFromCardId(searchHeader, cardId);

		if (cardReferenceList == null) {
			cardReferenceList = new ArrayList<>();
			session.setAttribute(
				Constants.CARD_REFERENCE_LIST, cardReferenceList);
		}

		PrintWriter out;

		try {
			out = ((MimeResponse)response).getWriter();
			SimpleCard card = new SimpleCard(cardId, pan);

			if (cardReferenceList.contains(card)) {
				cardReferenceList.remove(card);
				out.print("removeCard");
			}
			else {
				cardReferenceList.add(card);
				out.print("addCard");
			}
		}
		catch (Exception e) {
			logger.error(
				"Failed to update card reference list: " + e.getMessage());
		}

		//		logger.debug("[updateCardReferenceList] Cards comparing are: " + card.compareTo(card));

		for (int i = 0; i < cardReferenceList.size(); i++) {
			logger.debug(
				"[updateCardReferenceList] Card " + i +
					" in the reference list is: " +
						cardReferenceList.get(
							i
						).getCardId());
		}

		sessionStatus.setComplete();
	}

	/**
	 * Shows the search results of the Cards
	 *
	 * @param response
	 * @param request
	 * @return string
	 */
	@ActionMapping(
		params = Constants.CARD_ACTION + Constants.EQUALS + Constants.STATUS_UPDATE
	)
	public void updateCardStatus(
		ActionResponse response, ActionRequest request,
		@ModelAttribute CardSearchForm cardSearchForm) {

		logger.debug("In update Card Status action mapping ");

		try {
			String paramCardId = ParamUtil.getString(
				request, Constants.CARD_ID);
			String paramCardNewStatus = ParamUtil.getString(
				request, "cardNewStatus");

			String cuscalToken = getCuscalTokenFromRequest(request);

			if ((isNotBlank(paramCardId) || isNotBlank(cuscalToken)) &&
				isNotBlank(paramCardNewStatus)) {

				SearchHeader searchHeader = getSearchHeaderData(request);
				BigInteger cardId = null;

				if (isNotBlank(paramCardId)) {
					cardId = new BigInteger(paramCardId);
				}

				request.setAttribute(Constants.CARD_ID, cardId);
				request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);
				String cardStatus = paramCardNewStatus.toString();

				logger.info(
					"In update Card Status the status need to change for card Id: " +
						cardId + " and card new status is " + cardStatus);

				String updateReferenceNumber = null;

				if (isDataFromAuthenticEarth(request)) {
					logger.info(
						"[updateCardStatus] Going to call authentic Earth");
					updateReferenceNumber = cardsRouteService.updateCardStatus(
						cuscalToken, cardStatus, searchHeader);
				}
				else {
					logger.info("[updateCardStatus] Going to call authentic");
					updateReferenceNumber =
						cardsSearchService.updateCardStatusCode(
							cardId, cardStatus, searchHeader,
							PortletContext.newContext(response, request));
				}

				logger.info(
					"[updateCardStatus] Card Update Status reference number is: <" +
						updateReferenceNumber + ">");

				String paramCardInfoTab = ParamUtil.getString(
					request, Constants.CARD_INFO_TAB);

				if (!paramCardInfoTab.isEmpty())
					request.setAttribute(
						Constants.CARD_INFO_TAB, paramCardInfoTab);

				if (isNotBlank(updateReferenceNumber)) {
					logger.debug(
						"[updateCardStatus] After the audit success message Reference number is: %" +
							updateReferenceNumber + "%");

					request.setAttribute(
						Constants.IS_CARD_STATUS_UPADTE, updateReferenceNumber);
				}
				else {
					request.setAttribute(
						Constants.IS_CARD_STATUS_UPADTE, "failed");
				}

				response.getRenderParameters(
				).setValue(
					Constants.CARD_RENDER, Constants.CARD_DETAIL
				);
			}
			else {
				logger.debug(
					"In update Card Status the status need to change and card / card status is null");
			}
		}
		catch (WebServiceFailureError fe) {
			logger.error(
				"In update Card Status action mapping   WebServiceFailureError CAME ERROR MSG e.getCause()  is " +
					fe.getCause(),
				fe);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_UPDATE_ERROR);
		}
		catch (WebServiceException we) {
			logger.error(
				"In update Card Status action mapping   WebServiceFailureError CAME ERROR MSG e.getCause()  is " +
					we.getCause(),
				we);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}
		catch (Exception e) {
			logger.error(
				"In update Card Status action mapping   EXCEPTION CAME ERROR MSG e.getCause()  is " +
					e.getCause(),
				e);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}
	}

	/**
	 *
	 * @param cardSearchForm
	 * @param bindingResult
	 * @param response
	 * @param sessionStatus
	 * @param request
	 */
	@ActionMapping(
		params = Constants.CARD_ACTION + Constants.EQUALS + Constants.MCC_CONTROLS_UPDATE
	)
	public void updateMccControls(
		@ModelAttribute CardSearchForm cardSearchForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.info("[updateMccControls] start");

		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String cuscalToken = getCuscalTokenFromRequest(request);
		String mccControlVals = ParamUtil.getString(request, "mccControlVals");
		cardId = escapeXml(cardId);
		cardSearchForm.setCardId(cardId);
		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);

		logger.info("[updateMccControls] cardId - " + cardId);
		logger.info("[updateMccControls] cuscalToken - " + cuscalToken);

		try {
			SearchHeader searchHeader = getSearchHeaderData(request);
			CardInformation cardInformation = getCardInformationFromSwitch(
				response, request, cardId, cuscalToken);

			request.setAttribute("cardInfoData", cardInformation);
			cardSearchForm.setPanOrBin(cardInformation.getPan());

			OrganisationAccess orgAccess = cardRestService.getOrgAccess(
				searchHeader);

			cardRestService.updateMccControls(
				orgAccess, cuscalToken, mccControlVals);

			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.MCC_CONTROLS);
			response.setRenderParameter(Constants.CARD_ID, cardId);
		}
		catch (Exception ex) {
			logger.error(
				"[updateMccControls] the exception came message is  " +
					ex.getMessage(),
				ex);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}

		sessionStatus.setComplete();
		logger.info("[updateMccControls] end");
	}

	/**
	 *
	 * @param cardSearchForm
	 * @param bindingResult
	 * @param response
	 * @param sessionStatus
	 * @param request
	 */
	@ActionMapping(
		params = Constants.CARD_ACTION + Constants.EQUALS + Constants.TOKEN_UPDATE
	)
	public void updateToken(
		@ModelAttribute CardSearchForm cardSearchForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.info("[updateToken] start");

		String cardId = ParamUtil.getString(request, Constants.CARD_ID);
		String tokenId = ParamUtil.getString(request, Constants.TOKEN_ID);
		String action = ParamUtil.getString(request, Constants.TOKEN_ACTION);
		String cuscalToken = getCuscalTokenFromRequest(request);
		cardId = escapeXml(cardId);
		tokenId = escapeXml(tokenId);
		cardSearchForm.setCardId(cardId);
		cardSearchForm.setTokenId(tokenId);

		request.setAttribute(Constants.CARD_ID, cardId);
		request.setAttribute(Constants.CUSCAL_TOKEN, cuscalToken);

		logger.info("[updateToken] cardId - " + cardId);
		logger.info("[updateToken] cuscalToken - " + cuscalToken);
		logger.info("[updateToken] tokenId - " + tokenId);

		try {
			SearchHeader searchHeader = getSearchHeaderData(request);
			CardInformation cardInformation = getCardInformationFromSwitch(
				response, request, cardId, cuscalToken);

			request.setAttribute("cardInfoData", cardInformation);
			cardSearchForm.setPanOrBin(cardInformation.getPan());

			TokenLifecycleAction tokenLifecycleAction = null;
			String comment = null;
			boolean isValid = true;

			if (action != null) {
				tokenLifecycleAction = TokenLifecycleAction.valueOf(
					action.toUpperCase());

				if (tokenLifecycleAction.equals(TokenLifecycleAction.DELETE)) {
					comment = ParamUtil.getString(
						request, Constants.DELETE_COMMENT);

					if (StringUtils.isBlank(comment)) {
						request.setAttribute(
							"deleteCommentError", Boolean.TRUE);
						isValid = false;
					}
				}
				else if (tokenLifecycleAction.equals(
							TokenLifecycleAction.SUSPEND)) {

					comment = ParamUtil.getString(
						request, Constants.SUSPEND_COMMENT);

					if (StringUtils.isBlank(comment)) {
						request.setAttribute(
							"suspendCommentError", Boolean.TRUE);
						isValid = false;
					}
				}
				else if (tokenLifecycleAction.equals(
							TokenLifecycleAction.UNSUSPEND)) {

					comment = ParamUtil.getString(
						request, Constants.UNSUSPEND_COMMENT);

					if (StringUtils.isBlank(comment)) {
						request.setAttribute(
							"unsuspendCommentError", Boolean.TRUE);
						isValid = false;
					}
				}
			}

			if (!isValid) {
				response.setRenderParameter(
					Constants.CARD_RENDER, Constants.TOKEN_STATUS);
				response.setRenderParameter(Constants.CARD_ID, cardId);
				response.setRenderParameter(Constants.TOKEN_ID, tokenId);
			}
			else {
				boolean success = cardsSearchService.updateToken(
					cardSearchForm, searchHeader,
					PortletContext.newContext(response, request));

				if (!success) {
					throw new Exception("[updateToken] fail");
				}

				response.setRenderParameter(
					Constants.CARD_RENDER, Constants.TOKEN_WALLETS);
				response.setRenderParameter(Constants.CARD_ID, cardId);
			}
		}
		catch (Exception ex) {
			logger.error(
				"[updateToken] the exception came message is  " +
					ex.getMessage(),
				ex);
			response.setRenderParameter(
				Constants.CARD_RENDER, Constants.CARD_ERROR);
		}

		sessionStatus.setComplete();
		logger.info("[updateToken] end");
	}

	private HttpServletRequest _getHttpServletRequest(
		PortletRequest portletRequest) {

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		return httpServletRequest;
	}

	private void _overrideAttributes(
			RenderRequest request, CardSearchForm cardSearchForm)
		throws Exception {

		String[] singleValueParams = {
			"panOrBin", "issuer", "issuerShortName", "cardholderName",
			"postCode", "nonExpired", "expiryMonth", "expiryYear"
		};
		String[] arrayValueParams = {"cardStatus"};

		_overrideAttributesIfExist(
			request, singleValueParams, arrayValueParams, cardSearchForm);
	}

	/**
	 * Read param from PortletRequest, if not null then use BeanUtils to set value to the Object.
	 * All values is implicitly known as String or String[]
	 *
	 * @param portletRequest
	 * @param singleValueParams
	 * @param arrayValueParams
	 * @param obj
	 * @throws Exception
	 */
	private void _overrideAttributesIfExist(
			PortletRequest portletRequest, String[] singleValueParams,
			String[] arrayValueParams, Object obj)
		throws Exception {

		for (String param : singleValueParams) {
			String value = ParamUtil.getString(portletRequest, param, null);

			if (value != null) {
				BeanUtils.setProperty(obj, param, value);
			}
		}

		for (String param : arrayValueParams) {
			String[] value = ParamUtil.getStringValues(
				portletRequest, param, null);

			if (value != null) {
				BeanUtils.setProperty(obj, param, value);
			}
		}
	}

	private void _setRenderParams(
			CardSearchForm cardSearchForm, ActionResponse response)
		throws Exception {

		String[] singleAttributes = {
			"panOrBin", "issuer", "issuerShortName", "cardholderName",
			"postCode", "expiryMonth", "expiryYear"
		};
		String[] arrayAttributes = {"cardStatus"};

		Object obj = cardSearchForm;

		_setRenderParamsIfExists(
			response, singleAttributes, arrayAttributes, obj);

		response.setRenderParameter(
			"nonExpired", String.valueOf(cardSearchForm.isNonExpired()));
	}

	private void _setRenderParamsIfExists(
			ActionResponse response, String[] singleAttributes,
			String[] arrayAttributes, Object obj)
		throws Exception {

		for (String attr : singleAttributes) {
			String value = BeanUtils.getProperty(obj, attr);

			if (value != null) {
				response.setRenderParameter(attr, value);
			}
		}

		for (String attr : arrayAttributes) {
			String[] value = BeanUtils.getArrayProperty(obj, attr);

			if (value != null) {
				response.setRenderParameter(attr, value);
			}
		}
	}

	private List<DecoratedDevice> decorate(List<MobileDevice> _devices) {
		List<DecoratedDevice> devices = new ArrayList<>();

		if (_devices == null)

			return devices;	// return an empty list (as opposed to null)

		for (MobileDevice device : _devices) {
			DecoratedDevice decoratedDevice =
				DecoratedDevice.newDecoratedDevice(device);

			devices.add(decoratedDevice);
		}

		return devices;
	}

	private List<DecoratedToken> decorateTokens(List<TokenDetailType> _tokens) {
		List<DecoratedToken> tokens = new ArrayList<>();

		if (_tokens == null)

			return tokens;	// return an empty list (as opposed to null)

		Iterator<TokenDetailType> i = _tokens.iterator();

		while (i.hasNext()) {
			TokenDetailType token = i.next();

			DecoratedToken decoratedToken = DecoratedToken.newDecoratedToken(
				token);

			tokens.add(decoratedToken);
		}

		Collections.sort(
			tokens,
			new Comparator<DecoratedToken>() {

				public int compare(DecoratedToken o1, DecoratedToken o2) {
					return o2.getLastUpdatedTime(
					).compareTo(
						o1.getLastUpdatedTime()
					);
				}

			});

		return tokens;
	}

	private GetCardControlsPortalResponse getCardControls(
		String ipAddress, User user, String pan) {

		final GetCardControls getCardControlsRequest = new GetCardControls();
		getCardControlsRequest.setHeader(
			getCuscalAPIRequestHeader(ipAddress, user));
		getCardControlsRequest.setPan(pan);

		return cuscalApiService.getCardControls(getCardControlsRequest);
	}

	private CardInformation getCardInformationFromSwitch(
			Object responseObject, Object requestObject, String cardId,
			String cuscalToken)
		throws Exception {

		SearchHeader searchHeader = new SearchHeader();
		List<String> groups = new ArrayList<>();
		boolean isDataFromAuthenticEarth = false;
		PortletContext portletContext = null;
		CardInformation cardInformation = null;

		if ((requestObject instanceof RenderRequest) &&
			(responseObject instanceof RenderResponse)) {

			RenderRequest request = (RenderRequest)requestObject;
			RenderResponse response = (RenderResponse)responseObject;
			searchHeader = getSearchHeaderData(request);
			groups = Utility.allUserGroupRoles(request);
			isDataFromAuthenticEarth = isDataFromAuthenticEarth(request);
			portletContext = PortletContext.newContext(response, request);
		}
		else if ((requestObject instanceof ActionRequest) &&
				 (responseObject instanceof ActionResponse)) {

			ActionRequest request = (ActionRequest)requestObject;
			ActionResponse response = (ActionResponse)responseObject;
			searchHeader = getSearchHeaderData(request);
			groups = Utility.allUserGroupRoles(request);
			isDataFromAuthenticEarth = isDataFromAuthenticEarth(request);
			portletContext = PortletContext.newContext(response, request);
		}

		if (isDataFromAuthenticEarth) {
			logger.info("Calling Authentic Earth for card information");
			cardInformation = cardsRouteService.getCardInformation(
				cuscalToken, searchHeader, groups, portletContext);
		}
		else {
			logger.info("Calling Authentic 3 for card information");
			cardInformation =
				cardsSearchService.getCardInformationOnCardDetails(
					cardId, searchHeader, groups, portletContext);
		}

		return cardInformation;
	}

	private PortalRequestHeader getCuscalAPIRequestHeader(
		String ipAddress, User user) {

		PortalRequestHeader header = new PortalRequestHeader();

		header.setClientId("AUTHENTIC");
		header.setAppKey("PORTAL");

		if (StringUtils.isBlank(ipAddress)) {
			header.setIpAddress("127.0.0.1");
		}
		else {
			header.setIpAddress(ipAddress);
		}

		if (user != null) {
			header.setOperator(user.getScreenName());
		}

		return header;
	}

	private String getCuscalTokenFromRequest(Object requestObject) {
		if (requestObject instanceof PortletRequest) {
			PortletRequest request = (PortletRequest)requestObject;

			String paramCuscalToken = ParamUtil.getString(
				request, Constants.CUSCAL_TOKEN);

			if (!paramCuscalToken.isEmpty()) {
				return paramCuscalToken;
			}
			else if (null != request.getAttribute(Constants.CUSCAL_TOKEN)) {
				return request.getAttribute(
					Constants.CUSCAL_TOKEN
				).toString();
			}
		}

		return null;
	}

	private void handleUpdateCardControlsResponseFromAuthentic3(
		UpdateCardControlsPortalResponse updateCardControlsPortalResponse,
		CardSearchForm cardSearchForm, ActionResponse response) {

		if (updateCardControlsPortalResponse != null) {
			if (updateCardControlsPortalResponse.getHeader(
				).getResponseCode() != 0) {

				String message =
					"There has been an error updating the card controls for " +
						cardSearchForm.getMaskedPan() + ". Error: " +
							updateCardControlsPortalResponse.getHeader(
							).getMessage();

				logger.error("[updateCardControls] - " + message);
				response.setRenderParameter(
					"cardControlsError", Boolean.TRUE.toString());
			}
		}
		else {
			response.setRenderParameter(
				"cardControlsError", Boolean.TRUE.toString());
		}
	}

	private void handleUpdateCardControlsResponseFromAuthenticEarth(
		String updateCardControlsSvcResponse, CardSearchForm cardSearchForm,
		ActionResponse response) {

		try {
			logger.info("START - updateCardControls handle Aux Earth response");
			JsonObject svcResponse = new Gson().fromJson(
				updateCardControlsSvcResponse, JsonObject.class);

			JsonObject metaData = svcResponse.getAsJsonObject(Constants.META);
			JsonPrimitive errorCode = svcResponse.getAsJsonObject(
			).getAsJsonPrimitive(
				"errorCode"
			);
			JsonPrimitive errorMessage = svcResponse.getAsJsonObject(
			).getAsJsonPrimitive(
				"message"
			);

			if (errorCode != null) {
				String message =
					"There has been an error updating the card controls for " +
						cardSearchForm.getMaskedPan() + ". Error: " +
							errorMessage.getAsString();

				logger.error("[updateCardControls] - " + message);
				response.setRenderParameter(
					"cardControlsError", Boolean.TRUE.toString());
			}
			else {
				String code = getAsString(metaData, "code");

				if (isNotBlank(code)) {
					if (!Constants.SUCCESS.equalsIgnoreCase(code)) {
						response.setRenderParameter(
							"cardControlsError", Boolean.TRUE.toString());
					}
				}
			}
		}
		catch (Exception ex) {
			logger.error(
				"[updateCardControls] the updateCardControls  EXCEPTION CAME ERROR MSG e.getCause()  is " +
					ex.getCause(),
				ex);
			response.setRenderParameter(
				"cardControlsError", Boolean.TRUE.toString());
		}
	}

	private boolean isDataFromAuthenticEarth(Object requestObject) {
		HttpServletRequest httpServletRequest = null;

		if (requestObject instanceof RenderRequest) {
			RenderRequest request = (RenderRequest)requestObject;

			httpServletRequest = _getHttpServletRequest(request);
		}
		else if (requestObject instanceof ActionRequest) {
			ActionRequest request = (ActionRequest)requestObject;

			httpServletRequest = _getHttpServletRequest(request);
		}

		HttpSession session = httpServletRequest.getSession();

		if (null != session.getAttribute(
				Constants.IS_DATA_FROM_AUTHENTIC_EARTH)) {

			return Boolean.valueOf(
				session.getAttribute(
					Constants.IS_DATA_FROM_AUTHENTIC_EARTH
				).toString());
		}

		return false;
	}

	private boolean isShowCardLimitsTab(PortletRequest request) {
		final boolean showCardLimits = cardsSearchService.isShowCardLimits();

		if (!showCardLimits) {
			return false;
		}

		final SearchHeader searchHeader = getSearchHeaderData(request);

		String[] cardLimitsOrgs = cardsSearchService.getShowCardLimitsOrgs();

		if ((cardLimitsOrgs != null) && (cardLimitsOrgs.length > 0)) {
			boolean orgFound = false;

			if (isNotBlank(searchHeader.getUserOrgId())) {
				for (String org : cardLimitsOrgs) {
					if (org.equalsIgnoreCase(searchHeader.getUserOrgId())) {
						orgFound = true;
					}
				}
			}

			if (!orgFound) {
				return false;

				// request.setAttribute("showCardLimits", Boolean.FALSE);

			}
		}

		return true;
	}

	private List<AuditEventSearchResult> mapAuditHistoryResults(
		List<AuditEvent> auditEvents) {

		List<AuditEventSearchResult> searchResults = new ArrayList<>();

		if ((auditEvents == null) || (auditEvents.size() == 0)) {
			return searchResults;
		}

		logger.info("Audit events = " + auditEvents);

		for (AuditEvent auditEvent : auditEvents) {
			List<String> eventDetailsList = new ArrayList<>();

			if (auditEvent.getAttributes() != null) {
				for (String key :
						auditEvent.getAttributes(
						).keySet()) {

					if (key.startsWith("delta")) {
						final String jsonData = auditEvent.getAttributes(
						).get(
							key
						);

						if (!((jsonData == null) ||
							  jsonData.trim(
							  ).equals(
								  ""
							  ))) {

							JSONArray jsonArray;

							try {
								jsonArray = (JSONArray)new JSONParser().parse(
									jsonData);

								if ((jsonArray == null) ||
									(jsonArray.size() == 0)) {

									eventDetailsList.add("No changes");

									break;
								}

								for (int i = 0; i < jsonArray.size(); i++) {
									JSONObject jsonObj =
										(JSONObject)jsonArray.get(i);

									eventDetailsList.add(
										jsonObj.get(
											"value"
										).toString() + " - " +
											jsonObj.get(
												"name"
											).toString());
								}
							}
							catch (ParseException e) {
								logger.warn(e.getMessage());
							}
						}
					}
				}
			}
			else {
				eventDetailsList.add("N/A");
			}

			for (String eventDetails : eventDetailsList) {
				AuditEventSearchResult searchResult =
					new AuditEventSearchResult();

				searchResult.setDatetime(auditEvent.getEventTimestamp());
				searchResult.setEventDetails(eventDetails);
				searchResult.setAction(auditEvent.getEventAction());
				searchResult.setChannel(auditEvent.getChannelType());
				searchResult.setUser(auditEvent.getUserId());
				searchResults.add(searchResult);
			}
		}

		return searchResults;
	}

	private List<LimitsAuditEventSearchResult> mapLimitsAuditHistoryResults(
		List<AuditEvent> auditEvents) {

		List<LimitsAuditEventSearchResult> searchResults = new ArrayList<>();

		if ((auditEvents == null) || (auditEvents.size() == 0)) {
			return searchResults;
		}

		for (AuditEvent auditEvent : auditEvents) {
			if (auditEvent.getAttributes() != null) {
				LimitsAuditEventSearchResult searchResult =
					new LimitsAuditEventSearchResult();

				searchResult.setDatetime(auditEvent.getEventTimestamp());
				searchResult.setAction(auditEvent.getEventAction());
				searchResult.setChannel(auditEvent.getChannelType());
				searchResult.setUser(auditEvent.getUserId());

				for (String key :
						auditEvent.getAttributes(
						).keySet()) {

					if (key.equalsIgnoreCase("oldLimitValue")) {
						final String oldLimitValue = auditEvent.getAttributes(
						).get(
							key
						);
						searchResult.setOldLimit(
							oldLimitValue == null ? "N/A" : oldLimitValue);
					}
					else if (key.equalsIgnoreCase("cardLimit")) {
						final String jsonData = auditEvent.getAttributes(
						).get(
							key
						);

						if (!((jsonData == null) ||
							  jsonData.trim(
							  ).equals(
								  ""
							  ))) {

							JSONParser jsonParser = new JSONParser();

							try {
								JSONObject jsonObj =
									(JSONObject)jsonParser.parse(jsonData);

								if (jsonObj.get("id") != null) {
									final String limitId = jsonObj.get(
										"id"
									).toString();

									if (limitId.equals("1")) {
										searchResult.setEventDetails(
											"Per Transaction");
									}
									else if (limitId.equals("2")) {
										searchResult.setEventDetails("Daily");
									}
									else if (limitId.equals("3")) {
										searchResult.setEventDetails("Weekly");
									}
								}

								if (jsonObj.get("limit") != null) {
									searchResult.setNewLimit(
										jsonObj.get(
											"limit"
										).toString());
								}

								if (jsonObj.get("status") != null) {
									final String status = jsonObj.get(
										"status"
									).toString();

									if (status.equalsIgnoreCase("on")) {
										searchResult.setAction("Set");
									}
									else if (status.equalsIgnoreCase("off")) {
										searchResult.setAction("Removed");
										searchResult.setNewLimit("N/A");
									}
									else if (status.equalsIgnoreCase(
												"suspended") &&
											 (jsonObj.get("resumeDate") !=
												 null)) {

										searchResult.setAction(
											"Suspended until " +
												jsonObj.get(
													"resumeDate"
												).toString());
									}
								}
							}
							catch (ParseException e) {
								logger.warn(e.getMessage());
							}
						}
					}
				}

				searchResults.add(searchResult);
			}
		}

		return searchResults;
	}

	/**
	 *
	 * @param searchHeader
	 * @param cardId
	 * @return pan <em>String</em>
	 */
	private String panFromCardId(SearchHeader searchHeader, String cardId) {
		GetCardRequestType cardRequestType = new GetCardRequestType();

		cardRequestType.setCardId(new BigInteger(cardId));
		cardRequestType.setHeader(searchHeader);
		GetCardResponseType cardResponseType =
			cardsSearchService.getCardService(
			).getCard(
				cardRequestType
			);

		return cardResponseType.getPan();
	}

	private void reloadDeviceList(
		RenderRequest request, String cardId, String pan,
		final String ipAddress, User user) {

		final GetDeviceListPortal getDeviceListRequest =
			new GetDeviceListPortal();
		getDeviceListRequest.setHeader(
			getCuscalAPIRequestHeader(ipAddress, user));
		getDeviceListRequest.setPan(pan);

		final GetDeviceListPortalResponse getDeviceListResponse =
			cuscalApiService.getDeviceListPortal(getDeviceListRequest);

		if (getDeviceListResponse.getHeader(
			).getResponseCode() == 0) {

			List<MobileDevice> deviceList = null;

			if (getDeviceListResponse.getDevices() != null) {
				deviceList = getDeviceListResponse.getDevices(
				).getDevice();
			}

			logger.debug(
				"[showCardDevicesPage] card [" + cardId + "] has " +
					(deviceList == null ? 0 : deviceList.size()) +
						" devices attached to it");
			request.setAttribute("devices", decorate(deviceList));
			request.setAttribute("show4", Boolean.TRUE);
		}
		else {
			logger.error(
				"[deregisterDevicePage] the getDeviceListPortal call following deActivateDevicePortal returned an error response code of " +
					getDeviceListResponse.getHeader(
					).getResponseCode());
		}
	}

	private void showMobileTabs(PortletRequest request) {
		request.setAttribute(
			SHOW_MOBILE_DEVICES,
			Utility.hasRole(request, mobileDeviceRole) &&
			cardsSearchService.isShowMobileDevices());
	}

	private void showOptionalTabs(PortletRequest request) {
		request.setAttribute(
			"showCardControlHistoryUI",
			cardsSearchService.isShowCardControlHistory());
		request.setAttribute("showCardLimits", isShowCardLimitsTab(request));
		request.setAttribute(
			"showCardLimitsHistoryUI",
			cardsSearchService.isShowCardLimitsHistory());
		request.setAttribute("showLimits", cardsSearchService.isShowLimits());
		request.setAttribute(
			"showMccControlHistoryUI",
			cardsSearchService.isShowMccControlHistory());
		request.setAttribute(
			"showMccControls", cardsSearchService.isShowMccControls());
	}

	private UpdateCardControlsPortalResponse updateCardControls(
			String pan, String cardControls, String ipAddress, User user)
		throws Exception {

		final UpdateCardControlsPortal request = new UpdateCardControlsPortal();
		request.setHeader(getCuscalAPIRequestHeader(ipAddress, user));
		request.setPan(pan);
		request.setCardControls(cardControls);

		return cuscalApiService.updateCardControlsPortal(request);
	}

	private static final int AUDIT_HISTORY_PAGE_SIZE = 5;

	/**
	 * Logger object
	 */
	private static Log logger = LogFactoryUtil.getLog(
		CardsSearchController.class);

	@Autowired
	@Qualifier(Constants.AUDIT_REST_SERVICE)
	private AuditRestService auditRestService;

	@Autowired
	@Qualifier(Constants.CARD_REST_SERVICE)
	private CardRestService cardRestService;

	/**
	 * Card Route Service
	 */
	@Autowired
	@Qualifier(Constants.CARD_ROUTE_SERVICE)
	private CardsRouteService cardsRouteService;

	/**
	 * Service used to fetch data
	 */
	@Autowired
	@Qualifier(Constants.CARD_SEARCH_SERVICE)
	private CardsSearchService cardsSearchService;

	/**
	 * Card Validator
	 */
	@Autowired
	@Qualifier(Constants.CARD_FORM_VALIDATOR)
	private CardsSearchValidator cardsSearchValidator;

	@Autowired
	@Qualifier(Constants.CARD_CUSCALAPI_SERVICE)
	private PortalServices cuscalApiService;

	@Autowired
	@Qualifier(Constants.CUSCAL_TOKEN_WEBSERVICE)
	private CuscalTokenWebService cuscalTokenWebService;

	private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
	private String mobileDeviceRole = "mobileDevice";
	private String mobileTabBlurb = null;
	private String organisationIdOverride = null;
	private String organisationShortNameOverride = null;
	private String tokenLastUpdatedByBlurb = null;

}