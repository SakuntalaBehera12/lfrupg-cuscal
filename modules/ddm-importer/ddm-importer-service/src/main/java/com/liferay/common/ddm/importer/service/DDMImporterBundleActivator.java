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
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Allen R. Ziegenfus
 */
public class DDMImporterBundleActivator implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		_serviceTracker = new ServiceTracker<DDMImporter, DDMImporter>(
			bundleContext, DDMImporter.class, null) {

			@Override
			public DDMImporter addingService(
				ServiceReference<DDMImporter> serviceReference) {

				DDMImporter ddmImporter = bundleContext.getService(
					serviceReference);

				try {
					if (!_deployed) {
						_deployed = true;

						_log.info("Invoking DDMImporter auto deploy");

						ddmImporter.doAutoDeploy();
					}
				}
				catch (PortalException pe) {
					_log.error("Error deploying templates", pe);
				}

				return ddmImporter;
			}

		};

		_serviceTracker.open();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		_serviceTracker.close();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMImporterBundleActivator.class);

	private boolean _deployed = false;
	private ServiceTracker<DDMImporter, DDMImporter> _serviceTracker;

}