package api.socialPlatform.ApiForSocialApp.config;

import api.socialPlatform.ApiForSocialApp.repositories.IUserRepo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private IUserRepo userRepo;

    @Value("${secret.key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader(AUTHORIZATION);

        if(authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            try {
                String token = authenticationHeader.substring("Bearer ".length());
                Algorithm algorithm =Algorithm.HMAC256(secretKey.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                userRepo.findByUsername(username).orElseThrow(()->new Exception("User not found!"));
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                ErrorResponse errorResponse = new ErrorResponse(FORBIDDEN, e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(errorResponse.getStatusCodeValue());
                new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
            }
        }else filterChain.doFilter(request, response);
    }
}