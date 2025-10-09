package au.com.cuscal.common.shared.props.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

import java.io.InputStream;

public class CuscalSharedPropsUtil {

	public static InputStream getResourceStream(
		Class clazz, String propertyFilename) {

		String path = propertyFilename;

		InputStream stream = clazz.getResourceAsStream(path);

		if (stream == null) {
			stream = _fallbackWithPortalClassLoader(path);
		}

		return stream;
	}

	public static InputStream getResourceStream(
		ClassLoader clazzLoader, String propertyFilename) {

		String path = propertyFilename;

		InputStream stream = clazzLoader.getResourceAsStream(path);

		if (stream == null) {
			stream = _fallbackWithPortalClassLoader(path);
		}

		return stream;
	}

	private static InputStream _fallbackWithPortalClassLoader(String path) {
		if ((path != null) && path.startsWith(StringPool.SLASH)) {
			path = path.substring(1);
		}

		return PortalClassLoaderUtil.getClassLoader(
		).getResourceAsStream(
			path
		);
	}

}