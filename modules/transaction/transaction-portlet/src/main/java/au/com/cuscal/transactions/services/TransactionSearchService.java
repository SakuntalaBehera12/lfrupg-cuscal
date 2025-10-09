package au.com.cuscal.transactions.services;

import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;
import au.com.cuscal.framework.webservices.transaction.Customer;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;
import au.com.cuscal.transactions.domain.Codes;
import au.com.cuscal.transactions.domain.ReasonData;
import au.com.cuscal.transactions.domain.TicketDetails;
import au.com.cuscal.transactions.domain.TransactionDetail;
import au.com.cuscal.transactions.forms.PinChangeSearchForm;
import au.com.cuscal.transactions.forms.ServiceRequestForm;
import au.com.cuscal.transactions.forms.TransactionForm;
import au.com.cuscal.transactions.services.client.TransactionService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;

public interface TransactionSearchService {

	public String addServiceRequest(
			ServiceRequestForm form, TktRequestHeader header,
			PortletContext portletContext)
		throws Exception;

	public PinChangeSearchForm createPinChangeSearchFormObject();

	public TransactionForm createTransactionFormObject();

	public List<TicketDetails> findServiceRequest(
		String transactionId, String businessDate, TktRequestHeader header);

	public String[] getAllMessageCodeTypeForUIDropDown();

	public List<Customer> getCustomerAccessViewList(Long liferayOrgId);

	public Map<String, Set<Codes>> getMessageCodeDescription(
			PortletContext portletContext)
		throws Exception;

	public String getOrgShortName(String liferayOrgId, PortletRequest request);

	public TransactionDetail getPinChangeDetailByIdBusDateAndSrc(
			TransactionDetail pinChangeDetail, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public List<Object> getPinChangeListOnSearch(
			TransactionForm transactionForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public List<Codes> getResponseCodeDescription(PortletContext portletContext)
		throws Exception;

	public TransactionDetail getTransactionDetailByTxIdBusDateAndSrc(
			TransactionDetail transactionDetail, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public List<Object> getTransactionListOnSearch(
			TransactionForm transactionForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public TransactionService getTransactionService();

	public void logPoolStatistics();

	public void purgePools();

	public ReasonData retrieveListToDisplay(
		TransactionDetail transactionDetail, SearchHeader searchHeader,
		PortletRequest request);

	public void setTransactionService(TransactionService transactionService);

}