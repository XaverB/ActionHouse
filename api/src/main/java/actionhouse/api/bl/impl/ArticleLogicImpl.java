package actionhouse.api.bl.impl;

import actionhouse.api.bl.ArticleLogic;
import actionhouse.api.dao.ArticleRepository;
import actionhouse.api.dao.CustomerRepository;
import actionhouse.api.domain.Article;
import actionhouse.api.domain.ArticleStatus;
import actionhouse.api.domain.Bid;
import actionhouse.api.exceptions.ActionNotAllowedException;
import actionhouse.api.exceptions.ArticleNotFoundException;
import actionhouse.api.exceptions.AuthorizationException;
import actionhouse.api.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ArticleLogicImpl implements ArticleLogic {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Article> getArticles(ArticleStatus status, String searchTerm, Double maxPrice) {
        return articleRepository
                .findByFilters(
                        searchTerm,
                        maxPrice,
                        status
                );
    }

    @Override
    public Long createArticle(Article article, String seller) {
        article.setSeller(customerRepository.findByEmail(seller).orElseThrow(() -> new UserNotFoundException("Seller not found")));
        article.setStatus(ArticleStatus.LISTED);
        return articleRepository.save(article).getId();
    }

    @Override
    public void deleteArticle(Long id, String email) {
        var article = articleRepository.findById(id).orElseThrow(
                () -> new ArticleNotFoundException(id));

        if (article.getStatus() == ArticleStatus.AUCTION_RUNNING) {
            throw new ActionNotAllowedException("Cannot delete article while auction is running");
        }

        if (!article.getSeller().getEmail().equals(email)) {
            throw new AuthorizationException("Cannot delete article that is not yours");
        }
        articleRepository.delete(article);
    }

    @Override
    public void startAuction(Long id, String email) {
        var article = articleRepository.findById(id).orElseThrow(
                () -> new ArticleNotFoundException(id)
        );
        if (!article.getSeller().getEmail().equals(email)) {
            throw new AuthorizationException("Cannot start auction that is not yours");
        }
        if (article.getStatus() != ArticleStatus.LISTED) {
            throw new ActionNotAllowedException("Cannot start auction that is not listed");
        }
        article.setStatus(ArticleStatus.AUCTION_RUNNING);
        articleRepository.save(article);
    }

    @Override
    public void stopAuction(Long id, String email) {
        var article = articleRepository.findById(id).orElseThrow(
                () -> new ArticleNotFoundException(id)
        );
        if (!article.getSeller().getEmail().equals(email)) {
            throw new AuthorizationException("Cannot stop auction that is not yours");
        }
        if (article.getStatus() != ArticleStatus.AUCTION_RUNNING) {
            throw new ActionNotAllowedException("Cannot stop auction that is not running");
        }

        boolean hasBids = article.getBids().size() > 0;
        if (hasBids) {
            SetSold(article);
        } else {
            SetNotSold(article);
        }

        articleRepository.save(article);
    }

    private static void SetNotSold(Article article) {
        article.setStatus(ArticleStatus.NOT_SOLD);
        article.setBuyer(null);
        article.setHammerPrice(null);
    }

    private static void SetSold(Article article) {
        article.setStatus(ArticleStatus.SOLD);
        var highestBid = article.getBids().stream()
                .max(Comparator.comparingDouble(Bid::getBid))
                .orElseThrow();
        article.setHammerPrice(highestBid.getBid());
        article.setBuyer(highestBid.getBidder());
    }
}
