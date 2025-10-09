package au.com.cuscal.quickSearchTx.services;

import au.com.cuscal.transactions.forms.TransactionForm;

import java.util.List;

public interface TransactionQuickSearchService {

	public List<String> getTransactionListOnSerch(
		TransactionForm transactionForm);

}