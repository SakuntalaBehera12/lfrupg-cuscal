package au.com.cuscal.cards.services.client;

import au.com.cuscal.service.mobile.v1_0.DeActivateDevicePortal;
import au.com.cuscal.service.mobile.v1_0.DeActivateDevicePortalResponse;
import au.com.cuscal.service.mobile.v1_0.GetCardControls;
import au.com.cuscal.service.mobile.v1_0.GetCardControlsPortalResponse;
import au.com.cuscal.service.mobile.v1_0.GetDeviceListPortal;
import au.com.cuscal.service.mobile.v1_0.GetDeviceListPortalResponse;
import au.com.cuscal.service.mobile.v1_0.GetPanActivityRequest;
import au.com.cuscal.service.mobile.v1_0.GetPanActivityResponse;
import au.com.cuscal.service.mobile.v1_0.PortalServices;
import au.com.cuscal.service.mobile.v1_0.UpdateCardControlsPortal;
import au.com.cuscal.service.mobile.v1_0.UpdateCardControlsPortalResponse;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.springframework.stereotype.Component;

@Component(value = "cuscalApiService")
public class LocalCuscalApiServiceImpl implements PortalServices {

	public LocalCuscalApiServiceImpl() {
	}

	public LocalCuscalApiServiceImpl(String portalServiceUrl)
		throws MalformedURLException {

		System.setProperty(
			"javax.xml.ws.spi.Provider",
			"org.apache.cxf.jaxws.spi.ProviderImpl"
		);

		QName qName = new QName(
			"http://V1_0.mobile.service.cuscal.com.au/",
			"PortalServicesService"
		);

		URL wsdlURL = new URL(portalServiceUrl.split("\\?")[0]);

		Service service = Service.create(wsdlURL, qName);

		portalServices = service.getPort(PortalServices.class);
	}

	@Override
	public DeActivateDevicePortalResponse deActivateDevicePortal(
		DeActivateDevicePortal request) {

		return portalServices.deActivateDevicePortal(request);
	}

	@Override
	public GetCardControlsPortalResponse getCardControls(
		GetCardControls request) {

		return portalServices.getCardControls(request);
	}

	@Override
	public GetDeviceListPortalResponse getDeviceListPortal(
		GetDeviceListPortal request) {

		return portalServices.getDeviceListPortal(request);
	}

	@Override
	public GetPanActivityResponse getPanActivity(
		GetPanActivityRequest request) {

		return portalServices.getPanActivity(request);
	}

	@Override
	public UpdateCardControlsPortalResponse updateCardControlsPortal(
		UpdateCardControlsPortal request) {

		return portalServices.updateCardControlsPortal(request);
	}

	private PortalServices portalServices;

}