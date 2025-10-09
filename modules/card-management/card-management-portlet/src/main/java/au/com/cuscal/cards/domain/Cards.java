package au.com.cuscal.cards.domain;

import au.com.cuscal.cards.commons.Constants;
import au.com.cuscal.framework.cards.CardUtil;

import java.io.Serializable;

import java.util.Date;

/**
 * domain class
 *
 * @author Rajni Bharara
 *
 */
public class Cards implements Serializable {

	public Cards() {
	}

	public Cards(Builder builder) {
		this.pan = builder.pan;
		this.cardHolder = builder.cardHolder;
		this.address = builder.address;
		this.institution = builder.institution;
		this.status = builder.status;
		this.expiryDate = builder.expiryDate;
		this.lastUpdated = builder.lastUpdated;
		this.cardId = builder.cardId;
		this.totalListSize = builder.totalListSize;
		this.cuscalToken = builder.cuscalToken;
	}

	public String getAddress() {
		return address;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public String getCardId() {
		return cardId;
	}

	public String getCuscalToken() {
		return cuscalToken;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public String getInstitution() {
		return institution;
	}

	public Date getLastUpdated() {
		return lastUpdated;
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

	public String getPan() {
		return pan;
	}

	public String getStatus() {
		return status;
	}

	public int getTotalListSize() {
		return totalListSize;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public void setCuscalToken(String cuscalToken) {
		this.cuscalToken = cuscalToken;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setMaskedCharacter(char maskedCharacter) {
		this.maskedCharacter = maskedCharacter;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTotalListSize(int totalListSize) {
		this.totalListSize = totalListSize;
	}

	public String toString() {
		return "maskedPan-" + this.getMaskedPan() + ", " + "cuscalToken-" +
			this.cuscalToken + ", " + "institution-" + this.institution + ", " +
				"address-" + this.address + ", " + "cardHolder-" +
					this.cardHolder + ", " + "status-" + this.status + ", " +
						"expiryDate-" + this.expiryDate + ", " +
							"lastUpdated-" + this.lastUpdated + ", " +
								"totalListSize-" + this.totalListSize + ", " +
									"cardId-" + this.cardId + ", ";
	}

	public static class Builder {

		public Builder() {
		}

		public Cards build() {
			return new Cards(this);
		}

		public Builder setAddress(String address) {
			this.address = address;

			return this;
		}

		public Builder setCardHolder(String cardHolder) {
			this.cardHolder = cardHolder;

			return this;
		}

		public Builder setCardId(String cardId) {
			this.cardId = cardId;

			return this;
		}

		public Builder setCuscalToken(String cuscalToken) {
			this.cuscalToken = cuscalToken;

			return this;
		}

		public Builder setExpiryDate(String expiryDate) {
			this.expiryDate = expiryDate;

			return this;
		}

		public Builder setInstitution(String institution) {
			this.institution = institution;

			return this;
		}

		public Builder setLastUpdated(Date lastUpdated) {
			this.lastUpdated = lastUpdated;

			return this;
		}

		public Builder setPan(String pan) {
			this.pan = pan;

			return this;
		}

		public Builder setStatus(String status) {
			this.status = status;

			return this;
		}

		public Builder setTotalListSize(int totalListSize) {
			this.totalListSize = totalListSize;

			return this;
		}

		private String address;
		private String cardHolder;
		private String cardId;
		private String cuscalToken;
		private String expiryDate;
		private String institution;
		private Date lastUpdated;
		private String pan;
		private String status;
		private int totalListSize;

	}

	/**
	 *
	 */
	private static final long serialVersionUID = 5557225814823580892L;

	private String address;
	private String cardHolder;
	private String cardId;
	private String cuscalToken;
	private String expiryDate;
	private String institution;
	private Date lastUpdated;
	private char maskedCharacter = Constants.PAN_MASKED_CHARACTER;
	private String pan;
	private String status;
	private int totalListSize;

}