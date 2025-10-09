/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package au.com.cuscal.termsofuse.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;CuscalToC_TermsOfUse&quot; database table.
 *
 * @author Brian Wing Shun Chan
 * @see TermsOfUse
 * @generated
 */
public class TermsOfUseTable extends BaseTable<TermsOfUseTable> {

	public static final TermsOfUseTable INSTANCE = new TermsOfUseTable();

	public final Column<TermsOfUseTable, Long> entryId = createColumn(
		"entryId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<TermsOfUseTable, Long> groupId = createColumn(
		"groupId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<TermsOfUseTable, Long> companyId = createColumn(
		"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<TermsOfUseTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<TermsOfUseTable, String> screenName = createColumn(
		"screenName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<TermsOfUseTable, Double> version = createColumn(
		"version", Double.class, Types.DOUBLE, Column.FLAG_DEFAULT);
	public final Column<TermsOfUseTable, Date> acceptedDate = createColumn(
		"acceptedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<TermsOfUseTable, Date> createDate = createColumn(
		"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<TermsOfUseTable, Date> modifiedDate = createColumn(
		"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);

	private TermsOfUseTable() {
		super("CuscalToC_TermsOfUse", TermsOfUseTable::new);
	}

}