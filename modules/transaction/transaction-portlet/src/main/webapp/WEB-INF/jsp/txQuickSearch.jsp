<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %><%@
taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<%@ page contentType="text/html" isELIgnored="false" %>
<portlet:defineObjects />

<portlet:actionURL var="actionURL" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta content="text/html; charset=ISO-8859-1" http-equiv="Content-Type" />

		<title>Quick Transaction</title>
	</head>

	<body>
		<form:form action="${actionURL}" id="transactionForm" method="post" modelAttribute="transactionForm" name="transactionForm">
			<div>
				<p>Transaction Search</p>

				<div>
					<div>
						<form:radiobutton label="PAN" path="" />
						<form:radiobutton label="Terminal ID" path="" />
					</div>

					<div>
						<form:input id="panOrTid" path="panOrTid" value="${transactionForm.panOrTid}" />

						<form:errors cssClass="error" path="panOrTid" />

						<br />
					</div>

					<a href="${actionURL}">Search</a>
				</div>

				<br />
			</div>
		</form:form>
	</body>
</html>