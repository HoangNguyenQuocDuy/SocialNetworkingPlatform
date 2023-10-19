package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.dto.UserResponseDto;
import api.socialPlatform.ApiForSocialApp.model.ResponseObject;
import api.socialPlatform.ApiForSocialApp.services.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")

public class UserController {
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getUsers() {
        List<UserResponseDto> users = userService.getAllUser();
        if (users.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Find all users successfully!", users)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("FAILED", "List users is empty.", "")
        );
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseObject> getUser(@PathVariable UUID id){
        Optional<UserResponseDto> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get user by ID successfully!", user)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("FAILED", "User not found", "")
        );
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ResponseObject> getUser(@PathVariable String username){
        Optional<UserResponseDto> user = userService.getUserByUserName(username);
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get user by username successfully!", user)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("FAILED", "User not found", "")
        );
    }
}
