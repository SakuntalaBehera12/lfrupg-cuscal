package com.tcs.cuscal.portlet;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.model.Ticket;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortlet;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.TicketLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.Validator;

import com.tcs.cuscal.dao.LDAPUtil;

import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.internet.InternetAddress;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class ResetPassword extends LiferayPortlet {

	String fromPage = null;
	boolean success = false;

	public ResetPassword() {
	}

	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		_log.debug("In doView");
		System.out.println("in doview");
		System.out.println("fromPage is " + this.fromPage);
		String toView = this.passwordRecoveryJSP;

		if (StringUtils.equals(this.fromPage, "reset") && this.success) {
			toView = this.successJSP;
		}
		else if (StringUtils.equals(this.fromPage, "passRecovery_retypePage")) {
			toView = this.retypePasswordJSP;
		}
		else if (StringUtils.equals(this.fromPage, "mail")) {
			toView = this.retypePasswordJSP;
		}

		System.out.println("toView is " + toView);
		this.include(toView, renderRequest, renderResponse);
		this.success = false;
		this.fromPage = "";
	}

	public void init() {
		_log.debug("in init");
		this.passwordRecoveryJSP = this.getInitParameter(
			"passwordRecovery-jsp");
		this.retypePasswordJSP = this.getInitParameter("retypePassword-jsp");
		this.successJSP = this.getInitParameter("success-jsp");
		InputStream iStream = null;

		try {
			iStream = CuscalSharedPropsUtil.getResourceStream(
				getClass(), "ldap-config.properties");
			Properties props = new Properties();

			props.load(iStream);

			String tickMaxAgeStr = props.getProperty("ticketMaxAge");

			if ((tickMaxAgeStr != null) &&
				(tickMaxAgeStr.trim(
				).length() > 0)) {

				_log.info(
					"Read ticketMaxAge from ldap-config, value=" +
						tickMaxAgeStr);

				try {
					ticketMaxAge = Integer.parseInt(tickMaxAgeStr);
				}
				catch (Exception var5) {
				}
			}
		}
		catch (Exception e) {
			_log.error(e.getMessage());
		}

		_log.info("ticketMaxAge set to " + ticketMaxAge);
	}

	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

		this.success = false;
		this.fromPage = ParamUtil.getString(actionRequest, "fromPage");
		System.out.println("fromPage is " + this.fromPage);
		_log.debug("fromPage is " + this.fromPage);
		ServiceContext serviceContext = null;

		try {
			serviceContext = ServiceContextFactory.getInstance(
				User.class.getName(), actionRequest);
		}
		catch (PortalException var28) {
			var28.printStackTrace();
		}
		catch (SystemException var29) {
			var29.printStackTrace();
		}

		User user;

		if (StringUtils.equals(this.fromPage, "reset")) {
			boolean sendMail = false;
			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute("THEME_DISPLAY");

			Company company = themeDisplay.getCompany();
			long companyId = 0L;
			user = null;
			String passwordResetURL = null;

			try {
				user = this.getUser(actionRequest);
				String body;
				String fromName;

				if (user != null) {
					String userId = ParamUtil.getString(
						actionRequest, "userId");
					String answer1 = ParamUtil.getString(
						actionRequest, "answer1");
					String answer2 = ParamUtil.getString(
						actionRequest, "answer2");
					Attributes attrb = LDAPUtil.getInstance(
					).getAttribute(
						userId
					);

					Attribute secretansw1 = attrb.get("secretansw1");
					body = null;
					fromName = null;

					if (secretansw1 != null) {
						body = (String)secretansw1.get();
					}

					Attribute secretansw2 = attrb.get("secretansw2");

					if (secretansw2 != null) {
						fromName = (String)secretansw2.get();
					}

					if (StringUtils.equals(answer1, body) &&
						StringUtils.equals(answer2, fromName)) {

						sendMail = true;
						_log.debug("Answers matched..");
					}
					else {
						_log.debug("Answers did not match");
					}
				}

				if (sendMail) {
					PasswordPolicy passwordPolicy = user.getPasswordPolicy();
					long plid = serviceContext.getPlid();

					if (company.isSendPasswordResetLink()) {
						Date expirationDate = new Date(
							System.currentTimeMillis() +
								(long)(ticketMaxAge * 60 * 1000));

						_log.debug(
							"Ticket will be set to expire on " +
								expirationDate);
						Ticket ticket = TicketLocalServiceUtil.addTicket(
							company.getCompanyId(), User.class.getName(),
							user.getUserId(), 3, null, expirationDate,
							serviceContext);
						HttpServletRequest request =
							PortalUtil.getHttpServletRequest(actionRequest);

						fromName = PortalUtil.getPortletId(request);
						PortletURL url = PortletURLFactoryUtil.create(
							request, fromName, plid, "ACTION_PHASE");

						url.setWindowState(actionRequest.getWindowState());
						passwordResetURL =
							url.toString() + "&ticket=" + ticket.getKey();
					}

					ResourceBundle resourceBundle = ResourceBundle.getBundle(
						"com.tcs.cuscal.resources.Language");

					String subject = resourceBundle.getString(
						"resetPassword.mailSubject");
					body = resourceBundle.getString("resetPassword.mailBody");

					if (body != null) {
						body = body.replace("{link}", passwordResetURL);
					}

					fromName = null;
					String fromAddress = null;

					if (Validator.isNull(fromName)) {
						fromName = PrefsPropsUtil.getString(
							companyId, "admin.email.from.name");
					}

					if (Validator.isNull(fromAddress)) {
						fromAddress = PrefsPropsUtil.getString(
							companyId, "admin.email.from.address");
					}

					String toName = user.getFullName();
					String toAddress = user.getEmailAddress();
					InternetAddress from = new InternetAddress(
						fromAddress, fromName);
					InternetAddress to = new InternetAddress(toAddress, toName);

					MailMessage message = new MailMessage(
						from, to, subject, body, true);

					MessageBusUtil.sendMessage("liferay/mail", message);
					actionRequest.setAttribute("successMsg", Boolean.TRUE);
					this.success = true;
				}
				else {
					actionRequest.setAttribute("errorMsg", Boolean.TRUE);
				}
			}
			catch (Exception var30) {
				var30.printStackTrace();
			}
		}
		else if (StringUtils.equals(this.fromPage, "passRecovery_retypePage")) {
			Ticket ticket = this.getTicket(actionRequest);
			long userId = 0L;
			String newPassword;

			if (ticket != null) {
				userId = ticket.getClassPK();
				newPassword = actionRequest.getParameter("newPassword");
				String retypePassword = actionRequest.getParameter(
					"retypePassword");
				user = null;

				if (StringUtils.equals(newPassword, retypePassword)) {
					boolean updateSuccess = false;

					try {
						user = UserLocalServiceUtil.getUser(userId);
					}
					catch (SystemException var26) {
						var26.printStackTrace();
					}
					catch (PortalException var27) {
						var27.printStackTrace();
					}

					try {
						updateSuccess = LDAPUtil.getInstance(
						).modifyPasswordAttributeForForgotPassword(
							user.getScreenName(), newPassword
						);
					}
					catch (NamingException var25) {
						System.out.println("exception occurred");
						_log.warn(var25.getMessage(), var25);
						var25.printStackTrace();
					}

					if (updateSuccess) {
						try {
							TicketLocalServiceUtil.deleteTicket(ticket);
						}
						catch (SystemException var24) {
							var24.printStackTrace();
						}

						actionRequest.setAttribute("success", Boolean.TRUE);
					}
					else {
						actionRequest.setAttribute(
							"invalidPassword", Boolean.TRUE);
						actionRequest.setAttribute(
							"ticket",
							ParamUtil.get(actionRequest, "ticket", ""));
					}
				}
				else {
					actionRequest.setAttribute("noMatchPassword", Boolean.TRUE);
					actionRequest.setAttribute(
						"ticket", ParamUtil.get(actionRequest, "ticket", ""));
				}
			}
			else {
				newPassword = serviceContext.getPortalURL();
				actionResponse.sendRedirect(newPassword);
			}
		}
		else {
			String ticketStr = ParamUtil.get(actionRequest, "ticket", "");

			if (!StringUtils.isEmpty(ticketStr)) {
				Ticket ticket = this.getTicket(actionRequest);

				if (ticket != null) {
					this.fromPage = "mail";
					actionRequest.setAttribute("ticket", ticketStr);
					this.sendRedirect(actionRequest, actionResponse);
				}
				else {
					String homeURL = serviceContext.getPortalURL();

					actionResponse.sendRedirect(homeURL);
				}
			}
			else {
				this.fromPage = "reset";
			}
		}
	}

	protected Ticket getTicket(ActionRequest actionRequest) {
		String token = ParamUtil.getString(actionRequest, "ticket");

		if (Validator.isNull(token)) {
			return null;
		}

		Ticket ticket = null;

		try {
			ticket = TicketLocalServiceUtil.getTicket(token);

			if (!ticket.isExpired()) {
				return ticket;
			}

			_log.warn(
				"Access to expired ticket attempt: " + token +
					".  Ticket will be ignored. Ticket(" + ticket + ")");
			TicketLocalServiceUtil.deleteTicket(ticket);
		}
		catch (Exception var5) {
			_log.error(
				"Error processing ticket(" + ticket + "): " + var5.getMessage(),
				var5);
		}

		return null;
	}

	protected User getUser(ActionRequest actionRequest) {
		String screenName = ParamUtil.getString(actionRequest, "userId");
		User user = null;

		if (Validator.isNotNull(screenName)) {
			try {
				user = UserLocalServiceUtil.getUserByScreenName(
					PortalUtil.getCompanyId(actionRequest), screenName);
			}
			catch (PortalException var5) {
				var5.printStackTrace();
			}
			catch (SystemException var6) {
				var6.printStackTrace();
			}
		}

		return user;
	}

	protected void include(
			String path, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws IOException, PortletException {

		PortletRequestDispatcher portletRequestDispatcher =
			this.getPortletContext(
			).getRequestDispatcher(
				path
			);

		if (portletRequestDispatcher == null) {
			_log.error(path + " is not a valid include");
		}
		else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	protected String errorViewJSP;
	protected String loginJSP;
	protected String passwordRecoveryJSP;
	protected String retypePasswordJSP;
	protected String successJSP;

	private static Log _log = LogFactoryUtil.getLog(ResetPassword.class);

	private static final String propertyConfiguration = "ldap-config";
	private static int ticketMaxAge = 10;

}