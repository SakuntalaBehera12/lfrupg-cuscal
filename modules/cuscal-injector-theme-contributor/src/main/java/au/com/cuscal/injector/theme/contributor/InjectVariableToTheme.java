package au.com.cuscal.injector.theme.contributor;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.*;
import com.liferay.portal.kernel.service.*;
import com.liferay.portal.kernel.service.permission.GroupPermissionUtil;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.template.TemplateContextContributor;
import com.liferay.portal.kernel.theme.NavItem;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.product.navigation.personal.menu.configuration.PersonalMenuConfigurationRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Nhep Samedi
 */
@Component(
	immediate = true,
	property = "type=" + TemplateContextContributor.TYPE_THEME,
	service = TemplateContextContributor.class
)
public class InjectVariableToTheme implements TemplateContextContributor {

	public static Layout getOrAddEmbeddedPersonalApplicationLayout(
			User user, Group group, boolean privateLayout)
		throws PortalException {

		try {
			return LayoutLocalServiceUtil.getFriendlyURLLayout(
				group.getGroupId(), privateLayout,
				PropsValues.CONTROL_PANEL_LAYOUT_FRIENDLY_URL);
		}
		catch (NoSuchLayoutException noSuchLayoutException) {

			// LPS-52675

			if (_log.isDebugEnabled()) {
				_log.debug(noSuchLayoutException, noSuchLayoutException);
			}

			return _addEmbeddedPersonalApplicationLayout(
				user.getUserId(), group.getGroupId(), privateLayout);
		}
	}

	@Override
	public void prepare(
		Map<String, Object> contextObjects,
		HttpServletRequest httpServletRequest) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (!shouldInject(themeDisplay)) {
			return;
		}

		List<NavItem> branchNavItems = getBranchNavItems(httpServletRequest);

		boolean showLeftNavigation = showLeftNavigation(branchNavItems);
		contextObjects.put("groupPermissionUtil", new GroupPermissionUtil());
		contextObjects.put("portalPermissionUtil", new PortalPermissionUtil());
		contextObjects.put("branchNavItems", branchNavItems);
		contextObjects.put("showLeftNavigation", showLeftNavigation);
	}

	private static Layout _addEmbeddedPersonalApplicationLayout(
			long userId, long groupId, boolean privateLayout)
		throws PortalException {

		String friendlyURL = FriendlyURLNormalizerUtil.normalize(
			PropsValues.CONTROL_PANEL_LAYOUT_FRIENDLY_URL);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAttribute(
			"layout.instanceable.allowed", Boolean.TRUE);

		Layout layout = LayoutLocalServiceUtil.addLayout(
			StringPool.BLANK, userId, groupId, privateLayout,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			PropsValues.CONTROL_PANEL_LAYOUT_NAME, StringPool.BLANK,
			StringPool.BLANK, LayoutConstants.TYPE_PORTLET, true, true,
			friendlyURL, serviceContext);

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		layoutTypePortlet.setLayoutTemplateId(
			userId, "1_column_dynamic", false);

		return LayoutLocalServiceUtil.updateLayout(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			layout.getTypeSettings());
	}

	private List<NavItem> getBranchNavItems(
		HttpServletRequest httpServletRequest) {

		try {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			Layout layout = themeDisplay.getLayout();

			if (layout.isRootLayout()) {
				return Collections.singletonList(
					new NavItem(
						httpServletRequest, themeDisplay, layout));
			}

			List<Layout> ancestorLayouts = layout.getAncestors();

			List<NavItem> navItems = new ArrayList<>(
				ancestorLayouts.size() + 1);

			for (int i = ancestorLayouts.size() - 1; i >= 0; i--) {
				Layout ancestorLayout = ancestorLayouts.get(i);

				navItems.add(
					new NavItem(
						httpServletRequest, themeDisplay, ancestorLayout));
			}

			navItems.add(
				new NavItem(httpServletRequest, themeDisplay, layout));

			return navItems;
		}
		catch (PortalException e) {
			_log.error(String.format("method=getBranchNavItems error=%s", e));

			return new ArrayList<>();
		}
	}

	private String[] getSupportedThemeIds() {
		return new String[] {
			"cuscaltheme_WAR_cuscaltheme",
			"cuscalthemeeftconnect_WAR_cuscalthemeeftconnect"
		};
	}

	private boolean shouldInject(ThemeDisplay themeDisplay) {
		return ArrayUtil.contains(
			getSupportedThemeIds(), themeDisplay.getThemeId());
	}

	private boolean showLeftNavigation(List<NavItem> branchNavItems) {
		try {
			if ((branchNavItems.size() > 1) ||
				((branchNavItems.size() == 1) &&
				 branchNavItems.iterator(
				 ).next(
				 ).hasChildren())) {

				return true;
			}

			return false;
		}
		catch (Exception e) {
			_log.error(String.format("method=showLeftNavigation error=%s", e));

			return false;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		InjectVariableToTheme.class);

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private PersonalMenuConfigurationRegistry _personalMenuConfigurationRegistry;

	@Reference
	private UserLocalService _userLocalService;

}