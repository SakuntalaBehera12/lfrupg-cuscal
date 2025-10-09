package au.com.cuscal.vasco;

import au.com.cuscal.vasco.events.CustomPreServiceAction;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.osgi.service.component.annotations.Component;

/**
 * @author Nhep Samedi
 *
 */
@Component(
	immediate = true, property = "key=login.events.post",
	service = LifecycleAction.class
)
public class InitSession implements LifecycleAction {

	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent)
		throws ActionException {

		HttpServletRequest request = lifecycleEvent.getRequest();

		final HttpSession session = request.getSession();
		logger.debug(
			"InitSession run - Before The session Attribute for  _USER_STEPPEDUP  " +
				session.getAttribute("_USER_STEPPEDUP"));

		session.setAttribute("_USER_STEPPEDUP", (Object)"false");
		logger.debug(
			"InitSession run - After The session Attribute for  _USER_STEPPEDUP  " +
				session.getAttribute("_USER_STEPPEDUP"));

		session.setAttribute("_RETURN_URL", (Object)"/");
	}

	private static Logger logger = Logger.getLogger(
		CustomPreServiceAction.class);

}