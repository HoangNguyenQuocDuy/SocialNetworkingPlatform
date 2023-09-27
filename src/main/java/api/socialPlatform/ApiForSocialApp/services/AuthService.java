package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.AuthRequest;
import api.socialPlatform.ApiForSocialApp.dto.AuthResponse;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IUserRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public ResponseEntity<?> authenticate(AuthRequest request, HttpServletResponse response) {
        try {
            User user = userRepo.findByUsername(request.getUsername()).orElseThrow(()-> new Exception("User not found!"));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var jwtAccessToken = jwtService.generateToken(user);
            var jwtRefreshToken = jwtService.generateRefreshToken(user);

            response.setHeader("Bearer ", jwtAccessToken);

            return ResponseEntity.ok(
                    AuthResponse.builder()
                            .accessToken(jwtAccessToken)
                            .refreshToken(jwtRefreshToken)
                            .username(user.getUsername())
                            .build()
            );
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid Credential");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
