package api.socialPlatform.ApiForSocialApp.dto;

import api.socialPlatform.ApiForSocialApp.model.Post;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PostResponseDto {
    private List<String> postImageUrls;
    private String postDescription;
    private int likes;
    private Date createdAt;
    private Date updatedAt;
    private UUID userId;
    private UUID postId;

    public static PostResponseDto fromPost(Post post) {
        return PostResponseDto.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .likes(post.getLikes())
                .postImageUrls(post.getPostImageUrls())
                .postDescription(post.getPostDescription())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
