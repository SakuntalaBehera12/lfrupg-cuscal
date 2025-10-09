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

package au.com.cuscal.portal.action.hook;

import au.com.cuscal.termsofuse.service.TermsOfUseLocalService;

import com.liferay.journal.configuration.JournalServiceConfiguration;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;
import com.liferay.portal.kernel.settings.FallbackKeysSettingsUtil;
import com.liferay.portal.kernel.settings.Settings;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Vichai Vun
 */
@Component(
	immediate = true, property = "path=/portal/update_terms_of_use",
	service = StrutsAction.class
)
public class UpdateTermsOfUseAction implements StrutsAction {

	@Override
	public String execute(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		AuthTokenUtil.checkCSRFToken(
			httpServletRequest, UpdateTermsOfUseAction.class.getName());
		User user = PortalUtil.getUser(httpServletRequest);

		LOG.debug(
			String.format(
				"method=execute screenName=%s", user.getScreenName()));

		UserServiceUtil.updateAgreedToTermsOfUse(user.getUserId(), true);

		long companyId = PortalUtil.getCompanyId(httpServletRequest);

		updateUserTermsOfUse(
			PortalUtil.getScopeGroupId(httpServletRequest), companyId, user,
			getTermsOfUseVersion(companyId), new Date());

		return COMMON_REFERER_JSP;
	}

	private double getTermsOfUseVersion(long companyId) throws Exception {
		Settings settings = FallbackKeysSettingsUtil.getSettings(
			new CompanyServiceSettingsLocator(
				companyId, JournalServiceConfiguration.class.getName()));

		JournalServiceConfiguration journalServiceConfiguration =
			_configurationProvider.getCompanyConfiguration(
				JournalServiceConfiguration.class, companyId);

		long groupId = GetterUtil.getLong(
			settings.getValue(
				_TERMS_OF_USE_JOURNAL_ARTICLE_GROUP_ID_CONFIGURATION_PROPERTY,
				String.valueOf(
					journalServiceConfiguration.termsOfUseJournalArticleGroupId())));

		String articleId = settings.getValue(
			_TERMS_OF_USE_JOURNAL_ARTICLE_ID_CONFIGURATION_PROPERTY,
			String.valueOf(journalServiceConfiguration.termsOfUseJournalArticleId()));

		double latestVersion = 0;

		try {
			latestVersion = _journalArticleLocalService.getLatestVersion(
				groupId, articleId);
		}
		catch (PortalException e) {
			LOG.error(String.format("method=getTermsOfUseVersion error=%s", e));
		}

		return latestVersion;
	}

	private void updateUserTermsOfUse(
		long groupId, long companyId, User user, double version,
		Date acceptedDate) {

		_termsOfUseLocalService.acceptTermsOfUse(
			groupId, companyId, user.getUserId(), user.getScreenName(), version,
			acceptedDate);
		LOG.debug("method=updateUserTermsOfUse message=updated successfully");
	}

	private static final String COMMON_REFERER_JSP = "/common/referer_jsp.jsp";

	private static final Log LOG = LogFactoryUtil.getLog(
		UpdateTermsOfUseAction.class);

	private static final String
		_TERMS_OF_USE_JOURNAL_ARTICLE_GROUP_ID_CONFIGURATION_PROPERTY =
			"terms.of.use.journal.article.group.id";

	private static final String
		_TERMS_OF_USE_JOURNAL_ARTICLE_ID_CONFIGURATION_PROPERTY =
			"terms.of.use.journal.article.id";

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private TermsOfUseLocalService _termsOfUseLocalService;

}