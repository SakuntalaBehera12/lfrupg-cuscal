/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package au.com.cuscal.termsofuse.model;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the TermsOfUse service. Represents a row in the &quot;CuscalToC_TermsOfUse&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see TermsOfUseModel
 * @generated
 */
@ImplementationClassName("au.com.cuscal.termsofuse.model.impl.TermsOfUseImpl")
@ProviderType
public interface TermsOfUse extends PersistedModel, TermsOfUseModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>au.com.cuscal.termsofuse.model.impl.TermsOfUseImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<TermsOfUse, Long> ENTRY_ID_ACCESSOR =
		new Accessor<TermsOfUse, Long>() {

			@Override
			public Long get(TermsOfUse termsOfUse) {
				return termsOfUse.getEntryId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<TermsOfUse> getTypeClass() {
				return TermsOfUse.class;
			}

		};

}