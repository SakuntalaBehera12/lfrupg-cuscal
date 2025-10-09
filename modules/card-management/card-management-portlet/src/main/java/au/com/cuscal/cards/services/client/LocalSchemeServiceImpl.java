package au.com.cuscal.cards.services.client;

import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.framework.webservices.client.SchemeService;
import au.com.cuscal.framework.webservices.client.impl.AbstractCuscalWebServices;

import au.com.cuscal.framework.webservices.scheme.ActivateTokenRequest;
import au.com.cuscal.framework.webservices.scheme.ActivateTokenResponse;
import au.com.cuscal.framework.webservices.scheme.HandleTokenUpdateRequest;
import au.com.cuscal.framework.webservices.scheme.HandleTokenUpdateResponse;
import au.com.cuscal.framework.webservices.scheme.SearchTokensRequest;
import au.com.cuscal.framework.webservices.scheme.SearchTokensResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "schemeService")
public class LocalSchemeServiceImpl extends AbstractCuscalWebServices implements SchemeService {

	@Override
	public SearchTokensResponse searchTokens(SearchTokensRequest searchTokensRequest) {
		return _schemeService.searchTokens(searchTokensRequest);
	}

	@Override
	public ActivateTokenResponse activateToken(ActivateTokenRequest activateTokenRequest) {
		return _schemeService.activateToken(activateTokenRequest);
	}

	@Override
	public HandleTokenUpdateResponse updateToken(HandleTokenUpdateRequest handleTokenUpdateRequest) {
		return _schemeService.updateToken(handleTokenUpdateRequest);
	}

	@Autowired
	@Value("${webservice.scheme.url}")
	protected String schemeServicesUrl = null;

	private final SchemeService _schemeService = CuscalServiceLocator.getService(
		SchemeService.class.getName());

}