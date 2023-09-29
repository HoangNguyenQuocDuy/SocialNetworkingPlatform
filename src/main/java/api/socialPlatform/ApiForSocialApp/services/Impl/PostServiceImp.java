package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.dto.UserDto;
import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IPostRepo;
import api.socialPlatform.ApiForSocialApp.services.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImp implements IPostService {
    @Autowired
    private IPostRepo postRepo;
    @Autowired
    private UserServiceImpl userService;

    @Override
    public Post createPost(Post post, UUID userId) {
        Optional<User> userOptional = userService.findUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            post.setUser(user);
            user.setPosts(post);

            return postRepo.save(post);
        }

        return null;
    }

    @Override
    public Optional<Post> getPostById(UUID postId) {
        return postRepo.findById(postId);
    }

    @Override
    public ArrayList<Post> getAllPost(UUID userId) {
        Optional<User> userOptional = userService.findUserById(userId);
        ArrayList<Post> posts = new ArrayList<>();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            posts = (ArrayList<Post>) user.getPosts().stream().filter(
                    post -> post.getUser().getUserId() == userId
            );
        }
        return posts;
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
