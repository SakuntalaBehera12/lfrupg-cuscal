package au.com.cuscal.cards.services;

import au.com.cuscal.cards.domain.AccountInformation;
import au.com.cuscal.cards.domain.CardInformation;
import au.com.cuscal.cards.domain.CardLimits;
import au.com.cuscal.cards.domain.Cards;
import au.com.cuscal.cards.domain.TokenSearchResult;
import au.com.cuscal.cards.forms.CardSearchForm;
import au.com.cuscal.cards.services.client.LocalCardServiceImpl;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.transaction.Customer;
import au.com.cuscal.framework.webservices.transaction.SearchHeader;

import java.math.BigInteger;

import java.util.List;

/**
 * Service interface
 *
 * @author Rajni Bharara
 *
 */
public interface CardsSearchService {

	public boolean activateToken(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public CardSearchForm createCardSearchForm();

	public List<AccountInformation> getCardAccountInformationOnDetails(
			String cardId, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public CardInformation getCardInformationOnCardDetails(
			String cardId, SearchHeader searchHeader, List<String> groups,
			PortletContext portletContext)
		throws Exception;

	public CardLimits getCardLimitsOnCardDetails(
			String cardId, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public List<Cards> getCardListOnsearch(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public LocalCardServiceImpl getCardService();

	public List<Customer> getCustomerAccessView(
		long portalOrgId, PortletContext portletContext);

	public String getOrgShortName(String liferayOrgId);

	public String[] getShowCardLimitsOrgs();

	public boolean isShowCardControlHistory();

	public boolean isShowCardLimits();

	public boolean isShowCardLimitsHistory();

	public boolean isShowLimits();

	public boolean isShowMccControlHistory();

	public boolean isShowMccControls();

	public boolean isShowMobileDevices();

	public void logPoolStatistics();

	public void purgePools();

	public TokenSearchResult searchTokens(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public String updateCardStatusCode(
			BigInteger cardId, String cardStatus, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

	public boolean updateToken(
			CardSearchForm cardSearchForm, SearchHeader searchHeader,
			PortletContext portletContext)
		throws Exception;

}