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

import com.liferay.portal.kernel.util.GetterUtil;

/**
 * @author Allen R. Ziegenfus
 */
public class Template {

	public Template(String templateKey, String content, String cacheable) {
		_templateKey = templateKey;
		_content = content;

		if (cacheable == null) {
			_cacheable = null;
		}
		else {
			_cacheable = GetterUtil.getBoolean(cacheable);
		}
	}

	public Boolean getCacheable() {
		return _cacheable;
	}

	public String getContent() {
		return _content;
	}

	public String getTemplateKey() {
		return _templateKey;
	}

	public String toString() {
		return _templateKey;
	}

	private final Boolean _cacheable;
	private final String _content;
	private final String _templateKey;

}