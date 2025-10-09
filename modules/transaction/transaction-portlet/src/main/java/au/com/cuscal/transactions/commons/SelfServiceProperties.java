package au.com.cuscal.transactions.commons;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class SelfServiceProperties {

	public Properties getSelfserviceProperty() {
		return selfserviceProperty;
	}

	public void setSelfserviceProperty(Properties selfserviceProperty) {
		this.selfserviceProperty = selfserviceProperty;
	}

	private Properties selfserviceProperty;

}