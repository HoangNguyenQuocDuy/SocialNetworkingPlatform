package api.socialPlatform.ApiForSocialApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostRequestDto {
    private List<String> postImageUrls;
    private String postDescription;
    private int likes;
}
