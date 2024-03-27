package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.dto.PostRequestDto;
import api.socialPlatform.ApiForSocialApp.dto.PostResponseDto;
import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.model.User;
import api.socialPlatform.ApiForSocialApp.repositories.IPostRepo;
import api.socialPlatform.ApiForSocialApp.repositories.IUserRepo;
import api.socialPlatform.ApiForSocialApp.services.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImp implements IPostService {
    @Autowired
    private IPostRepo postRepo;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private IUserRepo userRepo;
//    @Autowired
//    private IUserLikedPostsRepository userLikedPostsRepository;
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
    public List<PostResponseDto> getAllPostsByUserId(UUID userId) {
        Optional<User> userOptional = userService.findUserById(userId);

        if (userOptional.isPresent()) {
            List<Post> postsFind = postRepo.findByUserUserId(userId);
            Collections.sort(postsFind);
            return postsFind.stream().map(PostResponseDto::fromPost).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<PostResponseDto> getAllPost(Pageable pageable) {
        return postRepo.findAll(pageable).stream().map(PostResponseDto::fromPost).collect(Collectors.toList());
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Exception.class })
    @Override
    public void deletePost(UUID postId,UUID userId) throws Exception {
        Optional<Post> postOptional = postRepo.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            User postUser = post.getUser();

            if (!postUser.getUserId().equals(userId)) {
                throw new Exception("User is not authorized to delete this post!");
            }

            post.getLikeByUsers().forEach(user -> user.getLikedPosts().remove(post));

            postRepo.delete(post);
        } else {
            throw new Exception("Post not found!");
        }
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

    @Override
    public PostResponseDto likedPost(User user, UUID postId) {
        Optional<Post> postOptional = postRepo.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            PostResponseDto postResponse = PostResponseDto.fromPost(post);

            Set<User> likeByUsers = post.getLikeByUsers();
            Set<Post> likePosts = user.getLikedPosts();

            if (post.getLikeByUsers().contains(user)) {
                likeByUsers.remove(user);
                likePosts.remove(post);
            } else {
                likeByUsers.add(user);
                likePosts.add(post);
            }
            post.setLikes(likeByUsers.size());
            post.setLikeByUsers(likeByUsers);

            user.setLikedPosts(likePosts);
            userRepo.save(user);
            postRepo.save(post);

            postResponse.setLikes(post.getLikeByUsers().stream().toList().size());

            return postResponse;
        }
        return null;
    }

    @Override
    public PostResponseDto dislikedPost(User user, UUID postId) {
        Optional<Post> postOptional = postRepo.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            post.dislike(user);
            PostResponseDto postResponse = PostResponseDto.fromPost(post);
            postRepo.save(post);
            postResponse.setLikes(post.getLikeByUsers().stream().toList().size());

            return postResponse;
        }
        return null;
    }

    @Override
    public void deleteLikePostByPostId(UUID postId) {

    }
}
