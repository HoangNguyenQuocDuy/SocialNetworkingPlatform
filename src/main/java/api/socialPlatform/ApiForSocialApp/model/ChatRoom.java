package api.socialPlatform.ApiForSocialApp.model;

import api.socialPlatform.ApiForSocialApp.dto.UserResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class ChatRoom {
    @Id
    private UUID id;
    private Set<UserResponseDto> users;
    private Date createdAt;

    @CreatedDate
    private Date updatedAt;

    @LastModifiedDate
    private String lastMessage;

    public void addUsers(Set<UserResponseDto> setUsers) {
        if (users == null) {
            users = new HashSet<>();

        }
        users.addAll(setUsers);
    }

    public void addUser(UserResponseDto user) {
        users.add(user);
    }
}
