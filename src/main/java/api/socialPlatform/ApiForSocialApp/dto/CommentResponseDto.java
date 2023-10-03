package api.socialPlatform.ApiForSocialApp.dto;

import api.socialPlatform.ApiForSocialApp.model.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class CommentResponseDto {
    private String content;
    private UUID userId;
    private UUID postId;
    private UUID commentId;
    private Date createdAt;
    private Date updatedAt;

    public static CommentResponseDto fromComment(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .userId(comment.getUser().getUserId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdateAt())
                .build();
    }
}
