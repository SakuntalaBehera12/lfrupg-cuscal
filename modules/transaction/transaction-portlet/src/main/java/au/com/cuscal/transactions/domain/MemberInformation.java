package au.com.cuscal.transactions.domain;

import java.io.Serializable;

public class MemberInformation implements Serializable {

	public String getMemberName() {
		return memberName;
	}

	public String getMemberNumber() {
		return memberNumber;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 4559175847590072496L;

	private String memberName;
	private String memberNumber;

}