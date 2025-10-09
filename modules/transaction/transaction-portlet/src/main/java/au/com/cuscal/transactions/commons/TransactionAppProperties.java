package au.com.cuscal.transactions.commons;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class TransactionAppProperties {

	/**
	 * Properties object getter
	 */
	public Properties getTransProps() {
		return transProps;
	}

	/**
	 * Properties object setter
	 */
	public void setTransProps(Properties transProps) {
		this.transProps = transProps;
	}

	/**
	 * Properties object
	 */
	private Properties transProps;

}