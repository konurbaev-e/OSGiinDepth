package org.konurbaev.auction.buyer.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;

import org.konurbaev.auction.*;

public class BidderServlet implements Servlet, Participant {

    private Auction auction;
    private String name;
    private PrintWriter writer;

    public BidderServlet(String name) {
        System.out.println("BidderServlet constructor is starting...");
        this.name = name;
    }

    public String getName() {
        System.out.println("BidderServlet.getName() is starting...");
        return this.name;
    }

    public void setAuction(Auction auction) {
        System.out.println("BidderServlet.setAuction is starting...");
        this.auction = auction;
    }

    public void destroy() {
        System.out.println("BidderServlet.destroy is starting...");
    }

    public ServletConfig getServletConfig() {
        System.out.println("BidderServlet.getServletConfig() is starting...");
        return null;
    }

    public String getServletInfo() {
        System.out.println("BidderServlet.getServletInfo() is starting...");
        return null;
    }

    public void init(ServletConfig config) throws ServletException {
        System.out.println("BidderServlet.init is starting...");
    }

    public void service(ServletRequest req, ServletResponse resp)
            throws ServletException, IOException {
        System.out.println("BidderServlet.service is starting...");

        String bidValue =
                req.getParameter("bid");

        String item =
                req.getParameter("item");

        try {
            if (bidValue == null || item == null) {
                throw new IllegalArgumentException("Invalid bid");
            } else {
                writer = resp.getWriter();
                Float price = new Float(bidValue);
                auction.bid(item, price, this);

                writer.println("Accepted bid of "
                        + bidValue + " for item " + item);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void onAcceptance(Auction auction, String item, float price) {
        System.out.println("BidderServlet.onAcceptance is starting...");
        writer.println(this.name + " was awarded " + item + " for " + price);
    }

    public void onRejection(Auction auction, String item, float bestBid) {
        System.out.println("BidderServlet.onRejection is starting...");
        writer.println("Bid for " + item + " from " + name + " was rejected");
    }
}