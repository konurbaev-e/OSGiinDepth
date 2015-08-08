package org.konurbaev.auction.seller.simple;

import org.osgi.framework.*;
import org.konurbaev.auction.*;

public class SellerActivator implements BundleActivator, ServiceListener {
    private BundleContext bundleContext;
    private Seller seller = new Seller("Seller 1");
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("SellerActivator is starting...");
        this.bundleContext = bundleContext;
        String filter =
                "(&(objectClass=" + Auction.class.getName()
                        + ")(" + Auction.TYPE
                        + "=Sealed-First-Price))";
        ServiceReference[] serviceReferences = bundleContext.getServiceReferences(Auction.class.getName(), filter);
        //ServiceReference<?>[] serviceReferences = bundleContext.getServiceReferences(null, filter);
        if (serviceReferences != null) {
            ask(serviceReferences[0]);
        } else {
            bundleContext.addServiceListener(this,
                    filter);
        }
    }
    public void serviceChanged(ServiceEvent
                                       serviceEvent) {
        System.out.println("SellerActivator.serviceChanged is starting...");
        switch (serviceEvent.getType()) {
            case ServiceEvent.REGISTERED: {
                ask(serviceEvent.getServiceReference());
                break;
            }
            default:
// do nothing
        }
    }
    private void ask(ServiceReference serviceReference) {
        System.out.println("SellerActivator.ask is starting...");
        Auction auction = (Auction)bundleContext.getService(serviceReference);
        if (auction != null) {
            seller.ask(auction);
            bundleContext.ungetService(serviceReference);
        }
    }
    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeServiceListener(this);
    }
}