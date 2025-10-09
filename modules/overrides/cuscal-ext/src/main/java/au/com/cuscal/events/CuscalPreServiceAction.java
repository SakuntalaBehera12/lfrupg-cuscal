package au.com.cuscal.events;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Ha Tang
 */
@Component(
	property = "key=servlet.service.events.pre", service = LifecycleAction.class
)
public class CuscalPreServiceAction extends Action {

	@Override
	public void run(
			final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse)
		throws ActionException {

		if (PropsValues.BROWSER_CACHE_DISABLED ||
			PropsValues.BROWSER_CACHE_SIGNED_IN_DISABLED) {

			return;
		}

		if (_CUSCAL_CUSTOM_BROWSER_CACHE_DISABLED) {
			httpServletResponse.setDateHeader(HttpHeaders.EXPIRES, 0);
			httpServletResponse.setHeader(
				HttpHeaders.CACHE_CONTROL,
				_CUSCAL_CUSTOM_CACHE_CONTROL_NO_CACHE_VALUE);
			httpServletResponse.setHeader(
				HttpHeaders.PRAGMA, HttpHeaders.PRAGMA_NO_CACHE_VALUE);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Cuscal custom - Disable browser cache");
		}
	}

	private static boolean _CUSCAL_CUSTOM_BROWSER_CACHE_DISABLED =
		GetterUtil.getBoolean(
			PropsUtil.get("cuscal.custom.browser.cache.disabled"));

	private static final String _CUSCAL_CUSTOM_CACHE_CONTROL_NO_CACHE_VALUE =
		"private, no-cache, must-revalidate";

	private static Log _log = LogFactoryUtil.getLog(
		CuscalPreServiceAction.class);

}