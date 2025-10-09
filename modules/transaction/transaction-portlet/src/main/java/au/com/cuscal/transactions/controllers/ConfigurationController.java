package au.com.cuscal.transactions.controllers;

import au.com.cuscal.transactions.commons.Constants;
import au.com.cuscal.transactions.forms.PinChangeSearchForm;
import au.com.cuscal.transactions.forms.TransactionForm;
import au.com.cuscal.transactions.services.TransactionSearchService;

import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Controller that returns that CUD Transaction Search View
 *
 * @author Rajni Bharara
 *
 */
@Controller("configurationController")
@RequestMapping("VIEW")
@SessionAttributes(types = ConfigurationController.class)
public class ConfigurationController {

	/**
	 * Find the current portlet title
	 *
	 * @param RenderRequest
	 * @return String
	 */
	public String findPortletTitle(RenderRequest renderRequest) {
		String portletId = (String)renderRequest.getAttribute(
			WebKeys.PORTLET_ID);
		logger.debug("findPortletTitle - portletId is   " + portletId);
		String portletTitle = PortletLocalServiceUtil.getPortletById(
			portletId
		).getPortletInfo(
		).getTitle();

		return portletTitle;
	}

	/**
	 * Creates the Transaction Form based
	 *
	 * @return TransactionForm
	 */
	@ModelAttribute(Constants.TRANSACTION_FORM)
	public TransactionForm getCommandObject() {
		TransactionForm transactionForm =
			transactionSearchService.createTransactionFormObject();
		String[] allMessageCodeOptions =
			transactionSearchService.getAllMessageCodeTypeForUIDropDown();

		if (null != allMessageCodeOptions) {
			transactionForm.setMsgTypeOptionDisplay(allMessageCodeOptions);
		}
		else {
			transactionForm.setMsgTypeOptionDisplay(null);
		}

		return transactionForm;
	}

	/**
	 * Creates the PinChangeSearch  Form based
	 *
	 * @return PinChangeSearchForm
	 */
	@ModelAttribute(Constants.PIN_CHANGE_SEARCH_FORM)
	public PinChangeSearchForm getPinChangeCommandObject() {
		logger.debug(
			"getPinChangeCommandObject - Inside the PinChangeCommandObject");

		PinChangeSearchForm pinChangeForm =
			transactionSearchService.createPinChangeSearchFormObject();

		return pinChangeForm;
	}

	/**
	 * Shows the initial view of the Transaction form
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @param TransactionForm
	 * @return string
	 */
	@RenderMapping
	public String showTransactionSearchPage(
		RenderResponse response, RenderRequest request,
		@ModelAttribute TransactionForm transactionForm) {

		logger.debug(
			"[ConfigurationController] showTransactionSearchPage - start  ");
		String resultPage = null;
		String portletTitle = findPortletTitle(request);

		logger.debug("showTransactionSearchPage - title is  " + portletTitle);

		if (portletTitle.equalsIgnoreCase(Constants.TRANSACTION_SEARCH)) {
			transactionForm.setSearchView(Constants.QUICK);
			resultPage = Constants.TX_SEARCH_PAGE;
		}
		else if (portletTitle.equalsIgnoreCase(
					Constants.CUD_TRANSACTION_SEARCH)) {

			resultPage = Constants.CUD_TX_SEARCH_PAGE;
		}
		else if (portletTitle.equalsIgnoreCase(
					Constants.PIN_CHANGE_TRANSACTION_SEARCH)) {

			resultPage = Constants.PIN_CHANGE_SEARCH_PAGE;
		}

		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(
			request);

		HttpSession session = servletRequest.getSession();

		session.removeAttribute(Constants.TX_LIST);
		session.removeAttribute(Constants.CUD_LIST);
		session.removeAttribute(Constants.PIN_CHANGE_SEARCH_RESULTS_LIST);
		logger.debug(
			"[ConfigurationController] showTransactionSearchPage - end  ");

		return resultPage;
	}

	/**
	 * Shows the Error view of the Transaction or Cud form
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return string - error page
	 */
	@RenderMapping(
		params = Constants.TX_RENDER + Constants.EQUALS + Constants.TX_ERROR_PAGE
	)
	public String showTxErrorPage(
		RenderResponse response, RenderRequest request) {

		logger.debug(" showTxErrorPage - start ");
		request.setAttribute(
			Constants.NO_DATA_FOUND_MSG, Constants.TX_NODATA_FOUND);
		logger.debug(" showTxErrorPage - end ");

		return Constants.TX_ERROR;
	}

	/**
	 * Logger object
	 */
	private static Logger logger = Logger.getLogger(
		ConfigurationController.class);

	/**
	 * Transaction Service used to fetch data
	 */
	@Autowired
	@Qualifier(Constants.TRANSACTION_SERVICE)
	private TransactionSearchService transactionSearchService;

}