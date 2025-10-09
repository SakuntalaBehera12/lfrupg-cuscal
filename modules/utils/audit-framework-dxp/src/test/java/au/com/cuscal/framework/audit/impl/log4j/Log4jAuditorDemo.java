package au.com.cuscal.framework.audit.impl.log4j;

import java.util.Date;

import org.apache.log4j.BasicConfigurator;

public class Log4jAuditorDemo {

	Log4jAuditor audit = null;
	Date when = new Date();
	String origin = "DEMO";
	String host = "localhost";
	String ip = "127.0.0.1";
	String port = "80";
	String who = "dchong";
	String category = "DEMO";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BasicConfigurator.configure();
			Log4jAuditorDemo demo = new Log4jAuditorDemo();

			demo.run(args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void run(String[] args) {
		audit = new Log4jAuditor();

		// action: user has logged in

		audit.success(
			when, origin, host, ip, port, who, category, "login successful");
		audit.success(
			origin, host, ip, port, who, category, "login successful");

		// action: user has failed to login correctly, with incorrect user/password

		audit.fail(
			when, origin, host, ip, port, who, category,
			"incorrect username/password");
		audit.fail(
			origin, host, ip, port, who, category,
			"incorrect username/password");

		// action: user has failed to login correctly, account is disabled

		audit.fail(
			when, origin, host, ip, port, who, category,
			"user account disabled");
		audit.fail(
			origin, host, ip, port, who, category, "user account disabled");

		// action: user has viewed transaction detail (primary key 90) for pan 123456

		audit.success(
			when, origin, host, ip, port, who, category,
			"user has accessed transaction_log#90 for pan 123456)");
		audit.success(
			origin, host, ip, port, who, category,
			"user has accessed transaction_log#90 for pan 123456)");
	}

}