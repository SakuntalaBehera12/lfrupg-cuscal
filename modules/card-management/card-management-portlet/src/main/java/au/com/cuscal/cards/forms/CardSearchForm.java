package au.com.cuscal.cards.forms;

import static au.com.cuscal.cards.commons.Constants.DASH;

import static org.apache.commons.lang.StringUtils.isBlank;

import au.com.cuscal.cards.commons.Constants;
import au.com.cuscal.framework.cards.CardUtil;

import java.io.Serializable;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * domain class
 *
 * @author Rajni Bharara
 *
 */
public class CardSearchForm implements Serializable {

	public static final String EXPIRY_DATE_MM_DEFAULT = "MM";

	public static final String EXPIRY_DATE_YYYY_DEFAULT = "YYYY";

	public static final boolean NON_EXPIRED_DEFAULT = false;

	public String getCardholderName() {
		return cardholderName;
	}

	public String getCardId() {
		return cardId;
	}

	public String[] getCardStatus() {
		return cardStatus;
	}

	public Map<String, String> getCardStatusMap() {
		return cardStatusMap;
	}

	public Map<String, String> getCardStatusMapForAPIs() {
		return cardStatusMapForAPIs;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	public String getExpiryMonth() {
		return expiryMonth;
	}

	public String getExpiryYear() {
		return expiryYear;
	}

	public String getFormattedExpiry() {
		if (EXPIRY_DATE_MM_DEFAULT.equals(expiryMonth) ||
			EXPIRY_DATE_YYYY_DEFAULT.equals(expiryYear) ||
			isBlank(expiryMonth) || isBlank(expiryYear)) {

			return null;
		}

		String mm = pad(expiryMonth, 2, paddingChar);
		String yyyy = pad(expiryYear, 4, paddingChar);

		String yyyymm = yyyy + DASH + mm;

		return yyyymm;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getIssuerShortName() {
		return issuerShortName;
	}

	public char getMaskedCharacter() {
		return maskedCharacter;
	}

	public String getMaskedPan() {
		return CardUtil.getInstance(
		).mask(
			this.panOrBin, this.maskedCharacter
		);
	}

	public String getNormalisedExpiry() {
		if (EXPIRY_DATE_MM_DEFAULT.equals(expiryMonth) ||
			EXPIRY_DATE_YYYY_DEFAULT.equals(expiryYear) ||
			isBlank(expiryMonth) || isBlank(expiryYear)) {

			return null;
		}

		String mm = pad(expiryMonth, 2, paddingChar);
		String yyyy = pad(expiryYear, 4, paddingChar);

		String yyyymm = yyyy + mm;

		return yyyymm;
	}

	public Map<String, String> getOrganisationsMap() {
		return organisationsMap;
	}

	public String getPanOrBin() {
		return panOrBin;
	}

	public String getPostCode() {
		return postCode;
	}

	public String[] getSchemeType() {
		return schemeType;
	}

	public Map<String, String> getSchemeTypeMap() {
		return schemeTypeMap;
	}

	/**
	 * @return the tokenId
	 */
	public String getTokenId() {
		return tokenId;
	}

	public boolean isNonExpired() {
		return nonExpired;
	}

	public void normalise() {
		normaliseCardExpiryDate();
		this.cardholderName =
			this.cardholderName != null ? this.cardholderName.toUpperCase() :
				this.cardholderName;
		this.issuer =
			this.issuer != null ? this.issuer.toUpperCase() : this.issuer;
	}

	public void resetForm() {
		logger.debug("In Card search form object reset method - Start ");
		panOrBin = null;
		issuer = null;
		cardholderName = null;
		postCode = null;

		cardStatus = null;
		schemeType = null;

		expiryMonth = EXPIRY_DATE_MM_DEFAULT;
		expiryYear = EXPIRY_DATE_YYYY_DEFAULT;

		nonExpired = NON_EXPIRED_DEFAULT;

		cardId = null;

		issuerShortName = null;
		logger.debug("In Card search form object reset method - End ");
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public void setCardStatus(String[] cardStatus) {
		this.cardStatus = cardStatus;
	}

	public void setCardStatusMap(Map<String, String> cardStatusMap) {
		this.cardStatusMap = cardStatusMap;
	}

	public void setCardStatusMapForAPIs(
		Map<String, String> cardStatusMapForAPIs) {

		this.cardStatusMapForAPIs = cardStatusMapForAPIs;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public void setIssuerShortName(String issuerShortName) {
		this.issuerShortName = issuerShortName;
	}

	public void setMaskedCharacter(char maskedCharacter) {
		this.maskedCharacter = maskedCharacter;
	}

	public void setNonExpired(boolean nonExpired) {
		this.nonExpired = nonExpired;
	}

	public void setOrganisationsMap(Map<String, String> organisationsMap) {
		this.organisationsMap = organisationsMap;
	}

	public void setPanOrBin(String panOrBin) {
		this.panOrBin = panOrBin;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public void setSchemeType(String[] schemeType) {
		this.schemeType = schemeType;
	}

	public void setSchemeTypeMap(Map<String, String> schemeTypeMap) {
		this.schemeTypeMap = schemeTypeMap;
	}

	/**
	 * @param tokenId the tokenId to set
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	protected void normaliseCardExpiryDate() {

		// only pad the month with leading zeros to 2 digits
		// year must be 4 digits, and we dont pad.

		if ("".equals(expiryMonth)) {
			expiryMonth = EXPIRY_DATE_MM_DEFAULT;
		}

		if ("".equals(expiryYear)) {
			expiryYear = EXPIRY_DATE_YYYY_DEFAULT;
		}

		expiryMonth = pad(expiryMonth, 2, paddingChar);
		//		expiryYear = pad(expiryYear, 4, paddingChar);
	}

	protected String pad(String s, int length, char paddingChar) {
		while (s.length() < length) {
			s = paddingChar + s;
		}

		return s;
	}

	/**
	 * Logger object
	 */
	private static final Logger logger = LoggerFactory.getLogger(
		CardSearchForm.class);

	private static final char paddingChar = '0';

	/**
	 *  UI Form object for the Cards Search
	 *
	 * @author Rajni Bharara
	 */
	private static final long serialVersionUID = -4777291462476626057L;

	private String cardholderName;
	private String cardId;
	private String[] cardStatus;
	private Map<String, String> cardStatusMap;
	private Map<String, String> cardStatusMapForAPIs;
	private String deviceId;
	private String expiryMonth = EXPIRY_DATE_MM_DEFAULT;
	private String expiryYear = EXPIRY_DATE_YYYY_DEFAULT;
	private String issuer;
	private String issuerShortName;
	private char maskedCharacter = Constants.PAN_MASKED_CHARACTER;
	private boolean nonExpired;
	private Map<String, String> organisationsMap;
	private String panOrBin;
	private String postCode;
	private String[] schemeType;
	private Map<String, String> schemeTypeMap;
	private String tokenId;

}