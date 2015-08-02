package org.konurbaev.auction;

public interface Participant {
    String getName();
    void onAcceptance(Auction auction, String item, float price);
    void onRejection(Auction auction, String item, float bestBid);
}