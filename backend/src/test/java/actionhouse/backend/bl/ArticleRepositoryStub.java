package actionhouse.backend.bl;

import actionhouse.backend.orm.domain.Article;
import actionhouse.backend.orm.repository.ArticleRepository;
import actionhouse.backend.orm.repository.IArticleRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ArticleRepositoryStub implements IArticleRepository {

    private final ArrayList<Article> articles;

    public ArticleRepositoryStub(Collection<Article> articles) {
        this.articles = new ArrayList<>(articles);
    }

    @Override
    public List<Article> getTopArticles(int count) {
        return articles.subList(0, count);
    }

    @Override
    public List<Article> findArticlesByDescription(String searchPhrase,
                                                   Double maxReservePrice) {
        var result = articles.stream().filter(a -> a.getDescription().contains(searchPhrase));
        if(maxReservePrice != null && maxReservePrice > 0)
            result = result.filter(a -> a.getReservePrice() <= maxReservePrice);


        return result.toList();
    }

    @Override
    public Article getById(long id) {
        return articles
                .stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Article save(Article entity) {
        articles.add(entity);
        return entity;
    }

    @Override
    public Article update(Article entity) {
        articles.stream()
                .filter(a -> a.getId() == entity.getId()).findFirst()
                .ifPresent(a -> {
                    a.setHammerPrice(entity.getHammerPrice());
                    a.setReservePrice(entity.getReservePrice());
                    a.setSeller(entity.getSeller());
                    a.setCategories(entity.getCategories());
                    a.setBids(entity.getBids());
                    a.setDescription(entity.getDescription());
                    a.setStatus(entity.getStatus());
                    a.setAuctionEndDate(entity.getAuctionEndDate());
                    a.setAuctionStartDate(entity.getAuctionStartDate());
                });
        return entity;
    }

    @Override
    public void delete(Article entity) {
        if (articles.contains(entity))
            articles.remove(entity);
    }
}
