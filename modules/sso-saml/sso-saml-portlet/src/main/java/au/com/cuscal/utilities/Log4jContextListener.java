package au.com.cuscal.utilities;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.InputStream;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

public class Log4jContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {

		// do nothing

	}

	/**
	 * Initialize log4j when the application is being started
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {

			// initialize log4j here

			ServletContext context = event.getServletContext();

			String log4jConfigFile = context.getInitParameter(
				"log4j-config-location");

			Properties properties = new Properties();
			InputStream inStream = CuscalSharedPropsUtil.getResourceStream(
				getClass().getClassLoader(), log4jConfigFile);

			if (inStream != null) {
				properties.load(inStream);
				PropertyConfigurator.configure(properties);
			}
		}
		catch (Exception e) {
			logger.error(e);
		}
	}

	private static final Log logger = LogFactoryUtil.getLog(
		Log4jContextListener.class);

}