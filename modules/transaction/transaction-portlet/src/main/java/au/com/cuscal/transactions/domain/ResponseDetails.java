package au.com.cuscal.transactions.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;

@JsonAutoDetect
public class ResponseDetails implements Serializable {

	public String getAuthorisationId() {
		return authorisationId;
	}

	public String getAuthorisedBy() {
		return authorisedBy;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getSwitchedBy() {
		return switchedBy;
	}

	public void setAuthorisationId(String authorisationId) {
		this.authorisationId = authorisationId;
	}

	public void setAuthorisedBy(String authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSwitchedBy(String switchedBy) {
		this.switchedBy = switchedBy;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 4117086456172008075L;

	private String authorisationId;
	private String authorisedBy;
	private String code;
	private String description;
	private String switchedBy;

}