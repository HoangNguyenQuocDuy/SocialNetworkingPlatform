package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.dto.CommentRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.PostResponseDto;
import api.socialPlatform.ApiForSocialApp.model.ResponseObject;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.services.Impl.CommentServiceImpl;
import api.socialPlatform.ApiForSocialApp.services.Impl.PostServiceImp;
import api.socialPlatform.ApiForSocialApp.services.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    @Autowired
    private PostServiceImp postService;
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/save/{postId}")
    public ResponseEntity<ResponseObject> submitComment(@PathVariable UUID postId,
            @RequestBody CommentRequestDto commentDto, @RequestHeader("Authorization") String token) {
        User user = userService.getUserByToken(token.substring(7));
        if (user != null) {
            try {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Create comment successfully!",
                                commentService.createComment(commentDto, postId, user))
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        new ResponseObject("FAILED", "Failed when create comments for post!", e.getMessage())
                );
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", "Failed when trying get create comment!", "User not found!")
            );
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResponseObject> getAllCommentsByPostId(@PathVariable UUID postId) {
        PostResponseDto post = postService.getPostById(postId);
        if (post != null) {
            try {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Get comments successfully!", commentService.getAllComment(postId))
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        new ResponseObject("FAILED", "Failed when get comments for post!", e.getMessage())
                );
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", "Failed when trying get comments for post!", "Post not found!")
            );
        }
    }

    @DeleteMapping("/{postId}/delete/{commentId}")
    public ResponseEntity<ResponseObject> deleteComment(@PathVariable UUID postId,
                                                        @PathVariable UUID commentId,
                                                        @RequestHeader("Authorization") String token) {
        User user = userService.getUserByToken(token.substring(7));
        if (user != null) {
            try {
                commentService.deleteComment(postId, commentId, user.getUserId());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Update comment successfully!",
                                commentService.getAllComment(postId))
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        new ResponseObject("FAILED", "Failed when delete comment!", e.getMessage())
                );
            }
        }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", "Failed when delete comment!", "User not found!")
            );
    }

    @PutMapping("/{postId}/update/{commentId}")
    public ResponseEntity<ResponseObject> updateComment(@PathVariable UUID postId,
                                                        @PathVariable UUID commentId,
                                                        @RequestHeader("Authorization") String token,
                                                        @RequestBody CommentRequestDto commentRequestDto) {
        User user = userService.getUserByToken(token.substring(7));
        if (user != null) {
            try {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Delete comment successfully!",
                                commentService.updateComment(postId, commentId, user.getUserId(), commentRequestDto)
                        ));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        new ResponseObject("FAILED", "Failed when delete comment!", e.getMessage())
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("FAILED", "Failed when update comment!", "User not found!")
        );
    }
}
