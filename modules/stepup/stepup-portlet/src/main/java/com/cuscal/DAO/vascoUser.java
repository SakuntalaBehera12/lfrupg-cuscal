package com.cuscal.DAO;

public class vascoUser {

	public vascoUser() {
	}

	public String getPassword() {
		return password;
	}

	public String getTokenID() {
		return tokenID;
	}

	public String getUserID() {
		return userID;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	private String password;
	private String tokenID;
	private String userID;

}