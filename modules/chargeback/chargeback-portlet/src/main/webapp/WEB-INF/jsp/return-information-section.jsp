<fieldset id="returnInfo">
	<legend>Return Information</legend>

	<div id="returnInfoReturnDate">
		<label class="medium" for="returnDate">Return date</label>

		<form:input class="date-pick" id="returnDate" path="returnInfo.returnDate" />
	</div>

	<div id="returnInfoReturnMethod">
		<label class="medium" for="returnMethod">Return method</label>

		<form:select cssClass="reset" id="returnMethod" path="returnInfo.returnMethod">
			<form:option value="0"><spring:message code="label.please.select" /></form:option>
			<form:option value="322">Face-to-Face</form:option>
			<form:option value="323">FedEx</form:option>
			<form:option value="324">DHL</form:option>
			<form:option value="325">Postal Service</form:option>
			<form:option value="326">UPS</form:option>
			<form:option value="327">Other</form:option>
		</form:select>
	</div>

	<div class="padded" id="returnInfoOtherMethod">
		<label class="medium" for="otherMethod">Other method</label>

		<form:input id="otherMethod" path="returnInfo.otherMethod" />
	</div>

	<div class="padded" id="returnInfoShippingNumber">
		<label class="medium" for="shippingNumber">Shipping/Tracking number</label>

		<form:input id="shippingNumber" path="returnInfo.shippingNumber" />
	</div>

	<div class="padded" id="returnInfoWhoSignedPackage">
		<label class="medium" for="whoSignedPackage">Who signed for this package</label>

		<form:input id="whoSignedPackage" path="returnInfo.whoSignedPackage" />
	</div>

	<div class="padded" id="returnInfoDeliveryAddress">
		<label class="medium" for="deliverAddress">Delivery address</label>

		<form:input id="deliveryAddress" path="returnInfo.deliveryAddress" />
	</div>

	<div id="returnInfoMerchantReceivedOn">
		<label class="medium" for="merchantReceivedOn">Merchant Received on</label>

		<form:input class="date-pick" id="merchantReceivedOn" path="returnInfo.merchantReceivedOn" />
	</div>

	<div id="returnInfoReturnAuthorisationNumber">
		<label class="medium" for="returnAuthorisationNumber">Return/Authorisation Number</label>

		<form:input id="returnAuthorisationNumber" path="returnInfo.returnAuthorisationNumber" />
	</div>

	<div id="returnInfoReturnInstruction">
		<label for="returnInstruction">For Merchandise returned per merchant instruction please provide instructions</label>

		<form:textarea id="returnInstruction" path="returnInfo.returnInstruction" />
	</div>
</fieldset>