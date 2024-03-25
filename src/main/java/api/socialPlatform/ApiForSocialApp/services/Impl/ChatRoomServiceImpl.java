package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.dto.ChatRoomRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.UserResponseDto;
import api.socialPlatform.ApiForSocialApp.model.ChatRoom;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IChatRoomRepo;
import api.socialPlatform.ApiForSocialApp.repositories.IUserRepo;
import api.socialPlatform.ApiForSocialApp.services.IChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatRoomServiceImpl implements IChatRoomService {

    @Autowired
    private IChatRoomRepo roomRepo;
    @Autowired
    private UserServiceImpl userService;
    @Override
    public ChatRoom createRoom(ChatRoomRequestDto chatRoomBody) throws Exception {
            Set<UserResponseDto> users = new HashSet<>();

            chatRoomBody.getUsersIds().forEach(userId -> {
                Optional<UserResponseDto> user = userService.getUserById(userId);
                if (user.isPresent()) {
                    users.add(user.get());
                } else {
                    try {
                        throw new Exception("User not found!");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            ChatRoom newChatRoom = ChatRoom.builder()
                    .id(UUID.randomUUID())
                    .users(users)
                    .roomName(chatRoomBody.getRoomName())
                    .build();

            return roomRepo.save(newChatRoom);

    }

    @Override
    public ChatRoom getRoomById(UUID roomId) throws Exception {
        Optional<ChatRoom> chatRoom = roomRepo.findById(roomId);
        if (chatRoom.isPresent()) return chatRoom.get();
        else throw new Exception("Room with id " + roomId + " not found!");
    }

    @Override
    public Set<ChatRoom> findRoomsByUserId(UUID userId) throws Exception {
        Set<ChatRoom> roomsFind = new HashSet<>();
        Optional<UserResponseDto> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Set<ChatRoom> userRooms = roomRepo.findChatRoomByUsersUserId(user.get().getUserId());
            roomsFind.addAll(userRooms);
        } else {
            throw new Exception("User not found!");
        }

        return roomsFind;
    }

    @Override
    public void deleteRoom(UUID roomId) {

    }

    @Override
    public ChatRoom addUserToRoom(UUID roomId, UUID userId) {
        return null;
    }

    @Override
    public ChatRoom updateRoom(ChatRoom chatRoom) {
        return roomRepo.save(chatRoom);
    }
}
