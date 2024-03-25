package api.socialPlatform.ApiForSocialApp.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class ChatRoomRequestDto {
    private String roomName;
    private Set<UUID> usersIds;
}
