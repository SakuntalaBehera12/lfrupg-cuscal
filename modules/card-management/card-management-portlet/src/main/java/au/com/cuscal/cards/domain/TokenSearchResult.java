package au.com.cuscal.cards.domain;

import au.com.cuscal.framework.webservices.scheme.TokenDetailType;

import java.util.List;

public class TokenSearchResult {

	public List<TokenDetailType> getTokens() {
		return tokens;
	}

	public boolean isPartialSuccess() {
		return partialSuccess;
	}

	public void setPartialSuccess(boolean partialSuccess) {
		this.partialSuccess = partialSuccess;
	}

	public void setTokens(List<TokenDetailType> tokens) {
		this.tokens = tokens;
	}

	private boolean partialSuccess;
	private List<TokenDetailType> tokens;

}