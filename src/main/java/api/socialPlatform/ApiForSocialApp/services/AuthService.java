package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.AuthRequest;
import api.socialPlatform.ApiForSocialApp.dto.AuthResponse;
import api.socialPlatform.ApiForSocialApp.dto.UserRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.UserResponseDto;
import api.socialPlatform.ApiForSocialApp.model.RefreshToken;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IUserRepo;
import api.socialPlatform.ApiForSocialApp.services.Impl.RefreshTokenServiceImp;
import api.socialPlatform.ApiForSocialApp.services.Impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private IUserRepo userRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RefreshTokenServiceImp refreshTokenServiceImp;

    public ResponseEntity<?> authenticate(AuthRequest request, HttpServletResponse response) {
        try {
            User user = userRepo.findByUsername(request.getUsername()).orElseThrow(()-> new Exception("User not found!"));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var jwtAccessToken = jwtService.generateToken(user);

            response.setHeader("Authentication", "Bearer " + jwtAccessToken);

            RefreshToken jwtRefreshToken = refreshTokenServiceImp.createRefreshToken(user);

            return ResponseEntity.ok(
                    AuthResponse.builder()
                            .accessToken(jwtAccessToken)
                            .refreshToken(jwtRefreshToken.getToken())
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

    public UserResponseDto createUser(UserRequestDto userRequestDto) throws Exception {
        Optional<User> userOptional = userRepo.findByUsername(userRequestDto.getUsername());
        if (userOptional.isPresent()) {
            throw new Exception("Username has exited!");
        } else if (userRepo.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new Exception("Email has been used!");
        } else {
            User user = User.builder()
                    .username(userRequestDto.getUsername())
                    .password(userRequestDto.getPassword())
                    .currentName(userRequestDto.getCurrentName())
                    .email(userRequestDto.getEmail())
                    .imageUrl(userRequestDto.getImageUrl())
                    .build();
            userService.saveUser(user);
            return UserResponseDto.fromUser(user);
        }
    }

    public AuthResponse refreshAccessToken(String oldRefreshToken) throws Exception {
        RefreshToken refreshToken = refreshTokenServiceImp.getRefreshTokenByToken(oldRefreshToken);

        if (refreshToken!= null && refreshToken.getExpiredAt().after(new Date())) {
            User user = userRepo.findByRefreshToken(refreshToken).orElseThrow(()-> new Exception("User not found!"));
            RefreshToken newRefreshToken = refreshTokenServiceImp.createRefreshToken(user);
            String newAccessToken = jwtService.generateToken(user);

            return AuthResponse
                    .builder()
                    .username(user.getUsername())
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken.getToken())
                    .build();
        } else if (refreshToken == null) {
            throw new Exception("Refresh token is not valid!");
        } else throw new Exception("Refresh token is expired!");
    }

}
