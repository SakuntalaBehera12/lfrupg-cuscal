package au.com.cuscal.cards.domain;

import java.io.Serializable;

/**
 * domain class
 *
 * @author Rajni Bharara
 *
 */
public class Access implements Serializable {

	public String getAccessAvailable() {
		return accessAvailable;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessAvailable(String accessAvailable) {
		this.accessAvailable = accessAvailable;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 4162250205957415621L;

	private String accessAvailable;
	private String accessType;

}