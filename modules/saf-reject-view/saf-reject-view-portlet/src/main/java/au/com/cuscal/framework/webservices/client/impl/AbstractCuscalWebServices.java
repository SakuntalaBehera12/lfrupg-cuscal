package au.com.cuscal.framework.webservices.client.impl;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;
import au.com.cuscal.framework.webservices.client.CuscalWebServices;
import au.com.cuscal.framework.webservices.pooling.WebServicePooling;

import com.germinus.easyconf.ComponentConfiguration;
import com.germinus.easyconf.ComponentProperties;
import com.germinus.easyconf.EasyConf;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public abstract class AbstractCuscalWebServices implements CuscalWebServices {

	public static final String ATM_SERVICE_URL_KEY = "webservice.atm.url";

	public static final String CARD_SERVICE_URL_KEY = "webservice.cards.url";

	public static final String COMMON_SERVICE_URL_KEY = "webservice.common.url";

	public static final String CONNECT_TIMEOUT_KEY =
		"webservice.connect.timeout";

	public static final String DEFAULT_COMPONENT_NAME = "webservice";

	public static final String DEFAULT_PROPERTY_FILE = "/webservice.properties";

	public static final String ENTITY_SERVICE_URL_KEY = "webservice.entity.url";

	public static final String PCT_SERVICE_URL_KEY = "webservice.pct.url";

	public static final String REQUEST_TIMEOUT_KEY =
		"webservice.request.timeout";

	public static final String SAF_SERVICE_URL_KEY = "webservice.saf.url";

	public static final String TRANSACTION_ARCHIVE_URL_KEY =
		"webservice.transactionArchive.url";

	public static final String TRANSACTION_SERVICE_URL_KEY =
		"webservice.transaction.url";

	public String getAtmServicesUrl() {
		return this.atmServicesUrl;
	}

	public String getCardServicesUrl() {
		return this.cardServicesUrl;
	}

	public String getCommonServicesUrl() {
		return this.commonServicesUrl;
	}

	public String getDefaultComponentName() {
		return this.defaultComponentName;
	}

	public String getEntityServicesUrl() {
		return this.entityServicesUrl;
	}

	public String getPctServicesUrl() {
		return this.pctServicesUrl;
	}

	public String getSafServicesUrl() {
		return this.safServicesUrl;
	}

	public String getTransactionArchiveUrl() {
		return this.transactionArchiveUrl;
	}

	public String getTransactionServicesUrl() {
		return this.transactionServicesUrl;
	}

	public WebServicePooling getWebServicePool() {
		return this.webServicePool;
	}

	public void loadProperties() throws IOException {
		loadProperties("/webservice.properties");
	}

	public void loadProperties(String propertyFilename) throws IOException {
		logger.debug("loadProperties - start");
		InputStream iStream = null;

		try {
			logger.info(
				"Initialising webservice urls from " + propertyFilename);
			iStream = CuscalSharedPropsUtil.getResourceStream(
				getClass(), propertyFilename);
			Properties p = new Properties();

			p.load(iStream);

			this.transactionServicesUrl = p.getProperty(
				"webservice.transaction.url");
			this.cardServicesUrl = p.getProperty("webservice.cards.url");
			this.entityServicesUrl = p.getProperty("webservice.entity.url");
			this.atmServicesUrl = p.getProperty("webservice.atm.url");
			this.pctServicesUrl = p.getProperty("webservice.pct.url");
			this.commonServicesUrl = p.getProperty("webservice.common.url");
			this.transactionArchiveUrl = p.getProperty(
				"webservice.transactionArchive.url");
			this.safServicesUrl = p.getProperty("webservice.saf.url");

			String connectTimeoutString = p.getProperty(
				"webservice.connect.timeout");

			if (StringUtils.isNotBlank(connectTimeoutString) &&
				StringUtils.isNumeric(connectTimeoutString)) {

				this.connectTimeout = Long.valueOf(
					connectTimeoutString
				).longValue();
			}

			String requestTimetouString = p.getProperty(
				"webservice.request.timeout");

			if (StringUtils.isNotBlank(requestTimetouString) &&
				StringUtils.isNumeric(requestTimetouString)) {

				this.requestTimeout = Long.valueOf(
					requestTimetouString
				).longValue();
			}

			logger.info(
				"Loaded webservice.transaction.url=" +
					this.transactionServicesUrl);

			logger.info("Loaded webservice.cards.url=" + this.cardServicesUrl);

			logger.info(
				"Loaded webservice.entity.url=" + this.entityServicesUrl);

			logger.info("Loaded webservice.pct.url=" + this.pctServicesUrl);
			logger.info("Loaded webservice.atm.url=" + this.atmServicesUrl);
			logger.info(
				"Loaded webservice.common.url=" + this.commonServicesUrl);

			logger.info(
				"Loaded webservice.transactionArchive.url=" +
					this.transactionArchiveUrl);

			logger.info("Loaded webservice.saf.url=" + this.safServicesUrl);

			this.propertiesLoaded = true;
		}
		finally {
			if (iStream != null) {
				iStream.close();
			}
		}

		logger.debug("loadProperties - end");
	}

	public void loadPropertiesEasyConf() throws IOException {
		loadPropertiesEasyConf(this.defaultComponentName);
	}

	public void loadPropertiesEasyConf(String componentName)
		throws IOException {

		logger.debug("loadProperties - start");

		ComponentConfiguration conf = EasyConf.getConfiguration(componentName);

		ComponentProperties p = conf.getProperties();

		this.transactionServicesUrl = p.getString("webservice.transaction.url");
		this.cardServicesUrl = p.getString("webservice.cards.url");
		this.entityServicesUrl = p.getString("webservice.entity.url");
		this.atmServicesUrl = p.getString("webservice.atm.url");
		this.pctServicesUrl = p.getString("webservice.pct.url");
		this.commonServicesUrl = p.getString("webservice.common.url");
		this.transactionArchiveUrl = p.getString(
			"webservice.transactionArchive.url");
		this.safServicesUrl = p.getString("webservice.saf.url");

		this.connectTimeout = p.getInt("webservice.connect.timeout");
		this.requestTimeout = p.getInt("webservice.request.timeout");

		logger.info(
			"Loaded webservice.transaction.url=" + this.transactionServicesUrl);

		logger.info("Loaded webservice.cards.url=" + this.cardServicesUrl);
		logger.info("Loaded webservice.entity.url=" + this.entityServicesUrl);

		logger.info("Loaded webservice.pct.url=" + this.pctServicesUrl);
		logger.info("Loaded webservice.atm.url=" + this.atmServicesUrl);
		logger.info("Loaded webservice.common.url=" + this.commonServicesUrl);

		logger.info(
			"Loaded webservice.transactionArchive.url=" +
				this.transactionArchiveUrl);

		logger.info("Loaded webservice.saf.url=" + this.safServicesUrl);

		this.propertiesLoaded = true;
		logger.debug("loadProperties - end");
	}

	public void logPoolStatistics() {
		this.webServicePool.logPoolStatistics();
	}

	public void purgePools() {
		logger.debug("purgePools - start");
		this.webServicePool.purgePools();
		logger.debug("purgePools - end");
	}

	public void setAtmServicesUrl(String atmServicesUrl) {
		this.atmServicesUrl = atmServicesUrl;
	}

	public void setCardServicesUrl(String cardServicesUrl) {
		this.cardServicesUrl = cardServicesUrl;
		initWebServicePools();
	}

	public void setCommonServicesUrl(String commonServicesUrl) {
		this.commonServicesUrl = commonServicesUrl;
		initWebServicePools();
	}

	public void setDefaultComponentName(String defaultComponentName) {
		this.defaultComponentName = defaultComponentName;
	}

	public void setEntityServicesUrl(String entityServicesUrl) {
		this.entityServicesUrl = entityServicesUrl;
		initWebServicePools();
	}

	public void setPctServicesUrl(String pctServicesUrl) {
		this.pctServicesUrl = pctServicesUrl;
	}

	public void setSafServicesUrl(String safServicesUrl) {
		this.safServicesUrl = safServicesUrl;
	}

	public void setTransactionArchiveUrl(String transactionArchiveUrl) {
		this.transactionArchiveUrl = transactionArchiveUrl;
	}

	public void setTransactionServicesUrl(String transactionServicesUrl) {
		this.transactionServicesUrl = transactionServicesUrl;
		initWebServicePools();
	}

	public void setWebServicePool(WebServicePooling webServicePool) {
		this.webServicePool = webServicePool;
	}

	protected void initWebServicePools() {
		if (!this.propertiesLoaded) {
			try {
				loadProperties();
			}
			catch (IOException e) {
				logger.error(
					"Failed to load configuration: " + e.getMessage(), e);
			}
		}

		this.webServicePool.setTransactionServicesUrl(
			this.transactionServicesUrl);
		this.webServicePool.setCardServicesUrl(this.cardServicesUrl);
		this.webServicePool.setEntityServicesUrl(this.entityServicesUrl);
		this.webServicePool.setCommonServicesUrl(this.commonServicesUrl);
		this.webServicePool.setPctServicesUrl(this.pctServicesUrl);
		this.webServicePool.setAtmServicesUrl(this.atmServicesUrl);
		this.webServicePool.setTransactionArchiveUrl(
			this.transactionArchiveUrl);
		this.webServicePool.setSafServicesUrl(this.safServicesUrl);

		this.webServicePool.setConnectTimeout(this.connectTimeout);
		this.webServicePool.setRequestTimeout(this.requestTimeout);
	}

	protected String atmServicesUrl = null;
	protected String cardServicesUrl = null;
	protected String commonServicesUrl = null;
	protected long connectTimeout = 0L;
	protected String entityServicesUrl = null;
	protected String pctServicesUrl = null;
	protected boolean propertiesLoaded = false;
	protected long requestTimeout = 0L;
	protected String safServicesUrl = null;
	protected String transactionArchiveUrl = null;
	protected String transactionServicesUrl = null;
	protected WebServicePooling webServicePool = new WebServicePooling();

	private static Logger logger = Logger.getLogger(
		AbstractCuscalWebServices.class);

	private String defaultComponentName = "webservice";

}