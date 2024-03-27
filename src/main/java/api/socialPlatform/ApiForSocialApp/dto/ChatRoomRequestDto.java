package api.socialPlatform.ApiForSocialApp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class ChatRoomRequestDto {
    private String roomName;
    private Set<UUID> usersIds;
    private UUID ownerId;
}
