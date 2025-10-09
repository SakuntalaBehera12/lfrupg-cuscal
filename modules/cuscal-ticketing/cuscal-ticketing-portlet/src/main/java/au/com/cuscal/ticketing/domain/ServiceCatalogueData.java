//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.domain;

import java.io.Serializable;

import java.util.HashMap;

public class ServiceCatalogueData implements Serializable {

	public HashMap<Long, String> getDescriptionMap() {
		return this.descriptionMap;
	}

	public HashMap<Long, String> getIdMap() {
		return this.idMap;
	}

	public HashMap<Long, String> getTypeIdMap() {
		return this.typeIdMap;
	}

	public HashMap<Long, String> getTypeMap() {
		return this.typeMap;
	}

	public HashMap<Long, String> getUrlMap() {
		return this.urlMap;
	}

	public void setDescriptionMap(final HashMap<Long, String> descriptionMap) {
		this.descriptionMap = descriptionMap;
	}

	public void setIdMap(final HashMap<Long, String> idMap) {
		this.idMap = idMap;
	}

	public void setTypeIdMap(final HashMap<Long, String> typeIdMap) {
		this.typeIdMap = typeIdMap;
	}

	public void setTypeMap(final HashMap<Long, String> typeMap) {
		this.typeMap = typeMap;
	}

	public void setUrlMap(final HashMap<Long, String> urlMap) {
		this.urlMap = urlMap;
	}

	private static final long serialVersionUID = 1L;

	private HashMap<Long, String> descriptionMap;
	private HashMap<Long, String> idMap;
	private HashMap<Long, String> typeIdMap;
	private HashMap<Long, String> typeMap;
	private HashMap<Long, String> urlMap;

}