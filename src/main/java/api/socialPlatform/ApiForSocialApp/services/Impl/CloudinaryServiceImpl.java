package api.socialPlatform.ApiForSocialApp.services.Impl;

import api.socialPlatform.ApiForSocialApp.services.ICloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements ICloudinaryService {

    @Autowired
    private Cloudinary cloudinary;
    @Override
    public Map upload(MultipartFile file) {
        try {
            Map<String, Object> params = ObjectUtils.asMap(
                    "folder", "SocialApp"
            );

            return cloudinary.uploader().upload(file.getBytes(), params);
        } catch (IOException e) {
            throw new RuntimeException("Image loading failed!");
        }
    }

    @Override
    public List<Map> uploadMultipleFile(MultipartFile[] files) throws Exception {
        try {
            List<Map> result = new ArrayList<>();

            for (MultipartFile file: files) {
                Map image = upload(file);
                result.add(image);
            }

            return result;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
