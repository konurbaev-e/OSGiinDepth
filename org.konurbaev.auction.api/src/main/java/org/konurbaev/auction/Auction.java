package org.konurbaev.auction;

public interface Auction {
    String TYPE = "auction-type";
    String DURATION = "auction-duration";

    Float ask(String item, float price, Participant seller) throws InvalidOfferException;
    Float bid(String item, float price, Participant buyer) throws InvalidOfferException;
}