package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.Bid;
import jakarta.persistence.EntityManager;

public class BidRepository extends BaseRepository<Bid> {
    public BidRepository(EntityManager entityManager) {
        super(Bid.class, entityManager);
    }
}
