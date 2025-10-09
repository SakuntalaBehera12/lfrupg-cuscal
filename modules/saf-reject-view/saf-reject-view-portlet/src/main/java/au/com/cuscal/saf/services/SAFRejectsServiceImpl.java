//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.saf.services;

import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.webservices.SAFRejectDetailsRequestType;
import au.com.cuscal.framework.webservices.SAFRejectDetailsResponseType;
import au.com.cuscal.framework.webservices.SAFRejectSearchRequestType;
import au.com.cuscal.framework.webservices.SAFRejectSearchResponseType;
import au.com.cuscal.framework.webservices.SearchHeader;
import au.com.cuscal.framework.webservices.StandardHeader;
import au.com.cuscal.framework.webservices.client.EntityService;
import au.com.cuscal.framework.webservices.client.SAFService;
import au.com.cuscal.framework.webservices.liferay.LiferayClientUtil;
import au.com.cuscal.saf.rejects.common.Constants;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service(Constants.SAF_REJECTS_SERVICE)
public class SAFRejectsServiceImpl implements SAFRejectsService {

	public SAFRejectsServiceImpl() {
		this.logger = LoggerFactory.getLogger(SAFRejectsServiceImpl.class);
	}

	public StringBuilder addAdditionalAuditMessage(
		final StringBuilder sb, final String message, final String type) {

		if (StringUtils.equalsIgnoreCase(Constants.SYSTEM_EXCEPTION, type)) {
			sb.append("<error>");
			sb.append("<message>");
			sb.append(message);
			sb.append("</message>");
			sb.append("</error>");
		}
		else {
			sb.append("<" + type + ">");
			sb.append(message);
			sb.append("</" + type + ">");
		}

		return sb;
	}

	public StringBuilder endAuditMessageBuilder(final StringBuilder sb) {
		sb.append("</audit>");

		return sb;
	}

	@Override
	public SAFRejectSearchResponseType findSafRejectsList(
		final SearchHeader header, final PortletContext context) {

		final PortletRequest request = context.getRequest();
		final PortletResponse response = context.getResponse();
		StringBuilder message = new StringBuilder();
		final SAFRejectSearchRequestType webserviceRequest =
			new SAFRejectSearchRequestType();
		webserviceRequest.setHeader(header);
		SAFRejectSearchResponseType webserviceResponse = null;

		webserviceResponse = this.safService.findSafRejectList(
			webserviceRequest);
		message = this.startAuditMessageBuilder();
		message.append("</parameters>");
		message.append("<result>");
		message.append("<found>");

		if (webserviceResponse != null) {
			if (webserviceResponse.getHeader() != null) {
				final String statusCode = webserviceResponse.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					message.append(
						webserviceResponse.getResults(
						).size());
					message.append("</found>");
					message.append("</result>");
					message = this.endAuditMessageBuilder(message);
					SAFRejectsServiceImpl.audit.success(
						response, request, "PORTAL",
						"SAF/REJECT_QUEUE/SEARCH/SUMMARY", message.toString());
				}
				else if (StringUtils.equalsIgnoreCase(
							Constants.RECORD_NOT_FOUND, statusCode)) {

					message.append(
						webserviceResponse.getResults(
						).size());
					message.append("</found>");
					message.append("</result>");
					message = this.endAuditMessageBuilder(message);
					SAFRejectsServiceImpl.audit.success(
						response, request, "PORTAL",
						"SAF/REJECT_QUEUE/SEARCH/SUMMARY", message.toString());
				}
				else {
					message.append("0</found>");
					message = this.addAdditionalAuditMessage(
						message, statusCode, Constants.SYSTEM_EXCEPTION);
					message.append("</result>");
					message = this.endAuditMessageBuilder(message);
					SAFRejectsServiceImpl.audit.fail(
						response, request, "PORTAL",
						"SAF/REJECT_QUEUE/SEARCH/SUMMARY", message.toString());
				}
			}
			else {
				message.append("0</found>");
				message = this.addAdditionalAuditMessage(
					message, "WS response header is null",
					Constants.SYSTEM_EXCEPTION);
				message.append("</result>");
				message = this.endAuditMessageBuilder(message);
				SAFRejectsServiceImpl.audit.fail(
					response, request, "PORTAL",
					"SAF/REJECT_QUEUE/SEARCH/SUMMARY", message.toString());
			}
		}
		else {
			message.append("0</found>");
			message = this.addAdditionalAuditMessage(
				message, "WS returned null response.",
				Constants.SYSTEM_EXCEPTION);
			message.append("</result>");
			message = this.endAuditMessageBuilder(message);
			SAFRejectsServiceImpl.audit.fail(
				response, request, "PORTAL", "SAF/REJECT_QUEUE/SEARCH/SUMMARY",
				message.toString());
		}

		return webserviceResponse;
	}

	public EntityService getEntityService() {
		return this.entityService;
	}

	@Override
	public SAFRejectDetailsResponseType getSafRejectDetails(
		final SearchHeader header, final PortletContext context,
		final String settlementDate) {

		this.logger.debug("getSafRejectDetails - start");
		final PortletRequest request = context.getRequest();
		final PortletResponse response = context.getResponse();
		StringBuilder message = new StringBuilder();

		message = this.startAuditMessageBuilder();
		message = this.buildSafRejectDetailsAuditMessage(
			settlementDate, message);
		final SAFRejectDetailsRequestType webserviceRequest =
			new SAFRejectDetailsRequestType();
		webserviceRequest.setHeader((StandardHeader)header);
		webserviceRequest.setDate(settlementDate);
		SAFRejectDetailsResponseType webserviceResponse = null;

		webserviceResponse = this.safService.getSafRejectDetails(
			webserviceRequest);

		if (webserviceResponse != null) {
			if (webserviceResponse.getHeader() != null) {
				final String statusCode = webserviceResponse.getHeader(
				).getStatusCode();

				if (StringUtils.equalsIgnoreCase(
						Constants.SUCCESS, statusCode)) {

					message.append(
						webserviceResponse.getResults(
						).size());
					message.append("</found>");
					message.append("</result>");
					message = this.endAuditMessageBuilder(message);
					SAFRejectsServiceImpl.audit.success(
						response, request, "PORTAL", "SAF/REJECT_QUEUE/SEARCH",
						message.toString());
				}
				else if (StringUtils.equalsIgnoreCase(
							Constants.RECORD_NOT_FOUND, statusCode)) {

					message.append("0</found>");
					message.append("</result>");
					message = this.endAuditMessageBuilder(message);
					SAFRejectsServiceImpl.audit.fail(
						response, request, "PORTAL", "SAF/REJECT_QUEUE/SEARCH",
						message.toString());
				}
				else {
					message.append("0</found>");
					message = this.addAdditionalAuditMessage(
						message, statusCode, Constants.SYSTEM_EXCEPTION);
					message.append("</result>");
					message = this.endAuditMessageBuilder(message);
					SAFRejectsServiceImpl.audit.fail(
						response, request, "PORTAL", "SAF/REJECT_QUEUE/SEARCH",
						message.toString());
				}
			}
			else {
				message.append("0</found>");
				message = this.addAdditionalAuditMessage(
					message, "WS response header is null",
					Constants.SYSTEM_EXCEPTION);
				message.append("</result>");
				message = this.endAuditMessageBuilder(message);
				SAFRejectsServiceImpl.audit.fail(
					response, request, "PORTAL", "SAF/REJECT_QUEUE/SEARCH",
					message.toString());
			}
		}
		else {
			message.append("0</found>");
			message = this.addAdditionalAuditMessage(
				message, "WS returned null response.",
				Constants.SYSTEM_EXCEPTION);
			message.append("</result>");
			message = this.endAuditMessageBuilder(message);
			SAFRejectsServiceImpl.audit.fail(
				response, request, "PORTAL", "SAF/REJECT_QUEUE/SEARCH/SUMMARY",
				message.toString());
		}

		this.logger.debug("getSafRejectDetails - end");

		return webserviceResponse;
	}

	public SAFService getSafService() {
		return this.safService;
	}

	@Override
	public String orgShortName(final Long orgId) {
		this.logger.debug("orgShortName - start");
		String orgShortName = null;

		try {
			orgShortName = LiferayClientUtil.getOrgShortName(
				this.entityService, orgId);
		}
		catch (Exception e) {
			this.logger.error(
				"orgShortName - Failed to call EntityService to get orgShortName for " +
					orgId + ". Check WS logs.");
		}

		this.logger.debug("orgShortName - end");

		return orgShortName;
	}

	public StringBuilder startAuditMessageBuilder() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<audit>");
		sb.append("<parameters>");

		return sb;
	}

	private StringBuilder buildSafRejectDetailsAuditMessage(
		final String settlementDate, final StringBuilder sb) {

		sb.append("<settlementDate>");
		sb.append(settlementDate);
		sb.append("</settlementDate>");
		sb.append("</parameters>");
		sb.append("<result>");
		sb.append("<found>");

		return sb;
	}

	private static Auditor audit;

	static {
		SAFRejectsServiceImpl.audit = Auditor.getInstance();
	}

	private final EntityService entityService = CuscalServiceLocator.getService(
		EntityService.class.getName());
	private final SAFService safService = CuscalServiceLocator.getService(
		SAFService.class.getName());
	private final Logger logger;

}