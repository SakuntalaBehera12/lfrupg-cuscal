/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.cuscal.common.constants;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Vuong Nguyen
 */
public enum AttributeField {

	BSB("cuscalbsb"), GIVEN_NAME("givenname"), SUR_NAME("sn"),
	COMMON_NAME("cn"), UID("uid"), MAIL("mail"),
	TELEPHONE_NUMBER("telephonenumber"), FAX_NUMBER("facsimiletelephonenumber"),
	ORGANISATIONAL_UNIT("o"), UNIQUE_MEMBER("uniqueMember");

	public static String[] getChequeViewAttributeNames() {
		Stream<AttributeField> fieldStream = Arrays.stream(
			new AttributeField[] {
				BSB, GIVEN_NAME, SUR_NAME, COMMON_NAME, UID, MAIL,
				TELEPHONE_NUMBER, ORGANISATIONAL_UNIT
			});

		return fieldStream.map(
			AttributeField::getKey
		).toArray(
			String[]::new
		);
	}

	public static String[] getLdapUserAttributeNames() {
		Stream<AttributeField> fieldStream = Arrays.stream(
			new AttributeField[] {
				BSB, GIVEN_NAME, SUR_NAME, COMMON_NAME, UID, MAIL,
				TELEPHONE_NUMBER, FAX_NUMBER, ORGANISATIONAL_UNIT
			});

		return fieldStream.map(
			AttributeField::getKey
		).toArray(
			String[]::new
		);
	}

	public String getKey() {
		return _key;
	}

	private AttributeField(String key) {
		_key = key;
	}

	private String _key;

}