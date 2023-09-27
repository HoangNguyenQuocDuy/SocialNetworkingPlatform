package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    @Value("${secret.key}")
    private String secretKey;

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 50*60*1000))
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
    }
    public String generateRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 70*60*1000))
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
    }
}
