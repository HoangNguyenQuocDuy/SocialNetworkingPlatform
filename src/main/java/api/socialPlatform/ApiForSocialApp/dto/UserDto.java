package api.socialPlatform.ApiForSocialApp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDto {
    private UUID userId;
    private String username;
    private String currentName;
    private String email;
    private String imageUrl;
}
