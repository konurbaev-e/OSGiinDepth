package org.konurbaev.auction.spi;

import java.util.Dictionary;
import org.konurbaev.auction.Auction;

public interface Auctioneer {
    Auction getAuction();
    Dictionary<String, Object> getAuctionProperties();
}