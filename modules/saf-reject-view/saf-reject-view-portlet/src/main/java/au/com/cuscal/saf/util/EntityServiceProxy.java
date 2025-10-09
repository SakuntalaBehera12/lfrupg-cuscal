package au.com.cuscal.saf.util;

import au.com.cuscal.common.framework.dxp.service.request.core.AbstractSelfRegisteringService;
import au.com.cuscal.framework.webservices.FindAttributeTypesRequestType;
import au.com.cuscal.framework.webservices.FindAttributeTypesResponseType;
import au.com.cuscal.framework.webservices.FindCategoriesRequestType;
import au.com.cuscal.framework.webservices.FindCategoriesResponseType;
import au.com.cuscal.framework.webservices.FindEntitiesByAttributeCategoryRequestType;
import au.com.cuscal.framework.webservices.FindEntitiesByAttributeCategoryResponseType;
import au.com.cuscal.framework.webservices.FindEntitiesByAttributeRequestType;
import au.com.cuscal.framework.webservices.FindEntitiesByAttributeResponseType;
import au.com.cuscal.framework.webservices.FindEntitiesByAttributeWithIdRequestType;
import au.com.cuscal.framework.webservices.FindEntitiesByAttributeWithIdResponseType;
import au.com.cuscal.framework.webservices.GetEntityByLongNameRequestType;
import au.com.cuscal.framework.webservices.GetEntityByLongNameResponseType;
import au.com.cuscal.framework.webservices.GetEntityByShortNameRequestType;
import au.com.cuscal.framework.webservices.GetEntityByShortNameResponseType;
import au.com.cuscal.framework.webservices.GetEntityRequestType;
import au.com.cuscal.framework.webservices.GetEntityResponseType;
import au.com.cuscal.framework.webservices.GetEntityWithHistoryRequestType;
import au.com.cuscal.framework.webservices.GetEntityWithHistoryResponseType;
import au.com.cuscal.framework.webservices.client.EntityService;
import au.com.cuscal.framework.webservices.client.impl.EntityServiceImpl;
import au.com.cuscal.framework.webservices.pooling.WebServicePooling;

import java.io.IOException;

public class EntityServiceProxy extends AbstractSelfRegisteringService implements EntityService  {

    @Override
    public GetEntityResponseType getEntity(GetEntityRequestType getEntityRequestType) {
        return entityServiceImpl.getEntity(getEntityRequestType);
    }

    @Override
    public GetEntityByShortNameResponseType getEntityByShortName(GetEntityByShortNameRequestType getEntityByShortNameRequestType) {
        return entityServiceImpl.getEntityByShortName(getEntityByShortNameRequestType);
    }

    @Override
    public GetEntityByLongNameResponseType getEntityByLongName(GetEntityByLongNameRequestType getEntityByLongNameRequestType) {
        return entityServiceImpl.getEntityByLongName(getEntityByLongNameRequestType);
    }

    @Override
    public FindEntitiesByAttributeResponseType findEntitiesByAttribute(FindEntitiesByAttributeRequestType findEntitiesByAttributeRequestType) {
        return entityServiceImpl.findEntitiesByAttribute(findEntitiesByAttributeRequestType);
    }

    @Override
    public FindEntitiesByAttributeWithIdResponseType findEntitiesByAttributeWithId(FindEntitiesByAttributeWithIdRequestType findEntitiesByAttributeWithIdRequestType) {
        return entityServiceImpl.findEntitiesByAttributeWithId(findEntitiesByAttributeWithIdRequestType);
    }

    @Override
    public FindEntitiesByAttributeCategoryResponseType findEntitiesByAttributeCategory(FindEntitiesByAttributeCategoryRequestType findEntitiesByAttributeCategoryRequestType) {
        return entityServiceImpl.findEntitiesByAttributeCategory(findEntitiesByAttributeCategoryRequestType);
    }

    @Override
    public FindAttributeTypesResponseType findAttributeTypes(FindAttributeTypesRequestType findAttributeTypesRequestType) {
        return entityServiceImpl.findAttributeTypes(findAttributeTypesRequestType);
    }

    @Override
    public FindCategoriesResponseType findCategories(FindCategoriesRequestType findCategoriesRequestType) {
        return entityServiceImpl.findCategories(findCategoriesRequestType);
    }

    @Override
    public GetEntityWithHistoryResponseType getEntityWithHistory(GetEntityWithHistoryRequestType getEntityWithHistoryRequestType) {
        return entityServiceImpl.getEntityWithHistory(getEntityWithHistoryRequestType);
    }

    @Override
    public String getTransactionServicesUrl() {
        return entityServiceImpl.getTransactionServicesUrl();
    }

    @Override
    public void setTransactionServicesUrl(String s) {
        entityServiceImpl.setTransactionServicesUrl(s);
    }

    @Override
    public String getCommonServicesUrl() {
        return entityServiceImpl.getCommonServicesUrl();
    }

    @Override
    public void setCommonServicesUrl(String s) {
        entityServiceImpl.setCommonServicesUrl(s);
    }

    @Override
    public String getEntityServicesUrl() {
        return entityServiceImpl.getEntityServicesUrl();
    }

    @Override
    public void setEntityServicesUrl(String s) {
        entityServiceImpl.setEntityServicesUrl(s);
    }

    @Override
    public String getCardServicesUrl() {
        return entityServiceImpl.getCardServicesUrl();
    }

    @Override
    public void setCardServicesUrl(String s) {
        entityServiceImpl.setCardServicesUrl(s);
    }

    @Override
    public String getAtmServicesUrl() {
        return entityServiceImpl.getAtmServicesUrl();
    }

    @Override
    public void setAtmServicesUrl(String s) {
        entityServiceImpl.setAtmServicesUrl(s);
    }

    @Override
    public String getSafServicesUrl() {
        return entityServiceImpl.getSafServicesUrl();
    }

    @Override
    public void setSafServicesUrl(String s) {
        entityServiceImpl.setSafServicesUrl(s);
    }

    @Override
    public WebServicePooling getWebServicePool() {
        return entityServiceImpl.getWebServicePool();
    }

    @Override
    public void setWebServicePool(WebServicePooling webServicePooling) {
        entityServiceImpl.setWebServicePool(webServicePooling);
    }

    @Override
    public void purgePools() {
        entityServiceImpl.purgePools();
    }

    @Override
    public void logPoolStatistics() {
        entityServiceImpl.logPoolStatistics();
    }

    @Override
    public void loadProperties() throws IOException {
        entityServiceImpl.loadProperties();
    }

    @Override
    public void loadProperties(String s) throws IOException {
        entityServiceImpl.loadProperties(s);
    }

    private final EntityServiceImpl entityServiceImpl = new EntityServiceImpl();

}
