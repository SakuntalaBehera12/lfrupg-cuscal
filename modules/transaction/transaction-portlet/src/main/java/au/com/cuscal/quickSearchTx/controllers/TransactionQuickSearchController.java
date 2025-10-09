package au.com.cuscal.quickSearchTx.controllers;

import au.com.cuscal.quickSearchTx.services.TransactionQuickSearchService;
import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.forms.TransactionForm;

import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Controller that returns that Transaction Quick Search View
 *
 * @author Rajni Bharara
 *
 */
@Controller("transactionQuickSearchController")
@RequestMapping("VIEW")
public class TransactionQuickSearchController {

	@ActionMapping
	public void findTransactionSearchList(
		@ModelAttribute TransactionForm transactionForm,
		BindingResult bindingResult, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		System.out.println(
			"[transactionQuickSearchController.findTransactionSearchList] In action url ");
		QName qname = new QName(
			"http://liferay.com/events", "forms.TransactionForm");
		transactionForm.setPanBin("12346657576");
		response.setEvent(qname, transactionForm);
		System.out.println(
			"[transactionQuickSearchController.findTransactionSearchList] sending response in event");
		sessionStatus.setComplete();
	}

	/**
	 * Transaction Validator
	 */
	//@Autowired
	//@Qualifier(Constants.TRANSACTION_FORM_VALIDATOR)
	//private TransactionSearchValidator transactionSearchValidator;

	/**
	 * Creates the Transaction Form based
	 *
	 * @return
	 */
	@ModelAttribute(Constants.TRANSACTION_FORM)
	public TransactionForm getCommandObject() {
		return new TransactionForm();
	}

	/**
	 * Constructor that sets up transactionSearchService via DI
	 *
	 * @param transactionSearchService
	 */
	//public TransactionQuickSearchController(TransactionQuickSearchService transactionQuickSearchService) {
	//	this.transactionQuickSearchService = transactionQuickSearchService;
	//}

	/**
	 * Shows the initial view of the Transaction form
	 *
	 * @param response
	 * @return
	 */
	@RenderMapping
	public String showQuickTransactionSearchPage(
		RenderResponse response, RenderRequest request) {

		System.out.println(
			"[transactionQuickSearchController.showQuickTransactionSearchPage] INSIDE THE DEFAULT URL OF RENDER MAPPING  ");

		return Constants.QUICK_SEARCH_PAGE;
	}

	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.TX_SEARCH_RESULT
	)
	public String showTransactionSearchResult(
		RenderResponse response, RenderRequest request) {

		System.out.println(
			"[TransactionQuickSearchController.showTransactionSearchResult] INSIDE THE TX QUICK SEARCH RESULT RENDER MAPPING  ");

		return Constants.TX_SEARCH_PAGE;
	}

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(
		TransactionQuickSearchController.class);

	/**
	 * Service used to save data
	 */
	@Autowired
	@Qualifier(Constants.TRANSACTION_QUICK_SERVICE)
	private TransactionQuickSearchService transactionQuickSearchService;

}