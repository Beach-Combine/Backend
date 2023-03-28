package beachcombine.backend.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthJoinRequest {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
    @Email
    private String email;
    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$", message="닉네임은 2자 이상 16자 이하, 영어 또는 숫자 또는 한글로 구성되어야 합니다")
    private String nickname;
}
