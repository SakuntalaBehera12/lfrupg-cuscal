package au.com.cuscal.chargeback.forms;

import au.com.cuscal.chargeback.common.ChargebackCodes;
import au.com.cuscal.chargeback.common.ChargebacksUtility;
import au.com.cuscal.chargeback.common.Constants;
import au.com.cuscal.framework.webservices.selfservice.AttributesType;
import au.com.cuscal.framework.webservices.selfservice.TicketType;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.web.multipart.MultipartFile;

public class ChargebackForm implements Serializable {

	public ChargebackForm() {
	}

	public ChargebackForm(TicketType ticket) {
		this.ticketId = Long.valueOf(
			ticket.getId()
		).toString();

		List<AttributesType> attType = ticket.getAttributes();

		for (AttributesType att : attType) {
			if (StringUtils.equalsIgnoreCase(
					"CHARGEBACK.TYPE", att.getType())) {

				this.type = Long.valueOf(
					att.getRequestCode()
				).toString();

				if (att.getRequestCode() == 44) {
					logger.debug("Build RFI");
					buildRfi(attType);
				}
				else if (att.getRequestCode() == 45) {
					logger.debug("Build Fraud");
					buildFraud(attType);
				}
				else if (att.getRequestCode() == 46) {
					logger.debug("Build Authorisation");
					//Have not built the edit section for the Processing Error Chargeback Type
					buildAuthorisation(attType);
				}
				else if (att.getRequestCode() == 47) {
					logger.debug("Build Processing Error");
					buildProcessingError(attType);
				}
				else if (att.getRequestCode() == 48) {
					buildCancelled(attType);
					logger.debug("Build Cancellation");
				}
				else if (att.getRequestCode() == 49) {
					buildNonReceipt(attType);
					logger.debug("Build Goods/Services");
				}
			}
		}
	}

	public AttemptToResolve getAttemptToResolve() {
		return attemptToResolve;
	}

	public CancellationInfo getCancellationInfo() {
		return cancellationInfo;
	}

	public String[] getCertifications() {
		return certifications;
	}

	public ChargebackCodes getChargebackCodes() {
		return chargebackCodes;
	}

	public CreditInformation getCreditInfo() {
		return creditInfo;
	}

	public ElaborationInfo getElaborationInfo() {
		return elaborationInfo;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getFraudReportingDate() {
		return fraudReportingDate;
	}

	public String getNote() {
		return note;
	}

	public QuestionnaireInformation getQuestionInfo() {
		return questionInfo;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public List<MultipartFile> getRequestAttachments() {
		return requestAttachments;
	}

	public RetrievalRequest getRetrievalRequest() {
		return retrievalRequest;
	}

	public ReturnInformation getReturnInfo() {
		return returnInfo;
	}

	/*private String memberMessage;
	private String updateMessage;*/

	public String getTicketId() {
		return ticketId;
	}

	public String getType() {
		return type;
	}

	public boolean isDisclaimer() {
		return disclaimer;
	}

	public void setAttemptToResolve(AttemptToResolve attemptToResolve) {
		this.attemptToResolve = attemptToResolve;
	}

	public void setCancellationInfo(CancellationInfo cancellationInfo) {
		this.cancellationInfo = cancellationInfo;
	}

	public void setCertifications(String[] certifications) {
		this.certifications = certifications;
	}

	public void setChargebackCodes(ChargebackCodes chargebackCodes) {
		this.chargebackCodes = chargebackCodes;
	}

	public void setCreditInfo(CreditInformation creditInfo) {
		this.creditInfo = creditInfo;
	}

	public void setDisclaimer(boolean disclaimer) {
		this.disclaimer = disclaimer;
	}

	public void setElaborationInfo(ElaborationInfo elaborationInfo) {
		this.elaborationInfo = elaborationInfo;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setFraudReportingDate(String fraudReportingDate) {
		this.fraudReportingDate = fraudReportingDate;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setQuestionInfo(QuestionnaireInformation questionInfo) {
		this.questionInfo = questionInfo;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	/*public void setMemberMessage(String memberMessage) {
		this.memberMessage = memberMessage;
	}

	public String getMemberMessage() {
		return memberMessage;
	}

	public void setUpdateMessage(String updateMessage) {
		this.updateMessage = updateMessage;
	}

	public String getUpdateMessage() {
		return updateMessage;
	}*/

	public void setRequestAttachments(List<MultipartFile> requestAttachments) {
		this.requestAttachments = requestAttachments;
	}

	public void setRetrievalRequest(RetrievalRequest retrievalRequest) {
		this.retrievalRequest = retrievalRequest;
	}

	public void setReturnInfo(ReturnInformation returnInfo) {
		this.returnInfo = returnInfo;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Build the attempt to resolve section for editing the forms.
	 *
	 * @param attributes
	 */
	protected void buildAttempToResolveInformationForForms(
		List<AttributesType> attributes) {

		AttemptToResolve resolveInfo = new AttemptToResolve();

		for (AttributesType att : attributes) {
			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.ATTEMPTTORESOLVE",
					att.getType())) {

				resolveInfo.setResolveWithMerchant(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.DATECONTACT", att.getType())) {

				resolveInfo.setErrorDateContact(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.NAMECONTACT", att.getType())) {

				resolveInfo.setNameContact(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.METHODCONTACT",
					att.getType())) {

				resolveInfo.setMethodContact(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.MERCHANTSRESPONSE",
					att.getType())) {

				resolveInfo.setMerchantResponse(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE", att.getType())) {

				if (att.getRequestCode() == 266) {
					resolveInfo.setNotApplicableLocalLaw(Boolean.TRUE);
				}
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.EXPLAINNORESOLVE",
					att.getType())) {

				resolveInfo.setExplainWhyNotResolve(att.getValue());
			}
		}

		this.attemptToResolve = resolveInfo;
	}

	/**
	 *
	 * @param attributes
	 */
	protected void buildAuthorisation(List<AttributesType> attributes) {
		ElaborationInfo elabInfo = new ElaborationInfo();
		List<String> crbRegions = new ArrayList<>();
		List<String> certs = new ArrayList<>();

		for (AttributesType att : attributes) {
			if (StringUtils.equals("CHARGEBACK.AUTHORISATION", att.getType())) {
				this.reasonCode = Long.valueOf(
					att.getRequestCode()
				).toString();
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.CARDDATE", att.getType())) {

				elabInfo.setAuthDate(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.CRBDATE", att.getType())) {

				elabInfo.setAuthCrbDate(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.CRBREGION", att.getType())) {

				crbRegions.add(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO", att.getType())) {

				if (att.getRequestCode() == 225) {
					elabInfo.setAuthRequiredNoTransDate(Boolean.TRUE);
				}

				//				if (att.getRequestCode() == 226) {
				if (att.getRequestCode() == 377) {
					elabInfo.setAuthObtainedIncorrectData(Boolean.TRUE);
				}

				if (att.getRequestCode() == 227) {
					elabInfo.setAuthTransExceedsAuthAmount(Boolean.TRUE);
				}

				if (att.getRequestCode() == 228) {
					elabInfo.setAuthNonMatchingMCC(Boolean.TRUE);
				}

				if (att.getRequestCode() == 229) {
					elabInfo.setAuthAccountNumberOnExceptionFile(Boolean.TRUE);
				}

				if (att.getRequestCode() == 213) {
					elabInfo.setCardStatusChanged(Boolean.TRUE);
				}
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.AUTHORISATION.INCORRECT",
					att.getType())) {

				elabInfo.setAuthObtainedIncorrectDataExplain(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.MCC.CLEARING", att.getType())) {

				elabInfo.setAuthMCCInClearing(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.MCC.AUTHORISATION",
					att.getType())) {

				elabInfo.setAuthMCCInSystemAuthorisation(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.AUTHDECLINEDDATE",
					att.getType())) {

				elabInfo.setAuthDeclinedDate(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.TRANSEXCEEDSAUTHAMOUNT",
					att.getType())) {

				elabInfo.setAuthTransExceedsAuthAmountExplain(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.CARDEXPIREDDATE",
					att.getType())) {

				elabInfo.setCardExpiredDate(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.CERTIFICATIONS", att.getType())) {

				certs.add(att.getValue());
			}
		}

		this.certifications = new String[certs.size()];
		certs.toArray(this.certifications);

		elabInfo.authCrbRegions = new String[crbRegions.size()];
		crbRegions.toArray(elabInfo.authCrbRegions);

		this.elaborationInfo = elabInfo;
	}

	/**
	 * A common method to build the cancellation information section for the editing forms.
	 *
	 * @param att
	 */
	protected void buildCancellationInformationForForms(
		List<AttributesType> attributes) {

		CancellationInfo cancelInfo = new CancellationInfo();

		for (AttributesType att : attributes) {
			//Cancellation Info

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.DATERECURRINGTRANCANED",
					att.getType())) {

				cancelInfo.setDateRecurringTransCancelled(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.DATEACQUIRERNOTIFIED",
					att.getType())) {

				cancelInfo.setDateAcquirerNotified(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.DATEPREVIOUSCHARGEBACK",
					att.getType())) {

				cancelInfo.setDatePreviousChargeback(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.ARN", att.getType())) {

				cancelInfo.setCancelledArn(String.valueOf(att.getValue()));
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.REASONCANCELLED",
					att.getType())) {

				cancelInfo.setReasonCancelled(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.CANCELDATE", att.getType())) {

				cancelInfo.setDateCancelled(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.SPOKEWITH", att.getType())) {

				cancelInfo.setSpokeWith(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.CANCELCODE", att.getType())) {

				cancelInfo.setCancellationCode(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.WASGIVENCANCELPOLICY",
					att.getType())) {

				cancelInfo.setWasCardholderGivenCancellationPolicy(
					Long.valueOf(
						att.getRequestCode()
					).toString());
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.WHATWASCANCELPOLICY",
					att.getType())) {

				cancelInfo.setWhatWasCancellationPolicy(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.WASCANCELCODEGIVEN",
					att.getType())) {

				cancelInfo.setWasCancellationCodeGiven(
					Long.valueOf(
						att.getRequestCode()
					).toString());
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO.CANCELCODENOTGIVEN",
					att.getType())) {

				cancelInfo.setExplainWhyCancellationCodeNotGiven(
					att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.CANCELLATIONINFO", att.getType())) {

				if (att.getRequestCode() == 346) {
					cancelInfo.setCancellationCodeGivenButNotRetained(
						Boolean.TRUE);
				}

				if (att.getRequestCode() == 347) {
					cancelInfo.setReservationWithin72Hours(Boolean.TRUE);
				}

				if (att.getRequestCode() == 348) {
					cancelInfo.setMerchantNotAcceptCancellation(Boolean.TRUE);
				}
			}
		}

		this.cancellationInfo = cancelInfo;
	}

	/**
	 *
	 * @param attributes
	 */
	protected void buildCancelled(List<AttributesType> attributes) {

		/*CancellationInfo canInfo = new CancellationInfo();
		AttemptToResolve attToRes = new AttemptToResolve();*/
		ElaborationInfo elabInfo = new ElaborationInfo();
		//		ReturnInformation returnInfo = new ReturnInformation();

		for (AttributesType att : attributes) {
			if (StringUtils.equals("CHARGEBACK.CANCELLED", att.getType())) {
				this.reasonCode = Long.valueOf(
					att.getRequestCode()
				).toString();
			}

			//Elaboration Info

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.WHATWASPURCHASED",
					att.getType())) {

				elabInfo.setWhatWasPurchased(
					Long.valueOf(
						att.getRequestCode()
					).toString());
			}

			if ("294".equals(elabInfo.getWhatWasPurchased())) { //Merchandise section

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.WHATWASWRONG",
						att.getType())) {

					elabInfo.setMerchandiseWhatWasWrong(
						Long.valueOf(
							att.getRequestCode()
						).toString());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.EXPLAINMERCHPURCHASED",
						att.getType())) {

					elabInfo.setMerchandiseOrdered(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.MERCHNOTASDESCRIBED",
						att.getType())) {

					elabInfo.setMerchandiseDetailsNotAsDescribed(
						att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.MERCHDEFECTIVE",
						att.getType())) {

					elabInfo.setDetailsDefective(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.MERCHRETURNED",
						att.getType())) {

					elabInfo.setMerchandiseReturn(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.EXPLAINWHYNOTRETURNED",
						att.getType())) {

					elabInfo.setWhyMerchandiseNotReturned(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.DIDMERCHANTREFUSERETURN",
						att.getType())) {

					elabInfo.setMerchantRefuse(
						Long.valueOf(
							att.getRequestCode()
						).toString());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.THEMERCHANT",
						att.getType())) {

					elabInfo.setMerchantRefusedReason(
						Long.valueOf(
							att.getRequestCode()
						).toString());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.MERCHCOUNTERFEITEXPLAIN",
						att.getType())) {

					elabInfo.setExpertIdentified(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.TERMSOFSALEMISREP",
						att.getType())) {

					elabInfo.setMerchandiseExplainTermsOfSale(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.MERCHCOUNTERFEITDATE",
						att.getType())) {

					elabInfo.setDateInformed(
						ChargebacksUtility.convertDateFromOneFormatToAnother(
							att.getValue(), Constants.DATE_FORMAT_LONG,
							Constants.DATE_FORMAT_SHORT));
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.MERCHQUALITY",
						att.getType())) {

					elabInfo.setMerchandiseQualityExplain(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO", att.getType())) {

					/*if (att.getRequestCode() == 318) {
						elabInfo.setMerchantRefusedAuthorisation(Boolean.TRUE);
					}

					if (att.getRequestCode() == 319) {
						elabInfo.setMerchantRefusedReturn(Boolean.TRUE);
					}

					if (att.getRequestCode() == 320) {
						elabInfo.setMerchantAdvisedNoReturn(Boolean.TRUE);
					}*/

					if (att.getRequestCode() == 305) {
						elabInfo.setCounterfeitDocumentation(Boolean.TRUE);
					}

					if (att.getRequestCode() == 304) {
						elabInfo.setCounterfeitCertification(Boolean.TRUE);
					}

					if (att.getRequestCode() == 374) {
						elabInfo.setMerchandiseAttachingIncidentReporting(
							Boolean.TRUE);
					}
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.WORKREDONE",
						att.getType())) {

					elabInfo.setWorkRedone(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.WHEREWORKREDONE",
						att.getType())) {

					elabInfo.setWhereWorkRedone(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.WHENWORKREDONE",
						att.getType())) {

					elabInfo.setWhenWorkRedone(
						ChargebacksUtility.convertDateFromOneFormatToAnother(
							att.getValue(), Constants.DATE_FORMAT_LONG,
							Constants.DATE_FORMAT_SHORT));
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.AMOUNTWORKREDONE",
						att.getType())) {

					elabInfo.setAmountWorkRedone(att.getValue());
				}
			}
			else if ("295".equals(elabInfo.getWhatWasPurchased())) { //Services section.

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.WHATWASWRONG",
						att.getType())) {

					elabInfo.setServicesWhatWasWrong(
						Long.valueOf(
							att.getRequestCode()
						).toString());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.SERVICENOTASDESCRIBED",
						att.getType())) {

					elabInfo.setServicesDetailsNotAsDescribed(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.EXPLAINSERVICEPURCHASED",
						att.getType())) {

					elabInfo.setServicesOrdered(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.DATEEXPETEDSERVICE",
						att.getType())) {

					elabInfo.setExpectedServicesDate(
						ChargebacksUtility.convertDateFromOneFormatToAnother(
							att.getValue(), Constants.DATE_FORMAT_LONG,
							Constants.DATE_FORMAT_SHORT));
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.TERMSOFSALEMISREP",
						att.getType())) {

					elabInfo.setServicesExplainTermsOfSale(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO", att.getType())) {

					if (att.getRequestCode() == 374) {
						elabInfo.setServicesAttachingIncidentReporting(
							Boolean.TRUE);
					}
				}
			}
			else if ("337".equals(elabInfo.getWhatWasPurchased())) { //Guaranteed reservation

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO", att.getType())) {

					if (att.getRequestCode() == 341) {
						elabInfo.setMerchantBilledMore(Boolean.TRUE);
					}
				}
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABINFO.ORIGCREDITNOTACCEPTED",
					att.getType())) {

				elabInfo.setOriginalCreditNotAccepted(
					Long.valueOf(
						att.getRequestCode()
					).toString());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.DIDYOUCANCEL", att.getType())) {

				elabInfo.setDidCancel(att.getValue());
			}
		}

		this.elaborationInfo = elabInfo;
		buildCancellationInformationForForms(attributes);
		buildAttempToResolveInformationForForms(attributes);
		buildReturnInformationForForms(attributes);
		buildCreditInformationForForms(attributes);
	}

	/**
	 * Build the credit information section for editing the forms.
	 *
	 * @param attributes
	 */
	protected void buildCreditInformationForForms(
		List<AttributesType> attributes) {

		CreditInformation creditInformation = new CreditInformation();

		for (AttributesType att : attributes) {
			if (StringUtils.equals(
					"CHARGEBACK.CREDITINFO.WASCREDITVOUCHERGIVEN",
					att.getType())) {

				creditInformation.setCreditVoucherGiven(
					Long.valueOf(
						att.getRequestCode()
					).toString());
			}

			if (StringUtils.equals(
					"CHARGEBACK.CREDITINFO.WASCREDITVOIDVOUCHERDATED",
					att.getType())) {

				creditInformation.setCreditVoucherDated(
					Long.valueOf(
						att.getRequestCode()
					).toString());
			}

			if (StringUtils.equals(
					"CHARGEBACK.CREDITINFO.DATECREDITVOUCHERVOIDED",
					att.getType())) {

				creditInformation.setDateCreditVoucher(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.CREDITINFO.CREDITAMOUNT", att.getType())) {

				creditInformation.setCreditAmount(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.CREDITINFO.INVOICENUMBER", att.getType())) {

				creditInformation.setInvoiceNumber(att.getValue());
			}
		}

		this.creditInfo = creditInformation;
	}

	/**
	 *
	 * @param attType
	 */
	protected void buildFraud(List<AttributesType> attType) {
		List<String> certs = new ArrayList<>();
		RetrievalRequest request = new RetrievalRequest();
		ElaborationInfo elabInfo = new ElaborationInfo();

		for (AttributesType att : attType) {
			if (StringUtils.equals("CHARGEBACK.FRAUD", att.getType())) {
				this.reasonCode = Long.valueOf(
					att.getRequestCode()
				).toString();
			}

			if (StringUtils.equals(
					"CHARGEBACK.CERTIFICATIONS", att.getType())) {

				certs.add(
					Long.valueOf(
						att.getValue()
					).toString());
			}

			if (StringUtils.equals(
					"CHARGEBACK.RETRIEVALREQUEST", att.getType())) {

				String temp =
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT);

				request.setTc52Date(temp);
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.CARDDATE", att.getType())) {

				String temp =
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT);

				elabInfo.setCardDate(temp);
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.CARDSTATUS", att.getType())) {

				elabInfo.setCardStatus(
					Long.valueOf(
						att.getRequestCode()
					).toString());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.FRAUDDATE", att.getType())) {

				String temp =
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT);

				elabInfo.setFraudDate(temp);
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.FRAUDREFNUMBER",
					att.getType())) {

				elabInfo.setFraudAdviceNo(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO", att.getType())) {

				if (att.getRequestCode() == 216) {
					elabInfo.setAcctNoFictitiousOrInvalid(Boolean.TRUE);
				}

				if (att.getRequestCode() == 384) {
					elabInfo.setVisaEuropeOnlyData(Boolean.TRUE);
				}
			}

			if (StringUtils.equals(
					"CHARGEBACK.FRAUDREPORTING", att.getType())) {

				String temp =
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT);

				this.fraudReportingDate = temp;
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.VISAEUROPEONLYDATE",
					att.getType())) {

				elabInfo.setVisaEuropeOnlyDate(att.getValue());
			}
		}

		this.certifications = new String[certs.size()];
		certs.toArray(this.certifications);

		this.elaborationInfo = elabInfo;
		this.retrievalRequest = request;
	}

	protected void buildNonReceipt(List<AttributesType> attributes) {
		QuestionnaireInformation quesInfo = new QuestionnaireInformation();
		ElaborationInfo elabInfo = new ElaborationInfo();

		for (AttributesType att : attributes) {
			if (StringUtils.equals("CHARGEBACK.NONRECEIPT", att.getType())) {
				this.reasonCode = Long.valueOf(
					att.getRequestCode()
				).toString();
			}

			if (StringUtils.equals(
					"CHARGEBACK.QUESTIONNAIRE.AMOUNTREQUESTED",
					att.getType())) {

				quesInfo.setAmountRequested(String.valueOf(att.getValue()));
			}

			if (StringUtils.equals(
					"CHARGEBACK.QUESTIONNAIRE.AMOUNTRECEIVED", att.getType())) {

				quesInfo.setAmountReceived(String.valueOf(att.getValue()));
			}

			if (StringUtils.equals(
					"CHARGEBACK.QUESTIONNAIRE.CASHLOADINFORMATION",
					att.getType())) {

				quesInfo.setCashLoadInformation(
					String.valueOf(att.getRequestCode()));
			}

			if (StringUtils.equals("CHARGEBACK.QUESTIONNAIRE", att.getType())) {
				if (att.getRequestCode() == 269) {
					quesInfo.setCashOrLoadValueNotReceived(Boolean.TRUE);
				}

				if (att.getRequestCode() == 270) {
					quesInfo.setPartialCashOrLoadValueReceived(Boolean.TRUE);
				}
			}

			//Elaboration information

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.WHATWASPURCHASED",
					att.getType())) {

				elabInfo.setQuestionnaireWhatWasPurchased(
					Long.valueOf(
						att.getRequestCode()
					).toString());
			}

			if (StringUtils.equalsIgnoreCase(
					"362", elabInfo.getQuestionnaireWhatWasPurchased())) { //Merchandise

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.EXPLAINMERCHPURCHASED",
						att.getType())) {

					elabInfo.setQuestionnaireMerchandiseOrdered(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.EXPECTEDRECEIPTDATE",
						att.getType())) {

					elabInfo.setQuestionnaireMerchandiseDateExpected(
						ChargebacksUtility.convertDateFromOneFormatToAnother(
							att.getValue(), Constants.DATE_FORMAT_LONG,
							Constants.DATE_FORMAT_SHORT));
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO", att.getType())) {

					if (att.getRequestCode() == 363) {
						elabInfo.setQuestionnaireMerchandiseNotReceived(
							Boolean.TRUE);
					}
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.MERCHANDISERECEIVEDON",
						att.getType())) {

					elabInfo.setQuestionnaireMerchandiseReceivedOn(
						ChargebacksUtility.convertDateFromOneFormatToAnother(
							att.getValue(), Constants.DATE_FORMAT_LONG,
							Constants.DATE_FORMAT_SHORT));
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.MERCHRETURNED",
						att.getType())) {

					elabInfo.setQuestionnaireMerchandiseReturn(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.DELIVERYLOCATION",
						att.getType())) {

					elabInfo.setQuestionnaireMerchandiseAgreedLocation(
						att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.GOODSCANCELNORECEIPT",
						att.getType())) {

					elabInfo.setQuestionnaireWasMerchandiseCancelledNonReceipt(
						att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.EXPLAINWHEREMERCHANDISE",
						att.getType())) {

					elabInfo.setQuestionnaireWhereIsMerchandise(att.getValue());
				}
			}
			else if (StringUtils.equalsIgnoreCase(
						"295", elabInfo.getQuestionnaireWhatWasPurchased())) { //Service

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.EXPLAINSERVICEPURCHASED",
						att.getType())) {

					elabInfo.setQuestionnaireServicesOrdered(att.getValue());
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO", att.getType())) {

					if (att.getRequestCode() == 364) {
						elabInfo.setQuestionnaireMerchantUnwilling(
							Boolean.TRUE);
					}

					if (att.getRequestCode() == 365) {
						elabInfo.setQuestionnaireServicesNotReceived(
							Boolean.TRUE);
					}
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.EXPECTEDRECEIPTDATE",
						att.getType())) {

					elabInfo.setQuestionnaireServicesDateExpected(
						ChargebacksUtility.convertDateFromOneFormatToAnother(
							att.getValue(), Constants.DATE_FORMAT_LONG,
							Constants.DATE_FORMAT_SHORT));
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.SERVICERECEIVEDON",
						att.getType())) {

					elabInfo.setQuestionnaireServicesReceivedOn(
						ChargebacksUtility.convertDateFromOneFormatToAnother(
							att.getValue(), Constants.DATE_FORMAT_LONG,
							Constants.DATE_FORMAT_SHORT));
				}

				if (StringUtils.equals(
						"CHARGEBACK.ELABORATIONINFO.SERVICECANCELNORECEIPT",
						att.getType())) {

					elabInfo.setQuestionnaireWasServicesCancelledNonReceipt(
						att.getValue());
				}
			}
		}

		this.questionInfo = quesInfo;
		this.elaborationInfo = elabInfo;
		buildCancellationInformationForForms(attributes);
		buildReturnInformationForForms(attributes);
		buildAttempToResolveInformationForForms(attributes);
	}

	/**
	 *
	 * @param attributes
	 */
	protected void buildProcessingError(List<AttributesType> attributes) {
		ElaborationInfo elabInfo = new ElaborationInfo();
		List<String> certs = new ArrayList<>();
		AttemptToResolve attToRes = new AttemptToResolve();

		for (AttributesType att : attributes) {
			if (StringUtils.equals(
					"CHARGEBACK.PROCESSINGERROR", att.getType())) {

				this.reasonCode = Long.valueOf(
					att.getRequestCode()
				).toString();
			}

			//elabInfo

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.TRANSACTION.DATE",
					att.getType())) {

				elabInfo.setProcessingErrorTransDate(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.SETTLEMENT.DATE",
					att.getType())) {

				elabInfo.setProcessingErrorSettleDate(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO", att.getType())) {

				if (att.getRequestCode() == 236) {
					elabInfo.setProcessingErrorCounterfeit(Boolean.TRUE);
				}

				if (att.getRequestCode() == 237) {
					elabInfo.setProcessingErrorAccountClosed(Boolean.TRUE);
				}

				if (att.getRequestCode() == 238) {
					elabInfo.setProcessingErrorOtherFraud(Boolean.TRUE);
				}

				if (att.getRequestCode() == 239) {
					elabInfo.setProcessingErrorPresentmentOlder180Days(
						Boolean.TRUE);
				}

				if (att.getRequestCode() == 233) {
					elabInfo.setProcessingErrorAccountNumberNegativeResponse(
						Boolean.TRUE);//todo
				}

				if (att.getRequestCode() == 240) {
					elabInfo.
						setProcessingErrorTransProcessedAfterTransactionDate(
							Boolean.TRUE);
				}

				if (att.getRequestCode() == 247) {
					elabInfo.
						setProcessingErrorTransactionCurrencyDifferentTransmitted(
							Boolean.TRUE);
				}

				if (att.getRequestCode() == 250) {
					elabInfo.setProcessingErrorTransactionCountryDifferent(
						Boolean.TRUE);
				}

				if (att.getRequestCode() == 253) {
					elabInfo.setProcessingErrorNonMatchingAccountNumber(
						Boolean.TRUE);
				}

				if (att.getRequestCode() == 254) {
					elabInfo.setProcessingErrorNonExistingAccount(Boolean.TRUE);
				}

				if (att.getRequestCode() == 256) {
					elabInfo.setProcessingErrorProofOtherMeans(Boolean.TRUE);
				}

				if (att.getRequestCode() == 213) {
					elabInfo.setProcessingErrorCardStatusChanged(Boolean.TRUE);
				}
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.TRANSACTION.INCORRECT",
					att.getType())) {

				elabInfo.setProcessingErrorTransactionIncorrect(
					String.valueOf(att.getRequestCode()));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.TRANSACTION.CURRENCY",
					att.getType())) {

				elabInfo.setProcessingErrorCurrencyTransaction(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.TRANSMITTED.CURRENCY",
					att.getType())) {

				elabInfo.setProcessingErrorCurrencyTransmitted(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.TRANSACTION.COUNTRY",
					att.getType())) {

				elabInfo.setProcessingErrorTransactionCountry(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.PORTAL.COUNTRY",
					att.getType())) {

				elabInfo.setProcessingErrorPortalCountry(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.ARN", att.getType())) {

				elabInfo.setProcessingErrorARN(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.TRANSACTION.DATE",
					att.getType())) {

				elabInfo.setProcessingErrorTransactionDate(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.PAYMENT.METHOD",
					att.getType())) {

				elabInfo.setProcessingErrorPaymentMethod(
					String.valueOf(att.getRequestCode()));
			}

			//attToRes

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.ATTEMPTTORESOLVE",
					att.getType())) {

				attToRes.setResolveWithMerchant(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.DATECONTACT", att.getType())) {

				attToRes.setErrorDateContact(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.NAMECONTACT", att.getType())) {

				attToRes.setNameContact(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.METHODCONTACT",
					att.getType())) {

				attToRes.setMethodContact(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.MERCHANTSRESPONSE",
					att.getType())) {

				attToRes.setMerchantResponse(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE.EXPLAINNORESOLVE",
					att.getType())) {

				attToRes.setExplainWhyNotResolve(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ATTEMPTTORESOLVE", att.getType())) {

				if (att.getRequestCode() == 266) {
					attToRes.setNotApplicableLocalLaw(Boolean.TRUE);
				}
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.PROCESSINGERRORDUETO",
					att.getType())) {

				elabInfo.setProcessingErrorDueTo(
					String.valueOf(att.getRequestCode()));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.INFOINCORRECT",
					att.getType())) {

				elabInfo.setInfoIncorrect(String.valueOf(att.getRequestCode()));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.AMOUNTINCORRECT",
					att.getType())) {

				elabInfo.setAmountIncorrect(
					String.valueOf(att.getRequestCode()));
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.INCORRECTAMOUNTWAS",
					att.getType())) {

				elabInfo.setIncorrectAmountWas(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.INCORRECTAMOUNTSHOULDBE",
					att.getType())) {

				elabInfo.setIncorrectAmountShouldBe(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.CARDNUMBER", att.getType())) {

				elabInfo.setCardNumber(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.ELABORATIONINFO.NAMEONVOUCHER",
					att.getType())) {

				elabInfo.setNameOnVoucher(att.getValue());
			}

			//certs

			if (StringUtils.equals(
					"CHARGEBACK.CERTIFICATIONS", att.getType())) {

				certs.add(att.getValue());
			}
		}

		this.certifications = new String[certs.size()];
		certs.toArray(this.certifications);

		this.elaborationInfo = elabInfo;
		this.attemptToResolve = attToRes;
	}

	/**
	 * Build the return information section for form editing.
	 *
	 * @param attributes
	 */
	protected void buildReturnInformationForForms(
		List<AttributesType> attributes) {

		ReturnInformation returnInformation = new ReturnInformation();

		for (AttributesType att : attributes) {
			//Return Info

			if (StringUtils.equals(
					"CHARGEBACK.RETURNINFO.RETURNDATE", att.getType())) {

				returnInformation.setReturnDate(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.RETURNINFO.RETURNMETHOD", att.getType())) {

				returnInformation.setReturnMethod(
					Long.valueOf(
						att.getRequestCode()
					).toString());
			}

			if (StringUtils.equals(
					"CHARGEBACK.RETURNINFO.OTHERMETHOD", att.getType())) {

				returnInformation.setOtherMethod(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.RETURNINFO.SHIPPINGNUMBER", att.getType())) {

				returnInformation.setShippingNumber(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.RETURNINFO.WHOSIGNEDPACKAGE", att.getType())) {

				returnInformation.setWhoSignedPackage(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.RETURNINFO.DELIVERYADDRESS", att.getType())) {

				returnInformation.setDeliveryAddress(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.RETURNINFO.MERCHANTRECEIVEDON",
					att.getType())) {

				returnInformation.setMerchantReceivedOn(
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT));
			}

			if (StringUtils.equals(
					"CHARGEBACK.RETURNINFO.RETURNAUTHNO", att.getType())) {

				returnInformation.setReturnAuthorisationNumber(att.getValue());
			}

			if (StringUtils.equals(
					"CHARGEBACK.RETURNINFO.MERCHANTINSTRUCTION",
					att.getType())) {

				returnInformation.setReturnInstruction(att.getValue());
			}
		}

		this.returnInfo = returnInformation;
	}

	/**
	 *
	 * @param attType
	 */
	protected void buildRfi(List<AttributesType> attType) {
		RetrievalRequest request = new RetrievalRequest();

		for (AttributesType att : attType) {
			if (StringUtils.equals(
					"CHARGEBACK.REQUESTFORINFORMATION", att.getType())) {

				this.reasonCode = Long.valueOf(
					att.getRequestCode()
				).toString();
			}

			if (StringUtils.equals(
					"CHARGEBACK.RETRIEVALREQUEST", att.getType())) {

				String temp =
					ChargebacksUtility.convertDateFromOneFormatToAnother(
						att.getValue(), Constants.DATE_FORMAT_LONG,
						Constants.DATE_FORMAT_SHORT);

				request.setTc52Date(temp);
			}
		}

		this.retrievalRequest = request;
	}

	private static final Logger logger = Logger.getLogger(ChargebackForm.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 6379106124191828618L;

	private AttemptToResolve attemptToResolve;
	private CancellationInfo cancellationInfo;
	private String[] certifications;
	private ChargebackCodes chargebackCodes;
	private CreditInformation creditInfo;
	private boolean disclaimer;
	private ElaborationInfo elaborationInfo;
	private String errorMsg;
	private String fraudReportingDate;
	private String note;
	private QuestionnaireInformation questionInfo;
	private String reasonCode;
	private List<MultipartFile> requestAttachments;
	private RetrievalRequest retrievalRequest;
	private ReturnInformation returnInfo;
	private String ticketId;
	private String type;

}