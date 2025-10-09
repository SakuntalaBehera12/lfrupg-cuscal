package au.com.cuscal.bpay.ticketing.validator.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import au.com.cuscal.bpay.ticketing.forms.AttributesInformation;
import au.com.cuscal.bpay.ticketing.forms.ServiceRequestForm;
import au.com.cuscal.bpay.ticketing.validator.BPayTicketingServiceRequestValidator;

import org.junit.jupiter.api.Test;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class BPayTicketingServiceRequestValidatorTest {

	@Test
	public void validatePayerReportedDate() {
		System.out.println("\nstarting validatePayerReportedDate Test");

		// Create a valid ServiceRequestForm for testing

		ServiceRequestForm validForm = mockFormData();

		printInput(validForm.getAttributesInformation());

		// Create Errors object for validation errors

		Errors errors = new BeanPropertyBindingResult(validForm, "validForm");

		// Create an instance of BPayTicketingServiceRequestValidator

		BPayTicketingServiceRequestValidator validator =
			new BPayTicketingServiceRequestValidator();

		// Call the method to validate

		validator.validatePayerReportedDate(validForm, errors);

		printErrorCodes(errors);

		// Assert that there are no errors

		assertFalse(errors.hasErrors());
	}

	@Test
	public void validatePayerReportedDate_With_InValid_Date() {
		System.out.println(
			"\nstarting validatePayerReportedDate_With_InValid_Date Test");

		// Create a Invalid ServiceRequestForm for testing

		ServiceRequestForm inValidForm = mockFormData();

		inValidForm.getAttributesInformation(
		).setPayerReportedDate(
			"12-0/03/2019"
		);

		printInput(inValidForm.getAttributesInformation());

		// Create Errors object for validation errors

		Errors errors = new BeanPropertyBindingResult(
			inValidForm, "inValidForm");

		// Create an instance of BPayTicketingServiceRequestValidator

		BPayTicketingServiceRequestValidator validator =
			new BPayTicketingServiceRequestValidator();

		// Call the method to validate

		validator.validatePayerReportedDate(inValidForm, errors);

		printErrorCodes(errors);

		// Assert that there are errors

		assertTrue(errors.hasErrors());
	}

	@Test
	public void validateReasonCode_InvalidData_FraudTypeEmpty() {
		System.out.println(
			"\nstarting validateReasonCode_InvalidData_FraudTypeEmpty Test");

		// Create a Invalid ServiceRequestForm for testing

		ServiceRequestForm inValidForm = mockFormData();

		printInput(inValidForm.getAttributesInformation());

		// Create Errors object for validation errors

		Errors errors = new BeanPropertyBindingResult(
			inValidForm, "inValidForm");

		// Create an instance of BPayTicketingServiceRequestValidator

		BPayTicketingServiceRequestValidator validator =
			new BPayTicketingServiceRequestValidator();

		// Call the method to validate

		validator.validateReason(inValidForm, errors);

		printErrorCodes(errors);

		// Assert that there are errors

		assertTrue(errors.hasErrors());
		assertEquals(
			"service.request.fraudType.empty",
			errors.getFieldError(
			).getCode());
	}

	@Test
	public void validateReasonCode_InvalidData_FraudTypeNotEmpty_AndFruadTypeOther_FraudInfoEmpty() {
		System.out.println(
			"\nstarting validateReasonCode_InvalidData_FraudTypeNotEmpty_AndFruadTypeOther_FraudInfoEmpty Test");

		// Create a Invalid ServiceRequestForm for testing

		ServiceRequestForm inValidForm = mockFormData();

		inValidForm.getAttributesInformation(
		).setFraudType(
			"4"
		);

		printInput(inValidForm.getAttributesInformation());

		// Create Errors object for validation errors

		Errors errors = new BeanPropertyBindingResult(
			inValidForm, "inValidForm");

		// Create an instance of BPayTicketingServiceRequestValidator

		BPayTicketingServiceRequestValidator validator =
			new BPayTicketingServiceRequestValidator();

		// Call the method to validate

		validator.validateReason(inValidForm, errors);

		printErrorCodes(errors);

		// Assert that there are errors

		assertTrue(errors.hasErrors());
		assertEquals(
			"service.request.fraudInfo.empty",
			errors.getFieldError(
			).getCode());
	}

	@Test
	public void validateReasonCode_InvalidData_FraudTypeNotEmpty_ScamTypeEmpty() {
		System.out.println(
			"\n starting validateReasonCode_InvalidData_FraudTypeNotEmpty_ScamTypeEmpty Test");

		// Create a Invalid ServiceRequestForm for testing

		ServiceRequestForm inValidForm = mockFormData();

		inValidForm.getAttributesInformation(
		).setFraudType(
			"3"
		);

		printInput(inValidForm.getAttributesInformation());

		// Create Errors object for validation errors

		Errors errors = new BeanPropertyBindingResult(
			inValidForm, "inValidForm");

		// Create an instance of BPayTicketingServiceRequestValidator

		BPayTicketingServiceRequestValidator validator =
			new BPayTicketingServiceRequestValidator();

		// Call the method to validate

		validator.validateReason(inValidForm, errors);

		printErrorCodes(errors);

		// Assert that there are errors

		assertTrue(errors.hasErrors());
		assertEquals(
			"service.request.scamType.empty",
			errors.getFieldError(
			).getCode());
	}

	@Test
	public void validateReasonCode_InvalidData_FraudTypeNotEmpty_ScamTypeNotEmpty_ScamInfoEmpty() {
		System.out.println(
			"\nstarting validateReasonCode_InvalidData_FraudTypeNotEmpty_ScamTypeNotEmpty_ScamInfoEmpty Test");

		// Create a Invalid ServiceRequestForm for testing

		ServiceRequestForm inValidForm = mockFormData();

		inValidForm.getAttributesInformation(
		).setFraudType(
			"3"
		);

		inValidForm.getAttributesInformation(
		).setScamType(
			"8"
		);

		printInput(inValidForm.getAttributesInformation());

		// Create Errors object for validation errors

		Errors errors = new BeanPropertyBindingResult(
			inValidForm, "inValidForm");

		// Create an instance of BPayTicketingServiceRequestValidator

		BPayTicketingServiceRequestValidator validator =
			new BPayTicketingServiceRequestValidator();

		// Call the method to validate

		validator.validateReason(inValidForm, errors);

		printErrorCodes(errors);

		// Assert that there are errors

		assertTrue(errors.hasErrors());
		assertEquals(
			"service.request.scamInfo.empty",
			errors.getFieldError(
			).getCode());
	}

	private ServiceRequestForm mockFormData() {
		ServiceRequestForm form = new ServiceRequestForm();
		AttributesInformation mockAttributesInformation =
			new AttributesInformation();

		mockAttributesInformation.setInvestigationType("2");
		mockAttributesInformation.setErrorCorrectionReason("5");
		mockAttributesInformation.setPayerReportedDate("12/03/2019");
		form.setAttributesInformation(mockAttributesInformation);

		return form;
	}

	private void printErrorCodes(Errors error) {
		System.out.println("Has Errors: " + error.hasErrors());

		if (error.hasErrors()) {
			System.out.println(
				"Error Code is :" +
					error.getFieldError(
					).getCode());
		}
	}

	private void printInput(AttributesInformation attributesInformation) {
		System.out.println(
			"input form [\nInvestigationType: " +
				attributesInformation.getInvestigationType() +
					"\nErrorCorrectionReason : " +
						attributesInformation.getErrorCorrectionReason() +
							"\npayerReportedDate : " +
								attributesInformation.getPayerReportedDate() +
									"\nfraudType: " +
										attributesInformation.getFraudType() +
											"\nscamType: " +
												attributesInformation.
													getScamType() +
														"\n fraudInfo: " +
															attributesInformation.
																getFraudInfo() +
																	"\nScamInfo : " +
																		attributesInformation.
																			getScamInfo() +
																				"\n]");
	}

}