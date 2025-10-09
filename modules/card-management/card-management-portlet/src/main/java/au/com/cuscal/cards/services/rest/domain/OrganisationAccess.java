package au.com.cuscal.cards.services.rest.domain;

public class OrganisationAccess {

	public OrganisationAccess appKey(String appKey) {
		this.appKey = appKey;

		return this;
	}

	public OrganisationAccess appName(String appName) {
		this.appName = appName;

		return this;
	}

	public String getAppKey() {
		return appKey;
	}

	public String getAppName() {
		return appName;
	}

	public String getInstitutionId() {
		return institutionId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public String getOrganisationId() {
		return organisationId;
	}

	public String getOrganisationType() {
		return organisationType;
	}

	public String getUserId() {
		return userId;
	}

	public OrganisationAccess institutionId(String institutionId) {
		this.institutionId = institutionId;

		return this;
	}

	public OrganisationAccess organisationId(String organisationId) {
		this.organisationId = organisationId;

		return this;
	}

	public OrganisationAccess organisationType(String organisationType) {
		this.organisationType = organisationType;

		return this;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId;
	}

	public void setOrganisationType(String organisationType) {
		this.organisationType = organisationType;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public OrganisationAccess userId(String userId) {
		this.userId = userId;

		return this;
	}

	private String appKey;
	private String appName;
	private String institutionId;
	private String operatorId;
	private String organisationId;
	private String organisationType;
	private String userId;

}