package au.com.cuscal.cards.domain;

public enum CardLimitStatus {

	ON("on"), OFF("off"), SUSPENDED("suspended");

	public static CardLimitStatus getCardLimitStatus(String name) {
		for (CardLimitStatus cardLimitStatus : CardLimitStatus.values()) {
			if (cardLimitStatus.name.equalsIgnoreCase(name)) {
				return cardLimitStatus;
			}
		}

		return null;
	}

	CardLimitStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private final String name;

}