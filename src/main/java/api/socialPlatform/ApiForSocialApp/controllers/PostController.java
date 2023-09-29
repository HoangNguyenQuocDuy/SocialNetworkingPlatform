package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.model.Post;
import api.socialPlatform.ApiForSocialApp.model.ResponseObject;
import api.socialPlatform.ApiForSocialApp.services.Impl.PostServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    @Autowired
    private PostServiceImp postServiceImp;
    @PostMapping("/save")
    public ResponseEntity<ResponseObject> submitPost(@RequestBody Object body,
                       @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Create post successfully!", body)
        );
    }

    @DeleteMapping("/delete")
    public Post deleteParticularPost(@RequestBody UUID postId) {
        return new Post();
    }
}
