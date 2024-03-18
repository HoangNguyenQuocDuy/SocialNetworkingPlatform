package api.socialPlatform.ApiForSocialApp.repositories;

import api.socialPlatform.ApiForSocialApp.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;
import java.util.UUID;

public interface IChatRoomRepo extends MongoRepository<ChatRoom, UUID> {
    Set<ChatRoom> findChatRoomByUsersUserId(UUID userId);
}
