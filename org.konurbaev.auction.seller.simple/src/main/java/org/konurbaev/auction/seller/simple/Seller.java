package org.konurbaev.auction.seller.simple;

import org.konurbaev.auction.*;
import org.konurbaev.auction.spi.Auctioneer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Seller implements Participant, Runnable {
    private final String BID = "bicycle";
    private final float COST = 24.0f;
    private final String name;
    private Auctioneer auctioneer;
    private final static Logger logger = LoggerFactory.getLogger(Seller.class);

    public Seller(String name) {
        logger.debug("Seller constructor is starting...");
        this.name = name;
    }

    public String getName() {
        logger.debug("Seller.getName() is starting...");
        return this.name;
    }

    public void ask(Auctioneer auctioneer) {
        logger.debug("Seller.ask is starting...");
        this.auctioneer = auctioneer;
        new Thread(this).start();
    }

    public void run() {
        logger.debug("Seller.run is starting...");
        try {
            auctioneer.getAuction().ask(BID, COST, this);
        } catch (InvalidOfferException e) {
            e.printStackTrace();
        }
        auctioneer = null;
    }

    public void onAcceptance(Auction auction, String item, float price) {
        logger.debug("Seller.onAcceptance is starting...");
        logger.debug(this.name + " sold " + item + " for " + price);
    }
    public void onRejection(Auction auction, String item, float bestBid) {
        logger.debug("Seller.onRejection is starting...");
        logger.debug("No bidders accepted asked price for " + item + ", best bid was " + bestBid);
    }
}