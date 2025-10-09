package au.com.cuscal.cards.domain;

import java.io.Serializable;

import java.util.List;

public class CardChannelPermission implements Serializable {

	public List<Access> getAccessTypes() {
		return accessTypes;
	}

	public String getChannelPermissionName() {
		return channelPermissionName;
	}

	public void setAccessTypes(List<Access> accessTypes) {
		this.accessTypes = accessTypes;
	}

	public void setChannelPermissionName(String channelPermissionName) {
		this.channelPermissionName = channelPermissionName;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 197026018537519006L;

	private List<Access> accessTypes;
	private String channelPermissionName;

}