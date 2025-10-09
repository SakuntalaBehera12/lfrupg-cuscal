package au.com.cuscal.common.framework.dxp.service.request.core;

import org.osgi.framework.ServiceRegistration;

public class AbstractSelfRegisteringService implements SelfRegisteringService {

    @Override
    public void setServiceRegistration(ServiceRegistration serviceRegistration) {
        _serviceRegistration = serviceRegistration;
    }
    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }


    private String beanName;
    private ServiceRegistration _serviceRegistration;

}
