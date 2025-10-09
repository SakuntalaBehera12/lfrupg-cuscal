package au.com.cuscal.chargeback.forms;

public class CreditInformation {

	public String getCreditAmount() {
		return creditAmount;
	}

	public String getCreditVoucherDated() {
		return creditVoucherDated;
	}

	public String getCreditVoucherGiven() {
		return creditVoucherGiven;
	}

	public String getDateCreditVoucher() {
		return dateCreditVoucher;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public void setCreditVoucherDated(String creditVoucherDated) {
		this.creditVoucherDated = creditVoucherDated;
	}

	public void setCreditVoucherGiven(String creditVoucherGiven) {
		this.creditVoucherGiven = creditVoucherGiven;
	}

	public void setDateCreditVoucher(String dateCreditVoucher) {
		this.dateCreditVoucher = dateCreditVoucher;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	private String creditAmount;
	private String creditVoucherDated;
	private String creditVoucherGiven;
	private String dateCreditVoucher;
	private String invoiceNumber;

}