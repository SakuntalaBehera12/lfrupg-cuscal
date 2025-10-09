<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/META-INF/resources/init.jsp" %>

<form class="cuscal-julian-date-converter" name="julian-date-converter-form">
	<h6>Date Converter</h6>

	<input checked id="ddmmyyyy" name="convert-date" onclick="javascript:doSomething();" type="radio" value="ddmmyyyy" />

	<label for="ddmmyyyy">To DD/MM/YYYY</label>

	<br />

	<input id="julian" name="convert-date" onclick="javascript:doSomething();" type="radio" value="julian" />

	<label for="julian">To Julian</label>

	<div>
		<label for="date">Date</label>

		<input class="entered-date" id="date" name="date" type="text" />
	</div>

	<div>The Julian date is: <span class="result-date"></span></div>

	<input class="btn calculate" type="button" value="Convert" />
</form>