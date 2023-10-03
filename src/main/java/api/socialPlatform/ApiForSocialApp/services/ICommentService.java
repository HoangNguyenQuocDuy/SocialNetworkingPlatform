package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.CommentRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.CommentResponseDto;
import api.socialPlatform.ApiForSocialApp.model.Comment;
import api.socialPlatform.ApiForSocialApp.model.User;

import java.util.List;
import java.util.UUID;

public interface ICommentService {
    CommentResponseDto createComment(CommentRequestDto commentDto, UUID postId, User user) throws Exception;
    List<CommentResponseDto> getAllComment(UUID postId);
    void deleteComment(UUID postId, UUID commentId, UUID userId) throws Exception;
    CommentResponseDto updateComment(UUID postId, UUID commentId, UUID userId, CommentRequestDto commentRequestDto) throws Exception;

}
