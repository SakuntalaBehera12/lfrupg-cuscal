package au.com.cuscal.cards.commons;

import au.com.cuscal.cards.domain.Cards;

import java.io.Serializable;

import java.util.List;

import org.displaytag.properties.SortOrderEnum;

public class CardsListSearchResult implements Serializable {

	public List<Cards> getCards() {
		return cards;
	}

	public int getPageNum() {
		return pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public String getSortName() {
		return sortName;
	}

	public SortOrderEnum getSortOrder() {
		return sortOrder;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setCards(List<Cards> cards) {
		this.cards = cards;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public void setSortOrder(SortOrderEnum sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -2732483893580888756L;

	private List<Cards> cards;
	private int pageNum = Constants.DEFAULT_PAGE_NUMBER;
	private int pageSize = Constants.DEFAULT_PAGE_SIZE;
	private String sortName;
	private SortOrderEnum sortOrder;
	private long totalCount;

}