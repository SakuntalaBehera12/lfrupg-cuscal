//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common;

import com.cuscal.common.exceptions.IteratorException;

import java.util.ArrayList;

public interface ValueListIterator {

	int getSize() throws IteratorException;

	int setPageSize(final int p0);

	ArrayList previousPage() throws IteratorException;

	ArrayList nextPage() throws IteratorException;

	void resetIndex();

	boolean hasMoreElements();

	boolean hasPreviousElements();

}