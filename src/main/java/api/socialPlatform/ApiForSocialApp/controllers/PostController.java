package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.dto.PostRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.PostResponseDto;
import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.model.ResponseObject;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.services.Impl.PostServiceImp;
import api.socialPlatform.ApiForSocialApp.services.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    @Autowired
    private PostServiceImp postServiceImp;
    @Autowired
    private UserServiceImpl userServiceImp;
    @PostMapping("/save")
    public ResponseEntity<ResponseObject> submitPost(@RequestBody PostRequestDto postDto,
                       @RequestHeader("Authorization") String token) {
        User user = userServiceImp.getUserByToken(token.substring(7));
        if(user != null) {
            try {
                Post postSave = postServiceImp.createPost(postDto, user);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Create post successfully!", PostResponseDto.fromPost(postSave))
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        new ResponseObject("FAILED", "Failed when trying create post!", e.getMessage())
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("FAILED", "Failed when trying create post!", "User not found!")
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseObject> getAllPosts(@PathVariable UUID userId) {
        try {
            List<PostResponseDto> posts = postServiceImp.getAllPosts(userId);
            if (posts != null) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Get all posts successfully!", posts)
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "User doesn't has any post", new ArrayList<>())
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseObject("FAILED", "Failed when get posts by userId", e.getMessage())
            );
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponseObject> getPostById(@PathVariable UUID postId) {
        try {
            PostResponseDto postResponseDto = postServiceImp.getPostById(postId);
            if (postResponseDto != null) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Get post successfully!", postResponseDto)
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Post not found!", null)
                );
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseObject("FAILED", "Failed when get post by ID!", e.getMessage())
            );
        }
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ResponseObject> deletePost(@PathVariable UUID postId, @RequestHeader("Authorization") String token) {
        User user = userServiceImp.getUserByToken(token.substring(7));
        if (user != null) {
            try {
                postServiceImp.deletePost(postId, user.getUserId());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Deleted post successfully", postServiceImp.getAllPosts(user.getUserId()))
                );
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        new ResponseObject("FAILED", "Failed when trying delete post!", e.getMessage())
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("FAILED", "Failed when trying delete post!", "User not found!")
        );
    }
    @PutMapping("/update/{postId}")
    public ResponseEntity<ResponseObject> updatePost(@PathVariable UUID postId,
                                                     @RequestBody PostRequestDto post,
                                                     @RequestHeader("Authorization") String token) {
        User user = userServiceImp.getUserByToken(token.substring(7));
        if (user != null) {
            try {
                Post postUpdate = postServiceImp.updatePost(postId, post, user.getUserId());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Updated post successfully", PostResponseDto.fromPost(postUpdate))
                );
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        new ResponseObject("FAILED", "Failed when trying update post!", e.getMessage())
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("FAILED", "Failed when trying update post!", "User not found!")
        );
    }
}
