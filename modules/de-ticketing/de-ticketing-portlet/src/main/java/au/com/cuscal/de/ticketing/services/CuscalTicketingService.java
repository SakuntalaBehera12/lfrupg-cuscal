//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.de.ticketing.services;

import au.com.cuscal.de.ticketing.forms.AttributesInformation;
import au.com.cuscal.de.ticketing.forms.ServiceRequestForm;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;

import com.liferay.portal.kernel.model.User;

import java.util.List;

public interface CuscalTicketingService {

	String addServiceRequest(
			final ServiceRequestForm p0, final TktRequestHeader p1,
			final PortletContext p2)
		throws Exception;

	String getOrgShortName(final Long p0);

	String updateServiceRequest(
			final ServiceRequestForm p0, final TktRequestHeader p1,
			final PortletContext p2, final User p3)
		throws Exception;

	List<AttributesInformation> spxTransactionSearch(
			final ServiceRequestForm p0, final TktRequestHeader p1,
			final PortletContext p2)
		throws Exception;

	List<AttributesInformation> spxPaymentTransactionSearch(
			final ServiceRequestForm p0, final TktRequestHeader p1,
			final PortletContext p2)
		throws Exception;

	List<AttributesInformation> spxClaimTransactionSearch(
			final ServiceRequestForm p0, final TktRequestHeader p1,
			final PortletContext p2)
		throws Exception;

	AttributesInformation setupAttributesInformation(
		final TicketType p0, final boolean p1);

}