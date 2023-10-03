package api.socialPlatform.ApiForSocialApp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserRequestDto {
    private String username;
    private String currentName;
    private String email;
    private String password;
    private String imageUrl;
}
