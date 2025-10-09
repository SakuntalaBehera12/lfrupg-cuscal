package au.com.cuscal.cards.domain;

import au.com.cuscal.cards.commons.Constants;
import au.com.cuscal.framework.cards.CardUtil;

import java.io.Serializable;

import java.util.List;

/**
 * domain class
 *
 * @author Rajni Bharara
 *
 */
public class CardLimits implements Serializable {

	public String getInstitution() {
		return institution;
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

	public String getPan() {
		return pan;
	}

	public String getStandardOverLimistTxt() {
		return standardOverLimistTxt;
	}

	// txt for UI

	public List<StandardOverrideLimits> getStandardOverrideLimits() {
		return standardOverrideLimits;
	}

	public String getUsageInfoTxt() {
		return usageInfoTxt;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public void setMaskedCharacter(char maskedCharacter) {
		this.maskedCharacter = maskedCharacter;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public void setStandardOverLimistTxt(String standardOverLimistTxt) {
		this.standardOverLimistTxt = standardOverLimistTxt;
	}

	public void setStandardOverrideLimits(
		List<StandardOverrideLimits> standardOverrideLimits) {

		this.standardOverrideLimits = standardOverrideLimits;
	}

	public void setUsageInfoTxt(String usageInfoTxt) {
		this.usageInfoTxt = usageInfoTxt;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -2532572176582787489L;

	private String institution;
	private char maskedCharacter = Constants.PAN_MASKED_CHARACTER;
	private String pan;
	private String standardOverLimistTxt = new String(
		"Standard/Override Limits");
	private List<StandardOverrideLimits> standardOverrideLimits;
	private String usageInfoTxt = new String("Usage Information");

}