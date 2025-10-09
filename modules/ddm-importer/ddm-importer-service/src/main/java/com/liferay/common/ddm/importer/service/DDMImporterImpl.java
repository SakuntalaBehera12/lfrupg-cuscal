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
import com.liferay.common.ddm.importer.Structure;
import com.liferay.common.ddm.importer.Template;
import com.liferay.dynamic.data.mapping.constants.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.constants.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.io.DDMFormSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormSerializerSerializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormSerializerSerializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.dynamic.data.mapping.util.DDMUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringParser;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.display.template.PortletDisplayTemplate;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Joan H. Kim
 * @author Allen R. Ziegenfus
 * @author Ryan Schuhler
 * @author Ha Tang
 */
@Component(
	configurationPid = "com.liferay.common.ddm.importer.DDMImporterConfiguration",
	immediate = true, service = DDMImporter.class
)
public class DDMImporterImpl implements DDMImporter {

	public static final String STRUCTURE = "structure";

	public static final String TEMPLATE = "template";

	public void addStructures(List<Structure> structures)
		throws PortalException {

		if (_log.isDebugEnabled()) {
			_log.debug("Using defaultUserId " + _getAdminUserId());
			_log.debug("Using guestGroupId " + _getGuestGroupId());
		}

		for (Structure structure : structures) {
			try {
				String structureKey = structure.getStructureKey();
				String definition = structure.getContent();

				if (Validator.isNull(definition)) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"Definition is null for structure: " +
								structureKey);
					}

					continue;
				}

				long groupId = getGroupId(structure.getGroupKey());
				long classNameId = _portal.getClassNameId(
					JournalArticle.class.getName());
				String storageType = "json";

				DDMStructure ddmStructure =
					_ddmStructureLocalService.fetchStructure(
						groupId, classNameId, structureKey);

				DDMFormDeserializerDeserializeRequest.Builder builder =
					DDMFormDeserializerDeserializeRequest.Builder.newBuilder(
						definition);

				DDMFormDeserializerDeserializeResponse
					ddmFormDeserializerDeserializeResponse =
						_ddmFormDeserializer.deserialize(builder.build());

				DDMForm ddmForm =
					ddmFormDeserializerDeserializeResponse.getDDMForm();

				if (ddmStructure != null) {
					DDMForm existingDDMForm = ddmStructure.getDDMForm();

					if (ddmForm.hashCode() != existingDDMForm.hashCode()) {
						if (_log.isDebugEnabled()) {
							_log.debug("Updating structure: " + structureKey);
						}

						ddmStructure =
							_ddmStructureLocalService.updateStructure(
								_getAdminUserId(),
								ddmStructure.getStructureId(), ddmForm,
								ddmStructure.getDDMFormLayout(),
								new ServiceContext());
					}
				}
				else {
					Map<Locale, String> nameMap = new HashMap<>();

					nameMap.put(
						ddmForm.getDefaultLocale(),
						structure.getStructureKey());

					if (_log.isDebugEnabled()) {
						_log.debug("Adding structure: " + structureKey);
					}

					ddmStructure = _ddmStructureLocalService.addStructure(
						StringPool.BLANK,
						_getAdminUserId(), groupId,
						DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
						classNameId, structureKey, nameMap,
						Collections.emptyMap(), ddmForm,
						DDMUtil.getDefaultDDMFormLayout(ddmForm), storageType,
						DDMStructureConstants.TYPE_DEFAULT,
						new ServiceContext());
				}

				addTemplates(structure, ddmStructure);
			}
			catch (PortalException pe) {
				_log.error("Error deploying structure " + structure, pe);
			}
		}
	}

	public void addTemplate(
			Template template, String groupKey, long classNameId, long classPK,
			long resourceClassNameId)
		throws PortalException {

		try {
			long groupId = getGroupId(groupKey);
			String script = template.getContent();
			String templateKey = template.getTemplateKey();

			DDMTemplate ddmTemplate = _ddmTemplateLocalService.fetchTemplate(
				groupId, classNameId, templateKey);

			if (ddmTemplate != null) {
				boolean cacheable = ddmTemplate.getCacheable();

				if (template.getCacheable() != null) {
					cacheable = GetterUtil.getBoolean(template.getCacheable());
				}

				if (StringUtil.equals(ddmTemplate.getScript(), script) &&
					(ddmTemplate.getCacheable() == cacheable)) {

					return;
				}

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Updating template in group: " + groupKey + ": " +
							templateKey + " cacheable: " +
								Boolean.toString(cacheable));
				}

				_ddmTemplateLocalService.updateTemplate(
					_getAdminUserId(), ddmTemplate.getTemplateId(),
					ddmTemplate.getClassPK(), ddmTemplate.getNameMap(),
					ddmTemplate.getDescriptionMap(), ddmTemplate.getType(),
					ddmTemplate.getMode(), ddmTemplate.getLanguage(), script,
					cacheable, ddmTemplate.getSmallImage(),
					ddmTemplate.getSmallImageURL(), null, new ServiceContext());
			}
			else {
				Map<Locale, String> nameMap = new HashMap<>();

				nameMap.put(LocaleUtil.getDefault(), template.getTemplateKey());

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Adding template in group: " + groupKey + ": " +
							templateKey);
				}

				boolean cacheable = Boolean.TRUE;

				if (template.getCacheable() != null) {
					cacheable = GetterUtil.getBoolean(template.getCacheable());
				}

				_ddmTemplateLocalService.addTemplate(
					StringPool.BLANK,
					_getAdminUserId(), groupId, classNameId, classPK,
					resourceClassNameId, templateKey, nameMap,
					Collections.emptyMap(),
					DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY,
					StringPool.BLANK, TemplateConstants.LANG_TYPE_FTL, script,
					cacheable, Boolean.FALSE, StringPool.BLANK, null,
					new ServiceContext());
			}
		}
		catch (PortalException pe) {
			_log.error("Error deploying template " + template, pe);
		}
	}

	public void addTemplates(Structure structure, DDMStructure ddmStructure)
		throws PortalException {

		long classNameId = _portal.getClassNameId(DDMStructure.class.getName());
		long resourceClassNameId = _portal.getClassNameId(
			JournalArticle.class.getName());

		for (Template template : structure.getTemplates()) {
			addTemplate(
				template, structure.getGroupKey(), classNameId,
				ddmStructure.getStructureId(), resourceClassNameId);
		}
	}

	@Override
	public void deploy(Bundle bundle) {
		try {
			deployStructuresAndTemplates(bundle);

			deployApplicationDisplayTemplates(bundle);
		}
		catch (Exception e) {
			_log.error("Error deploying structures and templates", e);
		}
	}

	@Override
	public void doAutoDeploy() {
		if (_ddmImporterConfiguration.autoDeploy()) {
			deploy(_bundleContext.getBundle());
		}
	}

	@Override
	public void dumpToFilesystem(String directory) throws PortalException {
		FileUtil.deltree(directory);
		_log.info("Creating directory " + directory);
		FileUtil.mkdirs(directory);

		List<DDMStructure> ddmStructures =
			_ddmStructureLocalService.getDDMStructures(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (DDMStructure ddmStructure : ddmStructures) {
			if (!StringUtil.equalsIgnoreCase(
					ddmStructure.getClassName(),
					JournalArticle.class.getName())) {

				continue;
			}

			Group group = _groupLocalService.fetchGroup(
				ddmStructure.getGroupId());

			if (group == null) {
				_log.error(
					"Could not find group for " +
						ddmStructure.getNameCurrentValue() + " group " +
							ddmStructure.getGroupId());

				continue;
			}

			DDMFormSerializerSerializeRequest.Builder builder =
				DDMFormSerializerSerializeRequest.Builder.newBuilder(
					ddmStructure.getDDMForm());

			DDMFormSerializerSerializeResponse
				ddmFormSerializerSerializeResponse =
					_ddmFormSerializer.serialize(builder.build());

			String definition = ddmFormSerializerSerializeResponse.getContent();

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				definition);

			definition = jsonObject.toString(4);

			String structureFileName = FileUtil.encodeSafeFileName(
				ddmStructure.getNameCurrentValue());

			structureFileName = structureFileName.replace(
				CharPool.FORWARD_SLASH, CharPool.PERIOD);

			try {
				_log.debug("Writing structureFile " + structureFileName);

				String structurePath = getStructurePath(
					directory, group.getGroupKey(),
					ddmStructure.getStructureKey(), structureFileName);

				FileUtil.write(structurePath, definition);

				for (DDMTemplate ddmTemplate : ddmStructure.getTemplates()) {
					String templatePath = getTemplatePath(
						directory, group.getGroupKey(),
						ddmStructure.getStructureKey(),
						ddmTemplate.getTemplateKey());

					FileUtil.write(templatePath, ddmTemplate.getScript());
				}
			}
			catch (IOException ioe) {
				_log.error(
					"Error writing structure or templates for file " +
						structureFileName,
					ioe);
			}
		}

		dumpApplicationDisplayTemplates(directory);
	}

	public String getADTTemplatePath(
		String directory, String groupKey, String className,
		String templateKey) {

		StringBundler sb = new StringBundler(10);

		sb.append(directory);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(_ADT);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(groupKey);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(className);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(templateKey);
		sb.append(_DOT_FTL);

		return sb.toString();
	}

	@Override
	public String getStructurePath(
		String directory, String groupKey, String structureKey,
		String structureFileName) {

		StringBundler sb = new StringBundler(10);

		sb.append(directory);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(_DDM);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(groupKey);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(structureKey);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(structureFileName);
		sb.append(_DOT_JSON);

		return sb.toString();
	}

	@Override
	public String getTemplatePath(
		String directory, String groupKey, String structureKey,
		String templateKey) {

		StringBundler sb = new StringBundler(10);

		sb.append(directory);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(_DDM);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(groupKey);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(structureKey);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(templateKey);
		sb.append(_DOT_FTL);

		return sb.toString();
	}

	@Activate
	protected void activate(
		Map<String, Object> properties, BundleContext bundleContext) {

		_bundleContext = bundleContext;

		_ddmImporterConfiguration = ConfigurableUtil.createConfigurable(
			DDMImporterConfiguration.class, properties);
	}

	protected void deployApplicationDisplayTemplates(Bundle bundle)
		throws IOException, PortalException {

		Enumeration<URL> adtTemplateURLs = bundle.findEntries(
			"/adt", "*.ftl", true);

		if (adtTemplateURLs == null) {
			return;
		}

		long resourceClassNameId = _portal.getClassNameId(
			PortletDisplayTemplate.class.getName());

		while (adtTemplateURLs.hasMoreElements()) {
			URL adtTemplateURL = adtTemplateURLs.nextElement();

			Map<String, String> adtTemplateParams = getADTResourceInfo(
				TEMPLATE, adtTemplateURL);

			getProperties(adtTemplateURL, adtTemplateParams);

			String templateKey = FileUtil.stripExtension(
				adtTemplateParams.get("filename"));

			Template template = new Template(
				templateKey, adtTemplateParams.get("content"),
				adtTemplateParams.get(_CACHEABLE));

			long classNameId = _portal.getClassNameId(
				adtTemplateParams.get("className"));

			addTemplate(
				template, adtTemplateParams.get("groupKey"), classNameId, 0,
				resourceClassNameId);
		}
	}

	protected void deployStructuresAndTemplates(Bundle bundle)
		throws IOException, PortalException {

		Enumeration<URL> structureURLs = bundle.findEntries(
			"/ddm", "*.json", true);

		if (structureURLs == null) {
			return;
		}

		List<Structure> structures = new ArrayList<>();

		while (structureURLs.hasMoreElements()) {
			URL structureURL = structureURLs.nextElement();

			Map<String, String> structureParams = getResourceInfo(
				STRUCTURE, structureURL);

			structures.add(getStructure(structureParams));
		}

		Stream<Structure> structureStream = structures.stream();

		Map<Tuple, Structure> groupedStructures = structureStream.collect(
			Collectors.toMap(
				structure -> new Tuple(
					structure.getGroupKey(), structure.getStructureKey()),
				structure -> structure));

		Enumeration<URL> templateURLs = bundle.findEntries(
			"/ddm", "*.ftl", true);

		while (templateURLs.hasMoreElements()) {
			URL templateURL = templateURLs.nextElement();

			Map<String, String> templateParams = getResourceInfo(
				TEMPLATE, templateURL);

			getProperties(templateURL, templateParams);

			Tuple tuple = new Tuple(
				templateParams.get("groupKey"),
				templateParams.get("structureKey"));

			Structure templateStructures = groupedStructures.get(tuple);

			// TODO throw exception if no match?

			if (templateStructures != null) {
				templateStructures.addTemplate(
					new Template(
						FileUtil.stripExtension(templateParams.get("filename")),
						templateParams.get("content"),
						templateParams.get(_CACHEABLE)));
			}
		}

		addStructures(structures);
	}

	protected void dumpApplicationDisplayTemplates(String directory) {
		List<DDMTemplate> ddmTemplates = _ddmTemplateLocalService.getTemplates(
			0);

		for (DDMTemplate ddmTemplate : ddmTemplates) {
			try {
				Group group = _groupLocalService.fetchGroup(
					ddmTemplate.getGroupId());

				if (group == null) {
					_log.error(
						"Could not find group for " +
							ddmTemplate.getNameCurrentValue() + " group " +
								ddmTemplate.getGroupId());

					continue;
				}

				String templatePath = getADTTemplatePath(
					directory, group.getGroupKey(), ddmTemplate.getClassName(),
					ddmTemplate.getTemplateKey());

				FileUtil.write(templatePath, ddmTemplate.getScript());
			}
			catch (IOException ioe) {
				_log.error(
					"Error writing template for file " +
						ddmTemplate.getNameCurrentValue(),
					ioe);
			}
		}
	}

	protected Map<String, String> getADTResourceInfo(String type, URL path)
		throws IOException {

		InputStream inputStream = new BufferedInputStream(path.openStream());

		String content = StringUtil.read(inputStream);

		StringParser stringParser = StringParser.create(
			"{fileEntry:[^/]+}//{directory:[^/]+}/{adt:[^/]+}/{groupKey:[^" +
				"/]+}/{className:[^/]+}/{filename:[^$]+}");

		Map<String, String> params = new HashMap<>();

		stringParser.parse(path.toString(), params);

		params.put("content", content);
		params.put("type", type);

		return params;
	}

	/**
	 *
	 * @param groupKey
	 * 			Could be the groupKey or friendlyURL
	 * @return
	 * 			groupId of the group, or groupId of Guest site if not found.
	 * @throws PortalException
	 */
	protected long getGroupId(String groupKey) throws PortalException {
		Group group = _groupLocalService.fetchGroup(
			_getDefaultCompanyId(), groupKey);

		if (group == null) {
			group = _groupLocalService.fetchFriendlyURLGroup(
				_getDefaultCompanyId(),
				StringPool.SLASH + StringUtil.lowerCase(groupKey));
		}

		if (group == null) {
			return _getGuestGroupId();
		}

		return group.getGroupId();
	}

	protected void getProperties(URL path, Map<String, String> params) {
		try {
			Properties properties = new Properties();

			String propertiesPath = path.toString() + _DOT_PROPERTIES;

			URL propertiesURL = new URL(propertiesPath);

			properties.load(propertiesURL.openStream());

			String cacheable = properties.getProperty(_CACHEABLE);

			if (cacheable != null) {
				params.put(_CACHEABLE, cacheable);
			}
		}
		catch (IOException ioe) {
		}
	}

	protected Map<String, String> getResourceInfo(String type, URL path)
		throws IOException {

		InputStream inputStream = new BufferedInputStream(path.openStream());

		String content = StringUtil.read(inputStream);

		//  bundleentry://64.fwk1259683403/ddm/10155/12483/lego.json

		StringParser stringParser = StringParser.create(
			"{fileEntry:[^/]+}//{directory:[^/]+}/{ddm:[^/]+}/{groupKey:[^" +
				"/]+}/{structureKey:[^/]+}/{filename:[^$]+}");

		Map<String, String> params = new HashMap<>();

		stringParser.parse(path.toString(), params);

		params.put("content", content);
		params.put("type", type);

		return params;
	}

	protected Structure getStructure(Map<String, String> params) {
		return new Structure(
			params.get("groupKey"), params.get("structureKey"),
			params.get("content"));
	}

	@Reference(unbind = "-")
	protected void setDDMStructureLocalService(
		DDMStructureLocalService ddmStructureLocalService) {

		_ddmStructureLocalService = ddmStructureLocalService;
	}

	private long _getAdminUserId() throws PortalException {
		if (_defaultAdminUserId > 0) {
			return _defaultAdminUserId;
		}

		_defaultAdminUserId = _userLocalService.getDefaultUser(
			_getDefaultCompanyId()
		).getUserId();

		return _defaultAdminUserId;
	}

	private long _getDefaultCompanyId() {
		if (_defaultCompanyId > 0) {
			return _defaultCompanyId;
		}

		try {
			long _defaultCompanyId = _portal.getDefaultCompanyId();

			return _defaultCompanyId;
		}
		catch (Exception e) {
			return _ddmImporterConfiguration.companyId();
		}
	}

	private long _getGuestGroupId() throws PortalException {
		if (_guestGroupId > 0) {
			return _guestGroupId;
		}

		_guestGroupId = _groupLocalService.getGroup(
			_getDefaultCompanyId(), _GUEST
		).getGroupId();

		return _guestGroupId;
	}

	private static final String _ADT = "adt";

	private static final String _CACHEABLE = "cacheable";

	private static final String _DDM = "ddm";

	private static final String _DOT_FTL = ".ftl";

	private static final String _DOT_JSON = ".json";

	private static final String _DOT_PROPERTIES = ".properties";

	private static final String _GUEST = "Guest";

	private static final Log _log = LogFactoryUtil.getLog(
		DDMImporterImpl.class);

	private static volatile long _defaultAdminUserId = 0;
	private static volatile long _defaultCompanyId = 0;
	private static volatile long _guestGroupId = 0;

	private BackgroundTaskExecutor _backgroundTaskExecutor;
	private BundleContext _bundleContext;

	@Reference
	private DDMFormDeserializer _ddmFormDeserializer;

	@Reference
	private DDMFormSerializer _ddmFormSerializer;

	private volatile DDMImporterConfiguration _ddmImporterConfiguration;
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private DDMTemplateLocalService _ddmTemplateLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserLocalService _userLocalService;

}