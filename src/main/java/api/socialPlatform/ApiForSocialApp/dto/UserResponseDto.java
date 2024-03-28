package api.socialPlatform.ApiForSocialApp.dto;

import api.socialPlatform.ApiForSocialApp.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponseDto {
    private UUID userId;
    private String username;
    private String currentName;
    private String email;
    private String imageUrl;
    private String resetPasswordCode;

    public static UserResponseDto fromUser(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .currentName(user.getCurrentName())
                .email(user.getEmail())
                .resetPasswordCode(user.getResetPasswordCode())
                .imageUrl(user.getImageUrl())
                .build();
    }
}
