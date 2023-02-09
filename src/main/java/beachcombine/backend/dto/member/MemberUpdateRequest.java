package beachcombine.backend.dto.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequest {

    private Long id;
    private String nickname;
    private String image;

}
