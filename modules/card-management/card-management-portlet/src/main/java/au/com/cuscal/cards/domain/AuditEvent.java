package au.com.cuscal.cards.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditEvent {

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public String getChannelType() {
		return channelType;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public String getCuscalToken() {
		return cuscalToken;
	}

	public String getDeviceHash() {
		return deviceHash;
	}

	public String getDirection() {
		return direction;
	}

	public String getDomain() {
		return domain;
	}

	public String getEventAction() {
		return eventAction;
	}

	public Date getEventTimestamp() {
		return eventTimestamp;
	}

	public String getEventType() {
		return eventType;
	}

	public String getGatewayName() {
		return gatewayName;
	}

	public long getId() {
		return id;
	}

	public String getInstitutionId() {
		return institutionId;
	}

	public String getIvUser() {
		return ivUser;
	}

	public Long getProcessingTime() {
		return processingTime;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getResponse() {
		return response;
	}

	public String getSubDomain() {
		return subDomain;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public void setCuscalToken(String cuscalToken) {
		this.cuscalToken = cuscalToken;
	}

	public void setDeviceHash(String deviceHash) {
		this.deviceHash = deviceHash;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setEventAction(String eventAction) {
		this.eventAction = eventAction;
	}

	public void setEventTimestamp(Date eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setGatewayName(String gatewayName) {
		this.gatewayName = gatewayName;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}

	public void setIvUser(String ivUser) {
		this.ivUser = ivUser;
	}

	public void setProcessingTime(Long processingTime) {
		this.processingTime = processingTime;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	private Map<String, String> attributes;
	private String channelType;
	private String correlationId;
	private String cuscalToken;
	private String deviceHash;
	private String direction;
	private String domain;
	private String eventAction;
	private Date eventTimestamp;
	private String eventType;
	private String gatewayName;
	private long id;
	private String institutionId;
	private String ivUser;
	private Long processingTime;
	private String resourceName;
	private String response;
	private String subDomain;
	private String userId;
	private Integer version;

}