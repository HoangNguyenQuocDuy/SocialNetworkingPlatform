package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.repositories.IPostRepo;
import api.socialPlatform.ApiForSocialApp.services.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostServiceImp implements IPostService {
    @Autowired
    private IPostRepo postRepo;
    @Autowired
    private UserServiceImpl userService;

    @Override
    public Post createPost(Post post, UUID userId) {
        return null;
    }

    @Override
    public Post findPostById(UUID postId) {
        return null;
    }

    @Override
    public Post findAllPost(UUID userId) {
        return null;
    }

    @Override
    public Post deletePost(UUID postId) {
        return null;
    }

    @Override
    public Post updatePost(UUID postId, Post post) {
        return null;
    }
}
