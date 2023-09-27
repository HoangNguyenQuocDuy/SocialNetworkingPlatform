package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.model.Comments;

import java.util.UUID;

public interface ICommentService {
    Comments createComment(Comments comments, UUID postId, UUID userId);
    Comments findCommentById(UUID commentId);
    Comments deleteComment(UUID commentId);
    Comments updateComment(UUID commentId);

}
