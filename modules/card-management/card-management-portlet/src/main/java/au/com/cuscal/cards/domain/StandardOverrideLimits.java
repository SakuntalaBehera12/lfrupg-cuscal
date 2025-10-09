package au.com.cuscal.cards.domain;

import java.io.Serializable;

/**
 * domain class
 *
 * @author Rajni Bharara
 *
 */
public class StandardOverrideLimits implements Serializable {

	public String getLimitAmount() {
		return limitAmount;
	}

	public String getLimitCount() {
		return limitCount;
	}

	public String getLimitType() {
		return limitType;
	}

	public String getUsageAmount() {
		return usageAmount;
	}

	public String getUsageCount() {
		return usageCount;
	}

	public void setLimitAmount(String limitAmount) {
		this.limitAmount = limitAmount;
	}

	public void setLimitCount(String limitCount) {
		this.limitCount = limitCount;
	}

	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	public void setUsageAmount(String usageAmount) {
		this.usageAmount = usageAmount;
	}

	public void setUsageCount(String usageCount) {
		this.usageCount = usageCount;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -2088016458339888630L;

	private String limitAmount;
	private String limitCount;
	private String limitType;
	private String usageAmount;
	private String usageCount;

}