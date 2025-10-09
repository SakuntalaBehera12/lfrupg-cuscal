package au.com.cuscal.common.framework.dxp.service.request.core;

import com.liferay.portal.kernel.module.util.SystemBundleUtil;

public class CuscalServiceLocator {

    public static <T> T getService(String serviceName) {
        return (T) SystemBundleUtil.callService(serviceName, obj -> obj);
    }

}
