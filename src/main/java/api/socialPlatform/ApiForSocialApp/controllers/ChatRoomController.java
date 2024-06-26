package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.dto.ChatRoomRequestDto;
import api.socialPlatform.ApiForSocialApp.model.ChatMessage;
import api.socialPlatform.ApiForSocialApp.model.ChatRoom;
import api.socialPlatform.ApiForSocialApp.model.ResponseObject;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.services.IChatMessageService;
import api.socialPlatform.ApiForSocialApp.services.IChatRoomService;
import api.socialPlatform.ApiForSocialApp.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/v1/rooms")
public class ChatRoomController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private IChatRoomService chatRoomService;
    @Autowired
    private IChatMessageService chatMessageService;
    @Autowired
    private IUserService userService;

    @MessageMapping("/chat.sendMessage")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) throws Exception {
        ChatMessage chatMessageSaved = chatMessageService.createChatMessage(chatMessage);
        ChatRoom chatRoom = chatRoomService.getRoomById(chatMessageSaved.getChatRoomId());
        chatRoom.setUpdatedAt(new Date());
        chatRoom.setLastMessage(chatMessageSaved.getContent());
        chatRoomService.updateRoom(chatRoom);

        messagingTemplate.convertAndSend(String.format("/rooms/%s", chatMessageSaved.getChatRoomId()), chatMessageSaved);

        return chatMessage;
    }

    @MessageMapping("/chat.createRoom")
    public ChatRoom addRoom(@Payload ChatRoomRequestDto chatRoomRequestDto) {
        try {
            ChatRoom chatRoom = chatRoomService.createRoom(chatRoomRequestDto);
            messagingTemplate.convertAndSend("/rooms", chatRoom);

            return chatRoom;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/chat.addUserToChatRoom")
    public ChatRoom addUserToChatRoom(@Payload Map<String, Object> payload) {
        try {
            UUID roomId = UUID.fromString((String) payload.get("roomId"));
            List<String> userIds = (List<String>) payload.get("usersIds");

            ChatRoom chatRoom = chatRoomService.addUserToRoom(roomId, userIds);
            messagingTemplate.convertAndSend(String.format("/rooms/%s/addUser", roomId), chatRoom);

            return chatRoom;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/chat.deleteUserFromRoom")
    public ChatRoom deleteUserFromRoom(@Payload Map<String, Object> payload) {
        try {
            UUID roomId = UUID.fromString((String) payload.get("roomId"));
            UUID userId = UUID.fromString((String) payload.get("userId"));

            ChatRoom chatRoom = chatRoomService.deleteUserFromRoom(roomId, userId);
            messagingTemplate.convertAndSend(String.format("/rooms/%s/deleteUser", roomId), chatRoom);

            return chatRoom;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseObject> getRoom(@PathVariable UUID roomId) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get room by id successfully", chatRoomService.getRoomById(roomId))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseObject("FAILED", "Failed when get room by id!", e.getMessage())
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getRooms(@RequestHeader("Authorization") String token) {
        try {
            User user = userService.getUserByToken(token.substring(7));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get rooms successfully", chatRoomService.findRoomsByUserId(user.getUserId()))
            );
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseObject("FAILED", "Failed when get room by userId!", e.getMessage())
            );
        }
    }

}
