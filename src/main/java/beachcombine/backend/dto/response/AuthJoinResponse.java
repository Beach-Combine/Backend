package beachcombine.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthJoinResponse {

    private Long id;
    private String loginId;
    private String email;
    private String nickname;
}
