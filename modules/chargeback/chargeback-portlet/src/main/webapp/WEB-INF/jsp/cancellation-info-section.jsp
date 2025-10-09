<%@ include file="init.jsp" %>

<fieldset id="cancellationInfo">
	<legend>Cancellation Information</legend>

	<div id="cancellationRequiredOneOrMore">
		<span><spring:message code="message.complete.one" /> <form:errors cssClass="error" path="cancellationInfo.dateInfo" /></span>

		<div class="padded" id="cancellationDateRecurringTransCancelled">
			<label class="large" for="dateRecurringTransCancelled"><spring:message code="label.recurring.date.cancelled" /></label>

			<input class="date-pick" id="dateRecurringTransCancelled" name="cancellationInfo.dateRecurringTransCancelled" type="text" value="${cancellationInformation.dateRecurringTransCancelled}" />
		</div>

		<div class="padded" id="cancellationDateAcquirerNotified">
			<label class="large" for="dateAcquirerNotified"><spring:message code="label.acquirer.date.notified" /></label>

			<input class="date-pick" id="dateAcquirerNotified" name="cancellationInfo.dateAcquirerNotified" type="text" value="${cancellationInformation.dateAcquirerNotified}" />
		</div>

		<div class="padded" id="cancellationDatePreviousChargeback">
			<label class="large" for="datePreviousChargeback"><spring:message code="label.previous.chargeback.date" /></label>

			<input class="date-pick" id="datePreviousChargeback" name="cancellationInfo.datePreviousChargeback" type="text" value="${cancellationInformation.datePreviousChargeback}" />
		</div>
	</div>

	<div id="cancellationCancelledArn">
		<label class="large" for="cancelledArn"><spring:message code="label.acquirer.reference.number" /></label>

		<input id="cancelledArn" maxlength="24" name="cancellationInfo.cancelledArn" type="text" value="${cancellationInformation.cancelledArn}" />
	</div>

	<div id="cancellationCancelledDate">
		<label class="large" for="cancelledDate"><spring:message code="label.cancelled.date" /></label>

		<input class="date-pick" id="cancelledDate" name="cancellationInfo.dateCancelled" type="text" value="${cancellationInformation.dateCancelled}" />
	</div>

	<div id="cancellationWasCancellationCodeGiven">
		<label class="large" for="wasCancellationCodeGiven">Was a cancellation code given</label>

		<select class="reset" id="wasCancellationCodeGiven" name="cancellationInfo.wasCancellationCodeGiven">
			<option value="0" <c:if test="${cancellationInformation.wasCancellationCodeGiven == 0}">selected="selected"</c:if>><spring:message code="label.please.select" /></option>
			<option value="342" <c:if test="${cancellationInformation.wasCancellationCodeGiven == 342}">selected="selected"</c:if>><spring:message code="label.yes" /></option>
			<option value="343" <c:if test="${cancellationInformation.wasCancellationCodeGiven == 343}">selected="selected"</c:if>><spring:message code="label.no" /></option>
			<option value="344" <c:if test="${cancellationInformation.wasCancellationCodeGiven == 344}">selected="selected"</c:if>>Not Applicable</option>
		</select>
	</div>

	<div id="cancellationCodeText">
		<label class="large" for="cancellationCode">Cancellation code</label>

		<input id="cancellationCode" name="cancellationInfo.cancellationCode" type="text" value="${cancellationInformation.cancellationCode}" />
	</div>

	<div id="cancellationCancellationCodeGivenButNotRetained">
		<input type="checkbox" name="cancellationInfo.cancellationCodeGivenButNotRetained" id="cancellationCodeGivenButNotRetained" <c:if test="${cancellationInformation.cancellationCodeGivenButNotRetained}">checked="checked"</c:if> />

		<label for="cancellationCodeGivenButNotRetained">Cancellation code given but not advised to retain it</label>
	</div>

	<div id="cancellationReservationWithin72Hours">
		<input type="checkbox" name="cancellationInfo.reservationWithin72Hours" id="reservationWithin72Hours" <c:if test="${cancellationInformation.reservationWithin72Hours}">checked="checked"</c:if> />

		<label for="reservationWithin72Hours">Reservation was made within 72 hours of the arrival date and cancelled by 6:00 P.M. day of arrival</label>
	</div>

	<div id="cancellationMerchantNotAcceptCancellation">
		<input type="checkbox" name="cancellationInfo.merchantNotAcceptCancellation" id="merchantNotAcceptCancellation" <c:if test="${cancellationInformation.merchantNotAcceptCancellation}">checked="checked"</c:if> />

		<label for="merchantNotAcceptCancellation">Merchant did not accept cancellation prior to the specified time</label>
	</div>

	<div id="cancellationExplainWhyCancellationCodeNotGiven">
		<label class="large" for="explainWhyCancellationCodeNotGiven">Please explain why a cancellation code was not given</label>

		<textarea id="explainWhyCancellationCodeNotGiven" name="cancellationInfo.explainWhyCancellationCodeNotGiven"><c:out value="${cancellationInformation.explainWhyCancellationCodeNotGiven}" /></textarea>
	</div>

	<div id="cancellationSpokeWith">
		<label class="large" for="spokeWith"><spring:message code="label.spoke.with" /></label>

		<input id="spokeWith" maxlength="50" name="cancellationInfo.spokeWith" type="text" value="${cancellationInformation.spokeWith}" />
	</div>

	<div id="cancellationWasCardholderGivenCancellationPolicy">
		<label class="large" for="wasCardholderGivenCancellationPolicy">Was the cardholder given a cancellation policy</label>

		<select id="wasCardholderGivenCancellationPolicy" name="cancellationInfo.wasCardholderGivenCancellationPolicy">
			<option value="0" <c:if test="${cancellationInformation.wasCardholderGivenCancellationPolicy == 0}">selected="selected"</c:if>><spring:message code="label.please.select" /></option>
			<option value="350" <c:if test="${cancellationInformation.wasCardholderGivenCancellationPolicy == 350}">selected="selected"</c:if>><spring:message code="label.yes" /></option>
			<option value="351" <c:if test="${cancellationInformation.wasCardholderGivenCancellationPolicy == 351}">selected="selected"</c:if>><spring:message code="label.no" /></option>
			<option value="352" <c:if test="${cancellationInformation.wasCardholderGivenCancellationPolicy == 352}">selected="selected"</c:if>>Not Applicable</option>
		</select>
	</div>

	<div id="cancellationWhatWasCancellationPolicy">
		<label class="large" for="whatWasCancellationPolicy">What was the policy</label>

		<textarea id="whatWasCancellationPolicy" name="cancellationInfo.whatWasCancellationPolicy"><c:out value="${cancellationInformation.whatWasCancellationPolicy}" /></textarea>
	</div>

	<div id="cancellationReasonCancelledRecurring">
		<label class="large" for="reasonCancelledRecurring"><spring:message code="label.reason.cancelled" /></label>

		<textarea class="limit" id="reasonCancelledRecurring" length="500" name="cancellationInfo.reasonCancelledRecurring"><c:out value="${cancellationInformation.reasonCancelledRecurring}" /></textarea>
	</div>

	<div id="cancellationReasonCancelled">
		<label class="large" for="reasonCancelled">Cancellation reason</label>

		<textarea class="limit" id="reasonCancelled" length="500" name="cancellationInfo.reasonCancelled"><c:out value="${cancellationInformation.reasonCancelled}" /></textarea>
	</div>
</fieldset>