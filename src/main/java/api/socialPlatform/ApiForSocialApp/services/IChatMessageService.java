package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.model.ChatMessage;

import java.util.List;
import java.util.UUID;

public interface IChatMessageService {
    ChatMessage createChatMessage(ChatMessage chatMessage);
    void deleteChatMessage(ChatMessage chatMessage);
    List<ChatMessage> getChatMessageByRoomId(UUID roomId);
}
