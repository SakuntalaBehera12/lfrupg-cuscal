package au.com.cuscal.cards.domain.ui;

import au.com.cuscal.framework.webservices.scheme.Token;
import au.com.cuscal.framework.webservices.scheme.TokenActivityEntity;
import au.com.cuscal.framework.webservices.scheme.TokenDetailTokenType;
import au.com.cuscal.framework.webservices.scheme.TokenDetailType;
import au.com.cuscal.framework.webservices.scheme.TokenUniqueReference;

import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

public class DecoratedToken {

	public static DecoratedToken newDecoratedToken(TokenDetailType token) {
		DecoratedToken decoratedToken = new DecoratedToken();

		if (token.getDeviceInfo() != null) {
			decoratedToken.setDeviceName(
				token.getDeviceInfo(
				).getDeviceName());
			decoratedToken.setDeviceNumber(
				token.getDeviceInfo(
				).getDeviceNumber());
		}

		if (token.getTokenInfo() != null) {
			decoratedToken.setStatus(
				token.getTokenInfo(
				).getTokenStatus());
		}

		decoratedToken.setFlowType("N/A");
		System.out.println(
			"token.getTokenInfo().getFlowType().value() " +
				token.getTokenInfo(
				).getFlowType());

		if (null != token.getTokenInfo(
			).getFlowType()) {

			String flowType = token.getTokenInfo(
			).getFlowType(
			).value();

			if ("R".equalsIgnoreCase(flowType)) {
				decoratedToken.setFlowType("Red");
			}
			else if ("Y".equalsIgnoreCase(flowType)) {
				decoratedToken.setFlowType("Yellow");
			}
			else if ("O".equalsIgnoreCase(flowType)) {
				decoratedToken.setFlowType("Orange");
			}
			else if ("G".equalsIgnoreCase(flowType)) {
				decoratedToken.setFlowType("Green");
			}
		}

		StringBuffer sb = new StringBuffer();

		if (token.getUpdatedBy() != null) {
			List<TokenActivityEntity> updaterList = token.getUpdatedBy(
			).getUpdater();

			if ((updaterList == null) || updaterList.isEmpty()) {
				sb.append("NOT AVAILABLE");
			}
			else {
				for (int i = 0;; sb.append(";")) {
					sb.append(updaterList.get(i));
					i++;

					if (i == updaterList.size()) {
						break;
					}
				}
			}
		}
		else {
			sb.append("NOT AVAILABLE");
		}

		decoratedToken.setLastUpdatedBy(
			sb.toString(
			).trim());
		decoratedToken.setLastUpdatedTime(
			getDate(token.getLastStatusUpdatedTime()));
		decoratedToken.setCuscalToken(token.getCuscalToken());

		if (token.getTokenInfo(
			).getToken() != null) {

			decoratedToken.setToken(
				token.getTokenInfo(
				).getToken(
				).toString());
		}
		else if (StringUtils.isNotBlank(token.getTokenSuffix())) {
			decoratedToken.setToken(token.getTokenSuffix());
		}

		decoratedToken.setScheme(
			token.getScheme(
			).toString());
		decoratedToken.setTokenId(
			token.getTokenInfo(
			).getTokenReference(
			).getTokenReferenceId());
		decoratedToken.setTokenRequster(getTokenRequester(token));
		decoratedToken.setAuxilaryToken(isAuxilaryToken(token));
		final String panSource = token.getPanSource();

		if ("API".equalsIgnoreCase(panSource)) {
			decoratedToken.setProvisioningSource("Issuer app");
		}
		else if ("COF".equalsIgnoreCase(panSource)) {
			decoratedToken.setProvisioningSource("Card on file");
		}
		else if ("Manual".equalsIgnoreCase(panSource)) {
			decoratedToken.setProvisioningSource("Wallet app");
		}

		return decoratedToken;
	}

	/**
	 * @return the cuscalToken
	 */
	public String getCuscalToken() {
		return cuscalToken;
	}

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * @return the deviceNumber
	 */
	public String getDeviceNumber() {
		return deviceNumber;
	}

	/**
	 *
	 * @return the flowType
	 */
	public String getFlowType() {
		return flowType;
	}

	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * @return the lastUpdatedTime
	 */
	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public String getMaskedToken() {
		if (token != null) {
			return "xxxx xxxx xxxx " + token.substring(token.length() - 4);
		}

		return "";
	}

	/**
	 * @return the provisioningSource
	 */
	public String getProvisioningSource() {
		return provisioningSource;
	}

	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	public String getStatusDescription() {
		if ("I".equals(status))

			return "Inactive";

		if ("L".equals(status)) // added for story PAYS2-3913

			return "Incomplete provisioning";

		if ("A".equals(status))

			return "Active";

		if ("S".equals(status))

			return "Suspended";

		if ("D".equals(status))

			return "Deactivated(Deleted)";

		return status;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the tokenId
	 */
	public String getTokenId() {
		return tokenId;
	}

	/**
	 * @return the tokenRequster
	 */
	public String getTokenRequster() {
		return tokenRequster;
	}

	/**
	 * @return the auxilaryToken
	 */
	public boolean isAuxilaryToken() {
		return auxilaryToken;
	}

	/**
	 * @param auxilaryToken the auxilaryToken to set
	 */
	public void setAuxilaryToken(boolean auxilaryToken) {
		this.auxilaryToken = auxilaryToken;
	}

	/**
	 * @param cuscalToken
	 *            the cuscalToken to set
	 */
	public void setCuscalToken(String cuscalToken) {
		this.cuscalToken = cuscalToken;
	}

	/**
	 * @param deviceName
	 *            the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @param deviceNumber
	 *            the deviceNumber to set
	 */
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	/**
	 *
	 * @param flowType to set
	 */
	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	/**
	 * @param lastUpdatedBy
	 *            the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * @param lastUpdatedTime
	 *            the lastUpdatedTime to set
	 */
	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	/**
	 * @param provisioningSource the provisioningSource to set
	 */
	public void setProvisioningSource(String provisioningSource) {
		this.provisioningSource = provisioningSource;
	}

	/**
	 * @param scheme
	 *            the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @param tokenId
	 *            the tokenId to set
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	/**
	 * @param tokenRequster the tokenRequster to set
	 */
	public void setTokenRequster(String tokenRequster) {
		this.tokenRequster = tokenRequster;
	}

	private static Date getDate(XMLGregorianCalendar cal) {
		if (cal == null)

			return null;

		return cal.toGregorianCalendar(
		).getTime();
	}

	/**
	 * a. searchTokensResponse/tokenDetails/type = WALLET
	 *		i. use searchTokensResponse/tokenDetails/walletType
	 * b. searchTokensResponse/tokenDetails/type = MERCHANT
	 *		i. use searchTokensResponse/tokenDetails/tokenInfo/tokenReference/walletProviderEntityName
	 *		ii. if not found, searchTokensResponse/tokenDetails/tokenInfo/tokenReference/walletProviderName
	 */
	private static String getTokenRequester(TokenDetailType tokenDetails) {
		if (tokenDetails == null) {
			return "";
		}

		switch (tokenDetails.getType()) {
			case WALLET:
				return tokenDetails.getWalletType();
			case MERCHANT:
				Token token = tokenDetails.getTokenInfo();

				if (token != null) {
					TokenUniqueReference tokenReference =
						token.getTokenReference();

					if (tokenReference != null) {
						if (StringUtils.isNotEmpty(
								tokenReference.getWalletProviderEntityName())) {

							return tokenReference.getWalletProviderEntityName();
						}

						return tokenReference.getWalletProviderName();
					}
				}

			default:
				return "";
		}
	}

	/**
	 * Disable editing of token, if both conditions are true (if it is an auxilary/secondary Wallet token it is an MND token)
	 *		i. searchTokensResponse/tokenDetails/type = WALLET
	 *		ii. searchTokensResponse/tokenDetails/auxiliaryToken = Y
	 */
	private static boolean isAuxilaryToken(TokenDetailType tokenDetails) {
		if (tokenDetails == null) {
			return false;
		}

		if ((tokenDetails.getType() == TokenDetailTokenType.WALLET) &&
			"Y".equalsIgnoreCase(tokenDetails.getAuxiliaryToken())) {

			return true;
		}

		return false;
	}

	private boolean auxilaryToken;
	private String cuscalToken;
	private String deviceName;
	private String deviceNumber;
	private String flowType;
	private String lastUpdatedBy;
	private Date lastUpdatedTime;
	private String provisioningSource;
	private String scheme;
	private String status;
	private String token;
	private String tokenId;
	private String tokenRequster;

}