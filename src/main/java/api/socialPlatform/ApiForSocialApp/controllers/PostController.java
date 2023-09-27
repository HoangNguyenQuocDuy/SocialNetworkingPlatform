package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.model.Post;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    @PostMapping("/save")
    public Post submitPost(@RequestBody Post body) {
        return new Post();
    }

    @GetMapping("/getPost")
    public ArrayList<Post> retrieveAllPost() {
        return new ArrayList<Post>();
    }

    @DeleteMapping("/delete")
    public Post deleteParticularPost(@RequestBody UUID postId) {
        return new Post();
    }
}
