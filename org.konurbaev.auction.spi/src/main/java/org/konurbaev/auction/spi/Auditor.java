package org.konurbaev.auction.spi;

import org.konurbaev.auction.Participant;

public interface Auditor {
    void onAcceptance(Auctioneer auctioneer, Participant participant,
                      String item, float ask,
                      float acceptedBid, Float [] bids);
    void onRejection(Auctioneer auctioneer, Participant participant,
                     String item, float ask,
                     Float [] rejectedBids);
}