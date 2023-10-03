package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.UserResponseDto;
import api.socialPlatform.ApiForSocialApp.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {

    User saveUser(User user);
    Optional<UserResponseDto> getUserById(UUID userId);
    Optional<User> findUserById(UUID userId);
    List<UserResponseDto> getAllUser();
    User getUserByToken(String token);
}
