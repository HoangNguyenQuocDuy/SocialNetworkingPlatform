package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.model.ChatMessage;
import api.socialPlatform.ApiForSocialApp.repositories.IChatMessageRepo;
import api.socialPlatform.ApiForSocialApp.services.IChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageServiceImpl implements IChatMessageService {
    @Autowired
    private IChatMessageRepo chatMessageRepo;

    @Override
    public ChatMessage createChatMessage(ChatMessage chatMessage) {
        chatMessage.setId(UUID.randomUUID());
        chatMessage.setCreatedAt(new Date());
        return chatMessageRepo.save(chatMessage);
    }

    @Override
    public void deleteChatMessage(ChatMessage chatMessage) {
        chatMessageRepo.delete(chatMessage);
    }

    @Override
    public List<ChatMessage> getChatMessageByRoomId(UUID roomId) {
        return chatMessageRepo.findChatMessageByChatRoomId(roomId);
    }
}
