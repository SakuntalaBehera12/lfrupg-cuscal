package au.com.cuscal.cards.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardLimit {

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	public long getLimit() {
		return limit;
	}

	public Map<String, String> getLimitActionMap() {
		return limitActionMap;
	}

	public long getMaxLimit() {
		return maxLimit;
	}

	public long getMinLimit() {
		return minLimit;
	}

	public String getName() {
		return name;
	}

	public String getPeriod() {
		return period;
	}

	public Date getResumeDate() {
		return resumeDate;
	}

	public String getSpend() {
		return spend;
	}

	public String getStatus() {
		return status;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public void setLimitActionMap(Map<String, String> limitActionMap) {
		this.limitActionMap = limitActionMap;
	}

	public void setMaxLimit(long maxLimit) {
		this.maxLimit = maxLimit;
	}

	public void setMinLimit(long minLimit) {
		this.minLimit = minLimit;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public void setResumeDate(Date resumeDate) {
		this.resumeDate = resumeDate;
	}

	public void setSpend(String spend) {
		this.spend = spend;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String description;
	private long id;
	private long limit;
	private Map<String, String> limitActionMap;
	private long maxLimit;
	private long minLimit;
	private String name;
	private String period;
	private Date resumeDate;
	private String spend;
	private String status;

}