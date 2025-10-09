package au.com.cuscal.cards.domain;

public class BaseCardControl {

	/**
	 * A verbose description of the control
	 **/
	public String getDescription() {
		return description;
	}

	/**
	 * Recommended order to display this control in relation to other controls in
	 * your UI
	 **/
	public Long getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * The unique ID of the card control
	 **/
	public Long getId() {
		return id;
	}

	/**
	 * The name of the card control
	 **/
	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String description;
	private Long displayOrder;
	private Long id;
	private String name;

}