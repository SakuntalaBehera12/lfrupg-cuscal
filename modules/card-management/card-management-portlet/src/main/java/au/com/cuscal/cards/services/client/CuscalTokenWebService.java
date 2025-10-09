package au.com.cuscal.cards.services.client;

import static au.com.cuscal.cards.commons.Constants.CUSCAL_TOKEN_WEBSERVICE;

import static org.slf4j.LoggerFactory.getLogger;

import au.com.cuscal.esb.token.v1.CuscalTokenServices;
import au.com.cuscal.esb.token.v1.CuscalTokenServices_Service;
import au.com.cuscal.esb.token.v1.ObjectFactory;
import au.com.cuscal.esb.token.v1.TokenCreateRequest;
import au.com.cuscal.esb.token.v1.TokenCreateResponse;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service(value = CUSCAL_TOKEN_WEBSERVICE)
public class CuscalTokenWebService {

	public String getToken(String pan) throws MalformedURLException {
		final TokenCreateRequest tokenCreateRequest = getTokenCreateRequest(
			pan);
		final TokenCreateResponse tokenCreateResponse =
			getTokenService().apiTokenCreate(tokenCreateRequest);

		return tokenCreateResponse.getTokenReference();
	}

	protected CuscalTokenServices getCuscalTokenServices(URL endpoint) {
		return new CuscalTokenServices_Service(
			endpoint
		).getCuscalTokenServicesSOAP();
	}

	private TokenCreateRequest getTokenCreateRequest(String pan) {
		final TokenCreateRequest request =
			new ObjectFactory().createTokenCreateRequest();
		request.setParticipantChannelName(participantChannelName);
		request.setTokenData(pan);
		request.setTokenExpiryMethod(tokenExpiryMethod);
		request.setTokenExpiryDateTime(tokenExpiryDateTime);
		request.setCardValidationMethod(cardValidationMethod);

		return request;
	}

	private CuscalTokenServices getTokenService() throws MalformedURLException {
		final URL endpoint = new URL(tokenSvcUrl);
		log.trace("Establishing connection to endpoint {}", endpoint);

		return getCuscalTokenServices(endpoint);
	}

	private static final Logger log = getLogger(CuscalTokenWebService.class);

	@Autowired
	@Value("${card.validation.method}")
	private String cardValidationMethod;

	@Autowired
	@Value("${participant.channel.name}")
	private String participantChannelName;

	@Autowired
	@Value("${token.expiry.datetime}")
	private String tokenExpiryDateTime;

	@Autowired
	@Value("${token.expiry.method}")
	private String tokenExpiryMethod;

	@Autowired
	@Value("${webservice.token.svc.url}")
	private String tokenSvcUrl;

}