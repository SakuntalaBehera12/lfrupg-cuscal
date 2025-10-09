//
// Decompiled by Procyon v0.6-prerelease

//

package au.com.cuscal.vasco.events;

import au.com.cuscal.common.shared.props.util.CuscalSharedPropsUtil;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Nhep Samedi
 */
@Component(
	property = "key=servlet.service.events.pre", service = LifecycleAction.class
)
public class CustomPreServiceAction extends Action {

	@Override
	public void run(
			final HttpServletRequest req, final HttpServletResponse resp)
		throws ActionException {

		if ((req.getRequestURI() == null) ||
			!req.getRequestURI(
			).equalsIgnoreCase(
				"/c/portal/layout"
			)) {

			return;
		}

		try {
			final ThemeDisplay themeDisplay = (ThemeDisplay)req.getAttribute(
				WebKeys.THEME_DISPLAY);
			final String currentUrl = themeDisplay.getURLCurrent();
			final String currentLayoutUrl = themeDisplay.getLayout(
			).getRegularURL(
				req
			);
			final HttpSession session = req.getSession();

			if (currentLayoutUrl.equalsIgnoreCase(REDIRECT_URL)) {
				return;
			}

			final long plid = ParamUtil.getLong(req, "p_l_id");

			if (plid == 0L) {
				return;
			}

			final Layout layout = LayoutLocalServiceUtil.getLayout(plid);
			final boolean requireStepUp = GetterUtil.getBoolean(
				layout.getExpandoBridge(
				).getAttribute(
					CUSTOM_FIELD, false
				));

			if (!requireStepUp) {
				if (currentUrl.indexOf(currentLayoutUrl) != -1) {
					session.setAttribute("_CANCEL_URL", currentUrl);
				}

				return;
			}

			if (!themeDisplay.isSignedIn()) {
				String signinUrl = themeDisplay.getURLSignIn();

				if (signinUrl.indexOf("?") != -1) {
					signinUrl = signinUrl.substring(0, signinUrl.indexOf("?"));
				}

				resp.sendRedirect(signinUrl);

				return;
			}

			LayoutPermissionUtil.check(
				themeDisplay.getPermissionChecker(), layout, "VIEW");

			if (requireStepUp) {
				final String userSteppedUp =
					(session.getAttribute("_USER_STEPPEDUP") == null) ?
						"false" :
							session.getAttribute(
								"_USER_STEPPEDUP"
							).toString(
							).trim();

				if (!userSteppedUp.equalsIgnoreCase("true")) {
					final String userCancel =
						(session.getAttribute("USER_CANCEL") == null) ? "" :
							session.getAttribute(
								"USER_CANCEL"
							).toString(
							).trim();

					if (userCancel.equalsIgnoreCase("cancel")) {
						session.setAttribute("USER_CANCEL", (Object)null);

						if (session.getAttribute("_CANCEL_URL") == null) {
							session.setAttribute("_CANCEL_URL", "/");
						}

						resp.sendRedirect(
							(String)session.getAttribute("_CANCEL_URL"));
					}
					else {
						session.setAttribute("_RETURN_URL", currentUrl);
						resp.sendRedirect(REDIRECT_URL);
					}
				}
			}
		}
		catch (Exception e) {
			if (e instanceof PrincipalException) {
				throw new ActionException(e);
			}

			_log.error(
				"VascoHook: There was an exception trying to check vasco permission: " +
					e.getMessage(),
				e);
		}
	}

	@Activate
	protected void activate() {
		try {
			prop.load(
				CuscalSharedPropsUtil.getResourceStream(
					getClass().getClassLoader(), "vasco-hook.properties"));
			REDIRECT_URL = prop.getProperty(
				"redirect_url", "/2fa"
			).trim();
			CUSTOM_FIELD = prop.getProperty(
				"custom_field", "2FA_STEPUP_REQUIRED"
			).trim();
		}
		catch (Exception e) {
			_log.error(
				"VascoHook: Could not load vasco-hook.properties: " +
					e.getMessage(),
				e);
		}
	}

	private static String CUSTOM_FIELD;

	private static final String LIFERAY_PORTAL_LAYOUT = "/c/portal/layout";

	private static final String PROP_CUSTOM_FIELD = "custom_field";

	private static final String PROP_REDIRECT_URL = "redirect_url";

	private static String REDIRECT_URL;

	private static final String USER_CANCEL = "USER_CANCEL";

	private static final String USER_CANCEL_URL = "_CANCEL_URL";

	private static final String USER_RETURN_URL = "_RETURN_URL";

	private static final String USER_STEPPEDUP = "_USER_STEPPEDUP";

	private static Logger _log = Logger.getLogger(CustomPreServiceAction.class);

	private static final Properties prop = new Properties();

}