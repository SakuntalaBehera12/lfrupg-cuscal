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

package com.tcs.cuscal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.util.PortalUtil;

import com.tcs.cuscal.configuration.AuthFilterConfiguration;

import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Tang Hieu Ha
 */
@Component(
	configurationPid = "com.tcs.cuscal.configuration.AuthFilterConfiguration",
	immediate = true,
	property = {
		"after-filter=Auto Login Filter", "dispatcher=FORWARD",
		"dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=AuthRedirectFilter", "url-pattern=/",
		"url-pattern=/home", "url-pattern=/web/cuscal",
		"url-pattern=/web/cuscal/", "url-pattern=/web/cuscal/home"
	},
	service = Filter.class
)
public class AuthRedirectFilter extends BaseFilter {

	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		_authFilterConfiguration = ConfigurableUtil.createConfigurable(
			AuthFilterConfiguration.class, properties);
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain chain)
		throws Exception {

		if (!_authFilterConfiguration.authFilterEnabled()) {
			if (getLog().isDebugEnabled()) {
				getLog().debug("AuthRedirectFilter is not enabled");
			}

			chain.doFilter(request, response);

			return;
		}

		boolean isLoggedIn = false;

		if (request != null) {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				isLoggedIn = true;
			}
		}

		String redirectURL = _authFilterConfiguration.loggedInHome();

		if (isLoggedIn && !redirectURL.isEmpty()) {
			request.getRequestDispatcher(
				redirectURL
			).forward(
				request, response
			);

			if (getLog().isDebugEnabled()) {
				getLog().debug("Logged in. Forwarding to " + redirectURL);
			}
		}
		else {
			chain.doFilter(request, response);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AuthRedirectFilter.class);

	private AuthFilterConfiguration _authFilterConfiguration;

}