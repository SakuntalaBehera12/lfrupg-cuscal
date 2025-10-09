package au.com.cuscal.cards.util;

import au.com.cuscal.common.framework.dxp.service.request.core.AbstractSelfRegisteringService;
import au.com.cuscal.framework.webservices.client.SchemeService;
import au.com.cuscal.framework.webservices.client.impl.SchemeServiceImpl;
import au.com.cuscal.framework.webservices.pooling.WebServicePooling;
import au.com.cuscal.framework.webservices.scheme.ActivateTokenRequest;
import au.com.cuscal.framework.webservices.scheme.ActivateTokenResponse;
import au.com.cuscal.framework.webservices.scheme.HandleTokenUpdateRequest;
import au.com.cuscal.framework.webservices.scheme.HandleTokenUpdateResponse;
import au.com.cuscal.framework.webservices.scheme.SearchTokensRequest;
import au.com.cuscal.framework.webservices.scheme.SearchTokensResponse;

import java.io.IOException;

public class SchemeServiceProxy extends AbstractSelfRegisteringService
    implements SchemeService {

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

    @Override
    public String getTransactionServicesUrl() {
        return _schemeService.getTransactionServicesUrl();
    }

    @Override
    public void setTransactionServicesUrl(String s) {
        _schemeService.setTransactionServicesUrl(s);
    }

    @Override
    public String getCommonServicesUrl() {
        return _schemeService.getCommonServicesUrl();
    }

    @Override
    public void setCommonServicesUrl(String s) {
        _schemeService.setCommonServicesUrl(s);
    }

    @Override
    public String getEntityServicesUrl() {
        return _schemeService.getEntityServicesUrl();
    }

    @Override
    public void setEntityServicesUrl(String s) {
        _schemeService.setEntityServicesUrl(s);
    }

    @Override
    public String getCardServicesUrl() {
        return _schemeService.getCardServicesUrl();
    }

    @Override
    public void setCardServicesUrl(String s) {
        _schemeService.setCardServicesUrl(s);
    }

    @Override
    public String getAtmServicesUrl() {
        return _schemeService.getAtmServicesUrl();
    }

    @Override
    public void setAtmServicesUrl(String s) {
        _schemeService.setAtmServicesUrl(s);
    }

    @Override
    public String getSafServicesUrl() {
        return _schemeService.getSafServicesUrl();
    }

    @Override
    public void setSafServicesUrl(String s) {
        _schemeService.setSafServicesUrl(s);
    }

    @Override
    public WebServicePooling getWebServicePool() {
        return _schemeService.getWebServicePool();
    }

    @Override
    public void setWebServicePool(WebServicePooling webServicePooling) {
        _schemeService.setWebServicePool(webServicePooling);
    }

    @Override
    public void purgePools() {
        _schemeService.purgePools();
    }

    @Override
    public void logPoolStatistics() {
        _schemeService.logPoolStatistics();
    }

    @Override
    public void loadProperties() throws IOException {
        _schemeService.loadProperties();
    }

    @Override
    public void loadProperties(String s) throws IOException {
        _schemeService.loadProperties(s);
    }

    @Override
    public String getSchemeServicesUrl() {
        return _schemeService.getSchemeServicesUrl();
    }

    @Override
    public void setSchemeServicesUrl(String s) {
        _schemeService.setSchemeServicesUrl(s);
    }

    private final SchemeServiceImpl _schemeService = new SchemeServiceImpl();

}
