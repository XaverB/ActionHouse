package actionhouse.api.bl.impl;

import actionhouse.api.bl.ArticleLogic;
import actionhouse.api.bl.BidLogic;
import actionhouse.api.dao.ArticleRepository;
import actionhouse.api.dao.BidRepository;
import actionhouse.api.dao.CustomerRepository;
import actionhouse.api.domain.ArticleStatus;
import actionhouse.api.domain.Bid;
import actionhouse.api.exceptions.ActionNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
public class BidLogicImpl implements BidLogic {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void bid(Long articleId, String bidder, Double price) {

        var customer = customerRepository.findByEmail(bidder).orElseThrow();
        var article = articleRepository.findById(articleId).orElseThrow();

        if (article.getStatus() != ArticleStatus.AUCTION_RUNNING) {
            throw new ActionNotAllowedException("Article is not running");
        }

        if(article.getSeller().getEmail().equals(bidder)) {
            throw new ActionNotAllowedException("Seller cannot bid on own article");
        }

        if(article.getAuctionEndDate() != null && article.getAuctionEndDate().isBefore(LocalDateTime.now())) {
            throw new ActionNotAllowedException("Auction has ended");
        }

        var highestBid = article.getBids().stream()
                .mapToDouble(Bid::getBid)
                .max()
                .orElse(article.getReservePrice());

        if (highestBid >= price) {
            throw new ActionNotAllowedException("Bid is too low");
        }

        var bid = new Bid();
        bid.setBid(price);
        bid.setArticle(article);
        bid.setBidder(customer);
        bid.setDate(LocalDateTime.now());

        article.addBid(bid);
        articleRepository.save(article);
        log.info("Bid placed: " + bid);
    }
}
