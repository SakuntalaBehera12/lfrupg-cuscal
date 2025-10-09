package au.com.cuscal.connect.util.resource.dxp.client;

import au.com.cuscal.framework.webservices.client.CustomerSelfServiceService;
import au.com.cuscal.framework.webservices.client.impl.AbstractCuscalWebServices;
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
import au.com.cuscal.framework.webservices.selfservice.SelfServiceWSService;
import au.com.cuscal.framework.webservices.selfservice.SpxClaimTransactionSearchRequest;
import au.com.cuscal.framework.webservices.selfservice.SpxClaimTransactionSearchResponse;
import au.com.cuscal.framework.webservices.selfservice.SpxPaymentTransactionSearchRequest;
import au.com.cuscal.framework.webservices.selfservice.SpxPaymentTransactionSearchResponse;
import au.com.cuscal.framework.webservices.selfservice.SpxTransactionSearchRequest;
import au.com.cuscal.framework.webservices.selfservice.SpxTransactionSearchResponse;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.selfservice.TktResponseHeader;
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

import org.apache.log4j.Logger;

public class CustomerSelfServiceServiceLocal extends AbstractCuscalWebServices
    implements CustomerSelfServiceService {

    public GetTicketResponse getServiceRequest(GetTicketRequest request) {
        _logger.debug("getTicket - start");

        GetTicketResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)
                this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Get Ticket start------------------------");
                _logger.debug("getTicket - header userId: " + header.getUserId());
                _logger.debug("getTicket - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("getTicket - header userOrgName: " + header.getUserOrgName());
                _logger.debug("getTicket - request ticketId: " + request.getTicketId());
                _logger.debug("---------------------------Get Ticket end------------------------");

                response = webservice.getTicket(request);

                _logger.debug("getTicket - response: " + response);
            }
        }
        catch (Exception e) {
            _logger.error("getTicket - webservice maybe down. " + e.getMessage(), e);

            response = new GetTicketResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("getTicket - end");

        return response;
    }

    public AddServiceRequestResponse addServiceRequest(AddServiceRequestRequest request) {
        _logger.debug("addTicket - start");

        AddServiceRequestResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)
                this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();
                _logger.debug("---------------------------Add Ticket start------------------------");
                _logger.debug("addTicket - header userId: " + header.getUserId());
                _logger.debug("addTicket - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("addTicket - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Add Ticket end------------------------");
                response = webservice.addServiceRequest(request);
            }
        }
        catch (Exception ie) {
            _logger.error("addTicket - Cannot connect to the WS. " + ie.getMessage(), ie);
            response = new AddServiceRequestResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("addTIcket - end");
        return response;
    }

    public AddSimpleServiceRequestResponse addSimpleServiceRequest(AddSimpleServiceRequestRequest request) {
        _logger.debug("addSimpleServiceRequest - start");

        AddSimpleServiceRequestResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)
                this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();
                _logger.debug("---------------------------Add Ticket start------------------------");
                _logger.debug("addTicket - header userId: " + header.getUserId());
                _logger.debug("addTicket - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("addTicket - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Add Ticket end------------------------");
                response = webservice.addSimpleServiceRequest(request);
            }
        }
        catch (Exception ie) {
            _logger.error("addTicket - Cannot connect to the WS. " + ie.getMessage(), ie);
            response = new AddSimpleServiceRequestResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("addTIcket - end");
        return response;
    }

    public FindServiceRequestResponse findServiceRequest(FindServiceRequestRequest request) {
        _logger.debug("findServiceRequest - start");

        FindServiceRequestResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Find Ticket start------------------------");
                _logger.debug("findServiceRequest - header userId: " + header.getUserId());
                _logger.debug("findServiceRequest - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("findServiceRequest - header userOrgName: " + header.getUserOrgName());
                _logger.debug("findServiceRequest - transactionId: " + request.getTransactionId());
                _logger.debug("findServiceRequest - businessDate: " + request.getBusinessDate());
                _logger.debug("---------------------------Find Ticket end------------------------");

                response = webservice.findServiceRequest(request);
            }
        }
        catch (Exception ie) {
            _logger.error("findServiceRequest - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new FindServiceRequestResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("findServiceRequest - end");

        return response;
    }

    public AddTicketNoteResponse addNote(AddTicketNoteRequest request) {
        _logger.debug("addNote - start");

        AddTicketNoteResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Add Note start------------------------");
                _logger.debug("addNote - header userId: " + header.getUserId());
                _logger.debug("addNote - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("addNote - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Add Note end------------------------");

                response = webservice.addTicketNote(request);
            }
        }
        catch (Exception e) {
            _logger.error("addNote - webservice maybe down. " + e.getMessage(), e);

            response = new AddTicketNoteResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("addNote - end");

        return response;
    }

    public GetTicketUserResponse getTicketUser(GetTicketUserRequest request) {
        _logger.debug("getTicketUser - start");

        GetTicketUserResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();
                _logger.debug("---------------------------Get Ticket User start------------------------");

                _logger.debug("getTicketUserRequest - header userId: " + header.getUserId());
                _logger.debug("getTicketUserRequest - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("getTicketUserRequest - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Get Ticket User end------------------------");

                response = webservice.getTicketUser(request);
            }
        }
        catch (Exception e) {
            _logger.error("getTicketUser - webservice maybe down. " + e.getMessage(), e);

            response = new GetTicketUserResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("getTicketUser - end");

        return response;
    }

    public FindCategoriesResponse findCategories(FindCategoriesRequest request) {
        _logger.debug("findCategories - start");

        FindCategoriesResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Find Categories start------------------------");
                _logger.debug("findServiceRequest - header userId: " + header.getUserId());
                _logger.debug("findServiceRequest - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("findServiceRequest - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Find Categories end------------------------");

                response = webservice.findCategories(request);
            }
        }
        catch (Exception e) {
            _logger.error("findCategories - webservice maybe down. " + e.getMessage(), e);

            response = new FindCategoriesResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("findCategories - end");

        return response;
    }

    public FindProductsResponse findProducts(FindProductsRequest request) {
        _logger.debug("findProducts - start");

        FindProductsResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Find Product start------------------------");
                _logger.debug("findProducts - header userId: " + header.getUserId());
                _logger.debug("findProducts - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("findProducts - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Find Product end------------------------");

                response = webservice.findProducts(request);
            }
        }
        catch (Exception e) {
            _logger.error("findProducts - webservice maybe down. " + e.getMessage(), e);

            response = new FindProductsResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("findProducts - end");

        return response;
    }

    public FindStatusesResponse findStatus(FindStatusRequest request) {
        _logger.debug("findStatus - start");

        FindStatusesResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Find Status start------------------------");
                _logger.debug("findStatus - header userId: " + header.getUserId());
                _logger.debug("findStatus - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("findStatus - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Find Status end------------------------");

                response = webservice.findStatuses(request);
            }
        }
        catch (Exception e) {
            _logger.error("findStatus - webservice maybe down. " + e.getMessage(), e);

            response = new FindStatusesResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("findStatus - end");

        return response;
    }

    public FindTypesResponse findTicketTypes(FindTypesRequest request) {
        _logger.debug("findTicketTypes - start");

        FindTypesResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Find Ticket Types start------------------------");
                _logger.debug("findStatus - header userId: " + header.getUserId());
                _logger.debug("findStatus - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("findStatus - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Find Ticket Types end------------------------");

                response = webservice.findTypes(request);
            }
        }
        catch (Exception e) {
            _logger.error("findStatus - webservice maybe down. " + e.getMessage(), e);

            response = new FindTypesResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("findStatus - end");

        return response;
    }

    public FindTicketsResponse findTickets(FindTicketsRequest request) {
        _logger.debug("findTickets - start");

        FindTicketsResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Find Ticket start------------------------");
                _logger.debug("findServiceRequest - header userId: " + header.getUserId());
                _logger.debug("findServiceRequest - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("findServiceRequest - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Find Ticket end------------------------");

                response = webservice.findTickets(request);
            }
        }
        catch (Exception e) {
            _logger.error("findTickets - webservice maybe down. " + e.getMessage(), e);

            response = new FindTicketsResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("findTickets - end");

        return response;
    }

    public GetTransactionDetailsResponse getTransactionDetails(GetTransactionDetailsRequest request) {
        _logger.debug("getTransactionDetails - start");

        GetTransactionDetailsResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Get Transaction Details start------------------------");
                _logger.debug("getTransactionDetails - header userId: " + header.getUserId());
                _logger.debug("getTransactionDetails - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("getTransactionDetails - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Get Transaction Details end------------------------");

                response = webservice.getTransactionDetails(request);
            }
        }
        catch (Exception e) {
            _logger.error("getTransactionDetails - webservice maybe down. " + e.getMessage(), e);

            response = new GetTransactionDetailsResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("getTransactionDetails - end");

        return response;
    }

    public AddTicketResponse addTicket(AddTicketRequest request) {
        _logger.debug("addTicket - start");

        AddTicketResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Add Ticket start------------------------");
                _logger.debug("addTicket - header userId: " + header.getUserId());
                _logger.debug("addTicket - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("addTicket - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Add Ticket end------------------------");

                response = webservice.addTicket(request);
            }
        }
        catch (Exception e) {
            _logger.error("addTicket - webservice maybe down. " + e.getMessage(), e);

            response = new AddTicketResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("addTicket - end");

        return response;
    }

    public GetTicketResponse getTicket(GetTicketRequest request) {
        _logger.debug("getTicket - start");

        GetTicketResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Get Ticket start------------------------");
                _logger.debug("getTicket - header userId: " + header.getUserId());
                _logger.debug("getTicket - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("getTicket - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Get Ticket end------------------------");

                response = webservice.getTicket(request);
            }
        }
        catch (Exception e) {
            _logger.error("addTicket - webservice maybe down. " + e.getMessage(), e);

            response = new GetTicketResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("addTicket - end");

        return response;
    }

    public TouchTicketResponse touchTicket(TouchTicketRequest request) {
        _logger.debug("touchTicket - start");

        TouchTicketResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Touch Ticket start------------------------");
                _logger.debug("touchTicket - header userId: " + header.getUserId());
                _logger.debug("touchTicket - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("touchTicket - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Touch Ticket end------------------------");

                response = webservice.touchTicket(request);
            }
        }
        catch (Exception e) {
            _logger.error("touchTicket - webservice maybe down. " + e.getMessage(), e);

            response = new TouchTicketResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("touchTicket - end");

        return response;
    }

    public UpdateTicketResponse updateTicket(UpdateTicketRequest request) {
        _logger.debug("updateTicket - start");

        UpdateTicketResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Update Ticket start------------------------");
                _logger.debug("updateTicket - header userId: " + header.getUserId());
                _logger.debug("updateTicket - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("updateTicket - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Update Ticket end------------------------");

                response = webservice.updateTicket(request);
            }
        }
        catch (Exception ie) {
            _logger.error("updateTicket - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new UpdateTicketResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("updateTicket - end");

        return response;
    }

    public UpdateTicketNoteResponse updateNote(UpdateTicketNoteRequest request) {
        _logger.debug("updateNote - start");

        UpdateTicketNoteResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Update Ticket Note start------------------------");
                _logger.debug("updateNote - header userId: " + header.getUserId());
                _logger.debug("updateNote - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("updateNote - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Update Ticket Note end------------------------");

                response = webservice.updateTicketNote(request);
            }
        }
        catch (Exception ie) {
            _logger.error("updateTicket - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new UpdateTicketNoteResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("updateTicket - end");

        return response;
    }

    public AddTicketCardResponse addCard(AddTicketCardRequest request) {
        _logger.debug("addCard - start");

        AddTicketCardResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Add Ticket Card start------------------------");
                _logger.debug("addCard - header userId: " + header.getUserId());
                _logger.debug("addCard - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("addCard - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Add Ticket Card end------------------------");

                response = webservice.addTicketCard(request);
            }
        }
        catch (Exception ie) {
            _logger.error("addCard - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new AddTicketCardResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("addCard - end");

        return response;
    }

    public AddTicketTransactionResponse addTransaction(AddTicketTransactionRequest request) {
        _logger.debug("addTransaction - start");
        AddTicketTransactionResponse response = null;
        this.initWebServicePools();
        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");
            if (null != webservice) {
                TktRequestHeader header = request.getHeader();
                _logger.debug("---------------------------Add Ticket Transaction start------------------------");
                _logger.debug("addTransaction - header userId: " + header.getUserId());
                _logger.debug("addTransaction - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("addTransaction - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Add Ticket Transaction end------------------------");
                response = webservice.addTicketTransaction(request);
            }
        }
        catch (Exception ie) {
            _logger.error("addTransaction - Cannot connect to the WS. " + ie.getMessage(), ie);
            response = new AddTicketTransactionResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("addTransaction - end");
        return response;
    }

    public FindDestinationsResponse findDestinations(FindDestinationsRequest request) {
        _logger.debug("findDestinations - start");
        FindDestinationsResponse response = null;
        this.initWebServicePools();
        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");
            if (null != webservice) {
                TktRequestHeader header = request.getHeader();
                _logger.debug("---------------------------Find Destinations start------------------------");
                _logger.debug("findDestinations - header userId: " + header.getUserId());
                _logger.debug("findDestinations - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("findDestinations - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Find Destinations end------------------------");
                response = webservice.findDestinations(request);
            }
        }
        catch (Exception ie) {
            _logger.error("findDestinations - Cannot connect to the WS. " + ie.getMessage(), ie);
            response = new FindDestinationsResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("findDestinations - end");
        return response;
    }

    public GetAttributesListResponse getAttributesList(GetAttributesListRequest request) {
        _logger.debug("getAttributesList - start");
        GetAttributesListResponse response = null;
        this.initWebServicePools();
        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");
            if (null != webservice) {
                TktRequestHeader header = request.getHeader();
                _logger.debug("---------------------------Get Attributes List start------------------------");
                _logger.debug("getAttributesList - header userId: " + header.getUserId());
                _logger.debug("getAttributesList - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("getAttributesList - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Get Attributes Type end------------------------");
                response = webservice.getAttributesList(request);
            }
        }
        catch (Exception ie) {
            _logger.error("getAttributesList - Cannot connect to the WS. " + ie.getMessage(), ie);
            response = new GetAttributesListResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("getAttributesList - end");
        return response;
    }

    public GetAttributesTypeResponse getAttributesType(GetAttributesTypeRequest request) {
        _logger.debug("getAttributesType - start");

        GetAttributesTypeResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Get Attributes Type start------------------------");
                _logger.debug("getAttributesType - header userId: " + header.getUserId());
                _logger.debug("getAttributesType - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("getAttributesType - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Get Attributes Type end------------------------");

                response = webservice.getAttributesType(request);
            }
        }
        catch (Exception ie) {
            _logger.error("getAttributesType - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new GetAttributesTypeResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("getAttributesType - end");

        return response;
    }

    public GetCompleteActionRequiredResponse getCompleteActionRequired(GetCompleteActionRequiredRequest request) {
        _logger.debug("getCompleteActionRequired - start");

        GetCompleteActionRequiredResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Get Complete Action Required start------------------------");
                _logger.debug("getCompleteActionRequired - header userId: " + header.getUserId());
                _logger.debug("getCompleteActionRequired - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("getCompleteActionRequired - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Get Complete Action Required end------------------------");

                response = webservice.getCompleteActionRequired(request);
            }
        }
        catch (Exception ie) {
            _logger.error("getCompleteActionRequired - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new GetCompleteActionRequiredResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("getCompleteActionRequired - end");

        return response;
    }

    public UploadAttachmentResponse uploadAttachment(UploadAttachmentRequest request) {
        _logger.debug("uploadAttachment - start");

        UploadAttachmentResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Upload Attachement start------------------------");
                _logger.debug("uploadAttachment - header userId: " + header.getUserId());
                _logger.debug("uploadAttachment - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("uploadAttachment - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Upload Attachement end------------------------");

                response = webservice.uploadAttachment(request);
            }
        }
        catch (Exception ie) {
            _logger.error("uploadAttachment - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new UploadAttachmentResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("uploadAttachment - end");

        return response;
    }

    public FindAttachmentsResponse findAttachments(FindAttachmentsRequest request) {
        _logger.debug("findAttachments - start");

        FindAttachmentsResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Find Attachements start------------------------");
                _logger.debug("findAttachments - header userId: " + header.getUserId());
                _logger.debug("findAttachments - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("findAttachments - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Find Attachements end------------------------");

                response = webservice.findAttachments(request);
            }
        }
        catch (Exception ie) {
            _logger.error("findAttachments - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new FindAttachmentsResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("findAttachments - end");

        return response;
    }

    public DownloadAttachmentResponse downloadAttachment(DownloadAttachmentRequest request) {
        _logger.debug("downloadAttachment - start");

        DownloadAttachmentResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------Download Attachement start------------------------");
                _logger.debug("downloadAttachment - header userId: " + header.getUserId());
                _logger.debug("downloadAttachment - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("downloadAttachment - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------Download Attachement end------------------------");

                response = webservice.downloadAttachment(request);
            }
        }
        catch (Exception ie) {
            _logger.error("downloadAttachment - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new DownloadAttachmentResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("downloadAttachment - end");

        return response;
    }

    private TktResponseHeader getConnectionBusyResponseHeaderForServiceRequest() {
        TktResponseHeader header = new TktResponseHeader();

        header.setStatusCode("SERVICE_BUSY");
        header.setMessage("Unable to handle user request due to connection unavailable");

        return header;
    }

    private TktResponseHeader getWSDownResponseHeaderForServiceRequest() {
        TktResponseHeader header = new TktResponseHeader();

        header.setStatusCode("SYSTEM_ERROR");
        header.setMessage("WS is down and cannot be contacted");

        return header;
    }

    public UpdateSimpleServiceRequestResponse updateSimpleServiceRequest(UpdateSimpleServiceRequestRequest request) {
        _logger.debug("updateSimpleServiceRequest - start");

        UpdateSimpleServiceRequestResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------updateSimpleServiceRequest start------------------------");
                _logger.debug("updateSimpleServiceRequest - header userId: " + header.getUserId());
                _logger.debug("updateSimpleServiceRequest - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("updateSimpleServiceRequest - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------updateSimpleServiceRequest end------------------------");

                response = webservice.updateSimpleServiceRequest(request);
            }
        }
        catch (Exception e) {
            _logger.error("updateSimpleServiceRequest - webservice maybe down. " + e.getMessage(), e);

            response = new UpdateSimpleServiceRequestResponse();
            response.setHeader(this.getConnectionBusyResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("updateSimpleServiceRequest - end");

        return response;
    }

    public SpxClaimTransactionSearchResponse spxClaimTransactionSearch(SpxClaimTransactionSearchRequest request) {
        _logger.debug("spxClaimTransactionSearch - start");

        SpxClaimTransactionSearchResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------spxClaimTransactionSearch start------------------------");
                _logger.debug("spxClaimTransactionSearch - header userId: " + header.getUserId());
                _logger.debug("spxClaimTransactionSearch - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("spxClaimTransactionSearch - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------spxClaimTransactionSearch end------------------------");

                response = webservice.spxClaimTransactionSearch(request);
            }
        }
        catch (Exception ie) {
            _logger.error("spxClaimTransactionSearch - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new SpxClaimTransactionSearchResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("spxClaimTransactionSearch - end");

        return response;
    }

    public SpxPaymentTransactionSearchResponse spxPaymentTransactionSearch(SpxPaymentTransactionSearchRequest request) {
        _logger.debug("spxPaymentTransactionSearch - start");

        SpxPaymentTransactionSearchResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------spxPaymentTransactionSearch start------------------------");
                _logger.debug("spxPaymentTransactionSearch - header userId: " + header.getUserId());
                _logger.debug("spxPaymentTransactionSearch - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("spxPaymentTransactionSearch - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------spxPaymentTransactionSearch end------------------------");

                response = webservice.spxPaymentTransactionSearch(request);
            }
        }
        catch (Exception ie) {
            _logger.error("spxPaymentTransactionSearch - Cannot connect to the WS. " + ie.getMessage(), ie);

            response = new SpxPaymentTransactionSearchResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("spxPaymentTransactionSearch - end");

        return response;
    }

    public SpxTransactionSearchResponse spxTransactionSearch(SpxTransactionSearchRequest request) {
        _logger.debug("spxTransactionSearch - start");

        SpxTransactionSearchResponse response = null;

        this.initWebServicePools();

        SelfServiceWSService webservice = null;

        try {
            webservice = (SelfServiceWSService)this.webServicePool.borrow("WS_TICKETING");

            if (null != webservice) {
                TktRequestHeader header = request.getHeader();

                _logger.debug("---------------------------spxTransactionSearch start------------------------");
                _logger.debug("spxTransactionSearch - header userId: " + header.getUserId());
                _logger.debug("spxTransactionSearch - header userOrgShortName: " + header.getUserOrgId());
                _logger.debug("spxTransactionSearch - header userOrgName: " + header.getUserOrgName());
                _logger.debug("---------------------------spxTransactionSearch end------------------------");

                response = webservice.spxTransactionSearch(request);
            }
        }
        catch (Exception exception) {
            _logger.error("spxTransactionSearch - Cannot connect to the WS. " + exception.getMessage(), exception);

            response = new SpxTransactionSearchResponse();
            response.setHeader(this.getWSDownResponseHeaderForServiceRequest());
        }
        finally {
            this.webServicePool.release(webservice);
        }

        _logger.debug("spxTransactionSearch - end");

        return response;
    }

    private static final Logger _logger = Logger.getLogger(CustomerSelfServiceServiceLocal.class);

}
