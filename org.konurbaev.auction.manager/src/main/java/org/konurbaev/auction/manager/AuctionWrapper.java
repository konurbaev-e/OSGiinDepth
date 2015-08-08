package org.konurbaev.auction.manager;

import java.util.*;

import org.konurbaev.auction.*;
import org.konurbaev.auction.spi.*;

public class AuctionWrapper implements Auction {

    private Collection<Auditor> auditors;
    private Auctioneer delegate;
    private Map<String,List<Float>> bidsPerItem =
            new HashMap<String,List<Float>>();
    private float ask;

    class ParticipantWrapper implements Participant {

        private Participant delegate;

        public ParticipantWrapper(Participant delegate) {
            System.out.println("ParticipantWrapper constuctor is starting...");
            this.delegate = delegate;
        }

        public String getName() {
            System.out.println("ParticipantWrapper.getName() is starting...");
            return delegate.getName();
        }

        public void onAcceptance(Auction auction, String item, float price) {
            System.out.println("ParticipantWrapper.onAcceptance is starting...");
            delegate.onAcceptance(auction, item, price);

            Float [] bids = bidsPerItem.get(item).toArray(new Float [0]);
            for (Auditor auditor : auditors) {
                auditor.onAcceptance(AuctionWrapper.this.delegate, delegate,
                        item, ask, price, bids);
            }
        }

        public void onRejection(Auction auction, String item,
                                float bestBid) {
            System.out.println("ParticipantWrapper.onRejection is starting...");
            delegate.onRejection(auction, item, bestBid);

            Float [] bids = bidsPerItem.get(item).toArray(new Float [0]);
            for (Auditor auditor : auditors) {
                auditor.onRejection(AuctionWrapper.this.delegate, delegate, item,
                        ask, bids);
            }
        }
    }

    public AuctionWrapper(Auctioneer delegate, Collection<Auditor> auditors) {
        System.out.println("AuctionWrapper constructor is starting...");
        this.delegate = delegate;
        this.auditors = auditors;
    }

    public Float ask(String item, float price, Participant seller)
            throws InvalidOfferException {
        System.out.println("AuctionWrapper.ask is starting...");
        ask = price;
        return delegate.getAuction().ask(item, price,
                new ParticipantWrapper(seller));
    }

    public Float bid(String item, float price, Participant buyer)
            throws InvalidOfferException {
        System.out.println("AuctionWrapper.bid is starting...");
        List<Float> bids = bidsPerItem.get(item);
        if (bids == null) {
            bids = new LinkedList<Float>();
            bidsPerItem.put(item, bids);
        }
        bids.add(price);

        return delegate.getAuction().bid(item, price,
                new ParticipantWrapper(buyer));
    }
}
