package api.socialPlatform.ApiForSocialApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID postId;
    @ElementCollection
    private List<String> postImageUrls;
    private long likes;
    private String postDescription;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "userId")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    @ManyToMany(mappedBy = "likedPosts")
    @JsonIgnore
    private Set<User> likeByUsers;

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

    public UUID getPostId() {
        return postId;
    }

    public List<String> getPostImageUrls() {
        return postImageUrls;
    }

    public long getLikes() {
        return likes;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Set<User> getLikeByUsers() {
        return likeByUsers;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public void setPostImageUrls(List<String> postImageUrls) {
        this.postImageUrls = postImageUrls;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setLikeByUsers(Set<User> likeByUsers) {
        this.likeByUsers = likeByUsers;
    }
}
