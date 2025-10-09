package com.tcs.cuscal;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.cookies.constants.CookiesConstants;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.util.CookieKeys;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;

/**
 * @author Maurice Sepe
 */
@Component(
	immediate = true,
	property = {
		"before-filter=Auto Login Filter", "dispatcher=FORWARD",
		"dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=Cuscal Security Filter",
		"url-pattern=/c/portal/logout"
	},
	service = Filter.class
)
public class CuscalSecurityFilter extends BaseFilter {

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	protected void processFilter(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws Exception {

		String requestURI = GetterUtil.getString(
			httpServletRequest.getRequestURI());

		if (requestURI.endsWith(_PORTAL_LOGOUT)) {
			cleanCookies(httpServletRequest, httpServletResponse);
		}

		super.processFilter(
			httpServletRequest, httpServletResponse, filterChain);
	}

	private void cleanCookies(
		final HttpServletRequest request, final HttpServletResponse response) {

		final HttpSession session = request.getSession();
		final String domain = CookieKeys.getDomain(request);
		final Cookie companyIdCookie = new Cookie(
			CookieKeys.COMPANY_ID, StringPool.BLANK);

		if (Validator.isNotNull(domain)) {
			companyIdCookie.setDomain(domain);
		}

		companyIdCookie.setMaxAge(0);
		companyIdCookie.setPath("/");
		final Cookie idCookie = new Cookie(
			CookiesConstants.NAME_ID, StringPool.BLANK);

		if (Validator.isNotNull(domain)) {
			idCookie.setDomain(domain);
		}

		idCookie.setMaxAge(0);
		idCookie.setPath(StringPool.SLASH);
		final Cookie passwordCookie = new Cookie(
			CookieKeys.PASSWORD, StringPool.BLANK);

		if (Validator.isNotNull(domain)) {
			passwordCookie.setDomain(domain);
		}

		passwordCookie.setMaxAge(0);
		passwordCookie.setPath(StringPool.SLASH);
		CookieKeys.addCookie(request, response, companyIdCookie);
		CookieKeys.addCookie(request, response, idCookie);
		CookieKeys.addCookie(request, response, passwordCookie);

		/* CUSCALSOW4-134: START VASCO-HOOK LOGOUT EVENT LOGIC */
		session.removeAttribute("_RETURN_URL");
		session.removeAttribute("_USER_STEPPEDUP");
		/* CUSCALSOW4-134: END VASCO-HOOK LOGOUT EVENT LOGIC */

		try {
			session.invalidate();
		}
		catch (Exception ex) {
			_log.error(ex);
		}
	}

	private static final String _PORTAL_LOGOUT = "/portal/logout";

	private static final Log _log = LogFactoryUtil.getLog(
		CuscalSecurityFilter.class);

}