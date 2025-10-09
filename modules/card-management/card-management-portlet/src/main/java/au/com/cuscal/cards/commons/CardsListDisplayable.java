package au.com.cuscal.cards.commons;

import au.com.cuscal.cards.domain.Cards;

import java.io.Serializable;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CardsListDisplayable implements PaginatedList, Serializable {

	public static CardsListDisplayable wrap(
		CardsListSearchResult cardsListSearchResult) {

		CardsListDisplayable displayable = new CardsListDisplayable();

		displayable.cardsListSearchResult = cardsListSearchResult;

		return displayable;
	}

	@Override
	public int getFullListSize() {

		// TODO Auto-generated method stub

		return (int)cardsListSearchResult.getTotalCount();
	}

	@Override
	public List<Cards> getList() {

		// TODO Auto-generated method stub

		return cardsListSearchResult.getCards();
	}

	@Override
	public int getObjectsPerPage() {

		// TODO Auto-generated method stub

		return cardsListSearchResult.getPageSize();
	}

	@Override
	public int getPageNumber() {

		// TODO Auto-generated method stub

		return cardsListSearchResult.getPageNum();
	}

	@Override
	public String getSearchId() {

		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public String getSortCriterion() {

		// TODO Auto-generated method stub

		logger.info(
			"The sort Criterion is in CardsListDisplayable object is  " +
				cardsListSearchResult.getSortName());

		return cardsListSearchResult.getSortName();
	}

	@Override
	public SortOrderEnum getSortDirection() {

		// TODO Auto-generated method stub

		logger.info(
			"The sort direction is in CardsListDisplayable object is  " +
				cardsListSearchResult.getSortOrder());

		return cardsListSearchResult.getSortOrder();
	}

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(
		CardsListDisplayable.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private CardsListSearchResult cardsListSearchResult;

}