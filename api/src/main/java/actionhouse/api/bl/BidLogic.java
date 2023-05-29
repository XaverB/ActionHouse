package actionhouse.api.bl;

public interface BidLogic {
    void bid(Long articleId, String bidder, Double price);
}
