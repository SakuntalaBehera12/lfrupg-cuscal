package au.com.cuscal.cards.domain;

public class Mcc {

	public Mcc code(Long code) {
		this.code = code;

		return this;
	}

	public Long getCode() {
		return code;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public Mcc merchantType(String merchantType) {
		this.merchantType = merchantType;

		return this;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	private Long code;
	private String merchantType;

}