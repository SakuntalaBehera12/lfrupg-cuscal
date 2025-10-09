<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@
taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@
taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %><%@
taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>

<%@ page contentType="text/html" isELIgnored="false" %>
<portlet:actionURL escapeXml="false" var="formSubmit">
	<portlet:param name="action" value="pinChangeSearchAction" />
</portlet:actionURL>

<h1>PIN Change History</h1>

<form:form action="${formSubmit}" autocomplete="off" method="post" modelAttribute="pinChangeSearchForm">
	<c:choose>
	<c:when test="${empty sessionScope.pinSearchResults}">
		<div id="search-form-wrapper" style="display: block;">
	</c:when>
	<c:otherwise>
		<div id="search-form-wrapper" style="display: none;">
	</c:otherwise>
	</c:choose>
		<fieldset>
			<legend>PIN Change Details<em>*</em></legend>

			<p>Please fill in at least one of these fields</p>

			<div>
				<label for="pin-change-pan">PAN:</label>

				<form:input autocomplete="off" id="pin-change-pan" path="panBin" type="text" value="" />

				<br />

				<form:errors class="error" path="panBin" />
			</div>

			<div>
				<label for="pin-change-terminal">Terminal ID:</label>

				<form:input autocomplete="off" id="pin-change-terminal" path="terminalId" type="text" value="" />
				<%-- <form:input id="pin-change-terminal" path="terminalId" type="text" value="CPCTLN01" /> --%>
				<br />

				<form:errors class="error" path="terminalId" />
			</div>
		</fieldset>

		<fieldset>
			<legend>Date/Time</legend>

			<div>
				<label for="pin-change-start-date">Start Date:<em>*</em></label>
				<%-- <form:input id="pin-change-start-date" path="startDate" value="06/11/2012" /> --%>
				<form:input class="date-pick" id="pin-change-start-date" path="startDate" value="" />

				<form:select path="startTimeHr">
					<c:forEach items="${pinChangeSearchForm.startHourDisplay}" var="hourCount">
					<form:option value="${hourCount}">
						<fmt:formatNumber minIntegerDigits="2" value="${hourCount}" />
					</form:option>
					</c:forEach>
				</form:select>

				<form:select path="startTimeMin">
					<c:forEach items="${pinChangeSearchForm.startMinuteDisplay}" var="minuteCount">
					<form:option value="${minuteCount}">
						<fmt:formatNumber minIntegerDigits="2" value="${minuteCount}" />
					</form:option>
					</c:forEach>
				</form:select>

				<br />

				<form:errors class="error" path="startDate" />
			</div>

			<div>
				<label for="pin-change-end-date">End Date:<em>*</em></label>
				<%-- <form:input id="pin-change-end-date" path="endDate" value="06/11/2012" /> --%>
				<form:input class="date-pick" id="pin-change-end-date" path="endDate" value="" />

				<form:select path="endTimeHr">
					<c:forEach items="${pinChangeSearchForm.endHourDisplay}" var="hourCount">
					<form:option value="${hourCount}">
						<fmt:formatNumber minIntegerDigits="2" value="${hourCount}" />
					</form:option>
					</c:forEach>
				</form:select>

				<form:select path="endTimeMin">
					<c:forEach items="${pinChangeSearchForm.endMinuteDisplay}" var="minuteCount">
					<form:option value="${minuteCount}">
						<fmt:formatNumber minIntegerDigits="2" value="${minuteCount}" />
					</form:option>
					</c:forEach>
				</form:select>

				<br />

				<form:errors class="error" path="endDate" />
			</div>
		</fieldset>

		<br />

		<input type="Submit" value="Search" />

		<br />

		<c:if test="${moreOrg}">
			<div class="portlet-msg-info">
				<span><spring:message code="cuscal.transaction.error.more.org.msg" /></span>
			</div>
		</c:if>
	</div>
</form:form>