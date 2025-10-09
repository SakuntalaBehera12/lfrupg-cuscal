//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common;

import com.cuscal.common.exceptions.IteratorException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class ValueListHandler implements ValueListIterator {

	public Collection getList() {
		return this.list;
	}

	public ArrayList getPage(int currentIndex, int pageSize) {
		if (currentIndex > this.list.size()) {
			currentIndex = this.list.size();
		}
		else if (currentIndex < 0) {
			currentIndex = 0;
		}

		if (pageSize <= 0) {
			pageSize = 1;
		}
		else if (pageSize > this.list.size()) {
			pageSize = this.list.size();
		}

		final ArrayList page = new ArrayList();
		final ListIterator current = this.list.listIterator(currentIndex);

		for (int i = 0; current.hasNext() && (i < pageSize); ++i) {
			page.add(current.next());
		}

		return page;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public int getSize() throws IteratorException {
		int size = 0;

		if (this.list != null) {
			size = this.list.size();

			return size;
		}

		throw new IteratorException("Unable to get size of list");
	}

	public boolean hasMoreElements() {
		if (this.currentIndex < this.arraySize) {
			return true;
		}

		return false;
	}

	public boolean hasPreviousElements() {
		if (this.previousStart >= 0) {
			return true;
		}

		return false;
	}

	public ArrayList nextPage() throws IteratorException {
		try {
			final int newIndex = this.currentIndex;
			this.previousStart = this.currentIndex;
			this.currentIndex += this.pageSize;

			if (this.currentIndex > this.arraySize) {
				this.currentIndex = this.arraySize;
			}

			return this.getPage(newIndex, this.pageSize);
		}
		catch (Exception ex) {
			ex.printStackTrace();

			throw new IteratorException(
				"Unable to find next page" + ex.getMessage());
		}
	}

	public ArrayList previousPage() throws IteratorException {
		try {
			this.currentIndex = this.previousStart - this.pageSize;

			if (this.currentIndex < 0) {
				this.currentIndex = 0;
			}

			final int newIndex = this.currentIndex;
			this.previousStart = this.currentIndex;

			if (this.previousStart == 0) {
				this.previousStart = -1;
			}

			this.currentIndex += this.pageSize;

			if (this.currentIndex > this.arraySize) {
				this.currentIndex = this.arraySize;
			}

			return this.getPage(newIndex, this.pageSize);
		}
		catch (Exception ex) {
			ex.printStackTrace();

			throw new IteratorException(
				"Unable to find previous page" + ex.getMessage());
		}
	}

	public void resetIndex() {
		this.currentIndex = 0;
		this.previousStart = 0;
	}

	public int setPageSize(final int numberOfElements) {
		final int oldSize = this.pageSize;

		if (numberOfElements < 1) {
			this.pageSize = 1;
		}
		else if (numberOfElements > this.arraySize) {
			this.pageSize = this.arraySize;
		}
		else {
			this.pageSize = numberOfElements;
		}

		return oldSize;
	}

	protected void setList(final List list) throws Exception {
		this.list = list;

		if (list == null) {
			throw new Exception("no items selected");
		}

		this.arraySize = list.size();
	}

	protected List list;

	private int arraySize;
	private int currentIndex;
	private int pageSize;
	private int previousStart;

}