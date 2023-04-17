package actionhouse.backend.orm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Article {

    @Id
    @TableGenerator(
            name = "Art_Gen",
            initialValue = 50,
            allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Art_Gen")
    private Long id;

    private String description;

    private Float reservePrice;

    private Float hammerPrice;

    private LocalDateTime auctionStartDate;

    private LocalDateTime auctionEndDate;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Customer seller;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Customer buyer;

    // We are using ORDINAL here, because queries with the status as a condition could be common
    // And ordinal comparison is much faster than string comparison
    @Enumerated(EnumType.ORDINAL) // see https://thorben-janssen.com/hibernate-enum-mappings/
    private ArticleStatus status;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @ToString.Exclude
    private Set<Bid> bids = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @ToString.Exclude
    private Set<Category> categories = new HashSet<>();

    public Article(Long articleId, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, String description, Float reservePrice, Float hammerPrice, Customer seller, Customer buyer, ArticleStatus articleStatus) {
        this.id = articleId;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.description = description;
        this.reservePrice = reservePrice;
        this.hammerPrice = hammerPrice;
        this.seller = seller;
        this.buyer = buyer;
        this.status = articleStatus;
    }

    public void addBid(Bid bid) {
        if(bid == null) {
            throw new IllegalArgumentException("bid must not be null");
        }

        if(bid.getArticle() != null) {
            bid.getArticle().getBids().remove(bid);
        }
        bid.setArticle(this);
        bids.add(bid);
    }

    public void removeBid(Bid bid) {
        if(bid == null) {
            throw new IllegalArgumentException("bid must not be null");
        }
        bids.remove(bid);
        bid.setArticle(null);
    }

    public void addBuyer(Customer buyer) {
        if(buyer == null) {
            throw new IllegalArgumentException("buyer must not be null");
        }
        if(buyer.getBoughtArticles().contains(this)) {
            buyer.removeBoughtArticle(this);
        }
        buyer.addBoughtArticle(this);
        this.buyer = buyer;
    }

    public void removeBuyer() {
        if(buyer == null) {
            throw new IllegalArgumentException("buyer must not be null");
        }
        if(buyer.getBoughtArticles().contains(this)) {
            buyer.removeBoughtArticle(this);
        }
        this.buyer = null;
    }

    public void addSeller(Customer seller) {
        if(seller == null) {
            throw new IllegalArgumentException("seller must not be null");
        }
        if(seller.getSoldArticles().contains(this)) {
            seller.removeSoldArticle(this);
        }
        seller.addSoldArticle(this);
        this.seller = seller;
    }

    public void removeSeller() {
        if(seller == null) {
            throw new IllegalArgumentException("seller must not be null");
        }
        if(seller.getSoldArticles().contains(this)) {
            seller.removeSoldArticle(this);
        }
        this.seller = null;
    }

    public void addCategory(Category category) {
        if(category == null) {
            throw new IllegalArgumentException("category must not be null");
        }
        if(category.getArticles().contains(this)) {
            category.getArticles().add(this);
        }
        category.getArticles().add(this);
        categories.add(category);
    }

    public void removeArticle(Category category) {
        if(category == null) {
            throw new IllegalArgumentException("category must not be null");
        }
        if(category.getArticles().contains(this)) {
            category.getArticles().remove(this);
        }
        categories.remove(category);
    }


    @Override
    public int hashCode()
    {
        return 42;
    }

    public boolean equals(Object other) {
        if(this == other) return true;
        if(other == null) return false;
        if(getClass() != other.getClass()) return false;
        Article otherArticle = (Article) other;
        return id != null && id.equals(otherArticle.getId());
    }
}
