package api.socialPlatform.ApiForSocialApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Message {
    @Id
    private UUID messageId;
    private String text;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "userId")
    @JsonBackReference
    private User user;

//    @ManyToOne()
//    @JoinColumn(name = "roomId", nullable = false, referencedColumnName = "roomId")
//    @JsonBackReference
//    private Room room;
}
