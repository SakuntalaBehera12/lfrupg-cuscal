package au.com.cuscal.common.framework.dxp.service.request.core;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.ModuleFrameworkPropsValues;
import com.liferay.portal.kernel.util.ProxyUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

public interface SelfRegisteringService extends InitializingBean, BeanNameAware {

    String getBeanName();

    void setServiceRegistration(final ServiceRegistration serviceRegistration);

    default void afterPropertiesSet() throws Exception {
        registerSelf();
    }

    default void registerSelf() throws InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {
        setServiceRegistration(registerService(getBeanName(), this));
    }

    default ServiceRegistration registerService(
            final String beanName, final Object service)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Bundle bundle = FrameworkUtil.getBundle(service.getClass());
        BundleContext bundleContext = bundle.getBundleContext();

        Class<?> clazz = service.getClass();

        if (ProxyUtil.isProxyClass(clazz)) {
            InvocationHandler invocationHandler = ProxyUtil.getInvocationHandler(service);
            Class<?> invocationHandlerClass = invocationHandler.getClass();

            Method method = invocationHandlerClass.getMethod("getTarget");
            Object target = method.invoke(invocationHandler);
            clazz = target.getClass();
        }

        Set<String> names = interfaceNames(
            service, ModuleFrameworkPropsValues.MODULE_FRAMEWORK_SERVICES_IGNORED_INTERFACES);

        if (names.isEmpty()) {
            return null;
        }

        HashMapDictionary<String, Object> properties =
                HashMapDictionaryBuilder.<String, Object>put("bean.id", beanName).put(
                        "origin.bundle.symbolic.name",
                        bundle::getSymbolicName
                ).build();

        return bundleContext.registerService(names.toArray(new String[0]),
                service, properties);
    }

    default Set<String> interfaceNames(
            Object object, String[] ignoredInterfaceNames) {

        Set<String> interfaceNames = new LinkedHashSet<>();

        Queue<Class<?>> queue = new ArrayDeque<>();

        queue.add(object.getClass());

        while (!queue.isEmpty()) {
            Class<?> clazz = queue.remove();

            for (Class<?> interfaceClass : clazz.getInterfaces()) {
                optionallyCollectInterface(
                        interfaceClass, interfaceNames,
                        ignoredInterfaceNames);

                queue.add(interfaceClass);
            }

            clazz = clazz.getSuperclass();

            if (clazz != null) {
                if (clazz.isInterface()) {
                    optionallyCollectInterface(
                            clazz, interfaceNames, ignoredInterfaceNames);
                }

                queue.add(clazz);
            }
        }


        optionallyCollectInterface(
                object.getClass(), interfaceNames, ignoredInterfaceNames);

        return interfaceNames;
    }

    default void optionallyCollectInterface(
        Class<?> clazz, Set<String> interfaceNames, String[] ignoredInterfaceNames) {

        String interfaceClassName = clazz.getName();

        for (String ignoredInterface : ignoredInterfaceNames) {
            if (!ignoredInterface.startsWith(StringPool.EXCLAMATION) &&
                    (ignoredInterface.equals(interfaceClassName) ||
                            (ignoredInterface.endsWith(StringPool.STAR) &&
                                    interfaceClassName.regionMatches(
                                            0, ignoredInterface, 0,
                                            ignoredInterface.length() - 1)))) {
                return;
            }
        }

        interfaceNames.add(clazz.getName());
    }

}

