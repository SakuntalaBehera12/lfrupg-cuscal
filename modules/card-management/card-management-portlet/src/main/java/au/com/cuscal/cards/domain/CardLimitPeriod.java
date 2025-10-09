package au.com.cuscal.cards.domain;

public enum CardLimitPeriod {

	TRANSACTION("transaction"), DAY("day"), WEEK("week");

	public static CardLimitPeriod getCardLimitPeriod(String name) {
		for (CardLimitPeriod cardLimitPeriod : CardLimitPeriod.values()) {
			if (cardLimitPeriod.name.equalsIgnoreCase(name)) {
				return cardLimitPeriod;
			}
		}

		return null;
	}

	CardLimitPeriod(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private final String name;

}