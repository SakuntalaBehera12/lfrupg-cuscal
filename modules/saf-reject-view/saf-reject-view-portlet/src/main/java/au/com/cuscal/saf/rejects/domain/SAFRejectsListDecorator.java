//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.saf.rejects.domain;

import java.io.Serializable;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SAFRejectsListDecorator implements Serializable {

	public int getCount() {
		return this.count;
	}

	public Date getDate() {
		return this.date;
	}

	public String getSettlementDate() {
		return this.settlementDate;
	}

	public void setCount(final int count) {
		this.count = count;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public void setSettlementDate(final String settlementDate) {
		this.settlementDate = settlementDate;
	}

	private static Logger logger = LoggerFactory.getLogger(
		SAFRejectsListDecorator.class);
	private static final long serialVersionUID = 1021730317494704011L;

	private int count;
	private Date date;
	private String settlementDate;

}