package org.konurbaev.auction.auctioneer.sealed;

import java.util.*;
import org.konurbaev.auction.Auction;
import org.konurbaev.auction.InvalidOfferException;
import org.konurbaev.auction.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SealedFirstPriceAuction implements Auction {
    private class Book {
        float ask;
        Participant seller;
        float highestBid;
        Participant highestBidder;
        int numberOfBids;
    }
    private Map<String, Book> openTransactions;
    private final int maxAllowedBids;
    private final static Logger logger = LoggerFactory.getLogger(SealedFirstPriceAuction.class);

    public SealedFirstPriceAuction(String duration) {
        logger.debug("SealedFirstPriceAuction constructor is starting...");
        this.maxAllowedBids = Integer.valueOf(duration);
        this.openTransactions = new HashMap<>();
    }

    public Float ask(String item, float price, Participant seller) throws InvalidOfferException {
        logger.debug("SealedFirstPriceAuction.ask is starting...");
        if (price <= 0.0f) {
            throw new InvalidOfferException("Ask must be greater than zero.");
        }
        Book book = openTransactions.get(item);
        if (book == null) {
            book = new Book();
            openTransactions.put(item, book);
        }
        if (book.seller != null) {
            throw new InvalidOfferException("Item [" + item + "] has already being auctioned.");
        }
        book.ask = price;
        book.seller = seller;
        logger.debug(seller.getName() + " offering item " + item + " for the asking price of " + price);
        return price;
    }

    public Float bid(String item, float price, Participant buyer) throws InvalidOfferException {
        logger.debug("SealedFirstPriceAuction.bid is starting...");

        if (price <= 0.0f) throw new InvalidOfferException("Bid must be greater than zero.");

        Book book = openTransactions.get(item);
        if (book == null) {
            book = new Book();
            openTransactions.put(item, book);
        }

        if (book.numberOfBids > maxAllowedBids) {
            logger.debug("book.numberOfBids " + book.numberOfBids +  " is bigger then maxAllowedBids " + maxAllowedBids);
            return null;
        }

        if (price > book.highestBid) {
            book.highestBid = price;
            book.highestBidder = buyer;
        }
        if ((++book.numberOfBids) == maxAllowedBids) {
            if (book.seller != null) {
                if (book.highestBid >= book.ask) {
                    book.seller.onAcceptance(this, item, book.highestBid);
                    book.highestBidder.onAcceptance(this, item, book.highestBid);
                } else {
                    book.seller.onRejection(this, item, book.highestBid);
                    book.highestBidder.onRejection(this, item, book.highestBid);
                }
            } else {
                book.highestBidder.onRejection(this, item, book.highestBid);
            }
            openTransactions.remove(item);
        } else {
            logger.debug(buyer.getName() + " bidding for item " + item);
        }
        return null;
    }
}