package au.com.cuscal.transactions.services.client;

import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.AddServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestRequest;
import au.com.cuscal.framework.webservices.selfservice.FindServiceRequestResponse;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesListRequest;
import au.com.cuscal.framework.webservices.selfservice.GetAttributesListResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTicketRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTicketResponse;
import au.com.cuscal.framework.webservices.selfservice.GetTransactionDetailsRequest;
import au.com.cuscal.framework.webservices.selfservice.GetTransactionDetailsResponse;

import java.util.HashMap;

public interface TransactionService
	extends au.com.cuscal.framework.webservices.client.TransactionService {

	public AddServiceRequestResponse addServiceRequest(
		AddServiceRequestRequest addServiceRequestRequest);

	public FindServiceRequestResponse findServiceRequest(
		FindServiceRequestRequest findServiceRequestRequest);

	public HashMap<String, String> getAccountIdentificationProps();

	public GetAttributesListResponse getAttributeList(
		GetAttributesListRequest getAttributesListRequest);

	public HashMap<String, String> getMessageCodeProps();

	public GetTicketResponse getTicket(GetTicketRequest getTicketRequest);

	public GetTransactionDetailsResponse getTransactionExtraDetails(
		GetTransactionDetailsRequest arnAndCardholderRequest);

	public HashMap<String, String> getWebServStatusCodeProps();

}