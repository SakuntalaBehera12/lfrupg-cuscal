package au.com.cuscal.cards.domain;

public class CardData {

	public CardData build(String pan, String cuscalToken, String institution) {
		this.pan = pan;
		this.cuscalToken = cuscalToken;
		this.institution = institution;

		return this;
	}

	public String getCuscalToken() {
		return cuscalToken;
	}

	public String getInstitution() {
		return institution;
	}

	public String getPan() {
		return pan;
	}

	public void setCuscalToken(String cuscalToken) {
		this.cuscalToken = cuscalToken;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	private String cuscalToken;
	private String institution;
	private String pan;

}