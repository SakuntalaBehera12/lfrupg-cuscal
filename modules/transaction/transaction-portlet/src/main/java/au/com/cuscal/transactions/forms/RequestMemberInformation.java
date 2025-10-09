package au.com.cuscal.transactions.forms;

public class RequestMemberInformation {

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

	private String memberName;
	private String memberNumber;

}