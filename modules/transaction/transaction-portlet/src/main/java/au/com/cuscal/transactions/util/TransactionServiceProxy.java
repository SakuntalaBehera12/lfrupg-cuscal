package au.com.cuscal.transactions.util;

import au.com.cuscal.common.framework.dxp.service.request.core.AbstractSelfRegisteringService;
import au.com.cuscal.framework.webservices.pooling.WebServicePooling;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdRequestType;
import au.com.cuscal.framework.webservices.transaction.FindEntitiesByAttributeWithIdResponseType;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsRequestType;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsResponseType;
import au.com.cuscal.framework.webservices.transaction.GetAs2805ResponseCodesRequestType;
import au.com.cuscal.framework.webservices.transaction.GetAs2805ResponseCodesResponseType;
import au.com.cuscal.framework.webservices.transaction.GetMessageCodesRequestType;
import au.com.cuscal.framework.webservices.transaction.GetMessageCodesResponseType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionRequestType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionResponseType;

import java.io.IOException;

public class TransactionServiceProxy extends AbstractSelfRegisteringService
    implements au.com.cuscal.framework.webservices.client.TransactionService  {

    @Override
    public FindTransactionsResponseType findTransactions(
        FindTransactionsRequestType findTransactionsRequestType) {

        return _transactionService.findTransactions(findTransactionsRequestType);
    }

    @Override
    public GetTransactionResponseType getTransaction(
        GetTransactionRequestType getTransactionRequestType) {

        return _transactionService.getTransaction(getTransactionRequestType);
    }

    @Override
    public GetMessageCodesResponseType getMessageCodes(
        GetMessageCodesRequestType getMessageCodesRequestType) {

        return _transactionService.getMessageCodes(getMessageCodesRequestType);
    }

    @Override
    public GetAs2805ResponseCodesResponseType getAs2805ResponseCodes(
        GetAs2805ResponseCodesRequestType getAs2805ResponseCodesRequestType) {

        return _transactionService.getAs2805ResponseCodes(
            getAs2805ResponseCodesRequestType);
    }

    @Override
    public FindEntitiesByAttributeWithIdResponseType findEntitiesByAttributeWithId(
        FindEntitiesByAttributeWithIdRequestType findEntitiesByAttributeWithIdRequestType) {

        return _transactionService.findEntitiesByAttributeWithId(
            findEntitiesByAttributeWithIdRequestType);
    }

    @Override
    public String getTransactionServicesUrl() {
        return _transactionService.getTransactionServicesUrl();
    }

    @Override
    public void setTransactionServicesUrl(String s) {
        _transactionService.setTransactionServicesUrl(s);
    }

    @Override
    public String getCommonServicesUrl() {
        return _transactionService.getCommonServicesUrl();
    }

    @Override
    public void setCommonServicesUrl(String s) {
        _transactionService.setCommonServicesUrl(s);
    }

    @Override
    public String getEntityServicesUrl() {
        return _transactionService.getEntityServicesUrl();
    }

    @Override
    public void setEntityServicesUrl(String s) {
        _transactionService.setAtmServicesUrl(s);
    }

    @Override
    public String getCardServicesUrl() {
        return _transactionService.getCardServicesUrl();
    }

    @Override
    public void setCardServicesUrl(String s) {
        _transactionService.setAtmServicesUrl(s);
    }

    @Override
    public String getAtmServicesUrl() {
        return _transactionService.getAtmServicesUrl();
    }

    @Override
    public void setAtmServicesUrl(String s) {
        _transactionService.setAtmServicesUrl(s);
    }

    @Override
    public String getSafServicesUrl() {
        return _transactionService.getSafServicesUrl();
    }

    @Override
    public void setSafServicesUrl(String s) {
        _transactionService.setSafServicesUrl(s);
    }

    @Override
    public WebServicePooling getWebServicePool() {
        return _transactionService.getWebServicePool();
    }

    @Override
    public void setWebServicePool(WebServicePooling webServicePooling) {
        _transactionService.setWebServicePool(webServicePooling);
    }

    @Override
    public void purgePools() {
        _transactionService.purgePools();
    }

    @Override
    public void logPoolStatistics() {
        _transactionService.logPoolStatistics();
    }

    @Override
    public void loadProperties() throws IOException {
        _transactionService.loadProperties();
    }

    @Override
    public void loadProperties(String s) throws IOException {
        _transactionService.loadProperties(s);
    }

    @Override
    public String getSchemeServicesUrl() {
        return _transactionService.getSchemeServicesUrl();
    }

    @Override
    public void setSchemeServicesUrl(String s) {
        _transactionService.setSchemeServicesUrl(s);
    }

    private final au.com.cuscal.framework.webservices.client.TransactionService _transactionService =
        new au.com.cuscal.framework.webservices.client.impl.TransactionServiceImpl();

}
