package actionhouse.api.controller;

import actionhouse.api.bl.AuthenticationLogic;
import actionhouse.api.dto.LoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AuthenticationController {

    @Autowired
    private AuthenticationLogic authenticationLogic;

    public AuthenticationController() {
        System.out.println("AuthenticationController");
        log.info("AuthenticationController created");
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Validates login credentials (Just kidding, we are just looking for the username).", tags = {"auth"})
    @ApiResponse(responseCode = "200", description = "Login success", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Login failed", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")})
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        log.info("login() with %s".formatted(loginDto));
        authenticationLogic.login(loginDto.email, loginDto.password);

        return ResponseEntity.ok().build();

    }
}
