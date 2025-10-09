package au.com.cuscal.transactions.services.client;

import au.com.cuscal.common.framework.dxp.service.request.core.CuscalServiceLocator;
import au.com.cuscal.framework.webservices.client.PCTService;
import au.com.cuscal.framework.webservices.pooling.WebServicePooling;
import au.com.cuscal.framework.webservices.transaction.AddPCTUserRequestType;
import au.com.cuscal.framework.webservices.transaction.AddPCTUserResponseType;
import au.com.cuscal.framework.webservices.transaction.DeletePCTUserRequestType;
import au.com.cuscal.framework.webservices.transaction.DeletePCTUserResponseType;
import au.com.cuscal.framework.webservices.transaction.DisablePCTUserRequestType;
import au.com.cuscal.framework.webservices.transaction.DisablePCTUserResponseType;
import au.com.cuscal.framework.webservices.transaction.EnablePCTUserRequestType;
import au.com.cuscal.framework.webservices.transaction.EnablePCTUserResponseType;
import au.com.cuscal.framework.webservices.transaction.FindPCTTerminalsRequestType;
import au.com.cuscal.framework.webservices.transaction.FindPCTTerminalsResponseType;
import au.com.cuscal.framework.webservices.transaction.FindPCTUsersRequestType;
import au.com.cuscal.framework.webservices.transaction.FindPCTUsersResponseType;
import au.com.cuscal.framework.webservices.transaction.GetPCTTerminalByPrimaryKeyRequestType;
import au.com.cuscal.framework.webservices.transaction.GetPCTTerminalByPrimaryKeyResponseType;
import au.com.cuscal.framework.webservices.transaction.GetPCTUserByIdRequestType;
import au.com.cuscal.framework.webservices.transaction.GetPCTUserByIdResponseType;
import au.com.cuscal.framework.webservices.transaction.GetPCTUserRequestType;
import au.com.cuscal.framework.webservices.transaction.GetPCTUserResponseType;
import au.com.cuscal.framework.webservices.transaction.ResetPCTUserPasswordRequestType;
import au.com.cuscal.framework.webservices.transaction.ResetPCTUserPasswordResponseType;
import au.com.cuscal.framework.webservices.transaction.UpdatePCTTerminalRequestType;
import au.com.cuscal.framework.webservices.transaction.UpdatePCTTerminalResponseType;
import au.com.cuscal.framework.webservices.transaction.UpdatePCTUserRequestType;
import au.com.cuscal.framework.webservices.transaction.UpdatePCTUserResponseType;
import au.com.cuscal.transactions.util.PCTServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component(value = "pctUserSearchService")
public class PctUserSearchServiceImpl implements PCTService {

	@Override
	public AddPCTUserResponseType pctAddUser(AddPCTUserRequestType addPCTUserRequestType) {
		return pctService.pctAddUser(addPCTUserRequestType);
	}

	@Override
	public UpdatePCTUserResponseType pctUpdateUser(UpdatePCTUserRequestType updatePCTUserRequestType) {
		return pctService.pctUpdateUser(updatePCTUserRequestType);
	}

	@Override
	public ResetPCTUserPasswordResponseType pctResetUserPassword(
			ResetPCTUserPasswordRequestType resetPCTUserPasswordRequestType) {

		return pctService.pctResetUserPassword(resetPCTUserPasswordRequestType);
	}

	@Override
	public DeletePCTUserResponseType pctDeleteUser(DeletePCTUserRequestType deletePCTUserRequestType) {
		return pctService.pctDeleteUser(deletePCTUserRequestType);
	}

	@Override
	public DisablePCTUserResponseType pctDisableUser(
			DisablePCTUserRequestType disablePCTUserRequestType) {

		return pctService.pctDisableUser(disablePCTUserRequestType);
	}

	@Override
	public EnablePCTUserResponseType pctEnableUser(
			EnablePCTUserRequestType enablePCTUserRequestType) {

		return pctService.pctEnableUser(enablePCTUserRequestType);
	}

	@Override
	public GetPCTUserResponseType pctGetUser(GetPCTUserRequestType getPCTUserRequestType) {
		return pctService.pctGetUser(getPCTUserRequestType);
	}

	@Override
	public GetPCTUserByIdResponseType pctGetUserById(
			GetPCTUserByIdRequestType getPCTUserByIdRequestType) {

		return pctService.pctGetUserById(getPCTUserByIdRequestType);
	}

	@Override
	public FindPCTUsersResponseType pctFindUsers(FindPCTUsersRequestType findPCTUsersRequestType) {
		return pctService.pctFindUsers(findPCTUsersRequestType);
	}

	@Override
	public FindPCTTerminalsResponseType pctFindTerminals(
			FindPCTTerminalsRequestType findPCTTerminalsRequestType) {

		return pctService.pctFindTerminals(findPCTTerminalsRequestType);
	}

	@Override
	public GetPCTTerminalByPrimaryKeyResponseType pctGetTerminal(
			GetPCTTerminalByPrimaryKeyRequestType getPCTTerminalByPrimaryKeyRequestType) {

		return pctService.pctGetTerminal(getPCTTerminalByPrimaryKeyRequestType);
	}

	@Override
	public UpdatePCTTerminalResponseType pctUpdateTerminal(
			UpdatePCTTerminalRequestType updatePCTTerminalRequestType) {

		return pctService.pctUpdateTerminal(updatePCTTerminalRequestType);
	}

	@Override
	public String getTransactionServicesUrl() {
		return pctService.getTransactionServicesUrl();
	}

	@Override
	public void setTransactionServicesUrl(String s) {
		pctService.setTransactionServicesUrl(s);
	}

	@Override
	public String getCommonServicesUrl() {
		return pctService.getCommonServicesUrl();
	}

	@Override
	public void setCommonServicesUrl(String s) {
		pctService.setCommonServicesUrl(s);
	}

	@Override
	public String getEntityServicesUrl() {
		return pctService.getEntityServicesUrl();
	}

	@Override
	public void setEntityServicesUrl(String s) {
		pctService.setEntityServicesUrl(s);
	}

	@Override
	public String getCardServicesUrl() {
		return pctService.getCardServicesUrl();
	}

	@Override
	public void setCardServicesUrl(String s) {
		pctService.setCardServicesUrl(s);
	}

	@Override
	public String getAtmServicesUrl() {
		return pctService.getAtmServicesUrl();
	}

	@Override
	public void setAtmServicesUrl(String s) {
		pctService.setAtmServicesUrl(s);
	}

	@Override
	public String getSafServicesUrl() {
		return pctService.getSafServicesUrl();
	}

	@Override
	public void setSafServicesUrl(String s) {
		pctService.setSafServicesUrl(s);
	}

	@Override
	public WebServicePooling getWebServicePool() {
		return pctService.getWebServicePool();
	}

	@Override
	public void setWebServicePool(WebServicePooling webServicePooling) {
		pctService.setWebServicePool(webServicePooling);
	}

	@Override
	public void purgePools() {
		pctService.purgePools();
	}

	@Override
	public void logPoolStatistics() {
		pctService.logPoolStatistics();
	}

	@Override
	public void loadProperties() throws IOException {
		pctService.loadProperties();
	}

	@Override
	public void loadProperties(String s) throws IOException {
		pctService.loadProperties(s);
	}

	@Override
	public String getSchemeServicesUrl() {
		return pctService.getSchemeServicesUrl();
	}

	@Override
	public void setSchemeServicesUrl(String s) {
		pctService.setSchemeServicesUrl(s);
	}

	@Autowired
	@Value("${webservice.pct.url}")
	protected String pctServicesUrl = null;

	private final PCTService pctService = CuscalServiceLocator.getService(
		PCTService.class.getName());

}