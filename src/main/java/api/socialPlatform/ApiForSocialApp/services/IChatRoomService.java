package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.ChatRoomRequestDto;
import api.socialPlatform.ApiForSocialApp.model.ChatRoom;
import api.socialPlatform.ApiForSocialApp.model.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IChatRoomService {
    ChatRoom createRoom(ChatRoomRequestDto chatRoomBody) throws Exception;
    ChatRoom getRoomById(UUID roomId) throws Exception;
    List <ChatRoom> findRoomsByUserId(UUID userId) throws Exception;
    void deleteRoom(UUID roomId);
    ChatRoom addUserToRoom(UUID roomId, List<String> userId) throws Exception;
    ChatRoom updateRoom(ChatRoom chatRoom);
    ChatRoom deleteUserFromRoom(UUID roomId, UUID userId) throws Exception;
}