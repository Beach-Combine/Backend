package beachcombine.backend.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthGoogleLoginRequest {

    private String id;
    private String email;
    private String displayName;
    private String photoUrl;
    private String serverAuthCode;
}
