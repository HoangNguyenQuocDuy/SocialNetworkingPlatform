package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.UserResponseDto;
import api.socialPlatform.ApiForSocialApp.model.RefreshToken;
import api.socialPlatform.ApiForSocialApp.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {

    User saveUser(User user);
    Optional<UserResponseDto> getUserById(UUID userId);
    Optional<UserResponseDto> getUserByUserName(String username);
    Optional<User> findUserById(UUID userId);
    List<UserResponseDto> getAllUser();
    User getUserByToken(String token);
    Optional<User> findUserByRefreshToken(RefreshToken refreshToken);
    List<UserResponseDto> findUsersByCurrentName(String currentName);
    void forgotPassword(String userId) throws Exception;
    void resetPassword(String email, String verificationCode, String newPassword) throws Exception;
//
//    List<UserResponseDto> deleteFriend(UUID userId);
}
