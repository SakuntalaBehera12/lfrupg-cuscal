package au.com.cuscal.transactions.util;

import au.com.cuscal.connect.util.resource.dxp.client.CustomerSelfServiceServiceLocal;
import au.com.cuscal.framework.webservices.client.CustomerSelfServiceService;
import au.com.cuscal.framework.webservices.pooling.WebServicePooling;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.AddSimpleServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.AddSimpleServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.AddTicketCardRequest;
import au.com.cuscal.framework.webservices.selfservice.AddTicketCardResponse;
import au.com.cuscal.framework.webservices.selfservice.AddTicketNoteRequest;
import au.com.cuscal.framework.webservices.selfservice.AddTicketNoteResponse;
import au.com.cuscal.framework.webservices.selfservice.AddTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.AddTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.AddTicketTransactionRequest;
import au.com.cuscal.framework.webservices.selfservice.AddTicketTransactionResponse;
import au.com.cuscal.framework.webservices.selfservice.DownloadAttachmentRequest;
import au.com.cuscal.framework.webservices.selfservice.DownloadAttachmentResponse;
import au.com.cuscal.framework.webservices.selfservice.FindAttachmentsRequest;
import au.com.cuscal.framework.webservices.selfservice.FindAttachmentsResponse;
import au.com.cuscal.framework.webservices.selfservice.FindCategoriesRequest;
import au.com.cuscal.framework.webservices.selfservice.FindCategoriesResponse;
import au.com.cuscal.framework.webservices.selfservice.FindDestinationsRequest;
import au.com.cuscal.framework.webservices.selfservice.FindDestinationsResponse;
import au.com.cuscal.framework.webservices.selfservice.FindProductsRequest;
import au.com.cuscal.framework.webservices.selfservice.FindProductsResponse;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.FindStatusRequest;
import au.com.cuscal.framework.webservices.selfservice.FindStatusesResponse;
import au.com.cuscal.framework.webservices.selfservice.FindTicketsRequest;
import au.com.cuscal.framework.webservices.selfservice.FindTicketsResponse;
import au.com.cuscal.framework.webservices.selfservice.FindTypesRequest;
import au.com.cuscal.framework.webservices.selfservice.FindTypesResponse;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesListRequest;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesListResponse;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesTypeRequest;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesTypeResponse;
import au.com.cuscal.framework.webservices.selfservice.GetCompleteActionRequiredRequest;
import au.com.cuscal.framework.webservices.selfservice.GetCompleteActionRequiredResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTicketUserRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTicketUserResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTransactionDetailsRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTransactionDetailsResponse;
import au.com.cuscal.framework.webservices.selfservice.SpxClaimTransactionSearchRequest;
import au.com.cuscal.framework.webservices.selfservice.SpxClaimTransactionSearchResponse;
import au.com.cuscal.framework.webservices.selfservice.SpxPaymentTransactionSearchRequest;
import au.com.cuscal.framework.webservices.selfservice.SpxPaymentTransactionSearchResponse;
import au.com.cuscal.framework.webservices.selfservice.SpxTransactionSearchRequest;
import au.com.cuscal.framework.webservices.selfservice.SpxTransactionSearchResponse;
import au.com.cuscal.framework.webservices.selfservice.TouchTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.TouchTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.UpdateSimpleServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.UpdateSimpleServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.UpdateTicketNoteRequest;
import au.com.cuscal.framework.webservices.selfservice.UpdateTicketNoteResponse;
import au.com.cuscal.framework.webservices.selfservice.UpdateTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.UpdateTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.UploadAttachmentRequest;
import au.com.cuscal.framework.webservices.selfservice.UploadAttachmentResponse;

import java.io.IOException;

public class CustomerSelfServiceServiceProxy implements
    au.com.cuscal.framework.webservices.client.CustomerSelfServiceService {

    @Override
    public GetTicketResponse getServiceRequest(GetTicketRequest getTicketRequest) {
        return _customerSelfServiceService.getServiceRequest(getTicketRequest);
    }

    @Override
    public AddTicketResponse addTicket(AddTicketRequest addTicketRequest) {
        return _customerSelfServiceService.addTicket(addTicketRequest);
    }

    @Override
    public GetTicketResponse getTicket(GetTicketRequest getTicketRequest) {
        return _customerSelfServiceService.getTicket(getTicketRequest);
    }

    @Override
    public FindTicketsResponse findTickets(FindTicketsRequest findTicketsRequest) {
        return _customerSelfServiceService.findTickets(findTicketsRequest);
    }

    @Override
    public TouchTicketResponse touchTicket(TouchTicketRequest touchTicketRequest) {
        return _customerSelfServiceService.touchTicket(touchTicketRequest);
    }

    @Override
    public UpdateTicketResponse updateTicket(UpdateTicketRequest updateTicketRequest) {
        return _customerSelfServiceService.updateTicket(updateTicketRequest);
    }

    @Override
    public AddTicketNoteResponse addNote(AddTicketNoteRequest addTicketNoteRequest) {
        return _customerSelfServiceService.addNote(addTicketNoteRequest);
    }

    @Override
    public UpdateTicketNoteResponse updateNote(UpdateTicketNoteRequest updateTicketNoteRequest) {
        return _customerSelfServiceService.updateNote(updateTicketNoteRequest);
    }

    @Override
    public AddTicketCardResponse addCard(AddTicketCardRequest addTicketCardRequest) {
        return _customerSelfServiceService.addCard(addTicketCardRequest);
    }

    @Override
    public AddTicketTransactionResponse addTransaction(
        AddTicketTransactionRequest addTicketTransactionRequest) {

        return _customerSelfServiceService.addTransaction(addTicketTransactionRequest);
    }

    @Override
    public FindDestinationsResponse findDestinations(
        FindDestinationsRequest findDestinationsRequest) {

        return _customerSelfServiceService.findDestinations(findDestinationsRequest);
    }

    @Override
    public FindProductsResponse findProducts(FindProductsRequest findProductsRequest) {
        return _customerSelfServiceService.findProducts(findProductsRequest);
    }

    @Override
    public FindTypesResponse findTicketTypes(FindTypesRequest findTypesRequest) {
        return _customerSelfServiceService.findTicketTypes(findTypesRequest);
    }

    @Override
    public FindCategoriesResponse findCategories(FindCategoriesRequest findCategoriesRequest) {
        return _customerSelfServiceService.findCategories(findCategoriesRequest);
    }

    @Override
    public FindStatusesResponse findStatus(FindStatusRequest findStatusRequest) {
        return _customerSelfServiceService.findStatus(findStatusRequest);
    }

    @Override
    public AddServiceRequestResponse addServiceRequest(
        AddServiceRequestRequest addServiceRequestRequest) {

        return _customerSelfServiceService.addServiceRequest(
            addServiceRequestRequest);
    }

    @Override
    public AddSimpleServiceRequestResponse addSimpleServiceRequest(
        AddSimpleServiceRequestRequest addSimpleServiceRequestRequest) {

        return _customerSelfServiceService.addSimpleServiceRequest(
            addSimpleServiceRequestRequest);
    }

    @Override
    public FindServiceRequestResponse findServiceRequest(
        FindServiceRequestRequest findServiceRequestRequest) {

        return _customerSelfServiceService.findServiceRequest(findServiceRequestRequest);
    }

    @Override
    public GetAttributesListResponse getAttributesList(
        GetAttributesListRequest getAttributesListRequest) {

        return _customerSelfServiceService.getAttributesList(getAttributesListRequest);
    }

    @Override
    public GetAttributesTypeResponse getAttributesType(
        GetAttributesTypeRequest getAttributesTypeRequest) {

        return _customerSelfServiceService.getAttributesType(getAttributesTypeRequest);
    }

    @Override
    public GetTicketUserResponse getTicketUser(GetTicketUserRequest getTicketUserRequest) {
        return _customerSelfServiceService.getTicketUser(getTicketUserRequest);
    }

    @Override
    public GetCompleteActionRequiredResponse getCompleteActionRequired(
        GetCompleteActionRequiredRequest getCompleteActionRequiredRequest) {

        return _customerSelfServiceService.getCompleteActionRequired(
            getCompleteActionRequiredRequest);
    }

    @Override
    public GetTransactionDetailsResponse getTransactionDetails(
        GetTransactionDetailsRequest getTransactionDetailsRequest) {

        return _customerSelfServiceService.getTransactionDetails(getTransactionDetailsRequest);
    }

    @Override
    public UploadAttachmentResponse uploadAttachment(
        UploadAttachmentRequest uploadAttachmentRequest) {

        return _customerSelfServiceService.uploadAttachment(uploadAttachmentRequest);
    }

    @Override
    public FindAttachmentsResponse findAttachments(FindAttachmentsRequest findAttachmentsRequest) {
        return _customerSelfServiceService.findAttachments(findAttachmentsRequest);
    }

    @Override
    public DownloadAttachmentResponse downloadAttachment(
        DownloadAttachmentRequest downloadAttachmentRequest) {

        return _customerSelfServiceService.downloadAttachment(
            downloadAttachmentRequest);
    }

    @Override
    public UpdateSimpleServiceRequestResponse updateSimpleServiceRequest(
        UpdateSimpleServiceRequestRequest updateSimpleServiceRequestRequest) {

        return _customerSelfServiceService.updateSimpleServiceRequest(
            updateSimpleServiceRequestRequest);
    }

    @Override
    public SpxClaimTransactionSearchResponse spxClaimTransactionSearch(
        SpxClaimTransactionSearchRequest spxClaimTransactionSearchRequest) {

        return _customerSelfServiceService.spxClaimTransactionSearch(
            spxClaimTransactionSearchRequest);
    }

    @Override
    public SpxPaymentTransactionSearchResponse spxPaymentTransactionSearch(
        SpxPaymentTransactionSearchRequest spxPaymentTransactionSearchRequest) {

        return _customerSelfServiceService.spxPaymentTransactionSearch(
            spxPaymentTransactionSearchRequest);
    }

    @Override
    public SpxTransactionSearchResponse spxTransactionSearch(
        SpxTransactionSearchRequest spxTransactionSearchRequest) {

        return _customerSelfServiceService.spxTransactionSearch(spxTransactionSearchRequest);
    }

    @Override
    public String getTransactionServicesUrl() {
        return _customerSelfServiceService.getTransactionServicesUrl();
    }

    @Override
    public void setTransactionServicesUrl(String s) {
        _customerSelfServiceService.setTransactionServicesUrl(s);
    }

    @Override
    public String getCommonServicesUrl() {
        return _customerSelfServiceService.getCommonServicesUrl();
    }

    @Override
    public void setCommonServicesUrl(String s) {
        _customerSelfServiceService.setCommonServicesUrl(s);
    }

    @Override
    public String getEntityServicesUrl() {
        return _customerSelfServiceService.getEntityServicesUrl();
    }

    @Override
    public void setEntityServicesUrl(String s) {
        _customerSelfServiceService.setEntityServicesUrl(s);
    }

    @Override
    public String getCardServicesUrl() {
        return _customerSelfServiceService.getCardServicesUrl();
    }

    @Override
    public void setCardServicesUrl(String s) {
        _customerSelfServiceService.setSafServicesUrl(s);
    }

    @Override
    public String getAtmServicesUrl() {
        return _customerSelfServiceService.getAtmServicesUrl();
    }

    @Override
    public void setAtmServicesUrl(String s) {
        _customerSelfServiceService.setAtmServicesUrl(s);
    }

    @Override
    public String getSafServicesUrl() {
        return _customerSelfServiceService.getSafServicesUrl();
    }

    @Override
    public void setSafServicesUrl(String s) {
        _customerSelfServiceService.setSafServicesUrl(s);
    }

    @Override
    public WebServicePooling getWebServicePool() {
        return _customerSelfServiceService.getWebServicePool();
    }

    @Override
    public void setWebServicePool(WebServicePooling webServicePooling) {
        _customerSelfServiceService.setWebServicePool(webServicePooling);
    }

    @Override
    public void purgePools() {
        _customerSelfServiceService.purgePools();
    }

    @Override
    public void logPoolStatistics() {
        _customerSelfServiceService.logPoolStatistics();
    }

    @Override
    public void loadProperties() throws IOException {
        _customerSelfServiceService.loadProperties();
    }

    @Override
    public void loadProperties(String s) throws IOException {
        _customerSelfServiceService.loadProperties(s);
    }

    @Override
    public String getSchemeServicesUrl() {
        return _customerSelfServiceService.getSchemeServicesUrl();
    }

    @Override
    public void setSchemeServicesUrl(String s) {
        _customerSelfServiceService.setSchemeServicesUrl(s);
    }

    private final CustomerSelfServiceService _customerSelfServiceService =
        new CustomerSelfServiceServiceLocal();

}
