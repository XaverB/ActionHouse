package actionhouse.api.dto;

import lombok.Data;

@Data
public class BidDto {
    public Double amount;
    public String bidder;
    public Long articleId;
}
