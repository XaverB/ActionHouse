package actionhouse.api.controller;

import actionhouse.api.annotations.AuthCheck;
import actionhouse.api.bl.ArticleLogic;
import actionhouse.api.bl.AuthenticationLogic;
import actionhouse.api.domain.Article;
import actionhouse.api.domain.ArticleStatus;
import actionhouse.api.dto.ArticleDto;
import actionhouse.api.dto.CreateArticleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ArticleController {
    @Autowired
    private ArticleLogic articleLogic;

    @Autowired
    private AuthenticationLogic authenticationLogic;

    @AuthCheck
    @GetMapping("/articles")
    @Operation(summary = "Get articles", description = "Returns a list of articles.", tags = {"articles"})
    @ApiResponse(responseCode = "200", description = "Login success",  content = { @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class))) })
    @ApiResponse(responseCode = "401", description = "Login failed", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")})
    public List<ArticleDto> getArticlesRunning(@RequestHeader String email,
                                               @RequestParam(required = false) ArticleStatus status,
                                               @RequestParam(required = false) Double maxPrice,
                                               @RequestParam(required = false) String searchTerm) throws AuthException {

        return articleLogic.getArticles(status,
                        searchTerm,
                        maxPrice)
                .stream().map(ArticleDto::Create)
                .toList();
    }

    @AuthCheck
    @PostMapping("/articles")
    @Operation(summary = "Create an article", description = "Creates an article.", tags = {"articles"})
    @ApiResponse(responseCode = "200", description = "Article created", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")})
    public ResponseEntity<?> createArticle(@RequestHeader String email,
                                           @RequestBody CreateArticleDto articleDto) throws AuthException {
        var article = new Article();
        article.setDescription(articleDto.description);
        article.setReservePrice(articleDto.price);

        var articleId = articleLogic.createArticle(article, email);
        return articleId != null ? ResponseEntity.ok().body(articleId)
                : ResponseEntity.badRequest().build();
    }

    @AuthCheck
    @DeleteMapping("/articles/{id}")
    @Operation(summary = "Delete an article", description = "Deletes an article.", tags = {"articles"})
    @ApiResponse(responseCode = "200", description = "Article deleted", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")})
    public ResponseEntity<?> deleteArticle(@RequestHeader String email,
                                           @PathVariable Long id) throws AuthException {

        articleLogic.deleteArticle(id, email);
        return ResponseEntity.noContent().build();

    }

    @AuthCheck
    @PutMapping("/articles/{id}/auction")
    @Operation(summary = "Start or stop an auction", description = "Starts or stops an auction.", tags = {"articles"})
    @ApiResponse(responseCode = "200", description = "Auction started", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")})
    public ResponseEntity<?> startAuction(@RequestHeader String email,
                                          @PathVariable Long id,
                                          @RequestParam AuctionAction action) throws AuthException {

        if (action == AuctionAction.START) {
            articleLogic.startAuction(id, email);
        } else {
            articleLogic.stopAuction(id, email);
        }

        return ResponseEntity.noContent().build();
    }
}
