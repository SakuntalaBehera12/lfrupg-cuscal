package au.com.cuscal.cards.domain;

public class Key {

	public Key() {
	}

	public Key(String identifier, String publicKey) {
		this.identifier = identifier;
		this.publicKey = publicKey;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public Key response(String identifier, String publicKey) {
		this.identifier = identifier;
		this.publicKey = publicKey;

		return this;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	private String identifier;
	private String publicKey;

}