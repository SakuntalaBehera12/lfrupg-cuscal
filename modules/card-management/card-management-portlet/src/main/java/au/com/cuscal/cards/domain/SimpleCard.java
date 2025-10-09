package au.com.cuscal.cards.domain;

public class SimpleCard implements Comparable<SimpleCard> {

	public SimpleCard(String cardId, String pan) {
		this.cardId = cardId;
		this.pan = pan;
	}

	@Override
	public int compareTo(SimpleCard o) {
		return pan.compareTo(o.getPan());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)

			return true;

		if (obj == null)

			return false;

		if (getClass() != obj.getClass())

			return false;

		SimpleCard other = (SimpleCard)obj;

		if (pan == null) {
			if (other.pan != null)

				return false;
		}

		return pan.equals(other.pan);
	}

	public String getCardId() {
		return this.cardId;
	}

	public String getPan() {
		return this.pan;
	}

	@Override
	public int hashCode() {
		return cardId.hashCode();
	}

	public String cardId;
	public String pan;

}