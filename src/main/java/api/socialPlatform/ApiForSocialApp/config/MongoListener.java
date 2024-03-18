//package api.socialPlatform.ApiForSocialApp.config;
//
//import api.socialPlatform.ApiForSocialApp.model.ChatRoom;
//import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
//import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//public class MongoListener extends AbstractMongoEventListener<ChatRoom> {
//    @Override
//    public void onBeforeSave(BeforeSaveEvent<ChatRoom> event) {
//        ChatRoom chatRoom = event.getSource();
//        if (chatRoom.getId() == null) {
//            chatRoom.setId(UUID.randomUUID());
//        }
//    }
//}
