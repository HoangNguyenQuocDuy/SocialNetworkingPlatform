package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.dto.PostRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.PostResponseDto;
import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.model.ResponseObject;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IPostRepo;
import api.socialPlatform.ApiForSocialApp.services.IPostService;
import api.socialPlatform.ApiForSocialApp.services.IUserService;
import api.socialPlatform.ApiForSocialApp.services.Impl.PostServiceImp;
import api.socialPlatform.ApiForSocialApp.services.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    @Autowired
    private IPostService postServiceImp;
    @Autowired
    private IUserService userServiceImp;

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getAllPosts(@RequestParam(defaultValue = "2")
                                                          int size, Pageable pageable) {
        try {
            List<PostResponseDto> posts = postServiceImp.getAllPost(pageable);

            if (posts.size() > 0) {
                return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                        new ResponseObject("OK", "Get all posts successfully!", posts)
                );
            }

            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(
                    new ResponseObject("FAILED", "Posts is empty", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", "Failed when trying create post!", e.getMessage())
            );
        }
    }
    @PostMapping("/save")
    public ResponseEntity<ResponseObject> submitPost(@RequestBody PostRequestDto postDto,
                       @RequestHeader("Authorization") String token) {
//        if(user != null) {
            try {
                User user = userServiceImp.getUserByToken(token.substring(7));
                Post postSave = postServiceImp.createPost(postDto, user);
                return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(
                        new ResponseObject("OK", "Create post successfully!", PostResponseDto.fromPost(postSave))
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                        new ResponseObject("FAILED", "Failed when trying create post!", e.getMessage())
                );
            }
//        }
//        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(
//                new ResponseObject("FAILED", "Failed when trying create post!", "User not found!")
//        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseObject> getAllPosts(@PathVariable UUID userId) {
        try {
            List<PostResponseDto> posts = postServiceImp.getAllPostsByUserId(userId);
            if (posts != null) {
                return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                        new ResponseObject("OK", "Get all posts successfully!", posts)
                );
            } else {
                return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(
                        new ResponseObject("FAILED", "User doesn't has any post", null)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", "Failed when get posts by userId", e.getMessage())
            );
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponseObject> getPostById(@PathVariable UUID postId) {
        try {
            PostResponseDto postResponseDto = postServiceImp.getPostById(postId);
            if (postResponseDto != null) {
                return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                        new ResponseObject("OK", "Get post successfully!", postResponseDto)
                );
            } else {
                return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(
                        new ResponseObject("FAILED", "Post not found!", null)
                );
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.valueOf(500)).body(
                    new ResponseObject("FAILED", "Failed when get post by ID!", e.getMessage())
            );
        }
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ResponseObject> deletePost(@PathVariable UUID postId, @RequestHeader("Authorization") String token) {
//        if (user != null) {
            try {
                User user = userServiceImp.getUserByToken(token.substring(7));
                postServiceImp.deletePost(postId, user.getUserId());
                return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                        new ResponseObject("OK", "Deleted post successfully", postId)
                );
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.valueOf(500)).body(
                        new ResponseObject("FAILED", "Failed when trying delete post!", e.getMessage())
                );
            }
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<ResponseObject> updatePost(@PathVariable UUID postId,
                                                     @RequestBody PostRequestDto post,
                                                     @RequestHeader("Authorization") String token) {
//        if (user != null) {
            try {
                User user = userServiceImp.getUserByToken(token.substring(7));
                Post postUpdate = postServiceImp.updatePost(postId, post, user.getUserId());
                return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                        new ResponseObject("OK", "Updated post successfully", PostResponseDto.fromPost(postUpdate))
                );
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.valueOf(500)).body(
                        new ResponseObject("FAILED", "Failed when trying update post!", e.getMessage())
                );
            }
//        }
//        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(
//                new ResponseObject("FAILED", "Failed when trying update post!", "User not found!")
//        );
    }

    @PutMapping("/update/{postId}/like")
    public ResponseEntity<ResponseObject> likePost(@PathVariable UUID postId,
                                                   @RequestHeader("Authorization") String token) {

//        if (user != null) {
            try {
                User user = userServiceImp.getUserByToken(token.substring(7));
                PostResponseDto postResponseDto = postServiceImp.likedPost(user, postId);

                return ResponseEntity.status(HttpStatus.valueOf(200)).body(
                        new ResponseObject("OK", "Likes post successfully!", postResponseDto)
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.valueOf(500)).body(
                        new ResponseObject("FAILED", "Failed when trying likes post!", e.getMessage())
                );
            }
//        }
//        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(
//                new ResponseObject("FAILED", "Failed when trying likes post!", "User not found!")
//        );
    }
}
