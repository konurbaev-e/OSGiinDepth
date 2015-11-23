package org.konurbaev.auction.manager;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.*;

import org.konurbaev.auction.Auction;
import org.konurbaev.auction.spi.Auctioneer;
import org.konurbaev.auction.spi.Auditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuctionManagerActivator implements BundleActivator, ServiceListener {

    private BundleContext bundleContext;
    private final Map<ServiceReference, ServiceRegistration> registeredAuctions = new HashMap<>();
    private final Map<ServiceReference, Auditor> registeredAuditors = new HashMap<>();
    private final static Logger logger = LoggerFactory.getLogger(AuctionManagerActivator.class);

    public void start(BundleContext bundleContext) throws Exception {
        logger.debug("AuctionManagerActivator is starting...");
        this.bundleContext = bundleContext;

        ServiceReference[] auctioneerReferences = bundleContext.getServiceReferences(Auctioneer.class.getName(),null);
        if (auctioneerReferences != null) {
            for (ServiceReference serviceReference : auctioneerReferences) {
                registerService(serviceReference);
            }
        }

        ServiceReference[] auditorReferences = bundleContext.getServiceReferences(Auditor.class.getName(),null);
        if (auditorReferences != null) {
            for (ServiceReference serviceReference : auditorReferences) {
                registerService(serviceReference);
            }
        }

        String auctionOrAuctioneerFilter = "(|" + "(objectClass=" + Auctioneer.class.getName() + ")" + "(objectClass=" + Auditor.class.getName() + ")" + ")";

        bundleContext.addServiceListener(this, auctionOrAuctioneerFilter);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        logger.debug("AuctionManagerActivator.serviceChanged is starting...");
        ServiceReference serviceReference = serviceEvent.getServiceReference();

        switch (serviceEvent.getType()) {
            case ServiceEvent.REGISTERED: {
                registerService(serviceReference);
                break;
            }
            case ServiceEvent.UNREGISTERING: {
                String [] serviceInterfaces = (String[]) serviceReference.getProperty("objectClass");
                if (Auctioneer.class.getName().equals(serviceInterfaces[0])) {
                    unregisterAuctioneer(serviceReference);
                } else {
                    unregisterAuditor(serviceReference);
                }
                bundleContext.ungetService(serviceReference);
                break;
            }
            default:
                // do nothing
        }
    }

    private void registerService(ServiceReference serviceReference) {
        logger.debug("AuctionManagerActivator.registerService is starting...");
        Object serviceObject = bundleContext.getService(serviceReference);

        if (serviceObject instanceof Auctioneer) {
            registerAuctioneer(serviceReference, (Auctioneer) serviceObject);
        } else {
            registerAuditor(serviceReference, (Auditor) serviceObject);
        }
    }

    private void registerAuditor(ServiceReference auditorServiceReference, Auditor auditor) {
        logger.debug("AuctionManagerActivator.registerAuditor is starting...");
        registeredAuditors.put(auditorServiceReference, auditor);
    }

    private void registerAuctioneer(ServiceReference auctioneerServiceReference,
                                    Auctioneer auctioneer) {
        logger.debug("AuctionManagerActivator.registerAuctioneer is starting...");
        Auction auction = new AuctionWrapper(auctioneer, registeredAuditors.values());

        ServiceRegistration auctionServiceRegistration = bundleContext.registerService(Auction.class.getName(),
                                                                                       auction,
                                                                                       auctioneer.getAuctionProperties());

        registeredAuctions.put(auctioneerServiceReference, auctionServiceRegistration);
    }

    private void unregisterAuditor(ServiceReference serviceReference) {
        logger.debug("AuctionManagerActivator.unregisterAuditor is starting...");
        registeredAuditors.remove(serviceReference);
    }

    private void unregisterAuctioneer(ServiceReference auctioneerServiceReference) {
        logger.debug("AuctionManagerActivator.unregisterAuctioneer is starting...");
        ServiceRegistration auctionServiceRegistration = registeredAuctions.remove(auctioneerServiceReference);

        if (auctionServiceRegistration != null) {
            auctionServiceRegistration.unregister();
        }
    }
}