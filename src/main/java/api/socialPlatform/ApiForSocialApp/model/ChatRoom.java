package api.socialPlatform.ApiForSocialApp.model;

import api.socialPlatform.ApiForSocialApp.dto.UserResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rooms")
public class ChatRoom implements Comparable<ChatRoom> {
    @Id
    private UUID id;
    private Set<UserResponseDto> users;
    private String lastMessage;
    private String roomName;
    private Date updatedAt;
    private Date createdAt;
    private UserResponseDto owner;


    public void addUsers(Set<UserResponseDto> setUsers) {
        if (users == null) {
            users = new HashSet<>();

        }
        users.addAll(setUsers);
    }

    public void addUser(UserResponseDto user) {
        users.add(user);
    }

    public void removeUser(UserResponseDto user) {
        users.remove(user);
    }

    @Override
    public int compareTo(ChatRoom otherChatRoom) {
        Date updatedDate1 = this.getUpdatedAt();
        Date updatedDate2 = otherChatRoom.getUpdatedAt();

        if (updatedDate1 != null && updatedDate2!=null) {
            return updatedDate2.compareTo(updatedDate1);
        } else if (updatedDate1 == null && updatedDate2==null) {
            return 1;
        } else if (updatedDate1 != null) {
            return -1;
        } else {
            return otherChatRoom.getCreatedAt().compareTo(this.getCreatedAt());
        }
    }
}
