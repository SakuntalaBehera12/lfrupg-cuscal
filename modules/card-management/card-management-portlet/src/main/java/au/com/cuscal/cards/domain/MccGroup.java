package au.com.cuscal.cards.domain;

import java.util.List;

public class MccGroup extends BaseCardControl {

	public MccGroup description(String description) {
		setDescription(description);

		return this;
	}

	public MccGroup displayOrder(Long displayOrder) {
		setDisplayOrder(displayOrder);

		return this;
	}

	public List<Mcc> getMccs() {
		return mccs;
	}

	public String getValue() {
		return value;
	}

	public MccGroup id(Long id) {
		setId(id);

		return this;
	}

	public MccGroup mccs(List<Mcc> mccs) {
		this.mccs = mccs;

		return this;
	}

	public MccGroup name(String name) {
		setName(name);

		return this;
	}

	public void setMccs(List<Mcc> mccs) {
		this.mccs = mccs;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MccGroup value(String value) {
		this.value = value;

		return this;
	}

	private List<Mcc> mccs;
	private String value;

}