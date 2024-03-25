package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.PostRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.PostResponseDto;
import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPostService {
    Post createPost(PostRequestDto post, User user);
    PostResponseDto getPostById(UUID postId);
    List<PostResponseDto> getAllPostsByUserId(UUID userId);
    List<PostResponseDto> getAllPost(Pageable pageable);
    void deletePost(UUID postId, UUID userId) throws Exception;
    Post updatePost(UUID postId, PostRequestDto post, UUID userId) throws Exception;
    PostResponseDto likedPost(User user, UUID postId);
    PostResponseDto dislikedPost(User user, UUID postId);
    void deleteLikePostByPostId(UUID postId);
}
