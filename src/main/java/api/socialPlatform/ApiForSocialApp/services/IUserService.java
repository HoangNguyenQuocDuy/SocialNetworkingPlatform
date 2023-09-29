package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.UserDto;
import api.socialPlatform.ApiForSocialApp.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {

    User saveUser(User user);
    Optional<UserDto> getUserById(UUID userId);
    Optional<User> findUserById(UUID userId);
    List<UserDto> getAllUser();
    User getUserByToken(String token);
}
