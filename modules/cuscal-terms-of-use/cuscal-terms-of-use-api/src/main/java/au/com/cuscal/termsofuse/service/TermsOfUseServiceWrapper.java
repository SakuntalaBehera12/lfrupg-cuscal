/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package au.com.cuscal.termsofuse.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link TermsOfUseService}.
 *
 * @author Brian Wing Shun Chan
 * @see TermsOfUseService
 * @generated
 */
public class TermsOfUseServiceWrapper
	implements ServiceWrapper<TermsOfUseService>, TermsOfUseService {

	public TermsOfUseServiceWrapper() {
		this(null);
	}

	public TermsOfUseServiceWrapper(TermsOfUseService termsOfUseService) {
		_termsOfUseService = termsOfUseService;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _termsOfUseService.getOSGiServiceIdentifier();
	}

	@Override
	public TermsOfUseService getWrappedService() {
		return _termsOfUseService;
	}

	@Override
	public void setWrappedService(TermsOfUseService termsOfUseService) {
		_termsOfUseService = termsOfUseService;
	}

	private TermsOfUseService _termsOfUseService;

}