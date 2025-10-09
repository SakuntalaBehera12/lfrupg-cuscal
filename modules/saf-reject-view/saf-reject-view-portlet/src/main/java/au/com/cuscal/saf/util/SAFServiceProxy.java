package au.com.cuscal.saf.util;

import au.com.cuscal.common.framework.dxp.service.request.core.AbstractSelfRegisteringService;
import au.com.cuscal.framework.webservices.SAFRejectDetailsRequestType;
import au.com.cuscal.framework.webservices.SAFRejectDetailsResponseType;
import au.com.cuscal.framework.webservices.SAFRejectSearchRequestType;
import au.com.cuscal.framework.webservices.SAFRejectSearchResponseType;
import au.com.cuscal.framework.webservices.client.SAFService;
import au.com.cuscal.framework.webservices.client.impl.SAFServiceImpl;
import au.com.cuscal.framework.webservices.pooling.WebServicePooling;

import java.io.IOException;

public class SAFServiceProxy extends AbstractSelfRegisteringService implements SAFService {

    @Override
    public SAFRejectSearchResponseType findSafRejectList(SAFRejectSearchRequestType safRejectSearchRequestType) {
        return safService.findSafRejectList(safRejectSearchRequestType);
    }

    @Override
    public SAFRejectDetailsResponseType getSafRejectDetails(SAFRejectDetailsRequestType safRejectDetailsRequestType) {
        return safService.getSafRejectDetails(safRejectDetailsRequestType);
    }

    @Override
    public String getTransactionServicesUrl() {
        return safService.getTransactionServicesUrl();
    }

    @Override
    public void setTransactionServicesUrl(String s) {
        safService.setTransactionServicesUrl(s);
    }

    @Override
    public String getCommonServicesUrl() {
        return safService.getCommonServicesUrl();
    }

    @Override
    public void setCommonServicesUrl(String s) {
        safService.setCommonServicesUrl(s);
    }

    @Override
    public String getEntityServicesUrl() {
        return safService.getEntityServicesUrl();
    }

    @Override
    public void setEntityServicesUrl(String s) {
        safService.setEntityServicesUrl(s);
    }

    @Override
    public String getCardServicesUrl() {
        return safService.getCardServicesUrl();
    }

    @Override
    public void setCardServicesUrl(String s) {
        safService.setCardServicesUrl(s);
    }

    @Override
    public String getAtmServicesUrl() {
        return safService.getAtmServicesUrl();
    }

    @Override
    public void setAtmServicesUrl(String s) {
        safService.setAtmServicesUrl(s);
    }

    @Override
    public String getSafServicesUrl() {
        return safService.getSafServicesUrl();
    }

    @Override
    public void setSafServicesUrl(String s) {
        safService.setSafServicesUrl(s);
    }

    @Override
    public WebServicePooling getWebServicePool() {
        return safService.getWebServicePool();
    }

    @Override
    public void setWebServicePool(WebServicePooling webServicePooling) {
        safService.setWebServicePool(webServicePooling);
    }

    @Override
    public void purgePools() {
        safService.purgePools();
    }

    @Override
    public void logPoolStatistics() {
        safService.logPoolStatistics();
    }

    @Override
    public void loadProperties() throws IOException {
        safService.loadProperties();
    }

    @Override
    public void loadProperties(String s) throws IOException {
        safService.loadProperties(s);
    }

    private final SAFServiceImpl safService = new SAFServiceImpl();

}
