package au.com.cuscal.bpay.ticketing.services.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

import au.com.cuscal.bpay.ticketing.common.BPayProperties;
import au.com.cuscal.bpay.ticketing.forms.AttributesInformation;
import au.com.cuscal.bpay.ticketing.forms.RequestTypeInformation;
import au.com.cuscal.bpay.ticketing.forms.ServiceRequestForm;
import au.com.cuscal.bpay.ticketing.services.CuscalTicketingServiceImpl;
import au.com.cuscal.framework.webservices.selfservice.SimpleAttributesType;

import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Method;

import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CuscalTicketingServiceImplTest {

	@BeforeEach
	public void setUp() throws IOException {
		MockitoAnnotations.initMocks(this);

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

		when(
			mockBpayProperties.getBpayProperty()
		).thenReturn(
			properties
		);
		mockBpayProperties.setBpayProperty(properties);
	}

	@Test
	public void testSetupAttributesInformationForServiceRequest_For_FraudType()
		throws Exception, IllegalAccessException, RuntimeException {

		// Create sample data for the test

		System.out.println(
			"\nStarting testSetupAttributesInformationForServiceRequest_For_FraudType Test");
		AttributesInformation sampleAttributesforFraudType =
			new AttributesInformation();

		sampleAttributesforFraudType.setInvestigationType("2");
		sampleAttributesforFraudType.setErrorCorrectionReason("5");
		sampleAttributesforFraudType.setPayerReportedDate("12-03-2025");
		sampleAttributesforFraudType.setFraudType("1");

		when(
			mockForm.getAttributesInformation()
		).thenReturn(
			sampleAttributesforFraudType
		);
		when(
			mockForm.getRequestTypeInformation()
		).thenReturn(
			new RequestTypeInformation()
		);

		Method privateMethod =
			CuscalTicketingServiceImpl.class.getDeclaredMethod(
				"setupAttributesInformationForServiceRequest",
				ServiceRequestForm.class);

		privateMethod.setAccessible(true);

		List<SimpleAttributesType> result =
			(List<SimpleAttributesType>)privateMethod.invoke(
				cuscalTicketingServiceImpl, mockForm);

		//Print Final Results after converting to List<SimpleAttributesType> format
		result.forEach(
			attribute -> System.out.println(
				"SimpleAttributesType[ id:" + attribute.getId() + ", value: " +
					attribute.getValue() + ", description: " +
						attribute.getDescription() + "]"));

		SimpleAttributesType expectedFraudTypeAttribute = result.stream(
		).filter(
			attribute -> attribute.getId() == 227
		).findFirst(
		).get();

		// Assert the expected outcome

		assertEquals("1", expectedFraudTypeAttribute.getValue());
		assertEquals(
			"Compromised Payer Account",
			expectedFraudTypeAttribute.getDescription());
		assertNotNull(result);
	}

	@Test
	public void testSetupAttributesInformationForServiceRequest_For_FraudType_Other()
		throws Exception, IllegalAccessException, RuntimeException {

		System.out.println(
			"\nstarting testSetupAttributesInformationForServiceRequest_For_FraudType_Other Test");

		// Create sample data for the test

		AttributesInformation sampleAttributes = new AttributesInformation();

		sampleAttributes.setInvestigationType("2");
		sampleAttributes.setErrorCorrectionReason("5");
		sampleAttributes.setPayerReportedDate("12-03-2025");
		sampleAttributes.setFraudType("4");
		sampleAttributes.setFraudInfo("Other fraud test information");

		when(
			mockForm.getAttributesInformation()
		).thenReturn(
			sampleAttributes
		);
		when(
			mockForm.getRequestTypeInformation()
		).thenReturn(
			new RequestTypeInformation()
		);

		Method privateMethod =
			CuscalTicketingServiceImpl.class.getDeclaredMethod(
				"setupAttributesInformationForServiceRequest",
				ServiceRequestForm.class);

		privateMethod.setAccessible(true);

		List<SimpleAttributesType> result =
			(List<SimpleAttributesType>)privateMethod.invoke(
				cuscalTicketingServiceImpl, mockForm);

		//Print Final Results after converting to List<SimpleAttributesType> format
		result.forEach(
			attribute -> System.out.println(
				"SimpleAttributesType[ id:" + attribute.getId() + ", value: " +
					attribute.getValue() + ", description: " +
						attribute.getDescription() + "]"));

		SimpleAttributesType expectedFraudTypeAttribute = result.stream(
		).filter(
			attribute -> attribute.getId() == 227
		).findFirst(
		).get();

		SimpleAttributesType expectedFraudInfoAttribute = result.stream(
		).filter(
			attribute -> attribute.getId() == 229
		).findFirst(
		).get();

		// Assert the expected outcome\

		assertEquals("4", expectedFraudTypeAttribute.getValue());
		assertEquals("Other", expectedFraudTypeAttribute.getDescription());
		assertEquals(
			"Other fraud test information",
			expectedFraudInfoAttribute.getValue());

		assertNotNull(result);
	}

	@Test
	public void testSetupAttributesInformationForServiceRequest_For_PayerReportedDate()
		throws Exception, IllegalAccessException, RuntimeException {

		System.out.println(
			"\nStarting testSetupAttributesInformationForServiceRequest_For_PayerReportedDate Test");

		// Create sample data for the test

		AttributesInformation sampleAttributes = new AttributesInformation();

		sampleAttributes.setPayerReportedDate("12-03-2025");

		when(
			mockForm.getAttributesInformation()
		).thenReturn(
			sampleAttributes
		);
		when(
			mockForm.getRequestTypeInformation()
		).thenReturn(
			new RequestTypeInformation()
		);

		Method privateMethod =
			CuscalTicketingServiceImpl.class.getDeclaredMethod(
				"setupAttributesInformationForServiceRequest",
				ServiceRequestForm.class);

		privateMethod.setAccessible(true);

		List<SimpleAttributesType> result =
			(List<SimpleAttributesType>)privateMethod.invoke(
				cuscalTicketingServiceImpl, mockForm);

		//Print Final Results after converting to List<SimpleAttributesType> format
		result.forEach(
			attribute -> System.out.println(
				"SimpleAttributesType[ id:" + attribute.getId() + ", value: " +
					attribute.getValue() + ", description: " +
						attribute.getDescription() + "]"));

		SimpleAttributesType expectedPayerReportedDateAttribute = result.stream(
		).filter(
			attribute -> attribute.getId() == 226
		).findFirst(
		).get();

		// Assert the expected outcome

		assertEquals(
			"12-03-2025", expectedPayerReportedDateAttribute.getValue());
		assertNotNull(result);
	}

	@Test
	public void testSetupAttributesInformationForServiceRequest_For_ScamType()
		throws Exception, IllegalAccessException, RuntimeException {

		System.out.println(
			"\nstarting testSetupAttributesInformationForServiceRequest_For_ScamType Test");

		// Create sample data for the test

		AttributesInformation sampleAttributes = new AttributesInformation();

		sampleAttributes.setInvestigationType("2");
		sampleAttributes.setErrorCorrectionReason("5");
		sampleAttributes.setPayerReportedDate("12-03-2025");
		sampleAttributes.setFraudType("3");
		sampleAttributes.setScamType("1");

		when(
			mockForm.getAttributesInformation()
		).thenReturn(
			sampleAttributes
		);
		when(
			mockForm.getRequestTypeInformation()
		).thenReturn(
			new RequestTypeInformation()
		);

		Method privateMethod =
			CuscalTicketingServiceImpl.class.getDeclaredMethod(
				"setupAttributesInformationForServiceRequest",
				ServiceRequestForm.class);

		privateMethod.setAccessible(true);

		List<SimpleAttributesType> result =
			(List<SimpleAttributesType>)privateMethod.invoke(
				cuscalTicketingServiceImpl, mockForm);

		//Print Final Results after converting to List<SimpleAttributesType> format
		result.forEach(
			attribute -> System.out.println(
				"SimpleAttributesType[ id:" + attribute.getId() + ", value: " +
					attribute.getValue() + ", description: " +
						attribute.getDescription() + "]"));

		SimpleAttributesType expectedFraudTypeAttribute = result.stream(
		).filter(
			attribute -> attribute.getId() == 227
		).findFirst(
		).get();

		SimpleAttributesType expectedScamTypeAttribute = result.stream(
		).filter(
			attribute -> attribute.getId() == 228
		).findFirst(
		).get();

		// Assert the expected outcome\

		assertEquals("3", expectedFraudTypeAttribute.getValue());
		assertEquals(
			"Victim of Scam", expectedFraudTypeAttribute.getDescription());
		assertEquals("1", expectedScamTypeAttribute.getValue());
		assertEquals(
			"Investment Scam", expectedScamTypeAttribute.getDescription());
		assertNotNull(result);
	}

	@Test
	public void testSetupAttributesInformationForServiceRequest_For_ScamType_Other()
		throws Exception, IllegalAccessException, RuntimeException {

		System.out.println(
			"\nstarting testSetupAttributesInformationForServiceRequest_For_ScamType_Other Test");

		// Create sample data for the test

		AttributesInformation sampleAttributes = new AttributesInformation();

		sampleAttributes.setInvestigationType("2");
		sampleAttributes.setErrorCorrectionReason("5");
		sampleAttributes.setPayerReportedDate("12-03-2025");
		sampleAttributes.setFraudType("3");
		sampleAttributes.setScamType("8");
		sampleAttributes.setScamInfo("Other Scam Test information");

		when(
			mockForm.getAttributesInformation()
		).thenReturn(
			sampleAttributes
		);
		when(
			mockForm.getRequestTypeInformation()
		).thenReturn(
			new RequestTypeInformation()
		);

		Method privateMethod =
			CuscalTicketingServiceImpl.class.getDeclaredMethod(
				"setupAttributesInformationForServiceRequest",
				ServiceRequestForm.class);

		privateMethod.setAccessible(true);

		List<SimpleAttributesType> result =
			(List<SimpleAttributesType>)privateMethod.invoke(
				cuscalTicketingServiceImpl, mockForm);

		//Print Final Results after converting to List<SimpleAttributesType> format
		result.forEach(
			attribute -> System.out.println(
				"SimpleAttributesType[ id:" + attribute.getId() + ", value: " +
					attribute.getValue() + ", description: " +
						attribute.getDescription() + "]"));

		SimpleAttributesType expectedFraudTypeAttribute = result.stream(
		).filter(
			attribute -> attribute.getId() == 227
		).findFirst(
		).get();

		SimpleAttributesType expectedScamTypeAttribute = result.stream(
		).filter(
			attribute -> attribute.getId() == 228
		).findFirst(
		).get();

		SimpleAttributesType expectedScamInfoAttribute = result.stream(
		).filter(
			attribute -> attribute.getId() == 230
		).findFirst(
		).get();

		// Assert the expected outcome\

		assertEquals("3", expectedFraudTypeAttribute.getValue());
		assertEquals(
			"Victim of Scam", expectedFraudTypeAttribute.getDescription());
		assertEquals("8", expectedScamTypeAttribute.getValue());
		assertEquals("Other Scam", expectedScamTypeAttribute.getDescription());
		assertEquals(
			"Other Scam Test information",
			expectedScamInfoAttribute.getValue());
		assertNotNull(result);
	}

	@InjectMocks
	private CuscalTicketingServiceImpl cuscalTicketingServiceImpl;

	@Mock
	private BPayProperties mockBpayProperties;

	@Mock
	private ServiceRequestForm mockForm;

}