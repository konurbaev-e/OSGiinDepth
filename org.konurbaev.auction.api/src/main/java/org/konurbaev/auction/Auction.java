package org.konurbaev.auction;

public interface Auction {
    /**
     * Value: String
     */
    String TYPE = "auction-type";
    /**
     * Value: Integer
     */
    String DURATION = "auction-duration";

    Float ask(String item, float price, Participant seller)
            throws InvalidOfferException;
    Float bid(String item, float price, Participant buyer)
            throws InvalidOfferException;
}