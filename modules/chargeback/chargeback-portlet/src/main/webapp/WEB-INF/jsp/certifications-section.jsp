<%-- Certifications fieldset --%>
<fieldset class="transactions" id="certifications">
	<legend>Certifications <a href="javascript:;" title="Please select one or more of the following"><strong>?</strong></a></legend>

	<form:errors cssClass="error" path="certifications" />

	<c:forEach items="${certifications}" var="cert">
		<div id="certCb${cert.key}">
			<form:checkbox id="${cert.key}" path="certifications" value="${cert.key}" /><label for="${cert.key}">${cert.value}</label>
		</div>
	</c:forEach>
</fieldset>