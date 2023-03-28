package beachcombine.backend.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthGoogleLoginRequest {

    @NotEmpty
    private String id;
    @Email
    private String email;
    @NotEmpty
    private String displayName;
    private String photoUrl;
    private String serverAuthCode;
}
