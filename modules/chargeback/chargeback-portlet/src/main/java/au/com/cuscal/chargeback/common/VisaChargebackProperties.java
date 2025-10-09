package au.com.cuscal.chargeback.common;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class VisaChargebackProperties {

	public Properties getVisaChargebackProperty() {
		return visaChargebackProperty;
	}

	public void setVisaChargebackProperty(Properties visaChargebackProperty) {
		this.visaChargebackProperty = visaChargebackProperty;
	}

	private Properties visaChargebackProperty;

}