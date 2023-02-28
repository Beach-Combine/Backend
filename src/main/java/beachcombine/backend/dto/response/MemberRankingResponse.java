package beachcombine.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRankingResponse {

    private Long id;
    private String nickname;
    private String image;
    private Integer point;
}