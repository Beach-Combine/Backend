package beachcombine.backend.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSaveRequest {

    private String loginId;
    private String password;
    private String email;
    private String nickname;
}
