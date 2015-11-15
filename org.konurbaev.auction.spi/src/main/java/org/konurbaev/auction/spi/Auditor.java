package org.konurbaev.auction.spi;

import org.konurbaev.auction.Participant;

import java.util.List;

public interface Auditor {

    void onAcceptance(Auctioneer auctioneer,
                      Participant participant,
                      String item,
                      float ask,
                      float acceptedBid,
                      List<Float> bids);

    void onRejection(Auctioneer auctioneer,
                     Participant participant,
                     String item,
                     float ask,
                     List<Float> rejectedBids);
}