package au.com.cuscal.transactions.domain;

import au.com.cuscal.framework.webservices.transaction.CodeDescription;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;

/**
 * MVC Model class that represents an TransactionDetail.
 *
 * @author Rajni
 *
 */
@JsonAutoDetect
public class TransactionDetail implements Serializable {

	public String getAdPank() {
		return adPank;
	}

	public String getAdTransactionDetailK() {
		return adTransactionDetailK;
	}

	public String getBusDate() {
		return busDate;
	}

	public String getCardControlRejectCode() {
		return cardControlRejectCode;
	}

	public String getCentralSiteBusinessDate() {
		return centralSiteBusinessDate;
	}

	public String getChipCardPresent() {
		return chipCardPresent;
	}

	public String getCode() {
		return code;
	}

	public ContactInformation getContactInformation() {
		return contactInformation;
	}

	public String getCustomData() {
		return customData;
	}

	public String getDataSrc() {
		return dataSrc;
	}

	public String getDescription() {
		return description;
	}

	public String getElectronicCommerceIndciator() {
		return electronicCommerceIndciator;
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

	//cud pk
	public String getFunctionCodeDescription() {
		return functionCodeDescription;
	}

	public String getIsNext() {
		return isNext;
	}

	public String getIsPrev() {
		return isPrev;
	}

	//Only used for the PIN Change History search.
	public MemberInformation getMemberInformation() {
		return memberInformation;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getMessageTypeCode() {
		return messageTypeCode;
	}

	public int getNextRowNum() {
		return nextRowNum;
	}

	public String getOriginIchName() {
		return originIchName;
	}

	public String getPinChangeOperatorId() {
		return pinChangeOperatorId;
	}

	public String getPinChangeSupervisorId() {
		return pinChangeSupervisorId;
	}

	public CodeDescription getPosCondition() {
		return posCondition;
	}

	public CodeDescription getPosEntryMode() {
		return posEntryMode;
	}

	public int getPrevRowNum() {
		return prevRowNum;
	}

	public ReasonData getReasonData() {
		return reasonData;
	}

	public RequestDetails getRequestDetails() {
		return requestDetails;
	}

	public ResponseDetails getResponseDetails() {
		return responseDetails;
	}

	public String getTrack2DataServiceCode() {
		return track2DataServiceCode;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getTransactionQualifierId() {
		return transactionQualifierId;
	}

	public String getUCAFCollectionIndicator() {
		return UCAFCollectionIndicator;
	}

	public String getUCAFCollectionIndicatorDescription() {
		return UCAFCollectionIndicatorDescription;
	}

	public String getVisaArn() {
		return visaArn;
	}

	public String getVisaOrAtmPos() {
		return visaOrAtmPos;
	}

	public String getVisaType() {
		return visaType;
	}

	public boolean isAccessibleIssuer() {
		return isAccessibleIssuer;
	}

	public boolean isDeclined() {
		return isDeclined;
	}

	public boolean isHsmUp() {
		return isHsmUp;
	}

	public boolean isReversal() {
		return isReversal;
	}

	public boolean isUserStepUp() {
		return isUserStepUp;
	}

	public boolean isValidServiceRequest() {
		return isValidServiceRequest;
	}

	public boolean isVisaAtm() {
		return isVisaAtm;
	}

	public void setAccessibleIssuer(boolean isAccessibleIssuer) {
		this.isAccessibleIssuer = isAccessibleIssuer;
	}

	public void setAdPank(String adPank) {
		this.adPank = adPank;
	}

	public void setAdTransactionDetailK(String adTransactionDetailK) {
		this.adTransactionDetailK = adTransactionDetailK;
	}

	public void setBusDate(String busDate) {
		this.busDate = busDate;
	}

	public void setCardControlRejectCode(String cardControlRejectCode) {
		this.cardControlRejectCode = cardControlRejectCode;
	}

	public void setCentralSiteBusinessDate(String centralSiteBusinessDate) {
		this.centralSiteBusinessDate = centralSiteBusinessDate;
	}

	public void setChipCardPresent(String chipCardPresent) {
		this.chipCardPresent = chipCardPresent;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setContactInformation(ContactInformation contactInformation) {
		this.contactInformation = contactInformation;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

	public void setDataSrc(String dataSrc) {
		this.dataSrc = dataSrc;
	}

	public void setDeclined(boolean isDeclined) {
		this.isDeclined = isDeclined;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setElectronicCommerceIndciator(
		String electronicCommerceIndciator) {

		this.electronicCommerceIndciator = electronicCommerceIndciator;
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

	public void setHsmUp(boolean isHsmUp) {
		this.isHsmUp = isHsmUp;
	}

	public void setIsNext(String isNext) {
		this.isNext = isNext;
	}

	public void setIsPrev(String isPrev) {
		this.isPrev = isPrev;
	}

	public void setMemberInformation(MemberInformation memberInformation) {
		this.memberInformation = memberInformation;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}

	public void setNextRowNum(int nextRowNum) {
		this.nextRowNum = nextRowNum;
	}

	public void setOriginIchName(String originIchName) {
		this.originIchName = originIchName;
	}

	public void setPinChangeOperatorId(String pinChangeOperatorId) {
		this.pinChangeOperatorId = pinChangeOperatorId;
	}

	public void setPinChangeSupervisorId(String pinChangeSupervisorId) {
		this.pinChangeSupervisorId = pinChangeSupervisorId;
	}

	public void setPosCondition(CodeDescription posCondition) {
		this.posCondition = posCondition;
	}

	public void setPosEntryMode(CodeDescription posEntryMode) {
		this.posEntryMode = posEntryMode;
	}

	public void setPrevRowNum(int prevRowNum) {
		this.prevRowNum = prevRowNum;
	}

	public void setReasonData(ReasonData reasonData) {
		this.reasonData = reasonData;
	}

	public void setRequestDetails(RequestDetails requestDetails) {
		this.requestDetails = requestDetails;
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.responseDetails = responseDetails;
	}

	public void setReversal(boolean isReversal) {
		this.isReversal = isReversal;
	}

	public void setTrack2DataServiceCode(String track2DataServiceCode) {
		this.track2DataServiceCode = track2DataServiceCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setTransactionQualifierId(String transactionQualifierId) {
		this.transactionQualifierId = transactionQualifierId;
	}

	public void setUCAFCollectionIndicator(String uCAFCollectionIndicator) {
		UCAFCollectionIndicator = uCAFCollectionIndicator;
	}

	public void setUCAFCollectionIndicatorDescription(
		String uCAFCollectionIndicatorDescription) {

		UCAFCollectionIndicatorDescription = uCAFCollectionIndicatorDescription;
	}

	public void setUserStepUp(boolean isUserStepUp) {
		this.isUserStepUp = isUserStepUp;
	}

	public void setValidServiceRequest(boolean isValidServiceRequest) {
		this.isValidServiceRequest = isValidServiceRequest;
	}

	public void setVisaArn(String visaArn) {
		this.visaArn = visaArn;
	}

	public void setVisaAtm(boolean isVisaAtm) {
		this.isVisaAtm = isVisaAtm;
	}

	public void setVisaOrAtmPos(String visaOrAtmPos) {
		this.visaOrAtmPos = visaOrAtmPos;
	}

	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}

	@Override
	public String toString() {
		return "TransactionDetail [code=" + code + ", description=" +
			description + ", messageType=" + messageType +
				", messageTypeCode=" + messageTypeCode + ", originIchName=" +
					originIchName + ", transactionId=" + transactionId +
						", busDate=" + busDate + ", dataSrc=" + dataSrc +
							", isUserStepUp=" + isUserStepUp + ", isHsmUp=" +
								isHsmUp + ", prevRowNum=" + prevRowNum +
									", nextRowNum=" + nextRowNum + ", isPrev=" +
										isPrev + ", isNext=" + isNext +
											", adPank=" + adPank +
												", etlProcessEndDate=" +
													etlProcessEndDate +
														", adTransactionDetailK=" +
															adTransactionDetailK +
																", pinChangeOperatorId=" +
																	pinChangeOperatorId +
																		", pinChangeSupervisorId=" +
																			pinChangeSupervisorId +
																				", externalTransactionId=" +
																					externalTransactionId +
																						", visaArn=" +
																							visaArn +
																								", visaOrAtmPos=" +
																									visaOrAtmPos +
																										", isVisaAtm=" +
																											isVisaAtm +
																												", isDeclined=" +
																													isDeclined +
																														", isReversal=" +
																															isReversal +
																																", isValidServiceRequest=" +
																																	isValidServiceRequest +
																																		", isAccessibleIssuer=" +
																																			isAccessibleIssuer +
																																				", visaType=" +
																																					visaType +
																																						", functionCode=" +
																																							functionCode +
																																								", functionCodeDescription=" +
																																									functionCodeDescription +
																																										", transactionQualifierId=" +
																																											transactionQualifierId +
																																												", requestDetails=" +
																																													requestDetails +
																																														", responseDetails=" +
																																															responseDetails +
																																																", memberInformation=" +
																																																	memberInformation +
																																																		", contactInformation=" +
																																																			contactInformation +
																																																				", reasonData=" +
																																																					reasonData +
																																																						", transactionCode=" +
																																																							transactionCode +
																																																								", customData=" +
																																																									customData +
																																																										", posCondition=" +
																																																											posCondition +
																																																												", posEntryMode=" +
																																																													posEntryMode +
																																																														", UCAFCollectionIndicator=" +
																																																															UCAFCollectionIndicator +
																																																																", electronicCommerceIndciator=" +
																																																																	electronicCommerceIndciator +
																																																																		", centralSiteBusinessDate=" +
																																																																			centralSiteBusinessDate +
																																																																				", chipCardPresent=" +
																																																																					chipCardPresent +
																																																																						", track2DataServiceCode=" +
																																																																							track2DataServiceCode +
																																																																								"]";
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -3741273341636423192L;

	private String adPank;
	private String adTransactionDetailK;
	private String busDate;
	private String cardControlRejectCode;
	private String centralSiteBusinessDate;
	private String chipCardPresent;
	private String code;
	private ContactInformation contactInformation;
	private String customData;
	private String dataSrc;
	private String description;
	private String electronicCommerceIndciator;
	private String etlProcessEndDate;
	private String externalTransactionId;
	private String functionCode;
	private String functionCodeDescription;
	private boolean isAccessibleIssuer;
	private boolean isDeclined;
	private boolean isHsmUp;
	private String isNext;
	private String isPrev;
	private boolean isReversal;
	private boolean isUserStepUp;
	private boolean isValidServiceRequest;
	private boolean isVisaAtm;
	private MemberInformation memberInformation;
	private String messageType;
	private String messageTypeCode;
	private int nextRowNum;
	private String originIchName;
	private String pinChangeOperatorId;
	private String pinChangeSupervisorId;
	private CodeDescription posCondition;
	private CodeDescription posEntryMode;
	private int prevRowNum;
	private ReasonData reasonData;
	private RequestDetails requestDetails;
	private ResponseDetails responseDetails;
	private String track2DataServiceCode;
	private String transactionCode;
	private String transactionId;
	private String transactionQualifierId;
	private String UCAFCollectionIndicator;
	private String UCAFCollectionIndicatorDescription;
	private String visaArn;
	private String visaOrAtmPos;
	private String visaType;

}