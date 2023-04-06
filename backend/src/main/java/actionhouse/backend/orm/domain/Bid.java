package actionhouse.backend.orm.domain;

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
    @GeneratedValue
    private long id;

    private double bid;

    private LocalDateTime date;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Customer bidder;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Article article;
}
