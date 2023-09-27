package api.socialPlatform.ApiForSocialApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data

public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID commentId;
    @Column(insertable = false, updatable = false)
    private UUID userId;
    @Column(insertable = false, updatable = false)
    private UUID postId;
    private Date createdAt;
    private String content;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "userId")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false, referencedColumnName = "postId")
    @JsonBackReference
    private Post post;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date(System.currentTimeMillis());
    }

    public Comments(UUID userId, UUID postId, String content) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }
}
