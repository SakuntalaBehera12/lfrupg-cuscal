//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.ticketing.common;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class SelfServiceProperties {

	public Properties getSelfserviceProperty() {
		return this.selfserviceProperty;
	}

	public void setSelfserviceProperty(final Properties selfserviceProperty) {
		this.selfserviceProperty = selfserviceProperty;
	}

	private Properties selfserviceProperty;

}