<%@ include file="init.jsp" %>

<portlet:renderURL escapeXml="false" var="submitErrorCorrectionFormActionUrl">
	<portlet:param name="render" value="errorCorrectionFormAction" />
</portlet:renderURL>

	<h1>BPay Error Correction</h1>

	<form:form action="${submitErrorCorrectionFormActionUrl}" id="errorCorrectionForm" method="post" modelAttribute="serviceRequestForm">
		<fieldset>
			<legend>Your Contact Details</legend>

			<br />

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="givenName">First Name</label>

				<form:input id="givenName" path="contactInformation.givenName" readonly="true" />

				<form:errors class="error" path="contactInformation.givenName" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="surname">Last Name</label>

				<form:input id="surname" path="contactInformation.surname" readonly="true" />

				<form:errors class="error" path="contactInformation.surname" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="email">Email</label>

				<form:input id="email" path="contactInformation.email" readonly="true" />

				<form:errors class="error" path="contactInformation.email" />
			</div>

			<div>
				<label class="label-fixed-width" for="phoneNumber">Phone Number</label>

				<form:input id="phoneNumber" path="contactInformation.phoneNumber" readonly="true" />

				<form:errors class="error" path="contactInformation.phoneNumber" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="organisation">Organisation</label>

				<form:input id="organisation" path="contactInformation.organisation" readonly="true" />

				<form:errors class="error" path="contactInformation.organisation" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="bsb">BSB</label>

				<form:input id="bsb" path="attributesInformation.bsb" />

				<form:errors class="error" path="attributesInformation.bsb" />
			</div>
		</fieldset>

		<fieldset>
			<legend>Payment and Other Details</legend>

			<br />

			<div>
				<label class="label-fixed-width" for="transactionRefNumber">Transaction Reference Number<em>*</em></label>

				<form:input id="transactionRefNumber" path="attributesInformation.transactionRefNumber" />

				<a href="javascript:;" title="This should be the receipt number provided to the member as confirmation of payment"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.transactionRefNumber" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="transactionDate">Transaction Date<em>*</em></label>

				<form:input id="transactionDate" path="attributesInformation.transactionDate" />

				<a href="javascript:;" title="This should be the date of transaction been made"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.transactionDate" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="billerName">Biller Name</label>

				<form:input id="billerName" path="attributesInformation.billerName" />

				<form:errors class="error" path="attributesInformation.billerName" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="billerCode">Biller Code<em>*</em></label>

				<form:input id="billerCode" path="attributesInformation.billerCode" />

				<a href="javascript:;" title="This should be the 6 digit BSB number"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.billerCode" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="customerRefNumber">Customer Reference Number<em>*</em></label>

				<form:input id="customerRefNumber" path="attributesInformation.customerRefNumber" />

				<a href="javascript:;" title="This should be the CRN of the person disputing the transaction"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.customerRefNumber" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="correctCRN">Correct CRN</label>

				<form:input id="correctCRN" path="attributesInformation.correctCRN" />

				<a href="javascript:;" title="This should be the correct CRN of the person disputing the transaction"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.correctCRN" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="paymentAmount">Payment Amount<em>*</em></label>

				<form:input id="paymentAmount" path="attributesInformation.paymentAmount" />

				<a href="javascript:;" title="This should be value of the transaction being disputed"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.paymentAmount" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="paymentMethod">Payment Method<em>*</em></label>

				<form:select id="paymentMethod" path="attributesInformation.paymentMethod">
					<option value="">Please select</option>

					<form:options items="${bPayListBoxData.paymentMethodMap}" />
				</form:select>

				<a href="javascript:;" title="This should be the payment method such as debit account, 001 or other"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.paymentMethod" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="entryMethod">Entry Method<em>*</em></label>

				<form:select id="entryMethod" path="attributesInformation.entryMethod">
					<option value="">Please select</option>

					<form:options items="${bPayListBoxData.entryMethodMap}" />
				</form:select>

				<a href="javascript:;" title="This should be the method used to conduct the transaction"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.entryMethod" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="reason">Reason for Request<em>*</em></label>

				<form:select id="reason" path="attributesInformation.reason">
					<option value="">Please select</option>

					<form:options items="${bPayListBoxData.reasonErrorCorrectionMap}" />
				</form:select>

				<a href="javascript:;" title="This should be the actual reason for the service request"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.reason" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="payerName">Payer's Name<em>*</em></label>

				<form:input id="payerName" path="attributesInformation.payerName" />

				<a href="javascript:;" title="This should be the actual payer's name"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.payerName" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="memberName">Member Name</label>

				<form:input id="memberName" path="attributesInformation.memberName" />

				<form:errors class="error" path="attributesInformation.memberName" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="memberNumber">Member Number<em>*</em></label>

				<form:input id="memberNumber" path="attributesInformation.memberNumber" />

				<a href="javascript:;" title="This should the membership number of the person who made the payment"><strong>?</strong></a>

				<form:errors class="error" path="attributesInformation.memberNumber" />
			</div>

			<div class="bpay-input-wrapper">
				<label class="label-fixed-width" for="comment">Comment</label>

				<form:input id="comment" path="attributesInformation.comment" />

				<form:errors class="error" path="attributesInformation.comment" />
			</div>

			<div class="bpay-input-wrapper">
				<form:checkbox path="disclaimer" /> I agree Cuscal is not liable for any errors and/or delays due to incorrect/incomplete information being supplied.
				<form:errors class="error" path="disclaimer" />
			</div>
		</fieldset>

		<label class="submit-label-fixed-width">&nbsp;</label><input type="submit" value="Submit" />
	</form:form>