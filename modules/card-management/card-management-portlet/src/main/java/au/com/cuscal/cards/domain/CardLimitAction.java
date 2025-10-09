package au.com.cuscal.cards.domain;

public enum CardLimitAction {

	SET("Set limit", "on"), REMOVE("Remove limit", "off"),
	SUSPEND("Suspend limit", "suspended"),
	RESUME("Remove suspension", "resume");

	public static CardLimitAction getCardLimitActionFromCode(String code) {
		for (CardLimitAction cardLimitAction : CardLimitAction.values()) {
			if (cardLimitAction.code.equalsIgnoreCase(code)) {
				return cardLimitAction;
			}
		}

		return null;
	}

	public static CardLimitAction getCardLimitActionFromName(String name) {
		for (CardLimitAction cardLimitAction : CardLimitAction.values()) {
			if (cardLimitAction.name.equalsIgnoreCase(name)) {
				return cardLimitAction;
			}
		}

		return null;
	}

	CardLimitAction(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	private final String code;
	private final String name;

}