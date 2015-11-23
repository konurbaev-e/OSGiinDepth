package org.konurbaev.auction.buyer.http;

import javax.servlet.ServletException;

import org.osgi.framework.*;
import org.osgi.service.http.*;

import org.konurbaev.auction.Auction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuyerServletActivator implements BundleActivator, ServiceListener {

    private BundleContext bundleContext;
    private final BidderServlet bidderServlet = new BidderServlet("Http Bidder");
    private HttpService httpService;
    private final static Logger logger = LoggerFactory.getLogger(BuyerServletActivator.class);

    public void start(BundleContext bundleContext) throws Exception {
        logger.debug("BuyerServletActivator is starting...");

        this.bundleContext = bundleContext;

        String filter = "(&(objectClass=" + Auction.class.getName() + ")(" + Auction.TYPE + "=Sealed-First-Price))";

        ServiceReference[] serviceReferences = bundleContext.getServiceReferences(Auction.class.getName(), filter);

        if (serviceReferences != null) {
            start(serviceReferences[0]);
        } else {
            bundleContext.addServiceListener(this, filter);
        }
    }

    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeServiceListener(this);
        if (httpService != null) {
            httpService.unregister("/bidder");
        }
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        logger.debug("BuyerServletActivator.serviceChanged is starting...");
        try {
            switch (serviceEvent.getType()) {
                case ServiceEvent.REGISTERED: {
                    start(serviceEvent.getServiceReference());
                    break;
                }
                case ServiceEvent.UNREGISTERING: {
                    stop(bundleContext);
                    break;
                }
                default:
                    // ignore other service events
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start(ServiceReference serviceReference) throws ServletException, NamespaceException {
        logger.debug("BuyerServletActivator (private void start) is starting...");
        Auction auction = (Auction) bundleContext.getService(serviceReference);

        if (auction != null) {
            bidderServlet.setAuction(auction);

            ServiceReference ref = bundleContext.getServiceReference(HttpService.class.getName());

            httpService = (HttpService) bundleContext.getService(ref);
            httpService.registerServlet("/bidder", bidderServlet, null, null);
        }
    }
}