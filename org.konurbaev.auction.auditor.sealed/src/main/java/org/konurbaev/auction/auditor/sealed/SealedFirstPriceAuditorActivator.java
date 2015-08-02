package org.konurbaev.auction.auditor.sealed;

import org.osgi.framework.*;
import org.konurbaev.auction.spi.Auditor;

public class SealedFirstPriceAuditorActivator implements BundleActivator {

    private ServiceRegistration serviceRegistration;

    public void start(BundleContext bundleContext) throws Exception {
        serviceRegistration =
                bundleContext.registerService(
                        Auditor.class.getName(),
                        new SealedFirstPriceAuditor(), null);
    }
    public void stop(BundleContext bundleContext) throws Exception {
        serviceRegistration.unregister();
    }
}