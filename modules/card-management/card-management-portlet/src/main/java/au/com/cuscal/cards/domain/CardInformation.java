package au.com.cuscal.cards.domain;

import au.com.cuscal.cards.commons.Constants;
import au.com.cuscal.framework.cards.CardUtil;

import java.io.Serializable;

import java.math.BigInteger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * domain class
 *
 * @author Rajni Bharara
 *
 */
public class CardInformation implements Serializable {

	public String getCardAccessTxt() {
		return cardAccessTxt;
	}

	public String getCardChannelPermissionsMessage() {
		return cardChannelPermissionsMessage;
	}

	public String getCardDetailsTxt() {
		return cardDetailsTxt;
	}

	public CardHolder getCardHolder() {
		return cardHolder;
	}

	public String getCardholderDetailsTxt() {
		return cardholderDetailsTxt;
	}

	// Card Details

	public BigInteger getCardId() {
		return cardId;
	}

	public List<CardChannelPermission> getCardPermissions() {
		return cardPermissions;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public String getCardStatusCode() {
		return cardStatusCode;
	}

	public Map<String, String> getCardStatusMap() {
		return cardStatusMap;
	}

	public String getCardStatusMessage() {
		return cardStatusMessage;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public String getInstitution() {
		return institution;
	}

	// Visa Exception

	public Date getLastMaintained() {
		return lastMaintained;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public char getMaskedCharacter() {
		return maskedCharacter;
	}

	public String getMaskedPan() {
		return CardUtil.getInstance(
		).mask(
			this.pan, this.maskedCharacter
		);
	}

	// UI text

	public String getPan() {
		return pan;
	}

	public String getPinCountFailures() {
		return pinCountFailures;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public String getSwitchResponseCode() {
		return switchResponseCode;
	}

	public String getVisaExceptionTxt() {
		return visaExceptionTxt;
	}

	public String getVisaReference() {
		return visaReference;
	}

	public boolean isHasCardPortedToPrimarySwitch() {
		return hasCardPortedToPrimarySwitch;
	}

	public boolean isVigilBlocked() {
		return isVigilBlocked;
	}

	public void setCardAccessTxt(String cardAccessTxt) {
		this.cardAccessTxt = cardAccessTxt;
	}

	public void setCardChannelPermissionsMessage(
		String cardChannelPermissionsMessage) {

		this.cardChannelPermissionsMessage = cardChannelPermissionsMessage;
	}

	public void setCardDetailsTxt(String cardDetailsTxt) {
		this.cardDetailsTxt = cardDetailsTxt;
	}

	public void setCardHolder(CardHolder cardHolder) {
		this.cardHolder = cardHolder;
	}

	public void setCardholderDetailsTxt(String cardholderDetailsTxt) {
		this.cardholderDetailsTxt = cardholderDetailsTxt;
	}

	public void setCardId(BigInteger cardId) {
		this.cardId = cardId;
	}

	public void setCardPermissions(
		List<CardChannelPermission> cardPermissions) {

		this.cardPermissions = cardPermissions;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public void setCardStatusCode(String cardStatusCode) {
		this.cardStatusCode = cardStatusCode;
	}

	public void setCardStatusMap(Map<String, String> cardStatusMap) {
		this.cardStatusMap = cardStatusMap;
	}

	public void setCardStatusMessage(String cardStatusMessage) {
		this.cardStatusMessage = cardStatusMessage;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setHasCardPortedToPrimarySwitch(
		boolean hasCardPortedToPrimarySwitch) {

		this.hasCardPortedToPrimarySwitch = hasCardPortedToPrimarySwitch;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public void setLastMaintained(Date lastMaintained) {
		this.lastMaintained = lastMaintained;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	public void setMaskedCharacter(char maskedCharacter) {
		this.maskedCharacter = maskedCharacter;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public void setPinCountFailures(String pinCountFailures) {
		this.pinCountFailures = pinCountFailures;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public void setSwitchResponseCode(String switchResponseCode) {
		this.switchResponseCode = switchResponseCode;
	}

	public void setVigilBlocked(boolean isVigilBlocked) {
		this.isVigilBlocked = isVigilBlocked;
	}

	public void setVisaExceptionTxt(String visaExceptionTxt) {
		this.visaExceptionTxt = visaExceptionTxt;
	}

	public void setVisaReference(String visaReference) {
		this.visaReference = visaReference;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -554124207648778765L;

	private String cardAccessTxt = new String("Card Access");
	private String cardChannelPermissionsMessage;
	private String cardDetailsTxt = new String("Card Details");
	private CardHolder cardHolder;
	private String cardholderDetailsTxt = new String("Cardholder Details");
	private BigInteger cardId;
	private List<CardChannelPermission> cardPermissions;
	private String cardStatus;
	private String cardStatusCode;
	private Map<String, String> cardStatusMap;
	private String cardStatusMessage;
	private String expiryDate;
	private boolean hasCardPortedToPrimarySwitch;
	private String institution;
	private boolean isVigilBlocked;
	private Date lastMaintained;
	private Date lastUpdated;
	private Date lastUsed;
	private char maskedCharacter = Constants.PAN_MASKED_CHARACTER;
	private String pan;
	private String pinCountFailures;
	private String responseDescription;
	private String switchResponseCode;
	private String visaExceptionTxt = new String("Visa Exception");
	private String visaReference;

}