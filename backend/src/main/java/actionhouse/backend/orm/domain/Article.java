package actionhouse.backend.orm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private long id;

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

    @OneToMany(mappedBy = "article", cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Bid> bids = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.REFRESH})
    private Set<Category> categories = new HashSet<>();
}
