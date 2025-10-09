package au.com.cuscal.cards.services.client;

import au.com.cuscal.framework.webservices.client.impl.AbstractCuscalWebServices;
import au.com.cuscal.framework.webservices.transaction.CardServicePortType;
import au.com.cuscal.framework.webservices.transaction.CommonServicePortType;
import au.com.cuscal.framework.webservices.transaction.EntityServicePortType;
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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "cardService")
public class LocalCardServiceImpl extends AbstractCuscalWebServices {

	public FindCardsResponseType findCards(FindCardsRequestType request) {
        try {
			_log.debug("findCards - start");

			FindCardsResponseType response = null;

			this.initWebServicePools();

			CardServicePortType webservice = null;

			webservice = (CardServicePortType)this.webServicePool.borrow("WS_CARDS");

			if (webservice != null) {
				response = webservice.findCards(request);
				this.webServicePool.release(webservice);
			}

			_log.debug("findCards - end");

			return response;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public GetCardResponseType getCard(GetCardRequestType request) {
		try {
			_log.debug("getCard - start");

			GetCardResponseType response = null;

			this.initWebServicePools();

			CardServicePortType webservice = (CardServicePortType)this.webServicePool.borrow("WS_CARDS");

			if (webservice != null) {
				response = webservice.getCard(request);
				this.webServicePool.release(webservice);
			}

			_log.debug("getCard - end");

			return response;
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public GetCardLimitsResponseType getCardLimits(GetCardLimitsRequestType request) {
		try {
			_log.debug("getCardLimits - start");

			GetCardLimitsResponseType response;

			response = null;

			this.initWebServicePools();

			CardServicePortType webservice = (CardServicePortType)this.webServicePool.borrow("WS_CARDS");

			if (webservice != null) {
				response = webservice.getCardLimits(request);
				this.webServicePool.release(webservice);
			}

			_log.debug("getCardLimits - end");

			return response;
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public GetCardAccountsResponseType getCardAccounts(
		GetCardAccountsRequestType request) {

		try {
			_log.debug("getCardAccounts - start");

			GetCardAccountsResponseType response = null;

			this.initWebServicePools();

			CardServicePortType webservice = (CardServicePortType)
				this.webServicePool.borrow("WS_CARDS");

			if (webservice != null) {
				response = webservice.getCardAccounts(request);
				this.webServicePool.release(webservice);
			}

			_log.debug("getCardAccounts - end");

			return response;
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}

	}

	public GetCustomerAccessViewResponseType getCustomerAccessView(
		GetCustomerAccessViewRequestType request) {

		try {
			_log.debug("getCustomerAccessView - start");

			GetCustomerAccessViewResponseType response = null;

			this.initWebServicePools();

			CommonServicePortType webservice = (CommonServicePortType)
				this.webServicePool.borrow("WS_COMMON");

			if (webservice != null) {
				response = webservice.getCustomerAccessView(request);
				this.webServicePool.release(webservice);
			}

			_log.debug("getCustomerAccessView - end");

			return response;
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}

	}

	public GetCardVigilBlockedResponseType getCardVigilBlocked(GetCardVigilBlockedRequestType request) {
		_log.debug("getCardVigilBlocked - start");

		GetCardVigilBlockedResponseType response = null;
		CardServicePortType webservice = null;

		try {
			this.initWebServicePools();


			webservice = (CardServicePortType)
				this.webServicePool.borrow("WS_CARDS");

			if (webservice != null) {
				response = webservice.getCardVigilBlocked(request);
			}

			_log.debug("getCardVigilBlocked - end");
		}
		catch (Exception e) {
			_log.error("getCardVigilBlocked - " + e.getMessage(), e);
			response = new GetCardVigilBlockedResponseType();
			response.setHeader(this.getConnectionBusyResponseHeader());
		}
		finally {
			this.webServicePool.release(webservice);
		}

		return response;
	}

	public UpdateCardStatusResponseType updateCardStatus(
		UpdateCardStatusRequestType request) {

		try {
			_log.debug("updateCardStatus - start");

			UpdateCardStatusResponseType response = null;

			this.initWebServicePools();

			CardServicePortType webservice = (CardServicePortType)
				this.webServicePool.borrow("WS_CARDS");

			if (webservice != null) {
				response = webservice.updateCardStatus(request);
				this.webServicePool.release(webservice);
			}

			_log.debug("updateCardStatus - end");

			return response;
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}

	}

	public FindEntitiesByAttributeWithIdResponseType findEntitiesByAttributeWithId(
		FindEntitiesByAttributeWithIdRequestType request) {

		try {
			_log.debug("findEntitiesByAttributeWithId - start");

			FindEntitiesByAttributeWithIdResponseType response = null;

			this.initWebServicePools();

			EntityServicePortType webservice = (EntityServicePortType)
				this.webServicePool.borrow("WS_ENTITY");

			if (webservice != null) {
				response = webservice.findEntitiesByAttributeWithId(request);
				this.webServicePool.release(webservice);
			}

			_log.debug("findEntitiesByAttributeWithId - end");

			return response;
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}

	}

	public IsInPrimarySwitchModeResponseType
		isInPrimarySwitchMode(IsInPrimarySwitchModeRequestType request) {

		try {
			_log.debug("isInPrimarySwitchMode - start");
			IsInPrimarySwitchModeResponseType response = null;

			this.initWebServicePools();

			CardServicePortType webservice = null;

			webservice = (CardServicePortType)this.webServicePool.borrow("WS_CARDS");

			if (webservice != null) {
				response = webservice.isInPrimarySwitchMode(request);
				this.webServicePool.release(webservice);
			}

			if (response != null) {
				_log.debug("isInPrimarySwitchMode - end, response=" + response.isIsInPrimarySwitchMode());
			}

			return response;

		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}

	}

	public GetCardChannelPermissionsResponseType getCardChannelPermissions(
		GetCardChannelPermissionsRequestType request) {

		try {
			_log.debug("getCardChannelPermissions - start");

			GetCardChannelPermissionsResponseType response = null;

			this.initWebServicePools();

			CardServicePortType webservice = null;

			webservice = (CardServicePortType)this.webServicePool.borrow("WS_CARDS");

			if (webservice != null) {
				response = webservice.getCardChannelPermissions(request);
				this.webServicePool.release(webservice);
			}

			_log.debug("getCardChannelPermissions - end");

			return response;
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}

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

	private static final Log _log = LogFactoryUtil.getLog(LocalCardServiceImpl.class);

}