package actionhouse.api.controller;

import actionhouse.api.annotations.AuthCheck;
import actionhouse.api.bl.AuthenticationLogic;
import actionhouse.api.bl.BidLogic;
import actionhouse.api.dto.BidDto;
import actionhouse.api.dto.LoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/bid", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class BidController {

    @Autowired
    private AuthenticationLogic authenticationLogic;

    @Autowired
    private BidLogic bidLogic;

    public BidController() {
        System.out.println("BidController");
        log.info("BidController created");
    }

    @AuthCheck
    @PostMapping("")
    @Operation(summary = "Bid on an article", description = "", tags = {"bid"})
    @ApiResponse(responseCode = "200", description = "Bid success", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bid failed", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")})
    public ResponseEntity<?> bid(@RequestHeader String email, @RequestBody BidDto bidDto) {
        log.info("bid() with %s".formatted(bidDto));
        bidLogic.bid(bidDto.articleId, email, bidDto.amount);
        return ResponseEntity.ok().build();
    }
}
