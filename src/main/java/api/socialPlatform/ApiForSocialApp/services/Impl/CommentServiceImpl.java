package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.model.Comments;
import api.socialPlatform.ApiForSocialApp.repositories.ICommentRepo;
import api.socialPlatform.ApiForSocialApp.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private ICommentRepo commentRepo;
    @Autowired
    private UserServiceImpl userService;

    @Override
    public Comments createComment(Comments comments, UUID postId, UUID userId) {
        return commentRepo.save(comments);
    }

    @Override
    public Comments findCommentById(UUID commentId) {
        return null;
    }

    @Override
    public Comments deleteComment(UUID commentId) {
        return null;
    }

    @Override
    public Comments updateComment(UUID commentId) {
        return null;
    }
}
