<%@ include file="init.jsp" %>

<%-- Hidden fields to hold the transaction information. --%>
<form:hidden class="masked-pan" path="transactionInformation.maskedPan" />
<form:hidden class="acquirer-id" path="transactionInformation.acquirerId" />
<form:hidden class="acquirer-name" path="transactionInformation.acquirerName" />
<form:hidden class="issuer-id" path="transactionInformation.issuerId" />
<form:hidden class="issuer-name" path="transactionInformation.issuerName" />
<form:hidden class="terminal-id" path="transactionInformation.terminalId" />
<form:hidden class="location-date" path="transactionInformation.locationDate" />
<form:hidden class="atm-or-pos" path="transactionInformation.atmPos" />
<form:hidden class="transaction-amount-value" path="transactionInformation.transactionAmount" />
<form:hidden class="transaction-amount-currency" path="transactionInformation.transactionAmountCurrency" />
<form:hidden class="cardholder-amount-value" path="transactionInformation.cardholderAmount" />
<form:hidden class="cardholder-amount-currency" path="transactionInformation.cardholderAmountCurrency" />
<form:hidden class="stan" path="transactionInformation.stan" />
<form:hidden class="cuscal-transaction-id" path="transactionInformation.cuscalTransactionId" />
<form:hidden class="visa-transaction-id" path="transactionInformation.visaTransactionId" />
<form:hidden class="arn" path="transactionInformation.arn" />
<form:hidden class="business-date" path="transactionInformation.businessDate" />

<fieldset class="contact-details">
	<legend>Your Contact Details</legend>

	<span>Please note any changes made here will be saved to your Cuscal Connect Portal profile.</span>

	<br />

	<html:inputHidden fieldClass="contact-organisation" fieldValue="Cuscal" label="Organisation" name="contactInformation.organisation" />

	<br />

	<html:inputField errorClass="error" errorId="given-name-error" fieldClass="given-name" label="First Name" name="contactInformation.givenName" requiredField="yes" />
	<%-- <form:errors cssClass="error" path="contactInformation.givenName" /> --%>
	<html:inputField errorClass="error" errorId="contact-surname-error" fieldClass="contact-surname" label="Last Name" name="contactInformation.surname" requiredField="yes" />
	<%-- <form:errors cssClass="error" path="contactInformation.surname" /> --%>

	<br />

	<html:inputField errorClass="error" errorId="contact-email-error" fieldClass="contact-email" label="Email" name="contactInformation.email" requiredField="yes" />
	<%-- <form:errors cssClass="error" path="contactInformation.email" /> --%>
	<html:inputField errorClass="error" errorId="contact-phone-error" fieldClass="contact-phone" label="Phone Number" name="contactInformation.phoneNumber" requiredField="yes" />
	<%-- <form:errors cssClass="error" path="contactInformation.phoneNumber" /> --%>
</fieldset>

<fieldset class="member-details">
	<legend>Member/Customer</legend>

	<html:ajaxHidden fieldClass="member-name" label="Name" name="memberInformation.memberName" />

	<html:inputField errorClass="error" errorId="member-number" fieldClass="member-number" label="Member/Customer No." labelClass="large" name="memberInformation.memberNumber" requiredField="no" />
	<%-- <form:errors cssClass="error" path="memberInformation.memberName" /> --%>
</fieldset>