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
    @GeneratedValue
    private Long id;

    private String description;

    private float reservePrice;

    private float hammerPrice;

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

    public Article(Long articleId, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, String description, float reservePrice, float hammerPrice, Customer seller, Customer buyer, ArticleStatus articleStatus) {
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
