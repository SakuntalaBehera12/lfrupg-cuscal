package au.com.cuscal.transactions.services;

import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;
import au.com.cuscal.transactions.domain.TransactionDetail;
import au.com.cuscal.transactions.forms.TransactionForm;

import java.util.List;

public interface CudTransactionSearchService {

	public TransactionForm createTransactionFormObject();

	public String getOrgShortName(String liferayOrgId);

	public TransactionDetail getTransactionCudDetailByTxIdBusDateAndSrc(
			TransactionDetail transactionDetail, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public List<Object> getTransactionCudListOnSearch(
			TransactionForm transactionForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public void logPoolStatistics();

	public void purgePools();

}