//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.saf.rejects.controller;

import au.com.cuscal.common.framework.memory.PerformanceMonitorManager;
import au.com.cuscal.framework.audit.context.PortletContext;
import au.com.cuscal.framework.webservices.SAFRejectDailyCount;
import au.com.cuscal.framework.webservices.SAFRejectDetailsEntry;
import au.com.cuscal.framework.webservices.SAFRejectDetailsResponseType;
import au.com.cuscal.framework.webservices.SAFRejectSearchResponseType;
import au.com.cuscal.framework.webservices.SearchHeader;
import au.com.cuscal.saf.rejects.common.Constants;
import au.com.cuscal.saf.rejects.domain.SAFRejectsDetailDecorator;
import au.com.cuscal.saf.rejects.domain.SAFRejectsListDecorator;
import au.com.cuscal.saf.services.SAFRejectsService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;
import com.liferay.portletmvc4spring.bind.annotation.ResourceMapping;

import java.io.BufferedWriter;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(Constants.SAF_REJECTS_CONTROLLER)
@RequestMapping({"VIEW"})
public class SAFRejectsController {

	public SAFRejectsController() {
		this.perfManager = PerformanceMonitorManager.getInstance();
	}

	@RenderMapping
	public String defaultView(
		final RenderRequest request, final RenderResponse response) {

		SAFRejectsController.logger.debug("defaultView - Start");
		final SearchHeader header = this.createSearchHeader(request);

		if (header == null) {
			request.setAttribute(Constants.ERROR_TYPE, Constants.LOGGED_OUT);
			SAFRejectsController.logger.error(
				"defaultView - user has logged out");

			return Constants.ERROR_PAGE;
		}

		final Organization userOrg = this.userOrgFromRequest(request);

		if (userOrg == null) {
			request.setAttribute(
				Constants.ERROR_TYPE, Constants.INVALID_CONFIG);
			SAFRejectsController.logger.error(
				"defaultView - user " + header.getUserId() +
					" has 0 or more than 1 organisations assigned to them");

			return Constants.ERROR_PAGE;
		}

		if (StringUtils.isBlank(header.getUserOrgId())) {
			request.setAttribute(
				Constants.ERROR_TYPE, Constants.SYSTEM_EXCEPTION);
			SAFRejectsController.logger.error(
				"defaultView - orgShortName from EntityService: " +
					header.getUserOrgId() +
						". Failed to call EntityService. Check WS logs.");

			return Constants.ERROR_PAGE;
		}

		final SAFRejectSearchResponseType webserviceResponse =
			this.safService.findSafRejectsList(
				header, PortletContext.newContext(response, request));

		if (webserviceResponse == null) {
			SAFRejectsController.logger.error(
				"defaultView - WS response is null. WS is uncontactable.");
			request.setAttribute(
				Constants.ERROR_TYPE, Constants.SYSTEM_EXCEPTION);

			return Constants.ERROR_PAGE;
		}

		if (webserviceResponse.getHeader() == null) {
			SAFRejectsController.logger.error(
				"defaultView - WS response header is null.");
			request.setAttribute(
				Constants.ERROR_TYPE, Constants.SYSTEM_EXCEPTION);

			return Constants.ERROR_PAGE;
		}

		final String statusCode = webserviceResponse.getHeader(
		).getStatusCode();

		if (StringUtils.equalsIgnoreCase(Constants.SYSTEM_ERROR, statusCode)) {
			SAFRejectsController.logger.error(
				"defaultView - WS returned SYSTEM_ERROR");
			request.setAttribute(
				Constants.ERROR_TYPE, Constants.SYSTEM_EXCEPTION);

			return Constants.ERROR_PAGE;
		}

		if (StringUtils.equalsIgnoreCase(
				Constants.VALIDATION_FAILURE, statusCode)) {

			SAFRejectsController.logger.error(
				"defaultView - WS returned VALIDATION_FAILURE.");
			request.setAttribute(
				Constants.ERROR_TYPE, Constants.SYSTEM_EXCEPTION);

			return Constants.ERROR_PAGE;
		}

		final List<SAFRejectDailyCount> rejectList =
			webserviceResponse.getResults();
		final List<SAFRejectsListDecorator> decoratedList = new ArrayList<>();

		for (final SAFRejectDailyCount entry : rejectList) {
			final SAFRejectsListDecorator decorated =
				new SAFRejectsListDecorator();

			try {
				final Date date = new SimpleDateFormat(
					Constants.DATE_FORMAT_YYYYMMDD
				).parse(
					entry.getDate()
				);
				decorated.setDate(date);
			}
			catch (ParseException e) {
				SAFRejectsController.logger.error(
					"defaultView - Could not format date. " + e.getMessage(),
					e);
			}

			decorated.setCount(entry.getCount());
			decorated.setSettlementDate(entry.getDate());
			decoratedList.add(decorated);
		}

		final int start = 0;
		final int end = decoratedList.size();
		final Double oneThird = Math.ceil(end / 3.0f);
		final Double twoThird = Math.ceil(2.0 * oneThird);
		final List<SAFRejectsListDecorator> decoratedList2 =
			decoratedList.subList(start, oneThird.intValue());
		final List<SAFRejectsListDecorator> decoratedList3 =
			decoratedList.subList(oneThird.intValue(), twoThird.intValue());
		final List<SAFRejectsListDecorator> decoratedList4 =
			decoratedList.subList(twoThird.intValue(), end);
		request.setAttribute("decoratedList1", (Object)decoratedList2);
		request.setAttribute("decoratedList2", (Object)decoratedList3);
		request.setAttribute("decoratedList3", (Object)decoratedList4);

		return Constants.HOME_PAGE;
	}

	@ResourceMapping(Constants.DOWNLOAD_REPORT)
	public void downloadReport(
		final ResourceRequest request, final ResourceResponse response) {

		this.perfManager.start();
		SAFRejectsController.logger.debug("downloadReport - start");
		final SearchHeader header = this.createSearchHeader(request);
		final String settlementDate = ParamUtil.getString(request, "sd");

		try {
			final SimpleDateFormat formatToDate = new SimpleDateFormat(
				Constants.DATE_FORMAT_YYYYMMDD);
			final Date date = formatToDate.parse(settlementDate);
			final SimpleDateFormat formatFromDate = new SimpleDateFormat(
				Constants.DATE_FORMAT_YYYYMMDD);
			final String stringDate = formatFromDate.format(date);

			if (StringUtils.equalsIgnoreCase(stringDate, settlementDate)) {
				this.perfManager.start("downloadReport - WebService Call");
				final SAFRejectDetailsResponseType webserviceResponse =
					this.safService.getSafRejectDetails(
						header, PortletContext.newContext(response, request),
						stringDate);
				this.perfManager.stop("downloadReport - WebService Call");

				if ((webserviceResponse != null) &&
					(webserviceResponse.getHeader() != null)) {

					final String statusCode = webserviceResponse.getHeader(
					).getStatusCode();

					try {
						if (StringUtils.equalsIgnoreCase(
								Constants.SUCCESS, statusCode) ||
							StringUtils.equalsIgnoreCase(
								Constants.RECORD_NOT_FOUND, statusCode)) {

							if (StringUtils.equalsIgnoreCase(
									Constants.RECORD_NOT_FOUND, statusCode)) {

								SAFRejectsController.logger.warn(
									"downloadReport - Encountered invalid date for the report.");
							}

							this.perfManager.start(
								"downloadReport - generateSAFDetailsCsv(response, settlementDate, webserviceResponse)");
							this.generateSAFDetailsCsv(
								response, settlementDate, webserviceResponse);
							this.perfManager.stop(
								"downloadReport - generateSAFDetailsCsv(response, settlementDate, webserviceResponse)");
						}
						else {
							SAFRejectsController.logger.error(
								"downloadReport - Could not export the list.");
							this.perfManager.start(
								"downloadReport - generateEmptyCsvFileWhenError(response);");
							this.generateEmptyCsvFileWhenError(response);
							this.perfManager.stop(
								"downloadReport - generateEmptyCsvFileWhenError(response);");
						}
					}
					catch (IOException e) {
						SAFRejectsController.logger.error(
							"downloadReport - Could not write to file" +
								e.getMessage(),
							e);
					}
				}
			}
			else {
				try {
					this.generateEmptyCsvFileWhenError(response);
				}
				catch (IOException e2) {
					SAFRejectsController.logger.error(
						"downloadReport - Could not write to file" +
							e2.getMessage(),
						e2);
				}
			}
		}
		catch (ParseException e3) {
			SAFRejectsController.logger.warn(
				"downloadReport - Invalid date entered. " + e3.getMessage(),
				e3);

			try {
				this.generateEmptyCsvFileWhenError(response);
			}
			catch (IOException e4) {
				SAFRejectsController.logger.error(
					"downloadReport - Could not write to file" +
						e4.getMessage(),
					e4);
			}
		}

		SAFRejectsController.logger.debug("downloadReport - end");
		this.perfManager.stopAll();
		this.perfManager.log();
	}

	public String getOrgFullNameOverride() {
		return this.orgFullNameOverride;
	}

	public Long getOrgIdOverride() {
		return this.orgIdOverride;
	}

	public String getOrgShortNameOverride() {
		return this.orgShortNameOverride;
	}

	public void setOrgFullNameOverride(final String orgFullNameOverride) {
		this.orgFullNameOverride = orgFullNameOverride;
	}

	public void setOrgIdOverride(final Long orgIdOverride) {
		this.orgIdOverride = orgIdOverride;
	}

	public void setOrgShortNameOverride(final String orgShortNameOverride) {
		this.orgShortNameOverride = orgShortNameOverride;
	}

	private SearchHeader createSearchHeader(final PortletRequest request) {
		SAFRejectsController.logger.debug("createSearchHeader - Start");
		SearchHeader header = new SearchHeader();
		final String username = this.usernameFromRequest(request);

		if (StringUtils.isNotBlank(username)) {
			header.setOrigin("PORTAL");
			header.setUserId(username);
			final Organization userOrg = this.userOrgFromRequest(request);

			if (userOrg != null) {
				String orgShortName = null;

				if (this.orgIdOverride != null) {
					orgShortName = this.safService.orgShortName(
						this.orgIdOverride);
				}
				else {
					orgShortName = this.safService.orgShortName(
						userOrg.getOrganizationId());
				}

				String orgFullName = userOrg.getName();

				if (StringUtils.isNotBlank(this.orgFullNameOverride)) {
					orgFullName = this.orgFullNameOverride;
				}

				header.setUserOrgName(orgFullName);
				header.setUserOrgId(orgShortName);
			}
		}
		else {
			header = null;
		}

		SAFRejectsController.logger.debug("createSearchHeader - end");

		return header;
	}

	private SAFRejectsDetailDecorator decorateRejectDetail(
		final SAFRejectDetailsEntry entry) {

		this.perfManager.start(
			"decorateRejectDetail - SAFRejectsDetailDecorator detail = new SAFRejectsDetailDecorator();");
		this.perfManager.registerLapCount(
			"decorateRejectDetail - SAFRejectsDetailDecorator detail = new SAFRejectsDetailDecorator();");
		final SAFRejectsDetailDecorator detail =
			new SAFRejectsDetailDecorator();
		this.perfManager.stop(
			"decorateRejectDetail - SAFRejectsDetailDecorator detail = new SAFRejectsDetailDecorator();");
		this.perfManager.start(
			"decorateRejectDetail - if (entry.getTimestamp() != null)");

		if (entry.getTimestamp() != null) {
			final Date date = entry.getTimestamp(
			).toGregorianCalendar(
			).getTime();
			detail.setTimestamp(date);
		}

		this.perfManager.registerLapCount(
			"decorateRejectDetail - if (entry.getTimestamp() != null)");
		this.perfManager.stop(
			"decorateRejectDetail - if (entry.getTimestamp() != null)");
		this.perfManager.start(
			"decorateRejectDetail - if (entry.getTransactionDateTime() != null)");

		if (entry.getTransactionDateTime() != null) {
			final Date date = entry.getTransactionDateTime(
			).toGregorianCalendar(
			).getTime();
			detail.setTransactionDateTime(date);
		}

		this.perfManager.registerLapCount(
			"decorateRejectDetail - if (entry.getTransactionDateTime() != null)");
		this.perfManager.stop(
			"decorateRejectDetail - if (entry.getTransactionDateTime() != null)");
		detail.setBusinessDate(entry.getBusinessDate());
		detail.setSettlementDate(entry.getSettlementDate());
		detail.setBin(entry.getBin());
		detail.setPrefix(entry.getPrefix());
		detail.setAcquirerId(entry.getAcquirerId());
		detail.setFinancialInstitution(entry.getFinancialInstitution());
		detail.setStan(entry.getStan());
		detail.setRrn(entry.getRrn());
		detail.setTransactionType(entry.getTransactionType());
		detail.setAccount(entry.getAccount());
		detail.setTransactionAmount(entry.getTransactionAmount());
		detail.setFeeAmount(entry.getFeeAmount());
		detail.setCompletedAmount(entry.getCompletedAmount());
		detail.setCardHolderBillingAmount(entry.getCardHolderBillingAmount());
		detail.setTransactionReversed(entry.getTransactionReversed());
		detail.setTerminalId(entry.getTerminalId());
		detail.setPan(entry.getPan());
		detail.setResponseCode(entry.getResponseCode());
		detail.setAuthorisedBy(entry.getAuthorisedBy());
		detail.setMessageType(entry.getMessageType());
		detail.setApprovalCode(entry.getApprovalCode());
		detail.setDestinationInterchangeName(
			entry.getDestinationInterchangeName());

		return detail;
	}

	private void generateEmptyCsvFileWhenError(final ResourceResponse response)
		throws IOException {

		response.setContentType(Constants.TEXT_CSV);
		response.setProperty(
			"Content-Disposition", "attachment;filename=error-TE-DSF.csv");
		response.setProperty("Set-Cookie", "fileDownload=true;path=/");
		final Appendable out = new BufferedWriter(response.getWriter());
		final CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
		this.populateCsvHeaderRow(printer);
		printer.printRecord(Constants.ERROR_MESSAGE);
		printer.flush();
	}

	private void generateSAFDetailsCsv(
			final ResourceResponse response, final String settlementDate,
			final SAFRejectDetailsResponseType webserviceResponse)
		throws IOException {

		this.perfManager.start("generateSAFDetailsCsv - init");
		response.setContentType(Constants.TEXT_CSV);
		response.setProperty(
			"Content-Disposition",
			"attachment;filename=\"" + settlementDate + "-TE-DSF.csv" + "\"");
		response.setProperty("Set-Cookie", "fileDownload=true;path=/");
		final Appendable out = new BufferedWriter(response.getWriter());
		final CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
		this.populateCsvHeaderRow(printer);
		final List<SAFRejectDetailsEntry> safDetailsList =
			webserviceResponse.getResults();
		final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			Constants.DATE_FORMAT_DDMMYYYY_HHMM);
		this.perfManager.stop("generateSAFDetailsCsv - init");
		this.perfManager.start(
			"generateSAFDetailsCsv - for (SAFRejectDetailsEntry entry : safDetailsList)");

		for (final SAFRejectDetailsEntry entry : safDetailsList) {
			this.perfManager.start(
				"generateSAFDetailsCsv - SAFRejectsDetailDecorator decorated = decorateRejectDetail(entry);");
			final SAFRejectsDetailDecorator decorated =
				this.decorateRejectDetail(entry);
			this.perfManager.stop(
				"generateSAFDetailsCsv - SAFRejectsDetailDecorator decorated = decorateRejectDetail(entry);");
			this.perfManager.start(
				"generateSAFDetailsCsv - printer.printRecord");
			printer.printRecord(
				new Object[] {
					dateFormatter.format(decorated.getTimestamp()),
					decorated.getBusinessDate(), decorated.getSettlementDate(),
					dateFormatter.format(decorated.getTransactionDateTime()),
					decorated.getBin(), decorated.getPrefix(),
					decorated.getAcquirerId(),
					"=\"" + decorated.getFinancialInstitution() + "\"",
					decorated.getStan(), "=\"" + decorated.getRrn() + "\"",
					decorated.getTransactionType(), decorated.getAccount(),
					decorated.getTransactionAmount(), decorated.getFeeAmount(),
					decorated.getCompletedAmount(),
					decorated.getCardHolderBillingAmount(),
					decorated.getTransactionReversed(),
					decorated.getTerminalId(),
					"=\"" + decorated.getPan() + "\"",
					decorated.getResponseCode(), decorated.getApprovalCode(),
					decorated.getAuthorisedBy(), decorated.getMessageType(),
					decorated.getDestinationInterchangeName()
				});
			this.perfManager.stop(
				"generateSAFDetailsCsv - printer.printRecord");
		}

		this.perfManager.stop(
			"generateSAFDetailsCsv - for (SAFRejectDetailsEntry entry : safDetailsList)");
		this.perfManager.start("generateSAFDetailsCsv - printer.flush()");
		printer.flush();
		this.perfManager.stop("generateSAFDetailsCsv - printer.flush()");
	}

	private void populateCsvHeaderRow(final CSVPrinter printer)
		throws IOException {

		printer.printRecord(
			"TIMESTAMP", "BUSINESS_DATE", "SETTLEMENT_DATE",
			"TRANSACTION_DATE_TIME", "BIN", "PREFIX", "ACQR_INST_ID", "FI",
			"STAN", "RRN", "TRAN_TYPE", "ACCOUNT", "TRAN_AMT", "FEE_AMT",
			"COMPLETED_AMT", "CARDHOLDER_BILLING_AMT", "REV_TRAN",
			"TERMINAL_ID", "PAN", "RESPONSE_CODE", "APPROVAL_CODE", "AUTH_BY",
			"MESSAGE_TYPE", "DEST_NAME");
	}

	private String usernameFromRequest(final PortletRequest request) {
		SAFRejectsController.logger.debug("usernameFromRequest - start");
		String username = null;
		User user = null;

		try {
			user = PortalUtil.getUser(request);

			if (user != null) {
				username = user.getScreenName();
			}
		}
		catch (PortalException e) {
			SAFRejectsController.logger.error(
				"usernameFromRequest - Portal Exception trying to get user from request. " +
					e.getMessage(),
				e);
		}
		catch (SystemException e2) {
			SAFRejectsController.logger.error(
				"usernameFromRequest - System Exception trying to get user from request. " +
					e2.getMessage(),
				e2);
		}

		return username;
	}

	private Organization userOrgFromRequest(final PortletRequest request) {
		User user = null;
		Organization org = null;

		try {
			user = PortalUtil.getUser(request);

			if (user != null) {
				final List<Organization> orgs =
					OrganizationLocalServiceUtil.getUserOrganizations(
						user.getUserId());

				if ((orgs != null) && (orgs.size() == 1)) {
					org = orgs.get(0);
				}
			}
		}
		catch (PortalException pe) {
			SAFRejectsController.logger.error(
				"usernameFromRequest - Portal exception getting user details " +
					pe.getMessage(),
				pe);
		}
		catch (SystemException se) {
			SAFRejectsController.logger.error(
				"usernameFromRequest - System exception getting user details " +
					se.getMessage(),
				se);
		}

		return org;
	}

	private static Logger logger;

	static {
		SAFRejectsController.logger = Logger.getLogger(
			SAFRejectsController.class);
	}

	private String orgFullNameOverride;
	private Long orgIdOverride;
	private String orgShortNameOverride;
	private final PerformanceMonitorManager perfManager;

	@Autowired
	@Qualifier(Constants.SAF_REJECTS_SERVICE)
	private SAFRejectsService safService;

}