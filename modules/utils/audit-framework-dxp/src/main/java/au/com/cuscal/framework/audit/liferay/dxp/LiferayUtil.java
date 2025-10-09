package au.com.cuscal.framework.audit.liferay.dxp;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.portlet.PortletRequest;

public class LiferayUtil {

	public static final int ENTITY_ATTRIBUTE_LIFERAY_ORGANISATION_ID = 4;

	public static final String ORG_SHORT_NAME = "orgShortName";

	public static String getActorsHost(String ip) {
		try {
			InetAddress addr = InetAddress.getByName(ip);

			return addr.getHostName();
		}
		catch (UnknownHostException e) {
			if (_log.isErrorEnabled()) {
				_log.error(e);
			}
		}

		return null;
	}

	public static String getRemoteHost(PortletRequest request) {
		String host = PortalUtil.getHttpServletRequest(
			request
		).getRemoteHost();

		return host;
	}

	public static String getRemoteIp(PortletRequest request) {
		String ip = PortalUtil.getHttpServletRequest(
			request
		).getRemoteAddr();

		return ip;
	}

	public static int getRemotePort(PortletRequest request) {
		int port = PortalUtil.getHttpServletRequest(
			request
		).getRemotePort();

		return port;
	}

	public static ThemeDisplay getThemeDisplay(PortletRequest request) {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return themeDisplay;
	}

	public static User getUser(long userId) throws PortalException {
		User user = UserLocalServiceUtil.getUser(userId);

		return user;
	}

	public static User getUser(PortletRequest request) throws PortalException {
		ThemeDisplay themeDisplay = getThemeDisplay(request);

		return getUser(themeDisplay);
	}

	public static User getUser(ThemeDisplay themeDisplay)
		throws PortalException {

		long userId = themeDisplay.getUserId();

		return getUser(userId);
	}

	private static Log _log = LogFactoryUtil.getLog(LiferayUtil.class);

}