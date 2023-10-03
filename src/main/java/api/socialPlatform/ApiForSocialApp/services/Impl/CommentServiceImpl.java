package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.dto.CommentRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.CommentResponseDto;
import api.socialPlatform.ApiForSocialApp.model.Comment;
import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.ICommentRepo;
import api.socialPlatform.ApiForSocialApp.repositories.IPostRepo;
import api.socialPlatform.ApiForSocialApp.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private ICommentRepo commentRepo;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PostServiceImp postService;
    @Autowired
    private IPostRepo postRepo;

    @Override
    public CommentResponseDto createComment(CommentRequestDto commentDto, UUID postId, User user) throws Exception {
        Optional<Post> postOptional = postRepo.findById(postId);
        if (!postOptional.isPresent())
            throw new Exception("Post not found!");
        Post post = postOptional.get();
        Comment commentSave = Comment.builder()
                                        .post(post)
                                        .user(user)
                                        .content(commentDto.getContent())
                                        .build();
        commentRepo.save(commentSave);

        return CommentResponseDto.fromComment(commentSave);
    }

    @Override
    public List<CommentResponseDto> getAllComment(UUID postId) {
        List<Comment> comments = commentRepo.findByPostPostId(postId);
        return comments.stream().map(CommentResponseDto::fromComment).collect(Collectors.toList());
    }

    @Override
    public void deleteComment(UUID postId, UUID commentId, UUID userId) throws Exception {
        Optional<Comment> commentOptional = commentRepo.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (comment.getPost().getPostId().equals(postId) && comment.getUser().getUserId().equals(userId)) {
                commentRepo.deleteById(commentId);
                return;
            } else if (!comment.getPost().getPostId().equals(postId)) {
                throw new Exception("Post dont have this comment!");
            } else throw new Exception("You are not authorization!");
        }
        throw new Exception("Comment not found!");
    }

    @Override
    public CommentResponseDto updateComment(UUID postId, UUID commentId, UUID userId, CommentRequestDto commentRequestDto) throws Exception {
        Optional<Comment> commentOptional = commentRepo.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (comment.getPost().getPostId().equals(postId) && comment.getUser().getUserId().equals(userId)) {
                comment.setContent(commentRequestDto.getContent());
                comment.setUpdateAt(new Date(System.currentTimeMillis()));
                commentRepo.save(comment);

                return CommentResponseDto.fromComment(comment);
            } else if (!comment.getPost().getPostId().equals(postId)) {
                throw new Exception("Post dont have this comment!");
            } else throw new Exception("You are not authorization!");
        }
        throw new Exception("Comment not found!");
    }
}
