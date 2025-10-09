
<%
/**
* Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
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
%>

<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ page import="com.liferay.portal.kernel.service.UserLocalServiceUtil" %>

<portlet:defineObjects />


<script type="text/javascript">
function setUserId(hiddenform,email,phone){
	
	document.getElementById(hiddenform).value =  document.getElementById("userid").value;
	
	return validate(email,phone);
	} 
	
function validate(email,phone) {
	   var regEmail = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
	   var regPhone = /^([0-9\(\)\/\+ \-]*)$/;
	   
	   var address = document.getElementById(email).value;
	   var phone = document.getElementById(phone).value;
	   
	   if(regEmail.test(address) == false) {
	      alert('Please enter valid Email Address..');
	      return false;
	   }
	   
	   if(regPhone.test(phone) == false) {
		  alert('Please enter valid Phone Number..');
		  return false;
		}
	   
	   return true;
	}
	
function setUserIdForPCT() {
	document.getElementById("cuscalConnectUserId").value = document.getElementById("userid").value;
}
</script>

<div>
	<div class="navigation" id="secondaryNav">
		<div id="secondaryTop"></div>
		<ul>
			<li class="first"><a href="javascript: void(0);">Manage your account</a></li>
		</ul>
		<div id="secondaryBottom"></div>
	</div>
	<div class="content" id="manageAccountContent">
		<a name="update-details"></a>
		<h1 id="contentHeading" style="margin: 0;">
			<liferay-ui:message key="manage.account.header" />
		</h1>
		<div id="manageAccountForm1">
			<h3><liferay-ui:message key="manage.account.details.header" /></h3>
			<label for="account.details">
				<liferay-ui:message key="manage.account.details.desc" />
			</label>
			<br />
			<%
				if(request.getAttribute("userDetailsMissingError")!=null && (Boolean)request.getAttribute("userDetailsMissingError")){%>
			<label class="warning">
				<liferay-ui:message key="manage.account.userDetailsMissingError.label" />
			</label> <% }
				if(request.getAttribute("userDetailsUpdateSuccess")!=null && (Boolean)request.getAttribute("userDetailsUpdateSuccess")){%>
			<label class="successful">
				<liferay-ui:message key="manage.account.userDetailsUpdateSuccess.label" />
			</label> <% } %>
			
			<form class="accountManagementForm" name="<portlet:namespace />ManageAccountPortletUserForm"
				method="post" action="<portlet:actionURL />" onsubmit="return setUserId('hiddenuserIdForm1','email','phoneNo')">
				<dl>
					<dt>
						<label for="userid"><liferay-ui:message	key="user.account.user.id" /></label>
					</dt>
					<dd>
						<input id="userid" name="userid" type="text" value='<%=renderRequest.getAttribute("screenName")%>' disabled />
					</dd>
					<dt>
						<label for="fname"><liferay-ui:message key="manage.account.fname" /><span class="required">*</span></label>
					</dt>
					<dd>
						<input id="fname" name="fname" type="text" value='<%=renderRequest.getAttribute("firstName")%>'>
					</dd>
					<dt>
						<label for="lname"><liferay-ui:message key="manage.account.lname" /><span class="required">*</span></label>
					</dt>
					<dd>
						<input id="lname" name="lname" autocomplete="off" type="text" value='<%=renderRequest.getAttribute("lastName")%>' />
					</dd>
					<dt>
						<label for="cname"><liferay-ui:message key="manage.account.client.name" /></label>
					</dt>
					<dd>
						<input id="cname" name="cname" type="text" value='<%=renderRequest.getAttribute("clientName")%>' disabled />
					</dd>
					<dt>
						<label for="email"><liferay-ui:message key="manage.account.email" /><span class="required">*</span></label>
					</dt>
					<dd><input id="email" name="email" autocomplete="off" type="text" 
						value='<%=renderRequest.getAttribute("email")%>' />
					</dd>
					<dt>
						<label for="phoneNo"><liferay-ui:message key="manage.account.phoneNo" /><span class="required">*</span></label>
					</dt>
					<dd>
						<input id="phoneNo" name="phoneNo" autocomplete="off" type="text" value='<%=renderRequest.getAttribute("phoneNo")%>' />
					</dd>
					<dt></dt>
					<dd>
						<input type="submit" value="<liferay-ui:message key="manage.account.details.button.label" />" class="button" />
						<input id="hiddenuserIdForm1" name="hiddenuserIdForm1" type="hidden" value="" />
						<input id="formName" name="formName" type="hidden" value='updateDetails' />
					</dd>
				</dl>
			</form>
		</div>
		<br />
		<div id="manageAccountForm2">
			<a name="update-password"></a>
			<h3><liferay-ui:message	key="manage.account.password.header" /></h3>
			<label for="cpw_msg">
				<liferay-ui:message key="manage.account.password.desc" />
			</label>
			<br />
			<%
				if(request.getAttribute("invalidPassword")!=null && (Boolean)request.getAttribute("invalidPassword")){%>
			<label class="warning">
				<liferay-ui:message key="resetPassword.invalidPassword.label" />
			</label> <% }
				if(request.getAttribute("verifyPassFailed")!=null && (Boolean)request.getAttribute("verifyPassFailed")){%>
			<label class="warning">
				<liferay-ui:message key="resetPassword.invalidOldPassword.label" />
			</label> <% }
				if(request.getAttribute("noMatchPassword")!=null && (Boolean)request.getAttribute("noMatchPassword")){%>
			<label class="warning">
				<liferay-ui:message key="resetPassword.noMatchPassword.label" />
			</label> <% }
				if(request.getAttribute("passUpdateSuccessMsg")!=null && (Boolean)request.getAttribute("passUpdateSuccessMsg")){%>
			<label class="successful">
				<liferay-ui:message key="manage.account.passUpdateSuccessMsg.label" />
			</label> <% } %>
			
			<form class="accountManagementForm" name="<portlet:namespace />ManageAccountPortletresetPasswordForm"
				method="post" action="<portlet:actionURL />" onsubmit="return setUserId('hiddenuserIdForm2','email','phoneNo')">
				<dl>
					<dt>
						<label for="oldPassword"><liferay-ui:message key="manage.account.password.old" /></label>
					</dt>
					<dd>
						<input id="oldPassword" name="oldPassword" autocomplete="off" type="password" value='' />
					</dd>
					<dt>
						<label for="newPassword"><liferay-ui:message key="manage.account.password.new" /></label>
					</dt>
					<dd>
						<input id="newPassword" name="newPassword" autocomplete="off" type="password" value='' />
					</dd>
					<dt>
						<label for="retypePassword"><liferay-ui:message key="manage.account.password.new.again" /></label>
					</dt>
					<dd>
						<input id="retypePassword" name="retypePassword" autocomplete="off" type="password" value='' />
					</dd>
					<dt></dt>
					<dd>
						<input type="submit" value="<liferay-ui:message key="manage.account.password.button.label" />" class="button" />
						<input id="formName" name="formName" type="hidden" value='resetPassword' />
						<input id="hiddenuserIdForm2" name="hiddenuserIdForm2" type="hidden" value="" />
					</dd>
				</dl>
			</form>
		</div>
		
		<br />
		<div id="manageAccountForm3">
			<a name="update-password-questions"></a>
			<h3><liferay-ui:message key="manage.account.secret.questions.header" /></h3>
			<label for="secQ_msg">
				<liferay-ui:message key="manage.account.secret.questions.desc" />
			</label> <br />
			<% if(request.getAttribute("answUpdateSuccessMsg")!=null && (Boolean)request.getAttribute("answUpdateSuccessMsg")){%>
			<label class="successful">
				<liferay-ui:message key="manage.account.answUpdateSuccessMsg.label" />
			</label> <br />
			<br />
			<% }
				if(request.getAttribute("answUpdateErrorMsg")!=null && (Boolean)request.getAttribute("answUpdateErrorMsg")){%>
			<label class="warning">
				<liferay-ui:message key="manage.account.answUpdateErrorMsg.label" />
			</label> <br />
			<br />
			<% } %>
			
			<form class="accountManagementForm" name="<portlet:namespace/>ManageAccountPortlet" method="post"
				action="<portlet:actionURL />" onsubmit="return setUserId('hiddenuserIdForm3','email','phoneNo')">
				<dl>
					<dt>
						<label><liferay-ui:message key="user.account.secret.question.1.label" /></label>
					</dt>
					<dd>
						<liferay-ui:message key="user.account.secret.question.1" />
					</dd>
					<dt>
						<label for="secQ_ans1"><liferay-ui:message key="user.account.secret.question.answer.label" /></label>
					</dt>
					<dd>
						<input id="secQ_ans1" name="secQ_ans1" type="text" value='' autocomplete="off" />
					</dd>
					<dt>
						<label><liferay-ui:message key="user.account.secret.question.2.label" /></label>
					</dt>
					<dd>
						<liferay-ui:message key="user.account.secret.question.2" />
					</dd>
					<dt>
						<label for="secQ_ans2"><liferay-ui:message key="user.account.secret.question.answer.label" /></label>
					</dt>
					<dd>
						<input id="secQ_ans2" name="secQ_ans2" type="text" value='' autocomplete="off" />
					</dd>
					<dt></dt>
					<dd>
						<input type="Submit" value="<liferay-ui:message key="manage.account.secret.questions.button.label" />" class="button" />
						<input id="formName" name="formName" type="hidden" value='updateAnswers' />
						<input id="hiddenuserIdForm3" name="hiddenuserIdForm3" type="hidden" value="" />
					</dd>
				</dl>
			</form>
		</div>
		
		<%-- <% if (renderRequest.getAttribute("changePCTPassword") != null && (Boolean)renderRequest.getAttribute("changePCTPassword")) { %>
		<br />
		
		<div id="update-pct-password">
			<a name="pct-password"></a>
			<form class="accountManagementForm" name="<portlet:namespace />_PCT_Password" action="<portlet:actionURL />" method="post" onSubmit="javascript: setUserIdForPCT();">
				<h3><liferay-ui:message key="pct.password.title" /></h3>
				<span><liferay-ui:message key="pct.password.description" /></span>
				
				<% 
				if (renderRequest.getAttribute("pctFormDisplay") != null && (Boolean)renderRequest.getAttribute("pctFormDisplay")) {
					
				if(request.getAttribute("updatePctPasswordSuccess") != null ) {
					if ((Boolean) request.getAttribute("updatePctPasswordSuccess")) {%>
					<p class="portlet-msg-success">
						<liferay-ui:message key="pct.password.update.success" />
					</p>
				<% } else { %>
					<p class="portlet-msg-error">
						 <liferay-ui:message key="pct.password.update.failed" />
					</p>
				<% } } %>
				
				<dl>
					<dt>
						<label><liferay-ui:message key="pct.user.id" /></label>
					</dt>
					<dd>
						<span><%= request.getAttribute("pctUserId") %></span>
					</dd>
					
					<dt>
						<label for="cuscalConnectPassword"><liferay-ui:message key="pct.cuscal.connect.password" /></label><br />
					</dt>
					<dd>
						<input type="password" name="cuscalConnectPassword" id="cuscalConnectPassword" autocomplete="off">
						<% if(request.getAttribute("invalidPortalPassword") != null && (Boolean)request.getAttribute("invalidPortalPassword")) { %>
						<span class="error">
							<liferay-ui:message key="pct.cuscal.connect.password.invalid" />
						</span>
						<% } %>
					</dd>
					
					<dt>
						<label for="pctPassword"><liferay-ui:message key="pct.new.password" /></label>
					</dt>
					<dd>
						<input type="password" name="pctPassword" id="pctPassword" value="" autocomplete="off" maxlength="4">
						<% if(request.getAttribute("invalidPctPassword") != null && (Boolean) request.getAttribute("invalidPctPassword")) { %>
						<span class="error">
							<liferay-ui:message key="pct.password.invalid" />
						</span>
						<% } %>
						<% if(request.getAttribute("passwordsNotEqual") != null && (Boolean)request.getAttribute("passwordsNotEqual")) { %>
						<span class="error">
							<liferay-ui:message key="pct.password.not.match" />
						</span>
						<% } %>
						
						<% if(request.getAttribute("newAndOldPasswordSame") != null && (Boolean) request.getAttribute("newAndOldPasswordSame")) { %>
						<span class="error">
							<liferay-ui:message key="pct.password.old" />
						</span>
						<% } %>
					</dd>
					
					<dt>
						<label for="retypePctPassword"><liferay-ui:message key="pct.retype.new.password" /></label>
					</dt>
					<dd>
						<input type="password" name="retypePctPassword" id="retypePctPassword" value="" autocomplete="off" maxlength="4">
						<% if(request.getAttribute("invalidRetypePctPassword") != null && (Boolean) request.getAttribute("invalidRetypePctPassword")) { %>
						<span class="error">
							<liferay-ui:message key="pct.retype.password.invalid" />
						</span>
						<% } %>
					</dd>
					
					<dt>
					</dt>
					<dd>
						<input type="submit" value="<liferay-ui:message key="pct.button.label" />" class="button" />
						<input type="hidden" name="cuscalConnectUserId" id="cuscalConnectUserId" value="">
						<input type="hidden" name="formName" type="hidden" value="changePctPassword">						
					</dd>
					
				</dl>
				<% } else { %>
					<span class="portlet-msg-error">
						<liferay-ui:message key="pct.webservice.error" />
					</span>
				<% } %>
			</form>
		</div>
		<% } %> --%>
	</div>
</div>

<% if (request.getAttribute("scrollTo") != null) {%>
<script type="text/javascript">
jQuery.noConflict();

function scrollToAnchor(aid){
    var aTag = jQuery("a[name='"+ aid +"']");
    jQuery('html,body').animate({scrollTop: aTag.offset().top},'slow');
}

Liferay.on('allPortletsReady', function() {
	setTimeout("scrollToAnchor('<%= request.getAttribute("scrollTo") %>')", 500);
});
</script>
<% } %>