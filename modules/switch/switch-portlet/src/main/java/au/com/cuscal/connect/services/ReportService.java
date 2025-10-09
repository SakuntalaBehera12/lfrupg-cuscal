package au.com.cuscal.connect.services;

import au.com.cuscal.connect.domain.ReportMetadata;
import au.com.cuscal.connect.forms.ReportForm;
import au.com.cuscal.connect.forms.ReportResultFormResults;
import au.com.cuscal.connect.forms.ReportsPagingParameters;
import au.com.cuscal.framework.webservices.Header;

import com.liferay.portal.kernel.model.User;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

public interface ReportService {

	public ReportResultFormResults findReportsListByDates(
		User user, ReportForm form, ReportsPagingParameters parameters,
		PortletRequest request, PortletResponse response);

	public boolean retrieveEncryptedReportFromWSCall(
		User user, Header header, String blobId, String runDate,
		ResourceResponse response, ResourceRequest request);

	public boolean retriveReportBlobObjByBlobIdAndBlobDate(
		User user, String blobId, String runDate, ResourceResponse response,
		ResourceRequest request);

	public boolean retriveReportBlobWithConvert(
		User user, String blobId, String runDate, ResourceResponse response,
		ResourceRequest request, String type);

	public ReportResultFormResults retriveReportResult(
		User user, ReportsPagingParameters parameters, PortletRequest request,
		PortletResponse response);

	public ReportMetadata retriveReportResultByBlobIdAndBlobDate(
		User user, String blobId, String runDate, PortletRequest request,
		PortletResponse response);

}