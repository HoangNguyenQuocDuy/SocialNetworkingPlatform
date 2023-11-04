package api.socialPlatform.ApiForSocialApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String token;
    private Date expiredAt;
    private Date createAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @JsonBackReference
    private User user;

    public UUID getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public User getUser() {
        return user;
    }

    @PrePersist
    private void onCreate() {
        this.createAt = new Date();
    }
}
