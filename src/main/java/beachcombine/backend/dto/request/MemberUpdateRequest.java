package beachcombine.backend.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequest {

    private String nickname;
    private String image;

}