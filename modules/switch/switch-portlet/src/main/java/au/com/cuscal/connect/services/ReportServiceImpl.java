package au.com.cuscal.connect.services;

import au.com.cuscal.common.framework.AuditWebServicesUtil;
import au.com.cuscal.common.framework.memory.PerformanceMonitorManager;
import au.com.cuscal.connect.commons.Constants;
import au.com.cuscal.connect.commons.Utility;
import au.com.cuscal.connect.dao.ReportMetadataDao;
import au.com.cuscal.connect.domain.ReportBlob;
import au.com.cuscal.connect.domain.ReportMetadata;
import au.com.cuscal.connect.forms.ReportForm;
import au.com.cuscal.connect.forms.ReportResultFormResults;
import au.com.cuscal.connect.forms.ReportsPagingParameters;
import au.com.cuscal.framework.audit.AuditOrigin;
import au.com.cuscal.framework.audit.category.AuditCategories;
import au.com.cuscal.framework.audit.liferay.dxp.Auditor;
import au.com.cuscal.framework.audit.xml.XmlMessage;
import au.com.cuscal.framework.util.pdf.FileUtil;
import au.com.cuscal.framework.util.pdf.PDFWriter;
import au.com.cuscal.framework.webservices.Header;
import au.com.cuscal.framework.webservices.ReportDataType;
import au.com.cuscal.framework.webservices.ReportTransferRequest;
import au.com.cuscal.framework.webservices.ReportTransferResponse;
import au.com.cuscal.framework.webservices.client.ReportsService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.activation.DataHandler;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rajni Bharara
 *
 */
@Service(value = "reportService")
@SuppressWarnings("deprecation")
public class ReportServiceImpl implements ReportService {

	/**
	 * Logger object
	 */
	private static Logger logger = Logger.getLogger(ReportServiceImpl.class);

	/**
	 * PerformanceMonitorManager object
	 */
	private PerformanceMonitorManager perfMon = null;

	/**
	 * Connecting to the webservice Client.
	 */
	private ReportsService reportsService = null;

	private String reportsFileMimeType = null;
	private Map<String, String> cachedReportsMimeTypeMap = null;

	/**
	 * ReportMetadataDao object
	 */
	@Autowired
	ReportMetadataDao reportMetadataDao;

	/**
	 * Audit object
	 */
	private static final Auditor audit = Auditor.getInstance();

	/**
	 * Buffer size for reading and writing temp files.
	 */
	int bufferSize = 1024 * 10;

	/**
	 * Get report list by default search params
	 *
	 * @param User
	 * @param ReportsPagingParameters
	 * @param PortletRequest
	 * @param PortletResponse
	 * @return ReportResultFormResults
	 */
	@Transactional
	public ReportResultFormResults retriveReportResult(
		User user, ReportsPagingParameters parameters, PortletRequest request,
		PortletResponse response) {

		ReportResultFormResults reportResultFormResults = null;
		long size = 0;
		List<Set<Long>> ownerReportTypeidStr = findOwnerIdAndReportTypeId(user);

		logger.debug(
			"retriveReportResult - ownerReportList size is: " +
				ownerReportTypeidStr.size());
		logger.debug(
			"retriveReportResult - ownerReportTypeidStr.get(0).size: " +
				ownerReportTypeidStr.get(
					0
				).size());
		logger.debug(
			"retriveReportResult - ownerReportTypeidStr.get(1).size(): " +
				ownerReportTypeidStr.get(
					1
				).size());

		if ((ownerReportTypeidStr != null) &&
			(ownerReportTypeidStr.get(
				0
			).size() > 0) &&
			(ownerReportTypeidStr.get(
				1
			).size() > 0)) {

			logger.debug(
				"retriveReportResult - Report Owner ids and Report Type Ids are " +
					ownerReportTypeidStr.get(0) + " AND " +
						ownerReportTypeidStr.get(1));
			reportResultFormResults = reportMetadataDao.retriveReportResult(
				ownerReportTypeidStr.get(0), ownerReportTypeidStr.get(1),
				parameters);

			if (reportResultFormResults != null) {
				if ((reportResultFormResults.getMetadatas() != null) &&
					(reportResultFormResults.getMetadatas(
					).size() > 0)) {

					size = reportResultFormResults.getMetadatas(
					).size();
				}

				auditMessageForSearch(
					null, user, size, null, request, response, Boolean.TRUE,
					Boolean.TRUE);
			}
			else {
				auditMessageForSearch(
					null, user, size, null, request, response, Boolean.FALSE,
					Boolean.TRUE);
			}
		}
		else {
			String message =
				"Liferay Org ID: " + getUserOrgId(user) +
					" has not been setup properly for Report access in Report_Access and Report_Org_Owner tables.";

			logger.error("retriveReportResult - " + message);

			auditMessageForSearch(
				null, user, size, message, request, response, Boolean.TRUE,
				Boolean.FALSE);
		}

		return reportResultFormResults;
	}

	/**
	 * Return the report details with blobId and runDate.
	 *
	 * @param User
	 * @param String
	 * @param String
	 * @param PortletRequest
	 * @param PortletResponse
	 * @return ReportMetadata
	 */
	@Transactional
	public ReportMetadata retriveReportResultByBlobIdAndBlobDate(
		User user, String blobId, String runDate, PortletRequest request,
		PortletResponse response) {

		ReportMetadata reportMetadata = null;
		List<Set<Long>> ownerReportTypeidStr = findOwnerIdAndReportTypeId(user);

		if ((ownerReportTypeidStr != null) &&
			(ownerReportTypeidStr.get(
				0
			).size() > 0) &&
			(ownerReportTypeidStr.get(
				1
			).size() > 0)) {

			logger.debug(
				"retriveReportResultByBlobIdAndBlobDate - Report Owner ids and Report Type Ids are " +
					ownerReportTypeidStr.get(0) + " AND " +
						ownerReportTypeidStr.get(1));
			reportMetadata =
				reportMetadataDao.retriveReportResultByBlobIdAndRunDate(
					ownerReportTypeidStr.get(0), ownerReportTypeidStr.get(1),
					blobId, runDate);

			if (reportMetadata != null) {
				auditMessageForReportDetailsAndDownloads(
					user, blobId, runDate, reportMetadata.getFileName(),
					reportMetadata.getReportType(
					).getName(),
					null, request, response, AuditCategories.REPORTS_DETAIL,
					Boolean.TRUE);
			}
			else {
				String message =
					"Could not retrieve report metadata for blobId: " + blobId +
						" with runDate: " + runDate;

				auditMessageForReportDetailsAndDownloads(
					user, blobId, runDate, null, null, message, request,
					response, AuditCategories.REPORTS_DETAIL, Boolean.FALSE);
			}
		}
		else {
			String message =
				"Liferay Org ID: " + getUserOrgId(user) +
					" has not been setup properly for Report access in Report_Access and Report_Org_Owner tables.";

			logger.error("retriveReportResultByBlobIdAndBlobDate - " + message);

			auditMessageForReportDetailsAndDownloads(
				user, blobId, runDate, null, null, message, request, response,
				AuditCategories.REPORTS_DETAIL, Boolean.FALSE);
		}

		return reportMetadata;
	}

	/**
	 * Get the report blob data for view and download in text format
	 *
	 * @param User
	 * @param String
	 * @param String
	 * @param ResourceResponse
	 * @param ResourceRequest
	 * @return boolean
	 */
	@Transactional
	public boolean retriveReportBlobObjByBlobIdAndBlobDate(
		User user, String blobId, String runDate, ResourceResponse response,
		ResourceRequest request) {

		boolean isSuccess = true;
		String fileName = "";
		String reportName = "";
		String auditCategory = null;
		ReportBlob reportBlob = null;
		ReportMetadata reportMetadata = null;
		ZipInputStream zipInputStream = null;
		List<Set<Long>> ownerReportTypeidStr = findOwnerIdAndReportTypeId(user);

		if ((ownerReportTypeidStr != null) &&
			(ownerReportTypeidStr.get(
				0
			).size() > 0) &&
			(ownerReportTypeidStr.get(
				1
			).size() > 0)) {

			logger.debug(
				"retriveReportBlobObjByBlobIdAndBlobDate - Report Owner ids and Report Type Ids are:" +
					ownerReportTypeidStr.get(0) + " AND " +
						ownerReportTypeidStr.get(1));
			logger.debug(
				"retriveReportBlobObjByBlobIdAndBlobDate - Calling the service method to find the blob object from database at " +
					Calendar.getInstance(
					).getTime());
			reportMetadata =
				reportMetadataDao.retriveReportResultByBlobIdAndRunDate(
					ownerReportTypeidStr.get(0), ownerReportTypeidStr.get(1),
					blobId, runDate);

			logger.debug(
				"retriveReportBlobObjByBlobIdAndBlobDate - Finished calling the service method to find the " +
					"blob object from database at " +
						Calendar.getInstance(
						).getTime());

			if (reportMetadata != null) {
				reportBlob = reportMetadata.getReportBlob();
				fileName = reportMetadata.getFileName();
				reportName = reportMetadata.getReportType(
				).getName();

				logger.debug(
					"retriveReportBlobObjByBlobIdAndBlobDate - fileName is: " +
						fileName);
				logger.debug(
					"retriveReportBlobObjByBlobIdAndBlobDate - reportName is: " +
						reportName);

				try {
					OutputStream output = response.getPortletOutputStream();

					byte[] buf = new byte[1024];
					logger.debug(
						"retriveReportBlobObjByBlobIdAndBlobDate - Converting UNCOMPRESSED blob report data into  at inputStream" +
							Calendar.getInstance(
							).getTime());

					InputStream inputStream = reportBlob.getReportData(
					).getBinaryStream();
					logger.debug(
						"retriveReportBlobObjByBlobIdAndBlobDate - Finished converting UNCOMPRESSED " +
							"blob report data into  at inputStream" +
								Calendar.getInstance(
								).getTime());

					if (reportMetadata.getIsCompressed()) {
						logger.debug(
							"retriveReportBlobObjByBlobIdAndBlobDate - Converting ****COMPRESSED*** blob " +
								"report data into  at inputStream" +
									Calendar.getInstance(
									).getTime());
						zipInputStream = new ZipInputStream(
							new BufferedInputStream(inputStream));
						logger.debug(
							"retriveReportBlobObjByBlobIdAndBlobDate - FINISHED Converting ****COMPRESSED*** " +
								"blob report data into  at inputStream" +
									Calendar.getInstance(
									).getTime());
					}

					//TODO: Make the changes here.

					if (StringUtils.equalsIgnoreCase(
							reportMetadata.getFileType(), "pdf")) {

						response.setContentType("application/force-download");
						response.setProperty(
							"Content-Disposition",
							"attachment; filename=" + fileName);
						response.setProperty(
							"Set-Cookie", "fileDownload=true;path=/");
					}
					else {
						response.setContentType("text/plain; charset=UTF-8");
					}

					if (request.getParameter("type") != null) {
						if (request.getParameter(
								"type"
							).equalsIgnoreCase(
								"dw"
							)) {

							logger.debug(
								"retriveReportBlobObjByBlobIdAndBlobDate - Calling Download link ");
							response.setProperty(
								"Content-Disposition",
								"attachment; filename=" +
									reportMetadata.getFileName());
							response.setProperty(
								"Set-Cookie", "fileDownload=true;path=/");
							auditCategory =
								AuditCategories.REPORTS_DOWNLOAD_TEXT;
						}
						else if (request.getParameter(
									"type"
								).equalsIgnoreCase(
									"pdf"
								) &&
								 request.getParameter(
									 "convert"
								 ).equalsIgnoreCase(
									 "no"
								 )) {

							auditCategory =
								AuditCategories.REPORTS_DOWNLOAD_PDF;
						}
					}
					else {
						byte[] preStart = {'<', 'p', 'r', 'e', '>'};
						output.write(preStart);
						auditCategory = AuditCategories.REPORTS_VIEW;
					}

					int length = 0;

					if (reportMetadata.getIsCompressed()) {
						logger.info(
							"retriveReportBlobObjByBlobIdAndBlobDate - Writing ****COMPRESSED *** to screen " +
								Calendar.getInstance(
								).getTime());
						logger.debug(
							"retriveReportBlobObjByBlobIdAndBlobDate - writing from ZIP File");
						ZipEntry entry = zipInputStream.getNextEntry();

						if (entry != null) {
							byte[] buffer = new byte[1024];

							while ((length = zipInputStream.read(buffer)) > 0) {
								logger.debug(
									"retriveReportBlobObjByBlobIdAndBlobDate - output.write step");
								output.write(buffer, 0, length);
							}
						}

						logger.debug(
							"retriveReportBlobObjByBlobIdAndBlobDate - outside the while loop ");

						if ((request.getParameter("type") != null) &&
							request.getParameter(
								"type"
							).equalsIgnoreCase(
								"view"
							)) {

							byte[] preEnd = {'<', '/', 'p', 'r', 'e', '>'};
							output.write(preEnd);
						}

						logger.debug(
							"retriveReportBlobObjByBlobIdAndBlobDate - closing connections ");
						output.flush();
						output.close();
						zipInputStream.close();
						logger.debug(
							"retriveReportBlobObjByBlobIdAndBlobDate - END Reading from zip file");
					}
					else {
						logger.debug(
							"retriveReportBlobObjByBlobIdAndBlobDate - writing from text File");
						logger.debug(
							"retriveReportBlobObjByBlobIdAndBlobDate - Writing ****UNCOMPRESSED *** to screen " +
								Calendar.getInstance(
								).getTime());

						while ((inputStream != null) &&
							   ((length = inputStream.read(buf)) != -1)) {

							output.write(buf, 0, length);
						}

						if ((request.getParameter("type") != null) &&
							request.getParameter(
								"type"
							).equalsIgnoreCase(
								"view"
							)) {

							byte[] preEnd = {'<', '/', 'p', 'r', 'e', '>'};
							output.write(preEnd);
						}

						output.flush();
						output.close();
						closeStream(inputStream, "Closing report inputStream");

						logger.debug(
							"retriveReportBlobObjByBlobIdAndBlobDate - FINISHED Writing ****UNCOMPRESSED *** to screen " +
								Calendar.getInstance(
								).getTime());
					}
				}
				catch (Exception e) {
					isSuccess = false;
					logger.error(
						"[ReportServiceImpl.downloadReportFileBlob] EXCEPTION CAME ERROR MSG is " +
							e.getMessage(),
						e);
				}
			}
			else {
				if ((request.getParameter("type") != null) &&
					request.getParameter(
						"type"
					).equalsIgnoreCase(
						"dw"
					)) {

					auditCategory = AuditCategories.REPORTS_DOWNLOAD_TEXT;
				}
				else {
					auditCategory = AuditCategories.REPORTS_VIEW;
				}

				isSuccess = false;
			}//report metadata is null.

			if (isSuccess) {
				auditMessageForReportDetailsAndDownloads(
					user, blobId, runDate, fileName, reportName, null, request,
					response, auditCategory, Boolean.TRUE);
			}
			else {
				auditMessageForReportDetailsAndDownloads(
					user, blobId, runDate, fileName, reportName, null, request,
					response, auditCategory, Boolean.FALSE);

				response.setContentType("text/html");
				response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "404");
				response.setProperty("Set-Cookie", "fileDownload=true;path=/");
			}
		}

		return isSuccess;
	}

	/**
	 * same as retriveReportBlobObjByBlobIdAndBlobDate except it converts the file to the type defined by the "type" parameter
	 * @param User
	 * @param String
	 * @param String
	 * @param ResourceResponse
	 * @param ResourceRequest
	 * @param String type String defining the format to convert to. Current values are { "pdf" }
	 * @return boolean
	 */
	@Transactional
	public boolean retriveReportBlobWithConvert(
		User user, String blobId, String runDate, ResourceResponse response,
		ResourceRequest request, String type) {

		logger.debug(
			"retriveReportBlobWithConvert - start, blobId=" + blobId +
				", rundate=" + runDate);

		perfMon = PerformanceMonitorManager.getInstance();
		perfMon.start();
		boolean isSuccess = true;
		ReportBlob reportBlob = null;
		ReportMetadata reportMetadata = null;
		String reportFileName = "";
		String reportName = "";

		List<Set<Long>> ownerReportTypeidStr = findOwnerIdAndReportTypeId(user);
		ZipInputStream zipInputStream = null;

		if ((ownerReportTypeidStr != null) &&
			(ownerReportTypeidStr.get(
				0
			).size() > 0) &&
			(ownerReportTypeidStr.get(
				1
			).size() > 0)) {

			logger.debug(
				"retriveReportBlobWithConvert - User=" + user.getScreenName() +
					", reportOwnerIds=" + ownerReportTypeidStr.get(0) +
						", reportTypeIds=" + ownerReportTypeidStr.get(1));

			perfMon.start("retriveReportBlobWithConvert - retrieve blob");
			reportMetadata =
				reportMetadataDao.retriveReportResultByBlobIdAndRunDate(
					ownerReportTypeidStr.get(0), ownerReportTypeidStr.get(1),
					blobId, runDate);
			perfMon.stop("retriveReportBlobWithConvert - retrieve blob");

			if (reportMetadata != null) {
				reportBlob = reportMetadata.getReportBlob();
				reportFileName = reportMetadata.getFileName();
				reportName = reportMetadata.getReportType(
				).getName();

				try {
					byte[] buffer = new byte[bufferSize];

					String basename = FileUtil.basename(
						reportMetadata.getFileName());

					InputStream inputStream = reportBlob.getReportData(
					).getBinaryStream();

					OutputStream output = response.getPortletOutputStream();

					String tmpFilePrefix = basename;
					String tmpZipSourceFileSuffix = ".zip";
					String tmpSourceFileSuffix = ".txt";
					String tmpDestinationFileSuffix = ".pdf";
					File tmpSourceFile = File.createTempFile(
						tmpFilePrefix, tmpSourceFileSuffix);
					File tmpDestinationFile = File.createTempFile(
						tmpFilePrefix, tmpDestinationFileSuffix);

					String tmpSourceFilename = tmpSourceFile.getAbsolutePath();
					String tmpDestinationFilename =
						tmpDestinationFile.getAbsolutePath();

					logger.debug(
						"retriveReportBlobWithConvert - tmpSourceFile=" +
							tmpSourceFilename);
					logger.debug(
						"retriveReportBlobWithConvert - tmpDestinationFile=" +
							tmpDestinationFilename);

					logger.debug(
						"retriveReportBlobWithConvert - writing temporary blob data to " +
							tmpSourceFilename);
					int length = 0;

					if (reportMetadata.getIsCompressed()) {
						logger.debug(
							"[retriveReportBlobWithConvert ] inside the ZIP file option");
						perfMon.start(
							"retriveReportBlobWithConvert - creating zip inputStream in local drive");
						File tmpZipSourceFile = File.createTempFile(
							tmpFilePrefix, tmpZipSourceFileSuffix);

						BufferedOutputStream zipBoStream =
							new BufferedOutputStream(
								new FileOutputStream(tmpZipSourceFile));
						BufferedInputStream biStream = new BufferedInputStream(
							inputStream);

						while ((length = biStream.read(buffer)) != -1) {
							zipBoStream.write(buffer, 0, length);
						}

						zipBoStream.close();
						perfMon.stop(
							"retriveReportBlobWithConvert - creating zip inputStream in local drive");

						perfMon.start(
							"retriveReportBlobWithConvert - reading the zip inputStream from local drive to stream output");
						BufferedInputStream unzipStream =
							new BufferedInputStream(
								new FileInputStream(tmpZipSourceFile));
						BufferedOutputStream boStream =
							new BufferedOutputStream(
								new FileOutputStream(tmpSourceFile));

						zipInputStream = new ZipInputStream(unzipStream);
						ZipEntry entry = zipInputStream.getNextEntry();

						if (entry != null) {
							byte[] compressedBuffer = new byte[bufferSize];
							perfMon.start(
								"retriveReportBlobWithConvert - reading the zip inputStream from local drive to stream output - writing zipped decompressed output");

							while ((length = zipInputStream.read(
										compressedBuffer)) > 0) {

								logger.debug(
									"[retriveReportBlobWithConvert ] inside the ZIP file option : while loop");
								logger.debug(
									"[retriveReportBlobWithConvert ] output.write step");
								boStream.write(compressedBuffer, 0, length);
							}

							perfMon.stop(
								"retriveReportBlobWithConvert - reading the zip inputStream from local drive to stream output - writing zipped decompressed output");
						}

						boStream.close();

						perfMon.stop(
							"retriveReportBlobWithConvert - reading the zip inputStream from local drive to stream output");
					}
					else {
						perfMon.start(
							"retriveReportBlobWithConvert - creating inputSteam (this is not a compressed report)");
						BufferedInputStream biStream = new BufferedInputStream(
							inputStream);
						BufferedOutputStream boStream =
							new BufferedOutputStream(
								new FileOutputStream(tmpSourceFile));
						perfMon.stop(
							"retriveReportBlobWithConvert - creating inputSteam (this is not a compressed report)");

						perfMon.start(
							"retriveReportBlobWithConvert - writing output to disk (this is not a compressed report)");

						while ((length = biStream.read(buffer)) != -1) {
							boStream.write(buffer, 0, length);
						}

						perfMon.stop(
							"retriveReportBlobWithConvert - writing output to disk (this is not a compressed report)");
						boStream.close();
					}

					logger.debug(
						"retriveReportBlobWithConvert - completed writing to " +
							tmpSourceFilename);
					logger.debug(
						"retriveReportBlobWithConvert - converting " +
							tmpSourceFilename + " to pdf now");

					PDFWriter pdfWriter = new PDFWriter();

					String s =
						"retriveReportBlobWithConvert - converting " +
							tmpSourceFilename + " -> " + tmpDestinationFilename;

					perfMon.start(s);
					pdfWriter.convert(
						tmpSourceFilename, tmpDestinationFilename);
					perfMon.stop(s);

					logger.debug(
						"retriveReportBlobWithConvert - streaming " + basename +
							".pdf to portlet response OutputStream");
					InputStream pdfStream = new BufferedInputStream(
						new FileInputStream(tmpDestinationFile));

					response.setContentType("application/force-download");
					response.setProperty(
						"Content-Disposition",
						"attachment; filename=" + basename + ".pdf");
					response.setProperty(
						"Set-Cookie", "fileDownload=true;path=/");
					length = 0;

					s =
						"retriveReportBlobWithConvert - streaming " +
							tmpDestinationFilename + " back to browser";
					perfMon.start(s);

					while ((length = pdfStream.read(buffer)) != -1) {
						output.write(buffer, 0, length);
					}

					perfMon.stop(s);

					logger.debug(
						"retriveReportBlobWithConvert - flushing portlet OutputStream and tidying up temporary resources/files");
					s = "retriveReportBlobWithConvert - flushing output stream";
					perfMon.start(s);
					output.flush();
					perfMon.stop(s);

					s =
						"retriveReportBlobWithConvert - closing temp i/o streams";
					perfMon.start(s);

					closeStream(pdfStream, "Failed to close PDF InputStream");
					closeStream(
						output,
						"Failed to close response.getPortletOutputStream");
					closeStream(
						inputStream,
						"Failed to close binary (BLOB) stream from database");
					perfMon.stop(s);

					s = "retriveReportBlobWithConvert - deleting temp files";
					perfMon.start(s);
					logger.debug(
						"retriveReportBlobWithConvert - attempting to delete temporary files");

					if (!tmpDestinationFile.delete()) {
						logger.warn(
							"retriveReportBlobWithConvert - unable to delete temporary file " +
								tmpDestinationFile.getAbsolutePath());
					}

					if (!tmpSourceFile.delete()) {
						logger.warn(
							"retriveReportBlobWithConvert - unable to delete temporary file " +
								tmpSourceFile.getAbsolutePath());
					}

					perfMon.stop(s);
					logger.debug(
						"retriveReportBlobWithConvert - completed temporary file deletion");
				}
				catch (Exception e) {
					isSuccess = false;
					logger.error(
						"retriveReportBlobWithConvert - failed to convert/download blobId=" +
							blobId + ", runDate=" + runDate + ": " +
								e.getMessage(),
						e);
				}
			}
			else {
				isSuccess = false;
			} // if(reportMetadata != null){

			if (isSuccess) {
				auditMessageForReportDetailsAndDownloads(
					user, blobId, runDate, reportFileName, reportName, null,
					request, response, AuditCategories.REPORTS_DOWNLOAD_PDF,
					Boolean.TRUE);
			}
			else {
				auditMessageForReportDetailsAndDownloads(
					user, blobId, runDate, reportFileName, reportName, null,
					request, response, AuditCategories.REPORTS_DOWNLOAD_PDF,
					Boolean.FALSE);

				response.setContentType("text/html");
				response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "404");
				response.setProperty("Set-Cookie", "fileDownload=true;path=/");
			}
		}

		logger.debug(
			"retriveReportBlobWithConvert - end, isSuccess=" + isSuccess);

		perfMon.stopAll();
		perfMon.log();

		return isSuccess;
	}

	/**
	 * This method calls the web service to download encrypted files from database
	 * @param User
	 * @param String
	 * @param String
	 * @param ResourceResponse
	 * @param ResourceRequest
	 * @return boolean
	 */
	@Transactional
	public boolean retrieveEncryptedReportFromWSCall(
		User user, Header header, String blobId, String runDate,
		ResourceResponse response, ResourceRequest request) {

		logger.debug("retriveEncryptedReportFromWSCall - start");
		getCachedReportsMimeTypeMap();

		List<Set<Long>> ownerReportTypeidStr = findOwnerIdAndReportTypeId(user);

		String message = null;
		String auditCategory = "";

		ReportTransferRequest requestType = new ReportTransferRequest();

		List<Role> userRoles = null;

		logger.debug(
			"----------------------Header details start------------------");
		logger.debug("header user: " + header.getUserId());
		logger.debug("header user org: " + header.getUserOrgId());
		logger.debug("header user org name: " + header.getUserOrgName());
		logger.debug("header origin: " + header.getOrigin());
		logger.debug(
			"----------------------Header details end------------------");

		requestType.setHeader(header);
		requestType.setReportId(blobId);

		logger.debug(
			"----------------------Request details start------------------");
		logger.debug("request blob id: " + requestType.getReportId());

		try {
			OutputStream outStream = response.getPortletOutputStream();

			if (user != null) {
				try {
					//userRoles = user.getRoles();

					List<Role> roles = RoleLocalServiceUtil.getUserRoles(
						user.getUserId());

					logger.debug("user roles are   size is  " + roles.size());
					List<Role> groupRoles = findUserGroupRoleList(user);
					List<String> arrayList = Utility.makeRolesNameString(roles);
					List<String> groupRolesList = Utility.makeRolesNameString(
						groupRoles);

					arrayList.addAll(groupRolesList);
					logger.debug(
						"ALL user roles are   size is  " + roles.size());

					List<String> rolesRequest = requestType.getRoleNameList();

					rolesRequest.addAll(arrayList);
				}
				catch (Exception e) {
					logger.error(
						"retrieveEncryptedReportFromWSCall - Could not get userRoles " +
							e.getMessage(),
						e);

					response.setContentType("text/html");
					response.setProperty(
						ResourceResponse.HTTP_STATUS_CODE, "400");

					auditMessageForReportDetailsAndDownloads(
						header, blobId, runDate, null, null, e.getMessage(),
						request, response, AuditCategories.REPORTS_DOWNLOAD_CSV,
						Boolean.FALSE);

					return false;
				}
			}
			else {
				response.setContentType("text/html");
				response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "400");

				message = "User is null";

				auditMessageForReportDetailsAndDownloads(
					header, blobId, runDate, null, null, message, request,
					response, AuditCategories.REPORTS_DOWNLOAD_CSV,
					Boolean.FALSE);

				return false;
			}

			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			try {
				Date date = formatter.parse(runDate);

				String formattedDate = formatter.format(date);

				if (!StringUtils.equalsIgnoreCase(formattedDate, runDate)) {
					throw new ParseException("Dates do not match", 0);
				}

				requestType.setBlobDate(formattedDate);
				logger.debug("blob date is: " + requestType.getBlobDate());
				logger.debug(
					"----------------------Request details end------------------");
			}
			catch (ParseException e) {
				logger.error(
					"retriveEncryptedReportFromWSCall - Could not parse the String to date. " +
						e.getMessage(),
					e);
				response.setContentType("text/html");
				response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "400");

				auditMessageForReportDetailsAndDownloads(
					header, blobId, runDate, null, null, e.getMessage(),
					request, response, AuditCategories.REPORTS_DOWNLOAD,
					Boolean.FALSE);

				return false;
			}

			logger.debug("retrieveEncryptedReportFromWSCall - request ");

			ReportTransferResponse responseType = reportsService.getReportFile(
				requestType);

			if ((ownerReportTypeidStr != null) &&
				(ownerReportTypeidStr.get(
					0
				).size() > 0) &&
				(ownerReportTypeidStr.get(
					1
				).size() > 0)) {

				if (responseType != null) {
					if (responseType.getHeader() != null) {
						String statusCode = responseType.getHeader(
						).getStatus();

						logger.debug(
							"retrieveEncryptedReportFromWSCall - statusCode: " +
								statusCode);

						if (StringUtils.equalsIgnoreCase(
								Constants.SUCCESS, statusCode)) {

							if (StringUtils.isNotBlank(
									responseType.getFileType())) {

								if (cachedReportsMimeTypeMap.containsKey(
										responseType.getFileType(
										).toUpperCase())) {

									response.setContentType(
										cachedReportsMimeTypeMap.get(
											responseType.getFileType(
											).toUpperCase()));
								}
								else {
									logger.error(
										"retrieveEncryptedReportFromWSCall - Report " +
											blobId + " for date " +
												requestType.getBlobDate() +
													" returned a File Type " +
														responseType.
															getFileType() +
																"that is not defined in the properties file.");

									response.setContentType(
										"application/force-download");
									response.setProperty(
										"Content-Transfer-Encoding", "binary");
								}
							}
							else {
								logger.error(
									"retrieveEncryptedReportFromWSCall - Report " +
										blobId + " for date " +
											requestType.getBlobDate() +
												" does not have a File Type defined.");

								response.setContentType(
									"application/force-download");
								response.setProperty(
									"Content-Transfer-Encoding", "binary");
							}

							logger.debug(
								"retrieveEncryptedReportFromWSCall - report content type is: " +
									response.getContentType());

							ReportDataType report = responseType.getReport();

							DataHandler handler = report.getReportData();
							response.setProperty(
								"Content-Disposition",
								"attachment; filename=" +
									responseType.getFileName());
							response.setProperty(
								"Set-Cookie", "fileDownload=true;path=/");

							if (StringUtils.equalsIgnoreCase(
									"csv", responseType.getFileType())) {

								auditCategory =
									AuditCategories.
										REPORTS_DOWNLOAD_CSV_ENCRYPTED;
							}
							else if (StringUtils.equalsIgnoreCase(
										"txt", responseType.getFileType())) {

								auditCategory =
									AuditCategories.
										REPORTS_DOWNLOAD_TEXT_ENCRYPTED;
							}
							else if (StringUtils.equalsIgnoreCase(
										"pdf", responseType.getFileType())) {

								auditCategory =
									AuditCategories.
										REPORTS_DOWNLOAD_PDF_ENCRYPTED;
							}
							else {
								auditCategory =
									AuditCategories.REPORTS_DOWNLOAD_ENCRYPTED;
							}

							auditMessageForReportDetailsAndDownloads(
								header, blobId, runDate,
								responseType.getReportName(),
								responseType.getFileType(), null, request,
								response, auditCategory, Boolean.TRUE);

							handler.writeTo(outStream);
						}
						else {
							message =
								"WS returned " + statusCode + " for report " +
									blobId + " with date " + runDate;
							logger.error(message);

							response.setProperty(
								"Set-Cookie", "fileDownload=true;path=/");
							response.setContentType("text/html");

							if (StringUtils.equalsIgnoreCase(
									Constants.RECORD_NOT_FOUND, statusCode)) {

								response.setProperty(
									ResourceResponse.HTTP_STATUS_CODE, "404");
							}
							else if (StringUtils.equalsIgnoreCase(
										Constants.SERVICE_BUSY, statusCode)) {

								//Setting response to Network connect timeout error.
								response.setProperty(
									ResourceResponse.HTTP_STATUS_CODE, "599");
							}
							else {
								response.setProperty(
									ResourceResponse.HTTP_STATUS_CODE, "400");
							}

							auditMessageForReportDetailsAndDownloads(
								header, blobId, runDate,
								responseType.getReportName(),
								responseType.getFileType(),
								responseType.getHeader(
								).getMessage(),
								request, response,
								AuditCategories.REPORTS_DOWNLOAD,
								Boolean.FALSE);

							return false;
						} //Return status code is not success.
					}
					else {
						message = "WS Response header is null";
						logger.error(message);

						response.setContentType("text/html");
						response.setProperty(
							ResourceResponse.HTTP_STATUS_CODE, "400");

						auditMessageForReportDetailsAndDownloads(
							header, blobId, runDate, null, null, message,
							request, response, AuditCategories.REPORTS_DOWNLOAD,
							Boolean.FALSE);

						return false;
					} //Response Header is null
				}
				else {
					message = "WS response is null";
					logger.error(message);

					response.setContentType("text/html");
					response.setProperty(
						ResourceResponse.HTTP_STATUS_CODE, "400");

					auditMessageForReportDetailsAndDownloads(
						header, blobId, runDate, null, null, message, request,
						response, AuditCategories.REPORTS_DOWNLOAD,
						Boolean.FALSE);

					return false;
				} //WS Response is null
			}
		}
		catch (IOException e) {
			logger.error(
				"Could not get OutputStream from ResourceResponse " +
					e.getMessage(),
				e);

			response.setContentType("text/html");
			response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "400");

			auditMessageForReportDetailsAndDownloads(
				header, blobId, runDate, null, null, e.getMessage(), request,
				response, AuditCategories.REPORTS_DOWNLOAD, Boolean.FALSE);

			return false;
		}

		logger.debug("retriveEncryptedReportFromWSCall - End");

		return true;
	}

	/**
	 * Find  list of reports by dates
	 *
	 * @param User
	 * @param ReportForm
	 * @param ReportsPagingParameters
	 * @return ReportResultFormResults
	 */
	@Transactional
	public ReportResultFormResults findReportsListByDates(
		User user, ReportForm form, ReportsPagingParameters parameters,
		PortletRequest request, PortletResponse response) {

		ReportResultFormResults reportResultFormResults =
			new ReportResultFormResults();
		List<Set<Long>> ownerReportTypeidStr = findOwnerIdAndReportTypeId(user);
		long size = 0;

		if ((ownerReportTypeidStr != null) &&
			(ownerReportTypeidStr.get(
				0
			).size() > 0) &&
			(ownerReportTypeidStr.get(
				1
			).size() > 0)) {

			logger.debug(
				"[ReportServiceImpl.findReportsListByDates] : Report Owner ids and Report Type Ids are  when Date Filter called " +
					ownerReportTypeidStr.get(0) + " " +
						ownerReportTypeidStr.get(1));
			reportResultFormResults = reportMetadataDao.findReportsListByDates(
				ownerReportTypeidStr.get(0), ownerReportTypeidStr.get(1), form,
				parameters);

			if (reportResultFormResults != null) {
				if ((reportResultFormResults.getMetadatas() != null) &&
					(reportResultFormResults.getMetadatas(
					).size() > 0)) {

					size = reportResultFormResults.getTotalCount();
				}

				auditMessageForSearch(
					form, user, size, null, request, response, Boolean.FALSE,
					Boolean.TRUE);
			}
			else {
				auditMessageForSearch(
					form, user, size, null, request, response, Boolean.FALSE,
					Boolean.FALSE);
			}
		}
		else {
			String message =
				"Liferay Org ID: " + getUserOrgId(user) +
					" has not been setup properly for Report access in Report_Access and Report_Org_Owner tables.";

			logger.error("findReportsListByDates - " + message);

			auditMessageForSearch(
				form, user, size, message, request, response, Boolean.FALSE,
				Boolean.FALSE);
		}

		return reportResultFormResults;
	}

	/**
	 * Find  list of unique owner access id and report type id for a user
	 *
	 * @param 	user	<em>User</em>
	 * @return 	List<Set<Long>>
	 */
	@Transactional
	public List<Set<Long>> findOwnerIdAndReportTypeId(User user) {
		logger.debug(
			"findOwnerIdAndReportTypeId - start, user=" + user.getScreenName());

		List<Set<Long>> ownerRepTypeIds = new ArrayList<>();

		try {
			Long userId = user.getUserId();
			List<Role> groupRoles = findUserGroupRoleList(user);

			List<Organization> organizations =
				OrganizationLocalServiceUtil.getUserOrganizations(userId);

			if ((organizations != null) && (organizations.size() == 1)) {
				Long userOrgId = organizations.get(
					0
				).getOrganizationId();

				List<Role> roles = RoleLocalServiceUtil.getUserRoles(userId);

				List<String> arrayList = Utility.makeRolesNameString(roles);

				for (String test : arrayList) {
					logger.debug("findOwnerIdAndReportTypeId - role: " + test);
				}

				List<String> groupRolesList = Utility.makeRolesNameString(
					groupRoles);

				arrayList.addAll(groupRolesList);

				ownerRepTypeIds.add(findOwnerAccessIdStr(userOrgId));
				logger.debug(
					"findOwnerIdAndReportTypeId - after findOwnerAccessIdStr: " +
						ownerRepTypeIds);
				ownerRepTypeIds.add(findReportTypeIdsByRolesNames(arrayList));
				logger.debug(
					"findOwnerIdAndReportTypeId - after findReportTypeIdsByRolesNames: " +
						ownerRepTypeIds);

				logger.debug(
					"findOwnerIdAndReportTypeId - user=" +
						user.getScreenName() + ", roles=" + arrayList +
							", Orgnization=" +
								organizations.get(
									0
								).getName());
			}
			else {
				logger.warn(
					"findOwnerIdAndReportTypeId - user=" +
						user.getScreenName() + " has " +
							(organizations == null ? "null" :
								String.valueOf(organizations.size())) +
									" organisation(s) assigned");

				if (organizations != null) {
					for (Organization organization : organizations) {
						logger.debug(
							"findOwnerIdAndReportTypeId - user=" +
								user.getScreenName() + " is in organisation " +
									organization.getName());
					}
				}

				ownerRepTypeIds = null;
			}
		}
		catch (Exception e) {
			logger.error(
				"findOwnerIdAndReportTypeId - Exception: " + e.getMessage(), e);
		}

		logger.debug(
			"findOwnerIdAndReportTypeId - end, returning=" + ownerRepTypeIds);

		return ownerRepTypeIds;
	}

	/**
	 * Find list of owner access id by user organisation short name
	 *
	 * @param Long
	 * @return Set<Long>
	 */
	@Transactional
	public Set<Long> findOwnerAccessIdStr(Long userOrgId) {
		Set<Long> longs = new HashSet<>();

		longs = reportMetadataDao.findOwnerAccessIdStr(userOrgId);
		logger.debug(
			"[ReportServiceImpl.findOwnerAccessIdStr] : Report Access ids String Value : " +
				longs.toArray());

		return longs;
	}

	/**
	 * Find report type id list based on role name
	 *
	 * @param List<String>
	 * @return Set<Long>
	 */
	@Transactional
	public Set<Long> findReportTypeIdsByRolesNames(List<String> roles) {
		Set<Long> longs = new HashSet<>();

		longs = reportMetadataDao.findReportTypeIdsByRolesNames(roles);
		logger.debug(
			"[ReportServiceImpl.findReportTypeIdsByRolesNames] : Report Type id by Roles Name are  : " +
				longs);

		return longs;
	}

	/**
	 * Find user group role list
	 *
	 * @param User
	 * @return List<Role>
	 */
	public List<Role> findUserGroupRoleList(User user) {
		List<Role> finalRolesLst = new ArrayList<>();
		List<Role> rolesLst = null;

		try {
			List<UserGroup> userGrps = user.getUserGroups();

			for (UserGroup userGroup : userGrps) {
				rolesLst = new ArrayList<>();
				long groupId = userGroup.getGroup(
				).getGroupId();
				logger.debug(
					"[ReportServiceImpl.findUserGroupRoleList] : group id are    " +
						groupId);
				rolesLst = RoleLocalServiceUtil.getGroupRoles(groupId);
				logger.debug(
					"[ReportServiceImpl.findUserGroupRoleList] : group user roles size is  " +
						rolesLst.size());

				for (Role role : rolesLst) {
					finalRolesLst.add(role);
				}
			}

			logger.debug(
				"[ReportServiceImpl.findUserGroupRoleList] : ALL ROLES size is  " +
					finalRolesLst.size());
		}
		catch (Exception e) {
			logger.error(
				"findUserGroupRoleList - exception: " + e.getMessage(), e);
		}

		return finalRolesLst;
	}

	/**
	 * Close the given input stream
	 *
	 * @param OutputStream
	 * @param String
	 * @return void
	 */
	private void closeStream(InputStream inputStream, String errorMessage) {
		if (inputStream == null)

			return;

		try {
			inputStream.close();
		}
		catch (Exception e) {
			logger.warn(errorMessage, e);
		}
	}

	/**
	 * Close the given output stream
	 *
	 * @param OutputStream
	 * @param String
	 * @return void
	 */
	private void closeStream(OutputStream outputStream, String errorMessage) {
		if (outputStream == null)

			return;

		try {
			outputStream.close();
		}
		catch (Exception e) {
			logger.warn(errorMessage, e);
		}
	}

	/**
	 * Audit the search requests.
	 *
	 * @param form
	 * @param user
	 * @param size
	 * @param exceptionMessage
	 * @param request
	 * @param response
	 * @param isDefaultSearch
	 * @param success
	 */
	public void auditMessageForSearch(
		ReportForm form, User user, Long size, String exceptionMessage,
		PortletRequest request, PortletResponse response,
		Boolean isDefaultSearch, Boolean success) {

		XmlMessage xmlAuditMessage = new XmlMessage();
		String message = "";

		//Build the parameters section.
		xmlAuditMessage.addParameter("userid", user.getScreenName());
		xmlAuditMessage.addParameter("liferayOrgId", getUserOrgId(user));

		if ((form != null) && !isDefaultSearch) {
			xmlAuditMessage.addParameter("fromDate", form.getFromDate());
			xmlAuditMessage.addParameter("toDate", form.getToDate());

			if (StringUtils.isNotBlank(form.getCustomerName())) {
				xmlAuditMessage.addParameter(
					"customerName", form.getCustomerName());
			}

			if (StringUtils.isNotBlank(form.getReportName())) {
				xmlAuditMessage.addParameter(
					"reportName", form.getReportName());
			}

			if (StringUtils.isNotBlank(form.getCustomerBin())) {
				xmlAuditMessage.addParameter(
					"customerBin", form.getCustomerBin());
			}
		}
		else {
			xmlAuditMessage.addParameter("defaultSearch", "true");
		}

		//Build the result section.
		xmlAuditMessage.addResult("found", String.valueOf(size));

		//Add the exception message to the comments section.

		if (!success)
			xmlAuditMessage.addComment("error", exceptionMessage);

		message = xmlAuditMessage.toXml();

		if (success) {
			audit.success(
				response, request, AuditOrigin.PORTAL_ORIGIN,
				AuditCategories.REPORTS_SEARCH, message);
		}
		else {
			audit.fail(
				response, request, AuditOrigin.PORTAL_ORIGIN,
				AuditCategories.REPORTS_SEARCH, message);
		}
	}

	/**
	 * Audit the details and download requests for normal JDBC calls.
	 *
	 * @param user
	 * @param reportId
	 * @param date
	 * @param reportName
	 * @param exceptionMessage
	 * @param request
	 * @param response
	 * @param category
	 * @param success
	 */
	private void auditMessageForReportDetailsAndDownloads(
		User user, String reportId, String date, String reportName,
		String reportType, String exceptionMessage, PortletRequest request,
		PortletResponse response, String category, Boolean success) {

		XmlMessage xmlMessage = new XmlMessage();
		String origin = AuditOrigin.PORTAL_ORIGIN;

		//Build the parameters
		xmlMessage.addParameter("userId", user.getScreenName());
		xmlMessage.addParameter("liferayOrgId", getUserOrgId(user));

		writeAuditMessage(
			reportId, date, reportName, reportType, exceptionMessage, request,
			response, category, success, xmlMessage, origin);
	}

	/**
	 * Audit the details and download requests for WebService calls.
	 * @param header
	 * @param reportId
	 * @param date
	 * @param reportName
	 * @param exceptionMessage
	 * @param request
	 * @param response
	 * @param category
	 * @param success
	 */
	private void auditMessageForReportDetailsAndDownloads(
		Header header, String reportId, String date, String reportName,
		String reportType, String exceptionMessage, PortletRequest request,
		PortletResponse response, String category, Boolean success) {

		XmlMessage xmlMessage = new XmlMessage();
		String origin = AuditOrigin.PORTAL_ORIGIN;

		//Build the parameters
		xmlMessage = AuditWebServicesUtil.addHeaderElementsToXmlMessage(
			header, xmlMessage);

		writeAuditMessage(
			reportId, date, reportName, reportType, exceptionMessage, request,
			response, category, success, xmlMessage, origin);
	}

	/**
	 * Audit the details and download requests.
	 *
	 * @param reportId
	 * @param date
	 * @param reportName
	 * @param exceptionMessage
	 * @param request
	 * @param response
	 * @param category
	 * @param success
	 * @param xmlMessage
	 * @param origin
	 */
	private void writeAuditMessage(
		String reportId, String date, String reportName, String reportType,
		String exceptionMessage, PortletRequest request,
		PortletResponse response, String category, Boolean success,
		XmlMessage xmlMessage, String origin) {

		String message;
		xmlMessage.addParameter("reportId", reportId);
		xmlMessage.addParameter("runDate", date);

		//Build the result section.

		if (success) {
			xmlMessage.addResult("found", "1");
		}
		else {
			xmlMessage.addResult("found", "0");
		}

		if (StringUtils.isNotBlank(reportName)) {
			xmlMessage.addResult("reportName", reportName);
		}
		else {
			xmlMessage.addResult("reportName", "UNKNOWN");
		}

		if (StringUtils.isNotBlank(reportType)) {
			xmlMessage.addResult("reportType", reportType);
		}
		else {
			xmlMessage.addResult("reportType", "UNKNOWN");
		}

		if (StringUtils.isNotBlank(exceptionMessage))
			xmlMessage.addComment("error", exceptionMessage);

		message = xmlMessage.toXml();

		if (success) {
			audit.success(response, request, origin, category, message);
		}
		else {
			audit.fail(response, request, origin, category, message);
		}
	}

	/**
	 * Cache the MimeTypes for the reports.
	 *
	 * @return	Map<String, String>
	 */
	@Cacheable(value = Constants.MIME_TYPE_MAP)
	private Map<String, String> getCachedReportsMimeTypeMap() {
		logger.debug("getCachedReportsMimeTypeMap - start");

		if (cachedReportsMimeTypeMap == null) {
			cachedReportsMimeTypeMap = new HashMap<>();

			String[] reportMimeTypes = reportsFileMimeType.split(
				Constants.COMMA);

			for (String mimeTypes : reportMimeTypes) {
				String[] mimeType = mimeTypes.split(Constants.EQUALS);
				cachedReportsMimeTypeMap.put(
					mimeType[0].toUpperCase(), mimeType[1]);
			}
		}

		logger.debug("getCachedReportsMimeTypeMap - start");

		return cachedReportsMimeTypeMap;
	}

	/**
	 * Return the Organisation ID for the logged in user.
	 *
	 * @param 	user <em>User</em>
	 * @return	String
	 */
	private String getUserOrgId(User user) {
		String orgId = null;

		if (user != null) {
			try {
				List<Organization> orgs = user.getOrganizations();

				if (orgs.size() == 1) {
					orgId =
						orgs.get(
							0
						).getOrganizationId() + "";
				}
			}
			catch (PortalException e) {
				logger.error(
					"getUserOrgId - Portal exception getting organisations for user: " +
						user.getUserId() + " " + e.getMessage(),
					e);
			}
			catch (SystemException e) {
				logger.error(
					"getUserOrgId - System exception getting organisations for user: " +
						user.getUserId() + " " + e.getMessage(),
					e);
			}
		}

		return orgId;
	}

	public ReportsService getReportsService() {
		return reportsService;
	}

	public void setReportsService(ReportsService reportsService) {
		this.reportsService = reportsService;
	}

	public String getReportsFileMimeType() {
		return reportsFileMimeType;
	}

	public void setReportsFileMimeType(String reportsFileMimeType) {
		this.reportsFileMimeType = reportsFileMimeType;
	}

}