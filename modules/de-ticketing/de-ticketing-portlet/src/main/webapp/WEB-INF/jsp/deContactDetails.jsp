<fieldset>
	<legend>Your Contact Details</legend>

	<div class="contact-info">
		<label class="">Organisation</label>

		<span><c:out value="${serviceRequestForm.contactInformation.reqInstFullNamefrom}" /></span>

		<form:hidden id="organisation" path="contactInformation.reqInstFullNamefrom" />
	</div>

	<div class="contact-info">
		<label class="">BSB</label>

		<span><c:out value="${serviceRequestForm.contactInformation.bsb}" /></span>

		<form:hidden id="bsb" path="contactInformation.bsb" />
		<form:hidden id="bsb" path="attributesInformation.bsb" value="${serviceRequestForm.contactInformation.bsb}" />
	</div>

	<br />

	<div class="contact-info">
		<label class="">First Name</label>

		<span><c:out value="${serviceRequestForm.contactInformation.contactOfficerFirstName}" /></span>

		<form:hidden id="givenName" path="contactInformation.contactOfficerFirstName" />
	</div>

	<div class="contact-info">
		<label class="">Last Name</label>

		<span><c:out value="${serviceRequestForm.contactInformation.contactOfficerLastName}" /></span>

		<form:hidden id="surname" path="contactInformation.contactOfficerLastName" />
	</div>

	<br />

	<div class="contact-info">
		<label class="">Email</label>

		<span><c:out value="${serviceRequestForm.contactInformation.emailFrom}" /></span>

		<form:hidden id="email" path="contactInformation.emailFrom" />
	</div>
</fieldset>

<div id="loading" style="display: none; z-index: 10000;">
	<div
		style="position: fixed; top: 0px; left: 0px; width: 100%; height: 100%; background-color: rgb(0, 0, 0); opacity: 0.3; z-index: 10000;">&nbsp;</div
	>
	<div
		style="position: fixed; height: 400px; width: 500px; top: 276.5px; left: 381.5px; background-color: rgb(255, 255, 255); text-align: center; border: 1px solid rgb(0, 0, 0); border-radius: 4px; z-index: 10001;"
	>
		<img
			src="<%= request.getContextPath() %>/images/ajax-loader.gif"
			style="position: relative; height: 32px; width: 32px; top: 184px; z-index: 10001;"
		>
		<div
			style="position: relative; top: 204px; font-weight: bold; text-align: center;">Please wait...</div
		>
	</div>
</div>