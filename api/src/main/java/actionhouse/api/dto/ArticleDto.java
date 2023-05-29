package actionhouse.api.dto;

import actionhouse.api.domain.Article;
import actionhouse.api.domain.ArticleStatus;
import actionhouse.api.domain.Bid;
import lombok.Data;

import java.util.Objects;

@Data
public class ArticleDto {
    public Long id;
    public String description;
    public Double price;
    public String seller;
    public ArticleStatus status;
    public String buyer;

    public static ArticleDto Create(Article article) {
        // doing manual mapping, because I don't want to fiddle around with the automapper configuration
        var articleDto = new ArticleDto();
        articleDto.id = article.getId();
        articleDto.description = article.getDescription();
        articleDto.seller = article.getSeller().getEmail();
        articleDto.status = article.getStatus();

        if (article.getStatus() == ArticleStatus.NOT_SOLD)
            articleDto.price = Double.valueOf(article.getReservePrice());
        else
            articleDto.price = article.getBids().stream().mapToDouble(Bid::getBid).max().orElse(article.getReservePrice());

        if(article.getStatus() == ArticleStatus.SOLD)
            articleDto.buyer = Objects.requireNonNull(article.getBuyer()).getEmail();

        return articleDto;
    }
}
