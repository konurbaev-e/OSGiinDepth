package org.konurbaev.auction.auctioneer.sealed;

import java.util.*;
import org.konurbaev.auction.Auction;
import org.konurbaev.auction.spi.Auctioneer;

public class SealedFirstPriceAuctioneer implements Auctioneer {
    private static final String SEALED_FIRST_PRICE = "Sealed-First-Price";
    private final int DURATION = 3;
    private final Dictionary<String, Object> properties =
            new Hashtable<String, Object>();
    private final Auction auction;
    public SealedFirstPriceAuctioneer() {
        properties.put(Auction.TYPE, SEALED_FIRST_PRICE);
        properties.put(Auction.DURATION, DURATION);
        auction = new SealedFirstPriceAuction(DURATION);
    }
    public Auction getAuction() {
        return auction;
    }
    public Dictionary<String, Object> getAuctionProperties() {
        return properties;
    }
}