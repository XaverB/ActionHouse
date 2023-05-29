package actionhouse.api.dao;

import actionhouse.api.domain.Article;
import actionhouse.api.domain.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    public List<Article> findByStatusOrderByDescriptionAsc(ArticleStatus status);


    @Query("SELECT a FROM Article a WHERE "
            + "(:searchTerm IS NULL OR LOWER(a.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) "
            + "AND (:articleStatus IS NULL OR  a.status = :articleStatus)"
            + "AND ("
            + "     (a.status = actionhouse.api.domain.ArticleStatus.AUCTION_RUNNING AND :maxPrice IS NULL OR (SELECT MAX(b.bid) FROM Bid b WHERE b.article = a) <= :maxPrice) "
            + "     OR "
            + "     (a.status <> actionhouse.api.domain.ArticleStatus.AUCTION_RUNNING AND :maxPrice IS NULL OR a.hammerPrice <= :maxPrice) "
            + ")"
            + "ORDER BY a.description ASC")
    List<Article> findByFilters(
            @Param("searchTerm") String searchTerm,
            @Param("maxPrice") Double maxPrice,
            @Param("articleStatus") ArticleStatus articleStatus);
}
