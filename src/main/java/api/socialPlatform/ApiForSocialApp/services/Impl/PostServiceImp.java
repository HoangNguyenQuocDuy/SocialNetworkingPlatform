package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.dto.PostRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.PostResponseDto;
import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IPostRepo;
import api.socialPlatform.ApiForSocialApp.services.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImp implements IPostService {
    @Autowired
    private IPostRepo postRepo;
    @Autowired
    private UserServiceImpl userService;

    @Override
    public Post createPost(PostRequestDto postDto, User user) {
        Post post = Post.builder()
                .postImageUrls(postDto.getPostImageUrls())
                .postDescription(postDto.getPostDescription())
                .user(user)
                .build();

        return postRepo.save(post);
    }

    @Override
    public PostResponseDto getPostById(UUID postId) {
        Optional<Post> postOptional =  postRepo.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return PostResponseDto.fromPost(post);
        }
        return null;
    }

    @Override
    public List<PostResponseDto> getAllPosts(UUID userId) {
        Optional<User> userOptional = userService.findUserById(userId);

        if (userOptional.isPresent()) {
            List<Post> postsFind = postRepo.findByUserUserId(userId);
            return postsFind.stream().map(PostResponseDto::fromPost).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void deletePost(UUID postId,UUID userId) throws Exception {
        Optional<Post> postDelete = postRepo.findById(postId);
        if (postDelete.isPresent() && postDelete.get().getUser().getUserId() == userId) {
            postRepo.deleteById(postId);
        }
        else if (postDelete.get().getUser().getUserId() != userId)
            throw new Exception("User id not authorization!");
        else
            throw new Exception("Post not found!");
    }

    @Override
    public Post updatePost(UUID postId, PostRequestDto post, UUID userId) throws Exception {
        Optional<Post> postFind = postRepo.findById(postId);
        if (postFind.isPresent() && postFind.get().getUser().getUserId() == userId) {
            Post postUpdate = postFind.get();
            postUpdate.setPostDescription(post.getPostDescription());
            postUpdate.setPostImageUrls(post.getPostImageUrls());
            postUpdate.setLikes(post.getLikes());
            postUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));

            return postRepo.save(postUpdate);
        } else if (postFind.get().getUser().getUserId() != userId)
            throw new Exception("User id not authorization!");
        else
            throw new Exception("Post not found!");
    }
}
