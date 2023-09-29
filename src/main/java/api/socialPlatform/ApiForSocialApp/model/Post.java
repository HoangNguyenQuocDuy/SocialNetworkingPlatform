package api.socialPlatform.ApiForSocialApp.model;

import api.socialPlatform.ApiForSocialApp.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    private UUID postId;
    private String postImageUrl;
    private int likes;
    private String postDescription;
    private Date createdAt;
    private Date updatedAt;

//    @Column(insertable = false, updatable = false)
//    private UUID userId;
//    private String username;
//    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "userId")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<Comments> comments;

    @PrePersist
    private void onCreate() {
        this.createdAt = new Date(System.currentTimeMillis());
    }
    @PreUpdate
    private void onUpdate() {
        this.createdAt = new Date(System.currentTimeMillis());
    }

//    public Post(UUID userId, String username, String imageUrl, int likes, String postDescription) {
//        this.userId = userId;
//        this.username = username;
//        this.imageUrl = imageUrl;
//        this.likes = likes;
//        this.postDescription = postDescription;
//    }
}
