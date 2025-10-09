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

package com.liferay.common.ddm.importer.service;

import com.liferay.common.ddm.importer.DDMImporter;
import com.liferay.common.ddm.importer.DDMImporterConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.Map;

import org.apache.felix.service.command.Descriptor;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Allen R. Ziegenfus
 */
@Component(
	configurationPid = "com.liferay.osb.www.ddm.importer.DDMImporterConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	property = {
		"osgi.command.function=deploy",
		"osgi.command.function=dumpToFilesystem", "osgi.command.scope=ddm"
	},
	service = DDMImporterOSGICommands.class
)
public class DDMImporterOSGICommands {

	@Descriptor("Deploy structures and templates into database")
	public void deploy() throws PortalException {
		_ddmImporter.deploy(_bundleContext.getBundle());
	}

	@Descriptor("Dump structures and templates from database to filesystem")
	public void dumpToFilesystem() throws PortalException {
		_ddmImporter.dumpToFilesystem(
			_ddmImporterConfiguration.dumpDirectory());
	}

	@Activate
	protected void activate(
		Map<String, Object> properties, BundleContext bundleContext) {

		_ddmImporterConfiguration = ConfigurableUtil.createConfigurable(
			DDMImporterConfiguration.class, properties);

		_bundleContext = bundleContext;
	}

	private BundleContext _bundleContext;

	@Reference
	private DDMImporter _ddmImporter;

	private volatile DDMImporterConfiguration _ddmImporterConfiguration;

}