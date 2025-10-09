package au.com.cuscal.cards.domain;

import au.com.cuscal.cards.commons.Constants;
import au.com.cuscal.framework.cards.CardUtil;

import java.io.Serializable;

import java.util.Date;

/**
 * domain class
 *
 * @author Rajni Bharara
 *
 */
public class AccountInformation implements Serializable {

	public String getAccountAccountAccess() {
		return accountAccountAccess;
	}

	public String getAccountMapDetailsTxt() {
		return accountMapDetailsTxt;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getAccountQualifier() {
		return accountQualifier;
	}

	public String getAccountType() {
		return accountType;
	}

	public String getAvailableBalance() {
		return availableBalance;
	}

	public String getCreditLine() {
		return creditLine;
	}

	public String getFundingAccount() {
		return fundingAccount;
	}

	public String getInstitution() {
		return institution;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public String getLedgerBalance() {
		return ledgerBalance;
	}

	public char getMaskedCharacter() {
		return maskedCharacter;
	}

	public String getMaskedPan() {
		return CardUtil.getInstance(
		).mask(
			this.pan, this.maskedCharacter
		);
	}

	// UI text

	public String getMsgAccountInfoTxt() {
		return msgAccountInfoTxt;
	}

	public String getPan() {
		return pan;
	}

	public String getPrimaryAccount() {
		return primaryAccount;
	}

	public void setAccountAccountAccess(String accountAccountAccess) {
		this.accountAccountAccess = accountAccountAccess;
	}

	public void setAccountMapDetailsTxt(String accountMapDetailsTxt) {
		this.accountMapDetailsTxt = accountMapDetailsTxt;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setAccountQualifier(String accountQualifier) {
		this.accountQualifier = accountQualifier;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	public void setCreditLine(String creditLine) {
		this.creditLine = creditLine;
	}

	public void setFundingAccount(String fundingAccount) {
		this.fundingAccount = fundingAccount;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setLedgerBalance(String ledgerBalance) {
		this.ledgerBalance = ledgerBalance;
	}

	public void setMaskedCharacter(char maskedCharacter) {
		this.maskedCharacter = maskedCharacter;
	}

	public void setMsgAccountInfoTxt(String msgAccountInfoTxt) {
		this.msgAccountInfoTxt = msgAccountInfoTxt;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public void setPrimaryAccount(String primaryAccount) {
		this.primaryAccount = primaryAccount;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -6609286053101263230L;

	private String accountAccountAccess = new String("Account Access");
	private String accountMapDetailsTxt = new String("Account Mapping Details");
	private String accountNumber;
	private String accountQualifier;
	private String accountType;
	private String availableBalance;
	private String creditLine;
	private String fundingAccount;
	private String institution;
	private Date lastUpdated;
	private String ledgerBalance;
	private char maskedCharacter = Constants.PAN_MASKED_CHARACTER;
	private String msgAccountInfoTxt = new String(
		"-----------Balance information is unavailable-----------");
	private String pan;
	private String primaryAccount;

}