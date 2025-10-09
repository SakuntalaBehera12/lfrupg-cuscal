package au.com.cuscal.transactions.services.client;

import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.framework.webservices.client.TransactionArchiveService;
import au.com.cuscal.framework.webservices.client.impl.AbstractCuscalWebServices;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsArchiveRequestType;
import au.com.cuscal.framework.webservices.transaction.FindTransactionsArchiveResponseType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionArchiveDetailsRequestType;
import au.com.cuscal.framework.webservices.transaction.GetTransactionArchiveDetailsResponseType;
import au.com.cuscal.transactions.util.TransactionArchiveServiceProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransactionArchiveServiceImpl extends AbstractCuscalWebServices
	implements TransactionArchiveService {

	@Override
	public FindTransactionsArchiveResponseType findTransactionsArchive(
		FindTransactionsArchiveRequestType findTransactionsArchiveRequestType) {

		return transactionArchiveService.findTransactionsArchive(
			findTransactionsArchiveRequestType);
	}

	@Override
	public GetTransactionArchiveDetailsResponseType getTransactionArchiveDetails(
		GetTransactionArchiveDetailsRequestType getTransactionArchiveDetailsRequestType) {

		return transactionArchiveService.getTransactionArchiveDetails(
			getTransactionArchiveDetailsRequestType);
	}

	@Autowired
	@Value("${webservice.common.url}")
	protected String commonServicesUrl = null;

	@Autowired
	@Value("${webservice.entity.url}")
	protected String entityServicesUrl = null;

	@Autowired
	@Value("${webservice.transactionArchive.url}")
	protected String transactionArchiveUrl = null;

	@Autowired
	@Value("${webservice.transaction.url}")
	protected String transactionServicesUrl = null;

	private final TransactionArchiveServiceProxy transactionArchiveService = CuscalServiceLocator.getService(
		TransactionArchiveService.class.getName());

}