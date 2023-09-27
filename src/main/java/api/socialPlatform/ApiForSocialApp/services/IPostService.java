package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.model.Post;

import java.util.UUID;

public interface IPostService {
    Post createPost(Post post, UUID userId);
    Post findPostById(UUID postId);
    Post findAllPost(UUID userId);
//    ArrayList<Post> getAllPost()
    Post deletePost(UUID postId);
    Post updatePost(UUID postId, Post post);
}
