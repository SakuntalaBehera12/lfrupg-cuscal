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

package au.com.cuscal.portal.julian.date.portlet;

import au.com.cuscal.portal.julian.date.constants.JulianConstants;
import au.com.cuscal.portal.julian.date.constants.JulianPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

/**
 * @author Vuong Nguyen
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.cuscal-julian-date",
		"com.liferay.portlet.icon=/icons/icon.png",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.header-portlet-javascript=/js/main.js",
		"com.liferay.portlet.css-class-wrapper=julianportlet-portlet",
		"com.liferay.portlet.instanceable=false",
		"javax.portlet.display-name=Julian Date",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=" + JulianConstants.VIEW_JULIAN_PORTLET,
		"javax.portlet.name=" + JulianPortletKeys.JULIAN_PORTLET,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class JulianPortlet extends MVCPortlet {
}