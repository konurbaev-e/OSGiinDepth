package org.konurbaev.auction.auctioneer.sealed;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.konurbaev.auction.Auction;
import org.konurbaev.auction.InvalidOfferException;
import org.konurbaev.auction.Participant;

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

        SealedFirstPriceAuction auction = new SealedFirstPriceAuction("1");

        MockParticipant participant = new MockParticipant();

        auction.ask("book", new Float(50.0), participant);
        auction.bid("book", new Float(50.0), participant);

        Assert.assertEquals(2, participant.numberOfAcceptances);
    }

}