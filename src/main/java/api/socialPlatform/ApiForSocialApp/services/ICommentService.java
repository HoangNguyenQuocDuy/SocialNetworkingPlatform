package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.CommentRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.CommentResponseDto;
import api.socialPlatform.ApiForSocialApp.model.Comment;
import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ICommentService {
    CommentResponseDto createComment(CommentRequestDto commentDto, UUID postId, User user) throws Exception;
    List<CommentResponseDto> getAllComments(UUID postId, Pageable pageable);
    void deleteComment(UUID postId, UUID commentId, User user) throws Exception;
    CommentResponseDto updateComment(UUID postId, UUID commentId, UUID userId, CommentRequestDto commentRequestDto) throws Exception;

}
