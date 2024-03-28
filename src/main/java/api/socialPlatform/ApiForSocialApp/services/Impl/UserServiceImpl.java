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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepo userRepo;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${secret.key}")
    private String secretKey;


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
    public List<UserResponseDto> findUsersByCurrentName(String currentName) {
        List<User> users = userRepo.findAllByCurrentNameContaining(currentName);
        List<UserResponseDto> usersResponse = new ArrayList<>();

        for (User user: users) {
            usersResponse.add(UserResponseDto.fromUser(user));
        }

        return usersResponse;
    }

    @Override
    public void forgotPassword(String email) throws Exception {
        Optional<User> user = userRepo.findByEmail(email);
        if (!user.isPresent()) {
            throw new RuntimeException("User with ID " + user.get().getUserId() + " not found!");
        }
        String verificationCode = generateVerificationCode();
        user.get().setResetPasswordCode(verificationCode);
        userRepo.save(user.get());

        sendVerificationCodeByEmail(email, verificationCode);
    }

    @Override
    public void resetPassword(String email, String verificationCode, String newPassword) throws Exception {
        Optional<User> user = userRepo.findByEmail(email);

        if (!user.isPresent()) {
            throw new Exception("User with ID " + user.get().getUserId() + " not found!");
        }
        if (!user.get().getResetPasswordCode().equals(verificationCode)) {
            throw new IllegalArgumentException("Invalid verification code");
        }
        user.get().setPassword(passwordEncoder.encode(newPassword));
        user.get().setResetPasswordCode(null);
        userRepo.save(user.get());
    }


    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
    public void sendVerificationCodeByEmail(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("YOUR VERIFICATION CODE FROM SOCIAL APP");
        message.setText("Your verification code is: " + verificationCode);

        javaMailSender.send(message);
    }
}
