package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.dto.PostRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.PostResponseDto;
import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPostService {
    Post createPost(PostRequestDto post, User user);
    PostResponseDto getPostById(UUID postId);
    List<PostResponseDto> getAllPosts(UUID userId);
    void deletePost(UUID postId, UUID userId) throws Exception;
    Post updatePost(UUID postId, PostRequestDto post, UUID userId) throws Exception;
}
