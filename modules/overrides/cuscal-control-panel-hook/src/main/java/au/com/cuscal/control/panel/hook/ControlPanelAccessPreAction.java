package au.com.cuscal.control.panel.hook;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.InputStream;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.osgi.service.component.annotations.Component;

/**
 * @author Nhep Samedi
 */
// this can be achieved via OOTB roles

@Component(
	property = "key=servlet.service.events.pre", service = LifecycleAction.class
)
public class ControlPanelAccessPreAction extends Action {

	@Override
	public void run(
			final HttpServletRequest request,
			final HttpServletResponse response)
		throws ActionException {

		try {
			final ThemeDisplay themeDisplay =
				(ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);

			if (Validator.isNull(themeDisplay) || !themeDisplay.isSignedIn()) {
				return;
			}

			if (!_isControlPanelGroup(themeDisplay)) {
				return;
			}

			final User currentUser = themeDisplay.getUser();

			final String[] controlPanelRoles = _getAllowedControlPanelRoles();

			if (!RoleServiceUtil.hasUserRoles(
					currentUser.getUserId(), currentUser.getCompanyId(),
					controlPanelRoles, true)) {

				_log.info(
					String.format(
						"User %s is not allowed to access the control panel.",
						currentUser.getScreenName()));

				throw new PrincipalException(
					String.format(
						"User %s cannot access the control panel.",
						request.getRemoteUser()));
			}
		}
		catch (Exception ex) {
			throw new ActionException(ex);
		}
	}

	private String[] _getAllowedControlPanelRoles() {
		if (_allowedControlPanelRoles != null) {
			return _allowedControlPanelRoles;
		}

		try {
			ClassLoader classLoader =
				ControlPanelAccessPreAction.class.getClassLoader();

			InputStream resourceAsStream =
				CuscalSharedPropsUtil.getResourceStream(
					classLoader, _CONTROL_PANEL_HOOK_PROPERTIES);

			Properties properties = PropertiesUtil.load(
				resourceAsStream, StringPool.UTF8);

			String allowedRolesProp = properties.getProperty(
				_PROP_ALLOWED_CONTROL_PANEL_ROLES, ""
			).trim();

			_allowedControlPanelRoles = Arrays.stream(
				allowedRolesProp.split(",")
			).map(
				String::trim
			).toArray(
				String[]::new
			);
		}
		catch (Exception e) {
			_log.error(
				String.format(
					"ControlPanelHook: Error while getting Control Panel roles " +
						"from control-panel-hook.properties: %s",
					e.getMessage()),
				e);

			return new String[0];
		}

		return _allowedControlPanelRoles;
	}

	private boolean _isControlPanelGroup(final ThemeDisplay themeDisplay)
		throws PortalException {

		long groupId = themeDisplay.getLayout(
		).getGroupId();

		if (_controlPanelGroupIds.contains(groupId)) {
			return true;
		}

		boolean isControlPanel = GroupLocalServiceUtil.getGroup(
			groupId
		).isControlPanel();

		if (isControlPanel) {
			_controlPanelGroupIds.add(groupId);
		}

		return isControlPanel;
	}

	private static final String _CONTROL_PANEL_HOOK_PROPERTIES =
		"control-panel-hook.properties";

	private static final String _PROP_ALLOWED_CONTROL_PANEL_ROLES =
		"allowed.control.panel.roles";

	private static final Logger _log = Logger.getLogger(
		ControlPanelAccessPreAction.class);

	private static String[] _allowedControlPanelRoles = null;
	private static List<Long> _controlPanelGroupIds =
		new CopyOnWriteArrayList<>();

}