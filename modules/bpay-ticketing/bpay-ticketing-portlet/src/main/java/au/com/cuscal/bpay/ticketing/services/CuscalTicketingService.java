package au.com.cuscal.bpay.ticketing.services;

import au.com.cuscal.bpay.ticketing.forms.AttributesInformation;
import au.com.cuscal.bpay.ticketing.forms.ServiceRequestForm;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.selfservice.TicketType;
import au.com.cuscal.framework.webservices.selfservice.TktRequestHeader;

import com.liferay.portal.kernel.model.User;

/**
 * Local service interface for the BPay
 *
 *
 */
public interface CuscalTicketingService {

	public String addServiceRequest(
			ServiceRequestForm form, TktRequestHeader header,
			PortletContext portletContext)
		throws Exception;

	public String getOrgShortName(Long liferayOrgId);

	public AttributesInformation setupAttributesInformation(TicketType ticket);

	public String updateServiceRequest(
			ServiceRequestForm form, TktRequestHeader header,
			PortletContext portletContext, User user)
		throws Exception;

}