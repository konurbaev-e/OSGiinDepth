package org.konurbaev.auction.auctioneer.sealed;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.konurbaev.auction.Auction;
import org.konurbaev.auction.InvalidOfferException;
import org.konurbaev.auction.Participant;
import org.konurbaev.auction.spi.Auctioneer;

public class TestSealedFirstPriceAuction {

    static class MockParticipant implements Participant {

        int numberOfAcceptances = 0;

        public String getName() {
            return "participant";
        }

        @Override
        public void onAcceptance(Auction auction, String item, float price) {
            numberOfAcceptances+=1;
        }

        @Override
        public void onRejection(Auction auction, String item, float bestBid) {
            Assert.fail("Rejected");
        }

    }

    @Test
    public void testSingleBidAsk() throws InvalidOfferException {

        SealedFirstPriceAuction auction = new SealedFirstPriceAuction(1);

        MockParticipant participant = new MockParticipant();

        auction.ask("book", new Float(50.0), participant);
        auction.bid("book", new Float(50.0), participant);

        Assert.assertEquals(2, participant.numberOfAcceptances);
    }

    @Test
    public void testAuctionnerActivator() throws Exception {

        BundleContext bundleContext = mock(BundleContext.class);
        when(bundleContext.getServiceReference(Auctioneer.class.getName())).thenReturn(mock(ServiceReference.class));

        SealedFirstPriceAuctioneerActivator activator =  new SealedFirstPriceAuctioneerActivator();

        activator.start(bundleContext);

        ServiceReference reference = bundleContext.getServiceReference(Auctioneer.class.getName());

        Assert.assertNotNull(reference);
    }

}