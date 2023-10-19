package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.dto.UserResponseDto;
import api.socialPlatform.ApiForSocialApp.model.RefreshToken;
import api.socialPlatform.ApiForSocialApp.model.ResponseObject;
import api.socialPlatform.ApiForSocialApp.services.Impl.RefreshTokenServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/refreshToken")
public class RefreshTokenController {
    @Autowired
    private RefreshTokenServiceImp refreshTokenService;

    @GetMapping("/")
    ResponseEntity<ResponseObject> getRefreshToken(@RequestBody UserResponseDto user) {
        RefreshToken refreshToken = refreshTokenService.getRefreshTokenByUsername(user.getUsername());
        if (refreshToken != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get refresh token by username successfully!", refreshToken)
            );
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                new ResponseObject("FAILED", "User hasn't never logged in!", null)
        );
    }
}
