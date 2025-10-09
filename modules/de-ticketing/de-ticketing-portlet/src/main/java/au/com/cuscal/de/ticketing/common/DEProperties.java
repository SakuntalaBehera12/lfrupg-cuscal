//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.common;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class DEProperties {

	public Properties getDeProperty() {
		return this.deProperty;
	}

	public void setDeProperty(final Properties deProperty) {
		this.deProperty = deProperty;
	}

	private Properties deProperty;

}