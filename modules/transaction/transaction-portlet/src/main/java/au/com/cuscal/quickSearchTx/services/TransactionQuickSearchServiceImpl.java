package au.com.cuscal.quickSearchTx.services;

import au.com.cuscal.transactions.forms.TransactionForm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author Rajni Bharara
 *
 */
@Service("transactionQuickSearchService")
public class TransactionQuickSearchServiceImpl
	implements TransactionQuickSearchService {

	public List<String> getTransactionListOnSerch(
		TransactionForm transactionForm) {

		return new ArrayList<>();
	}

}