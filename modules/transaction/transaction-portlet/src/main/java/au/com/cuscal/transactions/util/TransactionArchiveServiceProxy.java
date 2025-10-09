package au.com.cuscal.transactions.util;

import au.com.cuscal.common.framework.dxp.service.request.core.AbstractSelfRegisteringService;
import au.com.cuscal.framework.webservices.client.TransactionArchiveService;
import au.com.cuscal.framework.webservices.pooling.WebServicePooling;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsArchiveRequestType;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsArchiveResponseType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionArchiveDetailsRequestType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionArchiveDetailsResponseType;

import java.io.IOException;

public class TransactionArchiveServiceProxy extends AbstractSelfRegisteringService
    implements TransactionArchiveService {

    @Override
    public FindTransactionsArchiveResponseType findTransactionsArchive(
        FindTransactionsArchiveRequestType findTransactionsArchiveRequestType) {

        return _transactionArchiveService.findTransactionsArchive(
            findTransactionsArchiveRequestType);
    }

    @Override
    public GetTransactionArchiveDetailsResponseType getTransactionArchiveDetails(
        GetTransactionArchiveDetailsRequestType getTransactionArchiveDetailsRequestType) {

        return _transactionArchiveService.getTransactionArchiveDetails(
            getTransactionArchiveDetailsRequestType);
    }

    @Override
    public String getTransactionServicesUrl() {
        return _transactionArchiveService.getTransactionServicesUrl();
    }

    @Override
    public void setTransactionServicesUrl(String s) {
        _transactionArchiveService.setTransactionServicesUrl(s);
    }

    @Override
    public String getCommonServicesUrl() {
        return _transactionArchiveService.getCommonServicesUrl();
    }

    @Override
    public void setCommonServicesUrl(String s) {
        _transactionArchiveService.setCommonServicesUrl(s);
    }

    @Override
    public String getEntityServicesUrl() {
        return _transactionArchiveService.getEntityServicesUrl();
    }

    @Override
    public void setEntityServicesUrl(String s) {
        _transactionArchiveService.setEntityServicesUrl(s);
    }

    @Override
    public String getCardServicesUrl() {
        return _transactionArchiveService.getCardServicesUrl();
    }

    @Override
    public void setCardServicesUrl(String s) {
        _transactionArchiveService.setCardServicesUrl(s);
    }

    @Override
    public String getAtmServicesUrl() {
        return _transactionArchiveService.getAtmServicesUrl();
    }

    @Override
    public void setAtmServicesUrl(String s) {
        _transactionArchiveService.setAtmServicesUrl(s);
    }

    @Override
    public String getSafServicesUrl() {
        return _transactionArchiveService.getSafServicesUrl();
    }

    @Override
    public void setSafServicesUrl(String s) {
        _transactionArchiveService.setSafServicesUrl(s);
    }

    @Override
    public WebServicePooling getWebServicePool() {
        return _transactionArchiveService.getWebServicePool();
    }

    @Override
    public void setWebServicePool(WebServicePooling webServicePooling) {
        _transactionArchiveService.setWebServicePool(webServicePooling);
    }

    @Override
    public void purgePools() {
        _transactionArchiveService.purgePools();
    }

    @Override
    public void logPoolStatistics() {
        _transactionArchiveService.logPoolStatistics();
    }

    @Override
    public void loadProperties() throws IOException {
        _transactionArchiveService.loadProperties();
    }

    @Override
    public void loadProperties(String s) throws IOException {
        _transactionArchiveService.loadProperties(s);
    }

    @Override
    public String getSchemeServicesUrl() {
        return _transactionArchiveService.getSchemeServicesUrl();
    }

    @Override
    public void setSchemeServicesUrl(String s) {
        _transactionArchiveService.setSchemeServicesUrl(s);
    }

    private final TransactionArchiveService _transactionArchiveService =
        new au.com.cuscal.framework.webservices.client.impl.TransactionArchiveServiceImpl();

}
