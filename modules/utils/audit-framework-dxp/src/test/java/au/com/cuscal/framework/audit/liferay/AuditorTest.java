package au.com.cuscal.framework.audit.liferay;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AuditorTest {

	Auditor auditor = Auditor.getInstance();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BasicConfigurator.configure();
	}

	@Before
	public void setUp() throws Exception {
	}

	/*
	@Test
	public void testSuccessPortletResponsePortletRequestStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFailPortletResponsePortletRequestStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testAuditPortletResponsePortletRequestStringStringStringString() {
		fail("Not yet implemented");
	}

	*/

	@Test
	public void testAuditObject() {
		auditor.audit("auditor.audit success");
	}

	@Test
	public void testAuditObjectThrowable() {
		auditor.audit(
			"auditor.audit success with exception",
			new Exception("Dummy Exception"));
	}

	/*
	@Test
	public void testAuditDateStringStringStringStringStringStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testAuditStringStringStringStringStringStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testAuditStringHttpServletRequestStringStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testSuccessDateStringStringStringStringStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testSuccessStringStringStringStringStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testSuccessStringHttpServletRequestStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFailDateStringStringStringStringStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFailStringStringStringStringStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFailStringHttpServletRequestStringStringString() {
		fail("Not yet implemented");
	}

	*/
}