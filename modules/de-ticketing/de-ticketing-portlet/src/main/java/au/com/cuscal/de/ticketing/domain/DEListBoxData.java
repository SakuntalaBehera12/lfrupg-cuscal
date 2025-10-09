//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.domain;

import java.io.Serializable;

import java.util.HashMap;

public class DEListBoxData implements Serializable {

	public HashMap<Long, String> getClaimReasonMap() {
		return this.claimReasonMap;
	}

	public HashMap<Long, String> getClaimReplyMap() {
		return this.claimReplyMap;
	}

	public HashMap<Long, String> getGeneralYesNoMap() {
		return this.generalYesNoMap;
	}

	public HashMap<Long, String> getIntendedAccountDetailsMap() {
		return this.intendedAccountDetailsMap;
	}

	public HashMap<Long, String> getOfiClaimResultMap() {
		return this.ofiClaimResultMap;
	}

	public HashMap<Long, String> getOfiMistakenCheckboxResultMap() {
		return this.ofiMistakenCheckboxResultMap;
	}

	public HashMap<Long, String> getOfiMistakenResultMap() {
		return this.ofiMistakenResultMap;
	}

	public HashMap<Long, String> getOfiRecallResultMap() {
		return this.ofiRecallResultMap;
	}

	public HashMap<Long, String> getOutcomeMap() {
		return this.outcomeMap;
	}

	public HashMap<Long, String> getRequestForMap() {
		return this.requestForMap;
	}

	public HashMap<Long, String> getTimeframeMap() {
		return this.timeframeMap;
	}

	public HashMap<Long, String> getTransactionCodeMap() {
		return this.transactionCodeMap;
	}

	public void setClaimReasonMap(final HashMap<Long, String> claimReasonMap) {
		this.claimReasonMap = claimReasonMap;
	}

	public void setClaimReplyMap(final HashMap<Long, String> claimReplyMap) {
		this.claimReplyMap = claimReplyMap;
	}

	public void setGeneralYesNoMap(
		final HashMap<Long, String> generalYesNoMap) {

		this.generalYesNoMap = generalYesNoMap;
	}

	public void setIntendedAccountDetailsMap(
		final HashMap<Long, String> intendedAccountDetailsMap) {

		this.intendedAccountDetailsMap = intendedAccountDetailsMap;
	}

	public void setOfiClaimResultMap(
		final HashMap<Long, String> ofiClaimResultMap) {

		this.ofiClaimResultMap = ofiClaimResultMap;
	}

	public void setOfiMistakenCheckboxResultMap(
		final HashMap<Long, String> ofiMistakenCheckboxResultMap) {

		this.ofiMistakenCheckboxResultMap = ofiMistakenCheckboxResultMap;
	}

	public void setOfiMistakenResultMap(
		final HashMap<Long, String> ofiMistakenResultMap) {

		this.ofiMistakenResultMap = ofiMistakenResultMap;
	}

	public void setOfiRecallResultMap(
		final HashMap<Long, String> ofiRecallResultMap) {

		this.ofiRecallResultMap = ofiRecallResultMap;
	}

	public void setOutcomeMap(final HashMap<Long, String> outcomeMap) {
		this.outcomeMap = outcomeMap;
	}

	public void setRequestForMap(final HashMap<Long, String> requestForMap) {
		this.requestForMap = requestForMap;
	}

	public void setTimeframeMap(final HashMap<Long, String> timeframeMap) {
		this.timeframeMap = timeframeMap;
	}

	public void setTransactionCodeMap(
		final HashMap<Long, String> transactionCodeMap) {

		this.transactionCodeMap = transactionCodeMap;
	}

	private static final long serialVersionUID = 1L;

	private HashMap<Long, String> claimReasonMap;
	private HashMap<Long, String> claimReplyMap;
	private HashMap<Long, String> generalYesNoMap;
	private HashMap<Long, String> intendedAccountDetailsMap;
	private HashMap<Long, String> ofiClaimResultMap;
	private HashMap<Long, String> ofiMistakenCheckboxResultMap;
	private HashMap<Long, String> ofiMistakenResultMap;
	private HashMap<Long, String> ofiRecallResultMap;
	private HashMap<Long, String> outcomeMap;
	private HashMap<Long, String> requestForMap;
	private HashMap<Long, String> timeframeMap;
	private HashMap<Long, String> transactionCodeMap;

}