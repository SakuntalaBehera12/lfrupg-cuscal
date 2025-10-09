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

package com.liferay.portlet.users.admin.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.users.admin.constants.UsersAdminPortletKeys;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

/**
 * Path to Liferay's original source code:
 * https://github.com/liferay/liferay-portal-ee/blob/7.3.x/modules/apps/users-admin/users-admin-web/src/main/java/com/liferay/users/admin/web/internal/portlet/action/EditUserMVCRenderCommand.java
 *
 * Based on https://github.com/liferay/lfrgs-cuscal-upgrade-legacy/blob/source-checkin/lry-portal-decompile/ext-cuscal-liferay-ext-impl/com/liferay/portlet/enterpriseadmin/action/CuscalEditUserAction.java#L171
 *
 * Custom action for editing a user for the user management portlet
 *
 * @author Peter Huynh
 * @author Pei-Jung Lan
 * @author Vichai Vun
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + UsersAdminPortletKeys.MY_ACCOUNT,
		"javax.portlet.name=" + UsersAdminPortletKeys.MY_ORGANIZATIONS,
		"javax.portlet.name=" + UsersAdminPortletKeys.USERS_ADMIN,
		"mvc.command.name=/users_admin/edit_user", "service.ranking:Integer=100"
	},
	service = MVCRenderCommand.class
)
public class CuscalEditUserMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		return "/edit_user.jsp";
	}

}