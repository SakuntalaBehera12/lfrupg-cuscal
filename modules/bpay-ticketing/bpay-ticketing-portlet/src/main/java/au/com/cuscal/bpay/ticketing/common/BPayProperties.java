package au.com.cuscal.bpay.ticketing.common;

import java.util.Properties;

import org.springframework.stereotype.Component;

/**
 * Represents the persistent set of BPay Properties.
 *
 *
 */
@Component
public class BPayProperties {

	/**
	 * @return the bpayProperty
	 */
	public Properties getBpayProperty() {
		return bpayProperty;
	}

	/**
	 * @param bpayProperty the bpayProperty to set
	 */
	public void setBpayProperty(Properties bpayProperty) {
		this.bpayProperty = bpayProperty;
	}

	private Properties bpayProperty;

}