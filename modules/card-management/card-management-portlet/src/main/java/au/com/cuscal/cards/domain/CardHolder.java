package au.com.cuscal.cards.domain;

import java.io.Serializable;

/**
 * domain class
 *
 * @author Rajni Bharara
 *
 */
public class CardHolder implements Serializable {

	public String getCardHolderAddress() {
		return cardHolderAddress;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderAddress(String cardHolderAddress) {
		this.cardHolderAddress = cardHolderAddress;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -8230848867280796322L;

	private String cardHolderAddress;
	private String cardHolderName;

}