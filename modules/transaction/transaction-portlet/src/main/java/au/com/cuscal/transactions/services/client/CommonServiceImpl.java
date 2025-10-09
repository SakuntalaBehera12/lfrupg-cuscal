package au.com.cuscal.transactions.services.client;

import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.framework.webservices.client.CardService;
import au.com.cuscal.framework.webservices.client.impl.AbstractCuscalWebServices;
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
import au.com.cuscal.framework.webservices.transaction.UpdateCardStatusRequestType;
import au.com.cuscal.framework.webservices.transaction.UpdateCardStatusResponseType;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;

@Component("commonService")
public class CommonServiceImpl extends AbstractCuscalWebServices
	implements CommonService {

	@Override
	public FindCardsResponseType findCards(FindCardsRequestType findCardsRequestType) {
		return cardService.findCards(findCardsRequestType);
	}

	@Override
	public GetCardResponseType getCard(GetCardRequestType getCardRequestType) {
		return cardService.getCard(getCardRequestType);
	}

	@Override
	public GetCardLimitsResponseType getCardLimits(
		GetCardLimitsRequestType getCardLimitsRequestType) {

		return cardService.getCardLimits(getCardLimitsRequestType);
	}

	@Override
	public GetCardAccountsResponseType getCardAccounts(
		GetCardAccountsRequestType getCardAccountsRequestType) {

		return cardService.getCardAccounts(getCardAccountsRequestType);
	}

	@Override
	public GetCustomerAccessViewResponseType getCustomerAccessView(
		GetCustomerAccessViewRequestType params) {

		logger.debug("addServiceRequest - start");

		GetCustomerAccessViewResponseType response = null;

		if (null != params) {
			response = cardService.getCustomerAccessView(params);
		}

		logger.debug("addServiceRequest - end");

		return response;
	}

	@Override
	public UpdateCardStatusResponseType updateCardStatus(
		UpdateCardStatusRequestType updateCardStatusRequestType) {

		return cardService.updateCardStatus(updateCardStatusRequestType);
	}

	@Override
	public GetCardVigilBlockedResponseType getCardVigilBlocked(
		GetCardVigilBlockedRequestType getCardVigilBlockedRequestType) {

		return cardService.getCardVigilBlocked(getCardVigilBlockedRequestType);
	}

	@Override
	public FindEntitiesByAttributeWithIdResponseType findEntitiesByAttributeWithId(
		FindEntitiesByAttributeWithIdRequestType findEntitiesByAttributeWithIdRequestType) {

		return cardService.findEntitiesByAttributeWithId(findEntitiesByAttributeWithIdRequestType);
	}

	@Override
	public IsInPrimarySwitchModeResponseType isInPrimarySwitchMode(
		IsInPrimarySwitchModeRequestType isInPrimarySwitchModeRequestType) {

		return cardService.isInPrimarySwitchMode(isInPrimarySwitchModeRequestType);
	}

	@Override
	public GetCardChannelPermissionsResponseType getCardChannelPermissions(
		GetCardChannelPermissionsRequestType getCardChannelPermissionsRequestType) {

		return cardService.getCardChannelPermissions(getCardChannelPermissionsRequestType);
	}

	/**
	 * Logger object
	 */
	private static final Logger logger = Logger.getLogger(CommonServiceImpl.class);

	private final CardService cardService = CuscalServiceLocator.getService(
		CardService.class.getName());

}