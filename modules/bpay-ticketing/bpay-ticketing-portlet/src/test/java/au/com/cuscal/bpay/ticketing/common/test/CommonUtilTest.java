package au.com.cuscal.bpay.ticketing.common.test;

import static org.junit.Assert.assertEquals;

import au.com.cuscal.bpay.ticketing.common.BPayProperties;
import au.com.cuscal.bpay.ticketing.common.CommonUtil;
import au.com.cuscal.bpay.ticketing.domain.BPayListBoxData;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//@ExtendWith(MockitoExtension.class)
public class CommonUtilTest {

	@BeforeEach
	public void setup() throws IOException {

		// Initialize and assign values to properties

		Properties properties = new Properties();

		// Use the class loader of the test class to load the resource

		try (InputStream input = getClass(
			).getClassLoader(
			).getResourceAsStream(
				"bpay.properties"
			)) {

			if (input != null) {
				properties.load(input);
			}
			else {
				throw new IOException("Could not load the properties file.");
			}
		}

		bPayProperties.setBpayProperty(properties);
	}

	@Test
	public void testReadPropertiesFromFileForFraudTypeAttributes()
		throws IOException {

		BPayListBoxData bPayListBoxData = new BPayListBoxData();

		bPayListBoxData = CommonUtil.populateBPayListBoxData(bPayProperties);

		System.out.println(
			"loaded dropdown values from properties file for fraudType" +
				bPayListBoxData.getFraudTypeMap());
		assertEquals(
			"Compromised Payer Account",
			bPayListBoxData.getFraudTypeMap(
			).get(
				1L
			));
		assertEquals(
			"Acting as Money Mule",
			bPayListBoxData.getFraudTypeMap(
			).get(
				2L
			));
		assertEquals(
			"Victim of Scam",
			bPayListBoxData.getFraudTypeMap(
			).get(
				3L
			));
		assertEquals(
			"Other",
			bPayListBoxData.getFraudTypeMap(
			).get(
				4L
			));
	}

	@Test
	public void testReadPropertiesFromFileForScamTypeAttributes()
		throws IOException {

		BPayListBoxData bPayListBoxData = CommonUtil.populateBPayListBoxData(
			bPayProperties);

		System.out.println(
			"loaded dropdown values from properties file for ScamType" +
				bPayListBoxData.getScamTypeMap());
		assertEquals(
			"Investment Scam",
			bPayListBoxData.getScamTypeMap(
			).get(
				1L
			));
		assertEquals(
			"Romance Scam",
			bPayListBoxData.getScamTypeMap(
			).get(
				2L
			));
	}

	BPayProperties bPayProperties = new BPayProperties();

}