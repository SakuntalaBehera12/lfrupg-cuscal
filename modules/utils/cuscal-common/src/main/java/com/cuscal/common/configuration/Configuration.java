//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.configuration;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;

import java.io.IOException;

import java.util.HashMap;
import java.util.PropertyResourceBundle;

public class Configuration {

	public static HashMap configurations;

	static {
		Configuration.configurations = new HashMap();
	}

	public String getConfigItem(final String key) {
		return this.properties.getString(key);
	}

	protected static Configuration getInstance(
		final String configurationFileName, final Class childClassType) {

		if (configurationFileName == null) {
			return null;
		}

		Configuration config = (Configuration)Configuration.configurations.get(
			configurationFileName);

		if (config == null) {
			try {
				config = (Configuration)childClassType.newInstance();
				config.setConfigurationFileName(configurationFileName);
				config.init();
			}
			catch (Throwable t) {
				t.printStackTrace();

				return null;
			}

			Configuration.configurations.put(configurationFileName, config);
		}

		return config;
	}

	private void init() {
		try {
			this.properties = new PropertyResourceBundle(
				CuscalSharedPropsUtil.getResourceStream(
					getClass().getClassLoader(), this.configurationFileName));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setConfigurationFileName(final String fileName) {
		this.configurationFileName = fileName;
	}

	private String configurationFileName;
	private PropertyResourceBundle properties;

}