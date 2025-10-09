package com.tcs.cuscal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Ha Tang
 */
@ExtendedObjectClassDefinition(
	category = "cuscal-filter",
	scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(
	id = "com.tcs.cuscal.configuration.AuthFilterConfiguration",
	localization = "content/Language", name = "cuscal-auth-filter-configuration"
)
public interface AuthFilterConfiguration {

	@Meta.AD(deflt = "false", name = "auth-filter-enabled", required = false)
	public boolean authFilterEnabled();

	@Meta.AD(
		deflt = "/web/cuscal/LoggedInhome", name = "logged-in-home",
		required = false
	)
	public String loggedInHome();

}