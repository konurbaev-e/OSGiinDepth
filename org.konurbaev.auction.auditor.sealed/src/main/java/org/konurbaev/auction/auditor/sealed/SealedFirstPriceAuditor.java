package org.konurbaev.auction.auditor.sealed;

import org.apache.felix.scr.annotations.*;
import org.konurbaev.auction.Auction;
import org.konurbaev.auction.Participant;
import org.konurbaev.auction.spi.Auctioneer;
import org.konurbaev.auction.spi.Auditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

@Component(
        name = "org.konurbaev.auction.auditor.sealed.sealedfirstpriceauditor",
        immediate = true
)
@Properties({
})
@Service(Auditor.class)
public class SealedFirstPriceAuditor implements Auditor, SealedFirstPriceAuditorConstants {

    private final static Logger logger = LoggerFactory.getLogger(SealedFirstPriceAuditor.class);

    @Activate
    private void start() {
        logger.debug("Activating SealedFirstPriceAuditor");
    }

    public void onAcceptance(Auctioneer auctioneer,
                             Participant participant,
                             String item,
                             float ask,
                             float acceptedBid,
                             List<Float> bids) {
        logger.debug("SealedFirstPriceAuditor.onAcceptance is starting...");
        verify(auctioneer, participant, bids);
    }

    public void onRejection(Auctioneer auctioneer,
                            Participant participant,
                            String item,
                            float ask,
                            List<Float> rejectedBids) {
        logger.debug("SealedFirstPriceAuditor.onRejection is starting...");
        verify(auctioneer, participant, rejectedBids);
    }

    private void verify(Auctioneer auctioneer,
                        Participant participant,
                        List<Float> bids) {
        logger.debug("SealedFirstPriceAuditor.verify is starting...");
        if (SEALED_FIRST_PRICE.equals(auctioneer.getAuctionProperties().get(Auction.TYPE))) {
            bids.stream().reduce((a, b) -> {
                if (b - a <= 1.0)
                    logger.debug("Warning to '" + participant.getName() + "': bids (" + a + ", " + b + ") are too close together, possible disclosure may have happened");
                return b;
            });
        }
    }

}