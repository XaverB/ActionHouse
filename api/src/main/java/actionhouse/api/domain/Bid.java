package actionhouse.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Bid {

    @Id
    @TableGenerator(
            name = "Bid_Gen",
            initialValue = 50,
            allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Bid_Gen")
    private Long id;

    private double bid;

    private LocalDateTime date;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Customer bidder;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Article article;

    public Bid(Long id, double bid, LocalDateTime date, Customer bidder, Article article) {
        this.id = id;
        this.bid = bid;
        this.date = date;
        this.bidder = bidder;
        this.article = article;
    }

    @Override
    public int hashCode() {
        return 42;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Bid other = (Bid) obj;
        return id != null && id.equals(other.id);
    }
}
