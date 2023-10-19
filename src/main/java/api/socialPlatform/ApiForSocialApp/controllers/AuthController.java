package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.dto.AuthRequest;
import api.socialPlatform.ApiForSocialApp.dto.AuthResponse;
import api.socialPlatform.ApiForSocialApp.dto.RefreshTokenRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.UserRequestDto;
import api.socialPlatform.ApiForSocialApp.model.ResponseObject;
import api.socialPlatform.ApiForSocialApp.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody UserRequestDto userRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Register successfully!",
                            authService.createUser(userRequestDto))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseObject("FAILED", "Failed when register",
                            e.getMessage())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {

        return ResponseEntity.ok(authService.authenticate(authRequest, response));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseObject> refreshAccessToken(@RequestBody RefreshTokenRequestDto refreshToken) {

        try {
            AuthResponse authResponse = authService.refreshAccessToken(refreshToken.getRefreshToken());

            if (authResponse != null) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Refresh access token successfully!", authResponse)
                );
            }
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseObject("FAILED", "Failed when refresh access token!", null)
            );

        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseObject("FAILED", "Something went wrong!",
                            e.getMessage())
            );
        }
    }
}
