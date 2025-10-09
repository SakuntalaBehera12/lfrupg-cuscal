package au.com.cuscal.cards.domain;

public class RouteCard {

	public String getResponse() {
		return response;
	}

	public RouteCard response(String response) {
		this.response = response;

		return this;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	private String response;

}