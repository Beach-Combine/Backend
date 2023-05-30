package beachcombine.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokenResponse {

    private String accessToken;
    private String refreshToken;
    private String role;
    private Boolean tutorialCompleted;
}