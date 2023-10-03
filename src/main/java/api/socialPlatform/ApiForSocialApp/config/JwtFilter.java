package api.socialPlatform.ApiForSocialApp.config;

import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IUserRepo;
import api.socialPlatform.ApiForSocialApp.services.JWTService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private IUserRepo userRepo;

    @Value("${secret.key}")
    private String secretKey;
    @Autowired
    private JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader(AUTHORIZATION);

        if(authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            try {
                String accessToken = authenticationHeader.substring("Bearer ".length());
                Algorithm algorithm =Algorithm.HMAC256(secretKey.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(accessToken);

                if (decodedJWT.getExpiresAt().before(new Date())) {
                    String refreshToken = request.getHeader("Refresh-Token");
                    DecodedJWT decodedRefreshToken = verifier.verify(refreshToken);
                    if(decodedRefreshToken.getExpiresAt().before(new Date())) {
                        String newAccessToken = generateNewAccessTokenFromRefreshToken(decodedRefreshToken);
                        response.setHeader("Authorization", "Bearer " + newAccessToken);
                        DecodedJWT decodedNewJWT = verifier.verify(newAccessToken);

                        handleAuthorization(decodedNewJWT);
                        filterChain.doFilter(request, response);
                    }
                } else {
                    handleAuthorization(decodedJWT);
                    filterChain.doFilter(request, response);
                }
            } catch (Exception e) {
                ErrorResponse errorResponse = new ErrorResponse(FORBIDDEN, e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(errorResponse.getStatusCodeValue());
                new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
            }
        }else filterChain.doFilter(request, response);
    }

    private String generateNewAccessTokenFromRefreshToken(DecodedJWT decodedRefreshToken) throws Exception {
        Optional<User> userOptional = userRepo.findByUsername(decodedRefreshToken.getSubject());
        if (userOptional.isPresent()) {
            return jwtService.generateToken(userOptional.get());
        } else throw new Exception("User not found!");
    }

    private void handleAuthorization(DecodedJWT decodedJWT) throws Exception {
        String username = decodedJWT.getSubject();
        userRepo.findByUsername(username).orElseThrow(()->new Exception("User not found!"));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}