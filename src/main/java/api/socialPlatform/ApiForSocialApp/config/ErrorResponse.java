package api.socialPlatform.ApiForSocialApp.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus statusCode;
    private String errorMessage;
    private Object body;

    public ErrorResponse(HttpStatus statusCode, String errorMessage) {
        this(statusCode, errorMessage, errorMessage);
    }

    public int getStatusCodeValue() {
        return statusCode.value();
    }
}
