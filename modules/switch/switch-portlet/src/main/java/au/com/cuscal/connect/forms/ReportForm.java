package au.com.cuscal.connect.forms;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;

public class ReportForm implements Serializable {

	public static final int DEFAULT_PAGE_SIZE = 25;

	public static final int[] PAGE_SIZE_LIST = {10, 25, 50, 100};

	public String getCustomerBin() {
		return customerBin;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * return a collection of PageSizeOption objects to be used on the jsp.
	 * note: the intent is the jsp can use this collection in a <form:select...> but it doesnt work.
	 * this is left here for future integration.
	 * @return
	 */
	public Collection<PageSizeOption> getPageSizeOptions() {
		Collection<PageSizeOption> list = new ArrayList<>();

		for (int pageSize : PAGE_SIZE_LIST) {
			String pageSizeOption = String.valueOf(pageSize);

			PageSizeOption option = new PageSizeOption(
				String.valueOf(pageSize), pageSizeOption);

			list.add(option);
		}

		return list;
	}

	public String getReportName() {
		return reportName;
	}

	public String getToDate() {
		return toDate;
	}

	public void setCustomerBin(String customerBin) {
		this.customerBin = customerBin;
	}

	public void setCustomerName(String customerName) {
		this.customerName = normalise(customerName);
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setPageSize(int pageSize) {

		// only set the page size if its in the list.

		this.pageSize = validPageSize(pageSize) ? pageSize : this.pageSize;
	}

	public void setReportName(String reportName) {
		this.reportName = normalise(reportName);
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	/**
	 * verifies that the given pageSize is valid, ie in the PAGE_SIZE_LIST array.
	 * @param pageSize
	 * @return
	 */
	public boolean validPageSize(int pageSize) {
		for (int i = 0; i < PAGE_SIZE_LIST.length; i++) {
			if (pageSize == PAGE_SIZE_LIST[i]) {
				return true;
			}
		}

		return false;
	}

	public class PageSizeOption {

		public PageSizeOption(String value, String label) {
			this.value = value;
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public String getValue() {
			return value;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public void setValue(String value) {
			this.value = value;
		}

		private String label;
		private String value;

	}

	private String normalise(String s) {
		if ((s == null) ||
			(s.trim(
			).length() == 0)) {

			return "";
		}

		s = s.trim(
		).toUpperCase();

		return s;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 9042731086313324371L;

	private String customerBin;
	private String customerName;
	private String fromDate;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private String reportName;
	private String toDate;

}