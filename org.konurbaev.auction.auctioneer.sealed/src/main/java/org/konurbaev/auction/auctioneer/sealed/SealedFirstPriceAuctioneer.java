package org.konurbaev.auction.auctioneer.sealed;

import java.util.*;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.Properties;
import org.konurbaev.auction.Auction;
import org.konurbaev.auction.spi.Auctioneer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        name = "org.konurbaev.auction.auctioneer.sealed.sealedfirstpriceauctioneer",
        immediate = true
)
@Properties({
        @Property(name = Auction.TYPE, value = {SealedFirstPriceAuctioneerConstants.SEALED_FIRST_PRICE}),
        @Property(name = Auction.DURATION, value = {SealedFirstPriceAuctioneerConstants.DURATION})
})
@Service(Auctioneer.class)
public class SealedFirstPriceAuctioneer implements Auctioneer, SealedFirstPriceAuctioneerConstants {

    private final Dictionary<String, Object> properties = new Hashtable<>();

    private final Auction auction;
    private final static Logger logger = LoggerFactory.getLogger(SealedFirstPriceAuctioneer.class);

    @Activate
    private void start() {
        logger.debug("Activating SealedFirstPriceAuctioneer");
    }

    private SealedFirstPriceAuctioneer() {
        logger.debug("SealedFirstPriceAuctioneer constructor is starting...");
        properties.put(Auction.TYPE, SEALED_FIRST_PRICE);
        properties.put(Auction.DURATION, DURATION);
        auction = new SealedFirstPriceAuction(DURATION);
    }

    public Auction getAuction() {
        logger.debug("SealedFirstPriceAuctioneer.getAuction() is starting...");
        return auction;
    }

    public Dictionary<String, Object> getAuctionProperties() {
        logger.debug("SealedFirstPriceAuctioneer.getAuctionProperties() is starting...");
        return properties;
    }

}