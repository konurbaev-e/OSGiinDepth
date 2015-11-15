package org.konurbaev.auction.seller.simple;

import org.konurbaev.auction.spi.Auctioneer;
import org.osgi.framework.*;
import org.konurbaev.auction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SellerActivator implements BundleActivator, ServiceListener {
    private BundleContext bundleContext;
    private Seller seller = new Seller("Seller 1");
    private final static Logger logger = LoggerFactory.getLogger(Seller.class);

    public void start(BundleContext bundleContext) throws Exception {
        logger.debug("SellerActivator is starting...");
        this.bundleContext = bundleContext;
        String filter = "(&(objectClass=" + Auctioneer.class.getName() + ")(" + Auction.TYPE + "=Sealed-First-Price))";
        ServiceReference[] serviceReferences = bundleContext.getServiceReferences(Auctioneer.class.getName(), filter);
        if (serviceReferences != null) {
            ask(serviceReferences[0]);
        } else {
            bundleContext.addServiceListener(this, filter);
        }
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        logger.debug("SellerActivator.serviceChanged is starting...");
        switch (serviceEvent.getType()) {
            case ServiceEvent.REGISTERED: {
                ask(serviceEvent.getServiceReference());
                break;
            }
            default:
        }
    }
    private void ask(ServiceReference serviceReference) {
        logger.debug("SellerActivator.ask is starting...");
        Auctioneer auctioneer = (Auctioneer)bundleContext.getService(serviceReference);
        if (auctioneer != null) {
            seller.ask(auctioneer);
            bundleContext.ungetService(serviceReference);
        }
    }
    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeServiceListener(this);
    }
}