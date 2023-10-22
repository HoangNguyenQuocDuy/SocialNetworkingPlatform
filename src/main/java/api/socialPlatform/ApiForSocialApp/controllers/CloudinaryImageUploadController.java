package api.socialPlatform.ApiForSocialApp.controllers;

import api.socialPlatform.ApiForSocialApp.model.ResponseObject;
import api.socialPlatform.ApiForSocialApp.services.Impl.CloudinaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cloudinary")
public class CloudinaryImageUploadController {
    @Autowired
    private CloudinaryServiceImpl cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseObject> uploadImage(@RequestParam("image")MultipartFile file) {
        try {
            Map data = cloudinaryService.upload(file);

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Create comment successfully!",
                            data
            ));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", "Something went wrong", e.getMessage())
            );
        }
    }

    @PostMapping("/uploadMultipleFiles")
    public ResponseEntity<ResponseObject> uploadMultipleImage(@RequestParam("images")MultipartFile[] files) {
        try {
            List<Map> data = cloudinaryService.uploadMultipleFile(files);

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Create comment successfully!",
                            data
                    ));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", "Something went wrong", e.getMessage())
            );
        }
    }
}
