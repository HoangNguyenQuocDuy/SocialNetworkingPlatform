package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.model.ChatRoom;

import java.util.Set;
import java.util.UUID;

public interface IChatRoomService {
    ChatRoom createRoom(Set<UUID> userId) throws Exception;
    Set<ChatRoom> findRoomsByUserId(UUID userId) throws Exception;
    void deleteRoom(UUID roomId);
    ChatRoom addUserToRoom(UUID roomId, UUID userId);
}