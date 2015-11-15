package org.konurbaev.auction.buyer.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;

import org.konurbaev.auction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidderServlet implements Servlet, Participant {

    private Auction auction;
    private String name;
    private PrintWriter writer;
    private final static Logger logger = LoggerFactory.getLogger(BidderServlet.class);

    public BidderServlet(String name) {
        logger.debug("BidderServlet constructor is starting...");
        this.name = name;
    }

    public String getName() {
        logger.debug("BidderServlet.getName() is starting...");
        return this.name;
    }

    public void setAuction(Auction auction) {
        logger.debug("BidderServlet.setAuction is starting...");
        this.auction = auction;
    }

    public void destroy() {
        logger.debug("BidderServlet.destroy is starting...");
    }

    public ServletConfig getServletConfig() {
        logger.debug("BidderServlet.getServletConfig() is starting...");
        return null;
    }

    public String getServletInfo() {
        logger.debug("BidderServlet.getServletInfo() is starting...");
        return null;
    }

    public void init(ServletConfig config) throws ServletException {
        logger.debug("BidderServlet.init is starting...");
    }

    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        logger.debug("BidderServlet.service is starting...");

        String bidValue = req.getParameter("bid");

        String item = req.getParameter("item");

        try {
            if (bidValue == null || item == null) {
                throw new IllegalArgumentException("Invalid bid");
            } else {
                writer = resp.getWriter();
                Float price = new Float(bidValue);
                auction.bid(item, price, this);
                writer.println("Accepted bid of " + bidValue + " for item " + item);
                logger.debug("Accepted bid of " + bidValue + " for item " + item);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void onAcceptance(Auction auction, String item, float price) {
        logger.debug("BidderServlet.onAcceptance is starting...");
        writer.println(this.name + " was awarded " + item + " for " + price);
        logger.debug(this.name + " was awarded " + item + " for " + price);
    }

    public void onRejection(Auction auction, String item, float bestBid) {
        logger.debug("BidderServlet.onRejection is starting...");
        writer.println("Bid for " + item + " from " + name + " was rejected");
        logger.debug("Bid for " + item + " from " + name + " was rejected");
    }
}