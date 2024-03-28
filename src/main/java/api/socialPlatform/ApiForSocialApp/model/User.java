package api.socialPlatform.ApiForSocialApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    private String username;
    private String currentName;
    private String email;
    private String password;
    private String imageUrl;
    private String resetPasswordCode;
    @ElementCollection
    private Set<UUID> friendIds;
    @ElementCollection
    private Set<UUID> invitorIds;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;
    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;
    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "user_liked_posts",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "postId"))
    @JsonManagedReference
    private Set<Post> likedPosts;
    public User(String username, String currentName, String password, String imageUrl) {
        this.username = username;
        this.currentName = currentName;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    @PrePersist
    private void onCreate() {
        this.posts = new HashSet<Post>();
    }

    public Set<Post> setPosts(Post post) {
        posts.add(post);
        return posts;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getCurrentName() {
        return currentName;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Set<UUID> getFriendIds() {
        return friendIds;
    }

    public Set<UUID> getInvitorIds() {
        return invitorIds;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public Set<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFriendIds(Set<UUID> friendIds) {
        this.friendIds = friendIds;
    }

    public void setInvitorIds(Set<UUID> invitorIds) {
        this.invitorIds = invitorIds;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setLikedPosts(Set<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public String getResetPasswordCode() {
        return resetPasswordCode;
    }

    public void setResetPasswordCode(String resetPasswordCode) {
        this.resetPasswordCode = resetPasswordCode;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
