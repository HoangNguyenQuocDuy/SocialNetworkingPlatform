package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.model.ResponseObject;
import api.socialPlatform.ApiForSocialApp.services.IChatMessageService;
import api.socialPlatform.ApiForSocialApp.services.IChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ChatMessageController{
    @Autowired
    private IChatMessageService chatMessageService;

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ResponseObject> getChatMessages(@PathVariable UUID roomId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get messages for room successfully!",
                            chatMessageService.getChatMessageByRoomId(roomId)
                    ));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseObject("FAILED", "Failed when get messages for room!", e.getMessage())
            );
        }
    }
}
