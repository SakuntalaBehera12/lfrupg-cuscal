package au.com.cuscal.framework.audit.liferay.dxp;

import au.com.cuscal.framework.audit.AbstractPortletAuditor;
import au.com.cuscal.framework.audit.Audit;
import au.com.cuscal.framework.audit.impl.log4j.Log4jAuditor;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.log4j.Logger;

/**
 * Provides auditing functionality for a liferay environment.
 * @author dchong
 *
 */
public class Auditor extends AbstractPortletAuditor {

	public static Auditor getInstance() {
		if (_auditor == null) {
			_auditor = new Auditor(new Log4jAuditor());
		}

		return _auditor;
	}

	public Auditor() {
	}

	public Auditor(Audit audit) {
		this.audit = audit;
	}

	public void audit(
		PortletResponse response, PortletRequest request, String origin,
		String category, String successOrFail, String message) {

		PortletSessionInfo auditInfo = getPortletSessionInfo(request);

		if (Audit.SUCCESS.equalsIgnoreCase(successOrFail)) {
			audit.success(
				origin, auditInfo.getHost(), auditInfo.getIp(),
				auditInfo.getPort(), auditInfo.getUsername(), category,
				message);
		}
		else if (Audit.FAILURE.equalsIgnoreCase(successOrFail)) {
			audit.fail(
				origin, auditInfo.getHost(), auditInfo.getIp(),
				auditInfo.getPort(), auditInfo.getUsername(), category,
				message);
		}
		else {
			audit.audit(
				origin, auditInfo.getHost(), auditInfo.getIp(),
				auditInfo.getPort(), auditInfo.getUsername(), category,
				successOrFail, message);
		}
	}

	public void fail(
		PortletResponse response, PortletRequest request, String origin,
		String category, String message) {

		audit(response, request, origin, category, Audit.FAILURE, message);
	}

	public Audit getAudit() {
		return audit;
	}

	@Override
	public PortletSessionInfo getPortletSessionInfo(PortletRequest request) {
		String host = null, ip = null, port = null, username = null;

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);
		User user;

		try {
			user = LiferayUtil.getUser(themeDisplay);
			username = user.getScreenName();
		}
		catch (Exception e) {
			username = "UNKNOWN";
			logger.error(
				"Unable to get liferay user object: " + e.getMessage(), e);
		}

		ip = LiferayUtil.getRemoteIp(request); //user.getLastLoginIP() + "/" + user.getLoginIP() + "/" +
		//System.out.println("user.getLastLoginIP()=" + ip);
		//ip = user.getLoginIP();	// possible alternate way?
		host = LiferayUtil.getRemoteHost(request);
		port = "" + LiferayUtil.getRemotePort(request);

		PortletSessionInfo info = new PortletSessionInfo(
			username, host, ip, port);

		return info;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	public void success(
		PortletResponse response, PortletRequest request, String origin,
		String category, String message) {

		audit(response, request, origin, category, Audit.SUCCESS, message);
	}

	private static Auditor _auditor = null;

	private Logger logger = Logger.getLogger(Auditor.class);

}