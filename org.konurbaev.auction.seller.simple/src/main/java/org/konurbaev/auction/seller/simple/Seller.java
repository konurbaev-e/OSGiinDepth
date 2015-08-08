package org.konurbaev.auction.seller.simple;

import org.konurbaev.auction.*;

public class Seller implements Participant, Runnable {
    private final String name;
    private Auction auction;
    public Seller(String name) {
        System.out.println("Seller constructor is starting...");
        this.name = name;
    }
    public String getName() {
        System.out.println("Seller.getName() is starting...");
        return this.name;
    }
    public void ask(Auction auction) {
        System.out.println("Seller.ask is starting...");
        this.auction = auction;
        new Thread(this).start();
    }
    public void run() {
        System.out.println("Seller.run is starting...");
        try {
            auction.ask("bicycle", 24.0f, this);
        } catch (InvalidOfferException e) {
            e.printStackTrace();
        }
        auction = null;
    }
    public void onAcceptance(Auction auction, String item,
                             float price) {
        System.out.println("Seller.onAcceptance is starting...");
        System.out.println(this.name + " sold " + item + " for " + price);
    }
    public void onRejection(Auction auction, String item,
                            float bestBid) {
        System.out.println("Seller.onRejection is starting...");
        System.out.println("No bidders accepted asked price for " + item + ", best bid was " + bestBid);
    }
}