package au.com.cuscal.transactions.services.client;

import au.com.cuscal.framework.webservices.transaction.GetCustomerAccessViewRequestType;
import au.com.cuscal.framework.webservices.transaction.GetCustomerAccessViewResponseType;

public interface CommonService
	extends au.com.cuscal.framework.webservices.client.CardService {

	public GetCustomerAccessViewResponseType getCustomerAccessView(
		GetCustomerAccessViewRequestType params);

}