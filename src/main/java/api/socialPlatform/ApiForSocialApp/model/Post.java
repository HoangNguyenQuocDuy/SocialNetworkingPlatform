package api.socialPlatform.ApiForSocialApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID postId;
    @ElementCollection
    private List<String> postImageUrls;
    private int likes;
    private String postDescription;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "userId")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    @PrePersist
    private void onCreate() {
        this.createdAt = new Date(System.currentTimeMillis());
        this.likes = 0;
        this.comments = new HashSet<Comment>();
    }
    @PreUpdate
    private void onUpdate() {
        this.createdAt = new Date(System.currentTimeMillis());
    }

    public Set<Comment> setComments(Comment comment) {
        comments.add(comment);
        return comments;
    }
}
