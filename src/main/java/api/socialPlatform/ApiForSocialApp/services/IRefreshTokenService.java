package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.model.RefreshToken;
import api.socialPlatform.ApiForSocialApp.model.User;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken getRefreshTokenByToken(String token);
    RefreshToken getRefreshTokenByUsername(String username);
}
