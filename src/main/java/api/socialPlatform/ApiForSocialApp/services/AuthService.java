package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.AuthRequest;
import api.socialPlatform.ApiForSocialApp.dto.AuthResponse;
import api.socialPlatform.ApiForSocialApp.dto.UserRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.UserResponseDto;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IUserRepo;
import api.socialPlatform.ApiForSocialApp.services.Impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserServiceImpl userService;

    public ResponseEntity<?> authenticate(AuthRequest request, HttpServletResponse response) {
        try {
            User user = userRepo.findByUsername(request.getUsername()).orElseThrow(()-> new Exception("User not found!"));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var jwtAccessToken = jwtService.generateToken(user);
            var jwtRefreshToken = jwtService.generateRefreshToken(user);

            response.setHeader("Authentication", "Bearer " + jwtAccessToken);
            response.setHeader("Refresh-Token", jwtRefreshToken);

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
    }
