package au.com.cuscal.connect.test;

import static org.junit.Assert.*;

import au.com.cuscal.common.framework.AuditWebServicesUtil;
import au.com.cuscal.framework.audit.xml.XmlMessage;
import au.com.cuscal.framework.webservices.Header;

import java.util.List;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;

import org.junit.Before;
import org.junit.Test;

public class XMLMessageBuilderTest {

	Header header;
	XmlMessage xmlMessage;
	String username = "aaashish";
	String orgID = "12812";
	String orgName = "Cuscal";
	String origin = "portal";

	@Before
	public void init() {
		//Setup the XMLUnit test case.
		XMLUnit.setControlParser(
			"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		XMLUnit.setTestParser(
			"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		XMLUnit.setSAXParserFactory(
			"org.apache.xerces.jaxp.SAXParserFactoryImpl");
		XMLUnit.setIgnoreWhitespace(false);
		XMLUnit.setIgnoreAttributeOrder(false);
		XMLUnit.setIgnoreComments(true);

		header = new Header();
		header.setUserId(username);
		header.setUserOrgId(orgID);
		header.setUserOrgName(orgName);
		header.setOrigin(origin);

		xmlMessage = new XmlMessage();
	}

	@Test
	public void testAuditHeaderXmlMessage() throws Exception {
		xmlMessage = AuditWebServicesUtil.addHeaderElementsToXmlMessage(
			header, xmlMessage);

		String auditXmlMessage = xmlMessage.toXml();
		String compareToXml =
			"<audit><parameters>" + "<userId>aaashish</userId>" +
				"<organisation>12812</organisation>" +
					"<origin>portal</origin></parameters>" +
						"<result><comments></comments>" + "</result></audit>";

		Diff diff = new Diff(compareToXml, auditXmlMessage);

		DetailedDiff detailedDiff = new DetailedDiff(diff);

		System.out.println("XML Similar: " + diff.similar());
		System.out.println("XML Identical: " + diff.identical());
		//Display the differences if there are any.
		@SuppressWarnings("rawtypes")
		List allDifferences = detailedDiff.getAllDifferences();

		for (Object o : allDifferences) {
			Difference difference = (Difference)o;

			System.out.println(difference);
		}

		assertTrue("XML similar ", diff.similar());
		assertTrue("XML identical ", diff.identical());
	}

	@Test
	public void testXmlMessage() {
		XmlMessage message = new XmlMessage();

		message.addParameter("user", "aaashish");
		message.addParameter("liferayOrgId", "10290");
		message.addParameter("fromDate", "01/01/2012");
		message.addParameter("toDate", "31/01/2012");

		message.addResult("found", "0");

		message.addComment(
			"error",
			"Liferay Org ID: 10290 has not been setup properly for Report access in Report_Access and Report_Org_Owner tables.");

		System.out.println("xml string is: " + message.toXml());
	}

}