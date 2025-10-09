package au.com.cuscal.transactions.domain;

public class JsonResponseStatus {

	public Object getResult() {
		return result;
	}

	public String getStatus() {
		return status;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private Object result = null;
	private String status = null;

}