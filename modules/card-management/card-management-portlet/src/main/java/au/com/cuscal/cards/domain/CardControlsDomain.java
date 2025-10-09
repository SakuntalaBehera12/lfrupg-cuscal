package au.com.cuscal.cards.domain;

import java.io.Serializable;

public class CardControlsDomain implements Serializable {

	public CardControlsDomain() {
	}

	public CardControlsDomain(
		String controlText, String controlDescription, String controlId,
		String hideIfEnabled, String displayType, long displayOrder,
		String currentValue) {

		this.controlText = controlText;
		this.controlDescription = controlDescription;
		this.controlId = controlId;
		this.hideIfEnabled = hideIfEnabled;
		this.displayType = displayType;
		this.displayOrder = displayOrder;
		this.currentValue = currentValue;
	}

	public String getControlDescription() {
		return controlDescription;
	}

	public String getControlId() {
		return controlId;
	}

	public String getControlText() {
		return controlText;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public long getDisplayOrder() {
		return displayOrder;
	}

	public String getDisplayType() {
		return displayType;
	}

	public String getHideIfEnabled() {
		return hideIfEnabled;
	}

	public void setControlDescription(String controlDescription) {
		this.controlDescription = controlDescription;
	}

	public void setControlId(String controlId) {
		this.controlId = controlId;
	}

	public void setControlText(String controlText) {
		this.controlText = controlText;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public void setDisplayOrder(long displayOrder) {
		this.displayOrder = displayOrder;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public void setHideIfEnabled(String hideIfEnabled) {
		this.hideIfEnabled = hideIfEnabled;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String controlDescription;
	private String controlId;
	private String controlText;
	private String currentValue;
	private long displayOrder;
	private String displayType;
	private String hideIfEnabled;

}