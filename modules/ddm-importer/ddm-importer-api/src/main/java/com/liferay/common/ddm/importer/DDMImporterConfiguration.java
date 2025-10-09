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

package com.liferay.common.ddm.importer;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Allen Ziegenfus
 * @author Ha Tang
 */
@ExtendedObjectClassDefinition(category = "common-ddm-importer")
@Meta.OCD(id = "com.liferay.common.ddm.importer.DDMImporterConfiguration")
public interface DDMImporterConfiguration {

	@Meta.AD(deflt = "true", required = false)
	public boolean autoDeploy();

	@Meta.AD(deflt = "/tmp/liferay/dump", required = false)
	public String dumpDirectory();

	@Meta.AD(deflt = "10131", required = false)
	public long companyId();

}