package api.socialPlatform.ApiForSocialApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
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

    public User(String username, String currentName, String password, String imageUrl) {
        this.username = username;
        this.currentName = currentName;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public List<UUID> addFriend(UUID userId) {
        this.setFriendIds(userId);
        return (List<UUID>) friendIds;
    }

    @PrePersist
    private void onCreate() {
        this.posts = new HashSet<Post>();
    }

    public Set<Post> setPosts(Post post) {
        posts.add(post);
        return posts;
    }

    public Set<UUID> setFriendIds(UUID userId) {
        friendIds.add(userId);
        return friendIds;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
