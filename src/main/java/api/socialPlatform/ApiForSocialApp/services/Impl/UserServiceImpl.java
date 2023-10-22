package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.dto.UserResponseDto;
import api.socialPlatform.ApiForSocialApp.model.RefreshToken;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IUserRepo;
import api.socialPlatform.ApiForSocialApp.services.IUserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepo userRepo;

    @Value("${secret.key}")
    private String secretKey;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(user);
    }

    @Override
    public Optional<UserResponseDto> getUserById(UUID userId) {
        Optional<User> userOptional = userRepo.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponseDto userDto = UserResponseDto.fromUser(user);

            return Optional.of(userDto);
        } else return Optional.empty();
    }

    @Override
    public Optional<UserResponseDto> getUserByUserName(String username) {
        Optional<User> userOptional = userRepo.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponseDto userDto = UserResponseDto.fromUser(user);

            return Optional.of(userDto);
        } else return Optional.empty();
    }

    @Override
    public Optional<User> findUserById(UUID userId) {
        return userRepo.findByUserId(userId);
    }

    @Override
    public List<UserResponseDto> getAllUser() {
        return userRepo.findAll().stream().map(
                user -> UserResponseDto.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .currentName(user.getCurrentName())
                        .imageUrl(user.getImageUrl())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public User getUserByToken(String token) {
        try {
            Algorithm algorithm =Algorithm.HMAC256(secretKey.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String username = decodedJWT.getSubject();

            return userRepo.findByUsername(username).orElse(null);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public Optional<User> findUserByRefreshToken(RefreshToken refreshToken) {
        return userRepo.findByRefreshToken(refreshToken);
    }

    @Override
    public List<UserResponseDto> getListFriends(UUID userId) throws Exception {
        Optional<User> userOptional = userRepo.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            try {
                List<UserResponseDto> friends = caseToListUserResponseDto(user);

                return friends;
            } catch(Exception exception) {
                throw new Exception(exception.getMessage());
            }

        }

        return null;
    }

    @Override
    public List<UserResponseDto> addFriendToUser(UUID userId, UUID friendId) {
        Optional<User> userOptional = userRepo.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setFriendIds(friendId);

           return caseToListUserResponseDto(user);
        }
        return null;
    }

    private List<UserResponseDto> caseToListUserResponseDto(User user) {
        List<UserResponseDto> friends = new ArrayList<>();

        user.getFriendIds().forEach(id -> {
            User userFriend = userRepo.findByUserId(id).get();
            UserResponseDto userResponseDto = UserResponseDto.fromUser(userFriend);
            friends.add(userResponseDto);
        });

        return friends;
    }

//    @Override
//    public List<UserResponseDto> deleteFriend(UUID userId) {
//        return null;
//    }
}
