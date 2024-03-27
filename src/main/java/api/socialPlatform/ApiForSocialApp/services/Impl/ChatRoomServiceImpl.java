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

import javax.swing.text.html.Option;
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

            Optional<UserResponseDto> owner = userService.getUserById(chatRoomBody.getOwnerId());
            if (!owner.isPresent()) {
                throw new Exception("Owner's ID went wrong");
            }
            ChatRoom newChatRoom = ChatRoom.builder()
                    .id(UUID.randomUUID())
                    .users(users)
                    .owner(owner.get())
                    .roomName(chatRoomBody.getRoomName())
                    .createdAt(new Date())
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
    public List<ChatRoom> findRoomsByUserId(UUID userId) throws Exception {
        List<ChatRoom> roomsFind = new ArrayList<>();
        Optional<UserResponseDto> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Set<ChatRoom> userRooms = roomRepo.findChatRoomByUsersUserId(user.get().getUserId());
            roomsFind.addAll(userRooms);
            Collections.sort(roomsFind);
        } else {
            throw new Exception("User not found!");
        }

        return roomsFind;
    }

    @Override
    public void deleteRoom(UUID roomId) {

    }

    @Override
    public ChatRoom addUserToRoom(UUID roomId, List<String> userIds) throws Exception {
        Optional<ChatRoom> chatRoom = roomRepo.findById(roomId);
        List<UserResponseDto> users = new ArrayList<>();

        for (String userIdString : userIds) {
            try {
                UUID userId = UUID.fromString(userIdString);
                Optional<UserResponseDto> user = userService.getUserById(userId);
                if (!user.isPresent()) {
                    throw new Exception("User with id " + userId + " does not exist!");
                }
                users.add(user.get());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid UUID format for user id: " + userIdString);
            }
        }

        if (!chatRoom.isPresent()) {
            throw new Exception("ChatRoom with id " + roomId + " does not exit!");
        }

        Set<UserResponseDto> userSet = new HashSet<>(users);

        chatRoom.get().addUsers(userSet);
        chatRoom.get().setUpdatedAt(new Date());

        roomRepo.save(chatRoom.get());

        return chatRoom.get();
    }

    @Override
    public ChatRoom updateRoom(ChatRoom chatRoom) {
        return roomRepo.save(chatRoom);
    }

    @Override
    public ChatRoom deleteUserFromRoom(UUID roomId, UUID userId) throws Exception {
        Optional<ChatRoom> room = roomRepo.findById(roomId);
        Optional<User> user = userService.findUserById(userId);

        if (!user.isPresent()) {
            throw new Exception("User with ID " + userId + " not found");
        }
        if (!room.isPresent()) {
            throw new Exception("Room with ID " + roomId + " not found");
        }

        boolean isUserInRoom = false;
        for (UserResponseDto user1 : room.get().getUsers()) {
            if (user1.getUserId().equals(userId)) {
                isUserInRoom = true;
                break;
            }
        }
        if (!isUserInRoom) {
            throw new Exception("User not found in room with ID " + roomId);
        }

        room.get().removeUser(UserResponseDto.fromUser(user.get()));

        roomRepo.save(room.get());

        return room.get();
    }
}
