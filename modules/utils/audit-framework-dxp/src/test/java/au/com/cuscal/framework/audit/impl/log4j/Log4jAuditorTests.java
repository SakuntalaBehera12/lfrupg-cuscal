/**
 *
 */

package au.com.cuscal.framework.audit.impl.log4j;

import static org.junit.Assert.*;

import au.com.cuscal.framework.audit.*;

import java.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author dchong
 *
 */
public class Log4jAuditorTests {

	/**
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// Set up a simple configuration that logs on the console.

		BasicConfigurator.configure();
		log.info("log4j configured");
	}

	/**
	 * Test method for {@link au.Log4jAuditor#audit(Date, String, String, String, String, String, String, String)}.
	 */
	@Test
	public void testAuditDateStringStringStringStringStringStringString() {
		audit.audit(
			new Date(), "junit", "localhost", "127.0.0.1", "80", "testuser",
			"JUNIT TEST", "MY STATUS", "hello world");
	}

	/**
	 * Test method for {@link au.Log4jAuditor#audit(Object)}.
	 */
	@Test
	public void testAuditObject() {
		audit.audit("testAuditObject");
	}

	/**
	 * Test method for {@link au.Log4jAuditor#audit(Object, Throwable)}.
	 */
	@Test
	public void testAuditObjectThrowable() {
		audit.audit(
			"testAuditObjectThrowable msg",
			new Exception("testAuditObjectThrowable"));
	}

	/**
	 * Test method for {@link au.Log4jAuditor#fail(Date, String, String, String, String, String, String)}.
	 */
	@Test
	public void testFail() {
		audit.fail(
			new Date(), "junit", "localhost", "127.0.0.1", "80", "testuser",
			"JUNIT TEST", "testFail");
	}

	/**
	 * Test method for {@link au.Log4jAuditor#success(Date, String, String, String, String, String, String)}.
	 */
	@Test
	public void testSuccess() {
		audit.success(
			new Date(), "junit", "localhost", "127.0.0.1", "80", "testuser",
			"JUNIT TEST", "testSuccess");
	}

	private static Audit audit = new Log4jAuditor();
	private static Logger log = Logger.getLogger(Log4jAuditorTests.class);

}