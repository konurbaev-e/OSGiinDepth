package org.konurbaev.auction.auditor.sealed;

import org.konurbaev.auction.Auction;
import org.konurbaev.auction.Participant;
import org.konurbaev.auction.spi.Auctioneer;
import org.konurbaev.auction.spi.Auditor;

public class SealedFirstPriceAuditor implements Auditor {

    public void onAcceptance(Auctioneer auctioneer, Participant participant,
                             String item, float ask,
                             float acceptedBid, Float[] bids) {
        verify(auctioneer, participant, bids);
    }

    public void onRejection(Auctioneer auctioneer, Participant participant,
                            String item, float ask, Float[] rejectedBids) {
        verify(auctioneer, participant, rejectedBids);
    }

    private void verify(Auctioneer auctioneer, Participant participant,
                        Float[] bids) {
        if ("Sealed-First-Price".equals(
                auctioneer.getAuctionProperties().get(Auction.TYPE))) {
            for (int i = 0; i < bids.length - 1; i++) {
                if ((bids[i + 1] - bids[i]) <= 1.0) {
                    System.out.println("Warning to '" + participant.getName()
                            + "': bids (" + bids[i] + ", "
                            + bids[i+1] + ") are too close together, possible disclosure may have happened");
                }
            }
        }
    }

}