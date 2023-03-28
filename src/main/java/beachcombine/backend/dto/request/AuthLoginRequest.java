package beachcombine.backend.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginRequest {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
