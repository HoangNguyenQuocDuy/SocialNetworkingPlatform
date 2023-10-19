package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.model.RefreshToken;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IRefreshTokenRepo;
import api.socialPlatform.ApiForSocialApp.services.IRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenServiceImp implements IRefreshTokenService {
    @Autowired
    private IRefreshTokenRepo refreshTokenRepo;

    private String generateString() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken oldRefreshToken = user.getRefreshToken();
        if (oldRefreshToken != null && oldRefreshToken.getExpiredAt().after(new Date()))
            refreshTokenRepo.deleteByToken(oldRefreshToken.getToken());

        RefreshToken refreshToken = RefreshToken
                .builder()
                .user(user)
                .token(generateString())
                .expiredAt(new Date(System.currentTimeMillis() + 70*60*1000))
                .build();

        return refreshTokenRepo.save(refreshToken);
    }

    @Override
    public RefreshToken getRefreshTokenByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    @Override
    public RefreshToken getRefreshTokenByUsername(String username) {
        return refreshTokenRepo.findByUserUsername(username);
    }
}
