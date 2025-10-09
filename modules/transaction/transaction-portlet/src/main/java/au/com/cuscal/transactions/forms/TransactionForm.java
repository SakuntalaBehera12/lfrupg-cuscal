package au.com.cuscal.transactions.forms;

import au.com.cuscal.framework.cards.CardUtil;

import java.io.Serializable;

public class TransactionForm implements Serializable {

	public String getAmount() {
		return amount;
	}

	public String getAmountType() {
		return amountType;
	}

	public String getConditions() {
		return conditions;
	}

	public String getCurrTerminalId() {
		return currTerminalId;
	}

	public String getCuscalId() {
		return cuscalId;
	}

	public int getDateDiff() {
		return dateDiff;
	}

	public String getDateType() {
		return dateType;
	}

	public String getEndDate() {
		return endDate;
	}

	public String[] getEndHourDisplay() {
		return endHourDisplay;
	}

	public String[] getEndMinuteDisplay() {
		return endMinuteDisplay;
	}

	public String getEndTimeHr() {
		return endTimeHr;
	}

	public String getEndTimeMin() {
		return endTimeMin;
	}

	public String getExternalTransactionId() {
		return externalTransactionId;
	}

	public String getMaskedPanBin() {
		if (isPan) {
			return CardUtil.getInstance(
			).mask(
				panBin, 'X'
			);
		}

		return panBin;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public String getMsgId() {
		return msgId;
	}

	public String[] getMsgTypeOptionDisplay() {
		return msgTypeOptionDisplay;
	}

	public String getNormalisedPan() {
		return normalisedPan;
	}

	public String getPanBin() {
		return panBin;
	}

	public String getPanOrTid() {
		return panOrTid;
	}

	public String getRespId() {
		return respId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public String getRespToken() {
		return respToken;
	}

	public String getRrn() {
		return rrn;
	}

	public String getSearchView() {
		return searchView;
	}

	public String getStan() {
		return stan;
	}

	public String getStartDate() {
		return startDate;
	}

	public String[] getStartHourDisplay() {
		return startHourDisplay;
	}

	public String[] getStartMinuteDisplay() {
		return startMinuteDisplay;
	}

	public String getStartTimeHr() {
		return startTimeHr;
	}

	// 2FA fields

	public String getStartTimeMin() {
		return startTimeMin;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public String[] getTransactionCodes() {
		return transactionCodes;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserOrg() {
		return userOrg;
	}

	public boolean isBinTerminalOrCardAcceptor() {
		return isBinTerminalOrCardAcceptor;
	}

	public boolean isLocalAmount() {
		return isLocalAmount;
	}

	public boolean isLocalDate() {
		return isLocalDate;
	}

	public boolean isMaskedPan() {
		return isMaskedPan;
	}

	public boolean isPan() {
		return isPan;
	}

	public boolean isStanOrRrn() {
		return isStanOrRrn;
	}

	public boolean isTermimalId() {
		return isTermimalId;
	}

	public boolean isVisaIdOrCuscal() {
		return isVisaOrCuscal;
	}

	public void resetForm() {
		terminalId = null;
		startDate = "";
		endDate = "";
		panBin = null;
		normalisedPan = null;
		merchantId = null;
		amount = null;
		responseCode = null;
		messageCode = null;
		startTimeHr = "00";
		startTimeMin = "00";
		endTimeHr = "24";
		endTimeMin = "00";
		conditions = "=";
		externalTransactionId = null;
		//amountType = "transactionAmount";
		amountType = "billingAmount";
		//dateType = "system";
		dateType = "local";
		cuscalId = "";
		stan = "";
		rrn = "";
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public void setBinTerminalOrCardAcceptor(
		boolean isBinTerminalOrCardAcceptor) {

		this.isBinTerminalOrCardAcceptor = isBinTerminalOrCardAcceptor;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public void setCurrTerminalId(String currTerminalId) {
		this.currTerminalId = currTerminalId;
	}

	public void setCuscalId(String cuscalId) {
		this.cuscalId = cuscalId;
	}

	public void setDateDiff(int dateDiff) {
		this.dateDiff = dateDiff;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setEndHourDisplay(String[] endHourDisplay) {
		this.endHourDisplay = endHourDisplay;
	}

	public void setEndMinuteDisplay(String[] endMinuteDisplay) {
		this.endMinuteDisplay = endMinuteDisplay;
	}

	public void setEndTimeHr(String endTimeHr) {
		this.endTimeHr = endTimeHr;
	}

	public void setEndTimeMin(String endTimeMin) {
		this.endTimeMin = endTimeMin;
	}

	public void setExternalTransactionId(String externalTransactionId) {
		this.externalTransactionId = externalTransactionId;
	}

	public void setLocalAmount(boolean isLocalAmount) {
		this.isLocalAmount = isLocalAmount;
	}

	public void setLocalDate(boolean isLocalDate) {
		this.isLocalDate = isLocalDate;
	}

	public void setMaskedPan(boolean isMaskedPan) {
		this.isMaskedPan = isMaskedPan;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public void setMsgTypeOptionDisplay(String[] msgTypeOptionDisplay) {
		this.msgTypeOptionDisplay = msgTypeOptionDisplay;
	}

	public void setNormalisedPan(String normalisedPan) {
		this.normalisedPan = normalisedPan;
	}

	public void setPan(boolean isPan) {
		this.isPan = isPan;
	}

	public void setPanBin(String panBin) {
		this.panBin = panBin;
	}

	public void setPanOrTid(String panOrTid) {
		this.panOrTid = panOrTid;
	}

	public void setRespId(String respId) {
		this.respId = respId;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public void setRespToken(String respToken) {
		this.respToken = respToken;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public void setSearchView(String searchView) {
		this.searchView = searchView;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public void setStanOrRrn(boolean isStanOrRrn) {
		this.isStanOrRrn = isStanOrRrn;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setStartHourDisplay(String[] startHourDisplay) {
		this.startHourDisplay = startHourDisplay;
	}

	public void setStartMinuteDisplay(String[] startMinuteDisplay) {
		this.startMinuteDisplay = startMinuteDisplay;
	}

	public void setStartTimeHr(String startTimeHr) {
		this.startTimeHr = startTimeHr;
	}

	public void setStartTimeMin(String startTimeMin) {
		this.startTimeMin = startTimeMin;
	}

	public void setTermimalId(boolean isTermimalId) {
		this.isTermimalId = isTermimalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public void setTransactionCodes(String[] transactionCodes) {
		this.transactionCodes = transactionCodes;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserOrg(String userOrg) {
		this.userOrg = userOrg;
	}

	public void setVisaIdOrCuscal(boolean isVisaIdOrCuscal) {
		this.isVisaOrCuscal = isVisaIdOrCuscal;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 9042731086313324371L;

	private String amount;
	private String amountType;
	private String conditions;
	private String currTerminalId;
	private String cuscalId;
	private int dateDiff;
	private String dateType;
	private String endDate;
	private String[] endHourDisplay;
	private String[] endMinuteDisplay;
	private String endTimeHr;
	private String endTimeMin;
	private String externalTransactionId;
	private boolean isBinTerminalOrCardAcceptor;
	private boolean isLocalAmount;
	private boolean isLocalDate;
	private boolean isMaskedPan;
	private boolean isPan;
	private boolean isStanOrRrn;
	private boolean isTermimalId;
	private boolean isVisaOrCuscal;
	private String merchantId;
	private String messageCode;
	private String msgId;
	private String[] msgTypeOptionDisplay;
	private String normalisedPan;
	private String panBin;
	private String panOrTid;
	private String respId;
	private String responseCode;
	private String respToken;
	private String rrn;
	private String searchView;
	private String stan;
	private String startDate;
	private String[] startHourDisplay;
	private String[] startMinuteDisplay;
	private String startTimeHr;
	private String startTimeMin;
	private String terminalId;
	private String[] transactionCodes;
	private String userEmail;
	private String userId;
	private String userName;
	private String userOrg;

}