package au.com.cuscal.transactions.domain;

import java.io.Serializable;

import java.util.Date;

public class TransactionList implements Serializable {

	public String getAdPank() {
		return adPank;
	}

	public String getAdTransactionDetailK() {
		return adTransactionDetailK;
	}

	public float getAmount() {
		return amount;
	}

	public String getBusniessDate() {
		return busniessDate;
	}

	public String getCardAcceptorId() {
		return cardAcceptorId;
	}

	public float getCardHolderBillAmount() {
		return cardHolderBillAmount;
	}

	public Date getCudTransactionDate() {
		return cudTransactionDate;
	}

	public String getCudTranscationTime() {
		return cudTranscationTime;
	}

	public String getCurrencyCodeAcq() {
		return this.currencyCodeAcq;
	}

	public String getCurrencyCodeIss() {
		return this.currencyCodeIss;
	}

	public int getCurrencyFormatVal() {
		return currencyFormatVal;
	}

	public String getDataSource() {
		return dataSource;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public String getDescription() {
		return description;
	}

	public String getEtlProcessEndDate() {
		return etlProcessEndDate;
	}

	public String getExternalTransactionId() {
		return externalTransactionId;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public String getFunctionCodeDescription() {
		return functionCodeDescription;
	}

	public String getMessageDescription() {
		return messageDescription;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getPan() {
		return pan;
	}

	//cud
	public String getResponseCode() {
		return responseCode;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public String getSortableMsgType() {
		return sortableMsgType;
	}

	public Date getSwitchDateTime() {
		return switchDateTime;
	}

	public String getSystemTrace() {
		return systemTrace;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public String getTerminalLocation() {
		return terminalLocation;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getWebServiceMsg() {
		return webServiceMsg;
	}

	public boolean isMoreRecAvail() {
		return isMoreRecAvail;
	}

	public void setAdPank(String adPank) {
		this.adPank = adPank;
	}

	public void setAdTransactionDetailK(String adTransactionDetailK) {
		this.adTransactionDetailK = adTransactionDetailK;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public void setBusniessDate(String busniessDate) {
		this.busniessDate = busniessDate;
	}

	public void setCardAcceptorId(String cardAcceptorId) {
		this.cardAcceptorId = cardAcceptorId;
	}

	public void setCardHolderBillAmount(float cardHolderBillAmount) {
		this.cardHolderBillAmount = cardHolderBillAmount;
	}

	public void setCudTransactionDate(Date cudTransactionDate) {
		this.cudTransactionDate = cudTransactionDate;
	}

	public void setCudTranscationTime(String cudTranscationTime) {
		this.cudTranscationTime = cudTranscationTime;
	}

	public void setCurrencyCodeAcq(String currencyCode) {
		this.currencyCodeAcq = currencyCode;
	}

	public void setCurrencyCodeIss(String currencyCode) {
		this.currencyCodeIss = currencyCode;
	}

	public void setCurrencyFormatVal(int currencyFormatVal) {
		this.currencyFormatVal = currencyFormatVal;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEtlProcessEndDate(String etlProcessEndDate) {
		this.etlProcessEndDate = etlProcessEndDate;
	}

	public void setExternalTransactionId(String externalTransactionId) {
		this.externalTransactionId = externalTransactionId;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public void setFunctionCodeDescription(String functionCodeDescription) {
		this.functionCodeDescription = functionCodeDescription;
	}

	public void setMessageDescription(String messageDescription) {
		this.messageDescription = messageDescription;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public void setMoreRecAvail(boolean isMoreRecAvail) {
		this.isMoreRecAvail = isMoreRecAvail;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public void setSortableMsgType(String sortableMsgType) {
		this.sortableMsgType = sortableMsgType;
	}

	public void setSwitchDateTime(Date switchDateTime) {
		this.switchDateTime = switchDateTime;
	}

	public void setSystemTrace(String systemTrace) {
		this.systemTrace = systemTrace;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public void setTerminalLocation(String terminalLocation) {
		this.terminalLocation = terminalLocation;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setWebServiceMsg(String webServiceMsg) {
		this.webServiceMsg = webServiceMsg;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -1659252851231503070L;

	private String adPank;
	private String adTransactionDetailK;
	private float amount;
	private String busniessDate;
	private String cardAcceptorId;
	private float cardHolderBillAmount;
	private Date cudTransactionDate;
	private String cudTranscationTime;
	private String currencyCodeAcq;
	private String currencyCodeIss;
	private int currencyFormatVal;
	private String dataSource;
	private Date dateTime;
	private String description;
	private String etlProcessEndDate;
	private String externalTransactionId;
	private String functionCode;
	private String functionCodeDescription;
	private boolean isMoreRecAvail;
	private String messageDescription;
	private String messageType;
	private String pan;
	private String responseCode;
	private String responseDescription;
	private String sortableMsgType;
	private Date switchDateTime;
	private String systemTrace;
	private String terminalId;
	private String terminalLocation;
	private String transactionId;
	private String webServiceMsg;

}