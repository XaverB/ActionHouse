package actionhouse.api.dao;

import actionhouse.api.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository  extends JpaRepository<Article, Long> {
}
