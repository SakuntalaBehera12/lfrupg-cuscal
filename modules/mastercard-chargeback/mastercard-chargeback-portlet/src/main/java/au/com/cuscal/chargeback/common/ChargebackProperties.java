//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.chargeback.common;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class ChargebackProperties {

	public Properties getChargebackProperty() {
		return this.chargebackProperty;
	}

	public void setChargebackProperty(final Properties chargebackProperty) {
		this.chargebackProperty = chargebackProperty;
	}

	private Properties chargebackProperty;

}