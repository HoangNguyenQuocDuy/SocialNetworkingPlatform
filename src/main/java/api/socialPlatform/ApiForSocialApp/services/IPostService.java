package api.socialPlatform.ApiForSocialApp.services;

import api.socialPlatform.ApiForSocialApp.model.Post;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public interface IPostService {
    Post createPost(Post post, UUID userId);
    Optional<Post> getPostById(UUID postId);
    ArrayList<Post> getAllPost(UUID userId);
//    ArrayList<Post> getAllPost()
    Post deletePost(UUID postId);
    Post updatePost(UUID postId, Post post);
}
