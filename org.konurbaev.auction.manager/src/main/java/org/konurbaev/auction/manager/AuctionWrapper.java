package org.konurbaev.auction.manager;

import java.util.*;

import org.konurbaev.auction.*;
import org.konurbaev.auction.spi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuctionWrapper implements Auction {

    private Collection<Auditor> auditors;
    private Auctioneer delegate;
    private Map<String,List<Float>> bidsPerItem = new HashMap<>();
    private float ask;
    private final static Logger logger = LoggerFactory.getLogger(AuctionWrapper.class);

    class ParticipantWrapper implements Participant {

        private Participant delegate;

        public ParticipantWrapper(Participant delegate) {
            logger.debug("ParticipantWrapper constuctor is starting...");
            this.delegate = delegate;
        }

        public String getName() {
            logger.debug("ParticipantWrapper.getName() is starting...");
            return delegate.getName();
        }

        public void onAcceptance(Auction auction, String item, float price) {
            logger.debug("ParticipantWrapper.onAcceptance is starting...");
            delegate.onAcceptance(auction, item, price);
            auditors.stream().forEach(auditor -> auditor.onAcceptance(AuctionWrapper.this.delegate, delegate, item, ask, price, bidsPerItem.get(item)));
            /*for (Auditor auditor : auditors) {
                auditor.onAcceptance(AuctionWrapper.this.delegate, delegate, item, ask, price, bidsPerItem.get(item).stream());
            }*/
        }

        public void onRejection(Auction auction, String item, float bestBid) {
            logger.debug("ParticipantWrapper.onRejection is starting...");
            delegate.onRejection(auction, item, bestBid);
            auditors.stream().forEach(auditor -> auditor.onRejection(AuctionWrapper.this.delegate, delegate, item, ask, bidsPerItem.get(item)));
            /*
            Float [] bids = bidsPerItem.get(item).toArray(new Float [0]);
            for (Auditor auditor : auditors) {
                auditor.onRejection(AuctionWrapper.this.delegate, delegate, item, ask, bids);
            }*/
        }
    }

    public AuctionWrapper(Auctioneer delegate, Collection<Auditor> auditors) {
        logger.debug("AuctionWrapper constructor is starting...");
        this.delegate = delegate;
        this.auditors = auditors;
    }

    public Float ask(String item, float price, Participant seller) throws InvalidOfferException {
        logger.debug("AuctionWrapper.ask is starting...");
        ask = price;
        return delegate.getAuction().ask(item, price, new ParticipantWrapper(seller));
    }

    public Float bid(String item, float price, Participant buyer) throws InvalidOfferException {
        logger.debug("AuctionWrapper.bid is starting...");
        List<Float> bids = bidsPerItem.get(item);
        if (bids == null) {
            bids = new LinkedList<>();
            bidsPerItem.put(item, bids);
        }
        bids.add(price);

        return delegate.getAuction().bid(item, price, new ParticipantWrapper(buyer));
    }
}
