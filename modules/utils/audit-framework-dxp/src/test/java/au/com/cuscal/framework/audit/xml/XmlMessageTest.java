package au.com.cuscal.framework.audit.xml;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class XmlMessageTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testToXml() throws XmlMessageException {
		XmlMessage xmlMsg = new XmlMessage();

		xmlMsg.addParameter("userid", "dchong");
		xmlMsg.addParameter("liferayOrgId", "10290");
		xmlMsg.addParameter("fromDate", "01/01/2013");
		xmlMsg.addParameter("toDate", "05/05/2013");

		xmlMsg.addResult("found", "1");
		xmlMsg.addResult("reportName", "CP-UDL");
		xmlMsg.addResult("fileName", "20130306-584003020-CP-UDL.rpt");

		xmlMsg.addComment("successOrFailure", "SUCCESS");
		xmlMsg.addComment("resultCode", "SERVICE_BUSY");

		System.out.println(xmlMsg.toXml());

		// should really have a xmlCompare function here to test the result of xmlMsg.toXml() properly.

	}

}