//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.saf.services;

import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.SAFRejectDetailsResponseType;
import au.com.cuscal.framework.webservices.SAFRejectSearchResponseType;
import au.com.cuscal.framework.webservices.SearchHeader;

public interface SAFRejectsService {

	SAFRejectSearchResponseType findSafRejectsList(
		final SearchHeader p0, final PortletContext p1);

	SAFRejectDetailsResponseType getSafRejectDetails(
		final SearchHeader p0, final PortletContext p1, final String p2);

	String orgShortName(final Long p0);

}