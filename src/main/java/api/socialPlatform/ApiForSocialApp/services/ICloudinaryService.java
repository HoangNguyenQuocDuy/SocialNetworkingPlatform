package api.socialPlatform.ApiForSocialApp.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ICloudinaryService {

    Map  upload(MultipartFile file);
    List<Map> uploadMultipleFile(MultipartFile[] files) throws Exception;
}
