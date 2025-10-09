package au.com.cuscal.connect.controllers;

import au.com.cuscal.connect.commons.Constants;
import au.com.cuscal.connect.commons.Utility;
import au.com.cuscal.connect.domain.ReportMetadata;
import au.com.cuscal.connect.domain.ReportOwner;
import au.com.cuscal.connect.domain.ReportType;
import au.com.cuscal.connect.forms.ReportForm;
import au.com.cuscal.connect.forms.ReportResultForm;
import au.com.cuscal.connect.forms.ReportResultFormDisplayable;
import au.com.cuscal.connect.forms.ReportResultFormResults;
import au.com.cuscal.connect.forms.ReportsPagingParameters;
import au.com.cuscal.connect.services.ReportService;
import au.com.cuscal.connect.validator.ReportValidator;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.webservices.Header;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;
import com.liferay.portletmvc4spring.bind.annotation.ResourceMapping;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Controller that returns that Report Result View
 *
 * @author Rajni Bharara
 *
 */
@Controller("reportViewController")
@RequestMapping("VIEW")
@SessionAttributes(types = ReportForm.class)
public class ReportViewController {

	/**
	 * Constructor that sets up reportService via DI
	 *
	 * @param reportService
	 */
	public ReportViewController(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * Builds the Reports Paging Parameters
	 *
	 * @param RenderRequest
	 * @param ReportForm
	 * @return ReportsPagingParameters
	 */
	public ReportsPagingParameters buildReportsPagingParameters(
		RenderRequest request, ReportForm reportForm) {

		ReportsPagingParameters parameters = new ReportsPagingParameters();
		setWebContentIdInRequest(request);
		logger.debug(
			"[ReportViewController.buildReportsPagingParameters] : the page number in request is " +
				ParamUtil.getInteger(request, "page"));
		int pageNum = ParamUtil.getInteger(request, "page");
		parameters.setPageSize(reportForm.getPageSize());

		if (pageNum == 0) {
			parameters.setPageNum(0);
		}
		else {
			parameters.setPageNum(pageNum - 1);
		}

		return parameters;
	}

	/**
	 * Builds the Reports Paging Parameters for action
	 *
	 * @param ActionRequest
	 * @param ReportForm
	 * @return ReportsPagingParameters
	 */
	public ReportsPagingParameters buildReportsPagingParametersForAction(
		ActionRequest request, ReportForm reportForm) {

		ReportsPagingParameters parameters = new ReportsPagingParameters();
		setWebContentIdInRequest(request);
		logger.debug(
			"[ReportViewController.buildReportsPagingParametersForAction] : the page number in request is " +
				ParamUtil.getInteger(request, "page"));
		int pageNum = ParamUtil.getInteger(request, "page");
		parameters.setPageSize(reportForm.getPageSize());

		if (pageNum == 0) {
			parameters.setPageNum(0);
		}
		else {
			parameters.setPageNum(pageNum - 1);
		}

		return parameters;
	}

	/**
	 * Map the ReportMetadata from database into ReportResultForm
	 *
	 * @param ReportMetadata
	 * @return ReportResultForm
	 */
	public ReportResultForm covertObjectListToReportResultFormObj(
		ReportMetadata objects) {

		ReportResultForm reportResultForm = new ReportResultForm();
		ReportOwner reportOwner = objects.getReportOwner();
		ReportType reportType = objects.getReportType();
		Long fileSize = Long.valueOf(
			objects.getFileSize(
			).longValue());

		reportResultForm.setFileSizeType(findFileSizeType(fileSize));
		Float fileTransferSpeedVal = 512f;

		Float fileTransferSpeed = fileTransferSpeedVal * 128f;

		Float fileDownloadTime = Float.valueOf(fileSize / fileTransferSpeed);

		String reportBin = objects.getReportBin();

		if (!StringUtils.isBlank(reportBin)) {
			reportResultForm.setReportBin(reportBin);
		}

		reportResultForm.setIsArchived(objects.getIsArchived());

		if ((objects.getReportEndDate() != null) &&
			!objects.getReportEndDate(
			).toString(
			).equals(
				""
			)) {

			reportResultForm.setReportDate(objects.getReportEndDate());
			reportResultForm.setReportEndDate(objects.getReportEndDate());
		}

		if ((objects.getReportStartDate() != null) &&
			!objects.getReportStartDate(
			).toString(
			).equals(
				""
			)) {

			reportResultForm.setReportStartDate(objects.getReportStartDate());
		}

		reportResultForm.setReportName(reportType.getName());
		reportResultForm.setReportOwnerName(reportOwner.getName());
		reportResultForm.setReportTitle(reportType.getDescription());
		reportResultForm.setBlobFileId(
			objects.getReportMetaBlobKey(
			).getReport_id(
			).toString());
		reportResultForm.setRunDate(
			objects.getReportMetaBlobKey(
			).getReport_run_date());

		if ((fileDownloadTime >= 1) && (fileDownloadTime <= 59)) {
			reportResultForm.setFileDownloadTime(
				Math.round(fileDownloadTime) + " sec");
		}
		else if (fileDownloadTime >= 60) {
			Float fileDownloadMinTime = Float.valueOf(fileDownloadTime / 60);

			if ((fileDownloadMinTime >= 1) && (fileDownloadMinTime <= 59)) {
				reportResultForm.setFileDownloadTime(
					Math.round(fileDownloadMinTime) + " min");
			}
			else {
				if (fileDownloadMinTime > 60) {
					reportResultForm.setFileDownloadTime(" > 1 hr");
				}
			}
		}
		else {
			reportResultForm.setFileDownloadTime(" < 1 sec");
		}

		reportResultForm.setFileName(objects.getFileName());

		if (StringUtils.isBlank(objects.getFileType())) {
			logger.error(
				"covertObjectListToReportResultFormObj - Report " +
					reportResultForm.getBlobFileId() +
						" has no file type defined.");
		}

		reportResultForm.setFileType(objects.getFileType());

		reportResultForm.setIsEncrypted(objects.getIsEncrypted());

		reportResultForm.setFileSize(objects.getFileSize());

		return reportResultForm;
	}

	/**
	 * Returns the Blob data from BlobId
	 *
	 * @param ResourceRequest
	 * @param ResourceResponse
	 * @return void
	 */
	@ResourceMapping
	public void downloadReportFileBlob(
		ResourceRequest request, ResourceResponse response) {

		String blobId = ParamUtil.getString(request, "id", null);
		String runDate = ParamUtil.getString(request, "rd", null);
		String steppedUp = "false";

		try {
			setWebContentIdInRequest(request);

			if ((blobId != null) && (runDate != null)) {
				logger.debug(
					"[ReportViewController.downloadReportFileBlob ] : The blob id  is " +
						blobId);
				logger.debug(
					"[ReportViewController.downloadReportFileBlob ] : The blob run date  is " +
						runDate);
				User user = PortalUtil.getUser(request);

				if (user != null) {
					boolean isSuccess = false;
					String type = ParamUtil.getString(request, "type");
					HttpServletRequest httpServletRequest =
						PortalUtil.getHttpServletRequest(request);

					HttpSession httpSession = httpServletRequest.getSession();
					PortletSession portletSession = request.getPortletSession();

					steppedUp = GetterUtil.getString(
						portletSession.getAttribute(
							Constants.USER_STEPPEDUP,
							PortletSession.APPLICATION_SCOPE));

					if (steppedUp.isEmpty()) {
						steppedUp = GetterUtil.getString(
							httpSession.getAttribute(Constants.USER_STEPPEDUP));
					}

					if ("pdf".equalsIgnoreCase(type)) {
						if ("yes".equalsIgnoreCase(
								ParamUtil.getString(request, "convert"))) {

							isSuccess =
								reportService.retriveReportBlobWithConvert(
									user, blobId, runDate, response, request,
									type);
						}
						else {
							isSuccess =
								reportService.
									retriveReportBlobObjByBlobIdAndBlobDate(
										user, blobId, runDate, response,
										request);
						}
					}
					else if ("true".equalsIgnoreCase(steppedUp) &&
							 "ws".equalsIgnoreCase(type)) {

						logger.debug(
							"downloadReportFileBlob - session header is: " +
								httpSession.getAttribute(
									"serviceRequestHeader"));

						Header header = getHeaderForServiceRequest(
							user, request);

						isSuccess =
							reportService.retrieveEncryptedReportFromWSCall(
								user, header, blobId, runDate, response,
								request);
					}
					else {
						isSuccess =
							reportService.
								retriveReportBlobObjByBlobIdAndBlobDate(
									user, blobId, runDate, response, request);
					}

					logger.debug(
						"downloadReportFileBlob - After file view/Download: " +
							isSuccess);
				}
			}
		}
		catch (Exception e) {
			logger.error(
				"downloadReportFileBlob - EXCEPTION CAME ERROR MSG is " +
					e.getMessage(),
				e);
		}
	}

	/**
	 * Find out the size of the file
	 *
	 * @param Long
	 * @return String
	 */
	public String findFileSizeType(Long fileSize) {
		logger.debug(
			"[ReportViewController.findFileSizeType ] : The File Size Is : " +
				fileSize);
		String fileSizeType = "";
		Float kiloBytes = Float.valueOf(1024);
		Float megaBytes = Float.valueOf(1048576);
		Float gigaBytes = Float.valueOf(1073741824);

		if ((kiloBytes.longValue() <= fileSize.longValue()) &&
			(megaBytes.longValue() > fileSize.longValue())) {

			logger.debug(
				"[ReportViewController.findFileSizeType ] : The File Size Is Kilo Bytes : " +
					kiloBytes.longValue());
			float fileInKb = fileSize / kiloBytes;
			fileSizeType = Utility.Round(fileInKb, 2) + " KB";
		}
		else if ((megaBytes.longValue() <= fileSize.longValue()) &&
				 (gigaBytes.longValue() > fileSize.longValue())) {

			logger.debug(
				"[ReportViewController.findFileSizeType ] : The File Size Is Mega Bytes : " +
					megaBytes.longValue());
			float fileInKb = fileSize / megaBytes;
			fileSizeType = Utility.Round(fileInKb, 2) + " MB";
		}
		else if (gigaBytes.longValue() <= fileSize.longValue()) {
			logger.debug(
				"[ReportViewController.findFileSizeType ] : Lastly Giga Bytes ");
			float fileInKb = fileSize / gigaBytes;
			fileSizeType = Utility.Round(fileInKb, 2) + " GB";
		}
		else {
			fileSizeType = fileSize + " Bytes";
		}

		return fileSizeType;
	}

	/**
	 * 2FA action - This method set the report list in request attribute
	 *
	 * @param HttpSession
	 * @param ActionResponse
	 * @param User
	 * @param ActionRequest
	 * @return void
	 */
	public void findReportList(
		HttpSession session, ActionResponse response, User user,
		ActionRequest request) {

		ReportForm reportForm = null;
		setWebContentIdInRequest(request);

		if (session.getAttribute(_REPORT_FORM) != null) {
			reportForm = (ReportForm)session.getAttribute(_REPORT_FORM);

			ReportsPagingParameters parameters =
				buildReportsPagingParametersForAction(request, reportForm);
			List<ReportMetadata> metadatas = new ArrayList<>();
			ReportResultFormResults formResults =
				reportService.findReportsListByDates(
					user, reportForm, parameters, request, response);

			if ((formResults != null) && (formResults.getMetadatas() != null) &&
				(formResults.getMetadatas(
				).size() > 0)) {

				metadatas = formResults.getMetadatas();
				logger.debug(
					"[ReportViewController.findReportsByToAndFromDates] : Action mapping MATADATAS is not nulll");
				formResults.setReportList(
					populateDataFromReportResults(metadatas));
				request.setAttribute("reportDbList", formResults);
			}
			else {
				formResults.setReportList(new ArrayList<ReportResultForm>());
				request.setAttribute("reportDbList", formResults);
			}
		}
		else {
			logger.warn("Form object in session is null");
		}
	}

	/**
	 * Shows the initial view of the Report form
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @param ReportForm
	 * @return String
	 */
	@RenderMapping
	public String findReportListWithoutDatesFilterCat(
		RenderResponse response, RenderRequest request,
		@ModelAttribute ReportForm reportForm) {

		logger.debug(
			"[ReportViewController.findReportListWithoutDatesFilterCat]INSIDE THE DEFAULT URL OF RENDER MAPPING 222222 ");

		try {
			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpSession session = httpServletRequest.getSession();
			setWebContentIdInRequest(request);
			session.setAttribute(_REPORT_FORM, null);
			User user = null;

			try {
				user = PortalUtil.getUser(request);
			}
			catch (Exception e) {
				logger.error(
					"[ReportViewController.findReportListWithoutDatesFilterCat] EXCEPTION CAME when user is null  " +
						e.getMessage(),
					e);
			}

			if (user != null) {
				ReportsPagingParameters parameters =
					buildReportsPagingParameters(request, reportForm);
				List<ReportMetadata> metadatas = new ArrayList<>();
				ReportResultFormResults formResults =
					reportService.retriveReportResult(
						user, parameters, request, response);

				if ((formResults != null) &&
					(formResults.getMetadatas() != null) &&
					(formResults.getMetadatas(
					).size() > 0)) {

					metadatas = formResults.getMetadatas();
					List<ReportResultForm> forms =
						populateDataFromReportResults(metadatas);

					formResults.setReportList(forms);
					request.setAttribute(
						"reportList",
						ReportResultFormDisplayable.wrap(formResults));
				}
				else {
					request.setAttribute("noDataFoundMsg", "No reports found");
				}
			}

			return Constants.REPORT_RESULTS;
		}
		catch (Exception e) {
			logger.error(
				"[ReportViewController.findReportListWithoutDatesFilterCat] EXCEPTION CAME   in Default render " +
					e.getMessage(),
				e);

			/**
			 * StackTraceElement[] elements = (StackTraceElement[]) e
			 * .getStackTrace(); for (StackTraceElement stackTraceElement :
			 * elements) { logger.error(
			 * "[ReportViewController.findReportListWithoutDatesFilterCat] EXCEPTION CAME ERROR StackTrace() is  "
			 * + stackTraceElement.toString()); } logger.error(
			 * "[ReportViewController.findReportListWithoutDatesFilterCat] EXCEPTION CAME ERROR MSG is "
			 * + e.getMessage());
			 */
			request.setAttribute("errorMsg", Constants.ERROR_MSG);

			return Constants.REPORT_ERROR;
		}
	}

	/**
	 * Responsible for getting the Reports list when filter criteria is
	 * provided.
	 *
	 * @param ReportForm
	 * @param BindingResult
	 * @param ActionResponse
	 * @param SessionStatus
	 * @param ActionRequest
	 */
	@ActionMapping(
		params = Constants.REPORT_ACTION + Constants.EQUALS + Constants.SEND_REPORT_REQUEST
	)
	public void findReportsByToAndFromDates(
		@ModelAttribute ReportForm reportForm, BindingResult bindingResult,
		ActionResponse response, SessionStatus sessionStatus,
		ActionRequest request) {

		logger.debug(
			"[ReportViewController.findReportsByToAndFromDates] : INSIDE THE ON SUBMIT BUTTON ACTION  " +
				request.getParameter("reportAction"));
		setWebContentIdInRequest(request);
		logger.debug(
			"[ReportViewController.findReportsByToAndFromDates] :  Action mapping SEND_REPORT_REQUEST");

		reportValidator.validate(reportForm, bindingResult);

		if (!bindingResult.hasErrors()) {
			try {
				User user = PortalUtil.getUser(request);

				if (user != null) {
					logger.debug("In the Action mapping USER IS NOT NULL ");

					if (Utility.bothToDateAndFromDateAreNOT18MonthPastDate(
							reportForm)) {

						_setSessionAttributes(
							request, _REPORT_FORM, reportForm);
						ReportsPagingParameters parameters =
							buildReportsPagingParametersForAction(
								request, reportForm);
						List<ReportMetadata> metadatas = new ArrayList<>();
						ReportResultFormResults formResults =
							reportService.findReportsListByDates(
								user, reportForm, parameters, request,
								response);

						if ((formResults != null) &&
							(formResults.getMetadatas() != null) &&
							(formResults.getMetadatas(
							).size() > 0)) {

							metadatas = formResults.getMetadatas();
							logger.debug(
								"[ReportViewController.findReportsByToAndFromDates] : Action mapping MATADATAS is not nulll");
							formResults.setReportList(
								populateDataFromReportResults(metadatas));
							request.setAttribute("reportDbList", formResults);
						}
						else {
							formResults.setReportList(
								new ArrayList<ReportResultForm>());
							request.setAttribute("reportDbList", formResults);
						}

						logger.debug(
							"[ReportViewController.findReportsByToAndFromDates] : Action mapping Setting Render SHOW_REPORT_SUCCESS");
						response.setRenderParameter(
							Constants.REPORT_RENDER,
							Constants.SHOW_REPORT_SUCCESS);
					}
					else {
						logger.debug(
							"[ReportViewController.findReportsByToAndFromDates] : Action mapping Setting Render SHOW_REPORT_SUCCESS_18PAST_DATE");
						response.setRenderParameter(
							Constants.REPORT_RENDER,
							Constants.SHOW_REPORT_SUCCESS_18PAST_DATE);
					}
				}
				else {
					_setSessionAttributes(request, _REPORT_FORM, null);
				}
			}
			catch (Exception ex) {
				_setSessionAttributes(request, _REPORT_FORM, null);

				logger.error(
					"[ReportViewController.findReportsByToAndFromDates] EXCEPTION CAME ERROR MSG is " +
						ex.getMessage(),
					ex);
				request.setAttribute("errorMsg", Constants.ERROR_MSG);
				response.setRenderParameter(
					Constants.REPORT_RENDER, Constants.SHOW_REPORT_ERROR);
			}
		}
		else {
			logger.debug(
				"[ReportViewController.findReportsByToAndFromDates] : Action mapping Setting Render SHOW_ON_VALIDATION_ERROR");
			response.setRenderParameter(
				Constants.REPORT_RENDER, Constants.SHOW_ON_VALIDATION_ERROR);
			_setSessionAttributes(request, _REPORT_FORM, null);
		}

		sessionStatus.setComplete();
	}

	/**
	 * Send control to 2fa page and if valid perform the file download
	 *
	 * @param ReportForm
	 * @param ActionResponse
	 * @param SessionStatus
	 * @param ActionRequest
	 * @return void
	 */
	@ActionMapping(
		params = Constants.REPORT_ACTION + Constants.EQUALS + Constants.PAGE_2FA
	)
	public void forwardTo2FAPageAndBackToReportDownload(
		@ModelAttribute ReportForm reportForm, ActionResponse response,
		SessionStatus sessionStatus, ActionRequest request) {

		logger.info("forwardTo2FAPageAndBackToReportDownload - start");

		try {
			setWebContentIdInRequest(request);
			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpSession httpSession = httpServletRequest.getSession();
			PortletSession portletSession = request.getPortletSession();
			User user = PortalUtil.getUser(request);
			String sessionUserSteppedup = GetterUtil.getString(
				portletSession.getAttribute(
					Constants.USER_STEPPEDUP,
					PortletSession.APPLICATION_SCOPE));

			logger.debug(
				"forwardTo2FAPageAndBackToReportDownload - The session attribute is: " +
					sessionUserSteppedup);

			if (!StringUtils.isBlank(
					ParamUtil.getString(request, Constants.REPORT_ID)) &&
				!StringUtils.isBlank(
					ParamUtil.getString(request, Constants.RUN_DATE)) &&
				!StringUtils.isBlank(
					ParamUtil.getString(request, Constants.TYPE))) {

				logger.debug(
					"[forwardTo2FAPageAndBackToReportDownload] setting the request attributes ");

				httpSession.setAttribute(
					Constants.REPORT_ID,
					ParamUtil.getString(request, Constants.REPORT_ID));
				httpSession.setAttribute(
					Constants.RUN_DATE,
					ParamUtil.getString(request, Constants.RUN_DATE));
				httpSession.setAttribute(
					Constants.TYPE,
					ParamUtil.getString(request, Constants.TYPE));
			}

			String sessionUserCancel = GetterUtil.getString(
				portletSession.getAttribute(
					Constants.USER_CANCEL, PortletSession.APPLICATION_SCOPE));

			if (Constants.CANCEL.equals(sessionUserCancel)) {
				httpSession.setAttribute(Constants.USER_CANCEL, null);
				portletSession.setAttribute(
					Constants.USER_CANCEL, null,
					PortletSession.APPLICATION_SCOPE);

				findReportList(httpSession, response, user, request);

				request.setAttribute(
					Constants.IS_FA, isUserHas2FARole(request));
				httpSession.setAttribute(Constants._RETURN_URL, null);
				portletSession.setAttribute(
					Constants._RETURN_URL, null,
					PortletSession.APPLICATION_SCOPE);
				logger.debug("steppedup:   value is  " + sessionUserSteppedup);
				request.setAttribute(
					Constants.REPORT_ACTION, Constants._2FA_ACTION);
				response.getRenderParameters(
				).setValue(
					Constants.REPORT_RENDER, Constants.SHOW_REPORT_SUCCESS
				);
			}
			else if (Constants.FALSE.equals(sessionUserSteppedup) ||
					 Constants.ERROR.equals(sessionUserSteppedup)) {

				String url = PortalUtil.getCurrentURL(httpServletRequest);

				httpSession.setAttribute(Constants._RETURN_URL, url);
				portletSession.setAttribute(
					Constants._RETURN_URL, url,
					PortletSession.APPLICATION_SCOPE);

				try {
					response.sendRedirect("/2fa");
				}
				catch (IOException ioe) {
					logger.error(
						"Exception while sending the url to 2 FA page from Reports : " +
							ioe.getMessage(),
						ioe);
				}
			}
			else {
				logger.debug(
					"In else part after the 2fa ^^^^^^^^^^^^^^^^ " +
						reportForm.getFromDate());
				findReportList(httpSession, response, user, request);
				logger.debug(
					"forwardTo2FAPageAndBackToReportDownload - steppedUp value is: " +
						sessionUserSteppedup);

				request.setAttribute(
					Constants.REPORT_ACTION, Constants._2FA_ACTION);

				response.getRenderParameters(
				).setValue(
					Constants.REPORT_RENDER, Constants.SHOW_REPORT_SUCCESS
				);
			}
		}
		catch (Exception e) {
			logger.error(
				"Exception while process action for 2 FA page from Reports : " +
					e.getMessage(),
				e);
		}

		sessionStatus.setComplete();
		logger.info("forwardTo2FAPageAndBackToReportDownload - end");
	}

	public String getArticleId() {
		return articleId;
	}

	/**
	 * Creates the ReportForm based
	 *
	 * @return
	 */
	@ModelAttribute(Constants.REPORT_FORM)
	public ReportForm getCommandObject() {
		ReportForm reportForm = new ReportForm();

		reportForm.setFromDate(Utility.getYesterdayDate());
		reportForm.setToDate(Utility.getYesterdayDate());

		return reportForm;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getOrgIdOverride() {
		return orgIdOverride;
	}

	/**
	 * Check and set the user 2 FA roles detail in object.
	 *
	 * @param PortletRequest
	 * @return boolean
	 */
	public boolean isUserHas2FARole(PortletRequest request) {
		logger.info("setUser2FaRoles - start");
		boolean is2faAccessible = false;
		User user = null;

		try {
			user = PortalUtil.getUser(request);

			if (null != user) {
				Long userId = user.getUserId();

				List<Role> roles = RoleLocalServiceUtil.getUserRoles(userId);
				List<Role> grpRoles = Utility.findUserGroupRoleList(user);
				List<Role> allUserRoles = new ArrayList<>();

				allUserRoles.addAll(roles);
				allUserRoles.addAll(grpRoles);
				logger.debug(
					"[setUser2FaRoles]  All Roles count is " +
						allUserRoles.size());

				for (Role role : allUserRoles) {
					if (Constants.REPORT_ROLE_FA.equalsIgnoreCase(
							role.getName())) {

						is2faAccessible = true;
						logger.debug(
							"[setUser2FaRoles] setting role as true for Role  " +
								role.getName());
					}
				}
			}
		}
		catch (Exception e) {
			logger.error(
				"Exception while checking user has 2fa Roles or not : " +
					e.getMessage(),
				e);
		}

		logger.info("setUser2FaRoles - start");

		return is2faAccessible;
	}

	/**
	 * Populate Data From Report Results
	 *
	 * @param List
	 *            <ReportMetadata>
	 * @return List<ReportResultForm>
	 */
	public List<ReportResultForm> populateDataFromReportResults(
		List<ReportMetadata> metadatas) {

		List<ReportResultForm> resultForms = new ArrayList<>();
		ReportResultForm reportResultForm = null;

		for (ReportMetadata objects : metadatas) {
			reportResultForm = covertObjectListToReportResultFormObj(objects);
			resultForms.add(reportResultForm);
		}

		return resultForms;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setOrgIdOverride(String orgIdOverride) {
		this.orgIdOverride = orgIdOverride;
	}

	/**
	 * Setting Article Id and Group Id in request
	 *
	 * @param PortletRequest
	 * @return void
	 */
	public void setWebContentIdInRequest(PortletRequest portletRequest) {
		portletRequest.setAttribute("articleId", getArticleId());
		portletRequest.setAttribute("groupId", getGroupId());
	}

	/**
	 * Action mapping for default reports search
	 *
	 * @param ActionResponse
	 * @param ActionRequest
	 * @return void
	 */
	@ActionMapping(
		params = Constants.REPORT_ACTION + Constants.EQUALS + Constants.SHOW_REPORT_DEFAULT
	)
	public void showDefaultReportOnClearButton(
		ActionResponse response, ActionRequest request) {

		logger.debug(
			"[ReportViewController.findReportsByToAndFromDates] : INSIDE THE ON CLEAR BUTTON ACTION  " +
				request.getParameter("reportAction"));
		setWebContentIdInRequest(request);

		_setSessionAttributes(request, _REPORT_FORM, null);
	}

	/**
	 * Returns the success page after form is submitted
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.REPORT_RENDER + Constants.EQUALS + Constants.SHOW_REPORT_SUCCESS
	)
	public String showReportByDatesSuccessPage(
		RenderResponse response, RenderRequest request,
		@ModelAttribute ReportForm reportForm) {

		logger.debug(
			"[ReportViewController.showReportByDatesSuccessPage] : page number in ParamUtil is  " +
				ParamUtil.getInteger(request, "page"));
		setWebContentIdInRequest(request);
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(request);

		HttpSession httpSession = httpServletRequest.getSession();
		PortletSession portletSession = request.getPortletSession();

		int pageNum = ParamUtil.getInteger(request, "page");

		Object sessionReportForm = _getSessionAttributeWithFallback(
			httpServletRequest, request, _REPORT_FORM);

		if (sessionReportForm != null) {
			reportForm = (ReportForm)sessionReportForm;

			if (Utility.anyOneOfToDateAndFromDateAreNOT18MonthPastDate(
					reportForm)) {

				request.setAttribute("showDatePast18MonthMsg", "false");
			}
			else {
				request.setAttribute("showDatePast18MonthMsg", "true");
			}
		}

		String reportAction = GetterUtil.getString(
			request.getAttribute(Constants.REPORT_ACTION));

		if (Constants._2FA_ACTION.equals(reportAction)) {
			logger.debug("[showReportByDatesSuccessPage] : 2FA PAGE RENDER ");

			String userSteppedup = GetterUtil.getString(
				portletSession.getAttribute(
					Constants.USER_STEPPEDUP,
					PortletSession.APPLICATION_SCOPE));

			if (userSteppedup.isEmpty()) {
				userSteppedup = GetterUtil.getString(
					httpSession.getAttribute(Constants.USER_STEPPEDUP));
			}

			if (userSteppedup.equalsIgnoreCase("true")) {
				request.setAttribute(Constants.FROM_FA, Boolean.TRUE);
			}

			logger.debug(
				"[showReportByDatesSuccessPage] getting the request objects");

			String sessionReportId = GetterUtil.getString(
				httpSession.getAttribute(Constants.REPORT_ID));
			String sessionRunDate = GetterUtil.getString(
				httpSession.getAttribute(Constants.RUN_DATE));
			String sessionType = GetterUtil.getString(
				httpSession.getAttribute(Constants.TYPE));

			if (!StringUtils.isBlank(sessionReportId) &&
				!StringUtils.isBlank(sessionRunDate) &&
				!StringUtils.isBlank(sessionType)) {

				logger.debug(
					"[showReportByDatesSuccessPage] setting the request in RENDER MAPPING attributes ");
				request.setAttribute(Constants.REPORT_ID, sessionReportId);
				request.setAttribute(Constants.RUN_DATE, sessionRunDate);
				request.setAttribute(Constants.TYPE, sessionType);
			}
		}

		if (pageNum == 0) {
			logger.debug(
				"[ReportViewController.showReportByDatesSuccessPage] : for First time ");
			ReportResultFormResults reportResultFormResults =
				(ReportResultFormResults)request.getAttribute("reportDbList");

			if ((null != reportResultFormResults) &&
				(reportResultFormResults.getReportList(
				).size() > 0)) {

				request.setAttribute(
					"reportList",
					ReportResultFormDisplayable.wrap(reportResultFormResults));
			}
			else {
				request.setAttribute(
					"noDataFoundMsg",
					"No reports were found that matched your criteria");
			}
		}
		else {
			logger.debug(
				"[ReportViewController.showReportByDatesSuccessPage] : All other time ");

			try {
				User user = PortalUtil.getUser(request);

				if (user != null) {
					ReportForm reportFormSession = new ReportForm();
					logger.debug("In the Action mapping USER IS NOT NULL ");

					String fromDate = ParamUtil.getString(request, "fromDate");

					String toDate = ParamUtil.getString(request, "toDate");

					if (sessionReportForm != null) {
						reportFormSession = (ReportForm)sessionReportForm;
						logger.debug(
							"[ReportViewController.showReportByDatesSuccessPage ] : INSIDE THE SHOW_REPORT_SUCCESS AFTER THE ACTION URL FROM DATE is  " +
								reportFormSession.getFromDate());
						logger.debug(
							"[ReportViewController.showReportByDatesSuccessPage ] : INSIDE THE SHOW_REPORT_SUCCESS AFTER THE ACTION URL TO DATE  is  " +
								reportFormSession.getToDate());
					}
					else {
						if (Validator.isNotNull(fromDate) &&
							Validator.isNotNull(toDate)) {

							reportFormSession.setFromDate(fromDate);
							reportFormSession.setToDate(toDate);
						}
					}

					ReportsPagingParameters parameters =
						buildReportsPagingParameters(
							request, reportFormSession);
					List<ReportMetadata> metadatas = new ArrayList<>();
					ReportResultFormResults formResults =
						reportService.findReportsListByDates(
							user, reportFormSession, parameters, request,
							response);

					if ((formResults != null) &&
						(formResults.getMetadatas() != null) &&
						(formResults.getMetadatas(
						).size() > 0)) {

						metadatas = formResults.getMetadatas();
						logger.debug(
							"[ReportViewController.showReportByDatesSuccessPage] : Action mapping MATADATAS is not nulll");
						formResults.setReportList(
							populateDataFromReportResults(metadatas));
						request.setAttribute(
							"reportList",
							ReportResultFormDisplayable.wrap(formResults));
					}
					else {
						formResults.setReportList(
							new ArrayList<ReportResultForm>());
						request.setAttribute(
							"reportList",
							ReportResultFormDisplayable.wrap(formResults));
					}

					request.setAttribute(
						StringPool.UNDERLINE + _REPORT_FORM, sessionReportForm);
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				logger.error(
					"[ReportViewController.showReportByDatesSuccessPage] Exception Setting Render SHOW_REPORT_SUCCESS   " +
						ex.getMessage(),
					ex);
				request.setAttribute("errorMsg", Constants.ERROR_MSG);
			}
		}

		return Constants.REPORT_RESULTS;
	}

	/**
	 * Render mapping for SHOW REPORT SUCCESS 18PAST_DATE on search form
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.REPORT_RENDER + Constants.EQUALS + Constants.SHOW_REPORT_SUCCESS_18PAST_DATE
	)
	public String showReportByDatesSuccessPageOnPast18MonthDates(
		RenderResponse response, RenderRequest request) {

		logger.debug(
			"[ReportViewController.showReportByDatesSuccessPageOnPast18MonthDates] : SHOW_REPORT_SUCCESS_18PAST_DATE ");
		setWebContentIdInRequest(request);
		request.setAttribute("showDatePast18MonthMsg", "true");
		request.setAttribute(
			"noDataFoundMsg",
			"No reports were found that matched your criteria");

		return Constants.REPORT_RESULTS;
	}

	/**
	 * Returns the success page after form is submitted
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.REPORT_RENDER + Constants.EQUALS + Constants.SHOW_REPORT_ERROR
	)
	public String showReportError(
		RenderResponse response, RenderRequest request) {

		setWebContentIdInRequest(request);
		request.setAttribute("errorMsg", Constants.ERROR_MSG);

		return Constants.REPORT_ERROR;
	}

	/**
	 * Render mapping for validation issues on search form
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.REPORT_RENDER + Constants.EQUALS + Constants.SHOW_ON_VALIDATION_ERROR
	)
	public String showReportOnValidationErrors(
		RenderResponse response, RenderRequest request) {

		logger.debug(
			"[ReportViewController.showReportByDatesSuccessPageOnPast18MonthDates] : SHOW_ON_VALIDATION_ERROR ");
		setWebContentIdInRequest(request);
		request.setAttribute("noDataFoundMsg", "No reports found");

		return Constants.REPORT_RESULTS;
	}

	/**
	 * Shows the detail view of the Report
	 *
	 * @param RenderResponse
	 * @param RenderRequest
	 * @return String
	 */
	@RenderMapping(
		params = Constants.REPORT_RENDER + Constants.EQUALS + Constants.SHOW_VIEW_REPORT
	)
	public String showViewFileDetails(
		RenderResponse response, RenderRequest request) {

		String blobId = null;
		String runDate = null;

		try {
			setWebContentIdInRequest(request);

			if ((request.getParameter("id") != null) &&
				(request.getParameter("rd") != null)) {

				logger.debug(
					"[ReportViewController.showViewFileDetails] : The blob id REPORT DETAILS is " +
						request.getParameter("id"));
				logger.debug(
					"[ReportViewController.showViewFileDetails] : The blob run date REPORT DETAILS  is " +
						request.getParameter("rd"));
				request.setAttribute("blobFileId", request.getParameter("id"));
				request.setAttribute("runDate", request.getParameter("rd"));
				blobId = request.getParameter("id");
				runDate = request.getParameter("rd");
				User user = PortalUtil.getUser(request);

				if (user != null) {
					ReportMetadata metadatas = new ReportMetadata();

					metadatas =
						reportService.retriveReportResultByBlobIdAndBlobDate(
							user, blobId, runDate, request, response);

					if (metadatas != null) {
						request.setAttribute(
							"fileName", metadatas.getFileName());
						request.setAttribute(
							"reportDate", metadatas.getReportEndDate());
					}
				}
			}

			return Constants.REPORT_FILE_VIEW;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(
				"[ReportViewController.showViewFileDetails] EXCEPTION CAME ERROR MSG is " +
					e.getMessage());
			request.setAttribute("errorMsg", Constants.ERROR_MSG);

			return Constants.REPORT_ERROR;
		}
	}

	/**
	 *
	 * @param httpRequest
	 * 			Nullable. If null, will be retrieved from portletRequest
	 * @param portletRequest
	 * @param attributeName
	 * @return
	 */
	private Object _getSessionAttributeWithFallback(
		HttpServletRequest httpRequest, PortletRequest portletRequest,
		String attributeName) {

		HttpServletRequest httpServletRequest =
			httpRequest != null ? httpRequest :
				PortalUtil.getHttpServletRequest(portletRequest);

		HttpSession httpSession = httpServletRequest.getSession();

		Object obj = httpSession.getAttribute(attributeName);

		if (obj != null) {
			return obj;
		}

		return portletRequest.getPortletSession(
		).getAttribute(
			attributeName, PortletSession.APPLICATION_SCOPE
		);
	}

	private void _setSessionAttributes(
		PortletRequest portletRequest, HttpServletRequest httpRequest,
		String attributeName, Object obj) {

		if (httpRequest == null) {
			_setSessionAttributes(portletRequest, attributeName, obj);

			return;
		}

		httpRequest.getSession(
		).setAttribute(
			attributeName, obj
		);
		portletRequest.getPortletSession(
		).setAttribute(
			attributeName, obj, PortletSession.APPLICATION_SCOPE
		);
	}

	private void _setSessionAttributes(
		PortletRequest portletRequest, String attributeName, Object obj) {

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		HttpSession httpSession = httpServletRequest.getSession();

		httpSession.setAttribute(attributeName, obj);
		portletRequest.getPortletSession(
		).setAttribute(
			attributeName, obj, PortletSession.APPLICATION_SCOPE
		);
	}

	/**
	 *
	 * @param user
	 * @param request
	 * @return
	 */
	private Header getHeaderForServiceRequest(
		User user, PortletRequest request) {

		logger.debug(
			"getHeaderForServiceRequest - orgIdOverride: " + orgIdOverride);

		Header header = new Header();
		setWebContentIdInRequest(request);
		header.setOrigin(AuditOrigin.PORTAL_ORIGIN);

		if (user != null) {
			header.setUserId(user.getScreenName());

			try {
				List<Organization> orgs = user.getOrganizations();

				if ((orgs == null) || orgs.isEmpty()) {
					return null;
				}

				if (orgs.size() > 1) {
					return null;
				}

				if (StringUtils.isNotBlank(orgIdOverride)) {
					header.setUserOrgId(orgIdOverride);
				}
				else {
					Long orgId = orgs.get(
						0
					).getOrganizationId();

					header.setUserOrgId(orgId.toString());
				}

				header.setUserOrgName(
					orgs.get(
						0
					).getName());
			}
			catch (Exception e) {
				logger.error(
					"getHeaderForServiceRequest - Error getting the user's organisations. " +
						e.getMessage(),
					e);
			}
		}

		return header;
	}

	private static final String _REPORT_FORM = "reportForm";

	/**
	 * Logger object
	 */
	private static Logger logger = LoggerFactory.getLogger(
		ReportViewController.class);

	/**
	 * Web content Article Id
	 */
	private String articleId;

	/**
	 * Web content Group Id
	 */
	private String groupId;

	/**
	 * org Id Override
	 */
	private String orgIdOverride;

	/**
	 * Service used to save data
	 */
	private ReportService reportService;

	/**
	 * Report Validator
	 */
	@Autowired
	@Qualifier(Constants.REPORT_FORM_VALIDATOR)
	private ReportValidator reportValidator;

}